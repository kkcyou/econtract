package com.yaoan.module.econtract.api.bpm.borrow;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.dal.dataobject.borrow.BorrowContractDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.contractborrow.ContractBorrowBpmDO;
import com.yaoan.module.econtract.dal.mysql.borrow.BorrowContractMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.contractborrow.ContractBorrowBpmMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BorrowBpmApiImpl implements BorrowBpmApi {
    @Resource
    private ContractBorrowBpmMapper borrowBpmMapper;

    @Override
    @DataPermission(enable = false)
    public void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums) {
        ContractBorrowBpmDO entity = borrowBpmMapper.selectById(businessKey);
        if (ObjectUtil.isNotNull(entity)) {
            //更新审批时间
            entity.setApproveTime(LocalDateTime.now());

            //如果申请通过
            if (BpmProcessInstanceResultEnum.APPROVE == statusEnums) {
                // 更新条款审批流表状态
                entity.setResult(statusEnums.getResult());
                borrowBpmMapper.updateById(entity);
            }

            //如果申请被驳回
            if (BpmProcessInstanceResultEnum.BACK == statusEnums) {
                entity.setResult(statusEnums.getResult());
                borrowBpmMapper.updateById(entity);
            }

            //如果申请被发起人再次发给审批人
            if (BpmProcessInstanceResultEnum.PROCESS == statusEnums) {
                entity.setResult(statusEnums.getResult());
                borrowBpmMapper.updateById(entity);
            }

        }
    }
}
