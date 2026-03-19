package com.yaoan.module.system.api.user;

import com.yaoan.module.system.api.user.dto.OrganizationDTO;

import java.util.List;

/**
 * 单位 用户 API 接口
 *
 * @author zhc
 */
public interface OrganizationApi {

    String getOrgRegionCodeByOrgId(String orgId);

    /**
     * 通过单位 ID 查询单位信息
     *
     * @param id 单位 ID
     * @return 查询单位信息
     */
    OrganizationDTO getOrganization(String id);

    /**
     * 通过单位id集合 查询单位信息
     */
    List<OrganizationDTO> getOrganizationByIds(List<String> ids);

    OrganizationDTO getOrgInfoByName(String name);
}
