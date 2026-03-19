package com.yaoan.module.econtract.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Schema(description = "电子合同 - 撤销流程的 Request VO")
@Data
public class BpmTaskRepealReqVO {

    /**
     * 流程实例编号
     */
    @Schema(description = "流程实例编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotEmpty(message = "流程实例编号不能为空")
    private String processInstanceId;

    /**
     * 业务Id
     */
    @Schema(description = "业务Id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String businessId;

    /**
     * 对应的流程定义
     */
    @Schema(description = "流程定义", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String processDefinition;

}
