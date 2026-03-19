package com.yaoan.module.econtract.service.warningcfg.query.stragefactory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yaoan.framework.mybatis.core.query.QueryWrapperX;
import com.yaoan.module.econtract.dal.dataobject.bpm.contract.BpmContract;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.mysql.bpm.contract.BpmContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.service.warningcfg.query.DOQueryStrategy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ContractApproveQueryStrategy implements DOQueryStrategy<BpmContract> {
    @Resource
    private BpmContractMapper bpmContractMapper; // 注入对应的Mapper
    @Override
    public Class<BpmContract> getDOClass() {
        return BpmContract.class;
    }

    @Override
    public QueryWrapperX<BpmContract> createQueryWrapper() {
        return new QueryWrapperX<BpmContract>();
    }

    @Override
    public BaseMapper<BpmContract> getMapper() {
        return bpmContractMapper;
    }
}