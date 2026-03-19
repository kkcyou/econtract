package com.yaoan.module.econtract.dal.mysql.performTaskType;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.performtasktype.vo.PerformTaskTypeListReqVo;
import com.yaoan.module.econtract.dal.dataobject.performtasktype.PerformTaskTypeDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/3 15:24
 */
@Mapper
public interface PerformTaskTypeMapper extends BaseMapperX<PerformTaskTypeDO> {

    default PageResult<PerformTaskTypeDO> selectPerformTaskTypePage(PerformTaskTypeListReqVo vo){
        return selectPage(vo,new LambdaQueryWrapperX<PerformTaskTypeDO>()
                .betweenIfPresent(PerformTaskTypeDO::getCreateTime,vo.getCreateTimeStart(),vo.getCreateTimeEnd())
                .like(PerformTaskTypeDO::getName,vo.getSearchText())
                .or()
                .like(PerformTaskTypeDO::getCreator,vo.getSearchText())
        );
    }
    default  List<PerformTaskTypeDO> selectListByIds( List<String> perfTaskTypeIds){
        return  selectList(new LambdaQueryWrapperX<PerformTaskTypeDO>().inIfPresent(PerformTaskTypeDO::getId, perfTaskTypeIds).select(PerformTaskTypeDO::getId,PerformTaskTypeDO::getName));
    }

    default  PerformTaskTypeDO selectById(String id){
        return  selectOne(new LambdaQueryWrapperX<PerformTaskTypeDO>().eqIfPresent(PerformTaskTypeDO::getId, id).select(PerformTaskTypeDO::getId,PerformTaskTypeDO::getName));
    }

}
