package com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yaoan.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 角色工作台关联 Excel 导出 Request VO，参数和 RoleWorkbenchRelPageReqVO 是一致的")
@Data
public class RoleWorkbenchRelExportReqVO {

    @Schema(description = "角色id", example = "20430")
    private Long roleId;

    @Schema(description = "角色名称", example = "李四")
    private String roleName;

    @Schema(description = "工作台id", example = "5659")
    private String workbenchId;

    @Schema(description = "工作台名称", example = "赵六")
    private String workbenchName;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
