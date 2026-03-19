package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;

/**
 * @author wsh
 */
@Schema(description = "管理后台 - 合同审查规则 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractReviewItemsRespVO extends ContractReviewItemsBaseVO {

    @Schema(description = "合同类型名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractTypeName;

}
