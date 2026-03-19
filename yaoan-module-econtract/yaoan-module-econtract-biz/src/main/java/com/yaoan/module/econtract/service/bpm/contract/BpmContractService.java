package com.yaoan.module.econtract.service.bpm.contract;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.BigBpmContractRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.BpmContractCreateReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.BpmContractPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.BpmContractRespVO;

/**
 * <p>
 * 合同审批流程申请表 服务类
 * </p>
 *
 * @author doujiale
 * @since 2023-10-10
 */
public interface BpmContractService {

    /**
     * 创建合同审批申请
     *
     * @param loginUserId        当前登陆人ID
     * @param bpmContractCreateReqVO 请求参数
     * @return 流程实例ID
     */
    String createProcess(Long loginUserId, BpmContractCreateReqVO bpmContractCreateReqVO);

    /**
     * 退回 - 删除流程 - 推送电子合同 同步状态（修改成草稿箱）
     */
    String backProcess(BpmContractCreateReqVO bpmContractCreateReqVO) ;

    /**
     * 获取审批列表数据
     * @param reqVO 请求参数
     * @return 列表数据
     */
    PageResult<BpmContractRespVO> getApprovePage(BpmContractPageReqVO reqVO);

    BigBpmContractRespVO getBpmAllTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO);

    BigBpmContractRespVO getBpmDoneTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO);

    BigBpmContractRespVO getBpmToDoTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO);

    String createProcessV1(Long loginUserId,String id) throws Exception;

}
