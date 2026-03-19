package com.yaoan.module.econtract.controller.admin.contractarchives.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 合同档案 Response VO")
@Data
public class ContractArchivesReqVO {

    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.REQUIRED, example = "26904")
    private String contractId;

    @Schema(description = "档案id")
    private String id;

}