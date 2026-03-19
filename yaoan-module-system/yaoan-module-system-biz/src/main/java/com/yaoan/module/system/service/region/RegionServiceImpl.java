package com.yaoan.module.system.service.region;

import cn.hutool.core.collection.CollectionUtil;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.config.dto.SystemConfigRespDTO;
import com.yaoan.module.system.api.region.dto.MongoliaRegionDTO;
import com.yaoan.module.system.controller.admin.region.vo.BigRegionSimpleRespVO;
import com.yaoan.module.system.controller.admin.region.vo.RegionListReqVO;
import com.yaoan.module.system.controller.admin.region.vo.RegionReqVo;
import com.yaoan.module.system.controller.admin.region.vo.RegionSimpleRespVO;
import com.yaoan.module.system.convert.region.RegionConvert;
import com.yaoan.module.system.dal.dataobject.region.Region;
import com.yaoan.module.system.dal.mysql.region.RegionMapper;
import com.yaoan.module.system.enums.config.SystemConfigKeyEnums;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * <p>
 * 区划表 服务实现类
 * </p>
 *
 * @author doujiale
 * @since 2024-01-03
 */
@Service
public class RegionServiceImpl implements RegionService {

    @Resource
    private RegionMapper regionMapper;
    @Resource
    private SystemConfigApi systemConfigApi;


    public List<Region> getRegionList(RegionListReqVO reqVO) {
       String regionRoot= systemConfigApi.getConfigByKey(SystemConfigKeyEnums.REGION_ROOT.getKey());
        reqVO.setRegionRoot(regionRoot);
        return regionMapper.selectList(reqVO);
    }
    @Override
    public List<Region> getPostRegionList(RegionReqVo reqVO) {
        return regionMapper.selectPostList(reqVO);
    }

    @Override
    public BigRegionSimpleRespVO getSimpleRegionListV2(RegionListReqVO reqVO) {
        List<Region> list = getRegionList(reqVO);
        // 排序后，返回给前端
        list.sort(Comparator.comparing(Region::getSort, Comparator.nullsLast(Integer::compareTo)));
        List<RegionSimpleRespVO> respVOS = RegionConvert.INSTANCE.convertList02(list);
        List<String> cKeys = new ArrayList<String>();
        cKeys.add(SystemConfigKeyEnums.REGION_DATA_VERSION.getKey());
        List<SystemConfigRespDTO> systemConfigRespDTOList =
                systemConfigApi.getConfigsByCKeys(cKeys);

        BigRegionSimpleRespVO respVO = new BigRegionSimpleRespVO();
        respVO.setRegionSimpleRespVOList(respVOS);
        if (CollectionUtil.isNotEmpty(systemConfigRespDTOList)) {
            respVO.setRegionDataVersion(systemConfigRespDTOList.get(0).getCValue());
        }
        return respVO;
    }


}
