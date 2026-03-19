package com.yaoan.module.econtract.api.contract;

import com.yaoan.module.econtract.api.contract.dto.JDPushRepDTO;
import com.yaoan.module.econtract.api.contract.dto.JDRestResponseDTO;
import com.yaoan.module.econtract.api.contract.dto.JDTokenRepDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 猪八戒-服务工程超市 API
 *
 * @author ZHC
 */
@FeignClient(url = "${feign.client.jd.test.url}", name = "jd")
public interface OpenJDApi {

    /**
     * 推送合同数据到京东
     */
    @PostMapping(value = "/sysapi/order/contractFillNotice")
    JDRestResponseDTO contractFillNotice(@RequestBody JDPushRepDTO jdPushRepDTO);
    /**
     * 获取京东平台token
     */
    @PostMapping(value = "/sysapi/auth/accessToken")
    JDRestResponseDTO getToken(@RequestBody JDTokenRepDTO jdTokenRepDTO);


}
