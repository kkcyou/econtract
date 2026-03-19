package com.yaoan.module.econtract.service.paymentPlan;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.datapermission.core.rule.dept.DeptDataPermissionRule;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.ContractProcessInstanceRelationInfoRespDTO;
import com.yaoan.module.econtract.controller.admin.contract.vo.*;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.listPaymentApplication.PaymentApplicationListReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.listPaymentApplication.PaymentApplicationListRespVO;
import com.yaoan.module.econtract.controller.admin.paymentPlan.vo.*;
import com.yaoan.module.econtract.controller.admin.paymentRecord.vo.PaymentAmountRepVO;
import com.yaoan.module.econtract.controller.admin.paymentRecord.vo.PaymentRecordRepVO;
import com.yaoan.module.econtract.controller.admin.paymentRecord.vo.PaymentRecordRespVO;
import com.yaoan.module.econtract.controller.admin.relative.vo.ContractRelativeVO;
import com.yaoan.module.econtract.convert.contract.ContractConverter;
import com.yaoan.module.econtract.convert.contract.PaymentScheduleConverter;
import com.yaoan.module.econtract.convert.payment.PaymentApplicationConverter;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import com.yaoan.module.econtract.dal.dataobject.contract.*;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplScheRelDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplicationDO;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.mysql.businessfile.BusinessFileMapper;
import com.yaoan.module.econtract.dal.mysql.category.ContractCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractPerformanceLogMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contract.SimpleContractMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.payment.paymentapplication.PaymentApplScheRelMapper;
import com.yaoan.module.econtract.dal.mysql.payment.paymentapplication.PaymentApplicationMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import com.yaoan.module.econtract.enums.*;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.econtract.enums.payment.MoneyTypeEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import com.yaoan.module.econtract.enums.payment.PerformanceTypeEnums;
import com.yaoan.module.econtract.enums.payment.SettlementMethodEnums;
import com.yaoan.module.econtract.service.contract.ContractService;
import com.yaoan.module.system.api.dept.CompanyApi;
import com.yaoan.module.system.api.dept.PostApi;
import com.yaoan.module.system.api.dept.dto.PostDTO;
import com.yaoan.module.system.api.dept.dto.UserCompanyDeptRespDTO;
import com.yaoan.module.system.api.dept.dto.UserCompanyInfoRespDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;
import static com.yaoan.module.econtract.enums.perform.log.PerformanceLogModuleNameEnums.PAY_APPLICATION;
import static java.math.RoundingMode.HALF_UP;

/**
 * 支付计划实现类
 *
 * @author zhc
 * @since 2023-12-21
 */
@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {
    @Resource
    private ContractService contractService;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private SimpleContractMapper simpleContractMapper;
    @Resource
    private ContractCategoryMapper contractCategoryMapper;
    @Resource
    private CompanyApi companyApi;
    @Resource
    private AdminUserApi userApi;
    @Resource
    private PostApi postApi;
    @Resource
    private SignatoryRelMapper signatoryRelMapper;
    @Resource
    private RelativeMapper relativeMapper;
    @Resource
    private PaymentApplicationMapper paymentApplicationMapper;

    @Resource
    private ContractPerformanceLogMapper contractPerformanceLogMapper;
    @Resource
    private BusinessFileMapper businessFileMapper;

    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private PaymentApplScheRelMapper paymentApplScheRelMapper;


    @Override
    public PageResult<ContractPaymentPlanRespVO> queryAllPaymentPlan(PaymentPlanRepVO paymentPlanRepVO) {
        //获取当前时间
        Date date = new Date();
        paymentPlanRepVO.setCurrentDate(date);
        //1.获取支付计划信息
        PageResult<PaymentScheduleDO> paymentScheduleDOPageResult = paymentScheduleMapper.selectPage(paymentPlanRepVO);
        PageResult<ContractPaymentPlanRespVO> pageVO = PaymentScheduleConverter.INSTANCE.toPageVO(paymentScheduleDOPageResult);
        if (CollectionUtil.isNotEmpty(paymentScheduleDOPageResult.getList())) {
            Map<String, PaymentScheduleDO> paymentScheduleDOMap = CollectionUtils.convertMap(paymentScheduleDOPageResult.getList(), PaymentScheduleDO::getId);
            //2.获取合同信息
            Set<String> contractIdS = paymentScheduleDOPageResult.getList().stream().map(PaymentScheduleDO::getContractId).collect(Collectors.toSet());
            List<ContractDO> contractDOS = contractMapper.selectBatchIds(contractIdS);
            Map<String, ContractDO> contractDOMap = CollectionUtils.convertMap(contractDOS, ContractDO::getId);
            //3.获取合同类型信息
            Set<String> contractTypeIdS = contractDOS.stream().map(ContractDO::getContractType).collect(Collectors.toSet());
            List<ContractType> contractTypes = contractTypeMapper.selectBatchIds(contractTypeIdS);
            Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypes, ContractType::getId);
            //4.获取创建人
            Set<String> creatorIdS = paymentScheduleDOPageResult.getList().stream().map(PaymentScheduleDO::getCreator).collect(Collectors.toSet());
            Set<Long> creatorIds = creatorIdS.stream().map(Long::parseLong).collect(Collectors.toSet());
            //获取公司信息
            List<UserCompanyInfoRespDTO> userCompanyInfoList = companyApi.getUserCompanyInfoList(creatorIds);
            Map<Long, UserCompanyInfoRespDTO> usercompanyMaps = CollectionUtils.convertMap(userCompanyInfoList, UserCompanyInfoRespDTO::getUserId);
            //获取个人信息
            Map<Long, AdminUserRespDTO> userMaps = null;
            if (CollectionUtil.isEmpty(userCompanyInfoList)) {
                List<AdminUserRespDTO> userListByDeptIds = userApi.getUserListByDeptIds(userCompanyInfoList.stream().map(UserCompanyInfoRespDTO::getUserId).collect(Collectors.toList()));
                userMaps = CollectionUtils.convertMap(userListByDeptIds, AdminUserRespDTO::getId);
            }
            //获取相对方
            //获取签署方id集合
            List<SignatoryRelDO> signatoryRelations = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractIdS);
            Map<String, Relative> relativeMaps = null;
            Map<String, List<SignatoryRelDO>> contractRelationMap = null;
            if (CollectionUtil.isNotEmpty(signatoryRelations)) {
                contractRelationMap = CollectionUtils.convertMultiMap(signatoryRelations, SignatoryRelDO::getContractId);
                //获取相对方ids
                Set<String> ids = signatoryRelations.stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toSet());
                List<Relative> relativeList = relativeMapper.selectBatchIds(ids);
                relativeMaps = CollectionUtils.convertMap(relativeList, Relative::getId);
            }
            for (ContractPaymentPlanRespVO paymentPlanPageRespVO : pageVO.getList()) {
                ContractDO contractDO = contractDOMap == null ? null : contractDOMap.get(paymentPlanPageRespVO.getContractId());
                if (BeanUtil.isNotEmpty(contractDO)) {
                    //设置合同名称
                    paymentPlanPageRespVO.setContractName(contractDO.getName());
                    //设置合同编码
                    paymentPlanPageRespVO.setContractCode(contractDO.getCode());
                    //设置合同类型id
                    paymentPlanPageRespVO.setContractType(contractDO.getContractType());
                    //设置合同类型名称
                    paymentPlanPageRespVO.setContractTypeName(contractTypeMap.get(contractDO.getContractType()) == null ? null : contractTypeMap.get(contractDO.getContractType()).getName());

                    //设置合同的支付计划目前的排序标识（Pele追加）
                    paymentPlanPageRespVO.setCurrentScheduleSort(contractDO.getCurrentScheduleSort());
                }
                //设置支付状态名称
                if (ObjectUtil.isNotEmpty(paymentPlanPageRespVO.getStatus())) {
                    paymentPlanPageRespVO.setStatusName(PaymentScheduleStatusEnums.getInstance(paymentPlanPageRespVO.getStatus()).getInfo());
                }
                //设置我方签约主体
                PaymentScheduleDO paymentScheduleDO = paymentScheduleDOMap.get(paymentPlanPageRespVO.getId());
                if (BeanUtil.isNotEmpty(paymentScheduleDO)) {
                    if (ObjectUtil.isNotEmpty(usercompanyMaps.get(Long.valueOf(paymentScheduleDO.getCreator())))) {
                        //主体为单位或企业
                        paymentPlanPageRespVO.setMyContractParty(usercompanyMaps.get(Long.valueOf(paymentScheduleDO.getCreator())) == null ? null : usercompanyMaps.get(Long.valueOf(paymentScheduleDO.getCreator())).getName());
                    } else {
                        //发起方是个体，通过id 获取nickname
                        if (ObjectUtil.isNotEmpty(userMaps)) {
                            paymentPlanPageRespVO.setMyContractParty(userMaps.get(Long.valueOf(paymentScheduleDO.getCreator())) == null ? null : userMaps.get(Long.valueOf(paymentScheduleDO.getCreator())).getNickname());
                        }
                    }
                }
                //设置相对方签约主体
                List<SignatoryRelDO> signatoryRelDOS = contractRelationMap == null ? null : contractRelationMap.get(paymentPlanPageRespVO.getContractId());
                if (CollectionUtil.isNotEmpty(signatoryRelDOS)) {
                    List<String> signatoryList = new ArrayList<>();
                    for (SignatoryRelDO signatoryRelDO : signatoryRelDOS) {
                        Relative relative = relativeMaps == null ? null : relativeMaps.get(signatoryRelDO.getSignatoryId());
                        if (relative != null) {
                            signatoryList.add(relative.getCompanyName());
                        }
                    }
                    paymentPlanPageRespVO.setCounterparty(signatoryList);
                }
            }
        }
        return pageVO;
    }

    @Override
    public PageResult<PaymentRecordRespVO> queryAllPaymentRecord(PaymentRecordRepVO recordRepVO) {
        PageResult<PaymentApplicationDO> paymentApplicationDOPageResult = paymentApplicationMapper.selectPage(recordRepVO);
        PageResult<PaymentRecordRespVO> pageVO = PaymentApplicationConverter.INSTANCE.toPageVO(paymentApplicationDOPageResult);
        if (CollectionUtil.isNotEmpty(paymentApplicationDOPageResult.getList())) {
            //获取合同信息
            Set<String> contractIds = paymentApplicationDOPageResult.getList().stream().map(PaymentApplicationDO::getContractId).collect(Collectors.toSet());
            //获取相对方
            //获取签署方id集合
            List<SignatoryRelDO> signatoryRelations = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractIds);
            Map<String, Relative> relativeMaps = null;
            Map<String, List<SignatoryRelDO>> contractRelationMap = null;
            if (CollectionUtil.isNotEmpty(signatoryRelations)) {
                contractRelationMap = CollectionUtils.convertMultiMap(signatoryRelations, SignatoryRelDO::getContractId);
                //获取相对方ids
                Set<String> ids = signatoryRelations.stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toSet());
                List<Relative> relativeList = relativeMapper.selectBatchIds(ids);
                relativeMaps = CollectionUtils.convertMap(relativeList, Relative::getId);
            }
            //计划申请ids
            Set<String> ids = paymentApplicationDOPageResult.getList().stream().map(PaymentApplicationDO::getId).collect(Collectors.toSet());
            List<ContractDO> contractDOS = contractMapper.selectBatchIds(contractIds);
            Map<String, ContractDO> contractDOMap = CollectionUtils.convertMap(contractDOS, ContractDO::getId);
            //根据计划申请id查询支付计划
