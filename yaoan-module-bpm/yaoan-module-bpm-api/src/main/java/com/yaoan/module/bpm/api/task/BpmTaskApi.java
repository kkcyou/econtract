package com.yaoan.module.bpm.api.task;

import com.yaoan.module.bpm.api.task.dto.*;
import com.yaoan.module.bpm.api.task.dto.v2.BpmTaskApproveReqDTO;

import javax.validation.Valid;
import java.util.*;

/**
 * 流程实例 Api 接口
 *
 * @author doujl
 */
public interface BpmTaskApi {
    /**
     * 通过任务
     *
     * @param userId 用户编号
     * @param dto    通过请求
     */
    void approveTask(Long userId, @Valid BpmTaskApproveReqDTO dto);

    /**
     * 查询待办任务IDs
     *
     * @param userId             用户编号
     * @param processInstanceIds 流程示例IDs
     * @return 流程实例ID 和 taskId Map集合
     */
    Map<String, String> getTodoTaskList(Long userId, Collection<String> processInstanceIds);

    /**
     * 查询待办任务
     *
     * @param processInstanceId 流程示例ID
     * @return task集合
     */
    List<BpmTaskAllInfoRespVO> getTodoTaskList(String processInstanceId);
    /**
     * 查询用户指定流程定义key的流程实例信息 包含待办和已办 代办包含任务ID，已办信息包含最新一次执行结果
     *
     * @param userId                用户编号
     * @param processDefinitionKeys 查询流程定义key
     * @return 流程实例IDs
     */
    List<ContractProcessInstanceRelationInfoRespDTO> getAllRelationProcessInstanceInfosByProcessDefinitionKeys(Long userId, Collection<String> processDefinitionKeys);

    /**
     * 根据流程定义key和taskDefinitionKeyLike查询用户关联的流程实例信息 包含待办和已办 代办包含任务ID，已办信息包含最新一次执行结果`
     *
     * @param userId                用户编号
     * @param processDefinitionKeys 查询流程定义key
     * @param taskNameLike          任务定义key
     * @return 流程实例IDs
     */
    List<ContractProcessInstanceRelationInfoRespDTO> getAllRelationProcessInstanceInfosByProcessDefinitionKeys(Long userId, Collection<String> processDefinitionKeys, String taskNameLike);
    List<ContractProcessInstanceRelationInfoRespDTO> getAllRelationProcessInstanceInfosByProcessDefinitionKeys(List<Long> userId, Collection<String> processDefinitionKeys, String taskNameLike);
    // 只查待处理的
    List<ContractProcessInstanceRelationInfoRespDTO> getAllTODORelationProcessInstanceInfosByProcessDefinitionKeys(Long userId, Collection<String> processDefinitionKeys, String taskNameLike);
    List<ContractProcessInstanceRelationInfoRespDTO> getAllTODORelationProcessInstanceInfosByProcessDefinitionKeys(List<Long> userId, Collection<String> processDefinitionKeys, String taskNameLike);

    /**
     * 处理任务API
     */
    void handleTask(BpmHandleTaskReqDTO bpmHandleTaskReqDTO);
    /**
     * 处理任务API
     */
    void handleTask2(BpmHandleTaskReqDTO bpmHandleTaskReqDTO);
    /**
     * 撤回任务
     *
     * @param userId           执行人
     * @param taskRevokeReqDTO 撤回任务实例
     */
    void revokeTask(Long userId, TaskRevokeReqDTO taskRevokeReqDTO);

    /**
     * 发起人批量查看
     */
    List<BpmTaskAllInfoRespVO> getAllTaskInfoRespByProcessInstanceIds(Long userId, List<String> processInstanceIds);

    /**
     * 根据流程实例ID获取任务节点信息
     *
     * @param processInstanceId 流程实例ID
     * @return 任务节点list
     */
    List<BpmTaskAssignRespDTO> getAllTaskNameByProcessInstanceId(String processInstanceId);


    List<BpmTaskRespDTO> getTaskInfoListByProcessInstanceId(String processInstanceId);

    /**
     * 获取发起人,发起时间
     *
     * @param processInstanceId 流程实例ID
     * @return 返回创建信息
     */
    BpmTaskCreateDTO getCreateInfoByProcessInstanceId(String processInstanceId);

