package com.yaoan.module.econtract.controller.admin.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/17 11:54
 */
@Data
public class ThirdOrderAutoInfoRespVO extends OrderAutoInfoRespVO{
    /**
     * 合同来源（电子卖场：gpmall-5.3，电子交易（项目采购）：gpms-gpx-5.3，框采平台：gp-gpfa）
     */
    private String contractFrom;

    /**
     * 合同起草方：采购人（1）/供应商（2）。默认为供应商
     */
    private Integer contractDrafter;

    /**
     * 供应商社会信用代码/组织机构代码/个人身份证/供应商的纳税人识别号
     */
    @Schema(description = "供应商社会信用代码/组织机构代码/个人身份证/供应商的纳税人识别号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierCode;

    /**
     * 采购人统一社会信用代码/采购人纳税识别号
     */
    @Schema(description = "采购人统一社会信用代码/采购人纳税识别号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgCode;

    /**
     * 采购人开户名称
     */
    @Schema(description = "采购人开户名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orgAccountName;

    /**
     * 供应商开户名称
     */
    @Schema(description = "供应商开户名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierAccountName;

    /**
     * 供应商对应外商投资类型
     */
    @Schema(description = "供应商对应外商投资类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String foreignInvestmentType;

    /**
     * 采购人传真
     */
    @Schema(description = "采购人传真", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String orglinkFax;

    /**
     * 采购人传真
     */
    @Schema(description = "供应商传真", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierlinkFax;

    /**
     * 公司id
     */
    private Long companyId;
}
