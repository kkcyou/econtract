package com.yaoan.module.econtract.controller.admin.change;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.idempotent.core.annotation.Idempotent;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.controller.admin.change.vo.*;
import com.yaoan.module.econtract.controller.admin.change.vo.changerisk.ChangeRiskListRespVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.enums.payment.ApprovePageFlagEnums;
import com.yaoan.module.econtract.service.change.ContractChangeService;
import com.yaoan.module.system.api.user.AdminUserApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * @description: 合同变动
 * @author: Pele
 * @date: 2024/1/24 16:40
 */
@Slf4j
@RestController
@RequestMapping("econtract/contract")
@Validated
@Tag(name = "合同变动", description = "合同变动")
public class ContractChangeController {
    @Resource
    private ContractChangeService contractChangeService;
    @Resource
    private AdminUserApi adminUserApi;

    /**
     * 保存补充协议
     */
    @PostMapping("/saveSupplement")
    @Operation(summary = "保存补充协议")
    @OperateLog(logArgs = false)
    public CommonResult<String> saveSupplement(@RequestBody ContractChangeSaveReqVO vo) throws Exception {
        return success(contractChangeService.saveSupplement(vo));
    }

    /**
     * 发起补充协议申请
     */
    @PostMapping("/submitContractChangeApproveFlowable")
    @Operation(summary = "发起补充协议申请")
    @OperateLog(logArgs = false)
    @Idempotent(timeout = 10, timeUnit = TimeUnit.SECONDS, message = "正在审批中，请勿重复提交")
    public CommonResult<String> submitContractChangeApproveFlowable(@Valid @RequestBody IdReqVO reqVO) throws Exception {
        return success(contractChangeService.submitContractChangeApproveFlowable(getLoginUserId(), reqVO));
    }

    /**
     * 合同变更-申请列表
     */
    @PostMapping("/listSubmitContractChange")
    @Operation(summary = "合同变更-申请列表")
    public CommonResult<PageResult<ContractChangePageRespVO>> listSubmitContractChange(@RequestBody @Validated ContractChangePageReqVO reqVO) {
        PageResult<ContractChangePageRespVO> result = contractChangeService.listSubmitContractChange(reqVO);
        return success(result);
    }

