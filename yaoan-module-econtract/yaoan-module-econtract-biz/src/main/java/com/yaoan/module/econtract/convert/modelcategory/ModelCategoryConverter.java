package com.yaoan.module.econtract.convert.modelcategory;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.api.model.dto.ModelDTO;
import com.yaoan.module.econtract.api.modelcategory.dto.ModelCategoryDTO;
import com.yaoan.module.econtract.controller.admin.catalog.vo.ModelIdVO;
import com.yaoan.module.econtract.dal.dataobject.catalog.ModelCatalogDo;
import com.yaoan.module.econtract.dal.dataobject.model.SimpleModel;
import com.yaoan.module.econtract.dal.dataobject.modelcategory.ModelCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/3 16:10
 */
@Mapper
public interface ModelCategoryConverter {
    ModelCategoryConverter INSTANCE = Mappers.getMapper(ModelCategoryConverter.class);

    ModelCategory toEntity(ModelCategoryDTO modelDTO);
    ModelCategoryDTO toDTO(ModelCategory   modelCategory);

    List<ModelCategoryDTO> toOutputList(List<ModelCategory> demos);

    List<ModelCategory> toOutputModels(List<ModelCategoryDTO> demos);

    PageResult<ModelCategoryDTO> convertModelCategoryPage(PageResult<ModelCategory> page);

    List<ModelIdVO> modelToModelVO(List<SimpleModel> simpleModel);

    @Mapping(source = "simpleModel.id", target = "modelId")
    @Mapping(source = "simpleModel.name", target = "modelName")
    @Mapping(source = "simpleModel.code", target = "modelCode")
    ModelIdVO modelToModelVOSimple(SimpleModel simpleModel);

    List<ModelIdVO> toModelVO(List<ModelCatalogDo> modelCatalogDos);
}
