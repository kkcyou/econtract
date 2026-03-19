package com.yaoan.module.econtract.controller.admin.signet.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

@Schema(description = "Signet DetailsResp VO")
@Data
@ToString(callSuper = true)
public class SignetProcessPageRespVO {
    /**
     * 用印id
     */
    @Schema(description = "用印id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String id;

    /**
     * 印章id
     */
    @Schema(description = "印章id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sealId;

    /**
     * 印章名称
     */
    @Schema(description = "印章名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sealName;

    /**
     * 申请日期
     */
    @Schema(description = "申请日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date applyDate;

    /**
     * 申请人ID
     */
    @Schema(description = "申请人ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long applyUser;
    /**
     * 申请人名称
     */
    @Schema(description = "申请人名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String applyUserName;

    /**
     * 合同id
     */
    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractId;

    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractName;

    /**
     * 合同编号
     */
    @Schema(description = "合同编号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String contractCode;

    /**
     * 流程实例的编号
     */
    private String processInstanceId;
    /**
     * 任务id
     */
    private String taskId;
    /**
     * 审批状态
     */
    private Integer result;

    /**
     * 审批状态名称
     */
    @Schema(description = "审批状态名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String resultName;

    /**
     * {@link com.yaoan.module.econtract.enums.common.flow.FlowableStatusEnums}
     */
    private String flowableStatus;
    /**
     * 审批状态名称
     */
    private String flowableStatusName;
    /**
     * 已办任务操作结果
     */
    private Integer doneTaskResult;
    /**
     * 已办任务操作结果名称
     */
    private String doneTaskResultName;

    /**
     * 节点名称
     * */
    private String nodeName;

}
