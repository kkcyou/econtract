package com.yaoan.module.econtract.controller.admin.contract.vo;

import lombok.Data;

/**
 * 合同参数信息集合
 */
@Data
public class ContractParameterVO {
    /**
     * 参数id
     */
    private String paramId;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 参数内容
     */
    private String value;

    /**
     * 位置
     */
    private String position;

    /**
     * 提示语
     */
    private String prompt;
}
