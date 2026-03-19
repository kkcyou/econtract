package com.yaoan.module.econtract.convert.contract;

import com.yaoan.module.econtract.controller.admin.contract.vo.ContractParameterVO;
import com.yaoan.module.econtract.controller.admin.outward.contract.VO.ContractParameterRespDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractParameterDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ContractParameterConverter {
    ContractParameterConverter INSTANCE = Mappers.getMapper(ContractParameterConverter.class);

    ContractParameterDO toEntity(ContractParameterVO contractParameterVO);
    List<ContractParameterVO> toContractParameterVOS(List<ContractParameterDO> contractParameterDOS);

    List<ContractParameterRespDO> convertList(List<ContractParameterDO> list);

}
