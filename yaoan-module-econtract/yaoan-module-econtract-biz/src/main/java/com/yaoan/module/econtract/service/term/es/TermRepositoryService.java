package com.yaoan.module.econtract.service.term.es;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.term.vo.TermPageReqVO;
import com.yaoan.module.econtract.controller.admin.term.vo.TermSimpleRespVO;

import java.io.IOException;

/**
 * @description:
 * @author: Pele
 * @date: 2025-5-8 14:12
 */
public interface TermRepositoryService {


    void saveAll();

    void init();

    PageResult<TermSimpleRespVO> pageTerm4es3(TermPageReqVO vo);


}
