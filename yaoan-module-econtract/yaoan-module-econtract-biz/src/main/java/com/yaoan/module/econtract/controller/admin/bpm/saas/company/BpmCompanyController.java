package com.yaoan.module.econtract.controller.admin.bpm.saas.company;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.idempotent.core.annotation.Idempotent;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.bpm.saas.company.vo.BpmCompanyPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.saas.company.vo.BpmCompanyPageRespVO;
import com.yaoan.module.econtract.service.bpm.saas.company.BpmCompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.concurrent.TimeUnit;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-28 16:27
 */
@Slf4j
@RestController
@RequestMapping("econtract/bpm/company")
@Validated
@Tag(name = "加入公司申请的工作流", description = "加入公司申请的工作流")
public class BpmCompanyController {
    @Resource
    private BpmCompanyService bpmCompanyService;

    /**
     * 发起加入公司申请
     */
    @PostMapping("/submit")
    @Operation(summary = "发起加入公司申请")
    @OperateLog(logArgs = false)
    @PermitAll
    @Idempotent(timeout = 10, timeUnit = TimeUnit.SECONDS, message = "正在审批中，请勿重复提交")
    public CommonResult<String> submit(@Valid @RequestBody BpmCompanySubmitReqVO reqVO) {
        return success(bpmCompanyService.submit(reqVO));
    }

    /**
     * 审批列表（加入公司申请）
     */
    @GetMapping("/page")
    @Operation(summary = "审批列表（加入公司申请）")
    @OperateLog(logArgs = false)
//    @Idempotent(timeout = 10, timeUnit = TimeUnit.SECONDS, message = "正在审批中，请勿重复提交")
    public CommonResult<PageResult<BpmCompanyPageRespVO>> page(@Valid  BpmCompanyPageReqVO reqVO) {
        return success(bpmCompanyService.page(reqVO));
    }

}
