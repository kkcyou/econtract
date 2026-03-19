package com.yaoan.module.econtract.dal.mysql.gpx;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.gpx.GPXContractQuotationRelDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.PackageDetailInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/28 17:54
 */
@Mapper
public interface PackageDetailInfoMapper extends BaseMapperX<PackageDetailInfoDO> {
    @Update("<script>"
            + "UPDATE ecms_gpx_package_detail "
            + "SET deleted = 0 "
            + "WHERE package_guid IN "
            + "<foreach item='item' index='index' collection='packageIds' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    void active(@Param("packageIds") List<String> packageIds);

    default List<PackageDetailInfoDO> getPackageDetailList4Union(String contractGuid){
        MPJLambdaWrapper<PackageDetailInfoDO> mpjLambdaWrapper = new MPJLambdaWrapper<PackageDetailInfoDO>()
                .selectAll(PackageDetailInfoDO.class).orderByDesc(PackageDetailInfoDO::getUpdateTime);
        mpjLambdaWrapper.leftJoin(GPXContractQuotationRelDO.class,
                wrapper -> wrapper.eq(GPXContractQuotationRelDO::getPackageId, PackageDetailInfoDO::getPackageGuid)
                        .and(wrapper1 -> wrapper1.eq(GPXContractQuotationRelDO::getPlanDetailId, PackageDetailInfoDO::getPlanDetailId))
                        .eq(GPXContractQuotationRelDO::getContractId, contractGuid));
        return selectList(mpjLambdaWrapper);
    }
}
