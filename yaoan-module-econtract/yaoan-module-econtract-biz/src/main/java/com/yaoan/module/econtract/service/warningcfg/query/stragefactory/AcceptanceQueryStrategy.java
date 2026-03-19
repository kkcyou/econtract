package com.yaoan.module.econtract.service.warningcfg.query.stragefactory;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yaoan.framework.mybatis.core.query.QueryWrapperX;
import com.yaoan.module.econtract.dal.dataobject.acceptance.AcceptanceDO;
import com.yaoan.module.econtract.dal.mysql.acceptance.AcceptanceMapper;
import com.yaoan.module.econtract.service.warningcfg.query.DOQueryStrategy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AcceptanceQueryStrategy implements DOQueryStrategy<AcceptanceDO> {
    @Resource
    private AcceptanceMapper acceptanceMapper; // 注入对应的Mapper
    @Override
    public Class<AcceptanceDO> getDOClass() {
        return AcceptanceDO.class;
    }

    @Override
    public QueryWrapperX<AcceptanceDO> createQueryWrapper() {
        return new QueryWrapperX<AcceptanceDO>();
    }

    @Override
    public BaseMapper<AcceptanceDO> getMapper() {
        return acceptanceMapper;
    }
}