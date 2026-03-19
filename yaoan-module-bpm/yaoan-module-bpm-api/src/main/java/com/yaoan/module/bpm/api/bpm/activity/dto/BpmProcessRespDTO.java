package com.yaoan.module.bpm.api.bpm.activity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author doujiale
 */
@Schema(description = "进程response VO")
@Data
public class BpmProcessRespDTO {
    @Schema(description = "节点任务id")
    private String taskId;

    @Schema(description = "节点名称")
    private String taskName;

    @Schema(description = "操作人id", example = "123")
    private Long userId;

    @Schema(description = "操作人name", example = "zhangsan")
    private String userName;

    /**
     * 枚举 {@link com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum}
     */
    @Schema(description = "处理状态", example = "0")
    private Integer result;

    @Schema(description = "处理意见", example = "通过")
    private String reason;

    @Schema(description = "接收时间")
    private LocalDateTime claimTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;


}
