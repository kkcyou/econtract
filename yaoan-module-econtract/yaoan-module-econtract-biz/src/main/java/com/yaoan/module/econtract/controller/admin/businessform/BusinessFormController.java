package com.yaoan.module.econtract.controller.admin.businessform;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.controller.admin.businessform.vo.BusinessFormPageReqVO;
import com.yaoan.module.econtract.controller.admin.businessform.vo.BusinessFormRespVO;
import com.yaoan.module.econtract.controller.admin.businessform.vo.BusinessFormSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.businessform.BusinessFormDO;
import com.yaoan.module.econtract.service.businessform.BusinessFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.yaoan.framework.common.pojo.CommonResult.success;


@Tag(name = "管理后台 - 业务表单")
@RestController
@RequestMapping("/ecms/business-form")
@Validated
public class BusinessFormController {

    @Resource
    private BusinessFormService businessFormService;

    @PostMapping("/create")
    @Operation(summary = "创建业务表单")
  //  @PreAuthorize("@ss.hasPermission('ecms:business-form:create')")
    public CommonResult<String> createBusinessForm(@Valid @RequestBody BusinessFormSaveReqVO createReqVO) {
        return success(businessFormService.createBusinessForm(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新业务表单")
  //  @PreAuthorize("@ss.hasPermission('ecms:business-form:update')")
    public CommonResult<Boolean> updateBusinessForm(@Valid @RequestBody BusinessFormSaveReqVO updateReqVO) {
        businessFormService.updateBusinessForm(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除业务表单")
    @Parameter(name = "id", description = "编号", required = true)
   // @PreAuthorize("@ss.hasPermission('ecms:business-form:delete')")
    public CommonResult<Boolean> deleteBusinessForm(@RequestParam("id") String id) {
        businessFormService.deleteBusinessForm(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得业务表单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
  //  @PreAuthorize("@ss.hasPermission('ecms:business-form:query')")
    public CommonResult<BusinessFormRespVO> getBusinessForm(@RequestParam("id") String id) {
        BusinessFormDO businessForm = businessFormService.getBusinessForm(id);
        return success(BeanUtils.toBean(businessForm, BusinessFormRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得业务表单分页")
   // @PreAuthorize("@ss.hasPermission('ecms:business-form:query')")
    public CommonResult<PageResult<BusinessFormRespVO>> getBusinessFormPage(@Valid BusinessFormPageReqVO pageReqVO) {
        PageResult<BusinessFormDO> pageResult = businessFormService.getBusinessFormPage(pageReqVO);
        
        
        return success(BeanUtils.toBean(pageResult, BusinessFormRespVO.class));
    }

//    @GetMapping("/export-excel")
//    @Operation(summary = "导出业务表单 Excel")
//    @PreAuthorize("@ss.hasPermission('ecms:business-form:export')")
//    @ApiAccessLog(operateType = EXPORT)
//    public void exportBusinessFormExcel(@Valid BusinessFormPageReqVO pageReqVO,
//              HttpServletResponse response) throws IOException {
//        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
//        List<BusinessFormDO> list = businessFormService.getBusinessFormPage(pageReqVO).getList();
//        // 导出 Excel
//        ExcelUtils.write(response, "业务表单.xls", "数据", BusinessFormRespVO.class,
//                        BeanUtils.toBean(list, BusinessFormRespVO.class));
//    }

}