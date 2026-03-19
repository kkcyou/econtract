package com.yaoan.module.econtract.convert.contractaidraftshow;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo.ContractAiDraftShowCreateReqVO;
import com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo.ContractAiDraftShowExcelVO;
import com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo.ContractAiDraftShowRespVO;
import com.yaoan.module.econtract.controller.admin.contractaidraftshow.vo.ContractAiDraftShowUpdateReqVO;
import com.yaoan.module.econtract.dal.dataobject.contractaidraftshow.ContractAiDraftShowDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 合同模板推荐 Convert
 *
 * @author doujiale
 */
@Mapper
public interface ContractAiDraftShowConvert {

    ContractAiDraftShowConvert INSTANCE = Mappers.getMapper(ContractAiDraftShowConvert.class);

    ContractAiDraftShowDO convert(ContractAiDraftShowCreateReqVO bean);

    ContractAiDraftShowDO convert(ContractAiDraftShowUpdateReqVO bean);

    ContractAiDraftShowRespVO convert(ContractAiDraftShowDO bean);

    List<ContractAiDraftShowRespVO> convertList(List<ContractAiDraftShowDO> list);

    PageResult<ContractAiDraftShowRespVO> convertPage(PageResult<ContractAiDraftShowDO> page);

    List<ContractAiDraftShowExcelVO> convertList02(List<ContractAiDraftShowDO> list);

}
