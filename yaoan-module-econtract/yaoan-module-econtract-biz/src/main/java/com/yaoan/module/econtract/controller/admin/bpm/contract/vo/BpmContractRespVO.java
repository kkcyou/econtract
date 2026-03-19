package com.yaoan.module.econtract.controller.admin.bpm.contract.vo;

import com.yaoan.module.econtract.controller.admin.common.vo.flowable.FlowableParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author doujiale
 */
@Schema(description = "合同审批信息 Response VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class BpmContractRespVO extends BpmContractBaseVO {

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

    /**
     * 合同类型
     */
    private String contractType;
    /**
     * 合同类型名称
     */
    private String contractTypeName;
    /**
     * 审核时间
     */
    @Schema(description = "审核时间")
    private LocalDateTime approveTime;
    /**
     * 提交人
     */
    @Schema(description = "提交人")
    private String submitter;

    /**
     * {@link com.yaoan.module.econtract.enums.common.flow.FlowableStatusEnums}
     */
    private String flowableStatus;

    /**
     * 历史任务操作结果
     */
    private Integer hisTaskResult;

    /**
     * 已办任务操作结果
     */
    private Integer doneTaskResult;

    /**
     * 被分派到任务的人
     */
    private Long assigneeId;
    /**
     * 平台
     */
    private String platform;
    private String platformName;
}