    /**
     * 合同变更-获取任务分页（0=全部 1=已审批 2=未审批）
     */
    @PostMapping("getAutoBpmAllTaskPage")
    @Operation(summary = "获取任务分页（0=全部 1=已审批 2=未审批）")
    public CommonResult<BigContractChangeListApproveRespVO> getAutoBpmAllTaskPage(@Valid @RequestBody CommonBpmAutoPageReqVO pageVO) {
        int flag = pageVO.getFlag();
        String loginId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        pageVO.setApplicantId(loginId);
        ApprovePageFlagEnums approvePageFlagEnums = ApprovePageFlagEnums.getInstance(flag);
        //先将模糊查询的名字设好
        if (ObjectUtil.isNotNull(pageVO.getApplicantName())) {
            pageVO.setUserList(adminUserApi.getUserIdsByNameLike(pageVO.getApplicantName()));
        }
        switch (approvePageFlagEnums) {
            case ALL:
                return success(contractChangeService.getBpmAllTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
            case DONE:
                return success(contractChangeService.getBpmDoneTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
            case TO_DO:
                return success(contractChangeService.getBpmToDoTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
            default:
                return success(new BigContractChangeListApproveRespVO());
        }
    }

    /**
     * 根据合同id获得相关变动合同列表（弹窗）
     */
    @PostMapping("/listContractChangeByMainContractId")
    @Operation(summary = "根据合同id获得相关变动合同列表（弹窗）")
    public CommonResult<List<ContractChangePageRespVO>> listContractChangeByMainContractId(@RequestBody @Validated IdReqVO reqVO) {
        List<ContractChangePageRespVO> result = contractChangeService.listContractChangeByMainContractId(reqVO);
        return success(result);
    }


    /** --------------------------------------- 0129简化版 业务信息存于Bpm表的接口 ------------------------------------------ */

    /**
     * 合同变更保存接口
     */
    @PostMapping("/saveFastSupplement")
    @Operation(summary = "合同变更保存接口")
    @OperateLog(logArgs = false)
    public CommonResult<String> saveFastSupplement(@RequestBody ContractChangeSaveReqVO vo) throws Exception {
        return success(contractChangeService.saveFastSupplement(vo));
    }


    /**
     * 发起补充协议申请(暂时)
     */
    @PostMapping("/submitContractChangeApproveFlowableFast")
    @Operation(summary = "发起补充协议申请(暂时)")
    @OperateLog(logArgs = false)
    @Idempotent(timeout = 10, timeUnit = TimeUnit.SECONDS, message = "正在审批中，请勿重复提交")
    public CommonResult<String> submitContractChangeApproveFlowableFast(@Valid @RequestBody IdReqVO reqVO) throws Exception {
        return success(contractChangeService.submitContractChangeApproveFlowableFast(getLoginUserId(), reqVO));
    }

    /**
     * fast保存并提交补充协议(暂时)
     */
    @PostMapping("/saveAndSubmitFastSupplement")
    @Operation(summary = "fast保存补充协议")
    @OperateLog(logArgs = false)
    public CommonResult<String> saveAndSubmitFastSupplement(@RequestBody ContractChangeSaveReqVO vo) throws Exception {
        return success(contractChangeService.saveAndSubmitFastSupplement(vo));
    }

    /**
     * fast编辑并提交补充协议(暂时)
     */
    @PostMapping("/updateAndSubmitFastSupplement")
    @Operation(summary = "fast保存补充协议")
    @OperateLog(logArgs = false)
    public CommonResult<String> updateAndSubmitFastSupplement(@RequestBody ContractChangeSaveReqVO vo) throws Exception {
        return success(contractChangeService.updateAndSubmitFastSupplement(vo));
    }

    /**
     * 合同变更-申请列表(暂时)
     */
    @PostMapping("/listSubmitContractChangeFast")
    @Operation(summary = "合同变更-申请列表(暂时)")
    public CommonResult<PageResult<ContractChangePageRespVO>> listSubmitContractChangeFast(@RequestBody @Validated ContractChangePageReqVO reqVO) {
        PageResult<ContractChangePageRespVO> result = contractChangeService.listSubmitContractChangeFast(reqVO);
        return success(result);
    }


    /**
     * 合同变更-获取任务分页（0=全部 1=已审批 2=未审批）(暂时)
     */
    @PostMapping("getAutoBpmAllTaskPageFast")
    @Operation(summary = "获取任务分页（0=全部 1=已审批 2=未审批）")
    public CommonResult<BigContractChangeListApproveRespVO> getAutoBpmAllTaskPageFast(@Valid @RequestBody CommonBpmAutoPageReqVO pageVO) {
        int flag = pageVO.getFlag();
        String loginId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        pageVO.setApplicantId(loginId);
        ApprovePageFlagEnums approvePageFlagEnums = ApprovePageFlagEnums.getInstance(flag);
        //先将模糊查询的名字设好
        if (ObjectUtil.isNotNull(pageVO.getApplicantName())) {
            pageVO.setUserList(adminUserApi.getUserIdsByNameLike(pageVO.getApplicantName()));
        }
        switch (approvePageFlagEnums) {
            case ALL:
                return success(contractChangeService.getBpmAllTaskPageFast(WebFrameworkUtils.getLoginUserId(), pageVO));
                //我发起的，已审批
            case DONE:
                return success(contractChangeService.getBpmDoneTaskPageFast(WebFrameworkUtils.getLoginUserId(), pageVO));
                //草稿，待审批
            case TO_DO:
                return success(contractChangeService.getBpmToDoTaskPageFast(WebFrameworkUtils.getLoginUserId(), pageVO));
            default:
                return success(new BigContractChangeListApproveRespVO());
        }
    }

    /**
     * 合同变更-状态变更,获取任务分页(0关闭&取消&作废、关闭、2取消&作废)
     *
     * @param pageVO
     * @return
     */
    @PostMapping(value = "/getAutoBpmOverTaskPageFast")
    @Operation(summary = "状态变更列表（关闭、取消、作废）", description = "获取合同列表，flag=0:关闭&取消&作废;1:关闭；2:取消&作废")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<ContractChangePageRespVO>> getAutoBpmOverTaskPageFast(@Parameter(name = "contractPageReqVO", description = "获取合同关闭/取消/作废列表")
                                                                             @RequestBody ContractChangePageReqVO pageVO) {
        PageResult<ContractChangePageRespVO> result = contractChangeService.getAutoBpmOverTaskPageFast(pageVO);
        return success(result);
    }

    /**
     * 根据合同id获得相关变动合同列表（弹窗）（暂时）
     */
    @PostMapping("/listContractChangeByMainContractIdFast")
    @Operation(summary = "根据合同id获得相关变动合同列表（弹窗）（暂时）")
    public CommonResult<List<ContractChangePageRespVO>> listContractChangeByMainContractIdFast(@RequestBody @Validated IdReqVO reqVO) {
        List<ContractChangePageRespVO> result = contractChangeService.listContractChangeByMainContractIdFast(reqVO);
        return success(result);
    }

    /**
     * fast编辑补充协议(暂时)
     */
    @PostMapping("/updateFastSupplement")
    @Operation(summary = "fast编辑补充协议")
    @OperateLog(logArgs = false)
    public CommonResult<String> updateFastSupplement(@RequestBody ContractChangeSaveReqVO vo) throws Exception {
        return success(contractChangeService.updateFastSupplement(vo));
    }

    /**
     * fast删除补充协议(暂时)
     */
    @PostMapping("/deleteFastSupplement")
    @Operation(summary = "fast删除补充协议(暂时)")
    @OperateLog(logArgs = false)
    public CommonResult<String> deleteFastSupplement(@RequestBody IdReqVO vo) throws Exception {
        return success(contractChangeService.deleteFastSupplement(vo));
    }

    /**
     * fast补充协议详情
     */
    @PostMapping("/getContractChangeById")
    @Operation(summary = "fast补充协议详情")
    @OperateLog(logArgs = false)
    public CommonResult<ContractChangeOneRespVO> getContractChangeById(@RequestBody IdReqVO vo) throws Exception {
        return success(contractChangeService.getContractChangeById(vo));
    }

    /**
     * 获得相关变动合同列表
     */
    @PostMapping("/getContractChangeList")
    @Operation(summary = "获得相关变动合同列表")
    public CommonResult<PageResult<ContractChangeListRespVO>> getContractChangeList(@RequestBody @Validated ContractChangeListReqVO reqVO) {
        PageResult<ContractChangeListRespVO> result = contractChangeService.getContractChangeList(reqVO);
        return success(result);
    }

    /**
     * 合同状态变更申请
     */
    @PostMapping("/applyStatusChange")
    @Operation(summary = "合同状态变更申请")
    @OperateLog(logArgs = false)
    public CommonResult<String> applyStatusChange(@Valid @RequestBody ContractChangeStatusSaveReqVO vo) throws Exception {
        return success(contractChangeService.applyStatusChange(vo));
    }
    /**
     * 查看合同状态变更详情
     */
    @PostMapping("/getContractChangeDetails")
    @Operation(summary = "查看合同状态变更详情")
    @OperateLog(logArgs = false)
    public CommonResult<ContractChangeOneRespVO> getContractChangeDetails(@RequestBody IdReqVO vo) throws Exception {
        return success(contractChangeService.getContractChangeDetails(vo));
    }

    /**
     * 批量发起流程
     */
    @PostMapping("/batchInitiateStatusChangeApproval")
    @Operation(summary = "批量发起流程")
    @OperateLog(logArgs = false)
    @Idempotent(timeout = 10, timeUnit = TimeUnit.SECONDS, message = "正在审批中，请勿重复提交")
    public CommonResult<String> batchInitiateStatusChangeApproval(@Valid @RequestBody IdReqVO reqVO) throws Exception {
        return success(contractChangeService.batchInitiateStatusChangeApproval(getLoginUserId(), reqVO));
    }

    /**
     * 获取关键要素下拉框
     */
    @PostMapping("/getElementList")
    @Operation(summary = "获取关键要素下拉框")
    @OperateLog(logArgs = false)
    public CommonResult<List<ElementRespVO>> getElementList() throws Exception {
        return success(contractChangeService.getElementList());
    }


    @PostMapping("/getContractChangeOrgtNum")
    @Operation(summary = "状态变更申请查询代办数量")
    public CommonResult<Long> getContractChangeOrgtNum(@Valid @RequestBody CommonBpmAutoPageReqVO pageVO){

        String loginId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        pageVO.setApplicantId(loginId);
        //先将模糊查询的名字设好
        if (ObjectUtil.isNotNull(pageVO.getApplicantName())) {
            pageVO.setUserList(adminUserApi.getUserIdsByNameLike(pageVO.getApplicantName()));
        }
        return success(contractChangeService.getContractChangeOrgtNum(pageVO));
    }


    /**
     * 校验合同是否在解除中
     * true = 在解除中
     * false= 没申请解除
     *  */
    @GetMapping("/checkContractTerminating")
    @Operation(summary = "校验合同是否在解除中")
    public CommonResult<Boolean> checkContractTerminating(@RequestParam("id") String id){
        return success(contractChangeService.checkContractTerminating(id));
    }

    /**
     * 合同风险确认列表
     *  */
    @GetMapping("/changeRiskConfirmList")
    @Operation(summary = "合同风险确认列表")
    public CommonResult<ChangeRiskListRespVO> contractRiskConfirmList(@RequestParam("id") String id){
        return success(contractChangeService.changeRiskConfirmList(id));
    }

}
