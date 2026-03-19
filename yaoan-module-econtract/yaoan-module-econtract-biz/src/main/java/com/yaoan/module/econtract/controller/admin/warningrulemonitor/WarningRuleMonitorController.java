package com.yaoan.module.econtract.controller.admin.warningrulemonitor;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

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

import com.yaoan.module.econtract.controller.admin.warningrulemonitor.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningrulemonitor.WarningRuleMonitorRelDO;
import com.yaoan.module.econtract.convert.warningrulemonitor.WarningRuleMonitorConvert;
import com.yaoan.module.econtract.service.warningrulemonitorrel.WarningRuleMonitorRelService;

@Tag(name = "管理后台 - 预警规则与监控项关联关系表（new预警）")
@RestController
@RequestMapping("/ecms/warning-rule-monitor")
@Validated
public class WarningRuleMonitorController {

    @Resource
    private WarningRuleMonitorRelService warningRuleMonitorService;

    @PostMapping("/create")
    @Operation(summary = "创建预警规则与监控项关联关系表（new预警）")
    @PreAuthorize("@ss.hasPermission('ecms:warning-rule-monitor:create')")
    public CommonResult<String> createWarningRuleMonitor(@Valid @RequestBody WarningRuleMonitorCreateReqVO createReqVO) {
        return success(warningRuleMonitorService.createWarningRuleMonitor(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新预警规则与监控项关联关系表（new预警）")
    @PreAuthorize("@ss.hasPermission('ecms:warning-rule-monitor:update')")
    public CommonResult<Boolean> updateWarningRuleMonitor(@Valid @RequestBody WarningRuleMonitorUpdateReqVO updateReqVO) {
        warningRuleMonitorService.updateWarningRuleMonitor(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除预警规则与监控项关联关系表（new预警）")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ecms:warning-rule-monitor:delete')")
    public CommonResult<Boolean> deleteWarningRuleMonitor(@RequestParam("id") String id) {
        warningRuleMonitorService.deleteWarningRuleMonitor(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得预警规则与监控项关联关系表（new预警）")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ecms:warning-rule-monitor:query')")
    public CommonResult<WarningRuleMonitorRespVO> getWarningRuleMonitor(@RequestParam("id") String id) {
        WarningRuleMonitorRelDO warningRuleMonitor = warningRuleMonitorService.getWarningRuleMonitor(id);
        return success(WarningRuleMonitorConvert.INSTANCE.convert(warningRuleMonitor));
    }

    @GetMapping("/list")
    @Operation(summary = "获得预警规则与监控项关联关系表（new预警）列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('ecms:warning-rule-monitor:query')")
    public CommonResult<List<WarningRuleMonitorRespVO>> getWarningRuleMonitorList(@RequestParam("ids") Collection<String> ids) {
        List<WarningRuleMonitorRelDO> list = warningRuleMonitorService.getWarningRuleMonitorList(ids);
        return success(WarningRuleMonitorConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得预警规则与监控项关联关系表（new预警）分页")
    @PreAuthorize("@ss.hasPermission('ecms:warning-rule-monitor:query')")
    public CommonResult<PageResult<WarningRuleMonitorRespVO>> getWarningRuleMonitorPage(@Valid WarningRuleMonitorPageReqVO pageVO) {
        PageResult<WarningRuleMonitorRelDO> pageResult = warningRuleMonitorService.getWarningRuleMonitorPage(pageVO);
        return success(WarningRuleMonitorConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出预警规则与监控项关联关系表（new预警） Excel")
    @PreAuthorize("@ss.hasPermission('ecms:warning-rule-monitor:export')")
    @OperateLog(type = EXPORT)
    public void exportWarningRuleMonitorExcel(@Valid WarningRuleMonitorExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<WarningRuleMonitorRelDO> list = warningRuleMonitorService.getWarningRuleMonitorList(exportReqVO);
        // 导出 Excel
        List<WarningRuleMonitorExcelVO> datas = WarningRuleMonitorConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "预警规则与监控项关联关系表（new预警）.xls", "数据", WarningRuleMonitorExcelVO.class, datas);
    }

}
