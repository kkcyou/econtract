package com.yaoan.module.system.mq.basic.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 接受接口传来的数据 辅助类
 */
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class MqSupplierVO implements Serializable {
    private String userId;
    private String userName;
    private SupplierInfoVO supplierInfo;
}
