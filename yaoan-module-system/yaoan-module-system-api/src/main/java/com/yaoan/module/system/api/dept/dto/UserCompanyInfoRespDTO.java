package com.yaoan.module.system.api.dept.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户单位 Response DTO
 *
 * @author doujiale
 */
@Data
public class UserCompanyInfoRespDTO {

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 单位名称
     */
    private String name;
    /**
     * 用户名称
     */
    private String userName;

    /**
     * 信用代码
     */
    private String creditCode;

    /**
     * 是否是供应商
     */
    private Integer supplier;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
