package com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Schema(description = "管理后台 - 合同模板推荐更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractAiDraftShowUpdateReqVO extends ContractAiDraftShowBaseVO {

    @Schema(description = "模板ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "30947")
    @NotNull(message = "模板ID不能为空")
    private Long templateiId;

}
