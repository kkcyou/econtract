package com.yaoan.module.econtract.util;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.QueryWrapperX;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import dm.jdbc.util.StringUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * @date
 * @description
 */
public class DBExistUtil<T> {
    /**
     * 校验名称，编码是否存在
     *
     * @param
     * @return
     */
    public static <T>  void isExist(Object id, String name, String code, String entityType, BaseMapperX<T> paramCategoryMapper) {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        Long tenantId = null;
        if(ObjectUtil.isNotEmpty(loginUser)){
            if(ObjectUtil.isNotEmpty(loginUser.getTenantId())){
                tenantId = loginUser.getTenantId();
            }
        }
        QueryWrapperX codeQueryWrapper = setCodeQueryWrapper(new QueryWrapperX(), id, code, entityType,tenantId);
        QueryWrapperX nameQueryWrapper = setNameQueryWrapper(new QueryWrapperX(), id, name, entityType,tenantId);
        boolean  nameFlag = paramCategoryMapper.selectCount(nameQueryWrapper) > 0;
        boolean  codeFlag = paramCategoryMapper.selectCount(codeQueryWrapper) > 0;
        if (nameFlag) {
            throw exception(ErrorCodeConstants.NAME_EXISTS);
        } else if (codeFlag) {
            throw exception(ErrorCodeConstants.CODE_EXISTS);
        }
    }

    /**
     *
     * @param paramMapper
     * @param id
     * @param name
     * @return
     */
    public static <T>  void isNameExist(Object id, String name, BaseMapperX<T> paramMapper) {
        QueryWrapperX nameQueryWrapper = setNameQueryWrapper(new QueryWrapperX(), id, name,null,null);
        boolean  nameFlag = paramMapper.selectCount(nameQueryWrapper) > 0;
        if (nameFlag) {
            throw exception(ErrorCodeConstants.NAME_EXISTS);
        }
    }
    /**
     *
     * @param paramMapper
     * @param id
     * @param code
     * @return
     */
    public static <T>  void isCodeExist(Object id, String code, BaseMapperX<T> paramMapper) {
        QueryWrapperX nameQueryWrapper = setCodeQueryWrapper(new QueryWrapperX(), id, code,null,null);
        boolean  codeFlag = paramMapper.selectCount(nameQueryWrapper) > 0;
        if (codeFlag) {
            throw exception(ErrorCodeConstants.CODE_EXISTS);
        }
    }
    private static QueryWrapperX setNameQueryWrapper(QueryWrapperX paramwrapper, Object id, String name,String entityType,Long tenantId) {
        paramwrapper.eqIfPresent("name", name);
        paramwrapper.neIfPresent("id", id);
        if(StringUtil.isNotEmpty(entityType)){
            paramwrapper.eqIfPresent("entity_type", entityType);
        }
        if(ObjectUtil.isNotEmpty(tenantId)){
            paramwrapper.eq("tenant_id", tenantId);
        }
        return paramwrapper;
    }
    private static  QueryWrapperX setCodeQueryWrapper(QueryWrapperX paramwrapper, Object id, String code,String entityType,Long tenantId) {
        paramwrapper.eqIfPresent("code", code);
        paramwrapper.neIfPresent("id", id);
        if(StringUtil.isNotEmpty(entityType)){
            paramwrapper.eqIfPresent("entity_type", entityType);
        }
        if(ObjectUtil.isNotEmpty(tenantId)){
            paramwrapper.eq("tenant_id", tenantId);
        }
        return paramwrapper;
    }
    public static <T> List<Tree<String>> getModelCategoryListRespVOList(List<T> categories) {
        // 配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名
        // treeNodeConfig.setWeightKey("order"); // 权重排序字段 默认为weight
        treeNodeConfig.setIdKey("id"); // 默认为id可以不设置
        treeNodeConfig.setNameKey("name"); // 节点名对应名称 默认为name
        treeNodeConfig.setParentIdKey("parentId"); // 父节点 默认为parentId
        treeNodeConfig.setChildrenKey("children"); // 子点 默认为children
        // treeNodeConfig.setDeep(3); // 可以配置递归深度 从0开始计算 默认此配置为空,即不限制
        // 转换器 "0" - 最顶层父id值 一般为0之类  categories – 分类集合
        List<Tree<String>> treeNodes = TreeUtil.build(categories, "0", treeNodeConfig,
                // treeNode – 源数据实体
                // tree – 树节点实体
                (treeNode, tree) -> {
                    tree.setId(getFieldValue(treeNode, "id").toString());
                    tree.setParentId(getFieldValue(treeNode, "parentId").toString());
                    tree.setName(getFieldValue(treeNode, "name").toString());
                    // 扩展属性 ...
                    tree.putExtra("code", getFieldValue(treeNode, "code"));
                    tree.putExtra("tenantId", getFieldValue(treeNode, "tenantId"));
                    // 判断是否有子节点，如果没有则手动添加一个空的子节点列表
                    if (tree.getChildren()== null || tree.getChildren().isEmpty()) {
                        tree.setChildren(new ArrayList<>());
                    }
                });
        return treeNodes;
    }
    public static <T> List<Tree<String>> getCategoryListRespVOList(List<T> categories) {
        // 配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名
        // treeNodeConfig.setWeightKey("order"); // 权重排序字段 默认为weight
        treeNodeConfig.setIdKey("id"); // 默认为id可以不设置
        treeNodeConfig.setNameKey("name"); // 节点名对应名称 默认为name
        treeNodeConfig.setParentIdKey("parentId"); // 父节点 默认为parentId
        treeNodeConfig.setChildrenKey("children"); // 子点 默认为children
        // treeNodeConfig.setDeep(3); // 可以配置递归深度 从0开始计算 默认此配置为空,即不限制
        // 转换器 "0" - 最顶层父id值 一般为0之类  categories – 分类集合
        List<Tree<String>> treeNodes = TreeUtil.build(categories, "0", treeNodeConfig,
                // treeNode – 源数据实体
                // tree – 树节点实体
                (treeNode, tree) -> {
                    tree.setId(getFieldValue(treeNode, "id").toString());
                    tree.setParentId(getFieldValue(treeNode, "parentId").toString());
                    tree.setName(getFieldValue(treeNode, "name").toString());
                    // 扩展属性 ...
                    tree.putExtra("code", getFieldValue(treeNode, "code"));
                    // 判断是否有子节点，如果没有则手动添加一个空的子节点列表
                    if (tree.getChildren()== null || tree.getChildren().isEmpty()) {
                        tree.setChildren(new ArrayList<>());
                    }
                });
        return treeNodes;
    }
    public static <T> List<Tree<String>> getCategoryListRespVOListWithTime(List<T> categories) {
        // 配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名
        // treeNodeConfig.setWeightKey("order"); // 权重排序字段 默认为weight
        treeNodeConfig.setIdKey("id"); // 默认为id可以不设置
        treeNodeConfig.setNameKey("name"); // 节点名对应名称 默认为name
        treeNodeConfig.setParentIdKey("parentId"); // 父节点 默认为parentId
        treeNodeConfig.setChildrenKey("children");
        // treeNodeConfig.setDeep(3); // 可以配置递归深度 从0开始计算 默认此配置为空,即不限制
        // 转换器 "0" - 最顶层父id值 一般为0之类  categories – 分类集合
        List<Tree<String>> treeNodes = TreeUtil.build(categories, "0", treeNodeConfig,
                // treeNode – 源数据实体
                // tree – 树节点实体
                (treeNode, tree) -> {
                    tree.setId(getFieldValue(treeNode, "id").toString());
                    tree.setParentId(getFieldValue(treeNode, "parentId").toString());
                    tree.setName(getFieldValue(treeNode, "name").toString());
                    // 扩展属性 ...
                    tree.putExtra("code", getFieldValue(treeNode, "code"));
                    tree.putExtra("createTime", getFieldValue(treeNode, "createTime"));
                    tree.putExtra("creator", getFieldValue(treeNode, "creator"));
                    tree.putExtra("creatorName", getFieldValue(treeNode, "creatorName"));
                    // 判断是否有子节点，如果没有则手动添加一个空的子节点列表
                    if (tree.getChildren()== null || tree.getChildren().isEmpty()) {
                        tree.setChildren(new ArrayList<>());
                    }
                });
        return treeNodes;
    }

