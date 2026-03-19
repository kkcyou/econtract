package com.yaoan.module.econtract.api.contract;

import com.alibaba.fastjson.JSONObject;
import com.yaoan.module.econtract.api.contract.dto.GetTokenDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient(url ="${feign.client.url2}",name = "getToken")
public interface LegalismApi {

    @GetMapping(value = "/correction/getToken/notValidLogin")
    GetTokenDTO getToken(@RequestParam("appId") String appId, @RequestParam("tokenId") String tokenId);


    @PostMapping(value = "/correction/checkResult/getErrorInfos/notValidLogin")
    GetTokenDTO getErrorInfos(@RequestBody JSONObject requestBody);

}
