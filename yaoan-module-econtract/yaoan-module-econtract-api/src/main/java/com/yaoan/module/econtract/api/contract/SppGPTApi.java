package com.yaoan.module.econtract.api.contract;


import com.yaoan.module.econtract.api.contract.dto.SppGPTDetailDTO;
import com.yaoan.module.econtract.api.contract.dto.SppGPTGetTokenDTO;

import com.yaoan.module.econtract.api.contract.dto.SppGPTResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@FeignClient(url ="${feign.client.url3}",name = "SppGPT")
public interface SppGPTApi {
    /**
     * 获取token
     */
    @PostMapping(value = "/auth/v0/token/generate")
    SppGPTResponseDTO generateToken(@RequestBody SppGPTGetTokenDTO dto);

    /**
     * 上传文件
     */
    @PostMapping(value = "/contract/external/v1/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    SppGPTResponseDTO uploadFile(@RequestHeader("Authorization") String accessToken, @RequestParam("busType") String busType, @RequestPart("file") MultipartFile file);

    /**
     * 获取合同解析详情
     */
    @PostMapping(value = "/contract/external/v1/detail")
    SppGPTResponseDTO contractDetail(@RequestHeader("Authorization") String accessToken,@RequestBody SppGPTDetailDTO taskIds);
}
