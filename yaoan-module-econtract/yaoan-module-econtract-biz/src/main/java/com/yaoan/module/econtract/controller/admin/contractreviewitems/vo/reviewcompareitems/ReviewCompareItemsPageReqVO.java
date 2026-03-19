package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewcompareitems;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;


@Schema(description = "管理后台 - 审查比对检测项分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ReviewCompareItemsPageReqVO extends PageParam {

    @Schema(description = "一级检测标题")
    private String itemFirstLevel;

    @Schema(description = "二级检测标题")
    private String itemSecondLevel;

    @Schema(description = "检测项名称", example = "张三")
    private String itemName;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}