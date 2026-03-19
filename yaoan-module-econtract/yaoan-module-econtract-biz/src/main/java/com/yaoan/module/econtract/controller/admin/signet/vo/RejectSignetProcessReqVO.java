package com.yaoan.module.econtract.controller.admin.signet.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Schema(description = "电子合同 - 撤销流程的 Request VO")
@Data
public class RejectSignetProcessReqVO {
    /**
     * 合同id
     */
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String contractId;
    /**
     * 用印id
     */
    @Schema(description = "用印id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String id;

    @Schema(description = "任务编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotEmpty(message = "任务编号不能为空")
    private String taskId;

    @Schema(description = "审批意见", requiredMode = Schema.RequiredMode.REQUIRED, example = "不错不错！")
    @NotEmpty(message = "审批意见不能为空")
    private String reason;
}
