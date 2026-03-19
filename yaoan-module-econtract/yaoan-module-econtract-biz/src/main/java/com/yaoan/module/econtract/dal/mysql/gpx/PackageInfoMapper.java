package com.yaoan.module.econtract.dal.mysql.gpx;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.gpx.vo.GPXListReqVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.PackageInfoDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.SupplierInfoDO;
import com.yaoan.module.econtract.enums.gpx.BiddingMethodEnums;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collections;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/21 19:59
 */
@Mapper
public interface PackageInfoMapper extends BaseMapperX<PackageInfoDO> {

    /**
     * 供应商不可见联合采购数据
     */
    default PageResult<PackageInfoDO> selectPage4Sup(GPXListReqVO reqVO, List<String> idList) {
        MPJLambdaWrapper<PackageInfoDO> mpjLambdaWrapper = new MPJLambdaWrapper<PackageInfoDO>();
        mpjLambdaWrapper
                .selectAll(PackageInfoDO.class);
        mpjLambdaWrapper
//                .notIn(CollectionUtil.isNotEmpty(idList), PackageInfoDO::getPackageGuid, idList)
                .eq(PackageInfoDO::getIsLost, 0)
                .ne(PackageInfoDO::getHidden, 1)
                .orderByDesc(PackageInfoDO::getUpdateTime);
        if (reqVO.getBiddingMethodCode() != null) {
            mpjLambdaWrapper.eq(PackageInfoDO::getBiddingMethodCode, reqVO.getBiddingMethodCode());
        }
        if(ObjectUtil.isNotEmpty(reqVO.getSupplierName())){
            mpjLambdaWrapper.leftJoin(SupplierInfoDO.class, SupplierInfoDO::getPackageId, PackageInfoDO::getPackageGuid);
            mpjLambdaWrapper.like(SupplierInfoDO::getSupplierName, reqVO.getSupplierName());
        }
        if(ObjectUtil.isNotEmpty(reqVO.getProjectName())){
            mpjLambdaWrapper.like(PackageInfoDO::getProjectName, reqVO.getProjectName());
        }
        if(ObjectUtil.isNotEmpty(reqVO.getProjectCode())){
            mpjLambdaWrapper.like(PackageInfoDO::getProjectCode, reqVO.getProjectCode());
        }
        if(ObjectUtil.isNotEmpty(reqVO.getProjectType())){
            mpjLambdaWrapper.like(PackageInfoDO::getProjectType, reqVO.getProjectType());
        }
        // 供应商不可见联合采购数据
        mpjLambdaWrapper.ne(PackageInfoDO::getBiddingMethodCode, BiddingMethodEnums.UNION.getCode());
        return selectPage(reqVO, mpjLambdaWrapper);
    }


    /**
     * 交易起草列表，有正在进行的合同的包不展示
     */
    default PageResult<PackageInfoDO> selectPage1(GPXListReqVO reqVO, List<String> idList) {
        LoginUser user=SecurityFrameworkUtils.getLoginUser();
        String orgId = user.getOrgId();
        MPJLambdaWrapper<PackageInfoDO> mpjLambdaWrapper = new MPJLambdaWrapper<PackageInfoDO>()
                .selectAll(PackageInfoDO.class);
        mpjLambdaWrapper
                .notIn(CollectionUtil.isNotEmpty(idList), PackageInfoDO::getPackageGuid, idList)
                .eq(PackageInfoDO::getIsLost, 0)
                .ne(PackageInfoDO::getHidden, 1)
                .orderByDesc(PackageInfoDO::getUpdateTime)
        ;
        if(StringUtils.isNotBlank(orgId)){
            mpjLambdaWrapper.like(PackageInfoDO::getPurchaserOrgIds,orgId);
        }
        if (reqVO.getBiddingMethodCode() != null) {
            mpjLambdaWrapper.eq(PackageInfoDO::getBiddingMethodCode, reqVO.getBiddingMethodCode());
        }
        if(ObjectUtil.isNotEmpty(reqVO.getSupplierName())){
            mpjLambdaWrapper.leftJoin(SupplierInfoDO.class, SupplierInfoDO::getPackageId, PackageInfoDO::getPackageGuid);
            mpjLambdaWrapper.like(SupplierInfoDO::getSupplierName, reqVO.getSupplierName());
        }
        if(ObjectUtil.isNotEmpty(reqVO.getProjectName())){
            mpjLambdaWrapper.like(PackageInfoDO::getProjectName, reqVO.getProjectName());
        }
        if(ObjectUtil.isNotEmpty(reqVO.getProjectCode())){
            mpjLambdaWrapper.like(PackageInfoDO::getProjectCode, reqVO.getProjectCode());
        }
        if(ObjectUtil.isNotEmpty(reqVO.getProjectType())){
            mpjLambdaWrapper.like(PackageInfoDO::getProjectType, reqVO.getProjectType());
        }
        return selectPage(reqVO, mpjLambdaWrapper);
    }

    default List<PackageInfoDO> selectList4Contract(List<String> dolIdList) {
        if (CollectionUtil.isEmpty(dolIdList)) {
            return Collections.emptyList();
        }
        MPJLambdaWrapper<PackageInfoDO> wrapper = new MPJLambdaWrapper<PackageInfoDO>()
                .select(PackageInfoDO::getPackageGuid, PackageInfoDO::getBiddingMethodCode,PackageInfoDO::getProjectType, PackageInfoDO::getProjectTypeName)
                .selectAs(ContractOrderExtDO::getId, PackageInfoDO::getManagerName)
                .leftJoin(ContractOrderExtDO.class, ContractOrderExtDO::getBuyPlanPackageId, PackageInfoDO::getPackageGuid)
                .in(ContractOrderExtDO::getId, dolIdList);

        return selectList(wrapper);
    }

}