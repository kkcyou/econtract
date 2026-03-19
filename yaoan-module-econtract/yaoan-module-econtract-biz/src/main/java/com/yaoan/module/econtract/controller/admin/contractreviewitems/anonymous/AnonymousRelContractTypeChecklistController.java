package com.yaoan.module.econtract.controller.admin.contractreviewitems.anonymous;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relcontracttypechecklist.RelContractTypeChecklistSaveReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewchecklist.ReviewChecklistRespVO;
import com.yaoan.module.econtract.service.contractreviewitems.RelContractTypeChecklistService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description:
 * @author: Pele
 * @date: 2025-9-15 9:58
 */
@Tag(name = "管理后台(匿名访问) - 审查清单-审查规则关联")
@RestController
@RequestMapping("/anonymous/ecms/rel-checklist-rule")
@Validated
public class AnonymousRelContractTypeChecklistController {
    @Resource
    private RelContractTypeChecklistService relContractTypeChecklistService;

    @PermitAll
    @PostMapping("/create")
    @Operation(summary = "创建合同类型-审查清单关联")
    public CommonResult<String> createRelContractTypeChecklist(@Valid @RequestBody RelContractTypeChecklistSaveReqVO createReqVO) {
        String anomymous = "";
        return success(relContractTypeChecklistService.createRelContractTypeChecklist(createReqVO));
    }

    //
//    @PutMapping("/update")
//    @Operation(summary = "更新合同类型-审查清单关联")
//    public CommonResult<Boolean> updateRelContractTypeChecklist(@Valid @RequestBody RelContractTypeChecklistSaveReqVO updateReqVO) {
//        relContractTypeChecklistService.updateRelContractTypeChecklist(updateReqVO);
//        return success(true);
//    }
//
//    @DeleteMapping("/delete")
//    @Operation(summary = "删除合同类型-审查清单关联")
//    @Parameter(name = "id", description = "编号", required = true)
//    public CommonResult<Boolean> deleteRelContractTypeChecklist(@RequestParam("id") String id) {
//        relContractTypeChecklistService.deleteRelContractTypeChecklist(id);
//        return success(true);
//    }
//
//    @GetMapping("/get")
//    @Operation(summary = "获得合同类型-审查清单关联")
//    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    public CommonResult<RelContractTypeChecklistRespVO> getRelContractTypeChecklist(@RequestParam("id") String id) {
//        RelContractTypeChecklistDO relContractTypeChecklist = relContractTypeChecklistService.getRelContractTypeChecklist(id);
//        return success(BeanUtils.toBean(relContractTypeChecklist, RelContractTypeChecklistRespVO.class));
//    }
//
//    @GetMapping("/page")
//    @Operation(summary = "获得合同类型-审查清单关联分页")
//    public CommonResult<PageResult<RelContractTypeChecklistRespVO>> getRelContractTypeChecklistPage(@Valid RelContractTypeChecklistPageReqVO pageReqVO) {
//        PageResult<RelContractTypeChecklistDO> pageResult = relContractTypeChecklistService.getRelContractTypeChecklistPage(pageReqVO);
//        return success(BeanUtils.toBean(pageResult, RelContractTypeChecklistRespVO.class));
//    }
    @PermitAll
    @GetMapping("/getListByContractType")
    @Operation(summary = "获得合同类型-根据合同类型查询清单列表")
    public CommonResult<List<ReviewChecklistRespVO>> getRelContractTypeChecklist(@RequestParam(value = "contractType", required = false) String contractType, @RequestParam(value = "projectCategoryCode", required = false) String projectCategoryCode) {
        return success(relContractTypeChecklistService.getRelContractTypeChecklist(contractType, projectCategoryCode));
    }


}
