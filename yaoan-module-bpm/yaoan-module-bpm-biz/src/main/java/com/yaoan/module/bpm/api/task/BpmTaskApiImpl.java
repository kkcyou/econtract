package com.yaoan.module.bpm.api.task;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.google.common.collect.Maps;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.tenant.core.context.TenantContextHolder;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.module.bpm.api.task.dto.*;
import com.yaoan.module.bpm.api.task.dto.v2.BpmTaskApproveReqDTO;
import com.yaoan.module.bpm.controller.admin.task.vo.task.BpmTaskApproveReqVO;
import com.yaoan.module.bpm.controller.admin.task.vo.task.BpmTaskRejectReqVO;
import com.yaoan.module.bpm.convert.task.BpmTaskConvert;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.bpm.framework.flowable.core.util.FlowableUtils;
import com.yaoan.module.bpm.service.task.BpmProcessInstanceService;
import com.yaoan.module.bpm.service.task.BpmTaskService;
import com.yaoan.module.econtract.api.relative.RelativeApi;
import com.yaoan.module.econtract.enums.StatusConstants;
import com.yaoan.module.system.api.permission.PermissionApi;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.*;
import org.flowable.engine.HistoryService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.engine.TaskService;

import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.flowable.task.api.Task;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.common.util.collection.CollectionUtils.convertSet;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.module.bpm.enums.ErrorCodeConstants.*;
import static com.yaoan.module.bpm.enums.model.FlowableModelEnums.UPDATE;

import javax.annotation.Resource;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Flowable task Api 实现类
 *
 * @author doujiale
 */
@Service
@Validated
public class BpmTaskApiImpl implements BpmTaskApi {
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private BpmProcessInstanceService processInstanceService;
    //    @Resource
//    private BpmTaskAssignRuleMapper taskRuleMapper;
    @Resource
    private BpmTaskService bpmTaskService;
    @Resource
    private PermissionApi permissionApi;
    @Autowired
    private RelativeApi relativeApi;

    /**
     * 通过任务
     *
     * @param userId 用户编号
     * @param dto    通过请求
     */
    @Override
    public void approveTask(Long userId, BpmTaskApproveReqDTO dto) {
        BpmTaskApproveReqVO reqVO = BpmTaskConvert.INSTANCE.convertDTO2Req(dto);
        bpmTaskService.approveTask(userId, reqVO);
    }

    /**
     * 查询待办任务IDs
     *
     * @param userId             用户编号
     * @param processInstanceIds 流程示例IDs
     * @return 流程实例ID 和 taskId Map集合
     */
    @Override
    public Map<String, String> getTodoTaskList(Long userId, Collection<String> processInstanceIds) {
        return null;
    }

    @Override
    public List<BpmTaskAllInfoRespVO> getTodoTaskList(String processInstanceId) {
        // 查找当前流程实例中所有未完成的会签任务
        TaskQuery taskQuery = taskService.createTaskQuery()
                .processInstanceId(processInstanceId)
                .active();
        List<Task> uncompletedTasks = taskQuery.list();
        List<BpmTaskAllInfoRespVO> bpmTaskAllInfoRespVOS = uncompletedTasks.stream().map(task -> {
            BpmTaskAllInfoRespVO bpmTaskAllInfoRespVO = new BpmTaskAllInfoRespVO();
            bpmTaskAllInfoRespVO.setTaskId(task.getId());
            bpmTaskAllInfoRespVO.setAssigneeUserId(Long.valueOf(task.getAssignee()));
            bpmTaskAllInfoRespVO.setProcessInstanceId(task.getProcessInstanceId());
            bpmTaskAllInfoRespVO.setName(task.getName());
            return bpmTaskAllInfoRespVO;
        }).collect(Collectors.toList());
        return bpmTaskAllInfoRespVOS;
    }

    /**
     * 查询用户指定流程定义key的流程实例信息 包含待办和已办 代办包含任务ID，已办信息包含最新一次执行结果
     *
     * @param userId                用户编号
     * @param processDefinitionKeys 查询流程定义key
     * @return 流程实例IDs
     */
    @Override
    public List<ContractProcessInstanceRelationInfoRespDTO> getAllRelationProcessInstanceInfosByProcessDefinitionKeys(Long userId, Collection<String> processDefinitionKeys) {
        List<ContractProcessInstanceRelationInfoRespDTO> todoList = new ArrayList<>();
        List<Task> taskList = taskService.createTaskQuery().taskTenantId(TenantContextHolder.getTenantId().toString()).processDefinitionKeyIn(processDefinitionKeys).taskAssignee(String.valueOf(userId)).list();
        if (CollectionUtil.isNotEmpty(taskList)) {
            taskList.forEach(task -> {
                ContractProcessInstanceRelationInfoRespDTO contractProcessInstanceRelationInfoRespDTO = new ContractProcessInstanceRelationInfoRespDTO()
                        .setProcessInstanceId(task.getProcessInstanceId()).setTaskId(task.getId()).setProcessResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
                todoList.add(contractProcessInstanceRelationInfoRespDTO);
            });
        }
        List<ContractProcessInstanceRelationInfoRespDTO> doneList = new ArrayList<>();
        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery().processDefinitionKeyIn(processDefinitionKeys).taskTenantId(TenantContextHolder.getTenantId().toString()).finished().taskAssignee(String.valueOf(userId)).list();
        if (CollectionUtil.isNotEmpty(historicTasks)) {
            Map<String, HistoricTaskInstance> targetHistoricTasks = historicTasks.stream().collect(Collectors.toMap(HistoricTaskInstance::getProcessInstanceId, Function.identity(), (c1, c2) -> c1.getEndTime().after(c2.getEndTime()) ? c1 : c2));
            // 获得 TaskExtDO Map
            List<HistoricTaskInstance> bpmTaskExtDOs = historyService.createHistoricTaskInstanceQuery()
                    .taskIds(convertSet(targetHistoricTasks.values(), HistoricTaskInstance::getId))
                    .list();
            doneList = BpmTaskConvert.INSTANCE.convert2DTOList(targetHistoricTasks, bpmTaskExtDOs);
        }

        Map<String, ContractProcessInstanceRelationInfoRespDTO> todoMap = CollectionUtils.convertMap(todoList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        Map<String, ContractProcessInstanceRelationInfoRespDTO> doneMap = CollectionUtils.convertMap(doneList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        doneMap.putAll(todoMap);

        return new ArrayList<>(doneMap.values());
    }

    /**
     * 根据流程定义key和taskDefinitionKeyLike查询用户关联的流程实例信息 包含待办和已办 代办包含任务ID，已办信息包含最新一次执行结果`
     *
     * @param userId                用户编号
     * @param processDefinitionKeys 查询流程定义key
     * @param taskNameLike          任务定义key
     * @return 流程实例IDs
     */
    @Override
    public List<ContractProcessInstanceRelationInfoRespDTO> getAllRelationProcessInstanceInfosByProcessDefinitionKeys(Long userId, Collection<String> processDefinitionKeys, String taskNameLike) {
        //（相对方逻辑）免租户
        AtomicReference<Map<String, ContractProcessInstanceRelationInfoRespDTO>> atomic = new AtomicReference<>();
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {

                List<ContractProcessInstanceRelationInfoRespDTO> todoList = new ArrayList<>();
                List<Task> taskList = taskService.createTaskQuery().processDefinitionKeyIn(processDefinitionKeys).taskNameLike(taskNameLike).taskAssignee(String.valueOf(userId)).list();
                if (CollectionUtil.isNotEmpty(taskList)) {
                    taskList.forEach(task -> {
                        ContractProcessInstanceRelationInfoRespDTO contractProcessInstanceRelationInfoRespDTO = new ContractProcessInstanceRelationInfoRespDTO()
                                .setProcessInstanceId(task.getProcessInstanceId()).setTaskId(task.getId()).setProcessResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
                        todoList.add(contractProcessInstanceRelationInfoRespDTO);
                    });
                }
                List<ContractProcessInstanceRelationInfoRespDTO> doneList = new ArrayList<>();
                List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery().processDefinitionKeyIn(processDefinitionKeys).taskNameLike(taskNameLike).finished().taskAssignee(String.valueOf(userId)).list();
                if (CollectionUtil.isNotEmpty(historicTasks)) {
                    Map<String, HistoricTaskInstance> targetHistoricTasks = historicTasks.stream().collect(Collectors.toMap(HistoricTaskInstance::getProcessInstanceId, Function.identity(), (c1, c2) -> c1.getEndTime().after(c2.getEndTime()) ? c1 : c2));
                    // 获得 TaskExtDO Map
                    List<HistoricTaskInstance> bpmTaskExtDOs = historyService.createHistoricTaskInstanceQuery()
                            .taskIds(convertSet(targetHistoricTasks.values(), HistoricTaskInstance::getId))
                            .list();
                    doneList = BpmTaskConvert.INSTANCE.convert2DTOList(targetHistoricTasks, bpmTaskExtDOs);
                }

                Map<String, ContractProcessInstanceRelationInfoRespDTO> todoMap = CollectionUtils.convertMap(todoList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
                Map<String, ContractProcessInstanceRelationInfoRespDTO> doneMap = CollectionUtils.convertMap(doneList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
                doneMap.putAll(todoMap);
                atomic.set(doneMap);
            });
        });

        return new ArrayList<>(atomic.get().values());
    }

    @Override
    public List<ContractProcessInstanceRelationInfoRespDTO> getAllRelationProcessInstanceInfosByProcessDefinitionKeys(List<Long> userIds, Collection<String> processDefinitionKeys, String taskNameLike) {
        List taskAssigneeIds = userIds.stream().map(String::valueOf).collect(Collectors.toList());
        //（相对方逻辑）免租户
        AtomicReference<Map<String, ContractProcessInstanceRelationInfoRespDTO>> atomic = new AtomicReference<>();
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                List<Task> taskList =  new ArrayList<>();
                List<ContractProcessInstanceRelationInfoRespDTO> todoList = new ArrayList<>();
                if (StringUtils.isBlank(taskNameLike)){
                    taskList = taskService.createTaskQuery().processDefinitionKeyIn(processDefinitionKeys).taskAssigneeIds(taskAssigneeIds).list();
                } else {
                    taskList = taskService.createTaskQuery().processDefinitionKeyIn(processDefinitionKeys).taskNameLike(taskNameLike).taskAssigneeIds(taskAssigneeIds).list();
                }
                if (CollectionUtil.isNotEmpty(taskList)) {
                    taskList.forEach(task -> {
                        ContractProcessInstanceRelationInfoRespDTO contractProcessInstanceRelationInfoRespDTO = new ContractProcessInstanceRelationInfoRespDTO()
                                .setProcessInstanceId(task.getProcessInstanceId()).setTaskId(task.getId()).setProcessResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
                        todoList.add(contractProcessInstanceRelationInfoRespDTO);
                    });
                }
                List<ContractProcessInstanceRelationInfoRespDTO> doneList = new ArrayList<>();
                List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery().processDefinitionKeyIn(processDefinitionKeys).taskNameLike(taskNameLike).finished().taskAssigneeIds(taskAssigneeIds).list();
                if (CollectionUtil.isNotEmpty(historicTasks)) {
                    Map<String, HistoricTaskInstance> targetHistoricTasks = historicTasks.stream().collect(Collectors.toMap(HistoricTaskInstance::getProcessInstanceId, Function.identity(), (c1, c2) -> c1.getEndTime().after(c2.getEndTime()) ? c1 : c2));
                    // 获得 TaskExtDO Map
                    List<HistoricTaskInstance> bpmTaskExtDOs = historyService.createHistoricTaskInstanceQuery()
                            .taskIds(convertSet(targetHistoricTasks.values(), HistoricTaskInstance::getId))
                            .list();
                    doneList = BpmTaskConvert.INSTANCE.convert2DTOList(targetHistoricTasks, bpmTaskExtDOs);
                }

                Map<String, ContractProcessInstanceRelationInfoRespDTO> todoMap = CollectionUtils.convertMap(todoList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
                Map<String, ContractProcessInstanceRelationInfoRespDTO> doneMap = CollectionUtils.convertMap(doneList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
                doneMap.putAll(todoMap);
                atomic.set(doneMap);
            });
        });

