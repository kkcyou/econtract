package com.yaoan.module.system.api.region;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.module.system.api.region.dto.RegionDTO;
import com.yaoan.module.system.convert.region.RegionConvert;
import com.yaoan.module.system.dal.dataobject.region.Region;
import com.yaoan.module.system.dal.mysql.region.RegionMapper;
import jodd.util.StringUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/18 17:36
 */
@Service
public class RegionApiImpl implements RegionApi {

    @Resource
    private RegionMapper regionMapper;


    @Override
    public RegionDTO getRegionById(Long id) {
        Region region = regionMapper.selectById(id);
        return RegionConvert.INSTANCE.convert2DTO(region);
    }

    @Override
    public RegionDTO getRegionByCode(String code) {
        Region region = regionMapper.selectOne(Region::getRegionCode, code);
        return RegionConvert.INSTANCE.convert2DTO(region);
    }

    @Override
    public List<String> getPermitRegionByCode(String code) {
        Region region = regionMapper.selectOne(Region::getRegionCode, code);
        if (null == region) {
            return null;
        }
        List<Region> regions = regionMapper.selectList(Region::getRegionParentGuid, region.getRegionGuid());

        if (CollectionUtil.isNotEmpty(regions)) {
            return CollectionUtils.convertList(regions, Region::getRegionCode);
        }
        return null;
    }

    @Override
    public List<RegionDTO> getPermitRegion(String code) {

        return null;
    }
    @Override
    public String getParentRegionByCode(String regionCode) {
        Region region = regionMapper.selectOne(Region::getRegionCode, regionCode);
        if (ObjectUtil.isNotNull(region) && StringUtil.isNotEmpty(region.getRegionParentGuid())) {
            regionCode = region.getRegionCode();
            region = regionMapper.selectOne(Region::getRegionGuid, region.getRegionParentGuid());
            return ObjectUtil.isNotEmpty(region) ? region.getRegionCode() : regionCode;
        }
        return null;
    }
    @Override
    public List<RegionDTO> getRegionByCodes(List<String> regionCodeList) {
        List<Region> regionList= regionMapper.selectList(Region::getRegionCode, regionCodeList);
        if(CollectionUtil.isEmpty(regionList)) {
            return Collections.emptyList();
        }
        return RegionConvert.INSTANCE.listD2DTO(regionList);
    }

    @Override
    public RegionDTO getRegionByGuid(String id) {
        Region region = regionMapper.selectById(id);
        return RegionConvert.INSTANCE.convert2DTO(region);
    }


    @Override
    public List<RegionDTO> getRegionByGuidS(List<String> ids) {
        List<Region> regions = regionMapper.selectBatchIds(ids);
        return RegionConvert.INSTANCE.convert2DTOS(regions);
    }

    @Override
    public RegionDTO getRegionById(String regionGuid) {
        Region region = regionMapper.selectById(regionGuid);
        return RegionConvert.INSTANCE.convert2DTO(region);
    }

    @Override
    public String getRegionIdByName(String name) {
        Region region = regionMapper.selectOne(Region::getRegionName, name);
        return ObjectUtil.isEmpty(region)? null :region.getRegionGuid();
    }
}
