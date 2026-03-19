package com.yaoan.module.econtract.convert.freezed;

import com.yaoan.module.econtract.controller.admin.freezed.vo.FreezedContractCreateReqVO;
import com.yaoan.module.econtract.dal.dataobject.freezed.FreezedContractDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FreezedContractConvert {
    FreezedContractConvert INSTANCE = Mappers.getMapper(FreezedContractConvert.class);

    FreezedContractDO toEntity(FreezedContractCreateReqVO bean);
}
