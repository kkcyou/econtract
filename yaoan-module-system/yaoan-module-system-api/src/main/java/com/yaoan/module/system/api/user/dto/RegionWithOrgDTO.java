package com.yaoan.module.system.api.user.dto;

import lombok.Data;

import java.util.List;

/**
 * @author wsh
 */
@Data
public class RegionWithOrgDTO {

    /**
     * 区划id
     */
    private String id;
    /**
     * 区划编码
     */
    private String code;
    /**
     * 区划名称
     */
    private String name;
    /**
     * 区划父id
     */
    private String parentId;
    /**
     * 单位信息
     */
    private List<OrganizationDTO> children;

}
