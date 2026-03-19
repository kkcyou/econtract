package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relchecklistrule;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 审查清单-审查规则关联 Response VO")
@Data
@ExcelIgnoreUnannotated
public class RelChecklistRuleRespVO {

    @Schema(description = "关联id", requiredMode = Schema.RequiredMode.REQUIRED, example = "9149")
    @ExcelProperty("关联id")
    private String id;

    @Schema(description = "审查规则id", example = "1")
    @ExcelProperty("审查规则id")
    private String reviewId;

    @Schema(description = "审查清单id", example = "946")
    @ExcelProperty("审查清单id")
    private String reviewListId;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}