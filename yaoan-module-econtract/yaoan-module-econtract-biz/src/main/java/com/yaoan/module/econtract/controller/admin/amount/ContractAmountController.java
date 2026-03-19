package com.yaoan.module.econtract.controller.admin.amount;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.amount.vo.ContractAmountRespVO;
import com.yaoan.module.econtract.controller.admin.amount.vo.ContractTypeSignedStatisticRespVO;
import com.yaoan.module.econtract.controller.admin.amount.vo.SearchYearReqVO;
import com.yaoan.module.econtract.controller.admin.amount.vo.alert.GPMallAlertListReqVO;
import com.yaoan.module.econtract.controller.admin.amount.vo.alert.GPMallBigAlertListRespVO;
import com.yaoan.module.econtract.service.amount.ContractAmountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description: 合同签约金额
 * @author: Pele
 * @date: 2023/11/8 21:32
 */
@Slf4j
@RestController
@RequestMapping("econtract/contractAmount")
@Validated
@Tag(name = "合同签约金额", description = "合同签约金额操作接口")
public class ContractAmountController {

    @Resource
    private ContractAmountService contractAmountService;

    /**
     * 合同签约金额情况（折线图）
     */
    @PostMapping(value = "/contractAmount")
    @Operation(summary = "合同签约金额情况")
    public CommonResult<List<ContractAmountRespVO>> getContractAmount(@Valid @RequestBody SearchYearReqVO reqVO) {
        return success(contractAmountService.getContractAmount(reqVO));
    }

    /**
     * 合同签约类型 统计 展示(饼状图)
     */
    @PostMapping("/contractTypeSignedStatistic")
    @Operation(summary = "合同签约类型 统计 展示(饼状图)")
    @OperateLog(logArgs = false)
    public CommonResult<ContractTypeSignedStatisticRespVO> contractTypeSignedStatistic(SearchYearReqVO vo) {
        return success(contractAmountService.contractTypeSignedStatistic(vo));
    }

    /**
     * 工作台-待办提醒
     */
    @PostMapping(value = "listAlert")
    public CommonResult<GPMallBigAlertListRespVO> listAlert(@RequestBody GPMallAlertListReqVO vo) {
        return CommonResult.success(contractAmountService.listAlert(vo));
    }
}
