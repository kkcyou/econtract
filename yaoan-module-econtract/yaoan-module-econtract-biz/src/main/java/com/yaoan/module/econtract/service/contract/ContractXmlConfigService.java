package com.yaoan.module.econtract.service.contract;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractXmlConfigCreateVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractXmlConfigPageVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractXmlConfigQueryVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractXmlConfigVO;


public interface ContractXmlConfigService {
    String addContractXmlInfo(ContractXmlConfigCreateVO contractXmlInfo);

    void deleteContractXmlInfo(String id);

    String updateContractXmlInfo(ContractXmlConfigCreateVO contractXmlInfoDO);

    ContractXmlConfigVO getContractXmlInfoById(String id);

    PageResult<ContractXmlConfigPageVO> getAllContractXmlInfos(ContractXmlConfigQueryVO contractXmlInfoDO);
}
