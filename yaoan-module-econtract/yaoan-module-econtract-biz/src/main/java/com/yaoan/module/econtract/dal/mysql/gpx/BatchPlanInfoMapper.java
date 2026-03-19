package com.yaoan.module.econtract.dal.mysql.gpx;

import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.dal.dataobject.gpx.BatchPlanInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/5/29 17:34
 */
@Mapper
public interface BatchPlanInfoMapper extends BaseMapperX<BatchPlanInfoDO> {

    /**
     * 联合采购项目的相关计划
     * 线上数据，分包id是有的
     */
    default List<BatchPlanInfoDO> unionProjectPlanInfoList(List<String> projectIds) {
        return selectList(new LambdaQueryWrapperX<BatchPlanInfoDO>()
                .inIfPresent(BatchPlanInfoDO::getProjectId, projectIds)
//                .isNull(BatchPlanInfoDO::getPackageId)
        );
    }

    /**
     * 批量集中采购项目的相关计划
     * 分包id不为空
     */
    default List<BatchPlanInfoDO> batchProjectPlanInfoList(List<String> packageIds) {
        return selectList(new LambdaQueryWrapperX<BatchPlanInfoDO>()
                .inIfPresent(BatchPlanInfoDO::getPackageId, packageIds)
                .isNotNull(BatchPlanInfoDO::getPackageId)
        );
    }


    @Update("<script>"
            + "UPDATE ecms_gpx_batch_plan "
            + "SET deleted = 0 "
            + "WHERE id IN "
            + "<foreach item='item' index='index' collection='saveIds' open='(' separator=',' close=')'>"
            + "#{item}"
            + "</foreach>"
            + "</script>")
    void activate(@Param("saveIds") List<String> saveIds);
}
