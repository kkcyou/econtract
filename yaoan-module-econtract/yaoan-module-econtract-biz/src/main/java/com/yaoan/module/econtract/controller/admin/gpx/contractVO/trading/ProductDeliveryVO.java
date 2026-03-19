package com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 产品交付与验收
 */
@Data
public class ProductDeliveryVO {
    /**
     * 验收---1
     */
    @Schema(description = "验收", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String acceptance;
}
