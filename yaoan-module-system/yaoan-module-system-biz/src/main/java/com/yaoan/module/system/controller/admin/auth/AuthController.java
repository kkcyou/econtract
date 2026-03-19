package com.yaoan.module.system.controller.admin.auth;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.framework.security.config.SecurityProperties;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.tenant.core.aop.TenantIgnore;
import com.yaoan.module.system.controller.admin.auth.vo.*;
import com.yaoan.module.system.convert.auth.AuthConvert;
import com.yaoan.module.system.dal.dataobject.permission.MenuDO;
import com.yaoan.module.system.dal.dataobject.permission.RoleDO;
import com.yaoan.module.system.dal.dataobject.permission.UserRoleDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.dal.mysql.permission.UserRoleMapper;
import com.yaoan.module.system.enums.logger.LoginLogTypeEnum;
import com.yaoan.module.system.service.auth.AdminAuthService;
import com.yaoan.module.system.service.auth.AppAuthService;
import com.yaoan.module.system.service.permission.MenuService;
import com.yaoan.module.system.service.permission.PermissionService;
import com.yaoan.module.system.service.permission.RoleService;
import com.yaoan.module.system.service.social.SocialUserService;
import com.yaoan.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.enums.GlobalErrorCodeConstants.UNAUTHORIZED;
import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.framework.common.util.collection.CollectionUtils.convertSet;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.obtainAuthorization;
import static com.yaoan.module.system.enums.auth.AuthConstants.VERIFICATION_CODE;

/**
 * 管理后台 - 认证
 *
 * @author Pele
 */
@Tag(name = "管理后台 - 认证", description = "管理后台 - 认证")
@RestController
@RequestMapping("/system/auth")
@Validated
@Slf4j
public class AuthController {

    @Resource
    private AdminAuthService authService;
    @Resource
    private AppAuthService appAuthService;
    @Resource
    private AdminUserService userService;
    @Resource
    private RoleService roleService;
    @Resource
    private MenuService menuService;
    @Resource
    private PermissionService permissionService;
    @Resource
    private SocialUserService socialUserService;
    @Resource
    private SecurityProperties securityProperties;

    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private UserRoleMapper userRoleMapper;

//    @PostMapping("/login")
//    @PermitAll
//    @Operation(summary = "(Yudao)使用账号密码登录")
//    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
//    public CommonResult<AuthLoginRespVO> login(@RequestBody @Valid AuthLoginReqVO reqVO) {
//        return success(authService.login(reqVO));
//    }

    /**
     * 使用账号密码登录
     */
    @PostMapping("/login")
    @PermitAll
    @Operation(summary = "使用账号密码登录电子合同签约平台")
    @OperateLog(enable = false)
    public CommonResult<Object> login(@RequestBody @Valid AuthLoginSimpleReqVO reqVO) {
        return success(authService.loginYaoan(reqVO));
    }

    /**
     * 使用账号密码获取token
     */
    @PostMapping("/token")
    @PermitAll
    @Operation(summary = "使用账号密码获取token")
    @OperateLog(enable = false)
    public CommonResult<Object> login(@RequestBody @Valid AuthTokenSimpleReqVO reqVO) {
        return success(authService.authToken(reqVO));
    }

    /**
     * 获得验证码（六位），验证码有效时间时间5分钟
     */
    @GetMapping("/veriCode")
    @PermitAll
    @Operation(summary = "获得验证码（六位）")
    @OperateLog(enable = false)
    public String veriCode() {
        String code = IdUtil.simpleUUID().substring(0, 6).toUpperCase();
        redisTemplate.opsForValue().set("veriCode", code, 300, TimeUnit.SECONDS);
        String result = (String) redisTemplate.opsForValue().get(VERIFICATION_CODE);
        log.info("得到验证码：" + result);
        return code;
    }

    @PutMapping("activeUser")
    @Operation(summary = "激活账号")
//    @PreAuthorize("@ss.ion('system:user:activeUser')")
    @PermitAll
    public CommonResult<Boolean> activeUser(@Valid @RequestBody AuthActiveUserReqVO reqVO) {
        userService.activeUser(reqVO);
        return success(true);
    }

    @PostMapping("/logout")
    @PermitAll
    @Operation(summary = "登出系统")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<Boolean> logout(HttpServletRequest request) {
        String token = obtainAuthorization(request, securityProperties.getTokenHeader());
        if (StrUtil.isNotBlank(token)) {
            authService.logout(token, LoginLogTypeEnum.LOGOUT_SELF.getType());
        }
        return success(true);
    }

    @PostMapping("/refresh-token")
    @PermitAll
    @Operation(summary = "刷新令牌")
    @Parameter(name = "refreshToken", description = "刷新令牌", required = true)
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<AuthLoginRespVO> refreshToken(@RequestParam("refreshToken") String refreshToken) {
        return success(authService.refreshToken(refreshToken));
    }

    @PostMapping("/extend/refresh-token")
    @PermitAll
    @Operation(summary = "刷新令牌")
    @Parameter(name = "refreshToken", description = "刷新令牌", required = true)
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<AuthLoginRespVO> refreshExtendToken(@RequestParam("refreshToken") String refreshToken) {
        return success(authService.refreshExtendToken(refreshToken));
    }

