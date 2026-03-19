package com.yaoan.module.system.convert.dept;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.system.api.dept.dto.PostDTO;
import com.yaoan.module.system.controller.admin.dept.vo.post.*;
import com.yaoan.module.system.dal.dataobject.dept.PostDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;

@Mapper
public interface PostConvert {

    PostConvert INSTANCE = Mappers.getMapper(PostConvert.class);

    List<PostSimpleRespVO> convertList02(List<PostDO> list);

    PageResult<PostRespVO> convertPage(PageResult<PostDO> page);

    PostRespVO convert(PostDO id);

    PostDO convert(PostCreateReqVO bean);

    PostDO convert(PostUpdateReqVO reqVO);

    List<PostExcelVO> convertList03(List<PostDO> list);

    List<PostDTO> toDTOS(List<PostDO> list);

    Map<String, PostDTO> mapDO2DTO(Map<String, PostDO> postDOMap);

    PostDTO entity2DTO(PostDO entity);

    List<PostDTO> listEntity2DTO(List<PostDO> list);
}
