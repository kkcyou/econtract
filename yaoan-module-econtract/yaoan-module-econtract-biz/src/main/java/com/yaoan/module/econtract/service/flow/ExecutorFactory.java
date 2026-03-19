package com.yaoan.module.econtract.service.flow;

import com.yaoan.module.econtract.enums.ContractInstanceTypeEnums;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 执行器
 * </p>
 *
 * @author doujl
 * @since 2023-07-04
 */
@Component
public class ExecutorFactory {

    @Resource
    private ConfirmBothFlowExecutor confirmBothFlowExecutor;
    @Resource
    private ConfirmTripartiteFlowExecutor confirmTripartiteFlowExecutor;

    public FlowExecutor getExecutorByUserIds(List<Long> userIds) {

        if (userIds.size() == 4) {
            return confirmBothFlowExecutor;
        }
        if (userIds.size() == 6) {
            return confirmTripartiteFlowExecutor;
        }
        return null;
    }
}
