package com.yaoan.module.econtract.dal.mysql.signatoryrel;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SignatoryRelMapper extends BaseMapperX<SignatoryRelDO> {

   default SignatoryRelDO select4RelativeExpression(String contractId, Integer signatureType){
       MPJLambdaWrapper<SignatoryRelDO> mpjLambdaWrapper = new MPJLambdaWrapper<SignatoryRelDO>()
               .leftJoin(ContractOrderExtDO.class, ContractOrderExtDO::getId, SignatoryRelDO::getContractId)
               .eq(ContractOrderExtDO::getId, contractId)
               .eq(SignatoryRelDO::getType, signatureType)
               .selectAll(SignatoryRelDO.class)
               .distinct();
       return selectOne(mpjLambdaWrapper);

   }
}
