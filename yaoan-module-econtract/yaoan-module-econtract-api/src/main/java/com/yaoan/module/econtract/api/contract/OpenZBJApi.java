package com.yaoan.module.econtract.api.contract;

import com.yaoan.module.econtract.api.contract.dto.RestResponseDTO;
import com.yaoan.module.econtract.api.contract.dto.ZBJPushRespDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 猪八戒-服务工程超市 API
 *
 * @author ZHC
 */
@FeignClient(url = "${feign.client.zbj.test.url}", name = "zbj")
public interface OpenZBJApi {

    /**
     * 推送合同数据到服务超市
     */
    @PostMapping(value = "/api/hljgcy/contract/syncState")
    RestResponseDTO  syncState( @RequestBody ZBJPushRespDTO zbjPushRespDTO);

}
