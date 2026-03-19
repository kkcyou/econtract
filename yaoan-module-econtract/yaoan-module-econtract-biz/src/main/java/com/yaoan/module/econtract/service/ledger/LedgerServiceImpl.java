package com.yaoan.module.econtract.service.ledger;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.yulichang.query.MPJQueryWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.datapermission.core.rule.dept.DeptDataPermissionRule;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.expression.AndExpressionX;
import com.yaoan.framework.expression.OrExpressionX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.util.MyBatisUtils;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.bpm.api.bpm.activity.dto.BpmProcessRespDTO;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.BpmTaskAllInfoRespVO;
import com.yaoan.module.econtract.api.contract.dto.ContractFileDTO;
import com.yaoan.module.econtract.controller.admin.contract.vo.*;
import com.yaoan.module.econtract.controller.admin.ledger.vo.*;
import com.yaoan.module.econtract.controller.admin.ledger.vo.baseInfo.LedgerBaseInformationRespVO;
import com.yaoan.module.econtract.controller.admin.ledger.vo.baseInfo.RelativeManRespVO;
import com.yaoan.module.econtract.controller.admin.ledger.vo.document.AttachmentRespVO;
import com.yaoan.module.econtract.controller.admin.ledger.vo.document.FilingFileRespVO;
import com.yaoan.module.econtract.controller.admin.ledger.vo.document.LedgerContractDocumentRespVO;
import com.yaoan.module.econtract.controller.admin.ledger.vo.document.MainContractRespVO;
import com.yaoan.module.econtract.controller.admin.ledger.vo.flowable.ApproveResultRespVO;
import com.yaoan.module.econtract.controller.admin.ledger.vo.flowable.ApproveStarterRespVO;
import com.yaoan.module.econtract.controller.admin.ledger.vo.flowable.ApproverRespVO;
import com.yaoan.module.econtract.controller.admin.ledger.vo.flowable.LedgerFlowableRecordRespVO;
import com.yaoan.module.econtract.controller.admin.relative.vo.RelativeByUserRespVO;
import com.yaoan.module.econtract.convert.agreement.PrefAgreementRelConverter;
import com.yaoan.module.econtract.convert.contract.AttachmentRelConverter;
import com.yaoan.module.econtract.convert.contract.ContractConverter;
import com.yaoan.module.econtract.convert.contract.PaymentScheduleConverter;
import com.yaoan.module.econtract.convert.gcy.gpmall.ContractFileConverter;
import com.yaoan.module.econtract.convert.gcy.gpmall.ContractFileMapper;
import com.yaoan.module.econtract.convert.ledger.LedgerConverter;
import com.yaoan.module.econtract.convert.performance.perfTask.PerfTaskConverter;
import com.yaoan.module.econtract.convert.relation.IncidenceRelationConverter;
import com.yaoan.module.econtract.convert.term.ContractTermConverter;
import com.yaoan.module.econtract.dal.dataobject.agreement.PrefAgreementRelDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangeDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.model.ModelBpmDO;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import com.yaoan.module.econtract.dal.dataobject.category.ContractCategory;
import com.yaoan.module.econtract.dal.dataobject.contract.*;
import com.yaoan.module.econtract.dal.dataobject.contractarchives.ContractArchivesDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.ContractFileDO;
import com.yaoan.module.econtract.dal.dataobject.ledger.Ledger;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.dataobject.model.SimpleModel;
import com.yaoan.module.econtract.dal.dataobject.modelcategory.ModelCategory;
import com.yaoan.module.econtract.dal.dataobject.performance.contractPerformance.ContractPerformanceDO;
import com.yaoan.module.econtract.dal.dataobject.performance.perfTask.PerfTaskDO;
import com.yaoan.module.econtract.dal.dataobject.performtasktype.PerformTaskTypeDO;
import com.yaoan.module.econtract.dal.dataobject.relation.IncidenceRelation;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.dataobject.term.ContractTermDO;
import com.yaoan.module.econtract.dal.dataobject.terminate.TerminateContractDO;
import com.yaoan.module.econtract.dal.mysql.agreement.PrefAgreementRelMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.model.ModelBpmMapper;
import com.yaoan.module.econtract.dal.mysql.category.ContractCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.change.BpmContractChangeMapper;
import com.yaoan.module.econtract.dal.mysql.contract.*;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.ledger.LedgerMapper;
import com.yaoan.module.econtract.dal.mysql.model.ModelMapper;
import com.yaoan.module.econtract.dal.mysql.modelcategory.ModelCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.performTaskType.PerformTaskTypeMapper;
import com.yaoan.module.econtract.dal.mysql.performance.contractPerformance.ContractPerforMapper;
import com.yaoan.module.econtract.dal.mysql.performance.perforTask.PerforTaskMapper;
import com.yaoan.module.econtract.dal.mysql.relation.IncidenceRelationMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import com.yaoan.module.econtract.dal.mysql.terminate.TerminateContractMapper;
import com.yaoan.module.econtract.enums.*;
import com.yaoan.module.econtract.enums.change.ContractChangeTypeEnums;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import com.yaoan.module.econtract.enums.payment.MoneyTypeEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import com.yaoan.module.econtract.service.contract.ContractService;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import com.yaoan.module.system.api.dept.CompanyApi;
import com.yaoan.module.system.api.dept.dto.CompanyRespDTO;
import com.yaoan.module.system.api.dept.dto.DeptRespDTO;
import com.yaoan.module.system.api.dept.dto.UserCompanyInfoRespDTO;
import com.yaoan.module.system.api.permission.PermissionApi;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import dm.jdbc.util.StringUtil;
import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.enums.CommonStatusEnum.ENABLE;
import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.EMPTY_DATA_ERROR;

/**
 * @author Pele
 * @description 针对表【ecms_ledger(台账表)】的数据库操作Service实现
 */
@Service
public class LedgerServiceImpl extends ServiceImpl<LedgerMapper, Ledger> implements LedgerService {

    //主合同签署-双方
    public static final String PROCESS_KEY_BOTH = "ecms_contract_confirm_sign_both";
    //主合同签署-三方
    public static final String PROCESS_KEY_TRIPARTITE = "ecms_contract_confirm_sign_tripartite";
    static final String APPROVED_YES = "通过";
    @Resource
    private LedgerMapper ledgerMapper;
    @Resource
    private RelativeMapper relativeMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private AdminUserApi userApi;
    @Resource
    private CompanyApi companyApi;
    @Resource
    private FileApi fileApi;
    @Resource
    private ContractPerforMapper contractPerforMapper;
    @Resource
    private PrefAgreementRelMapper prefAgreementRelMapper;
    @Resource
    private TerminateContractMapper terminateContractMapper;
    @Resource
    private SignatoryRelMapper signatoryRelMapper;
    @Resource
    private PerfTaskConverter perfTaskConverter;
    @Resource
    private AttachmentRelMapper attachmentRelMapper;
    @Resource
    private PerforTaskMapper perforTaskMapper;
    @Resource
    private PerformTaskTypeMapper perfTaskTypeMapper;
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private ModelBpmMapper modelBpmMapper;
    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private IncidenceRelationMapper relationMapper;
    @Resource
    private DocumentRelMapper documentRelMapper;
    @Resource
    private ModelCategoryMapper modelCategoryMapper;
    @Resource
    private SimpleContractMapper simpleContractMapper;
    @Resource
    private BpmContractChangeMapper bpmContractChangeMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;

    @Resource
    private DeptDataPermissionRule rule;

    @Resource
    private PermissionApi permissionApi;

    @Resource
    private ContractFileMapper contractFileMapper;
    @Autowired
    private ContractService contractService;
    @Resource
    private ContractCategoryMapper contractCategoryMapper;

