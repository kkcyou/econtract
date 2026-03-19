package com.yaoan.module.econtract.api.gcy.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description: 订单请求参数
 * @author: zhc
 * @date: 2024-04-24
 */
@Data
public class OrderReqDTO {
    private static final long serialVersionUID = 6056488516698227018L;
    /**
     * 订单ID
     */
    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "订单ID不能为空")
    private String orderGuid;
    /**
     * 采购交易平台代码
     */
    @Schema(description = "商品品牌名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "采购交易平台代码不能为空")
    private String platform;

    /**
     * 订单状态编码
     */
    @Schema(description = "订单状态编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "订单状态编码不能为空")
    private String orderStatus;

}
