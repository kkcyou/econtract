package com.yaoan.module.econtract.service.flow;

import java.util.List;

/**
 * <p>
 * 执行器
 * </p>
 *
 * @author doujl
 * @since 2023-07-04
 */
public interface FlowExecutor {

    /**
     * 创建合同实例
     *
     * @param userIds 代办人IDs
     * @param businessKey 合同唯一标识
     * @return 流程示例ID
     */
    String execute(List<Long> userIds, String businessKey);

}
