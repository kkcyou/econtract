package com.yaoan.module.econtract.controller.admin.gcy.gpmall;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.CancellationFileVO;
import com.yaoan.module.econtract.service.gcy.gpmall.GPMallProjectService;
import com.yaoan.module.econtract.service.gpmall.GPMallContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description: 黑龙江合同
 * @author: zhc
 * @date: 2024-4-11
 */
@Slf4j
@RestController
@Validated
@Tag(name = "黑龙江合同", description = "黑龙江合同")
public class GPMallContractController {
    @Resource
    private GPMallContractService gpMallContractService;
    @Resource
    private GPMallProjectService gpMallProjectService;
    /**
     * 卖场和超市 无计划作废接口（非交易合同作废）
     * 监管未备案的 并且 签订完的 合同 可以作废
     */
    @PostMapping(value = "/econtract/contract/cancel")
    @OperateLog(enable = true)
    @Operation(summary = "卖场和超市 无计划作废接口（非交易合同作废）")
    public CommonResult<String> cancelContractById(@RequestBody CancellationFileVO vo) throws Exception {
        String result = gpMallContractService.cancelContract(vo);
        return success(result);
    }
}
