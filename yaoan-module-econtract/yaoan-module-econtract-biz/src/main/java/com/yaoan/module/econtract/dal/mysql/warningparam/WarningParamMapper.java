package com.yaoan.module.econtract.dal.mysql.warningparam;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.warningparam.WarningParamDO;
import org.apache.ibatis.annotations.Mapper;
import com.yaoan.module.econtract.controller.admin.warningparam.vo.*;

/**
 * 预警消息模板参数(new预警) Mapper
 *
 * @author admin
 */
@Mapper
public interface WarningParamMapper extends BaseMapperX<WarningParamDO> {

    default PageResult<WarningParamDO> selectPage(WarningParamPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WarningParamDO>()
                .likeIfPresent(WarningParamDO::getName, reqVO.getName())
                .eqIfPresent(WarningParamDO::getParamCfg, reqVO.getParamCfg())
                .eqIfPresent(WarningParamDO::getModelId, reqVO.getModelId())
                .betweenIfPresent(WarningParamDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WarningParamDO::getId));
    }

    default List<WarningParamDO> selectList(WarningParamExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<WarningParamDO>()
                .likeIfPresent(WarningParamDO::getName, reqVO.getName())
                .eqIfPresent(WarningParamDO::getParamCfg, reqVO.getParamCfg())
                .eqIfPresent(WarningParamDO::getModelId, reqVO.getModelId())
                .betweenIfPresent(WarningParamDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WarningParamDO::getId));
    }

}
