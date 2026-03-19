package com.yaoan.module.econtract.convert.contract;


import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractXmlConfigCreateVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractXmlConfigPageVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ContractXmlConfigVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractXmlConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ContractXmlConfigConvert {
    ContractXmlConfigConvert INSTANCE = Mappers.getMapper(ContractXmlConfigConvert.class);

    ContractXmlConfigDO vo2DO(ContractXmlConfigVO contractXmlInfo);

    ContractXmlConfigVO do2VO(ContractXmlConfigDO info);

    PageResult<ContractXmlConfigPageVO> convertPage(PageResult<ContractXmlConfigDO> result);

    ContractXmlConfigDO createvo2DO(ContractXmlConfigCreateVO contractXmlInfo);
}
