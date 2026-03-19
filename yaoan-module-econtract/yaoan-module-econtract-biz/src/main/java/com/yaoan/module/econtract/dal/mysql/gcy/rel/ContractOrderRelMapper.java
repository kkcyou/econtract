package com.yaoan.module.econtract.dal.mysql.gcy.rel;

import cn.hutool.core.collection.CollectionUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.controller.admin.order.vo.GPMallPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.rel.ContractOrderRelDO;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/12 14:00
 */
@Mapper
public interface ContractOrderRelMapper extends BaseMapperX<ContractOrderRelDO> {

    default List<ContractOrderRelDO> listGPMallOrgOrder(GPMallPageReqVO vo) {
        MPJLambdaWrapper<ContractOrderRelDO> mpjLambdaWrapper = new MPJLambdaWrapper<ContractOrderRelDO>()
                .leftJoin(ContractDO.class, ContractDO::getId, ContractOrderRelDO::getContractId)
                .select(ContractOrderRelDO::getOrderId, ContractOrderRelDO::getUpdateTime).orderByDesc(ContractOrderRelDO::getUpdateTime).distinct();

        mpjLambdaWrapper.eq(ContractDO::getPlatform, vo.getContractFrom())
                .notIn(ContractDO::getStatus, CollectionUtil.newArrayList(ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode()
                        , ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode(), ContractStatusEnums.CONTRACT_AUDITSTATUS_DELETE.getCode()));
        return selectList(mpjLambdaWrapper);

    }

    @Update("<script>"
            + "UPDATE ecms_contract_order_rel "
            + "SET deleted = 0 "
            + "WHERE contract_id IN "
            + "<foreach item='item' index='index' collection='contractIds' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    void active(@Param("contractIds") List<String> contractIds);
}
