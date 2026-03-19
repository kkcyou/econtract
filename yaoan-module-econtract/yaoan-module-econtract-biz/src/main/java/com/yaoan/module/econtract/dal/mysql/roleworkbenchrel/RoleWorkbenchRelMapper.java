package com.yaoan.module.econtract.dal.mysql.roleworkbenchrel;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;

import com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo.RoleWorkbenchRelExportReqVO;
import com.yaoan.module.econtract.controller.admin.roleworkbenchrel.vo.RoleWorkbenchRelPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.roleworkbenchrel.RoleWorkbenchRelDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 角色工作台关联 Mapper
 *
 * @author admin
 */
@Mapper
public interface RoleWorkbenchRelMapper extends BaseMapperX<RoleWorkbenchRelDO> {

    default PageResult<RoleWorkbenchRelDO> selectPage(RoleWorkbenchRelPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<RoleWorkbenchRelDO>()
                .eqIfPresent(RoleWorkbenchRelDO::getRoleId, reqVO.getRoleId())
                .likeIfPresent(RoleWorkbenchRelDO::getRoleName, reqVO.getRoleName())
                .eqIfPresent(RoleWorkbenchRelDO::getWorkbenchId, reqVO.getWorkbenchId())
                .likeIfPresent(RoleWorkbenchRelDO::getWorkbenchName, reqVO.getWorkbenchName())
                .betweenIfPresent(RoleWorkbenchRelDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(RoleWorkbenchRelDO::getId));
    }

    default List<RoleWorkbenchRelDO> selectList(RoleWorkbenchRelExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<RoleWorkbenchRelDO>()
                .eqIfPresent(RoleWorkbenchRelDO::getRoleId, reqVO.getRoleId())
                .likeIfPresent(RoleWorkbenchRelDO::getRoleName, reqVO.getRoleName())
                .eqIfPresent(RoleWorkbenchRelDO::getWorkbenchId, reqVO.getWorkbenchId())
                .likeIfPresent(RoleWorkbenchRelDO::getWorkbenchName, reqVO.getWorkbenchName())
                .betweenIfPresent(RoleWorkbenchRelDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(RoleWorkbenchRelDO::getId));
    }

}
