package com.yaoan.module.econtract.controller.admin.warningmonitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 预警监控项配置表（new预警） Response VO")
@Data
@ToString(callSuper = true)
public class WarningMonitorRespVO  {

    private String id;
    private String parentId;
    /**
     * 模块code（预留关联关系，暂不维护）
     */
    private String modelId;
    /**
     * 监控项名称
     */
    private String name;
    /**
     * 监控项类型（监控业务数据1，监控流程数据2）
     */
    private Integer type;
}
