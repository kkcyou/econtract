package com.yaoan.module.bpm.service.listener.contractborrow;

import cn.hutool.extra.spring.SpringUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.bpm.event.BpmProcessInstanceStatusEvent;
import com.yaoan.module.bpm.event.BpmProcessInstanceStatusEventListener;
import com.yaoan.module.econtract.api.bpm.contractBorrow.ContractBorrowBpmApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.yaoan.module.econtract.enums.ActivityConfigurationEnum.CONTRACT_BORROW_SUBMIT_APPROVE;
/**
 * 合同借阅的结果的监听器实现类
 * @author Pele
 */
@Slf4j
@Component
public class ContractBorrowBpmSubmitApproveResultListener extends BpmProcessInstanceStatusEventListener {

    @Override
    protected String getProcessDefinitionKey() {
        return CONTRACT_BORROW_SUBMIT_APPROVE.getDefinitionKey();
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        if (BpmProcessInstanceResultEnum.isEndResult(event.getStatus())) {
            ContractBorrowBpmApi borrowBpmApi = SpringUtil.getBean(ContractBorrowBpmApi.class);
            try {
                borrowBpmApi.notifyApproveStatus(event.getBusinessKey(), BpmProcessInstanceResultEnum.getInstance(event.getStatus()));
            } catch (Exception e) {
                log.error("ContractBorrowBpmSubmitApproveResultListener.notifyApproveStatus 监听异常");
                e.printStackTrace();
            }
        }
    }
}
