package com.yaoan.module.econtract.dal.mysql.contract;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.*;
import com.yaoan.module.econtract.controller.admin.ledger.vo.LedgerPageReqV2VO;
import com.yaoan.module.econtract.controller.admin.outward.contract.VO.ApiPageReqVO;
import com.yaoan.module.econtract.controller.admin.performance.v2.vo.ContractPerformV2ReqVO;
import com.yaoan.module.econtract.controller.admin.workbench.v2.vo.ContractDataReqVO;
import com.yaoan.module.econtract.controller.admin.workbench.v2.vo.ContractPerformReqVO;
import com.yaoan.module.econtract.controller.admin.workbench.vo.task.WorkBenchTaskReqVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.archive.BpmContractArchivesDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangeDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.contract.BpmContract;
import com.yaoan.module.econtract.dal.dataobject.bpm.contractborrow.ContractBorrowBpmDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractSealDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import com.yaoan.module.econtract.dal.dataobject.contractInvoiceManage.ContractInvoiceManageDO;
import com.yaoan.module.econtract.dal.dataobject.contractarchives.ContractArchivesDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.DraftOrderInfoDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplicationDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.deferred.PaymentDeferredApplyDO;
import com.yaoan.module.econtract.dal.dataobject.signet.BpmContractSignetDO;
import com.yaoan.module.econtract.enums.AmountTypeEnums;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.enums.ContractUploadTypeEnums;
import com.yaoan.module.econtract.enums.company.CompanyEnums;
import com.yaoan.module.econtract.enums.payment.CollectionTypeEnums;
import com.yaoan.module.system.api.dept.dto.UserCompanyDeptRespDTO;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.joda.time.LocalDate;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.yaoan.module.econtract.service.workbench.WorkBenchServiceImpl.*;

@Mapper
public interface ContractMapper extends BaseMapperX<ContractDO> {


    /**
     * 新增关联关系展示所有合同列表信息
     *
     * @param contractRepVO
     * @return
     */
    default List<ContractDO> selectList(ContractRepVO contractRepVO) {
        LambdaQueryWrapperX<ContractDO> wrapperX = new LambdaQueryWrapperX<ContractDO>();

        if (StringUtils.isNotEmpty(contractRepVO.getSearchText())) {
            wrapperX.and(wrapper -> wrapper
                    .or().like(ContractDO::getName, contractRepVO.getSearchText())
                    .or().like(ContractDO::getCode, contractRepVO.getSearchText())
            );
        }
        wrapperX.eq(ContractDO::getStatus, ContractStatusEnums.SIGN_COMPLETED.getCode());
        wrapperX.select(ContractDO.class, info ->
                !info.getColumn().equals("contract_content"));
        return selectList(wrapperX);
    }

    default PageResult<ContractDO> selectApiPage(ApiPageReqVO apiPageReqVO) {
        LambdaQueryWrapperX<ContractDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.eq(ContractDO::getUpload, ContractUploadTypeEnums.THIRD_PARTY.getCode())
                .likeIfPresent(ContractDO::getName, apiPageReqVO.getContractName())
                .likeIfPresent(ContractDO::getCode, apiPageReqVO.getContractCode())
                .eqIfPresent(ContractDO::getStatus, apiPageReqVO.getStatus())
                .betweenIfPresent(ContractDO::getValidity0, apiPageReqVO.getEffectDateStart(), apiPageReqVO.getEffectDateEnd())
                .betweenIfPresent(ContractDO::getValidity1, apiPageReqVO.getExpiryDateStart(), apiPageReqVO.getExpiryDateEnd());
        return selectPage(apiPageReqVO, wrapperX);
    }

    /**
     * 登录用户作为-发起人- 合同状态为签署完成 当前时间超过合同生效时间
     */
    default PageResult<ContractDO> selectPaymentSchedule(PaymentSchedulePageReqVO paymentSchedulePageReqVO) {
        MPJQueryWrapper<ContractDO> mpjQueryWrapper = new MPJQueryWrapper<ContractDO>()
                .select(ContractDO.class, info ->
                        !info.getColumn().equals("contract_content"));
//                .leftJoin("ecms_signatory_rel s on s.contract_id=t.id")
//                .leftJoin("ecms_relative r on r.id = s.signatory_id");
        mpjQueryWrapper.lambda().orderByDesc(ContractDO::getUpdateTime)
                //排除富文本字段
                .select(ContractDO.class, info ->
                        !"contract_content".equals(info.getColumn())).select("t.id");
        //合同状态为签署完成
        mpjQueryWrapper.eq("t.status", ContractStatusEnums.SIGN_COMPLETED.getCode());
        //当前时间大于合同生效开始时间
        if (ObjectUtil.isNotEmpty(paymentSchedulePageReqVO.getCurrentDate())) {
            mpjQueryWrapper.lt("t.validity0", paymentSchedulePageReqVO.getCurrentDate());
        }
        //当前时间小于合同生效结束时间
        if (ObjectUtil.isNotEmpty(paymentSchedulePageReqVO.getCurrentDate())) {
            mpjQueryWrapper.gt("t.validity1", paymentSchedulePageReqVO.getCurrentDate());
        }
        //合同编码
        if (ObjectUtil.isNotEmpty(paymentSchedulePageReqVO.getCode())) {
            mpjQueryWrapper.like("t.code", paymentSchedulePageReqVO.getCode());
        }
        //合同名称
        if (ObjectUtil.isNotEmpty(paymentSchedulePageReqVO.getName())) {
            mpjQueryWrapper.like("t.name", paymentSchedulePageReqVO.getName());
        }
        //合同类型
        if (ObjectUtil.isNotEmpty(paymentSchedulePageReqVO.getContractType())) {
            mpjQueryWrapper.eq("t.contract_type", paymentSchedulePageReqVO.getContractType());
        }
        if (StringUtils.isNotEmpty(paymentSchedulePageReqVO.getPayeeName())) {
            mpjQueryWrapper.lambda().like(ContractDO::getPartBName, paymentSchedulePageReqVO.getPayeeName());
        }
        return selectPage(paymentSchedulePageReqVO, mpjQueryWrapper);
    }

