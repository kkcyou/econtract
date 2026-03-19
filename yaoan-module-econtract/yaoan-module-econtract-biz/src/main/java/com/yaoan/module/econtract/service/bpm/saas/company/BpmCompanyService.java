package com.yaoan.module.econtract.service.bpm.saas.company;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.saas.company.BpmCompanySubmitReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.saas.company.vo.BpmCompanyPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.saas.company.vo.BpmCompanyPageRespVO;

import javax.validation.Valid;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-28 16:29
 */
public interface BpmCompanyService {

    /**
     * submit application
     * */
    String submit(@Valid BpmCompanySubmitReqVO reqVO);

    /**
     * approval list
     */
    PageResult<BpmCompanyPageRespVO> page(@Valid BpmCompanyPageReqVO reqVO);
}
