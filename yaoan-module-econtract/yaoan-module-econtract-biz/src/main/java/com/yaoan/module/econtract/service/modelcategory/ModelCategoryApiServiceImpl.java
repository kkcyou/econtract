package com.yaoan.module.econtract.service.modelcategory;

import cn.hutool.core.collection.CollectionUtil;
import com.yaoan.module.econtract.api.modelcategory.ModelCategoryApi;
import com.yaoan.module.econtract.api.modelcategory.dto.ModelCategoryDTO;
import com.yaoan.module.econtract.convert.modelcategory.ModelCategoryConverter;
import com.yaoan.module.econtract.dal.dataobject.modelcategory.ModelCategory;
import com.yaoan.module.econtract.dal.mysql.modelcategory.ModelCategoryMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/3 15:40
 */
@Service
public class ModelCategoryApiServiceImpl implements ModelCategoryApi {
    @Resource
    private ModelCategoryService modelCategoryService;
    @Resource
    private ModelCategoryMapper modelCategoryMapper;

    @Override
    public int insertModelCategory(ModelCategoryDTO modelCategoryDTO) {
        return modelCategoryService.insertmodelCategory(modelCategoryDTO);
    }

    @Override
    public int updateModelCategory(ModelCategoryDTO modelCategoryDTO) {
        return modelCategoryService.updateModelCategory(modelCategoryDTO);
    }

    @Override
    public ModelCategoryDTO getModelCategoey(Integer id) {
        ModelCategory modelCategoey = modelCategoryService.getModelCategoey(id);
        return ModelCategoryConverter.INSTANCE.toDTO(modelCategoey);
    }
    @Override
    public List<ModelCategoryDTO> getModelCategoey(List<Integer> ids) {
        if (CollectionUtil.isNotEmpty(ids)){
            List<ModelCategory> modelCategoey = modelCategoryService.getModelCategorys(ids);
            return ModelCategoryConverter.INSTANCE.toOutputList(modelCategoey);
        }
       return new ArrayList<>();
    }
}
