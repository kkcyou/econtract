package com.yaoan.module.econtract.service.bpm.template;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.template.vo.*;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.BatchSubmitReqVO;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/14 14:50
 */
public interface TemplateBpmService {

    String submitTemplateApproveFlowable(Long loginUserId, TemplateBpmSubmitCreateReqVO reqVO);

    PageResult<TemplateBpmPageRespVO> getTemplateApprovePage(TemplateBpmPageReqVO reqVO);
    
   BigTemplateListApproveRespVO getBpmAllTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO);

   BigTemplateListApproveRespVO getBpmDoneTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO);

   BigTemplateListApproveRespVO getBpmToDoTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO);

    String submitApproveFlowableBatch(Long loginUserId, BatchSubmitReqVO reqVO);
}
