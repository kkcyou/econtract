package com.yaoan.module.econtract.controller.admin.warningrulemonitor.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yaoan.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 预警规则与监控项关联关系表（new预警） Excel 导出 Request VO，参数和 WarningRuleMonitorPageReqVO 是一致的")
@Data
public class WarningRuleMonitorExportReqVO {

    @Schema(description = "规则id", example = "19868")
    private String ruleId;

    @Schema(description = "监控项id", example = "23073")
    private String monitorId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
