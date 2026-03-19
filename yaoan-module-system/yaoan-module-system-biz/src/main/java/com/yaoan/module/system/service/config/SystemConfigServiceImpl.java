package com.yaoan.module.system.service.config;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import com.yaoan.module.econtract.enums.common.IfEnums;
import com.yaoan.module.system.api.config.dto.FlowableConfigRespDTO;
import com.yaoan.module.system.api.config.dto.SystemConfigReqDTO;
import com.yaoan.module.system.controller.admin.config.vo.*;
import com.yaoan.module.system.convert.config.SystemConfigConverter;
import com.yaoan.module.system.dal.dataobject.config.SystemConfigDO;
import com.yaoan.module.system.dal.mysql.config.SystemConfigMapper;
import com.yaoan.module.system.enums.config.SystemConfigKeyEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.infra.enums.ErrorCodeConstants.DIY_ERROR;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/22 17:43
 */
@Slf4j
@Service
public class SystemConfigServiceImpl implements SystemConfigService {

    @Resource
    private SystemConfigMapper systemConfigMapper;

    @Override
    public PageResult<SystemConfigPageRespVO> list(SystemConfigReqVO reqVO) {
        PageResult<SystemConfigDO> entityPage = systemConfigMapper.selectPage(reqVO, new LambdaQueryWrapperX<SystemConfigDO>().likeIfPresent(SystemConfigDO::getCKey, reqVO.getConfigKey()));
        return SystemConfigConverter.INSTANCE.convert2Page(entityPage);
    }

    @Override
    public String update(SystemConfigUpdateReqVO reqVO) {
        SystemConfigDO systemConfigDO = SystemConfigConverter.INSTANCE.convertUpdate2Entity(reqVO);
        systemConfigMapper.updateById(systemConfigDO);
        return "success";
    }

    @Override
    public SystemConfigDO getSystemConfigByDTO(SystemConfigReqDTO reqDTO) {
        SystemConfigDO systemConfigDO = new SystemConfigDO();
        systemConfigDO = systemConfigMapper.selectOne(new LambdaQueryWrapperX<SystemConfigDO>()
                // 启用状态的
                .eq(SystemConfigDO::getStatus, true)
                //配置key
                .eqIfPresent(SystemConfigDO::getCKey, reqDTO.getCKey())
                // 流程定义key
                .eqIfPresent(SystemConfigDO::getProDefKey, reqDTO.getProDefKey())
        );
        return systemConfigDO;
    }

    @Override
    public FlowableConfigRespDTO getFlowableByProDefKey(ActivityConfigurationEnum activityConfigurationEnum) {
        FlowableConfigRespDTO result = new FlowableConfigRespDTO();
        List<SystemConfigDO> list = systemConfigMapper.selectList(new LambdaQueryWrapperX<SystemConfigDO>()
                .eq(SystemConfigDO::getStatus, true)
                .eqIfPresent(SystemConfigDO::getProDefKey, activityConfigurationEnum.getDefinitionKey())
        );
        SystemConfigDO ifApprove = new SystemConfigDO();
        SystemConfigDO ifBatchApprove = new SystemConfigDO();
        SystemConfigDO ifBatchSubmit = new SystemConfigDO();
        List<SystemConfigDO> ifApproveList = list.stream().filter(item -> SystemConfigKeyEnums.IF_FLOWABLE.getKey().equals(item.getCKey())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(ifApproveList)) {
            ifApprove = ifApproveList.get(0);
        }
        List<SystemConfigDO> ifBatchApproveList = list.stream().filter(item -> SystemConfigKeyEnums.IF_BATCH_APPROVE.getKey().equals(item.getCKey())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(ifBatchApproveList)) {
            ifBatchApprove = ifBatchApproveList.get(0);
        }
        List<SystemConfigDO> ifBatchSubmitList = list.stream().filter(item -> SystemConfigKeyEnums.IF_BATCH_SUBMIT.getKey().equals(item.getCKey())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(ifBatchSubmitList)) {
            ifBatchSubmit = ifBatchSubmitList.get(0);
        }
        result.setIfFlowable(ifApprove.getCValue());
        result.setIfBatchSubmit(ifBatchSubmit.getCValue());
        result.setIfBatchApprove(ifBatchApprove.getCValue());
        return result;
    }

