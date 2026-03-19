package com.yaoan.module.econtract.service.bpm.contractborrow;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.*;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord.BigContractBorrowRecordPageRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord.BorrowRecordRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord.ContractBorrowRecordPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord.ContractBorrowRecordPageRespVO;
import com.yaoan.module.econtract.controller.admin.change.vo.ContractChangeListApproveRespVO;

import java.util.Map;

/**
 * @description:
 * @author: Pele
 * @date: 2023/10/8 21:32
 */
public interface ContractBorrowBpmService {

    /**
     * 保存 发起申请
     * @param loginUserId
     * @param reqVO
     * @return
     */
    String submitContractBorrowApproveFlowable(Long loginUserId, ContractBorrowBpmSubmitCreateReqVO reqVO);

    /**
     * 档案 发起审批
     */
    String submitArchiveApproveFlowable(Long loginUserId, ContractBorrowBpmSubmitCreateReqVO reqVO);

    /**
     * 发起工作流
     * @param loginUserId
     * @param id
     */
    void approve(Long loginUserId,String id);

    /**
     * 删除申请
     * @param id
     */
    void delete(String id);

    BigContractBorrowRecordPageRespVO getBpmAllTaskPage(Long loginUserId, ContractBorrowBpmPageReqVO pageVO);

    BigContractBorrowRecordPageRespVO getBpmDoneTaskPage(Long loginUserId, ContractBorrowBpmPageReqVO pageVO);

    BigContractBorrowRecordPageRespVO getBpmToDoTaskPage(Long loginUserId, ContractBorrowBpmPageReqVO pageVO);

    /**
     * 归档借阅列表 - 全部
     */
    BigContractBorrowRecordPageRespVO getBpmAllTaskPageArchive(Long loginUserId, ContractBorrowBpmPageReqVO pageVO);

    /**
     * 归档借阅列表 - 已审批
     */
    BigContractBorrowRecordPageRespVO getBpmDoneTaskPageArchive(Long loginUserId, ContractBorrowBpmPageReqVO pageVO);

    /**
     * 归档借阅列表 - 未审批
     */
    BigContractBorrowRecordPageRespVO getBpmToDoTaskPageArchive(Long loginUserId, ContractBorrowBpmPageReqVO pageVO);

    /**
     * 借阅台账 - 借阅类型统计
     * @return
     */
    Map<String,Long> getBorrowTypeCount();

    /**
     * 档案借阅 - 归还
     */
    void returnArchive(BorrowReturnReqVO vo);

    PageResult<ContractBorrowBpmPageRespVO> getBpmPage(ContractBorrowBpmPageReqVO reqVO);

    PageResult<BorrowRecordRespVO> getBorrowRecordPage(ContractBorrowRecordPageReqVO reqVO);

    /**
     * 借阅申请列表
     * @param reqVO
     * @return
     */
    PageResult<ContractBorrowBpmPageRespVO> getBorrowPage(ContractBorrowBpmPageReqVO reqVO);

    /**
     * 借阅申请详情
     * @param id
     * @return
     */
    BorrowRespVO getBorrowDetail(String id);

    PageResult<ContractBorrowBpmPageRespVO> getBorrowList(ContractBorrowBpmPageReqVO reqVO);

    BorrowTypeCountRespVO getBorrowTypeCountV2();

    PageResult<BorrowRecordRespVO> borrowLedger(ContractBorrowRecordPageReqVO reqVO);
}
