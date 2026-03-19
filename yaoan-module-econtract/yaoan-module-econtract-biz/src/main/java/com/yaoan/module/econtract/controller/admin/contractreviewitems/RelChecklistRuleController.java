package com.yaoan.module.econtract.controller.admin.contractreviewitems;

import com.alibaba.fastjson.JSONObject;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relchecklistrule.RelChecklistRulePageReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relchecklistrule.RelChecklistRuleRespVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relchecklistrule.RelChecklistRuleSaveReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relchecklistrule.ReviewRulesRespVO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.RelChecklistRuleDO;
import com.yaoan.module.econtract.service.contractreviewitems.RelChecklistRuleService;
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


@Tag(name = "管理后台 - 审查清单-审查规则关联")
@RestController
@RequestMapping("/ecms/rel-checklist-rule")
@Validated
public class RelChecklistRuleController {

    @Resource
    private RelChecklistRuleService relChecklistRuleService;

    @PostMapping("/create")
    @Operation(summary = "创建审查清单-审查规则关联")
    public CommonResult<String> createRelChecklistRule(@Valid @RequestBody RelChecklistRuleSaveReqVO createReqVO) {
        return success(relChecklistRuleService.createRelChecklistRule(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新审查清单-审查规则关联")
    public CommonResult<Boolean> updateRelChecklistRule(@Valid @RequestBody RelChecklistRuleSaveReqVO updateReqVO) {
        relChecklistRuleService.updateRelChecklistRule(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除审查清单-审查规则关联")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteRelChecklistRule(@RequestParam("id") String id) {
        relChecklistRuleService.deleteRelChecklistRule(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得审查清单-审查规则关联")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<RelChecklistRuleRespVO> getRelChecklistRule(@RequestParam("id") String id) {
        RelChecklistRuleDO relChecklistRule = relChecklistRuleService.getRelChecklistRule(id);
        return success(BeanUtils.toBean(relChecklistRule, RelChecklistRuleRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得审查清单-审查规则关联分页")
    public CommonResult<PageResult<RelChecklistRuleRespVO>> getRelChecklistRulePage(@Valid RelChecklistRulePageReqVO pageReqVO) {
        PageResult<RelChecklistRuleDO> pageResult = relChecklistRuleService.getRelChecklistRulePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, RelChecklistRuleRespVO.class));
    }


    //根据清单ids查询清单关联的规则
    @PostMapping("/getContractCheckListByCheckListIds")
    @Operation(summary = "获得审查清单-根据ids查询清单关联规则")
    @PermitAll
    public String getContractCheckListByCheckListIds(@RequestBody List<String> checkListIds) {

        return JSONObject.toJSONString(relChecklistRuleService.getRelChecklistRuleByIds(checkListIds));
    }


    @PostMapping("/getReviewRulesByCheckListIds")
    @Operation(summary = "获得审查清单-根据ids查询清单关联规则")
    @PermitAll
    public List<ReviewRulesRespVO> getReviewRulesByCheckListIds(@RequestBody List<String> checkListIds) {

        return relChecklistRuleService.getReviewRulesByCheckListIds(checkListIds);
    }
}