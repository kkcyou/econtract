package com.yaoan.module.econtract.dal.mysql.terminate;

import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.dal.dataobject.terminate.TerminateContractDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TerminateContractMapper extends BaseMapperX<TerminateContractDO> {
}
