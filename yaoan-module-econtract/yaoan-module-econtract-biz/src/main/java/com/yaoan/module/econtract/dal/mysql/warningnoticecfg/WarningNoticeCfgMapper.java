package com.yaoan.module.econtract.dal.mysql.warningnoticecfg;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.warningnoticecfg.WarningNoticeCfgDO;
import org.apache.ibatis.annotations.Mapper;
import com.yaoan.module.econtract.controller.admin.warningnoticecfg.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * 预警通知配置表（new预警） Mapper
 *
 * @author admin
 */
@Mapper
public interface WarningNoticeCfgMapper extends BaseMapperX<WarningNoticeCfgDO> {

    default PageResult<WarningNoticeCfgDO> selectPage(WarningNoticeCfgPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WarningNoticeCfgDO>()
                .eqIfPresent(WarningNoticeCfgDO::getRuleId, reqVO.getRuleId())
                .eqIfPresent(WarningNoticeCfgDO::getUserType, reqVO.getUserType())
                .eqIfPresent(WarningNoticeCfgDO::getUserRole, reqVO.getUserRole())
                .eqIfPresent(WarningNoticeCfgDO::getUserIds, reqVO.getUserIds())
                .eqIfPresent(WarningNoticeCfgDO::getNoticeWay, reqVO.getNoticeWay())
                .eqIfPresent(WarningNoticeCfgDO::getContentTemplate, reqVO.getContentTemplate())
                .eqIfPresent(WarningNoticeCfgDO::getNoticeTimes, reqVO.getNoticeTimes())
                .betweenIfPresent(WarningNoticeCfgDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WarningNoticeCfgDO::getId));
    }

    default List<WarningNoticeCfgDO> selectList(WarningNoticeCfgExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<WarningNoticeCfgDO>()
                .eqIfPresent(WarningNoticeCfgDO::getRuleId, reqVO.getRuleId())
                .eqIfPresent(WarningNoticeCfgDO::getUserType, reqVO.getUserType())
                .eqIfPresent(WarningNoticeCfgDO::getUserRole, reqVO.getUserRole())
                .eqIfPresent(WarningNoticeCfgDO::getUserIds, reqVO.getUserIds())
                .eqIfPresent(WarningNoticeCfgDO::getNoticeWay, reqVO.getNoticeWay())
                .eqIfPresent(WarningNoticeCfgDO::getContentTemplate, reqVO.getContentTemplate())
                .eqIfPresent(WarningNoticeCfgDO::getNoticeTimes, reqVO.getNoticeTimes())
                .betweenIfPresent(WarningNoticeCfgDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WarningNoticeCfgDO::getId));
    }

    @Update("<script>"
            + "UPDATE ecms_warning_notice_cfg "
            + "SET deleted = 1 "
            + "WHERE config_id = #{configId}"
            + "</script>")
    void deleteBatch(@Param("configId")  String configId);
}
