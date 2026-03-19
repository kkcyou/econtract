package com.yaoan.module.econtract.service.category.termCategory;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryListRespVO;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryReqVO;
import com.yaoan.module.econtract.convert.category.CategoryConverter;
import com.yaoan.module.econtract.dal.dataobject.category.TermsCategory;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import com.yaoan.module.econtract.dal.mysql.category.TermsCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.term.TermMapper;
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
public class TermCategoryHandleServiceImpl implements ICategoryHandleService<CategoryReqVO> {
    @Resource
    TermsCategoryMapper termsCategoryMapper;
    @Resource
    private CategoryConverter categoryConverter;
    @Resource
    private TermMapper termMapper;
    @Override
    public Integer saveCategory(CategoryReqVO categoryReqVO) {
        DBExistUtil.isExist(categoryReqVO.getId(),categoryReqVO.getName(),categoryReqVO.getCode(),null,termsCategoryMapper);
        // 1.插入参数分类信息
        TermsCategory termsCategory = categoryConverter.toTermsCategory(categoryReqVO);
        termsCategoryMapper.insert(termsCategory);
        return termsCategory.getId();
    }

    @Override
    public Integer updateById(CategoryReqVO categoryReqVO) {
        DBExistUtil.isExist(categoryReqVO.getId(),categoryReqVO.getName(),categoryReqVO.getCode(),null,termsCategoryMapper);
        //2.1 修改参数分类  1
        termsCategoryMapper.updateById(categoryConverter.toTermsCategory(categoryReqVO));
        return categoryReqVO.getId();
    }

    @Override
    public void deleteById(String id) {
        // 1.删除参数分类信息
        List<Term> terms = termMapper.selectList(new LambdaQueryWrapperX<Term>().eq(Term::getCategoryId, id));
        if(CollectionUtil.isNotEmpty(terms)){
            throw exception(ErrorCodeConstants.DATA_IS_CALL);
        }
        termsCategoryMapper.deleteById(id);
    }

    @Override
    public List<Tree<String>> queryAllCategory(String type) {
        // 1.查询参数分类信息  1
        List<CategoryListRespVO> categoryListRespVOS = categoryConverter.termsCategoryToList(termsCategoryMapper.selectList());
        List<Tree<String>> categoryListRespVOList = DBExistUtil.getCategoryListRespVOList(categoryListRespVOS);
        return categoryListRespVOList;
    }

    @Override
    public CategoryReqVO queryCategoryById(String id) {
        // 1.查询参数分类信息  1
        CategoryReqVO  categoryBaseVO = categoryConverter.termsCategoryToVO(termsCategoryMapper.selectById(id));
        return categoryBaseVO;
    }
}
