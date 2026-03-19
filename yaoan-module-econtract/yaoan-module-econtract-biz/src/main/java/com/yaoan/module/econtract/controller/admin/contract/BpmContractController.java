package com.yaoan.module.econtract.controller.admin.contract;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.api.bpm.contract.BpmContractApiImpl;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.BigContractVersionListRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.*;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.enums.warning.WarningBusinessEnum;
import com.yaoan.module.econtract.framework.core.event.warning.EcmsWarningEvent;
import com.yaoan.module.econtract.framework.core.event.warning.EcmsWarningEventPublisher;
import com.yaoan.module.econtract.service.contract.ContractService;
import com.yaoan.module.econtract.service.contract.TaskService;
import com.yaoan.module.econtract.service.contract.version.ContractVersionService;
import com.yaoan.module.econtract.service.flow.FlowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

@Slf4j
@RestController
@RequestMapping("econtract/bpm")
@Validated
@Tag(name = "合同-任务流操作", description = "合同模块任务流操作接口")
public class BpmContractController {

    @Resource
    private ContractService contractService;
    @Resource
    private TaskService taskService;

    @Resource
    private FlowService flowService;

    @Resource
    private ContractVersionService contractVersionService;

    @Resource
    private EcmsWarningEventPublisher warningEventPublisher;

    
    @PostMapping(value = "/receiveContract")
    @Operation(summary = "接收第三方系统合同")
    @OperateLog(enable = false)
    @DataPermission
    public CommonResult<String> receiveContract(@Valid @RequestBody ContractReceiveVO contractReceiveVO) throws Exception {
        String ContractId = contractService.receiveContract(contractReceiveVO);
        warningEventPublisher.setEvent(new EcmsWarningEvent(this).setFlag("1").setModelCode(WarningBusinessEnum.CONTRACT_DRAFT.getCode()).setBussinessId(ContractId));
        return success(ContractId);
    }

    /**
     * 待发送页面 - 新增  -  发送
     * 修改合同签署状态 - 发起工作流
     * 双方/三方
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/drafting/{id}")
    @Operation(summary = "待发送 - 发送", description = "修改合同签署状态 - 发起工作流")
    public CommonResult<Boolean> sent(@PathVariable String id) {
        contractService.updateToSend(id);
        return success(true);
    }

    /**
     * 提交-多方合同确认签署流程
     * 待发送页面 - 新增  -  发送
     * 修改合同签署状态 - 发起工作流
     * 双方/三方
     */
    @PostMapping(value = "/submit")
    @Operation(summary = "待发送 - 发送", description = "修改合同签署状态 - 发起工作流")
    public CommonResult<String> submit(@RequestBody IdReqVO reqVO) {
        return success(contractService.submit(SecurityFrameworkUtils.getLoginUserId(), reqVO));
    }
    
    @PostMapping(value = "/govsubmit")
    @Operation(summary = "待发送 - 发送", description = "修改合同签署状态 - 发起工作流")
    public CommonResult<String> govsubmit(@RequestBody IdReqVO reqVO) {
        return success(contractService.govsubmit(SecurityFrameworkUtils.getLoginUserId(), reqVO));
    }

    /**
     * 待确认页面 - 发起签署
     * 修改合同签署状态 - 发起工作流
     *
     * @param contractBaseVO
     * @return
     */
    @Deprecated
    @PostMapping(value = "/initiate/signing")
    @Operation(summary = "待确认 - 发起签署", description = "修改合同签署状态 - 发起工作流")
    public CommonResult<Boolean> InitiateSigning(@Parameter(name = "contractBaseVO", description = "新增-发送")
                                                 @RequestBody ContractBaseVO contractBaseVO) {
        contractService.updateStatus4(contractBaseVO);
//        flowService.createContractSignProcessInstance(contractBaseVO.getId());
        return success(true);
    }

    @PutMapping("/send")
    @Operation(summary = "发送任务")
    public CommonResult<Boolean> sendTask(@Valid @RequestBody BpmTaskApproveReqVO reqVO) {
        taskService.sendTask(getLoginUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/approve")
    @Operation(summary = "通过任务")
    public CommonResult<Boolean> approveTask(@Valid @RequestBody BpmTaskApproveReqVO reqVO) throws Exception {
        taskService.approveTask(getLoginUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/reject")
    @Operation(summary = "不通过任务")
    public CommonResult<Boolean> rejectTask(@Valid @RequestBody BpmTaskRejectReqVO reqVO) {
        taskService.rejectTask(getLoginUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/revoke")
    @Operation(summary = "撤回任务")
    public CommonResult<Boolean> revokeTask(@Valid @RequestBody BpmTaskRevokeReqVO reqVO) {
        taskService.revokeTask(getLoginUserId(), reqVO);
        return success(true);
    }
    @PutMapping("/counterSignRevoke")
    @Operation(summary = "会签撤回任务")
    public CommonResult<Boolean> counterSignRevoke(@Valid @RequestBody BpmTaskRevokeReqVO reqVO) {
        taskService.counterSignRevokeTask(getLoginUserId(), reqVO);
        return success(true);
    }

    @PutMapping("/repeal")
    @Operation(summary = "撤销流程")
    public CommonResult<Boolean> repealTask(@Valid @RequestBody BpmTaskRepealReqVO reqVO) {
        taskService.repealTask(getLoginUserId(), reqVO);
        return success(true);
    }

    /**
     * 撤销流程V2(兼容多模块)
     */
    @PutMapping("/repealTaskV2")
    @Operation(summary = "撤销流程V2")
    public CommonResult<Boolean> repealTaskV2(@Valid @RequestBody BpmTaskRepealReqVO reqVO) {
        taskService.repealTaskV2(getLoginUserId(), reqVO);
        return success(true);
    }

    /** --------------------- 追加 ----------------------- */

    /**
     * 批量通过请求 接口
     */
    @PostMapping("/approveTaskBatch")
    @Operation(summary = " 批量通过请求 接口")
    @OperateLog(logArgs = false)
    public CommonResult<String> approveTaskBatch(@RequestBody List<BpmTaskApproveReqVO> voList) throws Exception {
        return success(taskService.approveTaskBatch(voList));
    }

    /**
     * 批量退回请求 接口
     */
    @PostMapping("/rejectTaskBatch")
    @Operation(summary = " 批量退回请求 接口")
    @OperateLog(logArgs = false)
    public CommonResult<String> rejectTaskBatch(@RequestBody List<BpmTaskRejectReqVO> voList) throws Exception {
        return success(taskService.rejectTaskBatch(voList));
    }

    /**
     * 合同版本列表 接口
     */
    @PostMapping("/listByContractId")
    @Operation(summary = " 批量退回请求 接口")
    @OperateLog(logArgs = false)
    public CommonResult<BigContractVersionListRespVO> listByContractId(@RequestBody IdReqVO vo) throws Exception {
        return success(contractVersionService.listByContractId(vo.getId()));
    }

    /**
     * 产品通过或退回合同审批接口
     */
    @PostMapping("/product_back")
    @Operation(summary = "产品退回合同审批接口")
    @OperateLog(logArgs = false)
    public CommonResult<String> productBackContract(@RequestBody IdReqVO vo) throws Exception {
        return success(contractVersionService.productBackContract(vo));
    }
}