    @GetMapping("/get-permission-info")
    @Operation(summary = "获取登录用户的权限信息")
    @DataPermission(enable = false)
    public CommonResult<AuthPermissionInfoRespVO> getPermissionInfo() {
        // 1.1 获得用户信息
        AdminUserDO user = userService.getUser(getLoginUserId());
        if (user == null) {
            throw exception(UNAUTHORIZED);
        }
        //1.从redis中获取企业的ID
        String acheKey = SecurityFrameworkUtils.getLoginUserKey4Space();
        //获取到相对方ID
        String relativeId = redisUtils.get(acheKey);
        Set<Long> roleIds=new HashSet<>();
        // 1.2 获得角色列表
        if(StrUtil.isNotBlank(relativeId)){
            //供应商用户
            List<UserRoleDO> roles = userRoleMapper.selectListByRelativeIdNUserId(relativeId,getLoginUserId());
            if(CollectionUtil.isEmpty(roles)){
                return null;
            }
             roleIds = roles.stream().filter(role->ObjectUtil.isNotEmpty(role.getRoleId())).map(UserRoleDO::getRoleId).collect(Collectors.toSet());
        }else {
            //采购人用户
             roleIds = permissionService.getUserRoleIdListByUserId(getLoginUserId());
        }
        List<RoleDO> roles = roleService.getRoleList(roleIds);
        roles.removeIf(role -> !CommonStatusEnum.ENABLE.getStatus().equals(role.getStatus())); // 移除禁用的角色

        // 1.3 获得菜单列表
        Set<Long> menuIds = permissionService.getRoleMenuListByRoleId(convertSet(roles, RoleDO::getId));
        List<MenuDO> menuList = menuService.getMenuList(menuIds);
        menuList.removeIf(menu -> !CommonStatusEnum.ENABLE.getStatus().equals(menu.getStatus())); // 移除禁用的菜单

        // 2. 拼接结果返回
        return success(AuthConvert.INSTANCE.convert(user, roles, menuList));
    }

    // ========== 短信登录相关 ==========

    @PostMapping("/sms-login")
    @PermitAll
    @Operation(summary = "使用短信验证码登录")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<AuthLoginRespVO> smsLogin(@RequestBody @Valid AuthSmsLoginReqVO reqVO) {
        return success(authService.smsLogin(reqVO));
    }

    /**
     * 发送 手机验证码
     */
    @PostMapping("/send-sms-code")
    @PermitAll
    @Operation(summary = "发送手机验证码")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<Boolean> sendLoginSmsCode(@RequestBody @Valid AuthSmsSendReqVO reqVO) {
        authService.sendSmsCode(reqVO);
        return success(true);
    }
    /**
     * 发送手机验证码
     */
    @PostMapping("/use-sms-code")
    @PermitAll
    @Operation(summary = "校验手机验证码")
//    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<Boolean> useLoginSmsCode(@RequestBody @Valid AuthSmsLoginReqVO reqVO) {
        return success(authService.useSmsCode(reqVO));
    }
    @PostMapping("/use-sms-code2")
    @PermitAll
    @Operation(summary = "发送手机验证码2")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<Boolean> sendLoginSmsCode2(@RequestBody @Valid AuthSmsLoginReqVO reqVO) {

        return success(authService.useSmsCode(reqVO));
    }
    // ========== 社交登录相关 ==========

    @GetMapping("/social-auth-redirect")
    @PermitAll
    @Operation(summary = "社交授权的跳转")
    @Parameters({
            @Parameter(name = "type", description = "社交类型", required = true),
            @Parameter(name = "redirectUri", description = "回调路径")
    })
    public CommonResult<String> socialLogin(@RequestParam("type") Integer type,
                                            @RequestParam("redirectUri") String redirectUri) {
        return CommonResult.success(socialUserService.getAuthorizeUrl(type, redirectUri));
    }

    @PostMapping("/social-login")
    @PermitAll
    @Operation(summary = "社交快捷登录，使用 code 授权码", description = "适合未登录的用户，但是社交账号已绑定用户")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<AuthLoginRespVO> socialQuickLogin(@RequestBody @Valid AuthSocialLoginReqVO reqVO) {
        return success(authService.socialLogin(reqVO));
    }

    @PostMapping("/app_login")
    @PermitAll
    @Operation(summary = "app用户登录")
    @OperateLog(enable = false)
    public CommonResult<CommonResult> getUserInfoByApp(@RequestBody @Valid AuthAppClientReqVO authAppClientRespVO) {
        CommonResult userInfoByApp = appAuthService.getUserInfoByApp(authAppClientRespVO);
        return success(userInfoByApp);
    }


    @GetMapping("/check-auth")
    @Operation(summary = "文件访问前的鉴权操作")
    @PermitAll
    @OperateLog(enable = false)
    public ResponseEntity<CommonResult<Boolean>> checkAuth(HttpServletRequest request) {
       return appAuthService.checkFileAuth(request);
    }
}
