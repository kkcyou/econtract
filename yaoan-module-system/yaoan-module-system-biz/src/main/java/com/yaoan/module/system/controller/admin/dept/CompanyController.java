package com.yaoan.module.system.controller.admin.dept;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.tenant.core.aop.TenantIgnore;
import com.yaoan.module.system.controller.admin.dept.vo.company.*;
import com.yaoan.module.system.controller.admin.dept.vo.saas.CompanySimpleSaveReqVO;
import com.yaoan.module.system.controller.admin.dept.vo.saas.SaasCompanyRespVO;
import com.yaoan.module.system.convert.dept.CompanyConvert;
import com.yaoan.module.system.dal.dataobject.dept.CompanyDO;
import com.yaoan.module.system.service.dept.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 管理后台 - 单位
 */
@Tag(name = "管理后台 - 单位")
@RestController
@RequestMapping("/system/company")
@Validated
public class CompanyController {

    @Resource
    private CompanyService companyService;

    /**
     * 创建单位
     * @param reqVO
     * @return
     */
    @PostMapping("create")
    @Operation(summary = "创建单位")
    public CommonResult<Long> createCompany(@Valid @RequestBody CompanyCreateReqVO reqVO) {
        Long companyId = companyService.createCompany(reqVO);
        return success(companyId);
    }

    /**
     * 更新单位
     * @param reqVO
     * @return
     */
    @PutMapping("update")
    @Operation(summary = "更新单位")
    public CommonResult<Boolean> updateCompany(@Valid @RequestBody CompanyUpdateReqVO reqVO) {
        companyService.updateCompany(reqVO);
        return success(true);
    }

    /**
     * 删除单位
     * @param id
     * @return
     */
    @DeleteMapping("delete")
    @Operation(summary = "删除单位")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> deleteCompany(@RequestParam("id") Long id) {
        companyService.deleteCompany(id);
        return success(true);
    }

    /**
     * 获取单位列表
     * @param reqVO
     * @return
     */
    @GetMapping("/list")
    @Operation(summary = "获取单位列表")
    public CommonResult<List<CompanyRespVO>> getCompanyList(CompanyListReqVO reqVO) {
        List<CompanyDO> list = companyService.getCompanyList(reqVO);
        list.sort(Comparator.comparing(CompanyDO::getSort));
        return success(CompanyConvert.INSTANCE.convertList(list));
    }

    /**
     * 获得单位信息
     * @param id
     * @return
     */
    @GetMapping("/get")
    @Operation(summary = "获得单位信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<CompanyRespVO> getCompany(@RequestParam("id") Long id) {
        return success(companyService.getCompany(id));
    }

    /**
     * 根据公司名称获取公司id
     */
    @GetMapping("/get/id")
    @Operation(summary = "根据公司名称获取公司id")
    @TenantIgnore
    public CommonResult<Long> getCompanyIdByName(@RequestParam("name") String name) {
        return success(companyService.getCompanyIdByName(name));
    }

    @PermitAll
    @TenantIgnore
    @DataPermission(enable = false)
    @GetMapping("/checkCreditCode")
    @Operation(summary = "校验企业的社会信用代码")
    public CommonResult<Boolean> checkCreditCode(@RequestParam("creditCode") String creditCode) {
        companyService.checkCreditCode(creditCode);
        return success(true);
    }

    @PermitAll
    @PostMapping("/saas/save")
    @Operation(summary = "创建saas企业")
    public CommonResult<Long> save(@RequestBody CompanySimpleSaveReqVO reqVO) {
        return success(companyService.save(reqVO));
    }

    @GetMapping("/saas/acheRelativeId")
    @Operation(summary = "缓存相对方id/切换企业空间")
    public CommonResult<String> acheRelativeId(@RequestParam("relativeId") String relativeId) {
        return success(companyService.acheRelativeId(relativeId));
    }

    @GetMapping("/saas/list4login")
    @Operation(summary = "展示用户相关企业和个人空间")
    public CommonResult<List<SaasCompanyRespVO>> list4login() {
        return success(companyService.list4login());
    }

}
