package com.yaoan.module.econtract.convert.category;

import com.yaoan.module.econtract.controller.admin.category.vo.CategoryListRespVO;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryReqVO;
import com.yaoan.module.econtract.controller.admin.param.vo.ParamListRespVO;
import com.yaoan.module.econtract.dal.dataobject.category.*;
import com.yaoan.module.econtract.dal.dataobject.modelcategory.ModelCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import java.util.List;

/**
 * @Author：doujl
 * @Description:  转换类
 * @Date: 2023年07月05日14:30:38
 */
@Mapper(componentModel = "spring")
public interface CategoryConverter {

    CategoryConverter INSTANCE = Mappers.getMapper(CategoryConverter.class);


    ParamCategory toParamCategory(CategoryReqVO bean);
    ModelCategory toModelCategory(CategoryReqVO bean);
    RelativeCategory toRelativeCategory(CategoryReqVO bean);
    TermsCategory toTermsCategory(CategoryReqVO bean);
    TemplateCategory toTemplateCategory(CategoryReqVO bean);
    ContractCategory toContractCategory(CategoryReqVO bean);
    ContractTypeCategory toContractTypeCategory(CategoryReqVO bean);

    ContractStatusCategory toContractStatusCategory(CategoryReqVO bean);
    ContractSettlementCategory toContractSettlementCategory(CategoryReqVO bean);

    List<CategoryListRespVO> paramCategoryToList(List<ParamCategory> categories);
    List<CategoryListRespVO> modelCategoryToList(List<ModelCategory> categories);
    List<CategoryListRespVO> termsCategoryToList(List<TermsCategory> categories);
    List<CategoryListRespVO> templateCategoryToList(List<TemplateCategory> categories);
    List<CategoryListRespVO> contractCategoryToList(List<ContractCategory> categories);
    List<CategoryListRespVO> contractTypeCategoryToList(List<ContractTypeCategory> categories);
    List<CategoryListRespVO> relativeCategoryToList(List<RelativeCategory> categories);
    List<ParamListRespVO> paramToList(List<ParamCategory> categories);
    List<CategoryListRespVO> contractStatusCategoryToList(List<ContractStatusCategory> categories);
    List<CategoryListRespVO> contractSettlementCategoryToList(List<ContractSettlementCategory> categories);
    
    CategoryReqVO paramCategoryToVO(ParamCategory paramCategory );
    CategoryReqVO modelCategoryToVO(ModelCategory modelCategory );
    CategoryReqVO termsCategoryToVO(TermsCategory termsCategory );
    CategoryReqVO templateCategoryToVO(TemplateCategory templateCategory );
    CategoryReqVO contractCategoryToVO(ContractCategory contractCategory );
    CategoryReqVO contractTypeCategoryToVO(ContractTypeCategory contractTypeCategory );
    CategoryReqVO relativeCategoryToVO(RelativeCategory relativeCategory );
    CategoryReqVO contractStatusCategoryToVO(ContractStatusCategory contractCategory );
    CategoryReqVO contractSettlementCategoryToVO(ContractSettlementCategory contractCategory );

}
