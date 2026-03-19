package com.yaoan.module.system.controller.admin.user;

import cn.hutool.core.collection.CollUtil;
import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.MapUtils;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.excel.core.util.ExcelUtils;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.system.controller.admin.auth.vo.AuthLoginRespVO;
import com.yaoan.module.system.controller.admin.auth.vo.AuthSmsLoginReqVO;
import com.yaoan.module.system.controller.admin.dept.vo.dept.DeptTreeVO;
import com.yaoan.module.system.controller.admin.dept.vo.dept.DeptUserRespVO;
import com.yaoan.module.system.controller.admin.permission.vo.role.RoleRespVO;
import com.yaoan.module.system.controller.admin.permission.vo.role.RoleSimpleRespVO;
import com.yaoan.module.system.controller.admin.user.vo.role.RoleSimple4UserRespVO;
import com.yaoan.module.system.controller.admin.user.vo.user.*;
import com.yaoan.module.system.convert.user.UserConvert;
import com.yaoan.module.system.dal.dataobject.dept.DeptDO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.enums.common.SexEnum;
import com.yaoan.module.system.service.dept.DeptService;
import com.yaoan.module.system.service.user.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.framework.common.util.collection.CollectionUtils.convertList;
import static com.yaoan.framework.common.util.collection.CollectionUtils.convertSet;
import static com.yaoan.framework.operatelog.core.enums.OperateTypeEnum.EXPORT;

/**
 * 管理后台 - 用户
 */
@Tag(name = "管理后台 - 用户")
@RestController
@RequestMapping("/system/user")
@Validated
public class UserController {

    @Resource
    private AdminUserService userService;
    @Resource
    private DeptService deptService;

    @PostMapping("/create")
    @Operation(summary = "新增用户")
//    @PreAuthorize("@ss.hasPermission('system:user:create')")
    public CommonResult<Long> createUser(@Valid @RequestBody UserCreateReqVO reqVO) {
        Long id = userService.createUser(reqVO);
        return success(id);
    }


    @PutMapping("update")
    @Operation(summary = "修改用户")
    @PreAuthorize("@ss.hasPermission('system:user:update')")
    public CommonResult<Boolean> updateUser(@Valid @RequestBody UserUpdateReqVO reqVO) {
        userService.updateUser(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除用户")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:user:delete')")
    public CommonResult<Boolean> deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return success(true);
    }

    @PutMapping("/update-password")
    @Operation(summary = "重置用户密码")
    @PreAuthorize("@ss.hasPermission('system:user:update-password')")
    public CommonResult<Boolean> updateUserPassword(@Valid @RequestBody UserUpdatePasswordReqVO reqVO) {
        userService.updateUserPassword(reqVO.getId(), reqVO.getPassword());
        return success(true);
    }


    @PostMapping("/forget-password")
    @PermitAll
    @Operation(summary = "修改密码")
    @OperateLog(enable = false) // 避免 Post 请求被记录操作日志
    public CommonResult<Boolean> changePwd(@RequestBody @Valid UserForgetPasswordReqVO reqVO) {
        userService.changeUserPassword(reqVO);
        return success(true);
    }

    @PutMapping("/update-status")
    @Operation(summary = "修改用户状态")
    @PreAuthorize("@ss.hasPermission('system:user:update')")
    public CommonResult<Boolean> updateUserStatus(@Valid @RequestBody UserUpdateStatusReqVO reqVO) {
        userService.updateUserStatus(reqVO.getId(), reqVO.getStatus());
        return success(true);
    }

