package com.yaoan.module.econtract.controller.admin.warningnoticecfg.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yaoan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 预警通知配置表（new预警）分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WarningNoticeCfgPageReqVO extends PageParam {

    @Schema(description = "预警规则id", example = "24523")
    private String ruleId;

    @Schema(description = "通知对象选取来源（1.选择用户，2.根据工作流）", example = "1")
    private Integer userType;

    @Schema(description = "用户角色id")
    private Integer userRole;

    @Schema(description = "用户ids(用逗号分割)")
    private String userIds;

    @Schema(description = "通知方式（1.站内信，2邮件，3短信）")
    private Integer noticeWay;

    @Schema(description = "通知模板")
    private String contentTemplate;

    @Schema(description = "通知次数")
    private Integer noticeTimes;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
