package com.yaoan.module.econtract.controller.admin.warningitemrule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 预警规则（new预警） Excel VO
 *
 * @author admin
 */
@Data
public class WarningItemRuleExcelVO {

    @ExcelProperty("主键")
    private String id;

    @ExcelProperty("预警事项id")
    private String warningItemId;

    @ExcelProperty("预警事项名称")
    private String warningItemName;

    @ExcelProperty("监控项id")
    private String monitorItemId;

    @ExcelProperty("监控项名称")
    private String monitorItemName;

    @ExcelProperty("比较类型（大于小于等于范围不等于立即执行）")
    private Integer compareType;

    @ExcelProperty("比较项1/阈值（整型）")
    private Integer compareItemStart;

    @ExcelProperty("比较项2/阈值  (整型)")
    private Integer compareItemEnd;

    @ExcelProperty("比较项1（浮点型，为金额和百分比预留）")
    private BigDecimal compareDecItemStart;

    @ExcelProperty("比较项2（浮点型，为金额和百分比预留）")
    private BigDecimal compareDecItemEnd;

    @ExcelProperty("比较项1（日期类型，预留）")
    private LocalDateTime compareDateItemStart;

    @ExcelProperty("比较项2（日期类型，预留）")
    private LocalDateTime compareDateItemEnd;

    @ExcelProperty("阈值单位（自然日，工作日，金额，数量，百分比）")
    private Integer compareDataType;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}
