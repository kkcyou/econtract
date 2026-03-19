package com.yaoan.module.econtract.controller.admin.bpm.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Pele
 */
@Schema(description = "模板页面信息 Response VO")
@Data
public class ModelBpmPageRespVO {

    /**
     * 模板编码
     */
    @Schema(description = "模板编码")
    private String name;

    /**
     * 模板编码
     */
    @Schema(description = "编码")
    private String code;

    /**
     * 模板id
     */
    @Schema(description = "模板id")
    private String modelId;

    /**
     * 审核内容
     */
    @Schema(description = "审核内容")
    private String approveContent;

    /**
     * 提交人
     */
    @Schema(description = "提交人")
    private String submitter;

    /**
     * 提交时间
     */
    @Schema(description = "提交时间")
    private LocalDateTime submitTime;

    /**
     * 流程结果Id
     */
    @Schema(description = "流程结果Id")
    private Integer result;

    /**
     * 审核状态
     */
    @Schema(description = "审核状态")
    private String approveStatus;

    /**
     * 审核时间
     */
    @Schema(description = "审核时间")
    private LocalDateTime approveTime;

    /**
     * 流程实例id
     */
    @Schema(description = "流程实例id")
    private String processInstanceId;

    /**
     * 流程任务id
     */
    @Schema(description = "流程任务id")
    private String taskId;
}