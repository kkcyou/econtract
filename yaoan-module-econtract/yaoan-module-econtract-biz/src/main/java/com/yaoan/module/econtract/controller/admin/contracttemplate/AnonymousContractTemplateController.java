package com.yaoan.module.econtract.controller.admin.contracttemplate;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.contracttemplate.vo.*;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.controller.admin.performtasktype.vo.DeleteVO;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.ContractTemplate;
import com.yaoan.module.econtract.service.contracttemplate.ContractTemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * @description: 参考模板
 * @author: Pele
 * @date: 2024/6/03 0:31
 */
@Slf4j
@RestController
@RequestMapping("anonymous/econtract/template")
@Validated
@Tag(name = "参考模板", description = "参考模板")
public class AnonymousContractTemplateController {

    @Resource
    private ContractTemplateService contractTemplateService;

    /**
     * 范本制作的列表展示（自己创建的，全状态范本）
     */
    @PostMapping("/getMyAllTemplatePage")
    @Operation(summary = "范本列表展示（自己创建的，全状态范本）")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<TemplateSimpleVo>> getMyAllTemplatePage(@RequestBody TemplateListReqVo vo) throws Exception {
        return success(contractTemplateService.getMyAllTemplatePage(vo));
    }

    /**
     * 范本库的范本列表展示（全表，审批通过状态范本）
     */
    @PostMapping("/getAllApprovedPage")
    @Operation(summary = "范本列表展示（全表，审批通过状态范本）")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<TemplateAllPermissionReqVo>> getAllApprovedPage(@RequestBody TemplateListReqVo vo) throws Exception {
        vo.setUploadType(1);// 只查文件上传的范本库
        return success(contractTemplateService.getAllTemplates(vo));
    }

    /**
     * 范本列表
     */
    @PostMapping("/page")
    @Operation(summary = "范本列表")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<TemplateAllPermissionReqVo>> getAllPage(@RequestBody TemplateListReqVo vo) {
        return success(contractTemplateService.getAllPage(vo));
    }

    /**
     * 获得单个范本信息
     */
    @PostMapping("/getTemplateById")
    @Operation(summary = "获得单个范本信息")
    @OperateLog(logArgs = false)
    public CommonResult<TemplateSingleVo> getTemplateById(@RequestBody IdReqVO vo) throws Exception {
        String id = vo.getId();
        return success(contractTemplateService.getTemplateById(id));
    }

    /**
     * 获得范本的发布机构
     */
    @PostMapping("/getPublishOrganList")
    @Operation(summary = "获得范本发布机构")
    @OperateLog(logArgs = false)
    public CommonResult<List<String>> getPublishOrganList() {
        return success(contractTemplateService.list().stream().map(ContractTemplate::getPublishOrgan).collect(Collectors.toList()));
    }

    /**
     * 保存参考模板
     */
    @Transactional(rollbackFor = Exception.class)
    @PutMapping("/saveTemplateByUpload")
    @Operation(summary = "保存参考模板")
    @OperateLog(logArgs = false)
    public CommonResult<String> saveTemplateByUpload(@Valid @ModelAttribute TemplateCreateVo vo) throws Exception {
        return success(contractTemplateService.saveTemplateByUpload(vo));
    }


    /**
     * 编辑参考模板
     */
    @PutMapping("/update")
    @Operation(summary = "编辑参考模板")
    @OperateLog(logArgs = false)
    public CommonResult<Boolean> updateTemplate(@Valid @ModelAttribute TemplateUpdateReqVO reqVO) throws Exception {
        contractTemplateService.updateTemplate(reqVO);
        return success(true);
    }

    /**
     * 根据参考模板展示相关条款
     */
    @PostMapping("/selectTermsByTemplateId")
    @Operation(summary = "根据参考模板展示相关条款")
    @OperateLog(logArgs = false)
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<List<TermListRespVO>> selectTermsByTemplateId(@RequestBody IdReqVO reqVO) {
        return success(contractTemplateService.selectTermsByTemplateId(reqVO));
    }


