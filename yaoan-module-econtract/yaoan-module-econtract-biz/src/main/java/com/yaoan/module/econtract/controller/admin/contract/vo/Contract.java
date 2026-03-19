package com.yaoan.module.econtract.controller.admin.contract.vo;

import lombok.Data;

import java.util.List;


@Data
public class Contract {
    /**
     * 任务编号
     */
    private String taskId;
    /**
     * 任务状态
     */
    private String status;
    /**
     * 合同基本信息
     */
    private ContractBaseInfo contractBaseInfo;
    /**
     * 采购明细
     */
    private List<PurchaseDetail> purchaseDetail;
    /**
     * 合同供应商
     */
    private List<Suppliers> suppliers;
    /**
     * 合同转让/分包供应商
     */
    private List<SubSuppliers> subSuppliers;
    /**
     * 支付计划
     */
    private List<PaymentPlan> paymentPlan;
    /**
     * 合同履约验收要求
     */
    private AcceptRequirement acceptRequirement;
    /**
     * 项目经理
     */
    private ProjectManager projectManager;

}
