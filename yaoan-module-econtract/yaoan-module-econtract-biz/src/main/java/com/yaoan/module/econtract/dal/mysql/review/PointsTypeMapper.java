package com.yaoan.module.econtract.dal.mysql.review;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.review.vo.ReviewPointsTypeReqVO;
import com.yaoan.module.econtract.dal.dataobject.review.PointsTypeDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PointsTypeMapper extends BaseMapperX<PointsTypeDO> {

    default List<PointsTypeDO> getPointsOneList(ReviewPointsTypeReqVO reviewPointsTypeReqVO){

        return selectList(new LambdaQueryWrapperX<PointsTypeDO>()
                .orderByDesc(PointsTypeDO::getCreateTime));
    }

    default List<PointsTypeDO> selectListByParentId(PointsTypeDO typeDO){
        return selectList(new LambdaQueryWrapperX<PointsTypeDO>()
                .eq(PointsTypeDO::getId,typeDO.getParentId()).or()
                .eq(PointsTypeDO::getParentId,typeDO.getParentId())
                .orderByDesc(PointsTypeDO::getCreateTime));
    };

    default List<PointsTypeDO> selectListById(PointsTypeDO typeDO){
        return selectList(new LambdaQueryWrapperX<PointsTypeDO>()
                .eq(PointsTypeDO::getId,typeDO.getId()).or()
                .eq(PointsTypeDO::getParentId,typeDO.getId())
                .orderByDesc(PointsTypeDO::getCreateTime));
    }

    default PointsTypeDO selectByName(String name){
        //通过名称模糊查询
        return selectOne(new LambdaQueryWrapperX<PointsTypeDO>()
                .eq(PointsTypeDO::getName,name));
    }
}
