package com.yaoan.module.system.dal.mysql.user;

import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.system.dal.dataobject.user.AgentDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AgentMapper extends BaseMapperX<AgentDO> {
}
