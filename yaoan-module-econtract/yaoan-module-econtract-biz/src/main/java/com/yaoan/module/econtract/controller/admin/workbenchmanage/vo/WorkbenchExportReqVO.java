package com.yaoan.module.econtract.controller.admin.workbenchmanage.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yaoan.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 工作台管理 Excel 导出 Request VO，参数和 WorkbenchPageReqVO 是一致的")
@Data
public class WorkbenchExportReqVO {

    @Schema(description = "工作台编码")
    private String code;

    @Schema(description = "工作台名称", example = "张三")
    private String name;

    @Schema(description = "组件路径")
    private String component;

    @Schema(description = "组件名称", example = "张三")
    private String componentName;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
