package com.yaoan.module.econtract.controller.admin.formconfig.item;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.formconfig.form.vo.FormSaveReqVO;
import com.yaoan.module.econtract.controller.admin.formconfig.item.vo.FormItemListRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.item.vo.FormItemOneRespVO;
import com.yaoan.module.econtract.controller.admin.formconfig.item.vo.FormItemSaveReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.service.formconfig.item.FormItemService;
import io.swagger.v3.oas.annotations.Operation;
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
 * @date: 2024/3/20 17:40
 */
@Slf4j
@Validated
@RestController
@RequestMapping("formItem")
public class FormItemController {
    @Resource
    private FormItemService formItemService;

    /**
     * 新增表项
     */
    @PostMapping("/save")
    @Operation(summary = "新增表项")
    @OperateLog(logArgs = false)
    public CommonResult<String> save(@RequestBody FormItemSaveReqVO vo) throws Exception {
        return success(formItemService.save(vo));
    }

    /**
     * 表项详情
     */
    @PostMapping("/getOne")
    @Operation(summary = "表项详情")
    @OperateLog(logArgs = false)
    public CommonResult<FormItemOneRespVO> getOne(@RequestBody IdReqVO vo) throws Exception {
        return success(formItemService.getOne(vo));
    }

    /**
     * 编辑表项
     */
    @PostMapping("/update")
    @Operation(summary = "编辑表项")
    @OperateLog(logArgs = false)
    public CommonResult<String> update(@RequestBody FormItemSaveReqVO vo) throws Exception {
        return success(formItemService.update(vo));
    }

    /**
     * 删除表项
     */
    @PostMapping("/deleteBatch")
    @Operation(summary = "删除表项")
    @OperateLog(logArgs = false)
    public CommonResult<String> deleteBatch(@RequestBody IdReqVO vo) throws Exception {
        return success(formItemService.deleteBatch(vo));
    }

    /**
     * 表项列表
     */
    @PostMapping("/list")
    @Operation(summary = "表单列表")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<FormItemListRespVO>> list(@RequestBody FormSaveReqVO vo) throws Exception {
        return success(formItemService.list(vo));
    }

    /**
     * 根据表单id获得表项
     */
    @PostMapping("/getItemsByFormId")
    @Operation(summary = "根据表单id获得表项")
    @OperateLog(logArgs = false)
    public CommonResult<List<FormItemListRespVO>> getItemsByFormId(@RequestBody IdReqVO vo) throws Exception {
        return success(formItemService.getItemsByFormId(vo));
    }
}
