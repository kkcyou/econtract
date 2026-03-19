package com.yaoan.framework.security.core.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yaoan.framework.common.exception.ServiceException;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.util.servlet.ServletUtils;
import com.yaoan.framework.security.config.ClientInfo;
import com.yaoan.framework.security.config.JwtInfo;
import com.yaoan.framework.security.config.SecurityProperties;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.JwtHelper;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.web.core.handler.GlobalExceptionHandler;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.system.api.oauth2.OAuth2TokenApi;
import com.yaoan.module.system.api.oauth2.dto.OAuth2AccessTokenCheckRespDTO;
import com.yaoan.module.system.api.oauth2.dto.OAuth2ClientCheckRespDTO;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Token 过滤器，验证 token 的有效性
 * 验证通过后，获得 {@link LoginUser} 信息，并加入到 Spring Security 上下文
 *
 * @author 芋道源码
 */
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final SecurityProperties securityProperties;

    private final GlobalExceptionHandler globalExceptionHandler;

    private final OAuth2TokenApi oauth2TokenApi;

    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // 关键判断：若 SecurityContext 已有认证信息（匿名过滤器已通过），直接放行
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            chain.doFilter(request, response);
            return;
        }
        if (securityProperties.getIgnoreUrls().contains(request.getRequestURI())){
            chain.doFilter(request, response);
            return;
        }

        String token = SecurityFrameworkUtils.obtainAuthorization(request, securityProperties.getTokenHeader());
        String wpsToken = SecurityFrameworkUtils.obtainAuthorization(request, securityProperties.getWebOfficeToken());
        if (StrUtil.isNotEmpty(wpsToken)) {
            token = wpsToken;
        }
        if (StrUtil.isNotEmpty(token)) {
            Integer userType = WebFrameworkUtils.getLoginUserType(request);
            try {
                // 1.1 基于 token 构建登录用户
                LoginUser loginUser = buildLoginUserByToken(token, userType);
                // 1.2 模拟 Login 功能，方便日常开发调试
                if (loginUser == null) {
                    loginUser = mockLoginUser(request, token, userType);
                }

                // 2. 设置当前用户
                if (loginUser != null) {
                    SecurityFrameworkUtils.setLoginUser(loginUser, request);
                }
            } catch (Throwable ex) {
                CommonResult<?> result = globalExceptionHandler.allExceptionHandler(request, ex);
                ServletUtils.writeJSON(response, result);
                return;
            }
        }


        String code = SecurityFrameworkUtils.obtainPermissionCode(request, securityProperties.getAccessToken());
        if (StrUtil.isNotEmpty(code)) {
            try {
                ClientInfo client = buildClientInfoByCode(code);
                if (client != null) {
                    SecurityFrameworkUtils.setPermissionInfo(client, request);
                }
            } catch (Throwable ex) {
                CommonResult<?> result = globalExceptionHandler.allExceptionHandler(request, ex);
                ServletUtils.writeJSON(response, result);
                return;
            }
        }

        // 继续过滤链
        chain.doFilter(request, response);
    }

    private ClientInfo buildClientInfoByCode(String code) throws Exception {
        ClientInfo clientInfo = JwtHelper.decodePermission(code);
        if (clientInfo == null || clientInfo.getId() == null) {
            return null;
        }
        OAuth2ClientCheckRespDTO targetInfo = oauth2TokenApi.getClientInfo(clientInfo.getId());
        return ClientInfo.builder().id(targetInfo.getId()).clientId(targetInfo.getClientId()).secret(targetInfo.getSecret()).expiresTime(clientInfo.getExpiresTime()).name(targetInfo.getName()).logo(targetInfo.getLogo()).description(targetInfo.getDescription()).build();
    }


    private LoginUser buildLoginUserByToken(String token, Integer userType) {
        try {
            OAuth2AccessTokenCheckRespDTO accessToken = oauth2TokenApi.checkAccessToken(token);
            if (accessToken == null) {
                return null;
            }
            // 用户类型不匹配，无权限
            if (ObjectUtil.notEqual(accessToken.getUserType(), userType)) {
                throw new AccessDeniedException("错误的用户类型");
            }
            AdminUserRespDTO adminUserRespDTO = oauth2TokenApi.getUserInfoByUserId(accessToken.getUserId());
            // 构建登录用户
            return new LoginUser().setId(accessToken.getUserId()).setUserType(accessToken.getUserType()).setType(adminUserRespDTO.getType()).setOrgId(adminUserRespDTO.getOrgId()).setSupplyId(adminUserRespDTO.getSupplyId())
                    .setTenantId(accessToken.getTenantId()).setScopes(accessToken.getScopes())
                    .setCompanyId(accessToken.getCompanyId()).setOrgId(adminUserRespDTO.getOrgId())
                    .setMobile(adminUserRespDTO.getMobile())
                    .setNickName(adminUserRespDTO.getNickname())
                    .setIdCard(adminUserRespDTO.getIdCard())
                    .setRealName(adminUserRespDTO.getRealName())
                    .setInviteMethod(adminUserRespDTO.getInviteMethod())
                    ;
        } catch (ServiceException serviceException) {
            // 校验 Token 不通过时，考虑到一些接口是无需登录的，所以直接返回 null 即可
            try {
                // 校验 Token 不通过时，考虑到一些接口是无需登录的，所以直接返回 null 即可
                JwtInfo decode = JwtHelper.decode(token);
                if (decode != null && StringUtils.isNotBlank(decode.getUserId())) {
                    List<AdminUserRespDTO> adminUserRespDTOs = oauth2TokenApi.getUsersByPlatformUserId(decode.getUserId());
                    if (CollectionUtil.isNotEmpty(adminUserRespDTOs)) {
                        //TenantContextHolder.setTenantId(adminUserRespDTOs.get(0).getTenantId());
                        if (adminUserRespDTOs.size() == 1) {
                            AdminUserRespDTO adminUserRespDTO = adminUserRespDTOs.get(0);
                            return new LoginUser().setId(adminUserRespDTO.getId()).setUserType(userType).setType(adminUserRespDTO.getType()).setAgentId(adminUserRespDTO.getAgentId()).setSupplyId(adminUserRespDTO.getSupplyId()).setOrgId(adminUserRespDTO.getOrgId()).setIsAdmin(adminUserRespDTO.getIsAdmin()).setRegionCode(adminUserRespDTO.getRegionCode())
                                    .setTenantId(adminUserRespDTO.getTenantId()).setScopes(null);
                        }
                        int userTypeNum = 0;
                        //平台编号 用户当前的业务类型 业务类型 1:监管用户 2:采购人 3:代理机构 4:供应商
                        //产品用户编号 用户当前的业务类型 业务类型 0:系统管理员,1:采购单位,2:供应商,3:代理机构,4:采购监管机构,5:财政业务部门,6:评审专家,7:金融机构用户
                        if ("1".equals(decode.getUserTypeNow())) {
                            userTypeNum = 4;
                        } else if ("2".equals(decode.getUserTypeNow())) {
                            userTypeNum = 1;
                        } else if ("4".equals(decode.getUserTypeNow())) {
                            userTypeNum = 2;
                        }
                        Integer finalUserTypeNum = userTypeNum;
                        AdminUserRespDTO adminUserResp = adminUserRespDTOs.stream().filter(adminUserRespDTO -> adminUserRespDTO.getType().equals(finalUserTypeNum)).findFirst().orElse(null);
                        if (adminUserResp != null) {
                            return new LoginUser().setId(adminUserResp.getId()).setUserType(userType).setType(adminUserResp.getType()).
                                    setAgentId(adminUserResp.getAgentId()).setSupplyId(adminUserResp.getSupplyId()).
                                    setOrgId(adminUserResp.getOrgId()).setIsAdmin(adminUserResp.getIsAdmin())//.setRegionCode(adminUserResp.getRegionCode())
                                    .setNickName(adminUserResp.getNickname())
                                    .setTenantId(null).setScopes(null);
                        }
                    } else {
                        System.out.println("【用户登录】查询不到平台该用户，decode--->" + decode);
                    }
                }
            } catch (Exception e) {
                System.out.println("【用户登录】信息解析异常" + e.getMessage());
                e.printStackTrace();
                return null;
            }
            return null;
        }
    }

    /**
     * 模拟登录用户，方便日常开发调试
     * <p>
     * 注意，在线上环境下，一定要关闭该功能！！！
     *
     * @param request  请求
     * @param token    模拟的 token，格式为 {@link SecurityProperties#getMockSecret()} + 用户编号
     * @param userType 用户类型
     * @return 模拟的 LoginUser
     */
    private LoginUser mockLoginUser(HttpServletRequest request, String token, Integer userType) {
        if (!securityProperties.getMockEnable()) {
            return null;
        }
        // 必须以 mockSecret 开头
        if (!token.startsWith(securityProperties.getMockSecret())) {
            return null;
        }
        // 构建模拟用户
        Long userId = Long.valueOf(token.substring(securityProperties.getMockSecret().length()));
        return new LoginUser().setId(userId).setUserType(userType)
                .setTenantId(WebFrameworkUtils.getTenantId(request));
    }

}
