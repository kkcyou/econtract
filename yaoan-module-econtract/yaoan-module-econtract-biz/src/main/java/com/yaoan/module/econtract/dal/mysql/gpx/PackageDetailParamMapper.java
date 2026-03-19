package com.yaoan.module.econtract.dal.mysql.gpx;

import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.gpx.PackageDetailParamDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/29 17:36
 */
@Mapper
public interface PackageDetailParamMapper extends BaseMapperX<PackageDetailParamDO> {
    @Update("<script>"
            + "UPDATE ecms_gpx_package_detail_param "
            + "SET deleted = 0 "
            + "WHERE id IN "
            + "<foreach item='item' index='index' collection='packageDetailParamIds' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    void active(@Param("packageDetailParamIds") List<String> packageDetailParamIds);
}
