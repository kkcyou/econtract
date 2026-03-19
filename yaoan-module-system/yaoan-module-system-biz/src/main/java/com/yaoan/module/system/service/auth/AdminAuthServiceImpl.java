package com.yaoan.module.system.service.auth;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.annotations.VisibleForTesting;
import com.xingyuv.captcha.model.common.ResponseModel;
import com.xingyuv.captcha.model.vo.CaptchaVO;
import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.framework.common.enums.UserTypeEnum;
import com.yaoan.framework.common.exception.util.ServiceExceptionUtil;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.util.monitor.TracerUtils;
import com.yaoan.framework.common.util.servlet.ServletUtils;
import com.yaoan.framework.common.util.validation.ValidationUtils;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.tenant.core.context.TenantContextHolder;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.feign.AuthCenterClient;
import com.yaoan.module.system.api.feign.IShuCaiApi;
import com.yaoan.module.system.api.logger.dto.LoginLogCreateReqDTO;
import com.yaoan.module.system.api.permission.dto.RoleRespDTO;
import com.yaoan.module.system.api.sms.SmsCodeApi;
import com.yaoan.module.system.api.sms.dto.code.SmsCodeUseReqDTO;
import com.yaoan.module.system.api.social.dto.SocialUserBindReqDTO;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.config.AuthCenterProperties;
import com.yaoan.module.system.controller.admin.auth.shucai.vo.AuthCodeRespVO;
import com.yaoan.module.system.controller.admin.auth.shucai.vo.AuthTokenCodeReqVO;
import com.yaoan.module.system.controller.admin.auth.shucai.vo.UserInfoRespVo;
import com.yaoan.module.system.controller.admin.auth.vo.*;
import com.yaoan.module.system.convert.auth.AuthConvert;
import com.yaoan.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.dal.mysql.user.AdminUserMapper;
import com.yaoan.module.system.enums.ErrorCodeConstants;
import com.yaoan.module.system.enums.logger.LoginLogTypeEnum;
import com.yaoan.module.system.enums.logger.LoginResultEnum;
import com.yaoan.module.system.enums.oauth2.OAuth2ClientConstants;
import com.yaoan.module.system.enums.oauth2.OAuth2GrantTypeEnum;
import com.yaoan.module.system.enums.permission.RoleCodeEnum;
import com.yaoan.module.system.enums.sms.SmsSceneEnum;
import com.yaoan.module.system.service.invitecode.InviteCodeService;
import com.yaoan.module.system.service.logger.LoginLogService;
import com.yaoan.module.system.service.member.MemberService;
import com.yaoan.module.system.service.oauth2.OAuth2TokenService;
import com.yaoan.module.system.service.permission.RoleService;
import com.yaoan.module.system.service.social.SocialUserService;
import com.yaoan.module.system.service.user.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.enums.GlobalErrorCodeConstants.TOKEN_ERROR;
import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.common.util.servlet.ServletUtils.getClientIP;
import static com.yaoan.module.system.enums.ErrorCodeConstants.*;
import static com.yaoan.module.system.enums.auth.AuthConstants.FIRST_LOGIN;
import static com.yaoan.module.system.enums.auth.AuthConstants.NOT_FIRST_LOGIN;

