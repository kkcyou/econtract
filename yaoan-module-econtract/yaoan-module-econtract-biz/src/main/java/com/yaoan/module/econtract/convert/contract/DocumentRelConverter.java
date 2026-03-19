package com.yaoan.module.econtract.convert.contract;

import com.yaoan.module.econtract.controller.admin.contract.vo.DocumentRelReqVo;
import com.yaoan.module.econtract.dal.dataobject.contract.DocumentRelDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DocumentRelConverter {
    DocumentRelConverter INSTANCE = Mappers.getMapper(DocumentRelConverter.class);

    DocumentRelDO convert(DocumentRelReqVo bean);
}