//            List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.selectList(PaymentScheduleDO::getApplyId, ids);
            List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.selectPlanForApplicationBatch(ids);
            Map<String, List<PaymentScheduleDO>> paymentScheduleDOSMap = CollectionUtils.convertMultiMap(paymentScheduleDOS, PaymentScheduleDO::getContractId);
            for (PaymentRecordRespVO paymentRecordRespVO : pageVO.getList()) {
                //设置合同名称和编码
                if (ObjectUtil.isNotEmpty(contractDOMap)) {
                    paymentRecordRespVO.setContractName(contractDOMap.get(paymentRecordRespVO.getContractId()) == null ? null : contractDOMap.get(paymentRecordRespVO.getContractId()).getName());
                    paymentRecordRespVO.setContractCode(contractDOMap.get(paymentRecordRespVO.getContractId()) == null ? null : contractDOMap.get(paymentRecordRespVO.getContractId()).getCode());
                }
                //计算实际支付时间-根据计划申请id查询
                if (ObjectUtil.isNotEmpty(paymentScheduleDOSMap)) {
                    List<PaymentScheduleDO> scheduleDOS = paymentScheduleDOSMap.get(paymentRecordRespVO.getContractId());
                    paymentRecordRespVO.setActualPayTime(CollectionUtil.isNotEmpty(scheduleDOS) ? scheduleDOS.get(0).getActualPayTime() : null);
                }
                //查询收款方信息（相对方签约主体）
                List<SignatoryRelDO> signatoryRelDOS = contractRelationMap == null ? null : contractRelationMap.get(paymentRecordRespVO.getContractId());
                if (CollectionUtil.isNotEmpty(signatoryRelDOS)) {
                    List<String> signatoryList = new ArrayList<>();
                    for (SignatoryRelDO signatoryRelDO : signatoryRelDOS) {
                        Relative relative = relativeMaps == null ? null : relativeMaps.get(signatoryRelDO.getSignatoryId());
                        if (relative != null) {
                            signatoryList.add(relative.getCompanyName());
                        }
                    }
                    paymentRecordRespVO.setCounterparty(signatoryList);
                }
            }
        }
        return pageVO;
    }

    @Override
    public PaymentPlanInfoRespVO queryPianpaymentById(String id) {
        //查询合同信息
        List<ContractRelativeVO> signatory = new ArrayList<>();
        ContractDO contractDO = contractMapper.selectById(id);
        PaymentPlanInfoRespVO result = ContractConverter.INSTANCE.tovo(contractDO);

        //查询申请人信息
        if (ObjectUtil.isNotEmpty(contractDO.getCreator())) {
            AdminUserRespDTO user = userApi.getUser(Long.valueOf(contractDO.getCreator()));
            if (ObjectUtil.isNotEmpty(user)) {
                Set<Long> postIds = user.getPostIds();
                if (CollectionUtil.isNotEmpty(postIds)) {
                    List<PostDTO> postList = postApi.getPostList(postIds);
                    if (CollectionUtil.isNotEmpty(postList)) {
                        result.setPast(postList.get(0).getName());
                    }
                }
            }
        }
        //查询合同分类名称
        if (ObjectUtil.isNotEmpty(contractDO.getContractCategory())) {
            result.setContractCategoryName(contractCategoryMapper.selectById(contractDO.getContractCategory()) == null ? null : contractCategoryMapper.selectById(contractDO.getContractCategory()).getName());
        }
        //查询合同类型名称
        if (ObjectUtil.isNotEmpty(contractDO.getContractType())) {
            result.setContractTypeName(contractTypeMapper.selectById(contractDO.getContractType()) == null ? null : contractTypeMapper.selectById(contractDO.getContractType()).getName());
        }
        //查询支付计划信息
        List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.selectList(PaymentScheduleDO::getContractId, id);
        result.setTotalPeriod(paymentScheduleDOS.size());
        List<PaymentPlanRespVO> vos = PaymentScheduleConverter.INSTANCE.toVOS(paymentScheduleDOS);
        Set<String> set = paymentScheduleDOS.stream().map(PaymentScheduleDO::getPayee).collect(Collectors.toSet());
        List<Relative> relativeList = relativeMapper.selectBatchIds(set);
        Map<String, Relative> relativeMap = CollectionUtils.convertMap(relativeList, Relative::getId);
        vos.forEach(item -> {
            //设置支付状态名称
            if (ObjectUtil.isNotEmpty(item.getStatus())) {
                item.setStatusName(PaymentScheduleStatusEnums.getInstance(item.getStatus()).getInfo());
            }
            //设置收款人名称-相对方
            if (ObjectUtil.isNotEmpty(item.getPayee())) {
                Relative relative = relativeMap.get(item.getPayee());
                if (ObjectUtil.isNotEmpty(relative)) {
                    if (EntityTypeEnums.COMPANY.getCode().equals(relative.getEntityType()) || EntityTypeEnums.ORGANIZATION.getCode().equals(relative.getEntityType())) {
                        //单位或企业
                        item.setPayeeName(relative.getCompanyName());
                    } else if (EntityTypeEnums.INDIVIDUAL.getCode().equals(relative.getEntityType())) {
                        //个人
                        item.setPayeeName(relative.getName());
                    }
                }

            }
        });
        result.setPaymentPlan(vos);

        //查询签署方信息
        ContractRelativeVO contractRelativeVO = new ContractRelativeVO();
        //获取甲方信息
        UserCompanyDeptRespDTO userCompanyDeptRespDTO = userApi.selectUserCompanyDept(Long.valueOf(contractDO.getCreator()));
        if (ObjectUtil.isNotEmpty(userCompanyDeptRespDTO)) {
            contractRelativeVO.setAccount(userCompanyDeptRespDTO.getUsername());
            contractRelativeVO.setName(userCompanyDeptRespDTO.getNickname());
            contractRelativeVO.setSignatoryPosition("甲方");
            //部门id为空 表示此账号为个人账户
            if (ObjectUtil.isEmpty(userCompanyDeptRespDTO.getDeptId())) {
                contractRelativeVO.setEntityType(EntityTypeEnums.INDIVIDUAL.getCode());
            } else {
                //部门id不为空 表示此账号为企业或者单位账户
                if (ObjectUtil.isNotEmpty(userCompanyDeptRespDTO.getCompanyInfo())) {
                    //此部门为顶级部门
                    contractRelativeVO.setCompanyName(userCompanyDeptRespDTO.getCompanyInfo().getName());
                    contractRelativeVO.setCreditCode(userCompanyDeptRespDTO.getCompanyInfo().getCreditCode());
                    contractRelativeVO.setEntityType(userCompanyDeptRespDTO.getCompanyInfo().getMajor().toString());
                }
            }
            //设置主体类型
            contractRelativeVO.setEntityTypeName(EntityTypeEnums.getInstance(contractRelativeVO.getEntityType()).getInfo());
            signatory.add(contractRelativeVO);
        }
        //获取相对方-乙方
        List<SignatoryRelDO> signatoryRelations = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, id);
        if (CollectionUtil.isNotEmpty(signatoryRelations)) {
            //获取相对方ids
            Set<String> ids = signatoryRelations.stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toSet());
            List<Relative> relativeList1 = relativeMapper.selectBatchIds(ids);
            for (Relative relative : relativeList1) {
                ContractRelativeVO contractRelativeVO2 = new ContractRelativeVO();
                //个人
                if (EntityTypeEnums.INDIVIDUAL.getCode().equals(relative.getEntityType())) {
                    contractRelativeVO2.setAccount(relative.getContactAccount());
                    contractRelativeVO2.setName(relative.getName());
                    contractRelativeVO2.setSignatoryPosition("乙方");
                } else {
                    //单位或企业
                    contractRelativeVO2.setAccount(relative.getContactAccount());
                    contractRelativeVO2.setName(relative.getName());
                    contractRelativeVO2.setSignatoryPosition("乙方");
                    contractRelativeVO2.setCompanyName(relative.getCompanyName());
                    contractRelativeVO2.setCreditCode(relative.getCardNo());
                }
                contractRelativeVO2.setEntityTypeName(EntityTypeEnums.getInstance(relative.getEntityType()).getInfo());
                contractRelativeVO2.setEntityType(relative.getEntityType());
                signatory.add(contractRelativeVO2);
            }
            result.setSignatory(signatory);
        }
        //结算类型
        if (ObjectUtil.isNotNull(contractDO)) {
            result.setAmountType(contractDO.getAmountType());
        }

        return result;
    }

    /**
     * 支付计划-金额统计
     */
    @Override
    public PaymentPlanAmountRespVO queryAmountInfo(PaymentAmountRepVO paymentPlanRepVO) {
        //获取当前时间
        Date date = new Date();
        paymentPlanRepVO.setCurrentDate(date);
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal paidAmount = BigDecimal.ZERO;
        BigDecimal noPayAmount = BigDecimal.ZERO;
        PaymentPlanAmountRespVO paymentPlanAmountRespVO = new PaymentPlanAmountRespVO();
        if (paymentPlanRepVO.getFlag() == 1) {
            //支付计划
            List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.selectAll(paymentPlanRepVO);
            if (CollectionUtil.isNotEmpty(paymentScheduleDOS)) {
                for (PaymentScheduleDO paymentScheduleDO : paymentScheduleDOS) {
                    totalAmount = totalAmount.add(paymentScheduleDO.getAmount());
                    if (Objects.equals(paymentScheduleDO.getStatus(), PaymentScheduleStatusEnums.PAYED.getCode())) {
                        paidAmount = paidAmount.add(paymentScheduleDO.getAmount());
                    }
                }
                noPayAmount = totalAmount.subtract(paidAmount);
            }
        } else if (paymentPlanRepVO.getFlag() == 2) {
            //付款记录/收款记录
            List<PaymentApplicationDO> paymentApplicationDOS = paymentApplicationMapper.selectAll(paymentPlanRepVO);
            if (CollectionUtil.isNotEmpty(paymentApplicationDOS)) {
                //获取合同信息
                Set<String> contractIds = paymentApplicationDOS.stream().map(PaymentApplicationDO::getContractId).collect(Collectors.toSet());
                List<ContractDO> contractDOS = contractMapper.selectBatchIds(contractIds);
                Map<String, ContractDO> contractDOMap = CollectionUtils.convertMap(contractDOS, ContractDO::getId);
                //计算合同总金额
                for (PaymentApplicationDO paymentApplicationDO : paymentApplicationDOS) {
                    if (ObjectUtil.isNotEmpty(contractDOMap.get(paymentApplicationDO.getContractId()))) {
                        Double amount = contractDOMap.get(paymentApplicationDO.getContractId()) == null ? null : contractDOMap.get(paymentApplicationDO.getContractId()).getAmount();

                        totalAmount = totalAmount.add(amount == null ? BigDecimal.ZERO : new BigDecimal(amount));
                        //计算已支付金额
                        if (paymentApplicationDO.getCurrentPayAmount() != null) {
                            paidAmount = paidAmount.add(paymentApplicationDO.getCurrentPayAmount());
                        }
                    }

                }
                //计算未支付金额
                noPayAmount = totalAmount.subtract(paidAmount);
            }
        }
        paymentPlanAmountRespVO.setTotalAmount(totalAmount).setPaidAmount(paidAmount).setNoPayAmount(noPayAmount);
        return paymentPlanAmountRespVO;
    }

    @Override
    public void pushPayInfo(List<String> ids) {
        ArrayList<PaymentScheduleDO> list = new ArrayList<>();
        for (String id : ids) {
            PaymentScheduleDO paymentScheduleDO = new PaymentScheduleDO().setId(id).setActualPayTime(new Date()).setStatus(PaymentScheduleStatusEnums.DONE.getCode());
            list.add(paymentScheduleDO);
        }
        paymentScheduleMapper.updateBatch(list);
    }

    /**
     * 付款管理-金额统计(Pele)
     */
    @Override
    public PaymentPlanAmountRespVO paymentManagementStatistics(PaymentAmountRepVO paymentPlanRepVO) {
        //获取当前时间
        Date date = new Date();

        //收款合同不需要进行时间限制
        if (paymentPlanRepVO.getAmountType() != 1) {
            paymentPlanRepVO.setCurrentDate(date);
        }
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal paidAmount = BigDecimal.ZERO;
        BigDecimal noPayAmount = BigDecimal.ZERO;
        BigDecimal ratio = BigDecimal.ZERO;
        PaymentPlanAmountRespVO paymentPlanAmountRespVO = new PaymentPlanAmountRespVO();


        List<String> userIds1 = new ArrayList<String>();
        List<String> userIds2 = new ArrayList<String>();
        if (StringUtils.isNotEmpty(paymentPlanRepVO.getPayerName())) {
            List<AdminUserRespDTO> adminUserRespDTOList = userApi.getUserListLikeNickname(paymentPlanRepVO.getPayerName());
            userIds1 = adminUserRespDTOList.stream().map(AdminUserRespDTO::getId).collect(Collectors.toList()).stream().map(String::valueOf).collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(paymentPlanRepVO.getPayeeName())) {
            List<AdminUserRespDTO> adminUserRespDTOList = userApi.getUserListLikeNickname(paymentPlanRepVO.getPayerName());
            userIds2 = adminUserRespDTOList.stream().map(AdminUserRespDTO::getId).collect(Collectors.toList()).stream().map(String::valueOf).collect(Collectors.toList());
        }

        //支付计划
        List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.listPaymentManagement(paymentPlanRepVO, userIds1, userIds2);

        if (CollectionUtil.isNotEmpty(paymentScheduleDOS)) {
            for (PaymentScheduleDO paymentScheduleDO : paymentScheduleDOS) {
                totalAmount = totalAmount.add(paymentScheduleDO.getAmount());
                if (Objects.equals(paymentScheduleDO.getStatus(), PaymentScheduleStatusEnums.PAYED.getCode())) {
                    paidAmount = paidAmount.add(paymentScheduleDO.getAmount());
                }
            }
            noPayAmount = totalAmount.subtract(paidAmount);
        }
        if (!BigDecimal.ZERO.equals(paidAmount)) {
            ratio = paidAmount.divide(totalAmount, 4, BigDecimal.ROUND_HALF_UP);
        }

        return paymentPlanAmountRespVO
                .setTotalAmount(totalAmount)
                .setPaidAmount(paidAmount)
                .setNoPayAmount(noPayAmount)
                .setRatio(ratio)
                ;
    }

    /**
     * 付款管理-列表
     */
    @Override
    public PageResult<PaymentSchedulePageRespVO> listPaymentManagement(PaymentSchedulePageReqVO paymentSchedulePageReqVO) {
        List<String> userIds1 = new ArrayList<String>();
        List<String> userIds2 = new ArrayList<String>();
        if (StringUtils.isNotEmpty(paymentSchedulePageReqVO.getPayerName())) {
            List<AdminUserRespDTO> adminUserRespDTOList = userApi.getUserListLikeNickname(paymentSchedulePageReqVO.getPayerName());
            userIds1 = adminUserRespDTOList.stream().map(AdminUserRespDTO::getId).collect(Collectors.toList()).stream().map(String::valueOf).collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(paymentSchedulePageReqVO.getPayeeName())) {
            List<AdminUserRespDTO> adminUserRespDTOList = userApi.getUserListLikeNickname(paymentSchedulePageReqVO.getPayerName());
            userIds2 = adminUserRespDTOList.stream().map(AdminUserRespDTO::getId).collect(Collectors.toList()).stream().map(String::valueOf).collect(Collectors.toList());
        }

        //支付计划
        //PageResult<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.listPaymentManagement(paymentSchedulePageReqVO, userIds1, userIds2);
        PageResult<SimpleContractDO> contractDOPageResult = simpleContractMapper.listPaymentManagement(paymentSchedulePageReqVO, userIds1, userIds2);

        PageResult<PaymentSchedulePageRespVO> resultPageRespVO = PaymentScheduleConverter.INSTANCE.pageDO2ContractRespPage(contractDOPageResult);
        enhancePage(resultPageRespVO);
        return resultPageRespVO;
    }

    @Override
    @DataPermission(includeRules = {DeptDataPermissionRule.class})
    public PageResult<PayPerformancePageRespVO> listPerformance(PaymentPlanRepVO paymentPlanRepVO) {
//        LambdaQueryWrapperX<PaymentScheduleDO> lambdaQueryWrapperX = new LambdaQueryWrapperX();

        if (ObjectUtil.isNotEmpty(paymentPlanRepVO.getIdentifier())){
            switch (paymentPlanRepVO.getIdentifier()){
                case 0:
                    break;
                case 1:
                    paymentPlanRepVO.setStatus(PaymentScheduleStatusEnums.UNPAID.getCode());
                    break;
                case 2:
                    paymentPlanRepVO.setStatus(PaymentScheduleStatusEnums.PAYED.getCode());
//                lambdaQueryWrapperX.isNotNull(PaymentScheduleDO::getPaidAmount);
                    break;
                case 3:
                    paymentPlanRepVO.setStatus(PaymentScheduleStatusEnums.DONE.getCode());
                    break;
                case 4:
                    paymentPlanRepVO.setStatus(PaymentScheduleStatusEnums.CLOSE.getCode());
                    break;
                default:
                    break;
            }
        }
        PageResult<PaymentScheduleDO> paymentScheduleDOPageResult = paymentScheduleMapper.selectPage(paymentPlanRepVO);
        if (paymentScheduleDOPageResult.getTotal() == 0) {
            return PageResult.empty();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        PageResult<PayPerformancePageRespVO> resultPageRespVO = PaymentScheduleConverter.INSTANCE.paymentPageDO2VO(paymentScheduleDOPageResult);
        List<String> contractIds = resultPageRespVO.getList().stream().map(PayPerformancePageRespVO::getContractId).collect(Collectors.toList());
        LambdaQueryWrapperX<SimpleContractDO> lambdaQueryWrapperX1 = new LambdaQueryWrapperX();
        lambdaQueryWrapperX1.inIfPresent(SimpleContractDO::getId, contractIds);
        List<SimpleContractDO> simpleContractDOList = simpleContractMapper.selectList(lambdaQueryWrapperX1);
        Map<String, SimpleContractDO> stringSimpleContractDOMap = CollectionUtils.convertMap(simpleContractDOList, SimpleContractDO::getId);
        //校验未申请金额
        List<String> planIds = resultPageRespVO.getList().stream().map(PayPerformancePageRespVO::getId).collect(Collectors.toList());
        List<PaymentApplScheRelDO> relDOList = paymentApplScheRelMapper.selectList(PaymentApplScheRelDO::getScheduleId,planIds);
        Map<String,List<PaymentApplScheRelDO>> relMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(relDOList)){
            relMap = CollectionUtils.convertMultiMap(relDOList,PaymentApplScheRelDO::getScheduleId);
        }
        Map<String, List<PaymentApplScheRelDO>> finalRelMap = relMap;
        resultPageRespVO.getList().forEach(payPerformancePageRespVO -> {
            SimpleContractDO simpleContractDO = stringSimpleContractDOMap.get(payPerformancePageRespVO.getContractId());
            if (simpleContractDO != null) {
                payPerformancePageRespVO.setContractCode(simpleContractDO.getCode());
                payPerformancePageRespVO.setContractName(simpleContractDO.getName());
                payPerformancePageRespVO.setMoneyTypeName(MoneyTypeEnums.getInstance(payPerformancePageRespVO.getMoneyType()).getInfo());
                payPerformancePageRespVO.setAmountTypeName(PerformanceTypeEnums.getInstance(payPerformancePageRespVO.getAmountType())!= null? AmountTypeEnums.getInstance(payPerformancePageRespVO.getAmountType()).getInfo():null);
                PaymentScheduleStatusEnums statusEnums =PaymentScheduleStatusEnums.getInstance(payPerformancePageRespVO.getStatus());
                if(ObjectUtil.isNotNull(statusEnums)){
                    payPerformancePageRespVO.setStatusName(statusEnums.getInfo());
                }
                if(ObjectUtil.isNull(payPerformancePageRespVO.getPaymentRatio()) && ObjectUtil.isNotNull(payPerformancePageRespVO.getStagePaymentAmount()) && 0!= simpleContractDO.getAmount()){
                    BigDecimal ratio =new BigDecimal(String.valueOf(payPerformancePageRespVO.getStagePaymentAmount())).setScale(2,HALF_UP)
                            .divide(new BigDecimal(String.valueOf(simpleContractDO.getAmount())).setScale(2,HALF_UP)).setScale(2,HALF_UP)
                            .multiply(new BigDecimal(100));
                            ;
                    payPerformancePageRespVO.setPaymentRatio(ratio.doubleValue());
                }
            }
            payPerformancePageRespVO.setFinishInfo(getFinishInfoV2(paymentScheduleDOPageResult));

            //计划时间校验
            if(ObjectUtil.isNotNull(payPerformancePageRespVO.getPaymentDate())){
                try {
                    payPerformancePageRespVO.setPaymentTime(sdf.parse(payPerformancePageRespVO.getPaymentDate()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    log.error("日期转换异常");
                }
            }
            //校验未申请金额
            List<PaymentApplScheRelDO> relDOS = finalRelMap.get(payPerformancePageRespVO.getId());
            Integer ableApply = 1;
            if(CollectionUtil.isNotEmpty(relDOS)){
                BigDecimal appliedAmount = BigDecimal.ZERO;
                appliedAmount = relDOS.stream().map(PaymentApplScheRelDO::getCurrentPayAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO,BigDecimal::add).setScale(2,HALF_UP);
                ableApply = payPerformancePageRespVO.getAmount().compareTo(appliedAmount) > 0 ? 1 : 0;
            }
            payPerformancePageRespVO.setAbleApply(ableApply);

        });

        return resultPageRespVO;
    }
    private String getFinishInfoV2(PageResult<PaymentScheduleDO> paymentScheduleDOPageResult) {
        Integer sum = Math.toIntExact(paymentScheduleDOPageResult.getTotal());
        Integer finished = (int) paymentScheduleDOPageResult.getList().stream()
                .filter(paymentScheduleDO -> Objects.equals(paymentScheduleDO.getStatus(), PaymentScheduleStatusEnums.DONE.getCode()))
                .count();
        String result = finished + "/" + sum;
        return result;
    }

    @Override
    public TotalAmountRespVO totalAmount( PaymentPlanRepVO paymentPlanRepVO) {
        TotalAmountRespVO respVO = new TotalAmountRespVO();
        List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.selectList(paymentPlanRepVO);
        if(CollectionUtil.isEmpty(paymentScheduleDOS)){
            return respVO;
        }
        paymentScheduleDOS.stream()
                // 过滤出不为空的 stagePaymentAmount
                .filter(paymentScheduleDO -> paymentScheduleDO.getStagePaymentAmount() != null)
                .forEach(paymentScheduleDO -> {
                    // 将 stagePaymentAmount 转换成 BigDecimal，并赋值给 amount
                    BigDecimal amount = BigDecimal.valueOf(paymentScheduleDO.getStagePaymentAmount());
                    paymentScheduleDO.setAmount(amount);
                });
        //将金额相加
        BigDecimal sum = paymentScheduleDOS.stream().map(PaymentScheduleDO::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);

        //已完成的
        BigDecimal doneSum = new BigDecimal(0);
        List<PaymentScheduleDO> donePaymentScheduleDOS  = paymentScheduleDOS.stream().filter(plan -> ObjectUtil.isNotNull(plan.getStatus())&& Objects.equals(PaymentScheduleStatusEnums.DONE.getCode(), plan.getStatus())).collect(Collectors.toList());
        if(CollectionUtil.isNotEmpty(donePaymentScheduleDOS)){
            doneSum = donePaymentScheduleDOS.stream().map(PaymentScheduleDO::getAmount).reduce(BigDecimal.ZERO,BigDecimal::add);
        }
        //工程计划金额之和
        Double stageAmountSum =  paymentScheduleDOS.stream().mapToDouble(paymentSchedule ->
                paymentSchedule.getStagePaymentAmount() != null
                        ? paymentSchedule.getStagePaymentAmount()
                        : 0.0)
                .sum();
        BigDecimal todoSum = sum.subtract(doneSum).setScale(2, HALF_UP);

        respVO.setReceivableAmount(sum);
        respVO.setReceivedAmount(doneSum);
        respVO.setUnreceivedAmount(todoSum);
        return respVO;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String insertPerformance(PerformV2SaveReqVO performV2SaveReqVO) {
        //校验款项金额
        checkAmount(performV2SaveReqVO);
        checkPaymentTime(performV2SaveReqVO);
        PaymentScheduleDO paymentScheduleDO = PaymentScheduleConverter.INSTANCE.perf2PaymentScheduleDO(performV2SaveReqVO);
        paymentScheduleMapper.insert(paymentScheduleDO);
        ContractPerformanceLogDO contractPerformanceLogDO = new ContractPerformanceLogDO();
        contractPerformanceLogDO.setBusinessId(paymentScheduleDO.getId());
        contractPerformanceLogDO.setBillId(paymentScheduleDO.getId());
        contractPerformanceLogDO.setModuleName("履约");
        contractPerformanceLogDO.setOperateName("新增了");
        contractPerformanceLogDO.setOperateContent("履约计划");
        contractPerformanceLogMapper.insert(contractPerformanceLogDO);
        return paymentScheduleDO.getId();
    }

    private void checkAmount(PerformV2SaveReqVO performV2SaveReqVO) {
        List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>()
                .eq(PaymentScheduleDO::getContractId, performV2SaveReqVO.getContractId()).orderByAsc(PaymentScheduleDO::getSort));
        //获取合同总金额
        ContractDO contractDO = contractMapper.selectById(performV2SaveReqVO.getContractId());
        if(ObjectUtil.isNotEmpty(contractDO)){
            BigDecimal amount =  BigDecimal.valueOf(contractDO.getAmount());
            if(ObjectUtil.isNotEmpty(paymentScheduleDOS)){
                //获取付款金额总和
                BigDecimal sumAmount = paymentScheduleDOS.stream().map(PaymentScheduleDO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal add = sumAmount.add(performV2SaveReqVO.getAmount());
                //比较合同金额和付款金额总和
                if(amount.compareTo(add)<0){
                    throw exception(ErrorCodeConstants.PAYMENT_EXCEEDS_CONTRACT);
                }
            }else{
                if(amount.compareTo(performV2SaveReqVO.getAmount())<0){
                    throw exception(ErrorCodeConstants.PAYMENT_EXCEEDS_CONTRACT);
                }
            }
        }
    }
    private void checkPaymentTime(PerformV2SaveReqVO performV2SaveReqVO) {
        if(ObjectUtil.isNotEmpty(performV2SaveReqVO.getPaymentTime())){
            if(StringUtils.isEmpty(performV2SaveReqVO.getContractId())){
                PaymentScheduleDO paymentScheduleDO = paymentScheduleMapper.selectById(performV2SaveReqVO.getId());
                if (ObjectUtil.isNotEmpty(paymentScheduleDO)){
                    performV2SaveReqVO.setContractId(paymentScheduleDO.getContractId());
                }
            }
            List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>()
                    .eq(PaymentScheduleDO::getContractId, performV2SaveReqVO.getContractId()).orderByDesc(PaymentScheduleDO::getPaymentTime));
            if(ObjectUtil.isNotEmpty(paymentScheduleDOS)){
                Integer sort = performV2SaveReqVO.getSort();
                //本期计划支付日期必须大于上一期计划支付日期且小于下期计划支付日期
                List<PaymentScheduleDO> nextPaymentScheduleDOS = paymentScheduleDOS.stream().filter(paymentScheduleDO -> Objects.equals(paymentScheduleDO.getSort(), sort + 1)).collect(Collectors.toList());
                List<PaymentScheduleDO> prePaymentScheduleDOS = paymentScheduleDOS.stream().filter(paymentScheduleDO -> Objects.equals(paymentScheduleDO.getSort(), sort - 1)).collect(Collectors.toList());
                if (ObjectUtil.isNotEmpty(prePaymentScheduleDOS)){
                    if (prePaymentScheduleDOS.get(0).getPaymentTime().compareTo(performV2SaveReqVO.getPaymentTime()) >= 0) {
                        throw exception(ErrorCodeConstants.PAYMENT_PAYMENTTIME_EARLY);
                    }
                }
                if (ObjectUtil.isNotEmpty(nextPaymentScheduleDOS)){
                    if (nextPaymentScheduleDOS.get(0).getPaymentTime().compareTo(performV2SaveReqVO.getPaymentTime()) <= 0) {
                        throw exception(ErrorCodeConstants.PAYMENT_PAYMENTTIME_EARLY);
                    }
                }
            }
        }
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String updatePerformance(PerformV2SaveReqVO performV2SaveReqVO) {
        //校验款项金额
        checkAmount(performV2SaveReqVO);
        checkPaymentTime(performV2SaveReqVO);
        PaymentScheduleDO paymentScheduleDO = PaymentScheduleConverter.INSTANCE.perf2PaymentScheduleDO(performV2SaveReqVO);
        paymentScheduleMapper.updateById(paymentScheduleDO);
        ContractPerformanceLogDO contractPerformanceLogDO = new ContractPerformanceLogDO();
        contractPerformanceLogDO.setBusinessId(paymentScheduleDO.getId());
        contractPerformanceLogDO.setBillId(paymentScheduleDO.getId());
        contractPerformanceLogDO.setModuleName("履约");
        contractPerformanceLogDO.setOperateName("编辑了");
        contractPerformanceLogDO.setOperateContent("履约计划");
        contractPerformanceLogMapper.insert(contractPerformanceLogDO);
        return paymentScheduleDO.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deletePerformance(List<String> ids) throws Exception {
        paymentScheduleMapper.deleteBatchIds(ids);
    }

    @Override
    public PayPerformanceDetailRespVO getPerformance(String id) throws Exception {
        LambdaQueryWrapperX<PaymentScheduleDO> lambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        lambdaQueryWrapperX.eq(PaymentScheduleDO::getId, id);
        PaymentScheduleDO paymentScheduleDO = paymentScheduleMapper.selectOne(lambdaQueryWrapperX);
        PayPerformanceDetailRespVO payPerformanceDetailRespVO = PaymentScheduleConverter.INSTANCE.PaymentScheduleDO2perfVO(paymentScheduleDO);
        MoneyTypeEnums moneyTypeEnums=MoneyTypeEnums.getInstance(payPerformanceDetailRespVO.getMoneyType());
        if(ObjectUtil.isNotEmpty(moneyTypeEnums)){
            payPerformanceDetailRespVO.setMoneyTypeName(moneyTypeEnums.getInfo());
        }        payPerformanceDetailRespVO.setAmountTypeName(AmountTypeEnums.getInstance(payPerformanceDetailRespVO.getAmountType()).getInfo());
        payPerformanceDetailRespVO.setConfirmFileList(businessFileMapper.selectByBusinessId(id));
        if(ObjectUtil.isNotEmpty(payPerformanceDetailRespVO.getStatus())){
            payPerformanceDetailRespVO.setStatusName(PaymentScheduleStatusEnums.getInstance(paymentScheduleDO.getStatus()).getInfo());
        }
        SimpleContractDO simpleContractDO = simpleContractMapper.selectById(paymentScheduleDO.getContractId());
        if (simpleContractDO != null) {
            payPerformanceDetailRespVO.setContractCode(simpleContractDO.getCode());
            payPerformanceDetailRespVO.setContractName(simpleContractDO.getName());
        }
        return payPerformanceDetailRespVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmPaymentPlan(PaymentPlanConfirmReqVO paymentPlanConfirmReqVO) throws Exception {
        PaymentScheduleDO paymentScheduleDO = paymentScheduleMapper.selectById(paymentPlanConfirmReqVO.getId());
        if(paymentScheduleDO == null ) {
            throw exception(SYSTEM_ERROR,"找不到该计划");
        }
        if(!PaymentScheduleStatusEnums.CONFIRM.getCode().equals(paymentScheduleDO.getStatus())) {
            throw exception(SYSTEM_ERROR,"状态错误，当前状态不是待确认");
        }
        paymentScheduleDO.setConfirmRemark(paymentPlanConfirmReqVO.getConfirmRemark());
        paymentScheduleDO.setStatus(PaymentScheduleStatusEnums.DONE.getCode());
        paymentScheduleDO.setConfirmTime(LocalDateTime.now());
        paymentScheduleDO.setActualPayTime(new Date());
        paymentScheduleDO.setPaidAmount(paymentScheduleDO.getAmount());
        //更新合同的已支付金额
//        BigDecimal contractPaidAmount = BigDecimal.ZERO;
//        contractPaidAmount.add(paymentScheduleDO.getPaidAmount());
//        contractMapper.update(null,
//                new LambdaUpdateWrapper<ContractDO>().eq(ContractDO::getId, paymentScheduleDO.getContractId()).set(ContractDO::getPaidAmount, contractPaidAmount));
        //已支付金额
        List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.selectList(PaymentScheduleDO::getContractId, paymentScheduleDO.getContractId());
        paymentScheduleDOS.forEach(item -> {
            if(Objects.equals(item.getStatus(), PaymentScheduleStatusEnums.DONE.getCode())) {
                //paymentScheduleDO.setPaidAmount(paymentScheduleDO.getPaidAmount().add(item.getAmount()));
                //如果全额付款 此时已支付金额= 计划金额，如果不是全额付款也不可以用已支付金额+计划金额  否则只可能>= 计划金额
                paymentScheduleDO.setPaidAmount(item.getAmount());
                paymentScheduleDO.setActualPayTime(new Date());
            }
        });
        paymentScheduleMapper.updateById(paymentScheduleDO);
        //保存附件
        List<BusinessFileDO>  fileList =  paymentPlanConfirmReqVO.getConfirmFileList();
        fileList.forEach(businessFileDO -> {
            businessFileDO.setBusinessId(paymentPlanConfirmReqVO.getId());
        });
        businessFileMapper.insertBatch(fileList);
        //保存日志
        ContractPerformanceLogDO contractPerformanceLogDO = new ContractPerformanceLogDO();
        contractPerformanceLogDO.setBusinessId(paymentPlanConfirmReqVO.getId());
        contractPerformanceLogDO.setBillId(paymentPlanConfirmReqVO.getId());
        contractPerformanceLogDO.setModuleName("履约");
        contractPerformanceLogDO.setOperateName("确认了");
        contractPerformanceLogDO.setOperateContent("履约计划");
        contractPerformanceLogMapper.insert(contractPerformanceLogDO);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void confirmPaymentPlanV2(PaymentPlanConfirmReqVO paymentPlanConfirmReqVO) throws Exception {
        // 收款/付款申请确认 修改申请的状态为已确认
        PaymentApplicationDO paymentApplicationDO = paymentApplicationMapper.selectById(paymentPlanConfirmReqVO.getId());
        if(paymentApplicationDO == null ) {
            // 如果付款搜不到则按收款查询确认
            confirmPaymentPlan(paymentPlanConfirmReqVO);
//            throw exception(SYSTEM_ERROR,"找不到该申请");
            return;
        }
        if(!IfNumEnums.NO.getCode().equals(paymentApplicationDO.getStatus())) {
            throw exception(SYSTEM_ERROR,"状态错误，当前状态不是待确认");
        }
        // 计算当前申请所包含的履约计划，如果该履约计划相关的申请金额等于履约计划金额，该履约状态修改为已完成
        paymentApplicationDO.setConfirmRemark(paymentPlanConfirmReqVO.getConfirmRemark());
        paymentApplicationDO.setStatus(IfNumEnums.YES.getCode());
        paymentApplicationDO.setConfirmTime(LocalDateTime.now());
        paymentApplicationDO.setActualPayTime(new Date());

        // 查到当前申请对应的所有的计划
        List<PaymentApplScheRelDO> paymentApplScheRelDOS = paymentApplScheRelMapper.selectList(PaymentApplScheRelDO::getApplicationId, paymentPlanConfirmReqVO.getId());
        Map<String, PaymentApplScheRelDO> scheRelDOMap = CollectionUtils.convertMap(paymentApplScheRelDOS, PaymentApplScheRelDO::getScheduleId);

        List<String> payScheduledIds = paymentApplScheRelDOS.stream().map(PaymentApplScheRelDO::getScheduleId).collect(Collectors.toList());
        List<PaymentScheduleDO> paymentSchedules = paymentScheduleMapper.selectList(PaymentScheduleDO::getId, payScheduledIds);

        for (PaymentScheduleDO scheduleDO : paymentSchedules) {
            BigDecimal paidAmount = ObjectUtil.isEmpty(scheduleDO.getPaidAmount()) ? BigDecimal.ZERO : scheduleDO.getPaidAmount();
            BigDecimal currentPayAmount = scheRelDOMap.get(scheduleDO.getId()).getCurrentPayAmount();
            currentPayAmount = ObjectUtil.isEmpty(currentPayAmount) ? BigDecimal.ZERO : currentPayAmount;
            scheduleDO.setPaidAmount(paidAmount.add(currentPayAmount));
            // 判断金额 修改状态
            if (paidAmount.compareTo(scheduleDO.getAmount()) >= 0){
                scheduleDO.setStatus(PaymentScheduleStatusEnums.DONE.getCode());
            } else {
                scheduleDO.setStatus(PaymentScheduleStatusEnums.PAYED.getCode());
            }
        }
        //修改计划信息
        paymentScheduleMapper.updateBatch(paymentSchedules);
        // 修改申请信息
        paymentApplicationMapper.updateById(paymentApplicationDO);
        //保存附件
        List<BusinessFileDO> fileList =  paymentPlanConfirmReqVO.getConfirmFileList();
        if (CollectionUtil.isNotEmpty(fileList)){
            fileList.forEach(businessFileDO -> {
                businessFileDO.setBusinessId(paymentPlanConfirmReqVO.getId());
            });
        }
        businessFileMapper.saveOrUpdateBatch(fileList);
        //保存日志
        ContractPerformanceLogDO contractPerformanceLogDO = new ContractPerformanceLogDO();
        contractPerformanceLogDO.setBusinessId(paymentPlanConfirmReqVO.getId());
        contractPerformanceLogDO.setBillId(paymentPlanConfirmReqVO.getId());
        contractPerformanceLogDO.setModuleName("履约");
        contractPerformanceLogDO.setOperateName("确认了");
        contractPerformanceLogDO.setOperateContent("履约申请");
        contractPerformanceLogMapper.insert(contractPerformanceLogDO);
    }
    @Override
    public List<ContractPerformanceLogDO> queryPerformanceLogs(String performanceId) {
        LambdaQueryWrapperX<ContractPerformanceLogDO> lambdaQueryWrapperX = new LambdaQueryWrapperX();
        lambdaQueryWrapperX.eq(ContractPerformanceLogDO::getBusinessId, performanceId);
        lambdaQueryWrapperX.orderByDesc(ContractPerformanceLogDO::getCreateTime);
        List<ContractPerformanceLogDO> performanceLogDOList = contractPerformanceLogMapper.selectList(lambdaQueryWrapperX);
        if (CollectionUtil.isNotEmpty(performanceLogDOList)) {
            DataPermissionUtils.executeIgnore(()->{
                List userIdList = performanceLogDOList.stream().map(ContractPerformanceLogDO::getCreator).collect(Collectors.toList());
                List adminUserRespDTOList = userApi.getUserList(userIdList);
                Map<Long, AdminUserRespDTO> userRespDTOMap = CollectionUtils.convertMap(adminUserRespDTOList, AdminUserRespDTO::getId);
                for (ContractPerformanceLogDO contractPerformanceLogDO : performanceLogDOList) {
                    AdminUserRespDTO user = userRespDTOMap.get(Long.valueOf(contractPerformanceLogDO.getCreator()));
                    contractPerformanceLogDO.setUserName(user.getNickname());
                }
            });

        }

        return performanceLogDOList;
    }

    private void enhancePage(PageResult<PaymentSchedulePageRespVO> resultPageRespVO) {

        List<PaymentSchedulePageRespVO> list = resultPageRespVO.getList();
        if (CollectionUtil.isNotEmpty(list)) {
            //合同类型
            List<ContractType> contractTypeList = contractTypeMapper.selectList();
            Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
            //创建人
            List<AdminUserRespDTO> adminUserRespDTOList = userApi.getUserList();
            Map<Long, AdminUserRespDTO> userRespDTOMap = CollectionUtils.convertMap(adminUserRespDTOList, AdminUserRespDTO::getId);
            //合同
            List<String> contractIdList = list.stream().map(PaymentSchedulePageRespVO::getId).collect(Collectors.toList());
            List<SimpleContractDO> contractDOList = simpleContractMapper.selectList(new LambdaQueryWrapperX<SimpleContractDO>().in(SimpleContractDO::getId, contractIdList));
            Map<String, SimpleContractDO> contractDOMap = CollectionUtils.convertMap(contractDOList, SimpleContractDO::getId);

//            Map<String, List<String>> nameForContractMap = getNameList(contractIdList);

            //支付计划(已付)
            List<PaymentScheduleDO> planList = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>()
                    .in(PaymentScheduleDO::getContractId, contractIdList)
                    .eq(PaymentScheduleDO::getStatus, PaymentScheduleStatusEnums.PAYED.getCode())
            );
            Map<String, PaymentScheduleDO> planMap = CollectionUtils.convertMap(planList, PaymentScheduleDO::getId);
            Map<String, List<PaymentScheduleDO>> planByContractIdMap = planList.stream()
                    .collect(Collectors.groupingBy(PaymentScheduleDO::getContractId));
            //相对方
//            List<Relative> relativeList = relativeMapper.selectList();
//            Map<String, Relative> relativeMap = CollectionUtils.convertMap(relativeList, Relative::getId);
            Map<String, List<String>> relativeMap = new HashMap<String, List<String>>();

            List<String> contractIds = contractIdList;
            //6.2 获取签署方ids
            List<String> signatoryIds = signatoryRelMapper.selectList(new LambdaQueryWrapperX<SignatoryRelDO>().inIfPresent(SignatoryRelDO::getContractId, contractIds)).stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toList());
            //6.3 获取相对方信息
            List<Relative> relativeList = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(signatoryIds)) {
                relativeList = relativeMapper.selectBatchIds(signatoryIds);
            }

            // 7.获取签署方信息-个人
            List<String> creatorIdsStr = contractDOList.stream().map(SimpleContractDO::getCreator).collect(Collectors.toList());
            List<Long> creatorIds = creatorIdsStr.stream().map(Long::valueOf).collect(Collectors.toList());
            //7.1 根据创建人ids获取公司信息
            List<UserCompanyInfoRespDTO> userCompanyInfoList = companyApi.getUserCompanyInfoList(creatorIds);
            Map<Long, UserCompanyInfoRespDTO> userCompanyMap = CollectionUtils.convertMap(userCompanyInfoList, UserCompanyInfoRespDTO::getUserId);

            //7.2 发起方是个体，通过id 获取用户 nickname
            List<AdminUserRespDTO> users = userApi.getUserList(userCompanyInfoList.stream().map(UserCompanyInfoRespDTO::getUserId).collect(Collectors.toList()));
            Map<Long, AdminUserRespDTO> userListByDeptMap = CollectionUtils.convertMap(users, AdminUserRespDTO::getId);

            for (PaymentSchedulePageRespVO resultVO : list) {
                //封装履约状态
                Date date = new Date();
                // 待履约：生效晚于现在；
                // 履约中：现在早于终止，晚于生效；
                // 履约完成：现在不早于终止日期
                //有效时间在当前时间之前-状态为待履约
                if (resultVO.getValidity0().getTime() > date.getTime()) {
                    resultVO.setStatusName("待履约");
                } else if (resultVO.getValidity1().getTime() > date.getTime() && resultVO.getValidity0().getTime() <= date.getTime()) {
                    resultVO.setStatusName("履约中");
                } else if (resultVO.getValidity1().getTime() <= date.getTime()) {
                    resultVO.setStatusName("履约完成");
                }
                resultVO.setContractId(resultVO.getId());
                if (ObjectUtil.isNotNull(resultVO.getId())) {
                    //找到对应的合同
                    SimpleContractDO contractDO = contractDOMap.get(resultVO.getContractId());
                    if (ObjectUtil.isNotNull(contractDO)) {
                        ContractType contractType = contractTypeMap.get(contractDO.getContractType());
                        if (ObjectUtil.isNotNull(contractType)) {
                            resultVO.setContractType(contractType.getId());
                            resultVO.setContractTypeName(contractType.getName());
                        }
                        resultVO.setCode(contractDO.getCode());

                        //签署方信息
                        List<String> signatoryNameList = signatoryNameById(relativeList, userCompanyMap, resultVO.getContractId(), contractDOMap, userListByDeptMap);
                        List<SignatoryRespVO> signatoryRespVOList = new ArrayList<>();
                        for (String s : signatoryNameList) {
                            signatoryRespVOList.add(new SignatoryRespVO().setSignatory(s));
                        }
                        resultVO.setSignatoryRespVOList(signatoryRespVOList);
                    }
                    //找到对应的付款计划
                    List<PaymentScheduleDO> payedPlans = planByContractIdMap.get(resultVO.getContractId());
                    BigDecimal ratio = BigDecimal.ZERO;
                    BigDecimal totalPayedAmount = BigDecimal.ZERO;
                    if (CollectionUtil.isNotEmpty(payedPlans)) {
                        totalPayedAmount = payedPlans.stream()
                                .map(PaymentScheduleDO::getAmount)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        ratio = totalPayedAmount.divide(new BigDecimal(contractDO.getAmount()), 4, HALF_UP);
                    }
                    resultVO.setPayedAmount(totalPayedAmount);
                    resultVO.setPaymentRatio(ratio);
                }
            }
        }
    }



    //根据合同id 获取签署方名称
    private List<String> signatoryNameById(List<Relative> relativeList, Map<Long, UserCompanyInfoRespDTO> userCompanyMap, String id,
                                           Map<String, SimpleContractDO> contractMap, Map<Long, AdminUserRespDTO> userListByDeptMap) {
        List<String> signatoryNameList = new ArrayList<>();
        SimpleContractDO contractDO = contractMap.get(id);
        if (ObjectUtil.isNotNull(contractDO)) {
            UserCompanyInfoRespDTO companyInfoRespDTO = userCompanyMap.get(Long.valueOf(contractDO.getCreator()));
            if (ObjectUtil.isNotNull(companyInfoRespDTO)) {
                signatoryNameList.add(companyInfoRespDTO.getName());

            } else {
                //发起方是个体，通过id 获取nickname
                AdminUserRespDTO userRespDTO = userListByDeptMap.get(contractDO.getCreator());
                if (ObjectUtil.isNotNull(userRespDTO)) {
                    signatoryNameList.add(userRespDTO.getNickname());
                }
            }

            for (Relative relative : relativeList) {
                if (!relative.getCompanyName().isEmpty()) {
                    signatoryNameList.add(relative.getCompanyName());
                } else {
                    signatoryNameList.add(relative.getName());
                }
            }
        }

        return signatoryNameList;
    }


    @Override
    public String publish(String id) {
        // 找到合同的金额
        ContractDO contractDO = contractMapper.selectById(id);
        if (ObjectUtil.isNull(contractDO)) {
            throw exception(SYSTEM_ERROR, "该合同不存在");
        }
        // 找到合同相关的计划金
        Double contractMoney = contractDO.getAmount();
        BigDecimal contractMoneyBD = new BigDecimal(Double.toString(contractMoney));

        // 计划的总金额是否等于合同金额
        BigDecimal totalScheduleAmount = new BigDecimal(0);
        List<PaymentScheduleDO> scheduleDOList = paymentScheduleMapper.selectList(PaymentScheduleDO::getContractId, id);
        if (CollectionUtil.isNotEmpty(scheduleDOList)) {
            totalScheduleAmount = scheduleDOList.stream().map(PaymentScheduleDO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        if (totalScheduleAmount.doubleValue() == contractMoney) {
            //金额相等，则修改合同状态和计划状态
            contractDO.setStatus(ContractStatusEnums.PERFORMING.getCode());
            contractMapper.updateById(contractDO);
            for (PaymentScheduleDO scheduleDO : scheduleDOList) {
                scheduleDO.setStatus(PaymentScheduleStatusEnums.TO_DO.getCode());
            }
            paymentScheduleMapper.updateBatch(scheduleDOList);
            savePerformanceLog(new ContractPerformanceLogDO()
                    .setModuleName(PAY_APPLICATION.getInfo()).setBusinessId(id).setBillId(id).setOperateName("发布了").setOperateContent("履约计划")
            );
        } else {
            // 相差金额的提醒
            BigDecimal differenceAmount = totalScheduleAmount.subtract(contractMoneyBD);
            if (differenceAmount.compareTo(BigDecimal.ZERO) < 0) {
                // 履约计划低于合同
                throw exception(SYSTEM_ERROR, "履约计划总金额低于合同金额 " + contractMoneyBD.subtract(totalScheduleAmount) + "元,请确认金额");
            } else {
                // 履约计划超出合同
                throw exception(SYSTEM_ERROR, "履约计划总金额超出合同金额 " + totalScheduleAmount + "元,请确认金额");
            }
        }

        return "success";
    }

    @Override
    public String savePerformanceLog(ContractPerformanceLogDO contractPerformanceLogDO) {
        contractPerformanceLogMapper.insert(contractPerformanceLogDO);
        return contractPerformanceLogDO.getId();
    }

    @Override
    public void saveBatchPerformanceLog(List<ContractPerformanceLogDO> contractPerformanceLogDOs) {
        contractPerformanceLogMapper.insertBatch(contractPerformanceLogDOs);
    }

    @Override
    public DashboardContractCountRespVO queryPerformanceTopCount() {

        //查询履约中的合同数量  
        Long  performanceCount = simpleContractMapper.selectCount(new LambdaQueryWrapper<SimpleContractDO>().eq(SimpleContractDO::getStatus,ContractStatusEnums.PERFORMING.getCode()));
        //查询风险合同数量
        LambdaQueryWrapperX<SimpleContractDO> lambdaQueryWrapperX = new LambdaQueryWrapperX();
        // 履约风险、履约争议、履约暂停、履约延期、履约逾期
        lambdaQueryWrapperX.in(SimpleContractDO::getStatus,
                ContractStatusEnums.PERFORMANCE_RISK.getCode(),
                ContractStatusEnums.PERFORMANCE_RISK_DISPUTE.getCode(),
                ContractStatusEnums.PERFORMANCE_RISK_PAUSE.getCode(),
                ContractStatusEnums.PERFORMANCE_RISK_EXTENSION.getCode(),
                ContractStatusEnums.PERFORMANCE_RISK_OVERDUE.getCode());
        Long riskCount = simpleContractMapper.selectCount(lambdaQueryWrapperX);

        //查询当前用户审批代办数量
        //查询收款代办数量
        List<ContractProcessInstanceRelationInfoRespDTO> invoiceDTOList =  bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.ECMS_CONTRACT_INVOICE.getDefinitionKey());
        //查询付款代办数量
        List<ContractProcessInstanceRelationInfoRespDTO> payDTOList =  bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.PAYMENT_APPLICATION_APPROVE.getDefinitionKey());
        //查询延期付款代办数量
        List<ContractProcessInstanceRelationInfoRespDTO> DEFERREDTOList =  bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.PAYMENT_PLAN_DEFERRED_APPLICATION.getDefinitionKey());


        DashboardContractCountRespVO dashboardContractCountRespVO = new DashboardContractCountRespVO();
        dashboardContractCountRespVO.setContractRiskCount(riskCount.intValue());
        dashboardContractCountRespVO.setContractPerformingCount(performanceCount.intValue());
        dashboardContractCountRespVO.setPendingApprovalCount(invoiceDTOList.size()+payDTOList.size()+DEFERREDTOList.size());

        return dashboardContractCountRespVO;
    }

    @Override
    @DataPermission(enable = false)
    public DashboardPerformanceMoneyRespVO queryPerformanceMoney(DashboardPerformanceMoneyReqVO dashboardPerformanceMoneyReqVO) {
        LambdaQueryWrapperX<PaymentScheduleDO> lambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        lambdaQueryWrapperX.select(PaymentScheduleDO::getStatus,PaymentScheduleDO::getAmountType,PaymentScheduleDO::getPaidAmount,PaymentScheduleDO::getAmount);
        LocalDate firstDay = LocalDate.of(dashboardPerformanceMoneyReqVO.getYear(),dashboardPerformanceMoneyReqVO.getMonth(),1);
        LocalDate endDay = YearMonth.of(dashboardPerformanceMoneyReqVO.getYear(),dashboardPerformanceMoneyReqVO.getMonth()).atEndOfMonth();
        lambdaQueryWrapperX.le(PaymentScheduleDO::getPaymentTime,endDay);
        lambdaQueryWrapperX.ge(PaymentScheduleDO::getPaymentTime,firstDay);

        //未开始、执行中、待确认、已完成
        lambdaQueryWrapperX.in(PaymentScheduleDO::getStatus,PaymentScheduleStatusEnums.TO_DO.getCode(),
                PaymentScheduleStatusEnums.DOING.getCode(),
                PaymentScheduleStatusEnums.CONFIRM.getCode(),
                PaymentScheduleStatusEnums.DONE.getCode()
                //已关闭的不计入
                //, PaymentScheduleStatusEnums.CLOSE.getCode()
                );
        List<PaymentScheduleDO> paymentScheduleDOList =  paymentScheduleMapper.selectList(lambdaQueryWrapperX);
        BigDecimal paidMoney = new BigDecimal(0);
        BigDecimal unpaidMoney = new BigDecimal(0);
        BigDecimal collectedMoney = new BigDecimal(0);
        BigDecimal uncollectedMoney = new BigDecimal(0);

        for (PaymentScheduleDO paymentScheduleDO : paymentScheduleDOList) {
            if(AmountTypeEnums.PAY.getCode().equals(paymentScheduleDO.getAmountType())){
                //付款
                if(paymentScheduleDO.getPaidAmount().compareTo(BigDecimal.ZERO)>0){
                    paidMoney =paidMoney.add(paymentScheduleDO.getAmount());
                    unpaidMoney=unpaidMoney.add(paymentScheduleDO.getAmount().subtract(paymentScheduleDO.getPaidAmount()));
                }else{
                    unpaidMoney= unpaidMoney.add(paymentScheduleDO.getAmount());
                }
//                if(paymentScheduleDO.getStatus().equals(PaymentScheduleStatusEnums.DONE.getCode())){
//                    paidMoney.add(paymentScheduleDO.getAmount());
//                    //unpaidMoney.add(paymentScheduleDO.getAmount().divide(paymentScheduleDO.getPaidAmount()));
//                }else{
//                    unpaidMoney.add(paymentScheduleDO.getAmount());
//                }
            }else if(AmountTypeEnums.RECEIPT.getCode().equals(paymentScheduleDO.getAmountType())){
                //收款
                if(paymentScheduleDO.getPaidAmount().compareTo(BigDecimal.ZERO)>0){
                    collectedMoney = collectedMoney.add(paymentScheduleDO.getAmount());
                    uncollectedMoney = uncollectedMoney.add(paymentScheduleDO.getAmount().subtract(paymentScheduleDO.getPaidAmount()));
                }else{
                    uncollectedMoney = uncollectedMoney.add(paymentScheduleDO.getAmount());
                }
            }
        }
        DashboardPerformanceMoneyRespVO dashboardPerformanceMoneyRespVO = new DashboardPerformanceMoneyRespVO();
        dashboardPerformanceMoneyRespVO.setCollectedMoney(collectedMoney.intValue()).setUnCollectedMoney(uncollectedMoney.intValue())
                .setPaidMoney(paidMoney.intValue()).setUnpaidMoney(unpaidMoney.intValue())
                .setTotalCollectPlanMoney(collectedMoney.add(uncollectedMoney).intValue())
                .setTotalPayPlanMoney(paidMoney.add(unpaidMoney).intValue());

        return dashboardPerformanceMoneyRespVO;
    }

    @Override
    public DashboardContractStatusCountRespVO queryContractStatusCount() {
        LambdaQueryWrapperX<SimpleContractDO> lambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        QueryWrapper<SimpleContractDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("status , count(*) as count").groupBy("status");
        queryWrapper.in("status",ContractStatusEnums.SIGN_COMPLETED.getCode(),
                ContractStatusEnums.PERFORMANCE_CLOSURE.getCode(),ContractStatusEnums.PERFORMING.getCode(),
                ContractStatusEnums.PERFORMANCE_COMPLETE.getCode(),
                ContractStatusEnums.PERFORMANCE_RISK.getCode(),
                ContractStatusEnums.PERFORMANCE_RISK_DISPUTE.getCode(),
                ContractStatusEnums.PERFORMANCE_RISK_PAUSE.getCode(),
                ContractStatusEnums.PERFORMANCE_RISK_EXTENSION.getCode(),
                ContractStatusEnums.PERFORMANCE_RISK_OVERDUE.getCode());
        List<Map<String,Object>> statusCountList=simpleContractMapper.selectMaps(queryWrapper);

        DashboardContractStatusCountRespVO dashboardContractStatusCountRespVO = new DashboardContractStatusCountRespVO();
        int totalContractCount=0, contractColseCount=0,contractRiskCount=0,contractPerformanceComplateCount=0,contractUnStartCount = 0;
        int contractRiskDisputeCount=0,contractRiskPauseCount=0,contractRiskExtensionCount=0,contractRiskOverdueCount=0;

        for(Map<String,Object> map:statusCountList){
           if(ContractStatusEnums.PERFORMANCE_CLOSURE.getCode().equals(map.get("status"))){
               //履约关闭
               contractColseCount += ((Long)map.get("count")).intValue();
           }else if(ContractStatusEnums.PERFORMANCE_RISK.getCode().equals(map.get("status"))){
               //履约风险--有更新的划分  理论上不会有此状态存在
               contractRiskCount += ((Long)map.get("count")).intValue();
           }else if(ContractStatusEnums.PERFORMING.getCode().equals(map.get("status"))){
               //履约中
               contractUnStartCount +=  ((Long)map.get("count")).intValue();
           }else if(ContractStatusEnums.SIGN_COMPLETED.getCode().equals(map.get("status"))){
               //签署完成--未开始
               contractUnStartCount +=  ((Long)map.get("count")).intValue();
           }else if (ContractStatusEnums.PERFORMANCE_COMPLETE.getCode().equals(map.get("status"))){
               //履约完成
               contractPerformanceComplateCount +=  ((Long)map.get("count")).intValue();
           }else if(ContractStatusEnums.PERFORMANCE_RISK_DISPUTE.getCode().equals(map.get("status"))){
               //履约争议
               contractRiskDisputeCount +=  ((Long)map.get("count")).intValue();
           }else if(ContractStatusEnums.PERFORMANCE_RISK_PAUSE.getCode().equals(map.get("status"))){
               //履约暂停
               contractRiskPauseCount += ((Long)map.get("count")).intValue();
           }else if(ContractStatusEnums.PERFORMANCE_RISK_EXTENSION.getCode().equals(map.get("status"))){
               //履约延期
               contractRiskExtensionCount +=  ((Long)map.get("count")).intValue();
           }else if(ContractStatusEnums.PERFORMANCE_RISK_OVERDUE.getCode().equals(map.get("status"))){
               //履约逾期
               contractRiskOverdueCount +=  ((Long)map.get("count")).intValue();
           }
           totalContractCount +=  ((Long)map.get("count")).intValue();
        }
        dashboardContractStatusCountRespVO.setTotalContractCount(totalContractCount);
        dashboardContractStatusCountRespVO.setContractColseCount(contractColseCount);
        dashboardContractStatusCountRespVO.setContractRiskCount(contractRiskCount);
        dashboardContractStatusCountRespVO.setContractPerformanceComplateCount(contractPerformanceComplateCount);
        dashboardContractStatusCountRespVO.setContractUnStartCount(contractUnStartCount);
        dashboardContractStatusCountRespVO.setContractRiskDisputeCount(contractRiskDisputeCount);
        dashboardContractStatusCountRespVO.setContractRiskPauseCount(contractRiskPauseCount);
        dashboardContractStatusCountRespVO.setContractRiskExtensionCount(contractRiskExtensionCount);
        dashboardContractStatusCountRespVO.setContractRiskOverdueCount(contractRiskOverdueCount);

        return dashboardContractStatusCountRespVO;
    }

    @Override
    public List<Map<String,Object>> queryPerformanceList() {
        LocalDate nowDate = LocalDate.now();
        //获取前一天开始时间
        LocalDate beginDate = nowDate.minusDays(1);
        //构建返回结果集
        List<Map<String,Object>> resultList = new ArrayList<>();
        //获取五天后的结束时间
        LocalDate fiveDaysLater = nowDate.plusDays(5);
        List<LocalDate> localDateList = new ArrayList<>();
        localDateList.add(beginDate);
        localDateList.add(nowDate);
        for (int i = 1; i <= 5; i++) {
            LocalDate nextDate = nowDate.plusDays(i);
            localDateList.add(nextDate);
        }


        LambdaQueryWrapperX<PaymentScheduleDO> lambdaQueryWrapperX = new LambdaQueryWrapperX();
        //拼接时间查询条件
        lambdaQueryWrapperX.in(PaymentScheduleDO::getPaymentTime, localDateList);
        //拼接状态查询条件，只查询已发布后状态的
        lambdaQueryWrapperX.in(PaymentScheduleDO::getStatus, PaymentScheduleStatusEnums.PAYED.getCode(), PaymentScheduleStatusEnums.CONFIRM.getCode(),
                PaymentScheduleStatusEnums.DONE.getCode(), PaymentScheduleStatusEnums.CLOSE.getCode());
        lambdaQueryWrapperX.orderByAsc(PaymentScheduleDO::getPaymentTime);

        List<PaymentScheduleDO> paymentScheduleDOList =  paymentScheduleMapper.selectList(lambdaQueryWrapperX);
        if(paymentScheduleDOList.size() == 0){
            for(LocalDate localDate:localDateList){
                Map temp = new HashMap();
                temp.put("time",localDate.toString());
                temp.put("data",new ArrayList<>());
                resultList.add(temp);
            }
            return resultList;
        }
        List<PayPerformancePageRespVO> payPerformancePageRespVOList =  PaymentScheduleConverter.INSTANCE.paymentDOList2VOList(paymentScheduleDOList);
        //拼接合同信息内容
        List<String> contractIds = payPerformancePageRespVOList.stream().map(PayPerformancePageRespVO::getContractId).collect(Collectors.toList());
        LambdaQueryWrapperX<SimpleContractDO> lambdaQueryWrapperX1 = new LambdaQueryWrapperX();
        lambdaQueryWrapperX1.in(SimpleContractDO::getId, contractIds);
        List<SimpleContractDO> simpleContractDOList = simpleContractMapper.selectList(lambdaQueryWrapperX1);
        Map<String, SimpleContractDO> stringSimpleContractDOMap = CollectionUtils.convertMap(simpleContractDOList, SimpleContractDO::getId);
        payPerformancePageRespVOList.forEach(payPerformancePageRespVO -> {
            SimpleContractDO simpleContractDO = stringSimpleContractDOMap.get(payPerformancePageRespVO.getContractId());
            if (simpleContractDO != null) {
                payPerformancePageRespVO.setContractCode(simpleContractDO.getCode());
                payPerformancePageRespVO.setContractName(simpleContractDO.getName());
                payPerformancePageRespVO.setMoneyTypeName(MoneyTypeEnums.getInstance(payPerformancePageRespVO.getMoneyType()).getInfo());
                payPerformancePageRespVO.setAmountTypeName(PerformanceTypeEnums.getInstance(payPerformancePageRespVO.getAmountType()).getInfo());
            }
        });

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String,List<PayPerformancePageRespVO>> dateListMap =  payPerformancePageRespVOList.stream().collect(Collectors.groupingBy(
                item -> dateFormat.format(item.getPaymentTime())
        ));
        for(LocalDate localDate:localDateList){
            Map temp = new HashMap();
            temp.put("time",localDate.toString());
           if(dateListMap.containsKey(localDate.toString())){
               temp.put("data",dateListMap.get(localDate.toString()));
           }else{
               temp.put("data",new ArrayList<>());
           }
           resultList.add(temp);
        }
        return resultList;
    }

    @Override
    public List<DashboardContractRiskRespVO> queryRiskContractList() {
        LambdaQueryWrapperX<SimpleContractDO> lambdaQueryWrapperX= new LambdaQueryWrapperX();
        lambdaQueryWrapperX.select(SimpleContractDO::getId,SimpleContractDO::getCode,SimpleContractDO::getName,SimpleContractDO::getAmountType,SimpleContractDO::getStatus,SimpleContractDO::getRiskDate);
        lambdaQueryWrapperX.in(SimpleContractDO::getStatus, ContractStatusEnums.PERFORMANCE_RISK.getCode(),
                ContractStatusEnums.PERFORMANCE_RISK_DISPUTE.getCode(),
                ContractStatusEnums.PERFORMANCE_RISK_PAUSE.getCode(),
                ContractStatusEnums.PERFORMANCE_RISK_EXTENSION.getCode(),
                ContractStatusEnums.PERFORMANCE_RISK_OVERDUE.getCode());
        lambdaQueryWrapperX.orderByAsc(SimpleContractDO::getRiskDate);
        PageParam pageParam = new PageParam();
        pageParam.setPageNo(1);
        pageParam.setPageSize(5);
        PageResult<SimpleContractDO> simpleContractDOPageResult =  simpleContractMapper.selectPage(pageParam,lambdaQueryWrapperX);
        //List<SimpleContractDO> simpleContractDOList = simpleContractMapper.selectList(lambdaQueryWrapperX);
        List<SimpleContractDO> simpleContractDOList = simpleContractDOPageResult.getList();
        List<DashboardContractRiskRespVO> dashboardPerformanceMoneyRespVOList = new ArrayList<>();
        LocalDate nowDate = LocalDate.now();
        simpleContractDOList.forEach(simpleContractDO -> {
            DashboardContractRiskRespVO dashboardContractRiskRespVO = new DashboardContractRiskRespVO();
            dashboardContractRiskRespVO.setId(simpleContractDO.getId());
            dashboardContractRiskRespVO.setCode(simpleContractDO.getCode());
            dashboardContractRiskRespVO.setName(simpleContractDO.getName());
            dashboardContractRiskRespVO.setStatus(simpleContractDO.getStatus());
            dashboardContractRiskRespVO.setStatusName(ContractStatusEnums.getInstance(simpleContractDO.getStatus()).getDesc());
            dashboardContractRiskRespVO.setAmountType(simpleContractDO.getAmountType());
            dashboardContractRiskRespVO.setRiskDate(simpleContractDO.getRiskDate());
            if(dashboardContractRiskRespVO.getRiskDate() != null ){
                LocalDate riskLocalDate = dashboardContractRiskRespVO.getRiskDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                long daysBetween = ChronoUnit.DAYS.between(riskLocalDate, nowDate);
                dashboardContractRiskRespVO.setRiskDay((int)daysBetween);
            }

            //
            AmountTypeEnums amountTypeEnums = AmountTypeEnums.getInstance(simpleContractDO.getAmountType());
            if(amountTypeEnums != null){
                dashboardContractRiskRespVO.setAmountTypeName(amountTypeEnums.getInfo());
            }
            dashboardPerformanceMoneyRespVOList.add(dashboardContractRiskRespVO);
        });

        return dashboardPerformanceMoneyRespVOList;
    }



    @Override
    public List<DashboardContractComplateRespVO> queryContractComplate(String year) {
        LambdaQueryWrapperX<SimpleContractDO> lambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        lambdaQueryWrapperX.select(SimpleContractDO::getPerformanceCompleteDate).eq(SimpleContractDO::getStatus,ContractStatusEnums.PERFORMANCE_COMPLETE.getCode()).like(SimpleContractDO::getPerformanceCompleteDate,year);
        List<SimpleContractDO> simpleContractDOList = simpleContractMapper.selectList(lambdaQueryWrapperX);
        List<DashboardContractComplateRespVO> dashboardContractComplateRespVOList = new ArrayList<>();
        Map<Integer,Integer> tempMap = new HashMap();
        simpleContractDOList.forEach(simpleContractDO -> {
            int month = simpleContractDO.getPerformanceCompleteDate().getMonth() + 1;
            if(tempMap.containsKey(month)){
                tempMap.put(month,tempMap.get(month)+1);
            }else{
                tempMap.put(month,1);
            }
        });
        //构建12个月的数据
        for(int i=1;i<= 12 ;i++){
            DashboardContractComplateRespVO dashboardContractComplateRespVO = new DashboardContractComplateRespVO();
            dashboardContractComplateRespVO.setMonth(i);
            dashboardContractComplateRespVO.setPerformanceComplateCount(tempMap.get(i) == null ? 0 : tempMap.get(i));
            dashboardContractComplateRespVOList.add(dashboardContractComplateRespVO);
        }

        return dashboardContractComplateRespVOList;
    }


//
//    /**
//     * 获得相对方信息
//     * Map<合同id, 相对方名字List>
//     * List里两个String
//     * 第一个是我方签署方
//     * 第二个是相对方名字
//     */
//    private Map<String, List<String>> getNameList(List<String> contractIdList) {
//
//        List<ContractDO> contractDOList = new ArrayList<ContractDO>();
//        for (String s : contractIdList) {
//            ContractDO contractDO = new ContractDO().setId(s);
//            contractDOList.add(contractDO);
//        }
//        PageResult<ContractDO> contractDOPageResult = new PageResult<ContractDO>().setList(contractDOList);
//        //封装展示集合结果
//        PageResult<ContractPageRespVO> result = getContractPageRespVOPageResult(contractDOPageResult);
//        List<ContractPageRespVO> respVOList = result.getList();
//        Map<String, ContractPageRespVO> contractMap = CollectionUtils.convertMap(respVOList, ContractPageRespVO::getId);
//        Map<String, List<String>> resultMap = new HashMap<String, List<String>>();
//        for (Map.Entry<String, ContractPageRespVO> entry : contractMap.entrySet()) {
//            List<String> names = entry.getValue().getSignatoryList();
//            resultMap.put(entry.getKey(), names);
//        }
//        return resultMap;
//    }
//
//    /**
//     * 获得相对方信息
//     */
//    private PageResult<ContractPageRespVO> getContractPageRespVOPageResult(PageResult<ContractDO> contractDOPageResult) {
//        //封装展示集合结果
//        PageResult<ContractPageRespVO> result = ContractConverter.INSTANCE.convertPage(contractDOPageResult);
//        try {
//            packageName(result);
//            return result;
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.error("付款管理，获得相对方信息异常");
//        }
//        return result;
//    }
//
//    private void packageName(PageResult<ContractPageRespVO> result) {
//
//        if (CollectionUtil.isNotEmpty(result.getList())) {
//            List<Integer> categoryIds = result.getList().stream().map(ContractPageRespVO::getContractCategory).filter(Objects::nonNull).collect(Collectors.toList());
//            List<ContractCategory> contractCategories = contractCategoryMapper.selectList(ContractCategory::getId, categoryIds);
//            Map<Integer, ContractCategory> contractCategoryMap = CollectionUtils.convertMap(contractCategories, ContractCategory::getId);
//
//            List<String> typeIds = result.getList().stream().map(ContractPageRespVO::getContractType).filter(StringUtils::isNotBlank).collect(Collectors.toList());
//            List<ContractType> contractTypes = contractTypeMapper.selectList(ContractType::getId, typeIds);
//            Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypes, ContractType::getId);
//
//            List<String> contractIds = result.getList().stream().map(ContractPageRespVO::getId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
//            List<SignatoryRelDO> signatoryRelations = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractIds);
//
//            Map<String, List<SignatoryRelDO>> contractRelationMap = CollectionUtils.convertMultiMap(signatoryRelations, SignatoryRelDO::getContractId);
//
//            List<String> relationDataIds = signatoryRelations.stream().map(SignatoryRelDO::getSignatoryId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
//            List<Relative> relatives = relativeMapper.selectBatchIds(relationDataIds);
//            Map<String, Relative> relativeMap = CollectionUtils.convertMap(relatives, Relative::getId);
//
//            List<Long> creatorIds = result.getList().stream().map(item -> Long.valueOf(item.getCreator())).collect(Collectors.toList());
//
//            List<AdminUserRespDTO> userInfo = userApi.getUserList(creatorIds);
//            Map<Long, AdminUserRespDTO> longAdminUserRespDTOMap = CollectionUtils.convertMap(userInfo, AdminUserRespDTO::getId);
//            List<UserCompanyInfoRespDTO> userCompanyInfoList = companyApi.getUserCompanyInfoList(creatorIds);
//            Map<Long, UserCompanyInfoRespDTO> companyInfoRespDTOMap = userCompanyInfoList.stream().collect(Collectors.toMap(UserCompanyInfoRespDTO::getUserId, Function.identity(), (v1, v2) -> v2));
//
//            result.getList().forEach(item -> {
//
//                // 更新ContractPageRespVO对象的contractCategoryName
//                ContractCategory category = contractCategoryMap.get(item.getContractCategory());
//                if (category != null) {
//                    item.setContractCategoryName(category.getName());
//                }
//                ContractType contractType = contractTypeMap.get(item.getContractType());
//                if (contractType != null) {
//                    item.setContractTypeName(contractType.getName());
//                }
//                AdminUserRespDTO adminUserRespDTO = longAdminUserRespDTOMap.get(Long.valueOf(item.getCreator()));
//                if (adminUserRespDTO != null) {
//                    item.setCreatorName(adminUserRespDTO.getNickname());
//                }
//                UserCompanyInfoRespDTO companyInfo = companyInfoRespDTOMap.get(Long.valueOf(item.getCreator()));
//                if (companyInfo != null) {
//                    item.setInitiator(companyInfo.getName());
//                }
//                List<SignatoryRelDO> signatoryRelDOS = contractRelationMap.get(item.getId());
//                if (CollectionUtil.isNotEmpty(signatoryRelDOS)) {
//                    List<String> signatoryList = new ArrayList<>();
//                    signatoryRelDOS.forEach(rel -> {
//                        Relative relative = relativeMap.get(rel.getSignatoryId());
//                        if (relative != null) {
//                            signatoryList.add(relative.getCompanyName());
//                        }
//                    });
//                    item.setSignatoryList(signatoryList);
//                }
//            });
//        }
//    }
}



