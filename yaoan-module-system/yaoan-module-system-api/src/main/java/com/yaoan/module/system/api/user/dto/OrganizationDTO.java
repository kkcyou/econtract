package com.yaoan.module.system.api.user.dto;

import lombok.Data;

/**
 * 管理后台的用户 DO
 *
 * @author 芋道源码
 */


@Data
public class OrganizationDTO {

    /**
     * 用户ID
     */
    private String id;
    /**
     * 单位编码
     */
    private String code;
    /**
     * 单位名称
     */
    private String name;
    /**
     * 单位地址
     */
    private String address;
    /**
     * 联系人
     */
    private String linkMan;

    /**
     * 联系人传真
     */
    private String linkFax;
    /**
     * 联系人电话
     */
    private String linkPhone;
    /**
     * 单位类型
     */
    private String type;
    /**
     * 单位类型名称
     */
    private String typeName;
    /**
     * 单位人区划编码
     */
    private String regionCode;
    /**
     * 单位人区划Guid
     */
    private String regionGuid;
    /**
     * 创建时间
     */
    private Integer payType;
    private String fixedPhone;
    private String postCode;
    private String legal;
    private String legalPhone;
    private String idCard;



}
