package com.yaoan.module.econtract.convert.formconfig;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/21 11:19
 */
@Mapper
public interface FormRelConverter {
    FormRelConverter INSTANCE = Mappers.getMapper(FormRelConverter.class);

}
