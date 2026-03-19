package com.yaoan.module.system.dal.mysql.systemuserrel;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.system.controller.admin.systemuserrel.vo.SystemuserRelExportReqVO;
import com.yaoan.module.system.controller.admin.systemuserrel.vo.SystemuserRelPageReqVO;
import com.yaoan.module.system.dal.dataobject.systemuserrel.SystemuserRelDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * 系统对接用户关系 Mapper
 *
 * @author lls
 */
@Mapper
public interface SystemuserRelMapper extends BaseMapperX<SystemuserRelDO> {

    default PageResult<SystemuserRelDO> selectPage(SystemuserRelPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SystemuserRelDO>()
                .eqIfPresent(SystemuserRelDO::getBuyerOrgId, reqVO.getBuyerOrgId())
                .eqIfPresent(SystemuserRelDO::getBuyerUserId, reqVO.getBuyerUserId())
                .eqIfPresent(SystemuserRelDO::getCurrentUserId, reqVO.getCurrentUserId())
                .eqIfPresent(SystemuserRelDO::getCurrentTenantId, reqVO.getCurrentTenantId())
                .eqIfPresent(SystemuserRelDO::getDeptId, reqVO.getDeptId())
                .likeIfPresent(SystemuserRelDO::getCreatorName, reqVO.getCreatorName())
                .betweenIfPresent(SystemuserRelDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SystemuserRelDO::getId));
    }

    default List<SystemuserRelDO> selectList(SystemuserRelExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<SystemuserRelDO>()
                .eqIfPresent(SystemuserRelDO::getBuyerOrgId, reqVO.getBuyerOrgId())
                .eqIfPresent(SystemuserRelDO::getBuyerUserId, reqVO.getBuyerUserId())
                .eqIfPresent(SystemuserRelDO::getCurrentUserId, reqVO.getCurrentUserId())
                .eqIfPresent(SystemuserRelDO::getCurrentTenantId, reqVO.getCurrentTenantId())
                .eqIfPresent(SystemuserRelDO::getDeptId, reqVO.getDeptId())
                .likeIfPresent(SystemuserRelDO::getCreatorName, reqVO.getCreatorName())
                .betweenIfPresent(SystemuserRelDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SystemuserRelDO::getId));
    }

}
