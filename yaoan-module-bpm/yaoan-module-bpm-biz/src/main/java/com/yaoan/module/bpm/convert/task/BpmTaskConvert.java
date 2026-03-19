package com.yaoan.module.bpm.convert.task;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.date.DateUtils;
import com.yaoan.framework.common.util.number.NumberUtils;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.bpm.api.task.dto.BpmHandleTaskReqDTO;
import com.yaoan.module.bpm.api.task.dto.BpmTaskAllInfoRespVO;
import com.yaoan.module.bpm.api.task.dto.BpmTaskRespDTO;
import com.yaoan.module.bpm.api.task.dto.ContractProcessInstanceRelationInfoRespDTO;
import com.yaoan.module.bpm.api.task.dto.v2.BpmTaskApproveReqDTO;
import com.yaoan.module.bpm.controller.admin.task.vo.instance.BpmProcessInstanceRespVO;
import com.yaoan.module.bpm.controller.admin.task.vo.task.BpmTaskApproveReqVO;
import com.yaoan.module.bpm.controller.admin.task.vo.task.BpmTaskRespVO;
import com.yaoan.module.bpm.dal.dataobject.definition.BpmFormDO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.bpm.enums.task.BpmTaskStatusEnum;
import com.yaoan.module.bpm.framework.flowable.core.util.BpmnModelUtils;
import com.yaoan.module.bpm.framework.flowable.core.util.FlowableUtils;
import com.yaoan.module.bpm.service.message.dto.BpmMessageSendWhenTaskCreatedReqDTO;
import com.yaoan.module.system.api.dept.dto.DeptRespDTO;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.service.impl.persistence.entity.TaskEntityImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.yaoan.framework.common.util.collection.CollectionUtils.*;
import static com.yaoan.framework.common.util.collection.MapUtils.findAndThen;
import static com.yaoan.module.bpm.framework.flowable.core.enums.BpmnVariableConstants.TASK_VARIABLE_REASON;
import static com.yaoan.module.bpm.framework.flowable.core.enums.BpmnVariableConstants.TASK_VARIABLE_STATUS;

