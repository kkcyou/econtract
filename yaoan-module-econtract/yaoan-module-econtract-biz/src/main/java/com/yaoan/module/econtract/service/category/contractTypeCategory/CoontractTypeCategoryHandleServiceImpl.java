package com.yaoan.module.econtract.service.category.contractTypeCategory;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryListRespVO;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryReqVO;
import com.yaoan.module.econtract.convert.category.CategoryConverter;
import com.yaoan.module.econtract.dal.dataobject.category.ContractTypeCategory;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.mysql.category.ContractTypeCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
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
public class CoontractTypeCategoryHandleServiceImpl implements ICategoryHandleService<CategoryReqVO> {
    @Resource
    ContractTypeCategoryMapper contractTypeCategoryMapper;
    @Resource
    private CategoryConverter categoryConverter;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Override
    public Integer saveCategory(CategoryReqVO categoryReqVO) {
        DBExistUtil.isExist(categoryReqVO.getId(),categoryReqVO.getName(),categoryReqVO.getCode(),null,contractTypeCategoryMapper);
        // 1.插入参数分类信息
        ContractTypeCategory contractTypeCategory = categoryConverter.toContractTypeCategory(categoryReqVO);
        contractTypeCategoryMapper.insert(contractTypeCategory);
        return contractTypeCategory.getId();
    }

    @Override
    public Integer updateById(CategoryReqVO categoryReqVO) {
        DBExistUtil.isExist(categoryReqVO.getId(),categoryReqVO.getName(),categoryReqVO.getCode(),null,contractTypeCategoryMapper);
        //2.1 修改参数分类  1
        contractTypeCategoryMapper.updateById(categoryConverter.toContractTypeCategory(categoryReqVO));
        return categoryReqVO.getId();
    }

    @Override
    public void deleteById(String id) {
        // 1.删除参数分类信息
        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>().eq(ContractType::getParentId, id));
        if(CollectionUtil.isNotEmpty(contractTypes)){
            throw exception(ErrorCodeConstants.DATA_IS_CALL);
        }
        contractTypeCategoryMapper.deleteById(id);
    }

    @Override
    public List<Tree<String>> queryAllCategory(String type) {
        // 1.查询参数分类信息  1
        List<CategoryListRespVO> categoryListRespVOS = categoryConverter.contractTypeCategoryToList(contractTypeCategoryMapper.selectList());
        List<Tree<String>> categoryListRespVOList = DBExistUtil.getCategoryListRespVOList(categoryListRespVOS);
        return categoryListRespVOList;
    }

    @Override
    public CategoryReqVO queryCategoryById(String id) {
        // 1.查询参数分类信息  1
        CategoryReqVO  categoryBaseVO = categoryConverter.contractTypeCategoryToVO(contractTypeCategoryMapper.selectById(id));
        return categoryBaseVO;
    }

}
