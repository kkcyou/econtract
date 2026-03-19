package com.yaoan.module.econtract.api.contract;

import com.yaoan.module.econtract.api.contract.dto.EcontractOrgDTO;
import com.yaoan.module.econtract.enums.ContractStatusEnums;

import java.util.List;
import java.util.Map;

/**
 * @author doujiale
 */
public interface ContractApi {


    /**
     * 通知合同状态任务执行
     *
     * @param processInstanceId 流程实例ID
     */
    void notifyContractStatus(String processInstanceId, ContractStatusEnums statusEnums);

    void contractChange(String processInstanceId);

    /**
     * 合同审批通过发送审批结果到电子合同
     */
    void  productApproveSendEcms(String processInstanceId,String contractId);

    /**
     * 合同签署发送后同步到电子合同
     */
    void  productSignSendEcms(String processInstanceId);

    void updateSign(String processInstanceId);
    void updateRelativeSignOrConfirm(String processInstanceId, String type);
    void updateSignReject(String processInstanceId);
    void updateRelativeSignReject(String processInstanceId, String type);

    // 相对方退回合同处理
    void rejectByRelativeSign(String processInstanceId);

    Boolean isNeedSignet(String processInstanceId,String contractId);

    List<Long> calculateUsers4OrgSup(String processInstanceId);

    // 合同相对方退回通知
    void notifyContractRejectByRelative(String processInstanceId);

    // 判断是否存在已签署
    void isContainsSigned(String processInstanceId);

    Map<Long,Long> getRelativeNameByUserIdMap(String contractId);

    // 从政采端获取单位
    EcontractOrgDTO getEcontractOrgFromZCById(String orgId);

    void withdrawal(String processInstanceId, Integer flag);

    void warningNotify(String processInstanceId);

    void updateConfirmReject(String processInstanceId);
}
