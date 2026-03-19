package com.yaoan.module.econtract.controller.admin.warningrisk.vo;


import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 预警结果分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WarningRiskPageReqVO extends PageParam {


    @Schema(description = "规则编码")
    private String warningRuleCode;

    @Schema(description = "规则名称", example = "王五")
    private String warningRuleName;

    @Schema(description = "规则类型 事前提醒1 事中提醒2", example = "2")
    private Integer warningRuleType;

    @Schema(description = "规则描述", example = "随便")
    private String warningRuleRemark;

    @Schema(description = "所属业务", example = "19280")
    private String businessTypeId;

    @Schema(description = "触发字段")
    private String warningRuleField;

    @Schema(description = "是否启用配置，0关闭，1启用")
    private String enable;

    @Schema(description = "逾期是否业务阻断，1阻断，0不阻断")
    private Integer isBlocked;

    @Schema(description = "所属公司", example = "23789")
    private Long companyId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    /**
     * id集合
     */
    @Schema(description = "id集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "demo")
    private List<String> ids;

}