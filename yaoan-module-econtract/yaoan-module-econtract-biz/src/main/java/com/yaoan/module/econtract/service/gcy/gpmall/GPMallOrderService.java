package com.yaoan.module.econtract.service.gcy.gpmall;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.api.contract.dto.ContractDataDTO;
import com.yaoan.module.econtract.api.gcy.order.DraftOrderInfo;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.order.BigGPMallPageRespVO;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.order.GPMallOrderDetailReqVO;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.order.GroupedDraftOrderInfoVO;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.order.OrderIdListVO;
import com.yaoan.module.econtract.controller.admin.order.vo.GPMallPageReqVO;
import com.yaoan.module.econtract.controller.admin.order.vo.GPMallPageRespVO;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/4 11:55
 */
public interface GPMallOrderService {

    void saveOrderInfo(DraftOrderInfo info);

    /**
     * 根据订单编号获取模板填充信息
     */
    ContractDataDTO queryByOrgOrderV2(OrderIdListVO orderIdListVO);

    /**
     * 根据订单编号获取模板填充信息
     */
    ContractDataDTO queryByOrgOrderV3(OrderIdListVO orderIdListVO);

    /**
     * 卖场订单列表
     *
     * @param vo
     * @return
     */
    PageResult<GroupedDraftOrderInfoVO> listGPMallOrgOrder(GPMallPageReqVO vo);

    /**
     * 卖场订单查询子订单 org 单位端
     */
    List<GPMallPageRespVO> queryGPMallOrderOrgDetail(GPMallOrderDetailReqVO vo);

    /**
     * 服务工程超市订单列表
     *
     * @param vo
     * @return
     */
    PageResult<GPMallPageRespVO> listGPMallOrder(GPMallPageReqVO vo);

    BigGPMallPageRespVO listGPMallOrders(GPMallPageReqVO vo);

    /**
     * 电子卖场订单回显起草信息 单位端( 可配置合并或非合并订单)(HLJ_pro)
     *
     * @param orderIdListVO
     * @return <{@link ContractDataDTO }>
     * @throws Exception
     */
    ContractDataDTO queryGPMallOrder4Draft(OrderIdListVO orderIdListVO) throws Exception;
}
