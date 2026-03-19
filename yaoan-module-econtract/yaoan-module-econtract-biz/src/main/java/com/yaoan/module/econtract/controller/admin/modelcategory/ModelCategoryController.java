package com.yaoan.module.econtract.controller.admin.modelcategory;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.api.modelcategory.dto.ModelCategoryDTO;
import com.yaoan.module.econtract.convert.modelcategory.ModelCategoryConverter;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.dataobject.modelcategory.ModelCategory;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.service.model.ModelService;
import com.yaoan.module.econtract.service.modelcategory.ModelCategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description:
 * @author: Pele
 * @date: 2023/8/3 15:58
 */
@Slf4j
@RestController
@RequestMapping("econtract/model")
@Validated
@Tag(name = "合同分类模板", description = "合同模板操作接口")
public class ModelCategoryController {

    @Resource
    private ModelCategoryService modelCategoryService;
    @Resource
    private ModelService modelService;

    /**
     * 列表展示查看
     */
    @PostMapping(value = "/page/listModelCategories")
    @Operation(summary = "获取ModelCategory列表", description = "获取ModelCategory列表")
    public CommonResult<PageResult<ModelCategoryDTO>> listModelCategories(
            @Parameter(name = "ModelCategoryQo", description = "获取模板分类列表") @RequestBody
                    ModelCategoryDTO modelCategoryDTO) {
        return success(ModelCategoryConverter.INSTANCE
                .convertModelCategoryPage(modelCategoryService.getModelCategoryPage(modelCategoryDTO)));

    }

    /**
     * 新增模板分类
     */
    @PostMapping(value = "/insertModelCategory")
    @Operation(summary = "新增模板分类", description = "新增模板分类")
    public CommonResult<Object> insertModelCategory(@RequestBody @Valid ModelCategoryDTO modelCategoryDTO) {
        if (ObjectUtil.isEmpty(modelCategoryDTO)) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_EMPTY_PARAM_ERROR);
        } else {
            return success(modelCategoryService.insertmodelCategory(modelCategoryDTO));
        }
    }

    /**
     * 删除模板分类
     */
    @PostMapping(value = "/delete/{id}")
    @Operation(summary = "删除模板分类", description = "删除模板分类")
    public CommonResult<Object> deleteModels(@RequestBody @Valid ModelCategoryDTO modelCategoryDTO) {
        ModelCategory modelCategory = ModelCategoryConverter.INSTANCE.toEntity(modelCategoryDTO);
        if (ObjectUtil.isEmpty(modelCategoryDTO)) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_EMPTY_PARAM_ERROR);
        } else {
            if (isSubModelStatusActive(modelCategory)) {
                throw exception(ErrorCodeConstants.MODEL_CATEGORY_SUBMODEL_ACTIVE_ERROR);
            } else {
                modelCategory.setDeleted(true);
                return success(modelCategoryService.removeById(modelCategory));
            }
        }
    }

    /**
     * 编辑模板分类
     */
    @PostMapping(value = "/updateModelCategory")
    @Operation(summary = "编辑模板分类", description = "编辑模板分类")
    public CommonResult<Object> updateModelCategory(@RequestBody ModelCategoryDTO modelCategoryDTO) {
        ModelCategory modelCategory = ModelCategoryConverter.INSTANCE.toEntity(modelCategoryDTO);
        if (ObjectUtil.isEmpty(modelCategoryDTO)) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_EMPTY_PARAM_ERROR);
        }
        if (isNameExists(modelCategory.getName())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_NAME_EXISTS_ERROR);
        }
        if (isCodeExists(modelCategory.getCode())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CODE_EXISTS_ERROR);
        }
        return success(modelCategoryService.updateById(modelCategory));

    }

    /**
     * 判断模板分类下是否有启用状态的模板
     */
    public boolean isSubModelStatusActive(ModelCategory modelCategory) {
        LambdaQueryWrapper<Model> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Model::getCategoryId, modelCategory.getId());
        List<Model> models = modelService.list(queryWrapper);
        return isStatusActive(models);
    }

    /**
     * 模板分类中是否含有启动状态的
     */
    public boolean isStatusActive(List<Model> models) {
        return models.stream().anyMatch(dto -> dto.getStatus() == 1);
    }

    /**
     * 是否名称已存在
     */
    public boolean isNameExists(String name) {
        List<ModelCategory> modelCategories = modelCategoryService.list();
        List<ModelCategory> filteredModelCategories =
                modelCategories.stream()
                        .filter(category -> name.equals(category.getName()))
                        .collect(Collectors.toList());
        return CollUtil.isNotEmpty(filteredModelCategories);
    }

    /**
     * 是否名称已存在
     */
    public boolean isCodeExists(String code) {
        List<ModelCategory> modelCategories = modelCategoryService.list();
        List<ModelCategory> filteredModelCategories =
                modelCategories.stream()
                        .filter(category -> code.equals(category.getCode()))
                        .collect(Collectors.toList());
        return CollUtil.isNotEmpty(filteredModelCategories);
    }
}
