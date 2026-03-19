package com.yaoan.module.econtract.service.category;


import cn.hutool.core.lang.tree.Tree;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryReqVO;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author doujl
 * @since 2023-07-24
 */
public interface CategoryService {
    Integer saveParamCategory(CategoryReqVO paramCategoryQo);
    Integer saveParamCategoryV2(CategoryReqVO paramCategoryQo);

    void deleteById(String id, String type);
    void deleteByIdV2(String id, String type);

    List<Tree<String>> queryAllParamCategory(String type);
    List<Tree<String>> queryAllParamCategoryV2(String type);
    List<Tree<String>> queryAllParamCategoryV3();

    CategoryReqVO queryACategoryById( String id,String type);
    CategoryReqVO queryACategoryByIdV2( String id,String type);
}
