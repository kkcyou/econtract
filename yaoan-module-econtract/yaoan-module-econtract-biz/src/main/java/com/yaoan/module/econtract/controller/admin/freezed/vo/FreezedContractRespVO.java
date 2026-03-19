package com.yaoan.module.econtract.controller.admin.freezed.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "TerminateContract RespVO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FreezedContractRespVO extends FreezedContractBaseVO {
    private static final long serialVersionUID = 1274358932155314705L;

    /**
     * 任务id
     */
    @Schema(description = "任务id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String taskId;

}
