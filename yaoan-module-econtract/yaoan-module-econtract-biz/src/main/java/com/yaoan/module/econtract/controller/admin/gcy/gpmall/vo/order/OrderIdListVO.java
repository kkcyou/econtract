package com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.order;

import lombok.Data;

import java.util.List;

@Data
public class OrderIdListVO {

    /**
     * 订单id集合
     */
    private List<String> idList;

    /**
     * 合同来源
     */
    private String contractFrom;
}
