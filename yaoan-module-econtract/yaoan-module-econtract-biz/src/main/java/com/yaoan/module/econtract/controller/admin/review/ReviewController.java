package com.yaoan.module.econtract.controller.admin.review;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.review.vo.*;
import com.yaoan.module.econtract.service.review.ReviewService;
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

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 审查清单管理
 */
@Slf4j
@RestController
@RequestMapping("econtract/review")
@Validated
@Tag(name = "审查清单管理", description = "审查清单操作接口")
public class ReviewController {
    @Resource
    private ReviewService reviewService;

    @PostMapping("/getList")
    @Operation(summary = "获取审查清单列表")
    public CommonResult<PageResult<ReviewPageRespVO>> getReviewList(@RequestBody ReviewPageReqVO vo) {
        return success(reviewService.getReviewList(vo));
    }

    @PostMapping("/create")
    @Operation(summary = "新增审查清单")
    public CommonResult<Object> create(@RequestBody ReviewCreateReqVO reviewCreateReqVO) {
        reviewService.create(reviewCreateReqVO);
        return success(true);
    }

    @PostMapping("/selectById")
    @Operation(summary = "根据id查看审查清单")
    public CommonResult<ReviewRespVO> selectById(@RequestBody ReviewReqVO reviewReqVO) {
        return success(reviewService.selectById(reviewReqVO));
    }

    @PostMapping("/update")
    @Operation(summary = "编辑审查清单")
    public CommonResult<Object> update(@RequestBody ReviewUpdateReqVO reviewUpdateReqVO) {
        reviewService.update(reviewUpdateReqVO);
        return success(true);
    }

    @PostMapping(value = "/delete")
    @Operation(summary = "批量删除审查清单")
    public CommonResult<Object> delete(@Valid @RequestBody ReviewIdListVO idListVO) throws Exception {
        reviewService.deleteByIdList(idListVO);
        return success(true);
    }

}
