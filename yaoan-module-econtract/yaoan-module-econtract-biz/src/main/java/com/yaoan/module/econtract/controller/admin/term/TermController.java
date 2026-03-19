package com.yaoan.module.econtract.controller.admin.term;

import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.BatchSubmitReqVO;
import com.yaoan.module.econtract.controller.admin.term.vo.*;
import com.yaoan.module.econtract.controller.admin.term.vo.bpm.TermListApproveReqVO;
import com.yaoan.module.econtract.controller.admin.term.vo.tree.TermTreeReqVO;
import com.yaoan.module.econtract.controller.admin.term.vo.tree.TermTreeRespVO;
import com.yaoan.module.econtract.service.term.es.TermRepositoryService;
import com.yaoan.module.econtract.enums.payment.ApprovePageFlagEnums;
import com.yaoan.module.econtract.service.term.TermService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.framework.web.core.util.WebFrameworkUtils.getLoginUserId;

/**
 * 条款
 */
@Slf4j
@RestController
@RequestMapping("econtract/term")
@Tag(name = "条款", description = "条款")
public class TermController {

    @Resource
    private TermService termService;

    /**
     * 分页查询条款列表
     *
     * @param reqVO
     * @return
     */
    @PostMapping("/page")
    @Operation(summary = "分页查询条款列表")
    public CommonResult<PageResult<TermRespVO>> queryByPage(@Validated @RequestBody TermPageReqVO reqVO) {
        PageResult<TermRespVO> result = termService.queryByPage(reqVO);
        return success(result);
    }

    /**
     * 新增条款
     *
     * @param addVo
     * @return
     */
    @PostMapping("/save")
    @Operation(summary = "新增条款")
    public CommonResult<String> addTerm(@RequestBody @Validated TermAddVO addVo) {
        return success(termService.addTerm(addVo));
    }

    /**
     * 修改条款
     *
     * @param updateVo
     * @return
     */
    @PostMapping("/update")
    @Operation(summary = "修改条款")
    public CommonResult<Boolean> updateTerm(@RequestBody @Validated TermUpdateVO updateVo) {
        termService.updateTerm(updateVo);
        return success(true);
    }

    /**
     * 发布条款
     *
     * @param updateVo
     * @return
     */
    @PostMapping("/publish")
    @Operation(summary = "发布条款")
    public CommonResult<Boolean> savePublishTerm(@RequestBody @Validated TermPublishVO updateVo) {
        termService.publishTerm(updateVo);
        return success(true);
    }

    /**
     * 发布/启用条款
     *
     * @param id
     * @return
     */
    @GetMapping("/publish/{id}")
    @Operation(summary = "发布条款")
    public CommonResult<Boolean> publishTerm(@PathVariable("id") String id) {
        termService.publish(id);
        return success(true);
    }
    /**
     * 禁用条款
     *
     * @param id
     * @return
     */
    @GetMapping("/cancel/{id}")
    @Operation(summary = "停用条款")
    public CommonResult<Boolean> cancelTerm(@PathVariable("id") String id) {
        termService.cancelTerm(id);
        return success(true);
    }

    /**
     * 按照id删除条款
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    @Operation(summary = "按照id删除条款")
    public CommonResult<Boolean> removeById(@PathVariable("id") String id) {
        termService.removeTermById(id);
        return success(true);
    }

    /**
     * 模糊查询合同条款列表
     *
     * @param queryVo
     * @return
     */
    @PostMapping("/list")
    @Operation(summary = "模糊查询合同条款列表")
    public CommonResult<List<TermRespVO>> queryByConditions(@RequestBody TermQueryVO queryVo) {
        return success(termService.queryByConditions(queryVo));
    }

    /**
     * 根据条款制作模板时展示列表
     *
     * @param queryVo
     * @return
     */
    @PostMapping("/list/v2")
    @Operation(summary = "根据条款制作模板时展示列表")
    public CommonResult<Map<String, List<TermListRespVO>>> queryAllTerms(@RequestBody TermQueryVO queryVo) {
        return success(termService.queryAllTerms(queryVo));
    }

    /**
     * 按照id查询条款详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "按照id查询条款详情")
    public CommonResult<TermRespVO> queryTermById(@PathVariable("id") String id) {
        return success(termService.getTermById(id));
    }

    /**
     * 查询分类和对应的条款
     */
    @PostMapping(value = "/listTermAndCategory")
    @Operation(summary = "查询分类和对应的条款", description = "查询分类和对应的条款")
    @OperateLog(logArgs = false)
    public CommonResult<List<TermTreeRespVO>> listTermAndCategory(@RequestBody TermTreeReqVO vo) {
        List<TermTreeRespVO> trees = termService.listTermAndCategory(vo);
        return success(trees);
    }

    /**
     * 条款申请 发起审批请求 接口
     */
    @PostMapping("/submitApproveTerm")
    @Operation(summary = "条款申请 发起审批请求 接口")
    @OperateLog(logArgs = false)
    public CommonResult<String> submitApproveTerm(@RequestBody TermAddVO vo) throws Exception {
        return success(termService.submitApproveTerm(vo));
    }


    /**
     * 获取任务分页（0=全部 1=已审批 2=未审批）
     */
    @PostMapping("getAutoBpmAllTaskPage")
    @Operation(summary = "获取任务分页（0=全部 1=已审批 2=未审批）")
    public CommonResult<TermBigListApproveRespVO> getAutoBpmAllTaskPage(@Valid @RequestBody TermListApproveReqVO pageVO) {
        int flag = pageVO.getFlag();
        String loginId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        pageVO.setApplicantId(loginId);
        ApprovePageFlagEnums approvePageFlagEnums = ApprovePageFlagEnums.getInstance(flag);
        switch (Objects.requireNonNull(approvePageFlagEnums)) {
            case ALL:
                return success(termService.getBpmAllTaskPage(getLoginUserId(), pageVO));
            case DONE:
                return success(termService.getBpmDoneTaskPage(getLoginUserId(), pageVO));
            case TO_DO:
                return success(termService.getBpmToDoTaskPage(getLoginUserId(), pageVO));
            default:
                return success(new TermBigListApproveRespVO());
        }
    }

    /**
     * 条款库列表/条款库
     */
    @PostMapping("/ListAutoTerm")
    @Operation(summary = "条款库列表/条款库")
    public CommonResult<TermBigRespVO> listAutoTerm(@RequestBody @Validated TermListApproveReqVO reqVO) {
        return success(termService.listAutoTerm(reqVO));
    }


    /**
     * 条款申请 批量发起审批请求 接口
     */
    @PostMapping("/submitApproveTermBatch")
    @Operation(summary = "条款申请 批量发起审批请求 接口")
    @OperateLog(logArgs = false)
    public CommonResult<String> submitApproveTermBatch(@RequestBody BatchSubmitReqVO vo) throws Exception {
        return success(termService.submitApproveTermBatch(vo));
    }

    @Resource
    private TermRepositoryService termRepositoryService;

    /**
     * 条款初始化ES
     * */
    @GetMapping("/init4es")
    public void init4es() {
        termRepositoryService.init();
    }

    /**
     * 条款批量导入ES
     * */
    @GetMapping("/addTerm4es")
    public void addTerm4es() {
        termRepositoryService.saveAll();
    }

    /**
     * 分页ES
     * */
    @PostMapping("/pageTerm4es")
    public CommonResult<PageResult<TermSimpleRespVO>> pageTerm4es(@RequestBody TermPageReqVO vo) throws IOException {
        return success(termRepositoryService.pageTerm4es3(vo));
    }

}
