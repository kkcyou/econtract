package com.yaoan.module.econtract.api.contract;

import com.yaoan.module.econtract.api.contract.dto.ContractDataDTO;
import com.yaoan.module.econtract.api.contract.dto.RestResponseDTO;
import com.yaoan.module.econtract.api.gcy.order.DraftOrderInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 框彩平台API
 *
 * @author ZHC
 * @since 2023-12-04
 */
@FeignClient(url = "${feign.client.shucai.test.url}", name = "gpfa-bpoc")
//@FeignClient(url = "${http://112.111.20.89:9980/}", name = "gpfa-bpoc")
public interface GPFAOpenApi {

    /**
     * 获取 起草合同时需要的业务数据
     */
    @GetMapping(value = "/gpfa-bpoc/openApi/yaContract/v1/getContractContentData")
    RestResponseDTO<ContractDataDTO> getContractContentData(@RequestHeader("access_token") String token, @RequestParam("orderGuid") String orderGuid);

    /**
     * 合同修改时根据合同id获取合同相关信息
     */
    @GetMapping(value = "/gpfa-bpoc/openApi/yaContract/v1/getEditContractData")
    RestResponseDTO<ContractDataDTO> getEditContractData(@RequestHeader("access_token") String token, @RequestParam("contractGuid") String contractGuid);

    /**
     * 推送合同信息至卖场进行保存备案
     */
    @PostMapping(value = "/gpfa-bpoc/openApi/yaContract/v1/receiveContractInfoList")
    RestResponseDTO<List<String>> receiveContractInfoList(@RequestHeader("access_token") String token, @RequestBody List<Object> contractInfoDTOs);

    /**
     * 订单信息查询接口
     *
     * @param token
     * @param orderGuid
     * @return
     */
    @GetMapping(value = "/gpfa-bpoc/openApi/yaContract/v1/getOrderInfo")
    RestResponseDTO<DraftOrderInfo> getOrderInfo(@RequestHeader("access_token") String token, @RequestParam("orderGuid") String orderGuid);


}
