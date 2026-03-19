package com.yaoan.module.econtract.controller.admin.codegeneration.vo;

import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/5 18:29
 */
@Data
public class CodeQueryReqVO {
    /**
     * 合同id
     */
    private String id;
    /**
     * 编号类型
     */
    private String type;
    /**
     * 编号规则id
     */
    private String codeRuleId;
    /**
     * 合同类型
     */
    private String contractType;
}
