package com.yaoan.module.econtract.service.contract;

import com.yaoan.module.econtract.controller.admin.contract.vo.BpmTaskApproveReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.BpmTaskRejectReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.BpmTaskRepealReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.BpmTaskRevokeReqVO;

import javax.validation.Valid;
import java.util.List;

public interface TaskService {


    /**
     * 通过任务
     *
     * @param userId 用户编号
     * @param reqVO  通过请求
     */
    void approveTask(Long userId, @Valid BpmTaskApproveReqVO reqVO) throws Exception;

    /**
     * 不通过任务
     *
     * @param userId 用户编号
     * @param reqVO  不通过请求
     */
    void rejectTask(Long userId, @Valid BpmTaskRejectReqVO reqVO);

    /**
     * 发送任务
     *
     * @param userId 用户编号
     * @param reqVO  发送任务
     */
    void sendTask(Long userId, BpmTaskApproveReqVO reqVO);

    /**
     * 撤回任务
     *
     * @param userId 用户编号
     * @param reqVO  撤回任务
     */
    void revokeTask(Long userId, BpmTaskRevokeReqVO reqVO);
    void counterSignRevokeTask(Long userId, BpmTaskRevokeReqVO reqVO);


    /**
     * 撤销任务
     *
     * @param userId 用户编号
     * @param reqVO  撤销任务
     */
    void repealTask(Long userId, BpmTaskRepealReqVO reqVO);

    /**
     * 撤销任务(兼容多模块)
     *
     * @param userId 用户编号
     * @param reqVO  撤销任务
     */
    void repealTaskV2(Long userId, BpmTaskRepealReqVO reqVO);

    /**
     *  批量退回请求 接口
     */
    String rejectTaskBatch(List<BpmTaskRejectReqVO> voList);

    /**
     *  批量通过请求 接口
     */
    String approveTaskBatch(List<BpmTaskApproveReqVO> voList);
}
