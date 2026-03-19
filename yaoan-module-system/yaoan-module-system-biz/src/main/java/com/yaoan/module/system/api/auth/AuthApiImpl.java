package com.yaoan.module.system.api.auth;

import com.yaoan.framework.common.enums.UserTypeEnum;
import com.yaoan.framework.common.util.monitor.TracerUtils;
import com.yaoan.framework.common.util.servlet.ServletUtils;
import com.yaoan.module.system.api.auth.dto.AuthLoginRespDTO;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.logger.dto.LoginLogCreateReqDTO;
import com.yaoan.module.system.api.sms.SmsCodeApi;
import com.yaoan.module.system.convert.auth.AuthConvert;
import com.yaoan.module.system.dal.dataobject.oauth2.OAuth2AccessTokenDO;
import com.yaoan.module.system.dal.mysql.user.AdminUserMapper;
import com.yaoan.module.system.enums.logger.LoginLogTypeEnum;
import com.yaoan.module.system.enums.logger.LoginResultEnum;
import com.yaoan.module.system.enums.oauth2.OAuth2ClientConstants;
import com.yaoan.module.system.service.invitecode.InviteCodeService;
import com.yaoan.module.system.service.logger.LoginLogService;
import com.yaoan.module.system.service.member.MemberService;
import com.yaoan.module.system.service.oauth2.OAuth2TokenService;
import com.yaoan.module.system.service.social.SocialUserService;
import com.yaoan.module.system.service.user.AdminUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-30 21:07
 */
@Service
public class AuthApiImpl implements AuthApi {
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


    @Override
    public AuthLoginRespDTO createTokenAfterLoginSuccess(Long userId, String username, LoginLogTypeEnum logType, int firstLogin, Long companyId) {

        // 插入登陆日志
        createLoginLog(userId, username, logType, LoginResultEnum.SUCCESS);
        // 创建访问令牌
        OAuth2AccessTokenDO accessTokenDO = oauth2TokenService.createAccessToken(userId, getUserType().getValue(),
                OAuth2ClientConstants.CLIENT_ID_DEFAULT, companyId, null);
        // 构建返回结果
        AuthLoginRespDTO resVo = AuthConvert.INSTANCE.convertDo2Dto(accessTokenDO);
        resVo.setFirstLogin(firstLogin);
        return resVo;
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

    private UserTypeEnum getUserType() {
        return UserTypeEnum.ADMIN;
    }

}
