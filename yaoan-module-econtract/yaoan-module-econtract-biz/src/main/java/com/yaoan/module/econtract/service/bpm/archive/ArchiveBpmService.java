package com.yaoan.module.econtract.service.bpm.archive;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.archive.vo.ArchiveBpmReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.archive.vo.BpmContractArchiveRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.archive.vo.PageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.archive.vo.PageRespVO;

public interface ArchiveBpmService {
    /**
     * 合同归档审批发起
     */
    String createProcess(Long loginUserId, ArchiveBpmReqVO reqVO);


    BpmContractArchiveRespVO getBpmAllTaskPage(Long loginUserId, PageReqVO pageVO);

    BpmContractArchiveRespVO getBpmDoneTaskPage(Long loginUserId, PageReqVO pageVO);

    BpmContractArchiveRespVO getBpmToDoTaskPage(Long loginUserId, PageReqVO pageVO);
}
