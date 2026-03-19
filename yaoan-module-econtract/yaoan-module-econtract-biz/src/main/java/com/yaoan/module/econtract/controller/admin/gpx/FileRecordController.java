package com.yaoan.module.econtract.controller.admin.gpx;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.module.econtract.controller.admin.gpx.vo.file.PermissionRespVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.file.SaveFileAndCompanyReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.service.gpx.FileRecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @description: 文件签章记录功能
 * @author: Pele
 * @date: 2024/7/4 16:22
 */
@Slf4j
@Validated
@RestController
@RequestMapping("fileRecord")
@Tag(name = "文件签章记录功能", description = "文件签章记录功能")
public class FileRecordController {

    @Resource
    private FileRecordService fileRecordService;

    /**
     * 保存合同文件和公司关系
     */
    @PostMapping(value = "/saveFileAndCompanyInfo")
    @Operation(summary = "保存合同文件和公司关系")
    public CommonResult<String> saveFileAndCompanyInfo(@RequestBody @Valid SaveFileAndCompanyReqVO contractPageReqVO) throws Exception {
        return CommonResult.success(fileRecordService.saveFileAndCompanyInfo(contractPageReqVO));
    }


    /**
     * 删除合同文件和公司关系
     */
    @PostMapping(value = "/deleteFileAndCompanyInfo")
    @Operation(summary = " 删除合同文件和公司关系")
    public CommonResult<String> deleteFileAndCompanyInfo(@RequestBody @Valid  IdReqVO vo) throws Exception {
        return CommonResult.success(fileRecordService.deleteFileAndCompanyInfo(vo.getId()));
    }

    /**
     * 是否能查看签章该文件
     */
    @PostMapping(value = "/getPermission")
    @Operation(summary = "是否通过")
    public CommonResult<PermissionRespVO> getPermission(@RequestBody @Valid  IdReqVO vo) throws Exception {
        return CommonResult.success(fileRecordService.getPermission(vo.getId()));
    }


    /**
     * 是否通过
     */
    @PostMapping(value = "/ifPass")
    @Operation(summary = "是否通过")
    public CommonResult<Boolean> ifPass(@RequestBody @Valid  IdReqVO vo) throws Exception {
        return CommonResult.success(fileRecordService.ifPass(vo.getId()));
    }

    /**
     * 是否通过
     */
    @PostMapping(value = "/ifPass1")
    @Operation(summary = "是否通过")
    public CommonResult<Boolean> ifPass1(@RequestBody @Valid  IdReqVO vo) throws Exception {
        return CommonResult.success(fileRecordService.ifPass1(vo.getId()));
    }

}