    /**
     * 台账-合同详情
     * */
    @Override
    public LedgerQueryRespVO queryById4Ledger(PrefRespVO prefRespVO) {
        //（相对方逻辑）免租户
//        AtomicReference<LedgerQueryRespVO> atomic = new AtomicReference<>();
//        DataPermissionUtils.executeIgnore(() -> {
//            TenantUtils.executeIgnore(() -> {
//
//                ContractDO contractDO = contractMapper.selectById(prefRespVO.getContractId());
//                if (contractDO == null) {
//                    throw exception(ErrorCodeConstants.CONTRACT_NAME_NOT_EXISTS);
//                }
//                ContractRespVO contractRespVO = ContractConverter.INSTANCE.convert(contractDO);
//                if (ObjectUtil.isNotEmpty(contractDO.getAmountType())) {
//                    contractRespVO.setAmountTypeName(AmountTypeEnums.getInstance(contractDO.getAmountType()) != null ? AmountTypeEnums.getInstance(contractDO.getAmountType()).getInfo() : null);
//                }
//
//                if (contractRespVO.getPlatform() != null) {
//                    contractRespVO.setPlatformName(PlatformEnums.getInstance(contractRespVO.getPlatform()).getInfo());
//                }
//                List<AttachmentRelRespVO> attachmentRelRespVOList = AttachmentRelConverter.INSTANCE.convert(attachmentRelMapper.selectList(AttachmentRelDO::getContractId, prefRespVO.getContractId()));
//                contractRespVO.setAttachmentList(attachmentRelRespVOList);
//
//                // 如果ecms_attachment_rel文件表里查不到数据，从ecms_neimeng_contract_file查询并返回
//                if (CollectionUtil.isEmpty(attachmentRelRespVOList)) {
//                    List<ContractFileDO> contractFileDOList = contractFileMapper.selectList(ContractFileDO::getContractId, prefRespVO.getContractId());
//                    List<ContractFileDTO> dtoList = ContractFileConverter.INSTANCE.toDTOS(contractFileDOList);
//                    contractRespVO.setAttachmentList(BeanUtils.toBean(dtoList, AttachmentRelRespVO.class));
//                }
//                //添加签署方信息
//                List<RelativeByUserRespVO> relativeByUserRespVOS = contractService.signatoryById(contractDO.getId());
//                //如果只有一个，需要供应商信息补全
//                if (1 == relativeByUserRespVOS.size()) {
//                    relativeByUserRespVOS.add(new RelativeByUserRespVO().setName(contractDO.getPartBName()).setId(contractDO.getPartBId()));
//                }
//                contractRespVO.setSignatoryList(relativeByUserRespVOS);
//                //合同分类、类型添加名称
//                Integer contractCategory = contractRespVO.getContractCategory();
//                if (ObjectUtil.isNotEmpty(contractCategory)) {
//                    ContractCategory category = contractCategoryMapper.selectById(contractRespVO.getContractCategory());
//                    contractRespVO.setContractCategoryName(category == null ? null : category.getName());
//                }
//                String contractType = contractRespVO.getContractType();
//                if (ObjectUtil.isNotEmpty(contractType)) {
//                    ContractType type = contractTypeMapper.selectById(contractRespVO.getContractType());
//                    contractRespVO.setContractTypeName(type == null ? null : type.getName());
//                }
//
//                //添加归档信息
//                if (contractDO.getDocument() != null && contractDO.getDocument() == 1) {
//                    //查询归档信息
//                    ContractArchivesDO contractArchivesDO = contractArchivesMapper.selectOne(ContractArchivesDO::getContractId, contractDO.getId());
//                    if (ObjectUtil.isNotEmpty(contractArchivesDO)) {
//                        contractRespVO.setDocumentName(adminUserApi.getUser(Long.valueOf(contractArchivesDO.getCreator())).getNickname());
//                        contractRespVO.setDocumentDate(contractDO.getUpdateTime());
//                        List<BusinessFileDO> businessFileDOS = businessFileMapper.selectList(BusinessFileDO::getBusinessId, contractArchivesDO.getId());
//                        if (ObjectUtil.isNotEmpty(businessFileDOS)) {
//                            contractRespVO.setDocumentAttachmentList(AttachmentRelConverter.INSTANCE.convertV2(businessFileDOS));
//                        }
//                        //封装借阅记录信息
//                        getBorrowList(contractArchivesDO, contractRespVO);
//                    }
//                }
//                //添加条款信息
//                List<ContractTermDO> contractTermDOList = contractTermMapper.selectList(new LambdaQueryWrapperX<ContractTermDO>().eq(ContractTermDO::getContractId, contractDO.getId()));
//                if (CollectionUtil.isNotEmpty(contractTermDOList)) {
//                    contractRespVO.setTerms(ContractTermConverter.INSTANCE.convertListVO(contractTermDOList));
//                }
//                atomic.set(contractRespVO);
//            });
//        });
//        LedgerQueryRespVO contractRespVO = atomic.get();
////        if (true) {
////            return contractRespVO;
////        }
//        //借阅记录中判断是否过期
//        if (ObjectUtil.isNotEmpty(prefRespVO.getRemainTime())) {
//            if ("已过期".equals(prefRespVO.getRemainTime()) || "未到借阅时间".equals(prefRespVO.getRemainTime())) {
//                return contractRespVO;
//            }
//        }
//        ContractDO contractDO = ContractConverter.INSTANCE.convert2Resp(contractRespVO);
//        if (contractDO.getStatus() != null && ContractStatusEnums.getInstance(contractDO.getStatus()) != null) {
//            contractRespVO.setStatusName(ContractStatusEnums.getInstance(contractDO.getStatus()).getDesc());
//        }
//
//        if (contractDO.getUpload() != 2) {
//        } else {
//            //TODO 通过上传合同创建
//
//        }
//        //添加补充协议数据
//        if (StringUtils.isNotBlank(prefRespVO.getPrefId())) {
//            List<PrefAgreementRelDO> prefAgreementRelDOS = prefAgreementRelMapper.selectList(PrefAgreementRelDO::getPrefId, prefRespVO.getPrefId());
//            if (ObjectUtils.isNotEmpty(prefAgreementRelDOS)) {
//                contractRespVO.setAgreements(PrefAgreementRelConverter.INSTANCE.convertList(prefAgreementRelDOS));
//            }
//        }
//        //添加终止合同信息
//        TerminateContractDO terminateContractDO = terminateContractMapper.selectOne(TerminateContractDO::getContractId, contractDO.getId());
//        if (ObjectUtils.isNotEmpty(terminateContractDO)) {
//            contractRespVO.setTerminationFileName(terminateContractDO.getFileName());
//            contractRespVO.setTerminationFileAddId(terminateContractDO.getFileAddId());
//        }
//        //添加履约任务信息集合
////        List<PerformanceTaskInfoRespVO> performanceTaskInfoRespVOList = perfTaskService.queryPerfTaskListById(prefRespVO.getPrefId());
////        if (CollectionUtil.isNotEmpty(performanceTaskInfoRespVOList)) {
////            contractRespVO.setPerformanceTaskInfoRespVOList(performanceTaskInfoRespVOList);
////        }
//        //添加主合同id，url
//        contractRespVO.setFileUrl(fileApi.getURL(contractRespVO.getFileAddId()));
//
//        //添加合同参数信息
//        List<ContractParameterDO> contractParameterDOList = contractParameterMapper.selectList(ContractParameterDO::getContractId, contractRespVO.getId());
//        if (CollectionUtil.isNotEmpty(contractParameterDOList)) {
//            contractRespVO.setContractParameterDOList(contractParameterDOList);
//        }
//        //添加合同章信息
//        List<ContractSealDO> contractSealDOList = contractSealMapper.selectList(ContractSealDO::getContractId, contractRespVO.getId());
//        if (CollectionUtil.isNotEmpty(contractSealDOList)) {
//            contractRespVO.setContractSealDOList(contractSealDOList);
//        }
//        //添加合同付款计划信息
//        List<PaymentScheduleDO> paymentScheduleDOList = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>()
//                .eq(PaymentScheduleDO::getContractId, contractRespVO.getId()).orderByAsc(PaymentScheduleDO::getSort));
//        if (CollectionUtil.isNotEmpty(paymentScheduleDOList)) {
//            List<PaymentScheduleRespVO> respVOList = PaymentScheduleConverter.INSTANCE.toRespVOList(paymentScheduleDOList);
//            for (PaymentScheduleRespVO respVO : respVOList) {
//                respVO.setMoneyTypeName(MoneyTypeEnums.getInstance(respVO.getMoneyType()).getInfo());
//                respVO.setAmountTypeName(AmountTypeEnums.getInstance(respVO.getAmountType()).getInfo());
//                if (ObjectUtil.isNotEmpty(respVO.getId())) {
//                    Relative relative = relativeMapper.selectById(respVO.getId());
//                    if (ObjectUtil.isNotEmpty(relative)) {
//                        if (ObjectUtil.isNotEmpty(relative.getCompanyName())) {
//                            respVO.setPayee(relative.getCompanyName());
//                        }
//                    }
//                    respVO.setStatusName(PaymentScheduleStatusEnums.getInstance(respVO.getStatus()).getInfo());
//                }
//            }
//            contractRespVO.setPaymentScheduleDOList(respVOList);
//        }
//        //添加合同采购内容信息
//        List<ContractPurchaseDO> contractPurchaseDOList = contractPurchaseMapper.selectList(ContractPurchaseDO::getContractId, contractRespVO.getId());
//        if (CollectionUtil.isNotEmpty(contractPurchaseDOList)) {
//            contractRespVO.setContractPurchaseDOList(contractPurchaseDOList);
//        }
//        //添加签定方信息 --- 内蒙需求
//        List<ContractSignatoryDO> contractSignatoryDOList = contractSignatoryMapper.selectList(ContractSignatoryDO::getContractId, contractRespVO.getId());
//        if (CollectionUtil.isNotEmpty(contractSignatoryDOList)) {
//            contractRespVO.setContractSignatoryDOList(contractSignatoryDOList);
//        }
//
//        //封装签署文件信息
//        contractRespVO.setContractInfo(new ContractInfoVO().setFileName(contractRespVO.getFileName()).setFileAddId(contractRespVO.getFileAddId()).setPdfFileId(contractRespVO.getPdfFileId()).setTemplateId(contractRespVO.getTemplateId()));
//        //文件名称托底
//        if (StringUtils.isBlank(contractRespVO.getFileName())) {
//            contractRespVO.setFileName(contractRespVO.getName() + ".pdf");
//        }
//        SimpleModel model = simpleModelMapper.selectById(contractRespVO.getTemplateId());
//        if (ObjectUtil.isNotNull(model)) {
//            contractRespVO.getContractInfo().setType(model.getType());
//        }
//        contractRespVO.setBuyPlanCode("无");
//
//
//        //封装合同变动信息
//        List<BpmContractChangeDO> bpmContractChangeDOS = bpmContractChangeMapper.selectList(BpmContractChangeDO::getMainContractId, contractRespVO.getId());
//        if (CollectionUtil.isNotEmpty(bpmContractChangeDOS)) {
//            contractRespVO.setContractChangeList(bpmContractChangeDOS);
//        }
//
//        //签署流程信息
//        String processInstanceId = contractDO.getProcessInstanceId();
//        if (StringUtils.isNotEmpty(processInstanceId)) {
//            List<BpmProcessRespDTO> activityRecord = bpmActivityApi.getActivityRecord(processInstanceId);
//            if (CollectionUtil.isNotEmpty(activityRecord)) {
//                contractRespVO.setBpmProcessRespDTOList(activityRecord);
//            }
//        }
//        //返回office类型合同的文件id
//        if (contractDO.getEditType() != null && 1 == contractDO.getEditType()) {
//            contractRespVO.setFileAddId(contractDO.getFileAddId());
//        }
//
//        //获取当前用户信息
//        AdminUserRespDTO userDO = adminUserApi.getUser(SecurityFrameworkUtils.getLoginUserId());
//        if (ObjectUtil.isNotEmpty(userDO)) {
//            //获取部门信息
//            DeptRespDTO dept = deptApi.getDept(userDO.getDeptId());
//            if (ObjectUtil.isNotEmpty(dept)) {
//                contractRespVO.setDeptId(dept.getId());
//                contractRespVO.setDeptName(dept.getName());
//            }
//        }
//
//        return contractRespVO;
        return null;
    }




