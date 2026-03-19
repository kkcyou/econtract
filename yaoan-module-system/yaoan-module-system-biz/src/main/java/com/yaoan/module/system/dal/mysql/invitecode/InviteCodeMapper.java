package com.yaoan.module.system.dal.mysql.invitecode;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.system.dal.dataobject.invitecode.InviteCodeDO;
import org.apache.ibatis.annotations.Mapper;
import com.yaoan.module.system.controller.admin.invitecode.vo.*;

/**
 * 邀请码管理 Mapper
 *
 * @author admin
 */
@Mapper
public interface InviteCodeMapper extends BaseMapperX<InviteCodeDO> {

    default PageResult<InviteCodeDO> selectPage(InviteCodePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InviteCodeDO>()
                .eqIfPresent(InviteCodeDO::getCode, reqVO.getCode())
                .eqIfPresent(InviteCodeDO::getValidDays, reqVO.getValidDays())
                .betweenIfPresent(InviteCodeDO::getValidEndDate, reqVO.getValidEndDate())
                .eqIfPresent(InviteCodeDO::getValidTimes, reqVO.getValidTimes())
                .eqIfPresent(InviteCodeDO::getType, reqVO.getType())
                .eqIfPresent(InviteCodeDO::getStatus, reqVO.getStatus())
                .eqIfPresent(InviteCodeDO::getUserId, reqVO.getUserId())
                .eqIfPresent(InviteCodeDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(InviteCodeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(InviteCodeDO::getId));
    }

    default List<InviteCodeDO> selectList(InviteCodeExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<InviteCodeDO>()
                .eqIfPresent(InviteCodeDO::getCode, reqVO.getCode())
                .eqIfPresent(InviteCodeDO::getValidDays, reqVO.getValidDays())
                .betweenIfPresent(InviteCodeDO::getValidEndDate, reqVO.getValidEndDate())
                .eqIfPresent(InviteCodeDO::getValidTimes, reqVO.getValidTimes())
                .eqIfPresent(InviteCodeDO::getType, reqVO.getType())
                .eqIfPresent(InviteCodeDO::getStatus, reqVO.getStatus())
                .eqIfPresent(InviteCodeDO::getUserId, reqVO.getUserId())
                .eqIfPresent(InviteCodeDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(InviteCodeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(InviteCodeDO::getId));
    }

}
