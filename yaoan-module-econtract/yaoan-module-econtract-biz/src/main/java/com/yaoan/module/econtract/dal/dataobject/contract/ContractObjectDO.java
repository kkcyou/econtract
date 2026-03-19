package com.yaoan.module.econtract.dal.dataobject.contract;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_object")
public class ContractObjectDO extends TenantBaseDO implements Serializable {

    private static final long serialVersionUID = 2220078248421172531L;

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
     * 订单id/包id
     */
    private String orderId;

    /**
     * 标的名称
     */
    private String objectName;

    /**
     * 品牌
     */
    private String brand;

    /**
     * 规格型号
     */
    private String regularType;

    /**
     * 标的单价
     */
    private BigDecimal objectUnitPrice;

    /**
     * 标的数量
     */
    private Double objectQuantity;

    /**
     * 计量单位
     */
    private String unit;

    /**
     * 标的金额
     */
    private BigDecimal objectAmount;

    /**
     * 标的说明
     */
    private String objectDesc;
}

