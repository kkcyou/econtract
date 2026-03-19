package com.yaoan.module.econtract.controller.admin.warning.vo;
import lombok.Data;

@Data
public class PackageInfoVO {
    /**
     * 采购包id
     */
    private String packageGuid;

    /**
     * 订单id
     */
    private String orderGuid;

    /**
     * 采购包名称
     */
    private String packageName;

    /**
     * 计划编号
     */
    private String buyPlanCode;

    /**
     * 采购单位
     */
    private String purchaseUnitName;

    /**
     * 中标金额
     */
    private Double winBidAmount;

    /**
     * 组织形式
     */
    private String orgMethodName;

    /**
     * 采购单位联系人
     */
    private String purchaseLinkName;

    /**
     * 采购单位联系人电话
     */
    private String purchaseLinkTel;
    /**
     * 采购方式
     */
    private String purchaseMethodName;
    /**
     * 实施形式
     */
    private String implementationName;



}
