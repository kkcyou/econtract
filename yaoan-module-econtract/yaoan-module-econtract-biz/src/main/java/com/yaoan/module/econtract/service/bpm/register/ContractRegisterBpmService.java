package com.yaoan.module.econtract.service.bpm.register;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.register.vo.BigContractRegisterListApproveRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.register.vo.ContractRegisterListApproveReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/26 10:58
 */
public interface ContractRegisterBpmService {

    String submit(Long loginUserId, IdReqVO reqVO);

   BigContractRegisterListApproveRespVO getBpmAllTaskPage(Long loginUserId, ContractRegisterListApproveReqVO pageVO);

   BigContractRegisterListApproveRespVO getBpmDoneTaskPage(Long loginUserId, ContractRegisterListApproveReqVO pageVO);

   BigContractRegisterListApproveRespVO getBpmToDoTaskPage(Long loginUserId, ContractRegisterListApproveReqVO pageVO);

    String submitBatch(Long loginUserId, IdReqVO vo);
}
