package com.yaoan.module.econtract.controller.admin.fastentry;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.module.econtract.controller.admin.fastentry.vo.FastEntryCreateReqVO;
import com.yaoan.module.econtract.controller.admin.fastentry.vo.FastEntryRespVO;
import com.yaoan.module.econtract.controller.admin.fastentry.vo.FastEntrySelVO;
import com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo.RoleWorkbenchRelCreateReqVO;
import com.yaoan.module.econtract.service.fastentry.FastEntryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.yaoan.framework.common.pojo.CommonResult.success;

@Tag(name = "用户-快捷入口")
@RestController
@RequestMapping("/econtract/fastentry")
@Validated
public class FastEntryController {

    @Resource
    private FastEntryService fastEntryService;

    @PostMapping("/create")
    @Operation(summary = "创建快捷入口配置")
    public CommonResult<Boolean> createFastEntry(@Valid @RequestBody FastEntryCreateReqVO reqVO) {
        fastEntryService.saveFastEntryData(reqVO);
        return success(true);
    }

    @PostMapping("/getFastEntryConfig")
    @Operation(summary = "获得快捷入口菜单配置")
    public CommonResult<FastEntryRespVO> getFastEntry(@RequestBody FastEntrySelVO getReq) {
        return success(fastEntryService.getUserFastEntryData(getReq));
    }

    @PostMapping("/getFastEntryAllMenus")
    @Operation(summary = "获得快捷入口菜单配置页面全部菜单")
    public CommonResult<FastEntryRespVO> getFastEntryAllMenus() {
        return success(fastEntryService.getFastEntryAllMenus());
    }
}
