package com.yaoan.module.bpm.service.activity;

import cn.hutool.core.collection.CollUtil;
import com.yaoan.module.bpm.api.bpm.activity.BpmActivityApi;
//import com.yaoan.module.bpm.api.bpm.activity.dto.ActProcDefDTO;
//import com.yaoan.module.bpm.api.bpm.activity.dto.BpmProcessRespDTO;
//import com.yaoan.module.bpm.controller.admin.task.vo.activity.BpmProcessRespVO;
//import com.yaoan.module.bpm.convert.bpm.activity.BpmActivityConverter;
//import com.yaoan.module.bpm.convert.task.BpmActivityConvert;
//import com.yaoan.module.bpm.dal.dataobject.act.ActReProcdefDO;
//import com.yaoan.module.bpm.dal.mysql.act.ActReProcdefMapper;
import com.yaoan.module.bpm.api.bpm.activity.dto.ActProcDefDTO;
import com.yaoan.module.bpm.api.bpm.activity.dto.BpmProcessRespDTO;
import com.yaoan.module.bpm.service.task.BpmActivityService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/10/18 17:39
 */
@Service
public class BpmActivityApiImpl implements BpmActivityApi {

    @Resource
    private BpmActivityService bpmActivityService;

    @Resource
    private BpmActivityService activityService;

    /**
     * 获取审批配置信息
     *
     * @param approveCode {关联 com.yaoan.module.econtract.enums.ActivityConfigurationEnum}
     * @return 审批配置信息
     */
    @Override
    public List<BpmProcessRespDTO> getActivityAssignInfoByApproveCode(Integer approveCode) {
        return null;
    }

    /**
     * 获取审批进程信息
     *
     * @param processInstanceId 流程实例的编号
     * @return 审批进程
     */
    @Override
    public List<BpmProcessRespDTO> getActivityInfoListByProcessInstanceId(String processInstanceId) {
        return null;
    }

    /**
     * 获取审批记录信息
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public List<BpmProcessRespDTO> getActivityRecord(String processInstanceId) {
        return null;
    }

    @Override
    public ActProcDefDTO getActReProcdefByDefinitionId(String definitionId) {
        return null;
    }

//    @Resource
//    private ActReProcdefMapper actReProcdefMapper;
//
//    /**
//     * 获取审批配置信息
//     * @param approveCode {@link com.yaoan.module.econtract.enums.ActivityConfigurationEnum}
//     * @return 审批配置信息
//     */
//    @Override
//    public List<BpmProcessRespDTO> getActivityAssignInfoByApproveCode(Integer approveCode) {
//        List<BpmProcessRespVO> bpmProcessResVOList= bpmActivityService.getActivityAssignInfoByApproveCode(approveCode);
//        if(CollUtil.isEmpty(bpmProcessResVOList)){
//            return Collections.emptyList();
//        }
//        return BpmActivityConverter.INSTANCE.convertRespVO2DTO(bpmProcessResVOList);
//    }
//
//
//    /**
//     * 获取审批进程信息
//     * @param processInstanceId 流程实例的编号
//     * @return 审批进程
//     */
//    @Override
//    public List<BpmProcessRespDTO> getActivityInfoListByProcessInstanceId(String processInstanceId) {
//        List<BpmProcessRespVO> bpmProcessResVOList= bpmActivityService.getActivityInfoListByProcessInstanceId(processInstanceId);
//        if(CollUtil.isEmpty(bpmProcessResVOList)){
//            return Collections.emptyList();
//        }
//        return BpmActivityConverter.INSTANCE.convertRespVO2DTO(bpmProcessResVOList);
//    }
//
//    @Override
//    public List<BpmProcessRespDTO> getActivityRecord(String processInstanceId) {
//        List<BpmProcessRespVO> bpmProcessRespVOS = activityService.getActivityRecordByProcessInstanceId(processInstanceId);
//        if(CollUtil.isEmpty(bpmProcessRespVOS)){
//            return Collections.emptyList();
//        }
//        List<BpmProcessRespDTO> bpmProcessRespDTOS = BpmActivityConvert.INSTANCE.convertListV1(bpmProcessRespVOS);
//        return bpmProcessRespDTOS;
//    }
//
//    @Override
//    public ActProcDefDTO getActReProcdefByDefinitionId(String definitionId) {
//        ActReProcdefDO entity=actReProcdefMapper.selectById(definitionId);
//        ActProcDefDTO result=BpmActivityConverter.INSTANCE.convertProDO2DTO(entity);
//        return result;
//    }
}
