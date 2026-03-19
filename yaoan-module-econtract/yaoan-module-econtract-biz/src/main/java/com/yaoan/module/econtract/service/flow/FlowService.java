package com.yaoan.module.econtract.service.flow;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * flow 服务类
 * </p>
 *
 * @author doujl
 * @since 2023-07-04
 */
public interface FlowService {

    /**
     * 创建合同确认流程实例
     *
     * @param contractId 合同ID
     * @return 实例的编号
     */
    String createContractProcessInstance(String contractId);

    /**
     * 创建合同签署流程实例
     *
     * @param contractId 合同ID
     * @return 实例的编号
     */
    String createContractSignProcessInstance(String contractId);

    List<Long> buildTaskUserIds(String contractId);

}
