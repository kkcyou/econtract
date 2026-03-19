package com.yaoan.module.econtract.service.warningcfg.query;

import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DOQueryStrategyFactory {
    private final Map<String, DOQueryStrategy<?>> strategyMap;

    public DOQueryStrategyFactory(Map<String, DOQueryStrategy<?>> strategyMap) {
        this.strategyMap = strategyMap;
    }

    @SuppressWarnings("unchecked")
    public <T extends BaseDO> DOQueryStrategy<T> getStrategy(String type) {
        return (DOQueryStrategy<T>) strategyMap.get(type);
    }
}