    /**
     * 流程定义Key
     *
     * @param definitionKey
     * @return 返回待办任务信息
     */
    List<ContractProcessInstanceRelationInfoRespDTO> getToDoTaskInfoByDefinitionKey(String definitionKey,String... otherDefinitionKeys);
    public List<ContractProcessInstanceRelationInfoRespDTO> getToDoTaskInfoByDefinitionKey4Saas(String definitionKey, String... definitionKeys);
    /**
     *
     * @param definitionKey
     * @param taskNameLike
     * @return
     */
    List<ContractProcessInstanceRelationInfoRespDTO> getToDoTaskInfoByDefinitionKeyAndStage(String definitionKey, TaskForWarningReqDTO params, String... taskNameLike);

    List<ContractProcessInstanceRelationInfoRespDTO> getALLToDoTaskInfoByDefinitionKey(String definitionKey,String... otherDefinitionKeys);

    /**
     * 流程定义Key
     *
     * @param definitionKey
     * @param processInstanceId
     * @return 该流程是否被审批过
     */
    public Boolean existApprovedHisTask(String definitionKey, String processInstanceId);

    /**
     * 流程定义Key
     *
     * @param definitionKey
     * @return 返回已办任务信息
     */
    List<ContractProcessInstanceRelationInfoRespDTO> getDoneTaskInfoByDefinitionKey(String definitionKey,String... otherDefinitionKeys);
    public List<ContractProcessInstanceRelationInfoRespDTO> getDoneTaskInfoByDefinitionKey4Saas(String definitionKey, String... definitionKeys);

    public List<ContractProcessInstanceRelationInfoRespDTO> getDoneTaskInfoByDefinitionKeyAndResult(String definitionKey, Integer result,String... otherDefinitionKeys);
    public List<ContractProcessInstanceRelationInfoRespDTO> getDoneTaskInfoByDefinitionKeyAndResult4Saas(String definitionKey, Integer taskResult, String... definitionKeys);
    /**
     * 流程定义Key
     *
     * @param definitionKey
     * @return 返回所有任务信息
     */
    List<ContractProcessInstanceRelationInfoRespDTO> getAllTaskInfoByDefinitionKey(String definitionKey,String... otherDefinitionKeys);
    List<ContractProcessInstanceRelationInfoRespDTO> getAllTaskInfoByDefinitionKey4Saas(String definitionKey,String... otherDefinitionKeys);

    /**
     * 根据流程实例ids
     *
     * @param processIds
     * @return 返回所有任务信息
     */
    List<SimpleTaskDTO> getAllTaskInfoByProcessIds(List<String> processIds);

    /**
     * 根据流程实例ids
     *
     * @param processIds
     * @return 返回所有任务信息
     */
    List<SimpleTaskDTO> getNextUntreatedTaskInfoByProcessIds(List<String> processIds);

    boolean ifUpdaterSubmit(String definitionKey, String processInstanceId);

    List<SimpleTaskDTO> getBpmTaskByTaskIds(List<String> taskIds);

    /**
     * 获得上一个节点的审批人信息(比如：高级审批人获得初级审批人的信息)
     */
    Set<Long> getLastApproverInfos(String processDefinitionId, String processInstanceId, String taskName, Long submitterId);


    /**
     * 根据 processInstanceId 找到 processDefinitionId
     */
    String getProcessDefinitionIdByProcessInstanceId(String processInstanceId);

    BpmTaskRespDTO getTaskRespDTO(String taskId);
    BpmTaskRespDTO getRunTaskRespDTO(String taskId);

    List<BpmTaskAllInfoRespVO> getAllTaskIdAndAssigneeNameByProcessInstanceIds(Long loginUserId, List<String> instanceList);

    List<BpmTaskAllInfoRespVO> getAllTaskIdByProcessInstanceIds(List<String> instanceList);
    /**
     * 不通过任务
     *
     * @param userId 用户编号
     * @param reqVO  不通过请求
     */
    void rejectTask(Long userId, BpmTaskRejectReqDTO reqVO);

    void setTaskVariable(Map<String, Object> reqVO, String... taskId);
}