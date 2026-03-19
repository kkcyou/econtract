package com.yaoan.module.econtract.controller.admin.bpm.contractborrow;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.idempotent.core.annotation.Idempotent;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.*;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord.BigContractBorrowRecordPageRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord.BorrowRecordRespVO;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord.ContractBorrowRecordPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord.ContractBorrowRecordPageRespVO;
import com.yaoan.module.econtract.enums.payment.ApprovePageFlagEnums;
import com.yaoan.module.econtract.service.bpm.contractborrow.ContractBorrowBpmService;
import com.yaoan.module.system.api.user.AdminUserApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

/**
 * @description: 合同借阅工作流模块
 * @author: Pele
 * @date: 2023/10/8 21:25
 */
@Slf4j
@RestController
@RequestMapping("econtract/bpm/contractborrow")
@Validated
@Tag(name = "合同借阅工作流模块", description = "合同借阅工作流模块操作接口")
public class ContractBorrowBpmController {
    @Resource
    private ContractBorrowBpmService borrowBpmService;

    @Resource
    private AdminUserApi adminUserApi;

    /**
     * 合同借阅请求审批发起
     */
    @PostMapping("/submitModelApproveFlowable")
    @Operation(summary = "合同借阅请求审批发起")
    @OperateLog(logArgs = false)
    @Idempotent(timeout = 10, timeUnit = TimeUnit.SECONDS, message = "正在审批中，请勿重复提交")
    public CommonResult<String> submitModelApproveFlowable(@RequestBody ContractBorrowBpmSubmitCreateReqVO reqVO) {
        return success(borrowBpmService.submitContractBorrowApproveFlowable(getLoginUserId(), reqVO));
    }

    /**
     * 档案借阅请求审批发起
     */
    @PostMapping("/archive/submit")
    @Operation(summary = "档案借阅请求审批发起")
    @OperateLog(logArgs = false)
    @Idempotent(timeout = 10, timeUnit = TimeUnit.SECONDS, message = "正在审批中，请勿重复提交")
    public CommonResult<String> submitArchiveApproveFlowable(@RequestBody ContractBorrowBpmSubmitCreateReqVO reqVO) {
        return success(borrowBpmService.submitArchiveApproveFlowable(getLoginUserId(), reqVO));
    }

    /**
     * 合同借阅申请草稿 - 发起申请
     */
    @GetMapping("/approve/{id}")
    @Operation(summary = "合同借阅申请草稿 - 发起申请")
    public CommonResult<Boolean> approve(@PathVariable("id") String id) {
        borrowBpmService.approve(getLoginUserId(), id);
        return success(true);
    }

    /**
     * 删除申请
     */
    @PostMapping("/delete/{id}")
    @Operation(summary = "删除申请")
    public CommonResult<Boolean> delete(@PathVariable("id") String id) {
        borrowBpmService.delete(id);
        return success(true);
    }

