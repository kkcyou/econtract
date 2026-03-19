package com.yaoan.module.econtract.convert.contract;

import com.yaoan.module.econtract.api.contract.dto.contract.SignatoryRelDTO;
import com.yaoan.module.econtract.controller.admin.contract.vo.SignatoryRelReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SignatoryRelConverter {
    SignatoryRelConverter INSTANCE = Mappers.getMapper(SignatoryRelConverter.class);

    SignatoryRelDO convert(SignatoryRelReqVO signatoryRelReqVO);
    List<SignatoryRelReqVO> toSignatoryRelReqVOS(List<SignatoryRelDO> signatoryRelReqVO);
    List<SignatoryRelDTO> toSignatoryRelDTOS(List<SignatoryRelDO> signatoryRelReqVO);
    List<SignatoryRelDO> toSignatoryRelDOS(List<SignatoryRelDTO> signatoryRelReqVO);
}
