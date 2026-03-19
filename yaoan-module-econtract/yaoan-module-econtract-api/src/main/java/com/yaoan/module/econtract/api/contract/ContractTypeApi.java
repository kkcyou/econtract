package com.yaoan.module.econtract.api.contract;

import com.yaoan.module.econtract.api.contracttype.dto.ContractTypeDTO;

import java.util.List;

public interface ContractTypeApi {
    // 为新租户创建默认合同类型
    List<ContractTypeDTO> createDefaultContractType(Long tenantId);
}
