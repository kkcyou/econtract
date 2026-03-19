package com.yaoan.module.econtract.api.listen;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.module.econtract.api.listen.dto.FileVersionEventDTO;
import com.yaoan.module.econtract.convert.version.FileVersionConvert;
import com.yaoan.module.econtract.dal.dataobject.bpm.contract.BpmContract;
import com.yaoan.module.econtract.dal.mysql.bpm.contract.BpmContractMapper;
import com.yaoan.module.econtract.framework.core.event.version.FileVersionEvent;
import com.yaoan.module.econtract.framework.core.event.version.FileVersionEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/19 17:57
 */
@Service
public class FileVersionEventPublisherApiImpl implements FileVersionEventPublisherApi {
    @Resource
    private FileVersionEventPublisher fileVersionEventPublisher;
    @Resource
    private BpmContractMapper bpmContractMapper;

    @Override
    public void sendEvent(FileVersionEventDTO dto) {
        BpmContract bpmContract= bpmContractMapper.selectOne(BpmContract::getProcessInstanceId,dto.getProcessInstanceId());
        if(ObjectUtil.isNotNull(bpmContract)){
            FileVersionEvent event = new FileVersionEvent(this)
                    .setBusinessId(bpmContract.getContractId())
                    .setBusinessType(dto.getBusinessType()).setRemark(dto.getRemark());
            fileVersionEventPublisher.sendEvent(event);
        }

    }
}
