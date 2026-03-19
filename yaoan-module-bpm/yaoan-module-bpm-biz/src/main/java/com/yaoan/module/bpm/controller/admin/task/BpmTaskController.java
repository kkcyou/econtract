package com.yaoan.module.bpm.controller.admin.task;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.number.NumberUtils;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.module.bpm.controller.admin.task.vo.task.*;
import com.yaoan.module.bpm.controller.admin.task.vo.task.withdraw.WithDrawalReqVo;
import com.yaoan.module.bpm.convert.task.BpmTaskConvert;
import com.yaoan.module.bpm.dal.dataobject.definition.BpmFormDO;
import com.yaoan.module.bpm.service.definition.BpmFormService;
import com.yaoan.module.bpm.service.definition.BpmProcessDefinitionService;
import com.yaoan.module.bpm.service.task.BpmProcessInstanceService;
import com.yaoan.module.bpm.service.task.BpmTaskService;
import com.yaoan.module.econtract.api.contract.ContractApi;
import com.yaoan.module.econtract.api.relative.RelativeApi;
import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import com.yaoan.module.system.api.dept.DeptApi;
import com.yaoan.module.system.api.dept.dto.DeptRespDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.framework.common.util.collection.CollectionUtils.*;
import static com.yaoan.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 流程任务实例")
@RestController
@RequestMapping("/bpm/task")
@Validated
public class BpmTaskController {

    @Resource
    private BpmTaskService taskService;
    @Resource
    private BpmProcessInstanceService processInstanceService;
    @Resource
    private BpmFormService formService;
    @Resource
    private BpmProcessDefinitionService bpmProcessDefinitionService;

    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private DeptApi deptApi;

    @GetMapping("todo-page")
    @Operation(summary = "获取 Todo 待办任务分页")
//    @PreAuthorize("@ss.hasPermission('bpm:task:query')")
    public CommonResult<PageResult<BpmTaskRespVO>> getTaskTodoPage(@Valid BpmTaskPageReqVO pageVO) {
        PageResult<Task> pageResult = taskService.getTaskTodoPage(getLoginUserId(), pageVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(PageResult.empty());
        }

        // 拼接数据
        Map<String, ProcessInstance> processInstanceMap = processInstanceService.getProcessInstanceMap(
                convertSet(pageResult.getList(), Task::getProcessInstanceId));
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(
                convertSet(processInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId())));
        return success(BpmTaskConvert.INSTANCE.buildTodoTaskPage(pageResult, processInstanceMap, userMap));
    }

    @GetMapping("done-page")
    @Operation(summary = "获取 Done 已办任务分页")
//    @PreAuthorize("@ss.hasPermission('bpm:task:query')")
    public CommonResult<PageResult<BpmTaskRespVO>> getTaskDonePage(@Valid BpmTaskPageReqVO pageVO) {
        PageResult<HistoricTaskInstance> pageResult = taskService.getTaskDonePage(getLoginUserId(), pageVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(PageResult.empty());
        }

        // 拼接数据
        Map<String, HistoricProcessInstance> processInstanceMap = processInstanceService.getHistoricProcessInstanceMap(
                convertSet(pageResult.getList(), HistoricTaskInstance::getProcessInstanceId));
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(
                convertSet(processInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId())));
        return success(BpmTaskConvert.INSTANCE.buildTaskPage(pageResult, processInstanceMap, userMap, null));
    }

    @GetMapping("manager-page")
    @Operation(summary = "获取全部任务的分页", description = "用于【流程任务】菜单")
//    @PreAuthorize("@ss.hasPermission('bpm:task:mananger-query')")
    public CommonResult<PageResult<BpmTaskRespVO>> getTaskManagerPage(@Valid BpmTaskPageReqVO pageVO) {
        PageResult<HistoricTaskInstance> pageResult = taskService.getTaskPage(getLoginUserId(), pageVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(PageResult.empty());
        }

        // 拼接数据
        Map<String, HistoricProcessInstance> processInstanceMap = processInstanceService.getHistoricProcessInstanceMap(
                convertSet(pageResult.getList(), HistoricTaskInstance::getProcessInstanceId));
        // 获得 User 和 Dept Map
        Set<Long> userIds = convertSet(processInstanceMap.values(), instance -> Long.valueOf(instance.getStartUserId()));
        userIds.addAll(convertSet(pageResult.getList(), task -> NumberUtils.parseLong(task.getAssignee())));
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(userIds);
        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(
                convertSet(userMap.values(), AdminUserRespDTO::getDeptId));
        return success(BpmTaskConvert.INSTANCE.buildTaskPage(pageResult, processInstanceMap, userMap, deptMap));
    }

    @GetMapping("/list-by-process-instance-id")
    @Operation(summary = "获得指定流程实例的任务列表", description = "包括完成的、未完成的")
    @Parameter(name = "processInstanceId", description = "流程实例的编号", required = true)
