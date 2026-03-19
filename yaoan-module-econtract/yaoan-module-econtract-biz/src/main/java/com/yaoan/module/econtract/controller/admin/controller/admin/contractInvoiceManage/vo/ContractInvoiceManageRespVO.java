package com.yaoan.module.econtract.controller.admin.controller.admin.contractInvoiceManage.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.contractarchives.vo.AttachmentVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.PaymentPlanRespVO;
import com.yaoan.module.econtract.dal.dataobject.contract.SimpleContractDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Schema(description = "管理后台 - 发票 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ContractInvoiceManageRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "15915")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "发票编码")
    @ExcelProperty("发票编码")
    private String code;

    @Schema(description = "发票抬头")
    @ExcelProperty("发票抬头")
    private String title;

    /**
     * 合同id
     */
    private String contractId;
    private String contractCode;
    private String contractName;
    /**
     * 计划id
     */
    private String planId;
    /**
     * 支付期数
     */
    private Integer sort;

    @Schema(description = "票款方式 先款后票0 先票后款1", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("票款方式 先款后票0 先票后款1")
    private Integer amountType;

    @Schema(description = "收款说明", example = "你猜")
    @ExcelProperty("收款说明")
    private String remark;

    @Schema(description = "开票单位")
    @ExcelProperty("开票单位")
    private String invoiceCompany;

    @Schema(description = "发票类型 0电子发票 1普通发票", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("发票类型 0电子发票 1普通发票")
    private Integer invoiceType;

    @Schema(description = "开票金额")
    @ExcelProperty("开票金额")
    private BigDecimal invoiceAmont;

    @Schema(description = "币种 默认人民币 rmb", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("币种 默认人民币 rmb")
    private String currencyType;

    @Schema(description = "开票时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("开票时间")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date invoiceDate;

    @Schema(description = "收款日期")
    @ExcelProperty("收款日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date payDate;

    @Schema(description = "发票抬头")
    private String invoiceTitle;
    @Schema(description = "纳税人识别号")
    @ExcelProperty("纳税人识别号")
    private String buyerNumber;

    @Schema(description = "相对方手机号")
    @ExcelProperty("相对方手机号")
    private String buyerTel;

    @Schema(description = "相对方地址")
    @ExcelProperty("相对方地址")
    private String buyerAddress;

    @Schema(description = "开户行", example = "李四")
    @ExcelProperty("开户行")
    private String bankName;

    @Schema(description = "银行账户", example = "25463")
    @ExcelProperty("银行账户")
    private String bankAccount;

    @Schema(description = "发票类型 0邮寄 1专人送达", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("发票类型 0邮寄 1专人送达")
    private Integer sendType;

    @Schema(description = "发票收件人/专送人员")
    @ExcelProperty("发票收件人/专送人员")
    private String sendPerson;

    @Schema(description = "联系电话")
    @ExcelProperty("联系电话")
    private String sendTel;

    @Schema(description = "邮寄地址")
    @ExcelProperty("邮寄地址")
    private String sendAddress;

    @Schema(description = "流程实例的编号", example = "5550")
    @ExcelProperty("流程实例的编号")
    private String processInstanceId;

    private String taskId;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "合同信息")
    private List<SimpleContractDO> contractList;

    /**
     * 申请人id
     */
    @Schema(description = "申请人id", requiredMode = Schema.RequiredMode.REQUIRED)
    private String creator;
    /**
     * 申请人名字
     */
    @Schema(description = "申请人名字", requiredMode = Schema.RequiredMode.REQUIRED)
    private String creatorName;

    /**
     * 附件集合
     */
    @Schema(description = "附件集合", example = "30")
    private List<AttachmentVO> attachmentIds;

    /**
     * 关联履约计划
     */
    private List<PaymentPlanRespVO> paymentPlanRespVOList;
}