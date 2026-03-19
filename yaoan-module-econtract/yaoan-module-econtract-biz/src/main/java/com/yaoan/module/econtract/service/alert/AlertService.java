package com.yaoan.module.econtract.service.alert;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.alert.vo.AlertReqVO;
import com.yaoan.module.econtract.controller.admin.alert.vo.AlertRespVO;

/**
 * @description: 合同提醒service
 * @author: Pele
 * @date: 2023/11/8 15:50
 */
public interface AlertService {
    /**
     * 删除流程id相关的所有提醒记录
     */
    public void deleteAllAlertsByProcessInstanceId(String processInstanceId);

    /**
     * 新增当前流程的提醒记录
     */
    public void addAlertByProcessInstanceId(String processInstanceId,String taskDefinitionKey);

    PageResult<AlertRespVO> getContractAlertPage(AlertReqVO vo);
}
