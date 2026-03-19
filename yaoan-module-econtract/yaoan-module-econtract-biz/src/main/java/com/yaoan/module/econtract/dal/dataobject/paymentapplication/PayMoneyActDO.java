package com.yaoan.module.econtract.dal.dataobject.paymentapplication;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 获取监管支付信息实体类
 *
 * @author lml
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract_pay_money_act")
public class PayMoneyActDO extends BaseDO implements Serializable {

    private static final long serialVersionUID = 8703443217406077789L;
    /**
     * 合同履约id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    //合同id
    private String contractGuid;
    //支付申请唯一标识
    private String contractPayInfoGuid;
    //支付申请编号
    private String payCode;
    //付款是否成功 1成功
    private Integer success;
    //本次支付申请金额
    private BigDecimal payMoney;
    //支付申请编号
    private String serialNumber;
    //实际支付金额
    private BigDecimal actMoney;
    //收款单位名称
    private String supplierName;
    // 收款账户名称
    private String payAccountName;
    //收款账户开户银行
    private String payBankName;
    //收款账户银行账号
    private String payBankAccount;
    //支付申请时间
    private LocalDateTime payApplyForTime;
    //实际支付时间
    private LocalDateTime payTime;
    // 数据入库时间
    private LocalDateTime createDate;
    //支付摘要(取消支付/支付错误的原因等)
    private String payEssentials;
}
