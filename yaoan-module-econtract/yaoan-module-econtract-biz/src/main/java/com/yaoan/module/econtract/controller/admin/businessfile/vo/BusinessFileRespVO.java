package com.yaoan.module.econtract.controller.admin.businessfile.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 业务数据和附件关联关系 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BusinessFileRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "30671")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "业务表id", requiredMode = Schema.RequiredMode.REQUIRED, example = "26313")
    @ExcelProperty("业务表id")
    private String businessId;

    @Schema(description = "附件id", example = "6398")
    @ExcelProperty("附件id")
    private Long fileId;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}