package com.yaoan.module.econtract.enums;

import com.yaoan.module.econtract.service.category.ICategoryHandleService;
import com.yaoan.module.econtract.service.category.contractAmountTypeCategory.ContractSettlementCategoryHandleServiceImpl;
import com.yaoan.module.econtract.service.category.contractCategory.CoontractCategoryHandleServiceImpl;
import com.yaoan.module.econtract.service.category.contractStatusCategory.ContractStatusCategoryHandleServiceImpl;
import com.yaoan.module.econtract.service.category.contractTypeCategory.CoontractTypeCategoryHandleServiceImpl;
import com.yaoan.module.econtract.service.category.modelCategory.ModelCategoryHandleServiceImpl;
import com.yaoan.module.econtract.service.category.paramCategory.ParamCategoryHandleServiceImpl;
import com.yaoan.module.econtract.service.category.relativeCategory.RelativeCategoryHandleServiceImpl;
import com.yaoan.module.econtract.service.category.templateCategory.TemplateCategoryHandleServiceImpl;
import com.yaoan.module.econtract.service.category.termCategory.TermCategoryHandleServiceImpl;
import lombok.Getter;
@Getter
public enum CategoryTypeCodeConstants {
    PARAM_CATEGORY ("1", "参数分类", ParamCategoryHandleServiceImpl.class),
    MODEL_CATEGORY ("2", "模板分类", ModelCategoryHandleServiceImpl.class),
    TERM_CATEGORY ("3", "条款分类", TermCategoryHandleServiceImpl.class),
    RELATIVE_CATEGORY ("4", "相对方", RelativeCategoryHandleServiceImpl.class),
    CONTRACT_CATEGORY ("5", "合同分类", CoontractCategoryHandleServiceImpl.class),
    CONTRACT_TYPE_CATEGORY ("6", "合同类型分类", CoontractTypeCategoryHandleServiceImpl.class),
    TEMPLATE_CATEGORY ("7", "范本分类", TemplateCategoryHandleServiceImpl.class),
    RELATIVE_PERSONAL_CATEGORY ("8", "相对方-个人分类", RelativeCategoryHandleServiceImpl.class),
    RELATIVE_COMPANY_CATEGORY ("9", "相对方-企业分类", RelativeCategoryHandleServiceImpl.class),
    RELATIVE_GOVERMENT_CATEGORY ("10", "相对方-单位分类", RelativeCategoryHandleServiceImpl.class),

    CONTRACT_STATUS_CATEGORY ("11", "合同状态", ContractStatusCategoryHandleServiceImpl.class),
    CONTRACT_AMOUNT_TYPE_CATEGORY ("12", "合同结算类型", ContractSettlementCategoryHandleServiceImpl.class);

    private final String code;
    private final String info;
    private final Class<ICategoryHandleService> clazz;

    CategoryTypeCodeConstants(String code, String info,Class clazz)
    {
        this.code = code;
        this.info = info;
        this.clazz = clazz;
    }

    public static CategoryTypeCodeConstants getInstance(String code) {
        for (CategoryTypeCodeConstants value : CategoryTypeCodeConstants.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
