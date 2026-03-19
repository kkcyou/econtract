package com.yaoan.module.econtract.controller.admin.businessformfield;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.controller.admin.businessformfield.vo.BusinessFormFieldPageReqVO;
import com.yaoan.module.econtract.controller.admin.businessformfield.vo.BusinessFormFieldRespVO;
import com.yaoan.module.econtract.controller.admin.businessformfield.vo.BusinessFormFieldSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.businessformfield.BusinessFormFieldDO;
import com.yaoan.module.econtract.service.businessformfield.BusinessFormFieldService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.yaoan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 表单字段")
@RestController
@RequestMapping("/ecms/business-form-field")
@Validated
public class BusinessFormFieldController {

    @Resource
    private BusinessFormFieldService businessFormFieldService;

    @PostMapping("/create")
    @Operation(summary = "创建表单字段")
   // @PreAuthorize("@ss.hasPermission('ecms:business-form-field:create')")
    public CommonResult<String> createBusinessFormField(@Valid @RequestBody BusinessFormFieldSaveReqVO createReqVO) {
        return success(businessFormFieldService.createBusinessFormField(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新表单字段")
    //@PreAuthorize("@ss.hasPermission('ecms:business-form-field:update')")
    public CommonResult<Boolean> updateBusinessFormField(@Valid @RequestBody BusinessFormFieldSaveReqVO updateReqVO) {
        businessFormFieldService.updateBusinessFormField(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除表单字段")
    @Parameter(name = "id", description = "编号", required = true)
   // @PreAuthorize("@ss.hasPermission('ecms:business-form-field:delete')")
    public CommonResult<Boolean> deleteBusinessFormField(@RequestParam("id") String id) {
        businessFormFieldService.deleteBusinessFormField(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得表单字段")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
   // @PreAuthorize("@ss.hasPermission('ecms:business-form-field:query')")
    public CommonResult<BusinessFormFieldRespVO> getBusinessFormField(@RequestParam("id") String id) {
        BusinessFormFieldDO businessFormField = businessFormFieldService.getBusinessFormField(id);
        return success(BeanUtils.toBean(businessFormField, BusinessFormFieldRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得表单字段分页")
  //  @PreAuthorize("@ss.hasPermission('ecms:business-form-field:query')")
    public CommonResult<PageResult<BusinessFormFieldRespVO>> getBusinessFormFieldPage(@Valid BusinessFormFieldPageReqVO pageReqVO) {
        PageResult<BusinessFormFieldDO> pageResult = businessFormFieldService.getBusinessFormFieldPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BusinessFormFieldRespVO.class));
    }

    
}