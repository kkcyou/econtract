package com.yaoan.module.econtract.convert.demo;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.api.demo.dto.DemoRespDTO;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoCreateReqVO;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoRespVO;
import com.yaoan.module.econtract.controller.admin.demo.vo.DemoUpdateReqVO;
import com.yaoan.module.econtract.dal.dataobject.demo.EcmsDemo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


/**
 * @Author：doujl
 * @Description:  转换类
 * @Date: 2023年07月05日14:30:38
 */
@Mapper
public interface DemoConverter {

    DemoConverter INSTANCE = Mappers.getMapper(DemoConverter.class);

    EcmsDemo convert(DemoCreateReqVO bean);

    EcmsDemo convert(DemoUpdateReqVO bean);

    DemoRespDTO convert03(EcmsDemo bean);

    PageResult<DemoRespVO> convertPage(PageResult<EcmsDemo> page);


}
