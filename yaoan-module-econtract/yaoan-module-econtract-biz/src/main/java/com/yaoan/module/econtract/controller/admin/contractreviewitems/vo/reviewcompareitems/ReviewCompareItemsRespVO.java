package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewcompareitems;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 审查比对检测项 Response VO")
@Data
@ExcelIgnoreUnannotated
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReviewCompareItemsRespVO {

//    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10136")
//    @ExcelProperty("主键ID")
//    private String id;

    @Schema(description = "一级检测标题")
    private String itemFirstLevel;

    @Schema(description = "二级检测标题")
    private String itemSecondLevel;

    @Schema(description = "检测项名称", example = "张三")
    private String itemName;

//    @Schema(description = "创建时间")
//    @ExcelProperty("创建时间")
//    private LocalDateTime createTime;

}