package com.yaoan.module.econtract.controller.admin.review.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "ReviewContract CreateReq VO")
@Data
public class ReviewContractCreateReqVO {

    /**
     * 审查清单id
     */
    @NotNull(message = "审查清单id不能为空")
    @Schema(description = "审查清单id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String reviewId;


    /**
     * 合同类型id
     */
    @NotNull(message = "合同类型id不能为空")
    @Schema(description = "合同类型id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String typeId;

    /**
     * 合同类型名称
     */
    @NotNull(message = "合同类型名称不能为空")
    @Schema(description = "合同类型名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String typeName;


}
