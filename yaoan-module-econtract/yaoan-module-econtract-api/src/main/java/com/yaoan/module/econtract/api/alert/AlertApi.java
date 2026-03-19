package com.yaoan.module.econtract.api.alert;

/**
 * @description:
 * @author: Pele
 * @date: 2023/11/8 16:08
 */
public interface AlertApi {
    /**
     * 删除流程id相关的所有提醒记录
     */
    public void deleteAllAlertsByProcessInstanceId(String processInstanceId);

    /**
     * 新增当前流程的提醒记录
     */
    public void addAlertByProcessInstanceId(String processInstanceId,String taskDefinitionKey);
}
