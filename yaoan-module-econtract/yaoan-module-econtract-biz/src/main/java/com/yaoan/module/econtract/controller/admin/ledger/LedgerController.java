package com.yaoan.module.econtract.controller.admin.ledger;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractPageReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractPageRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.PrefRespVO;
import com.yaoan.module.econtract.controller.admin.ledger.vo.*;
import com.yaoan.module.econtract.controller.admin.ledger.vo.baseInfo.LedgerBaseInformationRespVO;
import com.yaoan.module.econtract.controller.admin.ledger.vo.document.LedgerContractDocumentRespVO;
import com.yaoan.module.econtract.controller.admin.ledger.vo.flowable.LedgerFlowableRecordRespVO;
import com.yaoan.module.econtract.service.ledger.LedgerService;
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
import java.util.Map;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 台账模块
 */
@Slf4j
@RestController
@RequestMapping("econtract/ledger")
@Validated
@Tag(name = "台账模块", description = "台账模块操作接口")
public class LedgerController {
    @Resource
    private LedgerService ledgerService;

    @PostMapping("/page")
    @OperateLog(logArgs = false)
    @Operation(summary = "台账模块分页列表")
    public CommonResult<PageResult<LedgerPageRespVo>> getPostPage(@RequestBody LedgerListReqVo vo) {
        return success(ledgerService.getLedgerPage(vo));
    }

    @PostMapping("/total")
    @OperateLog(logArgs = false)
    @Operation(summary = "台账模块汇总数据")
    public CommonResult<Map<String,Object>> getTotal(@RequestBody LedgerPageReqV2VO vo) {
        return success(ledgerService.getTotal(vo));
    }

    @PostMapping("/page/v2")
    @OperateLog(logArgs = false)
    @Operation(summary = "台账模块分页列表")
    public CommonResult<PageResult<ContractPageRespVO>> getLedgerPage(@RequestBody LedgerPageReqV2VO vo) {
        return success(ledgerService.getLedgerPageV2(vo));
    }

    /**
     * 台账_基本信息
     */
    @PostMapping("/ledgerBaseInformation")
    @OperateLog(logArgs = false)
    @Operation(summary = "台账_基本信息")
    public CommonResult<LedgerBaseInformationRespVO> ledgerBaseInformation(@RequestBody LedgerContractIdReqVO vo) {
        return success(ledgerService.ledgerBaseInformation(vo));
    }

    /**
     * 台账_合同文件
     */
    @PostMapping("/ledgerContractDocument")
    @OperateLog(logArgs = false)
    @Operation(summary = "台账_合同文件")
    public CommonResult<LedgerContractDocumentRespVO> ledgerContractDocument(@RequestBody LedgerContractIdReqVO vo) {
        return success(ledgerService.ledgerContractDocument(vo));
    }

    /**
     * 台账_履约任务
     */
    @PostMapping("/ledgerPerformTask")
    @OperateLog(logArgs = false)
    @Operation(summary = "台账_履约任务")
    public CommonResult<PageResult<LedgerPerformTaskReRespVO>> ledgerPerformTask(@RequestBody LedgerContractIdPageReqVO vo) {
        return success(ledgerService.ledgerPerformTask(vo));
    }

    /**
     * 台账_模板信息
     */
    @PostMapping("/ledgerModelInfo")
    @OperateLog(logArgs = false)
    @Operation(summary = "台账_模板信息")
    public CommonResult<LedgerModelInfoRespVO> ledgerModelInfo(@RequestBody LedgerContractIdPageReqVO vo) {
        return success(ledgerService.ledgerModelInfo(vo));
    }

    /**
     * 台账_关联合同
     */
    @PostMapping("/ledgerRelationContract")
    @OperateLog(logArgs = false)
    @Operation(summary = "台账_关联合同")
    public CommonResult<List<LedgerRelationContractRespVO>> ledgerRelationContract(@RequestBody LedgerContractIdReqVO vo) throws Exception {
        return success(ledgerService.ledgerRelationContract(vo));
    }

    /**
     * 台账_流程记录
     */
    @PostMapping("/ledgerFlowableRecord")
    @OperateLog(logArgs = false)
    @Operation(summary = "台账_流程记录")
    public CommonResult<LedgerFlowableRecordRespVO> ledgerFlowableRecord(@RequestBody LedgerContractIdReqVO vo) {
        return success(ledgerService.ledgerFlowableRecord(vo));
    }

    /**
     * 新台账列表(从合同表查)
     * */
    @PostMapping("/listLedgerFromContract")
    @OperateLog(logArgs = false)
    @Operation(summary = "台账模块分页列表(从合同表查)")
    public CommonResult<PageResult<LedgerPageRespVo>> listLedgerFromContract(@RequestBody LedgerListReqVo vo) {
        return success(ledgerService.listLedgerFromContract(vo));
    }


    /**
     * 台账 合同查看  所有合同查看接口
     *
     * @param prefRespVO
     * @return
     */
    @PostMapping(value = "/queryById4Ledger/")
    @Operation(summary = "根据Id查看合同")
    public CommonResult<LedgerQueryRespVO> queryById4Ledger(@Valid @RequestBody PrefRespVO prefRespVO) throws Exception {
        LedgerQueryRespVO result = ledgerService.queryById4Ledger(prefRespVO);
        return success(result);
    }

}
