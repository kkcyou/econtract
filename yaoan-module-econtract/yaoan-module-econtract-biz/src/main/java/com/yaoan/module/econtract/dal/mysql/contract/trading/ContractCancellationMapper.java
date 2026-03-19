package com.yaoan.module.econtract.dal.mysql.contract.trading;

import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.gpx.CancellationFileDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ContractCancellationMapper extends BaseMapperX<CancellationFileDO> {
}
