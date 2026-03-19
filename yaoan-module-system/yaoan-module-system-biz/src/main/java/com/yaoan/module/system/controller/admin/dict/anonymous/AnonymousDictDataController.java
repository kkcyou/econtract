package com.yaoan.module.system.controller.admin.dict.anonymous;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.system.controller.admin.dict.vo.data.DictDataPageReqVO;
import com.yaoan.module.system.controller.admin.dict.vo.data.DictDataRespVO;
import com.yaoan.module.system.convert.dict.DictDataConvert;
import com.yaoan.module.system.service.dict.DictDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description:
 * @author: Pele
 * @date: 2025-9-15 10:39
 */

@Tag(name = "(匿名访问)管理后台 - 字典数据")
@RestController
@RequestMapping("anonymous/system/dict-data")
@Validated
public class AnonymousDictDataController {

    @Resource
    private DictDataService dictDataService;

    @GetMapping("/page")
    @Operation(summary = "/获得字典类型的分页列表")
    public CommonResult<PageResult<DictDataRespVO>> getDictTypePage(@Valid DictDataPageReqVO reqVO) {
        return success(DictDataConvert.INSTANCE.convertPage(dictDataService.getDictDataPage(reqVO)));
    }
}
