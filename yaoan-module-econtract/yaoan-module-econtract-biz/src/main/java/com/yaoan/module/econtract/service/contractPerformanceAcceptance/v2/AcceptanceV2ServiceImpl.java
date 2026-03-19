package com.yaoan.module.econtract.service.contractPerformanceAcceptance.v2;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.AcceptancePageReqVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.AcceptanceRespVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.query.*;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.save.AcceptanceCreateReqVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.save.AcceptancePlanReqVO;
import com.yaoan.module.econtract.controller.admin.wps.FileVO;
import com.yaoan.module.econtract.convert.acceptance.AcceptanceConverter;
import com.yaoan.module.econtract.convert.businessfile.BusinessFileConverter;
import com.yaoan.module.econtract.convert.contract.ContractConverter;
import com.yaoan.module.econtract.convert.contract.PaymentScheduleConverter;
import com.yaoan.module.econtract.dal.dataobject.acceptance.AcceptanceDO;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.mysql.acceptance.AcceptanceMapper;
import com.yaoan.module.econtract.dal.mysql.businessfile.BusinessFileMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import com.yaoan.module.econtract.enums.AmountTypeEnums;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.econtract.enums.payment.MoneyTypeEnums;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import com.yaoan.module.system.api.dept.DeptApi;
import com.yaoan.module.system.api.dept.dto.DeptRespDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 验收管理模块
 * @author: Pele
 * @date: 2025/4/21 17:57
 */
@Service
public class AcceptanceV2ServiceImpl implements AcceptanceV2Service {
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private ContractMapper contractMapper;
    @Autowired
    private RelativeMapper relativeMapper;
    @Autowired
    private BusinessFileMapper businessFileMapper;
    @Autowired
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private SignatoryRelMapper signatoryRelMapper;
    static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private FileApi fileApi;

