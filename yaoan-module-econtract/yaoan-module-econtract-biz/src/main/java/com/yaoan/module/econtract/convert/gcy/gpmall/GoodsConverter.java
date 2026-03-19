package com.yaoan.module.econtract.convert.gcy.gpmall;

import com.yaoan.module.econtract.api.gcy.order.GoodsVO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.GoodsDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/14 16:29
 */
@Mapper
public interface GoodsConverter {
    GoodsConverter INSTANCE = Mappers.getMapper(GoodsConverter.class);

    GoodsDO v2D(GoodsVO bean);

    List<GoodsDO> listVO2DO(List<GoodsVO> goodsVOS);
}
