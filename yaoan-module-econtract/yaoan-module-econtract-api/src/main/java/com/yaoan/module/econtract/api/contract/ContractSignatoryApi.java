package com.yaoan.module.econtract.api.contract;

import com.yaoan.module.econtract.api.contract.dto.contract.SignatoryRelDTO;

import java.util.List;

/**
 * 合同签订API
 */
public interface ContractSignatoryApi {
    /**
     * 根据用户ID获取所有合同信息
     */
     List<SignatoryRelDTO> getContractSignatoryRelList(String userId, List<String> contractIds) ;
    /**
     * 批量修改合同绑定的签订人ID
     */
    void   updateContractSignatory(List<SignatoryRelDTO> signatoryRelList);

}
