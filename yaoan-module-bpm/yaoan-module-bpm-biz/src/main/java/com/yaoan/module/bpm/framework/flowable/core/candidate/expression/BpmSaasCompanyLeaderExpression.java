package com.yaoan.module.bpm.framework.flowable.core.candidate.expression;

import com.yaoan.module.bpm.service.task.BpmProcessInstanceService;
import com.yaoan.module.econtract.api.bpm.company.BpmCompanyApi;
import com.yaoan.module.system.api.dept.CompanyApi;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-29 10:46
 */
@Component
public class BpmSaasCompanyLeaderExpression {
  @Resource private BpmCompanyApi bpmCompanyApi;

  @Resource private BpmProcessInstanceService processInstanceService;

  /**
   * 计算审批的候选人
   *
   * @param execution 流程执行实体
   * @return 发起人
   */
  public Set<Long> calculateUsers(DelegateExecution execution) {
    ProcessInstance processInstance =
        processInstanceService.getProcessInstance(execution.getProcessInstanceId());
    String bpmId = processInstance.getBusinessKey();

    List<Long> userId = bpmCompanyApi.calculateUsers4SaasCompanyExpression(bpmId);
    return new HashSet<>(userId);
  }
}