/**
 * Bpm 任务 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface BpmTaskConvert {

    BpmTaskConvert INSTANCE = Mappers.getMapper(BpmTaskConvert.class);

    default PageResult<BpmTaskRespVO> buildTodoTaskPage(PageResult<Task> pageResult,
                                                        Map<String, ProcessInstance> processInstanceMap,
                                                        Map<Long, AdminUserRespDTO> userMap) {
        return BeanUtils.toBean(pageResult, BpmTaskRespVO.class, taskVO -> {
            ProcessInstance processInstance = processInstanceMap.get(taskVO.getProcessInstanceId());
            if (processInstance == null) {
                return;
            }
            taskVO.setProcessInstance(BeanUtils.toBean(processInstance, BpmTaskRespVO.ProcessInstance.class));
            AdminUserRespDTO startUser = userMap.get(NumberUtils.parseLong(processInstance.getStartUserId()));
            taskVO.getProcessInstance().setStartUser(BeanUtils.toBean(startUser, BpmProcessInstanceRespVO.User.class));
        });
    }

    default PageResult<BpmTaskRespVO> buildTaskPage(PageResult<HistoricTaskInstance> pageResult,
                                                    Map<String, HistoricProcessInstance> processInstanceMap,
                                                    Map<Long, AdminUserRespDTO> userMap,
                                                    Map<Long, DeptRespDTO> deptMap) {
        List<BpmTaskRespVO> taskVOList = CollectionUtils.convertList(pageResult.getList(), task -> {
            BpmTaskRespVO taskVO = BeanUtils.toBean(task, BpmTaskRespVO.class);
            taskVO.setStatus(FlowableUtils.getTaskStatus(task)).setReason(FlowableUtils.getTaskReason(task));
            // 用户信息
            AdminUserRespDTO assignUser = userMap.get(NumberUtils.parseLong(task.getAssignee()));
            if (assignUser != null) {
                taskVO.setAssigneeUser(BeanUtils.toBean(assignUser, BpmProcessInstanceRespVO.User.class));
                findAndThen(deptMap, assignUser.getDeptId(), dept -> taskVO.getAssigneeUser().setDeptName(dept.getName()));
            }
            // 流程实例
            HistoricProcessInstance processInstance = processInstanceMap.get(taskVO.getProcessInstanceId());
            if (processInstance != null) {
                AdminUserRespDTO startUser = userMap.get(NumberUtils.parseLong(processInstance.getStartUserId()));
                taskVO.setProcessInstance(BeanUtils.toBean(processInstance, BpmTaskRespVO.ProcessInstance.class));
                taskVO.getProcessInstance().setStartUser(BeanUtils.toBean(startUser, BpmProcessInstanceRespVO.User.class));
            }
            return taskVO;
        });
        return new PageResult<>(taskVOList, pageResult.getTotal());
    }

    default List<BpmTaskRespVO> buildTaskListByProcessInstanceId(List<HistoricTaskInstance> taskList,
                                                                 HistoricProcessInstance processInstance,
                                                                 Map<Long, BpmFormDO> formMap,
                                                                 Map<Long, AdminUserRespDTO> userMap,
                                                                 Map<Long, DeptRespDTO> deptMap,
                                                                 BpmnModel bpmnModel) {
        List<BpmTaskRespVO> taskVOList = CollectionUtils.convertList(taskList, task -> {
            BpmTaskRespVO taskVO = BeanUtils.toBean(task, BpmTaskRespVO.class);
            Integer taskStatus = FlowableUtils.getTaskStatus(task);
            taskVO.setStatus(taskStatus).setReason(FlowableUtils.getTaskReason(task));
            // 流程实例
            AdminUserRespDTO startUser = userMap.get(NumberUtils.parseLong(processInstance.getStartUserId()));
            taskVO.setProcessInstance(BeanUtils.toBean(processInstance, BpmTaskRespVO.ProcessInstance.class));
            taskVO.getProcessInstance().setStartUser(BeanUtils.toBean(startUser, BpmProcessInstanceRespVO.User.class));
            // 表单信息
            BpmFormDO form = MapUtil.get(formMap, NumberUtils.parseLong(task.getFormKey()), BpmFormDO.class);
            if (form != null) {
                taskVO.setFormId(form.getId()).setFormName(form.getName()).setFormConf(form.getConf())
                        .setFormFields(form.getFields()).setFormVariables(FlowableUtils.getTaskFormVariable(task));
            }
            // 用户信息
            AdminUserRespDTO assignUser = userMap.get(NumberUtils.parseLong(task.getAssignee()));
            if (assignUser != null) {
                taskVO.setAssigneeUser(BeanUtils.toBean(assignUser, BpmProcessInstanceRespVO.User.class));
                findAndThen(deptMap, assignUser.getDeptId(), dept -> taskVO.getAssigneeUser().setDeptName(dept.getName()));
            }
            AdminUserRespDTO ownerUser = userMap.get(NumberUtils.parseLong(task.getOwner()));
            if (ownerUser != null) {
                taskVO.setOwnerUser(BeanUtils.toBean(ownerUser, BpmProcessInstanceRespVO.User.class));
                findAndThen(deptMap, ownerUser.getDeptId(), dept -> taskVO.getOwnerUser().setDeptName(dept.getName()));
            }
            if (BpmTaskStatusEnum.RUNNING.getStatus().equals(taskStatus)) {
                // 设置表单权限 TODO @芋艿 是不是还要加一个全局的权限 基于 processInstance 的权限；回复：可能不需要，但是发起人，需要有个权限配置
                // TODO @jason：貌似这么返回，主要解决当前审批 task 的表单权限，但是不同抄送人的表单权限，可能不太对。例如说，对 A 抄送人是隐藏某个字段。
                // @芋艿 表单权限需要分离开。单独的接口来获取了 BpmProcessInstanceService.getProcessInstanceFormFieldsPermission
                taskVO.setFieldsPermission(BpmnModelUtils.parseFormFieldsPermission(bpmnModel, task.getTaskDefinitionKey()));
                // 操作按钮设置
                taskVO.setButtonsSetting(BpmnModelUtils.parseButtonsSetting(bpmnModel, task.getTaskDefinitionKey()));
            }
            return taskVO;
        });

        // 拼接父子关系
        Map<String, List<BpmTaskRespVO>> childrenTaskMap = convertMultiMap(
                filterList(taskVOList, r -> StrUtil.isNotEmpty(r.getParentTaskId())),
                BpmTaskRespVO::getParentTaskId);
        for (BpmTaskRespVO taskVO : taskVOList) {
            taskVO.setChildren(childrenTaskMap.get(taskVO.getId()));
        }
        return filterList(taskVOList, r -> StrUtil.isEmpty(r.getParentTaskId()));
    }

    default List<BpmTaskRespVO> buildTaskListByParentTaskId(List<Task> taskList,
                                                            Map<Long, AdminUserRespDTO> userMap,
                                                            Map<Long, DeptRespDTO> deptMap) {
        return convertList(taskList, task -> BeanUtils.toBean(task, BpmTaskRespVO.class, taskVO -> {
            AdminUserRespDTO assignUser = userMap.get(NumberUtils.parseLong(task.getAssignee()));
            if (assignUser != null) {
                taskVO.setAssigneeUser(BeanUtils.toBean(assignUser, BpmProcessInstanceRespVO.User.class));
                DeptRespDTO dept = deptMap.get(assignUser.getDeptId());
                if (dept != null) {
                    taskVO.getAssigneeUser().setDeptName(dept.getName());
                }
            }
            AdminUserRespDTO ownerUser = userMap.get(NumberUtils.parseLong(task.getOwner()));
            if (ownerUser != null) {
                taskVO.setOwnerUser(BeanUtils.toBean(ownerUser, BpmProcessInstanceRespVO.User.class));
                findAndThen(deptMap, ownerUser.getDeptId(), dept -> taskVO.getOwnerUser().setDeptName(dept.getName()));
            }
        }));
    }

    default BpmMessageSendWhenTaskCreatedReqDTO convert(ProcessInstance processInstance, AdminUserRespDTO startUser,
                                                        Task task) {
        BpmMessageSendWhenTaskCreatedReqDTO reqDTO = new BpmMessageSendWhenTaskCreatedReqDTO();
        reqDTO.setProcessInstanceId(processInstance.getProcessInstanceId())
                .setProcessInstanceName(processInstance.getName()).setStartUserId(startUser.getId())
                .setStartUserNickname(startUser.getNickname()).setTaskId(task.getId()).setTaskName(task.getName())
                .setAssigneeUserId(NumberUtils.parseLong(task.getAssignee()));
        return reqDTO;
    }

    /**
     * 将父任务的属性，拷贝到子任务（加签任务）
     * <p>
     * 为什么不使用 mapstruct 映射？因为 TaskEntityImpl 还有很多其他属性，这里我们只设置我们需要的。
     * 使用 mapstruct 会将里面嵌套的各个属性值都设置进去，会出现意想不到的问题。
     *
     * @param parentTask 父任务
     * @param childTask  加签任务
     */
    default void copyTo(TaskEntityImpl parentTask, TaskEntityImpl childTask) {
        childTask.setName(parentTask.getName());
        childTask.setDescription(parentTask.getDescription());
        childTask.setCategory(parentTask.getCategory());
        childTask.setParentTaskId(parentTask.getId());
        childTask.setProcessDefinitionId(parentTask.getProcessDefinitionId());
        childTask.setProcessInstanceId(parentTask.getProcessInstanceId());
        childTask.setTaskDefinitionKey(parentTask.getTaskDefinitionKey());
        childTask.setTaskDefinitionId(parentTask.getTaskDefinitionId());
        childTask.setPriority(parentTask.getPriority());
        childTask.setCreateTime(new Date());
        childTask.setTenantId(parentTask.getTenantId());
    }

    BpmTaskApproveReqVO convertDTO2Req(BpmTaskApproveReqDTO dto);

    @Mapping(target = "id", source = "taskId")
    BpmTaskApproveReqVO convertHandleDTO2Req(BpmHandleTaskReqDTO bpmHandleTaskReqDTO);


    List<BpmTaskAllInfoRespVO> convertListHisTaskInstance2AllRespVo(List<HistoricTaskInstance> tasks);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "taskId", source="id")
    @Mapping(target = "result", source = "taskInstance", qualifiedByName = "getResult")
    @Mapping(target = "reason", expression = "java(getReason(taskInstance))")
    @Mapping(target = "assigneeUserId", source = "assignee", qualifiedByName = "getLongFromStr")
    @Mapping(target = "endTime", source = "endTime", qualifiedByName = "getLocalDate")
    BpmTaskAllInfoRespVO taskInstanceToBpmTaskAllInfoRespVO(HistoricTaskInstance taskInstance);

    default String getReason(HistoricTaskInstance taskInstance) {
        if (ObjectUtil.isNull(taskInstance)) {
            return null;
        }
        String reason = (String) taskInstance.getTaskLocalVariables().get(TASK_VARIABLE_REASON);
        return reason;
    }

    @Named("getResult")
    default Integer getResult(HistoricTaskInstance taskInstance) {
        if (ObjectUtil.isNotNull(taskInstance)) {
            Integer status = (Integer) taskInstance.getTaskLocalVariables().get(TASK_VARIABLE_STATUS);
            if (ObjectUtil.isNotNull(status)) {
                return status;
            }
        }
        //TODO 用 状态 审批中 托底
        return BpmProcessInstanceResultEnum.PROCESS.getResult();
    }

    @Named("getLongFromStr")
    default Long getLongFromStr(String str) {
        if (StringUtils.isBlank(str)) {
            return null;
        }
        return Long.valueOf(str);
    }

    @Named("getLocalDate")
    default LocalDateTime getLocalDate(Date date) {
        if (ObjectUtil.isNull(date)) {
            return null;
        }
        return DateUtils.of(date);
    }

    default List<ContractProcessInstanceRelationInfoRespDTO> convert2DTOList(Map<String, HistoricTaskInstance> targetHistoricTasks, List<HistoricTaskInstance> bpmTaskExtDOs) {
        List<ContractProcessInstanceRelationInfoRespDTO> result = new ArrayList<>();
        Map<String, HistoricTaskInstance> extDOMap = CollectionUtils.convertMap(bpmTaskExtDOs, HistoricTaskInstance::getId);
        targetHistoricTasks.forEach((processInstanceId, historicTaskInstance) -> {
            HistoricTaskInstance bpmTaskExtDO = extDOMap.get(historicTaskInstance.getId());
            ContractProcessInstanceRelationInfoRespDTO contractProcessInstanceRelationInfoRespDTO = new ContractProcessInstanceRelationInfoRespDTO().setProcessInstanceId(processInstanceId)
                    .setTaskId(bpmTaskExtDO.getId())
                    .setProcessResult((Integer) bpmTaskExtDO.getTaskLocalVariables().get(TASK_VARIABLE_STATUS));
            result.add(contractProcessInstanceRelationInfoRespDTO);
        });
        return result;
    }
    @Mapping(target = "taskId", source = "id")
    @Mapping(target = "definitionKey", expression = "java(getReason(taskInstance))")
    @Mapping(target = "endTime", source = "endTime", qualifiedByName = "getLocalDate")
    @Mapping(target = "result", source = "taskInstance", qualifiedByName = "getResult")
    BpmTaskRespDTO d2r2(HistoricTaskInstance taskInstance);


    List<BpmTaskRespDTO> cDO2RespList(List<Task> taskList);
}
