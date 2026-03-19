package com.yaoan.module.econtract.convert.terminate;

import com.yaoan.module.econtract.controller.admin.terminate.vo.TerminateContractCreateReqVO;
import com.yaoan.module.econtract.dal.dataobject.terminate.TerminateContractDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TerminateContractConvert {
    TerminateContractConvert INSTANCE = Mappers.getMapper(TerminateContractConvert.class);

    TerminateContractDO toEntity(TerminateContractCreateReqVO bean);
}
