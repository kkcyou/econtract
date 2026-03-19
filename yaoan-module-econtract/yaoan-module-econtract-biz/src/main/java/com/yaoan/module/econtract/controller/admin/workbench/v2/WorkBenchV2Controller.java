package com.yaoan.module.econtract.controller.admin.workbench.v2;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.controller.admin.workbench.v2.vo.ContractDataReqVO;
import com.yaoan.module.econtract.controller.admin.workbench.v2.vo.ContractDataRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.v2.vo.ContractPerformReqVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.statistic.WorkbenchStatisticRespVO;
import com.yaoan.module.econtract.service.workbench.v2.WorkBenchV2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description:
 * @author: Pele
 * @date: 2025/2/11 10:20
 */
@Slf4j
@RestController
@RequestMapping("econtract/agency/workbench/v2")
@Validated
@Tag(name = "单位端_工作台v2", description = "单位端_工作台v2")
public class WorkBenchV2Controller {

    @Resource
    private WorkBenchV2Service workBenchV2Service;

    /**
     * 单位端_工作台_合同数据接口
     * 当前人发起的所有状态没到通过的所有合同
     *
     * @param vo
     * @return {@link CommonResult }<{@link PageResult }<{@link ContractDataRespVO }>>
     */
    @PostMapping("/data4contract")
    @Operation(summary = "单位端_工作台_合同数据接口")
    public CommonResult<PageResult<ContractDataRespVO>> workbenchContractData(@Validated @RequestBody ContractDataReqVO vo) {
        return success(workBenchV2Service.workbenchContractData(vo));
    }

    /**
     * 单位端_工作台_合同履约接口
     * 根据一级合同类型找到所有状态的合同
     *
     * @param vo
     * @return {@link CommonResult }<{@link PageResult }<{@link ContractDataRespVO }>>
     */
    @PostMapping("/contractPerform")
    @Operation(summary = "单位端_工作台_合同履约接口")
    public CommonResult<PageResult<ContractDataRespVO>> contractPerform(@Validated@RequestBody ContractPerformReqVO vo) {
        return success(workBenchV2Service.contractPerform(vo));
    }

}
