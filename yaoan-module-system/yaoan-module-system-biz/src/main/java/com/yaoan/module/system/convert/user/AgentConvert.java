package com.yaoan.module.system.convert.user;

import com.yaoan.module.system.api.user.dto.AgentDTO;
import com.yaoan.module.system.dal.dataobject.user.AgentDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface AgentConvert {
    AgentConvert INSTANCE = Mappers.getMapper(AgentConvert.class);

    List<AgentDO> convertList(List<AgentDTO> bean);
}
