package com.yaoan.module.econtract.controller.admin.contract.vo.extraction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 签约方信息
 */
@Data
public class SignatoryInfoRespVo {
    /**
     * 采购人名称
     */
    @Schema(description = "采购人名称")
     private String buyerOrgName;

    /**
     * 甲方法定（或授权）代表人
     */
    @Schema(description = "甲方法定（或授权）代表人")
     private String buyerLegalPerson;

    /**
     * 联系方式(甲方)
     */
    @Schema(description = "联系方式(甲方)")
    private String buyerLinkMobile;

    /**
     * 地址(甲方)
     */
    @Schema(description = "地址(甲方)")
    private String deliveryAddress;

    /**
     * 传真(甲方)
     */
    @Schema(description = "传真(甲方)")
    private String orgFax;

    /**
     * 供应商名称
     */
    @Schema(description = "供应商名称")
    private String supplierName;
    /**
     * 供应商id
     */
    @Schema(description = "供应商id")
    private String supplierGuid;
    /**
     * 供应商社会信用代码/组织机构代码/个人身份证/供应商的纳税人识别号
     */
    @Schema(description = "供应商社会信用代码/组织机构代码/个人身份证", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "供应商社会信用代码/组织机构代码/个人身份证不能为空")
    private String	supplierCode;

    /**
     * 乙方法定代表人(或授权)代理人
     */
    @Schema(description = "乙方法定代表人(或授权)代理人")
    private String supplierProxy;

    /**
     * 供应商联系电话
     */
    @Schema(description = "供应商联系电话")
    private String supplierLinkMobile;

    /**
     * 供应商联系地址
     */
    @Schema(description = "供应商联系地址")
    private String registeredAddress;

    /**
     * 供应商传真
     */
    @Schema(description = "供应商传真")
    private String supplierFax;

    /**
     * 供应商所在区域
     */
    @Schema(description = "供应商所在区域")
    private String supplierLocation;

    /**
     * 供应商所在区域名称
     */
    @Schema(description = "供应商所在区域名称")
    private String supplierLocationName;

    /**
     * 供应商规模
     */
    @Schema(description = "供应商规模")
    private String supplierSize;

    /**
     * 供应商规模编号
     */
    @Schema(description = "供应商规模编号")
    private String supplierSizeCode;


    /**
     * 供应商特殊性质
     */
    @Schema(description = "供应商特殊性质")
    private String supplierFeatures;
    /**
     * 供应商特殊性质
     */
    @Schema(description = "供应商特殊性质编号")
    private String supplierFeaturesCode;

}
