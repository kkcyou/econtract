package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewchecklist;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 审查清单新增/修改 Request VO")
@Data
public class ReviewChecklistSaveReqVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "570")
    private String id;

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

    //创建时间
    @Schema(description = "创建日期")
    @JsonFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND, timezone = "GMT+8")
    private Date createTime;

    @Schema(description = "检测项列表")
    private List<String> checklistRuleList;

}