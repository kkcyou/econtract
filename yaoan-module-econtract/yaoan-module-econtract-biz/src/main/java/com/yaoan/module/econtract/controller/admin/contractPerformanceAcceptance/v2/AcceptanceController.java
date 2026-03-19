package com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.AcceptancePageReqVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.AcceptanceRespVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.query.AcceptanceQueryRespVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.save.AcceptanceCreateReqVO;
import com.yaoan.module.econtract.service.contractPerformanceAcceptance.v2.AcceptanceV2Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description:
 * @author: Pele
 * @date: 2025/4/21 17:55
 */
@Tag(name = "管理后台 - 验收v2")
@RestController
@RequestMapping("/acceptance/v2")
@Validated
public class AcceptanceController {
    @Resource
    private AcceptanceV2Service acceptanceV2Service;

    @PostMapping("/page")
    @Operation(summary = "获得验收分页")
    public CommonResult<PageResult<AcceptanceRespVO>> page(@Valid @RequestBody AcceptancePageReqVO reqVO) {
        PageResult<AcceptanceRespVO> pageResult = acceptanceV2Service.page(reqVO);
        return success(pageResult);
    }

    @PostMapping("/save")
    @Operation(summary = "保存验收申请")
    public CommonResult<String> save(@Valid @RequestBody AcceptanceCreateReqVO reqVO) {
        return success( acceptanceV2Service.save(reqVO));
    }

    @GetMapping("/get")
    @Operation(summary = "查看验收申请")
    public CommonResult<AcceptanceQueryRespVO> get(@RequestParam("id") String id) {
        return success( acceptanceV2Service.get(id));
    }



}
