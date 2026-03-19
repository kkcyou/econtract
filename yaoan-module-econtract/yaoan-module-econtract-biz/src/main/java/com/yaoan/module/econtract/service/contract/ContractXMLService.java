package com.yaoan.module.econtract.service.contract;

import com.yaoan.module.econtract.controller.admin.contract.vo.ValidatePdfVO;

public interface ContractXMLService {
    String getContractXML(String id);

    Long saveContractXML(String id, String contractId);

    Object validateContractXML(ValidatePdfVO vo);

    String getContractXMLString(ValidatePdfVO vo);
}
