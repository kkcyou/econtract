package com.yaoan.module.econtract.controller.admin.outward.templateCategory;

import cn.hutool.core.lang.tree.Tree;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.enums.CategoryTypeCodeConstants;
import com.yaoan.module.econtract.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description: 对外的合同模板分类
 * @author: Pele
 * @date: 2024/3/4 10:14
 */
@Slf4j
@Validated
@RestController
@RequestMapping("template")
@Tag(name = "对外API的合同模板", description = "对外的合同模板")
public class TemplateCategoryController {
    @Resource
    private CategoryService categoryService;

    /**
     * API_获取模板分类列表
     */
    @GetMapping("/category/list")
    @Operation(summary = "API_获取模板分类列表")
    @OperateLog(logArgs = false)
    public CommonResult<List<Tree<String>>> list() throws Exception {
        List<Tree<String>> categoryResponses = categoryService.queryAllParamCategoryV3();
        return success(categoryResponses);
    }


}
