package com.yaoan.module.econtract.controller.admin.aop.service;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.yaoan.module.econtract.api.contract.GPFAOpenApi;
import com.yaoan.module.econtract.api.contract.OpenApi;
import com.yaoan.module.econtract.api.contract.OpenJDApi;
import com.yaoan.module.econtract.api.contract.OpenZBJApi;
import com.yaoan.module.econtract.api.contract.dto.*;
import com.yaoan.module.econtract.api.contract.dto.mongolia.EncryptResponseDto;
import com.yaoan.module.econtract.api.contract.dto.mongolia.MedicalResponseDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.MedicalApi;
import com.yaoan.module.econtract.api.gcy.buyplan.OrgApi;
import com.yaoan.module.econtract.api.gcy.buyplan.SuperVisionApi;
import com.yaoan.module.econtract.api.gcy.buyplan.UniversityApi;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.ContractArchiveStateDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.ContractMVO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.EncryptDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.TokenRequestDTO;
import com.yaoan.module.econtract.controller.admin.aop.anno.MyOperateLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 服务实现类
 *
 * @author doujl
 * @since 2023-07-24
 */
@Service
public class OutOpenApiService {
    @Autowired
    private OpenJDApi openJDApi;
    @Autowired
    private OpenZBJApi openZBJApi;
    @Autowired
    private SuperVisionApi superVisionApi;
    @Autowired
    private UniversityApi universityApi;
    @Autowired
    private OrgApi orgApi;
    @Autowired
    private MedicalApi medicalApi;
    @Autowired
    private OpenApi openApi;
    @Autowired
    private GPFAOpenApi gpfaOpenApi;
    @MyOperateLog(name = "获取京东平台token",module = "黑龙江合同",url = "/sysapi/auth/accessToken" )
    @PostMapping(value = "/sysapi/auth/accessToken")
    public JDRestResponseDTO getToken(JDTokenRepDTO jdTokenRepDTO) {
        return openJDApi.getToken(jdTokenRepDTO );

    }
    @MyOperateLog(name = "推送合同数据到京东",module = "黑龙江合同",url = "/sysapi/order/contractFillNotice" )
    @PostMapping(value = "/sysapi/order/contractFillNotice")
    public JDRestResponseDTO contractFillNotice(JDPushRepDTO jdPushRepDTO) {
        return openJDApi.contractFillNotice(jdPushRepDTO );
    }
    @MyOperateLog(name = "推送合同数据到京东",module = "黑龙江合同",url = "/sysapi/order/contractFillNotice" )
    @PostMapping(value = "/sysapi/order/contractFillNotice")
    public String contractFillNoticeV2(String json,  String url ) {
        String result = HttpUtil.post(url, json);
        System.out.println("result:"+result);
        return result;
    }
    @MyOperateLog(name = "推送合同数据到服务超市",module = "黑龙江合同",url = "/api/hljgcy/contract/syncState" )
    @PostMapping(value = "/api/hljgcy/contract/syncState")
    public RestResponseDTO syncState(ZBJPushRespDTO zbjPushRespDTO) {
        return openZBJApi.syncState(zbjPushRespDTO );
    }
    @MyOperateLog(name = "推送合同数据到监管备案",module = "黑龙江合同",url = "/apibus/v70/gpmall/contract/setContract" )
//    @PostMapping(value = "/gateway/gp-data-exchange/v70/gpmall/contract/setContract")
    @PostMapping(value = "/apibus/v70/gpmall/contract/setContract")
    public  EncryptResponseDto setContract( String accessToken,  EncryptDTO reqDTO) {
        return superVisionApi.setContract(accessToken, reqDTO);
    }

    /**
     * 融通平台备案接口（请求头传递access_token，orgGuid，regionCode,reqDTO）
     * @param accessToken
     * @param orgGuid
     * @param regionCode
     * @param reqDTO
     * @return
     */
    @MyOperateLog(name = "推送合同数据到监管备案v2",module = "黑龙江合同",url = "/apibus/v70/gpmall/contract/setContract" )
    @PostMapping(value = "/apibus/v70/gpmall/contract/setContract")
    public  EncryptResponseDto setContractV2(String accessToken,String orgGuid, String regionCode, EncryptDTO reqDTO){
        return superVisionApi.setContractV2(accessToken,orgGuid, regionCode,reqDTO);
    }

    /**
     * 一般项目采购-备案
     */
    @MyOperateLog(name = "一般项目采购-备案",module = "黑龙江合同",url = "/gateway/gp-data-exchange/v70/gpx/contract/setContract" )
    @PostMapping(value = "/gateway/gp-data-exchange/v70/gpx/contract/setContract")
    public  EncryptResponseDto gpxSetContract( String accessToken,  EncryptDTO reqDTO) {
        return superVisionApi.gpxSetContract(accessToken, reqDTO);
    }

