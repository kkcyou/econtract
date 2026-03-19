package com.yaoan.module.econtract.service.workbench.v2;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.econtract.controller.admin.workbench.v2.vo.ContractDataReqVO;
import com.yaoan.module.econtract.controller.admin.workbench.v2.vo.ContractDataRespVO;
import com.yaoan.module.econtract.controller.admin.workbench.v2.vo.ContractPerformReqVO;
import com.yaoan.module.econtract.convert.contract.ContractConverter;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.gpx.PackageInfoDO;
import com.yaoan.module.econtract.dal.mysql.bpm.archive.BpmContractArchivesMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.contractborrow.ContractBorrowBpmMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractOrderExtMapper;
import com.yaoan.module.econtract.dal.mysql.contractinvoicemanage.ContractInvoiceManageMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.deferred.PaymentDeferredApplyMapper;
import com.yaoan.module.econtract.dal.mysql.gpx.PackageInfoMapper;
import com.yaoan.module.econtract.dal.mysql.model.ModelMapper;
import com.yaoan.module.econtract.dal.mysql.payment.paymentapplication.PaymentApplicationMapper;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.enums.ContractUploadTypeEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import com.yaoan.module.econtract.service.hljsupply.HLJSupplyService;
import com.yaoan.module.system.api.user.SupplyApi;
import com.yaoan.module.system.api.user.dto.SupplyDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: Pele
 * @date: 2025/2/11 10:22
 */
@Slf4j
@Service
public class WorkBenchV2ServiceImpl implements WorkBenchV2Service {
    private static final String FLAG_CONTRACT = "contract";
    private static final String FLAG_SEAL = "seal";
    private static final String FLAG_PAY = "pay";
    private static final String FLAG_COLLECTION = "collection";
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ContractOrderExtMapper contractOrderExtMapper;
    @Resource
    private HLJSupplyService hljSupplyService;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private SupplyApi supplyApi;
    /**
     * 单位端_工作台_合同数据接口
     * 当前人起草的合同状态（11,13）
     * 当前人发起的用印、付款、收款的所有状态没到通过的所有合同
     *
     * @param vo
     * @return {@link PageResult }<{@link ContractDataRespVO }>
     */
    @Override
    public PageResult<ContractDataRespVO> workbenchContractData( ContractDataReqVO vo) {
        String flag = vo.getFlag();
        switch (flag) {
            // 合同审批
            case FLAG_CONTRACT:
                return enhanceContract(vo);

            // 用印审批
            case FLAG_SEAL:

                return enhanceSeal(vo);

            // 付款审批
            case FLAG_PAY:

                return enhancePay(vo);

            // 收款审批
            case FLAG_COLLECTION:

                return enhanceCollection(vo);

            default:
                return null;
        }
    }

    /**
     * 单位端_工作台_合同履约接口
     * 根据一级合同类型找到所有状态的合同
     *
     * @param vo
     * @return <{@link PageResult }<{@link ContractDataRespVO }>>
     */
    @Override
    public PageResult<ContractDataRespVO> contractPerform(ContractPerformReqVO vo) {
        // 计算下属所有节点合同类型
        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>()
                .select(ContractType::getId,ContractType::getName,ContractType::getParentId)
                .eq(ContractType::getTypeStatus,1));
        if(CollectionUtil.isEmpty(contractTypes)){
            return new PageResult<>();
        }
        contractTypes = findDescendants( contractTypes,vo.getContractType() );
        List<Integer> govUpload = new ArrayList<>();
        govUpload.add(ContractUploadTypeEnums.ORDER_DRAFT.getCode());
        govUpload.add(ContractUploadTypeEnums.THIRD_PARTY.getCode());

        // 相关合同
        List<Integer> statusList = new ArrayList<>();
        statusList.add(ContractStatusEnums.PERFORMING.getCode());
        statusList.add(ContractStatusEnums.PERFORMANCE_CLOSURE.getCode());
        statusList.add(ContractStatusEnums.PERFORMANCE_COMPLETE.getCode());
        statusList.add(ContractStatusEnums.PERFORMANCE_RISK.getCode());
        statusList.add(ContractStatusEnums.PERFORMANCE_RISK_DISPUTE.getCode());
        statusList.add(ContractStatusEnums.PERFORMANCE_RISK_PAUSE.getCode());
        statusList.add(ContractStatusEnums.PERFORMANCE_RISK_EXTENSION.getCode());
        statusList.add(ContractStatusEnums.PERFORMANCE_RISK_OVERDUE.getCode());

