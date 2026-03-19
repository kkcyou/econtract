package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewchecklist;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;


@Schema(description = "管理后台 - 审查清单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReviewChecklistPageReqVO extends PageParam {

    @Schema(description = "审查清单名称", example = "张三")
    private String reviewListName;

    @Schema(description = "审查清单类型code")
    private String reviewListCode;

    @Schema(description = "审查清单类型名称", example = "1")
    private String reviewListType;

    @Schema(description = "状态 0开启 1关闭", example = "1")
    private Boolean status;

    @Schema(description = "备注")
    private String notes;

    @Schema(description = "创建时间")
//    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, timezone = "GMT+8")
    private Date[] createTime;

}