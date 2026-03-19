package com.yaoan.module.econtract.service.formconfig.form;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormListRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormOneRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormSaveReqVO;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/20 17:29
 */
public interface ECMSFormService {

    String save(FormSaveReqVO vo);

    String update(FormUpdateReqVO vo);

    FormOneRespVO getOne(IdReqVO vo);

    PageResult<FormListRespVO> list(FormSaveReqVO vo);

    String deleteBatch(IdReqVO vo);

    List<FormOneRespVO> listFormByBizId(IdReqVO vo);
}
