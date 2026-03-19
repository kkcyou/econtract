package com.yaoan.module.econtract.controller.admin.freezed;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.freezed.vo.FreezedContractCreateReqVO;
import com.yaoan.module.econtract.service.freezed.FreezedContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 合同冻结相应操作
 * 
 * @author lls
 * @since 2024-09-06
 */
@Slf4j
@RestController
@RequestMapping("econtract/freezed")
@Validated
@Tag(name = "合同冻结", description = "合同冻结相应操作")
public class FreezedContractController {
    @Resource
    private FreezedContractService freezedContractService;

    /**
     * 冻结合同创建（修改） 包含开启工作流
     * @param
     * @return
     */
    @Deprecated
    @PostMapping(value = "/create")
    @Operation(summary = "创建合同")
    @OperateLog(logArgs = false)
    public CommonResult<String> create(@Valid @ModelAttribute FreezedContractCreateReqVO freezedContractCreateReqVO) throws Exception {
        String id = freezedContractService.create(freezedContractCreateReqVO);
        return success(id);
    }

    /**
     * 冻结合同创建（修改）
     * @param
     * @return
     */
    @PostMapping(value = "/v2/create")
    @Operation(summary = "冻结合同")
    @OperateLog(logArgs = false)
    public CommonResult<String> createV2(@Valid @RequestBody FreezedContractCreateReqVO freezedContractCreateReqVO) throws Exception {
        String id = freezedContractService.createV2(freezedContractCreateReqVO);
        return success(id);
    }

    /**
     * 解冻
     * @param freezedContractCreateReqVO
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/v2/unfreezed")
    @Operation(summary = "解冻合同")
    @OperateLog(logArgs = false)
    public CommonResult<String> unfreezed(@Valid @RequestBody FreezedContractCreateReqVO freezedContractCreateReqVO) throws Exception {
        String id = freezedContractService.unFreezed(freezedContractCreateReqVO);
        return success(id);
    }
}
