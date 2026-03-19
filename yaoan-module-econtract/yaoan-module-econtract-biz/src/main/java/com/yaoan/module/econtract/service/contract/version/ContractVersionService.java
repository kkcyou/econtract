package com.yaoan.module.econtract.service.contract.version;

import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.BigContractVersionListRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.ContractVersionSaveReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/29 11:19
 */
public interface ContractVersionService {

    /**
     * 新增版本
     */
    public String save(ContractVersionSaveReqVO vo);

    /**
     * 根据合同id展示历史版本
     */
    public BigContractVersionListRespVO listByContractId(String contractId);


    String productBackContract(IdReqVO vo);
}
