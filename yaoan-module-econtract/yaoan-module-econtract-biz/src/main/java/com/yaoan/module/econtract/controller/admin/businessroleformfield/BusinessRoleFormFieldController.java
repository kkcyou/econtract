package com.yaoan.module.econtract.controller.admin.businessroleformfield;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.controller.admin.businessroleformfield.vo.*;
import com.yaoan.module.econtract.dal.dataobject.businessroleformfield.BusinessRoleFormFieldDO;
import com.yaoan.module.econtract.service.businessroleformfield.BusinessRoleFormFieldService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 角色字段关系")
@RestController
@RequestMapping("/ecms/business-role-form-field")
@Validated
public class BusinessRoleFormFieldController {

    @Resource
    private BusinessRoleFormFieldService businessRoleFormFieldService;

    @PostMapping("/create")
    @Operation(summary = "创建角色字段关系")
    public CommonResult<String> createBusinessRoleFormField(@Valid @RequestBody BusinessRoleFormFieldSaveReqVO createReqVO) {
        return success(businessRoleFormFieldService.createBusinessRoleFormField(createReqVO));
    }

    @PostMapping("/batchSave")
    @Operation(summary = "批量保存角色和字段的关系")
    public CommonResult<String> createBusinessRoleFormField(@Valid @RequestBody BusinessRoleFormFieldBatchSaveReqVO businessRoleFormFieldBatchSaveReqVO) {
        return success(businessRoleFormFieldService.batchCreateBusinessRoleFormField(businessRoleFormFieldBatchSaveReqVO));
    }
    
    @PutMapping("/update")
    @Operation(summary = "更新角色字段关系")
    public CommonResult<Boolean> updateBusinessRoleFormField(@Valid @RequestBody BusinessRoleFormFieldSaveReqVO updateReqVO) {
        businessRoleFormFieldService.updateBusinessRoleFormField(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除角色字段关系")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteBusinessRoleFormField(@RequestParam("id") String id) {
        businessRoleFormFieldService.deleteBusinessRoleFormField(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得角色字段关系")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<BusinessRoleFormFieldRespVO> getBusinessRoleFormField(@RequestParam("id") String id) {
        BusinessRoleFormFieldDO businessRoleFormField = businessRoleFormFieldService.getBusinessRoleFormField(id);
        return success(BeanUtils.toBean(businessRoleFormField, BusinessRoleFormFieldRespVO.class));
    }

    @GetMapping("/getFieldByBusinessTypeAndRole")
    @Operation(summary = "根据角色和业务获取字段")
    public CommonResult<List<BusinessRoleFormFieldRespVO>> getFieldsByBusinessTypeAndRole(@Valid BusinessRoleFormFieldReqVO reqVO) {
        List<BusinessRoleFormFieldDO> businessRoleFormField = businessRoleFormFieldService.getFieldsByBusinessTypeAndRole(reqVO);
        return success(BeanUtils.toBean(businessRoleFormField, BusinessRoleFormFieldRespVO.class));
    }
    
    @GetMapping("/page")
    @Operation(summary = "获得角色字段关系分页")
    public CommonResult<PageResult<BusinessRoleFormFieldRespVO>> getBusinessRoleFormFieldPage(@Valid BusinessRoleFormFieldPageReqVO pageReqVO) {
        PageResult<BusinessRoleFormFieldDO> pageResult = businessRoleFormFieldService.getBusinessRoleFormFieldPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BusinessRoleFormFieldRespVO.class));
    }

   

}