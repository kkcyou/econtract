package com.yaoan.module.system.mq.basic.domain.vo;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
@SuppressWarnings("serial")
public class UserSynVO implements Serializable {

    //用户id
    @SerializedName(value="oauthId")
    private String userGuid;
    //供应商id
    @SerializedName(value="supplierId")
    private String supplierGuid;
    //用户类型
    @SerializedName(value="userType")
    private Integer userType;
    //登录名
    @SerializedName(value="accountName")
    private String loginName;
    //用户名
    @SerializedName(value="userName")
    private String username;
    //密码
    @SerializedName(value="pwd")
    private String password;
    //是否管理员 0
    private Integer isAdmin;
    //是否有效 1
    private Integer  valid;

    @SerializedName(value="changepassword")
    private BigDecimal changePassword;
    @SerializedName(value="retrynumber")
    private BigDecimal retryNumber;
    @SerializedName(value="logintype")
    private BigDecimal loginType;
    @SerializedName(value="bankguid")
    private String bankGuid;
    private String regionGuid;
    private String cakey;
    private String qqCode;
    private String microMsgCode;
    @SerializedName(value = "mobilePhone")
    private String mobile;
    @SerializedName(value = "userEmail")
    private String email;
    @SerializedName(value = "regulatorsguid")
    private String regulatorsGuid;
    private String orgGuid;
    @SerializedName(value = "financedeptguid")
    private String financeDeptGuid;
    @SerializedName(value = "agentguid")
    private String agentGuid;
    @SerializedName(value = "expertguid")
    private String expertGuid;
    private BigDecimal dorder;
    private String idCard;
    private String modiUser;
    private Date modiDate;
    private String remark;
    //机构名
    private String orgName;
    @SerializedName(value = "userBusTel")
    private String telephone;

    /**
     * 用户拥有权限的组织id列表
     */
    private List<String> orgList;

    /**
     * 默认分配给采购单位的  agentguid,用于无采购凭据的采购项目
     */
    @SerializedName(value = "defaultagentguid")
    private String defaultAgentGuid;

    /**
     *  用户是否是可录入商品的协议供应商
     */
    @SerializedName(value = "supplieragreement")
    private Boolean supplierAgreement;



}
