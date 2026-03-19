package com.yaoan.module.econtract.service.term;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.BatchSubmitReqVO;
import com.yaoan.module.econtract.controller.admin.term.vo.*;
import com.yaoan.module.econtract.controller.admin.term.vo.bpm.TermBpmReqVO;
import com.yaoan.module.econtract.controller.admin.term.vo.bpm.TermListApproveReqVO;
import com.yaoan.module.econtract.controller.admin.term.vo.bpm.TermListApproveRespVO;
import com.yaoan.module.econtract.controller.admin.term.vo.tree.TermTreeReqVO;
import com.yaoan.module.econtract.controller.admin.term.vo.tree.TermTreeRespVO;
import com.yaoan.module.econtract.dal.dataobject.term.Term;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 合同 服务类
 * </p>
 *
 * @author doujiale
 * @since 2023-09-18
 */
public interface TermService extends IService<Term> {

    PageResult<TermRespVO> queryByPage(TermPageReqVO reqVO);

    /**
     * 新增条款
     */
    String addTerm(TermAddVO addVo);

    void updateTerm(TermUpdateVO updateVo);

    void publishTerm(TermPublishVO updateVo);

    void removeTermById(String id);

    List<TermRespVO> queryByConditions(TermQueryVO queryVo);

    TermRespVO getTermById(String id);

    void publish(String id);
    void cancelTerm(String id);

    Map<String, List<TermListRespVO>> queryAllTerms(TermQueryVO queryVo);

    List<TermTreeRespVO> listTermAndCategory(TermTreeReqVO vo);

    String submitApproveTerm(TermAddVO vo);

    TermBigListApproveRespVO getBpmAllTaskPage(Long loginUserId, TermListApproveReqVO pageVO);

    TermBigListApproveRespVO getBpmDoneTaskPage(Long loginUserId, TermListApproveReqVO pageVO);

    TermBigListApproveRespVO getBpmToDoTaskPage(Long loginUserId, TermListApproveReqVO pageVO);

    TermBigRespVO listAutoTerm(TermListApproveReqVO reqVO);

    String submitApproveTermBatch(BatchSubmitReqVO vo);

}
