package com.yaoan.module.econtract.controller.admin.warningnoticecfg.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import javax.validation.constraints.*;

/**
 * 预警通知配置表（new预警） Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class WarningNoticeCfgBaseVO {

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

}
