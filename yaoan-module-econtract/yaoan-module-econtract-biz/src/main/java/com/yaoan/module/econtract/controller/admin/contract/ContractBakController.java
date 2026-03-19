package com.yaoan.module.econtract.controller.admin.contract;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.idempotent.core.annotation.Idempotent;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.service.contract.ContractBakService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.annotation.Resource;

import static com.yaoan.framework.common.pojo.CommonResult.success;

@Slf4j
@RestController
@RequestMapping("econtract/bak")
@Validated
@Tag(name = "合同备案操作接口", description = "合同备案操作接口")
public class ContractBakController {

    @Resource
    private ContractBakService contractBakService;
    @GetMapping(value = "/contractBak")
    @OperateLog(enable = true)
    @Operation(summary = "备案")
    @Idempotent(timeout = 5, message = "正在处理中，请勿重复请求")
    public CommonResult<Boolean> contractBak(String id) throws Exception {
        contractBakService.contractBak(id);
        return success(true);
    }
}
