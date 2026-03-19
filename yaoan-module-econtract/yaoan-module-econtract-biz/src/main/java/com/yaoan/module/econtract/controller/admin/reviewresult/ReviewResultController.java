package com.yaoan.module.econtract.controller.admin.reviewresult;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.pojo.CommonResult;
import static com.yaoan.framework.common.pojo.CommonResult.success;

import com.yaoan.framework.excel.core.util.ExcelUtils;

import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import static com.yaoan.framework.operatelog.core.enums.OperateTypeEnum.*;

import com.yaoan.module.econtract.controller.admin.reviewresult.vo.*;
import com.yaoan.module.econtract.dal.dataobject.reviewresult.ReviewResultDO;
import com.yaoan.module.econtract.convert.reviewresult.ReviewResultConvert;
import com.yaoan.module.econtract.service.reviewresult.ReviewResultService;

@Tag(name = "管理后台 - 智能审查结果")
@RestController
@RequestMapping("/econtract/review-result")
@Validated
public class ReviewResultController {

    @Resource
    private ReviewResultService reviewResultService;

    @PostMapping("/create")
    @Operation(summary = "创建智能审查结果")
    public CommonResult<String> createReviewResult(@Valid @RequestBody ReviewResultCreateReqVO createReqVO) {
        return success(reviewResultService.createReviewResult(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新智能审查结果")
    public CommonResult<Boolean> updateReviewResult(@Valid @RequestBody ReviewResultUpdateReqVO updateReqVO) {
        reviewResultService.updateReviewResult(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除智能审查结果")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteReviewResult(@RequestParam("id") String id) {
        reviewResultService.deleteReviewResult(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得智能审查结果")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<ReviewResultRespVO> getReviewResult(@RequestParam("id") String id) {
        ReviewResultDO reviewResult = reviewResultService.getReviewResult(id);
        return success(ReviewResultConvert.INSTANCE.convert(reviewResult));
    }

    @GetMapping("/list")
    @Operation(summary = "获得智能审查结果列表")
    @Parameter(name = "ids", description = "编号列表", required = true, example = "1024,2048")
    public CommonResult<List<ReviewResultRespVO>> getReviewResultList(@RequestParam("ids") Collection<String> ids) {
        List<ReviewResultDO> list = reviewResultService.getReviewResultList(ids);
        return success(ReviewResultConvert.INSTANCE.convertList(list));
    }

    @GetMapping("/page")
    @Operation(summary = "获得智能审查结果分页")
    public CommonResult<PageResult<ReviewResultRespVO>> getReviewResultPage(@Valid ReviewResultPageReqVO pageVO) {
        return success(reviewResultService.getReviewResultPage(pageVO));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出智能审查结果 Excel")
    @OperateLog(type = EXPORT)
    public void exportReviewResultExcel(@Valid ReviewResultExportReqVO exportReqVO,
              HttpServletResponse response) throws IOException {
        List<ReviewResultDO> list = reviewResultService.getReviewResultList(exportReqVO);
        // 导出 Excel
        List<ReviewResultExcelVO> datas = ReviewResultConvert.INSTANCE.convertList02(list);
        ExcelUtils.write(response, "智能审查结果.xls", "数据", ReviewResultExcelVO.class, datas);
    }

}
