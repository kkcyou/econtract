package com.yaoan.module.econtract.convert.bpm.borrow;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.BorrowRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.ContractBorrowBpmPageRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord.BorrowRecordRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord.ContractBorrowRecordPageRespVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.contractborrow.ContractBorrowBpmDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ContractBorrowBpmConverter {
    ContractBorrowBpmConverter INSTANCE = Mappers.getMapper(ContractBorrowBpmConverter.class);
    PageResult<ContractBorrowBpmPageRespVO> convertPage (PageResult<ContractBorrowBpmDO> bean);
    PageResult<BorrowRecordRespVO> convertPage2 (PageResult<ContractBorrowBpmDO> bean);
    List<BorrowRecordRespVO> convertList2 (List<ContractBorrowBpmDO> bean);

    @Mapping(target = "contractName", source = "borrowName")
    ContractBorrowRecordPageRespVO convert (ContractBorrowBpmDO bean);
    BorrowRespVO convertVO (ContractBorrowBpmDO bean);
    List<ContractBorrowRecordPageRespVO> convertList (List<ContractBorrowBpmDO> bean);
}
