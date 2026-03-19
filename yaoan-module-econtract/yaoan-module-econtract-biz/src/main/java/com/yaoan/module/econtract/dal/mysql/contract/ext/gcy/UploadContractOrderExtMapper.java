package com.yaoan.module.econtract.dal.mysql.contract.ext.gcy;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.controller.admin.contract.vo.UploadContractCreateReqVO;

import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.UploadContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.rel.ContractOrderRelDO;
import com.yaoan.module.econtract.enums.gcy.gpmall.HLJContractStatusEnums;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/13 17:17
 */
@Mapper
public interface UploadContractOrderExtMapper extends BaseMapperX<UploadContractOrderExtDO> {

    default List<UploadContractOrderExtDO> selectList4Update(UploadContractCreateReqVO vo){
        MPJLambdaWrapper<UploadContractOrderExtDO> mpjLambdaWrapper = new MPJLambdaWrapper<UploadContractOrderExtDO>()
                .select(UploadContractOrderExtDO::getId, UploadContractOrderExtDO::getStatus, UploadContractOrderExtDO::getContractDrafter, UploadContractOrderExtDO::getName)
                .leftJoin(ContractOrderRelDO.class, ContractOrderRelDO::getContractId, UploadContractOrderExtDO::getId).distinct();
        mpjLambdaWrapper.in(ContractOrderRelDO::getOrderId, vo.getOrderIdList())
                .notIn(UploadContractOrderExtDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_DELETE.getCode());
        return selectList(mpjLambdaWrapper);

    }
}
