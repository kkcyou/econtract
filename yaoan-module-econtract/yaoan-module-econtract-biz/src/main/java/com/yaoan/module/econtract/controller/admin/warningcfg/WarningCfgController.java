package com.yaoan.module.econtract.controller.admin.warningcfg;

import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.*;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.page.WarningPageRespVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.update.WarningCfgTurnReqVO;
import com.yaoan.module.econtract.convert.warningcfg.WarningCfgConvert;
import com.yaoan.module.econtract.dal.dataobject.warningcfg.WarningCfgDO;
import com.yaoan.module.econtract.service.warningcfg.WarningCfgService;
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

@Tag(name = "管理后台 - 预警检查配置表(new预警)")
@RestController
@RequestMapping("/ecms/warning-cfg")
@Validated
public class WarningCfgController {

    @Resource
    private WarningCfgService warningCfgService;

    /**
     * 创建预警检查点(new预警)
     * */
    @PostMapping("/create")
    @Operation(summary = "创建预警检查点(new预警)")
    public CommonResult<String> createWarningCfg(@Valid @RequestBody WarningCfgCreateReqVO createReqVO) {
        return success(warningCfgService.createWarningCfg(createReqVO));
    }

    /**
     * 检查点详情(new预警)
     * */
    @GetMapping("/get")
    @Operation(summary = "检查点详情(new预警)")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<WarningCfgRespVO> getWarningCfg(@RequestParam("id") String id) {
        return success(warningCfgService.getWarningCfg(id));
    }

    /**
     * 检查点启用停用(new预警)
     * */
    @PostMapping("/updateStatus")
    @Operation(summary = "检查点启用停用(new预警)")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<String> updateStatus(@RequestBody WarningCfgTurnReqVO reqVO) {
        warningCfgService.updateStatus(reqVO);
        return success("success");
    }



    /**
     * 删除预警检查点(new预警)
     * */
    @DeleteMapping("/delete")
    @Operation(summary = "删除预警检查点(new预警)")
    @Parameter(name = "id", description = "编号", required = true)
//    @PreAuthorize("@ss.hasPermission('ecms:warning-cfg:delete')")
    public CommonResult<Boolean> deleteWarningCfg(@RequestParam("id") String id) {
        warningCfgService.deleteWarningCfg(id);
        return success(true);
    }

    /**
     * 获得预警检查点(new预警)分页
     * */
    @PostMapping("/page")
    @Operation(summary = "获得预警检查点(new预警)分页")
    public CommonResult<PageResult<WarningPageRespVO>> getWarningCfgPage(@RequestBody WarningCfgPageReqVO pageVO) {
        return success(warningCfgService.getWarningCfgPage(pageVO));
    }







    @PutMapping("/update")
    @Operation(summary = "更新预警检查配置表(new预警)")
    @PreAuthorize("@ss.hasPermission('ecms:warning-cfg:update')")
    public CommonResult<Boolean> updateWarningCfg(@Valid @RequestBody WarningCfgUpdateReqVO updateReqVO) {
        warningCfgService.updateWarningCfg(updateReqVO);
        return success(true);
    }





    @GetMapping("/list")
    @Operation(summary = "获得预警检查配置表(new预警)列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('ecms:warning-cfg:query')")
    public CommonResult<List<WarningCfgRespVO>> getWarningCfgList(@RequestParam("ids") Collection<String> ids) {
        List<WarningCfgDO> list = warningCfgService.getWarningCfgList(ids);
        return success(WarningCfgConvert.INSTANCE.convertList(list));
    }



    @GetMapping("/export-excel")
    @Operation(summary = "导出预警检查配置表(new预警) Excel")
    @PreAuthorize("@ss.hasPermission('ecms:warning-cfg:export')")
    @OperateLog(type = EXPORT)
    public void exportWarningCfgExcel(@Valid WarningCfgExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<WarningCfgDO> list = warningCfgService.getWarningCfgList(exportReqVO);
        // 导出 Excel
        List<WarningCfgExcelVO> datas = WarningCfgConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "预警检查配置表(new预警).xls", "数据", WarningCfgExcelVO.class, datas);
    }

}
