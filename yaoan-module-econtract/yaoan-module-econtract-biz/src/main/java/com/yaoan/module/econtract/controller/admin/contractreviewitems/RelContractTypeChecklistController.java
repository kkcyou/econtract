package com.yaoan.module.econtract.controller.admin.contractreviewitems;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relcontracttypechecklist.RelContractTypeChecklistPageReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relcontracttypechecklist.RelContractTypeChecklistRespVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relcontracttypechecklist.RelContractTypeChecklistSaveReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewchecklist.ReviewChecklistRespVO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.RelContractTypeChecklistDO;
import com.yaoan.module.econtract.service.contractreviewitems.RelContractTypeChecklistService;
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

import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;


@Tag(name = "管理后台 - 合同类型-审查清单关联")
@RestController
@RequestMapping("/ecms/rel-contract-type-checklist")
@Validated
public class RelContractTypeChecklistController {
    @Resource
    private RelContractTypeChecklistService relContractTypeChecklistService;

    @PostMapping("/create")
    @Operation(summary = "创建合同类型-审查清单关联")
    public CommonResult<String> createRelContractTypeChecklist(@Valid @RequestBody RelContractTypeChecklistSaveReqVO createReqVO) {String anomymous= "";
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

    @GetMapping("/getListByContractType")
    @Operation(summary = "获得合同类型-根据合同类型查询清单列表")
    @PermitAll
    public CommonResult<List<ReviewChecklistRespVO>> getRelContractTypeChecklist(@RequestParam(value = "contractType", required = false) String contractType,@RequestParam(value = "projectCategoryCode", required = false) String projectCategoryCode ) {
        return success(relContractTypeChecklistService.getRelContractTypeChecklist(contractType,projectCategoryCode));
    }


}