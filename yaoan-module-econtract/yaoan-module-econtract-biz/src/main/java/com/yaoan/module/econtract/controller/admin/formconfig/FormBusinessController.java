package com.yaoan.module.econtract.controller.admin.formconfig;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.formbusiness.FormBusinessListReqVO;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.formbusiness.FormBusinessListRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.formbusiness.FormBusinessSaveReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.service.formconfig.FormBusinessService;
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
 * @description: 表单业务
 * @author: Pele
 * @date: 2024/3/19 10:19
 */
@Slf4j
@Validated
@RestController
@RequestMapping("formBusiness")
@Tag(name = "表单业务", description = "表单业务")
public class FormBusinessController {
    @Resource
    private FormBusinessService formBusinessService;

    /**
     * 新增表单业务
     */
    @PostMapping("/saveFormBusiness")
    @Operation(summary = "新增表单业务")
    @OperateLog(logArgs = false)
    public CommonResult<String> saveFormBusiness(@RequestBody FormBusinessSaveReqVO vo) throws Exception {
        return success(formBusinessService.saveFormBusiness(vo));
    }

    /**
     * 编辑表单业务
     */
    @PostMapping("/updateFormBusiness")
    @Operation(summary = "编辑表单业务")
    @OperateLog(logArgs = false)
    public CommonResult<String> updateFormBusiness(@RequestBody FormBusinessSaveReqVO vo) throws Exception {
        return success(formBusinessService.updateFormBusiness(vo));
    }
    /**
     * 删除表单业务
     */
    @PostMapping("/deleteFormBusiness")
    @Operation(summary = "删除表单业务")
    @OperateLog(logArgs = false)
    public CommonResult<String> deleteFormBusiness(@RequestBody IdReqVO vo) throws Exception {
        return success(formBusinessService.deleteFormBusiness(vo));
    }
    /**
     * 表单业务列表
     */
    @PostMapping("/listFormBusiness")
    @Operation(summary = "表单业务列表")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<FormBusinessListRespVO>> listFormBusiness(@RequestBody FormBusinessListReqVO vo) throws Exception {
        return success(formBusinessService.listFormBusiness(vo));
    }

}
