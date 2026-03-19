package com.yaoan.module.econtract.controller.admin.codegeneration;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.codegeneration.vo.*;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.service.codegeneration.CodeGenerationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.common.IdentityConstraint;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description: 编号生成规则
 * @author: Pele
 * @date: 2024/9/5 10:28
 */
@Slf4j
@RestController
@RequestMapping("econtract/CodeGeneration")
@Validated
@Tag(name = "编号生成", description = "编号生成")
public class CodeGenerationController {

    @Resource
    private CodeGenerationService codeGenerationService;

    /**
     * 编号生成规则列表
     */
    @PostMapping("/list")
    @Operation(summary = "编号生成规则列表")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<CodeGenerationRespVO>> list(@RequestBody CodeGenerationReqVO vo) throws Exception {
        return success(codeGenerationService.list(vo));
    }

    /**
     * 编号生成规则新增
     */
    @PostMapping("/save")
    @Operation(summary = "编号生成规则新增")
    @OperateLog(logArgs = false)
    public CommonResult<String> save(@Valid @RequestBody CodeGenerationSaveReqVO vo) throws Exception {
        return success(codeGenerationService.save(vo));
    }

    /**
     * 获得编号
     */
    @PostMapping("/generateCodeByVO")
    @Operation(summary = "获得编号")
    @OperateLog(logArgs = false)
    public CommonResult<String> generateCodeByVO(@Valid @RequestBody CodeQueryReqVO vo) throws Exception {
        return success(codeGenerationService.generateCodeByVO(vo));
    }

    /**
     * 编号生成规则修改
     */
    @PostMapping("/update")
    @Operation(summary = "编号生成规则修改")
    @OperateLog(logArgs = false)
    public CommonResult<String> update(@RequestBody CodeGenerationUpdateVO vo) throws Exception {
        return success(codeGenerationService.update(vo));
    }

    /**
     * 批量删除
     */
    @PostMapping("/deleteBatch")
    @Operation(summary = "批量删除")
    @OperateLog(logArgs = false)
    public CommonResult<String> deleteBatch(@Valid @RequestBody IdReqVO vo) throws Exception {
        return success(codeGenerationService.deleteBatch(vo.getIdList()));
    }


    /**
     * 详情查看
     */
    @PostMapping("/queryCodeRuleById")
    @Operation(summary = "详情查看")
    @OperateLog(logArgs = false)
    public CommonResult<CodeGenDetailRespVO> queryCodeRuleById(@RequestBody IdReqVO vo) throws Exception {
        return success(codeGenerationService.queryCodeRuleById(vo.getId()));
    }
}
