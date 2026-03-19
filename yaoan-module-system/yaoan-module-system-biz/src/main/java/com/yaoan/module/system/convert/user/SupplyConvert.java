package com.yaoan.module.system.convert.user;

import com.yaoan.module.system.api.user.dto.SupplyDTO;
import com.yaoan.module.system.dal.dataobject.user.SupplyDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface SupplyConvert {

    SupplyConvert INSTANCE = Mappers.getMapper(SupplyConvert.class);

    SupplyDTO toDTO(SupplyDO bean);

    List<SupplyDO> convertList(List<SupplyDTO> bean);

    List<SupplyDTO> toDTOS(List<SupplyDO> bean);
}
