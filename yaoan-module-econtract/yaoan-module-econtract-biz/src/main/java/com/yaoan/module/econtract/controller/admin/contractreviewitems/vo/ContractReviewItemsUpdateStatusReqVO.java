package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author wsh
 */
@Schema(description = "管理后台 - 合同审查规则更新 Request VO")
@Data
@ToString(callSuper = true)
public class ContractReviewItemsUpdateStatusReqVO  {
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;

}
