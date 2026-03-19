package com.yaoan.module.econtract.controller.admin.contract.vo.ledger;

import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2025-5-21 14:35
 */
@Data
public class ContractBorrowRespVO {
    private String borrowBpmId;
    private String contractId;
    private String contractCode;
    private String contractName;
    private String contractTypeName;
    private String borrowerName;
    private String borrowerDeptName;
    @Schema(description = "档案id")
    private String archiveId;
    @Schema(description = "档案名称", example = "芋艿")
    private String name;
    @Schema(description = "档案载体 电子0 纸质1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String medium;

    @Schema(description = "档案载体 电子0 纸质1", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("档案载体 电子0 纸质1")
    private String mediumName;
    @Schema(description = "借阅状态")
    private Integer borrowStatus;

    @Schema(description = "借阅状态名称")
    private String borrowStatusName;
    /**
     * 申请时间
     */
    @Schema(description = "申请时间")
    private LocalDateTime createTime;

    /**
     * 借阅时间
     */
    @Schema(description = "借阅时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date submitTime;

    /**
     * 预计归还时间
     */
    @Schema(description = "预计归还时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date returnTime;



}
