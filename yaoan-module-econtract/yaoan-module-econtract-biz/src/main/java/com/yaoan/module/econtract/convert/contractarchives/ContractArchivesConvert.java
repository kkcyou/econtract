package com.yaoan.module.econtract.convert.contractarchives;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.archive.vo.PageRespVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.archive.BpmContractArchivesDO;
import com.yaoan.module.econtract.dal.dataobject.contractarchives.ContractArchivesDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ContractArchivesConvert {
    ContractArchivesConvert INSTANCE = Mappers.getMapper(ContractArchivesConvert.class);

    PageResult<PageRespVO> convertPage (PageResult<BpmContractArchivesDO> bean);
    List<PageRespVO> convertList (List<BpmContractArchivesDO> bean);
}
