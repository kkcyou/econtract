package com.yaoan.module.econtract.dal.mysql.watermark;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.watermark.vo.WatermarkExportReqVO;
import com.yaoan.module.econtract.controller.admin.watermark.vo.WatermarkPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.watermark.WatermarkDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 水印管理 Mapper
 *
 * @author lls
 */
@Mapper
public interface WatermarkMapper extends BaseMapperX<WatermarkDO> {

    default PageResult<WatermarkDO> selectPage(WatermarkPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WatermarkDO>()
                .eqIfPresent(WatermarkDO::getCode, reqVO.getCode())
                .likeIfPresent(WatermarkDO::getName, reqVO.getName())
                .eqIfPresent(WatermarkDO::getType, reqVO.getType())
                .eqIfPresent(WatermarkDO::getWatermarkSize, reqVO.getWatermarkSize())
                .eqIfPresent(WatermarkDO::getWatermarkAngle, reqVO.getWatermarkAngle())
                .eqIfPresent(WatermarkDO::getWatermarkAlpha, reqVO.getWatermarkAlpha())
                .eqIfPresent(WatermarkDO::getPosition, reqVO.getPosition())
                .eqIfPresent(WatermarkDO::getFileId, reqVO.getFileId())
                .eqIfPresent(WatermarkDO::getFileUrl, reqVO.getFileUrl())
                .eqIfPresent(WatermarkDO::getPicWidth, reqVO.getPicWidth())
                .eqIfPresent(WatermarkDO::getPicHeight, reqVO.getPicHeight())
                .eqIfPresent(WatermarkDO::getDeptId, reqVO.getDeptId())
                .orderByDesc(WatermarkDO::getId));
    }

    default List<WatermarkDO> selectList(WatermarkExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<WatermarkDO>()
                .eqIfPresent(WatermarkDO::getCode, reqVO.getCode())
                .likeIfPresent(WatermarkDO::getName, reqVO.getName())
                .eqIfPresent(WatermarkDO::getType, reqVO.getType())
                .eqIfPresent(WatermarkDO::getWatermarkSize, reqVO.getWatermarkSize())
                .eqIfPresent(WatermarkDO::getWatermarkAngle, reqVO.getWatermarkAngle())
                .eqIfPresent(WatermarkDO::getWatermarkAlpha, reqVO.getWatermarkAlpha())
                .eqIfPresent(WatermarkDO::getPosition, reqVO.getPosition())
                .eqIfPresent(WatermarkDO::getFileId, reqVO.getFileId())
                .eqIfPresent(WatermarkDO::getFileUrl, reqVO.getFileUrl())
                .eqIfPresent(WatermarkDO::getPicWidth, reqVO.getPicWidth())
                .eqIfPresent(WatermarkDO::getPicHeight, reqVO.getPicHeight())
                .eqIfPresent(WatermarkDO::getDeptId, reqVO.getDeptId())
                .betweenIfPresent(WatermarkDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WatermarkDO::getId));
    }

}
