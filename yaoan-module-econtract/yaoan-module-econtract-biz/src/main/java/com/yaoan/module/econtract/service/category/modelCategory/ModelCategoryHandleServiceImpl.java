package com.yaoan.module.econtract.service.category.modelCategory;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.tenant.core.context.TenantContextHolder;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryListRespVO;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryReqVO;
import com.yaoan.module.econtract.convert.category.CategoryConverter;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.dataobject.modelcategory.ClientModelCategory;
import com.yaoan.module.econtract.dal.dataobject.modelcategory.ModelCategory;
import com.yaoan.module.econtract.dal.mysql.model.ModelMapper;
import com.yaoan.module.econtract.dal.mysql.modelcategory.ClientModelCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.modelcategory.ModelCategoryMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.service.category.ICategoryHandleService;
import com.yaoan.module.econtract.util.DBExistUtil;
import com.yaoan.module.system.api.dept.dto.UserCompanyDeptRespDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
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
public class ModelCategoryHandleServiceImpl implements ICategoryHandleService<CategoryReqVO> {
    @Resource
    ModelCategoryMapper modelCategoryMapper;
    @Resource
    private ClientModelCategoryMapper clientModelCategoryMapper;
    @Resource
    private CategoryConverter categoryConverter;
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private AdminUserApi userApi;
    @Override
    public Integer saveCategory(CategoryReqVO categoryReqVO) {
        DBExistUtil.isExist(categoryReqVO.getId(),categoryReqVO.getName(),categoryReqVO.getCode(),null,modelCategoryMapper);
        //获取公司id
        long companyId = 0L;;
        UserCompanyDeptRespDTO userCompanyDeptRespDTO = userApi.selectUserCompanyDept(SecurityFrameworkUtils.getLoginUserId());
        if(ObjectUtil.isNotEmpty(userCompanyDeptRespDTO)&&ObjectUtil.isNotEmpty(userCompanyDeptRespDTO.getCompanyInfo())){
            companyId=userCompanyDeptRespDTO.getCompanyInfo().getId();
        }
        // 1.插入参数分类信息
        ModelCategory modelCategory = categoryConverter.toModelCategory(categoryReqVO);
        modelCategory.setCompanyId(companyId);
        modelCategory.setTenantId(TenantContextHolder.getTenantId());
        modelCategoryMapper.insert(modelCategory);
        return modelCategory.getId();
    }

    @Override
    public Integer updateById(CategoryReqVO categoryReqVO) {
        DBExistUtil.isExist(categoryReqVO.getId(),categoryReqVO.getName(),categoryReqVO.getCode(),null,modelCategoryMapper);
        //2.1 修改参数分类  1
        modelCategoryMapper.updateById(categoryConverter.toModelCategory(categoryReqVO));
        return categoryReqVO.getId();
    }

    @Override
    public void deleteById(String id) {
        //1.根据创建客户端创建的一级分类不可删除---根据分类id查询关联表
        Long count = clientModelCategoryMapper.selectCount(new LambdaQueryWrapperX<ClientModelCategory>().eqIfPresent(ClientModelCategory::getCategoryId, id));
        if(count>0){
            throw exception(ErrorCodeConstants.MODEL_CHECK_CONTRACT_TYPE_EMPTY,"此模板分类不允许删除");
        }
        // 2.删除参数分类信息
        List<Model> models = modelMapper.selectList(new LambdaQueryWrapperX<Model>().eq(Model::getCategoryId, id));
        if(CollectionUtil.isNotEmpty(models)){
            throw exception(ErrorCodeConstants.DATA_IS_CALL);
        }
        //3.校验此分类下有没有子分类
        Long count2 = modelCategoryMapper.selectCount(new LambdaQueryWrapperX<ModelCategory>().eqIfPresent(ModelCategory::getParentId, id));
        if(count2>0){
            throw exception(ErrorCodeConstants.MODEL_CHECK_CONTRACT_TYPE_EMPTY,"此模板分类有子分类，不允许删除");
        }
        modelCategoryMapper.deleteById(id);
    }

    @Override
    public List<Tree<String>> queryAllCategory(String type) {
        //获取公司id
        long companyId = 0L;;
        UserCompanyDeptRespDTO userCompanyDeptRespDTO = userApi.selectUserCompanyDept(SecurityFrameworkUtils.getLoginUserId());
        if(ObjectUtil.isNotEmpty(userCompanyDeptRespDTO)&&ObjectUtil.isNotEmpty(userCompanyDeptRespDTO.getCompanyInfo())){
            companyId=userCompanyDeptRespDTO.getCompanyInfo().getId();
        }
        // 1.查询模板分类信息
        List<CategoryListRespVO> categoryListRespVOS = categoryConverter.modelCategoryToList(modelCategoryMapper.selectList(companyId));
        List<Tree<String>> categoryListRespVOList = DBExistUtil.getModelCategoryListRespVOList(categoryListRespVOS);
        return categoryListRespVOList;
    }

    @Override
    public CategoryReqVO queryCategoryById(String id) {
        // 1.查询参数分类信息  1
        CategoryReqVO  categoryBaseVO = categoryConverter.modelCategoryToVO(modelCategoryMapper.selectById(id));
        return categoryBaseVO;
    }
}
