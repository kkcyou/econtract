package com.yaoan.module.econtract.convert.outwardcontract;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.econtract.controller.admin.contract.outwardcontract.vo.OutwardContractCreateReqVO;
import com.yaoan.module.econtract.controller.admin.contract.outwardcontract.vo.OutwardContractExcelVO;
import com.yaoan.module.econtract.controller.admin.contract.outwardcontract.vo.OutwardContractRespVO;
import com.yaoan.module.econtract.controller.admin.contract.outwardcontract.vo.OutwardContractUpdateReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.yaoan.module.econtract.dal.dataobject.outwardcontract.OutwardContractDO;

/**
 * 对外合同 Convert
 *
 * @author Pele
 */
@Mapper
public interface OutwardContractConvert {

    OutwardContractConvert INSTANCE = Mappers.getMapper(OutwardContractConvert.class);

    OutwardContractDO convert(OutwardContractCreateReqVO bean);

    OutwardContractDO convert(OutwardContractUpdateReqVO bean);

    OutwardContractRespVO convert(OutwardContractDO bean);

    List<OutwardContractRespVO> convertList(List<OutwardContractDO> list);

    PageResult<OutwardContractRespVO> convertPage(PageResult<OutwardContractDO> page);

    List<OutwardContractExcelVO> convertList02(List<OutwardContractDO> list);

}
