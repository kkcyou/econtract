package com.yaoan.module.econtract.controller.admin.businesstype.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 业务类型 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BusinessTypeRespVO {

    @Schema(description = "单据类型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1141")
    @ExcelProperty("单据类型id")
    private String id;

    @Schema(description = "单据类型编号")
    @ExcelProperty("单据类型编号")
    private String code;

    @Schema(description = "单据类型名称", example = "赵六")
    @ExcelProperty("单据类型名称")
    private String name;

    @Schema(description = "数据表", example = "张三")
    @ExcelProperty("数据表")
    private String tableName;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}