    //shuaihua的新方法
    public static <T> List<Tree<String>> getCategoryListRespVOListV2(List<T> categories) {
        // 配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名
        // treeNodeConfig.setWeightKey("order"); // 权重排序字段 默认为weight
        treeNodeConfig.setIdKey("id"); // 默认为id可以不设置
        treeNodeConfig.setNameKey("name"); // 节点名对应名称 默认为name
        treeNodeConfig.setParentIdKey("parentId"); // 父节点 默认为parentId
        treeNodeConfig.setChildrenKey("children"); // 子点 默认为children
        // treeNodeConfig.setDeep(3); // 可以配置递归深度 从0开始计算 默认此配置为空,即不限制
        // 转换器 "0" - 最顶层父id值 一般为0之类  categories – 分类集合
        List<Tree<String>> treeNodes = TreeUtil.build(categories, "0", treeNodeConfig,
                // treeNode – 源数据实体
                // tree – 树节点实体
                (treeNode, tree) -> {
                    tree.setId(getFieldValue(treeNode, "id").toString());
                    tree.setParentId(getFieldValue(treeNode, "parentId").toString());
                    tree.setName(getFieldValue(treeNode, "name").toString());
                    // 扩展属性 ...
                    tree.putExtra("code", getFieldValue(treeNode, "code"));
                    tree.putExtra("codeRuleId", getFieldValue(treeNode, "codeRuleId"));
                    tree.putExtra("platId", getFieldValue(treeNode, "platId"));
                    // 判断是否有子节点，如果没有则手动添加一个空的子节点列表
                    if (tree.getChildren()== null || tree.getChildren().isEmpty()) {
                        tree.setChildren(new ArrayList<>());
                    }
                });
        return treeNodes;
    }
    private static <T> T getFieldValue(T obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

}
