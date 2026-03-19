package com.yaoan.module.system.controller.admin.dept;

import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.api.relative.RelativeApi;
import com.yaoan.module.econtract.api.relative.dto.RelativeContactDTO;
import com.yaoan.module.system.controller.admin.dept.vo.dept.*;
import com.yaoan.module.system.convert.dept.DeptConvert;
import com.yaoan.module.system.dal.dataobject.dept.DeptDO;
import com.yaoan.module.system.service.dept.DeptService;
import com.yaoan.module.system.service.permission.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 部门")
@RestController
@RequestMapping("/system/dept")
@Validated
public class DeptController {

    @Resource
    private DeptService deptService;

    @Resource
    private RoleService roleService;
        @Resource
    private RelativeApi relativeApi;
    @Resource
    private RedisUtils redisUtils;
    @PostMapping("create")
    @Operation(summary = "创建部门")
    @PreAuthorize("@ss.hasPermission('system:dept:create')")
    public CommonResult<Long> createDept(@Valid @RequestBody DeptCreateReqVO reqVO) {
        Long deptId = deptService.createDept(reqVO);
        return success(deptId);
    }

    @PutMapping("update")
    @Operation(summary = "更新部门")
    @PreAuthorize("@ss.hasPermission('system:dept:update')")
    public CommonResult<Boolean> updateDept(@Valid @RequestBody DeptUpdateReqVO reqVO) {
        deptService.updateDept(reqVO);
        return success(true);
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除部门")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:dept:delete')")
    public CommonResult<Boolean> deleteDept(@RequestParam("id") Long id) {
        deptService.deleteDept(id);
        return success(true);
    }

    @GetMapping("/list")
    @Operation(summary = "获取部门列表")
    @PreAuthorize("@ss.hasPermission('system:dept:query')")
    @DataPermission(enable = false)
    public CommonResult<List<DeptRespVO>> getDeptList(DeptListReqVO reqVO) {
        reqVO.setUserType(roleService.roleType(getLoginUserId()));
        List<DeptDO> list = deptService.getDeptList(reqVO);
        list.sort(Comparator.comparing(DeptDO::getSort));
        return success(DeptConvert.INSTANCE.convertList(list));
    }

//    @GetMapping("/list-all-simple")
    @GetMapping({"/list-all-simple", "/simple-list"})
    @Operation(summary = "获取部门精简信息列表", description = "只包含被开启的部门，主要用于前端的下拉选项")
    @DataPermission(enable = false)
    public CommonResult<List<DeptSimpleRespVO>> getSimpleDeptList() {
        // 获得部门列表，只要开启状态的
        DeptListReqVO reqVO = new DeptListReqVO();
        reqVO.setUserType(roleService.roleType(getLoginUserId()));
        reqVO.setStatus(CommonStatusEnum.ENABLE.getStatus());
        List<DeptDO> list = deptService.getDeptList(reqVO);
        // 排序后，返回给前端
        list.sort(Comparator.comparing(DeptDO::getSort));
        return success(DeptConvert.INSTANCE.convertList02(list));
    }

    @GetMapping("/get")
    @Operation(summary = "获得部门信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:dept:query')")
    public CommonResult<DeptRespVO> getDept(@RequestParam("id") Long id) {
        return success(DeptConvert.INSTANCE.convert(deptService.getDept(id)));
    }

    /**
     * 获取顶级部门列表-父级部门id为0的
     * @return
     */
    @GetMapping("/listAllTop")
    @Operation(summary = "获取顶级部门列表-父级部门id为0的")
    @PreAuthorize("@ss.hasPermission('system:dept:query')")
    public CommonResult<List<DeptSimpleRespVO>> getParentDeptList() {
        List<DeptDO> list = deptService.getParentDeptList();
        list.sort(Comparator.comparing(DeptDO::getSort));
        return success(DeptConvert.INSTANCE.convertList02(list));
    }

    @GetMapping("/getListAllSimpleBy")
    @Operation(summary = "获取本相对方下的部门", description = "只包含被开启的部门，主要用于前端的下拉选项")
    @DataPermission(enable = false)
    public CommonResult<List<DeptSimpleRespVO>> getListAllSimpleBy() {
        //1.从redis中获取企业的ID
        String acheKey = SecurityFrameworkUtils.getLoginUserKey4Space();
        //获取到相对方ID
        String relativeId = redisUtils.get(acheKey);
        List<RelativeContactDTO> relativeContactDTOS = relativeApi.getRelativeUserId(relativeId,null);
        List<DeptDO> list = deptService.getListAllSimpleBy(relativeContactDTOS);
        // 排序后，返回给前端
        list.sort(Comparator.comparing(DeptDO::getSort));
        return success(DeptConvert.INSTANCE.convertList02(list));
    }
}
