package com.yaoan.module.econtract.service.contractrisk;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.exception.ErrorCode;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.contractrisk.vo.ContractRiskPageReqVO;
import com.yaoan.module.econtract.controller.admin.contractrisk.vo.ContractRiskRespVO;
import com.yaoan.module.econtract.controller.admin.contractrisk.vo.ContractRiskSaveReqVO;
import com.yaoan.module.econtract.controller.admin.contractrisk.vo.ContractRiskUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.payment.paymentapplication.vo.one.detail.BusinessFileVO;
import com.yaoan.module.econtract.convert.businessfile.BusinessFileConverter;
import com.yaoan.module.econtract.convert.risk.ContractRiskConverter;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contractrisk.ContractRiskDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contractrisk.ContractRiskMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.service.businessfile.BusinessFileService;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ContractStatusEnums.PERFORMING;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;
import static com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums.CLOSE;
import static com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums.DONE;


/**
 * 合同风险 Service 实现类
 *
 * @author lls
 */
@Service
@Validated
public class ContractRiskServiceImpl implements ContractRiskService {

    @Resource
    private ContractRiskMapper contractRiskMapper;
    @Resource
    private BusinessFileService businessFileService;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private AdminUserApi adminUserApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createContractRisk(ContractRiskSaveReqVO createReqVO) {
        // 插入
        ContractRiskDO contractRisk = BeanUtils.toBean(createReqVO, ContractRiskDO.class);
        contractRiskMapper.insert(contractRisk);
        //更新合同状态
        ContractDO contractDO = contractMapper.selectById(createReqVO.getContractId());
        if (ObjectUtil.isNull(contractDO)) {
            throw exception(SYSTEM_ERROR, "相关合同不存在");
        }
        contractDO.setStatus(createReqVO.getRiskType()).setRiskDate(new Date());
        contractDO.setPauseDate(new Date());
        contractMapper.updateById(contractDO);
        //关联附件
        businessFileService.createBatchBusinessFile(contractRisk.getId(), createReqVO.getFileVOList());
        // 返回
        return contractRisk.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateContractRisk(ContractRiskUpdateReqVO updateReqVO) {
        // 校验存在
        validateContractRiskExists(updateReqVO.getId());
        // 更新
        ContractRiskDO updateObj = BeanUtils.toBean(updateReqVO, ContractRiskDO.class);
        contractRiskMapper.updateById(updateObj);

        //关联附件
        businessFileService.deleteBusinessFile(updateReqVO.getContractId());
        businessFileService.createBatchBusinessFile(updateReqVO.getContractId(), updateReqVO.getFileVOList());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateContractRisk4Handle(ContractRiskUpdateReqVO updateReqVO) {
        // 更新转换
        ContractRiskDO updateObj = BeanUtils.toBean(updateReqVO, ContractRiskDO.class);
        // 校验存在
        validateContractRiskExists(updateReqVO.getId());
        String handleId = IdUtil.fastSimpleUUID();
        updateObj.setStatus(1);
        updateObj.setHandleId(handleId);
        contractRiskMapper.updateById(updateObj);
        
        //更新合同状态
        ContractDO contractDO = contractMapper.selectById(updateReqVO.getContractId());
        if (ObjectUtil.isNotNull(contractDO)) {
            contractDO.setStatus(updateReqVO.getHandleResultStatus());
        }
        contractMapper.updateById(contractDO);
        //履约完成增强
        enhanceComplete(updateReqVO);
        
        //风险处理的关联附件
        //由于handleid为自动生成的，所以此处不需要删除。风险只需要处理一次
        //businessFileService.deleteBusinessFile(handleId);
        businessFileService.createBatchBusinessFile(handleId, updateReqVO.getFileVOList());
    }

    private void enhanceComplete(ContractRiskUpdateReqVO reqVO) {
        if (Objects.equals(ContractStatusEnums.PERFORMANCE_COMPLETE.getCode(), reqVO.getHandleResultStatus())) {
            //若除了当前计划，其他履约计划均为【已完成】状态，合同履约状态可变更为履约完成，当前履约计划也变更为已完成状态
            List<PaymentScheduleDO> otherPlans = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>().ne(PaymentScheduleDO::getId, reqVO.getPlanId()).eq(PaymentScheduleDO::getContractId, reqVO.getContractId()));
            if (CollectionUtil.isNotEmpty(otherPlans)) {
                List<PaymentScheduleDO> donePlans = otherPlans.stream()
                        .filter(plan -> plan.getStatus() == 1)
                        .collect(Collectors.toList());
                if (otherPlans.size() == donePlans.size()) {
                    // 合同履约状态可变更为履约完成
                    ContractDO contractDO = new ContractDO().setId(reqVO.getContractId()).setStatus(ContractStatusEnums.PERFORMANCE_COMPLETE.getCode());
                    contractMapper.updateById(contractDO);
                    // 当前履约计划也变更为已完成状态
                    PaymentScheduleDO paymentScheduleDO = new PaymentScheduleDO().setId(reqVO.getPlanId()).setStatus(DONE.getCode());
                    paymentScheduleMapper.updateById(paymentScheduleDO);
                } else {
                    int toDoPlanCount = otherPlans.size() - donePlans.size();
                    //若除了当前计划，其他履约计划存在未完成的履约计划，不可变更为【履约完成】状态，提交时toast提示：当前履约计划中还有 [未完成个数] 个未完成的履约计划，请先完成
                    throw exception(SYSTEM_ERROR, "当前合同相关履约计划中还有 " + toDoPlanCount + "个 未完成的履约计划，请先完成。");
                }
            }
        }
    }

    @Override
    public void deleteContractRisk(String id) {
        // 校验存在
        validateContractRiskExists(id);
        // 删除
        contractRiskMapper.deleteById(id);
    }

    private void validateContractRiskExists(String id) {
        if (contractRiskMapper.selectById(id) == null) {
            throw exception(new ErrorCode(500, "不存在"));
        }
    }

    @Override
    public ContractRiskRespVO getContractRisk(String id) {
        ContractRiskRespVO respVO = new ContractRiskRespVO();
        LambdaQueryWrapperX<ContractRiskDO> lambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        lambdaQueryWrapperX.eq(ContractRiskDO::getContractId, id);
        //不取已处理的风险
        List statusList = new ArrayList<>();
        statusList.add("1");
        lambdaQueryWrapperX.notIn(ContractRiskDO::getStatus,statusList);
        ContractRiskDO contractRiskDO = contractRiskMapper.selectOne(lambdaQueryWrapperX);
        if (ObjectUtil.isNull(contractRiskDO)) {
            throw exception(SYSTEM_ERROR, "合同风险不存在。");
        }
        ContractDO contractDO = contractMapper.selectById(contractRiskDO.getContractId());
        respVO = ContractRiskConverter.INSTANCE.do2Resp(contractRiskDO);
        if (ObjectUtil.isNotNull(contractDO)) {
            respVO.setContractCode(contractDO.getCode());
            respVO.setContractName(contractDO.getName());
            ContractStatusEnums statusEnums= ContractStatusEnums.getInstance(contractDO.getStatus());
            if(ObjectUtil.isNotNull(statusEnums)) {
                respVO.setRiskType(statusEnums.getDesc());
            }
            ContractType contractType = contractTypeMapper.selectById(contractDO.getContractType());
            if (ObjectUtil.isNotNull(contractType)) {
                respVO.setContractTypeName(contractType.getName());
            }

            respVO.setContractCode(contractDO.getCode());
        }
        List<BusinessFileDO> fileDOList = businessFileService.selectListByBusiness(contractRiskDO.getId());
        if (CollectionUtil.isNotEmpty(fileDOList)) {
            List<BusinessFileVO> fileVOList = BusinessFileConverter.INSTANCE.d2R(fileDOList);
            respVO.setFileVOList(fileVOList);
        }
        if(contractRiskDO.getCreator() == null){
            respVO.setCreatorName("系统生成");
        }else{
            AtomicReference<AdminUserRespDTO> userRespDTO = new AtomicReference<>();
            DataPermissionUtils.executeIgnore(()->{
                userRespDTO.set(adminUserApi.getUser(Long.valueOf(contractRiskDO.getCreator())));

            });
            if (ObjectUtil.isNotNull(userRespDTO)) {
                respVO.setCreatorName(userRespDTO.get().getNickname());
            }
        }
        return respVO;
    }

    @Override
    public PageResult<ContractRiskDO> getContractRiskPage(ContractRiskPageReqVO pageReqVO) {
        return contractRiskMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closePerformance(String id) {
        ContractDO contractDO = new ContractDO().setId(id).setStatus(ContractStatusEnums.PERFORMANCE_CLOSURE.getCode());
        contractMapper.updateById(contractDO);
        //将该合同相关的，除了已完成的所有计划都变成关闭状态
        List<PaymentScheduleDO> toDoPlans = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>().eq(PaymentScheduleDO::getContractId, id).ne(PaymentScheduleDO::getStatus, DONE.getCode()));
        if (CollectionUtil.isNotEmpty(toDoPlans)) {
            toDoPlans.stream().forEach(plan -> {
                plan.setStatus(CLOSE.getCode());
            });
            paymentScheduleMapper.updateBatch(toDoPlans);
        }
    }

    /**
     * 若全部履约计划均为【已完成】状态，需要二次确认履约完成
     * 若存在未完成的履约计划，不可变更为【履约完成】状态，点击【履约完成】toast提示：当前履约计划中还有 [未完成个数] 个未完成的履约计划，请先完成
     */
    @Override
    public void check4completePerformance(String id) {
        //若除了当前计划，其他履约计划均为【已完成】状态，合同履约状态可变更为履约完成，当前履约计划也变更为已完成状态
        List<PaymentScheduleDO> toDoPlans = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>()
                .ne(PaymentScheduleDO::getStatus, DONE.getCode())
                .eq(PaymentScheduleDO::getContractId, id));
        if (CollectionUtil.isNotEmpty(toDoPlans)) {
            //若除了当前计划，其他履约计划存在未完成的履约计划，不可变更为【履约完成】状态，提交时toast提示：当前履约计划中还有 [未完成个数] 个未完成的履约计划，请先完成
            throw exception(SYSTEM_ERROR, "当前合同相关履约计划中还有 " + toDoPlans.size() + "个 未完成的履约计划，请先完成。");
        }
    }

    @Override
    public void completePerformance(String id) {
        //先校验合同相关计划是否都完成
        check4completePerformance(id);
        contractMapper.updateById(new ContractDO().setId(id).setStatus(ContractStatusEnums.PERFORMANCE_COMPLETE.getCode()).setPerformanceCompleteDate(new Date()));
    }

    /**
     * 风险忽略
     * 确认忽略当前风险，履约状态将变更为【履约中】
     *
     * @param id 合同id
     * @return
     */
    @Override
    public String ignoreRisk(String id) {
        ContractDO contractDO = contractMapper.selectOne(new LambdaQueryWrapperX<ContractDO>().eq(ContractDO::getId, id).select(ContractDO::getId, ContractDO::getName, ContractDO::getStatus));
        if (ObjectUtil.isNull(contractDO)) {
            throw exception(SYSTEM_ERROR, "该合同不存在。");
        }
        contractMapper.updateById(contractDO.setStatus(PERFORMING.getCode()));
        return id;
    }
}

