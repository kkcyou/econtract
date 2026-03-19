package com.yaoan.module.econtract.controller.admin.signet;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.idempotent.core.annotation.Idempotent;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.signet.vo.*;
import com.yaoan.module.econtract.enums.payment.ApprovePageFlagEnums;
import com.yaoan.module.econtract.service.signet.SignetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;


@Slf4j
@RestController
@Tag(name = "印章管理", description = "印章管理")
@RequestMapping("econtract/signet")
public class SignetController {
    @Resource
    private SignetService signetService;
    /**
     * 添加印章
     */
    @PostMapping("/increaseSignet")
    @Operation(summary = "添加和修改印章")
    public CommonResult<String> addTerm(@Valid @RequestBody SignetCreateReqVO vo) {
         String result = signetService.increaseSignet(vo);
        return success(result);
    }

    /**
     * 删除印章
     */
    @DeleteMapping(value = "/deleteSignet")
    @Operation(summary = "删除印章")
    public CommonResult<Boolean> deleteSignet(@RequestParam("id") String id){
        signetService.deleteSignet(id);
        return success(true);
    }

    /**
     * 获取已启用印章列表
     */
    @GetMapping("/getEnableSignetList")
    @Operation(summary = "获取已启用印章列表")
    public CommonResult<List<SignetDetailsRespVO>> getEnableSignetList() {
        List<SignetDetailsRespVO> signetList = signetService.getEnableSignetList();
        return success(signetList);
    }

    /**
     * 查看印章分页列表
     */
    @PostMapping("/getSignetListPage")
    @Operation(summary = "查看印章分页列表")
    public CommonResult<PageResult<SignetPageRespVO>> getSignetList(@RequestBody SignetPageReqVO vo) {
        PageResult<SignetPageRespVO> signetList = signetService.getSignetList(vo);
        return success(signetList);
    }

    /**
     * 根据id查询印章详情
     */
    @PostMapping("/getSignetDetails")
    @Operation(summary = "根据id查询印章详情")
    public CommonResult<SignetDetailsRespVO> getSignetDetails(@Valid @RequestBody SignetDetailsReqVO vo) throws Exception {
        SignetDetailsRespVO signetDetails = signetService.getSignetDetails(vo);
        return success(signetDetails);
    }

    /**
     * 修改印章状态
     */
    @PostMapping("/updateSignetStatus")
    @Operation(summary = "修改印章状态")
    public CommonResult<Boolean> updateSignetStatus(@Valid @RequestBody SignetDetailsReqVO vo) {
        signetService.updateSignetStatus(vo);
        return success(true);
    }

    /**
     * 获取印章规格下拉框
     */
    @GetMapping("/getSignetSpecs")
    @Operation(summary = "获取印章规格下拉框")
    public CommonResult<List<SignetSpecsVO>> getSignetSpecs() {
        List<SignetSpecsVO> result = signetService.getSignetSpecs();
        return success(result);
    }

    /**
     * 获取印章类型下拉框
     */
    @GetMapping("/getSignetType")
    @Operation(summary = "获取印章类型下拉框")
    public CommonResult<List<SignetTypeVO>> getSignetType() {
        List<SignetTypeVO> result = signetService.getSignetType();
        return success(result);
    }

    /**
     * 获取流程配置下拉框
     */
    @GetMapping("/getSignetProcessList")
    @Operation(summary = "获取流程配置下拉框")
    public CommonResult<List<SealProcessRespVO>> getSignetProcessList() {
        List<SealProcessRespVO> list = signetService.getSignetProcessList();
        return success(list);
    }


    /**
     * 保存和修改用印申请
     */
    @PostMapping("/save")
    @Operation(summary = "保存和修改用印申请")
    @OperateLog(logArgs = false)
    public CommonResult<String> save(@RequestBody SealBpmReqVO reqVO) throws Exception {
        return success(signetService.save(reqVO));
    }

    /**
     * 发起用印申请
     */
    @PostMapping("/createProcess")
    @Operation(summary = "发起用印申请")
    @OperateLog(logArgs = false)
    @Idempotent(timeout = 10, timeUnit = TimeUnit.SECONDS, message = "正在审批中，请勿重复提交")
    public CommonResult<String> createProcess(@RequestBody SealBpmReqVO reqVO) {
        return success(signetService.createProcess(getLoginUserId(), reqVO));
    }

    /**
     * 获取用印申请列表
     */
    @PostMapping("/getSignetProcessPage")
    @Operation(summary = "获取用印申请列表")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<SignetProcessPageRespVO>> getSignetProcessPage(@RequestBody SealApplicationListBpmReqVO reqVO) {
        return success(signetService.getSignetProcessPage(reqVO));
    }


    @GetMapping("/get")
    @Operation(summary = "获取用印申请详情")
    public CommonResult<SignetManageRespVO> getSignetManage(@RequestParam("id") String id) {
        SignetManageRespVO result = signetService.getSignetManage(id);
        return success(result);
    }

    /**
     * 获取任务分页（0=全部 1=已审批 2=未审批）
     */
    @PostMapping("getAutoBpmAllTaskPage")
    @Operation(summary = "获取任务分页（0=全部 1=已审批 2=未审批）")
    public CommonResult<PageResult<SignetProcessPageRespVO>> getAutoBpmAllTaskPage(@Valid @RequestBody SealApplicationListBpmReqVO pageVO) {
        // 0=全部 1=我发起的 2=草稿
        int flag = pageVO.getFlag();
        String loginId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        pageVO.setApplicantId(loginId);
        ApprovePageFlagEnums approvePageFlagEnums = ApprovePageFlagEnums.getInstance(flag);
        switch (approvePageFlagEnums) {
            case ALL:
                return success(signetService.getBpmAllTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
                //我发起的
            case DONE:
                return success(signetService.getBpmDoneTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
                //草稿
            case TO_DO:
                return success(signetService.getBpmToDoTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
            default:
                return success(new PageResult<SignetProcessPageRespVO>());
        }
    }

    /**
     * 驳回用印申请
     */
    @PostMapping("/rejectSignetProcess")
    @Operation(summary = "驳回用印申请")
    @OperateLog(logArgs = false)
    public CommonResult<Boolean> rejectSignetProcess(@RequestBody RejectSignetProcessReqVO reqVO) {
        signetService.rejectSignetProcess(reqVO);
        return success(true);
    }
    /**
     * 删除用印申请
     */
    @DeleteMapping(value = "/delete")
    @Operation(summary = "删除用印申请")
    public CommonResult<Boolean> delete(@RequestParam("id") String id){
        signetService.delete(id);
        return success(true);
    }



    @GetMapping("/getSignetsByContractId")
    @Operation(summary = "获取用印申请详情")
    public CommonResult<List<SignetListRespVO>> getSignetsByContractId(@RequestParam("contractId") String contractId) {
        return success(signetService.getSignetsByContractId(contractId));
    }


    @PostMapping("/getAgentNum")
    @Operation(summary = "用章申请查询代办数量")
    public CommonResult<SignetAgentNumRespVO> getAgentNum(@Valid @RequestBody SealApplicationListBpmReqVO pageVO){
        String loginId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        pageVO.setApplicantId(loginId);
        return success(signetService.getAgentNum(pageVO));
    }

}
