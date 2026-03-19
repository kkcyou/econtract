package com.yaoan.module.econtract.controller.admin.payment.deferred.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/10/8 17:04
 */
@Data
public class ContractInfoV2RespVO {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同编码
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 签署截止日期
     */
    private Date expirationDate;


    /**
     * 合同类型
     */
    private String contractType;

    /**
     * 合同类型
     */
    private String contractTypeName;


    /**
     * 签署日期
     */
    private Date signDate;

    /**
     * 合同有效期-开始时间
     */
    private Date validity0;

    /**
     * 合同有效期-结束时间
     */
    private Date validity1;

    /**
     * 合同金额
     */
    private Double amount;

    /**
     * 已付金额
     */
    private Double payedAmount;
    /**
     * 相关计划
     */
    private String plans;
}
