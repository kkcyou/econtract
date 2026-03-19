package com.yaoan.module.econtract.convert.contractversion;

import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.ContractVersionListRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.ContractVersionSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.version.ContractVersionDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/29 11:35
 */
@Mapper
public interface ContractVersionConverter {

    ContractVersionConverter INSTANCE = Mappers.getMapper(ContractVersionConverter.class);


    ContractVersionDO req2Entity(ContractVersionSaveReqVO vo);

    List<ContractVersionListRespVO> listEntity2Resp(List<ContractVersionDO> doList);

    ContractVersionListRespVO do2Resp(ContractVersionDO entity);
}
