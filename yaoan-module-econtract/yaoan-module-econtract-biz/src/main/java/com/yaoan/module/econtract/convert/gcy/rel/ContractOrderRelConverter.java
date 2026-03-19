package com.yaoan.module.econtract.convert.gcy.rel;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/12 14:03
 */
@Mapper
public interface ContractOrderRelConverter {
    ContractOrderRelConverter INSTANCE = Mappers.getMapper(ContractOrderRelConverter.class);

}
