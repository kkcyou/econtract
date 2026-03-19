package com.yaoan.module.system.api.config.dto;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/22 17:52
 */
@Data
public class SystemConfigRespDTO {
    /**
     * id
     */
    private Long id;

    /**
     * 标识
     */
    private String cKey;

    /**
     * 内容
     */
    private String cValue;

    /**
     * 备注
     */
    private String remark;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 启用状态（0=停用，1=启用）
     */
    private Boolean status;

    /**
     * 流程定义key
     */
    private String proDefKey;
}
