package com.yaoan.module.econtract.api.bpm.register;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.dal.dataobject.bpm.register.BpmContractRegisterDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.mysql.bpm.register.BpmContractRegisterMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.enums.ContractUploadTypeEnums;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: 合同登记 结束节点监听器
 * @author: Pele
 * @date: 2024/1/26 11:28
 */
@Slf4j
@Service
public class ContractRegisterBpmApiImpl implements ContractRegisterBpmApi {
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private BpmContractRegisterMapper bpmContractRegisterMapper;

    @Override
    public void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums) {

        ContractDO contractDO = contractMapper.selectById(businessKey);

        if (ObjectUtil.isNotNull(contractDO)) {
            BpmContractRegisterDO bpmDO = bpmContractRegisterMapper.selectOne(new LambdaQueryWrapperX<BpmContractRegisterDO>()
                    .eq(BpmContractRegisterDO::getContractId, contractDO.getId()));
            ContractUploadTypeEnums uploadTypeEnums = ContractUploadTypeEnums.getInstance(contractDO.getUpload());
            if (ObjectUtil.isNotNull(bpmDO)) {
                bpmDO.setResult(statusEnums.getResult());
                if (ObjectUtil.isNotNull(contractDO)) {
//                    //被退回
//                    if (BpmProcessInstanceResultEnum.BACK == statusEnums) {
//                        contractDO.setStatus(ContractStatusEnums.APPROVE_BACK.getCode());
//                    }
//                    //再次发起
//                    if (BpmProcessInstanceResultEnum.PROCESS == statusEnums) {
//                        contractDO.setStatus(ContractStatusEnums.CHECKING.getCode());
//                    }
                    //审批通过
                    if (BpmProcessInstanceResultEnum.APPROVE == statusEnums) {
                        //目前逻辑: 审批通过直接变成 签署完成状态
                        contractDO.setStatus(ContractStatusEnums.SIGN_COMPLETED.getCode());
                        contractDO.setIsSign(IfNumEnums.YES.getCode());
                        contractDO.setDocument(0);//jie追加逻辑
                    }
                    //合同登记（补录合同）：撤回后，合同状态=待送审，Bpm流程数据删除。
                    if (ContractUploadTypeEnums.REGISTER == uploadTypeEnums && BpmProcessInstanceResultEnum.CANCEL == statusEnums) {
                        //目前逻辑: 审批通过直接变成 签署完成状态
                        contractDO.setStatus(ContractStatusEnums.TO_BE_CHECK.getCode());
                        bpmContractRegisterMapper.deleteById(bpmDO);
                    }

                    contractMapper.updateById(contractDO);
                }
                // 不是撤回，就可以更新状。撤回操作会删除流程。
                if (BpmProcessInstanceResultEnum.CANCEL!=statusEnums){
                    bpmContractRegisterMapper.updateById(bpmDO);
                }

            }
        }
    }
}
