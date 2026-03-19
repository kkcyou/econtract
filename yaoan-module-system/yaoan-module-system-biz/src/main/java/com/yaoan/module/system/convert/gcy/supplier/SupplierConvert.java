package com.yaoan.module.system.convert.gcy.supplier;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.system.controller.admin.gcy.supplier.vo.SupplierInfoVo;
import com.yaoan.module.system.dal.dataobject.user.SupplyDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @description:
 * @author: doujl
 * @date: 2023/12/12 16:41
 */
@Mapper
public interface SupplierConvert {

    SupplierConvert INSTANCE = Mappers.getMapper(SupplierConvert.class);

    SupplierInfoVo convert2Vo(SupplyDO supplyDO);

    PageResult<SupplierInfoVo> convertPage(PageResult<SupplyDO> page);
}
