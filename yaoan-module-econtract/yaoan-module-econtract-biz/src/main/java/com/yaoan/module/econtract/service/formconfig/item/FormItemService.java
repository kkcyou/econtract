package com.yaoan.module.econtract.service.formconfig.item;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormListRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormOneRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormSaveReqVO;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.formconfig.item.vo.FormItemListRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.item.vo.FormItemOneRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.item.vo.FormItemSaveReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/20 17:31
 */
public interface FormItemService {


    String save(FormItemSaveReqVO vo);

    FormItemOneRespVO getOne(IdReqVO vo);

    String update(FormItemSaveReqVO vo);

    String deleteBatch(IdReqVO vo);

    PageResult<FormItemListRespVO> list(FormSaveReqVO vo);

    List<FormItemListRespVO> getItemsByFormId(IdReqVO vo);
}
