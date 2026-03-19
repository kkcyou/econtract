package com.yaoan.module.econtract.convert.formconfig;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormListRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormOneRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormSaveReqVO;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormUpdateReqVO;
import com.yaoan.module.econtract.dal.dataobject.formconfig.FormDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/21 14:33
 */
@Mapper
public interface FormConverter {
    FormConverter INSTANCE = Mappers.getMapper(FormConverter.class);


    FormDO saveReq2Entity(FormSaveReqVO vo);

    FormDO updateReq2Entity(FormUpdateReqVO vo);

    FormOneRespVO entity2OneRespVO(FormDO formDO);

    PageResult<FormListRespVO> pageEntity2Resp(PageResult<FormDO> doPageResult);

    List<FormOneRespVO> listEntity2Resp(List<FormDO> formDoList);
}
