package com.yaoan.module.econtract.controller.admin.reviewitembasis;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.excel.core.util.ExcelUtils;
import com.yaoan.module.econtract.controller.admin.reviewitembasis.vo.*;
import com.yaoan.module.econtract.convert.reviewitembasis.ReviewItemBasisConvert;
import com.yaoan.module.econtract.dal.dataobject.reviewitembasis.ReviewItemBasisDO;
import com.yaoan.module.econtract.service.reviewitembasis.ReviewItemBasisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 合同审查规则依据")
@RestController
@RequestMapping("/ecms/review-item-basis")
@Validated
public class ReviewItemBasisController {

    @Resource
    private ReviewItemBasisService reviewItemBasisService;

    @PostMapping("/create")
    @Operation(summary = "创建合同审查规则依据")
    public CommonResult<String> createReviewItemBasis(@Valid @RequestBody ReviewItemBasisCreateReqVO createReqVO) {
        return success(reviewItemBasisService.createReviewItemBasis(createReqVO));
    }

    @PostMapping("/update")
    @Operation(summary = "更新合同审查规则依据")
    public CommonResult<Boolean> updateReviewItemBasis(@Valid @RequestBody ReviewItemBasisUpdateReqVO updateReqVO) {
        reviewItemBasisService.updateReviewItemBasis(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除合同审查规则依据")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteReviewItemBasis(@RequestParam("id") String id) {
        reviewItemBasisService.deleteReviewItemBasis(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得合同审查规则依据")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<ReviewItemBasisRespVO> getReviewItemBasis(@RequestParam("id") String id) {
        ReviewItemBasisDO reviewItemBasis = reviewItemBasisService.getReviewItemBasis(id);
        return success(ReviewItemBasisConvert.INSTANCE.convert(reviewItemBasis));
    }

    @GetMapping("/list")
    @Operation(summary = "获得合同审查规则依据列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    public CommonResult<List<ReviewItemBasisRespVO>> getReviewItemBasisList(@RequestParam("ids") Collection<String> ids) {
        List<ReviewItemBasisDO> list = reviewItemBasisService.getReviewItemBasisList(ids);
        return success(ReviewItemBasisConvert.INSTANCE.convertList(list));
    }

    @PostMapping("/page")
    @Operation(summary = "获得合同审查规则依据分页")
    public CommonResult<PageResult<ReviewItemBasisRespVO>> getReviewItemBasisPage(@Valid @RequestBody ReviewItemBasisPageReqVO pageVO) {
        PageResult<ReviewItemBasisDO> pageResult = reviewItemBasisService.getReviewItemBasisPage(pageVO);
        return success(ReviewItemBasisConvert.INSTANCE.convertPage(pageResult));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出合同审查规则依据 Excel")
    public void exportReviewItemBasisExcel(@Valid ReviewItemBasisExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ReviewItemBasisDO> list = reviewItemBasisService.getReviewItemBasisList(exportReqVO);
        // 导出 Excel
        List<ReviewItemBasisExcelVO> datas = ReviewItemBasisConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "合同审查规则依据.xls", "数据", ReviewItemBasisExcelVO.class, datas);
    }

}
