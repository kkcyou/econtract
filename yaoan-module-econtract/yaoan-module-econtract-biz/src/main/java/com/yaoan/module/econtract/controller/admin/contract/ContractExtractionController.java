package com.yaoan.module.econtract.controller.admin.contract;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.module.econtract.controller.admin.contract.vo.*;
import com.yaoan.module.econtract.controller.admin.contract.vo.extraction.ContractExtractRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.extraction.TaskIdReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.extraction.UploadByFileIdReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.extraction.UploadReqVO;
import com.yaoan.module.econtract.service.contract.ContractExtractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

@Slf4j
@RestController
@RequestMapping("econtract/intelligent")
@Validated
@Tag(name = "合同-智能提取", description = "合同智能提取操作接口")
public class ContractExtractionController {

    @Resource
    private ContractExtractionService contractService;
    /**
     * 智能填写映射code 表单
     */
    @PostMapping(value = "/form/to/json")
    @Operation(summary = "智能填写映射code")
    public CommonResult<List<JsonFormRespVO>> fromToJson(@Valid @RequestBody FormToJsonReqVO formToJsonReqVO) throws Exception {
        List<JsonFormRespVO> result = contractService.toJsonForm(formToJsonReqVO);
        return success(result);
    }
    @PostMapping(value = "/token/generate")
    @Operation(summary = "SppGPT获取token")
    public CommonResult<TokenRespVO> generateToken(){
        TokenRespVO result = contractService.generateToken();
        return success(result);
    }

    @PostMapping(value = "/external/v1/upload")
    @Operation(summary = "SppGPT上传文件")
    public CommonResult<TaskIdRespVO> upload(UploadReqVO vo) throws Exception {
        TaskIdRespVO result = contractService.upload(vo);
        return success(result);
    }
    @PostMapping(value = "/external/v1/uploadByFileId")
    @Operation(summary = "SppGPT上传文件通过文件id")
    public CommonResult<TaskIdRespVO> uploadByFileId(@RequestBody UploadByFileIdReqVO vo) throws Exception {
        TaskIdRespVO result = contractService.uploadByFileId(vo);
        return success(result);
    }

    @PostMapping(value = "/detail")
    @Operation(summary = "SppGPT获取合同解析详情")
    public CommonResult<ContractExtractRespVO> contractDetail(@Valid @RequestBody TaskIdReqVO vo) {
        ContractExtractRespVO result = contractService.detail(vo);
        return success(result);
    }
    @GetMapping(value = "/getDetail")
    @Operation(summary = "获取合同解析详情")
    public CommonResult<ContractDetailRespVO> getDetail(@RequestBody String taskId) {
        ContractDetailRespVO result = contractService.getDetail(taskId);
        return success(result);
    }

}
