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
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * 预警规则（new预警） Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class WarningItemRuleBaseVO {

    @Schema(description = "预警事项id", example = "14436")
    private String warningItemId;

    @Schema(description = "预警事项名称", example = "芋艿")
    private String warningItemName;

    @Schema(description = "监控项id", example = "2657")
    private String monitorItemId;

    @Schema(description = "监控项名称", example = "张三")
    private String monitorItemName;

    @Schema(description = "比较类型（大于小于等于范围不等于立即执行）", example = "2")
    private Integer compareType;

    @Schema(description = "比较项1/阈值（整型）")
    private Integer compareItemStart;

    @Schema(description = "比较项2/阈值  (整型)")
    private Integer compareItemEnd;

    @Schema(description = "比较项1（浮点型，为金额和百分比预留）")
    private BigDecimal compareDecItemStart;

    @Schema(description = "比较项2（浮点型，为金额和百分比预留）")
    private BigDecimal compareDecItemEnd;

    @Schema(description = "比较项1（日期类型，预留）")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime compareDateItemStart;

    @Schema(description = "比较项2（日期类型，预留）")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime compareDateItemEnd;

    @Schema(description = "阈值单位（自然日，工作日，金额，数量，百分比）", example = "2")
    private Integer compareDataType;

}
