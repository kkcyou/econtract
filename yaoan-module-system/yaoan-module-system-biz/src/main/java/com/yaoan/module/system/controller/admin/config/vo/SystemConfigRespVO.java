package com.yaoan.module.system.controller.admin.config.vo;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/8 10:56
 */
@Data
public class SystemConfigRespVO {

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
