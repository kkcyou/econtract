package com.yaoan.module.econtract.controller.admin.amount.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: 签约金额req
 * @author: Pele
 * @date: 2023/11/8 21:21
 */
@Data
public class SearchYearReqVO {

    /**
     * 所查询年份
     */
    @Schema(description = "所查询年份", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private Integer searchYear;

}
