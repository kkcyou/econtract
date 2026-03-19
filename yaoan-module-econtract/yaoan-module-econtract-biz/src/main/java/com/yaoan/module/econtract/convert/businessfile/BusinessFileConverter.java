package com.yaoan.module.econtract.convert.businessfile;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.module.econtract.controller.admin.businessfile.vo.BusinessFileSaveReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.BusinessFileVO;
import com.yaoan.module.econtract.controller.admin.wps.FileVO;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/26 16:31
 */
@Mapper
public interface BusinessFileConverter {
    BusinessFileConverter INSTANCE = Mappers.getMapper(BusinessFileConverter.class);

    List<BusinessFileDO> listR2D(List<BusinessFileSaveReqVO> createReqVO);

    List<BusinessFileVO> d2R(List<BusinessFileDO> fileDOList);

    List<FileVO> dto2RespList(List<FileDTO> fileDTOList);

    @Mapping(target = "id", expression = "java(long2String(fileDTO.getId()))")
    FileVO d2RespVO(FileDTO fileDTO);

    default String long2String(Long id){
        if(ObjectUtil.isEmpty(id)){
            return "";
        }
        return  String.valueOf(id);
    }
}
