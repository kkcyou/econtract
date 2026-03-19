package com.yaoan.module.econtract.service.design;

import com.yaoan.module.econtract.controller.admin.design.vo.ContractInfoVO;
import org.springframework.stereotype.Service;

/**
 * @author doujiale
 */
@Service
public class ContractBuilderImpl implements ContractBuilder {



    private final ThreadLocal<ContractInfoVO> contractInfo = ThreadLocal.withInitial(
            () -> ContractInfoVO.builder().build());

    @Override
    public ContractBuilder buildBaseInfo(String contractId) {

        return this;
    }

    @Override
    public ContractBuilder buildRelationInfo(String contractId) {

        return this;
    }

    @Override
    public ContractBuilder buildAttachmentInfo(String contractId) {

        return this;
    }

    @Override
    public ContractInfoVO build() {
        return contractInfo.get();
    }

    @Override
    public void remove() {
        contractInfo.remove();
    }
}
