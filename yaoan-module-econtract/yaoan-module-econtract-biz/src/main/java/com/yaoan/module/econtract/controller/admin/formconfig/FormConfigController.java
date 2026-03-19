package com.yaoan.module.econtract.controller.admin.formconfig;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.FormConfigSaveReqVO;
import com.yaoan.module.econtract.controller.admin.formconfig.vo.FormConfigSingleRespVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.service.formconfig.FormConfigService;
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
 * @description: 表单配置
 * @author: Pele
 * @date: 2024/3/18 15:49
 */
@Slf4j
@Validated
@RestController
@RequestMapping("formConfig")
@Tag(name = "表单配置", description = "表单配置")
public class FormConfigController {
    @Resource
    private FormConfigService formConfigService;

    /**
     * 新增表单配置
     */
    @PostMapping("/saveFormConfig")
    @Operation(summary = "新增表单配置")
    @OperateLog(logArgs = false)
    public CommonResult<String> saveFormConfig(@RequestBody FormConfigSaveReqVO vo) throws Exception {
        return success(formConfigService.saveFormConfig(vo));
    }

    /**
     * 新增表单业务
     */
    @PostMapping("/updateFormConfig")
    @Operation(summary = "新增表单业务")
    @OperateLog(logArgs = false)
    public CommonResult<String> updateFormConfig(@RequestBody FormConfigSaveReqVO vo) throws Exception {
        return success(formConfigService.updateFormConfig(vo));
    }


    /**
     * 根据业务id获得表单业务
     */
    @PostMapping("/getFormBusinessByBusinessId")
    @Operation(summary = "根据业务id获得表单业务")
    @OperateLog(logArgs = false)
    public CommonResult<FormConfigSingleRespVO> getFormBusinessByBusinessId(@RequestBody IdReqVO vo) throws Exception {
        return success(formConfigService.getFormBusinessByBusinessId(vo));
    }


}
