package com.yaoan.module.econtract.dal.mysql.bpm.model;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.bpm.model.vo.ModelBpmPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.bpm.model.ModelBpmDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description:
 * @author: Pele
 * @date: 2023/9/13 19:15
 */
@Mapper
public interface ModelBpmMapper extends BaseMapperX<ModelBpmDO> {
    default PageResult<ModelBpmDO> selectPage(ModelBpmPageReqVO reqVO) {

        //todo: 提交人查询
        return selectPage(reqVO, new LambdaQueryWrapperX<ModelBpmDO>()
                .orderByDesc(ModelBpmDO::getCreateTime)
                .likeIfPresent(ModelBpmDO::getSubmitterName, reqVO.getSearchText())
                .likeIfPresent(ModelBpmDO::getModelName, reqVO.getSearchText())
                .eqIfPresent(ModelBpmDO::getResult, reqVO.getResult())
                //提交时间（就是创建时间）
                .betweenIfPresent(ModelBpmDO::getCreateTime, reqVO.getStartCreateTime(), reqVO.getEndCreateTime())
                //审批状态
                .eqIfPresent(ModelBpmDO::getResult, reqVO.getResult())
                //审批通过时间（只有审批通过状态的列表才能输入这个条件查询）
                .betweenIfPresent(ModelBpmDO::getUpdateTime, reqVO.getStartApproveSuccessTime(), reqVO.getEndApproveSuccessTime())
                //将 流程实例存在的流程DO找出
                .inIfPresent(ModelBpmDO::getProcessInstanceId, reqVO.getProcessInstanceIds()));
    }
}
