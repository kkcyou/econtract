package com.yaoan.module.system.dal.mysql.user;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.system.controller.admin.gcy.supplier.vo.SupplierPageReqVo;
import com.yaoan.module.system.dal.dataobject.user.SupplyDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SupplyMapper extends BaseMapperX<SupplyDO> {

    default PageResult<SupplyDO> selectPage(SupplierPageReqVo reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SupplyDO>()
                .likeIfPresent(SupplyDO::getSupplyCn, reqVO.getName()));
    }

    default List<SupplyDO> getSupplyByIdsAndName(List<String> supplierIds, String supplierName) {
        return selectList(new LambdaQueryWrapperX<SupplyDO>().inIfPresent(SupplyDO::getId, supplierIds)
                .likeIfPresent(SupplyDO::getSupplyCn, supplierName));

    }
}
