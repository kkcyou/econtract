package com.yaoan.module.econtract.controller.admin.review;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.module.econtract.controller.admin.review.vo.*;
import com.yaoan.module.econtract.service.review.ReviewContractService;
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
 * 审查清单和合同类型管理
 */
@Slf4j
@RestController
@RequestMapping("econtract/reviewContract")
@Validated
@Tag(name = "审查清单和合同类型管理", description = "审查清单和合同类型操作接口")
public class ReviewContractController {
    @Resource
    private ReviewContractService reviewContractService;

    @PostMapping("/create")
    @Operation(summary = "新增配置清单")
    public CommonResult<Object> create(@RequestBody ReviewContractCreateReqVO reviewContractCreateReqVO) {
        reviewContractService.create(reviewContractCreateReqVO);
        return success(true);
    }

    @PostMapping("/select")
    @Operation(summary = "查看配置清单")
    public CommonResult<ReviewContractRespVO> selectByTypeId(@RequestBody ReviewContractReqVO reqVO) {
        return success(reviewContractService.selectByTypeId(reqVO));
    }

    @PostMapping("/configLog")
    @Operation(summary = "配置记录")
    public CommonResult<ReviewConfigLogRespVO> getConfigLog(@RequestBody ReviewConfigLogReqVO reqVO) {
        return success(reviewContractService.getConfigLog(reqVO));
    }
}