    /**
     * 批量集中采购-备案
     */
    @MyOperateLog(name = "批量集中采购-备案",module = "黑龙江合同",url = "/gateway/gp-data-exchange/v70/gpxb/contract/setContract" )
    @PostMapping(value = "/gateway/gp-data-exchange/v70/gpxb/contract/setContract")
    public  EncryptResponseDto gpxbSetContract( String accessToken,  EncryptDTO reqDTO) {
        return superVisionApi.gpxbSetContract(accessToken, reqDTO);
    }

    /**
     * 高校一般项目采购-备案
     */
    @MyOperateLog(name = "高校一般项目采购-备案",module = "黑龙江合同",url = "/gateway/gp-data-exchange/v70/gpx/contract/university/setContract" )
    @PostMapping(value = "/gateway/gp-data-exchange/v70/gpx/contract/university/setContract")
    public  EncryptResponseDto gpxUniversitySetContract( String accessToken,  EncryptDTO reqDTO) {
        return universityApi.gpxSetContract(accessToken, reqDTO);
    }

    /**
     * 高校批量集中采购-备案
     */
    @MyOperateLog(name = "高校批量集中采购-备案",module = "黑龙江合同",url = "/gateway/gp-data-exchange/v70/gpxb/contract/university/setContract" )
    @PostMapping(value = "/gateway/gp-data-exchange/v70/gpxb/contract/university/setContract")
    public  EncryptResponseDto gpxbUniversitySetContract( String accessToken,  EncryptDTO reqDTO) {
        return universityApi.gpxbSetContract(accessToken, reqDTO);
    }

    /**
     * 采购服务平台一般项目采购-备案
     */
    @MyOperateLog(name = "采购服务平台一般项目采购-备案",module = "黑龙江合同",url = "/gateway/gp-data-exchange/v70/gpx/contract/org/setContract" )
    @PostMapping(value = "/gateway/gp-data-exchange/v70/gpx/contract/org/setContract")
    public  EncryptResponseDto gpxOrgSetContract( String accessToken,  EncryptDTO reqDTO) {
        return orgApi.gpxSetContract(accessToken, reqDTO);
    }

    /**
     * 采购服务平台批量集中采购-备案
     */
    @MyOperateLog(name = "采购服务平台批量集中采购-备案",module = "黑龙江合同",url = "/gateway/gp-data-exchange/v70/gpxb/contract/org/setContract" )
    @PostMapping(value = "/gateway/gp-data-exchange/v70/gpxb/contract/org/setContract")
    public  EncryptResponseDto gpxbOrgSetContract( String accessToken,  EncryptDTO reqDTO) {
        return orgApi.gpxbSetContract(accessToken, reqDTO);
    }
    /**
     * 医疗获取token
     */
    @MyOperateLog(name = "获取医疗token",module = "黑龙江合同",url = "/gateway/gp-data-exchange/v70/gpx/contract/getToken" )
    @PostMapping(value = "/gateway/gp-data-exchange/v70/gpx/contract/getToken")
    public String getToken(TokenRequestDTO request) {
        return medicalApi.getToken(request);
    }

    /**
     * 医疗备案
     */
    @MyOperateLog(name = "医疗备案",module = "黑龙江合同",url = "/gateway/gp-data-exchange/v70/gpx/contract/setContract" )
    @PostMapping(value = "/gateway/gp-data-exchange/v70/gpx/contract/setContract")
    public  String setMedicalContract( String accessToken,  ContractMVO reqDTO) {
        return medicalApi.setMedicalContract(accessToken, reqDTO);
    }

    /**
     * 医疗查询合同备案状态
     */
    @MyOperateLog(name = "医疗查询合同备案状态",module = "黑龙江合同",url = "/gateway/gp-data-exchange/v70/gpx/contract/getContractArchiveState" )
    @PostMapping(value = "/gateway/gp-data-exchange/v70/gpx/contract/getContractArchiveState")
    public MedicalResponseDTO getContractArchiveStateV2(String accessToken, ContractArchiveStateDTO contractReqDTO) {
        return medicalApi.getContractArchiveStateV2(accessToken, contractReqDTO);
    }


