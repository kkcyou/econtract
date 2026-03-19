package com.yaoan.module.system.api.user;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.system.api.region.RegionApi;
import com.yaoan.module.system.api.region.dto.RegionDTO;
import com.yaoan.module.system.api.user.dto.OrganizationDTO;
import com.yaoan.module.system.convert.user.OrganizationConvert;
import com.yaoan.module.system.dal.dataobject.user.OrganizationDO;
import com.yaoan.module.system.dal.mysql.user.OrganizationMapper;
import com.yaoan.module.system.service.user.OrganizationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 * 单位 用户 API 实现类
 *
 * @author zhc
 */
@Service
public class OrganizationApiImpl implements OrganizationApi {

    @Resource
    private OrganizationService organizationService;

    @Resource
    private OrganizationMapper organizationMapper;
    @Resource
    private RegionApi regionApi;


    @Override
    public String getOrgRegionCodeByOrgId(String orgId) {
        OrganizationDO organizationDO = organizationMapper.selectById(orgId);
        if (ObjectUtil.isNotEmpty(organizationDO) && ObjectUtil.isNotEmpty(organizationDO.getRegionGuid())) {
            RegionDTO regionByGuid = regionApi.getRegionByGuid(organizationDO.getRegionGuid());
            if (ObjectUtil.isNotEmpty(regionByGuid) && ObjectUtil.isNotEmpty(regionByGuid.getRegionCode())) {
                return regionByGuid.getRegionCode();
            }
        }
        return "";
    }


    @Override
    public OrganizationDTO getOrganization(String id) {
        OrganizationDTO organization = organizationService.getOrganization(id);
        return organization;
    }

    @Override
    public List<OrganizationDTO> getOrganizationByIds(List<String> ids) {
        List<OrganizationDTO> organizationDTOList =  organizationService.getOrganizationByIds(ids);
        return organizationDTOList;
    }

    @Override
    public OrganizationDTO getOrgInfoByName(String name) {
        OrganizationDO organization = organizationMapper.selectOne(new LambdaQueryWrapperX<OrganizationDO>().eq(OrganizationDO::getName, name).last(" limit 1"));
        return OrganizationConvert.INSTANCE.toDTO(organization);

    }
}
