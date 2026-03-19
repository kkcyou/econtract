package com.yaoan.module.econtract.controller.admin.controller.admin.contractInvoiceManage.vo;

import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;


@Schema(description = "管理后台 - 发票分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractInvoiceManagePageReqVO extends CommonBpmAutoPageReqVO {

    @Schema(description = "发票编码")
    private String code;

    @Schema(description = "发票抬头")
    private String title;

    /**
     * 合同id
     */
    private String contractId;
    /**
     * 合同名称
     */
    private String contractName;
    /**
     * 计划id
     */
    private String planId;
    
    private Integer status;
    
    @Schema(description = "票款方式 先款后票0 先票后款1", example = "1")
    private Integer amountType;

    @Schema(description = "收款说明", example = "你猜")
    private String remark;

    @Schema(description = "开票单位")
    private String invoiceCompany;

    @Schema(description = "发票类型 0电子发票 1普通发票", example = "1")
    private Integer invoiceType;

    @Schema(description = "开票金额")
    private BigDecimal invoiceAmont;

    @Schema(description = "币种 默认人民币 rmb", example = "2")
    private String currencyType;

    @Schema(description = "开票时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] invoiceDate;

    @Schema(description = "收款日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDate payDate;
    @Schema(description = "发票抬头")
    private String invoiceTitile;
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

    @Schema(description = "发票类型 0邮寄 1专人送达", example = "2")
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
     * 审批状态 全部 0 已审批1 未审批2
     */
    private Integer result;
    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}