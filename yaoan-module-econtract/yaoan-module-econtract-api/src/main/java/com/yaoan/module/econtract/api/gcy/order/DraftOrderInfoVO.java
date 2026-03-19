package com.yaoan.module.econtract.api.gcy.order;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
public class DraftOrderInfoVO {
    /**
     * 订单信息
     */
    @Schema(description = "订单信息", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<DraftOrderInfo> draftOrderInfoList;

    /**
     * 交易平台码
     */
    @Schema(description = "交易平台码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String platform;

}
