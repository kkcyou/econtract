package com.yaoan.module.econtract.service.hljsupply;

import com.yaoan.module.econtract.api.contract.SupplyFromHLJDTO;
import com.yaoan.module.system.api.user.dto.SupplyDTO;

import java.util.List;

/**
 * 供应商 用户  接口
 *
 * @author 芋道源码
 */
public interface HLJSupplyService {
    SupplyFromHLJDTO getSupplyFromHLJ(String id);
    List<SupplyFromHLJDTO> getSupplyFromHLJ(List<String> ids);

    SupplyDTO getSupply(String id);
    List<SupplyDTO> getSupplyList(List<String> ids);
}
