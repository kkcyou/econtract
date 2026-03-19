package com.yaoan.module.econtract.controller.admin.version;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.term.vo.TermRespVO;
import com.yaoan.module.econtract.controller.admin.version.vo.list.FileVersionPageReqVO;
import com.yaoan.module.econtract.controller.admin.version.vo.list.FileVersionPageRespVO;
import com.yaoan.module.econtract.service.version.FileVersionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description: 文件版本
 * @author: Pele
 * @date: 2024/8/29 15:15
 */
@Slf4j
@RestController
@RequestMapping("econtract/file/version")
@Tag(name = "文件版本", description = "文件版本")
public class FileVersionController {
    @Resource
    private FileVersionService fileVersionService;

    /**
     * 分页查询条款列表
     *
     * @param reqVO
     * @return
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询条款列表")
    public CommonResult<PageResult<FileVersionPageRespVO>> page(@Validated @RequestBody FileVersionPageReqVO reqVO) {
        return success(fileVersionService.page(reqVO));
    }
}
