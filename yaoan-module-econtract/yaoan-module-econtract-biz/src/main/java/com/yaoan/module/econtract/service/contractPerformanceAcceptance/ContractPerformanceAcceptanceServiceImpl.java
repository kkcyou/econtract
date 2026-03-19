package com.yaoan.module.econtract.service.contractPerformanceAcceptance;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.exception.ErrorCode;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.bpm.api.task.dto.ContractProcessInstanceRelationInfoRespDTO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.vo.AcceptanceApplyReqVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.vo.ContractPerformanceAcceptancePageReqVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.vo.ContractPerformanceAcceptanceRespVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.vo.ContractPerformanceAcceptanceSaveReqVO;
import com.yaoan.module.econtract.controller.admin.controller.admin.contractInvoiceManage.vo.ContractInvoiceManageRespVO;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractPerformanceLogDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SimpleContractDO;
import com.yaoan.module.econtract.dal.dataobject.contractPerformanceAcceptance.ContractPerformanceAcceptanceDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.mysql.businessfile.BusinessFileMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractPerformanceLogMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contract.SimpleContractMapper;
import com.yaoan.module.econtract.dal.mysql.contractPerformanceAcceptance.ContractPerformanceAcceptanceMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.enums.payment.PerformanceAcceptanceEnums;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;


/**
 * 验收 Service 实现类
 *
 * @author lls
 */
@Slf4j
@Service
@Validated
public class ContractPerformanceAcceptanceServiceImpl implements ContractPerformanceAcceptanceService {

