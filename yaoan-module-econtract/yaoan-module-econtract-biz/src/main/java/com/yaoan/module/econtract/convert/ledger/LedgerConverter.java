package com.yaoan.module.econtract.convert.ledger;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.api.ledger.dto.LedgerDTO;
import com.yaoan.module.econtract.controller.admin.ledger.vo.LedgerPageRespVo;
import com.yaoan.module.econtract.dal.dataobject.contract.SimpleContractDO;
import com.yaoan.module.econtract.dal.dataobject.ledger.Ledger;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/6 23:33
 */
@Mapper
public interface LedgerConverter {
    LedgerConverter INSTANCE = Mappers.getMapper(LedgerConverter.class);

    Ledger toEntity(LedgerDTO LedgerDTO);

    LedgerDTO toDTO(Ledger Ledger);

    List<LedgerDTO> toOutputList(List<Ledger> demos);

    List<Ledger> toOutputLedgers(List<LedgerDTO> demos);

    PageResult<LedgerPageRespVo> convertPage(PageResult<Ledger> page);

    List<LedgerPageRespVo> convert2RespVo(List<Ledger> ledgerList);

    PageResult<LedgerPageRespVo> convertRespPage(PageResult<SimpleContractDO> contractDOPage);

    List<LedgerPageRespVo> cRespVOList(List<SimpleContractDO> list);

    LedgerPageRespVo cRespVO(SimpleContractDO i);

}