    /**
     * 登录用户作为-发起人- 合同状态为签署完成 当前时间超过合同生效时间
     */
    default PageResult<ContractDO> selectRegisterPage(ContractPageReqVO contractPageReqVO) {
        MPJQueryWrapper<ContractDO> mpjQueryWrapper = new MPJQueryWrapper<ContractDO>()
                .select(ContractDO.class, info ->
                        !info.getColumn().equals("contract_content"))
//                .leftJoin("ecms_signatory_rel s on s.contract_id=t.id AND s.deleted = 0")
//                .leftJoin("ecms_relative r on r.id = s.signatory_id AND r.deleted = 0")
                .leftJoin("ecms_bpm_contract_register b on b.contract_id=t.id AND b.deleted = 0");
        mpjQueryWrapper.lambda().orderByDesc(ContractDO::getUpdateTime)
                //排除富文本字段
                .select(ContractDO.class, info ->
                        !"contract_content".equals(info.getColumn())).select("t.id");
        mpjQueryWrapper.eq("t.upload", ContractUploadTypeEnums.REGISTER.getCode());
        //查询借阅审批表申请状态
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getStatusList())) {
            mpjQueryWrapper.and(qw ->
                    qw.in("b.result", contractPageReqVO.getStatusList())
                            .or()
                            .eq("t.status", ContractStatusEnums.TO_BE_CHECK.getCode())
            );
        } else {
            if (ObjectUtil.isNotEmpty(contractPageReqVO.getStatus())) {
                if (contractPageReqVO.getStatus() == 1) {
                    mpjQueryWrapper.eq("b.result", contractPageReqVO.getStatus());
                } else if (contractPageReqVO.getStatus().equals(ContractStatusEnums.SIGN_COMPLETED.getCode())) {
                    mpjQueryWrapper.eq("t.status", ContractStatusEnums.SIGN_COMPLETED.getCode());
                }
            }
        }
        //签署类型
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getSignType())) {
            mpjQueryWrapper.eq("t.sign_type", contractPageReqVO.getSignType());
        }
        //合同金额范围
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getAmount0()) && ObjectUtil.isNotEmpty(contractPageReqVO.getAmount1())) {
            mpjQueryWrapper.between("t.amount", contractPageReqVO.getAmount0(), contractPageReqVO.getAmount1());
        }
        //签署日期范围
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getSignDate0()) && ObjectUtil.isNotEmpty(contractPageReqVO.getSignDate1())) {
            mpjQueryWrapper.between("t.sign_date", contractPageReqVO.getSignDate0(), contractPageReqVO.getSignDate1());
        }
        //补录时间范围
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getSupplementDate0()) && ObjectUtil.isNotEmpty(contractPageReqVO.getSupplementDate1())) {
            mpjQueryWrapper.between("t.create_time", contractPageReqVO.getSupplementDate0(), contractPageReqVO.getSupplementDate1());
        }
        //合同编码
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getCode())) {
            mpjQueryWrapper.like("t.code", contractPageReqVO.getCode());
        }
        //合同名称
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getName())) {
            mpjQueryWrapper.like("t.name", contractPageReqVO.getName());
        }
        //合同类型
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getContractType())) {
            mpjQueryWrapper.eq("t.contract_type", contractPageReqVO.getContractType());
        }
        if (StringUtils.isNotEmpty(contractPageReqVO.getRelativeName())) {
            mpjQueryWrapper.lambda().like(ContractDO::getPartBName, contractPageReqVO.getRelativeName());
        }
        return selectPage(contractPageReqVO, mpjQueryWrapper);
    }

    /**
     * 登录用户作为  -发起人-
     * <p>
     * ------草稿箱-------
     * 合同状态为  -待送审 11 | 审核不通过 13-
     * <p>
     * ------审核中-------
     * 合同状态为  -审核中- 12
     * <p>
     * ------待发送-------
     * 合同状态为  -待发送 0|被退回 1-
     * <p>
     * ------已发送-------
     * 合同状态为   -已发送- 2
     * <p>
     * ------待确认-------
     * 合同状态为   -待确认- 3
     *
     * @param contractPageReqVO
     * @return
     */
    default PageResult<ContractDO> selectSentPage(ContractPageReqVO contractPageReqVO) {
        MPJQueryWrapper<ContractDO> mpjQueryWrapper = new MPJQueryWrapper<ContractDO>()
                .select(ContractDO.class, info ->
                        !"contract_content".equals(info.getColumn()));
//                .leftJoin("ecms_signatory_rel s on s.contract_id=t.id AND s.deleted = 0")
//                .leftJoin("ecms_relative r on r.id = s.signatory_id AND r.deleted = 0");
//        if (ObjectUtil.isNotEmpty(contractPageReqVO.getIdentifier()) && contractPageReqVO.getIdentifier() == 0) {
//            //待发送列表不查upload=3或4的
//            List<Integer> uploadTypeList = new ArrayList<>();
//            uploadTypeList.add(ContractUploadTypeEnums.ORDER_DRAFT.getCode());
//            uploadTypeList.add(ContractUploadTypeEnums.THIRD_PARTY.getCode());
//            mpjQueryWrapper.lambda().notIn(ContractDO::getUpload, uploadTypeList);
//        }
        if (Integer.valueOf(0).equals(contractPageReqVO.getIdentifier())){
            mpjQueryWrapper.lambda().orderByDesc(ContractDO::getCreateTime);
        } else {
            mpjQueryWrapper.lambda().orderByDesc(ContractDO::getUpdateTime);
        }
        mpjQueryWrapper.lambda()
//        mpjQueryWrapper.lambda().orderByDesc(ContractDO::getUpdateTime)
                //排除富文本字段
                .select(ContractDO.class, info ->
                        !"contract_content".equals(info.getColumn())).select("t.id");
        ArrayList<Integer> list = new ArrayList<>();
        if (contractPageReqVO.getUploadList() != null && contractPageReqVO.getUploadList().size() > 0) {
            list = (ArrayList<Integer>) contractPageReqVO.getUploadList();
        } else {
            if (contractPageReqVO.getUpload() != null) {
                list.add(contractPageReqVO.getUpload());
            } else {
                list.add(ContractUploadTypeEnums.MODEL_DRAFT.getCode());
                list.add(ContractUploadTypeEnums.UPLOAD_FILE.getCode());

                list.add(ContractUploadTypeEnums.UPLOAD_CONTRACT_FILE.getCode());
                if (!contractPageReqVO.getStatusList().contains(ContractStatusEnums.TO_BE_CHECK.getCode())) {
                    //草稿箱不查第三方合同，其他地方查询
                    list.add(ContractUploadTypeEnums.THIRD_PARTY.getCode());
                    list.add(ContractUploadTypeEnums.ORDER_DRAFT.getCode());
                }
                //list.add(ContractUploadTypeEnums.THIRD_PARTY.getCode());
            }
        }

        mpjQueryWrapper.in("t.upload", list);

        if (ObjectUtil.isNotEmpty(contractPageReqVO.getContractCategory())) {
            mpjQueryWrapper.like("t.contract_category", contractPageReqVO.getContractCategory());
        }
        if (StringUtils.isNotEmpty(contractPageReqVO.getCode())) {
            mpjQueryWrapper.like("t.code", contractPageReqVO.getCode());
        }
        if (StringUtils.isNotEmpty(contractPageReqVO.getName())) {
            mpjQueryWrapper.like("t.name", contractPageReqVO.getName());
        }
        if (StringUtils.isNotEmpty(contractPageReqVO.getQueryKey())) {
            mpjQueryWrapper.and(it->{
                it.like("t.name",contractPageReqVO.getQueryKey()).or()
                        .like("t.code",contractPageReqVO.getQueryKey()).or()
                        .like("t.part_b_name",contractPageReqVO.getQueryKey()).or()
                        .like("t.part_a_name",contractPageReqVO.getQueryKey())
                        .or().like("t.amount", contractPageReqVO.getQueryKey());
            });
        }
        if (StringUtils.isNotEmpty(contractPageReqVO.getPartyBName())) {
            mpjQueryWrapper.like("t.part_b_name", contractPageReqVO.getPartyBName());
        }
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getExpirationDate0()) && ObjectUtil.isNotEmpty(contractPageReqVO.getExpirationDate1())) {
            mpjQueryWrapper.between("t.expiration_date", contractPageReqVO.getExpirationDate0().toDateStr(), contractPageReqVO.getExpirationDate1().toDateStr());
        }
        if (StringUtils.isNotEmpty(contractPageReqVO.getContractType())) {
            mpjQueryWrapper.like("t.contract_type", contractPageReqVO.getContractType());
        }
        //查询字段
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getStatus())) {
            mpjQueryWrapper.eq("t.status", contractPageReqVO.getStatus());
        }
        //默认查询合同状态字段
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getStatusList())) {
            mpjQueryWrapper.in("t.status", contractPageReqVO.getStatusList());
        }
        if (StringUtils.isNotEmpty(contractPageReqVO.getRelativeName())) {
            mpjQueryWrapper.lambda().like(ContractDO::getPartBName, contractPageReqVO.getRelativeName());
        }
        //发送时间筛选
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getSendTime0()) && ObjectUtil.isNotEmpty(contractPageReqVO.getSendTime1())) {
            mpjQueryWrapper.isNotNull("t.send_time").between("t.send_time", contractPageReqVO.getSendTime0().toDateStr(), contractPageReqVO.getSendTime1().toDateStr());
        }
        //确认时间筛选
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getConfirmTime0()) && ObjectUtil.isNotEmpty(contractPageReqVO.getConfirmTime1())) {
            mpjQueryWrapper.between("t.confirm_time", contractPageReqVO.getConfirmTime0().toDateStr(), contractPageReqVO.getConfirmTime1().toDateStr());
            mpjQueryWrapper.isNotNull("t.confirm_time");
        }
        if (ObjectUtil.isNotNull(contractPageReqVO.getYear())){
            mpjQueryWrapper.eq("YEAR(t.create_time)", contractPageReqVO.getYear());
        }
        return selectPage(contractPageReqVO, mpjQueryWrapper);
    }

    /**
     * 合同确认
     * 当前用户作为签署方
     * 合同状态为 2已发送 3待确认
     * <p>
     * 合同管理-合同签署
     *
     * @param contractPageReqVO
     * @return
     */
    default PageResult<ContractDO> selectAffirmPage(ContractPageReqVO contractPageReqVO, AdminUserRespDTO user) {
        String signId = contractPageReqVO.getRelativeId() != null ? contractPageReqVO.getRelativeId() : CompanyEnums.SUBMITTER.getCode();

        MPJQueryWrapper<ContractDO> mpjQueryWrapper = new MPJQueryWrapper<ContractDO>();
        if (CompanyEnums.SUBMITTER.getCode().equals(signId)) {
            mpjQueryWrapper
                    .select("DISTINCT s.contract_id");
//            mpjQueryWrapper.leftJoin("ecms_signatory_rel s on s.contract_id=t.id").leftJoin("ecms_relative r on r.id = s.signatory_id");
        } else {
            mpjQueryWrapper
                    .select(ContractDO.class, info ->
                            !info.getColumn().equals("contract_content"));
            // TODO 存在sql注入风险
//            mpjQueryWrapper.leftJoin("ecms_signatory_rel s on s.contract_id=t.id and s.signatory_id='" + signId + "'").leftJoin("ecms_relative r on r.id = s.signatory_id");

            mpjQueryWrapper.lambda().eq(ContractDO::getPartBId, signId);
        }
        mpjQueryWrapper.lambda()
                .orderByDesc(ContractDO::getUpdateTime)
                //排除富文本字段
                .select(ContractDO.class, info ->
                        !info.getColumn().equals("contract_content")).select("t.id");
//        mpjQueryWrapper.eq("s.deleted", 0).eq("r.deleted", 0);

        // 当前用户作为签署方-要查询的合同id集合
        if (CollectionUtil.isNotEmpty(contractPageReqVO.getContractIdList())) {
            mpjQueryWrapper.in("t.id", contractPageReqVO.getContractIdList());
        }
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getContractCategory())) {
            mpjQueryWrapper.like("t.contract_category", contractPageReqVO.getContractCategory());
        }
        if (StringUtils.isNotEmpty(contractPageReqVO.getCode())) {
            mpjQueryWrapper.like("t.code", contractPageReqVO.getCode());
        }
        if (StringUtils.isNotEmpty(contractPageReqVO.getName())) {
            mpjQueryWrapper.like("t.name", contractPageReqVO.getName());
        }
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getExpirationDate0()) && ObjectUtil.isNotEmpty(contractPageReqVO.getExpirationDate1())) {
            mpjQueryWrapper.between("t.expiration_date", contractPageReqVO.getExpirationDate0().toDateStr(), contractPageReqVO.getExpirationDate1().toDateStr());
        }
        if (StringUtils.isNotEmpty(contractPageReqVO.getContractType())) {
            mpjQueryWrapper.eq("t.contract_type", contractPageReqVO.getContractType());
        }
        if (CollectionUtil.isNotEmpty(contractPageReqVO.getContractTypes())) {
            mpjQueryWrapper.lambda().in(ContractDO::getContractType, contractPageReqVO.getContractTypes());
        }
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getContractSourceType())) {
            mpjQueryWrapper.lambda().eq(ContractDO::getContractSourceType, contractPageReqVO.getContractSourceType());
        }
        if (CollectionUtil.isNotEmpty(contractPageReqVO.getContractSourceTypes())) {
            mpjQueryWrapper.lambda().in(ContractDO::getContractSourceType, contractPageReqVO.getContractSourceTypes());
        }
        //查询字段
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getStatus())) {
            mpjQueryWrapper.eq("t.status", contractPageReqVO.getStatus());
        }
        // 签署状态
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getIsSign())) {
            mpjQueryWrapper.lambda().eq(ContractDO::getIsSign, contractPageReqVO.getIsSign());
        }
        // upload
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getUploadList())) {
            mpjQueryWrapper.lambda().in(ContractDO::getUpload, contractPageReqVO.getUploadList());
        }
        //默认查询合同状态字段
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getStatusList())) {
            mpjQueryWrapper.in("t.status", contractPageReqVO.getStatusList());
        }
        //合同管理-合同签署 搜索条件 -归档-
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getDocument())) {
            mpjQueryWrapper.eq("t.document", contractPageReqVO.getDocument());
        }
        if (StringUtils.isNotEmpty(contractPageReqVO.getRelativeName())) {
            mpjQueryWrapper.lambda().like(ContractDO::getPartBName, contractPageReqVO.getRelativeName());
//            mpjQueryWrapper.and(wrapper -> wrapper.like("r.company_name", contractPageReqVO.getRelativeName())
//                    .or()
//                    .like("r.name", contractPageReqVO.getRelativeName())
//                    .or()
//                    .like("r.contact_name", contractPageReqVO.getRelativeName()));
        }
        if (ObjectUtil.isNotEmpty(user)) {
            mpjQueryWrapper.and(w -> w.eq("t.dept_id", user.getDeptId()).or().eq("r.contact_id", user.getId()));
        }

        return selectPage(contractPageReqVO, mpjQueryWrapper);
    }

    /**
     * 合同管理-合同签署
     * 签署方-登录用户
     *
     * @param contractPageReqVO
     * @return
     */
    default PageResult<ContractDO> selectSignPage(ContractPageReqVO contractPageReqVO) {
        return selectPage(contractPageReqVO, new LambdaQueryWrapperX<ContractDO>()
                //归档
                .eqIfPresent(ContractDO::getDocument, contractPageReqVO.getDocument())
                //筛选当前用户为签署方合同集合
                .inIfPresent(ContractDO::getId, contractPageReqVO.getContractIdList())
                //合同状态为对应加入集合的状态
                .inIfPresent(ContractDO::getStatus, contractPageReqVO.getStatusList())
                //其他动态查询条件
                .likeIfPresent(ContractDO::getCode, contractPageReqVO.getCode())
                .likeIfPresent(ContractDO::getName, contractPageReqVO.getName())
                .likeIfPresent(ContractDO::getContractType, contractPageReqVO.getContractType())
                .betweenIfPresent(ContractDO::getExpirationDate, contractPageReqVO.getExpirationDate0(), contractPageReqVO.getExpirationDate1())
                .inIfPresent(ContractDO::getProcessInstanceId, contractPageReqVO.getProcessInstanceIds())
                .eqIfPresent(ContractDO::getContractCategory, contractPageReqVO.getContractCategory())
                .orderByDesc(ContractDO::getUpdateTime)
                //排除富文本字段
                .select(ContractDO.class, info ->
                        !info.getColumn().equals("contract_content")));
    }

    default PageResult<ContractDO> selectFilingPage(ContractPageReqVO contractPageReqVO) {
        return selectPage(contractPageReqVO, new LambdaQueryWrapperX<ContractDO>()
                .inIfPresent(ContractDO::getStatus, ContractStatusEnums.SIGN_COMPLETED.getCode(), ContractStatusEnums.PERFORMANCE_CLOSURE.getCode(), ContractStatusEnums.PERFORMING.getCode(), ContractStatusEnums.PERFORMANCE_COMPLETE.getCode()
                        , ContractStatusEnums.PERFORMANCE_RISK.getCode(), ContractStatusEnums.PERFORMANCE_RISK_DISPUTE.getCode(), ContractStatusEnums.PERFORMANCE_RISK_PAUSE.getCode(), ContractStatusEnums.PERFORMANCE_RISK_EXTENSION.getCode(), ContractStatusEnums.PERFORMANCE_RISK_OVERDUE.getCode())
                //归档全部数据-list：0 1
                .inIfPresent(ContractDO::getDocument, contractPageReqVO.getDocumentList())
                //归档状态
                .eqIfPresent(ContractDO::getDocument, contractPageReqVO.getDocument())
                //签署时间
                .betweenIfPresent(ContractDO::getExpirationDate, contractPageReqVO.getExpirationDate0(), contractPageReqVO.getExpirationDate1())
                //归档时间
                .betweenIfPresent(ContractDO::getUpdateTime, contractPageReqVO.getDocumentDate0(), contractPageReqVO.getDocumentDate1())
                .eqIfPresent(ContractDO::getContractType, contractPageReqVO.getContractType())
                .orderByDesc(ContractDO::getUpdateTime)
                //合同名称
                .likeIfPresent(ContractDO::getName, contractPageReqVO.getName())
                //合同编码
                .likeIfPresent(ContractDO::getCode, contractPageReqVO.getCode())
                //合同状态
                .inIfPresent(ContractDO::getStatus, contractPageReqVO.getStatus())
                //甲方
                .likeIfPresent(ContractDO::getPartAName, contractPageReqVO.getPartyAName())
                //乙方
                .likeIfPresent(ContractDO::getPartBName, contractPageReqVO.getPartyBName())
                //签署方
                .likeIfPresent(ContractDO::getPartBName, contractPageReqVO.getRelativeName())
                //合同金额
                .betweenIfPresent(ContractDO::getAmount, contractPageReqVO.getAmount0(), contractPageReqVO.getAmount1())
                //合同生效日期
                .betweenIfPresent(ContractDO::getValidity0, contractPageReqVO.getEffectiveDate0(), contractPageReqVO.getEffectiveDate1())
                //合同终止日期
                .betweenIfPresent(ContractDO::getValidity1, contractPageReqVO.getTerminationDate0(), contractPageReqVO.getTerminationDate1())
                .orderByDesc(ContractDO::getUpdateTime)
                //排除富文本字段
                .select(ContractDO.class, info ->
                        !info.getColumn().equals("contract_content")));
    }

    //备案列表
    default PageResult<ContractDO> selectFilingsPage(ContractPageReqVO contractPageReqVO) {
        List uploadList = new ArrayList();
        uploadList.add(ContractUploadTypeEnums.ORDER_DRAFT.getCode());
        uploadList.add(ContractUploadTypeEnums.THIRD_PARTY.getCode());
//        if(contractPageReqVO.getIsFilings() == 0){
//            
//        }
        return selectPage(contractPageReqVO, new LambdaQueryWrapperX<ContractDO>()
                .inIfPresent(ContractDO::getStatus, ContractStatusEnums.SIGN_COMPLETED.getCode(), ContractStatusEnums.PERFORMANCE_CLOSURE.getCode(), ContractStatusEnums.PERFORMING.getCode(), ContractStatusEnums.PERFORMANCE_COMPLETE.getCode()
                        , ContractStatusEnums.PERFORMANCE_RISK.getCode(), ContractStatusEnums.PERFORMANCE_RISK_DISPUTE.getCode(), ContractStatusEnums.PERFORMANCE_RISK_PAUSE.getCode(), ContractStatusEnums.PERFORMANCE_RISK_EXTENSION.getCode(), ContractStatusEnums.PERFORMANCE_RISK_OVERDUE.getCode())
                //备案全部数据-list：0 1 2
                .inIfPresent(ContractDO::getIsFilings, contractPageReqVO.getDocumentList())
                .inIfPresent(ContractDO::getUpload, uploadList)
                //备案状态
                .eqIfPresent(ContractDO::getIsFilings, contractPageReqVO.getIsFilings())
                //签署时间
                .betweenIfPresent(ContractDO::getExpirationDate, contractPageReqVO.getExpirationDate0(), contractPageReqVO.getExpirationDate1())
                .eqIfPresent(ContractDO::getContractType, contractPageReqVO.getContractType())
                .orderByDesc(ContractDO::getUpdateTime)
                //合同名称
                .likeIfPresent(ContractDO::getName, contractPageReqVO.getName())
                //合同编码
                .likeIfPresent(ContractDO::getCode, contractPageReqVO.getCode())
                //合同状态
                .inIfPresent(ContractDO::getStatus, contractPageReqVO.getStatus())
                //甲方
                .likeIfPresent(ContractDO::getPartAName, contractPageReqVO.getPartyAName())
                //乙方
                .likeIfPresent(ContractDO::getPartBName, contractPageReqVO.getPartyBName())
                //签署方
                .likeIfPresent(ContractDO::getPartBName, contractPageReqVO.getRelativeName())
                //合同金额
                .betweenIfPresent(ContractDO::getAmount, contractPageReqVO.getAmount0(), contractPageReqVO.getAmount1())
                //合同生效日期
                .betweenIfPresent(ContractDO::getValidity0, contractPageReqVO.getEffectiveDate0(), contractPageReqVO.getEffectiveDate1())
                //合同终止日期
                .betweenIfPresent(ContractDO::getValidity1, contractPageReqVO.getTerminationDate0(), contractPageReqVO.getTerminationDate1())
                .orderByDesc(ContractDO::getUpdateTime)
                //排除富文本字段
                .select(ContractDO.class, info ->
                        !info.getColumn().equals("contract_content")));
    }

    default PageResult<ContractDO> selectDocumentPage(ContractDocumentPageReqVO contractDocumentPageReqVO) {
        return selectPage(contractDocumentPageReqVO, new LambdaQueryWrapperX<ContractDO>()
                //上传合同方式
                .eq(ContractDO::getUpload, 1)
                .eqIfPresent(ContractDO::getContractCategory, contractDocumentPageReqVO.getContractCategory())
                .eqIfPresent(ContractDO::getAmountType, contractDocumentPageReqVO.getAmountType())
                .eqIfPresent(ContractDO::getDocument, contractDocumentPageReqVO.getDocument())
                .eqIfPresent(ContractDO::getAmount, contractDocumentPageReqVO.getAmount()));
    }

    /**
     * 获取档案借阅合同列表
     */
    default PageResult<ContractDO> selectLoanPage(LoanPageReqVO loanPageReqVO) {
        LambdaQueryWrapperX<ContractDO> contractDOLambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        contractDOLambdaQueryWrapperX.select(
                ContractDO::getId,
                ContractDO::getName,
                ContractDO::getCode,
                ContractDO::getArchiveUser,
                ContractDO::getArchiveTime,
                ContractDO::getBorrow
        ).eq(ContractDO::getDocument, 1).orderByDesc(ContractDO::getUpdateTime);
        contractDOLambdaQueryWrapperX.likeIfPresent(ContractDO::getName, loanPageReqVO.getName())
                .likeIfPresent(ContractDO::getCode, loanPageReqVO.getCode())
                .likeIfPresent(ContractDO::getArchiveUser, loanPageReqVO.getArchiveUser())
                .betweenIfPresent(ContractDO::getArchiveTime, loanPageReqVO.getArchiveStartTime(), loanPageReqVO.getArchiveEndTime())
                .eqIfPresent(ContractDO::getBorrow, loanPageReqVO.getBorrow());
        return selectPage(loanPageReqVO, contractDOLambdaQueryWrapperX);
    }

    /**
     * 合同签约类型统计
     * 查询当前用户所有签署完成的合同
     *
     * @param contractIdList
     * @return
     */
    default List<ContractDO> selectTypeStatistics(ArrayList<String> contractIdList) {
        return selectList(new QueryWrapper<ContractDO>()
                .select("contract_type")
                .in("id", contractIdList)
                .eq("status", ContractStatusEnums.SIGN_COMPLETED.getCode()));
    }

    default Boolean nameExist(String id, String name) {
        return selectCount(new LambdaQueryWrapperX<ContractDO>()
                .eqIfPresent(ContractDO::getName, name)
                .neIfPresent(ContractDO::getId, id)) > 0;
    }

    default Boolean codeExist(String id, String code) {
        return selectCount(new LambdaQueryWrapperX<ContractDO>()
                .eqIfPresent(ContractDO::getCode, code)
                .neIfPresent(ContractDO::getId, id)) > 0;
    }

    default Boolean exist(String name, String code) {
        return selectCount(new LambdaQueryWrapperX<ContractDO>()
                .eqIfPresent(ContractDO::getName, name)
                .eqIfPresent(ContractDO::getCode, code)) > 0;
    }

    /**
     * 获取最近使用模板的合同信息
     *
     * @return
     */
    default List<ContractDO> selectModelContract() {
        return selectList(new QueryWrapper<ContractDO>().select("template_id", "MAX(create_time) as create_time")
                .isNotNull("template_id")
                .groupBy("template_id")
                .orderByDesc("create_time")
                .last("LIMIT 6"));
    }

    /**
     * 获取最近使用文件的合同信息
     *
     * @return
     */
    default List<ContractDO> selectFileContract() {
        return selectList(new QueryWrapper<ContractDO>().select("file_add_id", "MAX(create_time) as create_time").lambda()
                .ne(ContractDO::getFileAddId, 0)
                .groupBy(ContractDO::getFileAddId)
                .orderByDesc(ContractDO::getCreateTime)
                .last("LIMIT 6"));
    }

    /**
     * 通过合同id，拿到金额
     */
    default ContractDO getAmount(String contractId) {
        return selectOne(new LambdaQueryWrapperX<ContractDO>()
                .select(ContractDO::getAmount)
                .eq(ContractDO::getId, contractId));

    }

    default PageResult<ContractDO> selectApprovePage(CommonBpmAutoPageReqVO pageVO) {
        if (CollectionUtils.isEmpty(pageVO.getInstanceIdList())) {
            return new PageResult<ContractDO>().setTotal(0L).setList(Collections.emptyList());
        }
        MPJLambdaWrapper<ContractDO> mpjLambdaWrapper = new MPJLambdaWrapper<ContractDO>()
                .leftJoin(BpmContract.class, BpmContract::getContractId, ContractDO::getId)
                .selectAll(ContractDO.class)
                .orderByDesc(ContractDO::getUpdateTime)
                .distinct();
        if (ObjectUtil.isNotNull(pageVO.getName())) {
            mpjLambdaWrapper.like(ContractDO::getName, pageVO.getName());
        }
        if (ObjectUtil.isNotNull(pageVO.getCode())) {
            mpjLambdaWrapper.like(ContractDO::getCode, pageVO.getCode());
        }
        if (ObjectUtil.isNotNull(pageVO.getContractType())) {
            mpjLambdaWrapper.eq(ContractDO::getContractType, pageVO.getContractType());
        }

        if (ObjectUtil.isNotNull(pageVO.getSubmitTime0())) {
            mpjLambdaWrapper.between(ContractDO::getSubmitTime, pageVO.getSubmitTime0(), pageVO.getSubmitTime1());
        }
        if (ObjectUtil.isNotNull(pageVO.getApproveTime0())) {
            mpjLambdaWrapper.between(ContractDO::getApproveTime, pageVO.getApproveTime0(), pageVO.getApproveTime1());
        }
        if (ObjectUtil.isNotNull(pageVO.getResult())) {
            mpjLambdaWrapper.eq(BpmContract::getResult, pageVO.getResult());
        }
        if (CollectionUtil.isNotEmpty(pageVO.getInstanceIdList())) {
            mpjLambdaWrapper.in(BpmContract::getProcessInstanceId, pageVO.getInstanceIdList());
        }
        return selectPage(pageVO, mpjLambdaWrapper);
    }

    default PageResult<ContractDO> selectContractChangeApprovePage(CommonBpmAutoPageReqVO pageVO) {
        if (CollectionUtils.isEmpty(pageVO.getInstanceIdList())) {
            return new PageResult<ContractDO>().setTotal(0L).setList(Collections.emptyList());
        }
        MPJLambdaWrapper<ContractDO> mpjLambdaWrapper = new MPJLambdaWrapper<ContractDO>()
                .leftJoin(BpmContractChangeDO.class, BpmContractChangeDO::getContractId, ContractDO::getId)
                .selectAll(ContractDO.class)
                .orderByDesc(ContractDO::getUpdateTime)
                .distinct();
        if (ObjectUtil.isNotNull(pageVO.getApproveTime0())) {
            mpjLambdaWrapper.between(ContractDO::getSubmitTime, pageVO.getApproveTime0(), pageVO.getApproveTime1());
        }
        if (ObjectUtil.isNotNull(pageVO.getResult())) {
            mpjLambdaWrapper.eq(BpmContractChangeDO::getResult, pageVO.getResult());
        }
        if (CollectionUtil.isNotEmpty(pageVO.getInstanceIdList())) {
            mpjLambdaWrapper.in(BpmContractChangeDO::getProcessInstanceId, pageVO.getInstanceIdList());
        }
        if (ObjectUtil.isNotNull(pageVO.getSubmitTime0())) {
            mpjLambdaWrapper.between(BpmContractChangeDO::getCreateTime, pageVO.getSubmitTime0(), pageVO.getSubmitTime1());
        }
        if (ObjectUtil.isNotNull(pageVO.getChangeType())) {
            mpjLambdaWrapper.eq(ContractDO::getChangeType, pageVO.getChangeType());
        }
        if (ObjectUtil.isNotNull(pageVO.getResult())) {
            mpjLambdaWrapper.eq(BpmContractChangeDO::getResult, pageVO.getResult());
        }
        if (ObjectUtil.isNotNull(pageVO.getCode())) {
            mpjLambdaWrapper.like(ContractDO::getCode, pageVO.getCode());
        }
        if (ObjectUtil.isNotNull(pageVO.getName())) {
            mpjLambdaWrapper.like(ContractDO::getName, pageVO.getName());
        }
        if (ObjectUtil.isNotNull(pageVO.getApplicantName())) {
            mpjLambdaWrapper.in(BpmContractChangeDO::getCreator, pageVO.getUserList());
        }
        return selectPage(pageVO, mpjLambdaWrapper);


    }

    /**
     * 合同借阅信息集合
     *
     * @param ids
     * @return
     */
    default List<ContractDO> selectIds(List<String> ids) {
        LambdaQueryWrapperX<ContractDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.select(
                ContractDO::getId,
                ContractDO::getName,
                ContractDO::getCode
        ).in(ContractDO::getId, ids).orderByDesc(ContractDO::getUpdateTime);
        return selectList(wrapperX);
    }

    default PageResult<ContractDO> selectLedgerPage(LedgerPageReqV2VO contractPageReqVO,List<String> contractIds, Long tenantId, Long deptId,String permissionSql) {


        LambdaQueryWrapperX<ContractDO> wrapperX = new LambdaQueryWrapperX<ContractDO>();
        wrapperX.likeIfPresent(ContractDO::getName, contractPageReqVO.getName())
                .likeIfPresent(ContractDO::getCode, contractPageReqVO.getCode())
                .inIfPresent(ContractDO::getStatus, contractPageReqVO.getStatus())
                .inIfPresent(ContractDO::getAmountType, contractPageReqVO.getAmountType())
                .leIfPresent(ContractDO::getAmount, contractPageReqVO.getMaxAmount())
                .geIfPresent(ContractDO::getAmount, contractPageReqVO.getMinAmount())
                .betweenIfPresent(ContractDO::getValidity0, contractPageReqVO.getBeginDate(), contractPageReqVO.getEndDate())
                .betweenIfPresent(ContractDO::getValidity1, contractPageReqVO.getExpirationDate0(), contractPageReqVO.getExpirationDate1())
                .betweenIfPresent(ContractDO::getSignDate,contractPageReqVO.getSignDate0(),contractPageReqVO.getSignDate1())
                .inIfPresent(ContractDO::getContractType, contractPageReqVO.getContractType())
                .likeIfPresent(ContractDO::getPartAName, contractPageReqVO.getPartAName())
                .likeIfPresent(ContractDO::getPartBName, contractPageReqVO.getPartBName())
               // .eqIfPresent(ContractDO::getTenantId, tenantId)
               // .eq(ContractDO::getDeptId, deptId)
                .ne(ContractDO::getStatus, ContractStatusEnums.TO_BE_CHECK.getCode())
                .apply(permissionSql)
                .select(ContractDO::getId, ContractDO::getName, ContractDO::getCode, ContractDO::getValidity0, ContractDO::getValidity1, ContractDO::getAmount, ContractDO::getContractType, ContractDO::getProcessInstanceId, ContractDO::getSignDate,
                        ContractDO::getSignDate,
                        ContractDO::getContractType, ContractDO::getPlatform, ContractDO::getUpload, ContractDO::getStatus, ContractDO::getPartAName, ContractDO::getPartBName, ContractDO::getIsOffline).orderByDesc(ContractDO::getUpdateTime);
//            if (CollectionUtil.isNotEmpty(contractIds)) {
//                wrapperX.or(wr -> wr.in(ContractDO::getId, contractIds));
//            }
        return selectPage(contractPageReqVO, wrapperX);

//        return selectPage(contractPageReqVO, new LambdaQueryWrapperX<ContractDO>()
//                .likeIfPresent(ContractDO::getName, contractPageReqVO.getName())
//                .likeIfPresent(ContractDO::getCode, contractPageReqVO.getCode())
//                .inIfPresent(ContractDO::getStatus, contractPageReqVO.getStatus())
//                .leIfPresent(ContractDO::getAmount, contractPageReqVO.getMaxAmount())
//                .geIfPresent(ContractDO::getAmount, contractPageReqVO.getMinAmount())
//                .geIfPresent(ContractDO::getValidity0, contractPageReqVO.getBeginDate())
//                .leIfPresent(ContractDO::getValidity1, contractPageReqVO.getEndDate())
//                .inIfPresent(ContractDO::getContractType, contractPageReqVO.getContractType())
//                .likeIfPresent(ContractDO::getPartAName, contractPageReqVO.getPartAName())
//                .likeIfPresent(ContractDO::getPartBName, contractPageReqVO.getPartBName())
//                .inIfPresent(ContractDO::getProcessInstanceId, contractPageReqVO.getProcessInstanceIds())
//                .geIfPresent(ContractDO::getExpirationDate, contractPageReqVO.getExpirationDate0())
//                .leIfPresent(ContractDO::getExpirationDate, contractPageReqVO.getExpirationDate1())
//                .select(ContractDO::getId, ContractDO::getName, ContractDO::getCode, ContractDO::getValidity0, ContractDO::getValidity1, ContractDO::getAmount, ContractDO::getContractType, ContractDO::getProcessInstanceId, ContractDO::getSignDate,
//                        ContractDO::getContractType, ContractDO::getStatus, ContractDO::getPartAName, ContractDO::getPartBName).orderByDesc(ContractDO::getUpdateTime));
    }

    default List<ContractDO> selectLedgerList(LedgerPageReqV2VO contractPageReqVO,List<String> contractIds, Long tenantId, Long deptId,String permissionSql) {


        LambdaQueryWrapperX<ContractDO> wrapperX = new LambdaQueryWrapperX<ContractDO>();
        wrapperX.likeIfPresent(ContractDO::getName, contractPageReqVO.getName())
                .likeIfPresent(ContractDO::getCode, contractPageReqVO.getCode())
                .inIfPresent(ContractDO::getStatus, contractPageReqVO.getStatus())
                .inIfPresent(ContractDO::getAmountType, contractPageReqVO.getAmountType())
                .leIfPresent(ContractDO::getAmount, contractPageReqVO.getMaxAmount())
                .geIfPresent(ContractDO::getAmount, contractPageReqVO.getMinAmount())
                .betweenIfPresent(ContractDO::getValidity0, contractPageReqVO.getBeginDate(), contractPageReqVO.getEndDate())
                .betweenIfPresent(ContractDO::getValidity1, contractPageReqVO.getExpirationDate0(), contractPageReqVO.getExpirationDate1())
                .inIfPresent(ContractDO::getContractType, contractPageReqVO.getContractType())
                .likeIfPresent(ContractDO::getPartAName, contractPageReqVO.getPartAName())
                .likeIfPresent(ContractDO::getPartBName, contractPageReqVO.getPartBName())
//                .eqIfPresent(ContractDO::getTenantId, tenantId)
//                .eq(ContractDO::getDeptId, deptId)
                .apply(permissionSql)
                .ne(ContractDO::getStatus, ContractStatusEnums.TO_BE_CHECK.getCode())

                .select(ContractDO::getId, ContractDO::getName, ContractDO::getAmountType, ContractDO::getCode, ContractDO::getValidity0, ContractDO::getValidity1, ContractDO::getAmount, ContractDO::getContractType, ContractDO::getProcessInstanceId, ContractDO::getSignDate,
                        ContractDO::getContractType, ContractDO::getStatus, ContractDO::getPartAName, ContractDO::getPartBName).orderByDesc(ContractDO::getUpdateTime);

//        if (CollectionUtil.isNotEmpty(contractIds)) {
//            wrapperX.or(wr -> wr.in(ContractDO::getId, contractIds));
//        }
        return selectList(wrapperX);

//        return selectList(new LambdaQueryWrapperX<ContractDO>()
//                .likeIfPresent(ContractDO::getName, contractPageReqVO.getName())
//                .likeIfPresent(ContractDO::getCode, contractPageReqVO.getCode())
//                .inIfPresent(ContractDO::getStatus, contractPageReqVO.getStatus())
//                .leIfPresent(ContractDO::getAmount, contractPageReqVO.getMaxAmount())
//                .geIfPresent(ContractDO::getAmount, contractPageReqVO.getMinAmount())
//                .geIfPresent(ContractDO::getValidity0, contractPageReqVO.getBeginDate())
//                .leIfPresent(ContractDO::getValidity1, contractPageReqVO.getEndDate())
//                .inIfPresent(ContractDO::getContractType, contractPageReqVO.getContractType())
//                .likeIfPresent(ContractDO::getPartAName, contractPageReqVO.getPartAName())
//                .likeIfPresent(ContractDO::getPartBName, contractPageReqVO.getPartBName())
//                .inIfPresent(ContractDO::getProcessInstanceId, contractPageReqVO.getProcessInstanceIds())
//                .geIfPresent(ContractDO::getExpirationDate, contractPageReqVO.getExpirationDate0())
//                .leIfPresent(ContractDO::getExpirationDate, contractPageReqVO.getExpirationDate1())
//                .select(ContractDO::getAmount, ContractDO::getId));
    }

    default List<ContractDO> select4Purchase() {
        MPJLambdaWrapper<ContractDO> mpjLambdaWrapper = new MPJLambdaWrapper<ContractDO>()
                .select(ContractDO::getId, ContractDO::getAmount);

        mpjLambdaWrapper.leftJoin(DraftOrderInfoDO.class, DraftOrderInfoDO::getOrderGuid, ContractDO::getOrderGuid)
                //6-签署完成
                .eq(ContractDO::getStatus, 6)
                .eq(DraftOrderInfoDO::getOrderCategory, "purchase");

        return selectList(mpjLambdaWrapper);
    }

    default List<ContractDO> selectPayed() {
        MPJLambdaWrapper<ContractDO> mpjLambdaWrapper = new MPJLambdaWrapper<ContractDO>()
                .selectAll(ContractDO.class);
        // 商品名称
        mpjLambdaWrapper.leftJoin(PaymentApplicationDO.class, PaymentApplicationDO::getContractId, ContractDO::getId)
                .eq(PaymentApplicationDO::getResult, BpmProcessInstanceResultEnum.APPROVE.getResult());
        return selectList(mpjLambdaWrapper);
    }

    default List<ContractDO> select4ContractType(String contractTypeName) {
        MPJLambdaWrapper<ContractDO> mpjLambdaWrapper = new MPJLambdaWrapper<ContractDO>()
                .selectAll(ContractDO.class);

        mpjLambdaWrapper.leftJoin(ContractType.class, ContractType::getId, ContractDO::getContractType)
                .leftJoin(DraftOrderInfoDO.class, DraftOrderInfoDO::getOrderGuid, ContractDO::getOrderGuid)
                .eq(DraftOrderInfoDO::getOrderCategory, "purchase")
                .like(ContractType::getName, contractTypeName);
        return selectList(mpjLambdaWrapper);
    }

    default PageResult<ContractDO> select4Performance(ContractPerformV2ReqVO reqVO, List<String> filteredContractIds, List<Integer> statusList) {
        LambdaQueryWrapperX<ContractDO> wrapperX = new LambdaQueryWrapperX<ContractDO>();
        wrapperX.select(ContractDO::getId, ContractDO::getName, ContractDO::getCode
                , ContractDO::getContractType, ContractDO::getSignDate, ContractDO::getStatus, ContractDO::getPauseDate);

        ContractStatusEnums statusEnums = ContractStatusEnums.getInstance(reqVO.getStatus());
        if (ObjectUtil.isNotNull(statusEnums)) {
            switch (statusEnums) {
                case SIGN_COMPLETED:
                    wrapperX.orderByAsc(ContractDO::getSignDate);
                    break;
                case PERFORMING:

                case PERFORMANCE_COMPLETE:

                case PERFORMANCE_CLOSURE:
                    wrapperX.orderByDesc(ContractDO::getSignDate);
                    break;

                case PERFORMANCE_RISK:
                    wrapperX.orderByAsc(ContractDO::getRiskDate);
                    break;

                default:
                    wrapperX.orderByDesc(ContractDO::getSignDate);

                    break;
            }
        }

//        wrapperX.leftJoin(PaymentScheduleDO.class, PaymentScheduleDO::getContractId, ContractDO::getId);

        //待履约列表不查upload=3或4的
//        if (Objects.equals(ContractStatusEnums.SIGN_COMPLETED.getCode(), reqVO.getStatus())) {
//            //待发送列表不查upload=3或4的
//            List<Integer> uploadTypeList = new ArrayList<>();
//            uploadTypeList.add(ContractUploadTypeEnums.ORDER_DRAFT.getCode());
//            uploadTypeList.add(ContractUploadTypeEnums.THIRD_PARTY.getCode());
//            wrapperX.notIn(ContractDO::getUpload, uploadTypeList);
//        }


        if (CollectionUtil.isNotEmpty(statusList)) {
            wrapperX.in(ContractDO::getStatus, statusList);
        } else {
            wrapperX.eq(ContractDO::getStatus, reqVO.getStatus());
        }
        //合同编号和名称，用同一个字段contractName
        if (StringUtils.isNotEmpty(reqVO.getContractName())) {
            wrapperX.and(w -> w.like(ContractDO::getName, reqVO.getContractName())
                    .or()
                    .like(ContractDO::getCode, reqVO.getContractName()));
        }
        //履约截止时间
//        if (ObjectUtil.isNotNull(reqVO.getPerformanceExpirationDate())) {
//            wrapperX.in(PaymentScheduleDO::getPaymentTime, reqVO.getPerformanceSignedDateDate0(), reqVO.getPerformanceSignedDateDate1());
//        }
        //签署完成时间
        if (ObjectUtil.isNotNull(reqVO.getPerformanceSignedDateDate0())) {
            wrapperX.between(ContractDO::getSignDate, reqVO.getPerformanceSignedDateDate0(), reqVO.getPerformanceSignedDateDate1());
        }
        //签署完成时间
        if (ObjectUtil.isNotNull(reqVO.getPerformanceSignedDate())) {
            wrapperX.eq(ContractDO::getSignDate, reqVO.getPerformanceSignedDate());
        }
        //合同类型
        if (ObjectUtil.isNotNull(reqVO.getContractType())) {
            wrapperX.eq(ContractDO::getContractType, reqVO.getContractType());
        }
        // 分情况排序
        // 待履约：签署完成日期最新在上
        // ↓注释掉。与上面的按状态排序逻辑存在冲突
//        if (ContractStatusEnums.SIGN_COMPLETED.getCode().equals(reqVO.getStatus())) {
//            wrapperX.orderByDesc(ContractDO::getSignDate);
//        }
        // 履约中：默认按照 履约发布时间最新在上
        if (ContractStatusEnums.PERFORMING.getCode().equals(reqVO.getStatus())) {
            wrapperX.orderByDesc(ContractDO::getSignDate);
        }
        // 履约完成 履约关闭  履约风险：完成时间最新在上
        if (ContractStatusEnums.PERFORMANCE_COMPLETE.getCode().equals(reqVO.getStatus()) || ContractStatusEnums.PERFORMANCE_RISK.getCode().equals(reqVO.getStatus()) || ContractStatusEnums.PERFORMANCE_CLOSURE.getCode().equals(reqVO.getStatus())) {
            wrapperX.orderByDesc(ContractDO::getUpdateTime);
        }
        return selectPage(reqVO, wrapperX);
    }

