package com.yaoan.module.econtract.api.bpm.payment.deferred;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.deferred.PaymentDeferredApplyDO;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.deferred.PaymentDeferredApplyMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;

/**
 * @description:
 * @author: Pele
 * @date: 2024/10/8 17:42
 */
@Service
public class PaymentDeferredApplicationBpmApiImpl implements PaymentDeferredApplicationBpmApi {
    @Resource
    private PaymentDeferredApplyMapper paymentDeferredApplyMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;


    @Override
    public void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums) {
        PaymentDeferredApplyDO entity = paymentDeferredApplyMapper.selectById(businessKey);
        if (ObjectUtil.isNotNull(entity)) {
            //如果是退回
            if (BpmProcessInstanceResultEnum.BACK == statusEnums) {
            }
            //如果是审批中
            if (BpmProcessInstanceResultEnum.PROCESS == statusEnums) {
                //如果是从退回状态变成的审批中，则需要更新计划状态（和提交申请的时候一样）
                if (Objects.equals(BpmProcessInstanceResultEnum.BACK.getResult(), entity.getResult())) {
                    checkScheduleForSubmit(businessKey);
                }
            }

            //如果申请通过,延期计划的支付时间，更新审批完成时间
            if (BpmProcessInstanceResultEnum.APPROVE == statusEnums) {
                //延期申请：更新审批完成时间
                entity.setApproveFinishTime(new Date());
                //履约计划：延期支付时间
                PaymentScheduleDO paymentScheduleDO = new PaymentScheduleDO()
                        .setId(entity.getPlanId())
                        .setPaymentTime(entity.getDeferredPaymentDate());
                paymentScheduleMapper.updateById(paymentScheduleDO);
            }
            // 更新付款审批流表状态
            entity.setResult(statusEnums.getResult());
            paymentDeferredApplyMapper.updateById(entity);
        }
    }

    /**
     * 校验该计划是否存在审批中的延期申请
     */
    private void checkScheduleForSubmit(String planId) {
        Long count = paymentDeferredApplyMapper.selectCount(new LambdaQueryWrapperX<PaymentDeferredApplyDO>()
                .eq(PaymentDeferredApplyDO::getPlanId, planId)
                .eq(PaymentDeferredApplyDO::getResult, BpmProcessInstanceResultEnum.PROCESS)
        );
        if (0 < count) {
            throw exception(SYSTEM_ERROR, "该计划已提起延期申请，不可重复提交。");
        }
    }
}