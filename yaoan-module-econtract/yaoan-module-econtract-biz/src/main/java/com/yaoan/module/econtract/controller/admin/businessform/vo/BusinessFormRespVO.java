package com.yaoan.module.econtract.controller.admin.businessform.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.yaoan.module.econtract.dal.dataobject.businessformfield.BusinessFormFieldDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 业务表单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BusinessFormRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "4444")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "编码")
    @ExcelProperty("编码")
    private String code;

    @Schema(description = "名称", example = "芋艿")
    @ExcelProperty("名称")
    private String name;

    @Schema(description = "所属业务id", requiredMode = Schema.RequiredMode.REQUIRED, example = "13539")
    @ExcelProperty("所属业务id")
    private String businessId;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    private List<BusinessFormFieldDO> businessFormFieldList; 
    
}