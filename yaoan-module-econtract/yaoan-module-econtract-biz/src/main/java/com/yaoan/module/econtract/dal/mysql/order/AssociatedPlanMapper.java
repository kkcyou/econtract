package com.yaoan.module.econtract.dal.mysql.order;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.AssociatedPlanDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.rel.ContractOrderRelDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:关联计划 mapper
 * @author: ZHC
 * @date: 2023/12/4 11:47
 */
@Mapper
public interface AssociatedPlanMapper extends BaseMapperX<AssociatedPlanDO> {

    default AssociatedPlanDO getOneByContractId(String id){
        MPJLambdaWrapper<AssociatedPlanDO> mpjLambdaWrapper = new MPJLambdaWrapper<AssociatedPlanDO>()
                .leftJoin(ContractOrderRelDO.class, ContractOrderRelDO::getOrderId, AssociatedPlanDO::getOrderId)
                .eq(ContractOrderRelDO::getContractId, id)
                .selectAll(AssociatedPlanDO.class)
                .distinct();
        return  selectOne(mpjLambdaWrapper);
    }
}
