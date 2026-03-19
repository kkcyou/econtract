package com.yaoan.module.econtract.controller.admin.warningmonitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
 * 预警监控项配置表（new预警） Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class WarningMonitorBaseVO {

    @Schema(description = "模块code（预留关联关系，暂不维护）", example = "9567")
    private String modelId;
    private String parentId;

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

    @Schema(description = "内置一个可配置的比较字段")
    private String compareStr;

}
