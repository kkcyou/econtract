package com.yaoan.module.econtract.convert.contract.trading;

import com.yaoan.module.econtract.api.gcy.buyplan.dto.ContractSupplierVo;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading.TradingSupplierVO;
import com.yaoan.module.econtract.dal.dataobject.contract.trading.TradingSupplierDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface TradingSupplierConverter {
    TradingSupplierConverter INSTANCE = Mappers.getMapper(TradingSupplierConverter.class);

    List<TradingSupplierDO> convertLsit(List<TradingSupplierVO> bean);

    @Mapping(source = "tradingSupplier.countryType", target = "investmentTypeName")
    TradingSupplierDO convertDO(TradingSupplierVO tradingSupplier);

    List<TradingSupplierVO> convertLsitV1(List<TradingSupplierDO> bean);

    @Mapping(target = "countryType", source = "tradingSupplierDO.investmentTypeName")
    TradingSupplierVO convertListV1(TradingSupplierDO tradingSupplierDO);

    List<ContractSupplierVo> convertLsitV2(List<TradingSupplierDO> bean);

//    @Mapping(target = "supplierGuid", source = "bean.supplierId")
//    @Mapping(target = "supplierCode", source = "bean.supplierTaxpayerNum")
//    @Mapping(target = "bankAccount", source = "bean.bankAccount")
//    ContractSupplierVo convertV2(TradingSupplierDO bean);

    @Mapping(target = "supplierGuid", source = "bean.supplierId")
    @Mapping(target = "supplierCode", source = "bean.supplierTaxpayerNum")
    @Mapping(target = "bankAccount", source = "bean.bankAccount")
    @Mapping(target = "supplierName", source = "bean.supplierName")
    @Mapping(target = "supplierSize", source = "bean.supplierSize")
    @Mapping(target = "supplierFeatures", source = "bean.supplierFeatures")
    @Mapping(target = "supplierLocation", source = "bean.supplierLocation")
    @Mapping(target = "foreignInvestmentType", source = "bean.foreignInvestmentType")
    @Mapping(target = "countryType", source = "bean.investmentTypeName")
    @Mapping(target = "payee", ignore = true) // 需要根据业务逻辑设置
    @Mapping(target = "isBidder", constant = "0") // 默认值
    @Mapping(target = "totalMoney", ignore = true) // 需要根据业务逻辑设置
    @Mapping(target = "accountName", source = "bean.payPlanbAccount")
    @Mapping(target = "bankName", source = "bean.bankName")
    @Mapping(target = "bankNO", ignore = true) // 需要根据业务逻辑设置
    @Mapping(target = "isEntrust", constant = "0") // 默认值
    @Mapping(target = "actualAccountName", ignore = true) // 需要根据业务逻辑设置
    @Mapping(target = "actualBankName", ignore = true) // 需要根据业务逻辑设置
    @Mapping(target = "actualBankNO", ignore = true) // 需要根据业务逻辑设置
    @Mapping(target = "actualBankAccount", ignore = true) // 需要根据业务逻辑设置
    @Mapping(target = "role", source = "bean.role")
    @Mapping(target = "payAmount", source = "bean.payAmount")
    ContractSupplierVo convertV2(TradingSupplierDO bean);
}
