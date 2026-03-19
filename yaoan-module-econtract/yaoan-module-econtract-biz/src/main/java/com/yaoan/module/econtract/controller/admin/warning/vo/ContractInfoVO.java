package com.yaoan.module.econtract.controller.admin.warning.vo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ContractInfoVO {
    /**
     * 合同id
     */
    private String id;

    /**
     * 合同编号
     */
    private String code;

    /**
     * 合同名称
     */
    private String name;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 采购单位名称
     */
    private String buyerOrgName;

    /**
     * 合同状态
     */
    private Integer status;

    /**
     * 合同状态名称
     */
    private String statusName;

    /**
     * 合同文件id
     */
    private Long pdfFileId;

    /**
     * 合同文件地址
     */
    private String pdfFilePath;
    /**
     * 合同金额
     */
    private BigDecimal totalMoney;

    /**
     * 支付计划列表
     */
    private List<ContractPaymentPlanVO> paymentPlanList;


}
