package com.yaoan.module.econtract.controller.admin.contract.vo;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ContractXmlConfigPageVO{
    /**
     * 主键ID
     */
    private String id;

    /**
     * 合同配置名称
     */
    private String name;

    /**
     * 合同配置编码
     */
    private String code;

    /**
     * 合同类型
     */
    private String contractType;
    private String contractTypeName;

    /**
     * 状态(1:关闭,0:开启)
     */
    private Integer status;

    /**
     * 描述信息
     */
    private String description;
    private LocalDateTime createTime;
}
