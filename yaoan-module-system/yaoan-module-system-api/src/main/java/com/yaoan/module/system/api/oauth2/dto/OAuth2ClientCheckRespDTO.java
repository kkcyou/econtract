package com.yaoan.module.system.api.oauth2.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * OAuth2.0 访问令牌的校验 Response DTO
 *
 * @author 芋道源码
 */
@Data
public class OAuth2ClientCheckRespDTO implements Serializable {

    private static final long serialVersionUID = -209673264689598786L;

    private Long id;
    /**
     * 客户端编号
     */
    private String clientId;
    /**
     * 客户端密钥
     */
    private String secret;
    /**
     * 应用名
     */
    private String name;
    /**
     * 应用图标
     */
    private String logo;
    /**
     * 应用描述
     */
    private String description;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 访问令牌的有效期
     */
    private Integer accessTokenValiditySeconds;
    /**
     * 刷新令牌的有效期
     */
    private Integer refreshTokenValiditySeconds;

}
