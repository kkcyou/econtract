package com.yaoan.module.econtract.service.freezed;

import com.yaoan.module.econtract.controller.admin.freezed.vo.FreezedContractCreateReqVO;

/**
 * 合同冻结
 * 
 */


public interface FreezedContractService {
    String create(FreezedContractCreateReqVO freezedContractCreateReqVO) throws Exception;

    String createV2(FreezedContractCreateReqVO freezedContractCreateReqVO) throws Exception;

    String unFreezed(FreezedContractCreateReqVO freezedContractCreateReqVO) throws Exception;

}
