package com.yaoan.module.econtract.api.contract;

import com.yaoan.module.econtract.api.contract.dto.ContractDataDTO;
import com.yaoan.module.econtract.api.contract.dto.RestResponseDTO;
import com.yaoan.module.econtract.api.contract.dto.TreeNodeDTO;
import com.yaoan.module.econtract.api.gcy.order.DraftOrderInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 协议定点API
 *
 * @author ZHC
 * @since 2023-12-04
 */
@FeignClient(url = "${feign.client.shucai.test.url}", name = "gpmall-gpem-interface")
public interface OpenApi {

    /**
     * 获取 起草合同时需要的业务数据
     */
    @GetMapping(value = "/gpmall-gpem-interface/openApi/contract/v1/getContractContentData")
    RestResponseDTO<ContractDataDTO> getContractContentData(@RequestHeader("access_token") String token, @RequestParam("orderGuid") String orderGuid);

    /**
     * 合同修改时根据合同id获取合同相关信息
     */
    @GetMapping(value = "/gpmall-gpem-interface/openApi/contract/v1/getEditContractData")
    RestResponseDTO<ContractDataDTO> getEditContractData(@RequestHeader("access_token") String token, @RequestParam("contractGuid") String contractGuid);

    /**
     * 推送合同信息至卖场进行保存备案
     */
    @PostMapping(value = "/gpmall-gpem-interface/openApi/contract/v1/receiveContractInfoList")
    RestResponseDTO<List<String>> receiveContractInfoList(@RequestHeader("access_token") String token, @RequestBody List<Object> contractInfoDTOs);

    /**
     * 订单信息查询接口
     *
     * @param token
     * @param orderGuid
     * @return
     */
    @GetMapping(value = "/gpmall-gpem-interface/openApi/contract/v1/getOrderInfo")
    RestResponseDTO<DraftOrderInfo> getOrderInfo(@RequestHeader("access_token") String token, @RequestParam("orderGuid") String orderGuid);

    /**
     * 获取区划配置品目数据
     *
     * @param token      平台token
     * @param regionCode 区划code
     * @return @RestResponseDTO
     */
    @GetMapping(value = "/gpmall-gpem-interface/openApi/contract/v1/getGoodsClassTreeByRegionCode")
    RestResponseDTO<List<TreeNodeDTO>> getGoodsClassTreeByRegionCode(@RequestHeader("access_token") String token, @RequestParam("regionCode") String regionCode);

    /**
     * 推送合同数据到协议定点
     *
     * @param token
     * @param kcPushRepDTO
     * @return
     */
    @PostMapping(value = "/gpmall-gpem-interface/openApi/contract/v1/getContractIdAndStatus")
    RestResponseDTO getContractIdAndStatusV2(@RequestHeader("access_token") String token, @RequestBody KCPushRepDTO kcPushRepDTO);

}
