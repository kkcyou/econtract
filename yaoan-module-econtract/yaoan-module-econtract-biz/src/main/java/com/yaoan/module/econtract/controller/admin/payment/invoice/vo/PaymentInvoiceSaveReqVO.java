package com.yaoan.module.econtract.controller.admin.payment.invoice.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "管理后台 - 发票信息新增/修改 Request VO")
@Data
public class PaymentInvoiceSaveReqVO {

    private String id;

    @Schema(description = "发票代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "发票代码不能为空")
    private String invoiceCode;

    @Schema(description = "发票号码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "发票号码不能为空")
    private String invoiceNo;

    @Schema(description = "类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "类型名称不能为空")
    private String invoiceTypeName;

    @Schema(description = "类型编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "类型编码不能为空")
    private String invoiceTypeCode;

    @Schema(description = "开票日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate billDate;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "纳税人名称(开票人)", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "纳税人名称(开票人)不能为空")
    private String otaxPayName;

    @Schema(description = "纳税人识别号（开票人）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "纳税人识别号（开票人）不能为空")
    private String otaxPayCode;

    @Schema(description = "开户银行（开票人）", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "开户银行（开票人）不能为空")
    private String otaxPayBankName;

    @Schema(description = "银行账号（开票人）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "银行账号（开票人）不能为空")
    private String otaxPayBankNo;

    @Schema(description = "地址（开票人）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "地址（开票人）不能为空")
    private String otaxPayAddr;

    @Schema(description = "电话（开票人）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "电话（开票人）不能为空")
    private String otaxPayTel;

    @Schema(description = "纳税人名称(付款方)", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @NotEmpty(message = "纳税人名称(付款方)不能为空")
    private String ptaxPayName;

    @Schema(description = "纳税人识别号（付款方）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "纳税人识别号（付款方）不能为空")
    private String ptaxPayCode;

    @Schema(description = "开户银行（付款方）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "开户银行（付款方）不能为空")
    private String ptaxPayBankName;

    @Schema(description = "银行账号（付款方）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "银行账号（付款方）不能为空")
    private String ptaxPayBankNo;

    @Schema(description = "地址（付款方）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "地址（付款方）不能为空")
    private String ptaxPayAddr;

    @Schema(description = "电话（付款方）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "电话（付款方）不能为空")
    private String ptaxPayTel;

    @Schema(description = "支付编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "16574")
    @NotEmpty(message = "支付编号不能为空")
    private String  payId;

    // 发票明细列表
    private List<PaymentInvoiceDetailSaveReqVO> invoiceDetailList;
}
