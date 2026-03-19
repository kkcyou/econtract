package com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.order;

import com.yaoan.module.econtract.controller.admin.order.vo.GPMallPageRespVO;
import lombok.Data;

import java.util.List;

@Data
public class GroupedDraftOrderInfoVO {

    /**
     * 计划编号-如果此订单没使用计划就无此字段
     */
    private String buyPlanCode;

    /**
     * 计划名称-如果此订单没使用计划就无此字段
     */
    private String buyPlanName;
    private String supplierName;
    /**
     * 采购单位
     */
    private String purchaserOrg;
    private Long count;
    private List<GPMallPageRespVO> list;


}
