package com.yaoan.module.econtract.controller.admin.businessformfield.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 表单字段 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BusinessFormFieldRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "662")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "编码")
    @ExcelProperty("编码")
    private String fieldCode;

    @Schema(description = "名称", example = "张三")
    @ExcelProperty("名称")
    private String fieldName;

    @Schema(description = "所属表单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "3557")
    @ExcelProperty("所属表单id")
    private String formId;

    @Schema(description = "所属业务id", requiredMode = Schema.RequiredMode.REQUIRED, example = "14245")
    @ExcelProperty("所属业务id")
    private String businessId;

    @Schema(description = "是否展示", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否展示")
    private Integer isShow;

    @Schema(description = "是否在搜索中展示", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否在搜索中展示")
    private Integer isSearch;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}