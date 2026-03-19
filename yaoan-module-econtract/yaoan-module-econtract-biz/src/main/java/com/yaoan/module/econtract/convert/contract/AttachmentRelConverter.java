package com.yaoan.module.econtract.convert.contract;

import com.yaoan.module.econtract.controller.admin.contract.vo.AttachmentRelCreateReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.AttachmentRelRespVO;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import com.yaoan.module.econtract.dal.dataobject.contract.AttachmentRelDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AttachmentRelConverter {
    AttachmentRelConverter INSTANCE = Mappers.getMapper(AttachmentRelConverter.class);

    AttachmentRelDO convert(AttachmentRelCreateReqVO attachmentRelCreateReqVO);
    List<AttachmentRelCreateReqVO> toAttachmentRelCreateReqVO(List<AttachmentRelDO> attachmentRelCreateReqDOS);

    List<AttachmentRelRespVO> convert(List<AttachmentRelDO> attachmentRelDOList);

    List<AttachmentRelRespVO> convertV2(List<BusinessFileDO> attachmentRelDOList);
    @Mapping(target = "attachmentName",source = "fileName")
    @Mapping(target = "attachmentAddId",source = "fileId")
    AttachmentRelRespVO convertToVO(BusinessFileDO attachmentRelDOList);
}
