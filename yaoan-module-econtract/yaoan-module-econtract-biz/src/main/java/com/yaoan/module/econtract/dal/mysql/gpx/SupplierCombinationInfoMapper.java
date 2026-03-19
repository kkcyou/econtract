package com.yaoan.module.econtract.dal.mysql.gpx;

import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.gpx.SupplierCombinationInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/29 17:38
 */
@Mapper
public interface SupplierCombinationInfoMapper extends BaseMapperX<SupplierCombinationInfoDO> {
    @Update("<script>"
            + "UPDATE ecms_gpx_supplier_combination "
            + "SET deleted = 0 "
            + "WHERE id IN "
            + "<foreach item='item' index='index' collection='saveIds' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    void active(@Param("saveIds") List<String> saveIds);
}
