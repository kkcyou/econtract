package com.yaoan.module.econtract.controller.admin.formconfig.form;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormListRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormOneRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormSaveReqVO;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.dal.mysql.formconfig.FormItemMapper;
import com.yaoan.module.econtract.service.formconfig.form.ECMSFormService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/20 17:23
 */
@Slf4j
@Validated
@RestController
@RequestMapping("ecmsform")
@Tag(name = "表单模块", description = "表单模块")
public class FormController {
    @Resource
    private ECMSFormService ecmsFormService;

    /**
     * 新增表单
     */
    @PostMapping("/save")
    @Operation(summary = "新增表单")
    @OperateLog(logArgs = false)
    public CommonResult<String> save(@RequestBody FormSaveReqVO vo) throws Exception {
        return success(ecmsFormService.save(vo));
    }

    /**
     * 表单详情
     */
    @PostMapping("/getOne")
    @Operation(summary = "表单详情")
    @OperateLog(logArgs = false)
    public CommonResult<FormOneRespVO> getOne(@RequestBody IdReqVO vo) throws Exception {
        return success(ecmsFormService.getOne(vo));
    }

    /**
     * 编辑表单
     */
    @PostMapping("/update")
    @Operation(summary = "编辑表单")
    @OperateLog(logArgs = false)
    public CommonResult<String> update(@RequestBody FormUpdateReqVO vo) throws Exception {
        return success(ecmsFormService.update(vo));
    }

    /**
     * 删除表单
     */
    @PostMapping("/deleteBatch")
    @Operation(summary = "删除表单")
    @OperateLog(logArgs = false)
    public CommonResult<String> deleteBatch(@RequestBody IdReqVO vo) throws Exception {
        return success(ecmsFormService.deleteBatch(vo));
    }

    /**
     * 表单列表
     */
    @PostMapping("/list")
    @Operation(summary = "表单列表")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<FormListRespVO>> list(@RequestBody FormSaveReqVO vo) throws Exception {
        return success(ecmsFormService.list(vo));
    }

    /**
     * 根据业务id找到表单
     */
    @PostMapping("/listFormByBizId")
    @Operation(summary = "删除表单")
    @OperateLog(logArgs = false)
    public CommonResult<List<FormOneRespVO>> listFormByBizId(@RequestBody IdReqVO vo) throws Exception {
        return success(ecmsFormService.listFormByBizId(vo));
    }
}
