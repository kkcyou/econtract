package com.yaoan.module.econtract.convert.config;

import com.yaoan.module.econtract.controller.admin.common.vo.flowable.FlowableConfigRespVO;
import com.yaoan.module.system.api.config.dto.FlowableConfigRespDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/26 11:12
 */
@Mapper
public interface SystemConfigDTOConverter {
    SystemConfigDTOConverter INSTANCE = Mappers.getMapper(SystemConfigDTOConverter.class);

    FlowableConfigRespVO dto2resp(FlowableConfigRespDTO respDTO);
}
