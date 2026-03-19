package com.yaoan.module.econtract.controller.admin.bpm.template.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/14 14:58
 */
@Data
public class TemplateBpmPageRespVO {
    /**
     * 范本名称
     */
    @Schema(description = "范本名称")
    private String name;

    /**
     * 范本id
     */
    @Schema(description = "范本id")
    private String templateId;

    /**
     * 范本编码
     */
    @Schema(description = "范本编码")
    private String code;

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
     * 结果id
     */
    @Schema(description = "结果id")
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
     * 流程实例id
     */
    @Schema(description = "流程任务id")
    private String taskId;
}
