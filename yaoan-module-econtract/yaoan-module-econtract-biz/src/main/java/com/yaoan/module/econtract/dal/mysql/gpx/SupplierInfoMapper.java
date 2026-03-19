package com.yaoan.module.econtract.dal.mysql.gpx;

import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.gpx.SupplierInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/21 20:00
 */
@Mapper
public interface SupplierInfoMapper extends BaseMapperX<SupplierInfoDO> {
    @Update("<script>"
            + "UPDATE ecms_gpx_supplier "
            + "SET deleted = 0 "
            + "WHERE id IN "
            + "<foreach item='item' index='index' collection='supplierIdList' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    void active(@Param("supplierIdList") List<String> supplierIdList);
}
