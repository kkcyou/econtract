package com.yaoan.module.econtract.controller.admin.code;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.excel.core.util.ExcelUtils;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.code.vo.CodeRuleCreateReqVO;
import com.yaoan.module.econtract.controller.admin.code.vo.ListCodeRuleReqVO;
import com.yaoan.module.econtract.controller.admin.code.vo.ListCodeRuleRespVO;
import com.yaoan.module.econtract.controller.admin.codegeneration.vo.CodeQueryReqVO;
import com.yaoan.module.econtract.service.code.CodeRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 编号规则配置
 */
@Slf4j
@RestController
@RequestMapping("econtract/code")
@Validated
@Tag(name = "编号规则配置", description = "编号规则配置")
public class CodeRuleController {
    @Resource
    private CodeRuleService codeRuleService;

    /**
     * 新增(修改)编号规则
     */
    @PostMapping(value = "/createOrUpdate")
    @Operation(summary = "新增(修改)编号规则")
    public CommonResult<String> createOrUpdate(@Valid @RequestBody CodeRuleCreateReqVO codeRuleCreateReqVO){
        String ContractId = codeRuleService.createOrUpdate(codeRuleCreateReqVO);
        return success(ContractId);
    }

    /**
     * 编号规则列表
     */
    @PostMapping(value = "/list")
    @Operation(summary = "编号规则列表")
    public CommonResult<PageResult<ListCodeRuleRespVO>> list(@RequestBody ListCodeRuleReqVO reqVO){
        return success(codeRuleService.list(reqVO));
    }

    /**
     * 查看编号规则详情
     */
    @GetMapping(value = "/getById")
    @Operation(summary = "查看编号规则详情")
    public CommonResult<ListCodeRuleRespVO> getById(@RequestParam("id") String id){
        return success(codeRuleService.getById(id));
    }

    /**
     * 根据编号规则生成编号
     */
    @PostMapping(value = "/generate")
    @Operation(summary = "根据编号规则生成编号")
    public CommonResult<String> generate(@RequestBody CodeQueryReqVO vo){
        return success(codeRuleService.generate(vo));
    }

    /**
     * 删除编号规则
     */
    @DeleteMapping(value = "/delete")
    @Operation(summary = "删除编号规则")
    public CommonResult<Boolean> delete(@RequestParam("id") String id){
        codeRuleService.delete(id);
        return success(true);
    }

    /**
     * 修改状态
     */
    @GetMapping(value = "/update/Status")
    @Operation(summary = "修改状态")
    public CommonResult<Boolean> updateStatus(@RequestParam("id") String id){
        codeRuleService.updateStatus(id);
        return success(true);
    }

    /**
     * 导出编码规则
     */
    @PostMapping(value = "/list/export")
    @Operation(summary = "导出编码规则")
    @OperateLog(logArgs = false)
    public void export(HttpServletResponse response, @RequestBody ListCodeRuleReqVO reqVO) throws IOException {
        PageResult<ListCodeRuleRespVO> result = codeRuleService.export(reqVO);
        // 设置文件名
        String filename = "编码规则.xlsx";

        // 设置响应头
        response.setContentType("application/vnd.ms-excel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        // 导出 Excel
        ExcelUtils.write(response, filename, "数据", ListCodeRuleRespVO.class, result.getList());
    }
}
