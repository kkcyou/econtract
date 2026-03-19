package com.yaoan.module.econtract.controller.admin.warningmonitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 预警监控项配置表（new预警） Excel VO
 *
 * @author admin
 */
@Data
public class WarningMonitorExcelVO {

    @ExcelProperty("主键")
    private String id;

    @ExcelProperty("模块code（预留关联关系，暂不维护）")
    private String modelId;

    @ExcelProperty("监控项名称")
    private String name;

    @ExcelProperty("监控项类型（监控业务数据1，监控流程数据2）")
    private Integer type;

    @ExcelProperty("业务表标识")
    private String businessCode;

    @ExcelProperty("监控业务表字段")
    private String businessField;

    @ExcelProperty("流程key")
    private String flowKey;

    @ExcelProperty("流程阶段/节点")
    private String flowStage;

    @ExcelProperty("比较类型（0取值，1计差）")
    private Integer compareType;

    @ExcelProperty("计差方式（>，<）")
    private Integer calculateType;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("内置一个可配置的比较字段")
    private String compareStr;

}
