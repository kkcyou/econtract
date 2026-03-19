package com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relcontracttypechecklist;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 合同类型-审查清单关联 Response VO")
@Data
@ExcelIgnoreUnannotated
public class RelContractTypeChecklistRespVO {

    @Schema(description = "关联id", requiredMode = Schema.RequiredMode.REQUIRED, example = "26560")
    @ExcelProperty("关联id")
    private String id;

    @Schema(description = "合同类型编号", example = "2")
    @ExcelProperty("合同类型编号")
    private String contractType;

    @Schema(description = "审查清单id", example = "31057")
    @ExcelProperty("审查清单id")
    private String reviewListId;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}