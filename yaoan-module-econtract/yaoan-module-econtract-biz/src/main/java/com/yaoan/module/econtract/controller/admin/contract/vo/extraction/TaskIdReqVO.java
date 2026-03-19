package com.yaoan.module.econtract.controller.admin.contract.vo.extraction;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;


@Data
public class TaskIdReqVO {
    @Schema(description = "任务id")
    @NotNull(message = "任务id不能为空")
    private String taskIds;
}
