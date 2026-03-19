package com.yaoan.module.econtract.controller.admin.payment.invoice.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Schema(description = "管理后台 - 发票信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PaymentInvoiceRespVO {

    @Schema(description = "发票信息主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "27069")
    @ExcelProperty("发票信息主键")
    private String id;

    @Schema(description = "发票代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("发票代码")
    private String invoiceCode;

    @Schema(description = "发票号码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("发票号码")
    private String invoiceNo;

    @Schema(description = "类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("类型名称")
    private String invoiceTypeName;

    @Schema(description = "类型编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("类型编码")
    private String invoiceTypeCode;

    @Schema(description = "开票日期")
    @ExcelProperty("开票日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String billDate;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "纳税人名称(开票人)", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("纳税人名称(开票人)")
    private String otaxPayName;

    @Schema(description = "纳税人识别号（开票人）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("纳税人识别号（开票人）")
    private String otaxPayCode;

    @Schema(description = "开户银行（开票人）", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("开户银行（开票人）")
    private String otaxPayBankName;

    @Schema(description = "银行账号（开票人）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("银行账号（开票人）")
    private String otaxPayBankNo;

    @Schema(description = "地址（开票人）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("地址（开票人）")
    private String otaxPayAddr;

    @Schema(description = "电话（开票人）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("电话（开票人）")
    private String otaxPayTel;

    @Schema(description = "纳税人名称(付款方)", requiredMode = Schema.RequiredMode.REQUIRED, example = "芋艿")
    @ExcelProperty("纳税人名称(付款方)")
    private String ptaxPayName;

    @Schema(description = "纳税人识别号（付款方）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("纳税人识别号（付款方）")
    private String ptaxPayCode;

    @Schema(description = "开户银行（付款方）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("开户银行（付款方）")
    private String ptaxPayBankName;

    @Schema(description = "银行账号（付款方）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("银行账号（付款方）")
    private String ptaxPayBankNo;

    @Schema(description = "地址（付款方）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("地址（付款方）")
    private String ptaxPayAddr;

    @Schema(description = "电话（付款方）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("电话（付款方）")
    private String ptaxPayTel;

    @Schema(description = "支付编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "16574")
    @ExcelProperty("支付编号")
    private String  payId;

    private List<PaymentInvoiceDetailRespVO> invoiceDetailList;

//    @Schema(description = "创建时间")
//    @ExcelProperty("创建时间")
//    private LocalDateTime createTime;
//
//    @Schema(description = "更新人")
//    @ExcelProperty("更新人")
//    private String updater;
//
//    @Schema(description = "创建人")
//    @ExcelProperty("创建人")
//    private String creator;

}