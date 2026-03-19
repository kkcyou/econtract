package com.yaoan.module.econtract.dal.mysql.category;


import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.dal.dataobject.category.TemplateCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/3 15:38
 */
@Mapper
public interface TemplateCategoryMapper extends BaseMapperX<TemplateCategory> {
    default List<TemplateCategory> selectMyCategoryList() {
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        LambdaQueryWrapperX<TemplateCategory> wrapper = new LambdaQueryWrapperX<TemplateCategory>();
        //当前用户的租户id和租户id为0
        wrapper.and(
                w -> w.eq(TemplateCategory::getTenantId, loginUser != null ? loginUser.getTenantId() : null)
                        .or()
                        .eq(TemplateCategory::getTenantId, 0));
        return selectList(wrapper);
    }

}
