package com.yaoan.module.econtract.controller.admin.warningmonitor.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yaoan.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 预警监控项配置表（new预警） Excel 导出 Request VO，参数和 WarningMonitorPageReqVO 是一致的")
@Data
public class WarningMonitorExportReqVO {

    @Schema(description = "模块code（预留关联关系，暂不维护）", example = "9567")
    private String modelId;

    @Schema(description = "监控项名称", example = "张三")
    private String name;

    @Schema(description = "监控项类型（监控业务数据1，监控流程数据2）", example = "2")
    private Integer type;

    @Schema(description = "业务表标识")
    private String businessCode;

    @Schema(description = "监控业务表字段")
    private String businessField;

    @Schema(description = "流程key")
    private String flowKey;

    @Schema(description = "流程阶段/节点")
    private String flowStage;

    @Schema(description = "比较类型（0取值，1计差）", example = "1")
    private Integer compareType;

    @Schema(description = "计差方式（>，<）", example = "2")
    private Integer calculateType;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "内置一个可配置的比较字段")
    private String compareStr;

}
