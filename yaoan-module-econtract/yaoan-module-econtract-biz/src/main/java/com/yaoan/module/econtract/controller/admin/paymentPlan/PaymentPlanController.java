package com.yaoan.module.econtract.controller.admin.paymentPlan;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.contract.vo.PayPerformanceDetailRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.PayPerformancePageRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.PaymentSchedulePageReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.PaymentSchedulePageRespVO;
import com.yaoan.module.econtract.controller.admin.paymentPlan.vo.*;
import com.yaoan.module.econtract.controller.admin.paymentRecord.vo.PaymentAmountRepVO;
import com.yaoan.module.econtract.controller.admin.paymentRecord.vo.PaymentRecordRepVO;
import com.yaoan.module.econtract.controller.admin.paymentRecord.vo.PaymentRecordRespVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractPerformanceLogDO;
import com.yaoan.module.econtract.service.paymentPlan.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 支付计划Controller
 *
 * @author zhc
 * @since 2023-12-21
 */
@Slf4j
@RestController
@RequestMapping("econtract/paymentPlan")
@Tag(name = "支付计划", description = "支付计划")
public class PaymentPlanController {
    @Resource
    private PaymentService paymentService;

    /**
     * 查询所有付款计划
     *
     * @param paymentPlanRepVO
     * @return
     */
    @PostMapping(value = "/page/list")
    @Operation(summary = "查询所有付款计划", description = "查询所有付款计划")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<ContractPaymentPlanRespVO>> queryAllPaymentPlan(@RequestBody PaymentPlanRepVO paymentPlanRepVO) {
        PageResult<ContractPaymentPlanRespVO> RelativePage = paymentService.queryAllPaymentPlan(paymentPlanRepVO);
        return success(RelativePage);
    }

    /**
     * 查询所有付款记录
     *
     * @param paymentRecordRepVO
     * @return
     */
    @PostMapping(value = "record/page/list")
    @Operation(summary = "查询所有付款记录", description = "查询所有付款记录")
    @OperateLog(logArgs = false)
    public CommonResult< PageResult<PaymentRecordRespVO>> queryAllPaymentRecord(@RequestBody PaymentRecordRepVO paymentRecordRepVO) {
        PageResult<PaymentRecordRespVO> RelativePage = paymentService.queryAllPaymentRecord(paymentRecordRepVO);
        return success(RelativePage);
    }

    /**
     * 卖场返回支付申请的支付时间和支付状态
     *
     * @param ids 计划申请id
     * @return
     */
    @PostMapping(value = "push")
    @Operation(summary = "卖场返回支付申请的支付时间和支付状态", description = "卖场返回支付申请的支付时间和支付状态")
    @OperateLog(logArgs = false)
    public CommonResult pushPayInfo(@RequestBody List<String> ids) {
        paymentService.pushPayInfo(ids);
        return success(true);
    }

    /**
     * 根据合同id获取相关计划+合同+签署方详情
     *
     * @param id 合同id
     * @return
     */
    @PostMapping(value = "query/{id}")
    @Operation(summary = "根据合同id查询支付计划详情", description = "根据合同id查询支付计划详情")
    @OperateLog(logArgs = false)
    public CommonResult<PaymentPlanInfoRespVO> queryPianpaymentById(@PathVariable String id) {
        PaymentPlanInfoRespVO paymentPlanInfoRespVO = paymentService.queryPianpaymentById(id);
        return success(paymentPlanInfoRespVO);
    }

    /**
     * 获取总金额，已支付金额及未支付金额
     *
     * @param paymentPlanRepVO  flag:1：支付计划 2：付款记录
     * @return
     */
    @PostMapping(value = "queryAmountInfo")
    @Operation(summary = "获取总金额，已支付金额及未支付金额", description = "获取总金额，已支付金额及未支付金额")
    @OperateLog(logArgs = false)
    public CommonResult<PaymentPlanAmountRespVO> queryAmountInfo(@RequestBody PaymentAmountRepVO paymentPlanRepVO) {
        PaymentPlanAmountRespVO paymentPlanAmountRespVO = paymentService.queryAmountInfo(paymentPlanRepVO);
        return success(paymentPlanAmountRespVO);
    }

    /**
     * 付款管理 - 金额统计
     *
     * @param paymentPlanRepVO
     * @return
     */
    @PostMapping(value = "paymentManagementStatistics")
    @Operation(summary = "付款管理 - 金额统计", description = "付款管理 - 金额统计")
    @OperateLog(logArgs = false)
    public CommonResult<PaymentPlanAmountRespVO> paymentManagementStatistics(@RequestBody PaymentAmountRepVO paymentPlanRepVO) {
        PaymentPlanAmountRespVO paymentPlanAmountRespVO = paymentService.paymentManagementStatistics(paymentPlanRepVO);
        return success(paymentPlanAmountRespVO);
    }

