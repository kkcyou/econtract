package com.yaoan.module.econtract.controller.admin.businessfile;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.excel.core.util.ExcelUtils;
import com.yaoan.module.econtract.controller.admin.businessfile.vo.BusinessFilePageReqVO;
import com.yaoan.module.econtract.controller.admin.businessfile.vo.BusinessFileRespVO;
import com.yaoan.module.econtract.controller.admin.businessfile.vo.BusinessFileSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import com.yaoan.module.econtract.service.businessfile.BusinessFileService;
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
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;


@Tag(name = "管理后台 - 业务数据和附件关联关系")
@RestController
@RequestMapping("/econtract/business-file")
@Validated
public class BusinessFileController {

    @Resource
    private BusinessFileService businessFileService;

    @PostMapping("/create")
    @Operation(summary = "创建业务数据和附件关联关系")
    @PreAuthorize("@ss.hasPermission('econtract:business-file:create')")
    public CommonResult<String> createBusinessFile(@Valid @RequestBody BusinessFileSaveReqVO createReqVO) {
        return success(businessFileService.createBusinessFile(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新业务数据和附件关联关系")
    @PreAuthorize("@ss.hasPermission('econtract:business-file:update')")
    public CommonResult<Boolean> updateBusinessFile(@Valid @RequestBody BusinessFileSaveReqVO updateReqVO) {
        businessFileService.updateBusinessFile(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除业务数据和附件关联关系")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('econtract:business-file:delete')")
    public CommonResult<Boolean> deleteBusinessFile(@RequestParam("id") String id) {
        businessFileService.deleteBusinessFile(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得业务数据和附件关联关系")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('econtract:business-file:query')")
    public CommonResult<BusinessFileRespVO> getBusinessFile(@RequestParam("id") String id) {
        BusinessFileDO businessFile = businessFileService.getBusinessFile(id);
        return success(BeanUtils.toBean(businessFile, BusinessFileRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得业务数据和附件关联关系分页")
    @PreAuthorize("@ss.hasPermission('econtract:business-file:query')")
    public CommonResult<PageResult<BusinessFileRespVO>> getBusinessFilePage(@Valid BusinessFilePageReqVO pageReqVO) {
        PageResult<BusinessFileDO> pageResult = businessFileService.getBusinessFilePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BusinessFileRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出业务数据和附件关联关系 Excel")
    @PreAuthorize("@ss.hasPermission('econtract:business-file:export')")
    public void exportBusinessFileExcel(@Valid BusinessFilePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<BusinessFileDO> list = businessFileService.getBusinessFilePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "业务数据和附件关联关系.xls", "数据", BusinessFileRespVO.class,
                        BeanUtils.toBean(list, BusinessFileRespVO.class));
    }

}