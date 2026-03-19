package com.yaoan.module.econtract.controller.admin.contractPerformMonitor.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/1 12:54
 */
@Data
public class PayableReqVo {
    @Schema(description = "所查询年份", requiredMode = Schema.RequiredMode.REQUIRED, example = "50")
    private Integer searchYear;
}
