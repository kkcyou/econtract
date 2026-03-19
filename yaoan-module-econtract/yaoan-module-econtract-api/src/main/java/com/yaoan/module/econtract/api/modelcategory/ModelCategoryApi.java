package com.yaoan.module.econtract.api.modelcategory;


import com.yaoan.module.econtract.api.modelcategory.dto.ModelCategoryDTO;

import java.util.List;

/**
 * 供应商 用户 API 接口
 *
 * @author 芋道源码
 */
public interface ModelCategoryApi {


    /**
     * 新增模板分类
     *
     * @param modelCategoryDTO 模板分类DTO对象
     * @return 查询供应商信息
     */
    int insertModelCategory(ModelCategoryDTO modelCategoryDTO);
    /**
     * 修改模板分类
     */
    int updateModelCategory(ModelCategoryDTO modelCategoryDTO);
    /**
     * 根据模板分类id获取模板分类
     */
    ModelCategoryDTO getModelCategoey(Integer id);
    List<ModelCategoryDTO> getModelCategoey(List<Integer> ids);
}
