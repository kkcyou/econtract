package com.yaoan.module.system.mq.basic.domain.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBasciBO implements Serializable {

    private String userGuid;
    private Integer changePassword;
    private Integer retryNumber;
    private String loginType;
    private String loginTypeName;
    private String orgType;
    private String orgTypeName;
    private String bankGuid;
    private String userName;
    private Integer admin;

    private String regionGuid;

    private String regionCode;

    private Integer userType;

    private String loginName;

    private String caKey;
    private String idCard;

    private String qqCode;

    private String microMsgCode;

    private String mobile;

    private String email;

    private String password;
    private String regulatorsGuid;

    private String orgGuid;
    private String financeDeptGuid;
    private String agentGuid;
    private String supplierGuid;
    private String expertGuid;

    private Integer dorder;

    private Integer valid;

    private String modiUser;

    private String remark;

    private String orgName;
    private String telephone;

    /**
     * 用户拥有权限的组织id列表
     */
    private List<String> orgList;

    /**
     * 默认分配给采购单位的  agentguid,用于无采购凭据的采购项目
     */
    private String defaultAgentGuid;

    /**
     * 用户是否是可录入商品的协议供应商
     */
    private Boolean supplierAgreement;

}
