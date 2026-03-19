package com.yaoan.module.econtract.controller.admin.warningmodel.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yaoan.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 预警模块来源（new预警） Excel 导出 Request VO，参数和 WarningModelPageReqVO 是一致的")
@Data
public class WarningModelExportReqVO {

    @Schema(description = "编码")
    private String code;

    @Schema(description = "名称", example = "张三")
    private String name;

    @Schema(description = "父级节点id", example = "19918")
    private String parentId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
