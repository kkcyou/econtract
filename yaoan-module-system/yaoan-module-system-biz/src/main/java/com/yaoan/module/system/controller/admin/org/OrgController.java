package com.yaoan.module.system.controller.admin.org;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.tenant.core.aop.TenantIgnore;
import com.yaoan.module.system.api.user.dto.OrganizationDTO;
import com.yaoan.module.system.api.user.dto.RegionWithOrgDTO;
import com.yaoan.module.system.controller.admin.org.vo.OrgReqVo;
import com.yaoan.module.system.controller.admin.org.vo.OrgRespVo;
import com.yaoan.module.system.controller.admin.region.vo.RegionReqVo;
import com.yaoan.module.system.service.user.OrganizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @author doujiale
 */
@Tag(name = "管理后台 - 单位")
@RestController
@RequestMapping("/system/mongolia/org")
@Validated
public class OrgController {

    @Resource
    private OrganizationService organizationService;

    @PostMapping("/list")
    @Operation(summary = "根据区划code获取单位列表")
    public CommonResult<List<OrganizationDTO>> getPostRegionList(RegionReqVo reqVO) {
        return success(organizationService.getOrganizationList(reqVO));
    }


    @PostMapping("/getOrgListByDistrict")
    @Operation(summary = "获取区划下单位列表")
    public CommonResult<List<RegionWithOrgDTO>> getOrgListByDistrict(RegionReqVo reqVO) {
        return success(organizationService.getOrgListByDistrict(reqVO));
    }


    @PostMapping("/getOrgList")
    @Operation(summary = "获取单位列表")
    public CommonResult<PageResult<OrgRespVo>> getOrgList(@Parameter(name = "reqVO", description = "获取单位列表")
                                                              @RequestBody OrgReqVo reqVO) {
        return success(organizationService.getOrgList(reqVO));
    }

    @GetMapping(value = "/openOrg/{id}")
    @Operation(summary = "开通单位")
    public CommonResult<String> openOrg(@PathVariable String id) {
        return success(organizationService.openOrg(id));
    }

}
