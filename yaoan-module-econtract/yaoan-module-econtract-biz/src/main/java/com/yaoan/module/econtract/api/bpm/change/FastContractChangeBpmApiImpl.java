package com.yaoan.module.econtract.api.bpm.change;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangeDO;
import com.yaoan.module.econtract.dal.mysql.change.BpmContractChangeMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: 暂时
 * 所有付款计划/收款计划状态更改为“已冻结”；
 * 变动流程解除申请通过：合同相关计划都变成“已关闭”
 * @author: Pele
 * @date: 2024/1/29 16:41
 */
@Slf4j
@Service
public class FastContractChangeBpmApiImpl implements FastContractChangeBpmApi {

    @Resource
    private BpmContractChangeMapper bpmContractChangeMapper;

    @Override
    public void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums) {
        BpmContractChangeDO bpmContractChangeDO = bpmContractChangeMapper.selectById(businessKey);

        if (ObjectUtil.isNotNull(bpmContractChangeDO)) {
            bpmContractChangeDO.setResult(statusEnums.getResult());
        }
        bpmContractChangeMapper.updateById(bpmContractChangeDO);


    }
}
