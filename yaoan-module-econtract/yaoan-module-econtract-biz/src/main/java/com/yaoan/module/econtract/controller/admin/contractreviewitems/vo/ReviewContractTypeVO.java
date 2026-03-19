package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author wsh
 */
@Schema(description = "管理后台 - 合同审查合同类型")
@Data
@ToString(callSuper = true)
public class ReviewContractTypeVO {
    @Schema(description = "合同类型id", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer contractTypeId;

    @Schema(description = "合同类型名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractTypeName;
}
