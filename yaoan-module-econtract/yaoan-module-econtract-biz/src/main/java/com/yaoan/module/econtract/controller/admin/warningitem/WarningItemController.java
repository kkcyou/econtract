package com.yaoan.module.econtract.controller.admin.warningitem;

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

import com.yaoan.module.econtract.controller.admin.warningitem.vo.*;
import com.yaoan.module.econtract.dal.dataobject.warningitem.WarningItemDO;
import com.yaoan.module.econtract.convert.warningitem.WarningItemConvert;
import com.yaoan.module.econtract.service.warningitem.WarningItemService;

@Tag(name = "管理后台 - 预警事项表（new预警）")
@RestController
@RequestMapping("/ecms/warning-item")
@Validated
public class WarningItemController {

    @Resource
    private WarningItemService warningItemService;

    /**
     * 创建预警事项表
     */
    @PostMapping("/create")
    @Operation(summary = "创建预警事项表（new预警）")
    public CommonResult<String> createWarningItem(@Valid @RequestBody WarningItemCreateReqVO createReqVO) {
        return success(warningItemService.createWarningItem(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新预警事项表（new预警）")
    @PreAuthorize("@ss.hasPermission('ecms:warning-item:update')")
    public CommonResult<Boolean> updateWarningItem(@Valid @RequestBody WarningItemUpdateReqVO updateReqVO) {
        warningItemService.updateWarningItem(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除预警事项表（new预警）")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ecms:warning-item:delete')")
    public CommonResult<Boolean> deleteWarningItem(@RequestParam("id") String id) {
        warningItemService.deleteWarningItem(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得预警事项表（new预警）")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ecms:warning-item:query')")
    public CommonResult<WarningItemRespVO> getWarningItem(@RequestParam("id") String id) {
        WarningItemDO warningItem = warningItemService.getWarningItem(id);
        return success(WarningItemConvert.INSTANCE.convert(warningItem));
    }

    @GetMapping("/list")
    @Operation(summary = "获得预警事项表（new预警）列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('ecms:warning-item:query')")
    public CommonResult<List<WarningItemRespVO>> getWarningItemList(@RequestParam("ids") Collection<String> ids) {
        List<WarningItemDO> list = warningItemService.getWarningItemList(ids);
        return success(WarningItemConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得预警事项表（new预警）分页")
    @PreAuthorize("@ss.hasPermission('ecms:warning-item:query')")
    public CommonResult<PageResult<WarningItemRespVO>> getWarningItemPage(@Valid WarningItemPageReqVO pageVO) {
        PageResult<WarningItemDO> pageResult = warningItemService.getWarningItemPage(pageVO);
        return success(WarningItemConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出预警事项表（new预警） Excel")
    @PreAuthorize("@ss.hasPermission('ecms:warning-item:export')")
    @OperateLog(type = EXPORT)
    public void exportWarningItemExcel(@Valid WarningItemExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<WarningItemDO> list = warningItemService.getWarningItemList(exportReqVO);
        // 导出 Excel
        List<WarningItemExcelVO> datas = WarningItemConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "预警事项表（new预警）.xls", "数据", WarningItemExcelVO.class, datas);
    }

}
