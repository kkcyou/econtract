package com.yaoan.module.econtract.api.gcy.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


/**
 *监管采购目录信息
 *
 * @author zhc
 */
@Data
public class PurCatalogInfoVo {
    /**
     * 采购目录编码
     */
    @Schema(description = "采购目录编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String purCatalogCode;
    /**
     * 采购目录编码名称
     */
    @Schema(description = "采购目录编码名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String purCatalogName;
    /**
     * 商品主键ID
     */
    @Schema(description = "商品主键ID", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String goodsId;

}
