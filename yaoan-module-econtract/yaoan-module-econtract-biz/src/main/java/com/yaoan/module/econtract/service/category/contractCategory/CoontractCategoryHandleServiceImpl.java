package com.yaoan.module.econtract.service.category.contractCategory;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryListRespVO;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryReqVO;
import com.yaoan.module.econtract.convert.category.CategoryConverter;
import com.yaoan.module.econtract.dal.dataobject.category.ContractCategory;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.mysql.category.ContractCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
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
public class CoontractCategoryHandleServiceImpl implements ICategoryHandleService<CategoryReqVO> {
    @Resource
    ContractCategoryMapper contractCategoryMapper;
    @Resource
    private CategoryConverter categoryConverter;
    @Resource
    private ContractMapper contractMapper;
    @Override
    public Integer saveCategory(CategoryReqVO categoryReqVO) {
        DBExistUtil.isExist(categoryReqVO.getId(),categoryReqVO.getName(),categoryReqVO.getCode(),null,contractCategoryMapper);
        // 1.插入参数分类信息
        ContractCategory contractCategory = categoryConverter.toContractCategory(categoryReqVO);
        contractCategoryMapper.insert(contractCategory);
        return contractCategory.getId();
    }

    @Override
    public Integer updateById(CategoryReqVO categoryReqVO) {
        DBExistUtil.isExist(categoryReqVO.getId(),categoryReqVO.getName(),categoryReqVO.getCode(),null,contractCategoryMapper);
        //2.1 修改参数分类  1
        contractCategoryMapper.updateById(categoryConverter.toContractCategory(categoryReqVO));
        return categoryReqVO.getId();
    }

    @Override
    public void deleteById(String id) {
        // 1.删除参数分类信息
        List<ContractDO> contracts = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>().eq(ContractDO::getContractCategory, id));
        if(CollectionUtil.isNotEmpty(contracts)){
            throw exception(ErrorCodeConstants.DATA_IS_CALL);
        }
        contractCategoryMapper.deleteById(id);
    }

    @Override
    public List<Tree<String>> queryAllCategory(String type) {
        // 1.查询参数分类信息  1
        List<CategoryListRespVO> categoryListRespVOS = categoryConverter.contractCategoryToList(contractCategoryMapper.selectList());
        List<Tree<String>> categoryListRespVOList = DBExistUtil.getCategoryListRespVOList(categoryListRespVOS);
        return categoryListRespVOList;
    }

    @Override
    public CategoryReqVO queryCategoryById(String id) {
        // 1.查询参数分类信息  1
        CategoryReqVO  categoryBaseVO = categoryConverter.contractCategoryToVO(contractCategoryMapper.selectById(id));
        return categoryBaseVO;
    }
}