//     List<ContractDO> selectUsedMost( Long loginUserId,Integer size);

    default ContractDO selectOneByPlanId(String id) {
        MPJLambdaWrapper<ContractDO> wrapper = new MPJLambdaWrapper<ContractDO>()
                .selectAll(ContractDO.class)
                .leftJoin(PaymentScheduleDO.class, PaymentScheduleDO::getContractId, ContractDO::getId)
                .eq(PaymentScheduleDO::getId, id);
        return selectOne(wrapper);
    }

    default List<ContractDO> selectAllSigned(Date startDate, Date endDate) {
        List amtType = new ArrayList();
        amtType.add( AmountTypeEnums.PAY.getCode());
        amtType.add( AmountTypeEnums.RECEIPT.getCode());
        amtType.add( AmountTypeEnums.DIRECTION.getCode());

//        List uploadList = new ArrayList();
//        uploadList.add(ContractUploadTypeEnums.ORDER_DRAFT.getCode());
//        uploadList.add(ContractUploadTypeEnums.THIRD_PARTY.getCode());
        return selectList(new LambdaQueryWrapperX<ContractDO>()
                .select(ContractDO::getId, ContractDO::getStatus, ContractDO::getAmountType, ContractDO::getUpload, ContractDO::getAmount, BaseDO::getCreateTime)
                .and(wrapper->wrapper.in(ContractDO::getAmountType, amtType).or(
                        subWrapper-> subWrapper.isNull(ContractDO::getAmountType)
//                                .in(ContractDO::getUpload, uploadList)
                ))
                .between(ContractDO::getCreateTime, startDate, endDate)
                .in(ContractDO::getStatus, ContractStatusEnums.SIGN_COMPLETED.getCode(), ContractStatusEnums.PERFORMANCE_CLOSURE.getCode(), ContractStatusEnums.PERFORMING.getCode(), ContractStatusEnums.PERFORMANCE_COMPLETE.getCode()
                        , ContractStatusEnums.PERFORMANCE_RISK.getCode() , ContractStatusEnums.CONTRACT_CHANGE.getCode(), ContractStatusEnums.PERFORMANCE_RISK_DISPUTE.getCode(), ContractStatusEnums.PERFORMANCE_RISK_PAUSE.getCode(), ContractStatusEnums.PERFORMANCE_RISK_EXTENSION.getCode(), ContractStatusEnums.PERFORMANCE_RISK_OVERDUE.getCode())
        );
    }

    @Select("SELECT template_id, COUNT(*) AS code " +
            "FROM ecms_contract " +
            "WHERE deleted = 0 " +
            "AND creator = #{loginUserId} " +
            "GROUP BY template_id " +
            "ORDER BY code DESC, template_id ASC " +
            "LIMIT #{size}")
    List<ContractDO> selectUsedMost(@Param("loginUserId") Long loginUserId, @Param("size") Integer size);

    default PageResult<ContractDO> selectPage4Bench(WorkBenchTaskReqVO reqVO) {
        if (CollectionUtils.isEmpty(reqVO.getInstanceIdList())) {
            return new PageResult<ContractDO>().setTotal(0L).setList(Collections.emptyList());
        }
        MPJLambdaWrapper<ContractDO> mpjLambdaWrapper = new MPJLambdaWrapper<ContractDO>()
                .leftJoin(BpmContract.class, BpmContract::getContractId, ContractDO::getId)
                .select(ContractDO::getId, ContractDO::getCode, ContractDO::getName, ContractDO::getAmount, ContractDO::getContractType, ContractDO::getProcessInstanceId, ContractDO::getCreateTime, ContractDO::getUpdateTime, ContractDO::getSubmitTime)
                .selectAs(BpmContract::getProcessInstanceId, ContractDO::getAgentId)//实例id映射到代理id
                .selectAs(BpmContract::getCreateTime, ContractDO::getSubmitTime)
                .in(BpmContract::getProcessInstanceId, reqVO.getInstanceIdList())
                .orderByDesc(ContractDO::getUpdateTime)
                .distinct();
        return selectPage(reqVO, mpjLambdaWrapper);
    }

    default Long count4Bench(List<String> instanceIdList) {
        if (CollectionUtils.isEmpty(instanceIdList)) {
            return 0L;
        }
        MPJLambdaWrapper<ContractDO> mpjLambdaWrapper = new MPJLambdaWrapper<ContractDO>()
                .leftJoin(BpmContract.class, BpmContract::getContractId, ContractDO::getId)
                .in(BpmContract::getProcessInstanceId, instanceIdList)
                .orderByDesc(ContractDO::getUpdateTime)
                .distinct();
        return selectCount(mpjLambdaWrapper);
    }

    default Long countSign4Bench(List<String> instanceIdList) {
        if (CollectionUtils.isEmpty(instanceIdList)) {
            return 0L;
        }
        return selectCount(new LambdaQueryWrapperX<ContractDO>()
                .in(ContractDO::getProcessInstanceId, instanceIdList)
                .eq(ContractDO::getStatus,ContractStatusEnums.TO_BE_SIGNED.getCode())
                .orderByDesc(ContractDO::getUpdateTime));
    }

    default PageResult<ContractDO> selectSignPage4Bench(WorkBenchTaskReqVO reqVO) {
        if (CollectionUtils.isEmpty(reqVO.getInstanceIdList())) {
            return new PageResult<ContractDO>().setTotal(0L).setList(Collections.emptyList());
        }

        return selectPage(reqVO, new LambdaQueryWrapperX<ContractDO>()
                .select(ContractDO::getId, ContractDO::getCode, ContractDO::getName, ContractDO::getAmount, ContractDO::getContractType, ContractDO::getProcessInstanceId, ContractDO::getCreateTime, ContractDO::getUpdateTime)
                .in(ContractDO::getProcessInstanceId, reqVO.getInstanceIdList())
                .eq(ContractDO::getStatus,ContractStatusEnums.TO_BE_SIGNED.getCode())
                .orderByDesc(ContractDO::getUpdateTime));
    }

    default PageResult<ContractDO> selectPaymentPage4Bench(WorkBenchTaskReqVO reqVO) {
        if (CollectionUtils.isEmpty(reqVO.getInstanceIdList())) {
            return new PageResult<ContractDO>().setTotal(0L).setList(Collections.emptyList());
        }
        MPJLambdaWrapper<ContractDO> mpjLambdaWrapper = new MPJLambdaWrapper<ContractDO>()
                .leftJoin(PaymentApplicationDO.class, PaymentApplicationDO::getContractId, ContractDO::getId)
                .select(ContractDO::getId, ContractDO::getCode, ContractDO::getName, ContractDO::getAmount, ContractDO::getContractType, ContractDO::getProcessInstanceId, ContractDO::getCreateTime, ContractDO::getUpdateTime, ContractDO::getSubmitTime)
                .selectAs(PaymentApplicationDO::getProcessInstanceId, ContractDO::getAgentId)//实例id映射到代理id
                .selectAs(PaymentApplicationDO::getCreateTime, ContractDO::getSubmitTime)
                .in(PaymentApplicationDO::getProcessInstanceId, reqVO.getInstanceIdList())
                .orderByDesc(ContractDO::getCreateTime)
                .distinct();
        return selectPage(reqVO, mpjLambdaWrapper);
    }

    default PageResult<ContractDO> selectBorrowPage4Bench(WorkBenchTaskReqVO reqVO) {
        if (CollectionUtils.isEmpty(reqVO.getInstanceIdList())) {
            return new PageResult<ContractDO>().setTotal(0L).setList(Collections.emptyList());
        }
        MPJLambdaWrapper<ContractDO> mpjLambdaWrapper = new MPJLambdaWrapper<ContractDO>()
                .leftJoin(ContractArchivesDO.class, ContractArchivesDO::getContractId, ContractDO::getId)
                .leftJoin(ContractBorrowBpmDO.class, ContractBorrowBpmDO::getArchiveId, ContractArchivesDO::getId)
                .select(ContractDO::getId, ContractDO::getCode, ContractDO::getName, ContractDO::getAmount, ContractDO::getContractType, ContractDO::getProcessInstanceId, ContractDO::getCreateTime, ContractDO::getUpdateTime)
                .selectAs(ContractBorrowBpmDO::getProcessInstanceId, ContractDO::getAgentId)//实例id映射到代理id
                .in(ContractBorrowBpmDO::getProcessInstanceId, reqVO.getInstanceIdList())
                .orderByDesc(ContractDO::getCreateTime)
                .distinct();
        return selectPage(reqVO, mpjLambdaWrapper);
    }

    default PageResult<ContractDO> selectArchivePage4Bench(WorkBenchTaskReqVO reqVO) {
        if (CollectionUtils.isEmpty(reqVO.getInstanceIdList())) {
            return new PageResult<ContractDO>().setTotal(0L).setList(Collections.emptyList());
        }
        MPJLambdaWrapper<ContractDO> mpjLambdaWrapper = new MPJLambdaWrapper<ContractDO>()
                .leftJoin(ContractArchivesDO.class, ContractArchivesDO::getContractId, ContractDO::getId)
                .leftJoin(BpmContractArchivesDO.class, BpmContractArchivesDO::getArchiveId, ContractArchivesDO::getId)
                .select(ContractDO::getId, ContractDO::getCode, ContractDO::getName, ContractDO::getAmount, ContractDO::getContractType, ContractDO::getProcessInstanceId, ContractDO::getCreateTime, ContractDO::getUpdateTime, ContractDO::getSubmitTime)
                .selectAs(BpmContractArchivesDO::getProcessInstanceId, ContractDO::getAgentId)//实例id映射到代理id
                .selectAs(PaymentApplicationDO::getCreateTime, ContractDO::getSubmitTime)
                .in(BpmContractArchivesDO::getProcessInstanceId, reqVO.getInstanceIdList())
                .orderByDesc(ContractDO::getCreateTime)
                .distinct();
        return selectPage(reqVO, mpjLambdaWrapper);
    }

    default PageResult<ContractDO> selectInvoicePage4Bench(WorkBenchTaskReqVO reqVO) {
        if (CollectionUtils.isEmpty(reqVO.getInstanceIdList())) {
            return new PageResult<ContractDO>().setTotal(0L).setList(Collections.emptyList());
        }
        MPJLambdaWrapper<ContractDO> mpjLambdaWrapper = new MPJLambdaWrapper<ContractDO>()
                .leftJoin(ContractInvoiceManageDO.class, ContractInvoiceManageDO::getContractId, ContractDO::getId)
                .select(ContractDO::getId, ContractDO::getCode, ContractDO::getName, ContractDO::getAmount, ContractDO::getContractType, ContractDO::getProcessInstanceId, ContractDO::getCreateTime, ContractDO::getUpdateTime, ContractDO::getSubmitTime)
                .selectAs(ContractInvoiceManageDO::getProcessInstanceId, ContractDO::getAgentId)//实例id映射到代理id
                .in(ContractInvoiceManageDO::getProcessInstanceId, reqVO.getInstanceIdList())
                .orderByDesc(ContractDO::getCreateTime)
                .distinct();
        return selectPage(reqVO, mpjLambdaWrapper);
    }

    default PageResult<ContractDO> selectDeferredPage4Bench(WorkBenchTaskReqVO reqVO) {
        if (CollectionUtils.isEmpty(reqVO.getInstanceIdList())) {
            return new PageResult<ContractDO>().setTotal(0L).setList(Collections.emptyList());
        }
        MPJLambdaWrapper<ContractDO> mpjLambdaWrapper = new MPJLambdaWrapper<ContractDO>()
                .leftJoin(PaymentDeferredApplyDO.class, PaymentDeferredApplyDO::getContractId, ContractDO::getId)
                .select(ContractDO::getId, ContractDO::getCode, ContractDO::getName, ContractDO::getAmount, ContractDO::getContractType, ContractDO::getProcessInstanceId, ContractDO::getCreateTime, ContractDO::getUpdateTime)
                .selectAs(PaymentDeferredApplyDO::getProcessInstanceId, ContractDO::getAgentId)//实例id映射到代理id
                .in(PaymentDeferredApplyDO::getProcessInstanceId, reqVO.getInstanceIdList())
                .orderByDesc(ContractDO::getCreateTime)
                .distinct();
        return selectPage(reqVO, mpjLambdaWrapper);
    }

    default List<ContractDO> select4Trend(Map<String, Date> rsMap) {
        return selectList(new LambdaQueryWrapperX<ContractDO>()
                .select(ContractDO::getId,ContractDO::getAmount,ContractDO::getSignDate)
                .eq(ContractDO::getStatus,ContractStatusEnums.SIGN_COMPLETED.getCode())
                .between(ContractDO::getSignDate, rsMap.get(TREND_START_DATE),rsMap.get(TREND_END_DATE)));
    }

    default PageResult<ContractDO> selectDataPage(String loginUserId, ContractDataReqVO vo){
        return selectPage(vo,
                new LambdaQueryWrapperX<ContractDO>()
                        .select(ContractDO::getName,ContractDO::getId,ContractDO::getCode,ContractDO::getAmount,ContractDO::getUpload,ContractDO::getCreateTime,ContractDO::getStatus)
                        .eq(ContractDO::getCreator,loginUserId)
                        .ne(ContractDO::getStatus,ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode())
                        .orderByDesc(ContractDO::getCreateTime)
        );

    }

    default PageResult<ContractDO> selectDataPage4Seal(String loginUserId, ContractDataReqVO vo){
        MPJLambdaWrapper<ContractDO> mpjLambdaWrapper = new MPJLambdaWrapper<ContractDO>()
                .select(ContractDO::getName,ContractDO::getId,ContractDO::getCode,ContractDO::getCreateTime,ContractDO::getUpload,ContractDO::getAmount,ContractDO::getStatus)
                .leftJoin(BpmContractSignetDO.class, BpmContractSignetDO::getBusinessId, ContractDO::getId)
                .eq(ContractDO::getCreator,loginUserId)
                .orderByDesc(ContractDO::getCreateTime);
        return selectPage(vo,mpjLambdaWrapper);
    }

    default PageResult<ContractDO> selectDataPage4Pay(String loginUserId, ContractDataReqVO vo){
        MPJLambdaWrapper<ContractDO> mpjLambdaWrapper = new MPJLambdaWrapper<ContractDO>()
                .select(ContractDO::getName,ContractDO::getId,ContractDO::getCode,ContractDO::getCreateTime,ContractDO::getUpload,ContractDO::getAmount,ContractDO::getStatus)
                .leftJoin(PaymentApplicationDO.class, PaymentApplicationDO::getContractId, ContractDO::getId)
                .eq(PaymentApplicationDO::getCollectionType, CollectionTypeEnums.PAYMENT.getCode())
                .eq(ContractDO::getCreator,loginUserId)
                .orderByDesc(ContractDO::getCreateTime);
        return selectPage(vo,mpjLambdaWrapper);
    }

    default PageResult<ContractDO> selectDataPage4Collection(String loginUserId, ContractDataReqVO vo){
        MPJLambdaWrapper<ContractDO> mpjLambdaWrapper = new MPJLambdaWrapper<ContractDO>()
                .select(ContractDO::getName,ContractDO::getId,ContractDO::getCode,ContractDO::getCreateTime,ContractDO::getUpload,ContractDO::getAmount,ContractDO::getStatus)
                .leftJoin(PaymentApplicationDO.class, PaymentApplicationDO::getContractId, ContractDO::getId)
                .eq(PaymentApplicationDO::getCollectionType, CollectionTypeEnums.COLLECTION.getCode())
                .eq(ContractDO::getCreator,loginUserId)
                .orderByDesc(ContractDO::getCreateTime);
        return selectPage(vo,mpjLambdaWrapper);
    }




    default PageResult<ContractDO> contractPerform(ContractPerformReqVO vo, List<String> contractTypeIds, List<Integer> govUpload){
        LambdaQueryWrapperX<ContractDO> wrapperX = new LambdaQueryWrapperX<ContractDO>();
        wrapperX.select(ContractDO::getId,ContractDO::getStatus,ContractDO::getCode,ContractDO::getName,ContractDO::getSupplierId,ContractDO::getCreateTime,ContractDO::getAmount,ContractDO::getPartBName);
        if(1==vo.getSource()){
            wrapperX.in(ContractDO::getStatus,govUpload);
        }else {
            wrapperX.notIn(ContractDO::getStatus,govUpload);
        }
        wrapperX.in(ContractDO::getContractType,contractTypeIds)
                .orderByDesc(ContractDO::getCreateTime);
        return selectPage(vo,wrapperX);
    }
    default PageResult<ContractDO> selectSealPage(WorkBenchTaskReqVO reqVO) {
        if (CollectionUtils.isEmpty(reqVO.getInstanceIdList())) {
            return new PageResult<ContractDO>().setTotal(0L).setList(Collections.emptyList());
        }
        MPJLambdaWrapper<ContractDO> mpjLambdaWrapper = new MPJLambdaWrapper<ContractDO>()
                .leftJoin(BpmContractSignetDO.class, BpmContractSignetDO::getBusinessId, ContractDO::getId)
                .select(ContractDO::getId, ContractDO::getCode, ContractDO::getName, ContractDO::getAmount, ContractDO::getContractType, ContractDO::getProcessInstanceId, ContractDO::getCreateTime, ContractDO::getUpdateTime, ContractDO::getSubmitTime)
                .selectAs(BpmContractSignetDO::getProcessInstanceId, ContractDO::getAgentId)//实例id映射到代理id
                .selectAs(BpmContractSignetDO::getCreateTime, ContractDO::getSubmitTime)
                .in(BpmContractSignetDO::getProcessInstanceId, reqVO.getInstanceIdList())
                .orderByDesc(ContractDO::getCreateTime)
                .distinct();
        return selectPage(reqVO, mpjLambdaWrapper);
    }

    ;

    default List<ContractDO> selectAllSignedByTime(String startTime, String endTime) {

        List uploadList = new ArrayList();
        uploadList.add(ContractUploadTypeEnums.ORDER_DRAFT.getCode());
        uploadList.add(ContractUploadTypeEnums.THIRD_PARTY.getCode());
        LambdaQueryWrapper<ContractDO> queryWrapper = new LambdaQueryWrapperX<ContractDO>()
                .select(ContractDO::getId, ContractDO::getStatus, ContractDO::getAmount, ContractDO::getContractType)
                .and(wrapper -> wrapper.eq(ContractDO::getAmountType, AmountTypeEnums.PAY.getCode()).or(
                        subWrapper -> subWrapper.isNull(ContractDO::getAmountType).in(ContractDO::getUpload, uploadList)
                ))
                .in(ContractDO::getStatus, ContractStatusEnums.SIGN_COMPLETED.getCode(), ContractStatusEnums.PERFORMANCE_CLOSURE.getCode(), ContractStatusEnums.PERFORMING.getCode(), ContractStatusEnums.PERFORMANCE_COMPLETE.getCode()
                        , ContractStatusEnums.PERFORMANCE_RISK.getCode(), ContractStatusEnums.CONTRACT_CHANGE.getCode(), ContractStatusEnums.PERFORMANCE_RISK_DISPUTE.getCode(), ContractStatusEnums.PERFORMANCE_RISK_PAUSE.getCode(), ContractStatusEnums.PERFORMANCE_RISK_EXTENSION.getCode(), ContractStatusEnums.PERFORMANCE_RISK_OVERDUE.getCode());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (ObjectUtil.isNotEmpty(startTime) && ObjectUtil.isNotEmpty(endTime)) {
            queryWrapper.ge(ContractDO::getCreateTime, LocalDateTime.parse(startTime, formatter))
                    .lt(ContractDO::getCreateTime, LocalDateTime.parse(endTime, formatter));
        }
        return selectList(queryWrapper);
    }


    default PageResult<ContractDO> queryTransferContractAll(TransferContractAllReqVO reqVO,String relativeId) {
        Long loginUserId = WebFrameworkUtils.getLoginUserId();
        MPJLambdaWrapper<ContractDO> wrapperX = new MPJLambdaWrapper<ContractDO>()
                .leftJoin(SignatoryRelDO.class, SignatoryRelDO::getContractId, ContractDO::getId)
                .eq(SignatoryRelDO::getContactId, loginUserId)
                .eq(SignatoryRelDO::getSignatoryId, relativeId)
                .orderByDesc(ContractDO::getUpdateTime)
                .select(ContractDO::getId, ContractDO::getName, ContractDO::getSignDate,
                        ContractDO::getValidity1, ContractDO::getStatus, ContractDO::getPartBId,
                        ContractDO::getPartBName, ContractDO::getPartAId, ContractDO::getPartAName,
                        ContractDO::getSendTime,ContractDO::getUpload,ContractDO::getCreator);
        if (StringUtils.isNotEmpty(reqVO.getName())) {
            wrapperX.like(ContractDO::getName, reqVO.getName());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getStatusFlag()) && 1 == reqVO.getStatusFlag()) {
            // 1:进行中(4：待签署:5：已拒签，2：待确认（采购人已发送）+IsSign=0)
            wrapperX.in(ContractDO::getStatus, ContractStatusEnums.TO_BE_SIGNED.getCode(), ContractStatusEnums.SIGN_REJECTED.getCode(), ContractStatusEnums.SENT.getCode());
            wrapperX.eq(ContractDO::getIsSign, 0);
        }
        if (ObjectUtil.isNotEmpty(reqVO.getStatusFlag()) && 2 == reqVO.getStatusFlag()) {
            // 2：已完成（6已完成+IsSign=1）
            wrapperX.in(ContractDO::getStatus, ContractStatusEnums.SIGN_COMPLETED.getCode());
            wrapperX.eq(ContractDO::getIsSign, 1);
        }
        //已逾期
        if (ObjectUtil.isNotEmpty(reqVO.getStatusFlag()) && 3 == reqVO.getStatusFlag()) {

            wrapperX.and(wrapper -> wrapper
                    //超期待签订
                    .eq(ContractDO::getStatus, ContractStatusEnums.SIGN_OVERDUE.getCode())
                    .or(subWrapper -> subWrapper
                            //超期已签订
                            .apply("sign_date > validity1")
                            .eq(ContractDO::getIsSign, 1))
            );
        }
        return selectPage(reqVO, wrapperX);
    }

    default PageResult<ContractDO> selectPage(ContractPageReqVO reqVO) {
        LambdaQueryWrapperX<ContractDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.inIfPresent(ContractDO::getId,reqVO.getSumContractIds());
        wrapperX.likeIfPresent(ContractDO::getName, reqVO.getName());
        if (StringUtils.isNotBlank(reqVO.getName())) {
            wrapperX.or().like(ContractDO::getName, reqVO.getName())
                    .or().like(ContractDO::getCode, reqVO.getName());
        }
        wrapperX.orderByDesc(ContractDO::getCreateTime);
        return selectPage(reqVO, wrapperX);
    }
}
