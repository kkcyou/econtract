package com.yaoan.module.econtract.controller.admin.roleworkbenchrel;

import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo.*;
import com.yaoan.module.econtract.controller.admin.workbenchmanage.vo.WorkbenchRespVO;
import com.yaoan.module.econtract.convert.roleworkbenchrel.RoleWorkbenchRelConvert;
import com.yaoan.module.econtract.dal.dataobject.roleworkbenchrel.RoleWorkbenchRelDO;
import com.yaoan.module.econtract.service.roleworkbenchrel.RoleWorkbenchRelService;
import com.yaoan.module.system.api.permission.RoleApi;
import com.yaoan.module.system.api.permission.dto.RoleRespDTO;
import liquibase.pro.packaged.R;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
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
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;


@Tag(name = "管理后台 - 角色工作台关联")
@RestController
@RequestMapping("/roleworkbench")
@Validated
public class RoleWorkbenchRelController {

    @Resource
    private RoleWorkbenchRelService roleWorkbenchRelService;
    
    
    
    @PostMapping("/create")
    @Operation(summary = "创建角色工作台关联")
    public CommonResult<String> createRoleWorkbenchRel(@Valid @RequestBody RoleWorkbenchRelCreateReqVO createReqVO) {
        return success(roleWorkbenchRelService.createRoleWorkbenchRel(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新角色工作台关联")
    public CommonResult<Boolean> updateRoleWorkbenchRel(@Valid @RequestBody RoleWorkbenchRelUpdateReqVO updateReqVO) {
        roleWorkbenchRelService.updateRoleWorkbenchRel(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除角色工作台关联")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteRoleWorkbenchRel(@RequestParam("id") String id) {
        roleWorkbenchRelService.deleteRoleWorkbenchRel(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得角色工作台关联")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<RoleWorkbenchRelRespVO> getRoleWorkbenchRel(@RequestParam("id") String id) {
        RoleWorkbenchRelDO roleWorkbenchRel = roleWorkbenchRelService.getRoleWorkbenchRel(id);
        return success(RoleWorkbenchRelConvert.INSTANCE.convert(roleWorkbenchRel));
    }
    @PostMapping("/getWorkBenchInfo")
    @Operation(summary = "获得角色工作台关联")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<WorkbenchRespVO> getWorkBenchInfo( @RequestBody RoleWorkbenchRelCreateReqVO getReq) {
//
        return success(roleWorkbenchRelService.getWorkBenchInfo(getReq));
    }
    @GetMapping("/list")
    @Operation(summary = "获得角色工作台关联列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    public CommonResult<List<RoleWorkbenchRelRespVO>> getRoleWorkbenchRelList(@RequestParam("ids") Collection<String> ids) {
        List<RoleWorkbenchRelDO> list = roleWorkbenchRelService.getRoleWorkbenchRelList(ids);
        return success(RoleWorkbenchRelConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得角色工作台关联分页")
    public CommonResult<PageResult<RoleWorkbenchRelRespVO>> getRoleWorkbenchRelPage(@Valid RoleWorkbenchRelPageReqVO pageVO) {
        PageResult<RoleWorkbenchRelDO> pageResult = roleWorkbenchRelService.getRoleWorkbenchRelPage(pageVO);
        return success(RoleWorkbenchRelConvert.INSTANCE.convertPage(pageResult));
    }


}
