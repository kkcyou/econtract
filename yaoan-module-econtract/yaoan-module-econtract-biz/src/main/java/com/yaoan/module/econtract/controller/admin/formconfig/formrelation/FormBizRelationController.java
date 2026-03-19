package com.yaoan.module.econtract.controller.admin.formconfig.formrelation;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.formconfig.formrelation.vo.FormRelSaveReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.service.formconfig.rel.FormBizRelService;
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
 * @description:
 * @author: Pele
 * @date: 2024/3/20 18:00
 */
@Slf4j
@Validated
@RestController
@RequestMapping("formBizRel")
@Tag(name = "表单关系", description = "表单关系")
public class FormBizRelationController {
    @Resource
    private FormBizRelService formBizRelService;

    /**
     * 新增表单关系
     */
    @PostMapping("/save")
    @Operation(summary = "新增表单关系")
    @OperateLog(logArgs = false)
    public CommonResult<String> save(@RequestBody FormRelSaveReqVO vo) throws Exception {
        return success(formBizRelService.save(vo));
    }

    /**
     * 编辑表单关系
     */
    @PostMapping("/update")
    @Operation(summary = "编辑表单关系")
    @OperateLog(logArgs = false)
    public CommonResult<String> update(@RequestBody FormRelSaveReqVO vo) throws Exception {
        return success(formBizRelService.update(vo));
    }

    /**
     * 删除表单关系
     */
    @PostMapping("/delete")
    @Operation(summary = "删除表单关系")
    @OperateLog(logArgs = false)
    public CommonResult<String> delete(@RequestBody IdReqVO vo) throws Exception {
        return success(formBizRelService.delete(vo));
    }
}
