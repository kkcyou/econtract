package com.yaoan.module.econtract.controller.admin.bpm.model;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.idempotent.core.annotation.Idempotent;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.model.vo.ModelBigListApproveRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.model.vo.ModelBpmPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.model.vo.ModelBpmPageRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.model.vo.ModelBpmSubmitCreateReqVO;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.BatchSubmitReqVO;
import com.yaoan.module.econtract.enums.payment.ApprovePageFlagEnums;
import com.yaoan.module.econtract.service.bpm.model.ModelBpmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/13 19:36
 */
@Slf4j
@RestController
@RequestMapping("econtract/bpm/model")
@Validated
@Tag(name = "模板工作流模块", description = "模板工作流模块操作接口")
public class ModelBpmController {
    @Resource
    private ModelBpmService modelBpmService;

    /**
     * 模板请求审批发起
     */
    @PostMapping("/submitModelApproveFlowable")
    @Operation(summary = "模板请求审批发起")
    @OperateLog(logArgs = false)
    @Idempotent(timeout = 10, timeUnit = TimeUnit.SECONDS, message = "正在审批中，请勿重复提交")
    public CommonResult<String> submitModelApproveFlowable(@Valid @RequestBody ModelBpmSubmitCreateReqVO reqVO) throws Exception {
        return success(modelBpmService.submitModelApproveFlowable(getLoginUserId(), reqVO));
    }

    /**
     * 获取模板审批列表数据
     */
    @PostMapping("/page")
    @Operation(summary = "获取模板审批列表数据")
    @DataPermission(enable = false)
    public CommonResult<PageResult<ModelBpmPageRespVO>> getApprovePage(@RequestBody ModelBpmPageReqVO reqVO) {
        return success(modelBpmService.getApprovePage(reqVO));
    }

    /**
     * 获取任务分页（flag： 0=全部 1=已审批 2=未审批）
     */
    @PostMapping("getAutoBpmAllTaskPage")
    @Operation(summary = "获取任务分页（0=全部 1=已审批 2=未审批）")
    public CommonResult<ModelBigListApproveRespVO> getAutoBpmAllTaskPage(@Valid @RequestBody CommonBpmAutoPageReqVO pageVO) {
        int flag = pageVO.getFlag();
        String loginId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        pageVO.setApplicantId(loginId);
        ApprovePageFlagEnums approvePageFlagEnums = ApprovePageFlagEnums.getInstance(flag);
        switch (approvePageFlagEnums) {
            case ALL:
                return success(modelBpmService.getBpmAllTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
            case DONE:
                return success(modelBpmService.getBpmDoneTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
            case TO_DO:
                return success(modelBpmService.getBpmToDoTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
            default:
                return success(new ModelBigListApproveRespVO());
        }
    }

    /**
     * 模板批量请求审批发起(现用)
     */
    @PostMapping("/submitModelApproveFlowableBatch")
    @Operation(summary = "模板批量请求审批发起(现用)")
    @OperateLog(logArgs = false)
    @Idempotent(timeout = 10, timeUnit = TimeUnit.SECONDS, message = "正在审批中，请勿重复提交")
    public CommonResult<String> submitModelApproveFlowableBatch(@Valid @RequestBody BatchSubmitReqVO reqVO) {
        return success(modelBpmService.submitModelApproveFlowableBatch(getLoginUserId(), reqVO));
    }
}