//    @PreAuthorize("@ss.hasPermission('bpm:task:query')")
    public CommonResult<List<BpmTaskRespVO>> getTaskListByProcessInstanceId(
            @RequestParam("processInstanceId") String processInstanceId) {
        List<HistoricTaskInstance> taskList = taskService.getTaskListByProcessInstanceId(processInstanceId);
        if (CollUtil.isEmpty(taskList)) {
            return success(Collections.emptyList());
        }

        // 拼接数据
        HistoricProcessInstance processInstance = processInstanceService.getHistoricProcessInstance(processInstanceId);
        // 获得 User 和 Dept Map
        Set<Long> userIds = convertSetByFlatMap(taskList, task ->
                Stream.of(NumberUtils.parseLong(task.getAssignee()), NumberUtils.parseLong(task.getOwner())));
        userIds.add(NumberUtils.parseLong(processInstance.getStartUserId()));
        AtomicReference<Map<Long, AdminUserRespDTO>> userMap = new AtomicReference<>(new HashMap<>());
                DataPermissionUtils.executeIgnore(()->{
                    TenantUtils.executeIgnore(()->{
                        userMap.set(adminUserApi.getUserMap(userIds));
                    });
        });
       // 如果流程为相对方审批，则修改相对方操作人部门名称为公司名称
        if (ActivityConfigurationEnum.ECMS_CONTRACT_RELATIVES.getDefinitionKey().equals(processInstance.getProcessDefinitionKey())){
            ContractApi contractApi = SpringUtil.getBean(ContractApi.class);
            Map<Long, Long> virtualIdForUserId = contractApi.getRelativeNameByUserIdMap(processInstance.getBusinessKey());
            List<Long> relContractIds = new ArrayList<>();

            for (Long userId : userIds) {
                if (userMap.get().get(userId) == null && virtualIdForUserId.get(userId) != null){
                    relContractIds.add(virtualIdForUserId.get(userId));
                }
            }

            AtomicReference<Map<Long, AdminUserRespDTO>> relUserMap = new AtomicReference<>(new HashMap<>());
            DataPermissionUtils.executeIgnore(()->{
                relUserMap.set(adminUserApi.getUserMap(relContractIds));
            });
            for (Long userId : userIds) {
                if (userMap.get().get(userId) == null){
                    if (virtualIdForUserId.get(userId) != null && relUserMap.get().get(virtualIdForUserId.get(userId)) != null){
                        userMap.get().put(userId, relUserMap.get().get(virtualIdForUserId.get(userId)).setDeptId(userId));
                    }else {
                        userMap.get().put(userId, new AdminUserRespDTO().setDeptId(userId));
                    }
                }
            }

            /*userMap.get().forEach((id,user)->{
                // 获取该合同对应的相对方id-name的map
                Map<Long, String> relativeNameByUserIdMap = contractApi.getRelativeNameByUserIdMap(processInstance.getBusinessKey());
                if (relativeNameByUserIdMap.containsKey(id)){
                    user.setNickname(relativeNameByUserIdMap.get(id));
                }
            });*/
        }

        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(
                convertSet(userMap.get().values(), AdminUserRespDTO::getDeptId));
        RelativeApi relativeApi = SpringUtil.getBean(RelativeApi.class);
        Map<Long, String> relativeNameVirtualIds = relativeApi.getRelativeNameVirtualIds(userIds);
        for (Long userId : userIds) {
            if (deptMap.get(userId) == null){
                deptMap.put(userId,new DeptRespDTO().setName(relativeNameVirtualIds.get(userId)));
            }
        }
        // 获得 Form Map
        Map<Long, BpmFormDO> formMap = formService.getFormMap(
                convertSet(taskList, task -> NumberUtils.parseLong(task.getFormKey())));
        // 获得 BpmnModel
        BpmnModel bpmnModel = bpmProcessDefinitionService.getProcessDefinitionBpmnModel(processInstance.getProcessDefinitionId());
        return success(BpmTaskConvert.INSTANCE.buildTaskListByProcessInstanceId(taskList, processInstance,
                formMap, userMap.get(), deptMap, bpmnModel));
    }

    @GetMapping("/process/list")
    @Operation(summary = "获得指定流程实例的任务列表", description = "包括完成的、未完成的")
    @Parameter(name = "processInstanceId", description = "流程实例的编号", required = true)
