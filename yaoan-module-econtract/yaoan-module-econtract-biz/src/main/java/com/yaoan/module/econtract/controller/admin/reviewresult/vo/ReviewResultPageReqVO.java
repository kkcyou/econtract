package com.yaoan.module.econtract.controller.admin.reviewresult.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yaoan.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 智能审查结果分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReviewResultPageReqVO extends PageParam {

    @Schema(description = "合同id", example = "13430")
    private String contractId;

    @Schema(description = "审查结果（0=通过，1=低风险，2=中风险，3=高风险）")
    private Integer result;

    @Schema(description = "标题")
    private String title;

    @Schema(description = "风险等级（1=低风险，2=中风险，3=高风险）")
    private Integer riskLevel;

    @Schema(description = "风险提示")
    private String riskWarning;

    @Schema(description = "版本")
    private Long version;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
