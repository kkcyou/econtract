package com.yaoan.module.econtract.api.pgx.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class WarnPlatformRequestDTO {
    /**
     * 令牌(需放在请求头内)
     */
    private String access_token;
    /**
     * 请求参数(JSON)加密后的字符串
     */
    private String message;
    /**
     * 加密校验码 没用上但必传
     */
    private String mac = " ";
    /**
     * 接口调用时间(时间戳) 非必传
     */
    private Date createDate = new Date();
}
