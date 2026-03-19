package com.yaoan.module.econtract.dal.mysql.gcy.buyplan;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.supervise.vo.QueryBuyPlanBilReplVO;
import com.yaoan.module.econtract.dal.dataobject.gcy.buyplan.EcmsGcyBuyPlanBill;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 计划采购清单明细表 Mapper 接口
 * </p>
 *
 * @author doujiale
 * @since 2024-03-18
 */
@Mapper
public interface EcmsGcyBuyPlanBillMapper extends BaseMapperX<EcmsGcyBuyPlanBill> {

    default PageResult<EcmsGcyBuyPlanBill> selectPage(QueryBuyPlanBilReplVO reqVO) {
        LambdaQueryWrapperX<EcmsGcyBuyPlanBill> queryWrapperX = new LambdaQueryWrapperX<EcmsGcyBuyPlanBill>();
        if(StringUtils.isNotEmpty(reqVO.getSearchText())){
            queryWrapperX.and(wrapper -> wrapper
                    .or().like(EcmsGcyBuyPlanBill::getGoodsName,reqVO.getSearchText())
                    .or().like(EcmsGcyBuyPlanBill::getPurCatalogName,reqVO.getSearchText())
            );
        }
        queryWrapperX.eqIfPresent(EcmsGcyBuyPlanBill::getBuyPlanPackageGuid, reqVO.getBidGuid());
        return selectPage(reqVO, queryWrapperX);
    }


}
