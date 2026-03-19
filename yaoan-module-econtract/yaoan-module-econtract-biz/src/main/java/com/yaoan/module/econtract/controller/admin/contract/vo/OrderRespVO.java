package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/14 19:49
 */
@Data
public class OrderRespVO {
    /**
     * id主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 订单编码
     */
    private String orderGuid;

    /**
     * 订单编号
     */
    private String orderCode;

    /**
     * 供应商id
     */
    private String supplierGuid;

    /**
     * 供应商名称
     */
    private String supplierName;


}
