package com.yaoan.module.econtract.convert.modelcategory;

import com.yaoan.module.econtract.api.modelcategory.dto.ClientModelCategoryDTO;
import com.yaoan.module.econtract.dal.dataobject.modelcategory.ClientModelCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @description:
 * @author: zhc
 * @date: 2024/03-05 16:10
 */
@Mapper
public interface ClientModelCategoryConverter {
    ClientModelCategoryConverter INSTANCE = Mappers.getMapper(ClientModelCategoryConverter.class);

    ClientModelCategory toEntity(ClientModelCategoryDTO clientModelCategoryDTO);

}
