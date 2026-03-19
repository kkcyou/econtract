package com.yaoan.module.econtract.convert.contract.trading;

import com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading.PurchaseVO;
import com.yaoan.module.econtract.dal.dataobject.contract.trading.PurchaseDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.PackageDetailInfoDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PurchaseConverter {
    PurchaseConverter INSTANCE = Mappers.getMapper(PurchaseConverter.class);

    PurchaseDO convert(PurchaseVO purchaseVO);

    PurchaseVO convert(PurchaseDO purchaseDO);

    List<PurchaseDO> convertListV1(List<PurchaseVO> purchaseVOList);

    List<PurchaseVO> convertListV2(List<PurchaseDO> purchaseVOList);

    List<PurchaseVO> convertList(List<PackageDetailInfoDO> packageDetailInfoDOList);
}
