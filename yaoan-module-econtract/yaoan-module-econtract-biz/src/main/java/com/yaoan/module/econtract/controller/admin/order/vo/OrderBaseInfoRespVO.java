package com.yaoan.module.econtract.controller.admin.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/15 17:48
 */
@Data
public class OrderBaseInfoRespVO {
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 合同编号
     */
    private String contractCode;

    /**
     * 订单总额
     */
    private BigDecimal orderTotalAmount;

    /**
     * 订单id
     */
    private String orderId;

    private String modelId;




}
