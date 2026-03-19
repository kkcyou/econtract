package com.yaoan.module.econtract.service.bpm.archive;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.yaoan.module.bpm.api.task.dto.BpmTaskAllInfoRespVO;
import com.yaoan.module.bpm.api.task.dto.ContractProcessInstanceRelationInfoRespDTO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.bpm.archive.vo.ArchiveBpmReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.archive.vo.BpmContractArchiveRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.archive.vo.PageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.archive.vo.PageRespVO;
import com.yaoan.module.econtract.controller.admin.change.vo.BigContractChangeListApproveRespVO;
import com.yaoan.module.econtract.controller.admin.change.vo.ContractChangeListApproveRespVO;
import com.yaoan.module.econtract.convert.config.SystemConfigDTOConverter;
import com.yaoan.module.econtract.convert.contract.ContractConverter;
import com.yaoan.module.econtract.convert.contractarchives.ContractArchivesConvert;
import com.yaoan.module.econtract.dal.dataobject.bpm.archive.BpmContractArchivesDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangeDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contractarchives.ContractArchivesDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.mysql.bpm.archive.BpmContractArchivesMapper;
import com.yaoan.module.econtract.dal.mysql.contractarchives.ContractArchivesMapper;
import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.StatusConstants;
import com.yaoan.module.econtract.enums.common.flow.FlowableStatusEnums;
import com.yaoan.module.econtract.util.EcontractUtil;
import com.yaoan.module.econtract.util.flowable.FlowableUtil;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Service
public class ArchiveBpmServiceImpl implements ArchiveBpmService {
    @Resource
    private BpmContractArchivesMapper bpmContractArchivesMapper;

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Resource
    private BpmTaskApi bpmTaskApi;

    @Resource
    private ContractArchivesMapper contractArchivesMapper;

    @Resource
    private AdminUserApi userApi;

    @Resource
    private SystemConfigApi systemConfigApi;


    @Override
    public String createProcess(Long loginUserId, ArchiveBpmReqVO reqVO) {
        String uuid = IdUtil.fastSimpleUUID();
        //检测归档文件是否有在途流程
        if (bpmContractArchivesMapper.selectCount(
                new LambdaQueryWrapper<BpmContractArchivesDO>()
                        .eq(BpmContractArchivesDO::getArchiveId, reqVO.getArchiveId())
                        .eq(BpmContractArchivesDO::getResult, 1)) > 0)
        {
            throw exception(ErrorCodeConstants.SYSTEM_ERROR, "归档文件有在途流程，请勿重复提交");
        }
        //发起流程
        Map<String, Object> processInstanceVariables = new HashMap<String, Object>(16);
        processInstanceVariables.put("archiveId", reqVO.getArchiveId());
        // 2.2 流程实例id
        String processInstanceId = processInstanceApi.createProcessInstance(loginUserId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(ActivityConfigurationEnum.CONTRACT_ARCHIVE_APPLY.getDefinitionKey())
                        .setVariables(processInstanceVariables).setBusinessKey(uuid));

        AdminUserRespDTO adminUserRespDTO = userApi.getUser(loginUserId);
        BpmContractArchivesDO bpmContractArchivesDO = new BpmContractArchivesDO()
                .setId(uuid)
                .setArchiveId(reqVO.getArchiveId())
                .setType(reqVO.getType())
                .setRemark(reqVO.getReason())
                .setResult(BpmProcessInstanceResultEnum.PROCESS.getResult())
                .setProcessInstanceId(processInstanceId)
                .setCreatorName(adminUserRespDTO.getNickname());
        bpmContractArchivesMapper.insert(bpmContractArchivesDO);
        return processInstanceId;
    }

    @Override
    public BpmContractArchiveRespVO getBpmAllTaskPage(Long loginUserId, PageReqVO pageVO) {
        // 查询所有任务
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(ActivityConfigurationEnum.CONTRACT_ARCHIVE_APPLY.getDefinitionKey());
        if (CollectionUtil.isEmpty(processInstanceRelationInfoRespDTOList)) {
            return new BpmContractArchiveRespVO();
        }
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<BpmContractArchivesDO> bpmContractArchivesDOPageResult = bpmContractArchivesMapper.selectBpmPage(pageVO);
        return enhanceBpmPage(bpmContractArchivesDOPageResult, instanceRelationInfoRespDTOMap);
    }