    @Resource
    private ContractPerformanceAcceptanceMapper contractPerformanceAcceptanceMapper;
    @Resource
    private ContractPerformanceLogMapper contractPerformanceLogMapper;
    @Resource
    private BusinessFileMapper businessFileMapper;
    @Resource
    private SimpleContractMapper simpleContractMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private AdminUserApi userApi;
    @Resource
    private ContractTypeMapper contractTypeMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createContractPerformanceAcceptance(ContractPerformanceAcceptanceSaveReqVO createReqVO) {
        //如果该计划存在审批中，则不可重复发起
        Long count = contractPerformanceAcceptanceMapper.selectCount(new LambdaQueryWrapperX<ContractPerformanceAcceptanceDO>().eq(ContractPerformanceAcceptanceDO::getPlanId,createReqVO.getPlanId()).eq(ContractPerformanceAcceptanceDO::getStatus,0));
        if( 0<count ){
            throw exception(DIY_ERROR,"请勿重复提交该计划。");
        }
        // 插入
        ContractPerformanceAcceptanceDO contractPerformanceAcceptance = BeanUtils.toBean(createReqVO, ContractPerformanceAcceptanceDO.class);
        contractPerformanceAcceptance.setStatus(PerformanceAcceptanceEnums.UNDO.getCode());
        contractPerformanceAcceptanceMapper.insert(contractPerformanceAcceptance);
        List<BusinessFileDO>  fileList =  createReqVO.getApplyFileList();
        fileList.forEach(businessFileDO -> {
            businessFileDO.setBusinessId(contractPerformanceAcceptance.getId());
        });
        businessFileMapper.insertBatch(fileList);
        ContractPerformanceLogDO contractPerformanceLogDO = new ContractPerformanceLogDO();
        contractPerformanceLogDO.setBusinessId(contractPerformanceAcceptance.getPlanId());
        contractPerformanceLogDO.setBillId(contractPerformanceAcceptance.getId());
        contractPerformanceLogDO.setModuleName("验收");
        contractPerformanceLogDO.setOperateName("发起了");
        contractPerformanceLogDO.setOperateContent("验收申请");
        contractPerformanceLogMapper.insert(contractPerformanceLogDO);
        
        // 返回
        return contractPerformanceAcceptance.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateContractPerformanceAcceptance(ContractPerformanceAcceptanceSaveReqVO updateReqVO) {
        // 校验存在
        validateContractPerformanceAcceptanceExists(updateReqVO.getId());
        // 更新
        ContractPerformanceAcceptanceDO updateObj = BeanUtils.toBean(updateReqVO, ContractPerformanceAcceptanceDO.class);
        contractPerformanceAcceptanceMapper.updateById(updateObj);
        businessFileMapper.deleteByBusinessId(updateObj.getId());
        List<BusinessFileDO>  fileList =  updateReqVO.getApplyFileList();
        fileList.forEach(businessFileDO -> {
            businessFileDO.setBusinessId(updateObj.getId());
        });
        businessFileMapper.insertBatch(fileList);

        ContractPerformanceLogDO contractPerformanceLogDO = new ContractPerformanceLogDO();
        contractPerformanceLogDO.setBusinessId(updateObj.getPlanId());
        contractPerformanceLogDO.setBillId(updateObj.getId());
        contractPerformanceLogDO.setModuleName("验收");
        contractPerformanceLogDO.setOperateName("修改了");
        contractPerformanceLogDO.setOperateContent("验收申请");
        contractPerformanceLogMapper.insert(contractPerformanceLogDO);
    }

    
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteContractPerformanceAcceptance(String id) {
        // 校验存在
        validateContractPerformanceAcceptanceExists(id);
        ContractPerformanceAcceptanceDO contractPerformanceAcceptanceDO =  contractPerformanceAcceptanceMapper.selectById(id);
        // 删除
        contractPerformanceAcceptanceMapper.deleteById(id);
        businessFileMapper.deleteByBusinessId(id);
        if(contractPerformanceAcceptanceDO.getAcceptanceId() != null && contractPerformanceAcceptanceDO.getAcceptanceId().length() > 0) {
            businessFileMapper.deleteByBusinessId(contractPerformanceAcceptanceDO.getAcceptanceId());
        }
        ContractPerformanceLogDO contractPerformanceLogDO = new ContractPerformanceLogDO();
        contractPerformanceLogDO.setBusinessId(contractPerformanceAcceptanceDO.getPlanId());
        contractPerformanceLogDO.setBillId(contractPerformanceAcceptanceDO.getId());
        contractPerformanceLogDO.setModuleName("验收");
        contractPerformanceLogDO.setOperateName("删除了");
        contractPerformanceLogDO.setOperateContent("验收申请");
        contractPerformanceLogMapper.insert(contractPerformanceLogDO);
    }
    private void validateContractPerformanceAcceptanceExists(String id) {
        if (contractPerformanceAcceptanceMapper.selectById(id) == null) {
            throw exception(new ErrorCode(500,"不存在"));
        }
    }

    @Override
    public ContractPerformanceAcceptanceRespVO getContractPerformanceAcceptance(String id) {
        ContractPerformanceAcceptanceDO contractPerformanceAcceptanceDO = contractPerformanceAcceptanceMapper.selectById(id);
        if (contractPerformanceAcceptanceDO == null){
            return new ContractPerformanceAcceptanceRespVO();
        }
        ContractPerformanceAcceptanceRespVO contractPerformanceAcceptanceRespVO = BeanUtils.toBean(contractPerformanceAcceptanceDO, ContractPerformanceAcceptanceRespVO.class);
        List<BusinessFileDO> businessFileDOList = businessFileMapper.selectByBusinessId(id);
        contractPerformanceAcceptanceRespVO.setApplyFileList(businessFileDOList);
        if(contractPerformanceAcceptanceDO.getAcceptanceId() != null && contractPerformanceAcceptanceDO.getAcceptanceId().length() > 0){
            List<BusinessFileDO> acceptanceFileDOList = businessFileMapper.selectByBusinessId(contractPerformanceAcceptanceDO.getAcceptanceId());
            contractPerformanceAcceptanceRespVO.setAcceptanceFileList(acceptanceFileDOList);
        }
        if(ObjectUtil.isNotNull(contractPerformanceAcceptanceRespVO.getContractId())){
            LambdaQueryWrapperX<SimpleContractDO> lambdaQueryWrapperX = new LambdaQueryWrapperX();
            lambdaQueryWrapperX.eq(SimpleContractDO::getId, contractPerformanceAcceptanceRespVO.getContractId());
            List<SimpleContractDO> simpleContractDOList =simpleContractMapper.selectList(lambdaQueryWrapperX);
            enhanceSimpleContracts(simpleContractDOList);
            contractPerformanceAcceptanceRespVO.setContractList(simpleContractDOList);
        }
        if(ObjectUtil.isNotNull(contractPerformanceAcceptanceRespVO.getPlanId())){
            PaymentScheduleDO paymentScheduleDO = paymentScheduleMapper.selectById(contractPerformanceAcceptanceRespVO.getPlanId());
            if(paymentScheduleDO != null){
                contractPerformanceAcceptanceRespVO.setPlanName(paymentScheduleDO.getName());
                
                contractPerformanceAcceptanceRespVO.setPaymentScheduleDO(paymentScheduleDO);
            }
        }
        //验收人
        if(ObjectUtil.isNotNull(contractPerformanceAcceptanceRespVO.getAcceptanceUser())){
            AdminUserRespDTO adminUserRespDTO = userApi.getUser(contractPerformanceAcceptanceRespVO.getAcceptanceUser());
            if(adminUserRespDTO != null){
                contractPerformanceAcceptanceRespVO.setAcceptanceUserName(adminUserRespDTO.getNickname());
            }
        }
        //发起人
        AdminUserRespDTO adminUserRespDTO = userApi.getUser(Long.valueOf(contractPerformanceAcceptanceRespVO.getCreator()));
        contractPerformanceAcceptanceRespVO.setContractName(adminUserRespDTO.getNickname());
        return contractPerformanceAcceptanceRespVO;
    }

    private void enhanceSimpleContracts(List<SimpleContractDO> simpleContractDOList) {

        if(CollectionUtil.isNotEmpty(simpleContractDOList)){
            List<String> types = simpleContractDOList.stream().map(SimpleContractDO::getContractType).collect(Collectors.toList());
            List<ContractType> contractTypeList = contractTypeMapper.selectList(ContractType::getId,types);
            if(CollectionUtil.isEmpty(contractTypeList)){
                return ;
            }
            Map<String,ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypeList,ContractType::getId);
            for (SimpleContractDO contractDO : simpleContractDOList) {
                ContractType contractType = contractTypeMap.get(contractDO.getContractType());
                if(ObjectUtil.isNotNull(contractType)){
                    contractDO.setContractType(contractType.getName());
                }
            }
        }
    }


