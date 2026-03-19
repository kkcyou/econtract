package com.yaoan.module.econtract.controller.admin.bpm.contract;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.idempotent.core.annotation.Idempotent;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.BigBpmContractRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.BpmContractCreateReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.BpmContractPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.BpmContractRespVO;
import com.yaoan.module.econtract.enums.payment.ApprovePageFlagEnums;
import com.yaoan.module.econtract.service.bpm.contract.BpmContractService;
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
 * @author doujiale
 */
@Slf4j
@RestController
@RequestMapping("econtract/bpm/contract")
@Validated
@Tag(name = "合同审批模块", description = "合同审批模块")
public class BpmContractApproveController {

    @Resource
    private BpmContractService bpmContractService;

    @Idempotent(timeout = 10, timeUnit = TimeUnit.SECONDS, message = "正在审批中，请勿重复提交")
    @PostMapping("/create")
    @Operation(summary = "创建请求申请")
    public CommonResult<String> createProcess(@Valid @RequestBody BpmContractCreateReqVO bpmContractCreateReqVO) {
        return success(bpmContractService.createProcess(getLoginUserId(), bpmContractCreateReqVO));
    }

    /**
     * 退回 - 删除流程 - 推送电子合同 同步状态（修改成草稿箱）
     * @param bpmContractCreateReqVO
     * @return
     */
    @PostMapping("/back")
    public CommonResult<String> backProcess(@Valid @RequestBody BpmContractCreateReqVO bpmContractCreateReqVO) {
        return success(bpmContractService.backProcess(bpmContractCreateReqVO));
    }

    @PostMapping("/page")
    @Operation(summary = "获取审批列表数据")
    public CommonResult<PageResult<BpmContractRespVO>> getApprovePage(@RequestBody BpmContractPageReqVO reqVO) {
        return success(bpmContractService.getApprovePage(reqVO));
    }

    /**
     * 获取任务分页（0=全部 1=已审批 2=未审批）
     */
    @PostMapping("getAutoBpmAllTaskPage")
    @Operation(summary = "获取任务分页（0=全部 1=已审批 2=未审批）")
    public CommonResult<BigBpmContractRespVO> getAutoBpmAllTaskPage(@Valid @RequestBody CommonBpmAutoPageReqVO pageVO) {
        int flag = pageVO.getFlag();
        String loginId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        pageVO.setApplicantId(loginId);
        ApprovePageFlagEnums approvePageFlagEnums = ApprovePageFlagEnums.getInstance(flag);
        switch (approvePageFlagEnums) {
            case ALL:
                return success(bpmContractService.getBpmAllTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
            case DONE:
                return success(bpmContractService.getBpmDoneTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
            case TO_DO:
                return success(bpmContractService.getBpmToDoTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
            default:
                return success(new BigBpmContractRespVO());
        }
    }
}