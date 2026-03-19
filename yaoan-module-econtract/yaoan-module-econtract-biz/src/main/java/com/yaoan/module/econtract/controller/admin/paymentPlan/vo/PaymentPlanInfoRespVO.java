package com.yaoan.module.econtract.controller.admin.paymentPlan.vo;

import com.yaoan.module.econtract.controller.admin.relative.vo.ContractRelativeVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 支付计划详情vo
 *
 * @author zhc
 * @since 2023-12-21
 */
@Data
public class PaymentPlanInfoRespVO {
    /**
     * 合同id
     */
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractId;
    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractName;
    /**
     * 合同编码
     */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractCode;
    /**
     * 合同类型
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractType;
    /**
     * 合同类型
     */
    @Schema(description = "合同类型名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractTypeName;
    /**
     * 签约地点
     */
    @Schema(description = "签约地点", requiredMode = Schema.RequiredMode.REQUIRED)
    private String location;

    /**
     * 签署截止日期
     */
    private Date expirationDate;
    /**
     * 申请人岗位
     */
    private String past;

    /**
     * 合同有效期
     */
    private Long validity;

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
     * 结算类型
     */
    private Integer amountType;

    /**
     * 合同分类
     */
    private Integer contractCategory;
    /**
     * 合同分类名称
     */
    private String contractCategoryName;


    /**
     * 合同签署方
     */
    @Schema(description = "合同签署方", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<ContractRelativeVO> signatory;
    /**
     * 合同支付计划
     */
    private List<PaymentPlanRespVO> paymentPlan;
    /**
     * 共几期
     */
    private Integer totalPeriod;

}
