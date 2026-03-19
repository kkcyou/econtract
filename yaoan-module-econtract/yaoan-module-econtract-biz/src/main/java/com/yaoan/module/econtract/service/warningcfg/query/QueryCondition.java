package com.yaoan.module.econtract.service.warningcfg.query;

// 查询条件实体类

import lombok.Data;

@Data
public class QueryCondition {
    private String field;       // 字段名（如 "age"）
    private String calField;       // 字段名（如 "age"）
    private Integer operator;    // 操作符（"le", "ge", "eq", "in"） 枚举类 WarningCompareTypeEnum
    private Integer monitorCalType;    // 监控项计算方式 0
    private Integer n = 0;
    private Object value;       // 值（单个值或集合）
    private Boolean sqlFlag = false;       // sql操作
}