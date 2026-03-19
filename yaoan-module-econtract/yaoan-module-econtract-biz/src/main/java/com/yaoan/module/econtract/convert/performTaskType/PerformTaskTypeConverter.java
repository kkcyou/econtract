package com.yaoan.module.econtract.convert.performTaskType;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoRespVO;
import com.yaoan.module.econtract.controller.admin.performtasktype.vo.PerformTaskTypeAllVO;
import com.yaoan.module.econtract.controller.admin.performtasktype.vo.PerformTaskTypeCreateReqVO;
import com.yaoan.module.econtract.controller.admin.performtasktype.vo.PerformTaskTypeRespVO;
import com.yaoan.module.econtract.controller.admin.performtasktype.vo.PerformTaskTypeUpdateReqVO;
import com.yaoan.module.econtract.dal.dataobject.performtasktype.PerformTaskTypeDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/3 15:32
 */
@Mapper
public interface PerformTaskTypeConverter {
    PerformTaskTypeConverter INSTANCE = Mappers.getMapper(PerformTaskTypeConverter.class);

    PageResult<DemoRespVO> convertPage(PageResult<PerformTaskTypeRespVO> performTaskTypePage);


    PerformTaskTypeDO convert2DO(PerformTaskTypeCreateReqVO reqVO);

    PerformTaskTypeDO convert2DO(PerformTaskTypeUpdateReqVO reqVO);



    List<PerformTaskTypeAllVO> convert2AllVO(List<PerformTaskTypeDO> vos);


    PageResult<PerformTaskTypeRespVO> convert(PageResult<PerformTaskTypeDO> taskTypeDOPageResult);

    PageResult<PerformTaskTypeRespVO> convert2R(PageResult<PerformTaskTypeDO> doPageResult);
}