    @Override
    public PageResult<ContractPerformanceAcceptanceRespVO> getContractPerformanceAcceptancePage(ContractPerformanceAcceptancePageReqVO pageReqVO) {
        PageResult<ContractPerformanceAcceptanceDO> pageResult = contractPerformanceAcceptanceMapper.selectPage(pageReqVO);
        PageResult<ContractPerformanceAcceptanceRespVO> performanceAcceptanceRespVOPageResult = BeanUtils.toBean(pageResult, ContractPerformanceAcceptanceRespVO.class);
        List<ContractPerformanceAcceptanceRespVO> contractPerformanceAcceptanceRespVOList =  performanceAcceptanceRespVOPageResult.getList();
        if(contractPerformanceAcceptanceRespVOList.size() == 0 ){
            return performanceAcceptanceRespVOPageResult;
        }
        List<Long> userIdList = contractPerformanceAcceptanceRespVOList.stream().map(ContractPerformanceAcceptanceRespVO::getAcceptanceUser).collect(Collectors.toList());
        List adminUserRespDTOList = userApi.getUserList(userIdList);
        Map<Long, AdminUserRespDTO> userRespDTOMap = CollectionUtils.convertMap(adminUserRespDTOList, AdminUserRespDTO::getId);


        List<String> contractIds = new ArrayList<>();// contractPerformanceAcceptanceRespVOList.stream().map(ContractPerformanceAcceptanceRespVO::getContractId).collect(Collectors.toList());
        List<String> planIds = new ArrayList<>();// contractPerformanceAcceptanceRespVOList.stream().map(ContractPerformanceAcceptanceRespVO::getPlanId).collect(Collectors.toList());
        for(ContractPerformanceAcceptanceRespVO acceptanceRespVO:contractPerformanceAcceptanceRespVOList){
            contractIds.add(acceptanceRespVO.getContractId());
            planIds.add(acceptanceRespVO.getPlanId());
        }
        
        LambdaQueryWrapperX<SimpleContractDO> lambdaQueryWrapperX = new LambdaQueryWrapperX();
        lambdaQueryWrapperX.in(SimpleContractDO::getId, contractIds);
        List<SimpleContractDO> simpleContractDOList = simpleContractMapper.selectList(lambdaQueryWrapperX);
        Map<String,SimpleContractDO> simpleContractDOMap = CollectionUtils.convertMap(simpleContractDOList, SimpleContractDO::getId);
        LambdaQueryWrapperX<PaymentScheduleDO> planlambdaQueryWrapperX = new LambdaQueryWrapperX();
        planlambdaQueryWrapperX.in(PaymentScheduleDO::getId, planIds);
        List<PaymentScheduleDO> paymentScheduleDOList = paymentScheduleMapper.selectList(planlambdaQueryWrapperX);
        Map<String,PaymentScheduleDO> paymentDOMap = CollectionUtils.convertMap(paymentScheduleDOList, PaymentScheduleDO::getId);


        if(CollectionUtil.isNotEmpty(contractPerformanceAcceptanceRespVOList)){
            contractPerformanceAcceptanceRespVOList.forEach(contractPerformanceAcceptanceRespVO -> {
                if(simpleContractDOMap.get(contractPerformanceAcceptanceRespVO.getContractId())!=null){
                    SimpleContractDO tempDO = simpleContractDOMap.get(contractPerformanceAcceptanceRespVO.getContractId());
                    PaymentScheduleDO tempPlanDO = paymentDOMap.get(contractPerformanceAcceptanceRespVO.getPlanId());
                    if(userRespDTOMap.containsKey(contractPerformanceAcceptanceRespVO.getAcceptanceUser())){
                        contractPerformanceAcceptanceRespVO.setAcceptanceUserName(userRespDTOMap.get(contractPerformanceAcceptanceRespVO.getAcceptanceUser()).getNickname());
                    }
                    if(tempPlanDO!=null){
                        contractPerformanceAcceptanceRespVO.setContractCode(tempDO.getCode());
                        contractPerformanceAcceptanceRespVO.setContractName(tempDO.getName());
                    }
                   if(tempPlanDO!=null){
                       contractPerformanceAcceptanceRespVO.setPaymentRatio(tempPlanDO.getPaymentRatio());
                       contractPerformanceAcceptanceRespVO.setAmount(tempPlanDO.getAmount());
                       contractPerformanceAcceptanceRespVO.setSort(tempPlanDO.getSort());
                   }
                    
                }
            });
        }

       

        return performanceAcceptanceRespVOPageResult;
    }

