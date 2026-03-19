package com.yaoan.module.system.controller.admin.config;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.tenant.core.aop.TenantIgnore;
import com.yaoan.module.system.controller.admin.config.vo.*;
import com.yaoan.module.system.controller.admin.config.vo.annotation.AnnotationPermissionConfigReqVO;
import com.yaoan.module.system.service.config.SystemConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/23 10:06
 */
@Tag(name = "管理后台 - 动态配置")
@RestController
@RequestMapping("/system/config")
@Validated
public class SystemConfigController {
    @Resource
    private SystemConfigService systemConfigService;

    /**
     * 展示所有系统动态配置列表
     */
    @PostMapping("/list")
    @Operation(summary = "动态配置列表")
    public CommonResult<PageResult<SystemConfigPageRespVO>> list(@Valid @RequestBody SystemConfigReqVO reqVO) {
        return success(systemConfigService.list(reqVO));
    }

    /**
     * 修改配置
     */
    @PostMapping("/update")
    @Operation(summary = "修改配置")
    public CommonResult<String> update(@Valid @RequestBody SystemConfigUpdateReqVO reqVO) {
        return success(systemConfigService.update(reqVO));
    }

    /**
     * 获取区划数据版本号(给前端对比)
     */
    @GetMapping("/getRegionDataVersion")
    @Operation(summary = "获取区划数据版本号")
    public CommonResult<RegionDataVersionRespVO> getRegionDataVersion(RegionDataVersionReqVO reqVO) {
        return success(systemConfigService.getRegionDataVersion(reqVO));
    }

    /**
     * 设置审批批注的浏览权限
     */
    @PostMapping("/setPermissionForApproveScanAnnotations")
    @Operation(summary = "设置审批批注的浏览权限")
    public CommonResult<String> setPermissionForApproveScanAnnotations(@RequestBody SystemConfigReqVO reqVO) {
        return success(systemConfigService.setPermissionForApproveScanAnnotations(reqVO));
    }

    /**
     * 设置抄送人对批注的浏览权限，是全部不可见还是全部可见
     */
    @PostMapping("/setPermissionForCopyScanAnnotations")
    @Operation(summary = "设置抄送人对批注的浏览权限")
    public CommonResult<String> setPermissionForCopyScanAnnotations(@RequestBody SystemConfigReqVO reqVO) {
        return success(systemConfigService.setPermissionForCopyScanAnnotations(reqVO));
    }

    /**
     * 设置系统动态配置
     */
    @PostMapping("/setSystemConfig")
    @Operation(summary = "设置系统动态配置")
    public CommonResult<String> setSystemConfig(@Valid @RequestBody SystemConfigReqVO reqVO) {
        return success(systemConfigService.setSystemConfig(reqVO));
    }

    /**
     * 获得配置的值
     */
    @GetMapping("/getConfigValue")
    @Operation(summary = "获得配置的值")
    public CommonResult<String> getConfigValue(@RequestParam("key") String key) {
        return success(systemConfigService.getConfigValue(key));
    }

    @PermitAll
    @TenantIgnore
    @GetMapping("/getConfigTitle")
    @Operation(summary = "获得配置的系统名称")
    public CommonResult<String> getConfigTitle() {
        String key = "home_title";
        return success(systemConfigService.getConfigValue(key));
    }

}