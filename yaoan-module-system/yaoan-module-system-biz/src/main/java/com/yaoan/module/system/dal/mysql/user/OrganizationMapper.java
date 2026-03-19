package com.yaoan.module.system.dal.mysql.user;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.system.controller.admin.gcy.supplier.vo.SupplierPageReqVo;
import com.yaoan.module.system.controller.admin.org.vo.OrgReqVo;
import com.yaoan.module.system.controller.admin.region.vo.RegionReqVo;
import com.yaoan.module.system.dal.dataobject.user.OrganizationDO;
import com.yaoan.module.system.dal.dataobject.user.SupplyDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface OrganizationMapper extends BaseMapperX<OrganizationDO> {

   default List<OrganizationDO> selectByRegionCode(RegionReqVo reqVO){
       return selectList(new LambdaQueryWrapperX<OrganizationDO>()
               .eqIfPresent(OrganizationDO::getRegionCode, reqVO.getRegionCode()));
   }



    default PageResult<OrganizationDO> selectPage(OrgReqVo reqVO) {
        LambdaQueryWrapperX<OrganizationDO> organizationDOLambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        organizationDOLambdaQueryWrapperX.likeIfPresent(OrganizationDO::getName, reqVO.getName());
        if (CollectionUtil.isNotEmpty(reqVO.getOrgIds())){
            if (IfNumEnums.YES.getCode().equals(reqVO.getIsOpen())){
                organizationDOLambdaQueryWrapperX.inIfPresent(OrganizationDO::getId, reqVO.getOrgIds());
            }
            if (IfNumEnums.NO.getCode().equals(reqVO.getIsOpen())){
                organizationDOLambdaQueryWrapperX.notIn(OrganizationDO::getId, reqVO.getOrgIds());
            }
        }
        return selectPage(reqVO, organizationDOLambdaQueryWrapperX);
    }


}
