package com.yaoan.module.econtract.controller.admin.warningcfg.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yaoan.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 预警检查配置表(new预警) Excel 导出 Request VO，参数和 WarningCfgPageReqVO 是一致的")
@Data
public class WarningCfgExportReqVO {

    @Schema(description = "检查点名称", example = "赵六")
    private String name;

    @Schema(description = "模块来源")
    private String modelCode;

    @Schema(description = "模块来源名称", example = "王五")
    private String modelName;

    @Schema(description = "启用状态", example = "2")
    private String status;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
