package com.yaoan.module.system.controller.admin.user.saas;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.tenant.core.aop.TenantIgnore;
import com.yaoan.module.system.controller.admin.user.saas.vo.SaasUserPageRespVO;
import com.yaoan.module.system.controller.admin.user.saas.vo.SaasUserSaveReqVO;
import com.yaoan.module.system.controller.admin.user.saas.vo.SaasUserTransferAdminUserReqVO;
import com.yaoan.module.system.controller.admin.user.vo.user.*;
import com.yaoan.module.system.convert.user.UserConvert;
import com.yaoan.module.system.dal.dataobject.dept.DeptDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.service.dept.DeptService;
import com.yaoan.module.system.service.user.AdminUserService;
import com.yaoan.module.system.service.user.saas.SaasUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-23 19:52
 */
@Tag(name = "Saas用户管理---针对供应商用户")
@RestController
@RequestMapping("/system/user/saas")
@Validated
public class SaasUserController {

    @Resource
    private SaasUserService saasUserService;
    @Resource
    private AdminUserService userService;
    @Resource
    private DeptService deptService;

    @PermitAll
    @TenantIgnore
    @DataPermission(enable = false)
    @PostMapping("/create")
    @Operation(summary = "新增Saas用户")
    public CommonResult<Long> save(@Valid @RequestBody SaasUserSaveReqVO reqVO) {
        Long id = saasUserService.save(reqVO);
        return success(id);
    }
    @PostMapping("/page")
    @Operation(summary = "Saas用户管理分页数据获取接口")
    public CommonResult<PageResult<SaasUserPageRespVO>> querySaasUserList(@RequestBody UserPageReqVO reqVO) {
        //1.根据
        PageResult<SaasUserPageRespVO> saasUserListRespVOS = saasUserService.querySaasUserList(reqVO);
        return success(saasUserListRespVOS);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除saas用户")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> deleteUser(@RequestParam("id") Long id) {
        saasUserService.deleteUser(id);
        return success(true);
    }
    @PutMapping("update")
    @Operation(summary = "修改saas用户")
    public CommonResult<Boolean> updateUser(@Valid @RequestBody UserUpdateReqVO reqVO) {
        userService.updateUser(reqVO);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得saas用户详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<UserRespVO> getUser(@RequestParam("id") Long id) {
        UserRespVO userInfo = userService.getUserInfo(id);
        return success(userInfo);
    }
    @PutMapping("/update-status")
    @Operation(summary = "修改saas用户状态")
//    @PreAuthorize("@ss.hasPermission('system:user:update')")
    public CommonResult<Boolean> updateUserStatus(@Valid @RequestBody UserUpdateStatusReqVO reqVO) {
        saasUserService.updateUserStatus(reqVO.getId(), reqVO.getStatus());
        return success(true);
    }

    @PutMapping("/update-password")
    @Operation(summary = "重置saas用户密码")
    public CommonResult<Boolean> updateUserPassword(@Valid @RequestBody UserUpdatePasswordReqVO reqVO) {
        userService.updateUserPassword(reqVO.getId(), reqVO.getPassword());
        return success(true);
    }
    @GetMapping("/transferAdminUser")
    @Operation(summary = "转让saas用户管理员")
    public CommonResult<Boolean> transferAdminUser(@RequestParam("id") Long id) {
        saasUserService.transferAdminUser(id);
        return success(true);
    }
    @PostMapping("/transferContract")
    @Operation(summary = "saas用户管理员转交合同")
    public CommonResult<Boolean> transferContract(@RequestBody SaasUserTransferAdminUserReqVO reqVO) {
        saasUserService.transferContract(reqVO);
        return success(true);
    }
}
