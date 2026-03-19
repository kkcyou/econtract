package com.yaoan.module.econtract.controller.admin.warningnoticecfg;

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

import com.yaoan.module.econtract.controller.admin.warningnoticecfg.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningnoticecfg.WarningNoticeCfgDO;
import com.yaoan.module.econtract.convert.warningnoticecfg.WarningNoticeCfgConvert;
import com.yaoan.module.econtract.service.warningnoticecfg.WarningNoticeCfgService;

@Tag(name = "管理后台 - 预警通知配置表（new预警）")
@RestController
@RequestMapping("/ecms/warning-notice-cfg")
@Validated
public class WarningNoticeCfgController {

    @Resource
    private WarningNoticeCfgService warningNoticeCfgService;

    /**
     * 创建预警通知配置
     * */
    @PostMapping("/create")
    @Operation(summary = "创建预警通知配置（new预警）")
    @PreAuthorize("@ss.hasPermission('ecms:warning-notice-cfg:create')")
    public CommonResult<String> createWarningNoticeCfg(@Valid @RequestBody WarningNoticeCfgCreateReqVO createReqVO) {
        return success(warningNoticeCfgService.createWarningNoticeCfg(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新预警通知配置表（new预警）")
    @PreAuthorize("@ss.hasPermission('ecms:warning-notice-cfg:update')")
    public CommonResult<Boolean> updateWarningNoticeCfg(@Valid @RequestBody WarningNoticeCfgUpdateReqVO updateReqVO) {
        warningNoticeCfgService.updateWarningNoticeCfg(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除预警通知配置表（new预警）")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ecms:warning-notice-cfg:delete')")
    public CommonResult<Boolean> deleteWarningNoticeCfg(@RequestParam("id") String id) {
        warningNoticeCfgService.deleteWarningNoticeCfg(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得预警通知配置表（new预警）")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ecms:warning-notice-cfg:query')")
    public CommonResult<WarningNoticeCfgRespVO> getWarningNoticeCfg(@RequestParam("id") String id) {
        WarningNoticeCfgDO warningNoticeCfg = warningNoticeCfgService.getWarningNoticeCfg(id);
        return success(WarningNoticeCfgConvert.INSTANCE.convert(warningNoticeCfg));
    }

    @GetMapping("/list")
    @Operation(summary = "获得预警通知配置表（new预警）列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('ecms:warning-notice-cfg:query')")
    public CommonResult<List<WarningNoticeCfgRespVO>> getWarningNoticeCfgList(@RequestParam("ids") Collection<String> ids) {
        List<WarningNoticeCfgDO> list = warningNoticeCfgService.getWarningNoticeCfgList(ids);
        return success(WarningNoticeCfgConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得预警通知配置表（new预警）分页")
    @PreAuthorize("@ss.hasPermission('ecms:warning-notice-cfg:query')")
    public CommonResult<PageResult<WarningNoticeCfgRespVO>> getWarningNoticeCfgPage(@Valid WarningNoticeCfgPageReqVO pageVO) {
        PageResult<WarningNoticeCfgDO> pageResult = warningNoticeCfgService.getWarningNoticeCfgPage(pageVO);
        return success(WarningNoticeCfgConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出预警通知配置表（new预警） Excel")
    @PreAuthorize("@ss.hasPermission('ecms:warning-notice-cfg:export')")
    @OperateLog(type = EXPORT)
    public void exportWarningNoticeCfgExcel(@Valid WarningNoticeCfgExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<WarningNoticeCfgDO> list = warningNoticeCfgService.getWarningNoticeCfgList(exportReqVO);
        // 导出 Excel
        List<WarningNoticeCfgExcelVO> datas = WarningNoticeCfgConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "预警通知配置表（new预警）.xls", "数据", WarningNoticeCfgExcelVO.class, datas);
    }

}
