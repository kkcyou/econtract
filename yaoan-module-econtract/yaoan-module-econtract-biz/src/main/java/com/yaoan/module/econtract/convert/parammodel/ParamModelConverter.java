package com.yaoan.module.econtract.convert.parammodel;

import com.yaoan.module.econtract.controller.admin.model.vo.ParamModelVo;
import com.yaoan.module.econtract.dal.dataobject.paramModel.ParamModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/17 14:45
 */
@Mapper
public interface ParamModelConverter {
    ParamModelConverter INSTANCE = Mappers.getMapper(ParamModelConverter.class);
    List<ParamModel> toEntityList(List<ParamModelVo> bean);

}
