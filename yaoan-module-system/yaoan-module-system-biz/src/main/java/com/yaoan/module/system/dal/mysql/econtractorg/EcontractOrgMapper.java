package com.yaoan.module.system.dal.mysql.econtractorg;

import java.util.*;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.system.dal.dataobject.econtractorg.EcontractOrgDO;
import org.apache.ibatis.annotations.Mapper;
import com.yaoan.module.system.controller.admin.econtractorg.vo.*;

/**
 * 电子合同单位信息 Mapper
 *
 * @author admin
 */
@Mapper
public interface EcontractOrgMapper extends BaseMapperX<EcontractOrgDO> {

    default PageResult<EcontractOrgDO> selectPage(EcontractOrgPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<EcontractOrgDO>()
                .likeIfPresent(EcontractOrgDO::getName, reqVO.getName())
                .eqIfPresent(EcontractOrgDO::getAddress, reqVO.getAddress())
                .eqIfPresent(EcontractOrgDO::getTaxpayerId, reqVO.getTaxpayerId())
                .eqIfPresent(EcontractOrgDO::getLinkFax, reqVO.getLinkFax())
                .eqIfPresent(EcontractOrgDO::getLinkMan, reqVO.getLinkMan())
                .eqIfPresent(EcontractOrgDO::getLinkPhone, reqVO.getLinkPhone())
                .eqIfPresent(EcontractOrgDO::getBankAccount, reqVO.getBankAccount())
                .likeIfPresent(EcontractOrgDO::getBankAccountName, reqVO.getBankAccountName())
                .likeIfPresent(EcontractOrgDO::getBankName, reqVO.getBankName())
                .eqIfPresent(EcontractOrgDO::getRegionCode, reqVO.getRegionCode())
                .betweenIfPresent(EcontractOrgDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(EcontractOrgDO::getPostCode, reqVO.getPostCode())
                .eqIfPresent(EcontractOrgDO::getLegal, reqVO.getLegal())
                .eqIfPresent(EcontractOrgDO::getLegalPhone, reqVO.getLegalPhone())
                .eqIfPresent(EcontractOrgDO::getRegionGuid, reqVO.getRegionGuid())
                .orderByDesc(EcontractOrgDO::getId));
    }

    default List<EcontractOrgDO> selectList(EcontractOrgExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<EcontractOrgDO>()
                .likeIfPresent(EcontractOrgDO::getName, reqVO.getName())
                .eqIfPresent(EcontractOrgDO::getAddress, reqVO.getAddress())
                .eqIfPresent(EcontractOrgDO::getTaxpayerId, reqVO.getTaxpayerId())
                .eqIfPresent(EcontractOrgDO::getLinkFax, reqVO.getLinkFax())
                .eqIfPresent(EcontractOrgDO::getLinkMan, reqVO.getLinkMan())
                .eqIfPresent(EcontractOrgDO::getLinkPhone, reqVO.getLinkPhone())
                .eqIfPresent(EcontractOrgDO::getBankAccount, reqVO.getBankAccount())
                .likeIfPresent(EcontractOrgDO::getBankAccountName, reqVO.getBankAccountName())
                .likeIfPresent(EcontractOrgDO::getBankName, reqVO.getBankName())
                .eqIfPresent(EcontractOrgDO::getRegionCode, reqVO.getRegionCode())
                .betweenIfPresent(EcontractOrgDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(EcontractOrgDO::getPostCode, reqVO.getPostCode())
                .eqIfPresent(EcontractOrgDO::getLegal, reqVO.getLegal())
                .eqIfPresent(EcontractOrgDO::getLegalPhone, reqVO.getLegalPhone())
                .eqIfPresent(EcontractOrgDO::getRegionGuid, reqVO.getRegionGuid())
                .orderByDesc(EcontractOrgDO::getId));
    }

}
