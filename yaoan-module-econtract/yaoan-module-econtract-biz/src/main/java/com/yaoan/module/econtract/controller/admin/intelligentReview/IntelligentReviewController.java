package com.yaoan.module.econtract.controller.admin.intelligentReview;

import cn.hutool.core.codec.Base64;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.framework.security.core.annotations.PrePermissioned;
import com.yaoan.module.econtract.controller.admin.contract.vo.*;
import com.yaoan.module.econtract.service.intelligentReview.IntelligentReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 智能审查
 */
@Slf4j
@RestController
@RequestMapping("econtract/contract")
@Validated
@Tag(name = "智能审查", description = "智能审查")
public class IntelligentReviewController {

    @Resource
    private IntelligentReviewService intelligentReviewService;
    /**
     * 智能填写映射code
     */
    @PostMapping(value = "/to/json")
    @Operation(summary = "智能填写映射code")
    @PermitAll
    @OperateLog(logArgs = false)
    public CommonResult<List<JsonRespVO>> toJson(@Valid @RequestBody ToJsonReqVO toJsonReqVO) throws Exception {
        List<JsonRespVO> result = intelligentReviewService.toJson(toJsonReqVO);
        return success(result);
    }

    /**
     * 智能填写映射code
     */
    @PostMapping(value = "/to/jsonV2")
    @Operation(summary = "法意智能提取")
    @PermitAll
    @OperateLog(logArgs = false)
    public CommonResult< Map<String, String>> jsonV2(@Valid @RequestBody ToJsonReqVO toJsonReqVO) throws Exception {
        Map<String, String> result = intelligentReviewService.jsonV2(toJsonReqVO);
        return success(result);
    }

    /**
     * 法易--智能提取接口--获取模型接口
     * 智能填写映射code 表单
     */
    @PostMapping(value = "/form/to/json")
    @Operation(summary = "法易智能提取映射code")
    @PermitAll
    @OperateLog(logArgs = false)
    public CommonResult<List<JsonFormRespVO>> fromToJson(@RequestBody FormToJsonReqVO formToJsonReqVO) throws Exception {
        List<JsonFormRespVO> result = intelligentReviewService.toJsonForm(formToJsonReqVO);
        return success(result);
    }

    /**
     * 法意智能审查，获取token
     */
    @GetMapping(value = "/getToken")
    @Operation(summary = "获取token")
    @PermitAll
    @OperateLog(logArgs = false)
    public GetTokenRespVO getToken(@RequestParam("appId") String appId, @RequestParam("tokenId") String tokenId) throws UnsupportedEncodingException {
        GetTokenRespVO getTokenRespVO = intelligentReviewService.getToken(appId, tokenId);
        return getTokenRespVO;
    }

    /**
     * 获取纠错信息接口
     */
    @PostMapping(value = "/getErrorInfos")
    @Operation(summary = "获取纠错信息接口")
    @PermitAll
    @OperateLog(logArgs = false)
    public ErrorInfosRespVO getErrorInfos(@RequestBody ErrorInfosReqVO vo) throws JsonProcessingException {
        ErrorInfosRespVO respVO = intelligentReviewService.getErrorInfos(vo);
        return respVO;
    }

    /**
     * 纠错接口加密
     */
    @PostMapping(value = "/intelligentReview")
    @Operation(summary = "纠错接口加密")
    @PermitAll
    @OperateLog(logArgs = false)
    public CommonResult<CorrectionRespVO> intelligentReview(CorrectionReqVO reqVO) throws Exception {
        CorrectionRespVO result = intelligentReviewService.correction(reqVO);
        return success(result);
    }

    /**
     * 纠错接口
     */
    @PostMapping(value = "/correction")
    @Operation(summary = "纠错接口")
    @PermitAll
//    @PrePermissioned
    @OperateLog(logArgs = false)
    public CommonResult<CorrectionRespVO> correction(CorrectionReqVO reqVO) throws Exception {
        CorrectionRespVO result = intelligentReviewService.intelligentReview(reqVO);
        return success(result);
    }
    /**
     * 流程系统保存接口
     */
    @PostMapping(value = "/saveFile")
    @Operation(summary = "流程系统保存接口")
    @PermitAll
    @OperateLog(logArgs = false)
    public CommonResult<Map<String, Object>> saveFile(String CaseID,byte[] DocumentData,String Sign) throws IOException {
        Map<String, Object> result = intelligentReviewService.saveFile(CaseID,DocumentData,Sign);
        return success(result);
    }

    @PostMapping(value = "/token/generate")
    @Operation(summary = "SppGPT获取token")
    public CommonResult<TokenRespVO> generateToken() {
        TokenRespVO result = intelligentReviewService.generateToken();
        return success(result);
    }

    @PostMapping(value = "/external/v1/upload")
    @Operation(summary = "SppGPT上传文件")
    public CommonResult<TaskIdRespVO> upload(@RequestParam("file") MultipartFile file) throws Exception {
        TaskIdRespVO result = intelligentReviewService.upload(file);
        return success(result);
    }

    @PostMapping(value = "/detail")
    @Operation(summary = "SppGPT获取合同解析详情")
    public CommonResult<ContractDetailRespVO> contractDetail(@RequestParam("taskIds") String taskIds) {
        ContractDetailRespVO result = intelligentReviewService.detail(taskIds);
        return success(result);
    }

    @GetMapping(value = "/getDetail")
    @Operation(summary = "获取合同解析详情")
    public CommonResult<ContractDetailRespVO> getDetail(@RequestParam("taskId") String taskId) {
        ContractDetailRespVO result = intelligentReviewService.getDetail(taskId);
        return success(result);
    }


    public static void main(String[] args) throws Exception {
        String orgId = "0";
        String userId = "ceshi";
        String tokenId = Base64.encode(orgId + "@" + userId);
        System.out.println("tokenId:"+tokenId);
    }
}
