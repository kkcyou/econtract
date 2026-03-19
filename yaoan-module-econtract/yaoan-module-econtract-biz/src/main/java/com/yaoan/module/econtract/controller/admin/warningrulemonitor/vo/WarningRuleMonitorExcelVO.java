package com.yaoan.module.econtract.controller.admin.warningrulemonitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 预警规则与监控项关联关系表（new预警） Excel VO
 *
 * @author admin
 */
@Data
public class WarningRuleMonitorExcelVO {

    @ExcelProperty("主键")
    private String id;

    @ExcelProperty("规则id")
    private String ruleId;

    @ExcelProperty("监控项id")
    private String monitorId;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
