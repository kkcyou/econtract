package com.yaoan.module.system.mq.basic.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * mq 同步采购人用户接收参数实体类 (请勿修改,如基础库对接字段有修改可调整)
 **/
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class MqUserVO implements Serializable {

    private String userGuid;

    private String userName;

    private String regionGuid;

    private String regionCode;

    private Integer userType;

    private String loginName;

    private String qqCode;

    private String microMsgCode;

    private String mobile;

    private String email;

    private String password;

    private String orgGuid;

    private String supplierguid;
    private String supplierId;

    private Integer valid;

    private String modiUser;

    private String remark;

    private String orgName;

    private String telphone;

    private String idCard;

    private Integer admin;


}
