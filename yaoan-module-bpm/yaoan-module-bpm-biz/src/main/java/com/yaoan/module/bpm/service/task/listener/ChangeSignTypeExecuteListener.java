package com.yaoan.module.bpm.service.task.listener;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.service.task.BpmProcessInstanceService;
import com.yaoan.module.econtract.api.contract.ContractApi;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.MultiInstanceLoopCharacteristics;
import org.flowable.bpmn.model.UserTask;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.ExecutionListener;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 合同相对方签署任务的监听器
 *
 * @author doujiale
 */
@Slf4j
public class ChangeSignTypeExecuteListener implements ExecutionListener {
    public static final String ALL_APPROVE_COMPLETE_EXPRESSION = "${ nrOfCompletedInstances >= nrOfInstances }";
    @Override
    public void notify(DelegateExecution execution) {
        RepositoryService repositoryService = SpringUtil.getBean(RepositoryService.class);
        TaskService taskService = SpringUtil.getBean(TaskService.class);
        // 1. 获取流程定义 Key
        String processDefKey = execution.getProcessDefinitionId();
        // 2. 通过 RepositoryService 查询 BpmnModel
        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefKey);
        // 3. 查找任务节点
        UserTask userTask = (UserTask) bpmnModel.getFlowElement(execution.getCurrentActivityId());
        MultiInstanceLoopCharacteristics loopCharacteristics = userTask.getLoopCharacteristics();
        if (loopCharacteristics == null) {
            loopCharacteristics = new MultiInstanceLoopCharacteristics();
            userTask.setLoopCharacteristics(loopCharacteristics);
        }
        // 设置签批方式
        loopCharacteristics.setSequential(isSequential(execution.getProcessInstanceId()));
        if(loopCharacteristics.isSequential()){
            loopCharacteristics.setCompletionCondition(ALL_APPROVE_COMPLETE_EXPRESSION);
            // 指定实例数量
            loopCharacteristics.setLoopCardinality("1");
        }
    }


    // 会签还是依次签
    private Boolean isSequential(String processInstanceId) {
        BpmProcessInstanceService processInstanceService = SpringUtil.getBean(BpmProcessInstanceService.class);
        ProcessInstance processInstance = processInstanceService.getProcessInstance(processInstanceId);
        // 获取变量值
        Map<String, Object> processVariables = processInstance.getProcessVariables();
        Object isSequential = processVariables.get("isSequential");
        if (ObjectUtil.isEmpty(isSequential)){
            return false;
        }
        return (Boolean) isSequential;
    }
}
