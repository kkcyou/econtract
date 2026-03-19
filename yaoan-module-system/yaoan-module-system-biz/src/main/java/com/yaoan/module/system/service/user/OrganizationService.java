package com.yaoan.module.system.service.user;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.system.api.user.dto.OrganizationDTO;
import com.yaoan.module.system.api.user.dto.RegionWithOrgDTO;
import com.yaoan.module.system.controller.admin.org.vo.OrgReqVo;
import com.yaoan.module.system.controller.admin.org.vo.OrgRespVo;
import com.yaoan.module.system.controller.admin.region.vo.RegionReqVo;

import java.util.List;

/**
 * 单位 用户  接口
 *
 * @author zhc
 */
public interface OrganizationService {

    /**
     * 根据单位名称获取单位信息
     */
    List<OrganizationDTO> getOrganizationByName(String name);

    /**
     * 通过单位  ID 查询单位 信息
     *
     * @param id 单位  ID
     * @return 查询单位 信息
     */
    OrganizationDTO getOrganization(String id);

    List<OrganizationDTO> getOrganizationList(RegionReqVo reqVO);

    List<OrganizationDTO> getOrganizationByIds(List<String> ids);

    List<OrganizationDTO> getOrganizationListByRegionGuid(String regionGuid);

    List<RegionWithOrgDTO> getOrgListByDistrict(RegionReqVo reqVO);

    PageResult<OrgRespVo> getOrgList(OrgReqVo reqVO);

    String openOrg(String orgId);
}
