package com.yaoan.module.econtract.service.ledger;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractPageReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractPageRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.PrefRespVO;
import com.yaoan.module.econtract.controller.admin.ledger.vo.*;
import com.yaoan.module.econtract.controller.admin.ledger.vo.baseInfo.LedgerBaseInformationRespVO;
import com.yaoan.module.econtract.controller.admin.ledger.vo.document.LedgerContractDocumentRespVO;
import com.yaoan.module.econtract.controller.admin.ledger.vo.flowable.LedgerFlowableRecordRespVO;
import com.yaoan.module.econtract.controller.admin.performance.performanceTask.vo.PerformanceTaskInfoRespVO;
import com.yaoan.module.econtract.dal.dataobject.ledger.Ledger;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author Pele
 * @description 针对表【ecms_ledger(台账表)】的数据库操作Service
 * @createDate 2023-08-06 23:21:59
 */
public interface LedgerService extends IService<Ledger> {

    public PageResult<LedgerPageRespVo> getLedgerPage(LedgerListReqVo dto);

    LedgerBaseInformationRespVO ledgerBaseInformation(LedgerContractIdReqVO vo);

    LedgerContractDocumentRespVO ledgerContractDocument(LedgerContractIdReqVO vo);

    PageResult<LedgerPerformTaskReRespVO> ledgerPerformTask(LedgerContractIdPageReqVO vo);

    LedgerModelInfoRespVO ledgerModelInfo(LedgerContractIdPageReqVO vo);

    List<LedgerRelationContractRespVO> ledgerRelationContract(LedgerContractIdReqVO vo) throws Exception;

    LedgerFlowableRecordRespVO ledgerFlowableRecord(LedgerContractIdReqVO vo);

    PageResult<LedgerPageRespVo> listLedgerFromContract(LedgerListReqVo vo);

    /**
     * 获取台账分页列表
     *
     * @param vo req
     * @return response
     */
    PageResult<ContractPageRespVO> getLedgerPageV2(LedgerPageReqV2VO vo);

    /**
     * 获取台账汇总
     *
     * @param vo req
     * @return response
     */
    Map<String, Object> getTotal(LedgerPageReqV2VO vo);

    LedgerQueryRespVO queryById4Ledger(@Valid PrefRespVO prefRespVO);
}
