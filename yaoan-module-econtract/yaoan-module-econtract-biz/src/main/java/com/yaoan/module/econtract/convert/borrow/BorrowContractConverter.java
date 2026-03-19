package com.yaoan.module.econtract.convert.borrow;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord.BorrowRecordRespVO;
import com.yaoan.module.econtract.dal.dataobject.borrow.BorrowContractDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BorrowContractConverter {
    BorrowContractConverter INSTANCE = Mappers.getMapper(BorrowContractConverter.class);

    List<BorrowRecordRespVO> convertList(List<BorrowContractDO> bean);
    PageResult<BorrowRecordRespVO> convertPage(PageResult<BorrowContractDO> bean);
}