    @GetMapping("/page")
    @Operation(summary = "获得用户分页列表")
    @PreAuthorize("@ss.hasPermission('system:user:list')")
    public CommonResult<PageResult<UserPageItemRespVO>> getUserPage(@Valid UserPageReqVO reqVO) {
        // 获得用户分页列表
        PageResult<AdminUserDO> pageResult = userService.getUserPage(reqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotal())); // 返回空
        }

        // 获得拼接需要的数据
        Collection<Long> deptIds = convertList(pageResult.getList(), AdminUserDO::getDeptId);
        Map<Long, DeptDO> deptMap = deptService.getDeptMap(deptIds);
        // 拼接结果返回
        List<UserPageItemRespVO> userList = new ArrayList<>(pageResult.getList().size());
        pageResult.getList().forEach(user -> {
            UserPageItemRespVO respVO = UserConvert.INSTANCE.convert(user);
            respVO.setDept(UserConvert.INSTANCE.convert(deptMap.get(user.getDeptId())));
            userList.add(respVO);
        });
        return success(new PageResult<>(userList, pageResult.getTotal()));
    }

    //    @GetMapping("/list-all-simple")
    @GetMapping({"/list-all-simple", "/simple-list"})
    @Operation(summary = "获取用户精简信息列表", description = "只包含被开启的用户，主要用于前端的下拉选项")
    public CommonResult<List<UserSimpleRespVO>> getSimpleUserList() {
        // 获用户列表，只要开启状态的
        List<AdminUserDO> list = userService.getUserListByStatus(CommonStatusEnum.ENABLE.getStatus());
        // 排序后，返回给前端
        return success(UserConvert.INSTANCE.convertList04(list));
    }

    @GetMapping({"/list-by-role/roleId"})
    @Parameter(name = "roleId", description = "编号", required = true, example = "1024")
    @Operation(summary = "获取用户精简信息列表", description = "只包含被开启的用户，主要用于前端的下拉选项")
    public CommonResult<List<UserSimpleRespVO>> getSimpleUserListByRole(@RequestParam("roleId") Long roleId) {
        // 获用户列表，只要开启状态的
        List<AdminUserDO> list = userService.getUserListByRoleId(roleId);
        // 排序后，返回给前端
        return success(UserConvert.INSTANCE.convertList04(list));
    }

    @GetMapping("/get")
    @Operation(summary = "获得用户详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:user:query')")
    public CommonResult<UserRespVO> getUser(@RequestParam("id") Long id) {
        AdminUserDO user = userService.getUser(id);
        // 获得部门数据
        DeptDO dept = deptService.getDept(user.getDeptId());
        return success(UserConvert.INSTANCE.convert(user).setDept(UserConvert.INSTANCE.convert(dept)));
    }

    @GetMapping("/export")
    @Operation(summary = "导出用户")
    @PreAuthorize("@ss.hasPermission('system:user:export')")
    @OperateLog(type = EXPORT)
    public void exportUserList(@Validated UserExportReqVO reqVO,
                               HttpServletResponse response) throws IOException {
        // 获得用户列表
        List<AdminUserDO> users = userService.getUserList(reqVO);

        // 获得拼接需要的数据
        Collection<Long> deptIds = convertList(users, AdminUserDO::getDeptId);
        Map<Long, DeptDO> deptMap = deptService.getDeptMap(deptIds);
        Map<Long, AdminUserDO> deptLeaderUserMap = userService.getUserMap(
                convertSet(deptMap.values(), DeptDO::getLeaderUserId));
        // 拼接数据
        List<UserExcelVO> excelUsers = new ArrayList<>(users.size());
        users.forEach(user -> {
            UserExcelVO excelVO = UserConvert.INSTANCE.convert02(user);
            // 设置部门
            MapUtils.findAndThen(deptMap, user.getDeptId(), dept -> {
                excelVO.setDeptName(dept.getName());
                // 设置部门负责人的名字
                MapUtils.findAndThen(deptLeaderUserMap, dept.getLeaderUserId(),
                        deptLeaderUser -> excelVO.setDeptLeaderNickname(deptLeaderUser.getNickname()));
            });
            excelUsers.add(excelVO);
        });

        // 输出
        ExcelUtils.write(response, "用户数据.xls", "用户列表", UserExcelVO.class, BeanUtils.toBean(excelUsers, UserExcelVO.class));

    }

    @GetMapping("/get-import-template")
    @Operation(summary = "获得导入用户模板")
    public void importTemplate(HttpServletResponse response) throws IOException {
        // 手动创建导出 demo
        List<UserImportExcelVO> list = Arrays.asList(
                UserImportExcelVO.builder().username("yunai").deptId(1L).email("yunai@iocoder.cn").mobile("15601691300")
                        .nickname("芋道").status(CommonStatusEnum.ENABLE.getStatus()).sex(SexEnum.MALE.getSex()).build(),
                UserImportExcelVO.builder().username("yuanma").deptId(2L).email("yuanma@iocoder.cn").mobile("15601701300")
                        .nickname("源码").status(CommonStatusEnum.DISABLE.getStatus()).sex(SexEnum.FEMALE.getSex()).build()
        );

        // 输出
        ExcelUtils.write(response, "用户导入模板.xls", "用户列表", UserImportExcelVO.class, list);
    }

    @PostMapping("/import")
    @Operation(summary = "导入用户")
    @Parameters({
            @Parameter(name = "file", description = "Excel 文件", required = true),
            @Parameter(name = "updateSupport", description = "是否支持更新，默认为 false", example = "true")
    })
    @PreAuthorize("@ss.hasPermission('system:user:import')")
    public CommonResult<UserImportRespVO> importExcel(@RequestParam("file") MultipartFile file,
                                                      @RequestParam(value = "updateSupport", required = false, defaultValue = "false") Boolean updateSupport) throws Exception {
        List<UserImportExcelVO> list = ExcelUtils.read(file, UserImportExcelVO.class);
        return success(userService.importUserList(list, updateSupport));
    }

    /**
     * 获得用户详情及部门信息
     *
     * @return
     */
    @GetMapping("/getDeptAndUser")
    @Operation(summary = "获取登录用户所在公司所有部门及员工")
    public CommonResult<List<DeptUserRespVO>> getDeptAndUser() {
        List<DeptUserRespVO> deptAndUserList = userService.getDeptAndUserList();
        return success(deptAndUserList);

    }

    @GetMapping("/getCompanyUserTreeList")
    @Operation(summary = "获取公司下用户信息列表")
    public CommonResult<List<Object>> getCompanyUserTreeList() {
        List deptTreeVOS = userService.getCompanyUserTreeList(CommonStatusEnum.ENABLE.getStatus());
        return success(deptTreeVOS);
    }

    @GetMapping("/getCompanyUserList")
    @Operation(summary = "获取公司下用户信息列表")
    public CommonResult<List<UserSimpleRespVO>> getCompanyUserList() {
        List<AdminUserDO> list = userService.getCompanyUserList(CommonStatusEnum.ENABLE.getStatus());
        return success(UserConvert.INSTANCE.convertList04(list));
    }

    @GetMapping("/getRoleUserList")
    @Operation(summary = "获取当前租户下相关的角色以及用户信息列表")
    public CommonResult<List<RoleSimple4UserRespVO>> getRoleUserList() {
        return success(userService.getRoleUserList());
    }

    @PostMapping("/setAppOpenId")
    @Operation(summary = "appOpenId用户绑定")
    public CommonResult<Boolean> setAppOpenId(@Valid @RequestBody UserOpenIdReqVO reqVO) {
        userService.updateUserOpenId(reqVO.getId(), reqVO.getUserOpenId());
        return success(true);
    }
}
