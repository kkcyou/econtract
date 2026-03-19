package com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/21 20:17
 */
@Data
public class ContractInfoRespVO {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 签署截止日期
     */
    private Date expirationDate;


    /**
     * 合同类型
     */
    private String contractType;

    /**
     * 合同类型名称
     */
    private String contractName;

    /**
     * 签署日期
     */
    private Date signDate;

    /**
     * 合同有效期-开始时间
     */
    private Date validity0;

    /**
     * 合同有效期-结束时间
     */
    private Date validity1;

    /**
     * 合同金额
     */
    private Double amount;

    /**
     * 已付金额
     */
    private BigDecimal payedAmount;

    /**
     * 在途支付金额
     */
    private BigDecimal payingAmount;


    /**
     * 本期付款后付款进度
     */
    private BigDecimal payRate;
}