    /**
     * 预览范本
     */
//    @PostMapping("/getFiveTemplates")
//    @Operation(summary = "预览相关范本")
//    @OperateLog(logArgs = false)
//    public CommonResult<Object> getFiveTemplates(int templateCategoryId) throws Exception {
//        return success(contractTemplateService.getFiveTemplates(templateCategoryId));
//    }

    /**
     * 删除范本
     */
    @DeleteMapping("delete")
    @Operation(summary = "删除范本")
    @OperateLog(logArgs = false)
    public CommonResult<Boolean> deleteTemplate(@Valid @RequestBody DeleteVO reqVO) {
        contractTemplateService.deleteTemplate(reqVO);
        return success(true);
    }

    /**
     * 新增范本-直接发送审批
     * 返回 model_bpm_id
     */
    @PutMapping(value = "/insertTemplateAndSubmitApprove")
    @Operation(summary = "新增范本-直接发送审批", description = "新增范本-直接发送审批")
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(logArgs = false)
    public CommonResult<String> insertTemplateAndSubmitApprove(@ModelAttribute TemplateCreateSubmitReqVO vo) throws Exception {
        return success(contractTemplateService.insertTemplateAndSubmitApprove(vo));
    }

    /**
     * 编辑范本_直接发送审批
     * 返回 model_bpm_id
     */
    @PutMapping(value = "/updateTemplateAndSubmitApprove")
    @Operation(summary = "编辑范本_直接发送审批", description = "编辑范本_直接发送审批")
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(logArgs = false)
    public CommonResult<String> updateTemplateAndSubmitApprove(@ModelAttribute TemplateUpdateSubmitReqVO vo) throws Exception {
        return success(contractTemplateService.updateTemplateAndSubmitApprove(vo));
    }


    /**
     * 获得最新版本号
     */
    @GetMapping(value = "/version/{code}/{i}")
    @Operation(summary = "获得最新版本号", description = "获得最新版本号")
    @OperateLog(logArgs = false)
    public CommonResult<TemplateVersionVO> getTemplateVersion(@PathVariable String code, @PathVariable Integer i) {
        return success(contractTemplateService.getTemplateVersion(code, i));
    }

    /**
     * 查看模板历史版本
     */
    @GetMapping(value = "/history/{code}")
    @Operation(summary = "查看模板历史版本", description = "查看模板历史版本")
    @OperateLog(logArgs = false)
    public CommonResult<List<TemplateHistoryRespVO>> getModelHistory(@PathVariable String code) {
        return success(contractTemplateService.getModelHistory(code));
    }

    @PostMapping("/getTemplateQuotePage")
    @Operation(summary = "范本引用情况")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<TemplateQuotePageRespVO>> getTemplateQuotePage(@RequestBody TemplateQuotePageReqVO vo) {
        return success(contractTemplateService.getTemplateQuotePage(vo));
    }

    /**
     * 批量发布/批量取消
     */
    @PostMapping("/batch/publish")
    @Operation(summary = "批量发布/批量取消")
    @OperateLog(logArgs = false)
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Boolean> batchPublish(@RequestBody BatchPublishReqVO reqVO) {
        contractTemplateService.batchPublish(reqVO);
        return success(true);
    }

    /**
     * 查看单个参考模板
     */
    @PostMapping("/getTemplate")
    @Operation(summary = "查看单个参考模板")
    @OperateLog(logArgs = false)
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<TemplateOneRespVo> getTemplate(@RequestBody IdReqVO reqVO) {
        return success(contractTemplateService.getTemplate(reqVO.getId()));
    }

    /**
     * 范本导入富文本
     */
    @PutMapping("/insertRtf4Template")
    @Operation(summary = "")
    @OperateLog(logArgs = false)
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<String> insertRtf4Template(@ModelAttribute  TemplateCreateVo reqVO) {
        return success(contractTemplateService.insertRtf4Template(reqVO));
    }

    /**
     * 下载范本
     * 富文本转pdf后，下载
     */
    @GetMapping(value = "/downloadTemplate/{templateId}")
    @Operation(summary = "下载范本", description = "下载范本")
    @OperateLog(logArgs = false)
    public void downloadTemplate(HttpServletResponse response, @PathVariable String templateId ) throws Exception {
        contractTemplateService.downloadTemplate(response,templateId);
    }




}
