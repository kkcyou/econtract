package com.yaoan.module.econtract.api.alert;

import com.yaoan.module.econtract.service.alert.AlertService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description:
 * @author: Pele
 * @date: 2023/11/8 16:09
 */
@Service
public class AlertApiImpl implements AlertApi {

    @Resource
    private AlertService alertService;

    @Override
    public void deleteAllAlertsByProcessInstanceId(String processInstanceId) {
        alertService.deleteAllAlertsByProcessInstanceId(processInstanceId);
    }

    @Override
    public void addAlertByProcessInstanceId(String processInstanceId,String taskDefinitionKey) {
        alertService.addAlertByProcessInstanceId(processInstanceId, taskDefinitionKey);
    }
}
