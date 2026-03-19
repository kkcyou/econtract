package com.yaoan.module.econtract.dal.dataobject.acceptance;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description: 验收申请
 * @author: Pele
 * @date: 2025/4/22 11:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_acceptance")
public class AcceptanceDO extends DeptBaseDO {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 状态
     * 1=验收中
     * 2=验收通过
     * 5=验收不通过
     */
    private Integer status;

    /**
     * 申请日期
     */
    private Date applyDate;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 计划id
     */
    private String planId;

    /**
     * 验收时间
     */
    private Date acceptanceDate;

    /**
     * 预计结款时间
     */
    private Date expectedPayDate;

    /**
     * 本次结算金额
     */
    private BigDecimal currentPayMoney;

    /**
     * 本次结算比例
     */
    private BigDecimal currentPayRatio;

    /**
     * 关联材料id
     * */
    private String businessId;
    /**
     * 验收备注
     */
    private String remark;

}
