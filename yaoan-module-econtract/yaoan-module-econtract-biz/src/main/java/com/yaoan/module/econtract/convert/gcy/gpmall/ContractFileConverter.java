package com.yaoan.module.econtract.convert.gcy.gpmall;

import com.yaoan.module.econtract.api.contract.dto.ContractFileDTO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.ContractFileDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/12/2 17:03
 */
@Mapper
public interface ContractFileConverter {
    ContractFileConverter INSTANCE = Mappers.getMapper(ContractFileConverter.class);

    @Mapping(source = "filePath", target = "fileUrl")
    ContractFileDO toDO(ContractFileDTO contractFileDTO);

    List<ContractFileDO> toDOS(List<ContractFileDTO> contractFileDTOS);


    List<ContractFileDTO> toDTOS(List<ContractFileDO> contractFileDOList);
}
