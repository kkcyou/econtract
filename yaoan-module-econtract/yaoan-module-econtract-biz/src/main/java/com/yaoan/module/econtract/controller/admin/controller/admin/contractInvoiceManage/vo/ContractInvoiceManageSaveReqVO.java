package com.yaoan.module.econtract.controller.admin.controller.admin.contractInvoiceManage.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "管理后台 - 发票新增/修改 Request VO")
@Data
public class ContractInvoiceManageSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "15915")
    private String id;

    @Schema(description = "发票编码")
    private String code;

    @Schema(description = "发票抬头")
    @NotNull
    private String title;

    /**
     * 合同id
     */
    private String contractId;
    /**
     * 计划id
     */
    private String planId;
    
    @Schema(description = "票款方式 先款后票0 先票后款1", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "票款方式 先款后票0 先票后款1不能为空")
    private Integer amountType;

    @Schema(description = "收款说明", example = "你猜")
    private String remark;

    @Schema(description = "开票单位")
    private String invoiceCompany;

    @Schema(description = "发票类型 0电子发票 1普通发票", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "发票类型 0电子发票 1普通发票不能为空")
    private Integer invoiceType;

    @Schema(description = "开票金额")
    private BigDecimal invoiceAmont;

    @Schema(description = "币种 默认人民币 rmb", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotEmpty(message = "币种 默认人民币 rmb不能为空")
    private String currencyType;

    @Schema(description = "开票时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开票时间不能为空")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;

    @Schema(description = "收款日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date payDate;
    @Schema(description = "发票抬头")
    private String invoiceTitle;
    @Schema(description = "纳税人识别号")
    private String buyerNumber;

    @Schema(description = "相对方手机号")
    private String buyerTel;

    @Schema(description = "相对方地址")
    private String buyerAddress;

    @Schema(description = "开户行", example = "李四")
    private String bankName;

    @Schema(description = "银行账户", example = "25463")
    private String bankAccount;

    @Schema(description = "发票类型 0邮寄 1专人送达", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "发票类型 0邮寄 1专人送达不能为空")
    private Integer sendType;

    @Schema(description = "发票收件人/专送人员")
    private String sendPerson;

    @Schema(description = "联系电话")
    private String sendTel;

    @Schema(description = "邮寄地址")
    private String sendAddress;

    @Schema(description = "流程实例的编号", example = "5550")
    private String processInstanceId;

    /**
     * 期数
     * */
    private Integer sort;

    /**
     * 提交标识
     * 0=不提交
     * 1=提交
     */
    private Integer isSubmit;

}