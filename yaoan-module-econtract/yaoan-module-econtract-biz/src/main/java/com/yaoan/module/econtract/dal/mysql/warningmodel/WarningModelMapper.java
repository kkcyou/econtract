package com.yaoan.module.econtract.dal.mysql.warningmodel;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.warningmodel.WarningModelDO;
import org.apache.ibatis.annotations.Mapper;
import com.yaoan.module.econtract.controller.admin.warningmodel.vo.*;

/**
 * 预警模块来源（new预警） Mapper
 *
 * @author admin
 */
@Mapper
public interface WarningModelMapper extends BaseMapperX<WarningModelDO> {

    default PageResult<WarningModelDO> selectPage(WarningModelPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WarningModelDO>()
                .eqIfPresent(WarningModelDO::getCode, reqVO.getCode())
                .likeIfPresent(WarningModelDO::getName, reqVO.getName())
                .eqIfPresent(WarningModelDO::getParentId, reqVO.getParentId())
                .betweenIfPresent(WarningModelDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WarningModelDO::getId));
    }

    default List<WarningModelDO> selectList(WarningModelExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<WarningModelDO>()
                .eqIfPresent(WarningModelDO::getCode, reqVO.getCode())
                .likeIfPresent(WarningModelDO::getName, reqVO.getName())
                .eqIfPresent(WarningModelDO::getParentId, reqVO.getParentId())
                .betweenIfPresent(WarningModelDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WarningModelDO::getId));
    }

}
