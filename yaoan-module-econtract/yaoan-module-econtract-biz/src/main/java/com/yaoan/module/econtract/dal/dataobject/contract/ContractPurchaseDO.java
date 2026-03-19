package com.yaoan.module.econtract.dal.dataobject.contract;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 合同采购内容
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_purchase")
public class ContractPurchaseDO extends TenantBaseDO {
    private static final long serialVersionUID = -1626428449573030996L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 序号
     */
    private Long sort;

    /**
     * 品目编码
     */
    private String itemCode;

    /**
     * 品目名称
     */
    private String itemName;

    /**
     * 采购标的
     */
    private String purchaseTarget;

    /**
     * 是否进口
     */
    private Integer isImport;

    /**
     * 采购数量
     */
    private Double purchaseQuantity;

    /**
     * 计量单位
     */
    private String measuringUnit;

    /**
     * 规格型号
     */
    private String specification;

    /**
     * 单价（元）
     */
    private Double unitPrice;

    /**
     * 金额（元）
     */
    private Double amount;
}
