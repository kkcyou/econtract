package com.yaoan.module.econtract.controller.admin.relative;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.param.vo.*;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.BigPaymentApplicationListBpmRespVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.bpm.PaymentApplicationListBpmReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.listPaymentApplication.PaymentApplicationListReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.listPaymentApplication.PaymentApplicationListRespVO;
import com.yaoan.module.econtract.controller.admin.relative.blacklistVo.BlacklistApplyReqVO;
import com.yaoan.module.econtract.controller.admin.relative.blacklistVo.BlacklistHandleReqVO;
import com.yaoan.module.econtract.controller.admin.relative.blacklistVo.BlacklistPageReqVO;
import com.yaoan.module.econtract.controller.admin.relative.blacklistVo.BlacklistRespVO;
import com.yaoan.module.econtract.controller.admin.relative.vo.*;
import com.yaoan.module.econtract.enums.payment.ApprovePageFlagEnums;
import com.yaoan.module.econtract.service.relative.RelativeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author doujl
 * @since 2023-07-24
 */
@Slf4j
@RestController
@RequestMapping("econtract/relative")
@Tag(name = "相对方", description = "相对方")
public class RelativeController {
    @Autowired
    private RelativeService relativeService;

    @PutMapping(value = "/create")
    @Operation(summary = "新增相对方", description = "新增相对方")
    @OperateLog(logArgs = false)
    public CommonResult<String> createRelative(@RequestBody RelativeCreateReqVO relatives) {
        String id = relativeService.saveRelative(relatives);
        return success(id);
    }

    @DeleteMapping(value = "/delete/ids")
    @Operation(summary = "删除相对方", description = "删除相对方")
    @OperateLog(logArgs = false)
    public CommonResult<Boolean> deleteRelative(@RequestBody ReqIdsVO reqIdsVO) {
        relativeService.removeByIds(reqIdsVO);
        return success(true);
    }

    @PostMapping(value = "/update")
    @Operation(summary = "修改相对方", description = "修改相对方")
    @OperateLog(logArgs = false)
    public CommonResult<Boolean> updateRelativeById(@RequestBody RelativeUpdateReqVO relativeUpdateReqVO) throws Exception {
        relativeService.updateById(relativeUpdateReqVO);
        return success(true);
    }

    @GetMapping(value = "/query/{id}")
    @Operation(summary = "查看相对方信息", description = "查看相对方信息")
    @OperateLog(logArgs = false)
    public CommonResult<RelativeRespVO> queryRelativeById(@PathVariable String id) {
        RelativeRespVO relativeRespVO = relativeService.queryRelativeById(id);
        return success(relativeRespVO);
    }

    @PostMapping(value = "/queryAllByContact")
    @Operation(summary = "查询已添加联系人且已激活的信息", description = "查询已添加联系人且已激活的信息")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<RelativeListRespVO>> queryAllByContact(@RequestBody RelativePageReqVO relativePageReqVO) {
        PageResult<RelativeListRespVO> relativeRespVOs = relativeService.queryAllByContact(relativePageReqVO);
        return success(relativeRespVOs);
    }

    @PostMapping(value = "/page/list")
    @Operation(summary = "查询所有相对方", description = "查询所有相对方")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<RelativeListRespVO>> queryAllRelative(@RequestBody RelativePageReqVO relativePageReqVO) {
        PageResult<RelativeListRespVO> RelativePage = relativeService.queryAllRelative(relativePageReqVO);
        return success(RelativePage);
    }
//不需要,需要改查用户+部门+企业表
    @GetMapping(value = "/page/queryRelativeByUser")
    @Operation(summary = "根据登录用户查询当前发送方信息", description = "根据登录用户查询当前发送方信息")
    @OperateLog(logArgs = false)
    public CommonResult<RelativeByUserRespVO> queryRelativeByLoginUser() {
        RelativeByUserRespVO relativeRespVO = relativeService.queryRelativeByLoginUser();
        return success(relativeRespVO);
    }

    @PutMapping(value = "/contact/create")
    @Operation(summary = "新增联系人", description = "新增联系人")
    @OperateLog(logArgs = false)
    public CommonResult<Boolean> saveContact(@RequestBody ContactReqVO contactReqVO) {
        relativeService.saveContact(contactReqVO);
        return success(true);
    }

