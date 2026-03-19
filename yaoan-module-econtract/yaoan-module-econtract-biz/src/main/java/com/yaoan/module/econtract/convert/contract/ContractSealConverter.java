package com.yaoan.module.econtract.convert.contract;

import com.yaoan.module.econtract.controller.admin.contract.vo.ContractSealVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractSealDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ContractSealConverter {
    ContractSealConverter INSTANCE = Mappers.getMapper(ContractSealConverter.class);

    ContractSealDO toEntity(ContractSealVO contractSealVO);
    List<ContractSealVO> toContractSealVOS(List<ContractSealDO> contractSealDOS);
}
