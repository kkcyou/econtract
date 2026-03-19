package com.yaoan.module.bpm.service.task.listener;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.framework.tenant.core.context.TenantContextHolder;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.enums.task.ContractProcessInstanceResultEnum;
import com.yaoan.module.bpm.service.task.BpmProcessInstanceService;
import com.yaoan.module.econtract.api.contract.ContractApi;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.service.delegate.DelegateTask;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 合同相对方签署任务的监听器
 *
 * @author doujiale
 */
@Slf4j
public class RelativeConfirmExecuteListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {

        TaskService taskService = SpringUtil.getBean(TaskService.class);
        ContractApi contractApi = SpringUtil.getBean(ContractApi.class);
        // 判断是否为循环退回或者默认提交的任务，如是则不做任何处理
        // 判断该任务是否为循环退回的会签任务
        Boolean isReject = (Boolean) taskService.getVariable(delegateTask.getId(), "is_reject");
        // 判断该任务是否为撤回退回后任务完成的操作
        Boolean committed = (Boolean) taskService.getVariable(delegateTask.getId(), "committed");
        if ((isReject != null && isReject) || (committed != null && committed)){
            log.info("触发了合同相对方自动完成任务，ProcessInstanceId:{}", delegateTask.getProcessInstanceId());
            // 通过一次判断后及时清理相关参数，避免后续影响
            taskService.setVariable(delegateTask.getId(), "committed", false);
            taskService.setVariable(delegateTask.getId(), "is_reject", false);
        } else {
            BpmProcessInstanceService processInstanceService = SpringUtil.getBean(BpmProcessInstanceService.class);
            ProcessInstance processInstance = processInstanceService.getProcessInstance(delegateTask.getProcessInstanceId());
            if (processInstance != null) {
                // 获取变量值
                Map<String, Object> processVariables = processInstance.getProcessVariables();
                if (ContractProcessInstanceResultEnum.REJECT.getCommand().equals(processVariables.get("passVal"))){
                    log.info("触发了合同相对方确认退回任务，ProcessInstanceId:{}", delegateTask.getProcessInstanceId());
                    contractApi.updateRelativeSignReject(delegateTask.getProcessInstanceId(),"CONFIRM");
                    // 设置跳过后续节点参数（依次签属使用）
                    taskService.setVariable(delegateTask.getId(), "skipFlag", IfNumEnums.YES.getCode());
                    // 查找当前流程实例中所有未完成的会签任务（会签使用）
                    TaskQuery taskQuery = taskService.createTaskQuery()
                            .processInstanceId(delegateTask.getProcessInstanceId())
                            .active();
                    List<Task> uncompletedTasks = taskQuery.list();
                    List<Task> tasks = uncompletedTasks.stream().filter(item -> !delegateTask.getId().equals(item.getId())).collect(Collectors.toList());
                    // 将剩余未完成的会签任务标记为退回（设置退回标识）并完成任务
                    for (Task uncompletedTask : tasks) {
                        taskService.setVariable(uncompletedTask.getId(), "is_reject", true);
                        taskService.complete(uncompletedTask.getId());
                    }
                    // 调用站内信通知其他相对方合同退回信息
                    contractApi.notifyContractRejectByRelative(delegateTask.getProcessInstanceId());
                } else {
                    // 不是循环退回操作，且不是撤回退回后任务完成的操作，就修改合同相对方确认/签署状态
                    log.info("触发了合同相对方确认任务，ProcessInstanceId:{}", delegateTask.getProcessInstanceId());
                    taskService.setVariable(delegateTask.getId(), "skipFlag", IfNumEnums.NO.getCode());
                    contractApi.updateRelativeSignOrConfirm(delegateTask.getProcessInstanceId(),"CONFIRM");
                }
            }
        }
    }

}
