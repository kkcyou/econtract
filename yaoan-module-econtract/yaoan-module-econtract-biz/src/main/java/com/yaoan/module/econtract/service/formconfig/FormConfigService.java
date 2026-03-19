package com.yaoan.module.econtract.service.formconfig;


import com.yaoan.module.econtract.controller.admin.formconfig.vo.FormConfigSaveReqVO;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.FormConfigSingleRespVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/18 19:30
 */
public interface FormConfigService {

    String saveFormConfig(FormConfigSaveReqVO vo);

    String updateFormConfig(FormConfigSaveReqVO vo);

    FormConfigSingleRespVO getFormBusinessByBusinessId(IdReqVO vo);
}