    /**
     * 合同借阅-获取任务分页（0=全部 1=已审批 2=未审批） 新
     */
    @PostMapping("getAutoBpmAllTaskPage")
    @Operation(summary = "获取任务分页（0=全部 1=已审批 2=未审批）")
    public CommonResult<BigContractBorrowRecordPageRespVO> getAutoBpmAllTaskPage(@Valid @RequestBody ContractBorrowBpmPageReqVO pageVO) {
        int flag = pageVO.getFlag();
        String loginId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        pageVO.setApplicantId(loginId);
        ApprovePageFlagEnums approvePageFlagEnums = ApprovePageFlagEnums.getInstance(flag);
        //先将模糊查询的名字设好
        if (ObjectUtil.isNotNull(pageVO.getApplicantName())) {
            List<String> userIdsByNameLike = adminUserApi.getUserIdsByNameLike(pageVO.getApplicantName());
            if (CollectionUtil.isEmpty(userIdsByNameLike)) {
                PageResult<ContractBorrowRecordPageRespVO> pageResult = new PageResult<ContractBorrowRecordPageRespVO>()
                        .setList(Collections.emptyList()).setTotal(0L);
                return success(new BigContractBorrowRecordPageRespVO().setPageResult(pageResult));
            }
            pageVO.setUserList(userIdsByNameLike);
        }
        switch (approvePageFlagEnums) {
            case ALL:
                return success(borrowBpmService.getBpmAllTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
            case DONE:
                return success(borrowBpmService.getBpmDoneTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
            case TO_DO:
                return success(borrowBpmService.getBpmToDoTaskPage(WebFrameworkUtils.getLoginUserId(), pageVO));
            default:
                return success(new BigContractBorrowRecordPageRespVO());
        }
    }

    /**
     * 合同借阅-获取任务分页（0=全部 1=已审批 2=未审批） 新 - 档案
     */
    @PostMapping("/archive/Page")
    @Operation(summary = "获取任务分页（0=全部 1=已审批 2=未审批）")
    public CommonResult<BigContractBorrowRecordPageRespVO> getArchivePage(@Valid @RequestBody ContractBorrowBpmPageReqVO pageVO) {
        int flag = pageVO.getFlag();
        String loginId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        pageVO.setApplicantId(loginId);
        ApprovePageFlagEnums approvePageFlagEnums = ApprovePageFlagEnums.getInstance(flag);
        //先将模糊查询的名字设好
        if (ObjectUtil.isNotEmpty(pageVO.getApplicantName())) {
            List<String> userIdsByNameLike = adminUserApi.getUserIdsByNameLike(pageVO.getApplicantName());
            if (CollectionUtil.isEmpty(userIdsByNameLike)) {
                PageResult<ContractBorrowRecordPageRespVO> pageResult = new PageResult<ContractBorrowRecordPageRespVO>()
                        .setList(Collections.emptyList()).setTotal(0L);
                return success(new BigContractBorrowRecordPageRespVO().setPageResult(pageResult));
            }
            pageVO.setUserList(userIdsByNameLike);
        }
        switch (approvePageFlagEnums) {
            case ALL:
                /**
                 * 档案借阅（记录/台账） result = -1 已归还
                 */
                return success(borrowBpmService.getBpmAllTaskPageArchive(WebFrameworkUtils.getLoginUserId(), pageVO));
            case DONE:
                return success(borrowBpmService.getBpmDoneTaskPageArchive(WebFrameworkUtils.getLoginUserId(), pageVO));
            case TO_DO:
                return success(borrowBpmService.getBpmToDoTaskPageArchive(WebFrameworkUtils.getLoginUserId(), pageVO));
            default:
                return success(new BigContractBorrowRecordPageRespVO());
        }
    }

    /**
     * 借阅台账 - 借阅类型统计
     */
    @GetMapping("/getBorrowTypeCount")
    @Operation(summary = "借阅台账 - 借阅类型统计")
    public CommonResult<Map<String,Long>> getBorrowTypeCount() {
        return success(borrowBpmService.getBorrowTypeCount());
    }
    /**
     * 借阅台账 - 借阅类型统计
     */
    @GetMapping("/getBorrowTypeCountV2")
    @Operation(summary = "借阅台账 - 借阅类型统计")
    public CommonResult<BorrowTypeCountRespVO> getBorrowTypeCountV2() {
        BorrowTypeCountRespVO result = borrowBpmService.getBorrowTypeCountV2();
        return success(result);
    }

    /**
     * 档案借阅 -归还
     */
    @PostMapping("/return")
    @Operation(summary = "档案借阅 -归还")
    public CommonResult<Boolean> returnArchive(@RequestBody BorrowReturnReqVO vo) {
        borrowBpmService.returnArchive(vo);
        return success(true);
    }

    /**
     * 获取合同借阅审批列表数据
     */
    @PostMapping("/page")
    @Operation(summary = "获取合同借阅审批列表数据")
    public CommonResult<PageResult<ContractBorrowBpmPageRespVO>> getApprovePage(@RequestBody ContractBorrowBpmPageReqVO reqVO) {
        return success(borrowBpmService.getBpmPage(reqVO));
    }

    /**
     * 借阅记录列表  新
     */
    @PostMapping("/listMyAllContractBorrowRecord")
    @Operation(summary = "借阅记录列表")
    public CommonResult<PageResult<BorrowRecordRespVO>> listMyAllContractBorrowRecord(@RequestBody ContractBorrowRecordPageReqVO reqVO) {
        return success(borrowBpmService.getBorrowRecordPage(reqVO));
    }

    /**
     * 借阅台账列表  新
     */
    @PostMapping("/borrowLedger")
    @Operation(summary = "借阅台账列表")
    public CommonResult<PageResult<BorrowRecordRespVO>> borrowLedger(@RequestBody ContractBorrowRecordPageReqVO reqVO) {
        return success(borrowBpmService.borrowLedger(reqVO));
    }

    /**
     * 借阅申请列表  新
     */
    @PostMapping("/list/borrow")
    @Operation(summary = "借阅申请列表")
    public CommonResult<PageResult<ContractBorrowBpmPageRespVO>> listBorrow(@RequestBody ContractBorrowBpmPageReqVO reqVO) {
        return success(borrowBpmService.getBorrowPage(reqVO));
    }

    /**
     * 借阅申请详情
     */
    @GetMapping("/detail/{id}")
    @Operation(summary = "借阅申请详情")
    public CommonResult<BorrowRespVO> detail(@PathVariable String id) {
        return success(borrowBpmService.getBorrowDetail(id));
    }
    @PostMapping("/borrowList")
    @Operation(summary = "借阅列表")
    public CommonResult<PageResult<ContractBorrowBpmPageRespVO>> borrowList(@RequestBody ContractBorrowBpmPageReqVO reqVO) {
        return success(borrowBpmService.getBorrowList(reqVO));
    }

}