        List<String> contractTypeIds =contractTypes.stream().map(ContractType::getId).collect(Collectors.toList());
        contractTypeIds.add(vo.getContractType());
        PageResult<ContractDO> contractDOPageResult = contractMapper.contractPerform(vo,contractTypeIds,govUpload);
        PageResult<ContractDataRespVO> result = ContractConverter.INSTANCE.do2DataResp(contractDOPageResult);
        return  enhance4ContractPerform(result);

    }

    private PageResult<ContractDataRespVO> enhance4ContractPerform(PageResult<ContractDataRespVO> contractDOPageResult) {
        if(ObjectUtil.isNull(contractDOPageResult)||CollectionUtil.isEmpty(contractDOPageResult.getList())){
            return new PageResult<>();
        }
//        List<String> supplierIds = contractDOPageResult.getList().stream().map(ContractDataRespVO::getSupplierId).collect(Collectors.toList());
//        List<SupplyDTO> supplyDTOList = supplyApi.getSupplyByIds(supplierIds);
//        if(CollectionUtil.isEmpty(supplyDTOList)){
//            supplyDTOList = hljSupplyService.getSupplyList(supplierIds);
//        }
//
//        Map<String,SupplyDTO> supplyDTOMap = new HashMap<>();
//        if(CollectionUtil.isNotEmpty(supplyDTOList)){
//            supplyDTOMap = CollectionUtils.convertMap(supplyDTOList,SupplyDTO::getId);
//        }
        //付款信息
        List<Integer> payedStatus = new ArrayList<Integer>();
        payedStatus.add(PaymentScheduleStatusEnums.DONE.getCode());
        payedStatus.add(PaymentScheduleStatusEnums.CLOSE.getCode());

        List<String> contractIds = contractDOPageResult.getList().stream().map(ContractDataRespVO::getId).collect(Collectors.toList());
        List<PaymentScheduleDO> paymentScheduleDOList = paymentScheduleMapper.enhance4ContractPerform(contractIds,payedStatus);
        Map<String,List<PaymentScheduleDO>> planMap = new HashMap<>();
        Map<String, BigDecimal> payedMap = new HashMap<>();
        if(CollectionUtil.isNotEmpty(paymentScheduleDOList)){
            planMap = CollectionUtils.convertMultiMap(paymentScheduleDOList,PaymentScheduleDO::getContractId);
            for (Map.Entry<String, List<PaymentScheduleDO>> entry : planMap.entrySet()) {
                BigDecimal totalPaymentMoney = entry.getValue().stream()
                        .map(p -> Optional.ofNullable(p.getAmount()).orElse(BigDecimal.ZERO))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .setScale(2, BigDecimal.ROUND_HALF_UP);
                Double totalStageMoney = entry.getValue().stream()
                        .mapToDouble(p -> Optional.ofNullable(p.getStagePaymentAmount()).orElse(0.0)) // 如果是null，使用0.0
                        .sum();
                BigDecimal totalStageMoney2 = new BigDecimal(String.valueOf(totalStageMoney)).setScale(2,BigDecimal.ROUND_HALF_UP);
                totalPaymentMoney = totalPaymentMoney.add(totalStageMoney2).setScale(2,BigDecimal.ROUND_HALF_UP);
                payedMap.put(entry.getKey(), totalPaymentMoney);
            }
        }
        for (ContractDataRespVO dataRespVO : contractDOPageResult.getList()) {
            //合同状态
            ContractStatusEnums contractStatusEnums = ContractStatusEnums.getInstance(dataRespVO.getStatus());
            if(ObjectUtil.isNotNull(contractStatusEnums)){
                dataRespVO.setStatusName(contractStatusEnums.getDesc());
            }
            //供应商
            dataRespVO.setSupplierName(dataRespVO.getPartBName());
//            SupplyDTO supplyDTO = supplyDTOMap.get(dataRespVO.getSupplierId());
//            if(ObjectUtil.isNotNull(supplyDTO)){
//                dataRespVO.setSupplierName(supplyDTO.getSupplyCn());
//            }
            //付款进度
            BigDecimal period = new BigDecimal(0);
            BigDecimal payed = payedMap.get(dataRespVO.getId());
            if(ObjectUtil.isNotNull(payed)){
                period = payed.divide(new BigDecimal(dataRespVO.getAmount()));
                dataRespVO.setPeriod(period);
            }

        }
        return  contractDOPageResult;
    }

    private PageResult<ContractDataRespVO> enhanceContract(ContractDataReqVO vo) {
        String LoginUserId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        return enhanceInfo(ContractConverter.INSTANCE.do2DataResp(contractMapper.selectDataPage(LoginUserId,vo)));
    }

    private PageResult<ContractDataRespVO> enhanceSeal(ContractDataReqVO vo) {
        String LoginUserId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        return enhanceInfo(ContractConverter.INSTANCE.do2DataResp(contractMapper.selectDataPage4Seal(LoginUserId,vo)));
    }

    private PageResult<ContractDataRespVO> enhancePay(ContractDataReqVO vo) {
        String LoginUserId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        return enhanceInfo(ContractConverter.INSTANCE.do2DataResp(contractMapper.selectDataPage4Pay(LoginUserId,vo)));
    }

    private PageResult<ContractDataRespVO> enhanceCollection(ContractDataReqVO vo) {
        String LoginUserId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());
        return enhanceInfo(ContractConverter.INSTANCE.do2DataResp(contractMapper.selectDataPage4Collection(LoginUserId,vo)));
    }
