package com.yaoan.module.econtract.controller.admin.performance.performanceTask.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * BasePerformanceTask
 */
@lombok.Data
public class BasePerformanceTask {
    /**
     * 履约任务类型
     */
    @Schema(description = "履约任务类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String perfTaskTypeId;
    /**
     * 履约任务名
     */
    @Schema(description = "履约任务名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    /**
     * 前置履约任务id
     */
    private String beforTaskId;
    /**
     * 合同履约id
     */
    private String contractPerfId;
    /**
     * 履约时间
     */
    @Schema(description = "履约时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "履约时间不能为空")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date perfTime;
    /**
     * 单位
     */
    private String unit;
    /**
     * 数量
     */
    private Long number;
    /**
     * 履约任务内容
     */
    @Schema(description = "履约任务内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "履约任务内容不能为空")
    private String content;
    /**
     * 用户id
     */
    private List<Long> userIds;
    /**
     * 履约提醒时间
     */
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date remindTime;


}
