package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Schema(description = "电子合同 - 撤回流程任务的 Request VO")
@Data
public class BpmTaskRevokeReqVO {

    /**
     * 流程实例编号
     */
    @Schema(description = "流程实例编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotEmpty(message = "流程实例编号不能为空")
    private String processInstanceId;

    /**
     * 任务编号
     */
    @Schema(description = "任务编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotEmpty(message = "任务编号不能为空")
    private String taskId;


    /**
     * 对应的流程定义
     */
    @Schema(description = "流程定义", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String processDefinition;

    /**
     * 对应的流程定义
     */
    @Schema(description = "所属业务id", requiredMode = Schema.RequiredMode.REQUIRED, example = "14245")
    private String businessId;
}
