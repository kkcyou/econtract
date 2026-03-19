package com.yaoan.module.econtract.controller.admin.warningparam;

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

import com.yaoan.module.econtract.controller.admin.warningparam.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningparam.WarningParamDO;
import com.yaoan.module.econtract.convert.warningparam.WarningParamConvert;
import com.yaoan.module.econtract.service.warningparam.WarningParamService;

@Tag(name = "管理后台 - 预警消息模板参数(new预警)")
@RestController
@RequestMapping("/ecms/warning-param")
@Validated
public class WarningParamController {

    @Resource
    private WarningParamService warningParamService;

    /**
     * 获得预警消息模板参数(new预警)分页
     * */
    @PostMapping("/page")
    @Operation(summary = "获得预警消息模板参数(new预警)分页")
    public CommonResult<PageResult<WarningParamRespVO>> getWarningParamPage(@RequestBody WarningParamPageReqVO pageVO) {
        PageResult<WarningParamDO> pageResult = warningParamService.getWarningParamPage(pageVO);
        return success(WarningParamConvert.INSTANCE.convertPage(pageResult));
    }



    @PostMapping("/create")
    @Operation(summary = "创建预警消息模板参数(new预警)")
    // @PreAuthorize("@ss.hasPermission('ecms:warning-param:create')")
    public CommonResult<String> createWarningParam(@Valid @RequestBody WarningParamCreateReqVO createReqVO) {
        return success(warningParamService.createWarningParam(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新预警消息模板参数(new预警)")
    // @PreAuthorize("@ss.hasPermission('ecms:warning-param:update')")
    public CommonResult<Boolean> updateWarningParam(@Valid @RequestBody WarningParamUpdateReqVO updateReqVO) {
        warningParamService.updateWarningParam(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除预警消息模板参数(new预警)")
    @Parameter(name = "id", description = "编号", required = true)
    // @PreAuthorize("@ss.hasPermission('ecms:warning-param:delete')")
    public CommonResult<Boolean> deleteWarningParam(@RequestParam("id") String id) {
        warningParamService.deleteWarningParam(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得预警消息模板参数(new预警)")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    // @PreAuthorize("@ss.hasPermission('ecms:warning-param:query')")
    public CommonResult<WarningParamRespVO> getWarningParam(@RequestParam("id") String id) {
        WarningParamDO warningParam = warningParamService.getWarningParam(id);
        return success(WarningParamConvert.INSTANCE.convert(warningParam));
    }

    @GetMapping("/list")
    @Operation(summary = "获得预警消息模板参数(new预警)列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    // @PreAuthorize("@ss.hasPermission('ecms:warning-param:query')")
    public CommonResult<List<WarningParamRespVO>> getWarningParamList(@RequestParam("ids") Collection<String> ids) {
        List<WarningParamDO> list = warningParamService.getWarningParamList(ids);
        return success(WarningParamConvert.INSTANCE.convertList(list));
    }



    @GetMapping("/export-excel")
    @Operation(summary = "导出预警消息模板参数(new预警) Excel")
    // @PreAuthorize("@ss.hasPermission('ecms:warning-param:export')")
    @OperateLog(type = EXPORT)
    public void exportWarningParamExcel(@Valid WarningParamExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<WarningParamDO> list = warningParamService.getWarningParamList(exportReqVO);
        // 导出 Excel
        List<WarningParamExcelVO> datas = WarningParamConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "预警消息模板参数(new预警).xls", "数据", WarningParamExcelVO.class, datas);
    }

}
