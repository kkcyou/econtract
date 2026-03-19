package com.yaoan.module.econtract.controller.admin.warningmonitor;

import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
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

import com.yaoan.module.econtract.controller.admin.warningmonitor.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningmonitor.WarningMonitorDO;
import com.yaoan.module.econtract.convert.warningmonitor.WarningMonitorConvert;
import com.yaoan.module.econtract.service.warningmonitor.WarningMonitorService;

@Tag(name = "管理后台 - 预警监控项配置表（new预警）")
@RestController
@RequestMapping("/ecms/warning-monitor")
@Validated
public class WarningMonitorController {

    @Resource
    private WarningMonitorService warningMonitorService;

    /**
     * 获得预警监控项配置表（new预警）
     * */
    @PostMapping("/list")
    @Operation(summary = "获得预警监控项配置表（new预警）列表")
    public CommonResult<List<WarningMonitorRespVO>> list(@RequestBody WarningMonitorListReqVO listReqVO) {
        return success(warningMonitorService.list(listReqVO));
    }



    @PostMapping("/create")
    @Operation(summary = "创建预警监控项配置表（new预警）")
    // @PreAuthorize("@ss.hasPermission('ecms:warning-monitor:create')")
    public CommonResult<String> createWarningMonitor(@Valid @RequestBody WarningMonitorCreateReqVO createReqVO) {
        return success(warningMonitorService.createWarningMonitor(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新预警监控项配置表（new预警）")
    // @PreAuthorize("@ss.hasPermission('ecms:warning-monitor:update')")
    public CommonResult<Boolean> updateWarningMonitor(@Valid @RequestBody WarningMonitorUpdateReqVO updateReqVO) {
        warningMonitorService.updateWarningMonitor(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除预警监控项配置表（new预警）")
    @Parameter(name = "id", description = "编号", required = true)
    // @PreAuthorize("@ss.hasPermission('ecms:warning-monitor:delete')")
    public CommonResult<Boolean> deleteWarningMonitor(@RequestParam("id") String id) {
        warningMonitorService.deleteWarningMonitor(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得预警监控项配置表（new预警）")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    // @PreAuthorize("@ss.hasPermission('ecms:warning-monitor:query')")
    public CommonResult<WarningMonitorRespVO> getWarningMonitor(@RequestParam("id") String id) {
        WarningMonitorDO warningMonitor = warningMonitorService.getWarningMonitor(id);
        return success(WarningMonitorConvert.INSTANCE.convert(warningMonitor));
    }



    @GetMapping("/page")
    @Operation(summary = "获得预警监控项配置表（new预警）分页")
    // @PreAuthorize("@ss.hasPermission('ecms:warning-monitor:query')")
    public CommonResult<PageResult<WarningMonitorRespVO>> getWarningMonitorPage(@Valid WarningMonitorPageReqVO pageVO) {
        PageResult<WarningMonitorDO> pageResult = warningMonitorService.getWarningMonitorPage(pageVO);
        return success(WarningMonitorConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出预警监控项配置表（new预警） Excel")
    // @PreAuthorize("@ss.hasPermission('ecms:warning-monitor:export')")
    @OperateLog(type = EXPORT)
    public void exportWarningMonitorExcel(@Valid WarningMonitorExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<WarningMonitorDO> list = warningMonitorService.getWarningMonitorList(exportReqVO);
        // 导出 Excel
        List<WarningMonitorExcelVO> datas = WarningMonitorConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "预警监控项配置表（new预警）.xls", "数据", WarningMonitorExcelVO.class, datas);
    }

}
