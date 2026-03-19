package com.yaoan.module.econtract.api.gcy.gpmall;

import com.yaoan.module.econtract.api.gcy.GPMallApi;
import com.yaoan.module.econtract.api.gcy.order.DraftOrderInfo;
import com.yaoan.module.econtract.service.gcy.gpmall.GPMallOrderService;

import javax.annotation.Resource;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/4 11:32
 */
public class GPMallApiImpl implements GPMallApi {
@Resource
private GPMallOrderService gpMallOrderService;


    @Override
    public String pushGPMallOrderInfoToEContract(DraftOrderInfo info) {
        gpMallOrderService.saveOrderInfo(info);
        return "success";
    }
}
