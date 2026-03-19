package com.yaoan.module.system.dal.mysql.region;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.system.controller.admin.region.vo.RegionListReqVO;
import com.yaoan.module.system.controller.admin.region.vo.RegionPageReqVO;
import com.yaoan.module.system.controller.admin.region.vo.RegionReqVo;
import com.yaoan.module.system.dal.dataobject.region.Region;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 区划表 Mapper 接口
 * </p>
 *
 * @author doujiale
 * @since 2024-01-03
 */
@Mapper
public interface RegionMapper extends BaseMapperX<Region> {


    default List<Region> selectList(RegionListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<Region>()
                .likeIfPresent(Region::getRegionName, reqVO.getName())
                .eqIfPresent(Region::getRegionParentGuid, reqVO.getRegionRoot()));
    }

    default List<Region> selectPostList(RegionReqVo reqVO) {
        return selectList(new LambdaQueryWrapperX<Region>()
                .eqIfPresent(Region::getRegionParentGuid, reqVO.getRegionParentGuid()));
    }

    default List<Region> selectListByParentId(Collection<String> parentIds) {
        return selectList(Region::getRegionParentGuid, parentIds);
    }

    default PageResult<Region> selectPage(RegionPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<Region>()
                .likeIfPresent(Region::getRegionName, reqVO.getName())
                .eqIfPresent(Region::getRegionParentGuid, reqVO.getRegionParentGuid()));
    }


}
