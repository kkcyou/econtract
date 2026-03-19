package com.yaoan.module.econtract.convert.contractaidraftrecord;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contractdraft.vo.ContractAiDraftRecordCreateReqVO;
import com.yaoan.module.econtract.controller.admin.contractdraft.vo.ContractAiDraftRecordRespVO;
import com.yaoan.module.econtract.controller.admin.contractdraft.vo.ContractTypeShort;
import com.yaoan.module.econtract.dal.dataobject.contractaidraftrecord.ContractAiDraftRecordDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 合同智能起草记录 Convert
 *
 * @author doujiale
 */
@Mapper
public interface ContractAiDraftRecordConvert {

    ContractAiDraftRecordConvert INSTANCE = Mappers.getMapper(ContractAiDraftRecordConvert.class);

    ContractAiDraftRecordDO convert(ContractAiDraftRecordCreateReqVO bean);

    ContractAiDraftRecordRespVO convert(ContractAiDraftRecordDO bean);

    List<ContractAiDraftRecordRespVO> convertList(List<ContractAiDraftRecordDO> list);

    PageResult<ContractAiDraftRecordRespVO> convertPage(PageResult<ContractAiDraftRecordDO> page);


    List<ContractTypeShort> convertList2(List<ContractType> contractTypes);
}
