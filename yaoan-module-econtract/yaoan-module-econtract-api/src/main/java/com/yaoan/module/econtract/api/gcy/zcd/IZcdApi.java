package com.yaoan.module.econtract.api.gcy.zcd;

import com.yaoan.module.econtract.api.gcy.zcd.dto.ResponseDTO;
import com.yaoan.module.econtract.api.gcy.zcd.dto.ZcdCommonDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 对接数采金融服务
 *
 * @author doujiale
 */
@FeignClient(url = "${feign.client.shucai.test.url:https://test.gcycloud.cn}", name = "zcd")
public interface IZcdApi {

    @PostMapping(value = "/zcdservice/rest/v1/bidinfo/condition/query/gettoken")
    String gettoken(@RequestParam("grant_type") String grantType,
                      @RequestParam(value = "client_id", required = false) String clientId,
                      @RequestParam(value = "client_secret", required = false) String clientSecret,
                      @RequestParam(value = "response_type", required = false) String responseType);

    @PostMapping(value = "zcdservice/rest/v1/bidinfo/condition/query/selecBankAccount")
    ResponseDTO selectBankAccount(@RequestBody ZcdCommonDTO zcdCommonDTO);

//    @PostMapping(value = "zcdservice/rest/v1/bidinfo/condition/query/selecBankAccount")
//    String selectBankAccount(@RequestBody ZcdCommonDTO zcdCommonDTO);



}
