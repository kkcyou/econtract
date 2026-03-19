package com.yaoan.framework.security.core.filter;

import com.yaoan.framework.common.enums.UserTypeEnum;
import com.yaoan.framework.security.config.SecurityProperties;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.system.api.oauth2.OAuth2TokenApi;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static com.yaoan.framework.common.exception.enums.GlobalErrorCodeConstants.SESSIONTIMEOUT;
@RequiredArgsConstructor
public class AnonymousJwtFilter extends OncePerRequestFilter {
    private final SecurityProperties securityProperties;

    private final OAuth2TokenApi oauth2TokenApi;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 1. 判断当前请求是否需要匿名 JWT 校验（匹配 ANONYMOUS_JWT_URLS）
        String requestUri = request.getRequestURI();
        boolean needAnonymousJwt = true;
        if (securityProperties.getIgnoreUrls().contains(requestUri)) {
            needAnonymousJwt = false;
        }
        if (!needAnonymousJwt) {
            // 不需要匿名 JWT 校验，直接放行（交给其他过滤器处理，如登录态校验）
            filterChain.doFilter(request, response);
            return;
        }

        // 2. 提取请求头中的 JWT（格式：Authorization: Bearer <token>）
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Token 无效，放行给原有过滤器
            filterChain.doFilter(request, response);
            return;
        }
        String token = authHeader.substring(7); // 截取 "Bearer " 后的 Token
        Integer userType = WebFrameworkUtils.getLoginUserType(request);

        // 3. 校验 JWT 有效性（仅验签、过期，不关联用户）
        Long aLong = 0L;
        try {
            // 判断 Token 是否可以转为数值类型，可以说明传的是session否则传的是token。
            aLong = Long.valueOf(token);
        } catch (Exception e) {
            // 6. 校验过程异常（如 Token 格式错误），放行给原有过滤器
            filterChain.doFilter(request, response);
        }
        if (!Long.valueOf(0).equals(aLong)){
            // 如果是session
            if (oauth2TokenApi.validateToken(token)) {
                // 角色设为 ROLE_ANONYMOUS，后续接口可通过 @PreAuthorize("hasRole('ANONYMOUS')") 控制权限
                List<GrantedAuthority> authorities = Collections.singletonList(
                        new SimpleGrantedAuthority("ROLE_ANONYMOUS")
                );
                // 认证对象：用户名可设为固定值（如 "anonymous"），密码为 null，权限为匿名角色
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        token, null, authorities
                );
                // 存入 SecurityContext，标记请求已通过鉴权
                SecurityContextHolder.getContext().setAuthentication(authToken);
                SecurityFrameworkUtils.setLoginUser(new LoginUser().setId(Long.valueOf(token)).setUserType(UserTypeEnum.ANONYMOUS.getValue()).setTenantId(176L), request);

                // 直接放行到接口，不调用 filterChain.doFilter，跳过原有过滤器
                filterChain.doFilter(request, response);
            } else {
                // session会话过期
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(SESSIONTIMEOUT.getCode());
                String json = String.format("{\"code\":402,\"msg\":\"%s\",\"data\":null}", SESSIONTIMEOUT.getMsg());
                response.getWriter().write(json);
            }
        }
    }

    /**
     * 统一返回 401 错误响应（符合 Yudao 接口返回格式）
     */
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        String json = String.format("{\"code\":401,\"msg\":\"%s\",\"data\":null}", message);
        response.getWriter().write(json);
    }
}