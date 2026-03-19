package com.yaoan.module.bpm.service.listener.template;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.bpm.event.BpmProcessInstanceStatusEvent;
import com.yaoan.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.yaoan.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEvent;
import com.yaoan.module.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventListener;
import com.yaoan.module.econtract.api.bpm.template.TemplateBpmApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description: 继承框架的事件监听器，当流程走到结束节点时，会交给监听器一个事件 event
 * @author: Pele
 * @date: 2023/9/14 18:25
 */
@Component
@Slf4j
public class TemplateBpmSubmitListener extends BpmProcessInstanceStatusEventListener {

    @Override
    protected String getProcessDefinitionKey() {
        return "template_submit_approve";
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event)  {
        if (BpmProcessInstanceResultEnum.isEndResult(event.getStatus())) {
            // 获取 业务流程Api
            TemplateBpmApi templateBpmApi = SpringUtil.getBean(TemplateBpmApi.class);
            try{
                //通知更新范本数据
                templateBpmApi.notifyApproveStatus(event.getBusinessKey(), BpmProcessInstanceResultEnum.getInstance(event.getStatus()));
            }catch (Exception e){
                log.error("【条款审批结果监听异常】");
            }
         }
    }
}
