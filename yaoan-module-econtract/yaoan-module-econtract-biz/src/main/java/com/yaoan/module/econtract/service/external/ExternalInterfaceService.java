package com.yaoan.module.econtract.service.external;

import com.yaoan.module.econtract.api.gcy.buyplan.dto.EncryptDTO;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.BpmContractCreateReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.DraftOrderInfoDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.PackageInfoDO;

/**
 * 对外接口
 *
 * @author zhc
 * @since 2024-04-24
 */
public interface ExternalInterfaceService {
    /**
     * 接收订单信息接口
     */
    EncryptDTO saveOrderInfo(EncryptDTO encryptDTO);
    /**
     * 接收订单信息接口
     */
    EncryptDTO saveOrderInfo4Temp(EncryptDTO encryptDTO);

    void checkSignGetClass(EncryptDTO encryptDTO);


    EncryptDTO saveGPMallOrderInfo(EncryptDTO encryptDTO);

    EncryptDTO updateOrderStatus(EncryptDTO encryptDTO);

    EncryptDTO updateStatus(DraftOrderInfoDO draftOrderInfoDO);

    EncryptDTO updateStatusPackage(PackageInfoDO packageInfoDO);

    String getContractSignInfo(ContractOrderExtDO encryptDTO) throws  Exception;

    /**
     * 供应商签章后退回，同步状态
     * @param encryptDTO
     * @return
     */
    String getContractBack(BpmContractCreateReqVO encryptDTO);
}
