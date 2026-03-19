package com.yaoan.module.econtract.service.change;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.controller.admin.change.vo.*;
import com.yaoan.module.econtract.controller.admin.change.vo.changerisk.ChangeRiskListRespVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/24 17:17
 */
public interface ContractChangeService {
    String saveSupplement(ContractChangeSaveReqVO vo);


    String submitContractChangeApproveFlowable(Long loginUserId, IdReqVO reqVO);

    BigContractChangeListApproveRespVO getBpmAllTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO);

    BigContractChangeListApproveRespVO getBpmDoneTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO);

    BigContractChangeListApproveRespVO getBpmToDoTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO);

    /**
     * 合同管理 - 合同完结
     * 变更类型为   -关闭、取消、作废
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractChangePageRespVO>  getAutoBpmOverTaskPageFast(ContractChangePageReqVO contractPageReqVO);

    PageResult<ContractChangePageRespVO> listSubmitContractChange(ContractChangePageReqVO reqVO);

    List<ContractChangePageRespVO> listContractChangeByMainContractId(IdReqVO reqVO);

    String saveFastSupplement(ContractChangeSaveReqVO vo) throws Exception;

    String submitContractChangeApproveFlowableFast(Long loginUserId, IdReqVO reqVO);

    PageResult<ContractChangePageRespVO> listSubmitContractChangeFast(ContractChangePageReqVO reqVO);

    BigContractChangeListApproveRespVO getBpmAllTaskPageFast(Long loginUserId, CommonBpmAutoPageReqVO pageVO);

    BigContractChangeListApproveRespVO getBpmDoneTaskPageFast(Long loginUserId, CommonBpmAutoPageReqVO pageVO);

    BigContractChangeListApproveRespVO getBpmToDoTaskPageFast(Long loginUserId, CommonBpmAutoPageReqVO pageVO);

    List<ContractChangePageRespVO> listContractChangeByMainContractIdFast(IdReqVO reqVO);

    String updateFastSupplement(ContractChangeSaveReqVO vo);

    String deleteFastSupplement(IdReqVO vo);

    ContractChangeOneRespVO getContractChangeById(IdReqVO vo) throws Exception;

    String saveAndSubmitFastSupplement(ContractChangeSaveReqVO vo) throws Exception;

    String updateAndSubmitFastSupplement(ContractChangeSaveReqVO vo);

    PageResult<ContractChangeListRespVO> getContractChangeList(ContractChangeListReqVO reqVO);

    String applyStatusChange(ContractChangeStatusSaveReqVO vo) throws Exception;

    ContractChangeOneRespVO getContractChangeDetails(IdReqVO vo) throws Exception;

    String batchInitiateStatusChangeApproval(Long loginUserId, IdReqVO reqVO);

    List<ElementRespVO> getElementList();
    Long getContractChangeOrgtNum( CommonBpmAutoPageReqVO pageVO);

    Boolean checkContractTerminating(String id);

    ChangeRiskListRespVO changeRiskConfirmList(String id);
}
