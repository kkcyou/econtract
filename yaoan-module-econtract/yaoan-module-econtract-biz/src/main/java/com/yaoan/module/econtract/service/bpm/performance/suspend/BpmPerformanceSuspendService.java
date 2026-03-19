package com.yaoan.module.econtract.service.bpm.performance.suspend;


import com.yaoan.module.econtract.controller.admin.bpm.performance.suspend.vo.BpmProcessRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.performance.suspend.vo.BpmSuspendCreateReqVO;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author doujl
 * @since 2023-07-24
 */
public interface BpmPerformanceSuspendService {

    /**
     * 创建中止申请
     *
     * @param loginUserId        当前登陆人ID
     * @param suspendCreateReqVO 请求参数
     * @return 流程实例ID
     */
    String createSuspend(Long loginUserId, BpmSuspendCreateReqVO suspendCreateReqVO);

    /**
     * @param processInstanceId 流程实例ID
     * @return 审批进程
     */
    List<BpmProcessRespVO> process(String processInstanceId);
}
