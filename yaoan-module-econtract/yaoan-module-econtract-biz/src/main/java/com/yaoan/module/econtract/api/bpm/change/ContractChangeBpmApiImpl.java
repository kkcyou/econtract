package com.yaoan.module.econtract.api.bpm.change;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.dal.dataobject.acceptance.AcceptanceDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangeDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.mysql.acceptance.AcceptanceMapper;
import com.yaoan.module.econtract.dal.mysql.change.BpmContractChangeMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.enums.change.ContractChangeTypeEnums;
import com.yaoan.module.econtract.enums.change.IsAcceptanceEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ContractStatusEnums.CONTRACT_CLOSING;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/24 18:12
 */
@Slf4j
@Service
public class ContractChangeBpmApiImpl implements ContractChangeBpmApi {
    @Resource
    private BpmContractChangeMapper bpmContractChangeMapper;
    @Resource
    private ContractMapper contractMapper;
    @Autowired
    private PaymentScheduleMapper paymentScheduleMapper;

    @Override
    public void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums) {
        /** 正常逻辑 */
        ContractDO contractDO = contractMapper.selectById(businessKey);

        if (ObjectUtil.isNotNull(contractDO)) {
            BpmContractChangeDO bpmContractChangeDO = bpmContractChangeMapper.selectOne(new LambdaQueryWrapperX<BpmContractChangeDO>()
                    .eq(BpmContractChangeDO::getContractId, contractDO.getId()));
            if (ObjectUtil.isNotNull(bpmContractChangeDO)) {
                bpmContractChangeDO.setResult(statusEnums.getResult());
                if (ObjectUtil.isNotNull(contractDO)) {
                    //被退回
                    if (BpmProcessInstanceResultEnum.BACK == statusEnums) {
                        contractDO.setStatus(ContractStatusEnums.APPROVE_BACK.getCode());
                    }
                    //再次发起
                    if (BpmProcessInstanceResultEnum.PROCESS == statusEnums) {
                        contractDO.setStatus(ContractStatusEnums.CHECKING.getCode());
                    }
                    //审批通过
                    if (BpmProcessInstanceResultEnum.APPROVE == statusEnums) {
                        contractDO.setStatus(ContractStatusEnums.TO_BE_SENT.getCode());
                    }
                    contractMapper.updateById(contractDO);
                }
                bpmContractChangeMapper.updateById(bpmContractChangeDO);
            }
        }
    }


    /**
     * 合同更改为 “解除中”
     * 所有付款计划/收款计划状态更改为“已冻结”；
     */
    @Override
    public void submit(String businessKey, BpmProcessInstanceResultEnum resultEnum) {
        BpmContractChangeDO bpmContractChangeDO = bpmContractChangeMapper.selectById(businessKey);
        if (ObjectUtil.isNull(bpmContractChangeDO)) {
            throw exception(DIY_ERROR, "数据异常，请联系管理员。");
        }
        ContractDO contractDO = contractMapper.selectById(bpmContractChangeDO.getMainContractId());
        Integer changeType = bpmContractChangeDO.getChangeType();
        ContractChangeTypeEnums changeTypeEnums = ContractChangeTypeEnums.getInstance(changeType);
        if (Objects.isNull(changeTypeEnums)) {
            throw exception(DIY_ERROR, "数据异常，请联系管理员");
        }
        switch (changeTypeEnums) {
            //解除
            case TERMINATE:
                handleTerminate(contractDO);
                break;

            case CHANGE:
                // 待补充……
                break;
            default:
                break;


        }


    }
@Resource
private AcceptanceMapper acceptanceMapper;
    private void handleTerminate(ContractDO contractDO) {
        //草稿提交
        //合同状态 =>解除中
        List<PaymentScheduleDO> scheduleDOList = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>()
                .eq(PaymentScheduleDO::getContractId, contractDO.getId()));


        if (CollectionUtil.isNotEmpty(scheduleDOList)) {
            scheduleDOList.forEach(scheduleDO -> {
                if(PaymentScheduleStatusEnums.TO_DO.getCode().equals(scheduleDO.getStatus())){
                    //待执行的计划状态 =>已冻结
                    scheduleDO.setStatus(PaymentScheduleStatusEnums.FROZEN.getCode());

                    //计划验收状态从 待验收 变成 已关闭验收
                    scheduleDO.setIsAcceptance(IsAcceptanceEnums.CLOSED.getCode());
                }
            });
            paymentScheduleMapper.updateBatch(scheduleDOList);


        }

        contractDO.setOldStatus(contractDO.getStatus());
        contractDO.setStatus(CONTRACT_CLOSING.getCode());
        contractMapper.updateById(contractDO);

    }
}
