package com.yaoan.module.econtract.controller.admin.payment.paymentapplication;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.StatisticsAmountV2RespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.BigPaymentApplicationListBpmRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.PaymentApplicationListBpmReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.listPaymentApplication.PaymentApplicationListReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.listPaymentApplication.PaymentApplicationListRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.PaymentApplicationReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.PaymentApplicationRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.PaymentApplicationSaveReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.PaymentApplicationUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.statisticsamount.StatisticsAmountRespVO;
import com.yaoan.module.econtract.enums.payment.ApprovePageFlagEnums;
import com.yaoan.module.econtract.service.payment.paymentapplication.PaymentApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

/**
 * @description: 付款申请接口
 * @author: Pele
 * @date: 2023/12/21 17:16
 */
@Slf4j
@RestController
@RequestMapping("econtract/paymentapplication")
@Validated
@Tag(name = "付款申请接口", description = "付款申请接口")
public class PaymentApplicationController {

    @Resource
    private PaymentApplicationService paymentApplicationService;

    /**
     * 付款申请分页列表 接口
     */
    @PostMapping("/listPaymentApplication")
    @Operation(summary = "付款申请分页列表 接口")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<PaymentApplicationListRespVO>> listPaymentApplication(@RequestBody PaymentApplicationListReqVO vo) throws Exception {
        return success(paymentApplicationService.listPaymentApplication(vo));
    }

    @PostMapping(value = "/page")
    @Operation(summary = "履约申请-数据展示", description = "获取履约申请列表")
    public CommonResult<PageResult<PaymentApplicationListRespVO>> getPaymentApplicationPage(@RequestBody PaymentApplicationListReqVO paymentPlanRepVO) {
        PageResult<PaymentApplicationListRespVO> result = paymentApplicationService.getPaymentApplicationPage(paymentPlanRepVO);
        return success(result);
    }

    /**
     * 金额统计 接口
     */
    @PostMapping("/statisticsAmount")
    @Operation(summary = "金额统计 接口")
    @OperateLog(logArgs = false)
    public CommonResult<StatisticsAmountRespVO> statisticsAmount(@RequestBody PaymentApplicationListBpmReqVO vo) throws Exception {
        return success(paymentApplicationService.statisticsAmount(vo));
    }

    /**
     * 付款申请 详情查看 接口
     */
    @PostMapping("/getPaymentApplication")
    @Operation(summary = "付款申请 详情查看 接口")
    @OperateLog(logArgs = false)
    public CommonResult<PaymentApplicationRespVO> getPaymentApplication(@RequestBody PaymentApplicationReqVO vo) throws Exception {
        return success(paymentApplicationService.getPaymentApplication(vo));
    }

    /**
     * 付款申请 基本信息 保存 接口
     */
    @PostMapping("/savePaymentApplication")
    @Operation(summary = "付款申请 基本信息 保存 接口")
    @OperateLog(logArgs = false)
    public CommonResult<String> savePaymentApplication(@Validated @RequestBody PaymentApplicationSaveReqVO vo) throws Exception {
        return success(paymentApplicationService.savePaymentApplication(vo));
    }

    @PostMapping("/saveCollectionApplication")
    @Operation(summary = "收款申请 基本信息 保存 接口")
    @OperateLog(logArgs = false)
    public CommonResult<String> saveCollectionApplication(@RequestBody PaymentApplicationSaveReqVO vo) throws Exception {
        return success(paymentApplicationService.saveCollectionApplication(vo));
    }

    /**
     * 付款申请 基本信息 编辑 接口
     */
    @PostMapping("/updatePaymentApplication")
    @Operation(summary = "付款申请 基本信息 编辑 接口")
    @OperateLog(logArgs = false)
    public CommonResult<String> updatePaymentApplication(@Validated  @RequestBody PaymentApplicationUpdateReqVO vo) throws Exception {
        return success(paymentApplicationService.updatePaymentApplication(vo));
    }

    /**
     * 付款申请 基本信息 删除 接口
     */
    @PostMapping("/deletePaymentApplications")
    @Operation(summary = "付款申请 基本信息 删除 接口")
    @OperateLog(logArgs = false)
    public CommonResult<String> deletePaymentApplications(@RequestBody IdReqVO vo) throws Exception {
        return success(paymentApplicationService.deletePaymentApplications(vo));
    }

