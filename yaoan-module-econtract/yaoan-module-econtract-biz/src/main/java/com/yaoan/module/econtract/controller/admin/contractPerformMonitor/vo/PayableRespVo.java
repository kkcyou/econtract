package com.yaoan.module.econtract.controller.admin.contractPerformMonitor.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/1 12:53
 */
@Data
public class PayableRespVo {
    /**
     * 履约日期
     * */
    @Schema(description = "履约日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private Date  performDate;

    /**
     *履约金额
     * */
    @Schema(description = "履约金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private Integer payAmount;

    /**
     *实付金额
     * */
    @Schema(description = "实付金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private Integer actualPayment;


}
