package com.yaoan.module.econtract.service.warningcfg.query.stragefactory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yaoan.framework.mybatis.core.query.QueryWrapperX;
import com.yaoan.module.econtract.dal.dataobject.bpm.contractborrow.ContractBorrowBpmDO;
import com.yaoan.module.econtract.dal.mysql.bpm.contractborrow.ContractBorrowBpmMapper;
import com.yaoan.module.econtract.service.warningcfg.query.DOQueryStrategy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class ContractBorrowQueryStrategy implements DOQueryStrategy<ContractBorrowBpmDO> {
    @Resource
    private ContractBorrowBpmMapper contractBorrowBpmMapper; // 注入对应的Mapper
    @Override
    public Class<ContractBorrowBpmDO> getDOClass() {
        return ContractBorrowBpmDO.class;
    }

    @Override
    public QueryWrapperX<ContractBorrowBpmDO> createQueryWrapper() {
        return new QueryWrapperX<ContractBorrowBpmDO>();
    }

    @Override
    public BaseMapper<ContractBorrowBpmDO> getMapper() {
        return contractBorrowBpmMapper;
    }
}