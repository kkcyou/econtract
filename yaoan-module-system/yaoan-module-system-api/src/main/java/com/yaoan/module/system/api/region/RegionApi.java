package com.yaoan.module.system.api.region;

import com.yaoan.module.system.api.region.dto.RegionDTO;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/18 17:32
 */
public interface RegionApi {


    RegionDTO getRegionById(Long id);

    RegionDTO getRegionByCode(String code);

    /**
     * 获取本级及下级区划
     *
     * @param code 目标区划编码
     * @return 区划列表可查看
     */
    List<String> getPermitRegionByCode(String code);

    /**
     * 获取本级及下级区划
     *
     * @param code 目标区划编码
     * @return 区划列表可查看
     */
    List<RegionDTO> getPermitRegion(String code);

    /**
     * 获取上级区划编码
     *
     * @param regionCode
     * @return
     */

    String getParentRegionByCode(String regionCode);

    List<RegionDTO> getRegionByCodes(List<String> regionCodeList);

    RegionDTO getRegionByGuid(String regionGuid);

    List<RegionDTO> getRegionByGuidS(List<String> id);
    /**
     * 根据区划guid获取区划信息
     * @param regionGuid
     * @return
     */
    RegionDTO getRegionById(String regionGuid);

    String getRegionIdByName(String name);
}
