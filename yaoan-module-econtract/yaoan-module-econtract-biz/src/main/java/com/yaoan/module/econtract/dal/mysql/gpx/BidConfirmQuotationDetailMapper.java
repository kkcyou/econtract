package com.yaoan.module.econtract.dal.mysql.gpx;

import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.gpx.BidConfirmQuotationDetailDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.PackageDetailInfoDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.PackageInfoDO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/29 17:35
 */
@Mapper
public interface BidConfirmQuotationDetailMapper extends BaseMapperX<BidConfirmQuotationDetailDO> {
    @Update("<script>"
            + "UPDATE ecms_gpx_bid_confirm_quotation_detail "
            + "SET deleted = 0 "
            + "WHERE id IN "
            + "<foreach item='item' index='index' collection='saveIds' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    void active(@Param("saveIds") List<String> saveIds);

    default List<BidConfirmQuotationDetailDO> selectList4Union(String packageId) {
        MPJLambdaWrapper<BidConfirmQuotationDetailDO> mpjLambdaWrapper = new MPJLambdaWrapper<BidConfirmQuotationDetailDO>()
                .selectAll(BidConfirmQuotationDetailDO.class).orderByDesc(BidConfirmQuotationDetailDO::getUpdateTime);

        if (StringUtils.isNotBlank(packageId)) {
            mpjLambdaWrapper.leftJoin(PackageDetailInfoDO.class, PackageDetailInfoDO::getDetailId, BidConfirmQuotationDetailDO::getPackageDetailId)
                    .leftJoin(PackageInfoDO.class, PackageInfoDO::getPackageGuid, PackageDetailInfoDO::getPackageGuid)
                    .eq(PackageInfoDO::getPackageGuid, packageId)
            ;
        }

        return selectList(mpjLambdaWrapper);
    }
}
