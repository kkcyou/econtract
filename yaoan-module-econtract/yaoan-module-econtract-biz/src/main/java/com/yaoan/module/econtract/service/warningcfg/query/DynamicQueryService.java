package com.yaoan.module.econtract.service.warningcfg.query;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.framework.mybatis.core.query.QueryWrapperX;
import com.yaoan.module.econtract.enums.warning.WarningCompareTypeEnum;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DynamicQueryService {
    private final DOQueryStrategyFactory strategyFactory;

    public DynamicQueryService(DOQueryStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    /**
     * 动态查询方法
     * @param doType    DO 类型标识（如 "user", "order"）
     * @param conditions 条件列表（字段、操作符、值）
     * @param <T>       DO 类型
     * @return 查询结果
     */
    public <T extends BaseDO> List<T> dynamicQuery(String doType, List<QueryCondition> conditions) {
        // 1. 获取策略与 Mapper
        DOQueryStrategy<T> strategy = strategyFactory.getStrategy(doType);
        BaseMapper<T> mapper = strategy.getMapper();
        // 2. 构建查询条件
        QueryWrapperX<T> wrapper = strategy.createQueryWrapper();
        for (QueryCondition condition : conditions) {
            QueryConditionBuilder.addCondition(wrapper, condition.getField(),condition.getCalField(), condition.getN(), WarningCompareTypeEnum.getInstance(condition.getOperator()), condition.getValue(), condition.getSqlFlag());
        }

        // 3. 执行查询
        return mapper.selectList(wrapper);
    }
}

