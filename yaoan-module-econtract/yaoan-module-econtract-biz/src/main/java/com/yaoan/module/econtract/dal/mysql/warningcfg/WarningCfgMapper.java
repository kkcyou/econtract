package com.yaoan.module.econtract.dal.mysql.warningcfg;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.WarningCfgExportReqVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.WarningCfgPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.warningcfg.WarningCfgDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 预警检查配置表(new预警) Mapper
 *
 * @author admin
 */
@Mapper
public interface WarningCfgMapper extends BaseMapperX<WarningCfgDO> {

    default PageResult<WarningCfgDO> selectPage(WarningCfgPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WarningCfgDO>()
                .likeIfPresent(WarningCfgDO::getName, reqVO.getName())
                .eqIfPresent(WarningCfgDO::getModelCode, reqVO.getModelCode())
                .likeIfPresent(WarningCfgDO::getModelName, reqVO.getModelName())
                .inIfPresent(WarningCfgDO::getModelId, reqVO.getModelIds())
                .eqIfPresent(WarningCfgDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(WarningCfgDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WarningCfgDO::getCreateTime));
    }

    default List<WarningCfgDO> selectList(WarningCfgExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<WarningCfgDO>()
                .likeIfPresent(WarningCfgDO::getName, reqVO.getName())
                .eqIfPresent(WarningCfgDO::getModelCode, reqVO.getModelCode())
                .likeIfPresent(WarningCfgDO::getModelName, reqVO.getModelName())
                .eqIfPresent(WarningCfgDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(WarningCfgDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WarningCfgDO::getId));
    }

}
