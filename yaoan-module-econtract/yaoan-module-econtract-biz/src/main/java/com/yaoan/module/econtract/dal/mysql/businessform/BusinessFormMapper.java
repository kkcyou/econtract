package com.yaoan.module.econtract.dal.mysql.businessform;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.businessform.vo.BusinessFormPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.businessform.BusinessFormDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 业务表单 Mapper
 *
 * @author lls
 */
@Mapper
public interface BusinessFormMapper extends BaseMapperX<BusinessFormDO> {

    default PageResult<BusinessFormDO> selectPage(BusinessFormPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BusinessFormDO>()
                .eqIfPresent(BusinessFormDO::getCode, reqVO.getCode())
                .likeIfPresent(BusinessFormDO::getName, reqVO.getName())
                .eqIfPresent(BusinessFormDO::getBusinessId, reqVO.getBusinessId())
                .betweenIfPresent(BusinessFormDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BusinessFormDO::getId));
    }

}