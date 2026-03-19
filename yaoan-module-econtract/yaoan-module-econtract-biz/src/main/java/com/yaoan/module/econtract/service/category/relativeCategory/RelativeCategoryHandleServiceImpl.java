package com.yaoan.module.econtract.service.category.relativeCategory;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.enums.CategoryTypeCodeConstants;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryListRespVO;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryReqVO;
import com.yaoan.module.econtract.convert.category.CategoryConverter;
import com.yaoan.module.econtract.dal.dataobject.category.RelativeCategory;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.mysql.category.RelativeCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.enums.EntityTypeEnums;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.service.category.ICategoryHandleService;
import com.yaoan.module.econtract.util.DBExistUtil;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
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
public class RelativeCategoryHandleServiceImpl implements ICategoryHandleService<CategoryReqVO> {
    @Resource
    RelativeCategoryMapper relativeCategoryMapper;
    @Resource
    private CategoryConverter categoryConverter;
    @Resource
    private RelativeMapper relativeMapper;
    @Override
    public Integer saveCategory(CategoryReqVO categoryReqVO) {
        String entityType = entityType(categoryReqVO.getType());
        DBExistUtil.isExist(categoryReqVO.getId(),categoryReqVO.getName(),categoryReqVO.getCode(),entityType,relativeCategoryMapper);
        // 1.插入参数分类信息
        RelativeCategory relativeCategory = categoryConverter.toRelativeCategory(categoryReqVO);
        relativeCategory.setEntityType(entityType);
        relativeCategoryMapper.insert(relativeCategory);
        return relativeCategory.getId();
    }

    @Override
    public Integer updateById(CategoryReqVO categoryReqVO) {
        String entityType = entityType(categoryReqVO.getType());
        DBExistUtil.isExist(categoryReqVO.getId(),categoryReqVO.getName(),categoryReqVO.getCode(),entityType,relativeCategoryMapper);
        //2.1 修改参数分类  1
        RelativeCategory relativeCategory = categoryConverter.toRelativeCategory(categoryReqVO);
        relativeCategory.setEntityType(entityType);
        relativeCategoryMapper.updateById(relativeCategory);
        return relativeCategory.getId();
    }

    @Override
    public void deleteById(String id) {
        // 1.删除参数分类信息
        List<Relative> relatives = relativeMapper.selectList(new LambdaQueryWrapperX<Relative>().eq(Relative::getCategoryId, id));
        if(CollectionUtil.isNotEmpty(relatives)){
            throw exception(ErrorCodeConstants.DATA_IS_CALL);
        }
        relativeCategoryMapper.deleteById(id);
    }

    @Override
    public List<Tree<String>> queryAllCategory(String type) {
        // 1.查询参数分类信息  1
        String entityType = entityType(type);
        List<CategoryListRespVO> categoryListRespVOS = categoryConverter.relativeCategoryToList(relativeCategoryMapper.selectList(new LambdaQueryWrapperX<RelativeCategory>().eqIfPresent(RelativeCategory::getEntityType,entityType)));
        List<Tree<String>> categoryListRespVOList = DBExistUtil.getCategoryListRespVOList(categoryListRespVOS);
        return categoryListRespVOList;
    }

    @Override
    public CategoryReqVO queryCategoryById(String id) {
        // 1.查询参数分类信息  1
        CategoryReqVO  categoryBaseVO = categoryConverter.relativeCategoryToVO(relativeCategoryMapper.selectById(id));
        return categoryBaseVO;
    }


    private  String entityType(String type){
        String entityType=null;
        switch (CategoryTypeCodeConstants.getInstance(type)) {
            case RELATIVE_PERSONAL_CATEGORY:
                entityType= EntityTypeEnums.INDIVIDUAL.getCode();
                break ;
            case RELATIVE_COMPANY_CATEGORY:
                entityType= EntityTypeEnums.COMPANY.getCode();
                break;
            case RELATIVE_GOVERMENT_CATEGORY:
                entityType= EntityTypeEnums.ORGANIZATION.getCode();
                break;
        }
        return  entityType;
    }
}
