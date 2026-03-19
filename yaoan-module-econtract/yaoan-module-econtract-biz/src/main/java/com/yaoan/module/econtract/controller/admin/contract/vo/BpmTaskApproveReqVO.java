package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Collection;

@Schema(description = "电子合同 - 通过流程任务的 Request VO")
@Data
public class BpmTaskApproveReqVO {

    /**
     * 任务编号
     */
    @Schema(description = "任务编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotEmpty(message = "任务编号不能为空")
    private String taskId;

    @Schema(description = "审批意见", requiredMode = Schema.RequiredMode.REQUIRED, example = "不错不错！")
    private String reason;

    @Schema(description = "业务id", requiredMode = Schema.RequiredMode.REQUIRED, example = "不错不错！")
    private String businessId;
    /**
     * 对应的流程定义
     */
    @Schema(description = "流程定义", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String processDefinition;

    @Schema(description = "抄送的用户编号数组", requiredMode = Schema.RequiredMode.REQUIRED, example = "1,2")
    private Collection<Long> copyUserIds;
}
