package com.yaoan.module.econtract.controller.admin.model;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.controller.admin.contracttemplate.vo.TemplateCreateVo;
import com.yaoan.module.econtract.controller.admin.model.vo.*;
import com.yaoan.module.econtract.controller.admin.model.vo.client.ModelClientRespVO;
import com.yaoan.module.econtract.service.model.ModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;

/**
 * 合同模板
 *
 * @author: Pele
 * @date: 2023/8/3 15:56
 */
@Slf4j
@RestController
@RequestMapping("econtract/model")
@Validated
@Tag(name = "合同模板", description = "合同模板操作接口")
public class ModelController {

    @Resource
    private ModelService modelService;

    /**
     * 展示全部审批通过的列表(模板库)
     */
    @PostMapping("/getAllModelPage")
    @Operation(summary = "模板库-全部审批通过的列表")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<ModelStoreRespVO>> getAllModelPage(@RequestBody ModelListReqVO vo) throws Exception {
        return success(modelService.getAllModelPage(vo));
    }

    /**
     * 展示当前用户创建的所有状态的模板(制作模板)
     */
    @PostMapping("/getMyModelPage")
    @Operation(summary = "展示当前用户创建的所有状态的模板(制作模板)")
    @OperateLog(logArgs = false)
    public CommonResult<ModelBigPageRespVO> getMyModelPage(@RequestBody ModelListReqVO vo) throws Exception {
        return success(modelService.getMyModelPage(vo));
    }

    /**
     * 查看单个模板（jiale）
     */
    @PostMapping("/getModel")
    @Operation(summary = "查看单个模板")
    @OperateLog(logArgs = false)
    public CommonResult<ModelSingleRespVo> getModel(@RequestBody IdReqVO vo) throws Exception {
        return success(modelService.getModel(vo.getId()));
    }

    /**
     * 批量删除模板
     */
    @PostMapping(value = "/delete")
    @Operation(summary = "批量删除模板", description = "批量删除模板")
    @OperateLog(logArgs = false)
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Object> deleteModels(@RequestBody DelReqVo vo) throws Exception {
        modelService.deleteModels(vo.getIds());
        return success(true);
    }

    /**
     * 编辑模板(激活状态不可编辑) doujiale
     */
    @PutMapping(value = "/updateModel")
    @Operation(summary = "编辑模板", description = "编辑模板")
    @OperateLog(logArgs = false)
    public CommonResult<String> updateModel(@ModelAttribute ModelUpdateVO vo) throws Exception {
        String id;
        id = modelService.updateModel(vo);
        return success(id);
    }

    /**
     * 新增模板-上传文件-保存
     * 上传成功，返回url；上传失败，返回“fail”
     */
    @PutMapping(value = "/insertModelByUpload")
    @Operation(summary = "新增模板-上传文件-保存", description = "新增模板-上传文件-保存")
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(logArgs = false)
    public CommonResult<ModelIdCodeRespVO> insertModelByUpload(@ModelAttribute ModelCreateReqVO vo) throws Exception {
        return success(modelService.insertModelByUpload(vo));
    }

    /**
     * 推模板数据
     * 上传成功，返回url；上传失败，返回“fail”
     */
    @PostMapping(value = "/pushModel")
    @Operation(summary = "推模板数据", description = "推模板数据")
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(logArgs = false)
    public CommonResult<ModelIdCodeRespVO> pushModel(@RequestBody ModelCreateReqVO vo) throws Exception {
        return success(modelService.insertModelByUpload(vo));
    }

    /**
     * 新增模板-直接发送审批
     * 返回 model_bpm_id
     */
    @PutMapping(value = "/insertModelAndSubmitApprove")
    @Operation(summary = "新增模板-直接发送审批", description = "新增模板-直接发送审批")
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(logArgs = false)
    public CommonResult<String> insertModelAndSubmitApprove(@ModelAttribute ModelCreateSubmitReqVO vo) throws Exception {
        return success(modelService.insertModelAndSubmitApprove(vo));
    }

