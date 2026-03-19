package com.yaoan.module.econtract.controller.admin.ledger.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/19 19:27
 */
@Data
public class LedgerModelInfoRespVO {

    /**
     * 合同模板名称
     */
    private String modelName;

    /**
     * 合同模板编码
     */
    private String modelCode;

    /**
     * 合同模板类型
     */
    private Integer modelCategoryId;

    /**
     * 合同模板类型str
     */
    private String modelCategoryStr;

    /**
     * 模板时效
     */
    private String modelEffectStr;

    /**
     * 合同模板有效期
     */
    private String effectPeriodStr;

    /**
     * 模板创建时间
     */
    private LocalDateTime modelCreateTime;


}
