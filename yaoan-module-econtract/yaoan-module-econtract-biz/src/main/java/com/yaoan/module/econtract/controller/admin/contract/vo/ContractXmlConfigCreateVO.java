package com.yaoan.module.econtract.controller.admin.contract.vo;


import lombok.Data;

@Data
public class ContractXmlConfigCreateVO{
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

    /**
     * 状态(0:关闭,1:开启)
     */
    private Integer status;

    /**
     * 描述信息
     */
    private String description;
    /**
     * XML配置内容
     */
    private String xmlContent;
    private String configJson;
    private ConfigDataVO configData;
}