    /**
     * 编辑模板-直接发送审批
     * 返回 model_bpm_id
     */
    @PutMapping(value = "/updateModelAndSubmitApprove")
    @Operation(summary = "编辑模板-直接发送审批", description = "编辑模板-直接发送审批")
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(logArgs = false)
    public CommonResult<String> updateModelAndSubmitApprove(@ModelAttribute @Validated ModelUpdateSubmitReqVO vo) throws Exception {
        return success(modelService.updateModelAndSubmitApprove(vo));
    }

    /**
     * 根据合同类型，获得模板
     * 返回模板id和名称
     */
    @PutMapping(value = "/getModelByContractType")
    @Operation(summary = "根据合同类型，返回模板id和名称", description = "根据合同类型，返回模板id和名称")
    @Transactional(rollbackFor = Exception.class)
    @OperateLog(logArgs = false)
    public CommonResult<ModelGetContractTypeRespVO> getModelByContractType(@RequestBody IdReqVO vo) throws Exception {
        return success(modelService.getModelByContractType(vo));
    }

    /**
     * 查看模板历史版本
     */
    @GetMapping(value = "/model/history/{code}")
    @Operation(summary = "查看模板历史版本", description = "查看模板历史版本")
    @OperateLog(logArgs = false)
    public CommonResult<List<ModelPageRespVO>> getModelHistory(@PathVariable String code) throws Exception {
        return success(modelService.getModelHistoryV1(code));
    }

    /**
     * 获得最新版本号
     */
    @GetMapping(value = "/model/version/{code}/{i}")
    @Operation(summary = "获得最新版本号", description = "获得最新版本号")
    @OperateLog(logArgs = false)
    public CommonResult<ModelVersionVO> getModelVersion(@PathVariable String code, @PathVariable Integer i) throws Exception {
        return success(modelService.getModelVersion(code, i));
    }

    /**
     * 修改启用状态 -生效时间 启用字段
     */
    @PutMapping(value = "/model/enable")
    @Operation(summary = "修改启用状态", description = "修改启用状态")
    @OperateLog(logArgs = false)
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Object> updateModelEnable(@RequestParam String id) {
        Boolean b = modelService.updateModelEnable(id);
        return success(b);
    }

    /**
     * 修改收藏状态
     */
    @PutMapping(value = "/model/collect")
    @Operation(summary = "修改收藏状态", description = "修改收藏状态")
    @OperateLog(logArgs = false)
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<Object> updateModelCollect(@RequestParam String id) {
        Boolean b = modelService.updateModelCollect(id);
        return success(b);
    }

    /**
     * 模板导入富文本
     */
    @PutMapping("/insertRtf4Model")
    @Operation(summary = "")
    @OperateLog(logArgs = false)
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<String> insertRtf4Model(@ModelAttribute ModelCreateReqVO reqVO) {
        return success(modelService.insertRtf4Model(reqVO));
    }

    /**
     * 展示当前用户创建的所有状态的模板(制作模板)
     */
    @GetMapping("/getMyModelList")
    @Operation(summary = "展示当前用户创建的所有状态的模板(制作模板)列表")
    @OperateLog(logArgs = false)
    public CommonResult<List<ModelIdAndNameVO>> getMyModelList() throws Exception {
        return success(modelService.getMyModelList());
    }


    /**
     * 根据单位id同步政采模板
     */
    @GetMapping(value = "/model/getModelByOrgId/{orgId}")
    @Operation(summary = "根据单位id同步政采模板", description = "根据单位id同步政采模板")
    @OperateLog(logArgs = false)
    public CommonResult<Boolean> getModelByOrgId(@PathVariable String orgId) throws Exception {
        modelService.getModelByOrgId(orgId);
        return success(true);
    }


    /**
     * 获得平台列表
     */
    @GetMapping(value = "/model/listModelClient")
    @Operation(summary = "获得平台列表", description = "获得平台列表")
    @OperateLog(logArgs = false)
    @DataPermission(enable = false)
    public CommonResult<List<ModelClientRespVO>> listModelClient() throws Exception {
        return success( modelService.listModelClient());
    }


}
