package com.yaoan.module.econtract.controller.admin.supervise.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class ProcessRecordVO {

    /**
     * 待确认时间
     */
    @Schema(description = "待确认时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat( pattern = "yyyy-MM-dd")
    private Date sendTime;
    /**
     * 已确认时间
     */
    @Schema(description = "已确认时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat( pattern = "yyyy-MM-dd")
    private Date sureTime;
    /**
     * 供应商已盖章时间
     */
    @Schema(description = "供应商已盖章时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat( pattern = "yyyy-MM-dd")
    private Date supplierSignTime;
    /**
     * 已盖章时间
     */
    @Schema(description = "已盖章时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat( pattern = "yyyy-MM-dd")
    private Date orgSignTime;
    /**
     * 合同状态
     */
    @Schema(description = "合同状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer status;
    /**
     * 合同状态名称
     */
    @Schema(description = "合同状态名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String statusName;

}
