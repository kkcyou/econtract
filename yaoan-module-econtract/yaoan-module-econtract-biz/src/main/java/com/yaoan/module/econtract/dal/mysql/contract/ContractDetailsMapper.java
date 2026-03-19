package com.yaoan.module.econtract.dal.mysql.contract;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDetailsDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ContractDetailsMapper extends BaseMapperX<ContractDetailsDO> {

}
