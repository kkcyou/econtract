package com.yaoan.module.econtract.service.formconfig;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.formbusiness.FormBusinessListReqVO;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.formbusiness.FormBusinessListRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.formbusiness.FormBusinessSaveReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/19 10:20
 */
public interface FormBusinessService {

    String saveFormBusiness(FormBusinessSaveReqVO vo);

    String updateFormBusiness(FormBusinessSaveReqVO vo);

    String deleteFormBusiness(IdReqVO vo);

    PageResult<FormBusinessListRespVO> listFormBusiness(FormBusinessListReqVO vo);
}
