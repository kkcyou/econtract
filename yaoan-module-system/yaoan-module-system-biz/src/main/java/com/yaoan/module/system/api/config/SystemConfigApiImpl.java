package com.yaoan.module.system.api.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.common.IfEnums;
import com.yaoan.module.system.api.config.dto.FlowableConfigRespDTO;
import com.yaoan.module.system.api.config.dto.SystemConfigReqDTO;
import com.yaoan.module.system.api.config.dto.SystemConfigRespDTO;
import com.yaoan.module.system.controller.admin.config.vo.SystemConfigRespVO;
import com.yaoan.module.system.convert.config.SystemConfigConverter;
import com.yaoan.module.system.dal.dataobject.config.SystemConfigDO;
import com.yaoan.module.system.enums.config.ConfigIfApproveEnums;
import com.yaoan.module.system.enums.config.SystemConfigKeyEnums;
import com.yaoan.module.system.service.config.SystemConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/22 17:53
 */
@Service
public class SystemConfigApiImpl implements SystemConfigApi {
    @Resource
    private SystemConfigService systemConfigService;

    @Override
    public Boolean ifApprove(ActivityConfigurationEnum activityConfigurationEnum) {
        Boolean result = false;
        //确认配置
        SystemConfigKeyEnums enums = SystemConfigKeyEnums.IF_FLOWABLE;
        SystemConfigReqDTO reqDTO = new SystemConfigReqDTO().setCKey(enums.getKey()).setProDefKey(activityConfigurationEnum.getDefinitionKey());
        //找到配置信息
        SystemConfigDO entity = systemConfigService.getSystemConfigByDTO(reqDTO);
        if (ObjectUtil.isNotNull(entity)) {
            ConfigIfApproveEnums resultEnum = ConfigIfApproveEnums.getInstance(entity.getCValue());
            if (ObjectUtil.isNotNull(resultEnum)) {
                result = resultEnum.getResult();
            }
        }
        return result;
    }

    @Override
    public FlowableConfigRespDTO getFlowableByProDefKey(ActivityConfigurationEnum activityConfigurationEnum) {
        FlowableConfigRespDTO respDTO = systemConfigService.getFlowableByProDefKey(activityConfigurationEnum);
        return respDTO;
    }

    @Override
    public List<SystemConfigRespDTO> getConfigsByCKeys(List<String> cKeys) {
        List<SystemConfigRespVO> systemConfigRespVOS = systemConfigService.getConfigsByCKeys(cKeys);
        List<SystemConfigRespDTO> systemConfigRespDTO = SystemConfigConverter.INSTANCE.convertListResp2DTO(systemConfigRespVOS);
        return systemConfigRespDTO;
    }

    @Override
    public String getPermissionForApproveScanAnnotations() {
        String result = ErrorCodeConstants.EMPTY_DATA_ERROR.getMsg();
        List<String> keys = new ArrayList<String>();
        keys.add(SystemConfigKeyEnums.APPROVER_PERMISSION_SCAN_ANNOTATION.getKey());
        List<SystemConfigRespVO> respVO = systemConfigService.getConfigsByCKeys(keys);
        if (CollectionUtil.isNotEmpty(respVO)) {
            result = respVO.get(0).getCValue();
        }
        return result;
    }

    @Override
    public String getPermissionForCopyScanAnnotations() {
        String result = ErrorCodeConstants.EMPTY_DATA_ERROR.getMsg();
        List<String> keys = new ArrayList<String>();
        keys.add(SystemConfigKeyEnums.CARBON_COPY_PERMISSION_SCAN_ANNOTATION.getKey());
        List<SystemConfigRespVO> respVO = systemConfigService.getConfigsByCKeys(keys);
        if (CollectionUtil.isNotEmpty(respVO)) {
            result = respVO.get(0).getCValue();
        }
        return result;
    }
    @Override
    public String getConfigByKey(String configKey) {
        return systemConfigService.getConfigByKey(configKey);
    }

    @Override
    public Boolean ifNeedSignet() {
        Boolean result = false;
        SystemConfigKeyEnums enums = SystemConfigKeyEnums.IF_NEED_SIGNET;
        SystemConfigDO entity = systemConfigService.ifNeedSignet(enums.getKey());
        if (ObjectUtil.isNotNull(entity)) {
            ConfigIfApproveEnums resultEnum = ConfigIfApproveEnums.getInstance(entity.getCValue());
            if (ObjectUtil.isNotNull(resultEnum)) {
                result = resultEnum.getResult();
            }
        }
        return result;
    }
    @Override
    public Boolean ifAddXml(String configKey) {
        SystemConfigDO entity = systemConfigService.getIfAddXmlByKey(configKey);
        if(ObjectUtil.isNotNull(entity)){
            return entity.getCValue().equals("y");
        }
        return false;
    }
}
