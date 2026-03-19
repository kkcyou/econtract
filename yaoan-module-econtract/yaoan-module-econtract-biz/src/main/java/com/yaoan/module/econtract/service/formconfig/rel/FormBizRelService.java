package com.yaoan.module.econtract.service.formconfig.rel;

import com.yaoan.module.econtract.controller.admin.formconfig.formrelation.vo.FormRelSaveReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/21 11:51
 */
public interface FormBizRelService {

    String save(FormRelSaveReqVO vo);

    String update(FormRelSaveReqVO vo);

    String delete(IdReqVO vo);
}
