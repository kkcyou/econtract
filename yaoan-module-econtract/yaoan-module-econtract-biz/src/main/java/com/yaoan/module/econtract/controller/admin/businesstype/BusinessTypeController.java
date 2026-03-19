package com.yaoan.module.econtract.controller.admin.businesstype;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.controller.admin.businesstype.vo.BusinessTypePageReqVO;
import com.yaoan.module.econtract.controller.admin.businesstype.vo.BusinessTypeRespVO;
import com.yaoan.module.econtract.controller.admin.businesstype.vo.BusinessTypeSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.businesstype.BusinessTypeDO;
import com.yaoan.module.econtract.service.businesstype.BusinessTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.yaoan.framework.common.pojo.CommonResult.success;


@Tag(name = "管理后台 - 业务类型")
@RestController
@RequestMapping("/ecms/business-type")
@Validated
public class BusinessTypeController {

    @Resource
    private BusinessTypeService businessTypeService;

    @PostMapping("/create")
    @Operation(summary = "创建业务类型")
    //@PreAuthorize("@ss.hasPermission('ecms:business-type:create')")
    public CommonResult<String> createBusinessType(@Valid @RequestBody BusinessTypeSaveReqVO createReqVO) {
        return success(businessTypeService.createBusinessType(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新业务类型")
//@PreAuthorize("@ss.hasPermission('ecms:business-type:update')")
    public CommonResult<Boolean> updateBusinessType(@Valid @RequestBody BusinessTypeSaveReqVO updateReqVO) {
        businessTypeService.updateBusinessType(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除业务类型")
    @Parameter(name = "id", description = "编号", required = true)
   // @PreAuthorize("@ss.hasPermission('ecms:business-type:delete')")
    public CommonResult<Boolean> deleteBusinessType(@RequestParam("id") String id) {
        businessTypeService.deleteBusinessType(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得业务类型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
   // @PreAuthorize("@ss.hasPermission('ecms:business-type:query')")
    public CommonResult<BusinessTypeRespVO> getBusinessType(@RequestParam("id") String id) {
        BusinessTypeDO businessType = businessTypeService.getBusinessType(id);
        return success(BeanUtils.toBean(businessType, BusinessTypeRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得业务类型分页")
//@PreAuthorize("@ss.hasPermission('ecms:business-type:query')")
    public CommonResult<PageResult<BusinessTypeRespVO>> getBusinessTypePage(@Valid BusinessTypePageReqVO pageReqVO) {
        PageResult<BusinessTypeDO> pageResult = businessTypeService.getBusinessTypePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BusinessTypeRespVO.class));
    }

   

}