//    @PreAuthorize("@ss.hasPermission('bpm:task:query')")
    public CommonResult<List<BpmProcessRespVO>> listTaskProcessList(@RequestParam("processInstanceId") String processInstanceId){
        List<HistoricTaskInstance> taskList = taskService.getTaskListByProcessInstanceId(processInstanceId);
        if (CollUtil.isEmpty(taskList)) {
            return success(Collections.emptyList());
        }

        // 拼接数据
        HistoricProcessInstance processInstance = processInstanceService.getHistoricProcessInstance(processInstanceId);
        // 获得 User 和 Dept Map
        Set<Long> userIds = convertSetByFlatMap(taskList, task ->
                Stream.of(NumberUtils.parseLong(task.getAssignee()), NumberUtils.parseLong(task.getOwner())));
        userIds.add(NumberUtils.parseLong(processInstance.getStartUserId()));
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(userIds);
        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(
                convertSet(userMap.values(), AdminUserRespDTO::getDeptId));
        // 获得 Form Map
        Map<Long, BpmFormDO> formMap = formService.getFormMap(
                convertSet(taskList, task -> NumberUtils.parseLong(task.getFormKey())));
        // 获得 BpmnModel
        BpmnModel bpmnModel = bpmProcessDefinitionService.getProcessDefinitionBpmnModel(processInstance.getProcessDefinitionId());
        List<BpmTaskRespVO> bpmTaskRespVOList = BpmTaskConvert.INSTANCE.buildTaskListByProcessInstanceId(taskList, processInstance,
                formMap, userMap, deptMap, bpmnModel);
        List<BpmProcessRespVO> bpmProcessRespVOList = new ArrayList<>();
        for (BpmTaskRespVO bpmTaskRespVO : bpmTaskRespVOList) {
            BpmProcessRespVO bpmProcessRespVO = new BpmProcessRespVO();
            bpmProcessRespVO.setTaskId(bpmTaskRespVO.getId());
            bpmProcessRespVO.setTaskName(bpmTaskRespVO.getName());
            bpmProcessRespVO.setResult(bpmTaskRespVO.getStatus());
            if (bpmTaskRespVO.getAssigneeUser() != null) {
                bpmProcessRespVO.setUserId(bpmTaskRespVO.getAssigneeUser().getId());
                bpmProcessRespVO.setUserName(bpmTaskRespVO.getAssigneeUser().getNickname());
            }
            bpmProcessRespVO.setReason(bpmTaskRespVO.getReason());
            bpmProcessRespVO.setClaimTime(bpmTaskRespVO.getCreateTime());
            bpmProcessRespVO.setEndTime(bpmTaskRespVO.getEndTime());
            bpmProcessRespVO.setCreateTime(bpmTaskRespVO.getCreateTime());
            bpmProcessRespVOList.add(bpmProcessRespVO);
        }
        return success(bpmProcessRespVOList);
    }

    @PutMapping("/approve")
    @Operation(summary = "通过任务")
//    @PreAuthorize("@ss.hasPermission('bpm:task:update')")
    public CommonResult<Boolean> approveTask(@Valid @RequestBody BpmTaskApproveReqVO reqVO) {
        taskService.approveTask(getLoginUserId(), reqVO);
        return success(true);
    }


    @GetMapping("/list-by-return")
    @Operation(summary = "获取所有可回退的节点", description = "用于【流程详情】的【回退】按钮")
    @Parameter(name = "taskId", description = "当前任务ID", required = true)
//    @PreAuthorize("@ss.hasPermission('bpm:task:update')")
    public CommonResult<List<BpmTaskRespVO>> getTaskListByReturn(@RequestParam("id") String id) {
        List<UserTask> userTaskList = taskService.getUserTaskListByReturn(id);
        return success(convertList(userTaskList, userTask -> // 只返回 id 和 name
                new BpmTaskRespVO().setName(userTask.getName()).setTaskDefinitionKey(userTask.getId())));
    }

    @PutMapping("/return")
    @Operation(summary = "回退任务", description = "用于【流程详情】的【回退】按钮")
