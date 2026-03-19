package com.yaoan.module.econtract.controller.admin.bpm.performance.suspend.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author doujiale
 */
@Schema(description = "履约进程response VO")
@Data
public class BpmProcessRespVO {

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

    @Schema(description = "操作时间")
    private LocalDateTime operateTime;


}
