package com.yaoan.module.econtract.controller.admin.outward.contract;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.idempotent.core.annotation.Idempotent;
import com.yaoan.module.econtract.controller.admin.contract.vo.*;
import com.yaoan.module.econtract.controller.admin.outward.contract.VO.*;
import com.yaoan.module.econtract.service.contract.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 合同对外api
 */
@Slf4j
@RestController
@RequestMapping("contractAPI/contract")
@Validated
@Tag(name = "合同对外api", description = "合同对外api")
public class ContractOutwardController {
    @Resource
    private ContractService contractService;

    /**
     * 验签
     */
    @PostMapping(value = "/check")
    @Operation(summary = "验签")
    public CommonResult<String> check(@Valid @RequestBody ContractDraft contractDraft) throws UnsupportedEncodingException {
        String result = contractService.checkContractOW(contractDraft);
        return success(result);
    }

    /**
     * 合同创建(修改)
     * @param apiCreateReqVO
     * @return
     */
    @PostMapping(value = "/create")
    @Operation(summary = "创建合同")
    @Idempotent(timeout = 1, timeUnit = TimeUnit.SECONDS, message = "正在创建中，请勿重复申请")
    public CommonResult<String> create(@Valid @RequestBody ApiCreateReqVO apiCreateReqVO) {
        String result = contractService.createContractOW(apiCreateReqVO);
        return success(result);
    }

    /**
     * 合同查看
     */
    @GetMapping(value = "/getById/{id}")
    @Operation(summary = "查看合同")
    @Idempotent(timeout = 1, timeUnit = TimeUnit.SECONDS, message = "正在查询中，请勿重复申请")
    public CommonResult<ApiRespVO> getById(@PathVariable String id) throws Exception {
        ApiRespVO result = contractService.getByIdOW(id);
        return success(result);
    }

    /**
     * 合同信息查询
     */
    @GetMapping(value = "/getByIdInfo/{id}")
    @Operation(summary = "合同信息查询")
    @Idempotent(timeout = 1, timeUnit = TimeUnit.SECONDS, message = "正在查询中，请勿重复申请")
    public CommonResult<ApiInfoRespVO> getByIdInfo(@PathVariable String id) throws Exception {
        ApiInfoRespVO result = contractService.getInfoByIdOW(id);
        return success(result);
    }

    /**
     * 合同状态同步
     */
    @PostMapping(value = "/updateStatus4")
    @Operation(summary = "合同状态同步")
    public CommonResult<Boolean> updateStatus(@RequestBody List<ApiStatusReqVO> list) {
        Boolean aBoolean = contractService.updateStatus(list);
        return success(aBoolean);
    }

    /**
     * 列表查询
     */
    @PostMapping(value = "/list")
    @Operation(summary = "列表查询")
    public CommonResult<PageResult<ApiPageRespVO>> list(@RequestBody ApiPageReqVO apiPageReqVO) {
        PageResult<ApiPageRespVO> result = contractService.getApiPage(apiPageReqVO);
        return success(result);
    }

    /**
     * 根据合同id 将文件富文本 -> pdf 并上传minIO
     * @param id
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/to/pdf/{id}")
    @Operation(summary = "文件格式转换，富文本、doc(x) -> pdf 并上传minIO")
    public CommonResult<HashMap<String, Long>> toPdf(@PathVariable String id) throws Exception {
        HashMap<String, Long> result = contractService.toPdfById(id);
        return success(result);
    }

}