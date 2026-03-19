package com.yaoan.framework.security.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author : doujl
 * @className : ClientInfo
 * @description :
 * @date : 2023年12月07日18:11:38
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClientInfo {

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
     * 过期时间
     */
    private Date expiresTime;
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
     * <p>
     * 枚举 {@link }
     */
    private Integer status;



}
