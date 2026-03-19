package com.yaoan.module.econtract.convert.formconfig;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormShowItemRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.item.vo.FormItemListRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.item.vo.FormItemOneRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.item.vo.FormItemSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.formconfig.FormItemDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/21 11:19
 */
@Mapper
public interface FormItemConverter {
    FormItemConverter INSTANCE = Mappers.getMapper(FormItemConverter.class);

    FormItemDO req2Entity(FormItemSaveReqVO vo);

    FormItemOneRespVO entity2Resp(FormItemDO formItemDO);

    FormItemListRespVO do2Resp(FormItemDO entity);

    List<FormItemListRespVO> listDO2Resp(List<FormItemDO> entityList);

    PageResult<FormItemListRespVO> pageEntity2Resp(PageResult<FormItemDO> formItemDOPage);


    List<FormShowItemRespVO> listEntity2ShowResp(List<FormItemDO> items);

    FormShowItemRespVO entity2ShowResp(FormItemDO entity);
}
