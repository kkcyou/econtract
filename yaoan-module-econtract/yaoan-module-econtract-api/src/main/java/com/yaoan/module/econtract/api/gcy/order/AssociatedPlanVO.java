package com.yaoan.module.econtract.api.gcy.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @description: 关联计划信息
 * @author: zhc
 * @date: 2024-02-18 19:25
 */
@Data
public class AssociatedPlanVO {

    private static final long serialVersionUID = 7416495742303775359L;
    /**
     * 计划id
     */
    @NotBlank(message = "计划id不能为空")
    @Schema(description = "计划id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyPlanId;

    /**
     * 计划名称
     */
    @NotBlank(message = "计划名称不能为空")
    @Schema(description = "计划名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyPlanName;

    /**
     * 计划编号
     */
    @NotBlank(message = "计划编号不能为空")
    @Schema(description = "计划编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String buyPlanCode;

    /**
     * 计划金额
     */
    @NotNull(message = "计划金额不能为空施")
    @Schema(description = "计划金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal buyPlanMoney;

    /**
     * 可签约金额
     */
    @Schema(description = "可签约金额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal signMoney;

    /**
     * 实施形式  BuyPlanImplementEnums
     */
//    @NotBlank(message = "实施形式不能为空")
    @Schema(description = "实施形式", requiredMode = Schema.RequiredMode.REQUIRED)
    private String implementationForm;
    /**
     * 实施形式名称
     */
//    @NotBlank(message = "实施形式名称不能为空")
    private String implementationFormName;

    /**
     * 采购方式
     */
    @Schema(description = "采购方式", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String purchaseMethod;
    /**
     * 采购方式名称
     */
    @Schema(description = "采购方式名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String purchaseMethodName;
    /**
     * 计划来源
     */
    @Schema(description = "计划来源(参见选项字典【BuyPlanSource】定义)", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanSource;
    /**
     * 计划来源名称
     */
    @Schema(description = "计划来源名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String buyPlanSourceName;
    /**
     * 是否进口产品采购(1:是,0:否)
     */
    @Schema(description = "是否进口产品采购", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer isImports;
    /***
     * 是否涉密采购(1:是,0:否)
     */
    @Schema(description = "是否涉密采购", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer secret;
    /**
     * 采购组织形式
     */
    private String kind;
}
