package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.yaoan.framework.common.pojo.PageResult;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentAmount {
    /**
     * 合同总额
     */
    private BigDecimal totalAmount;

    /**
     * 已支付金额
     */
    private BigDecimal paidAmount;

    /**
     * 已支付金额比例
     */
    private Double paidRatio;

    /**
     * 未支付金额
     */
    private BigDecimal unpaidAmount;

    /**
     * 未支付金额比例
     */
    private Double unpaidRatio;

}