    @Override
    public BpmContractArchiveRespVO getBpmDoneTaskPage(Long loginUserId, PageReqVO pageVO) {
        Integer taskResult = StatusConstants.TEMP_INTEGER;
        // 查询待办任务
        if (ObjectUtil.isNotNull(pageVO.getTaskResult())) {
            taskResult = pageVO.getTaskResult();
        }
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(ActivityConfigurationEnum.CONTRACT_ARCHIVE_APPLY.getDefinitionKey(), taskResult);
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<BpmContractArchivesDO> bpmContractArchivesDOPageResult = bpmContractArchivesMapper.selectBpmPage(pageVO);
        return enhanceBpmPage(bpmContractArchivesDOPageResult, instanceRelationInfoRespDTOMap);
    }

    @Override
    public BpmContractArchiveRespVO getBpmToDoTaskPage(Long loginUserId, PageReqVO pageVO) {
        // 查询待办任务
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.CONTRACT_ARCHIVE_APPLY.getDefinitionKey());
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<BpmContractArchivesDO> bpmContractArchivesDOPageResult = bpmContractArchivesMapper.selectBpmPage(pageVO);
        return enhanceBpmPage(bpmContractArchivesDOPageResult, instanceRelationInfoRespDTOMap);
    }

    private BpmContractArchiveRespVO enhanceBpmPage(  PageResult<BpmContractArchivesDO> doPageResult, Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap) {
        List<BpmContractArchivesDO> doList = doPageResult.getList();
        List<PageRespVO> resultList = ContractArchivesConvert.INSTANCE.convertList(doList);
        if (CollectionUtil.isNotEmpty(doList)) {
            List<String> ids = doPageResult.getList().stream().map(BpmContractArchivesDO::getArchiveId).collect(Collectors.toList());
            List<ContractArchivesDO> contractArchivesDOS = contractArchivesMapper.selectBatchIds(ids);
            //档案map
            Map<String, ContractArchivesDO> contractArchivesMap = contractArchivesDOS.stream()
                    .collect(Collectors.toMap(
                            ContractArchivesDO::getId, // 键
                            contractArchivesDO -> contractArchivesDO // 值
                    ));
            //发起人信息map
            List<AdminUserRespDTO> userRespDTOList = userApi.getUserList();
            Map<Long, AdminUserRespDTO> userRespDTOMap = CollectionUtils.convertMap(userRespDTOList, AdminUserRespDTO::getId);
            //流程信息
            Map<String, BpmContractArchivesDO> bpmDOMap = CollectionUtils.convertMap(doList, BpmContractArchivesDO::getProcessInstanceId);
            List<String> instanceList = doList.stream().map(BpmContractArchivesDO::getProcessInstanceId).collect(Collectors.toList());

            Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            Map<String, BpmTaskAllInfoRespVO> taskEndTimeMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            Map<String, BpmTaskAllInfoRespVO> taskDoneEndTimeMap = new HashMap<String, BpmTaskAllInfoRespVO>();

            Long loginUserId = getLoginUserId();
            List<BpmTaskAllInfoRespVO> originalTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> taskEndTimeAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

            List<BpmTaskAllInfoRespVO> taskDoneEndTimeAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            //待处理的任务
            Map<String, BpmTaskAllInfoRespVO> toDoTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> toDoTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

            if (CollectionUtil.isNotEmpty(instanceList)) {
                originalTaskAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(Long.valueOf(loginUserId), instanceList);
                taskAllInfoRespVOList = EcontractUtil.distinctTask(originalTaskAllInfoRespVOList);
                taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

                toDoTaskAllInfoRespVOList = EcontractUtil.distinctTaskNullEndTimeByUserId(originalTaskAllInfoRespVOList, getLoginUserId());
                toDoTaskMap = CollectionUtils.convertMap(toDoTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

                taskDoneEndTimeAllInfoRespVOList = EcontractUtil.distinctDoneTaskLatestEndTime(originalTaskAllInfoRespVOList);
                taskDoneEndTimeMap = CollectionUtils.convertMap(taskDoneEndTimeAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            }

            //有结束时间的流程任务
            if (CollectionUtil.isNotEmpty(instanceList)) {
                taskEndTimeAllInfoRespVOList = EcontractUtil.distinctTaskHaveEndTime(originalTaskAllInfoRespVOList);
                taskEndTimeMap = CollectionUtils.convertMap(taskEndTimeAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            }
            for (PageRespVO respVO : resultList) {
                ContractArchivesDO contractArchivesDO = contractArchivesMap.get(respVO.getArchiveId());
                if (ObjectUtil.isNotEmpty(contractArchivesDO)) {
                    if (ObjectUtil.isNotEmpty(contractArchivesDO.getName())) {
                        respVO.setName(contractArchivesDO.getName());
                    }
                    if (ObjectUtil.isNotEmpty(contractArchivesDO.getCode())){
                        respVO.setCode(contractArchivesDO.getCode());
                    }
                    if (ObjectUtil.isNotEmpty(contractArchivesDO.getProCode())){
                        respVO.setProCode(contractArchivesDO.getProCode());
                    }
                    if (ObjectUtil.isNotEmpty(contractArchivesDO.getProName())){
                        respVO.setProName(contractArchivesDO.getProName());
                    }
                    if(ObjectUtil.isNotEmpty(contractArchivesDO.getContractId())){
                        respVO.setContractId(contractArchivesDO.getContractId());
                    }
                }
                respVO.setTaskId(taskMap.get(respVO.getProcessInstanceId()).getTaskId());
                BpmContractArchivesDO bpmDO = bpmDOMap.get(respVO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(bpmDO)) {
                    //流程实例
                    respVO.setProcessInstanceId(bpmDO.getProcessInstanceId());
                    //流程状态
                    respVO.setResult(bpmDO.getResult());
                    //流程任务
                    ContractProcessInstanceRelationInfoRespDTO processInstanceRelationInfoRespDTO = instanceRelationInfoRespDTOMap.get(bpmDO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(processInstanceRelationInfoRespDTO)) {
                        respVO.setTaskId(processInstanceRelationInfoRespDTO.getTaskId());
                    }
                }
                //全部审批列表的审批状态(找不到当前登录人的待办任务，既是已办任务)
                BpmTaskAllInfoRespVO toDoTask = toDoTaskMap.get(bpmDO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(toDoTask)) {
                    respVO.setFlowableStatus(FlowableUtil.getFlowableStatus(loginUserId, toDoTask.getAssigneeUserId()));
                    respVO.setFlowableStatusName(FlowableStatusEnums.getInstance(respVO.getFlowableStatus()).getInfo());

                }
                //审批状态(全部里)
                else {
                    respVO.setFlowableStatus(FlowableStatusEnums.DONE.getCode());
                    respVO.setFlowableStatusName(FlowableStatusEnums.getInstance(respVO.getFlowableStatus()).getInfo());
                }
                //已审批任务的状态赋值（当前人对该审批的最近的一次操作结果）
                ContractProcessInstanceRelationInfoRespDTO infoRespDTO = instanceRelationInfoRespDTOMap.get(bpmDO.getProcessInstanceId());
                if (ObjectUtil.isNotNull(infoRespDTO)) {
                    respVO.setDoneTaskResult(infoRespDTO.getProcessResult());
                }
                if(ObjectUtil.isNotEmpty(respVO.getType())){
                    if(respVO.getType() == 0){
                        respVO.setTypeName("归档");
                    }else{
                        respVO.setTypeName("补充");
                    }
                }
            }
            PageResult<PageRespVO> pageResult = new PageResult<PageRespVO>();
            pageResult.setList(resultList).setTotal(doPageResult.getTotal());
            BpmContractArchiveRespVO respVO = new BpmContractArchiveRespVO().setPageResult(pageResult);
            //获取配置
            respVO.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE)));
            return respVO;
        }
        BpmContractArchiveRespVO result = new BpmContractArchiveRespVO()
                .setPageResult(new PageResult<PageRespVO>()
                        .setList(Collections.emptyList())
                        .setTotal(doPageResult.getTotal())
                );
        result.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.CONTRACT_CHANGE_APPLICATION_APPROVE)));
        return result;
    }
}
