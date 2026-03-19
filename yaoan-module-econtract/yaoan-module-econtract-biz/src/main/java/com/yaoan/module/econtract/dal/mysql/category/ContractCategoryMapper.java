package com.yaoan.module.econtract.dal.mysql.category;


import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.dal.dataobject.category.ContractCategory;
import com.yaoan.module.econtract.dal.dataobject.category.TermsCategory;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/3 15:38
 */
@Mapper
public interface ContractCategoryMapper extends BaseMapperX<ContractCategory> {

}
