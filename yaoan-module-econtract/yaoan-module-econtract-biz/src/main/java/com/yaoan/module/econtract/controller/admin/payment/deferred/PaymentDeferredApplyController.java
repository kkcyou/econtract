package com.yaoan.module.econtract.controller.admin.payment.deferred;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.payment.deferred.vo.*;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.BigPaymentApplicationListBpmRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.PaymentApplicationListBpmReqVO;
import com.yaoan.module.econtract.enums.payment.ApprovePageFlagEnums;
import com.yaoan.module.econtract.service.deferred.PaymentDeferredApplyService;
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
 * @description: 计划延期申请
 * @author: Pele
 * @date: 2024/9/30 16:27
 */
@Slf4j
@RestController
@RequestMapping("econtract/deferredapply")
@Validated
@Tag(name = "计划延期申请", description = "计划延期申请")
public class PaymentDeferredApplyController {

    @Resource
    private PaymentDeferredApplyService paymentDeferredApplyService;

    /**
     * 计划延期申请 保存 接口
     */
    @PostMapping("/saveOrUpdate")
    @Operation(summary = "计划延期申请 基本信息 保存 接口")
    @OperateLog(logArgs = false)
    public CommonResult<String> saveOrUpdate(@Validated @RequestBody PaymentDeferredApplySaveReqVO vo) throws Exception {
        return success(paymentDeferredApplyService.saveOrUpdate(vo));
    }

    /**
     * 计划延期申请 编辑接口
     */
    @PostMapping("/update")
    @Operation(summary = "计划延期申请 基本信息 编辑接口")
    @OperateLog(logArgs = false)
    public CommonResult<String> update(@Validated @RequestBody PaymentDeferredApplyUpdateReqVO vo) throws Exception {
        return success(paymentDeferredApplyService.update(vo));
    }

    /**
     * 计划延期申请 删除 接口
     */
    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "计划延期申请 删除 接口")
    @OperateLog(logArgs = false)
    public CommonResult<String> delete(@PathVariable("id") String id) throws Exception {
        return success(paymentDeferredApplyService.delete(id));
    }

    /**
     * 计划延期申请 详情
     */
    @GetMapping("/query/{id}")
    @Operation(summary = "计划延期申请 详情 ")
    public CommonResult<PaymentDeferredApplicationRespVO> query(@PathVariable("id") String id) throws Exception {
        return success(paymentDeferredApplyService.query(id));
    }


    /**
     * 计划延期 列表
     */
    @PostMapping("/listDeferredApplication")
    @Operation(summary = "计划延期 列表 接口")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<PaymentDeferredListRespVO>> listDeferredApplication(@RequestBody PaymentDeferredListReqVO vo) throws Exception {
        return success(paymentDeferredApplyService.listDeferredApplication(vo));
    }

    /**
     * 计划延期 发起审批请求 接口
     */
    @GetMapping("/submit/{id}")
    @Operation(summary = "计划延期 发起审批请求 接口")
    @OperateLog(logArgs = false)
    public CommonResult<String> submit(@PathVariable("id") String id) throws Exception {
        return success(paymentDeferredApplyService.submit(id));
    }

    /**
     * 计划延期保存后提交 接口
     */
    @PostMapping("/save2submit")
    @Operation(summary = "计划延期保存后提交 接口")
    @OperateLog(logArgs = false)
    public CommonResult<String> save2submit(@Validated @RequestBody PaymentDeferredApplySaveReqVO vo) throws Exception {
        return success(paymentDeferredApplyService.save2submit(vo));
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
                return success(paymentDeferredApplyService.getBpmAllTaskPage(getLoginUserId(), pageVO));
            case DONE:
                return success(paymentDeferredApplyService.getBpmDoneTaskPage(getLoginUserId(), pageVO));
            case TO_DO:
                return success(paymentDeferredApplyService.getBpmToDoTaskPage(getLoginUserId(), pageVO));
            default:
                return success(new BigPaymentApplicationListBpmRespVO());
        }
    }

}
