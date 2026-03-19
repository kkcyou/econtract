package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 支付计划返回类
 */
@Data
public class PaymentSchedulePageRespVO {
    /**
     * 合同id
     */
    private String id;

    /**
     * 合同编码
     */
    private String code;

    /**
     * 合同名称
     */
    private String name;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 合同类型
     */
    private String contractType;

    /**
     * 合同类型名称
     */
    private String contractTypeName;

    /**
     * 我方签约主体id
     */
    private String mySignatoryId;

    /**
     * 我方签约主体名称
     */
    private String mySignatoryName;

    /**
     * 相对方签约主体名称集合
     */
    private List<String> signatoryList;

    /**
     * 合同金额
     */
    private BigDecimal amount;

    /**
     * 已付金额
     */
    private BigDecimal payedAmount;

    /**
     * 合同付款进度
     */
    private BigDecimal paymentRatio;

    /**
     * 合同状态
     */
    private Integer status;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建人名称
     */
    private String creatorName;

    /**
     * 签署方集合
     */
    @Schema(description = "签署方集合", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<SignatoryRespVO> signatoryRespVOList;

    /**
     * 合同有效期-开始时间
     */
    private Date validity0;

    /**
     * 合同有效期-结束时间
     */
    private Date validity1;

    /**
     * 履约状态
     */
    private String statusName;

    
    
}
