package com.yaoan.module.econtract.controller.admin.warningmonitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 预警监控项配置表（new预警） Excel 导出 Request VO，参数和 WarningMonitorPageReqVO 是一致的")
@Data
public class WarningMonitorListReqVO {

    /**
     * 主键id集合
     */
    private List<String> idList;
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

}