@Resource
private PackageInfoMapper packageInfoMapper;
    private PageResult<ContractDataRespVO> enhanceInfo(PageResult<ContractDataRespVO> do2DataResp) {
        if(ObjectUtil.isEmpty(do2DataResp) || CollectionUtil.isEmpty(do2DataResp.getList()) ){
            return do2DataResp;
        }
        List<String> contactIds = do2DataResp.getList().stream().map(ContractDataRespVO::getId).collect(Collectors.toList());
        List<ContractOrderExtDO> orderExtDOList = contractOrderExtMapper.selectList(ContractOrderExtDO::getId, contactIds);
        Map<String,ContractOrderExtDO> extDOMap = new HashMap<String,ContractOrderExtDO>();
        if(CollectionUtil.isNotEmpty(orderExtDOList)){
            extDOMap = CollectionUtils.convertMap(orderExtDOList,ContractOrderExtDO::getId);
        }
        List<PackageInfoDO> packageInfoDOList = packageInfoMapper.selectList4Contract(contactIds);
        Map<String,PackageInfoDO>  packageInfoDOMap = new HashMap<String,PackageInfoDO>();
        if(CollectionUtil.isNotEmpty(packageInfoDOList)){
            packageInfoDOMap = CollectionUtils.convertMap(packageInfoDOList,PackageInfoDO::getManagerName);
        }
        for (ContractDataRespVO respVO : do2DataResp.getList()) {
            ContractStatusEnums statusEnums = ContractStatusEnums.getInstance(respVO.getStatus());
            if(ObjectUtil.isNotNull(statusEnums)){
                respVO.setStatusName(statusEnums.getDesc());
            }
            ContractOrderExtDO extDO = extDOMap.get(respVO.getId());
            if(ObjectUtil.isNotNull(extDO)){
                respVO.setPlatform(extDO.getPlatform());
                respVO.setContractSourceType(extDO.getContractSourceType());

                PackageInfoDO packageInfoDO = packageInfoDOMap.get(extDO.getId());
                if(ObjectUtil.isNotNull(packageInfoDO)){
                    respVO.setBiddingMethodCode(packageInfoDO.getBiddingMethodCode());
                    respVO.setProjectType(packageInfoDO.getProjectType());

                }
            }
        }

        return do2DataResp;
    }




    // 找出所有子节点及更低级的节点
    public static List<ContractType> findDescendants(List<ContractType> contractTypes, String contractTypeId) {
        List<ContractType> result = new ArrayList<>();

        // 找出目标节点及其子节点
        for (ContractType contract : contractTypes) {
            // 如果当前节点的 id 与目标 id 匹配，则开始查找其子节点
            if (contract.getParentId() != null && contract.getParentId().equals(contractTypeId)) {
                result.add(contract); // 添加当前节点到结果集合
                // 递归查找子节点的子节点
                findDescendantsRecursive(contractTypes, contract.getId(), result);
            }
        }

        return result;
    }

    // 递归查找子节点及更低级的子节点
    private static void findDescendantsRecursive(List<ContractType> contractTypes, String parentId, List<ContractType> result) {
        for (ContractType contract : contractTypes) {
            // 如果当前节点的 parentId 匹配，说明是子节点
            if (contract.getParentId() != null && contract.getParentId().equals(parentId)) {
                result.add(contract); // 添加当前子节点
                // 递归查找这个子节点的子节点
                findDescendantsRecursive(contractTypes, contract.getId(), result);
            }
        }
    }
}
