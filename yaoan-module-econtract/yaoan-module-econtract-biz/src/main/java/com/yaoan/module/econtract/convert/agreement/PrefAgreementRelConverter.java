package com.yaoan.module.econtract.convert.agreement;


import com.yaoan.module.econtract.controller.admin.agreement.vo.PrefAgreementRelCreateReqVO;
import com.yaoan.module.econtract.controller.admin.agreement.vo.PrefAgreementRelRespVO;
import com.yaoan.module.econtract.dal.dataobject.agreement.PrefAgreementRelDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "spring")
public interface PrefAgreementRelConverter {

    PrefAgreementRelConverter INSTANCE = Mappers.getMapper(PrefAgreementRelConverter.class);
    PrefAgreementRelDO toEntity(PrefAgreementRelCreateReqVO bean);

    PrefAgreementRelRespVO convert(PrefAgreementRelDO bean);

    List<PrefAgreementRelRespVO> convertList(List<PrefAgreementRelDO> beans);
}
