package com.yaoan.module.econtract.controller.admin.order.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description: 自动生成合同数据RespVO
 * @author: Pele
 * @date: 2024/1/17 15:39
 */
@Data
public class OrderAutoInfoRespVO extends OrderBaseInfoRespVO {
    private String orderId;

    /**
     * 合同编号
     * 生成规则：站点里可以配置
     */
    private String contractCode;

    /**
     * 合同名称
     * 生成规则：“采购人+品目+交易方式”
     */
    private String contractName;


    /**
     * 计划编号
     */
    private String buyPlanCode;

    /**
     * 计划名称
     */
    private String buyPlanName;

    /**
     * 供应商名字
     */
    private String supplierName;

    /**
     * 采购计划编号
     */
    private String planCode;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行账号
     */
    private String bankAccount;

    /**
     * 采购人签章
     */
    private String buyerSeal;

    /**
     * 供应商签章
     */
    private String supplierSeal;

    /**
     * 相关商品信息
     */
    private List<GoodsRespVO> goodsRespVOList;

    /**
     * 订单总额
     */
    private BigDecimal orderTotalAmount;

    /**
     * 采购人（甲方）
     */
    private String buyerName;

    /**
     * 合同金额人民币大写
     */
    private String upperCaseAmount;
    /**
     * 交货地点
     */
    private String dealAddress;
    /**
     * 付款方式-框采
     */
    private String payMethod;

    /**
     * 采购负责人
     */
    private String purchaserLeader;

    /**
     * 甲方代表/甲方联系人/甲方指派联系人
     */
    private String buyerProxy;

    /**
     * 采购负责人电话
     */
    private String purchaserPhone;

    /**
     * 采购负责人地址
     */
    private String purchaserAddress;

    /**
     * 合同签订日期
     */
    private Date contractSignTime;

    /**
     * 供应商开户银行名称
     */
    private String supplierBankName;


}
