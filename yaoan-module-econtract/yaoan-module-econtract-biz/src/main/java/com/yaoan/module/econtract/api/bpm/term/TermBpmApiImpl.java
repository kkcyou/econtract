package com.yaoan.module.econtract.api.bpm.term;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import com.yaoan.module.econtract.dal.mysql.term.TermMapper;
import com.yaoan.module.econtract.dal.mysql.term.TermRepository;
import com.yaoan.module.econtract.enums.term.TermStatusEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/10 11:17
 */
@Service
public class TermBpmApiImpl implements TermBpmApi {
    @Resource
    private TermMapper termMapper;
    @Autowired
    private TermRepository termRepository;
    @Override
    @DataPermission(enable = false)
    public void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums) {
        Term entity = termMapper.selectById(businessKey);
        if (ObjectUtil.isNotNull(entity)) {
            //如果申请通过
            if (BpmProcessInstanceResultEnum.APPROVE == statusEnums) {
                //更新审批时间
                entity.setApproveTime(LocalDateTime.now());
                entity.setStatus(TermStatusEnums.YES.getCode());
                // 更新条款审批流表状态
                entity.setResult(statusEnums.getResult());
                termMapper.updateById(entity);
                termRepository.saveSimple(entity);
            }

            //如果申请被驳回
            if (BpmProcessInstanceResultEnum.BACK == statusEnums) {
                entity.setResult(statusEnums.getResult());
                termMapper.updateById(entity);
            }

            //如果申请被发起人再次发给审批人
            if (BpmProcessInstanceResultEnum.PROCESS == statusEnums) {
                entity.setResult(statusEnums.getResult());
                termMapper.updateById(entity);
            }
        }
    }
}
