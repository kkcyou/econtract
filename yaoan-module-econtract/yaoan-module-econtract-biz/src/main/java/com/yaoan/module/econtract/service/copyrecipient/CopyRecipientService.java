package com.yaoan.module.econtract.service.copyrecipient;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.copyrecipient.vo.CopyRecipientPageReqVO;
import com.yaoan.module.econtract.controller.admin.copyrecipient.vo.CopyRecipientPageRespVO;

/**
 * @description:
 * @author: Pele
 * @date: 2024/4/1 22:37
 */
public interface CopyRecipientService {

    PageResult<CopyRecipientPageRespVO> list(CopyRecipientPageReqVO reqVO);
}
