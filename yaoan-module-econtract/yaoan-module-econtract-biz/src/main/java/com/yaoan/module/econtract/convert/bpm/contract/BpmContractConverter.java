package com.yaoan.module.econtract.convert.bpm.contract;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.BpmContractRespVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.contract.BpmContract;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * @Author：doujl
 * @Description:  转换类
 * @Date: 2023年07月05日14:30:38
 */
@Mapper
public interface BpmContractConverter {

    BpmContractConverter INSTANCE = Mappers.getMapper(BpmContractConverter.class);

    PageResult<BpmContractRespVO> convertPage(PageResult<BpmContract> page);


}
