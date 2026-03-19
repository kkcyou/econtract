package com.yaoan.module.econtract.convert.formconfig;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.FormReqVO;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.FormRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.formbusiness.FormBusinessListReqVO;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.formbusiness.FormBusinessListRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.formbusiness.FormBusinessSaveReqVO;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.item.FormItemReqVO;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.item.FormItemRespVO;
import com.yaoan.module.econtract.dal.dataobject.formconfig.FormBusinessDO;
import com.yaoan.module.econtract.dal.dataobject.formconfig.FormBusinessRelDO;
import com.yaoan.module.econtract.dal.dataobject.formconfig.FormDO;
import com.yaoan.module.econtract.dal.dataobject.formconfig.FormItemDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/18 19:41
 */
@Mapper
public interface FormConfigConverter {
    FormConfigConverter INSTANCE = Mappers.getMapper(FormConfigConverter.class);


    FormBusinessDO bizReq2DO(FormBusinessSaveReqVO vo);

    FormDO formReq2DO(FormReqVO vo);

    List<FormItemDO> listItemReq2DO(List<FormItemReqVO> list);

    FormItemDO convertItem(FormItemReqVO formItemReqVO);

    List<FormBusinessRelDO> listRelReq2DO(List<FormReqVO> list);

    FormBusinessRelDO relReq2DO (FormReqVO vo);

    
    List<FormBusinessListRespVO> pageBizDO2Resp(List<FormBusinessDO> list);

    FormBusinessListRespVO convertBiz(FormBusinessDO entity);

    List<FormItemRespVO> listItemDO2Resp(List<FormItemDO> itemDOList);

    List<FormRespVO> listFormDO2Resp(List<FormDO> formDOList);

    PageResult<FormBusinessListRespVO> bizPageDo2Resp(PageResult<FormBusinessDO> doPageResult);
}
