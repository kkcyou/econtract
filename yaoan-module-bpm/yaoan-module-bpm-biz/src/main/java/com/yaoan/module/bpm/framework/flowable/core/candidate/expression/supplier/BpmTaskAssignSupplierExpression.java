package com.yaoan.module.bpm.framework.flowable.core.candidate.expression.supplier;

import com.yaoan.framework.common.util.collection.SetUtils;
import com.yaoan.framework.common.util.number.NumberUtils;
import com.yaoan.module.bpm.service.task.BpmProcessInstanceService;
import com.yaoan.module.econtract.api.contract.ContractApi;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description:
 * @author: Pele
 * @date: 2025/2/27 16:29
 */
@Component
public class BpmTaskAssignSupplierExpression {
    @Resource
    private BpmProcessInstanceService processInstanceService;
    @Resource
    private ContractApi contractApi;

    /**
     * 计算审批的候选人
     *
     * @param execution 流程执行实体
     * @return 发起人
     */
    public Set<Long> calculateUsers(ExecutionEntityImpl execution) {
        ProcessInstance processInstance = processInstanceService.getProcessInstance(execution.getProcessInstanceId());
        List<Long> userIds = contractApi.calculateUsers4OrgSup(processInstance.getProcessInstanceId());
        return new HashSet<>(userIds);
    }

}
