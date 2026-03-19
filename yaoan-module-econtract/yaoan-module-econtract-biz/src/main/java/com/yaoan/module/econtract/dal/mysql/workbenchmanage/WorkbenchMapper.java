package com.yaoan.module.econtract.dal.mysql.workbenchmanage;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.controller.admin.workbenchmanage.vo.WorkbenchExportReqVO;
import com.yaoan.module.econtract.controller.admin.workbenchmanage.vo.WorkbenchPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.workbenchmanage.WorkbenchDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 工作台管理 Mapper
 *
 * @author lls
 */
@Mapper
public interface WorkbenchMapper extends BaseMapperX<WorkbenchDO> {

    default PageResult<WorkbenchDO> selectPage(WorkbenchPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<WorkbenchDO>()
                .eqIfPresent(WorkbenchDO::getCode, reqVO.getCode())
                .likeIfPresent(WorkbenchDO::getName, reqVO.getName())
                .eqIfPresent(WorkbenchDO::getComponent, reqVO.getComponent())
                .likeIfPresent(WorkbenchDO::getComponentName, reqVO.getComponentName())
                .betweenIfPresent(WorkbenchDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WorkbenchDO::getId));
    }

    default List<WorkbenchDO> selectList(WorkbenchExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<WorkbenchDO>()
                .eqIfPresent(WorkbenchDO::getCode, reqVO.getCode())
                .likeIfPresent(WorkbenchDO::getName, reqVO.getName())
                .eqIfPresent(WorkbenchDO::getComponent, reqVO.getComponent())
                .likeIfPresent(WorkbenchDO::getComponentName, reqVO.getComponentName())
                .betweenIfPresent(WorkbenchDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(WorkbenchDO::getId));
    }

}