    @Override
    public PageResult<LedgerPageRespVo> getLedgerPage(LedgerListReqVo vo) {
        PageResult<Ledger> ledgerPageResult = ledgerMapper.selectPage(vo, new LambdaQueryWrapperX<Ledger>()
                .orderByDesc(Ledger::getCreateTime)
                //合同类型
                .eqIfPresent(Ledger::getContractType, vo.getContractType())
                //合同状态
                .eqIfPresent(Ledger::getContractStatus, vo.getContractStatus())
                //合同名称
                .likeIfPresent(Ledger::getName, vo.getName()));

        PageResult<LedgerPageRespVo> result = LedgerConverter.INSTANCE.convertPage(ledgerPageResult);

        if (CollectionUtil.isEmpty(result.getList())) {
            return result;
        }
        List<String> contractIdlist = result.getList().stream().map(LedgerPageRespVo::getContractId).collect(Collectors.toList());
        List<String> contractTypeIds = ledgerPageResult.getList().stream().map(Ledger::getContractType).collect(Collectors.toList());
        List<String> contractCodes = ledgerPageResult.getList().stream().map(Ledger::getCode).collect(Collectors.toList());
        List<ContractType> contractTypeInfos = contractTypeMapper.selectBatchIds(contractTypeIds);
        Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypeInfos, ContractType::getId);

        List<Relative> relatives = relativeMapper.selectList();
        Map<String, Relative> relativeMap = CollectionUtils.convertMap(relatives, Relative::getId);

        List<ContractDO> contractDOList = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>()
                .inIfPresent(ContractDO::getCode, contractCodes)
                //排除富文本字段
                .select(ContractDO.class, info ->
                        !"contract_content".equals(info.getColumn()))

        );
        Map<String, ContractDO> contractMap = CollectionUtils.convertMap(contractDOList, ContractDO::getCode);

        //合同变动的信息
        List<BpmContractChangeDO> bpmContractChangeDOList = bpmContractChangeMapper.selectList(new LambdaQueryWrapperX<BpmContractChangeDO>()
                .inIfPresent(BpmContractChangeDO::getMainContractId, contractIdlist)
        );

        Map<String, List<BpmContractChangeDO>> groupedByMainContractId = bpmContractChangeDOList.stream()
                .collect(Collectors.groupingBy(BpmContractChangeDO::getMainContractId));
        result.getList().forEach(item -> {
            // （暂定的）获取最新合同数据
            ContractDO contractDO = contractMap.get(item.getCode());
            if (contractDO != null) {
                //（暂定的）批量赋值合同状态
                ContractStatusEnums instance = ContractStatusEnums.getInstance(contractDO.getStatus());
                if (instance != null) {
                    item.setContractStatus(contractDO.getStatus());
                    item.setContractStatusStr(instance.getDesc());
                }
            }
//            //批量赋值合同状态
//            ContractStatusEnums instance = ContractStatusEnums.getInstance(item.getContractStatus());
//            if (instance != null) {
//                item.setContractStatusStr(instance.getDesc());
//            }
            //批量赋值合同类型名称
            ContractType contractType = contractTypeMap.get(item.getContractType());
            if (contractType != null) {
                item.setContractTypeName(contractType.getName());
            }
            //批量赋值向对方名称
            Relative relative = relativeMap.get(item.getCounterparty());
            if (relative != null) {
                item.setCounterpartyName(relative.getName());
            }
            //变更次数
            if (CollectionUtil.isNotEmpty(groupedByMainContractId.get(item.getContractId()))) {
                Integer changeCount = groupedByMainContractId.get(item.getContractId()).size();
                item.setChangeCount(changeCount);
            }
        });

        return result;
    }

    /**
     * 基本信息
     */
    @Override
    public LedgerBaseInformationRespVO ledgerBaseInformation(LedgerContractIdReqVO vo) {

        LedgerBaseInformationRespVO respVO = new LedgerBaseInformationRespVO();
        String contractId = vo.getContractId();
        // 合同
        ContractDO contractDO = contractMapper.selectById(contractId);
        if (contractDO == null) {
            return new LedgerBaseInformationRespVO();
        }
        // 相对方
        MPJQueryWrapper<Relative> queryWrapper = new MPJQueryWrapper<Relative>().selectAll(Relative.class)
                .leftJoin("ecms_signatory_rel s on s.contract_id = t.id");
        // TODO 存在sql注入风险
        queryWrapper.inSql("t.id", " SELECT s.signatory_id FROM ecms_signatory_rel s WHERE s.deleted = 0 AND s.contract_id = " + "'" + contractId + "'");

        List<Relative> relativeList = relativeMapper.selectList(queryWrapper);
        List<RelativeManRespVO> relativeManRespVOS = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(relativeList)) {
            relativeList.forEach(
                    relative -> {
                        RelativeManRespVO relativeManRespVO = new RelativeManRespVO();
                        CompanyRespDTO companyRespDTO = companyApi.getCompany(relative.getRelativeCompanyId(), ENABLE.getStatus());
                        String yourCompanyName = companyRespDTO == null ? "" : companyRespDTO.getName();
                        relativeManRespVO.setDeptName(yourCompanyName);
                        relativeManRespVO.setName(relative.getName());
                        relativeManRespVO.setTel(relative.getContactTel());
                        relativeManRespVOS.add(relativeManRespVO);
                    }
            );
        }

        respVO.setContractName(contractDO.getName());
        respVO.setContractCode(contractDO.getCode());
        ContractType contractType = contractTypeMapper.selectById(contractDO.getContractType());
        if (ObjectUtil.isNotNull(contractType)) {
            respVO.setContractTypeStr(contractType.getName());
        }
        ContractStatusEnums contractStatusEnums = ContractStatusEnums.getInstance(contractDO.getStatus());
        respVO.setContractStatus(contractStatusEnums.getDesc());

        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        AdminUserRespDTO loginUser = userApi.getUser(loginUserId);
        if (loginUser != null) {
            respVO.setMyCompanyManTel(loginUser.getMobile());
            respVO.setMyrCompanyManName(loginUser.getNickname());
        }