    /**
     * 采购合同备案状态获取
     */
    @MyOperateLog(name = "获取采购合同备案状态",module = "黑龙江合同",url = "/gateway/gp-data-exchange/v70/gpx/contract/getContractArchiveState" )
    @PostMapping(value = "/gateway/gp-data-exchange/v70/gpx/contract/getContractArchiveState")
    public  EncryptResponseDto getContractArchiveState( String accessToken,  EncryptDTO reqDTO) {
        return orgApi.getContractArchiveState(accessToken, reqDTO);
    }
    /**
     * 服务超市，京东卖场备案状态获取
     */
    @MyOperateLog(name = "服务超市，京东卖场备案状态获取",module = "黑龙江合同",url = "/apibus/v70/gpx/contract/getContractArchiveState" )
    @PostMapping(value = "/apibus/v70/gpx/contract/getContractArchiveState")
    public  EncryptResponseDto getContractArchiveStateV3( String accessToken,  EncryptDTO reqDTO) {
        return superVisionApi.getContractArchiveState(accessToken, reqDTO);
    }
    /**
     * 融通平台备案状态获取
     */
    @MyOperateLog(name = "融通平台备案状态获取",module = "黑龙江合同",url = "/apibus/v70/gpx/contract/getContractArchiveState" )
    @PostMapping(value = "/apibus/v70/gpx/contract/getContractArchiveState")
    public  EncryptResponseDto getContractArchiveStateV4( String accessToken,String orgGuid, String regionCode,  EncryptDTO reqDTO) {
        String archiveState = superVisionApi.getContractArchiveStateV4(accessToken, orgGuid, regionCode, reqDTO);
        return JSONObject.parseObject(archiveState, EncryptResponseDto.class);
    }


    /**
     * 文件上传
     */
    @MyOperateLog(name = "上传文件到监管",module = "黑龙江合同",url = "/apibus/v70/gpx/contract/uploadContractAttachment" )
    @PostMapping(value = "apibus/v70/gpx/contract/uploadContractAttachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public  EncryptResponseDto uploaderFile( String accessToken,  MultipartFile file) {
        return superVisionApi.uploaderFile(accessToken, file);
    }
    /**
     * 2024-07-15 新增    融通平台上传附件接口（请求头传递access_token，orgGuid，regionCode，file）
     * @param accessToken
     * @return
     */
    @MyOperateLog(name = "融通平台上传附件接口",module = "黑龙江合同",url = "/apibus/v70/file/uploaderFile" )
    @PostMapping(value = "/apibus/v70/file/uploaderFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public  EncryptResponseDto uploaderFileV2( String accessToken, String orgGuid, String regionCode, MultipartFile file) {
        return superVisionApi.uploaderFileV2(accessToken,orgGuid,regionCode, file);
    }


    /**
     * 文件上传
     */
    @MyOperateLog(name = "上传文件到监管",module = "黑龙江合同",url = "/gateway/gp-data-exchange/file/uploaderFile" )
    @PostMapping(value = "/gateway/gp-data-exchange/file/uploaderFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public  EncryptResponseDto uploaderFileV1( String accessToken,  MultipartFile file) {
        return superVisionApi.uploaderFileV1(accessToken, file);
    }
    /**
     * 采购合同撤销
     */
    @MyOperateLog(name = "采购合同撤销",module = "黑龙江合同",url = "/apibus/v70/gpx/contract/deleteContract" )
    @PostMapping(value = "/apibus/v70/gpx/contract/deleteContract")
    public  EncryptResponseDto deleteContract( String accessToken,EncryptDTO reqDTO) {
        return superVisionApi.deleteContract(accessToken,reqDTO);
    }
    /**
     * 2024-07-15 新增    融通平台采购合同撤销（请求头传递access_token，orgGuid，regionCode,reqDTO）
     */
    @MyOperateLog(name = "融通平台采购合同撤销",module = "黑龙江合同",url = "/apibus/v70/gpx/contract/deleteContract" )
    @PostMapping(value = "/apibus/v70/gpx/contract/deleteContract")
    public  EncryptResponseDto deleteContractV3( String accessToken, String orgGuid, String regionCode,EncryptDTO reqDTO) {
        return superVisionApi.deleteContractV3(accessToken, orgGuid,regionCode,reqDTO);
    }


    @MyOperateLog(name = "推送合同信息至框采进行保存",module = "黑龙江合同",url = "/gpfa-bpoc/openApi/yaContract/v1/receiveContractInfoList" )
    @PostMapping(value = "/gpfa-bpoc/openApi/yaContract/v1/receiveContractInfoList")
    public  RestResponseDTO<List<String>> receiveContractInfoList(String token, List<Object> contractInfoDTOs) {
        return gpfaOpenApi.receiveContractInfoList(token, contractInfoDTOs);
    }
    @MyOperateLog(name = "推送合同信息至定点进行保存",module = "黑龙江合同",url = "/gpmall-gpem-interface/openApi/contract/v1/receiveContractInfoList" )
    @PostMapping(value = "/gpmall-gpem-interface/openApi/contract/v1/receiveContractInfoList")
    public  RestResponseDTO<List<String>> receiveContractInfoListD(String token, List<Object> contractInfoDTOs) {
        return openApi.receiveContractInfoList(token, contractInfoDTOs);
    }
}
