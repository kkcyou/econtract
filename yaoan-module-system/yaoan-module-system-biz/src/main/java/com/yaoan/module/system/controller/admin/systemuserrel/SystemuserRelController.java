package com.yaoan.module.system.controller.admin.systemuserrel;


import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.excel.core.util.ExcelUtils;
import com.yaoan.module.system.controller.admin.systemuserrel.vo.*;
import com.yaoan.module.system.convert.systemuserrel.SystemuserRelConvert;
import com.yaoan.module.system.dal.dataobject.systemuserrel.SystemuserRelDO;
import com.yaoan.module.system.service.systemuserrel.SystemuserRelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 系统对接用户关系")
@RestController
@RequestMapping("/ecms/systemuser-rel")
@Validated
public class SystemuserRelController {

    @Resource
    private SystemuserRelService systemuserRelService;

    @PostMapping("/create")
    @Operation(summary = "创建系统对接用户关系")
    @PreAuthorize("@ss.hasPermission('ecms:systemuser-rel:create')")
    public CommonResult<String> createSystemuserRel(@Valid @RequestBody SystemuserRelCreateReqVO createReqVO) {
        return success(systemuserRelService.createSystemuserRel(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新系统对接用户关系")
    @PreAuthorize("@ss.hasPermission('ecms:systemuser-rel:update')")
    public CommonResult<Boolean> updateSystemuserRel(@Valid @RequestBody SystemuserRelUpdateReqVO updateReqVO) {
        systemuserRelService.updateSystemuserRel(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除系统对接用户关系")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ecms:systemuser-rel:delete')")
    public CommonResult<Boolean> deleteSystemuserRel(@RequestParam("id") String id) {
        systemuserRelService.deleteSystemuserRel(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得系统对接用户关系")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ecms:systemuser-rel:query')")
    public CommonResult<SystemuserRelRespVO> getSystemuserRel(@RequestParam("id") String id) {
        SystemuserRelDO systemuserRel = systemuserRelService.getSystemuserRel(id);
        return success(SystemuserRelConvert.INSTANCE.convert(systemuserRel));
    }

    @GetMapping("/list")
    @Operation(summary = "获得系统对接用户关系列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('ecms:systemuser-rel:query')")
    public CommonResult<List<SystemuserRelRespVO>> getSystemuserRelList(@RequestParam("ids") Collection<String> ids) {
        List<SystemuserRelDO> list = systemuserRelService.getSystemuserRelList(ids);
        return success(SystemuserRelConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得系统对接用户关系分页")
    @PreAuthorize("@ss.hasPermission('ecms:systemuser-rel:query')")
    public CommonResult<PageResult<SystemuserRelRespVO>> getSystemuserRelPage(@Valid SystemuserRelPageReqVO pageVO) {
        PageResult<SystemuserRelDO> pageResult = systemuserRelService.getSystemuserRelPage(pageVO);
        return success(SystemuserRelConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出系统对接用户关系 Excel")
    public void exportSystemuserRelExcel(@Valid SystemuserRelExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<SystemuserRelDO> list = systemuserRelService.getSystemuserRelList(exportReqVO);
        // 导出 Excel
        List<SystemuserRelExcelVO> datas = SystemuserRelConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "系统对接用户关系.xls", "数据", SystemuserRelExcelVO.class, datas);
    }

}
