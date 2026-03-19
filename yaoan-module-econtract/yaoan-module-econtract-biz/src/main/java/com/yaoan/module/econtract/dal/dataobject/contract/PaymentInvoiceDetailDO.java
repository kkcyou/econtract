package com.yaoan.module.econtract.dal.dataobject.contract;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 发票信息明细 DO
 *
 * @author 芋道源码
 */
@TableName("ecms_payment_invoice_detail")
@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentInvoiceDetailDO  extends DeptBaseDO implements Serializable {

    /**
     * 发票明细主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 发票信息主键
     */
    private String invoiceId;

    /**
     * 货物数量
     */
    private Integer goodsNum;

    /**
     * 发票号码
     */
    private String invoiceNo;

    /**
     * 项目名称=发票名称（服务、货物）
     */
    private String proName;
//    /**
//     * 项目编号
//     */
//    private String proCode;
    /**
     * 规格型号
     */
    private String specMod;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 税率
     */
    private BigDecimal taxRate;
    /**
     * 税额
     */
    private BigDecimal taxAmt;
    /**
     * 税价
     */
    private BigDecimal taxPrice;

    /**
     * 单位
     */
    private String unit;

}