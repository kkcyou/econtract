package com.yaoan.module.econtract.service.param;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.param.vo.*;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author doujl
 * @since 2023-07-24
 */
public interface ParamService {
    String saveParamV2(ParamReqVO paramReqVO) throws JsonProcessingException;
    List<String> removeByIds(ReqIdsVO paramByIdsVO) throws Exception;
    PageResult<ParamRespVO> queryAllParam(ParamPageReqVO paramPageReqVO);
    ParamRespVO queryParamById(String id) throws Exception;
    List<ParamListRespVO> queryParamAndCategory(BaseParamVO baseParamVO);

    List<TermV2RespVO> queryParamByModelId(String id);
}
