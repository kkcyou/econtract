package com.yaoan.module.econtract.api.bpm.archive;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.dal.dataobject.bpm.archive.BpmContractArchivesDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contractarchives.ContractArchivesDO;
import com.yaoan.module.econtract.dal.mysql.bpm.archive.BpmContractArchivesMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contractarchives.ContractArchivesMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class ArchiveBpmApiImpl implements ArchiveBpmApi{
    @Resource
    private BpmContractArchivesMapper bpmContractArchivesMapper;

    @Resource
    private ContractArchivesMapper contractArchivesMapper;
    @Resource
    private ContractMapper contractMapper;
    @Override
    public void notifyApproveStatus(String businessKey, BpmProcessInstanceResultEnum statusEnums) {
        BpmContractArchivesDO bpmContractArchivesDO = bpmContractArchivesMapper.selectById(businessKey);
        if (ObjectUtil.isNotEmpty(bpmContractArchivesDO)){
            //修改工作流表状态
            bpmContractArchivesMapper.updateById(bpmContractArchivesDO.setResult(statusEnums.getResult()));
            //归档表修改状态记录归档时间
            ContractArchivesDO contractArchivesDO = contractArchivesMapper.selectOne(new LambdaQueryWrapperX<ContractArchivesDO>()
                    .eq(ContractArchivesDO::getId, bpmContractArchivesDO.getArchiveId()));
            if(ObjectUtil.isNotEmpty(contractArchivesDO)){
                contractArchivesDO.setStatus(1);
                if(bpmContractArchivesDO.getType() == 0){
                    contractArchivesDO.setArchiveTime(LocalDateTime.now());
                }
                contractArchivesMapper.updateById(contractArchivesDO);
                //修改合同归档状态
                contractMapper.updateById(new ContractDO().setId(contractArchivesDO.getContractId()).setDocument(1));
            }
        }
    }
}
