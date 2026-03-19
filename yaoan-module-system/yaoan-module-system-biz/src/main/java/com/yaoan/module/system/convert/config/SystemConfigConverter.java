package com.yaoan.module.system.convert.config;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.system.api.config.dto.SystemConfigRespDTO;
import com.yaoan.module.system.controller.admin.config.vo.SystemConfigPageRespVO;
import com.yaoan.module.system.controller.admin.config.vo.SystemConfigReqVO;
import com.yaoan.module.system.controller.admin.config.vo.SystemConfigRespVO;
import com.yaoan.module.system.controller.admin.config.vo.SystemConfigUpdateReqVO;
import com.yaoan.module.system.dal.dataobject.config.SystemConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/22 17:48
 */
@Mapper
public interface SystemConfigConverter {

    SystemConfigConverter INSTANCE = Mappers.getMapper(SystemConfigConverter.class);

    SystemConfigRespDTO convertEntity2DTO(SystemConfigDO entity);

    PageResult<SystemConfigPageRespVO> convert2Page(PageResult<SystemConfigDO> entityPage);

    @Mapping(target = "CKey", source = "configKey")
    @Mapping(target = "CValue", source = "configValue")
    SystemConfigDO convertUpdate2Entity(SystemConfigUpdateReqVO reqVO);

    SystemConfigRespDTO convertResp2DTO(SystemConfigRespVO systemConfigRespVOS);

    List<SystemConfigRespDTO> convertListResp2DTO(List<SystemConfigRespVO> systemConfigRespVOS);

    List<SystemConfigRespVO> convertListEntity2Resp(List<SystemConfigDO> systemConfigDOS);

    SystemConfigRespVO convertEntity2Resp(SystemConfigDO systemConfigDOS);

    @Mapping(target = "CKey", source = "configKey")
    @Mapping(target = "CValue", source = "configValue")
    SystemConfigDO req2Entity(SystemConfigReqVO reqVO);
}