//        respVO.setMyCompanyName(contractDO.getMySignatory());
        respVO.setContractSignTime(contractDO.getSignDate());
        respVO.setRelativeManList(relativeManRespVOS);
        respVO.setContractSignTime(contractDO.getSignDate());
        return respVO;
    }

    /**
     * 合同文件
     */
    @Override
    public LedgerContractDocumentRespVO ledgerContractDocument(LedgerContractIdReqVO vo) {
        List<AttachmentRespVO> attachmentRespVOList = new ArrayList<>();
        MainContractRespVO mainContractRespVO = new MainContractRespVO();

        String contractId = vo.getContractId();
        ContractDO contractDO = contractMapper.selectById(contractId);
        if (contractDO == null) {
            return new LedgerContractDocumentRespVO();
        }

        //主文件
        FileDTO fileDTO = fileApi.selectById(contractDO.getPdfFileId());
        if (ObjectUtil.isNotNull(fileDTO)) {

            mainContractRespVO.setFileName(fileDTO.getName());
            mainContractRespVO.setFileId(fileDTO.getId());
            mainContractRespVO.setName(cleanSuffix(fileDTO.getName()));
            mainContractRespVO.setFileUrl(fileDTO.getUrl());

        }
        //附件
        List<AttachmentRelDO> attachmentRelDOList = attachmentRelMapper.selectList(new LambdaQueryWrapperX<AttachmentRelDO>().eq(AttachmentRelDO::getContractId, contractId));

        List<Long> attachmentFileIdList = attachmentRelDOList.stream()
                .map(AttachmentRelDO::getAttachmentAddId)
                .collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(attachmentFileIdList)) {
            //找到所有附件文件
            List<FileDTO> files = fileApi.selectBatchIds(attachmentFileIdList);
            Map<Long, FileDTO> attachmentFileDTOMap = CollectionUtils.convertMap(files, FileDTO::getId);
            attachmentRelDOList.forEach(attachmentRelDO -> {
                AttachmentRespVO attachmentRespVO = new AttachmentRespVO();
                FileDTO attachFile = attachmentFileDTOMap.get(attachmentRelDO.getAttachmentAddId());
                if (ObjectUtil.isNotNull(attachFile)) {
                    attachmentRespVO.setFileUrl(attachFile.getUrl());
                    attachmentRespVO.setFileName(attachFile.getName());
                }
                attachmentRespVO.setFileId(attachmentRelDO.getAttachmentAddId());
                attachmentRespVO.setName(attachmentRelDO.getAttachmentName());
                attachmentRespVOList.add(attachmentRespVO);
            });
        }

        //归档文件
        FilingFileRespVO filingFileRespVO = new FilingFileRespVO();
        DocumentRelDO documentRelDO = documentRelMapper.selectOne(new LambdaQueryWrapperX<DocumentRelDO>().eq(DocumentRelDO::getContractId, contractId));
        if (documentRelDO != null) {
            FileDTO filingFile = fileApi.selectById(documentRelDO.getAddId());
            filingFileRespVO.setFileId(filingFile.getId());
            filingFileRespVO.setFileName(filingFile.getName());
            filingFileRespVO.setFileUrl(filingFile.getUrl());
            filingFileRespVO.setFileName(cleanSuffix(filingFile.getName()));
        }

        LedgerContractDocumentRespVO result = new LedgerContractDocumentRespVO();
        result.setMainContract(mainContractRespVO);
        result.setAttachmentRespVOList(attachmentRespVOList);
        result.setFilingFileRespVO(filingFileRespVO);
        return result;
    }

    /**
     * 履约任务
     */
    @Override
    public PageResult<LedgerPerformTaskReRespVO> ledgerPerformTask(LedgerContractIdPageReqVO vo) {
        ContractPerformanceDO contractPerformanceDO = contractPerforMapper.selectOne(new LambdaQueryWrapperX<ContractPerformanceDO>().eqIfPresent(ContractPerformanceDO::getContractId, vo.getContractId()));
        PageResult<LedgerPerformTaskReRespVO> taskInfoRespVOs = null;
        if (BeanUtil.isNotEmpty(contractPerformanceDO)) {
            PageResult<PerfTaskDO> perfTaskDOPageResult = perforTaskMapper.selectPage(vo, new LambdaQueryWrapperX<PerfTaskDO>().eq(PerfTaskDO::getContractPerfId, contractPerformanceDO.getId()));
            //1。将实体类转换成vo
            taskInfoRespVOs = perfTaskConverter.toVOList2(perfTaskDOPageResult);
            for (LedgerPerformTaskReRespVO perfTaskvo : taskInfoRespVOs.getList()) {
                //2.设置履约任务类型名称,履约人名称
                setPerformanceTaskInfoRespVO(perfTaskvo);
            }
        }
        return taskInfoRespVOs;
    }

    private void setPerformanceTaskInfoRespVO(LedgerPerformTaskReRespVO taskInfoRespVO) {
        //1.设置履约任务类型名称
        if (StringUtil.isNotEmpty(taskInfoRespVO.getPerfTaskTypeId())) {
            PerformTaskTypeDO performTaskTypeDO = perfTaskTypeMapper.selectById(taskInfoRespVO.getPerfTaskTypeId());
            taskInfoRespVO.setPerfTaskTypeName(performTaskTypeDO == null ? null : performTaskTypeDO.getName());
        }
        //2.设置履约人名称
        if (StringUtil.isNotEmpty(taskInfoRespVO.getConfirmer())) {
            taskInfoRespVO.setConfirmerName(getUserName(Long.valueOf(taskInfoRespVO.getConfirmer())));
        }
    }

    private String getUserName(Long id) {
        //5.设置创建人名称
        AdminUserRespDTO user = userApi.getUser(id);
        return user.getNickname();
    }

    /**
     * 模板信息
     */
    @Override
    public LedgerModelInfoRespVO ledgerModelInfo(LedgerContractIdPageReqVO vo) {
        LedgerModelInfoRespVO result = new LedgerModelInfoRespVO();
        String contractId = vo.getContractId();
        ContractDO contractDO = contractMapper.selectById(contractId);
        MPJQueryWrapper<Model> queryWrapper = new MPJQueryWrapper<Model>().selectAll(Model.class)
                .leftJoin("ecms_contract c on c.template_id = t.id");
        // TODO 存在sql注入风险
        queryWrapper.inSql("c.id", " SELECT c.id FROM ecms_contract c WHERE c.deleted = 0 AND c.id = " + "'" + contractId + "'");

        Model model = modelMapper.selectOne(queryWrapper);
        if (ObjectUtil.isNull(model)) {
            return new LedgerModelInfoRespVO();
        }
        result.setModelName(model.getName());
        result.setModelCode(model.getCode());
        result.setModelCategoryId(model.getCategoryId());
        ModelCategory modelCategory = modelCategoryMapper.selectOne(new LambdaQueryWrapperX<ModelCategory>().eq(ModelCategory::getId, model.getCategoryId()));
        if (ObjectUtil.isNotNull(modelCategory)) {
            result.setModelCategoryStr(modelCategory.getName());
        }

        result.setModelCreateTime(model.getCreateTime());

        //时效模式
        ModelEffectEnum effectEnum = ModelEffectEnum.getInstance(model.getTimeEffectModel());
        result.setModelEffectStr(effectEnum.getInfo());

        //合同有效期
        String effectPeriod = "";
        //如果是长期有效
        if (effectEnum == ModelEffectEnum.LIMITED_TIME_EFFECT) {
            effectPeriod = String.valueOf(model.getEffectStartTime()) + " —— " + String.valueOf(model.getEffectEndTime());
        }
        if (effectEnum == ModelEffectEnum.FOREVER_TIME_EFFECT) {
            effectPeriod = "长期有效";
        }
        result.setEffectPeriodStr(effectPeriod);

        return result;
    }

    /**
     * 关联合同
     */
    @Override
    public List<LedgerRelationContractRespVO> ledgerRelationContract(LedgerContractIdReqVO vo) throws Exception {
        //当该合同为正常签署的一份合同，非框架协议、非其他合同的变更协议及续签合同时有合同续签协议，框架协议，补充协议，合同终止（或合同变更），其他协议
        //1.根据合同id查看该合同 的所有关联合同信息
        List<IncidenceRelation> incidenceRelations = relationMapper.selectList(new LambdaQueryWrapperX<IncidenceRelation>().eq(IncidenceRelation::getContractId, vo.getContractId()));
        List<LedgerRelationContractRespVO> incidenceRelationResplistVOS = IncidenceRelationConverter.INSTANCE.tolistVo2(incidenceRelations);
        List<String> contractIds = incidenceRelations.stream().map(IncidenceRelation::getRelationContractId).collect(Collectors.toList());
        //查询合同信息
        Map<String, ContractDO> contractPerfMap = getContractPerfMap(contractIds);
        Map<String, ContractType> contractTypeMap = getContractTypeMap(contractIds);
        //校验该合同是否为其他合同的框架协议、变更协议及续签协议
        Long count = relationMapper.selectCount2(vo.getContractId());
        incidenceRelationResplistVOS.forEach(item -> item.setContractName(contractPerfMap.get(item.getRelationContractId()) == null ? null : contractPerfMap.get(item.getRelationContractId()).getName()));
        //设置我方签约主体和相对方签约主体
        for (LedgerRelationContractRespVO incidenceRelationResplistVO : incidenceRelationResplistVOS) {
            setSignatoryListInfo(incidenceRelationResplistVO, incidenceRelationResplistVO.getRelationContractId(), contractPerfMap);
            ContractDO contractDO = contractPerfMap.get(incidenceRelationResplistVO.getRelationContractId());
            if (BeanUtil.isNotEmpty(contractDO)) {
                incidenceRelationResplistVO.setContractTypeName(contractTypeMap.get(contractDO.getContractType()) == null ? null : contractTypeMap.get(contractDO.getContractType()).getName());
            }
        }
        if (count > 0) {
            //查看为其他合同的框架协议，变更协议，合同续签时的合同信息及关联关系
            List<IncidenceRelation> incidenceRelations1 = relationMapper.selectList(new LambdaQueryWrapperX<IncidenceRelation>().eq(IncidenceRelation::getRelationContractId, vo.getContractId()));
            List<LedgerRelationContractRespVO> incidenceRelationResplistVOS1 = IncidenceRelationConverter.INSTANCE.tolistVo2(incidenceRelations1);
            List<String> contractIds1 = incidenceRelations1.stream().map(IncidenceRelation::getContractId).collect(Collectors.toList());
            Map<String, ContractDO> contractPerfMap1 = getContractPerfMap(contractIds1);
            Map<String, ContractType> contractTypeMap1 = getContractTypeMap(contractIds1);
            for (LedgerRelationContractRespVO incidenceRelationvo : incidenceRelationResplistVOS1) {
                ContractDO contractDO = contractPerfMap1.get(incidenceRelationvo.getContractId());
                if (BeanUtil.isNotEmpty(contractDO)) {
                    incidenceRelationvo.setContractName(contractDO.getName());
                    incidenceRelationvo.setContractTypeName(contractTypeMap1.get(contractDO.getContractType() == null ? null : contractDO.getContractType()).getName());
                    setSignatoryListInfo(incidenceRelationvo, incidenceRelationvo.getContractId(), contractPerfMap1);
                }

                //为其他合同的框架协议,关系为子合同
                if (IncidenceRelationEnums.OUTLINE_AGREEMENT.getCode().equals(incidenceRelationvo.getIncidenceRelation())) {
                    incidenceRelationvo.setIncidenceRelation(IncidenceRelationEnums.SON_CONTRACT.getCode());
                }
                //为其他合同的续签合同或者变更合同
                if (IncidenceRelationEnums.CONTRACT_RENEWAL_AGREEMENT.getCode().equals(incidenceRelationvo.getIncidenceRelation()) || IncidenceRelationEnums.CONTRACT_CHANGE_AGREEMENT.getCode().equals(incidenceRelationvo.getIncidenceRelation())) {
                    incidenceRelationvo.setIncidenceRelation(IncidenceRelationEnums.PARENT_CONTRACT.getCode());
                }
                incidenceRelationResplistVOS.add(incidenceRelationvo);
            }
        }
        //查看是否有补充文件，有则有补充协议
        ContractPerformanceDO contractPerformanceDO = contractPerforMapper.selectOne(new LambdaQueryWrapperX<ContractPerformanceDO>()
                .eqIfPresent(ContractPerformanceDO::getContractId, vo.getContractId())
                .eqIfPresent(ContractPerformanceDO::getCreator, WebFrameworkUtils.getLoginUserId()));
        if (BeanUtil.isNotEmpty(contractPerformanceDO)) {
            List<PrefAgreementRelDO> prefAgreementRelDOS = prefAgreementRelMapper.selectList(new LambdaQueryWrapperX<PrefAgreementRelDO>().eqIfPresent(PrefAgreementRelDO::getPrefId, contractPerformanceDO.getId()));
            if (prefAgreementRelDOS.size() > 0) {
                //有合同补充协议，添加合同补充协议
                for (PrefAgreementRelDO prefAgreementRelDO : prefAgreementRelDOS) {
                    LedgerRelationContractRespVO incidenceRelationResplistVO = new LedgerRelationContractRespVO();
                    incidenceRelationResplistVO.setIncidenceRelation(IncidenceRelationEnums.CONTRACT_REPLENISH_AGREEMENT.getCode());
                    incidenceRelationResplistVO.setContractName("-");
                    incidenceRelationResplistVO.setCreateTime(prefAgreementRelDO.getCreateTime());
                    String url = fileApi.getURL(prefAgreementRelDO.getInfraFileId());
                    incidenceRelationResplistVO.setFileId(prefAgreementRelDO.getInfraFileId());
                    incidenceRelationResplistVO.setFileName(prefAgreementRelDO.getFileName());
                    incidenceRelationResplistVO.setFileurl(url);
                    incidenceRelationResplistVOS.add(incidenceRelationResplistVO);
                }
            }
        }
        //新增合同终止协议，返回文件名和文件路径，文件id
        ContractDO contractDO = contractMapper.selectOne(new LambdaQueryWrapperX<ContractDO>().eqIfPresent(ContractDO::getId, vo.getContractId()));
        if (BeanUtil.isNotEmpty(contractDO) && ContractStatusEnums.TERMINATED.getCode().equals(contractDO.getStatus())) {
            TerminateContractDO terminateContractDO = terminateContractMapper.selectOne(new LambdaQueryWrapperX<TerminateContractDO>().eqIfPresent(TerminateContractDO::getContractId, vo.getContractId()));
            if (BeanUtil.isNotEmpty(terminateContractDO)) {
                LedgerRelationContractRespVO incidenceRelationResplistVO = new LedgerRelationContractRespVO();
                //文件ID
                incidenceRelationResplistVO.setIncidenceRelation(IncidenceRelationEnums.CONTRACT_STOP_AGREEMENT.getCode());
                incidenceRelationResplistVO.setContractName("-");
                String url = fileApi.getURL(terminateContractDO.getFileAddId());
                incidenceRelationResplistVO.setFileId(terminateContractDO.getFileAddId());
                incidenceRelationResplistVO.setFileName(terminateContractDO.getFileName());
                incidenceRelationResplistVO.setFileurl(url);
                incidenceRelationResplistVO.setCreateTime(terminateContractDO.getCreateTime());
                incidenceRelationResplistVOS.add(incidenceRelationResplistVO);
            }
        }
        //列表按照关联时间，倒叙排列；
        Collections.sort(incidenceRelationResplistVOS, new Comparator<LedgerRelationContractRespVO>() {
            @Override
            public int compare(LedgerRelationContractRespVO u1, LedgerRelationContractRespVO u2) {
                // 注意这里使用了降序排序，即倒序
                return u2.getCreateTime().compareTo(u1.getCreateTime());
            }
        });
        return incidenceRelationResplistVOS;
    }

    /**
     * 流程记录
     */
    @Override
    public LedgerFlowableRecordRespVO ledgerFlowableRecord(LedgerContractIdReqVO vo) {
        String adviceStr = "";
        String contractId = vo.getContractId();
        List<AdminUserRespDTO> userList = userApi.getUserList();
        Map<Long, AdminUserRespDTO> userMap = CollectionUtils.convertMap(userList, AdminUserRespDTO::getId);
        ContractDO contractDO = contractMapper.selectById(contractId);
        if (contractDO == null) {
            return new LedgerFlowableRecordRespVO();
        }
        //发起人
        ApproveStarterRespVO starterRespVO = new ApproveStarterRespVO();
        //找到相应模板创建时间最近的一条流程记录（因为同一个模板可以多次审批，暂定选一个最近的）
        ModelBpmDO modelBpmDO = modelBpmMapper.selectOne(new LambdaQueryWrapperX<ModelBpmDO>()
                .eq(ModelBpmDO::getModelId, contractDO.getTemplateId())
                .orderByDesc(ModelBpmDO::getCreateTime).last("LIMIT 1"));
        if (modelBpmDO != null) {
            starterRespVO.setStartTime(modelBpmDO.getCreateTime());
            AdminUserRespDTO starterUserDO = userMap.get(modelBpmDO.getUserId());
            if (starterUserDO != null) {
                starterRespVO.setName(starterUserDO.getNickname());
            }
        }

        //审批人list
        List<ApproverRespVO> approverRespVO = new ArrayList<>();
        //找到模板
        Model model = modelMapper.selectOne(new LambdaQueryWrapperX<Model>().eq(Model::getId, contractDO.getTemplateId()));
        if (model != null) {
            //找到模板的审批流程
            // 找到合同创建时间之前，
            Long userId = Long.valueOf(model.getCreator());
//        List<BpmTaskAllInfoRespVO> taskList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceId(userId, modelBpmDO.getProcessInstanceId());
            // 模板相关流程信息，
            List<ModelBpmDO> modelBpmDOList = modelBpmMapper.selectList(new LambdaQueryWrapperX<ModelBpmDO>()
                    .orderByDesc(ModelBpmDO::getCreateTime)
                    .eq(ModelBpmDO::getModelId, model.getId()));
            if (CollectionUtil.isNotEmpty(modelBpmDOList)) {
                ModelBpmDO latestModelBpm = modelBpmDOList.get(0);
                // 通过实例找到相关任务，
                List<String> instanceIds = new ArrayList<String>();
                instanceIds.add(latestModelBpm.getProcessInstanceId());
                List<BpmTaskAllInfoRespVO> taskList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(userId, instanceIds);
                // 找到任务的审批人和审批时间
                if (CollectionUtil.isEmpty(taskList)) {
                    throw exception(EMPTY_DATA_ERROR);
                }

                for (BpmTaskAllInfoRespVO t : taskList) {
                    adviceStr = adviceStr + " 审批意见： " + t.getReason();
                    ApproverRespVO approverResp = new ApproverRespVO();
                    AdminUserRespDTO userRespDTO = userApi.getUser(t.getAssigneeUserId());
                    if (userRespDTO != null) {
                        // 节点审批人姓名
                        approverResp.setApproveName(userRespDTO.getNickname());
                    }
                    // 节点审批时间
                    approverResp.setApproveTime(t.getEndTime());
                    approverRespVO.add(approverResp);
                }
            }
        }
        //审批结果
        ApproveResultRespVO resultRespVO = new ApproveResultRespVO();
        //一定是通过
        resultRespVO.setApproveResult(APPROVED_YES);
        resultRespVO.setApproveAdvice(adviceStr);

        return new LedgerFlowableRecordRespVO().setStarter(starterRespVO).setApproverRespVO(approverRespVO).setResultRespVO(resultRespVO);
    }

    String cleanSuffix(String fileName) {
        String[] parts = fileName.split("\\.");
        String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf("."));
        return nameWithoutExtension;
    }

    private Map<String, ContractDO> getContractPerfMap(List<String> contractIds) {
        //查询合同信息
        List<ContractDO> contractDOS = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>().inIfPresent(ContractDO::getId, contractIds));
        return CollectionUtils.convertMap(contractDOS, ContractDO::getId);
    }

    private Map<String, ContractType> getContractTypeMap(List<String> contractIds) {
        //查询合同信息
        List<ContractDO> contractDOS = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>().inIfPresent(ContractDO::getId, contractIds));
        List<String> contractTypeIds = contractDOS.stream().map(ContractDO::getContractType).collect(Collectors.toList());
        List<ContractType> contractTypeDOS = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>().inIfPresent(ContractType::getId, contractTypeIds));
        return CollectionUtils.convertMap(contractTypeDOS, ContractType::getId);
    }

    private void setSignatoryListInfo(LedgerRelationContractRespVO ledgerRelationContractRespVO, String id, Map<String, ContractDO> contractPerfMap) {
        //7.设置签署方
        ArrayList<SignatoryRespVO> signatoryRespVOList = new ArrayList<>();

        if (contractPerfMap.get(id).getUpload() == 0) {
            //7.1 通过草拟创建合同
            List<String> signatoryNameList = signatoryNameById(id);
            for (String s : signatoryNameList) {
                signatoryRespVOList.add(new SignatoryRespVO().setSignatory(s));
            }
            ledgerRelationContractRespVO.setSignatoryList(signatoryRespVOList);
        } else {
            //7.2 TODO 通过上传合同创建

        }
    }

    //根据合同id 获取签署方名称
    private List<String> signatoryNameById(String id) {
        //获取签署方id集合
        List<String> ids = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, id).stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toList());
        List<Relative> relativeList = relativeMapper.selectBatchIds(ids);
        List<String> signatoryNameList = new ArrayList<>();
        //添加发起方公司名称
        ArrayList<Long> longs = new ArrayList<>();
        longs.add(Long.valueOf(contractMapper.selectById(id).getCreator()));
        List<UserCompanyInfoRespDTO> userCompanyInfoList = companyApi.getUserCompanyInfoList(longs);
        if (StringUtils.isNotBlank(userCompanyInfoList.get(0).getName())) {
            signatoryNameList.add(userCompanyInfoList.get(0).getName());
        } else {
            //发起方是个体，通过id 获取nickname
            List<AdminUserRespDTO> userListByDeptIds = userApi.getUserListByDeptIds(userCompanyInfoList.stream().map(UserCompanyInfoRespDTO::getUserId).collect(Collectors.toList()));
            signatoryNameList.add(userListByDeptIds.get(0).getNickname());
        }
        for (Relative relative : relativeList) {
            if (!relative.getCompanyName().isEmpty()) {
                signatoryNameList.add(relative.getCompanyName());
            } else {
                signatoryNameList.add(relative.getName());
            }
        }
        return signatoryNameList;
    }


    @Override
    public PageResult<LedgerPageRespVo> listLedgerFromContract(LedgerListReqVo vo) {
        PageResult<LedgerPageRespVo> result = new PageResult<LedgerPageRespVo>();
        PageResult<SimpleContractDO> contractDOPage = simpleContractMapper.listLedgerFromContract(vo);
        if (CollectionUtil.isEmpty(contractDOPage.getList())) {
            return result;
        }
        result = LedgerConverter.INSTANCE.convertRespPage(contractDOPage);
        List<String> contractIdList = contractDOPage.getList().stream().map(SimpleContractDO::getId).collect(Collectors.toList());
        List<String> contractTypeIds = contractDOPage.getList().stream().map(SimpleContractDO::getContractType).collect(Collectors.toList());
        List<String> contractCodes = contractDOPage.getList().stream().map(SimpleContractDO::getCode).collect(Collectors.toList());
        List<ContractType> contractTypeInfos = contractTypeMapper.selectBatchIds(contractTypeIds);
        Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypeInfos, ContractType::getId);

        List<Relative> relatives = relativeMapper.selectList();
        Map<String, Relative> relativeMap = CollectionUtils.convertMap(relatives, Relative::getId);

        List<ContractDO> contractDOList = contractMapper.selectList(new LambdaQueryWrapperX<ContractDO>()
                .inIfPresent(ContractDO::getCode, contractCodes)
                //排除富文本字段
                .select(ContractDO.class, info ->
                        !"contract_content".equals(info.getColumn()))

        );
        Map<String, ContractDO> contractMap = CollectionUtils.convertMap(contractDOList, ContractDO::getCode);

        //合同变动的信息
        List<BpmContractChangeDO> bpmContractChangeDOList = bpmContractChangeMapper.selectList(new LambdaQueryWrapperX<BpmContractChangeDO>()
                .inIfPresent(BpmContractChangeDO::getMainContractId, contractIdList)
        );

        Map<String, List<BpmContractChangeDO>> groupedByMainContractId = bpmContractChangeDOList.stream()
                .collect(Collectors.groupingBy(BpmContractChangeDO::getMainContractId));
        result.getList().forEach(item -> {
            // （暂定的）获取最新合同数据
            ContractDO contractDO = contractMap.get(item.getCode());
            if (contractDO != null) {
                //（暂定的）批量赋值合同状态
                ContractStatusEnums instance = ContractStatusEnums.getInstance(contractDO.getStatus());
                if (instance != null) {
                    item.setContractStatus(contractDO.getStatus());
                    item.setContractStatusStr(instance.getDesc());
                }
            }
//            //批量赋值合同状态
//            ContractStatusEnums instance = ContractStatusEnums.getInstance(item.getContractStatus());
//            if (instance != null) {
//                item.setContractStatusStr(instance.getDesc());
//            }
            //批量赋值合同类型名称
            ContractType contractType = contractTypeMap.get(item.getContractType());
            if (contractType != null) {
                item.setContractTypeName(contractType.getName());
            }
            //批量赋值向对方名称
            Relative relative = relativeMap.get(item.getCounterparty());
            if (relative != null) {
                item.setCounterpartyName(relative.getName());
            }
            //变更次数
            if (CollectionUtil.isNotEmpty(groupedByMainContractId.get(item.getContractId()))) {
                Integer changeCount = groupedByMainContractId.get(item.getContractId()).size();
                item.setChangeCount(changeCount);
            }
        });

        return result;
    }

    @Override
    @DataPermission(enable = false)
    public PageResult<ContractPageRespVO> getLedgerPageV2(LedgerPageReqV2VO contractPageReqVO) {

        //工作流执行人操作状态查询
//        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(WebFrameworkUtils.getLoginUserId(), new ArrayList<>(Arrays.asList(PROCESS_KEY_BOTH, PROCESS_KEY_TRIPARTITE)));
//        List<String> processInstanceIds = CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
//        if (CollectionUtil.isEmpty(processInstanceIds)) {
//            return PageResult.empty(0L);
//        }
//        contractPageReqVO.setProcessInstanceIds(processInstanceIds);
        //获取当前登录用户id
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        //根据userId获取部门id
        AdminUserRespDTO user = userApi.getUser(loginUser.getId());
        Long dept;
        if (ObjectUtil.isNotEmpty(user)) {
            dept = user.getDeptId();
        } else {
            dept = null;
        }
        //根据userId查询相对方表
        List<Relative> relativeList = relativeMapper.selectList4Relative(loginUser.getId());
        AtomicReference<List<String>> contractIdList = new AtomicReference<>(new ArrayList<>());
        if (CollectionUtil.isNotEmpty(relativeList)) {
            TenantUtils.executeIgnore(()->{
                //根据相对方id查询合同和相对方中间表
                List<SignatoryRelDO> contractRelativeDOList = signatoryRelMapper.selectList(new LambdaQueryWrapperX<SignatoryRelDO>()
                        .inIfPresent(SignatoryRelDO::getSignatoryId, CollectionUtils.convertList(relativeList, Relative::getId))
                );
                if (CollectionUtil.isNotEmpty(contractRelativeDOList)) {
                    contractIdList.set(contractRelativeDOList.stream().map(SignatoryRelDO::getContractId).collect(Collectors.toList()));
                }
            });
           
        }
        /**
         * 拼接权限sql
         */
        String finalComexpression = buildPerssionSql(contractIdList.get());

        AtomicReference<PageResult<ContractPageRespVO>> result = new AtomicReference<>(new PageResult<>());
        List<String> finalContractIdList = contractIdList.get();
       
        TenantUtils.executeIgnore(() -> {
            if (ObjectUtil.isNull(contractPageReqVO.getSignDate0())) {
                //默认查询当年签署的合同
                int year = DateUtil.thisYear();
                DateTime dateTime0 = DateUtil.parseDateTime(year + "/01/01 00:00:00");
                DateTime dateTime1 = DateUtil.parseDateTime(year + "/12/30 23:59:59");
                contractPageReqVO.setSignDate0(dateTime0);
                contractPageReqVO.setSignDate1(dateTime1);
            }


            PageResult<ContractDO> pageResult = contractMapper.selectLedgerPage(contractPageReqVO, finalContractIdList, loginUser.getTenantId(), dept, finalComexpression);
            result.set(ContractConverter.INSTANCE.convertPage(pageResult));
        });


        return enhance(result.get());
    }
    public String buildPerssionSql(List<String> contractIdList){
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        Expression expression = rule.getExpression("ecms_contract",  null);
        rule.addUserColumn("ecms_contract","creator");
        Expression tenantExpression = new EqualsTo(MyBatisUtils.buildColumn("ecms_contract", null, "tenant_id"), new LongValue(loginUser.getTenantId()));
        String comexpression = null ;
        if(expression == null){
            HexValue oneEqualsZero = new HexValue("1 = 1");
            expression = new AndExpressionX(oneEqualsZero,tenantExpression);
        }else{
            expression =new AndExpressionX(expression,tenantExpression);
        }
        if(contractIdList.size() > 0 ){
            Expression inExpression = new InExpression(MyBatisUtils.buildColumn("ecms_contract", null, "id"),
                    new ExpressionList(CollectionUtils.convertList(contractIdList, StringValue::new)));
            comexpression = new OrExpressionX(expression,inExpression).toString();
        }else{
            comexpression = expression.toString();
        }
        return comexpression;
    } 
    @Override
    @DataPermission(enable = false)
    public Map<String, Object> getTotal(LedgerPageReqV2VO vo) {
//        //工作流执行人操作状态查询
//        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(WebFrameworkUtils.getLoginUserId(), new ArrayList<>(Arrays.asList(PROCESS_KEY_BOTH, PROCESS_KEY_TRIPARTITE)));
//        List<String> processInstanceIds = CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
//        if (CollectionUtil.isEmpty(processInstanceIds)) {
//            return new HashMap<>();
//        }
//        vo.setProcessInstanceIds(processInstanceIds);
        //获取当前登录用户id
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        //根据userId获取部门id
        AdminUserRespDTO user = userApi.getUser(loginUser.getId());
        Long dept;
        if (ObjectUtil.isNotEmpty(user)) {
            dept = user.getDeptId();
        } else {
            dept = null;
        }
        //根据userId查询相对方表
        List<Relative> relativeList = relativeMapper.selectList4Relative(loginUser.getId());
        AtomicReference<List<String>> contractIdList = new AtomicReference<>(new ArrayList<>());
        if (CollectionUtil.isNotEmpty(relativeList)) {
            TenantUtils.executeIgnore(()->{
                //根据相对方id查询合同和相对方中间表
                List<SignatoryRelDO> contractRelativeDOList = signatoryRelMapper.selectList(new LambdaQueryWrapperX<SignatoryRelDO>()
                        .inIfPresent(SignatoryRelDO::getSignatoryId, CollectionUtils.convertList(relativeList, Relative::getId))
                );
                if (CollectionUtil.isNotEmpty(contractRelativeDOList)) {
                    contractIdList.set(contractRelativeDOList.stream().map(SignatoryRelDO::getContractId).collect(Collectors.toList()));
                }
            });
          
        }
        /**
         * 拼接权限sql
         */
        String finalComexpression = buildPerssionSql(contractIdList.get());
        
        Map<String, Object> map = new HashMap<>();
        List<String> finalContractIdList = contractIdList.get();
        TenantUtils.executeIgnore(() -> {
            List<ContractDO> result = contractMapper.selectLedgerList(vo, finalContractIdList, loginUser.getTenantId(), dept,finalComexpression);


//        List<ContractDO> result = contractMapper.selectLedgerList(vo);
            Double totalMoney = CollectionUtils.convertList(result, ContractDO::getAmount).stream().reduce(0d, Double::sum);

            map.put("paid", "0.00");
            map.put("unpaid", "0.00");
            map.put("collected", "0.00");
            map.put("uncollected", "0.00");
            map.put("sumPay", "0.00");
            map.put("sumCollect", "0.00");
            map.put("payContractCount", 0);
            map.put("collectContractCount", 0);
            map.put("noSettleContractCount", 0);
            map.put("directionContractCount", 0);
            if (CollectionUtil.isNotEmpty(result)) {
                List<String> payContractIdList = result.stream().filter(contractDO -> AmountTypeEnums.PAY.getCode().equals(contractDO.getAmountType())).map(ContractDO::getId).collect(Collectors.toList());
                List<String> collectionContractIdList = result.stream().filter(contractDO -> AmountTypeEnums.RECEIPT.getCode().equals(contractDO.getAmountType())).map(ContractDO::getId).collect(Collectors.toList());
                List<String> noSettleContractIdList = result.stream().filter(contractDO -> AmountTypeEnums.NO_SETTLE.getCode().equals(contractDO.getAmountType())).map(ContractDO::getId).collect(Collectors.toList());
                List<String> directionContractIdList = result.stream().filter(contractDO -> AmountTypeEnums.DIRECTION.getCode().equals(contractDO.getAmountType())).map(ContractDO::getId).collect(Collectors.toList());
                map.put("payContractCount", payContractIdList.size());
                map.put("collectContractCount", collectionContractIdList.size());
                map.put("noSettleContractCount", noSettleContractIdList.size());
                map.put("directionContractCount", directionContractIdList.size());
                if (CollectionUtil.isNotEmpty(payContractIdList)) {
                    List<PaymentScheduleDO> selectList = paymentScheduleMapper.selectList(PaymentScheduleDO::getContractId, payContractIdList);
                    BigDecimal sumPay = selectList.stream().map(PaymentScheduleDO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal paid = selectList.stream().filter(item -> PaymentScheduleStatusEnums.DONE.getCode().equals(item.getStatus()) || PaymentScheduleStatusEnums.CLOSE.getCode().equals(item.getStatus())).map(PaymentScheduleDO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal unpaid = sumPay.subtract(paid);
                    map.put("paid", paid.toPlainString());
                    map.put("unpaid", unpaid.toPlainString());
                    //map.put("sumPay", new BigDecimal(paid).add(new BigDecimal(unpaid)));
                    map.put("sumPay", sumPay.toPlainString());
                }
                if (CollectionUtil.isNotEmpty(collectionContractIdList)) {
                    List<PaymentScheduleDO> selectList = paymentScheduleMapper.selectList(PaymentScheduleDO::getContractId, collectionContractIdList);
                    BigDecimal sumCollect = selectList.stream().map(PaymentScheduleDO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal collected = selectList.stream().filter(item -> PaymentScheduleStatusEnums.DONE.getCode().equals(item.getStatus()) || PaymentScheduleStatusEnums.CLOSE.getCode().equals(item.getStatus())).map(PaymentScheduleDO::getPaidAmount).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);
                    BigDecimal uncollected = sumCollect.subtract(collected);
                    map.put("collected", collected.toPlainString());
                    map.put("uncollected", uncollected.toPlainString());
                    map.put("sumCollect", sumCollect.toPlainString());

                }
            }
            map.put("contractSize", result.size());
            map.put("sumMoney", totalMoney);
        });
        return map;
    }


    private PageResult<ContractPageRespVO> enhance(PageResult<ContractPageRespVO> result) {
        if (CollectionUtil.isNotEmpty(result.getList())) {
            List<String> contractIds = result.getList().stream().map(ContractPageRespVO::getId).collect(Collectors.toList());
            List<String> contractTypeIds = result.getList().stream().map(ContractPageRespVO::getContractType).collect(Collectors.toList());
            List<ContractType> contractTypeInfos = contractTypeMapper.selectBatchIds(contractTypeIds);
            Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypeInfos, ContractType::getId);
            List<BpmContractChangeDO> bpmContractChangeDOList = bpmContractChangeMapper.selectList(new LambdaQueryWrapperX<BpmContractChangeDO>()
                    .in(BpmContractChangeDO::getMainContractId, contractIds));
            Map<String, List<BpmContractChangeDO>> groupedByMainContractId = bpmContractChangeDOList.stream()
                    .collect(Collectors.groupingBy(BpmContractChangeDO::getMainContractId));
            List<String> relativeContractIds = new ArrayList<>();
            LoginUser user = SecurityFrameworkUtils.getLoginUser();
            if (ObjectUtil.isNotEmpty(user)) {
                List<Relative> relatives = relativeMapper.selectList4Relative(user.getId());
                if (CollectionUtil.isNotEmpty(relatives)) {
                    List<String> relativeIds = relatives.stream().map(Relative::getId).collect(Collectors.toList());
                    List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getSignatoryId, relativeIds);
                    //当前用户作为相对方的合同id集合
                    relativeContractIds = signatoryRelDOS.stream().map(SignatoryRelDO::getContractId).collect(Collectors.toList());
                }
            }
            for (ContractPageRespVO item : result.getList()) {
                //（暂定的）批量赋值合同状态
                ContractStatusEnums instance = ContractStatusEnums.getInstance(item.getStatus());
                if (instance != null) {
                    item.setStatusName(instance.getDesc());
                }
                PlatformEnums platformEnums = PlatformEnums.getInstance(item.getPlatform());
                if (platformEnums != null) {
                    item.setPlatformName(platformEnums.getInfo());
                }
                ContractUploadTypeEnums contractUploadTypeEnums = ContractUploadTypeEnums.getInstance(item.getUpload());
                if (contractUploadTypeEnums != null) {
                    item.setUploadName(contractUploadTypeEnums.getInfo());
                }
                //批量赋值合同类型名称
                ContractType contractType = contractTypeMap.get(item.getContractType());
                if (contractType != null) {
                    item.setContractTypeName(contractType.getName());
                }
                //变动次数
                if (CollectionUtil.isNotEmpty(groupedByMainContractId)) {
                    List<BpmContractChangeDO> changeDOList = groupedByMainContractId.get(item.getId());
                    if (CollectionUtil.isNotEmpty(changeDOList)) {
                        item.setChangeCount(changeDOList.size());
                        if (CollectionUtil.isNotEmpty(changeDOList)) {
                            item.setChangeType(changeDOList.get(0).getChangeType());
                            item.setChangeTypeName(ContractChangeTypeEnums.getInstance(changeDOList.get(0).getChangeType()).getInfo());
                        }
                    }
                }
                item.setIsRelative((CollectionUtil.isNotEmpty(relativeContractIds) && relativeContractIds.contains(item.getId())) ? IfNumEnums.YES.getCode() : IfNumEnums.NO.getCode());
            }
        }
        return result;
    }

}


