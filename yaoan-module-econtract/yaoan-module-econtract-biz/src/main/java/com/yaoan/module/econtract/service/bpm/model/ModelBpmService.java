package com.yaoan.module.econtract.service.bpm.model;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.model.vo.*;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.BatchSubmitReqVO;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/13 19:39
 */
public interface ModelBpmService {

    String submitModelApproveFlowable(Long loginUserId, ModelBpmSubmitCreateReqVO reqVO) throws Exception;

    PageResult<ModelBpmPageRespVO> getApprovePage(ModelBpmPageReqVO reqVO);

    ModelBigListApproveRespVO getBpmAllTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO);

    ModelBigListApproveRespVO getBpmDoneTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO);

    ModelBigListApproveRespVO getBpmToDoTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO);

    String submitModelApproveFlowableBatch(Long loginUserId, BatchSubmitReqVO reqVO);

}
