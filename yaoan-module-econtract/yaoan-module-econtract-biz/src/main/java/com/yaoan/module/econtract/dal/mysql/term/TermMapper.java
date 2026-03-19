package com.yaoan.module.econtract.dal.mysql.term;

import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.term.vo.TermPageReqVO;
import com.yaoan.module.econtract.controller.admin.term.vo.bpm.TermListApproveReqVO;
import com.yaoan.module.econtract.controller.admin.term.vo.tree.TermTreeReqVO;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.ContractTemplateTermRelDO;
import com.yaoan.module.econtract.dal.dataobject.term.ModelTerm;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import com.yaoan.module.econtract.enums.term.TermLibraryEnums;
import com.yaoan.module.econtract.enums.term.TermStatusEnums;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUser;
import static com.yaoan.module.econtract.enums.StatusConstants.STATUS_IS_ACTIVE;

/**
 * <p>
 * 合同 Mapper 接口
 * </p>
 *
 * @author doujiale
 * @since 2023-09-18
 */
@Mapper
public interface TermMapper extends BaseMapperX<Term> {

    static final Integer TERM_MAKE = 0;
    static final Integer TERM_STORE = 1;

    default PageResult<Term> selectPage(TermPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<Term>().orderByDesc(Term::getUpdateTime).likeIfPresent(Term::getCode, reqVO.getCode()).likeIfPresent(Term::getName, reqVO.getName()).eqIfPresent(Term::getTermType, reqVO.getTermType()).eqIfPresent(Term::getStatus, reqVO.getStatus()).eqIfPresent(Term::getCategoryId, reqVO.getCategoryId()));
    }

    /**
     * 条款树状结构
     */
    default List<Term> listTermAndCategory(TermTreeReqVO vo) {
        return selectList(new LambdaQueryWrapperX<Term>()
                //必须启用的
                .eq(Term::getStatus, String.valueOf(STATUS_IS_ACTIVE))
                .eqIfPresent(Term::getCategoryId, vo.getCategoryId())
                .eqIfPresent(Term::getTermType, vo.getTermType())
                .likeIfPresent(Term::getName, vo.getName()));
    }

    /**
     * 条款审批所用（审批人查看）
     */
    default PageResult<Term> selectApprovePage(TermListApproveReqVO pageVO) {
        return selectPage(pageVO, new LambdaQueryWrapperX<Term>()
                        .inIfPresent(Term::getProcessInstanceId, pageVO.getInstanceIdList())
                        //自己发起的申请不可出现在自己负责的审批列表，被退回的审批任务只能在申请列表展示并处理。
//                .neIfPresent(Term::getCreator, pageVO.getApplicantId())
                        .orderByDesc(Term::getUpdateTime)
                        .betweenIfPresent(Term::getCreateTime, pageVO.getCreateTime0(), pageVO.getCreateTime1())
                        .betweenIfPresent(Term::getApplyTime, pageVO.getApproveTime0(), pageVO.getApproveTime1())
                        .eqIfPresent(Term::getTermType, pageVO.getTermType()).eqIfPresent(Term::getContractType, pageVO.getContractType())
                        .likeIfPresent(Term::getName, pageVO.getName())
                        .likeIfPresent(Term::getCode, pageVO.getCode())
                        .inIfPresent(Term::getCategoryId,  pageVO.getCategoryIds())
                        .likeIfPresent(Term::getContractType, pageVO.getContractType())
                        .eqIfPresent(TenantBaseDO::getTenantId, getLoginUser().getTenantId())
        );
    }

    /**
     * 条款库列表/条款库
     * flag =0 ：条款制作（非通过）：
     * frontCode：{@link CommonFlowableReqVOResultStatusEnums}
     * ALL, TO_SEND, SUCCESS
     * flag =1：条款库（审批通过）
     */
    default PageResult<Term> listApprovedTerm(TermListApproveReqVO reqVO, List<Long> userIdList) {
        Integer flag = reqVO.getFlag();

        if (2 == flag) {
            MPJLambdaWrapper<Term> mpjLambdaWrapper = new MPJLambdaWrapper<Term>()
                    .selectAll(Term.class).orderByDesc(Term::getCreateTime);
            // 根据模板id找到条款
            mpjLambdaWrapper.leftJoin(ModelTerm.class, ModelTerm::getTermId, Term::getId)
                    .like(ModelTerm::getModelId, reqVO.getModelId())
                    .eq(Term::getResult, BpmProcessInstanceResultEnum.APPROVE.getResult())
            ;

            return selectPage(reqVO, mpjLambdaWrapper);
        }

        LambdaQueryWrapperX<Term> wrapperX = new LambdaQueryWrapperX<Term>()
                .orderByDesc(Term::getUpdateTime)
                .likeIfPresent(Term::getName, reqVO.getName())
                .eqIfPresent(Term::getTermType, reqVO.getTermType())
                .eqIfPresent(Term::getContractType, reqVO.getContractType())
                .betweenIfPresent(Term::getCreateTime, reqVO.getCreateTime0(), reqVO.getCreateTime1())
                .betweenIfPresent(Term::getApproveTime, reqVO.getApproveTime0(), reqVO.getApproveTime1())
                .inIfPresent(Term::getCategoryId, reqVO.getCategoryIds())
                .likeIfPresent(Term::getCode, reqVO.getCode())
                .eqIfPresent(Term::getStatus, reqVO.getStatus())
                .eqIfPresent(Term::getResult, reqVO.getResult())
                ;
        if (TermLibraryEnums.AGENCY.getCode().equals(reqVO.getTermLibrary())){
            wrapperX.eq(Term::getTenantId, getLoginUser().getTenantId())
                    .eq(Term::getTermLibrary, TermLibraryEnums.AGENCY.getCode());
        } else if (TermLibraryEnums.COMMON.getCode().equals(reqVO.getTermLibrary())){
            wrapperX.eq(Term::getTenantId, 0L).
                    eq(Term::getTermLibrary, TermLibraryEnums.COMMON.getCode());
        } else {
            wrapperX.in(Term::getTenantId, Arrays.asList(0L, getLoginUser().getTenantId()));
        }
        // 创建者姓名
        if (StringUtils.isNotBlank(reqVO.getCreatorName())) {
            List<String> stringUserIdList = userIdList.stream().map(String::valueOf).collect(Collectors.toList());
            wrapperX.inIfPresent(Term::getCreator, stringUserIdList);
        }

        //条款制作
        if (TERM_MAKE.equals(flag)) {
            if (StringUtils.isNotEmpty(reqVO.getFrontCode())) {
                CommonFlowableReqVOResultStatusEnums enums = CommonFlowableReqVOResultStatusEnums.getFrontInstance(reqVO.getFrontCode());
                if (ObjectUtil.isNotNull(enums)) {
                    switch (enums) {
                        case TO_SEND:
                            wrapperX.and(w -> w
                                    .or()
                                    .eq(Term::getResult, CommonFlowableReqVOResultStatusEnums.TO_SEND.getResultCode())
                                    .or()
                                    .eq(Term::getResult, CommonFlowableReqVOResultStatusEnums.REJECTED.getResultCode()));

                            break;
                        default:
                            wrapperX.eq(Term::getResult, enums.getResultCode());
                            break;
                    }
                }
            }
        }

        //条款库
        if (TERM_STORE.equals(reqVO.getFlag())) {
            wrapperX.eq(Term::getResult, BpmProcessInstanceResultEnum.APPROVE.getResult());
        }
        return selectPage(reqVO, wrapperX);
    }

    default List<Term> selectTermsByTemplateId(String templateId) {
        MPJLambdaWrapper<Term> mpjLambdaWrapper = new MPJLambdaWrapper<Term>()
                .selectAll(Term.class).orderByDesc(Term::getCreateTime);
        // 根据模板id找到条款
        mpjLambdaWrapper.leftJoin(ContractTemplateTermRelDO.class, ContractTemplateTermRelDO::getTermId, Term::getId)
                .eq(ContractTemplateTermRelDO::getTemplateId, templateId)
        ;

        return selectList(mpjLambdaWrapper);

    }

    /**
     * 根据参考模板id找到相关条款
     */
    default List<Term> getTermsByTemplateId(String templateId) {
        MPJLambdaWrapper<Term> mpjLambdaWrapper = new MPJLambdaWrapper<Term>()
                .selectAll(Term.class).orderByDesc(Term::getCreateTime);
        mpjLambdaWrapper.leftJoin(ContractTemplateTermRelDO.class, ContractTemplateTermRelDO::getTermId, Term::getId)
                .like(ContractTemplateTermRelDO::getTemplateId, templateId);

        return selectList(mpjLambdaWrapper);
    }

    /**
     * 根据模板找到条款
     */
    default List<Term> selectListByModelId(String id) {
        MPJLambdaWrapper<Term> mpjLambdaWrapper = new MPJLambdaWrapper<Term>()
                .selectAll(Term.class).orderByDesc(Term::getCreateTime);
        mpjLambdaWrapper.leftJoin(ModelTerm.class, ModelTerm::getTermId, Term::getId)
                .like(ModelTerm::getModelId, id);
        return selectList(mpjLambdaWrapper);
    }
}
