package com.yaoan.module.system.service.config;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import com.yaoan.module.system.api.config.dto.FlowableConfigRespDTO;
import com.yaoan.module.system.api.config.dto.SystemConfigReqDTO;
import com.yaoan.module.system.controller.admin.config.vo.*;
import com.yaoan.module.system.dal.dataobject.config.SystemConfigDO;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/22 17:42
 */
public interface SystemConfigService {

  

    PageResult<SystemConfigPageRespVO> list(SystemConfigReqVO reqVO);

    String update(SystemConfigUpdateReqVO reqVO);

    SystemConfigDO getSystemConfigByDTO(SystemConfigReqDTO reqDTO);

    FlowableConfigRespDTO getFlowableByProDefKey(ActivityConfigurationEnum activityConfigurationEnum);

    RegionDataVersionRespVO getRegionDataVersion(RegionDataVersionReqVO reqVO);

    List<SystemConfigRespVO> getConfigsByCKeys(List<String> cKeys);



    String setPermissionForApproveScanAnnotations(SystemConfigReqVO reqVO);

    String setPermissionForCopyScanAnnotations(SystemConfigReqVO reqVO);

    String setSystemConfig(SystemConfigReqVO reqVO);

    String getConfigByKey(String configKey);

    SystemConfigDO ifNeedSignet(String cKey);

    String getConfigValue(String key);

    SystemConfigDO getIfAddXmlByKey(String configKey);
}