    @Override
    public PageResult<AcceptanceRespVO> page(AcceptancePageReqVO pageReqVO) {
        PageResult<PaymentScheduleDO> pageResult = paymentScheduleMapper.selectPageV2(pageReqVO);
        PageResult<AcceptanceRespVO> pageRespVO = PaymentScheduleConverter.INSTANCE.accepDO2RespPage(pageResult);
        if (CollectionUtil.isEmpty(pageRespVO.getList())) {
            return pageRespVO;
        }
        List<String> contractIds = pageRespVO.getList().stream().map(AcceptanceRespVO::getContractId).collect(Collectors.toList());
        List<ContractDO> contractDOS = contractMapper.selectList(ContractDO::getId, contractIds);
        Map<String, ContractDO> contractDOMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(contractDOS)) {
            contractDOMap = CollectionUtils.convertMap(contractDOS, ContractDO::getId);
        }
        List<AcceptanceDO> acceptanceDOList = acceptanceMapper.selectList(AcceptanceDO::getContractId, contractIds);
        Map<String, List<AcceptanceDO>> acceptanceDOListMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(acceptanceDOList)) {
            acceptanceDOListMap = CollectionUtils.convertMultiMap(acceptanceDOList, AcceptanceDO::getPlanId);
        }

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

        for (AcceptanceRespVO respVO : pageRespVO.getList()) {
            ContractDO contractDO = contractDOMap.get(respVO.getContractId());
            if (ObjectUtil.isNotNull(contractDO)) {
                respVO.setContractName(contractDO.getName());
                respVO.setContractCode(contractDO.getCode());
                //相对方
                respVO.setRelativeName(contractDO.getPartBName());
            }
            //结算类型
            AmountTypeEnums amountTypeEnums = AmountTypeEnums.getInstance(respVO.getAmountType());
            if (ObjectUtil.isNotNull(amountTypeEnums)) {
                respVO.setAmountTypeName(amountTypeEnums.getInfo());
            }
            //款项名称
            MoneyTypeEnums moneyTypeEnums = MoneyTypeEnums.getInstance(respVO.getMoneyType());
            if (ObjectUtil.isNotNull(moneyTypeEnums)) {
                respVO.setMoneyTypeName(moneyTypeEnums.getInfo());
            }
            List<AcceptanceDO> acceptanceDOS = acceptanceDOListMap.get(respVO.getId());
            if (ObjectUtil.isNotNull(acceptanceDOS)) {
                BigDecimal settleAmount = acceptanceDOS.stream().map(AcceptanceDO::getCurrentPayMoney).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                respVO.setSettledAmount(settleAmount);
                respVO.setSettledRatio(settleAmount.divide(respVO.getAmount(), 2, RoundingMode.HALF_UP).abs());
            } else {
                //已结算金额
                respVO.setSettledAmount(BigDecimal.ZERO);
                respVO.setSettledRatio(BigDecimal.ZERO);
            }
            respVO.setIsRelative((CollectionUtil.isNotEmpty(relativeContractIds) && relativeContractIds.contains(respVO.getContractId())) ? IfNumEnums.YES.getCode() : IfNumEnums.NO.getCode());

        }
        return pageRespVO;
    }

    @Resource
    private AcceptanceMapper acceptanceMapper;

    @Override
    public String save(AcceptanceCreateReqVO reqVO) {
        List<AcceptanceDO> acceptanceDOS = new ArrayList<>();
        String businessId = IdUtil.simpleUUID();
        for (AcceptancePlanReqVO planReqVO : reqVO.getAcceptancePlanReqVOList()) {
            AcceptanceDO entity = AcceptanceConverter.INSTANCE.req2Do(reqVO);
            entity.setPlanId(planReqVO.getPlanId())
                    .setContractId(reqVO.getContractId())
                    .setApplyDate(reqVO.getApplyDate())
                    .setTitle(reqVO.getTitle())
                    .setBusinessId(businessId)
                    .setCurrentPayMoney(planReqVO.getCurrentPayMoney())
                    .setCurrentPayRatio(planReqVO.getCurrentPayRatio());

            //默认验收通过
            entity.setStatus(2);
            acceptanceDOS.add(entity);
        }
        acceptanceMapper.insertBatch(acceptanceDOS);

        //计划状态改成已验收
        PaymentScheduleDO paymentScheduleDO = new PaymentScheduleDO().setId(reqVO.getPlanId()).setIsAcceptance(IfNumEnums.YES.getCode());
        paymentScheduleMapper.updateById(paymentScheduleDO);

        //验收材料
        List<BusinessFileDO> businessFileDOS = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(reqVO.getFileIdList())) {
            //TODO 如果前端能传文件名称，则可减少此次查询
            List<FileDTO> fileDTOS = fileApi.selectBatchIds(reqVO.getFileIdList());
            Map<Long, FileDTO> fileDTOMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(fileDTOS)) {
                fileDTOMap = CollectionUtils.convertMap(fileDTOS, FileDTO::getId);
            }

            Map<Long, FileDTO> finalFileDTOMap = fileDTOMap;
            reqVO.getFileIdList().forEach(fileId -> {
                BusinessFileDO businessFileDO = new BusinessFileDO()
                        .setBusinessId(businessId)
                        .setFileId(fileId);
                FileDTO fileDTO = finalFileDTOMap.get(fileId);
                if (ObjectUtil.isNotNull(fileDTO)) {
                    businessFileDO.setFileName(fileDTO.getName());
                }
                businessFileDOS.add(businessFileDO);
            });

            businessFileMapper.insertBatch(businessFileDOS);
        }

        return "success";
    }

    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private DeptApi deptApi;

    @Override
    public AcceptanceQueryRespVO get(String id) {
        AcceptanceQueryRespVO respVO = new AcceptanceQueryRespVO();
        ApplyInfoRespVO applyInfoRespVO = new ApplyInfoRespVO();
        StageAcceptance stageAcceptance = new StageAcceptance();
        ContractDO contractDO = contractMapper.selectOneByPlanId(id);
        PaymentScheduleDO paymentScheduleDO = paymentScheduleMapper.selectById(id);
        List<AcceptanceDO> acceptanceDOs = acceptanceMapper.selectList(AcceptanceDO::getPlanId, id);
        AcceptanceDO acceptanceDO = null;
        if (CollectionUtil.isNotEmpty(acceptanceDOs)) {
            acceptanceDO = acceptanceDOs.get(0);
        }

        //1 申请信息

        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        AdminUserRespDTO adminUserRespDTO = adminUserApi.getUser(loginUserId);
        applyInfoRespVO.setApplicantId(String.valueOf(loginUserId))
                .setApplicantName(ObjectUtil.isNull(adminUserRespDTO) ? "" : adminUserRespDTO.getNickname());
        if (ObjectUtil.isNull(acceptanceDO)) {
            applyInfoRespVO.setTitle(enhanceTitle(paymentScheduleDO, contractDO, adminUserRespDTO));
            applyInfoRespVO.setApplyDate(new Date());
        } else {
            applyInfoRespVO.setTitle(acceptanceDO.getTitle());
            applyInfoRespVO.setApplyDate(acceptanceDO.getApplyDate());
        }


        DeptRespDTO deptRespDTO = deptApi.getDept(adminUserRespDTO.getDeptId());
        if (ObjectUtil.isNotNull(deptRespDTO)) {
            applyInfoRespVO.setDeptId(String.valueOf(deptRespDTO.getId()))
                    .setDeptName(deptRespDTO.getName());
        }
        respVO.setApplyInfoRespVO(applyInfoRespVO);

        //2 合同信息
        if (ObjectUtil.isNotNull(contractDO)) {

            AcceptanceContractRespVO contractRespVO = ContractConverter.INSTANCE.acceptDO2Resp(contractDO);
            ContractType contractType = contractTypeMapper.selectById(contractRespVO.getContractType());
            contractRespVO.setContractId(contractDO.getId())
                    .setContractCode(contractDO.getCode())
                    .setContractName(contractDO.getName())
                    .setAmount(contractDO.getAmount())
                    .setSignDate(contractDO.getSignDate())
                    .setContractTypeName(ObjectUtil.isNotNull(contractType) ? contractType.getName() : "")
                    .setValiditySum(DateUtil.format(contractDO.getValidity0(), sdf) + " ~ " + DateUtil.format(contractDO.getValidity1(), sdf))
            ;
            respVO.setContractRespVO(contractRespVO);
        }
        List<AcceptancePlanRespVO> acceptancePlanRespVOList = new ArrayList<>();

        //单本次计划的验收计划
        AcceptancePlanRespVO acceptancePlanRespVO = PaymentScheduleConverter.INSTANCE.accepDO2Resp(paymentScheduleDO);
        if (ObjectUtil.isNotNull(contractDO)) {
            AmountTypeEnums amountTypeEnums = AmountTypeEnums.getInstance(contractDO.getAmountType());
            acceptancePlanRespVO.setAmountTypeName(ObjectUtil.isNull(amountTypeEnums) ? "" : amountTypeEnums.getInfo());
        }
        if (ObjectUtil.isNotNull(paymentScheduleDO)) {
            MoneyTypeEnums moneyTypeEnums = MoneyTypeEnums.getInstance(paymentScheduleDO.getMoneyType());
            acceptancePlanRespVO.setMoneyTypeName(ObjectUtil.isNull(moneyTypeEnums) ? "" : moneyTypeEnums.getInfo());
            //相对方
            Relative relative = relativeMapper.selectById(paymentScheduleDO.getPayee());
            acceptancePlanRespVO.setRelativeName(ObjectUtil.isNull(relative) ? "" : relative.getName());
            //本次结算
            if (ObjectUtil.isNotNull(acceptanceDO)) {
                acceptancePlanRespVO.setCurrentPayMoney(acceptanceDO.getCurrentPayMoney());
                acceptancePlanRespVO.setCurrentPayRatio(acceptanceDO.getCurrentPayRatio());
                stageAcceptance.setAcceptanceDate(acceptanceDO.getAcceptanceDate());
                stageAcceptance.setExpectedPayDate(acceptanceDO.getExpectedPayDate());
            }
        }

//        //获得该计划相关验收申请(如果可以多次验收)
//        List<AcceptanceDO> acceptanceDOS = acceptanceMapper.selectList(AcceptanceDO::getPlanId, id);

//        if (CollectionUtil.isNotEmpty(acceptancePlanRespVOList)) {
//
//                acceptancePlanRespVO.setContractName(acceptancePlanRespVO.getContractName());
//            验收历史记录
//            for (AcceptancePlanRespVO acceptancePlanRespVO : acceptancePlanRespVOList) {
//                if (ObjectUtil.isNotNull(contractDO)) {
//                    AmountTypeEnums amountTypeEnums = AmountTypeEnums.getInstance(contractDO.getAmountType());
//                    acceptancePlanRespVO.setAmountTypeName(ObjectUtil.isNull(amountTypeEnums) ? "" : amountTypeEnums.getInfo());
//                }
//                if (ObjectUtil.isNotNull(paymentScheduleDO)) {
//                    MoneyTypeEnums moneyTypeEnums = MoneyTypeEnums.getInstance(paymentScheduleDO.getMoneyType());
//                    acceptancePlanRespVO.setMoneyTypeName(ObjectUtil.isNull(moneyTypeEnums) ? "" : moneyTypeEnums.getInfo());
//                    acceptancePlanRespVO.setAmount(paymentScheduleDO.getAmount());
//                    acceptancePlanRespVO.setPaymentRatio(paymentScheduleDO.getPaymentRatio());
//                    //相对方
//                    Relative relative = relativeMapper.selectById(paymentScheduleDO.getPayee());
//                    acceptancePlanRespVO.setRelativeName(ObjectUtil.isNull(relative) ? "" : relative.getName());
//                }
//
//                acceptancePlanRespVO.setContractName(acceptancePlanRespVO.getContractName());
//        }
        acceptancePlanRespVOList.add(acceptancePlanRespVO);
        //3 阶段验收
        respVO.setStageAcceptance(stageAcceptance.setAcceptancePlanRespVOList(acceptancePlanRespVOList));

        //4 验收材料
        if (ObjectUtil.isNotNull(acceptanceDO)) {
            List<BusinessFileDO> businessFileDOS = businessFileMapper.selectList(BusinessFileDO::getBusinessId, acceptanceDO.getBusinessId());
            if (CollectionUtil.isNotEmpty(businessFileDOS)) {
                List<Long> fileIds = businessFileDOS.stream().map(BusinessFileDO::getFileId).collect(Collectors.toList());
                List<FileDTO> fileDTOList = fileApi.selectBatchIds(fileIds);
                if (CollectionUtil.isNotEmpty(fileDTOList)) {
                    List<FileVO> fileVOList = BusinessFileConverter.INSTANCE.dto2RespList(fileDTOList);
                    respVO.setFileVOList(fileVOList);
                }

            }
        }

        //验收备注
        respVO.setRemark(ObjectUtil.isNotNull(acceptanceDO) ? acceptanceDO.getRemark() : "");
        return respVO;
    }

    /**
     * 标题（默认生成标题：「合同名称」-第「*」期结算计划验收申请-「申请人姓名」-「申请日期」）；
     */
    private String enhanceTitle(PaymentScheduleDO paymentScheduleDO, ContractDO contractDO, AdminUserRespDTO adminUserRespDTO) {
        return ObjectUtil.isNull(contractDO) ? "" : contractDO.getName()
                + "-第" + (ObjectUtil.isNull(paymentScheduleDO) ? "" : paymentScheduleDO.getSort()) + "期结算计划验收申请-"
                + (ObjectUtil.isNull(adminUserRespDTO) ? "" : adminUserRespDTO.getNickname())
                + "-" + DateUtil.format(new Date(), sdf);
    }
}
