package com.yaoan.module.econtract.service.warningcfg.query;

import com.yaoan.framework.mybatis.core.query.QueryWrapperX;
import com.yaoan.module.econtract.enums.warning.WarningCompareTypeEnum;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class QueryConditionBuilder {
    /**
     * 动态添加查询条件
     * @param wrapper QueryWrapperX
     * @param field   字段名（如 "age", "amount"）
     * @param operator 操作符（"le", "ge", "eq", "in"）
     * @param value   值
     * @param sqlFlag   是否执行查sql
     */
    public static <T> void addCondition(QueryWrapperX<T> wrapper, String field,String calField, Integer n, WarningCompareTypeEnum operator, Object value, Boolean sqlFlag) {
        switch (operator) {
            case LESS:
                if (StringUtils.isNotEmpty(calField)){
                    wrapper.apply("ABS(DATEDIFF("+field+", "+calField+")) < {0}", n);
                } else {
                    wrapper.lt(field, value);
                }
                break;
            case LESS_EQUAL:
                if (StringUtils.isNotEmpty(calField)){
                    wrapper.apply("ABS(DATEDIFF("+field+", "+calField+")) <= {0}", n);
                } else {
                    wrapper.le(field, value);
                }
                break;
            case GREATER:
                if (StringUtils.isNotEmpty(calField)){
                    wrapper.apply("ABS(DATEDIFF("+field+", "+calField+")) > {0}", n);
                } else {
                    wrapper.gt(field, value);
                }
                break;
            case GREATER_EQUAL:
                if (StringUtils.isNotEmpty(calField)){
                    wrapper.apply("ABS(DATEDIFF("+field+", "+calField+")) >= {0}", n);
                } else {
                    wrapper.ge(field, value);
                }
                break;
            case EQUAL:
                wrapper.eq(field, value);
                break;
            case NOT_EQUAL:
                wrapper.ne(field, value);
                break;
            case BETWEEN:
                if (value instanceof Collection<?>) {
                    wrapper.betweenIfPresent(field, ((List<?>) value).get(0), ((List<?>) value).get(1));
                }
                break;
            case IN:
                if (value instanceof Collection<?>) {
                    wrapper.in(field, (Collection<?>) value);
                }
                if (sqlFlag){
                    wrapper.inSql(field, value.toString());
                }
                break;
            default:
                throw new IllegalArgumentException("不支持的操作符：" + operator);
        }
    }
}

