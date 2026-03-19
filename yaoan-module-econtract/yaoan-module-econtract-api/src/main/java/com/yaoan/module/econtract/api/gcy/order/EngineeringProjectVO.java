package com.yaoan.module.econtract.api.gcy.order;

import lombok.Data;

/**
 * @description: 订单的工程项目信息
 * @author: Pele
 * @date: 2023/12/3 19:25
 */
@Data
public class EngineeringProjectVO{

    private static final long serialVersionUID = 3736100192712680723L;
    /**
     * id主键
     */
    private String id;

    /**
     * 承建单位名称
     */
    private String contractorName;

    /**
     * 项目编号
     */
    private String engineeringProjectCode;

    /**
     * 工程项目主键ID
     */
    private String engineeringProjectGuid;

    /**
     * 项目名称
     */
    private String engineeringProjectName;

    /**
     * 甲方单位
     */
    private String ownerOrgName;


}
