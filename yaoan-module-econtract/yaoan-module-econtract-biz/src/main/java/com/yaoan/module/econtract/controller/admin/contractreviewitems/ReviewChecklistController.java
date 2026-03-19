package com.yaoan.module.econtract.controller.admin.contractreviewitems;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewchecklist.ReviewChecklistPageReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewchecklist.ReviewChecklistRespVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewchecklist.ReviewChecklistSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ReviewChecklistDO;
import com.yaoan.module.econtract.service.contractreviewitems.ReviewChecklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;


@Tag(name = "管理后台 - 审查清单")
@RestController
@RequestMapping("/ecms/review-checklist")
@Validated
public class ReviewChecklistController {

    @Resource
    private ReviewChecklistService reviewChecklistService;

    @PostMapping("/create")
    @Operation(summary = "创建审查清单")
    public CommonResult<String> createReviewChecklist(@Valid @RequestBody ReviewChecklistSaveReqVO createReqVO) {
        return success(reviewChecklistService.createReviewChecklist(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新审查清单")
    public CommonResult<Boolean> updateReviewChecklist(@Valid @RequestBody ReviewChecklistSaveReqVO updateReqVO) {
        reviewChecklistService.updateReviewChecklist(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除审查清单")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteReviewChecklist(@RequestParam("id") String id) {
        reviewChecklistService.deleteReviewChecklist(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得审查清单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<ReviewChecklistRespVO> getReviewChecklist(@RequestParam("id") String id) {
        ReviewChecklistRespVO reviewChecklist = reviewChecklistService.getReviewChecklist(id);
        return success(reviewChecklist);
    }

    @PostMapping("/page")
    @Operation(summary = "获得审查清单分页")
    public CommonResult<PageResult<ReviewChecklistRespVO>> getReviewChecklistPage(@RequestBody @Valid ReviewChecklistPageReqVO pageReqVO) {
        PageResult<ReviewChecklistDO> pageResult = reviewChecklistService.getReviewChecklistPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ReviewChecklistRespVO.class));
    }

    //获取清单不分页
    @GetMapping("/getList")
    @Operation(summary = "获得审查清单不分页")
    public CommonResult<List<ReviewChecklistRespVO>> getReviewChecklistList() {
        return success(reviewChecklistService.getReviewChecklistList());
    }

    //更改状态
    @PostMapping("/updateStatus")
    @Operation(summary = "更改审查清单状态")
    public CommonResult<Boolean> updateReviewStatus(@Valid @RequestBody ReviewChecklistSaveReqVO updateReqVO) {
        reviewChecklistService.updateReviewChecklistStatus(updateReqVO);
        return success(true);
    }

}