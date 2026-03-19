package com.yaoan.module.econtract.controller.admin.gpx.vo.draft;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/21 18:16
 */
@Data
public class SupplierInfo {
    private static final long serialVersionUID = 5875656008668821806L;
    /**
     * id
     */
    private String id;
    /**
     * 项目id
     */
    private String projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 项目编码
     */
    private String projectCode;
    /**
     * 包id
     */
    private String packageId;
    /**
     * 包号
     */
    private String packageNumber;
    /**
     * 包名称
     */
    private String packageName;
    /**
     * 供应商id
     */
    private String supplierId;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 中标金额
     */
    private String winBidAmount;
    /**
     * 联合体供应商信息
     */
    private List<SupplierCombinationInfo> supplierCombinationInfoList;
    /**
     * 供应商报价明细信息
     */
    private List<BidConfirmQuotationDetail> bidConfirmQuotationDetailList;
}
