package com.yaoan.module.econtract.api.bpm.sigent;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.bpm.signet.BpmSignetApi;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.signet.BpmContractSignetDO;
import com.yaoan.module.econtract.dal.mysql.bpm.contract.BpmContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.signet.BpmContractSignetMapper;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.framework.core.event.version.FileVersionEventPublisher;
import com.yaoan.module.infra.api.file.FileApi;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * ContractApi 实现类
 *
 * @author doujl
 */
@Slf4j
@Service
public class BpmSignetApiImpl implements BpmSignetApi {

    @Resource
    private ContractMapper contractMapper;
    @Resource
    private BpmContractSignetMapper bpmContractSignetMapper;

    @Override
    @DataPermission(enable = false)
    public void notifyBpmSignetApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums) {
        BpmContractSignetDO bpmContractSignetDO = bpmContractSignetMapper.selectById(businessKey);
        if(ObjectUtil.isNotEmpty(bpmContractSignetDO)){
            //1.更新申请表状态
            bpmContractSignetDO.setResult(statusEnums.getResult());
            bpmContractSignetMapper.updateById(bpmContractSignetDO);
            //2.更新合同表状态
            ContractDO contractDO = contractMapper.selectById(bpmContractSignetDO.getBusinessId());
            if(ObjectUtil.isNotEmpty(contractDO)){
                if (BpmProcessInstanceResultEnum.APPROVE == statusEnums) {
                    contractDO.setStatus(ContractStatusEnums.TO_BE_SIGNED.getCode());
                    contractMapper.updateById(contractDO);
                }
                if (BpmProcessInstanceResultEnum.REJECT == statusEnums) {
                    contractDO.setStatus(ContractStatusEnums.CONTRACT_AUDITSTATUS_NOT_SIGNED.getCode());
                    contractMapper.updateById(contractDO);
                }
            }
        }
    }

}
