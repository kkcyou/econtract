package com.yaoan.module.econtract.service.category.paramCategory;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryListRespVO;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryReqVO;
import com.yaoan.module.econtract.convert.category.CategoryConverter;
import com.yaoan.module.econtract.dal.dataobject.category.ParamCategory;
import com.yaoan.module.econtract.dal.dataobject.param.Param;
import com.yaoan.module.econtract.dal.mysql.category.ParamCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.param.ParamMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.service.category.ICategoryHandleService;
import com.yaoan.module.econtract.util.DBExistUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service
public class ParamCategoryHandleServiceImpl implements ICategoryHandleService<CategoryReqVO> {
    @Resource
    ParamCategoryMapper paramCategoryMapper;
    @Resource
    private CategoryConverter categoryConverter;
    @Resource
    private ParamMapper paramMapper;
    @Override
    public Integer saveCategory(CategoryReqVO categoryReqVO) {
        DBExistUtil.isExist(categoryReqVO.getId(),categoryReqVO.getName(),categoryReqVO.getCode(),null,paramCategoryMapper);
        // 1.插入参数分类信息
        ParamCategory paramCategory = categoryConverter.toParamCategory(categoryReqVO);
        paramCategoryMapper.insert(paramCategory);
        return paramCategory.getId();
    }

    @Override
    public Integer updateById(CategoryReqVO categoryReqVO) {
        DBExistUtil.isExist(categoryReqVO.getId(),categoryReqVO.getName(),categoryReqVO.getCode(),null,paramCategoryMapper);
        //2.1 修改参数分类  1
        paramCategoryMapper.updateById(categoryConverter.toParamCategory(categoryReqVO));
        return categoryReqVO.getId();
    }

    @Override
    public void deleteById(String id) {
        // 1.删除参数分类信息
        List<Param> params = paramMapper.selectList(new LambdaQueryWrapperX<Param>().eq(Param::getCategoryId, id));
        if(CollectionUtil.isNotEmpty(params)){
            throw exception(ErrorCodeConstants.DATA_IS_CALL);
        }
        paramCategoryMapper.deleteById(id);
    }

    @Override
    public List<Tree<String>> queryAllCategory(String type) {
        // 1.查询参数分类信息  1
        List<CategoryListRespVO> categoryListRespVOS = categoryConverter.paramCategoryToList(paramCategoryMapper.selectList());
        try {
        List<Tree<String>> categoryListRespVOList = DBExistUtil.getCategoryListRespVOList(categoryListRespVOS);
        return categoryListRespVOList;
        }catch (Exception e){
            e.printStackTrace();
            log.error("方法queryAllCategory异常");
        }
    return null;
    }

    @Override
    public CategoryReqVO queryCategoryById(String id) {
        // 1.查询参数分类信息  1
        CategoryReqVO  categoryBaseVO = categoryConverter.paramCategoryToVO(paramCategoryMapper.selectById(id));
        return categoryBaseVO;
    }
}
