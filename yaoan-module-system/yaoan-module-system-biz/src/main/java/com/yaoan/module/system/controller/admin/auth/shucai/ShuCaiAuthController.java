package com.yaoan.module.system.controller.admin.auth.shucai;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.framework.tenant.core.aop.TenantIgnore;
import com.yaoan.module.system.controller.admin.auth.shucai.vo.AuthTokenCodeReqVO;
import com.yaoan.module.system.controller.admin.auth.shucai.vo.UserInfoRespVo;
import com.yaoan.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import com.yaoan.module.system.service.auth.AdminAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.obtainAuthorization;

/**
 * 管理后台 - 数采认证
 *
 * @author doujl
 */
@Tag(name = "管理后台 - 数采认证", description = "管理后台 - 数采认证")
@RestController
@RequestMapping("/system/auth/prevention")
@Validated
@Slf4j
public class ShuCaiAuthController {

    @Resource
    private AdminAuthService authService;

    /**
     * 使用授权码获取token
     */
    @PostMapping("/token/code")
    @PermitAll
    @Operation(summary = "使用授权码获取token")
    public CommonResult<Object> token(@RequestBody @Valid AuthTokenCodeReqVO reqVO) throws Exception {
        return success(authService.authCodeToken(reqVO));
    }

    @PermitAll
    @TenantIgnore
    @PostMapping("/login")
    @Operation(summary = "登录接口")
    public CommonResult<AuthLoginRespVO> login(@RequestBody @Valid AuthTokenCodeReqVO reqVO) throws Exception {
        return success(authService.loginMock(reqVO));
    }
    /**
     * 获取用户信息
     */
    @GetMapping("/userInfo")
    @PermitAll
    @Operation(summary = "获取用户信息")
    @OperateLog(enable = false)
    public UserInfoRespVo userInfo(HttpServletRequest request) throws Exception {
        if (StringUtils.isBlank(request.getHeader("access_token"))) {
            throw new Exception("token不能为空");
        }
        return null; //authService.getUserInfo(request);
    }

    @PostMapping("/logout")
    @PermitAll
    @Operation(summary = "数采登出系统")
    @OperateLog(enable = false)
    public CommonResult<Boolean> logout(HttpServletRequest request, @RequestParam("logoutRedirectUrl") String logoutRedirectUrl) throws Exception {

//        String token = obtainAuthorization(request, securityProperties.getTokenHeader());
//        if (StringUtils.isBlank(token)) {
//            throw new Exception("token不能为空");
//        }
//        authService.logout(token, logoutRedirectUrl);
        return success(true);
    }
}
