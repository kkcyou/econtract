package com.yaoan.module.econtract.service.gpx.zcd;

import com.yaoan.module.econtract.controller.admin.gpx.vo.SupplyBankReqVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.SupplyBankRespVO;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/20 17:24
 */
public interface ZcdService {
    public SupplyBankRespVO bankAccount(SupplyBankReqVO vo) throws Exception;
}
