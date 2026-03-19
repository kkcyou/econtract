package com.yaoan.module.econtract.service.cx;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.api.changxie.ChangXieApi;
import com.yaoan.module.econtract.api.changxie.dto.CXFileConverterDTO;
import com.yaoan.module.econtract.api.changxie.dto.CXFileConverterRespDTO;
import com.yaoan.module.econtract.api.changxie.dto.add.CXAddReqDTO;
import com.yaoan.module.econtract.api.changxie.dto.add.CXAddRespDTO;
import com.yaoan.module.econtract.api.changxie.dto.add.CXOrder2ContractDTO;
import com.yaoan.module.econtract.api.changxie.dto.cleandraft.CXCleanDraftReqDTO;
import com.yaoan.module.econtract.api.changxie.dto.getcontent.GetDocContentReqDTO;
import com.yaoan.module.econtract.api.changxie.dto.getcontent.GetDocContentRespDTO;
import com.yaoan.module.econtract.api.changxie.dto.getcontent.ResultTxtDTO;
import com.yaoan.module.econtract.api.changxie.dto.insertcontent.InsertDocContentReqDTO;
import com.yaoan.module.econtract.api.changxie.dto.insertcontent.InsertDocContentRespDTO;
import com.yaoan.module.econtract.api.changxie.dto.insertcontent.ResultDocRspDTO;
import com.yaoan.module.econtract.api.changxie.dto.save.CXSaveReqDTO;
import com.yaoan.module.econtract.api.changxie.dto.save.CXSaveRespDTO;
import com.yaoan.module.econtract.api.changxie.dto.watermark.CXWaterMarkReqDTO;
import com.yaoan.module.econtract.api.changxie.dto.watermark.CXWaterMarkRespDTO;
import com.yaoan.module.econtract.controller.admin.cx.vo.CXCallbackReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.CXCallbackRespVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.CXReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.CXRespVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.add.CXAddJsonDTO;
import com.yaoan.module.econtract.controller.admin.cx.vo.add.CXAddReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.cleandraft.CXCleanDraftReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.cleandraft.CXCleanDraftRespVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.converter.CXFileConverterReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.getcontent.GetDocContentReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.insertcontent.InsertDocContentReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.ordercontract.CXCreateContractByOrderReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.save.CXSaveReqVO;
import com.yaoan.module.econtract.controller.admin.cx.vo.watermark.CXWaterMarkReqVO;
import com.yaoan.module.econtract.convert.changxie.ChangXieConverter;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.DraftOrderInfoDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.ProjectDO;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.mysql.model.ModelMapper;
import com.yaoan.module.econtract.dal.mysql.order.*;
import com.yaoan.module.econtract.enums.changxie.CXStatusEnums;
import com.yaoan.module.econtract.enums.changxie.FileTypeEnums;
import com.yaoan.module.econtract.service.contract.ContractService;
import com.yaoan.module.econtract.util.EcontractUtil;
import com.yaoan.module.econtract.util.MinioUtils;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import com.yaoan.module.infra.api.file.dto.FileWpsDTO;
import com.yaoan.module.infra.api.file.dto.FilesDTO;
import com.yaoan.module.infra.enums.FileConfigEnum;
import com.yaoan.module.infra.enums.FileUploadPathEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.*;
import static com.yaoan.module.econtract.enums.StatusConstants.SUFFIX_PDF;
import static com.yaoan.module.econtract.enums.changxie.CXStatusEnums.DISCONNECT;
import static com.yaoan.module.econtract.enums.changxie.CXStatusEnums.DISCONNECT_CX_SERVE_ERROR;
import static com.yaoan.module.econtract.util.EcontractUtil.getSimpleFileSuffix;
import static com.yaoan.module.econtract.util.EcontractUtil.inputStreamToBytes;
import static com.yaoan.module.infra.enums.FileUploadPathEnum.*;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/25 10:19
 */
