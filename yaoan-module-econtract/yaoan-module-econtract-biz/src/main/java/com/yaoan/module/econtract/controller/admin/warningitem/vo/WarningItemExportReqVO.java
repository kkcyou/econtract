package com.yaoan.module.econtract.controller.admin.warningitem.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yaoan.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 预警事项表（new预警） Excel 导出 Request VO，参数和 WarningItemPageReqVO 是一致的")
@Data
public class WarningItemExportReqVO {

    @Schema(description = "检查点id", example = "29497")
    private String configId;

    @Schema(description = "预警事项名称", example = "芋艿")
    private String itemName;

    @Schema(description = "风险说明", example = "你猜")
    private String itemRemark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
