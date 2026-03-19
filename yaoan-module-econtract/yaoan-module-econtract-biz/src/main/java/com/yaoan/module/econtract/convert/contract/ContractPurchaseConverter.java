package com.yaoan.module.econtract.convert.contract;

import com.yaoan.module.econtract.controller.admin.contract.vo.ContractPurchaseReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractPurchaseDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ContractPurchaseConverter {
    ContractPurchaseConverter INSTANCE = Mappers.getMapper(ContractPurchaseConverter.class);
    ContractPurchaseDO convert(ContractPurchaseReqVO contractPurchaseReqVO);
    List<ContractPurchaseReqVO> toContractPurchaseReqVO(List<ContractPurchaseDO> contractPurchaseReqDOS);
}
