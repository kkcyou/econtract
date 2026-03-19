package com.yaoan.module.econtract.service.contract;

import com.yaoan.module.econtract.controller.admin.contract.vo.*;
import com.yaoan.module.econtract.controller.admin.contract.vo.extraction.ContractExtractRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.extraction.TaskIdReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.extraction.UploadByFileIdReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.extraction.UploadReqVO;

import java.util.List;

public interface ContractExtractionService {
    /**
     * 智能填写映射code 表单
     *
     * @param formToJsonReqVO
     * @return
     */
    List<JsonFormRespVO> toJsonForm(FormToJsonReqVO formToJsonReqVO) throws Exception;


    TokenRespVO generateToken();

    TaskIdRespVO upload(UploadReqVO vo) throws Exception;

    ContractExtractRespVO detail(TaskIdReqVO vo);

    ContractDetailRespVO getDetail(String taskId);

    TaskIdRespVO uploadByFileId(UploadByFileIdReqVO vo) throws Exception;
}
