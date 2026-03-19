package com.yaoan.module.econtract.controller.admin.warningrisk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 预警规则明细新增/修改 Request VO")
@Data
public class WarningRuleConfigDetailSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "21748")
    private String id;

    @Schema(description = "规则id", requiredMode = Schema.RequiredMode.REQUIRED, example = "27014")
    @NotEmpty(message = "规则id不能为空")
    private String warningRuleId;

    @Schema(description = "是否启用配置，0关闭，1启用", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "是否启用配置，0关闭，1启用不能为空")
    private String enable;

    @Schema(description = "提醒类型", example = "1")
    private String warningType;

    @Schema(description = "提醒时间", example = "1")
    private Integer warningDays;

    @Schema(description = "提醒类型表达式")
    private String warningTypeExpression;

    @Schema(description = "触发条件")
    private String warningCondition;

    @Schema(description = "风险等级 低风险1 中风险2 高风险3")
    private Integer warningLevel;

    @Schema(description = "推送方式(多选),站内信message/短信sms/邮件email", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotEmpty(message = "推送方式(多选),站内信message/短信sms/邮件email不能为空")
    private String pushType;

    @Schema(description = "推送对象(多选),经办人creator/合同管理员admin ", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "推送对象(多选),经办人creator/合同管理员admin 不能为空")
    private String pushTarget;

    @Schema(description = "消息模板", example = "8087")
    private Long pushTemplateId;

    @Schema(description = "短信模板渠道 发送短信的平台")
    private Integer pushChannel;

    @Schema(description = "所属公司", example = "23754")
    private Long companyId;

    @Schema(description = "提醒内容", example = "8087")
    private String pushTemplateName;

    @Schema(description = "提醒类型名称", example = "8087")
    private String warningName;

}