/**
 * Auth Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Slf4j
public class AdminAuthServiceImpl implements AdminAuthService {

    @Resource
    private AdminUserService userService;
    @Resource
    private AdminUserMapper userMapper;
    @Resource
    private LoginLogService loginLogService;
    @Resource
    private OAuth2TokenService oauth2TokenService;
    @Resource
    private SocialUserService socialUserService;
    @Resource
    private MemberService memberService;
    @Resource
    private SmsCodeApi smsCodeApi;
    @Resource 
    private SystemConfigApi systemConfigApi;

    @Resource
    private InviteCodeService inviteCodeService;
    /**
     * 验证码的开关，默认为 true
     */
    @Value("${yudao.captcha.enable:true}")
    private Boolean captchaEnable;

    /**
     * 邀请码的开关，默认为 true
     */
    @Value("${yudao.invitecode.enable:true}")
    private Boolean inviteCodeEnable;
    /**
     * 用户初始密码
     */
    @Value("${user-config.init_user_password:123456789}")
    private String initUserPassword;

    @Resource
    private IShuCaiApi shuCaiApi;
    @Resource
    private AuthCenterClient authCenterClient;
    @Resource
    private AuthCenterProperties authCenterProperties;
    
    /**
     * 验证用户和密码（校验首次登录）
     */
    @Override
    public LoginRespVO authenticate2(String username, String password) {
        final LoginLogTypeEnum logTypeEnum = LoginLogTypeEnum.LOGIN_USERNAME;
        // 校验账号是否存在
//        AdminUserDO user = userService.getUserByUsername(username);
        AdminUserDO user = userService.getUserByUsernameOrMobile(username);


        if (user == null) {
            createLoginLog(null, username, logTypeEnum, LoginResultEnum.BAD_CREDENTIALS);
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        String cvalue  = systemConfigApi.getConfigByKey("is_platuser_login");
        Boolean bool = false;
        if(StringUtils.isEmpty(cvalue) || ("n").equals(cvalue)) {
            bool = true;
        }
        if (bool && StringUtils.isNotBlank(user.getPlatformUserId())) {
            createLoginLog(null, username, logTypeEnum, LoginResultEnum.USER_DISABLED);
            throw exception(AUTH_LOGIN_PLATFORM_DISABLED);
        }
        //校验用户名和密码
        if (!userService.isPasswordMatch(password, user.getPassword())) {
            createLoginLog(user.getId(), username, logTypeEnum, LoginResultEnum.BAD_CREDENTIALS);
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        // 校验是否禁用
        if (ObjectUtil.notEqual(user.getStatus(), CommonStatusEnum.ENABLE.getStatus())) {
            createLoginLog(user.getId(), username, logTypeEnum, LoginResultEnum.USER_DISABLED);
            throw exception(AUTH_LOGIN_USER_DISABLED);
        }
        //校验是否是第一次登录,修改备注
        LoginRespVO vo = AuthConvert.INSTANCE.convert(user);
        vo.setFirstLogin(NOT_FIRST_LOGIN);
        vo.setTenantId(user.getTenantId());
        TenantContextHolder.setTenantId(user.getTenantId());
        //校验是否是首次需要激活，
        // 等于初始密码，就是首次登录
        // 不等于初始密码，就是再次登录
        if (StringUtils.equals(password, initUserPassword)) {
            vo.setFirstLogin(FIRST_LOGIN);
        }

        return vo;
    }

    /**
     * 验证用户和密码
     */
    @Override
    public AdminUserDO authenticate(String username, String password) {
        final LoginLogTypeEnum logTypeEnum = LoginLogTypeEnum.LOGIN_USERNAME;
        // 校验账号是否存在
        AdminUserDO user = userService.getUserByUsername(username);
        //校验是否是第一次登录,修改备注
        if (ObjectUtil.isNull(user.getLoginDate())) {
            throw exception(ErrorCodeConstants.AUTH_FIRST_LOGIN);
        }
        if (user == null) {
            createLoginLog(null, username, logTypeEnum, LoginResultEnum.BAD_CREDENTIALS);
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        if (StringUtils.isNotBlank(user.getPlatformUserId())) {
            createLoginLog(null, username, logTypeEnum, LoginResultEnum.USER_DISABLED);
            throw exception(AUTH_LOGIN_PLATFORM_DISABLED);
        }
        
        if (!userService.isPasswordMatch(password, user.getPassword())) {
            createLoginLog(user.getId(), username, logTypeEnum, LoginResultEnum.BAD_CREDENTIALS);
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        // 校验是否禁用
        if (ObjectUtil.notEqual(user.getStatus(), CommonStatusEnum.ENABLE.getStatus())) {
            createLoginLog(user.getId(), username, logTypeEnum, LoginResultEnum.USER_DISABLED);
            throw exception(AUTH_LOGIN_USER_DISABLED);
        }
        return user;
    }

    @Override
    public AuthLoginRespVO login(AuthLoginReqVO reqVO) {
//        // 校验验证码
//        validateCaptcha(reqVO);

        // 使用账号密码，进行登录
        AdminUserDO user = authenticate(reqVO.getUsername(), reqVO.getPassword());

        // 如果 socialType 非空，说明需要绑定社交用户
        if (reqVO.getSocialType() != null) {
            socialUserService.bindSocialUser(new SocialUserBindReqDTO(user.getId(), getUserType().getValue(),
                    reqVO.getSocialType(), reqVO.getSocialCode(), reqVO.getSocialState()));
        }
        // 创建 Token 令牌，记录登录日志
        return createTokenAfterLoginSuccess(user.getId(), reqVO.getUsername(), LoginLogTypeEnum.LOGIN_USERNAME, user.getCompanyId());
    }


    @Override
    public AuthLoginRespVO loginYaoan(AuthLoginSimpleReqVO reqVO) {
        // 使用账号密码，进行登录
        LoginRespVO vo = authenticate2(reqVO.getUsername(), reqVO.getPassword());
       // LoginRespVO vo = authenticate2(reqVO.getUsername(), reqVO.getPassword());

        // 判断是否配置了邀请码校验
        if (inviteCodeEnable) {
            inviteCodeService.validInviteCode(reqVO.getInviteCode(), vo.getId());
        }

        // 创建 Token 令牌，记录登录日志
        AuthLoginRespVO tokenAfterLoginSuccess = createTokenAfterLoginSuccess(vo.getId(), reqVO.getUsername(), LoginLogTypeEnum.LOGIN_USERNAME, vo.getFirstLogin(), vo.getCompanyId());
        return tokenAfterLoginSuccess.setTenantId(vo.getTenantId());
    }

    @Override
    public AuthLoginRespVO loginAppOpenId(String openId) {
        // 获得用户信息
        AtomicReference<AdminUserDO> atomic = new AtomicReference<>();
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                AdminUserDO userByAppOpenId = userMapper.selectByAppOpenId(openId);
                atomic.set(userByAppOpenId);
            });
        });
        if (ObjectUtil.isEmpty(atomic.get())){
            throw exception(USER_NOT_EXISTS);

        }
        AdminUserDO user = atomic.get();

        // 创建 Token 令牌，记录登录日志
        TenantContextHolder.setTenantId(user.getTenantId());
        AuthLoginRespVO tokenAfterLoginSuccess = createTokenAfterLoginSuccess(user.getId(), user.getUsername(), LoginLogTypeEnum.LOGIN_APP_OPEN_ID, user.getCompanyId());
        return tokenAfterLoginSuccess.setTenantId(user.getTenantId());
    }


    @Override
    public void sendSmsCode(AuthSmsSendReqVO reqVO) {
        // 登录场景，验证是否存在
        if (userService.getUserByMobile(reqVO.getMobile()) == null) {
            throw exception(AUTH_MOBILE_NOT_EXISTS);
        }
        // 发送验证码
        smsCodeApi.sendSmsCode(AuthConvert.INSTANCE.convert(reqVO).setCreateIp(getClientIP()));
    }

    @Override
    public Boolean useSms(AuthSmsLoginReqVO reqVO) {
        smsCodeApi.useSmsCode(new SmsCodeUseReqDTO().setCode(reqVO.getCode()).setMobile(reqVO.getMobile()).setUsedIp(getClientIP()));
        return true;
    }

    @Override
    public AuthLoginRespVO smsLogin(AuthSmsLoginReqVO reqVO) {
        // 校验验证码
        smsCodeApi.useSmsCode(AuthConvert.INSTANCE.convert(reqVO, SmsSceneEnum.ADMIN_MEMBER_LOGIN.getScene(), getClientIP()));

        // 获得用户信息
        AdminUserDO user = userService.getUserByMobile(reqVO.getMobile());
        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }

        // 创建 Token 令牌，记录登录日志
        return createTokenAfterLoginSuccess(user.getId(), reqVO.getMobile(), LoginLogTypeEnum.LOGIN_MOBILE,user.getCompanyId());
    }


    @Override
    public Boolean useSmsCode(AuthSmsLoginReqVO reqVO) {
        // 校验验证码
        try {
            smsCodeApi.useSmsCode(AuthConvert.INSTANCE.convert(reqVO, SmsSceneEnum.ADMIN_MEMBER_LOGIN.getScene(), getClientIP()));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void createLoginLog(Long userId, String username,
                                LoginLogTypeEnum logTypeEnum, LoginResultEnum loginResult) {
        // 插入登录日志
        LoginLogCreateReqDTO reqDTO = new LoginLogCreateReqDTO();
        reqDTO.setLogType(logTypeEnum.getType());
        reqDTO.setTraceId(TracerUtils.getTraceId());
        reqDTO.setUserId(userId);
        reqDTO.setUserType(getUserType().getValue());
        reqDTO.setUsername(username);
        reqDTO.setUserAgent(ServletUtils.getUserAgent());
        reqDTO.setUserIp(ServletUtils.getClientIP());
        reqDTO.setResult(loginResult.getResult());
        loginLogService.createLoginLog(reqDTO);
        // 更新最后登录时间
        if (userId != null && Objects.equals(LoginResultEnum.SUCCESS.getResult(), loginResult.getResult())) {
            userService.updateUserLogin(userId, ServletUtils.getClientIP());
        }
    }

    @Override
    public AuthLoginRespVO socialLogin(AuthSocialLoginReqVO reqVO) {
        // 使用 code 授权码，进行登录。然后，获得到绑定的用户编号
        Long userId = socialUserService.getBindUserId(UserTypeEnum.ADMIN.getValue(), reqVO.getType(),
                reqVO.getCode(), reqVO.getState());
        if (userId == null) {
            throw exception(AUTH_THIRD_LOGIN_NOT_BIND);
        }

        // 获得用户
        AdminUserDO user = userService.getUser(userId);
        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }

        // 创建 Token 令牌，记录登录日志
        return createTokenAfterLoginSuccess(user.getId(), user.getUsername(), LoginLogTypeEnum.LOGIN_SOCIAL,user.getCompanyId());
    }

    private AuthLoginRespVO createTokenAfterLoginSuccess(Long userId, String username, LoginLogTypeEnum logType, Long companyId) {
        // 插入登陆日志
        createLoginLog(userId, username, logType, LoginResultEnum.SUCCESS);
        // 创建访问令牌
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.createAccessToken(userId, getUserType().getValue(),
                OAuth2ClientConstants.CLIENT_ID_DEFAULT, companyId, null);
        // 构建返回结果
        return AuthConvert.INSTANCE.convert(accessTokenDO);
    }

    private AuthLoginRespVO createTokenAfterLoginSuccess(Long userId, String username, LoginLogTypeEnum logType, int firstLogin, Long companyId) {

        // 插入登陆日志
        createLoginLog(userId, username, logType, LoginResultEnum.SUCCESS);
        // 创建访问令牌
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.createAccessToken(userId, getUserType().getValue(),
                OAuth2ClientConstants.CLIENT_ID_DEFAULT, companyId, null);
        // 构建返回结果
        AuthLoginRespVO resVo = AuthConvert.INSTANCE.convert(accessTokenDO);
        resVo.setFirstLogin(firstLogin);
        return resVo;
    }

    @Override
    public AuthLoginRespVO refreshToken(String refreshToken) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.refreshAccessToken(refreshToken, OAuth2ClientConstants.CLIENT_ID_DEFAULT);
        return AuthConvert.INSTANCE.convert(accessTokenDO);
    }

    @Override
    public AuthLoginRespVO refreshExtendToken(String refreshToken) {
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.refreshExtendAccessToken(refreshToken);

        return AuthConvert.INSTANCE.convert(accessTokenDO);
    }

    @Override
    public AuthTokenRespVO authToken(AuthTokenSimpleReqVO reqVO) {

        // 使用账号密码，进行登录
        AdminUserDO user = authenticate(reqVO.getUsername(), reqVO.getPassword());

        // 创建 Token 令牌，记录登录日志
        return createToken(user.getId(), user.getCompanyId());
    }

    private AuthTokenRespVO createToken(Long userId, Long companyId) {

        // 创建访问令牌
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.createAccessToken(userId, getUserType().getValue(),
                OAuth2ClientConstants.CLIENT_ID_DEFAULT, companyId, null);
        // 构建返回结果
        return AuthConvert.INSTANCE.convert2Token(accessTokenDO);
    }

    @Override
    public void logout(String token, Integer logType) {
        // 删除访问令牌
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.removeAccessToken(token);
        if (accessTokenDO == null) {
            return;
        }
        // 删除成功，则记录登出日志
        createLogoutLog(accessTokenDO.getUserId(), accessTokenDO.getUserType(), logType);
    }

    private void createLogoutLog(Long userId, Integer userType, Integer logType) {
        LoginLogCreateReqDTO reqDTO = new LoginLogCreateReqDTO();
        reqDTO.setLogType(logType);
        reqDTO.setTraceId(TracerUtils.getTraceId());
        reqDTO.setUserId(userId);
        reqDTO.setUserType(userType);
        if (ObjectUtil.equal(getUserType().getValue(), userType)) {
            reqDTO.setUsername(getUsername(userId));
        } else {
            reqDTO.setUsername(memberService.getMemberUserMobile(userId));
        }
        reqDTO.setUserAgent(ServletUtils.getUserAgent());
        reqDTO.setUserIp(ServletUtils.getClientIP());
        reqDTO.setResult(LoginResultEnum.SUCCESS.getResult());
        loginLogService.createLoginLog(reqDTO);
    }

    private String getUsername(Long userId) {
        if (userId == null) {
            return null;
        }
        AdminUserDO user = userService.getUser(userId);
        return user != null ? user.getUsername() : null;
    }

    private UserTypeEnum getUserType() {
        return UserTypeEnum.ADMIN;
    }
    @Override
    public AuthCodeRespVO authCodeToken(AuthTokenCodeReqVO reqVO) throws Exception {

        String oauthTokenStr = null;
        try {
            oauthTokenStr = authCenterClient.oauthCenterToken(OAuth2GrantTypeEnum.AUTHORIZATION_CODE.getGrantType(), reqVO.getCode(), authCenterProperties.getClientId(), authCenterProperties.getSecretId(), authCenterProperties.getUsername(), authCenterProperties.getPassword(), reqVO.getRedirectUri());
        } catch (Exception e) {
            throw exception(TOKEN_ERROR);
        }
        JSONObject jsonObject = JSONObject.parseObject(oauthTokenStr);
        if (jsonObject.get("error") != null) {
            throw new Exception(jsonObject.getString("error_description"));
        }
        return new AuthCodeRespVO().setAccess_token(jsonObject.getString("access_token"))
                .setRefresh_token(jsonObject.getString("refresh_token"))
                .setExpires_in(jsonObject.getString("expires_in"))
                .setScope(jsonObject.getString("scope"))
                .setToken_type(jsonObject.getString("token_type"))
                .setJti(jsonObject.getString("jti"));
    }

