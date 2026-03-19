package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewchecklist;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 审查清单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ReviewChecklistRespVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "570")
    @ExcelProperty("主键id")
    private String id;

    @Schema(description = "审查清单名称", example = "张三")
    @ExcelProperty("审查清单名称")
    private String reviewListName;

    @Schema(description = "审查清单类型code")
    @ExcelProperty("审查清单类型code")
    private String reviewListCode;

    @Schema(description = "审查清单类型名称", example = "1")
    @ExcelProperty("审查清单类型名称")
    private String reviewListType;

    @Schema(description = "状态 0开启 1关闭", example = "1")
    @ExcelProperty("状态 0开启 1关闭")
    private Boolean status;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String notes;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建时间")
    private List<String> itemIds;

}