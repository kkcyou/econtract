package com.yaoan.module.econtract.api.modelcategory;


import com.yaoan.module.econtract.api.modelcategory.dto.ClientModelCategoryDTO;

/**
 * 客户端模板分类关联表 API 接口
 *
 * @author zhc
 */
public interface ClientModelCategoryApi {


    /**
     * 新增客户端模板分类关联表
     *
     * @param clientModelCategoryDTO 客户端模板分类关联表DTO对象
     * @return 查询客户端模板分类关联表信息
     */
    int insertClientModelCategory(ClientModelCategoryDTO clientModelCategoryDTO);
    /**
     * 根据客户端id获取模板分类id
     */
    Integer getModelCategoryId(String clientId);
}
