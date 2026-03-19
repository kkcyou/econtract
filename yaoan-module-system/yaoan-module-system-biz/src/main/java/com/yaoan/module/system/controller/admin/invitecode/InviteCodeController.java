package com.yaoan.module.system.controller.admin.invitecode;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

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

import com.yaoan.module.system.controller.admin.invitecode.vo.*;
import com.yaoan.module.system.dal.dataobject.invitecode.InviteCodeDO;
import com.yaoan.module.system.convert.invitecode.InviteCodeConvert;
import com.yaoan.module.system.service.invitecode.InviteCodeService;

@Tag(name = "管理后台 - 邀请码管理")
@RestController
@RequestMapping("/system/invite-code")
@Validated
public class InviteCodeController {

    @Resource
    private InviteCodeService inviteCodeService;

    @PostMapping("/create")
    @Operation(summary = "创建邀请码管理")
    public CommonResult<Integer> createInviteCode(@Valid @RequestBody InviteCodeSaveReqVO createReqVO) {
        return success(inviteCodeService.createInviteCode(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新邀请码管理")
    public CommonResult<Boolean> updateInviteCode(@Valid @RequestBody InviteCodeSaveReqVO updateReqVO) {
        inviteCodeService.updateInviteCode(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除邀请码管理")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteInviteCode(@RequestParam("id") Integer id) {
        inviteCodeService.deleteInviteCode(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得邀请码管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<InviteCodeRespVO> getInviteCode(@RequestParam("id") Integer id) {
        InviteCodeDO inviteCode = inviteCodeService.getInviteCodeInfo(id);
        return success(InviteCodeConvert.INSTANCE.convert(inviteCode));
    }

    @GetMapping("/list")
    @Operation(summary = "获得邀请码管理列表")
    @PreAuthorize("@ss.hasPermission('system:invite-code:query')")
    public CommonResult<List<InviteCodeRespVO>> getInviteCodeList(@RequestParam("ids") Collection<Integer> ids) {
        List<InviteCodeDO> list = inviteCodeService.getInviteCodeList(ids);
        return success(InviteCodeConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得邀请码管理分页")
    public CommonResult<PageResult<InviteCodeRespVO>> getInviteCodePage(@Valid InviteCodePageReqVO pageVO) {
        PageResult<InviteCodeDO> pageResult = inviteCodeService.getInviteCodePage(pageVO);
        return success(InviteCodeConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出邀请码管理 Excel")
    @OperateLog(type = EXPORT)
    public void exportInviteCodeExcel(@Valid InviteCodeExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<InviteCodeDO> list = inviteCodeService.getInviteCodeList(exportReqVO);
        // 导出 Excel
        List<InviteCodeExcelVO> datas = InviteCodeConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "邀请码管理.xls", "数据", InviteCodeExcelVO.class, datas);
    }


    @GetMapping("/getOneInviteCode")
    @Operation(summary = "获得一个邀请码")
    public CommonResult<String> getOneInviteCode() {
        String inviteCode = inviteCodeService.getInviteCode();
        return success(inviteCode);
    }
}
