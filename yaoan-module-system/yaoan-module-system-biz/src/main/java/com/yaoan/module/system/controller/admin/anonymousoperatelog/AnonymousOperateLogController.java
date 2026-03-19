package com.yaoan.module.system.controller.admin.anonymousoperatelog;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.yaoan.module.system.controller.admin.anonymousoperatelog.vo.*;
import com.yaoan.module.system.dal.dataobject.anonymousoperatelog.AnonymousOperateLogDO;
import com.yaoan.module.system.convert.anonymousoperatelog.AnonymousOperateLogConvert;
import com.yaoan.module.system.service.anonymousoperatelog.AnonymousOperateLogService;

@Tag(name = "管理后台 - 匿名用户操作日志记录")
@RestController
@RequestMapping("/system/anonymous-operate-log")
@Validated
public class AnonymousOperateLogController {

    @Resource
    private AnonymousOperateLogService anonymousOperateLogService;


    @GetMapping("/get")
    @Operation(summary = "获得匿名用户操作日志记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('system:anonymous-operate-log:query')")
    public CommonResult<AnonymousOperateLogRespVO> getAnonymousOperateLog(@RequestParam("id") Long id) {
        AnonymousOperateLogDO anonymousOperateLog = anonymousOperateLogService.getAnonymousOperateLog(id);
        return success(AnonymousOperateLogConvert.INSTANCE.convert(anonymousOperateLog));
    }

    @GetMapping("/list")
    @Operation(summary = "获得匿名用户操作日志记录列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    @PreAuthorize("@ss.hasPermission('system:anonymous-operate-log:query')")
    public CommonResult<List<AnonymousOperateLogRespVO>> getAnonymousOperateLogList(@RequestParam("ids") Collection<Long> ids) {
        List<AnonymousOperateLogDO> list = anonymousOperateLogService.getAnonymousOperateLogList(ids);
        return success(AnonymousOperateLogConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得匿名用户操作日志记录分页")
    @PreAuthorize("@ss.hasPermission('system:anonymous-operate-log:query')")
    public CommonResult<PageResult<AnonymousOperateLogRespVO>> getAnonymousOperateLogPage(@Valid AnonymousOperateLogPageReqVO pageVO) {
        PageResult<AnonymousOperateLogDO> pageResult = anonymousOperateLogService.getAnonymousOperateLogPage(pageVO);
        return success(AnonymousOperateLogConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出匿名用户操作日志记录 Excel")
    @PreAuthorize("@ss.hasPermission('system:anonymous-operate-log:export')")
    @OperateLog(type = EXPORT)
    public void exportAnonymousOperateLogExcel(@Valid AnonymousOperateLogExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<AnonymousOperateLogDO> list = anonymousOperateLogService.getAnonymousOperateLogList(exportReqVO);
        // 导出 Excel
        List<AnonymousOperateLogExcelVO> datas = AnonymousOperateLogConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "匿名用户操作日志记录.xls", "数据", AnonymousOperateLogExcelVO.class, datas);
    }

}
