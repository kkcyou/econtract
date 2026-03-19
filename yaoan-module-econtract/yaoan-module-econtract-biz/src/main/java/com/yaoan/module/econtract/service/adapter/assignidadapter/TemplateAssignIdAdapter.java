package com.yaoan.module.econtract.service.adapter.assignidadapter;

import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.econtract.dal.mysql.bpm.template.TemplateBpmMapper;
import com.yaoan.module.econtract.dal.mysql.contracttemplate.ContractTemplateMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/5 10:35
 */
@Service
public class TemplateAssignIdAdapter implements AssignIdAdapter {
    @Resource
    private ContractTemplateMapper contractTemplateMapper;
    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private TemplateBpmMapper templateBpmMapper;

    @Override
    public Map<String, String> getUserIdList(List<String> instanceList) {
        return bpmTaskApi.getTodoTaskList(SecurityFrameworkUtils.getLoginUserId(), instanceList);
    }


}
