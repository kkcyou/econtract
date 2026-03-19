package com.yaoan.module.econtract.api.purchasing;

import com.yaoan.module.econtract.api.purchasing.dto.ReqIdsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 虚拟系统的三个api
 * @author Hongcai
 */
@FeignClient(url ="${feign.client.observation.url}",name = "yaoan-observation")
public interface IProjectPurchasingApi {

    @PostMapping( value = "/purchasing/ids")
    String queryPurchasingByIds(@RequestBody ReqIdsDTO reqIdsDTO);
    @PostMapping( value = "/econtract/framework/ids")
    String queryFrameworkByIds(@RequestBody ReqIdsDTO reqIdsDTO);
    @PostMapping( value = "/econtract/elect/ids")
    String queryElectronicsStoreByIds(@RequestBody ReqIdsDTO reqIdsDTO);
}