    /**
     * 付款申请 发起审批请求 接口
     */
    @PostMapping("/submit")
    @Operation(summary = "付款申请 发起审批请求 接口")
    @OperateLog(logArgs = false)
    public CommonResult<String> submit(@RequestBody PaymentApplicationSaveReqVO vo) throws Exception {
        return success(paymentApplicationService.submitApprovePaymentApplication(vo));
    }

    /**
     * 付款申请 保存且发起审批请求 接口
     */
    @PostMapping("/submitApprovePaymentApplication")
    @Operation(summary = "付款申请 保存且发起审批请求 接口")
    @OperateLog(logArgs = false)
    public CommonResult<String> submitApprovePaymentApplication(@RequestBody PaymentApplicationSaveReqVO vo) throws Exception {
        return success(paymentApplicationService.submitApprovePaymentApplication(vo));
    }

    /**
     * ----------------------------- 工作流task -----------------------------
     */

    /**
     * 获取待办任务分页
     */
    @PostMapping("getBpmToDoTaskPage")
    @Operation(summary = "获取 Todo 待办任务分页")
    public CommonResult<BigPaymentApplicationListBpmRespVO> getBpmToDoTaskPage(@Valid @RequestBody PaymentApplicationListBpmReqVO pageVO) {
        return success(paymentApplicationService.getBpmToDoTaskPage(getLoginUserId(), pageVO));
    }

    /**
     * 获取Done已办任务分页
     */
    @PostMapping("getBpmDoneTaskPage")
    @Operation(summary = "获取Done已办任务分页")
    public CommonResult<BigPaymentApplicationListBpmRespVO> getBpmDoneTaskPage(@Valid @RequestBody PaymentApplicationListBpmReqVO pageVO) {
        return success(paymentApplicationService.getBpmDoneTaskPage(getLoginUserId(), pageVO));
    }

    /**
     * 获取所有任务分页
     */
    @PostMapping("getBpmAllTaskPage")
    @Operation(summary = "获取所有任务分页")
    public CommonResult<BigPaymentApplicationListBpmRespVO> getBpmAllTaskPage(@Valid @RequestBody PaymentApplicationListBpmReqVO pageVO) {
        return success(paymentApplicationService.getBpmAllTaskPage(getLoginUserId(), pageVO));
    }

    /**
     * 获取任务分页（0=全部 1=已审批 2=未审批）
     */
    @PostMapping("getAutoBpmAllTaskPage")
    @Operation(summary = "获取任务分页（0=全部 1=已审批 2=未审批）")
    public CommonResult<BigPaymentApplicationListBpmRespVO> getAutoBpmAllTaskPage(@Valid @RequestBody PaymentApplicationListBpmReqVO pageVO) {
        int flag = pageVO.getFlag();
        String loginId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        pageVO.setApplicantId(loginId);
        ApprovePageFlagEnums approvePageFlagEnums = ApprovePageFlagEnums.getInstance(flag);
        switch (approvePageFlagEnums) {
            case ALL:
                return success(paymentApplicationService.getBpmAllTaskPage(getLoginUserId(), pageVO));
            case DONE:
                return success(paymentApplicationService.getBpmDoneTaskPage(getLoginUserId(), pageVO));
            case TO_DO:
                return success(paymentApplicationService.getBpmToDoTaskPage(getLoginUserId(), pageVO));
            default:
                return success(new BigPaymentApplicationListBpmRespVO());
        }
    }

    /**
     * 审批金额统计 接口
     */
    @PostMapping("/statisticsApproveAmount")
    @Operation(summary = "审批金额统计 接口")
    @OperateLog(logArgs = false)
    public CommonResult<StatisticsAmountRespVO> statisticsApproveAmount(@RequestBody PaymentApplicationListBpmReqVO vo) throws Exception {
        return success(paymentApplicationService.statisticsApproveAmount(vo));
    }

    /**
     * 工作台金额统计V2 接口
     */
    @GetMapping("/statisticsV2")
    @Operation(summary = "工作台金额统计V2 接口")
    @OperateLog(logArgs = false)
    public CommonResult<StatisticsAmountV2RespVO> statisticsV2( ) throws Exception {
        return success(paymentApplicationService.statisticsV2());
    }
}
