package com.yaoan.module.econtract.convert.gcy.gpmall;

import com.yaoan.module.econtract.controller.admin.gpx.contractVO.CommentCreateReqVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.CommentCreateRespVO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.CommentDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface CommentConverter {
    CommentConverter INSTANCE = Mappers.getMapper(CommentConverter.class);

    List<CommentDO> toEntityList(List<CommentCreateReqVO> bean);

    List<CommentCreateRespVO> toRespVOList(List<CommentDO> bean);
}
