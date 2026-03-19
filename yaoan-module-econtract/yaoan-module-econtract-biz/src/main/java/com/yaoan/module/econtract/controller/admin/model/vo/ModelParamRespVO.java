package com.yaoan.module.econtract.controller.admin.model.vo;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/5 17:48
 */
@Data
public class ModelParamRespVO {
    /**
     * 参数id
     */
    private String id;

    /**
     * 参数名称
     */
    private String name;

    /**
     * 参数编码
     */
    private String code;

    /**
     * 参数类型
     */
    private String type;

    /**
     * 合同类型
     */
    private String contractType;

    /**
     * 是否可编辑
     */
    private String ifEdit;

    /**
     * 参数值
     */
    private String paramValue;
}
