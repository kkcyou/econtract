package com.yaoan.module.econtract.controller.admin.bpm.archive;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.idempotent.core.annotation.Idempotent;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.bpm.archive.vo.ArchiveBpmReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.archive.vo.BpmContractArchiveRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.archive.vo.PageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.archive.vo.PageRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.BigBpmContractRespVO;
import com.yaoan.module.econtract.enums.payment.ApprovePageFlagEnums;
import com.yaoan.module.econtract.service.bpm.archive.ArchiveBpmService;
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

@Slf4j
@RestController
@RequestMapping("econtract/bpm/archive")
@Validated
@Tag(name = "合同归档工作流模块", description = "合同归档工作流模块操作接口")
public class ArchiveBpmController {
    @Resource
    private ArchiveBpmService archiveBpmService;

    /**
     * 合同归档审批发起
     */
    @PostMapping("/create")
    @Operation(summary = "合同归档请求审批发起")
    @OperateLog(logArgs = false)
    @Idempotent(timeout = 10, timeUnit = TimeUnit.SECONDS, message = "正在审批中，请勿重复提交")
    public CommonResult<String> createProcess(@RequestBody ArchiveBpmReqVO reqVO) {
        return success(archiveBpmService.createProcess(getLoginUserId(), reqVO));
    }

    /**
     * 获取任务分页（0=全部 1=已审批 2=未审批）
     */
    @PostMapping("getAutoBpmAllTaskPage")
    @Operation(summary = "获取任务分页（0=全部 1=已审批 2=未审批）")
    public CommonResult<BpmContractArchiveRespVO> getAutoBpmAllTaskPage(@Valid @RequestBody PageReqVO pageVO) {
        int flag = pageVO.getFlag();
        String loginId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        pageVO.setApplicantId(loginId);
        ApprovePageFlagEnums approvePageFlagEnums = ApprovePageFlagEnums.getInstance(flag);
        switch (approvePageFlagEnums) {
            case ALL:
                return success(archiveBpmService.getBpmAllTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
            case DONE:
                return success(archiveBpmService.getBpmDoneTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
            case TO_DO:
                return success(archiveBpmService.getBpmToDoTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
            default:
                return success(new BpmContractArchiveRespVO());
        }
    }
}
