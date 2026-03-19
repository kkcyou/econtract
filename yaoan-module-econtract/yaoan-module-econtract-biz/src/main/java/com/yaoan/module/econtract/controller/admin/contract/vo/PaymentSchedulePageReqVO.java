package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;

/**
 * 付款计划入参
 */
@Schema(description = "Document Rel Req VO")
@Data
@EqualsAndHashCode
@ToString(callSuper = true)
public class PaymentSchedulePageReqVO extends PageParam {

    /**
     * 合同编码
     */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String code;

    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    /**
     * 合同类型
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractType;

    /**
     * 我方签约主体名称
     */
    @Schema(description = "我方签约主体名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String payerName;

    /**
     * 相对方主体名称
     */
    @Schema(description = "相对方主体名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String payeeName;

    @Schema(description = "合同状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String status;

    /**
     * 当前时间
     */
    private Date currentDate;
    /**
     * 乙方名称
     */
    private String partBName;

    /**
     * 付款/收款
     */
    private Integer amountType;
}