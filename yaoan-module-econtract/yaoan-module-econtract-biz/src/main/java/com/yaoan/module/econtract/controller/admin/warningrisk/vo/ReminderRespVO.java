package com.yaoan.module.econtract.controller.admin.warningrisk.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Schema(description = "预警管理后台 -  提醒返回字段")
@Data
public class ReminderRespVO {
    @Schema(description = "合同id")
    private Long contractId;

    @Schema(description = "合同名称")
    private String contractName;

    @Schema(description = "天数")
    private Integer days;

    @Schema(description = "合同签署截止日期")
    private Date expirationDate;

}
