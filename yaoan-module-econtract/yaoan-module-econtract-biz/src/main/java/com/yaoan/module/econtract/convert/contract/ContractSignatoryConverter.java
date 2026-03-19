package com.yaoan.module.econtract.convert.contract;

import com.yaoan.module.econtract.controller.admin.contract.vo.ContractSignatoryReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractSignatoryDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ContractSignatoryConverter {

    ContractSignatoryConverter INSTANCE = Mappers.getMapper(ContractSignatoryConverter.class);

    ContractSignatoryDO convert(ContractSignatoryReqVO contractSignatoryReqVO);
    List<ContractSignatoryReqVO> toContractSignatoryReqVOS(List<ContractSignatoryDO> contractSignatoryReqDOS);
}
