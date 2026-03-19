package com.yaoan.module.econtract.convert.hljsupply;

import com.yaoan.module.econtract.api.contract.SupplyFromHLJDTO;
import com.yaoan.module.econtract.convert.freezed.FreezedContractConvert;
import com.yaoan.module.system.api.user.dto.SupplyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2025/3/8 13:51
 */
@Mapper
public interface HljSupplyConverter {
    HljSupplyConverter INSTANCE = Mappers.getMapper(HljSupplyConverter.class);

    SupplyDTO convertSingleton(SupplyFromHLJDTO supplyFromHLJ);

    List<SupplyDTO> convertList(List<SupplyFromHLJDTO> supplyFromHLJ);
}
