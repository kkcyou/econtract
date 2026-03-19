package com.yaoan.framework.security.core.util;

import com.yaoan.framework.security.config.ClientInfo;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Date;

import static com.yaoan.module.system.enums.CommonConstants.USER_KEY_FOR_SPACE;

/**
 * 安全服务工具类
 *
 * @author 芋道源码
 */
public class SecurityFrameworkUtils {

    public static final String AUTHORIZATION_BEARER = "Bearer";

    private SecurityFrameworkUtils() {
    }

    @Nullable
    public static String getClientId() {
        ClientInfo info = getClientInfo();
        if (info == null) {
            return null;
        }
        return info.getClientId();
    }

    /**
     * 从请求中，获得认证 Token
     *
     * @param request 请求
     * @param header  认证 Token 对应的 Header 名字
     * @return 认证 Token
     */
    public static String obtainAuthorization(HttpServletRequest request, String header) {
        String authorization = request.getHeader(header);
        if (!StringUtils.hasText(authorization)) {
            return null;
        }
        int index = authorization.indexOf(AUTHORIZATION_BEARER + " ");
        if (index == -1) { // 未找到
            return null;
        }
        return authorization.substring(index + 7).trim();
    }


    /**
     * 从请求中，获得许可码 code
     *
     * @param request 请求
     * @param header  认证 dode 对应的 Header 名字
     * @return 许可码 code
     */
    public static String obtainPermissionCode(HttpServletRequest request, String header) {
        String code = request.getHeader(header);
        if (!StringUtils.hasText(code)) {
            return null;
        }
        return code.trim();
    }


    /**
     * 获得当前认证信息
     *
     * @return 认证信息
     */
    public static Authentication getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        if (context == null) {
            return null;
        }
        return context.getAuthentication();
    }

    /**
     * 获取当前用户
     *
     * @return 当前用户
     */
    @Nullable
    public static LoginUser getLoginUser() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getPrincipal() instanceof LoginUser ? (LoginUser) authentication.getPrincipal() : null;
    }

    /**
     * 获得当前用户的编号，从上下文中
     *
     * @return 用户编号
     */
    @Nullable
    public static Long getLoginUserId() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getId() : null;
    }

    @Nullable
    public static String getLoginUserKey4Space() {
        LoginUser loginUser = getLoginUser();
        if (loginUser == null) {
            return null;
        }
        return USER_KEY_FOR_SPACE + loginUser.getTenantId() + loginUser.getId();
    }

    /**
     * 获取当前许可信息
     *
     * @return 当前客户端许可信息
     */
    @Nullable
    public static ClientInfo getClientInfo() {
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            return null;
        }
        return authentication.getCredentials() instanceof ClientInfo ? (ClientInfo) authentication.getCredentials() : null;
    }


    private static Authentication buildPermissionAuthentication(ClientInfo client, HttpServletRequest request) {
        LoginUser loginUser = getLoginUser();
        // 创建 UsernamePasswordAuthenticationToken 对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginUser, client, Collections.emptyList());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }

    /**
     * 设置当前许可
     *
     * @param client  登录用户
     * @param request 请求
     */
    public static void setPermissionInfo(ClientInfo client, HttpServletRequest request) {
        // 创建 Authentication，并设置到上下文
        Authentication authentication = buildPermissionAuthentication(client, request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    /**
     * 获得当前客户端许可有效时间，从上下文中
     *
     * @return 有效时间
     */
    @Nullable
    public static Date getExpiresTime() {
        ClientInfo clientInfo = getClientInfo();
        return clientInfo != null ? clientInfo.getExpiresTime() : null;
    }


    /**
     * 获得当前租户的套餐类型，从上下文中
     *
     * @return 套餐类型
     */
    public static Integer getTenantPackageType() {
        LoginUser loginUser = getLoginUser();
        return loginUser != null ? loginUser.getTenantPackageType() : 0;
    }

    /**
     * 设置当前用户
     *
     * @param loginUser 登录用户
     * @param request   请求
     */
    public static void setLoginUser(LoginUser loginUser, HttpServletRequest request) {
        // 创建 Authentication，并设置到上下文
        Authentication authentication = buildAuthentication(loginUser, request);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 额外设置到 request 中，用于 ApiAccessLogFilter 可以获取到用户编号；
        // 原因是，Spring Security 的 Filter 在 ApiAccessLogFilter 后面，在它记录访问日志时，线上上下文已经没有用户编号等信息
        WebFrameworkUtils.setLoginUserId(request, loginUser.getId());
        WebFrameworkUtils.setLoginUserType(request, loginUser.getUserType());
    }

    private static Authentication buildAuthentication(LoginUser loginUser, HttpServletRequest request) {
        // 创建 UsernamePasswordAuthenticationToken 对象
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginUser, null, Collections.emptyList());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        return authenticationToken;
    }


}
