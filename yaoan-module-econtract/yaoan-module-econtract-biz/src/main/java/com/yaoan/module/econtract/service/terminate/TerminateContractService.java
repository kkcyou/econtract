package com.yaoan.module.econtract.service.terminate;

import com.yaoan.module.econtract.controller.admin.terminate.vo.TerminateContractCreateReqVO;

public interface TerminateContractService {
    String create(TerminateContractCreateReqVO terminateContractCreateReqVO) throws Exception;

    String createV2(TerminateContractCreateReqVO terminateContractCreateReqVO) throws Exception;
}
