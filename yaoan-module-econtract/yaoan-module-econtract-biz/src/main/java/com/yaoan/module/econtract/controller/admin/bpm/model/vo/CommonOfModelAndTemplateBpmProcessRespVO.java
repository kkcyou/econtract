package com.yaoan.module.econtract.controller.admin.bpm.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2023/10/19 13:51
 */
@Data
public class CommonOfModelAndTemplateBpmProcessRespVO {

    @Schema(description = "节点名称")
    /**  节点名称*/
    private String taskName;

    @Schema(description = "操作人id", example = "123")
    /**  操作人id*/
    private Long userId;

    @Schema(description = "操作人name", example = "zhangsan")
    /**  操作人name*/
    private String userName;

    /**
     * 处理状态
     * 枚举 {@link com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum}
     */
    @Schema(description = "处理状态", example = "0")
    private Integer result;

    @Schema(description = "处理意见", example = "通过")
    private String reason;

    @Schema(description = "接收时间")
    /**  接收时间*/
    private LocalDateTime claimTime;

    @Schema(description = "结束时间")
    /**  结束时间*/
    private LocalDateTime endTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    /**  创建时间*/
    private LocalDateTime createTime;

}
