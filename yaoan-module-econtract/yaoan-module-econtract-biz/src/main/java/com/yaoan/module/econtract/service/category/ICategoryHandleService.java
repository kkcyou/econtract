package com.yaoan.module.econtract.service.category;


import cn.hutool.core.lang.tree.Tree;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author doujl
 * @since 2023-07-24
 */
public interface ICategoryHandleService<T> {
    Integer saveCategory(T paramCategoryQo);
    Integer updateById(T paramCategoryQo);
    void deleteById(String id);
    List<Tree<String>> queryAllCategory(String type);
    T queryCategoryById( String id);
}
