package com.yaoan.module.econtract.api.bpm.payment.invoice;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contractInvoiceManage.ContractInvoiceManageDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contractinvoicemanage.ContractInvoiceManageMapper;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @description: 收款申请
 * @author: Pele
 * @date: 2024/10/10 17:10
 */
@Service
public class InvoiceApplicationBpmApiImpl implements InvoiceApplicationBpmApi {
    @Resource
    private ContractInvoiceManageMapper contractInvoiceManageMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private ContractMapper contractMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums) {
        ContractInvoiceManageDO entity = contractInvoiceManageMapper.selectById(businessKey);
        if (ObjectUtil.isNotNull(entity)) {
            //退回
            if (BpmProcessInstanceResultEnum.BACK == statusEnums) {

            }
            //再次提交
            if (BpmProcessInstanceResultEnum.PROCESS == statusEnums) {

            }
            //通过,计划变成待确认状态
            if (BpmProcessInstanceResultEnum.APPROVE == statusEnums) {
                ContractInvoiceManageDO invoiceManageDO=contractInvoiceManageMapper.selectById(businessKey);
                contractInvoiceManageMapper.updateById(new ContractInvoiceManageDO().setId(businessKey).setStatus(BpmProcessInstanceResultEnum.APPROVE.getResult()));
                if(ObjectUtil.isNotNull(invoiceManageDO)){
                    paymentScheduleMapper.updateById(new PaymentScheduleDO().setId(invoiceManageDO.getPlanId()).setStatus(PaymentScheduleStatusEnums.CONFIRM.getCode()));

                }
            }

        }
    }
}
