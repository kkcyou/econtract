package com.yaoan.module.system.controller.admin.econtractorg;

import com.yaoan.module.econtract.api.gcy.buyplan.dto.EncryptDTO;
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

import com.yaoan.module.system.controller.admin.econtractorg.vo.*;
import com.yaoan.module.system.dal.dataobject.econtractorg.EcontractOrgDO;
import com.yaoan.module.system.convert.econtractorg.EcontractOrgConvert;
import com.yaoan.module.system.service.econtractorg.EcontractOrgService;

@Tag(name = "管理后台 - 电子合同单位信息")
@RestController
@RequestMapping("/system/econtract-org")
@Validated
public class EcontractOrgController {

    @Resource
    private EcontractOrgService econtractOrgService;

    @PostMapping("/create")
    @Operation(summary = "创建电子合同单位信息")
    @PreAuthorize("@ss.hasPermission('system:econtract-org:create')")
    public CommonResult<String> createEcontractOrg(@Valid @RequestBody EcontractOrgSaveReqVO createReqVO) {
        return success(econtractOrgService.createEcontractOrg(createReqVO));
    }

    @PostMapping("/save")
    @Operation(summary = "保存电子合同单位信息")
    public CommonResult<Boolean> saveEcontractOrg(@Valid @RequestBody EcontractOrgSaveReqVO saveReqVO) {
        econtractOrgService.saveEcontractOrg(saveReqVO);
        return success(true);
    }

    @PutMapping("/update")
    @Operation(summary = "更新电子合同单位信息")
    @PreAuthorize("@ss.hasPermission('system:econtract-org:update')")
    public CommonResult<Boolean> updateEcontractOrg(@Valid @RequestBody EcontractOrgSaveReqVO updateReqVO) {
        econtractOrgService.updateEcontractOrg(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除电子合同单位信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('system:econtract-org:delete')")
    public CommonResult<Boolean> deleteEcontractOrg(@RequestParam("id") String id) {
        econtractOrgService.deleteEcontractOrg(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得电子合同单位信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:econtract-org:query')")
    public CommonResult<EcontractOrgRespVO> getEcontractOrg(@RequestParam("id") String id) {
        EcontractOrgDO econtractOrg = econtractOrgService.getEcontractOrg(id);
        return success(EcontractOrgConvert.INSTANCE.convert(econtractOrg));
    }

    @GetMapping("/list")
    @Operation(summary = "获得电子合同单位信息列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:econtract-org:query')")
    public CommonResult<List<EcontractOrgRespVO>> getEcontractOrgList(@RequestParam("ids") Collection<String> ids) {
        List<EcontractOrgDO> list = econtractOrgService.getEcontractOrgList(ids);
        return success(EcontractOrgConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得电子合同单位信息分页")
    @PreAuthorize("@ss.hasPermission('system:econtract-org:query')")
    public CommonResult<PageResult<EcontractOrgRespVO>> getEcontractOrgPage(@Valid EcontractOrgPageReqVO pageVO) {
        PageResult<EcontractOrgDO> pageResult = econtractOrgService.getEcontractOrgPage(pageVO);
        return success(EcontractOrgConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出电子合同单位信息 Excel")
    @PreAuthorize("@ss.hasPermission('system:econtract-org:export')")
    @OperateLog(type = EXPORT)
    public void exportEcontractOrgExcel(@Valid EcontractOrgExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<EcontractOrgDO> list = econtractOrgService.getEcontractOrgList(exportReqVO);
        // 导出 Excel
        List<EcontractOrgExcelVO> datas = EcontractOrgConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "电子合同单位信息.xls", "数据", EcontractOrgExcelVO.class, datas);
    }

}
