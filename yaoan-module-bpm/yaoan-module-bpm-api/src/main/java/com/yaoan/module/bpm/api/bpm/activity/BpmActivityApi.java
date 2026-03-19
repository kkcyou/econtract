package com.yaoan.module.bpm.api.bpm.activity;

import com.yaoan.module.bpm.api.bpm.activity.dto.ActProcDefDTO;
import com.yaoan.module.bpm.api.bpm.activity.dto.BpmProcessRespDTO;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/10/18 17:31
 */
public interface BpmActivityApi {

    /**
     * 获取审批配置信息
     *
     * @param approveCode {关联 com.yaoan.module.econtract.enums.ActivityConfigurationEnum}
     * @return 审批配置信息
     */
    List<BpmProcessRespDTO> getActivityAssignInfoByApproveCode(Integer approveCode);

    /**
     * 获取审批进程信息
     *
     * @param processInstanceId 流程实例的编号
     * @return 审批进程
     */
    List<BpmProcessRespDTO> getActivityInfoListByProcessInstanceId(String processInstanceId);

    /**
     * 获取审批记录信息
     * @param processInstanceId
     * @return
     */
    List<BpmProcessRespDTO> getActivityRecord (String processInstanceId);

    ActProcDefDTO getActReProcdefByDefinitionId(String definitionId);

}
