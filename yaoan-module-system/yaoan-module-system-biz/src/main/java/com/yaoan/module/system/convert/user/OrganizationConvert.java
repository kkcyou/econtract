package com.yaoan.module.system.convert.user;

import com.yaoan.module.system.api.user.dto.OrganizationDTO;
import com.yaoan.module.system.dal.dataobject.user.OrganizationDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper
public interface OrganizationConvert {

    OrganizationConvert INSTANCE = Mappers.getMapper(OrganizationConvert.class);

    OrganizationDTO toDTO(OrganizationDO bean);

    List<OrganizationDTO> toDTOList(List<OrganizationDO> bean);

    List<OrganizationDO> convertList(List<OrganizationDTO> bean);
}
