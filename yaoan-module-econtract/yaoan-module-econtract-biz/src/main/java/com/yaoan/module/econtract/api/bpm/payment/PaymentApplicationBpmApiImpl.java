package com.yaoan.module.econtract.api.bpm.payment;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplicationDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.payment.paymentapplication.PaymentApplicationMapper;
import com.yaoan.module.econtract.enums.StatusEnums;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleApplyStatusEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import com.yaoan.module.econtract.service.paymentPlan.PaymentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @description: 付款申请监听
 * @author: Pele
 * @date: 2023/12/26 15:59
 */
@Service
public class PaymentApplicationBpmApiImpl implements PaymentApplicationBpmApi {
    @Resource
    private PaymentApplicationMapper paymentApplicationMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private PaymentService paymentService;

    @Override
    @DataPermission(enable = false)
    public void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums) {
        PaymentApplicationDO entity = paymentApplicationMapper.selectById(businessKey);
        if (ObjectUtil.isNotNull(entity)) {
            //如果申请通过
            if (BpmProcessInstanceResultEnum.APPROVE == statusEnums) {
                // 更新付款审批流表状态
                entity.setResult(statusEnums.getResult());
                // 算出已付金额
                List<PaymentScheduleDO> payedScheduleDOList = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>()
                        .eqIfPresent(PaymentScheduleDO::getContractId, entity.getContractId())
                        .eqIfPresent(PaymentScheduleDO::getStatus, PaymentScheduleStatusEnums.PAYED.getCode()));
                BigDecimal afterPayedAmount = payedScheduleDOList.stream()
                        .map(PaymentScheduleDO::getAmount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                afterPayedAmount = afterPayedAmount.add(entity.getCurrentPayAmount());
                entity.setAfterPayedAmount(afterPayedAmount);
                // 设置履约确认状态为待确认
                entity.setStatus(IfNumEnums.NO.getCode());
                paymentApplicationMapper.updateById(entity);

                //根据申请id找到计划
                List<PaymentScheduleDO> paymentScheduleDOList = paymentScheduleMapper.selectPlanForApplication(businessKey);

                // 推送申请通过的支付计划给 监管中心。
                List<String> planIds = paymentScheduleDOList.stream().map(PaymentScheduleDO::getId).collect(Collectors.toList());

                if (CollectionUtil.isNotEmpty(paymentScheduleDOList)) {
                    for (PaymentScheduleDO scheduleDO : paymentScheduleDOList) {
                        //支付计划：审批状态
                        scheduleDO.setApplyStatus(PaymentScheduleApplyStatusEnums.APPLY_SUCCESS.getCode());
                        scheduleDO.setStatus(PaymentScheduleStatusEnums.CONFIRM.getCode());
                    }
                    paymentScheduleMapper.updateBatch(paymentScheduleDOList);
                    //得到这批支付计划中sort最大的值
                    Optional<Integer> maxSort = paymentScheduleDOList.stream()
                            .map(PaymentScheduleDO::getSort)
                            .max(Integer::compareTo);
                    Integer defaultValue = 0;
                    //如果maxSort为空，取值为0
                    Integer maxSortValue = maxSort.orElseGet(() -> defaultValue);
                    //更新合同的支付计划sort标识
                    contractMapper.updateById(new ContractDO().setId(paymentScheduleDOList.get(0).getContractId()).setCurrentScheduleSort(Integer.valueOf(maxSortValue)));

                    //将付款申请和支付计划同步：已支付状态
                    updatePayedStatus(planIds, entity);

                }
            }

            //如果申请被退回
            if (BpmProcessInstanceResultEnum.BACK == statusEnums) {
                entity.setResult(statusEnums.getResult());
                //付款计划状态同步
                List<PaymentScheduleDO> scheduleDOList = paymentScheduleMapper.getSchedulesByAppId(businessKey);
                if (CollectionUtil.isNotEmpty(scheduleDOList)) {
                    PaymentScheduleDO paymentScheduleDO = scheduleDOList.get(0);
                    paymentScheduleDO.setStatus(PaymentScheduleStatusEnums.TO_DO.getCode());
                    paymentScheduleDO.setApplyStatus(PaymentScheduleApplyStatusEnums.DRAFT.getCode());
                    paymentScheduleMapper.updateById(paymentScheduleDO);
                }
                paymentApplicationMapper.updateById(entity);
            }

            //如果申请被发起人再次发给审批人
            if (BpmProcessInstanceResultEnum.PROCESS == statusEnums) {
                entity.setResult(statusEnums.getResult())
                        .setResult(CommonFlowableReqVOResultStatusEnums.APPROVING.getResultCode())
                        .setFlowStatus(StatusEnums.APPROVING.getCode());
                paymentApplicationMapper.updateById(entity);
            }
        }
    }

    /**
     * 同步计划和申请的状态（临时）
     * TEMP（临时采用，后期对接监管，会调整）
     */
    private void updatePayedStatus(List<String> planIds, PaymentApplicationDO entity) {
//        paymentService.pushPayInfo(planIds);
        paymentApplicationMapper.updateById(entity.setPayDate(new Date()));

    }
}
