package com.yaoan.module.econtract.service.flow;

import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 执行器
 * </p>
 *
 * @author doujl
 * @since 2023-07-04
 */
@Component
public class SignTripartiteFlowExecutor implements FlowExecutor {

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    public static final String PROCESS_KEY = "ecms_contract_sign_tripartite";

    @Override
    public String execute(List<Long> userIds, String businessKey) {
        // 发起 BPM 流程
        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("assign0", userIds.get(0));
        processInstanceVariables.put("assign1", userIds.get(0));
        processInstanceVariables.put("assign2", userIds.get(1));
        processInstanceVariables.put("assign3", userIds.get(2));
        return processInstanceApi.createProcessInstance(WebFrameworkUtils.getLoginUserId(),
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(businessKey));
    }
}