//    @PreAuthorize("@ss.hasPermission('bpm:task:update')")
    public CommonResult<Boolean> returnTask(@Valid @RequestBody BpmTaskReturnReqVO reqVO) {
        taskService.returnTask(getLoginUserId(), reqVO);
        return success(true);
    }

/*    @PutMapping("/returnV2")
    @Operation(summary = "回退任务V2,业务数据记录退回的状态", description = "用于【流程详情】的【回退】按钮")
//    @PreAuthorize("@ss.hasPermission('bpm:task:update')")
    public CommonResult<Boolean> returnTaskV2(@Valid @RequestBody BpmTaskReturnV2ReqVO reqVO) {
        taskService.returnTaskV2(getLoginUserId(), reqVO);
        return success(true);
    }*/

    @PutMapping("/delegate")
    @Operation(summary = "委派任务", description = "用于【流程详情】的【委派】按钮")
//    @PreAuthorize("@ss.hasPermission('bpm:task:update')")
    public CommonResult<Boolean> delegateTask(@Valid @RequestBody BpmTaskDelegateReqVO reqVO) {
        taskService.delegateTask(getLoginUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/transfer")
    @Operation(summary = "转派任务", description = "用于【流程详情】的【转派】按钮")
//    @PreAuthorize("@ss.hasPermission('bpm:task:update')")
    public CommonResult<Boolean> transferTask(@Valid @RequestBody BpmTaskTransferReqVO reqVO) {
        taskService.transferTask(getLoginUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/create-sign")
    @Operation(summary = "加签", description = "before 前加签，after 后加签")
//    @PreAuthorize("@ss.hasPermission('bpm:task:update')")
    public CommonResult<Boolean> createSignTask(@Valid @RequestBody BpmTaskSignCreateReqVO reqVO) {
        taskService.createSignTask(getLoginUserId(), reqVO);
        return success(true);
    }

    @DeleteMapping("/delete-sign")
    @Operation(summary = "减签")
//    @PreAuthorize("@ss.hasPermission('bpm:task:update')")
    public CommonResult<Boolean> deleteSignTask(@Valid @RequestBody BpmTaskSignDeleteReqVO reqVO) {
        taskService.deleteSignTask(getLoginUserId(), reqVO);
        return success(true);
    }

    @GetMapping("/list-by-parent-task-id")
    @Operation(summary = "获得指定父级任务的子任务列表") // 目前用于，减签的时候，获得子任务列表
    @Parameter(name = "parentTaskId", description = "父级任务编号", required = true)
//    @PreAuthorize("@ss.hasPermission('bpm:task:query')")
    public CommonResult<List<BpmTaskRespVO>> getTaskListByParentTaskId(@RequestParam("parentTaskId") String parentTaskId) {
        List<Task> taskList = taskService.getTaskListByParentTaskId(parentTaskId);
        if (CollUtil.isEmpty(taskList)) {
            return success(Collections.emptyList());
        }
        // 拼接数据
        Map<Long, AdminUserRespDTO> userMap = adminUserApi.getUserMap(convertSetByFlatMap(taskList,
                user -> Stream.of(NumberUtils.parseLong(user.getAssignee()), NumberUtils.parseLong(user.getOwner()))));
        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(
                convertSet(userMap.values(), AdminUserRespDTO::getDeptId));
        return success(BpmTaskConvert.INSTANCE.buildTaskListByParentTaskId(taskList, userMap, deptMap));
    }

    /**
     * 将已审批的任务撤回来
     */
    @PostMapping("/withdrawal")
    @Operation(summary = "将已审批的任务撤回来", description = "最后一岗不可撤，下一节点已被处理的不可撤")
    public CommonResult<String> withdrawal(@RequestBody WithDrawalReqVo reqVo) {
        taskService.withdrawal(reqVo);
        return success("success");
    }

    @PutMapping("/reject")
    @Operation(summary = "不通过任务")
//    @PreAuthorize("@ss.hasPermission('bpm:task:update')")
    public CommonResult<Boolean> rejectTask(@Valid @RequestBody BpmTaskRejectReqVO reqVO) {
        taskService.rejectTask(getLoginUserId(), reqVO);
        return success(true);
    }


}
