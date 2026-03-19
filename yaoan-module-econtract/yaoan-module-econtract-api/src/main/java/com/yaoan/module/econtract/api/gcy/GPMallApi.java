package com.yaoan.module.econtract.api.gcy;

import com.yaoan.module.econtract.api.gcy.order.DraftOrderInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @description: 电子卖场
 * @author: Pele
 * @date: 2023/12/4 11:07
 */
@FeignClient(url ="${feign.client.gpmall.url}",name = "gpmall")
public interface GPMallApi {

    @PostMapping( value = "/econtract/draft/order")
    String pushGPMallOrderInfoToEContract(@RequestBody DraftOrderInfo info);


}
