package com.yaoan.module.econtract.dal.mysql.modelcategory;


import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.tenant.core.context.TenantContextHolder;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.module.econtract.api.modelcategory.dto.ModelCategoryDTO;
import com.yaoan.module.econtract.dal.dataobject.modelcategory.ModelCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/3 15:38
 */
@Mapper
public interface ModelCategoryMapper extends BaseMapperX<ModelCategory> {
    default PageResult<ModelCategory> selectPage(ModelCategoryDTO modelCategoryDTO) {
        Long tenantId = TenantContextHolder.getTenantId();
        return selectPage(modelCategoryDTO, new LambdaQueryWrapperX<ModelCategory>()
                .likeIfPresent(ModelCategory::getName, modelCategoryDTO.getName())
                .in(ModelCategory::getTenantId,tenantId,0)
        );
    }
    /**
     * 根据条件匹配查询参数信息
     * @return
     */
    default List<ModelCategory> selectList(Long companyId) {
        Long tenantId = TenantContextHolder.getTenantId();
        //companyId为0表示通用分类
        return selectList(new LambdaQueryWrapperX<ModelCategory>()
                .inIfPresent(ModelCategory::getCompanyId, companyId,0)
                .inIfPresent(ModelCategory::getTenantId, tenantId,0)
                .orderByAsc(ModelCategory::getTenantId,ModelCategory::getCode)
        );
    }
    default List<ModelCategory> selectList() {
        //tenantId
        Long tenantId = TenantContextHolder.getTenantId();
        return selectList(new LambdaQueryWrapperX<ModelCategory>()
                .in(ModelCategory::getTenantId, tenantId,0)
        );
    }
}