//    @VisibleForTesting
//    void validateCaptcha(AuthRegisterReqVO reqVO) {
//        // 如果验证码关闭，则不进行校验
//        if (!captchaEnable) {
//            return;
//        }
//        // 校验验证码
//        ValidationUtils.validate(validator, reqVO, AuthLoginReqVO.CodeEnableGroup.class);
//        CaptchaVO captchaVO = new CaptchaVO();
//        captchaVO.setCaptchaVerification(reqVO.getCaptchaVerification());
//        ResponseModel response = captchaService.verification(captchaVO);
//        // 验证不通过
//        if (!response.isSuccess()) {
//            throw ServiceExceptionUtil.exception(ErrorCodeConstants.AUTH_REGISTER_CAPTCHA_CODE_ERROR, response.getRepMsg());
//        }
//    }
    public UserInfoRespVo getUserInfo(HttpServletRequest request) {
        String userInfoStr = shuCaiApi.userInfo(request.getHeader("access_token"));
        JSONObject jsonObject = JSONObject.parseObject(userInfoStr);
        JSONObject result = jsonObject.getJSONObject("data");
        return JSONObject.parseObject(result.toJSONString(), UserInfoRespVo.class);
    }

    @Override
    public AuthLoginRespVO loginMock(AuthTokenCodeReqVO reqVO) throws Exception {
        String oauthTokenStr = null;
        try {
            oauthTokenStr = shuCaiApi.oauthToken(OAuth2GrantTypeEnum.AUTHORIZATION_CODE.getGrantType(), authCenterProperties.getClientId(), authCenterProperties.getSecretId(), reqVO.getCode(), reqVO.getRedirectUri());
        } catch (Exception e) {
            throw  exception(TOKEN_ERROR);
        }
        JSONObject jsonObject = JSONObject.parseObject(oauthTokenStr);
        if (jsonObject.get("error") != null) {
            throw new Exception(jsonObject.getString("error_description"));
        }
        String userInfoStr  = shuCaiApi.userInfo(jsonObject.getString("access_token"));
        JSONObject userjsonObject = JSONObject.parseObject(userInfoStr);
        JSONObject userInfoJson = userjsonObject.getJSONObject("data");
        UserInfoRespVo userInfoRespVo = JSONObject.parseObject(userInfoJson.toJSONString(), UserInfoRespVo.class);
        AdminUserRespDTO adminUserRespDTO = userService.getUserByPlatformUserId(userInfoRespVo.getUserId());
        
        return new AuthLoginRespVO().setAccessToken(jsonObject.getString("access_token"))
                .setRefreshToken(jsonObject.getString("refresh_token"))
                .setExpiresTime(DateUtil.toLocalDateTime(DateUtil.offsetSecond(DateUtil.date(), (int)Double.parseDouble(jsonObject.getString("expires_in")))))
                .setUserId(adminUserRespDTO.getId()).setTenantId(adminUserRespDTO.getTenantId()).setAdminUserRespDTO(adminUserRespDTO);
    }

}
