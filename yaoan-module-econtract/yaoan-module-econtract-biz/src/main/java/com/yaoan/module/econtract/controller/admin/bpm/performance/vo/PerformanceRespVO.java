package com.yaoan.module.econtract.controller.admin.bpm.performance.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author doujiale
 */
@Schema(description = "履约中止信息 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PerformanceRespVO extends PerformanceBaseVO {

    @Schema(description = "申请信息标识")
    private String id;

    @Schema(description = "任务标识")
    private String taskId;

    @Schema(description = "流程实例的编号")
    private String processInstanceId;

    @Schema(description = "提交人名称")
    private String userName;

    @Schema(description = "审批状态Str")
    private String resultStr;


}
