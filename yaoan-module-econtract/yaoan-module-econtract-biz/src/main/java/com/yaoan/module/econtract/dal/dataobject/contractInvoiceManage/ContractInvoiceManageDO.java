package com.yaoan.module.econtract.dal.dataobject.contractInvoiceManage;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.module.econtract.enums.InvoiceAmountTypeEnums;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 发票 DO
 *
 * @author lls
 */
@TableName("ecms_contract_invoice_manage")
@KeySequence("ecms_contract_invoice_manage_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractInvoiceManageDO extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = 1901015870718603558L;
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 发票编码
     */
    private String code;
    /**
     * 发票抬头
     */
    private String title;

    /**
     * 合同id
     */
    private String contractId;
    /**
     * 计划id
     */
    private String planId;

    private Integer status;

    /**
     * 票款方式 先款后票0 先票后款1
     * {@link InvoiceAmountTypeEnums}
     */
    private Integer amountType;
    /**
     * 收款说明
     */
    private String remark;
    /**
     * 开票单位
     */
    private String invoiceCompany;
    /**
     * 发票类型 0电子发票 1普通发票
     */
    private Integer invoiceType;
    /**
     * 开票金额
     */
    private BigDecimal invoiceAmont;
    /**
     * 币种 默认人民币 rmb
     */
    private String currencyType;
    /**
     * 开票时间
     */
    private LocalDateTime invoiceDate;
    /**
     * 收款日期
     */
    private LocalDate payDate;
    /**
     * 纳税人识别号
     */
    private String invoiceTitle;
    /**
     * 纳税人识别号
     */
    private String buyerNumber;
    /**
     * 相对方手机号
     */
    private String buyerTel;
    /**
     * 相对方地址
     */
    private String buyerAddress;
    /**
     * 开户行
     */
    private String bankName;
    /**
     * 银行账户
     */
    private String bankAccount;
    /**
     * 发票类型 0邮寄 1专人送达
     */
    private Integer sendType;
    /**
     * 发票收件人/专送人员
     */
    private String sendPerson;
    /**
     * 联系电话
     */
    private String sendTel;
    /**
     * 邮寄地址
     */
    private String sendAddress;
    /**
     * 流程实例的编号
     */
    private String processInstanceId;

}