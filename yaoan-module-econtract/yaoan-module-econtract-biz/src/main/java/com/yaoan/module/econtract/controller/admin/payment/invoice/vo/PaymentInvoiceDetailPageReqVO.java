package com.yaoan.module.econtract.controller.admin.payment.invoice.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 发票信息明细分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PaymentInvoiceDetailPageReqVO extends PageParam {

    @Schema(description = "发票信息主键")
    private String invoiceId;

    @Schema(description = "货物、服务数量", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer goodsNum;

    @Schema(description = "发票号码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "发票号码不能为空")
    private String invoiceNo;

    @Schema(description = "项目名称=发票名称（应税劳务服务、货物名称）")
    private String proName;

    @Schema(description = "项目编号")
    private String proCode;

    @Schema(description = "规格型号")
    private String specMod;

    @Schema(description = "单价", example = "21641")
    private BigDecimal price;

    @Schema(description = "税率")
    private BigDecimal taxRate;

    @Schema(description = "税额")
    private BigDecimal taxAmt;

    @Schema(description = "税价", example = "8605")
    private BigDecimal taxPrice;

    @Schema(description = "单位", requiredMode = Schema.RequiredMode.REQUIRED)
    private String unit;
}