package com.yaoan.module.econtract.controller.admin.terminate;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.terminate.vo.TerminateContractCreateReqVO;
import com.yaoan.module.econtract.service.terminate.TerminateContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 合同终止相应操作
 */
@Slf4j
@RestController
@RequestMapping("econtract/terminate")
@Validated
@Tag(name = "合同终止", description = "合同终止相应操作")
public class TerminateContractController {
    @Resource
    private TerminateContractService terminateContractService;

    /**
     * 终止合同创建（修改） 包含开启工作流
     * @param
     * @return
     */
    @Deprecated
    @PostMapping(value = "/create")
    @Operation(summary = "创建合同")
    @OperateLog(logArgs = false)
    public CommonResult<String> create(@Valid @ModelAttribute TerminateContractCreateReqVO terminateContractCreateReqVO) throws Exception {
        String id = terminateContractService.create(terminateContractCreateReqVO);
        return success(id);
    }

    /**
     * 终止合同创建（修改）
     * @param
     * @return
     */
    @PostMapping(value = "/v2/create")
    @Operation(summary = "创建合同")
    @OperateLog(logArgs = false)
    public CommonResult<String> createV2(@Valid @RequestBody TerminateContractCreateReqVO terminateContractCreateReqVO) throws Exception {
        String id = terminateContractService.createV2(terminateContractCreateReqVO);
        return success(id);
    }
}
