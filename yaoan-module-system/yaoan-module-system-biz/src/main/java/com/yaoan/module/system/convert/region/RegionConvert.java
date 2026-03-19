package com.yaoan.module.system.convert.region;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.system.api.region.dto.MongoliaRegionDTO;
import com.yaoan.module.system.api.region.dto.RegionDTO;
import com.yaoan.module.system.controller.admin.region.vo.RegionRespVO;
import com.yaoan.module.system.controller.admin.region.vo.RegionSimpleRespVO;
import com.yaoan.module.system.dal.dataobject.region.Region;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RegionConvert {

    RegionConvert INSTANCE = Mappers.getMapper(RegionConvert.class);

    List<RegionRespVO> convertList(List<Region> list);

    List<MongoliaRegionDTO> convertDTOList(List<Region> list);

    List<RegionSimpleRespVO> convertList02(List<Region> list);

    PageResult<RegionSimpleRespVO> convertPage(PageResult<Region> page);

    @Mapping(target = "id",source = "regionGuid")
    RegionDTO convert2DTO(Region region);

    List<RegionDTO> listD2DTO(List<Region> regionList);


    List<RegionDTO> convert2DTOS(List<Region> regions);
}
