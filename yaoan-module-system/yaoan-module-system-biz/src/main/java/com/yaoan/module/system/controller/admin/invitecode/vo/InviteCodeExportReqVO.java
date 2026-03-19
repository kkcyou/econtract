package com.yaoan.module.system.controller.admin.invitecode.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yaoan.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 邀请码管理 Excel 导出 Request VO，参数和 InviteCodePageReqVO 是一致的")
@Data
public class InviteCodeExportReqVO {

    @Schema(description = "邀请码")
    private String code;

    @Schema(description = "有效天数")
    private Integer validDays;

    @Schema(description = "有效截至日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] validEndDate;

    @Schema(description = "可用次数（-1不限次数）")
    private Integer validTimes;

    @Schema(description = "类型", example = "2")
    private Integer type;

    @Schema(description = "是否启用", example = "2")
    private Integer status;

    @Schema(description = "使用的用户id", example = "25868")
    private Long userId;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