    @Override
    public RegionDataVersionRespVO getRegionDataVersion(RegionDataVersionReqVO reqVO) {
        String regionDataVersion = "0";
        String ifUpdate = "y";
        SystemConfigDO systemConfigDO = systemConfigMapper.selectOne(
                new LambdaQueryWrapperX<SystemConfigDO>()
                        .eq(SystemConfigDO::getCKey, SystemConfigKeyEnums.REGION_DATA_VERSION.getKey()));
        if (ObjectUtil.isNotNull(systemConfigDO)) {
            regionDataVersion = systemConfigDO.getCValue();
            ifUpdate = getIfUpdate(systemConfigDO, reqVO);
        }
        return new RegionDataVersionRespVO().setRegionDataVersion(regionDataVersion).setIfUpdate(ifUpdate);
    }

    private String getIfUpdate(SystemConfigDO systemConfigDO, RegionDataVersionReqVO reqVO) {
        if (Integer.valueOf(systemConfigDO.getCValue()).equals(Integer.valueOf(reqVO.getRegionDataVersion()))) {
            return IfEnums.NO.getCode();
        } else {
            return IfEnums.YES.getCode();
        }
    }

    @Override
    public List<SystemConfigRespVO> getConfigsByCKeys(List<String> cKeys) {
        List<SystemConfigDO> systemConfigDOS = systemConfigMapper.selectList(new LambdaQueryWrapperX<SystemConfigDO>()
                .inIfPresent(SystemConfigDO::getCKey, cKeys)
        );
        List<SystemConfigRespVO> respVOS = SystemConfigConverter.INSTANCE.convertListEntity2Resp(systemConfigDOS);
        return respVOS;
    }


    @Override
    public String setPermissionForApproveScanAnnotations(SystemConfigReqVO reqVO) {
        SystemConfigDO entity = new SystemConfigDO();
        entity = SystemConfigConverter.INSTANCE.req2Entity(reqVO);
        systemConfigMapper.update(entity,new LambdaQueryWrapperX<SystemConfigDO>().eq(SystemConfigDO::getCKey,SystemConfigKeyEnums.APPROVER_PERMISSION_SCAN_ANNOTATION.getKey()));
        return "success";
    }


    @Override
    public String setPermissionForCopyScanAnnotations(SystemConfigReqVO reqVO) {
        SystemConfigDO entity = new SystemConfigDO();
        entity = SystemConfigConverter.INSTANCE.req2Entity(reqVO);
        systemConfigMapper.update(entity,new LambdaQueryWrapperX<SystemConfigDO>().eq(SystemConfigDO::getCKey,SystemConfigKeyEnums.CARBON_COPY_PERMISSION_SCAN_ANNOTATION.getKey()));
        return "success";
    }

    @Override
    public String setSystemConfig(SystemConfigReqVO reqVO) {
        SystemConfigDO entity= SystemConfigConverter.INSTANCE.req2Entity(reqVO);
        systemConfigMapper.updateById(entity);
        return "success";
    }
    @Override
    public String getConfigByKey(String configKey) {
        SystemConfigDO systemConfigDO = systemConfigMapper.selectOne(new LambdaQueryWrapperX<SystemConfigDO>()
                .select(SystemConfigDO::getCValue)
                .eq(SystemConfigDO::getCKey, configKey)
                .orderByDesc(BaseDO::getCreateTime)
                .last(" limit 1"));
        return ObjectUtil.isNull(systemConfigDO) ? "" : systemConfigDO.getCValue();
    }

    @Override
    public SystemConfigDO ifNeedSignet(String cKey) {
        return systemConfigMapper.selectOne(SystemConfigDO::getCKey, cKey);
    }

    @Override
    public String getConfigValue(String key) {
      SystemConfigDO systemConfigDO=  systemConfigMapper.selectOne(SystemConfigDO::getCKey, key);
      if(ObjectUtil.isNull(systemConfigDO)){
          log.error("请先找管理员配置，key： " + key);
          throw exception(DIY_ERROR,"缺少必要配置信息。请联系管理员");
      }
        return systemConfigDO.getCValue();
    }

    @Override
    public SystemConfigDO getIfAddXmlByKey(String configKey) {
        return systemConfigMapper.selectOne(new LambdaQueryWrapperX<SystemConfigDO>()
                .select(SystemConfigDO::getCValue)
                .eq(SystemConfigDO::getCKey, configKey));
    }


}
