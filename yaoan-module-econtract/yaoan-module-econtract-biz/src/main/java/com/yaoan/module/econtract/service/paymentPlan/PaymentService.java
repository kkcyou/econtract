package com.yaoan.module.econtract.service.paymentPlan;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contract.vo.PayPerformanceDetailRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.PayPerformancePageRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.PaymentSchedulePageReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.PaymentSchedulePageRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.listPaymentApplication.PaymentApplicationListReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.listPaymentApplication.PaymentApplicationListRespVO;
import com.yaoan.module.econtract.controller.admin.paymentPlan.vo.*;
import com.yaoan.module.econtract.controller.admin.paymentRecord.vo.PaymentAmountRepVO;
import com.yaoan.module.econtract.controller.admin.paymentRecord.vo.PaymentRecordRepVO;
import com.yaoan.module.econtract.controller.admin.paymentRecord.vo.PaymentRecordRespVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractPerformanceLogDO;

import java.util.List;
import java.util.Map;

/**
 * 支付计划实现类
 *
 * @author zhc
 * @since 2023-12-21
 */
public interface PaymentService {
    /**
     * 分页获取支付计划列表数据
     *
     * @param paymentPlanRepVO
     * @return
     */
    PageResult<ContractPaymentPlanRespVO> queryAllPaymentPlan(PaymentPlanRepVO paymentPlanRepVO);

    /**
     * 卖场返回支付申请的支付时间和支付状态
     *
     * @param id 计划申请id
     * @return
     */
    void pushPayInfo(List<String> id);

    /**
     * 分页获取付款记录列表数据
     *
     * @param recordRepVO
     * @return
     */
    PageResult<PaymentRecordRespVO> queryAllPaymentRecord(PaymentRecordRepVO recordRepVO);

    /**
     * 根据计划id获取相关计划+合同+签署方详情
     *
     * @param id 计划申请id
     * @return
     */
    PaymentPlanInfoRespVO queryPianpaymentById(String id);

    /**
     * 获取总金额，已支付金额及未支付金额
     *
     * @param paymentPlanRepVO flag  1：支付计划 2：付款记录
     * @return
     */
    PaymentPlanAmountRespVO queryAmountInfo(PaymentAmountRepVO paymentPlanRepVO);

    PaymentPlanAmountRespVO paymentManagementStatistics(PaymentAmountRepVO paymentPlanRepVO);

    PageResult<PaymentSchedulePageRespVO> listPaymentManagement(PaymentSchedulePageReqVO paymentSchedulePageReqVO);

    /**
     * @param paymentPlanRepVO
     * @return
     */
    PageResult<PayPerformancePageRespVO> listPerformance(PaymentPlanRepVO paymentPlanRepVO);
    /**
     * 新增履约计划/付款计划
     *
     * @param performV2SaveReqVO
     * @return
     */
    String insertPerformance(PerformV2SaveReqVO performV2SaveReqVO);

    /**
     * 修改履约计划
     *
     * @param performV2SaveReqVO
     * @return
     */
    String updatePerformance(PerformV2SaveReqVO performV2SaveReqVO);

    /**
     * 批量删除付款计划
     *
     * @param ids
     * @throws Exception
     */
    void deletePerformance(List<String> ids) throws Exception;

    PayPerformanceDetailRespVO getPerformance(String id) throws Exception;

    void confirmPaymentPlan(PaymentPlanConfirmReqVO paymentPlanConfirmReqVO) throws Exception;
    void confirmPaymentPlanV2(PaymentPlanConfirmReqVO paymentPlanConfirmReqVO) throws Exception;

    List<ContractPerformanceLogDO> queryPerformanceLogs(String performanceId);

    String publish(String id);

    /**
     * 新增履约日志
     */
    String savePerformanceLog(ContractPerformanceLogDO contractPerformanceLogDO);
    /**
     * 批量新增履约日志
     */
    void saveBatchPerformanceLog(List<ContractPerformanceLogDO> contractPerformanceLogDOs);




    DashboardContractCountRespVO queryPerformanceTopCount();

    DashboardPerformanceMoneyRespVO queryPerformanceMoney(DashboardPerformanceMoneyReqVO dashboardPerformanceMoneyReqVO);

    DashboardContractStatusCountRespVO queryContractStatusCount();

    List<Map<String,Object>> queryPerformanceList();


    List<DashboardContractRiskRespVO> queryRiskContractList();

    TotalAmountRespVO totalAmount(PaymentPlanRepVO paymentPlanRepVO);

    List<DashboardContractComplateRespVO> queryContractComplate(String year);
    
}
