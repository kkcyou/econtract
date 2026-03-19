package com.yaoan.module.econtract.service.category;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.query.QueryWrapperX;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryListRespVO;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryReqVO;
import com.yaoan.module.econtract.convert.category.CategoryConverter;
import com.yaoan.module.econtract.dal.dataobject.category.*;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.ContractTemplate;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.dataobject.modelcategory.ClientModelCategory;
import com.yaoan.module.econtract.dal.dataobject.modelcategory.ModelCategory;
import com.yaoan.module.econtract.dal.dataobject.param.Param;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import com.yaoan.module.econtract.dal.mysql.category.*;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contracttemplate.ContractTemplateMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.model.ModelMapper;
import com.yaoan.module.econtract.dal.mysql.modelcategory.ClientModelCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.modelcategory.ModelCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.param.ParamMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.term.TermMapper;
import com.yaoan.module.econtract.enums.CategoryTypeCodeConstants;
import com.yaoan.module.econtract.enums.EntityTypeEnums;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.util.DBExistUtil;
import dm.jdbc.util.StringUtil;
import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author doujl
 * @since 2023-07-24
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    ParamCategoryMapper paramCategoryMapper;
    @Resource
    TermsCategoryMapper termsCategoryMapper;
    @Resource
    ModelCategoryMapper modelCategoryMapper;
    @Resource
    RelativeCategoryMapper relativeCategoryMapper;
    @Resource
    TemplateCategoryMapper templateCategoryMapper;
    @Resource
    ContractCategoryMapper contractCategoryMapper;
    @Resource
    ContractTypeCategoryMapper contractTypeCategoryMapper;
    @Resource
    private  CategoryConverter categoryConverter;
    @Resource
    private ParamMapper paramMapper;
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private TermMapper termMapper;
    @Resource
    private RelativeMapper relativeMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private ContractTemplateMapper templateMapper;
    @Resource
    private ClientModelCategoryMapper clientModelCategoryMapper;


    @Override
    public Integer saveParamCategoryV2(CategoryReqVO categoryReqVO) {
        ICategoryHandleService categoryHandle = getICategoryHandleService(categoryReqVO.getType());
        Integer id=null;
        //2. 处理分类数据
        if (ObjectUtil.isNotEmpty(categoryReqVO.getId())) {
            id = categoryHandle.updateById(categoryReqVO);
        } else {
            //插入数据
            id = categoryHandle.saveCategory(categoryReqVO);
        }
        return id;
    }
    //8：相对方-政府分类 9：相对方-企业分类
    @Override
    public void deleteByIdV2(String id, String type) {
        ICategoryHandleService categoryHandle = getICategoryHandleService(type);
        categoryHandle.deleteById(id);
    }
    @Override
    public List<Tree<String>> queryAllParamCategoryV2(String type) {
        ICategoryHandleService categoryHandle = getICategoryHandleService(type);
        if(BeanUtil.isNotEmpty(categoryHandle.queryAllCategory(type))){
            return categoryHandle.queryAllCategory(type);
        }else {
            return  new ArrayList<>()  ;
        }
    }

    @Override
    public List<Tree<String>> queryAllParamCategoryV3( ) {
            List<ClientModelCategory> categories = clientModelCategoryMapper.selectList(ClientModelCategory::getClientId, SecurityFrameworkUtils.getClientId());
            List<CategoryListRespVO> categoryListRespVOS = categoryConverter.modelCategoryToList(modelCategoryMapper.selectList());
            List<Tree<String>> categoryListRespVOList = DBExistUtil.getCategoryListRespVOList(categoryListRespVOS);
            List<Integer> collect = categories.stream().map(ClientModelCategory::getCategoryId).collect(Collectors.toList());
            return categoryListRespVOList.stream().filter(item -> collect.contains(Integer.valueOf(item.getId()))).collect(Collectors.toList());
    }

    @Override
    public CategoryReqVO queryACategoryByIdV2(String id,String type) {
        ICategoryHandleService categoryHandle = getICategoryHandleService(type);
        return (CategoryReqVO)categoryHandle.queryCategoryById(id);
    }

    private  ICategoryHandleService getICategoryHandleService(String type){
        CategoryTypeCodeConstants instance = CategoryTypeCodeConstants.getInstance(type);
        ICategoryHandleService categoryHandle = SpringUtil.getBean(instance.getClazz());
        return categoryHandle;
    }


    @Override
    public Integer saveParamCategory(CategoryReqVO categoryReqVO) {
        //1.校验名称，编码是否重复
        isExist(categoryReqVO.getId(), categoryReqVO.getName(), categoryReqVO.getCode(), categoryReqVO.getType());
        //2. 处理分类数据
        if (ObjectUtil.isNotEmpty(categoryReqVO.getId())) {
            if (CategoryTypeCodeConstants.PARAM_CATEGORY.getCode().equals(categoryReqVO.getType())) {
                //2.1 修改参数分类  1
                paramCategoryMapper.updateById(categoryConverter.toParamCategory(categoryReqVO));
            } else if (CategoryTypeCodeConstants.MODEL_CATEGORY.getCode().equals(categoryReqVO.getType())) {
                // 2.2.修改模板分类信息
                modelCategoryMapper.updateById(categoryConverter.toModelCategory(categoryReqVO));
            } else if (CategoryTypeCodeConstants.TERM_CATEGORY.getCode().equals(categoryReqVO.getType())) {
                // 2.3.修改条款分类信息
                termsCategoryMapper.updateById(categoryConverter.toTermsCategory(categoryReqVO));
                //2.4. 修改相对方分类信息
            } else if (CategoryTypeCodeConstants.RELATIVE_CATEGORY.getCode().equals(categoryReqVO.getType())) {
                relativeCategoryMapper.updateById(categoryConverter.toRelativeCategory(categoryReqVO));
            } else if (CategoryTypeCodeConstants.CONTRACT_CATEGORY.getCode().equals(categoryReqVO.getType())) {
                //2.5. 修改合同分类信息
                contractCategoryMapper.updateById(categoryConverter.toContractCategory(categoryReqVO));
            } else if (CategoryTypeCodeConstants.CONTRACT_TYPE_CATEGORY.getCode().equals(categoryReqVO.getType())) {
                //2.6. 修改合同类型分类信息
                contractTypeCategoryMapper.updateById(categoryConverter.toContractTypeCategory(categoryReqVO));
            } else if (CategoryTypeCodeConstants.TEMPLATE_CATEGORY.getCode().equals(categoryReqVO.getType())) {
                // 2.7.修改范本分类信息
                templateCategoryMapper.updateById(categoryConverter.toTemplateCategory(categoryReqVO));
            } else if (CategoryTypeCodeConstants.RELATIVE_PERSONAL_CATEGORY.getCode().equals(categoryReqVO.getType())||
                    CategoryTypeCodeConstants.RELATIVE_COMPANY_CATEGORY.getCode().equals(categoryReqVO.getType())
                    ||CategoryTypeCodeConstants.RELATIVE_GOVERMENT_CATEGORY.getCode().equals(categoryReqVO.getType())) {
                //2.8. 修改相对方-个人/企业/单位分类信息
                String entityType = entityType(categoryReqVO.getType());
                RelativeCategory relativeCategory = categoryConverter.toRelativeCategory(categoryReqVO);
                relativeCategory.setEntityType(entityType);
                relativeCategoryMapper.updateById(relativeCategory);
            }
        } else {
            //插入数据
            Integer id = saveParamCategoryInfo(categoryReqVO);
            categoryReqVO.setId(id);
        }
        return  categoryReqVO.getId();
    }
    //8：相对方-政府分类 9：相对方-企业分类
    @Override
    public void deleteById(String id, String type) {
        //参数分类  1
        if (CategoryTypeCodeConstants.PARAM_CATEGORY.getCode().equals(type)) {
            // 1.删除参数分类信息
            List<Param> params = paramMapper.selectList(new LambdaQueryWrapperX<Param>().eq(Param::getCategoryId, id));
           if(CollectionUtil.isNotEmpty(params)){
               throw exception(ErrorCodeConstants.DATA_IS_CALL);
           }
            paramCategoryMapper.deleteById(id);
        } else if (CategoryTypeCodeConstants.MODEL_CATEGORY.getCode().equals(type)) {
            // 2.删除模板分类信息
            List<Model> models = modelMapper.selectList(new LambdaQueryWrapperX<Model>().eq(Model::getCategoryId, id));
            if(CollectionUtil.isNotEmpty(models)){
                throw exception(ErrorCodeConstants.DATA_IS_CALL);
            }
            modelCategoryMapper.deleteById(id);
        } else if (CategoryTypeCodeConstants.TERM_CATEGORY.getCode().equals(type)) {
            // 3.删除条款分类信息
            List<Term> terms = termMapper.selectList(new LambdaQueryWrapperX<Term>().eq(Term::getCategoryId, id));
            if(CollectionUtil.isNotEmpty(terms)){
                throw exception(ErrorCodeConstants.DATA_IS_CALL);
            }
            termsCategoryMapper.deleteById(id);
        } else if (CategoryTypeCodeConstants.RELATIVE_CATEGORY.getCode().equals(type)) {
            //4. 删除相对方分类信息
            List<Relative> relatives = relativeMapper.selectList(new LambdaQueryWrapperX<Relative>().eq(Relative::getCategoryId, id));
            if(CollectionUtil.isNotEmpty(relatives)){
                throw exception(ErrorCodeConstants.DATA_IS_CALL);
            }
            relativeCategoryMapper.deleteById(id);
        } else if (CategoryTypeCodeConstants.CONTRACT_CATEGORY.getCode().equals(type)) {
            //5. 删除合同分类信息
            List<ContractDO> contractDOS = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>().eq(ContractDO::getContractCategory, id));
            if(CollectionUtil.isNotEmpty(contractDOS)){
                throw exception(ErrorCodeConstants.DATA_IS_CALL);
            }
            contractCategoryMapper.deleteById(id);
        } else if (CategoryTypeCodeConstants.CONTRACT_TYPE_CATEGORY.getCode().equals(type)) {
            //6. 删除合同类型分类信息
            List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>().eq(ContractType::getParentId, id));
            if(CollectionUtil.isNotEmpty(contractTypes)){
                throw exception(ErrorCodeConstants.DATA_IS_CALL);
            }
            contractTypeCategoryMapper.deleteById(id);
        } else if (CategoryTypeCodeConstants.TEMPLATE_CATEGORY.getCode().equals(type)) {
            // 7.删除范本分类信息
            List<ContractTemplate> templates = templateMapper.selectList(new LambdaQueryWrapperX<ContractTemplate>().eq(ContractTemplate::getTemplateCategoryId, id));
            if(CollectionUtil.isNotEmpty(templates)){
                throw exception(ErrorCodeConstants.DATA_IS_CALL);
            }
            templateCategoryMapper.deleteById(id);
        }
        else if (CategoryTypeCodeConstants.RELATIVE_PERSONAL_CATEGORY.getCode().equals(type)||
                CategoryTypeCodeConstants.RELATIVE_COMPANY_CATEGORY.getCode().equals(type)
                ||CategoryTypeCodeConstants.RELATIVE_GOVERMENT_CATEGORY.getCode().equals(type)) {
            List<Relative> relatives = relativeMapper.selectList(new LambdaQueryWrapperX<Relative>().eq(Relative::getCategoryId, id));
            if(CollectionUtil.isNotEmpty(relatives)){
                throw exception(ErrorCodeConstants.DATA_IS_CALL);
            }
            //8. 删除相对方-个人/企业/单位分类信息
            relativeCategoryMapper.deleteById(id);
        }
    }

    @Override
    public List<Tree<String>> queryAllParamCategory(String type) {
        List<Tree<String>> categoryListRespVOList = new ArrayList<>();
        if (CategoryTypeCodeConstants.PARAM_CATEGORY.getCode().equals(type)) {
            // 1.查询参数分类信息  1
            categoryListRespVOList = getCategoryListRespVOList(categoryConverter.paramCategoryToList(paramCategoryMapper.selectList()));
        } else if (CategoryTypeCodeConstants.MODEL_CATEGORY.getCode().equals(type)) {
            // 2.查询模板分类信息  2
            categoryListRespVOList = getCategoryListRespVOList(categoryConverter.modelCategoryToList(modelCategoryMapper.selectList()));
        } else if (CategoryTypeCodeConstants.TERM_CATEGORY.getCode().equals(type)) {
            // 3.查询条款分类信息  3
            categoryListRespVOList = getCategoryListRespVOList(categoryConverter.termsCategoryToList(termsCategoryMapper.selectList()));
        } else if (CategoryTypeCodeConstants.RELATIVE_CATEGORY.getCode().equals(type)) {
            // 4.查询相对方分类信息  4
            categoryListRespVOList = getCategoryListRespVOList(categoryConverter.relativeCategoryToList(relativeCategoryMapper.selectList()));
        } else if (CategoryTypeCodeConstants.CONTRACT_CATEGORY.getCode().equals(type)) {
            //5. 查询合同分类信息   5
            categoryListRespVOList = getCategoryListRespVOList(categoryConverter.contractCategoryToList(contractCategoryMapper.selectList()));
        } else if (CategoryTypeCodeConstants.CONTRACT_TYPE_CATEGORY.getCode().equals(type)) {
            //6. 查询合同类型分类信息 6
            categoryListRespVOList = getCategoryListRespVOList(categoryConverter.contractTypeCategoryToList(contractTypeCategoryMapper.selectList()));
        } else if (CategoryTypeCodeConstants.TEMPLATE_CATEGORY.getCode().equals(type)) {
            // 7.查询范本分类信息   7
            categoryListRespVOList = getCategoryListRespVOList(categoryConverter.templateCategoryToList(templateCategoryMapper.selectList()));
        }
        else if (CategoryTypeCodeConstants.RELATIVE_PERSONAL_CATEGORY.getCode().equals(type)||
                CategoryTypeCodeConstants.RELATIVE_COMPANY_CATEGORY.getCode().equals(type)
                ||CategoryTypeCodeConstants.RELATIVE_GOVERMENT_CATEGORY.getCode().equals(type)) {
            // 8.查询相对方-个人/企业/单位分类信息   8 9 10
            String entityType = entityType(type);
            categoryListRespVOList = getCategoryListRespVOList(categoryConverter.relativeCategoryToList(relativeCategoryMapper.selectList(new LambdaQueryWrapperX<RelativeCategory>().eq(RelativeCategory::getEntityType,entityType))));
        }
        return categoryListRespVOList;
    }

    @Override
    public CategoryReqVO queryACategoryById(String id,String type) {
        CategoryReqVO categoryBaseVO = new CategoryReqVO();
        if (CategoryTypeCodeConstants.PARAM_CATEGORY.getCode().equals(type)) {
            // 1.查询参数分类信息  1
            categoryBaseVO = categoryConverter.paramCategoryToVO(paramCategoryMapper.selectById(id));
        } else if (CategoryTypeCodeConstants.MODEL_CATEGORY.getCode().equals(type)) {
            // 2.查询模板分类信息  2
            categoryBaseVO = categoryConverter.modelCategoryToVO(modelCategoryMapper.selectById(id));
        } else if (CategoryTypeCodeConstants.TERM_CATEGORY.getCode().equals(type)) {
            // 3.查询条款分类信息  3
            categoryBaseVO = categoryConverter.termsCategoryToVO(termsCategoryMapper.selectById(id));
        } else if (CategoryTypeCodeConstants.RELATIVE_CATEGORY.getCode().equals(type)) {
            // 4.查询相对方分类信息  4
            categoryBaseVO = categoryConverter.relativeCategoryToVO(relativeCategoryMapper.selectById(id));
        } else if (CategoryTypeCodeConstants.CONTRACT_CATEGORY.getCode().equals(type)) {
            //5. 查询合同分类信息   5
            categoryBaseVO = categoryConverter.contractCategoryToVO(contractCategoryMapper.selectById(id));
        } else if (CategoryTypeCodeConstants.CONTRACT_TYPE_CATEGORY.getCode().equals(type)) {
            //6. 查询合同类型分类信息 6
            categoryBaseVO = categoryConverter.contractTypeCategoryToVO(contractTypeCategoryMapper.selectById(id));
        } else if (CategoryTypeCodeConstants.TEMPLATE_CATEGORY.getCode().equals(type)) {
            // 7.删除范本分类信息   7
            categoryBaseVO = categoryConverter.templateCategoryToVO(templateCategoryMapper.selectById(id));
        } else if (CategoryTypeCodeConstants.RELATIVE_PERSONAL_CATEGORY.getCode().equals(type)||
                CategoryTypeCodeConstants.RELATIVE_COMPANY_CATEGORY.getCode().equals(type)
                ||CategoryTypeCodeConstants.RELATIVE_GOVERMENT_CATEGORY.getCode().equals(type)) {
            // 8.查询相对方-个人/企业/单位分类信息   8 9 10
            categoryBaseVO = categoryConverter.relativeCategoryToVO(relativeCategoryMapper.selectById(id));
        }
        return categoryBaseVO;
    }





    private List<Tree<String>> getCategoryListRespVOList(List<CategoryListRespVO> categories) {
        //配置
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        // 自定义属性名
//        treeNodeConfig.setWeightKey("order"); // 权重排序字段 默认为weight
        treeNodeConfig.setIdKey("id"); // 默认为id可以不设置
        treeNodeConfig.setNameKey("name"); // 节点名对应名称 默认为name
        treeNodeConfig.setParentIdKey("parentId"); // 父节点 默认为parentId
        treeNodeConfig.setChildrenKey("children"); // 子点 默认为children
//        treeNodeConfig.setDeep(3); // 可以配置递归深度 从0开始计算 默认此配置为空,即不限制
        //转换器 "0" - 最顶层父id值 一般为0之类  categories – 分类集合
        List<Tree<String>> treeNodes = TreeUtil.build(categories, "0", treeNodeConfig,
                // treeNode – 源数据实体
                // tree – 树节点实体
                (treeNode, tree) -> {
                    tree.setId(treeNode.getId().toString());
                    tree.setParentId(treeNode.getParentId().toString());
                    tree.setName(treeNode.getName());
                    // 扩展属性 ...
                    tree.putExtra("code", treeNode.getCode());
                });
        return treeNodes;
    }

    public void isExist(Integer id, String name, String code, String type) {
        boolean nameFlag = true;
        boolean codeFlag = true;
        //1.参数分类  1
        if (CategoryTypeCodeConstants.PARAM_CATEGORY.getCode().equals(type)) {
            QueryWrapperX paramWrapperName = setNameQueryWrapper(new QueryWrapperX<ParamCategory>(), id, name,null);
            QueryWrapperX paramWrapperCode = setCodeQueryWrapper(new QueryWrapperX<ParamCategory>(), id, code,null);
            nameFlag = paramCategoryMapper.selectCount(paramWrapperName) > 0;
            codeFlag = paramCategoryMapper.selectCount(paramWrapperCode) > 0;
        } else if (CategoryTypeCodeConstants.MODEL_CATEGORY.getCode().equals(type)) {
            //2.模板分类 2
            QueryWrapperX modelWrapperName = setNameQueryWrapper(new QueryWrapperX<ModelCategory>(), id, name,null);
            QueryWrapperX modelWrapperCode = setCodeQueryWrapper(new QueryWrapperX<ModelCategory>(), id, code,null);
            nameFlag = modelCategoryMapper.selectCount(modelWrapperName) > 0;
            codeFlag = modelCategoryMapper.selectCount(modelWrapperCode) > 0;
        } else if (CategoryTypeCodeConstants.TERM_CATEGORY.getCode().equals(type)) {
            // 3.条款分类信息
            QueryWrapperX termsWrapperName = setNameQueryWrapper(new QueryWrapperX<TermsCategory>(), id, name,null);
            QueryWrapperX termsWrapperCode = setCodeQueryWrapper(new QueryWrapperX<TermsCategory>(), id, code,null);
            nameFlag = termsCategoryMapper.selectCount(termsWrapperName) > 0;
            codeFlag = termsCategoryMapper.selectCount(termsWrapperCode) > 0;
        } else if (CategoryTypeCodeConstants.RELATIVE_CATEGORY.getCode().equals(type)) {
            //相对方分个人类 4
            QueryWrapperX relativeWrapperName = setNameQueryWrapper(new QueryWrapperX<RelativeCategory>(), id, name,null);
            QueryWrapperX relativeWrapperoCde = setCodeQueryWrapper(new QueryWrapperX<RelativeCategory>(), id, code,null);
            nameFlag = relativeCategoryMapper.selectCount(relativeWrapperName) > 0;
            codeFlag = relativeCategoryMapper.selectCount(relativeWrapperoCde) > 0;
        } else if (CategoryTypeCodeConstants.CONTRACT_CATEGORY.getCode().equals(type)) {
            //5. 查询合同分类信息
            QueryWrapperX contractWrapperName = setNameQueryWrapper(new QueryWrapperX<ContractCategory>(), id, name,null);
            QueryWrapperX contractWrapperCode = setCodeQueryWrapper(new QueryWrapperX<ContractCategory>(), id, code,null);
            nameFlag = contractCategoryMapper.selectCount(contractWrapperName) > 0;
            codeFlag = contractCategoryMapper.selectCount(contractWrapperCode) > 0;
        } else if (CategoryTypeCodeConstants.CONTRACT_TYPE_CATEGORY.getCode().equals(type)) {
            //6. 查询合同类型分类信息
            QueryWrapperX contractTypeWrapperName = setNameQueryWrapper(new QueryWrapperX<ContractTypeCategory>(), id, name,null);
            QueryWrapperX contractTypeWrapperCode = setCodeQueryWrapper(new QueryWrapperX<ContractTypeCategory>(), id, code,null);
            nameFlag = contractTypeCategoryMapper.selectCount(contractTypeWrapperName) > 0;
            codeFlag = contractTypeCategoryMapper.selectCount(contractTypeWrapperCode) > 0;
        } else if (CategoryTypeCodeConstants.TEMPLATE_CATEGORY.getCode().equals(type)) {
            // 7.查询范本分类信息
            QueryWrapperX templateWrapperName = setNameQueryWrapper(new QueryWrapperX<TemplateCategory>(), id, name,null);
            QueryWrapperX templateWrapperCode = setCodeQueryWrapper(new QueryWrapperX<TemplateCategory>(), id, code,null);
            nameFlag = templateCategoryMapper.selectCount(templateWrapperName) > 0;
            codeFlag = templateCategoryMapper.selectCount(templateWrapperCode) > 0;
        } else if (CategoryTypeCodeConstants.RELATIVE_PERSONAL_CATEGORY.getCode().equals(type)||
                CategoryTypeCodeConstants.RELATIVE_COMPANY_CATEGORY.getCode().equals(type)
                ||CategoryTypeCodeConstants.RELATIVE_GOVERMENT_CATEGORY.getCode().equals(type)) {
            //相对方个人、企业，单位分类信息
            String entityType = entityType(type);
            QueryWrapperX relativeWrapperName = setNameQueryWrapper(new QueryWrapperX<RelativeCategory>(), id, name,entityType);
            QueryWrapperX relativeWrapperoCde = setCodeQueryWrapper(new QueryWrapperX<RelativeCategory>(), id, code,entityType);
            nameFlag = relativeCategoryMapper.selectCount(relativeWrapperName) > 0;
            codeFlag = relativeCategoryMapper.selectCount(relativeWrapperoCde) > 0;
        }
        if (nameFlag) {
            throw exception(ErrorCodeConstants.NAME_EXISTS);
        } else if (codeFlag) {
            throw exception(ErrorCodeConstants.CODE_EXISTS);
        }

    }

    private QueryWrapperX setNameQueryWrapper(QueryWrapperX paramwrapper, Integer id, String name,String entityType) {
        paramwrapper.eqIfPresent("name", name);
        paramwrapper.neIfPresent("id", id);
        if(StringUtil.isNotEmpty(entityType)){
            paramwrapper.eqIfPresent("entity_type", entityType);
        }
        return paramwrapper;
    }

    private QueryWrapperX setCodeQueryWrapper(QueryWrapperX paramwrapper, Integer id, String code,String entityType) {
        paramwrapper.eqIfPresent("code", code);
        paramwrapper.neIfPresent("id", id);
        if(StringUtil.isNotEmpty(entityType)){
            paramwrapper.eqIfPresent("entity_type", entityType);
        }
        return paramwrapper;
    }
    public Integer saveParamCategoryInfo(CategoryReqVO categoryReqVO) {
        Integer id=null;
        //插入数据
        if (CategoryTypeCodeConstants.PARAM_CATEGORY.getCode().equals(categoryReqVO.getType())) {
            // 1.插入参数分类信息
            ParamCategory paramCategory = categoryConverter.toParamCategory(categoryReqVO);
            paramCategoryMapper.insert(paramCategory);
            id =paramCategory.getId();
        } else if (CategoryTypeCodeConstants.MODEL_CATEGORY.getCode().equals(categoryReqVO.getType())) {
            // 2.插入模板分类信息
            ModelCategory modelCategory = categoryConverter.toModelCategory(categoryReqVO);
            modelCategoryMapper.insert(modelCategory);
            id=modelCategory.getId();
        } else if (CategoryTypeCodeConstants.TERM_CATEGORY.getCode().equals(categoryReqVO.getType())) {
            // 3.插入条款分类信息
            TermsCategory termsCategory = categoryConverter.toTermsCategory(categoryReqVO);
            termsCategoryMapper.insert(termsCategory);
            id=termsCategory.getId();
        } else if (CategoryTypeCodeConstants.RELATIVE_CATEGORY.getCode().equals(categoryReqVO.getType())) {
            //4. 插入相对方分类信息
            RelativeCategory relativeCategory = categoryConverter.toRelativeCategory(categoryReqVO);
            relativeCategoryMapper.insert(relativeCategory);
            id=relativeCategory.getId();
        } else if (CategoryTypeCodeConstants.CONTRACT_CATEGORY.getCode().equals(categoryReqVO.getType())) {
            //5. 插入合同分类信息
            ContractCategory contractCategory = categoryConverter.toContractCategory(categoryReqVO);
            contractCategoryMapper.insert(contractCategory);
            id=contractCategory.getId();
        } else if (CategoryTypeCodeConstants.CONTRACT_TYPE_CATEGORY.getCode().equals(categoryReqVO.getType())) {
            //6. 插入合同类型分类信息
            ContractTypeCategory contractTypeCategory = categoryConverter.toContractTypeCategory(categoryReqVO);
            contractTypeCategoryMapper.insert(contractTypeCategory);
            id=contractTypeCategory.getId();
        } else if (CategoryTypeCodeConstants.TEMPLATE_CATEGORY.getCode().equals(categoryReqVO.getType())) {
            // 7.插入范本分类信息
            TemplateCategory templateCategory = categoryConverter.toTemplateCategory(categoryReqVO);
            templateCategoryMapper.insert(templateCategory);
            id=templateCategory.getId();
        } else if (CategoryTypeCodeConstants.RELATIVE_PERSONAL_CATEGORY.getCode().equals(categoryReqVO.getType())||
                CategoryTypeCodeConstants.RELATIVE_COMPANY_CATEGORY.getCode().equals(categoryReqVO.getType())
                ||CategoryTypeCodeConstants.RELATIVE_GOVERMENT_CATEGORY.getCode().equals(categoryReqVO.getType())) {
            // 8.插入个人、企业，单位分类信息
            RelativeCategory relativeCategory = categoryConverter.toRelativeCategory(categoryReqVO);
            String entityType = entityType(categoryReqVO.getType());
            relativeCategory.setEntityType(entityType);
            relativeCategoryMapper.insert(relativeCategory);
            id=relativeCategory.getId();
        }
         return id;
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
