package com.yaoan.module.system.api.config;

import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import com.yaoan.module.system.api.config.dto.FlowableConfigRespDTO;
import com.yaoan.module.system.api.config.dto.SystemConfigRespDTO;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/22 17:51
 */
public interface SystemConfigApi {


    /**
     * 判断是否走审批流
     */
    Boolean ifApprove(ActivityConfigurationEnum activityConfigurationEnum);


    /**
     * 根据流程定义key，获取工作流配置信息
     */
    FlowableConfigRespDTO getFlowableByProDefKey(ActivityConfigurationEnum activityConfigurationEnum);

    /**
     * 根据配置key，获取配置信息
     */
    List<SystemConfigRespDTO> getConfigsByCKeys(List<String> cKeys);

    /**
     * 查看审批人的批注浏览权限
     * {@link com.yaoan.module.system.enums.config.SystemConfigValueEnums}
     */
    String getPermissionForApproveScanAnnotations();

    /**
     * 查看抄送人的批注浏览权限
     * {@link com.yaoan.module.econtract.enums.common.IfEnums}
     */
    String getPermissionForCopyScanAnnotations();


    String getConfigByKey(String key);

    /**
     * 判断租户是否需要用印审批
     */
    Boolean ifNeedSignet();

    /**
     * PDF中是否需要添加xml
     */
    Boolean ifAddXml(String configKey);
}