    @DeleteMapping(value = "/contact/delete/{id}")
    @Operation(summary = "删除联系人", description = "删除联系人")
    @OperateLog(logArgs = false)
    public CommonResult<Boolean> deleteContact(@PathVariable String id) {
        relativeService.deleteContact(id);
        return success(true);
    }

    @PostMapping(value = "/blacklist/create")
    @Operation(summary = "相对方加入黑名单", description = "相对方加入黑名单")
    @OperateLog(logArgs = false)
    public CommonResult<String> createBlacklistApply(@RequestBody BlacklistApplyReqVO blacklistApplyReqVO) {
        String id  = relativeService.saveRelativeBlacklist(blacklistApplyReqVO);
        return success(id);
    }
    
    @PostMapping(value = "/page/blacklist")
    @Operation(summary = "查询相对方黑名单", description = "查询相对方黑名单")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<BlacklistRespVO>> getBlacklistpage(@RequestBody BlacklistPageReqVO blacklistPageReqVO) {
        PageResult<BlacklistRespVO> page = relativeService.getRelativeBlacklist(blacklistPageReqVO);
        return success(page);
    }

    @PostMapping(value = "/blacklist/audit")
    @Operation(summary = "审批黑名单申请", description = "审批黑名单申请")
    @OperateLog(logArgs = false)
    public CommonResult<String> auditBlacklistApply(@RequestBody BlacklistHandleReqVO blacklistApplyReqVO) {
        String id  = relativeService.auditRelativeBlacklistApply(blacklistApplyReqVO);
        return success(id);
    }

    @PostMapping(value = "/blacklist/reject")
    @Operation(summary = "驳回黑名单申请", description = "驳回黑名单申请")
    @OperateLog(logArgs = false)
    public CommonResult<String> rejectBlacklistApply(@RequestBody BlacklistHandleReqVO blacklistApplyReqVO) {
        String id  = relativeService.rejectRelativeBlacklistApply(blacklistApplyReqVO);
        return success(id);
    }

    @GetMapping(value = "/blacklist/query/{id}")
    @Operation(summary = "查看相对方黑名单信息", description = "查看相对方黑名单信息")
    @OperateLog(logArgs = false)
    public CommonResult<BlacklistRespVO> queryRelativeBlacklistById(@PathVariable String id) {
        BlacklistRespVO blacklistRespVO = relativeService.queryRelativeBlacklistById(id);
        return success(blacklistRespVO);
    }

    /**
     * 发起相对方申请
     * */
    @PostMapping("/submit")
    @Operation(summary = "发起相对方申请 接口")
    @OperateLog(logArgs = false)
    public CommonResult<String> submit(@RequestBody RelativeSubReqVO vo) throws Exception {
        return success(relativeService.submit(vo));
    }

    /**
     * 相对方-"我提交的相对方申请"列表 接口
     */
    @PostMapping("/listRelativeApplication")
    @Operation(summary = "相对方-\"我提交的相对方申请\"列表  接口")
    @OperateLog(logArgs = false)
    public CommonResult<PageResult<RelativeBpmListBpmRespVO>> listApplication(@RequestBody RelativeBpmPageReqVO vo) throws Exception {
        return success(relativeService.listApplication(vo));
    }

    /**
     * 相对方审批列表
     * */
    @PostMapping("/getAutoBpmAllTaskPage")
    @Operation(summary = "获取任务分页（0=全部 1=已审批 2=未审批）")
    public CommonResult<BigRelativeBpmRespVO> getAutoBpmAllTaskPage(@Valid @RequestBody RelativeBpmPageReqVO pageVO) {
        int flag = pageVO.getFlag();
        String loginId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        pageVO.setApplicantId(loginId);
        ApprovePageFlagEnums approvePageFlagEnums = ApprovePageFlagEnums.getInstance(flag);
        switch (approvePageFlagEnums) {
            case ALL:
                return success(relativeService.getBpmAllTaskPage(getLoginUserId(), pageVO));
            case DONE:
                return success(relativeService.getBpmDoneTaskPage(getLoginUserId(), pageVO));
            case TO_DO:
                return success(relativeService.getBpmToDoTaskPage(getLoginUserId(), pageVO));
            default:
                return success(new BigRelativeBpmRespVO());
        }
    }


}
