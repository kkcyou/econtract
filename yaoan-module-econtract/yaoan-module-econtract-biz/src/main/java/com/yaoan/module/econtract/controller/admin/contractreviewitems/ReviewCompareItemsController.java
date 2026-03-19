package com.yaoan.module.econtract.controller.admin.contractreviewitems;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewcompareitems.ReviewCompareItemsPageReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewcompareitems.ReviewCompareItemsRespVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewcompareitems.ReviewCompareItemsSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ReviewCompareItemsDO;
import com.yaoan.module.econtract.service.contractreviewitems.ReviewCompareItemsService;
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
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.pojo.CommonResult.success;


@Tag(name = "管理后台 - 审查比对检测项")
@RestController
@RequestMapping("/ecms/compare")
@Validated
public class ReviewCompareItemsController {

    @Resource
    private ReviewCompareItemsService reviewCompareItemsService;

    @PostMapping("/create")
    @Operation(summary = "创建审查比对检测项")
    public CommonResult<String> createReviewCompareItems(@Valid @RequestBody ReviewCompareItemsSaveReqVO createReqVO) {
        return success(reviewCompareItemsService.createReviewCompareItems(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新审查比对检测项")
    public CommonResult<Boolean> updateReviewCompareItems(@Valid @RequestBody ReviewCompareItemsSaveReqVO updateReqVO) {
        reviewCompareItemsService.updateReviewCompareItems(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除审查比对检测项")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteReviewCompareItems(@RequestParam("id") String id) {
        reviewCompareItemsService.deleteReviewCompareItems(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得审查比对检测项")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<ReviewCompareItemsRespVO> getReviewCompareItems(@RequestParam("id") String id) {
        ReviewCompareItemsDO reviewCompareItems = reviewCompareItemsService.getReviewCompareItems(id);
        return success(BeanUtils.toBean(reviewCompareItems, ReviewCompareItemsRespVO.class));
    }

    @PermitAll
    @GetMapping("/getAll")
    @Operation(summary = "获得所有审查比对检测项")
    public List<Map<String, Object>> getAllReviewCompareItems() {
        List<ReviewCompareItemsDO> reviewCompareItems = reviewCompareItemsService.getAllReviewCompareItems();
        List<ReviewCompareItemsRespVO> compareItemsResps = BeanUtils.toBean(reviewCompareItems, ReviewCompareItemsRespVO.class);
        // 1. 先分组
        Map<String, List<ReviewCompareItemsRespVO>> groupedMap = compareItemsResps.stream()
                .collect(Collectors.groupingBy(ReviewCompareItemsRespVO::getItemFirstLevel));

        // 2. 对每个分组内的 List 进行排序
        groupedMap.forEach((key, list) -> {
            list.sort(Comparator.comparing(ReviewCompareItemsRespVO::getItemSecondLevel));
        });

        // Convert to list of maps format
        List<Map<String, Object>> result = new ArrayList<>();


        groupedMap.forEach((firstLevel, secondLevelMap) -> {
            List<Object> secondLevelList = new ArrayList<>();
            Map<String, Object> firstLevelMap = new HashMap<>();
            firstLevelMap.put("itemFirstLevel", firstLevel);
            secondLevelMap.forEach(item -> item.setItemFirstLevel(null).setItemSecondLevel(null));
            secondLevelList.addAll(secondLevelMap);
            firstLevelMap.put("children", secondLevelList);
            result.add(firstLevelMap);
        });

        return result;
    }

    @GetMapping("/page")
    @Operation(summary = "获得审查比对检测项分页")
    public CommonResult<PageResult<ReviewCompareItemsRespVO>> getReviewCompareItemsPage(@Valid ReviewCompareItemsPageReqVO pageReqVO) {
        PageResult<ReviewCompareItemsDO> pageResult = reviewCompareItemsService.getReviewCompareItemsPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ReviewCompareItemsRespVO.class));
    }

}