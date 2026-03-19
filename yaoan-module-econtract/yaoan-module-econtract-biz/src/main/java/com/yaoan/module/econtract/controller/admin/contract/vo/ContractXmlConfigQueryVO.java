package com.yaoan.module.econtract.controller.admin.contract.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import lombok.Data;

import java.util.Date;

@Data
public class ContractXmlConfigQueryVO extends PageParam {
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
    @JsonFormat( pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date startCreateTime;

    @JsonFormat( pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date endCreateTime;
}
