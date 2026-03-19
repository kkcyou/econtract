package com.yaoan.module.econtract.convert.annotation;

import com.yaoan.module.econtract.controller.admin.annotation.vo.AnnotationListRespVO;
import com.yaoan.module.econtract.controller.admin.annotation.vo.AnnotationSaveUpdateReqVO;
import com.yaoan.module.econtract.dal.dataobject.annotation.AnnotationDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/25 15:21
 */
@Mapper
public interface AnnotationConverter {

    AnnotationConverter INSTANCE = Mappers.getMapper(AnnotationConverter.class);

    List<AnnotationDO> listAnnoSaveUpdateReq2Entity(List<AnnotationSaveUpdateReqVO> vo);

    @Mapping(target = "annotationRange", source = "range")
    AnnotationDO annoSaveUpdateReq2Entity(AnnotationSaveUpdateReqVO vo);

    List<AnnotationListRespVO> listEntity2RespVO(List<AnnotationDO> annotationList);

    @Mapping(target = "range", source = "annotationRange")
    AnnotationListRespVO entity2RespVO(AnnotationDO entity);
}
