package com.yaoan.module.system.api.oauth2;

import cn.hutool.core.util.IdUtil;
import com.yaoan.module.system.api.oauth2.dto.OAuth2AccessTokenCheckRespDTO;
import com.yaoan.module.system.api.oauth2.dto.OAuth2AccessTokenCreateReqDTO;
import com.yaoan.module.system.api.oauth2.dto.OAuth2AccessTokenRespDTO;
import com.yaoan.module.system.api.oauth2.dto.OAuth2ClientCheckRespDTO;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * OAuth2.0 Token API 接口
 *
 * @author 芋道源码
 */
public interface OAuth2TokenApi {

    /**
     * 创建访问令牌
     *
     * @param reqDTO 访问令牌的创建信息
     * @return 访问令牌的信息
     */
    OAuth2AccessTokenRespDTO createAccessToken(@Valid OAuth2AccessTokenCreateReqDTO reqDTO);

    /**
     * 校验访问令牌
     *
     * @param accessToken 访问令牌
     * @return 访问令牌的信息
     */
    OAuth2AccessTokenCheckRespDTO checkAccessToken(String accessToken);

    /**
     * 获取客户端信息
     *
     * @param clientId 客户端标识
     * @return 访问令牌的信息
     */
    OAuth2ClientCheckRespDTO getClientInfo(Long clientId);


    /**
     * 移除访问令牌
     *
     * @param accessToken 访问令牌
     * @return 访问令牌的信息
     */
    OAuth2AccessTokenRespDTO removeAccessToken(String accessToken);

    /**
     * 刷新访问令牌
     *
     * @param refreshToken 刷新令牌
     * @param clientId 客户端编号
     * @return 访问令牌的信息
     */
    OAuth2AccessTokenRespDTO refreshAccessToken(String refreshToken, String clientId);
    List<AdminUserRespDTO> getUsersByPlatformUserId(String userId);
    AdminUserRespDTO getUserInfoByUserId(Long userId);

    /**
     * 3. 验证 Token 有效性（含过期、关联用户存在性）
     * @param token 待验证的 Token
     * @return 有效返回 true，无效返回 false
     */
    Boolean validateToken(String token);
}
