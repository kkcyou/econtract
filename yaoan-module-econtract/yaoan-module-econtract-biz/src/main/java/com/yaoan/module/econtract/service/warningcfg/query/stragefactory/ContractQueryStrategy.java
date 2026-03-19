package com.yaoan.module.econtract.service.warningcfg.query.stragefactory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yaoan.framework.mybatis.core.query.QueryWrapperX;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.service.warningcfg.query.DOQueryStrategy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class ContractQueryStrategy implements DOQueryStrategy<ContractDO> {
    @Resource
    private ContractMapper contractMapper; // 注入对应的Mapper
    @Override
    public Class<ContractDO> getDOClass() {
        return ContractDO.class;
    }

    @Override
    public QueryWrapperX<ContractDO> createQueryWrapper() {
        return new QueryWrapperX<ContractDO>();
    }

    @Override
    public BaseMapper<ContractDO> getMapper() {
        return contractMapper;
    }
}