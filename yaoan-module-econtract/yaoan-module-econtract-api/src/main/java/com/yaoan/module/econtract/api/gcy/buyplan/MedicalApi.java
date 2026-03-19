package com.yaoan.module.econtract.api.gcy.buyplan;

import com.yaoan.module.econtract.api.contract.dto.mongolia.EncryptResponseDto;
import com.yaoan.module.econtract.api.contract.dto.mongolia.MedicalResponseDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.ContractArchiveStateDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.ContractMVO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.DeleteContractRequestDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.TokenRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(url = "${feign.client.medical.dev.url}", name = "medical")
public interface MedicalApi {
    @PostMapping(value = "/api/admin/api/authToken/username")
    String getToken(@RequestBody TokenRequestDTO request);

    // 合同备案
    @PostMapping(value = "/api/tradePlatform/V20/contract/setContract")
    String setMedicalContract(@RequestHeader("Authorization") String accessToken, @RequestBody ContractMVO reqDTO);

    // 获取合同备案状态
    @PostMapping(value = "/api/tradePlatform/V20/contract/getContractArchiveState")
    MedicalResponseDTO getContractArchiveStateV2(@RequestHeader("Authorization") String accessToken, @RequestBody ContractArchiveStateDTO contractReqDTO);


    // 删除合同
    @PostMapping(value = "/api/tradePlatform/V20/contract/deleteContract")
    EncryptResponseDto deleteContract(@RequestHeader("Authorization") String accessToken, @RequestBody DeleteContractRequestDTO requestDTO);
}
