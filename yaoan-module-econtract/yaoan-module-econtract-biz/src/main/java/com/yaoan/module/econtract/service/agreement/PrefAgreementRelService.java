package com.yaoan.module.econtract.service.agreement;

import com.yaoan.module.econtract.controller.admin.agreement.vo.PrefAgreementRelCreateReqVO;


public interface PrefAgreementRelService {
    String createAgreement(PrefAgreementRelCreateReqVO prefAgreementRelCreateReqVO) throws Exception;
}