        return new ArrayList<>(atomic.get().values());
    }

    @Override
    public List<ContractProcessInstanceRelationInfoRespDTO> getAllTODORelationProcessInstanceInfosByProcessDefinitionKeys(Long userId, Collection<String> processDefinitionKeys, String taskNameLike) {
        //（相对方逻辑）免租户
        AtomicReference<Map<String, ContractProcessInstanceRelationInfoRespDTO>> atomic = new AtomicReference<>();
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {

                List<ContractProcessInstanceRelationInfoRespDTO> todoList = new ArrayList<>();
                List<Task> taskList = taskService.createTaskQuery().processDefinitionKeyIn(processDefinitionKeys).taskNameLike(taskNameLike).taskAssignee(String.valueOf(userId)).list();
                if (CollectionUtil.isNotEmpty(taskList)) {
                    taskList.forEach(task -> {
                        ContractProcessInstanceRelationInfoRespDTO contractProcessInstanceRelationInfoRespDTO = new ContractProcessInstanceRelationInfoRespDTO()
                                .setProcessInstanceId(task.getProcessInstanceId()).setTaskId(task.getId()).setProcessResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
                        todoList.add(contractProcessInstanceRelationInfoRespDTO);
                    });
                }

                Map<String, ContractProcessInstanceRelationInfoRespDTO> todoMap = CollectionUtils.convertMap(todoList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
                atomic.set(todoMap);
            });
        });

        return new ArrayList<>(atomic.get().values());
    }

    @Override
    public List<ContractProcessInstanceRelationInfoRespDTO> getAllTODORelationProcessInstanceInfosByProcessDefinitionKeys(List<Long> userIds, Collection<String> processDefinitionKeys, String taskNameLike) {
        List taskAssigneeIds = userIds.stream().map(String::valueOf).collect(Collectors.toList());
        //（相对方逻辑）免租户
        AtomicReference<Map<String, ContractProcessInstanceRelationInfoRespDTO>> atomic = new AtomicReference<>();
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {

                List<ContractProcessInstanceRelationInfoRespDTO> todoList = new ArrayList<>();
                List<Task> taskList = new ArrayList<>();
                if (StringUtils.isBlank(taskNameLike)){
                    taskList = taskService.createTaskQuery().processDefinitionKeyIn(processDefinitionKeys).taskAssigneeIds(taskAssigneeIds).list();
                } else {
                    taskList = taskService.createTaskQuery().processDefinitionKeyIn(processDefinitionKeys).taskNameLike(taskNameLike).taskAssigneeIds(taskAssigneeIds).list();
                }
                if (CollectionUtil.isNotEmpty(taskList)) {
                    taskList.forEach(task -> {
                        ContractProcessInstanceRelationInfoRespDTO contractProcessInstanceRelationInfoRespDTO = new ContractProcessInstanceRelationInfoRespDTO()
                                .setProcessInstanceId(task.getProcessInstanceId()).setTaskId(task.getId()).setProcessResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
                        todoList.add(contractProcessInstanceRelationInfoRespDTO);
                    });
                }

                Map<String, ContractProcessInstanceRelationInfoRespDTO> todoMap = CollectionUtils.convertMap(todoList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
                atomic.set(todoMap);
            });
        });

        return new ArrayList<>(atomic.get().values());
    }

    /**
     * 处理任务API
     *
     * @param bpmHandleTaskReqDTO
     */
    @Override
    public void handleTask(BpmHandleTaskReqDTO bpmHandleTaskReqDTO) {
        bpmTaskService.handleTask(bpmHandleTaskReqDTO);
    }

    /**
     * 处理任务API
     *
     * @param bpmHandleTaskReqDTO
     */
    @Override
    public void handleTask2(BpmHandleTaskReqDTO bpmHandleTaskReqDTO) {
        bpmTaskService.handleTask2(bpmHandleTaskReqDTO);
    }

    @Override
    public void revokeTask(Long userId, TaskRevokeReqDTO taskRevokeReqDTO) {

        // taskRevokeReqDTO为封装的Task对象
        String processInstanceId = taskRevokeReqDTO.getProcessInstanceId();
        String myTaskId = taskRevokeReqDTO.getTaskId();
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                // 校验流程是否结束
                ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                        .processInstanceId(processInstanceId).active()
                        .singleResult();
                if (processInstance == null) {
                    throw exception(PROCESS_INSTANCE_NOT_EXISTS);
                }

                // 当前任务
                HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(myTaskId)
                        .processInstanceId(processInstanceId).singleResult();
                if (historicTaskInstance == null) {
                    throw exception(PROCESS_INSTANCE_NOT_EXISTS);
                }
                String myActivityId = null;
                List<HistoricActivityInstance> actInstList =
                        historyService.createHistoricActivityInstanceQuery()
                                .executionId(historicTaskInstance.getExecutionId())
                                .finished().list();
                for (HistoricActivityInstance hai : actInstList) {
                    if (myTaskId.equals(hai.getTaskId())) {
                        myActivityId = hai.getActivityId();
                        break;
                    }
                }
                BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
                FlowNode myFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(myActivityId);
                // 获取所有下一任务节点的标识ID
                Map<String, String> taskKeyMap = Maps.newHashMap();

                // 获取所有下一任务节点对应的FlowElement
                List<FlowElement> flowElementList = new ArrayList<>();
                selectNextFlowElements(bpmnModel, myActivityId, flowElementList);

                for (FlowElement flowElement : flowElementList) {
                    String eleId = flowElement.getId();
                    taskKeyMap.put(eleId, eleId);
                }

                // 获取当前流程代办事项，没有代办事项表明流程结束或已挂起
                List<Task> allTaskList = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
                if (allTaskList.size() <= 0) {
                    throw exception(PROCESS_INSTANCE_NOT_EXISTS);
                }

                // 判断所有下一任务节点中是否有代办任务，没有则表示任务已办理或已撤回，此时无法再执行撤回操作
                List<Task> nextTaskList = new ArrayList<>();

                for (Task task : allTaskList) {
                    if (taskKeyMap.containsKey(task.getTaskDefinitionKey())) {
                        nextTaskList.add(task);
                    }
                }
                if (nextTaskList.size() <= 0) {
                    throw exception(PROCESS_INSTANCE_NOT_EXISTS);
//                return Result.error("任务已办理或已撤回，无法执行撤回操作");
                }


                // 执行撤回操作
                for (Task task : nextTaskList) {
                    Execution execution = runtimeService.createExecutionQuery()
                            .executionId(task.getExecutionId()).singleResult();
                    String activityId = execution.getActivityId();
                    FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess()
                            .getFlowElement(activityId);
                    // 记录原活动方向
                    List<SequenceFlow> oriSequenceFlows = new ArrayList<SequenceFlow>();
                    oriSequenceFlows.addAll(flowNode.getOutgoingFlows());
                    flowNode.getOutgoingFlows().clear();
                    // 建立新方向
                    List<SequenceFlow> newSequenceFlowList = new ArrayList<SequenceFlow>();
                    SequenceFlow newSequenceFlow = new SequenceFlow();
                    newSequenceFlow.setId("sid-" + UUID.randomUUID().toString());
                    newSequenceFlow.setSourceFlowElement(flowNode);
                    newSequenceFlow.setTargetFlowElement(myFlowNode);
                    newSequenceFlowList.add(newSequenceFlow);

                    flowNode.setOutgoingFlows(newSequenceFlowList);

                    taskService.addComment(task.getId(), task.getProcessInstanceId(), "主动撤回");
                    taskService.resolveTask(task.getId());
                    taskService.claim(task.getId(), userId.toString());
                    taskService.complete(task.getId());
                    flowNode.setOutgoingFlows(oriSequenceFlows);
                }
            });
        });
    }

    public List<FlowElement> getNextTaskElements(BpmnModel bpmnModel, String activityId) {
        List<FlowElement> nextTaskElements = new ArrayList<>();

        // 获取指定活动的 FlowElement
        FlowElement currentFlowElement = bpmnModel.getFlowElement(activityId);

        if (currentFlowElement instanceof FlowNode) {
            FlowNode flowNode = (FlowNode) currentFlowElement;

            // 遍历输出流向，找到所有下一任务节点
            for (org.flowable.bpmn.model.SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
                FlowElement targetFlowElement = bpmnModel.getFlowElement(sequenceFlow.getTargetRef());
                if (targetFlowElement instanceof FlowNode) {
                    nextTaskElements.add(targetFlowElement);
                }
            }
        }

        return nextTaskElements;
    }

    /**
     * 发起人批量查看
     *
     * @param userId
     * @param processInstanceIds
     */
    @Override
    public List<BpmTaskAllInfoRespVO> getAllTaskInfoRespByProcessInstanceIds(Long userId, List<String> processInstanceIds) {
        return bpmTaskService.getAllTaskInfoRespByProcessInstanceIds(userId, processInstanceIds);
    }

    /**
     * 根据流程实例ID获取任务节点信息
     * 流程进程用-废弃
     *
     * @param processInstanceId 流程实例ID
     * @return 任务节点list
     */
    @Override
    public List<BpmTaskAssignRespDTO> getAllTaskNameByProcessInstanceId(String processInstanceId) {
        return null;
    }

    /**
     * 流程进程用-废弃
     *
     * @param processInstanceId
     * @return
     */
    @Override
    public List<BpmTaskRespDTO> getTaskInfoListByProcessInstanceId(String processInstanceId) {
        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId)
                .orderByHistoricTaskInstanceEndTime().desc().list();
        if (CollectionUtil.isEmpty(taskList)) {
            return new ArrayList<BpmTaskRespDTO>();
        }
        List<BpmTaskRespDTO> taskRespDTOList = new ArrayList<>();
        for (HistoricTaskInstance task : taskList) {
            BpmTaskRespDTO taskRespDTO = new BpmTaskRespDTO()
                    .setTaskId(task.getId())
                    .setAssignee(task.getAssignee())
                    .setClaimTime(task.getStartTime());


            taskRespDTOList.add(taskRespDTO);
        }
        return taskRespDTOList;
    }

    /**
     * 获取发起人,发起时间
     * 流程进程用-废弃
     *
     * @param processInstanceId 流程实例ID
     * @return 返回创建信息
     */
    @Override
    public BpmTaskCreateDTO getCreateInfoByProcessInstanceId(String processInstanceId) {
        return null;
    }

    /**
     * 流程定义Key
     *
     * @param definitionKey
     * @return 返回待办任务信息
     */
    @Override
    public List<ContractProcessInstanceRelationInfoRespDTO> getToDoTaskInfoByDefinitionKey(String definitionKey, String... definitionKeys) {
        List<String> combinedKeys = new ArrayList<>();
        combinedKeys.add(definitionKey);
        if (ObjectUtil.isNotEmpty(definitionKeys)) {
            Collections.addAll(combinedKeys, definitionKeys);
        }
        Long userId = getLoginUserId();
        List<Task> taskList = taskService.createTaskQuery().processDefinitionKeyIn(combinedKeys)
                .taskTenantId(TenantContextHolder.getTenantId().toString())
                .taskAssignee(String.valueOf(userId))
                .orderByTaskCreateTime().desc().list();
        //将退回申请人节点的任务过滤掉
        taskList = taskList.stream().filter(item -> !item.getTaskDefinitionKey().equals(UPDATE.getKey())).collect(Collectors.toList());
        List<ContractProcessInstanceRelationInfoRespDTO> respDTOList = new ArrayList<ContractProcessInstanceRelationInfoRespDTO>();

        for (Task task : taskList) {
            ContractProcessInstanceRelationInfoRespDTO dto = new ContractProcessInstanceRelationInfoRespDTO();
            dto.setTaskId(task.getId());
            dto.setProcessInstanceId(task.getProcessInstanceId());
//            List<BpmTaskExtDO> bpmTaskExtDOS = taskExtMapper.selectList(new LambdaQueryWrapperX<BpmTaskExtDO>().eq(BpmTaskExtDO::getProcessInstanceId, task.getProcessInstanceId()).orderByDesc(BpmTaskExtDO::getEndTime));
//            if (CollectionUtil.isNotEmpty(bpmTaskExtDOS)) {
//                dto.setLastResult(bpmTaskExtDOS.get(0).getResult());
//            }
//            dto.setAssignee(task.getAssignee());
            respDTOList.add(dto);
        }


        return respDTOList;
    }


    @Override
    public List<ContractProcessInstanceRelationInfoRespDTO> getToDoTaskInfoByDefinitionKey4Saas(String definitionKey, String... definitionKeys) {
        List<String> combinedKeys = new ArrayList<>();
        combinedKeys.add(definitionKey);
        if (ObjectUtil.isNotEmpty(definitionKeys)) {
            Collections.addAll(combinedKeys, definitionKeys);
        }
        Long userId = getLoginUserId();
        Long virtualId = relativeApi.getVirtualId4User(userId);
        List<Task> taskList = taskService.createTaskQuery().processDefinitionKeyIn(combinedKeys)
                .taskTenantId(TenantContextHolder.getTenantId().toString())
                .taskAssignee(String.valueOf(virtualId))
                .orderByTaskCreateTime().desc().list();
        //将退回申请人节点的任务过滤掉
        taskList = taskList.stream().filter(item -> !item.getTaskDefinitionKey().equals(UPDATE.getKey())).collect(Collectors.toList());
        List<ContractProcessInstanceRelationInfoRespDTO> respDTOList = new ArrayList<ContractProcessInstanceRelationInfoRespDTO>();

        for (Task task : taskList) {
            ContractProcessInstanceRelationInfoRespDTO dto = new ContractProcessInstanceRelationInfoRespDTO();
            dto.setTaskId(task.getId());
            dto.setProcessInstanceId(task.getProcessInstanceId());
            respDTOList.add(dto);
        }


        return respDTOList;
    }
    /**
     * 流程定义Key
     *
     * @param definitionKey
     * @return 返回待办任务信息
     */
    @Override
    public List<ContractProcessInstanceRelationInfoRespDTO> getALLToDoTaskInfoByDefinitionKey(String definitionKey, String... definitionKeys) {
        List<String> combinedKeys = new ArrayList<>();
        combinedKeys.add(definitionKey);
        if (ObjectUtil.isNotEmpty(definitionKeys)) {
            Collections.addAll(combinedKeys, definitionKeys);
        }
        List<Task> taskList = taskService.createTaskQuery().processDefinitionKeyIn(combinedKeys)
                .taskTenantId(TenantContextHolder.getTenantId().toString())
                .orderByTaskCreateTime().desc().list();
        //将退回申请人节点的任务过滤掉
        taskList = taskList.stream().filter(item -> !item.getTaskDefinitionKey().equals(UPDATE.getKey())).collect(Collectors.toList());
        List<ContractProcessInstanceRelationInfoRespDTO> respDTOList = new ArrayList<ContractProcessInstanceRelationInfoRespDTO>();

        for (Task task : taskList) {
            ContractProcessInstanceRelationInfoRespDTO dto = new ContractProcessInstanceRelationInfoRespDTO();
            dto.setTaskId(task.getId());
            dto.setProcessInstanceId(task.getProcessInstanceId());
//            List<BpmTaskExtDO> bpmTaskExtDOS = taskExtMapper.selectList(new LambdaQueryWrapperX<BpmTaskExtDO>().eq(BpmTaskExtDO::getProcessInstanceId, task.getProcessInstanceId()).orderByDesc(BpmTaskExtDO::getEndTime));
//            if (CollectionUtil.isNotEmpty(bpmTaskExtDOS)) {
//                dto.setLastResult(bpmTaskExtDOS.get(0).getResult());
//            }
//            dto.setAssignee(task.getAssignee());
            respDTOList.add(dto);
        }


        return respDTOList;
    }

    @Override
    public List<ContractProcessInstanceRelationInfoRespDTO> getToDoTaskInfoByDefinitionKeyAndStage(String definitionKey, TaskForWarningReqDTO params, String... stage) {
        List<String> combinedKeys = new ArrayList<>();
        combinedKeys.add(definitionKey);
        TaskQuery taskQuery = taskService.createTaskQuery().processDefinitionKeyIn(combinedKeys);
        if (StringUtils.isNotEmpty(stage[0])) {
            taskQuery.taskNameLike(stage[0]);
        }
        if (ObjectUtil.isNotEmpty(params.getEndDate())) {
            taskQuery.taskCreatedBefore(params.getEndDate());
        }
        if (ObjectUtil.isNotEmpty(params.getStartDate())) {
            taskQuery.taskCreatedAfter(params.getStartDate());
        }
        if (ObjectUtil.isNotEmpty(params.getProcessInstanceId())) {
            taskQuery.processInstanceId(params.getProcessInstanceId());
        }
        if (ObjectUtil.isNotEmpty(params.getTenantId())) {
            taskQuery.taskTenantId(String.valueOf(params.getTenantId()));
        }
        List<Task> taskList = taskQuery.orderByTaskCreateTime().desc().list();
        if (CollectionUtil.isEmpty(taskList)){
            return new ArrayList<>();
        }
        Set<String> pocessInstanceIds = taskList.stream().map(TaskInfo::getProcessInstanceId).collect(Collectors.toSet());
        Map<String, ProcessInstance> processInstanceMap = processInstanceService.getProcessInstanceMap(pocessInstanceIds);
        //将退回申请人节点的任务过滤掉
        taskList = taskList.stream().filter(item -> !item.getTaskDefinitionKey().equals(UPDATE.getKey())).collect(Collectors.toList());
        List<ContractProcessInstanceRelationInfoRespDTO> respDTOList = new ArrayList<ContractProcessInstanceRelationInfoRespDTO>();

        for (Task task : taskList) {
            ContractProcessInstanceRelationInfoRespDTO dto = new ContractProcessInstanceRelationInfoRespDTO();
            dto.setTaskId(task.getId());
            dto.setProcessInstanceId(task.getProcessInstanceId());
            dto.setCreateTime(task.getCreateTime());
            ProcessInstance processInstance = processInstanceMap.get(task.getProcessInstanceId());
            if (processInstance != null) {
                dto.setProcessInstanceCreator(processInstance.getStartUserId());
            }
//            List<BpmTaskExtDO> bpmTaskExtDOS = taskExtMapper.selectList(new LambdaQueryWrapperX<BpmTaskExtDO>().eq(BpmTaskExtDO::getProcessInstanceId, task.getProcessInstanceId()).orderByDesc(BpmTaskExtDO::getEndTime));
//            if (CollectionUtil.isNotEmpty(bpmTaskExtDOS)) {
//                dto.setLastResult(bpmTaskExtDOS.get(0).getResult());
//            }
            dto.setAssignee(task.getAssignee());
            respDTOList.add(dto);
        }


        return respDTOList;
    }

    /**
     * 流程定义Key
     *
     * @param definitionKey
     * @param processInstanceId
     * @return 该流程是否被审批过
     */

    @Override
    public Boolean existApprovedHisTask(String definitionKey, String processInstanceId) {

        Long userId = getLoginUserId();
        // 查询已办任务
        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                .taskTenantId(TenantContextHolder.getTenantId().toString())
                //符合流程实例id
                .processInstanceId(processInstanceId)
                //符合流程定义key
                .processDefinitionKey(definitionKey)
                // 已完成
                .finished()
                // 审批时间倒序
                .orderByHistoricTaskInstanceEndTime().desc().list();
        //存在已完成的任务则已经被审批过
        if (CollectionUtil.isNotEmpty(taskList)) {
            return true;
        }
        //没有已完成的任务则没有审批过
        return false;
    }


    @Override
    public List<ContractProcessInstanceRelationInfoRespDTO> getDoneTaskInfoByDefinitionKeyAndResult(String definitionKey, Integer taskResult, String... definitionKeys) {
        List<String> combinedKeys = new ArrayList<>();
        combinedKeys.add(definitionKey);
        if (ObjectUtil.isNotEmpty(definitionKeys)) {
            Collections.addAll(combinedKeys, definitionKeys);
        }
        Long userId = getLoginUserId();
        // 查询已办任务
        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                .taskTenantId(TenantContextHolder.getTenantId().toString())
                .processDefinitionKeyIn(combinedKeys)
                // 已完成
                .finished()
                // 分配给自己
                .taskAssignee(String.valueOf(userId))
                // 审批时间倒序
                .orderByHistoricTaskInstanceEndTime().desc().list();
        List<String> taskIds = taskList.stream().map(HistoricTaskInstance::getId).collect(Collectors.toList());
        //获得任务的处理结果
        List<BpmTaskAllInfoRespVO> bpmTaskAllInfoRespVOList = bpmTaskService.getAllTaskInfoRespByTaskIds(getLoginUserId(), taskIds);
//        Map<String, BpmTaskAllInfoRespVO> taskAllInfoRespVOMap = new HashMap<String, BpmTaskAllInfoRespVO>();
        if (CollectionUtil.isNotEmpty(bpmTaskAllInfoRespVOList)) {
            //过滤掉已取消的任务
            bpmTaskAllInfoRespVOList = bpmTaskAllInfoRespVOList.stream().filter(item -> !item.getResult().equals(BpmProcessInstanceResultEnum.CANCEL.getResult())).collect(Collectors.toList());
            //只保留最新的已办任务
            bpmTaskAllInfoRespVOList = distinctDoneTaskLatestEndTime(bpmTaskAllInfoRespVOList);
//            taskAllInfoRespVOMap = CollectionUtils.convertMap(bpmTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getTaskId);
//            taskAllInfoRespVOMap = saveLatestTask(taskAllInfoRespVOMap);
        }
        List<ContractProcessInstanceRelationInfoRespDTO> respDTOList = new ArrayList<ContractProcessInstanceRelationInfoRespDTO>();

//        for (HistoricTaskInstance task : taskList) {
//            ContractProcessInstanceRelationInfoRespDTO dto = new ContractProcessInstanceRelationInfoRespDTO();
//            dto.setTaskId(task.getId());
//            dto.setProcessInstanceId(task.getProcessInstanceId());
//            //获得任务的处理结果，便于之后去除已取消状态的任务。
//            BpmTaskAllInfoRespVO bpmTaskAllInfoRespVO = taskAllInfoRespVOMap.get(task.getId());
//            if (ObjectUtil.isNotNull(bpmTaskAllInfoRespVO)) {
//                dto.setProcessResult(bpmTaskAllInfoRespVO.getResult());
//            }
//            respDTOList.add(dto);
//        }
        List<ContractProcessInstanceRelationInfoRespDTO> result = new ArrayList<ContractProcessInstanceRelationInfoRespDTO>();
        for (BpmTaskAllInfoRespVO taskAllInfoRespVO : bpmTaskAllInfoRespVOList) {
            ContractProcessInstanceRelationInfoRespDTO rs = new ContractProcessInstanceRelationInfoRespDTO();
            rs.setTaskId(taskAllInfoRespVO.getTaskId());
            rs.setProcessInstanceId(taskAllInfoRespVO.getProcessInstanceId());
            rs.setProcessResult(taskAllInfoRespVO.getResult());
            rs.setLastestApproveTime(taskAllInfoRespVO.getEndTime());
            result.add(rs);
        }

        //如果有筛选审批状态
        if (StatusConstants.TEMP_INTEGER != taskResult) {
            return result.stream()
                    .filter(dto -> Objects.equals(dto.getProcessResult(), taskResult)) // 筛选出processResult等于result的元素
                    .filter(dto -> dto.getProcessResult() != null) //结果为空的，则不符合唯一已办条件
                    .collect(Collectors.toList());
        }
//        return respDTOList.stream()
//                .filter(dto -> dto.getProcessResult() != null) //结果为空的，则不符合唯一已办条件
//                .filter(dto -> !dto.getProcessResult().equals(BpmProcessInstanceResultEnum.CANCEL.getResult()))
//                .collect(Collectors.toList());
        //如果没有筛选审批状态
        return result;
    }



    @Override
    public List<ContractProcessInstanceRelationInfoRespDTO> getDoneTaskInfoByDefinitionKeyAndResult4Saas(String definitionKey, Integer taskResult, String... definitionKeys) {
        List<String> combinedKeys = new ArrayList<>();
        combinedKeys.add(definitionKey);
        if (ObjectUtil.isNotEmpty(definitionKeys)) {
            Collections.addAll(combinedKeys, definitionKeys);
        }
        Long userId = getLoginUserId();
        Long virtualId = relativeApi.getVirtualId4User(userId);
        // 查询已办任务
        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                .taskTenantId(TenantContextHolder.getTenantId().toString())
                .processDefinitionKeyIn(combinedKeys)
                // 已完成
                .finished()
                // 分配给相对方虚拟id
                .taskAssignee(String.valueOf(virtualId))
                // 审批时间倒序
                .orderByHistoricTaskInstanceEndTime().desc().list();
        List<String> taskIds = taskList.stream().map(HistoricTaskInstance::getId).collect(Collectors.toList());
        //获得任务的处理结果
        List<BpmTaskAllInfoRespVO> bpmTaskAllInfoRespVOList = bpmTaskService.getAllTaskInfoRespByTaskIds(virtualId, taskIds);
        if (CollectionUtil.isNotEmpty(bpmTaskAllInfoRespVOList)) {
            //过滤掉已取消的任务
            bpmTaskAllInfoRespVOList = bpmTaskAllInfoRespVOList.stream().filter(item -> !item.getResult().equals(BpmProcessInstanceResultEnum.CANCEL.getResult())).collect(Collectors.toList());
            //只保留最新的已办任务
            bpmTaskAllInfoRespVOList = distinctDoneTaskLatestEndTime4Saas(virtualId,bpmTaskAllInfoRespVOList);

        }

        List<ContractProcessInstanceRelationInfoRespDTO> result = new ArrayList<ContractProcessInstanceRelationInfoRespDTO>();
        for (BpmTaskAllInfoRespVO taskAllInfoRespVO : bpmTaskAllInfoRespVOList) {
            ContractProcessInstanceRelationInfoRespDTO rs = new ContractProcessInstanceRelationInfoRespDTO();
            rs.setTaskId(taskAllInfoRespVO.getTaskId());
            rs.setProcessInstanceId(taskAllInfoRespVO.getProcessInstanceId());
            rs.setProcessResult(taskAllInfoRespVO.getResult());
            rs.setLastestApproveTime(taskAllInfoRespVO.getEndTime());
            result.add(rs);
        }

        //如果有筛选审批状态
        if (StatusConstants.TEMP_INTEGER != taskResult) {
            return result.stream()
                    .filter(dto -> Objects.equals(dto.getProcessResult(), taskResult)) // 筛选出processResult等于result的元素
                    .filter(dto -> dto.getProcessResult() != null) //结果为空的，则不符合唯一已办条件
                    .collect(Collectors.toList());
        }

        //如果没有筛选审批状态
        return result;
    }
    /**
     * 流程定义Key
     *
     * @param definitionKey
     * @return 返回所有任务信息
     */
    @Override
    public List<ContractProcessInstanceRelationInfoRespDTO> getAllTaskInfoByDefinitionKey(String definitionKey, String... definitionKeys) {

        // 查询已办任务
        List<ContractProcessInstanceRelationInfoRespDTO> respDoneDTOList = getDoneTaskInfoByDefinitionKey(definitionKey, definitionKeys);
        //待办任务
        List<ContractProcessInstanceRelationInfoRespDTO> respToDoDTOList = getToDoTaskInfoByDefinitionKey(definitionKey, definitionKeys);

        Map<String, ContractProcessInstanceRelationInfoRespDTO> todoMap = CollectionUtils.convertMap(respToDoDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        Map<String, ContractProcessInstanceRelationInfoRespDTO> doneMap = CollectionUtils.convertMap(respDoneDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        doneMap.putAll(todoMap);
        return new ArrayList<ContractProcessInstanceRelationInfoRespDTO>(doneMap.values());
    }

    @Override
    public List<ContractProcessInstanceRelationInfoRespDTO> getAllTaskInfoByDefinitionKey4Saas(String definitionKey, String... definitionKeys) {

        // 查询已办任务
        List<ContractProcessInstanceRelationInfoRespDTO> respDoneDTOList = getDoneTaskInfoByDefinitionKey4Saas(definitionKey, definitionKeys);
        //待办任务
        List<ContractProcessInstanceRelationInfoRespDTO> respToDoDTOList = getToDoTaskInfoByDefinitionKey4Saas(definitionKey, definitionKeys);

        Map<String, ContractProcessInstanceRelationInfoRespDTO> todoMap = CollectionUtils.convertMap(respToDoDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        Map<String, ContractProcessInstanceRelationInfoRespDTO> doneMap = CollectionUtils.convertMap(respDoneDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        doneMap.putAll(todoMap);
        return new ArrayList<ContractProcessInstanceRelationInfoRespDTO>(doneMap.values());
    }

    public List<ContractProcessInstanceRelationInfoRespDTO> getAllTasksInfoByDefinitionKey(String definitionKey) {
        Long userId = getLoginUserId();
        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                .taskTenantId(TenantContextHolder.getTenantId().toString())
                .processDefinitionKey(definitionKey)
                // 分配给自己
                .taskAssignee(String.valueOf(userId))
                // 审批时间倒序
                .orderByHistoricTaskInstanceEndTime().desc().list();
        List<String> processInstanceIds = taskList.stream().map(HistoricTaskInstance::getProcessInstanceId).collect(Collectors.toList());
        List<BpmTaskAllInfoRespVO> bpmTaskAllInfoRespVOList = bpmTaskService.getAllTaskInfoRespByProcessInstanceIds(getLoginUserId(), processInstanceIds);
        Map<String, BpmTaskAllInfoRespVO> taskAllInfoRespVOMap = new HashMap<String, BpmTaskAllInfoRespVO>();
        if (CollectionUtil.isNotEmpty(bpmTaskAllInfoRespVOList)) {
            taskAllInfoRespVOMap = CollectionUtils.convertMap(bpmTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getTaskId);
        }
        List<ContractProcessInstanceRelationInfoRespDTO> respDTOList = new ArrayList<ContractProcessInstanceRelationInfoRespDTO>();
        for (HistoricTaskInstance task : taskList) {
            ContractProcessInstanceRelationInfoRespDTO dto = new ContractProcessInstanceRelationInfoRespDTO();
            dto.setTaskId(task.getId());
            dto.setProcessInstanceId(task.getProcessInstanceId());
            dto.setLastestApproveTime(task.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            BpmTaskAllInfoRespVO bpmTaskAllInfoRespVO = taskAllInfoRespVOMap.get(task.getId());
            //给每个任务赋值操作结果
            if (ObjectUtil.isNotNull(bpmTaskAllInfoRespVO)) {
                dto.setProcessResult(bpmTaskAllInfoRespVO.getResult());
            }
//            BpmTaskExtDO bpmTaskExtDO = taskExtMapper.selectByTaskId(task.getId());
//            dto.setAssignee(bpmTaskExtDO.getAssigneeUserId().toString());
//            dto.setProcessResult(bpmTaskExtDO.getResult());

            respDTOList.add(dto);
        }
        //将已取消的任务去除
        return respDTOList.stream()
                .filter(dto -> !BpmProcessInstanceResultEnum.CANCEL.getResult().equals(dto.getProcessResult()))
                .collect(Collectors.toList());
    }

    /**
     * 根据流程实例ids
     *
     * @param processIds
     * @return 返回所有任务信息
     */
    @Override
    public List<SimpleTaskDTO> getAllTaskInfoByProcessIds(List<String> processIds) {
        return null;
    }

    /**
     * 根据流程实例ids
     *
     * @param processIds
     * @return 返回所有任务信息
     */
    @Override
    public List<SimpleTaskDTO> getNextUntreatedTaskInfoByProcessIds(List<String> processIds) {
        return null;
    }

    @Override
    public boolean ifUpdaterSubmit(String definitionKey, String processInstanceId) {
        return false;
    }

    @Override
    public List<SimpleTaskDTO> getBpmTaskByTaskIds(List<String> taskIds) {
        return null;
    }

    /**
     * 获得上一个节点的审批人信息(比如：高级审批人获得初级审批人的信息)
     *
     * @param processDefinitionId
     * @param processInstanceId
     * @param taskName
     * @param submitterId
     */
    @Override
    public Set<Long> getLastApproverInfos(String processDefinitionId, String processInstanceId, String taskName, Long submitterId) {
        return null;
    }

    /**
     * 根据 processInstanceId 找到 processDefinitionId
     *
     * @param processInstanceId
     */
    @Override
    public String getProcessDefinitionIdByProcessInstanceId(String processInstanceId) {
        return null;
    }

    @Override
    public BpmTaskRespDTO getTaskRespDTO(String taskId) {
        return BpmTaskConvert.INSTANCE.d2r2(historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult());
    }

    @Override
    public BpmTaskRespDTO getRunTaskRespDTO(String taskId) {
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
        return new BpmTaskRespDTO().setTaskId(taskId).setAssignee(task.getAssignee()).setProcessInstanceId(task.getProcessInstanceId());
    }

    @Override
    public List<BpmTaskAllInfoRespVO> getAllTaskIdAndAssigneeNameByProcessInstanceIds(Long loginUserId, List<String> instanceList) {
        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery()
                .includeTaskLocalVariables()
                .taskAssignee(String.valueOf(loginUserId))
                .processInstanceIdIn(instanceList)
                .taskTenantId(FlowableUtils.getTenantId());
        List<HistoricTaskInstance> tasks = taskQuery.list();
        List<BpmTaskAllInfoRespVO> allInfoRespVOS = BpmTaskConvert.INSTANCE.convertListHisTaskInstance2AllRespVo(tasks);
        return allInfoRespVOS;
    }

    @Override
    public List<BpmTaskAllInfoRespVO> getAllTaskIdByProcessInstanceIds(List<String> instanceList) {
        // 过滤掉列表中的null值
        instanceList = instanceList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        HistoricTaskInstanceQuery taskQuery = historyService.createHistoricTaskInstanceQuery()
                .includeTaskLocalVariables()
                .processInstanceIdIn(instanceList)
                .taskTenantId(FlowableUtils.getTenantId());
        List<HistoricTaskInstance> tasks = taskQuery.list();
        List<BpmTaskAllInfoRespVO> allInfoRespVOS = BpmTaskConvert.INSTANCE.convertListHisTaskInstance2AllRespVo(tasks);
        return allInfoRespVOS;
    }

    @Override
    public void rejectTask(Long userId, BpmTaskRejectReqDTO reqVO) {
        BpmTaskRejectReqVO vo = new BpmTaskRejectReqVO();
        vo.setId(reqVO.getId());
        vo.setReason(reqVO.getReason());
        bpmTaskService.rejectTask(userId, vo);
    }

    @Override
    public void setTaskVariable(Map<String, Object> reqVO, String... taskId) {
        for (String s : taskId) {
            reqVO.forEach(
                    (key, value) -> taskService.setVariable(s, key, value)
            );
        }
    }


//
//    @Resource
//    private TaskService taskService;
//    @Resource
//    private BpmTaskExtMapper taskExtMapper;
//    @Resource
//    private HistoryService historyService;
//    @Resource
//    private RuntimeService runtimeService;
//    @Resource
//    private RepositoryService repositoryService;
//    @Resource
//    private BpmProcessInstanceService processInstanceService;
//    @Resource
//    private BpmTaskAssignRuleMapper taskRuleMapper;
//    @Resource
//    private BpmTaskService bpmTaskService;
//    @Resource
//    private PermissionApi permissionApi;
//
//    @Override
//    public Map<String, String> getTodoTaskList(Long userId, Collection<String> processInstanceIds) {
//        List<Task> taskList = taskService.createTaskQuery().taskTenantId(TenantContextHolder.getTenantId().toString()).processInstanceIdIn(processInstanceIds).taskAssignee(String.valueOf(userId))
//                .orderByTaskCreateTime().desc().list();
//        return CollectionUtils.convertMap(taskList, Task::getProcessInstanceId, Task::getId);
//    }
//
//    @Override
//    public List<ContractProcessInstanceRelationInfoRespDTO> getTodoTaskListV2(Long userId, Collection<String> processInstanceIds) {
//        List<Task> taskList = taskService.createTaskQuery().taskTenantId(TenantContextHolder.getTenantId().toString()).processInstanceIdIn(processInstanceIds).taskAssignee(String.valueOf(userId))
//                .orderByTaskCreateTime().desc().list();
//        List<ContractProcessInstanceRelationInfoRespDTO> result = BpmTaskConvert.INSTANCE.listDO2DTO(taskList);
//        return result;
//    }
//
//    @Override
//    public List<String> getAllRelationProcessInstanceIds(Long userId) {
//
//        List<Task> taskList = taskService.createTaskQuery().taskTenantId(TenantContextHolder.getTenantId().toString()).taskAssignee(String.valueOf(userId)).list();
//        List<String> todoProcessInstanceIds = CollectionUtils.convertList(taskList, Task::getProcessInstanceId);
//        List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().finished().taskTenantId(TenantContextHolder.getTenantId().toString()).taskAssignee(String.valueOf(userId)).list();
//        List<String> historicProcessInstanceIds = CollectionUtils.convertList(historicTaskInstances, HistoricTaskInstance::getProcessInstanceId);
//
//        return CollectionUtil.union(todoProcessInstanceIds, historicProcessInstanceIds).stream().distinct().collect(Collectors.toList());
//    }
//
//    @Override
//    public List<ContractProcessInstanceRelationInfoRespDTO> getAllRelationProcessInstanceInfos(Long userId) {
//
//        List<ContractProcessInstanceRelationInfoRespDTO> todoList = new ArrayList<>();
//        List<Task> taskList = taskService.createTaskQuery().taskTenantId(TenantContextHolder.getTenantId().toString()).taskAssignee(String.valueOf(userId)).list();
//        if (CollectionUtil.isNotEmpty(taskList)) {
//            taskList.forEach(task -> {
//                ContractProcessInstanceRelationInfoRespDTO contractProcessInstanceRelationInfoRespDTO = new ContractProcessInstanceRelationInfoRespDTO()
//                        .setProcessInstanceId(task.getProcessInstanceId()).setTaskId(task.getId()).setProcessResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
//                todoList.add(contractProcessInstanceRelationInfoRespDTO);
//            });
//        }
//        List<ContractProcessInstanceRelationInfoRespDTO> doneList = new ArrayList<>();
//        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery().taskTenantId(TenantContextHolder.getTenantId().toString()).finished().taskAssignee(String.valueOf(userId)).list();
//        if (CollectionUtil.isNotEmpty(historicTasks)) {
//            Map<String, HistoricTaskInstance> targetHistoricTasks = historicTasks.stream().collect(Collectors.toMap(HistoricTaskInstance::getProcessInstanceId, Function.identity(), (c1, c2) -> c1.getEndTime().after(c2.getEndTime()) ? c1 : c2));
//            // 获得 TaskExtDO Map
//            List<BpmTaskExtDO> bpmTaskExtDOs = taskExtMapper.selectListByTaskIds(convertSet(targetHistoricTasks.values(), HistoricTaskInstance::getId));
//            doneList = BpmTaskConvert.INSTANCE.convert2DTOList(targetHistoricTasks, bpmTaskExtDOs);
//        }
//
//        Map<String, ContractProcessInstanceRelationInfoRespDTO> todoMap = CollectionUtils.convertMap(todoList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
//        Map<String, ContractProcessInstanceRelationInfoRespDTO> doneMap = CollectionUtils.convertMap(doneList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
//        doneMap.putAll(todoMap);
//
//        return new ArrayList<>(doneMap.values());
//    }
//
//    @Override
//    public List<ContractProcessInstanceRelationInfoRespDTO> getAllRelationProcessInstanceInfosByProcessDefinitionKeys(Long userId, Collection<String> processDefinitionKeys) {
//
//        List<ContractProcessInstanceRelationInfoRespDTO> todoList = new ArrayList<>();
//        List<Task> taskList = taskService.createTaskQuery().taskTenantId(TenantContextHolder.getTenantId().toString()).processDefinitionKeyIn(processDefinitionKeys).taskAssignee(String.valueOf(userId)).list();
//        if (CollectionUtil.isNotEmpty(taskList)) {
//            taskList.forEach(task -> {
//                ContractProcessInstanceRelationInfoRespDTO contractProcessInstanceRelationInfoRespDTO = new ContractProcessInstanceRelationInfoRespDTO()
//                        .setProcessInstanceId(task.getProcessInstanceId()).setTaskId(task.getId()).setProcessResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
//                todoList.add(contractProcessInstanceRelationInfoRespDTO);
//            });
//        }
//        List<ContractProcessInstanceRelationInfoRespDTO> doneList = new ArrayList<>();
//        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery().processDefinitionKeyIn(processDefinitionKeys).taskTenantId(TenantContextHolder.getTenantId().toString()).finished().taskAssignee(String.valueOf(userId)).list();
//        if (CollectionUtil.isNotEmpty(historicTasks)) {
//            Map<String, HistoricTaskInstance> targetHistoricTasks = historicTasks.stream().collect(Collectors.toMap(HistoricTaskInstance::getProcessInstanceId, Function.identity(), (c1, c2) -> c1.getEndTime().after(c2.getEndTime()) ? c1 : c2));
//            // 获得 TaskExtDO Map
//            List<BpmTaskExtDO> bpmTaskExtDOs = taskExtMapper.selectListByTaskIds(convertSet(targetHistoricTasks.values(), HistoricTaskInstance::getId));
//            doneList = BpmTaskConvert.INSTANCE.convert2DTOList(targetHistoricTasks, bpmTaskExtDOs);
//        }
//
//        Map<String, ContractProcessInstanceRelationInfoRespDTO> todoMap = CollectionUtils.convertMap(todoList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
//        Map<String, ContractProcessInstanceRelationInfoRespDTO> doneMap = CollectionUtils.convertMap(doneList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
//        doneMap.putAll(todoMap);
//
//        return new ArrayList<>(doneMap.values());
//    }
//
//    @Override
//    public List<ContractProcessInstanceRelationInfoRespDTO> getAllRelationProcessInstanceInfosByProcessDefinitionKeys(Long userId, Collection<String> processDefinitionKeys, String taskNameLike) {
//
//        List<ContractProcessInstanceRelationInfoRespDTO> todoList = new ArrayList<>();
//        List<Task> taskList = taskService.createTaskQuery().taskTenantId(TenantContextHolder.getTenantId().toString()).processDefinitionKeyIn(processDefinitionKeys).taskNameLike(taskNameLike).taskAssignee(String.valueOf(userId)).list();
//        if (CollectionUtil.isNotEmpty(taskList)) {
//            taskList.forEach(task -> {
//                ContractProcessInstanceRelationInfoRespDTO contractProcessInstanceRelationInfoRespDTO = new ContractProcessInstanceRelationInfoRespDTO()
//                        .setProcessInstanceId(task.getProcessInstanceId()).setTaskId(task.getId()).setProcessResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
//                todoList.add(contractProcessInstanceRelationInfoRespDTO);
//            });
//        }
//        List<ContractProcessInstanceRelationInfoRespDTO> doneList = new ArrayList<>();
//        List<HistoricTaskInstance> historicTasks = historyService.createHistoricTaskInstanceQuery().taskTenantId(TenantContextHolder.getTenantId().toString()).processDefinitionKeyIn(processDefinitionKeys).taskNameLike(taskNameLike).finished().taskAssignee(String.valueOf(userId)).list();
//        if (CollectionUtil.isNotEmpty(historicTasks)) {
//            Map<String, HistoricTaskInstance> targetHistoricTasks = historicTasks.stream().collect(Collectors.toMap(HistoricTaskInstance::getProcessInstanceId, Function.identity(), (c1, c2) -> c1.getEndTime().after(c2.getEndTime()) ? c1 : c2));
//            // 获得 TaskExtDO Map
//            List<BpmTaskExtDO> bpmTaskExtDOs = taskExtMapper.selectListByTaskIds(convertSet(targetHistoricTasks.values(), HistoricTaskInstance::getId));
//            doneList = BpmTaskConvert.INSTANCE.convert2DTOList(targetHistoricTasks, bpmTaskExtDOs);
//        }
//
//        Map<String, ContractProcessInstanceRelationInfoRespDTO> todoMap = CollectionUtils.convertMap(todoList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
//        Map<String, ContractProcessInstanceRelationInfoRespDTO> doneMap = CollectionUtils.convertMap(doneList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
//        doneMap.putAll(todoMap);
//
//        return new ArrayList<>(doneMap.values());
//    }
//
//    @Override
//    public void handleTask(BpmHandleTaskReqDTO bpmHandleTaskReqDTO) {
//
//        // 校验任务存在
//        Task task = checkTask(bpmHandleTaskReqDTO.getUserId(), bpmHandleTaskReqDTO.getTaskId());
//        // 校验流程实例存在
//        ProcessInstance instance = processInstanceService.getProcessInstance(task.getProcessInstanceId());
//        if (instance == null) {
//            throw exception(PROCESS_INSTANCE_NOT_EXISTS);
//        }
//
//        // 完成任务，审批通过
//        taskService.complete(task.getId(), bpmHandleTaskReqDTO.getVariables());
//
//        // 更新任务拓展表为通过
//        taskExtMapper.updateByTaskId(
//                new BpmTaskExtDO().setTaskId(task.getId()).setResult(bpmHandleTaskReqDTO.getResult()).setReason(bpmHandleTaskReqDTO.getReason()));
//
//    }
//
//    @Override
//    public void revokeTask(Long userId, TaskRevokeReqDTO taskRevokeReqDTO) {
//
//        // taskRevokeReqDTO为封装的Task对象
//        String processInstanceId = taskRevokeReqDTO.getProcessInstanceId();
//        String myTaskId = taskRevokeReqDTO.getTaskId();
//        // 校验流程是否结束
//        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceTenantId(TenantContextHolder.getTenantId().toString())
//                .processInstanceId(processInstanceId).active()
//                .singleResult();
//        if (processInstance == null) {
//            throw exception(PROCESS_INSTANCE_NOT_EXISTS);
//        }
//
//        // 当前任务
//        HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(myTaskId).taskTenantId(TenantContextHolder.getTenantId().toString())
//                .processInstanceId(processInstanceId).singleResult();
//        if (historicTaskInstance == null) {
//            throw exception(PROCESS_INSTANCE_NOT_EXISTS);
//        }
//        String myActivityId = null;
//        List<HistoricActivityInstance> actInstList =
//                historyService.createHistoricActivityInstanceQuery().activityTenantId(TenantContextHolder.getTenantId().toString())
//                        .executionId(historicTaskInstance.getExecutionId())
//                        .finished().list();
//        for (HistoricActivityInstance hai : actInstList) {
//            if (myTaskId.equals(hai.getTaskId())) {
//                myActivityId = hai.getActivityId();
//                break;
//            }
//        }
//        BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
//        FlowNode myFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(myActivityId);
//        // 获取所有下一任务节点的标识ID
//        Map<String, String> taskKeyMap = Maps.newHashMap();
//
//        // 获取所有下一任务节点对应的FlowElement
//        List<FlowElement> flowElementList = getNextTaskElements(bpmnModel, myActivityId);
//
//        for (FlowElement flowElement : flowElementList) {
//            String eleId = flowElement.getId();
//            taskKeyMap.put(eleId, eleId);
//        }
//
//        // 获取当前流程代办事项，没有代办事项表明流程结束或已挂起
//        List<Task> allTaskList = taskService.createTaskQuery().taskTenantId(TenantContextHolder.getTenantId().toString()).processInstanceId(processInstanceId).list();
//        if (allTaskList.size() <= 0) {
//            throw exception(PROCESS_INSTANCE_NOT_EXISTS);
//        }
//
//        // 判断所有下一任务节点中是否有代办任务，没有则表示任务已办理或已撤回，此时无法再执行撤回操作
//        List<Task> nextTaskList = Lists.newArrayList();
//
//        for (Task task : allTaskList) {
//            if (taskKeyMap.containsKey(task.getTaskDefinitionKey())) {
//                nextTaskList.add(task);
//            }
//        }
//        if (nextTaskList.size() <= 0) {
//            throw exception(PROCESS_INSTANCE_NOT_EXISTS);
////                return Result.error("任务已办理或已撤回，无法执行撤回操作");
//        }
//
//
//        // 执行撤回操作
//        for (Task task : nextTaskList) {
//            Execution execution = runtimeService.createExecutionQuery().executionTenantId(TenantContextHolder.getTenantId().toString())
//                    .executionId(task.getExecutionId()).singleResult();
//            String activityId = execution.getActivityId();
//            FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess()
//                    .getFlowElement(activityId);
//            // 记录原活动方向
//            List<SequenceFlow> oriSequenceFlows = new ArrayList<SequenceFlow>();
//            oriSequenceFlows.addAll(flowNode.getOutgoingFlows());
//            flowNode.getOutgoingFlows().clear();
//            // 建立新方向
//            List<SequenceFlow> newSequenceFlowList = new ArrayList<SequenceFlow>();
//            SequenceFlow newSequenceFlow = new SequenceFlow();
//            newSequenceFlow.setId("sid-" + UUID.randomUUID().toString());
//            newSequenceFlow.setSourceFlowElement(flowNode);
//            newSequenceFlow.setTargetFlowElement(myFlowNode);
//            newSequenceFlowList.add(newSequenceFlow);
//
//            flowNode.setOutgoingFlows(newSequenceFlowList);
//
//            taskService.addComment(task.getId(), task.getProcessInstanceId(), "主动撤回");
//            taskService.resolveTask(task.getId());
//            taskService.claim(task.getId(), userId.toString());
//            taskService.complete(task.getId());
//            flowNode.setOutgoingFlows(oriSequenceFlows);
//        }
//    }
//
//    public List<FlowElement> getNextTaskElements(BpmnModel bpmnModel, String activityId) {
//        List<FlowElement> nextTaskElements = new ArrayList<>();
//
//        // 获取指定活动的 FlowElement
//        FlowElement currentFlowElement = bpmnModel.getFlowElement(activityId);
//
//        if (currentFlowElement instanceof FlowNode) {
//            FlowNode flowNode = (FlowNode) currentFlowElement;
//
//            // 遍历输出流向，找到所有下一任务节点
//            for (org.flowable.bpmn.model.SequenceFlow sequenceFlow : flowNode.getOutgoingFlows()) {
//                FlowElement targetFlowElement = bpmnModel.getFlowElement(sequenceFlow.getTargetRef());
//                if (targetFlowElement instanceof FlowNode) {
//                    nextTaskElements.add(targetFlowElement);
//                }
//            }
//        }
//
//        return nextTaskElements;
//    }
//
//    private Task getTask(String id) {
//        return taskService.createTaskQuery().taskId(id).singleResult();
//    }
//
//    /**
//     * 校验任务是否存在， 并且是否是分配给自己的任务
//     *
//     * @param userId 用户 id
//     * @param taskId task id
//     */
//    private Task checkTask(Long userId, String taskId) {
//        Task task = getTask(taskId);
//        if (task == null) {
//            throw exception(TASK_COMPLETE_FAIL_NOT_EXISTS);
//        }
//        if (!Objects.equals(userId, NumberUtils.parseLong(task.getAssignee()))) {
//            throw exception(TASK_COMPLETE_FAIL_ASSIGN_NOT_SELF);
//        }
//        return task;
//    }
//
//    /**
//     * 根据流程实例，查找最新的流程任务的所有数据
//     */
//    @Override
//    public List<BpmTaskAllInfoRespVO> getAllTaskInfoRespByProcessInstanceIds(Long userId, List<String> processInstanceIds) {
//        List<BpmTaskAllInfoRespVO> respVOList = new ArrayList<BpmTaskAllInfoRespVO>();
//        List<BpmTaskExtDO> taskExtDOS = taskExtMapper.selectList(new LambdaQueryWrapperX<BpmTaskExtDO>()
//                .inIfPresent(BpmTaskExtDO::getProcessInstanceId, processInstanceIds)
//                .orderByAsc(BpmTaskExtDO::getCreateTime)
//        );
//        if (CollectionUtil.isNotEmpty(taskExtDOS)) {
//            respVOList = BpmTaskConvert.INSTANCE.convert2respVo(taskExtDOS);
//
//        }
//
//        return respVOList;
//    }

//    @Override
//    public List<BpmTaskAssignRespDTO> getAllTaskNameByProcessInstanceId(String processInstanceId) {
//        String processDefinitionId = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getProcessDefinitionId();
//        List<BpmTaskAssignRuleDO> rules = taskRuleMapper.selectList(new LambdaQueryWrapperX<BpmTaskAssignRuleDO>().eq(BpmTaskAssignRuleDO::getProcessDefinitionId, processDefinitionId));
//
//        List<BpmTaskAssignRespDTO> result = new ArrayList<>();
//        rules.forEach(item -> {
//            List<Long> userIds = new ArrayList<>(item.getOptions());
//            result.add(new BpmTaskAssignRespDTO().setUserId(userIds.get(0)).setDefinitionKey(item.getTaskDefinitionKey()));
//        });
//        return result;
//    }

//    @Override
//    public List<BpmTaskRespDTO> getTaskInfoListByProcessInstanceId(String processInstanceId) {
//        return bpmTaskService.getTaskInfoListByProcessInstanceId(processInstanceId);
//    }
//
//    @Override
//    public BpmTaskCreateDTO getCreateInfoByProcessInstanceId(String processInstanceId) {
//        HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
//        if (processInstance == null) {
//            return null;
//        }
//        return new BpmTaskCreateDTO().setUserId(Long.valueOf(processInstance.getStartUserId())).setCreateTime(LocalDateTime.ofInstant(processInstance.getStartTime().toInstant(), ZoneId.systemDefault()));
//    }
//
//    @Override
//    public List<ContractProcessInstanceRelationInfoRespDTO> getToDoTaskInfoByDefinitionKey(String definitionKey) {
//        Long userId = getLoginUserId();
//        List<Task> taskList = taskService.createTaskQuery().processDefinitionKey(definitionKey)
//                .taskTenantId(TenantContextHolder.getTenantId().toString())
//                .taskAssignee(String.valueOf(userId))
//                .orderByTaskCreateTime().desc().list();
//        //将退回申请人节点的任务过滤掉
//        taskList = taskList.stream().filter(item -> !item.getName().equals(UPDATE.getName())).collect(Collectors.toList());
//        List<ContractProcessInstanceRelationInfoRespDTO> respDTOList = new ArrayList<ContractProcessInstanceRelationInfoRespDTO>();
//
//        for (Task task : taskList) {
//            ContractProcessInstanceRelationInfoRespDTO dto = new ContractProcessInstanceRelationInfoRespDTO();
//            dto.setTaskId(task.getId());
//            dto.setProcessInstanceId(task.getProcessInstanceId());
    ////            List<BpmTaskExtDO> bpmTaskExtDOS = taskExtMapper.selectList(new LambdaQueryWrapperX<BpmTaskExtDO>().eq(BpmTaskExtDO::getProcessInstanceId, task.getProcessInstanceId()).orderByDesc(BpmTaskExtDO::getEndTime));
    ////            if (CollectionUtil.isNotEmpty(bpmTaskExtDOS)) {
    ////                dto.setLastResult(bpmTaskExtDOS.get(0).getResult());
    ////            }
    ////            dto.setAssignee(task.getAssignee());
//            respDTOList.add(dto);
//        }
//
//
//        return respDTOList;
//    }

    @Override
    public List<ContractProcessInstanceRelationInfoRespDTO> getDoneTaskInfoByDefinitionKey(String definitionKey, String... definitionKeys) {
        List<String> combinedKeys = new ArrayList<>();
        combinedKeys.add(definitionKey);
        if (ObjectUtil.isNotEmpty(definitionKeys)) {
            Collections.addAll(combinedKeys, definitionKeys);
        }
        Long userId = getLoginUserId();
        // 查询已办任务
        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                .taskTenantId(TenantContextHolder.getTenantId().toString())
                .processDefinitionKeyIn(combinedKeys)
                // 已完成
                .finished()
                // 分配给自己
                .taskAssignee(String.valueOf(userId))
                // 审批时间倒序
                .orderByHistoricTaskInstanceEndTime().desc().list();
        List<String> processInstanceIds = taskList.stream().map(HistoricTaskInstance::getProcessInstanceId).collect(Collectors.toList());
        List<BpmTaskAllInfoRespVO> bpmTaskAllInfoRespVOList = bpmTaskService.getAllTaskInfoRespByProcessInstanceIds(getLoginUserId(), processInstanceIds);
        Map<String, BpmTaskAllInfoRespVO> taskAllInfoRespVOMap = new HashMap<String, BpmTaskAllInfoRespVO>();
        if (CollectionUtil.isNotEmpty(bpmTaskAllInfoRespVOList)) {
            taskAllInfoRespVOMap = CollectionUtils.convertMap(bpmTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getTaskId);
        }
        List<ContractProcessInstanceRelationInfoRespDTO> respDTOList = new ArrayList<ContractProcessInstanceRelationInfoRespDTO>();
        for (HistoricTaskInstance task : taskList) {
            ContractProcessInstanceRelationInfoRespDTO dto = new ContractProcessInstanceRelationInfoRespDTO();
            dto.setTaskId(task.getId());
            dto.setProcessInstanceId(task.getProcessInstanceId());
            dto.setLastestApproveTime(task.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            BpmTaskAllInfoRespVO bpmTaskAllInfoRespVO = taskAllInfoRespVOMap.get(task.getId());
            //给每个任务赋值操作结果
            if (ObjectUtil.isNotNull(bpmTaskAllInfoRespVO)) {
                dto.setProcessResult(bpmTaskAllInfoRespVO.getResult());
            }
//            BpmTaskExtDO bpmTaskExtDO = taskExtMapper.selectByTaskId(task.getId());
//            dto.setAssignee(bpmTaskExtDO.getAssigneeUserId().toString());
//            dto.setProcessResult(bpmTaskExtDO.getResult());

            respDTOList.add(dto);
        }
        //将已取消的任务去除
        return respDTOList.stream()
                .filter(dto -> !BpmProcessInstanceResultEnum.CANCEL.getResult().equals(dto.getProcessResult()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ContractProcessInstanceRelationInfoRespDTO> getDoneTaskInfoByDefinitionKey4Saas(String definitionKey, String... definitionKeys) {
        List<String> combinedKeys = new ArrayList<>();
        combinedKeys.add(definitionKey);
        if (ObjectUtil.isNotEmpty(definitionKeys)) {
            Collections.addAll(combinedKeys, definitionKeys);
        }
        Long userId = getLoginUserId();
        Long virtualId = relativeApi.getVirtualId4User(userId);
        // 查询已办任务
        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
                .taskTenantId(TenantContextHolder.getTenantId().toString())
                .processDefinitionKeyIn(combinedKeys)
                // 已完成
                .finished()
                // 分配给自己
                .taskAssignee(String.valueOf(virtualId))
                // 审批时间倒序
                .orderByHistoricTaskInstanceEndTime().desc().list();
        List<String> processInstanceIds = taskList.stream().map(HistoricTaskInstance::getProcessInstanceId).collect(Collectors.toList());
        List<BpmTaskAllInfoRespVO> bpmTaskAllInfoRespVOList = bpmTaskService.getAllTaskInfoRespByProcessInstanceIds(virtualId, processInstanceIds);
        Map<String, BpmTaskAllInfoRespVO> taskAllInfoRespVOMap = new HashMap<String, BpmTaskAllInfoRespVO>();
        if (CollectionUtil.isNotEmpty(bpmTaskAllInfoRespVOList)) {
            taskAllInfoRespVOMap = CollectionUtils.convertMap(bpmTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getTaskId);
        }
        List<ContractProcessInstanceRelationInfoRespDTO> respDTOList = new ArrayList<ContractProcessInstanceRelationInfoRespDTO>();
        for (HistoricTaskInstance task : taskList) {
            ContractProcessInstanceRelationInfoRespDTO dto = new ContractProcessInstanceRelationInfoRespDTO();
            dto.setTaskId(task.getId());
            dto.setProcessInstanceId(task.getProcessInstanceId());
            dto.setLastestApproveTime(task.getEndTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            BpmTaskAllInfoRespVO bpmTaskAllInfoRespVO = taskAllInfoRespVOMap.get(task.getId());
            //给每个任务赋值操作结果
            if (ObjectUtil.isNotNull(bpmTaskAllInfoRespVO)) {
                dto.setProcessResult(bpmTaskAllInfoRespVO.getResult());
            }
//            BpmTaskExtDO bpmTaskExtDO = taskExtMapper.selectByTaskId(task.getId());
//            dto.setAssignee(bpmTaskExtDO.getAssigneeUserId().toString());
//            dto.setProcessResult(bpmTaskExtDO.getResult());

            respDTOList.add(dto);
        }
        //将已取消的任务去除
        return respDTOList.stream()
                .filter(dto -> !BpmProcessInstanceResultEnum.CANCEL.getResult().equals(dto.getProcessResult()))
                .collect(Collectors.toList());
    }

//    @Override
//    public Boolean existApprovedHisTask(String definitionKey, String processInstanceId) {
//
//        Long userId = getLoginUserId();
//        // 查询已办任务
//        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
//                .taskTenantId(TenantContextHolder.getTenantId().toString())
//                //符合流程实例id
//                .processInstanceId(processInstanceId)
//                //符合流程定义key
//                .processDefinitionKey(definitionKey)
//                // 已完成
//                .finished()
//                // 审批时间倒序
//                .orderByHistoricTaskInstanceEndTime().desc().list();
//        //存在已完成的任务则已经被审批过
//        if (CollectionUtil.isNotEmpty(taskList)) {
//            return true;
//        }
//        //没有已完成的任务则没有审批过
//        return false;
//    }
//
//    /**
//     * 新增批注用
//     */
//    @Override
//    public List<ContractProcessInstanceRelationInfoRespDTO> getAllLatestDoneTasks(String definitionKey, String processInstanceId) {
//        List<ContractProcessInstanceRelationInfoRespDTO> result = new ArrayList<ContractProcessInstanceRelationInfoRespDTO>();
//        List<ContractProcessInstanceRelationInfoRespDTO> finalResult = new ArrayList<ContractProcessInstanceRelationInfoRespDTO>();
//
//        Long userId = getLoginUserId();
//        // 查询已办任务
//        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
//                .taskTenantId(TenantContextHolder.getTenantId().toString())
//                //符合流程实例id
//                .processInstanceId(processInstanceId)
//                //符合流程定义key
//                .processDefinitionKey(definitionKey)
//                // 已完成
//                .finished()
//                // 审批时间倒序
//                .orderByHistoricTaskInstanceEndTime().desc().list();
//
//        List<String> processInstanceIds = taskList.stream().map(HistoricTaskInstance::getProcessInstanceId).collect(Collectors.toList());
//        List<BpmTaskAllInfoRespVO> bpmTaskAllInfoRespVOList = bpmTaskService.getAllTaskInfoRespByProcessInstanceIds(getLoginUserId(), processInstanceIds);
//        Map<String, BpmTaskAllInfoRespVO> taskAllInfoRespVOMap = new HashMap<String, BpmTaskAllInfoRespVO>();
//        if (CollectionUtil.isNotEmpty(bpmTaskAllInfoRespVOList)) {
//            taskAllInfoRespVOMap = CollectionUtils.convertMap(bpmTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getTaskId);
//        }
//
//        if (CollectionUtil.isNotEmpty(taskList)) {
//            for (HistoricTaskInstance taskInstance : taskList) {
//                ContractProcessInstanceRelationInfoRespDTO respDTO = new ContractProcessInstanceRelationInfoRespDTO();
//                respDTO.setAssignee(taskInstance.getAssignee());
//                BpmTaskAllInfoRespVO bpmTaskAllInfoRespVO = taskAllInfoRespVOMap.get(taskInstance.getId());
//                if (ObjectUtil.isNotNull(bpmTaskAllInfoRespVO)) {
//                    respDTO.setProcessResult(bpmTaskAllInfoRespVO.getResult());
//                    respDTO.setTaskId(bpmTaskAllInfoRespVO.getTaskId());
//                    respDTO.setAssignee(String.valueOf(bpmTaskAllInfoRespVO.getAssigneeUserId()));
//                    respDTO.setLastestApproveTime(bpmTaskAllInfoRespVO.getCreateTime());
//                    result.add(respDTO);
//                }
//            }
//            //找出退回任务中，处理时间最晚的（也就是上一个任务）
//            Optional<ContractProcessInstanceRelationInfoRespDTO> latestElement = result.stream()
//                    .filter(dto -> Objects.equals(dto.getProcessResult(), BpmProcessInstanceResultEnum.BACK.getResult()) || Objects.equals(dto.getProcessResult(), BpmProcessInstanceResultEnum.CANCEL.getResult()))
//                    .max((dto1, dto2) -> dto1.getLastestApproveTime().compareTo(dto2.getLastestApproveTime()));
//            //只留下最近一次时间的所有任务
//            if (!Optional.empty().equals(latestElement)) {
//                LocalDateTime latestTime = latestElement.get().getLastestApproveTime();
//                for (ContractProcessInstanceRelationInfoRespDTO infoRespDTO : result) {
//                    if (latestTime.equals(infoRespDTO.getLastestApproveTime())) {
//                        finalResult.add(infoRespDTO);
//                    }
//                }
//            }
//        }
//
//        return finalResult;
//    }
//
//    @Override
//    public List<ContractProcessInstanceRelationInfoRespDTO> getDoneTaskInfoByDefinitionKeyAndResult(String definitionKey, Integer taskResult) {
//        Long userId = getLoginUserId();
//        // 查询已办任务
//        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
//                .taskTenantId(TenantContextHolder.getTenantId().toString())
//                .processDefinitionKey(definitionKey)
//                // 已完成
//                .finished()
//                // 分配给自己
//                .taskAssignee(String.valueOf(userId))
//                // 审批时间倒序
//                .orderByHistoricTaskInstanceEndTime().desc().list();
//        List<String> taskIds = taskList.stream().map(HistoricTaskInstance::getId).collect(Collectors.toList());
//        //获得任务的处理结果
//        List<BpmTaskAllInfoRespVO> bpmTaskAllInfoRespVOList = bpmTaskService.getAllTaskInfoRespByTaskIds(getLoginUserId(), taskIds);
//        Map<String, BpmTaskAllInfoRespVO> taskAllInfoRespVOMap = new HashMap<String, BpmTaskAllInfoRespVO>();
//        if (CollectionUtil.isNotEmpty(bpmTaskAllInfoRespVOList)) {
//            //过滤掉已取消的任务
//            bpmTaskAllInfoRespVOList = bpmTaskAllInfoRespVOList.stream().filter(item -> !item.getResult().equals(BpmProcessInstanceResultEnum.CANCEL.getResult())).collect(Collectors.toList());
//            //只保留最新的已办任务
//            bpmTaskAllInfoRespVOList = distinctDoneTaskLatestEndTime(bpmTaskAllInfoRespVOList);
//            taskAllInfoRespVOMap = CollectionUtils.convertMap(bpmTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getTaskId);
////            taskAllInfoRespVOMap = saveLatestTask(taskAllInfoRespVOMap);
//        }
//        List<ContractProcessInstanceRelationInfoRespDTO> respDTOList = new ArrayList<ContractProcessInstanceRelationInfoRespDTO>();
//
////        for (HistoricTaskInstance task : taskList) {
////            ContractProcessInstanceRelationInfoRespDTO dto = new ContractProcessInstanceRelationInfoRespDTO();
////            dto.setTaskId(task.getId());
////            dto.setProcessInstanceId(task.getProcessInstanceId());
////            //获得任务的处理结果，便于之后去除已取消状态的任务。
////            BpmTaskAllInfoRespVO bpmTaskAllInfoRespVO = taskAllInfoRespVOMap.get(task.getId());
////            if (ObjectUtil.isNotNull(bpmTaskAllInfoRespVO)) {
////                dto.setProcessResult(bpmTaskAllInfoRespVO.getResult());
////            }
////            respDTOList.add(dto);
////        }
//        List<ContractProcessInstanceRelationInfoRespDTO> result = new ArrayList<ContractProcessInstanceRelationInfoRespDTO>();
//        for (BpmTaskAllInfoRespVO taskAllInfoRespVO : bpmTaskAllInfoRespVOList) {
//            ContractProcessInstanceRelationInfoRespDTO rs = new ContractProcessInstanceRelationInfoRespDTO();
//            rs.setTaskId(taskAllInfoRespVO.getTaskId());
//            rs.setProcessInstanceId(taskAllInfoRespVO.getProcessInstanceId());
//            rs.setProcessResult(taskAllInfoRespVO.getResult());
//            rs.setLastestApproveTime(taskAllInfoRespVO.getEndTime());
//            result.add(rs);
//        }
//
//        //如果有筛选审批状态
//        if (StatusConstants.TEMP_INTEGER != taskResult) {
//            return result.stream()
//                    .filter(dto -> Objects.equals(dto.getProcessResult(), taskResult)) // 筛选出processResult等于result的元素
//                    .filter(dto -> dto.getProcessResult() != null) //结果为空的，则不符合唯一已办条件
//                    .collect(Collectors.toList());
//        }
////        return respDTOList.stream()
////                .filter(dto -> dto.getProcessResult() != null) //结果为空的，则不符合唯一已办条件
////                .filter(dto -> !dto.getProcessResult().equals(BpmProcessInstanceResultEnum.CANCEL.getResult()))
////                .collect(Collectors.toList());
//        //如果没有筛选审批状态
//        return result;
//    }
//
    /**
     * 去重，保留处理过的任务（只保留endTime最新的任务）
     */
    public List<BpmTaskAllInfoRespVO> distinctDoneTaskLatestEndTime4Saas(Long virtualId, List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList) {
        List<BpmTaskAllInfoRespVO> filteredList = new ArrayList<>(taskAllInfoRespVOList.stream()
                .filter(task -> Objects.equals(task.getAssigneeUserId(), virtualId)) // 筛选出 assigneeUserId == virtualId 的任务
                .filter(task -> task.getEndTime() != null) // 筛选出 endTime 不为空的任务
                .collect(Collectors.toMap(
                        BpmTaskAllInfoRespVO::getProcessInstanceId, // 以 processInstanceId 作为 key 进行分组
                        Function.identity(), // 当有重复 key 时，保留当前元素
                        (existing, replacement) -> {
                            if (existing.getEndTime().isAfter(replacement.getEndTime())) {
                                return existing; // 如果已有元素的 endTime 更晚，则保留已有元素
                            } else {
                                return replacement; // 否则保留当前元素
                            }
                        }
                ))
                .values());
        return filteredList;
    }
    /**
     * 去重，保留处理过的任务（只保留endTime最新的任务）
     */
    public List<BpmTaskAllInfoRespVO> distinctDoneTaskLatestEndTime(List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList) {
        Long userId = SecurityFrameworkUtils.getLoginUserId();
        List<BpmTaskAllInfoRespVO> filteredList = new ArrayList<>(taskAllInfoRespVOList.stream()
                .filter(task -> Objects.equals(task.getAssigneeUserId(), userId)) // 筛选出 assigneeUserId == userId 的任务
                .filter(task -> task.getEndTime() != null) // 筛选出 endTime 不为空的任务
                .collect(Collectors.toMap(
                        BpmTaskAllInfoRespVO::getProcessInstanceId, // 以 processInstanceId 作为 key 进行分组
                        Function.identity(), // 当有重复 key 时，保留当前元素
                        (existing, replacement) -> {
                            if (existing.getEndTime().isAfter(replacement.getEndTime())) {
                                return existing; // 如果已有元素的 endTime 更晚，则保留已有元素
                            } else {
                                return replacement; // 否则保留当前元素
                            }
                        }
                ))
                .values());
        return filteredList;
    }
//
//    private Map<String, BpmTaskAllInfoRespVO> saveLatestTask(Map<String, BpmTaskAllInfoRespVO> taskAllInfoRespVOMap) {
//        // 处理后的新Map
//        Map<String, BpmTaskAllInfoRespVO> updatedMap = new HashMap<>();
//
//        for (BpmTaskAllInfoRespVO task : taskAllInfoRespVOMap.values()) {
//            String processInstanceId = task.getProcessInstanceId();
//            LocalDateTime time = task.getEndTime();
//            if (!updatedMap.containsKey(processInstanceId) || time.isAfter(updatedMap.get(processInstanceId).getEndTime())) {
//                updatedMap.put(processInstanceId, task);
//            }
//        }
//        return updatedMap;
//    }
//
//    @Override
//    public List<ContractProcessInstanceRelationInfoRespDTO> getAllTaskInfoByDefinitionKey(String definitionKey) {
//
//        // 查询已办任务
//        List<ContractProcessInstanceRelationInfoRespDTO> respDoneDTOList = getDoneTaskInfoByDefinitionKey(definitionKey);
//        //待办任务
//        List<ContractProcessInstanceRelationInfoRespDTO> respToDoDTOList = getToDoTaskInfoByDefinitionKey(definitionKey);
//
//        Map<String, ContractProcessInstanceRelationInfoRespDTO> todoMap = CollectionUtils.convertMap(respToDoDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
//        Map<String, ContractProcessInstanceRelationInfoRespDTO> doneMap = CollectionUtils.convertMap(respDoneDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
//        doneMap.putAll(todoMap);
//        return new ArrayList<ContractProcessInstanceRelationInfoRespDTO>(doneMap.values());
//    }

//    @Override
//    public List<SimpleTaskDTO> getAllTaskInfoByProcessIds(List<String> processIds) {
//        List<SimpleTaskDTO> respDTOList = new ArrayList<SimpleTaskDTO>();
//        List<BpmTaskExtDO> taskExtDOList = taskExtMapper.selectList(new LambdaQueryWrapperX<BpmTaskExtDO>().inIfPresent(BpmTaskExtDO::getProcessInstanceId, processIds));
//        if (CollectionUtil.isNotEmpty(taskExtDOList)) {
//            respDTOList = BpmTaskConvert.INSTANCE.convert2DTOList2(taskExtDOList);
//        }
//        return respDTOList;
//    }

//    /**
//     * 根据流程实例ids
//     *
//     * @param processIds
//     * @return 返回所有EndTime为null的任务信息
//     */
//    @Override
//    public List<SimpleTaskDTO> getNextUntreatedTaskInfoByProcessIds(List<String> processIds) {
//        List<SimpleTaskDTO> respDTOList = new ArrayList<SimpleTaskDTO>();
//        List<BpmTaskExtDO> taskExtDOList = taskExtMapper.selectList(new LambdaQueryWrapperX<BpmTaskExtDO>()
//                .inIfPresent(BpmTaskExtDO::getProcessInstanceId, processIds)
//                .isNull(BpmTaskExtDO::getEndTime)
//        );
//        if (CollectionUtil.isNotEmpty(taskExtDOList)) {
//            respDTOList = BpmTaskConvert.INSTANCE.convert2DTOList2(taskExtDOList);
//        }
//        return respDTOList;
//    }
//
//    /**
//     * 是否是发起人再次提交
//     */
//    @Override
//    public boolean ifUpdaterSubmit(String definitionKey, String processInstanceId) {
//
//        Long userId = getLoginUserId();
//        List<BpmTaskExtDO> taskExtDOList = taskExtMapper.selectList(new LambdaQueryWrapperX<BpmTaskExtDO>().eq(BpmTaskExtDO::getProcessInstanceId, processInstanceId));
//        Map<String, BpmTaskExtDO> taskExtDOMap = CollectionUtils.convertMap(taskExtDOList, BpmTaskExtDO::getTaskId);
//
//        // 查询已办任务
//        List<HistoricTaskInstance> taskList = historyService.createHistoricTaskInstanceQuery()
//                .taskTenantId(TenantContextHolder.getTenantId().toString())
//                //符合流程实例id
//                .processInstanceId(processInstanceId)
//                //符合流程定义key
//                .processDefinitionKey(definitionKey)
//                // 已完成
//                .finished()
//                // 审批时间倒序
//                .orderByHistoricTaskInstanceEndTime().desc().list();
//        //存在已完成的任务则已经被审批过
//        if (CollectionUtil.isNotEmpty(taskList)) {
//            HistoricTaskInstance lastTaskInstance = taskList.get(0);
//            if (ObjectUtil.isNotNull(lastTaskInstance)) {
//                //校验最近的历史任务是再次提交
//                if (UPDATE.getKey().equals(lastTaskInstance.getTaskDefinitionKey())) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public List<SimpleTaskDTO> getBpmTaskByTaskIds(List<String> taskIds) {
//        List<SimpleTaskDTO> result = new ArrayList<SimpleTaskDTO>();
//        List<BpmTaskAllInfoRespVO> list = bpmTaskService.getAllTaskInfoRespByTaskIds(getLoginUserId(), taskIds);
//        if (CollectionUtil.isNotEmpty(list)) {
//            result = BpmTaskConvert.INSTANCE.convertListResp2DTO(list);
//        }
//        return result;
//    }
//
//    /**
//     * 获得上一个节点的审批人信息(比如：高级审批人获得初级审批人的信息)
//     */
//    @Override
//    public Set<Long> getLastApproverInfos(String processDefinitionId, String processInstanceId, String taskName, Long submitterId) {
//        FlowableModelEnums modelEnums = FlowableModelEnums.getInstanceByName(taskName);
//        if (ObjectUtil.isNotNull(modelEnums)) {
//            //不可是第一节点
//            if (FlowableModelEnums.FIRST_LEVEL != modelEnums) {
//                Integer lastIndex = modelEnums.getIndex() - 1;
//                FlowableModelEnums lastApprover = FlowableModelEnums.getInstanceByIndex(lastIndex);
//                if (ObjectUtil.isNotNull(lastApprover)) {
//                    BpmTaskAssignRuleDO assignRuleDO = taskRuleMapper.selectOne(new LambdaQueryWrapperX<BpmTaskAssignRuleDO>()
//                            .eqIfPresent(BpmTaskAssignRuleDO::getProcessDefinitionId, processDefinitionId)
//                            .eqIfPresent(BpmTaskAssignRuleDO::getTaskDefinitionKey, lastApprover.getKey())
//                    );
//                    if (ObjectUtil.isNotNull(assignRuleDO)) {
//                        Integer ruleType = assignRuleDO.getType();
//                        BpmTaskAssignRuleTypeEnum ruleTypeEnum = BpmTaskAssignRuleTypeEnum.getInstance(ruleType);
//                        if (ObjectUtil.isNotNull(ruleTypeEnum)) {
//                            switch (ruleTypeEnum) {
//                                //角色分配
//                                case ROLE:
//                                    Set<Long> options = assignRuleDO.getOptions();
//                                    if (CollectionUtil.isNotEmpty(options)) {
//                                        permissionApi.getApproverIdsBySubmitterAndRoleIds(Long.valueOf(submitterId), options);
//                                    }
//
//                                default:
//                                    return Collections.emptySet();
//                            }
//
//                        }
//
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 根据 processInstanceId 找到 processDefinitionId
//     */
//    @Override
//    public String getProcessDefinitionIdByProcessInstanceId(String processInstanceId) {
//        String result = "";
//        List<BpmTaskExtDO> taskExtDOS = taskExtMapper.selectList(new LambdaQueryWrapperX<BpmTaskExtDO>()
//                .eqIfPresent(BpmTaskExtDO::getProcessInstanceId, processInstanceId)
//                .select(BpmTaskExtDO::getProcessDefinitionId)
//        );
//        if (CollectionUtil.isNotEmpty(taskExtDOS)) {
//            result = taskExtDOS.get(0).getProcessDefinitionId();
//        }
//        return result;
//    }
//
//    @Override
//    public BpmTaskRespDTO getTaskRespDTO(String taskId) {
//        BpmTaskRespDTO result = new BpmTaskRespDTO();
//        BpmTaskExtDO taskDO = taskExtMapper.selectOne(new LambdaQueryWrapperX<BpmTaskExtDO>().eq(BpmTaskExtDO::getTaskId, taskId));
//        if (ObjectUtil.isNotNull(taskDO)) {
//            result = BpmTaskConvert.INSTANCE.bpmTaskEntity2RespDTO(taskDO);
//        }
//        return result;
//    }
//
//
//    @Override
//    public List<BpmTaskAllInfoRespVO> getAllTaskIdAndAssigneeNameByProcessInstanceIds(Long loginUserId, List<String> instanceList) {
//        List<BpmTaskAllInfoRespVO> respVOList = new ArrayList<BpmTaskAllInfoRespVO>();
//        List<BpmTaskExtDO> taskExtDOS = taskExtMapper.selectList(new LambdaQueryWrapperX<BpmTaskExtDO>()
//                .select(BpmTaskExtDO::getId, BpmTaskExtDO::getTaskId, BpmTaskExtDO::getAssigneeUserId, BpmTaskExtDO::getEndTime, BpmTaskExtDO::getCreateTime, BpmTaskExtDO::getProcessInstanceId)
//                .in(BpmTaskExtDO::getProcessInstanceId, instanceList)
//        );
//        if (CollectionUtil.isNotEmpty(taskExtDOS)) {
//            respVOList = BpmTaskConvert.INSTANCE.convert2respVo(taskExtDOS);
//
//        }
//
//        return respVOList;
//    }

    private void selectNextFlowElements(BpmnModel bpmnModel, String myActivityId, List flowElements) {
        List<FlowElement> nextTaskElements = getNextTaskElements(bpmnModel, myActivityId);
        flowElements.addAll(nextTaskElements);
        for (FlowElement flowElement : nextTaskElements) {
            // 判断是否为网关
            if (flowElement instanceof ExclusiveGateway || flowElement instanceof ParallelGateway || flowElement instanceof InclusiveGateway) {
                selectNextFlowElements(bpmnModel, flowElement.getId(), flowElements);
            }
        }
    }

}


