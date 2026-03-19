package com.yaoan.module.econtract.controller.admin.outward.template;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.outward.template.vo.ModelApiListReqVO;
import com.yaoan.module.econtract.controller.admin.outward.template.vo.ModelApiListRespVO;
import com.yaoan.module.econtract.service.model.ModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description: 对外的合同模板
 * @author: Pele
 * @date: 2024/3/4 10:14
 */
@Slf4j
@Validated
@RestController
@RequestMapping("template")
@Tag(name = "对外API的合同模板", description = "对外的合同模板")
public class ModelOutwardController {
    @Resource
    private ModelService modelService;

    /**
     * API_模板列表
     */
    @PostMapping("/list")
    @Operation(summary = "API_模板列表")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<ModelApiListRespVO>> list(@RequestBody ModelApiListReqVO vo) throws Exception {
        return success(modelService.list(vo));
    }

    /**
     * API_模板查询
     */
    @PostMapping(value = "/search/list")
    @Operation(summary = "API_模板查询", description = "API_模板查询")
    @OperateLog(logArgs = false)
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<PageResult<ModelApiListRespVO>> searchList(@RequestBody ModelApiListReqVO vo) {
        return success(modelService.searchList(vo));
    }



}
