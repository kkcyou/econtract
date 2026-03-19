package com.yaoan.module.econtract.controller.admin.businessroleformfield.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 角色字段关系 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BusinessRoleFormFieldRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10377")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "角色id", requiredMode = Schema.RequiredMode.REQUIRED, example = "17963")
    @ExcelProperty("角色id")
    private String roleId;

    @Schema(description = "编码")
    @ExcelProperty("编码")
    private String fieldCode;

    @Schema(description = "名称", example = "赵六")
    @ExcelProperty("名称")
    private String fieldName;

    @Schema(description = "所属字段id", requiredMode = Schema.RequiredMode.REQUIRED, example = "4322")
    @ExcelProperty("所属字段id")
    private String fieldId;

    @Schema(description = "所属表单id", requiredMode = Schema.RequiredMode.REQUIRED, example = "22904")
    @ExcelProperty("所属表单id")
    private String formId;

    @Schema(description = "所属业务id", requiredMode = Schema.RequiredMode.REQUIRED, example = "28787")
    @ExcelProperty("所属业务id")
    private String businessId;

    @Schema(description = "是否展示", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("是否展示")
    private Integer isShow;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}