    private PageResult<ContractInvoiceManageRespVO> enhanceBpmAndContractPage(PageResult<ContractInvoiceManageRespVO> contractInvoiceManagePageReqVOPageResult, Map<String, ContractProcessInstanceRelationInfoRespDTO> contractProcessInstanceRelationInfoRespDTOMap) {
        List<ContractInvoiceManageRespVO> contractInvoiceManageRespVOList = contractInvoiceManagePageReqVOPageResult.getList();
        List<String> contractIds = contractInvoiceManageRespVOList.stream().map(ContractInvoiceManageRespVO::getContractId).collect(Collectors.toList());
        LambdaQueryWrapperX<SimpleContractDO> lambdaQueryWrapperX = new LambdaQueryWrapperX();
        lambdaQueryWrapperX.in(SimpleContractDO::getId, contractIds);
        List<SimpleContractDO> simpleContractDOList = simpleContractMapper.selectList(lambdaQueryWrapperX);
        Map<String,SimpleContractDO> simpleContractDOMap = CollectionUtils.convertMap(simpleContractDOList, SimpleContractDO::getId);

        if(CollectionUtil.isNotEmpty(contractInvoiceManageRespVOList)){
            contractInvoiceManageRespVOList.forEach(contractInvoiceManageRespVO -> {
                contractInvoiceManageRespVO.setTaskId(contractProcessInstanceRelationInfoRespDTOMap.get(contractInvoiceManageRespVO.getProcessInstanceId()).getTaskId());
                if(simpleContractDOMap.get(contractInvoiceManageRespVO.getContractId())!=null){
                    SimpleContractDO tempDO = simpleContractDOMap.get(contractInvoiceManageRespVO.getContractId());
                    contractInvoiceManageRespVO.setContractCode(tempDO.getCode());
                    contractInvoiceManageRespVO.setContractName(tempDO.getName());
                }
            });
        }


        return contractInvoiceManagePageReqVOPageResult;
    }
    
    @Override
    public String acceptancePerformance(AcceptanceApplyReqVO createReqVO) {
        ContractPerformanceAcceptanceDO contractPerformanceAcceptanceDO = contractPerformanceAcceptanceMapper.selectById(createReqVO.getId());
        if(contractPerformanceAcceptanceDO.getAcceptanceId() != null && contractPerformanceAcceptanceDO.getAcceptanceId().length() > 0){
            throw exception(SYSTEM_ERROR,"该验收申请已被验收，不可重复验收！");
        }
        contractPerformanceAcceptanceDO.setAcceptanceId(IdUtil.fastSimpleUUID());
        contractPerformanceAcceptanceDO.setAcceptanceRemark(createReqVO.getAcceptanceRemark());
        contractPerformanceAcceptanceDO.setStatus(createReqVO.getStatus());
        contractPerformanceAcceptanceMapper.updateById(contractPerformanceAcceptanceDO);
        List<BusinessFileDO>  fileList =  createReqVO.getAcceptanceFileList();
        fileList.forEach(businessFileDO -> {
            businessFileDO.setBusinessId(contractPerformanceAcceptanceDO.getAcceptanceId());
        });
        businessFileMapper.insertBatch(fileList);
        return contractPerformanceAcceptanceDO.getAcceptanceId();
    }


}