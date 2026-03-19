package com.yaoan.module.econtract.service.design;

import com.yaoan.module.econtract.controller.admin.design.vo.ContractInfoVO;
import com.yaoan.module.econtract.framework.core.event.EcmsContractResultEvent;
import com.yaoan.module.econtract.framework.core.event.EcmsContractResultEventPublisher;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * demo 服务实现类
 * </p>
 *
 * @author doujl
 * @since 2023-07-04
 */
@Service
public class DesignServiceImpl implements DesignService {

    @Resource
    private ContractBuilder contractBuilder;
    @Resource
    private EcmsContractResultEventPublisher publisher;

    @Override
    public ContractInfoVO getContractDetailById(String contractId) {

        ContractInfoVO build;
        try {
            build = contractBuilder.buildBaseInfo(contractId)
                    .buildRelationInfo(contractId)
                    .buildAttachmentInfo(contractId)
                    .build();
        } finally {
            contractBuilder.remove();
        }

        return build;
    }

    @Override
    public void touchContract(String id) {

        publisher.sendContractResultEvent(new EcmsContractResultEvent(this).setResult(1).setId(""));
    }
}