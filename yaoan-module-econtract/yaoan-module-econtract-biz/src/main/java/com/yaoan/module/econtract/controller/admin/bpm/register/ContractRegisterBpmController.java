package com.yaoan.module.econtract.controller.admin.bpm.register;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.idempotent.core.annotation.Idempotent;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.bpm.register.vo.BigContractRegisterListApproveRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.register.vo.ContractRegisterListApproveReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.enums.payment.ApprovePageFlagEnums;
import com.yaoan.module.econtract.service.bpm.register.ContractRegisterBpmService;
import com.yaoan.module.system.api.user.AdminUserApi;
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
 * @description: 合同登记的工作流
 * @author: Pele
 * @date: 2024/1/26 10:55
 */
@Slf4j
@RestController
@RequestMapping("econtract/bpm/contractRegister")
@Validated
@Tag(name = "合同登记的工作流", description = "合同登记的工作流")
public class ContractRegisterBpmController {

    @Resource
    private ContractRegisterBpmService contractRegisterBpmService;
    @Resource
    private AdminUserApi adminUserApi;

    /**
     * 合同登记请求审批发起
     */
    @PostMapping("/submit")
    @Operation(summary = "合同登记请求审批发起")
    @OperateLog(logArgs = false)
    @Idempotent(timeout = 10, timeUnit = TimeUnit.SECONDS, message = "请勿重复发起提交")
    public CommonResult<String> submit(@Valid @RequestBody IdReqVO vo) throws Exception {
        return success(contractRegisterBpmService.submit(getLoginUserId(), vo));
    }

    /**
     * 合同登记请求批量审批发起
     */
    @PostMapping("/submitBatch")
    @Operation(summary = "合同登记请求批量审批发起")
    @OperateLog(logArgs = false)
    @Idempotent(timeout = 10, timeUnit = TimeUnit.SECONDS, message = "正在审批中，请勿重复提交")
    public CommonResult<String> submitBatch(@Valid @RequestBody IdReqVO vo) throws Exception {
        return success(contractRegisterBpmService.submitBatch(getLoginUserId(), vo));
    }

    /**
     * 合同登记-获取任务分页（0=全部 1=已审批 2=未审批）
     */
    @PostMapping("getAutoBpmAllTaskPage")
    @Operation(summary = "获取任务分页（0=全部 1=已审批 2=未审批）")
    public CommonResult<BigContractRegisterListApproveRespVO> getAutoBpmAllTaskPage(@Valid @RequestBody ContractRegisterListApproveReqVO pageVO) {
        int flag = pageVO.getFlag();
        String loginId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        pageVO.setApplicantId(loginId);
        ApprovePageFlagEnums approvePageFlagEnums = ApprovePageFlagEnums.getInstance(flag);
        //先将模糊查询的名字设好
        if (ObjectUtil.isNotNull(pageVO.getApplicantName())) {
            pageVO.setUserList(adminUserApi.getUserIdsByNameLike(pageVO.getApplicantName()));
        }
        switch (approvePageFlagEnums) {
            case ALL:
                return success(contractRegisterBpmService.getBpmAllTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
            case DONE:
                return success(contractRegisterBpmService.getBpmDoneTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
            case TO_DO:
                return success(contractRegisterBpmService.getBpmToDoTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
            default:
                return success(new BigContractRegisterListApproveRespVO());
        }
    }
}
