package com.yaoan.module.econtract.controller.admin.contract.outwardcontract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Schema(description = "管理后台 - 对外合同更新 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OutwardContractUpdateReqVO extends OutwardContractBaseVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "22283")
    @NotNull(message = "主键不能为空")
    private String id;

}
