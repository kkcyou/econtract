package com.yaoan.module.econtract.service.category.templateCategory;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryListRespVO;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryReqVO;
import com.yaoan.module.econtract.convert.category.CategoryConverter;
import com.yaoan.module.econtract.dal.dataobject.category.TemplateCategory;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.ContractTemplate;
import com.yaoan.module.econtract.dal.mysql.category.TemplateCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.contracttemplate.ContractTemplateMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.service.category.ICategoryHandleService;
import com.yaoan.module.econtract.util.DBExistUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author doujl
 * @since 2023-07-24
 */
@Service
public class TemplateCategoryHandleServiceImpl implements ICategoryHandleService<CategoryReqVO> {
    @Resource
    TemplateCategoryMapper templateCategoryMapper;
    @Resource
    private CategoryConverter categoryConverter;
    @Resource
    private ContractTemplateMapper templateMapper;
    @Override
    public Integer saveCategory(CategoryReqVO categoryReqVO) {
        DBExistUtil.isExist(categoryReqVO.getId(),categoryReqVO.getName(),categoryReqVO.getCode(),null,templateCategoryMapper);
        // 1.插入参数分类信息
        TemplateCategory templateCategory = categoryConverter.toTemplateCategory(categoryReqVO);
        templateCategoryMapper.insert(templateCategory);
        return templateCategory.getId();
    }

    @Override
    public Integer updateById(CategoryReqVO categoryReqVO) {
        DBExistUtil.isExist(categoryReqVO.getId(),categoryReqVO.getName(),categoryReqVO.getCode(),null,templateCategoryMapper);
        //2.1 修改参数分类  1
        // 如果是通用分类数据不允许修改
        TemplateCategory templateCategory = templateCategoryMapper.selectById(categoryReqVO.getId());
        if (ObjectUtil.isNotEmpty(templateCategory) && "0".equals(String.valueOf(templateCategory.getTenantId()))){
            throw exception(ErrorCodeConstants.DIY_ERROR,"通用模板分类不允许修改");
        }
        templateCategoryMapper.updateById(categoryConverter.toTemplateCategory(categoryReqVO));
        return categoryReqVO.getId();
    }

    @Override
    public void deleteById(String id) {
        // 1.删除参数分类信息
        List<ContractTemplate> templates = templateMapper.selectList(new LambdaQueryWrapperX<ContractTemplate>().eq(ContractTemplate::getTemplateCategoryId, id));
        if(CollectionUtil.isNotEmpty(templates)){
            throw exception(ErrorCodeConstants.DATA_IS_CALL);
        }
        // 如果是通用分类数据不允许删除
        List<Long> deleteDataTenantIds = templates.stream().map(TenantBaseDO::getTenantId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(deleteDataTenantIds) && deleteDataTenantIds.contains(0L)){
            throw exception(ErrorCodeConstants.DIY_ERROR,"通用模板分类不允许删除");
        }
        templateCategoryMapper.deleteById(id);
    }

    @Override
    public List<Tree<String>> queryAllCategory(String type) {
        // 1.查询参数分类信息  1
        List<CategoryListRespVO> categoryListRespVOS = categoryConverter.templateCategoryToList(templateCategoryMapper.selectMyCategoryList());
        List<Tree<String>> categoryListRespVOList = DBExistUtil.getCategoryListRespVOList(categoryListRespVOS);
        return categoryListRespVOList;
    }

    @Override
    public CategoryReqVO queryCategoryById(String id) {
        // 1.查询参数分类信息  1
        CategoryReqVO  categoryBaseVO = categoryConverter.templateCategoryToVO(templateCategoryMapper.selectById(id));
        return categoryBaseVO;
    }
}
