package com.yaoan.module.econtract.dal.mysql.contracttype;


import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.contracttype.vo.ContractTypeExportReqVO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author Pele
 * @description 针对表【ecms_contract_type】的数据库操作Mapper
 * @createDate 2023-08-08 16:50:22
 * @Entity .ContractType
 */
@Mapper
public interface ContractTypeMapper extends BaseMapperX<ContractType> {
    default List<ContractType> selectList(ContractTypeExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ContractType>()
                .likeIfPresent(ContractType::getName, reqVO.getName())
                .eqIfPresent(ContractType::getTypeStatus, reqVO.getTypeStatus())
                .orderByDesc(ContractType::getUpdateTime));
    }

    default List<ContractType> selectByPlatId(String platId) {
        return selectList(new LambdaQueryWrapperX<ContractType>().select(ContractType::getName, ContractType::getPlatId).eq(ContractType::getPlatId, platId));
    }

    // 根据父类ID查找子节点
    @Select("SELECT * FROM ecms_contract_type_new WHERE parent_id = #{parentId}")
    List<ContractType> findByParentId(String parentId);
}




