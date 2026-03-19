package com.yaoan.module.econtract.convert.param;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.param.vo.ParamByTermRespVO;
import com.yaoan.module.econtract.controller.admin.param.vo.ParamReqVO;
import com.yaoan.module.econtract.controller.admin.param.vo.ParamRespVO;
import com.yaoan.module.econtract.controller.admin.term.vo.termparam.TermParamRespVO;
import com.yaoan.module.econtract.dal.dataobject.param.Param;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @Author：doujl
 * @Description:  转换类
 * @Date: 2023年07月05日14:30:38
 */
@Mapper(componentModel = "spring")
public interface ParamConverter {

    ParamConverter INSTANCE = Mappers.getMapper(ParamConverter.class);
    Param toEntity(ParamReqVO bean);



    ParamRespVO toVO(Param param);

    PageResult<ParamRespVO> convertPage(PageResult<Param> page);
    List<TermParamRespVO> toList(List<Param> termParams);

    ParamByTermRespVO do2Resp(Param param);
}
