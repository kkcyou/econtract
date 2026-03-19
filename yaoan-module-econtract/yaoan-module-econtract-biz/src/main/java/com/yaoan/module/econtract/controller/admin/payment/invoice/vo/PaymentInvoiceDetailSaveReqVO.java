package com.yaoan.module.econtract.controller.admin.payment.invoice.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 发票信息明细新增/修改 Request VO")
@Data
public class PaymentInvoiceDetailSaveReqVO {

    @Schema(description = "发票信息主键", requiredMode = Schema.RequiredMode.REQUIRED)
    private String invoiceId;

    @Schema(description = "货物、服务数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer goodsNum;

    @Schema(description = "发票号码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String invoiceNo;

    @Schema(description = "项目名称=发票名称（应税劳务服务、货物名称）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String proName;


    @Schema(description = "规格型号", requiredMode = Schema.RequiredMode.REQUIRED)
    private String specMod;

    @Schema(description = "单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "21641")
    private BigDecimal price;

    @Schema(description = "税率", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal taxRate;

    @Schema(description = "税额", requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal taxAmt;

    @Schema(description = "价税合计金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "8605")
    private BigDecimal taxPrice;

    @Schema(description = "单位", requiredMode = Schema.RequiredMode.REQUIRED)
    private String unit;
}
