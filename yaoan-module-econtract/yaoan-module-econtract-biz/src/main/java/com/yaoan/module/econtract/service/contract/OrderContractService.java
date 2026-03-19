package com.yaoan.module.econtract.service.contract;


import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.save.OrderContractCreateReqVO;

public interface OrderContractService {

    String createContract(OrderContractCreateReqVO contractCreateReqVO) throws Exception;
}