@Slf4j
@Service
public class ChangXieServiceImpl implements ChangXieService {
    private static final String RESULT_DOCX = "result.docx";

    @Resource
    private FileApi fileApi;
    @Resource
    private MinioUtils minioUtils;
    @Resource
    private ChangXieApi changXieApi;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private GPMallOrderAccessoryMapper gpMallOrderAccessoryMapper;
    @Resource
    private GPMallFileAttachmentMapper gpMallFileAttachmentMapper;
    @Resource
    private GPMallEngineeringProjectMapper gpMallEngineeringProjectMapper;
    @Resource
    private GPMallGoodsMapper gpMallGoodsMapper;
    @Resource
    private GPMallProjectMapper gpMallProjectMapper;
    @Resource
    private GPMallOrderOldMapper gpMallOrderMapper;
    @Resource
    private ContractService contractService;
    @Resource
    private ModelMapper modelMapper;

    private static int getFieldCount(Class<?> clazz) {
        // 获取类中声明的所有字段
        Field[] fields = clazz.getDeclaredFields();
        // 返回字段数量
        return fields.length;
    }

    @Override
    public CXRespVO saveFile(CXReqVO reqVO) {

        return null;
    }

    /**
     * userdata :合同名称
     */
    @Override
    public CXCallbackRespVO singleFileUploads(CXCallbackReqVO reqVO) throws IOException {
        log.info("==========【畅写服务 回调 开始】============");
        CXCallbackRespVO result = new CXCallbackRespVO();
        CXStatusEnums statusEnums = CXStatusEnums.getInstance(reqVO.getStatus());
        if (ObjectUtil.isNotNull(statusEnums)) {
            switch (statusEnums) {
                //正常关闭
                case DISCONNECT:
                case FORCE_SAVE:
                    String cxUrl = reqVO.getUrl();
                    URL url = new URL(cxUrl);
                    InputStream inputStream = url.openStream();
                    byte[] bytes = inputStreamToBytes(inputStream);
                    //将修改后的文件内容同步到接收到的文件id里
//                    String fileName = reqVO.getUserdata();
//                    String savePath = CX_SAVE_PATH + EcontractUtil.getTimeFolderPath() + IdUtil.fastSimpleUUID() + "/" + fileName;
//                    Long fileId = Long.valueOf(reqVO.getKey());
                    //文件更新
//                    Boolean bl = fileApi.updateCXFile(fileId, fileName, savePath, bytes);
//                    if (bl) {
//                        //更新成功
//                        return result.setError(DISCONNECT.getError());
//                    } else {
//                        return result.setError(FORCE_SAVE_CX_SERVER_ERROR.getError());
//                    }

//                    将修改后的文件内容覆盖掉文件id原本的url所对应的内容
                    try {
                        Long fileId = Long.valueOf(reqVO.getKey());
                        FileDTO fileDTO = fileApi.selectById(fileId);
                        if (ObjectUtil.isNull(fileDTO)) {
                            log.error("文件不存在" + fileDTO.getId());
                            throw exception(SYSTEM_ERROR, "请确认文件编号后重试。");
                        }
                        String savePath = fileDTO.getPath();
                        String fileName = fileDTO.getName();

                        fileApi.updateCXFile(fileId, fileName, savePath, bytes, fileDTO);
                        fileApi.createFile(fileName, savePath, bytes);

                        return result.setError(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error(" ==================================================== 【畅写回调异常】 ============================================================================= ");
                        return result.setError(-1);
                    }

                    //服务关闭报错
                case DISCONNECT_CX_SERVE_ERROR:
                case FORCE_SAVE_CX_SERVER_ERROR:
                    return result.setError(DISCONNECT_CX_SERVE_ERROR.getError());

                default:
                    return result.setError(DISCONNECT.getError());
            }


        }

        return result;
    }

    /**
     * 畅写文件转换
     */
    @Override
    public CXFileConverterRespDTO converter(CXFileConverterReqVO reqVO) {
        CXFileConverterDTO dto = ChangXieConverter.INSTANCE.req2DTO(reqVO);
        return changXieApi.converter(dto);
    }

    /**
     * 指定内容域插入数据
     */
    @Override
    public CXAddRespDTO addtocontentcontrol(CXAddReqVO reqVO) {
        CXAddReqDTO dto = ChangXieConverter.INSTANCE.addReq2DTO(reqVO);
        String jsonResponse = changXieApi.addtocontentcontrol(dto);

        // 使用 Hutool JSON 库解析 JSON 响应字符串
        JSONObject jsonObject = new JSONObject(jsonResponse);
        // 使用 Hutool JSON 库将 List<CXAddJsonVO> 转换为 JSON 字符串
        // 获取各个字段的值
        String key = jsonObject.getStr("key");
        JSONObject urls = jsonObject.getJSONObject("urls");
        String resultDocxUrl = urls.getStr("result.docx");
        Boolean end = jsonObject.getBool("end");
        CXAddRespDTO result = new CXAddRespDTO();
        result.setResultUrl(resultDocxUrl);
        result.setEnd(end);
        result.setKey(key);
        return result;
    }

    @Override
    public CXWaterMarkRespDTO insertwatermark(CXWaterMarkReqVO reqVO) {
        CXWaterMarkReqDTO dto = new CXWaterMarkReqDTO();
        dto.setFileUrl(reqVO.getFileUrl());
        dto.setSText(reqVO.getWaterText());
        dto.setBIsDiagonal(false);
        String dtoStr = JSONUtil.toJsonStr(dto);
//        String sep = "\"";
//        String dtoStr = "{" +sep+ "fileUrl" + sep+": " + reqVO.getFileUrl() + sep+"," + "sText: "+ sep+": " + sep + reqVO.getWaterText() + sep + "," + "bIsDiagonal: "+ sep+": " + false + "}";
//        String jsonResponse = changXieApi.insertwatermark(dto);

        // 设置请求头部信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 准备请求体数据
        String requestBody = dtoStr;

        // 创建 HTTP 请求实体对象
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // 目标服务的 URL
        String url = "http://120.53.0.50:8092/insertwatermark";
        String responseBody = "";
        try {
            // 发送 POST 请求
            ResponseEntity<String> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

            // 获取响应状态码
            HttpStatus statusCode = responseEntity.getStatusCode();
            System.out.println("Response Status Code: " + statusCode);

            // 获取响应体数据
            responseBody = responseEntity.getBody();
            System.out.println("Response Body: " + responseBody);
        } catch (Exception e) {
            System.err.println("POST Request Error: " + e.getMessage());
        }


        // 使用 Hutool JSON 库解析 JSON 响应字符串
        JSONObject jsonObject = new JSONObject(responseBody);
        // 使用 Hutool JSON 库将 List<CXAddJsonVO> 转换为 JSON 字符串
        // 获取各个字段的值
        String key = jsonObject.getStr("key");
        JSONObject urls = jsonObject.getJSONObject("urls");
        String resultDocxUrl = urls.getStr("result.docx");
        Boolean end = jsonObject.getBool("end");
        CXWaterMarkRespDTO result = new CXWaterMarkRespDTO();
        result.setResultUrl(resultDocxUrl);
        result.setEnd(end);
        result.setKey(key);
        return result;
    }

    @Override
    public CXSaveRespDTO cxSave(CXSaveReqVO reqVO) {
        CXSaveReqDTO dto = ChangXieConverter.INSTANCE.cxSaveReq2DTO(reqVO);
        return changXieApi.cxSave(dto);
    }

    @Override
    public Long createContractByOrder(CXCreateContractByOrderReqVO reqVO) throws Exception {
        // 1，获取订单信息
        DraftOrderInfoDO orderDO = gpMallOrderMapper.selectById(reqVO.getId());
        if (ObjectUtil.isNull(orderDO)) {
            throw exception(SYSTEM_ERROR, "订单数据为空");
        }
        CXOrder2ContractDTO dto = getCXDTOFromOrder(orderDO);
        Model model = modelMapper.selectById(orderDO.getModelId());
        if (ObjectUtil.isNull(model)) {
            throw exception(SYSTEM_ERROR, "模板数据为空");
        }
        String contractName = "XX市财政部采购合同";

        //2，获得模板-拷贝模板副本
        byte[] fileContent = fileApi.getFileContentById(Long.valueOf(model.getRemoteFileId()));
        String fileName = contractName + FileTypeEnums.DOCX.getCode();
        Long copyFileId = fileApi.uploadFile(fileName, FileUploadPathEnum.CONTRACT_DRAFT.getPath() + fileName, fileContent);

        //3，回填内容到副本
        CXAddRespDTO respDTO = new CXAddRespDTO();
        if (CollectionUtil.isNotEmpty(reqVO.getZoneNames())) {
            respDTO = fillZonesToCopyFile(reqVO.getZoneNames(), dto, copyFileId);
        }
        if (ObjectUtil.isNotNull(respDTO)) {
            String cxUrl = respDTO.getResultUrl();
            URL url = new URL(cxUrl);
            InputStream inputStream = url.openStream();
            byte[] bytes = inputStreamToBytes(inputStream);
            fileApi.updateCXFile(copyFileId, fileName, CX_SAVE_PATH.getPath() + fileName, bytes, null);
        } else {
            throw exception(SYSTEM_ERROR, "请求在线编辑服务异常");
        }
        return copyFileId;
    }


    /**
     * 将内容域赋值回填到副本文件中
     */
    private CXAddRespDTO fillZonesToCopyFile(List<String> zoneNames, CXOrder2ContractDTO cxOrder2ContractDTO, Long copyFileId) {
        List<CXAddJsonDTO> zoneInfos = new ArrayList<CXAddJsonDTO>();

        // 使用流和过滤器筛选出以 "con_" 开头的元素，并去掉前缀
        List<String> pureZoneNames = zoneNames.stream().filter(name -> name.startsWith("con_"))
                // 去掉 "con_" 前缀
                .map(name -> name.substring(4)).collect(Collectors.toList());

//        for (String zoneName : pureZoneNames) {
//            CXAddJsonVO zoneInfo = new CXAddJsonVO();
//            zoneInfo.setName(zoneName);
//            String javaZoneName = convertCamelToUnderscore(zoneName);
//            if (isFieldPresentInClass(DraftOrderInfoDO.class, javaZoneName)) {
//                String value = getFieldValue(DraftOrderInfoDO.class, javaZoneName);
//                zoneInfo.setContent(value);
//            }
//            zoneInfos.add(zoneInfo);
//        }
        CXAddReqDTO cxAddReqDTO = new CXAddReqDTO();
        FileWpsDTO fileDTO = fileApi.selectWpsDTOById(copyFileId);
        if (ObjectUtil.isNull(fileDTO)) {
            throw exception(EMPTY_DATA_ERROR);
        }

        cxAddReqDTO.setFileUrl(fileDTO.getUrl());

        List<CXAddJsonDTO> jasonList = new ArrayList<CXAddJsonDTO>();
        jasonList = getValueFromDTO(cxOrder2ContractDTO);
        String jasonStr = JSONUtil.toJsonStr(jasonList);
        cxAddReqDTO.setJsonArr(jasonStr);
        String response = changXieApi.addtocontentcontrol(cxAddReqDTO);

        // 使用 Hutool JSON 库解析 JSON 响应字符串
        JSONObject jsonObject = new JSONObject(response);
        // 使用 Hutool JSON 库将 List<CXAddJsonVO> 转换为 JSON 字符串
        // 获取各个字段的值
        String key = jsonObject.getStr("key");
        JSONObject urls = jsonObject.getJSONObject("urls");
        String resultDocxUrl = urls.getStr("result.docx");
        Boolean end = jsonObject.getBool("end");
        CXAddRespDTO result = new CXAddRespDTO();
        result.setResultUrl(resultDocxUrl);
        result.setEnd(end);
        result.setKey(key);
        return result;
    }

    private List<CXAddJsonDTO> getValueFromDTO(CXOrder2ContractDTO cxOrder2ContractDTO) {
        List<CXAddJsonDTO> result = new ArrayList<CXAddJsonDTO>();
        CXAddJsonDTO jsonVO1 = new CXAddJsonDTO();
        jsonVO1.setName("contract-name");
        jsonVO1.setContent("XX设备采购合同");
        result.add(jsonVO1);
        CXAddJsonDTO jsonVO2 = new CXAddJsonDTO();
        jsonVO2.setName("contract-code");
        jsonVO2.setContent("HT-240426");
        result.add(jsonVO2);
        CXAddJsonDTO jsonVO3 = new CXAddJsonDTO();
        jsonVO3.setName("partA");
        jsonVO3.setContent("XX市财政部");
        result.add(jsonVO3);
        CXAddJsonDTO jsonVO4 = new CXAddJsonDTO();
        jsonVO4.setName("partB");
        jsonVO4.setContent("XX科技公司");
        result.add(jsonVO4);
//        CXAddJsonVO jsonVO5 = new CXAddJsonVO();
//        jsonVO5.setName("head_package_sort");
//        jsonVO5.setContent("1");
//        result.add(jsonVO5);
        return result;
    }

    private CXOrder2ContractDTO getCXDTOFromOrder(DraftOrderInfoDO orderInfoDO) {
        CXOrder2ContractDTO dto = new CXOrder2ContractDTO();
        ProjectDO projectDO = gpMallProjectMapper.selectOne(new LambdaQueryWrapperX<ProjectDO>().eqIfPresent(ProjectDO::getOrderId, orderInfoDO.getOrderGuid()));
        if (ObjectUtil.isNotNull(projectDO)) {
            dto.setProjectName(projectDO.getProjectName());
            dto.setPackageName("采购包" + projectDO.getProjectCode());
            dto.setPackageSort("1");
        }

        dto.setPackageSort("1");
        dto.setCode(EcontractUtil.getCodeAutoByTimestamp(orderInfoDO.getSupplierName()));
        dto.setPartAAddr("北京市西城区");
        dto.setPartBAddr("上海市静安区");
        dto.setSignDate(new Date());
        return dto;
    }

    @Override
    public GetDocContentRespDTO getDocContent(GetDocContentReqVO reqVO) {
        GetDocContentReqDTO dto = ChangXieConverter.INSTANCE.getContentReq2DTO(reqVO);
        String response = changXieApi.getDocContent(dto);
        // 使用 Hutool JSON 库解析 JSON 响应字符串
        JSONObject jsonObject = new JSONObject(response);
        // 使用 Hutool JSON 库将 List<CXAddJsonVO> 转换为 JSON 字符串
        // 获取各个字段的值
        String key = jsonObject.getStr("key");
        JSONObject urls = jsonObject.getJSONObject("urls");
        String resultDocxUrl = urls.getStr("result.txt");
        Boolean end = jsonObject.getBool("end");
        String code = jsonObject.getStr("code");
        GetDocContentRespDTO result = new GetDocContentRespDTO();
        result.setCode(Integer.valueOf(code));
        result.setEnd(end);
        result.setKey(key);
        ResultTxtDTO txtDTO = new ResultTxtDTO().setResultTxt(resultDocxUrl);
        List<ResultTxtDTO> dtoList = new ArrayList<ResultTxtDTO>();
        dtoList.add(txtDTO);
        result.setUrls(dtoList);
        return result;
    }

    @Override
    public InsertDocContentRespDTO insertContentToDoc(InsertDocContentReqVO reqVO) {
        InsertDocContentReqDTO dto = ChangXieConverter.INSTANCE.insertContentToDoc(reqVO);
        String response = changXieApi.insertContentToDoc(dto);
        // 使用 Hutool JSON 库解析 JSON 响应字符串
        JSONObject jsonObject = new JSONObject(response);
        // 使用 Hutool JSON 库将 List<CXAddJsonVO> 转换为 JSON 字符串
        // 获取各个字段的值
        String key = jsonObject.getStr("key");
        JSONObject urls = jsonObject.getJSONObject("urls");
        String resultDocxUrl = urls.getStr("result.txt");
        Boolean end = jsonObject.getBool("end");
        String code = jsonObject.getStr("code");
        InsertDocContentRespDTO result = new InsertDocContentRespDTO();
        result.setCode(Integer.valueOf(code));
        result.setEnd(end);
        result.setKey(key);
        ResultDocRspDTO resultDTO = new ResultDocRspDTO().setResultDoc(resultDocxUrl);
        List<ResultDocRspDTO> dtoList = new ArrayList<ResultDocRspDTO>();
        dtoList.add(resultDTO);
        result.setUrls(dtoList);
        return result;
    }

    @Override
    public CXCleanDraftRespVO cleandraft(CXCleanDraftReqVO reqVO) {
        CXCleanDraftReqDTO dto = ChangXieConverter.INSTANCE.cleandraft2DTO(reqVO);

        String response = changXieApi.cleandraft(dto);
        JSONObject jsonObject = new JSONObject(response);
        String key = jsonObject.getStr("key");
        Boolean end = jsonObject.getBool("end");
        JSONObject urlsJson = jsonObject.getJSONObject("urls");
        String url = "";
        if (ObjectUtil.isNotNull(urlsJson)) {
            url = urlsJson.getStr(RESULT_DOCX);
        }
        Map<String, String> map = new HashMap<>();
        map.put(RESULT_DOCX, url);
        return new CXCleanDraftRespVO().setKey(key).setEnd(end).setUrls(map);
    }


    @Override
    public Long cleandraftV2(Long fileId) {
        FileDTO fileDTO = fileApi.selectById(fileId);
        if (ObjectUtil.isNull(fileDTO)) {
            throw exception(DIY_ERROR, "文件:" + fileId + "不存在");
        }
        Long result = 0L;
        log.info("清稿前："+fileDTO.getUrl());
        if (Long.valueOf(18).equals(fileDTO.getConfigId())){
            try {
                fileDTO.setUrl(minioUtils.generatePresignedUrl(fileDTO.getBucketName(), fileDTO.getPath()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        CXCleanDraftRespVO respVO = cleandraft(new CXCleanDraftReqVO().setFileUrl(fileDTO.getUrl()).setAcceptAllRevision(true));
        log.info("清稿后："+ com.alibaba.fastjson.JSONObject.toJSON(respVO));
        if (ObjectUtil.isNotNull(respVO) && respVO.getEnd()) {
            if (ObjectUtil.isNotNull(respVO.getUrls()) && ObjectUtil.isNotNull(respVO.getUrls().get(RESULT_DOCX))) {
                //先下载到本地，再上传到minio
                try {
                    URL url = new URL(respVO.getUrls().get(RESULT_DOCX));
                    InputStream inputStream = url.openStream();
                    byte[] bytes = inputStreamToBytes(inputStream);
                    String suffix = EcontractUtil.getFileSuffix(fileDTO.getName());
                    String outFileName = IdUtil.fastSimpleUUID() + suffix;
//                    return fileApi.uploadFile(outFileName, "E:/桌面/其他" + outFileName, bytes);
                    return fileApi.uploadFile(outFileName, CX_CLEAN_PATH + outFileName, bytes);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("【畅写服务】 一键清稿异常");
                }
            }
        }

        return result;
    }

    @Override
    public Map<String,String> cleandraftV3(Long fileId) {
        FileDTO fileDTO = fileApi.selectById(fileId);
        if (ObjectUtil.isNull(fileDTO)) {
            throw exception(DIY_ERROR, "文件:" + fileId + "不存在");
        }
        Map<String, String> map = new HashMap<>();
        Long result = 0L;
        String fileUrl = "";
        log.info("清稿前："+fileDTO.getUrl());
        try {
            fileUrl = minioUtils.generatePresignedUrl(fileDTO.getBucketName(), fileDTO.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        CXCleanDraftRespVO respVO = cleandraft(new CXCleanDraftReqVO().setFileUrl(fileUrl).setAcceptAllRevision(true));
        log.info("清稿后："+ com.alibaba.fastjson.JSONObject.toJSON(respVO));
        if (ObjectUtil.isNotNull(respVO) && respVO.getEnd()) {
            if (ObjectUtil.isNotNull(respVO.getUrls()) && ObjectUtil.isNotNull(respVO.getUrls().get(RESULT_DOCX))) {
                //先下载到本地，再上传到minio
                try {
                    URL url = new URL(respVO.getUrls().get(RESULT_DOCX));
                    InputStream inputStream = url.openStream();
                    byte[] bytes = inputStreamToBytes(inputStream);
                    String suffix = EcontractUtil.getFileSuffix(fileDTO.getName());
                    String outFileName = IdUtil.fastSimpleUUID() + suffix;
//                    return fileApi.uploadFile(outFileName, "E:/桌面/其他" + outFileName, bytes);
                     fileApi.uploadFile(outFileName, CX_CLEAN_PATH + outFileName, bytes, fileDTO.getBucketName());
                    map.put("fileName",fileDTO.getName());
                    map.put("fileUrl",respVO.getUrls().get(RESULT_DOCX));
                    map.put("fileId",String.valueOf(fileId));

                    return map;

                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("【畅写服务】 一键清稿异常");
                }
            }
        }

        return map;
    }
    @Override
    public Long converterDocx2Pdf(Long fileId) {
        FileDTO fileDTO = fileApi.selectById(fileId);
        if (ObjectUtil.isNull(fileDTO)) {
            throw exception(DIY_ERROR, "文件:" + fileId + "不存在");
        }
        String fileName = fileDTO.getName();
        String fileType = getSimpleFileSuffix(fileName);
        if (StringUtils.isBlank(fileType)) {
            log.error(fileId + "的文件格式异常"+"请使用支持的格式后重试。");
            throw exception(DIY_ERROR, "请使用支持的格式后重试。");
        }
        CXFileConverterRespDTO respDTO = changXieApi.converter(new CXFileConverterDTO().setKey(String.valueOf(fileId)).setFiletype(fileType).setOutputtype(SUFFIX_PDF).setUrl(fileDTO.getUrl()));
        if (ObjectUtil.isNotNull(respDTO) && respDTO.getEndConvert()) {

            String outFileUrl = respDTO.getFileUrl();
            //先下载到本地，再上传到minio
            try {
                URL url = new URL(outFileUrl);
                InputStream inputStream = url.openStream();
                byte[] bytes = inputStreamToBytes(inputStream);
                String outFileName = IdUtil.fastSimpleUUID() + FileTypeEnums.PDF.getCode();
                Long pdfFileId = fileApi.uploadFile(outFileName, FileUploadPathEnum.CX_SAVE_PATH.getPath() + outFileName, bytes);
                return pdfFileId;
            } catch (Exception e) {
                e.printStackTrace();
                log.error("【畅写服务】转换文件异常");
            }

        }
        return null;
    }

    @Override
    public Long converterDocx2PdfV2(Long fileId,FileUploadPathEnum pathEnum) {
        FileDTO fileDTO = fileApi.selectById(fileId);
        if (ObjectUtil.isNull(fileDTO)) {
            throw exception(DIY_ERROR, "文件:" + fileId + "不存在");
        }
        if(ObjectUtil.isNull(pathEnum)){

        }
        String fileName = fileDTO.getName();
        String fileType = getSimpleFileSuffix(fileName);
        if (StringUtils.isBlank(fileType)) {
            throw exception(DIY_ERROR, fileId + "的文件格式异常");
        }

        if(FileConfigEnum.CONTRACT_SIGNING.getId().equals(fileDTO  .getConfigId())){
            try {
                String bucket = fileDTO.getBucketName();
                if (StringUtils.isEmpty(bucket)){
                    bucket = minioUtils.getBucketNameByTenantId();
                }
                fileDTO.setUrl(minioUtils.generatePresignedUrl(bucket, fileDTO.getPath()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        CXFileConverterRespDTO respDTO = changXieApi.converter(new CXFileConverterDTO().setKey(String.valueOf(fileId)).setFiletype(fileType).setOutputtype(SUFFIX_PDF).setUrl(fileDTO.getUrl()));

        if (ObjectUtil.isNotNull(respDTO) && respDTO.getEndConvert()) {

            String outFileUrl = respDTO.getFileUrl();
            //先下载到本地，再上传到minio
            try {
                URL url = new URL(outFileUrl);
                InputStream inputStream = url.openStream();
                byte[] bytes = inputStreamToBytes(inputStream);
                String outFileName = IdUtil.fastSimpleUUID() + FileTypeEnums.PDF.getCode();
                Long pdfFileId = fileApi.uploadFile(outFileName, pathEnum.getPath() + outFileName, bytes);
                return pdfFileId;
            } catch (Exception e) {
                e.printStackTrace();
                log.error("【畅写服务】转换文件异常");
            }

        }
        return null;
    }


    @Override
    public Long converterDocx2PdfV3(Map<String,String> map, FileUploadPathEnum pathEnum, Long pdfFileId) {

        String fileName = map.get("fileName");
        String fileType = getSimpleFileSuffix(fileName);
        String fileUrl = map.get("fileUrl");
        String fileId = map.get("fileId");
        if (StringUtils.isBlank(fileType)) {
            throw exception(DIY_ERROR, fileId + "的文件格式异常");
        }
        log.info("toPDF前："+fileUrl);
        CXFileConverterDTO reqDTO = new CXFileConverterDTO().setKey(String.valueOf(pdfFileId)).setFiletype(fileType).setOutputtype(SUFFIX_PDF).setUrl(fileUrl);
        CXFileConverterRespDTO respDTO = changXieApi.converter(reqDTO);
        log.info("toPDF请求参数："+JSONUtil.toJsonStr(reqDTO));
        log.info("toPDF后："+fileId+","+respDTO.getFileUrl());

        if (ObjectUtil.isNotNull(respDTO) && respDTO.getEndConvert()) {

            String outFileUrl = respDTO.getFileUrl();
            //先下载到本地，再上传到minio
            try {
                URL url = new URL(outFileUrl);
                InputStream inputStream = url.openStream();
                byte[] bytes = inputStreamToBytes(inputStream);
                String outFileName = IdUtil.fastSimpleUUID() + FileTypeEnums.PDF.getCode();

                FileDTO pdfFile = new FileDTO();
                pdfFile.setId(pdfFileId);
                pdfFile.setUrl(fileUrl);
                FileDTO fileDTO = fileApi.uploadFileV2(outFileName, pathEnum.getPath() + outFileName, bytes);
                log.info("FileDTO："+JSONUtil.toJsonStr(fileDTO));
                log.info("toPDF后ID："+pdfFileId.toString());
                // 更新pdf文件信息
                fileApi.updateFileInfo(fileDTO.setId(pdfFileId));
                return pdfFileId;
            } catch (Exception e) {
                e.printStackTrace();
                log.error("【畅写服务】转换文件异常");
            }

        }
        return null;
    }


}
