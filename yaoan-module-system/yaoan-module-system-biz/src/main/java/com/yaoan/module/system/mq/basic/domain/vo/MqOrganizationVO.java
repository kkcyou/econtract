package com.yaoan.module.system.mq.basic.domain.vo;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
/**
 * mq 接收采购单位信息实体类 (请勿修改,如基础库对接字段有修改可调整)
 **/
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class MqOrganizationVO implements Serializable {

    private String orgGuid;

    private String regionGuid;

    private String orgCode;

    private String orgName;

    private String shortName;

    private String orgPGuid;

    private Integer orgType;

    private String linkMan;

    @SerializedName("fixedTelephone")
    private String linkMobile;

    private String linkFax;

    private String postCode;

    private String address;

    private String interfaceCode;

    private Integer valid;

    private String modiUser;

    private String remark;

    private String linkPhone;

    private String email;

    private String regionCode;

    /**
     * 第三方系统毕加索的orgid
     */
    private String bijiasuoOrgId;

    private String legal;
    private String legalPhone;
}
