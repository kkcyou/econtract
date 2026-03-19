package com.yaoan.module.econtract.service.signet;


import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.signet.vo.*;

import java.util.Date;
import java.util.List;

public interface SignetService {

    String increaseSignet(SignetCreateReqVO vo);

    PageResult<SignetPageRespVO> getSignetList(SignetPageReqVO vo);

    SignetDetailsRespVO getSignetDetails(SignetDetailsReqVO vo) throws Exception;

    void updateSignetStatus(SignetDetailsReqVO vo);

    List<SignetSpecsVO> getSignetSpecs();

    List<SignetTypeVO> getSignetType();


    List<SealProcessRespVO> getSignetProcessList();

    String createProcess(Long loginUserId, SealBpmReqVO reqVO);

    SignetManageRespVO getSignetManage(String id);

    PageResult<SignetProcessPageRespVO> getBpmAllTaskPage(Long loginUserId, SealApplicationListBpmReqVO pageVO);

    PageResult<SignetProcessPageRespVO> getBpmDoneTaskPage(Long loginUserId, SealApplicationListBpmReqVO pageVO);

    PageResult<SignetProcessPageRespVO> getBpmToDoTaskPage(Long loginUserId, SealApplicationListBpmReqVO pageVO);

    List<SignetDetailsRespVO> getEnableSignetList();
    List<SignetListRespVO> getSignetsByContractId(String contractId);

    String save(SealBpmReqVO reqVO) throws Exception;

    PageResult<SignetProcessPageRespVO> getSignetProcessPage(SealApplicationListBpmReqVO reqVO);

    void rejectSignetProcess(RejectSignetProcessReqVO reqVO);

    void delete(String id);

    void deleteSignet(String id);
    SignetAgentNumRespVO getAgentNum( SealApplicationListBpmReqVO pageVO);
}
