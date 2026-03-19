package com.yaoan.module.econtract.api.gcy.buyplan;

import com.yaoan.module.econtract.api.contract.dto.mongolia.EncryptResponseDto;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.EncryptDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description:
 * @author: doujiale
 * @date: 2023/11/28 11:45
 */
@FeignClient(url = "${feign.client.shucai.test.url:https://test.gcycloud.cn}", name = "supervision")
public interface SuperVisionApi {

    /**
     * 订单管理:列表查看和搜索
     */
    @PostMapping(value = "/gateway/gp-data-exchange/v70/gpx/buyPlan/getBuyPlan")
    EncryptResponseDto getBuyPlanV2(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO reqDTO);


    @PostMapping(value = "/gateway/gp-data-exchange/v70/gpx/buyPlan/setBuyPlanExecStatus")
    EncryptResponseDto setBuyPlanExecStatus(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO reqDTO);

//    @PostMapping(value = "/gateway/gp-data-exchange/v70/gpx/contract/getContractArchiveState")
    @PostMapping(value = "/apibus/v70/gpx/contract/getContractArchiveState")
    EncryptResponseDto getContractArchiveState(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO reqDTO);

    /**
     * 2024-07-15 新增    融通平台获取备案状态接口（请求头传递access_token，orgGuid，regionCode,reqDTO）
     * @param accessToken
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/apibus/v70/gpx/contract/getContractArchiveState")
    String getContractArchiveStateV4(@RequestHeader("access_token") String accessToken,@RequestHeader("orgGuid") String orgGuid,@RequestHeader("regionCode") String regionCode,@RequestBody EncryptDTO reqDTO);

    @PostMapping(value = "/gateway/gp-data-exchange/v70/gpx/contract/getContractArchiveState")
    EncryptResponseDto getContractArchiveStateV2(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO reqDTO);

//    @PostMapping(value = "/gateway/gp-data-exchange/v70/gpmall/contract/setContract")
            @PostMapping(value = "/apibus/v70/gpmall/contract/setContract")
    EncryptResponseDto setContract(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO reqDTO);

    /**
     * 2024-07-15 新增    融通平台备案接口（请求头传递access_token，orgGuid，regionCode,reqDTO）
     * @param accessToken
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/apibus/v70/gpmall/contract/setContract")
    EncryptResponseDto setContractV2(@RequestHeader("access_token") String accessToken,@RequestHeader("orgGuid") String orgGuid,@RequestHeader("regionCode") String regionCode, @RequestBody EncryptDTO reqDTO);

    /**
     * 一般项目采购-备案
     */
    @PostMapping(value = "/gateway/gp-data-exchange/v70/gpx/contract/setContract")
    EncryptResponseDto gpxSetContract(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO reqDTO);

    /**
     * 批量集中采购-备案
     */
    @PostMapping(value = "/gateway/gp-data-exchange/v70/gpxb/contract/setContract")
    EncryptResponseDto gpxbSetContract(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO reqDTO);

    /**
     * 采购合同撤销
     */
    @PostMapping(value = "/apibus/v70/gpx/contract/deleteContract")
    EncryptResponseDto deleteContract(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO reqDTO);

    /**
     * 2024-07-15 新增    融通平台采购合同撤销（请求头传递access_token，orgGuid，regionCode,reqDTO）
     * @param accessToken
     * @param reqDTO
     * @return
     */
    @PostMapping(value = "/apibus/v70/gpx/contract/deleteContract")
    EncryptResponseDto deleteContractV3(@RequestHeader("access_token") String accessToken, @RequestHeader("orgGuid") String orgGuid,@RequestHeader("regionCode") String regionCode,@RequestBody EncryptDTO reqDTO);

    @PostMapping(value = "/gateway/gp-data-exchange/v70/gpx/contract/deleteContract")
    EncryptResponseDto deleteContractV2(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO reqDTO);
    /**
     *采购目录
     */
    @PostMapping(value = "/gateway/gp-data-exchange/v70/basic/dictionary/getPurCatalog")
    EncryptResponseDto getPurCatalog(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO reqDTO);
    /**
     * 政府购买服务指导性目录
     */
    @PostMapping(value = "/gateway/gp-data-exchange/v70/basic/dictionary/getGovServiceCatalog")
    EncryptResponseDto getGovServiceCatalog(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO reqDTO);

    /**
     * 文件上传
     */
    @PostMapping(value = "/apibus/v70/gpx/contract/uploadContractAttachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    EncryptResponseDto uploaderFile(@RequestHeader("access_token") String accessToken, @RequestPart("file") MultipartFile file);
    /**
     * 2024-07-15 新增    融通平台上传附件接口（请求头传递access_token，orgGuid，regionCode，file）
     * @param accessToken
     * @return
     */
//    @PostMapping(value = "/apibus/gp-data-exchange/file/uploaderFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PostMapping(value = "/apibus/v70/file/uploaderFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    EncryptResponseDto uploaderFileV2(@RequestHeader("access_token") String accessToken,@RequestHeader("orgGuid") String orgGuid,@RequestHeader("regionCode") String regionCode, @RequestPart("file") MultipartFile file);

    /**
     * 文件上传
     */
    @PostMapping(value = "/gateway/gp-data-exchange/file/uploaderFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    EncryptResponseDto uploaderFileV1(@RequestHeader("access_token") String accessToken, @RequestPart("file") MultipartFile file);


    @PostMapping(value = "/gateway/gp-data-exchange/v70/payInfo/getPayMoneyAct")
    EncryptResponseDto getPayMoneyAct(@RequestHeader("access_token") String accessToken, @RequestBody EncryptDTO reqDTO);

}
