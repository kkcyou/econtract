package com.yaoan.module.econtract.api.gcy.buyplan;

import com.yaoan.module.econtract.api.contract.dto.mongolia.EncryptResponseDto;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.EncryptDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "${feign.client.shucai.test.url:https://test.gcycloud.cn}", name = "org")
public interface OrgApi {
    /**
     * 一般项目采购-备案
     */
    @PostMapping(value = "/psp-data-exchange/v70/gpx/contract/setContract")
    EncryptResponseDto gpxSetContract(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO reqDTO);

    /**
     * 批量集中采购-备案
     */
    @PostMapping(value = "/psp-data-exchange/v70/gpxb/contract/setContract")
    EncryptResponseDto gpxbSetContract(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO reqDTO);

    /**
     * 查询备案状态
     */
    @PostMapping(value = "/psp-data-exchange/v70/gpx/contract/getContractArchiveState")
    EncryptResponseDto getContractArchiveState(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO reqDTO);

    /**
     * 采购合同撤销
     */
    @PostMapping(value = "/psp-data-exchange/v70/gpx/contract/deleteContract")
    EncryptResponseDto deleteContract(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO reqDTO);
}
