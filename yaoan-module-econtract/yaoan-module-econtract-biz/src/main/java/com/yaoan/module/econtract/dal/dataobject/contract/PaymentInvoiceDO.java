package com.yaoan.module.econtract.dal.dataobject.contract;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author 10113
 */
@TableName("ecms_payment_invoice")
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentInvoiceDO extends DeptBaseDO implements Serializable {

    /**
     * 发票信息主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 发票代码
     */
    private String invoiceCode;
    /**
     * 发票号码
     */
    private String invoiceNo;
    /**
     * 类型名称
     */
    private String invoiceTypeName;
    /**
     * 类型编码
     */
    private String invoiceTypeCode;
    /**
     * 开票日期
     */
    private LocalDate billDate;
    /**
     * 备注
     */
    private String remark;
    /**
     * 纳税人名称(开票人)
     */
    private String otaxPayName;
    /**
     * 纳税人识别号（开票人）
     */
    private String otaxPayCode;
    /**
     * 开户银行（开票人）
     */
    private String otaxPayBankName;
    /**
     * 银行账号（开票人）
     */
    private String otaxPayBankNo;
    /**
     * 地址（开票人）
     */
    private String otaxPayAddr;
    /**
     * 电话（开票人）
     */
    private String otaxPayTel;
    /**
     * 纳税人名称(付款方)
     */
    private String ptaxPayName;
    /**
     * 纳税人识别号（付款方）
     */
    private String ptaxPayCode;
    /**
     * 开户银行（付款方）
     */
    private String ptaxPayBankName;
    /**
     * 银行账号（付款方）
     */
    private String ptaxPayBankNo;
    /**
     * 地址（付款方）
     */
    private String ptaxPayAddr;
    /**
     * 电话（付款方）
     */
    private String ptaxPayTel;
    /**
     * 支付编号
     */
    private String  payId;


}