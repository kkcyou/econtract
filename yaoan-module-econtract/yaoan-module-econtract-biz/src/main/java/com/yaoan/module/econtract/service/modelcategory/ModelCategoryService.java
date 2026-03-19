package com.yaoan.module.econtract.service.modelcategory;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.api.modelcategory.dto.ModelCategoryDTO;
import com.yaoan.module.econtract.dal.dataobject.modelcategory.ModelCategory;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/3 15:39
 */
public interface ModelCategoryService extends IService<ModelCategory> {

    PageResult<ModelCategory> getModelCategoryPage(ModelCategoryDTO modelDTO);

    int insertmodelCategory(ModelCategoryDTO modelDTO);
    /**
     * 修改模板分类
     */
    int updateModelCategory(ModelCategoryDTO modelCategoryDTO);
    /**
     * 根据模板分类id获取模板分类
     */
    ModelCategory getModelCategoey(Integer id);
    List<ModelCategory> getModelCategorys(List<Integer> ids);
}
