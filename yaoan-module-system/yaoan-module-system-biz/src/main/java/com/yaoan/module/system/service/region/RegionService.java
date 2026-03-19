package com.yaoan.module.system.service.region;

import com.yaoan.module.system.controller.admin.region.vo.BigRegionSimpleRespVO;
import com.yaoan.module.system.controller.admin.region.vo.RegionListReqVO;
import com.yaoan.module.system.controller.admin.region.vo.RegionReqVo;
import com.yaoan.module.system.dal.dataobject.region.Region;

import java.util.List;

/**
 * <p>
 * 区划表 服务类
 * </p>
 *
 * @author doujiale
 * @since 2024-01-03
 */
public interface RegionService {


    BigRegionSimpleRespVO getSimpleRegionListV2(RegionListReqVO reqVO);
    /**
     * 根据条件查询区域
     * @param reqVO 查询条件
     * @return 区域列表
     */
    List<Region> getRegionList(RegionListReqVO reqVO);
    List<Region> getPostRegionList(RegionReqVo reqVO);


}