    /**
     * 付款管理-列表展示（Pele）
     */
    @PostMapping(value = "listPaymentManagement")
    @Operation(summary = "付款管理-数据展示", description = "获取付款列表")
    public CommonResult<PageResult<PaymentSchedulePageRespVO>> listPaymentManagement(@RequestBody PaymentSchedulePageReqVO paymentSchedulePageReqVO) {
        PageResult<PaymentSchedulePageRespVO> result = paymentService.listPaymentManagement(paymentSchedulePageReqVO);
        return success(result);
    }

    @PostMapping(value = "/listPerformance")
    @Operation(summary = "履约计划-数据展示", description = "获取履约计划列表")
    public CommonResult<PageResult<PayPerformancePageRespVO>> listPerformance(@RequestBody PaymentPlanRepVO paymentPlanRepVO) {
        PageResult<PayPerformancePageRespVO> result = paymentService.listPerformance(paymentPlanRepVO);
        return success(result);
    }

    @PostMapping(value = "/totalAmount")
    @Operation(summary = "履约计划列表-统计金额", description = "履约计划列表-统计金额")
    @OperateLog(logArgs = false)
    public CommonResult<TotalAmountRespVO> totalAmount(@RequestBody PaymentPlanRepVO paymentPlanRepVO) {
        return success(paymentService.totalAmount(paymentPlanRepVO));
    }

    @PostMapping(value = "/insert")
    @Operation(summary = "新增履约计划")
    public CommonResult<String> insert(@Valid @RequestBody PerformV2SaveReqVO performV2SaveReqVO) {
        String planId = paymentService.insertPerformance(performV2SaveReqVO);
        return success(planId);
    }

    @PostMapping(value = "/update")
    @Operation(summary = "修改履约计划")
    public CommonResult<String> update(@Valid @RequestBody PerformV2SaveReqVO performV2SaveReqVO) {
        String planId = paymentService.updatePerformance(performV2SaveReqVO);
        return success(planId);
    }
    @PostMapping(value = "/batchdelete")
    @Operation(summary = "批量删除履约计划")
    public CommonResult<Boolean> batchDelete(@Valid @RequestBody PaymentPlanIdListVO paymentPlanIdListVO) throws Exception {
        paymentService.deletePerformance(paymentPlanIdListVO.getIds());
        return success(true);
    }
    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "根据id删除履约计划", description = "删除履约计划")
    @OperateLog(logArgs = false)
    public CommonResult<Boolean> deleteContact(@PathVariable String id) throws Exception {
        List idList  = new ArrayList();
        idList.add(id);
        paymentService.deletePerformance(idList);
        return success(true);
    }
    @GetMapping("/getperformance/{id}")
    @Operation(summary = "根据履约计划id获取履约计划")
    public CommonResult<PayPerformanceDetailRespVO> getperformance(@PathVariable("id") String id) throws Exception {
        PayPerformanceDetailRespVO payPerformanceDetailRespVO = paymentService.getPerformance(id);
        return success(payPerformanceDetailRespVO);
    }
//    @PostMapping("/confirmPlan")
//    @Operation(summary = "财务确认收款/确认付款")
//    public CommonResult<Boolean> confirmPlan(@Valid @RequestBody PaymentPlanConfirmReqVO performV2SaveReqVO) throws Exception {
//        paymentService.confirmPaymentPlan(performV2SaveReqVO);
//        return success(true);
//    }

    @PostMapping("/confirmPlan")
    @Operation(summary = "财务确认收款/确认付款")
    public CommonResult<Boolean> confirmPlanV2(@Valid @RequestBody PaymentPlanConfirmReqVO performV2SaveReqVO) throws Exception {
        paymentService.confirmPaymentPlanV2(performV2SaveReqVO);
        return success(true);
    }

    @GetMapping(value = "/getlogs/{id}")
    @Operation(summary = "根据履约计划id获取履约日志", description = "获取履约日志")
    @OperateLog(logArgs = false)
    public CommonResult<List<ContractPerformanceLogDO>> getlogs(@PathVariable String id) throws Exception {
        List<ContractPerformanceLogDO> contractPerformanceLogDOList = paymentService.queryPerformanceLogs(id);
        return success(contractPerformanceLogDOList);
    }


    @GetMapping(value = "/publish/{id}")
    @Operation(summary = "发布履约计划", description = "发布履约计划")
    @OperateLog(logArgs = false)
    public CommonResult<String> publish(@PathVariable String id) throws Exception {
        return success(paymentService.publish(id));
    }




}
