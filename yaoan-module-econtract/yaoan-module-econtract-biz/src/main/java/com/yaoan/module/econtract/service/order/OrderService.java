package com.yaoan.module.econtract.service.order;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.api.contract.dto.ContractDataDTO;
import com.yaoan.module.econtract.api.gcy.order.DraftOrderInfo;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.controller.admin.order.vo.*;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/15 15:02
 */
public interface OrderService {

    void receiveOrderInfos(List<DraftOrderInfo> draftOrderInfoList);

    PageResult<GPMallPageRespVO> listGPMallOrder(GPMallPageReqVO vo);

    OrderBaseInfoRespVO getOrderBaseInfo(IdReqVO vo);

    OrderAutoInfoRespVO getAutoInfo(IdReqVO vo);

    /**
     * 第三方数据列表（黑龙江迁移过来的）
     */
    PageResult<GPMallPageRespVO> listThirdData(GPMallPageReqVO vo);

    PageResult<GPMallPageRespVO> listGPMallOrder2(GPMallPageReqVO vo);


    ThirdOrderAutoInfoRespVO autoCreateContractByOrderId(IdReqVO vo);



    /**
     * ---------------------------------------------------------------- 第三方数据集成 ----------------------------------------------------------------
     */
    PageResult<GPMallPageV2RespVO> listGPMallOrderV2(GPMallPageReqVO vo);

    OrderAutoInfoV2RespVO getAutoInfoV2(IdReqVO vo);
    ContractDataDTO getOrderInfoAndTemplateInfo(String id);
}
