package com.yaoan.module.econtract.controller.admin.warningitemrule;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.pojo.CommonResult;
import static com.yaoan.framework.common.pojo.CommonResult.success;

import com.yaoan.framework.excel.core.util.ExcelUtils;

import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import static com.yaoan.framework.operatelog.core.enums.OperateTypeEnum.*;

import com.yaoan.module.econtract.controller.admin.warningitemrule.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningitemrule.WarningItemRuleDO;
import com.yaoan.module.econtract.convert.warningitemrule.WarningItemRuleConvert;
import com.yaoan.module.econtract.service.warningitemrule.WarningItemRuleService;

@Tag(name = "管理后台 - 预警规则（new预警）")
@RestController
@RequestMapping("/ecms/warning-item-rule")
@Validated
public class WarningItemRuleController {

    @Resource
    private WarningItemRuleService warningItemRuleService;

    /**
     * 创建预警规则（new预警）
     * */
    @PostMapping("/create")
    @Operation(summary = "创建预警规则（new预警）")
    public CommonResult<String> createWarningItemRule(@Valid @RequestBody WarningItemRuleCreateReqVO createReqVO) {
        return success(warningItemRuleService.createWarningItemRule(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新预警规则（new预警）")
    @PreAuthorize("@ss.hasPermission('ecms:warning-item-rule:update')")
    public CommonResult<Boolean> updateWarningItemRule(@Valid @RequestBody WarningItemRuleUpdateReqVO updateReqVO) {
        warningItemRuleService.updateWarningItemRule(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除预警规则（new预警）")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ecms:warning-item-rule:delete')")
    public CommonResult<Boolean> deleteWarningItemRule(@RequestParam("id") String id) {
        warningItemRuleService.deleteWarningItemRule(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得预警规则（new预警）")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ecms:warning-item-rule:query')")
    public CommonResult<WarningItemRuleRespVO> getWarningItemRule(@RequestParam("id") String id) {
        WarningItemRuleDO warningItemRule = warningItemRuleService.getWarningItemRule(id);
        return success(WarningItemRuleConvert.INSTANCE.convert(warningItemRule));
    }

    @GetMapping("/list")
    @Operation(summary = "获得预警规则（new预警）列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('ecms:warning-item-rule:query')")
    public CommonResult<List<WarningItemRuleRespVO>> getWarningItemRuleList(@RequestParam("ids") Collection<String> ids) {
        List<WarningItemRuleDO> list = warningItemRuleService.getWarningItemRuleList(ids);
        return success(WarningItemRuleConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得预警规则（new预警）分页")
    @PreAuthorize("@ss.hasPermission('ecms:warning-item-rule:query')")
    public CommonResult<PageResult<WarningItemRuleRespVO>> getWarningItemRulePage(@Valid WarningItemRulePageReqVO pageVO) {
        PageResult<WarningItemRuleDO> pageResult = warningItemRuleService.getWarningItemRulePage(pageVO);
        return success(WarningItemRuleConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出预警规则（new预警） Excel")
    @PreAuthorize("@ss.hasPermission('ecms:warning-item-rule:export')")
    @OperateLog(type = EXPORT)
    public void exportWarningItemRuleExcel(@Valid WarningItemRuleExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<WarningItemRuleDO> list = warningItemRuleService.getWarningItemRuleList(exportReqVO);
        // 导出 Excel
        List<WarningItemRuleExcelVO> datas = WarningItemRuleConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "预警规则（new预警）.xls", "数据", WarningItemRuleExcelVO.class, datas);
    }

}
