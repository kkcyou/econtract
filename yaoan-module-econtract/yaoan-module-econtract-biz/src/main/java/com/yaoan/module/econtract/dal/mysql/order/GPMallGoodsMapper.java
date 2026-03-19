package com.yaoan.module.econtract.dal.mysql.order;

import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.GoodsDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/4 11:47
 */
@Mapper
public interface GPMallGoodsMapper extends BaseMapperX<GoodsDO> {

    @Update("<script>"
            + "UPDATE ecms_gcy_goods "
            + "SET deleted = 0 "
            + "WHERE contract_id IN "
            + "<foreach item='item' index='index' collection='contractIds' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    void active(@Param("contractIds") List<String> contractIds);
}
