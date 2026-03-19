package com.yaoan.module.econtract.convert.gcy.gpmall;

import com.yaoan.module.econtract.api.gcy.buyplan.dto.ContractSupplierVo;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;



/**
 * @description:合同关联的供应商
 * @author: zhc
 * @date: 2024-01-23 14:13
 */
@Mapper
public interface ContractSupplierConverter {
    ContractSupplierConverter INSTANCE = Mappers.getMapper(ContractSupplierConverter.class);




//    @Mapping(target = "supplierGuid", source = "supplier.supplierGuid")
//    @Mapping(target = "supplierCode", source = "supplier.supplierCode")
//    @Mapping(target = "supplierName", source = "supplier.supplierName")
//    @Mapping(target = "supplierSize", source = "account.supplierSize")
//    @Mapping(target = "supplierFeatures", source = "account.supplierFeatures")
//    @Mapping(target = "supplierLocation", source = "account.supplierLocation")
//    @Mapping(target = "foreignInvestmentType", source = "account.foreignInvestmentType")
//    @Mapping(target = "totalMoney", source = "account.supplierPayeeMoney")
//    @Mapping(target = "accountName", source = "account.payeeAccountName")
//    @Mapping(target = "bankName", source = "account.bankName")
//    @Mapping(target = "bankAccount", source = "account.bankAccount")
//    @Mapping(target = "isEntrust", source = "account.isUseAgencyAccount")
//    @Mapping(target = "actualAccountName", source = "account.actualPayeeAccountName")
//    @Mapping(target = "actualBankName", source = "account.actualBankName")
//    @Mapping(target = "actualBankAccount", source = "account.actualBankAccount")
//    @Mapping(target = "countryType", source = "account.countryType")
//    ContractSupplierVo convert(ContractSupplierDO supplier, ContractPayeeAccountDO account);


    @Mapping(target = "supplierGuid", source = "supplierId")
    @Mapping(target = "accountName", source = "supplierBankAccountName")
    ContractSupplierVo convert(ContractOrderExtDO supplier);
}
