package com.yaoan.module.system.controller.admin.region;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.module.system.controller.admin.region.vo.BigRegionSimpleRespVO;
import com.yaoan.module.system.controller.admin.region.vo.RegionListReqVO;
import com.yaoan.module.system.controller.admin.region.vo.RegionReqVo;
import com.yaoan.module.system.controller.admin.region.vo.RegionRespVO;
import com.yaoan.module.system.convert.region.RegionConvert;
import com.yaoan.module.system.dal.dataobject.region.Region;
import com.yaoan.module.system.service.region.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description:
 * @author: Pele
 * @date: 2024/6/6 16:57
 */
@Tag(name = "管理后台 - 区划")
@RestController
@RequestMapping("/system/mongolia/region")
@Validated
public class RegionController {
    @Resource
    private RegionService regionService;
    /**
     * 获取区划精简信息列表V2
     */
    @GetMapping({"/getSimpleRegionListV2"})
    @Operation(summary = "获取区划精简信息列表V2")
    public CommonResult<BigRegionSimpleRespVO> getSimpleRegionListV2(RegionListReqVO reqVO) {
        return success(regionService.getSimpleRegionListV2(reqVO));
    }

    @PostMapping("/list")
    @Operation(summary = "获取区划列表")
    public CommonResult<List<RegionRespVO>> getPostRegionList(RegionReqVo reqVO) {

        List<Region> list = regionService.getPostRegionList(reqVO);
        List<Region> regions = list.stream().filter(region -> StringUtils.isNumeric(region.getRegionCode())).collect(Collectors.toList());
        return success(RegionConvert.INSTANCE.convertList(regions));
    }
}
