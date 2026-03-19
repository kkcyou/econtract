package com.yaoan.module.econtract.dal.mysql.contract.ext.gcy;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.save.OrderContractCreateReqV2Vo;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.rel.ContractOrderRelDO;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.enums.gcy.gpmall.HLJContractStatusEnums;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.yaoan.module.econtract.service.workbench.WorkBenchServiceImpl.TREND_END_DATE;
import static com.yaoan.module.econtract.service.workbench.WorkBenchServiceImpl.TREND_START_DATE;


/**
 * @description:
 * @author: Pele
 * @date: 2024/11/6 16:40
 */
@Mapper
public interface ContractOrderExtMapper extends BaseMapperX<ContractOrderExtDO> {

    default List<ContractOrderExtDO> selectList1(OrderContractCreateReqV2Vo gpMallContractCreateReqVO) {
        MPJLambdaWrapper<ContractOrderExtDO> mpjLambdaWrapper = new MPJLambdaWrapper<ContractOrderExtDO>()
                .select(ContractOrderExtDO::getId, ContractOrderExtDO::getStatus, ContractOrderExtDO::getContractDrafter, ContractOrderExtDO::getOrderId)
                .leftJoin(ContractOrderRelDO.class, ContractOrderRelDO::getContractId, ContractOrderExtDO::getId)
                .distinct();
        mpjLambdaWrapper.notIn(ContractOrderExtDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_DELETE.getCode());
        if (CollectionUtil.isNotEmpty(gpMallContractCreateReqVO.getOrderIdList())) {
            mpjLambdaWrapper.in(ContractOrderExtDO::getOrderId, gpMallContractCreateReqVO.getOrderIdList());
        }

        return selectList(mpjLambdaWrapper);
    }


    default List<ContractOrderExtDO> selectAllSigned() {
        List<Integer> status = new ArrayList<>();
        status.add(ContractStatusEnums.SIGN_COMPLETED.getCode());
        status.add(ContractStatusEnums.PERFORMANCE_CLOSURE.getCode());
        status.add(ContractStatusEnums.PERFORMING.getCode());
        status.add(ContractStatusEnums.PERFORMANCE_COMPLETE.getCode());
        status.add(ContractStatusEnums.PERFORMANCE_RISK.getCode());
        status.add(ContractStatusEnums.PERFORMANCE_RISK_DISPUTE.getCode());
        status.add(ContractStatusEnums.PERFORMANCE_RISK_PAUSE.getCode());
        status.add(ContractStatusEnums.PERFORMANCE_RISK_EXTENSION.getCode());
        status.add(ContractStatusEnums.PERFORMANCE_RISK_OVERDUE.getCode());
        status.add(ContractStatusEnums.SIGN_COMPLETED.getCode());
        status.add(ContractStatusEnums.SIGN_COMPLETED.getCode());


        return selectList(new LambdaQueryWrapperX<ContractOrderExtDO>()
                .select(ContractOrderExtDO::getId, ContractOrderExtDO::getStatus, ContractOrderExtDO::getTotalMoney)
                        .in(ContractOrderExtDO::getStatus,status)
//                .in(ContractOrderExtDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode())
        );
    }

    default List<ContractOrderExtDO> select4Trend(Map<String, Date> rsMap) {

        MPJLambdaWrapper<ContractOrderExtDO> wrapper = new MPJLambdaWrapper<ContractOrderExtDO>()
                .select(ContractOrderExtDO::getId, ContractOrderExtDO::getTotalMoney, ContractOrderExtDO::getContractSignTime)
                .leftJoin(ContractDO.class,ContractDO::getId,ContractOrderExtDO::getId)
                .eq(ContractDO::getStatus, ContractStatusEnums.SIGN_COMPLETED.getCode())
                .between(ContractOrderExtDO::getContractSignTime, rsMap.get(TREND_START_DATE), rsMap.get(TREND_END_DATE))
                ;

        return selectList(wrapper);
    }

    default List<ContractOrderExtDO> selectAllSignedByTime(String startTime, String endTime){
        LambdaQueryWrapper<ContractOrderExtDO> queryWrapper = new LambdaQueryWrapperX<ContractOrderExtDO>()
                .select(ContractOrderExtDO::getId, ContractOrderExtDO::getStatus, ContractOrderExtDO::getTotalMoney, ContractOrderExtDO::getProjectCategoryCode)
                .in(ContractOrderExtDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        if (ObjectUtil.isNotEmpty(startTime) && ObjectUtil.isNotEmpty(endTime)) {
            queryWrapper.ge(ContractOrderExtDO::getCreateTime, LocalDateTime.parse(startTime, formatter))
                    .lt(ContractOrderExtDO::getCreateTime, LocalDateTime.parse(endTime, formatter));
        }
        return selectList(queryWrapper);
    };
}
