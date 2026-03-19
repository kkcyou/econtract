package com.yaoan.module.econtract.convert.attachment;

import com.yaoan.module.econtract.controller.admin.contract.vo.AttachmentRelCreateReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.AttachmentRelDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AttachmentRelConverter {
    AttachmentRelConverter INSTANCE = Mappers.getMapper(AttachmentRelConverter.class);

    AttachmentRelDO convert(AttachmentRelCreateReqVO bean);
}
