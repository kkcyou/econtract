package com.yaoan.module.system.controller.admin.org.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * RegionReqVO
 */
@Data
public class OrgRespVo {
    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
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
     * 联系传真
     */
    private String linkFax;
    /**
     * 联系电话
     */
    private String linkPhone;
    /**
     * 联系人
     */
    private String linkMan;
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
