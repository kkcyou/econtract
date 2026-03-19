package com.yaoan.module.econtract.service.category.contractStatusCategory;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryListRespVO;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryReqVO;
import com.yaoan.module.econtract.convert.category.CategoryConverter;
import com.yaoan.module.econtract.dal.dataobject.category.ContractCategory;
import com.yaoan.module.econtract.dal.dataobject.category.ContractStatusCategory;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.mysql.category.ContractCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.category.ContractStatusCategoryMapper;
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
 * @author lls
 * @since 2024-09-05
 */
@Service
public class ContractStatusCategoryHandleServiceImpl implements ICategoryHandleService<CategoryReqVO> {
    @Resource
    ContractStatusCategoryMapper contractStatusCategoryMapper;
    @Resource
    private CategoryConverter categoryConverter;
    @Resource
    private ContractMapper contractMapper;
    @Override
    public Integer saveCategory(CategoryReqVO categoryReqVO) {
        DBExistUtil.isExist(categoryReqVO.getId(),categoryReqVO.getName(),categoryReqVO.getCode(),null,contractStatusCategoryMapper);
        // 1.插入参数分类信息
        ContractStatusCategory contractStatusCategory = categoryConverter.toContractStatusCategory(categoryReqVO);
        contractStatusCategoryMapper.insert(contractStatusCategory);
        return contractStatusCategory.getId();
    }

    @Override
    public Integer updateById(CategoryReqVO categoryReqVO) {
        DBExistUtil.isExist(categoryReqVO.getId(),categoryReqVO.getName(),categoryReqVO.getCode(),null,contractStatusCategoryMapper);
        //2.1 修改参数分类  1
        contractStatusCategoryMapper.updateById(categoryConverter.toContractStatusCategory(categoryReqVO));
        return categoryReqVO.getId();
    }

    @Override
    public void deleteById(String id) {
        // 1.删除参数分类信息
        List<ContractDO> contracts = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>().eq(ContractDO::getContractCategory, id));
        if(CollectionUtil.isNotEmpty(contracts)){
            throw exception(ErrorCodeConstants.DATA_IS_CALL);
        }
        contractStatusCategoryMapper.deleteById(id);
    }

    @Override
    public List<Tree<String>> queryAllCategory(String type) {
        // 1.查询参数分类信息  1
        List<CategoryListRespVO> categoryListRespVOS = categoryConverter.contractStatusCategoryToList(contractStatusCategoryMapper.selectList());
        List<Tree<String>> categoryListRespVOList = DBExistUtil.getCategoryListRespVOListWithTime(categoryListRespVOS);
        return categoryListRespVOList;
    }

    @Override
    public CategoryReqVO queryCategoryById(String id) {
        // 1.查询参数分类信息  1
        CategoryReqVO  categoryBaseVO = categoryConverter.contractStatusCategoryToVO(contractStatusCategoryMapper.selectById(id));
        return categoryBaseVO;
    }
}
