package com.yaoan.module.econtract.convert.alert;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.alert.vo.AlertRespVO;
import com.yaoan.module.econtract.dal.dataobject.alert.AlertDO;
import com.yaoan.module.econtract.enums.process.ContractProcessStageEnums;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/11/8 20:38
 */
@Mapper
public interface AlertConverter {

    AlertConverter INSTANCE = Mappers.getMapper(AlertConverter.class);

    List<AlertRespVO> convertDO2RespVO(List<AlertDO> entityList);

    @Named("getFlowStageStr")
    default String getFlowStageStr(String taskDefinitionKey) {
        String result = "";
        if (StringUtils.contains(taskDefinitionKey, ContractProcessStageEnums.CONTRACT_FLOW_STAGE_CONFIRM.getCode())) {
            result = ContractProcessStageEnums.CONTRACT_FLOW_STAGE_CONFIRM.getInfo();
        }
        if (StringUtils.contains(taskDefinitionKey, ContractProcessStageEnums.CONTRACT_FLOW_STAGE_SIGN.getCode())) {
            result = ContractProcessStageEnums.CONTRACT_FLOW_STAGE_SIGN.getInfo();
        }
        if (StringUtils.contains(taskDefinitionKey, ContractProcessStageEnums.CONTRACT_FLOW_STAGE_APPROVE_LAW_WORKS.getCode()) || StringUtils.contains(taskDefinitionKey,
                ContractProcessStageEnums.CONTRACT_FLOW_STAGE_APPROVE_DEPARTMENT_LEADER.getCode())) {
            result = ContractProcessStageEnums.CONTRACT_FLOW_STAGE_APPROVE_LAW_WORKS.getInfo();
        }
        if (StringUtils.contains(taskDefinitionKey, ContractProcessStageEnums.CONTRACT_FLOW_STAGE_CREATE.getCode())) {
            result = ContractProcessStageEnums.CONTRACT_FLOW_STAGE_CREATE.getInfo();
        }
        return result;
    }


    @Mapping(source = "flowStage", target = "flowStageStr", qualifiedByName = "getFlowStageStr")
    @Mapping(source = "flowStage", target = "flowStage")
    AlertRespVO convertDO2RespVO(AlertDO entity);

    PageResult<AlertRespVO> DOList2VOList(PageResult<AlertDO> pageResult);
}
