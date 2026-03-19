package com.yaoan.module.system.controller.admin.invitecode.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * 邀请码管理 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class InviteCodeBaseVO {

    @Schema(description = "邀请码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "邀请码不能为空")
    private String code;

    @Schema(description = "有效天数,-1为永久有效")
    private Integer validDays;

    @Schema(description = "有效截至日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date validEndDate;

    @Schema(description = "可用次数（-1不限次数）")
    private Integer validTimes;

    @Schema(description = "类型")
    private Integer type;

    @Schema(description = "是否启用")
    private Integer status;

    @Schema(description = "使用的用户id，-1不限用户")
    private Long userId;

    @Schema(description = "备注")
    private String remark;

}
