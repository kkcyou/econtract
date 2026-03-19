package com.yaoan.module.econtract.api.pgx.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: Pele
 * @date: 2024/7/29 18:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WarnPlatformResponseDTO {
    /**
     * 返回状态码(0:成功,1:失败)
     */
    private String status;
    /**
     * 返回状态信息，成功时返回success
     */
    private String message;
    /**
     * 返回结果JSON或JSONArray
     */
    private String data;
    /**
     * Data数据通过校验方法生成校验码
     */
    private String mac;
    /**
     * 接口调用时间(时间戳)
     */
    private String createDate;
}
