package com.yaoan.module.econtract.service.performTaskType;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.performtasktype.vo.*;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/3 15:22
 */
public interface PerformTaskTypeService {

    PageResult<PerformTaskTypeRespVO>  getPerformTaskTypePage(PerformTaskTypeListReqVo vo);

    String create(PerformTaskTypeCreateReqVO reqVO);

    void updatePerformTaskType(PerformTaskTypeUpdateReqVO reqVO);

    void deletePerformTaskType(DeleteVO reqVO);
}
