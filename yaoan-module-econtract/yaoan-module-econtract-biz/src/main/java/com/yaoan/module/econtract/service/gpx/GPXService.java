package com.yaoan.module.econtract.service.gpx;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractPlaybackV3RespVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading.SupplierCombinationInfoVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.GPXListReqVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.GPXListRespVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.autofill.PlaybackReqVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.draft.ProjectInfo;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/20 15:06
 */
public interface GPXService {

    /**
     * 交易执行-接收项目包信息
     */
    String draft(List<ProjectInfo> list);

    /**
     * 交易执行-起草列表
     */
    PageResult<GPXListRespVO> list(GPXListReqVO vo);


    GPXContractPlaybackV3RespVO playbackInfoV3(PlaybackReqVO reqVO) throws Exception;

    List<SupplierCombinationInfoVO> getSupplierInfoList(PlaybackReqVO reqVO) throws Exception;

    public void checkPlan(String packageId, String contractId);
}
