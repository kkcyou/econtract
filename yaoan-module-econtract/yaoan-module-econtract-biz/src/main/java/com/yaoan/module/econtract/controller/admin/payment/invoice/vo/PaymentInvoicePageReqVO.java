package com.yaoan.module.econtract.controller.admin.payment.invoice.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 发票信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PaymentInvoicePageReqVO extends PageParam {

    @Schema(description = "发票代码")
    private String invoiceCode;

    @Schema(description = "发票号码")
    private String invoiceNo;

    @Schema(description = "类型名称")
    private String invoiceTypeName;

    @Schema(description = "类型编码")
    private String invoiceTypeCode;

    @Schema(description = "开票日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate billDate;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "纳税人名称(开票人)")
    private String otaxPayName;

    @Schema(description = "纳税人识别号（开票人）")
    private String otaxPayCode;

    @Schema(description = "开户银行（开票人）")
    private String otaxPayBankName;

    @Schema(description = "银行账号（开票人）")
    private String otaxPayBankNo;

    @Schema(description = "地址（开票人）")
    private String otaxPayAddr;

    @Schema(description = "电话（开票人）")
    private String otaxPayTel;

    @Schema(description = "纳税人名称(付款方)")
    private String ptaxPayName;

    @Schema(description = "纳税人识别号（付款方）")
    private String ptaxPayCode;

    @Schema(description = "开户银行（付款方）")
    private String ptaxPayBankName;

    @Schema(description = "银行账号（付款方）")
    private String ptaxPayBankNo;

    @Schema(description = "地址（付款方）")
    private String ptaxPayAddr;

    @Schema(description = "电话（付款方）")
    private String ptaxPayTel;

    @Schema(description = "支付编号")
    private String payId;


    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "更新人")
    private String updater;

    @Schema(description = "创建人")
    private String creator;

}