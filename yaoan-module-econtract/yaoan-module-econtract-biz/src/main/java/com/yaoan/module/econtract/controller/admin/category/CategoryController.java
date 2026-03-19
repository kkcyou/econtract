package com.yaoan.module.econtract.controller.admin.category;

import cn.hutool.core.lang.tree.Tree;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.module.econtract.controller.admin.category.vo.CategoryReqVO;
import com.yaoan.module.econtract.service.category.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;
import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * <p>
 * 分类
 * </p>
 *
 * @author doujl
 * @since 2023-07-24
 */

@Slf4j
@RestController
@RequestMapping("econtract/category")
@Tag(name = "分类", description = "分类")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 新增/修改分类
     * @param params
     * @return
     */
    @PutMapping(value = "/create")
    @Operation(summary = "新增/修改分类", description = "新增/修改分类")
    public CommonResult<Integer> create(@RequestBody CategoryReqVO params)  {
        Integer id = categoryService.saveParamCategory(params);
        return success(id);
    }

    /**
     * 新增/修改分类v2
     * @param params
     * @return
     */
    @PutMapping(value = "/createV2")
    @Operation(summary = "新增/修改分类v2", description = "新增/修改分类")
    public CommonResult<Integer> createV2(@RequestBody CategoryReqVO params)  {
        Integer id = categoryService.saveParamCategoryV2(params);
        return success(id);
    }

    /**
     * 删除分类
     * @param id
     * @param type
     * @return
     * @throws IOException
     */
    @DeleteMapping(value = "/delete/{id}")
    @Operation(summary = "删除分类", description = "删除分类")
    public CommonResult<Boolean> delete(@PathVariable("id") String id, String type) throws IOException {
        categoryService.deleteById(id, type);
        return success(true);
    }

    /**
     * 删除分类v2
     * @param id
     * @param type
     * @return
     * @throws IOException
     */
    @DeleteMapping(value = "/deleteV2/{id}")
    @Operation(summary = "删除分类v2", description = "删除分类")
    public CommonResult<Boolean> deleteV2(@PathVariable("id") String id, String type) throws IOException {
        categoryService.deleteByIdV2(id, type);
        return success(true);
    }

    /**
     * 查询所有分类
     * @param type
     * @return
     */
    @GetMapping(value = "/list")
    @Operation(summary = "查询所有分类", description = "查询所有分类")
    public CommonResult<List<Tree<String>>> queryAllParamCategory(String type)  {
        List<Tree<String>> categoryResponses = categoryService.queryAllParamCategory(type);
        return success(categoryResponses);
    }

    /**
     * 查询所有分类v2
     * @param type
     * @return
     */
    @GetMapping(value = "/listV2")
    @Operation(summary = "查询所有分类v2", description = "查询所有分类")
    public CommonResult<List<Tree<String>>> queryAllParamCategoryV2(String type)  {
        List<Tree<String>> categoryResponses = categoryService.queryAllParamCategoryV2(type);
        return success(categoryResponses);
    }

    /**
     * 根据分类id查询分类信息
     * @param id
     * @param type
     * @return
     */
    @GetMapping(value = "query/{id}")
    @Operation(summary = "根据分类id查询分类信息", description = "查询所有分类")
    public CommonResult<CategoryReqVO> queryACategoryById(@PathVariable String id,String type)  {
        CategoryReqVO categoryBaseVO = categoryService.queryACategoryById(id,type);
        return success(categoryBaseVO);
    }

    /**
     * 根据分类id查询分类信息v2
     * @param id
     * @param type
     * @return
     */
    @GetMapping(value = "queryV2/{id}")
    @Operation(summary = "根据分类id查询分类信息v2", description = "查询所有分类")
    public CommonResult<CategoryReqVO> queryACategoryByIdV2(@PathVariable String id,String type)  {
        CategoryReqVO categoryBaseVO = categoryService.queryACategoryByIdV2(id,type);
        return success(categoryBaseVO);
    }
}
