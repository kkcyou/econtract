package com.yaoan.module.econtract.controller.admin.review;

import cn.hutool.core.lang.tree.Tree;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.review.vo.*;
import com.yaoan.module.econtract.service.review.PointsTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 审查点类型管理
 */
@Slf4j
@RestController
@RequestMapping("econtract/reviewPointsType")
@Validated
@Tag(name = "审查点类型管理", description = "审查点类型操作接口")
public class PointsTypeController {
    @Resource
    private PointsTypeService pointsTypeService;

    @PostMapping("/getList")
    @Operation(summary = "获取审查点类型列表")
    public CommonResult<List<ReviewPointsTypeRespVO>> getPointsList(@RequestBody ReviewPointsTypeReqVO reviewPointsTypeReqVO) {
        return success(pointsTypeService.getPointsList(reviewPointsTypeReqVO));
    }


}
