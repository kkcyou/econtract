package com.yaoan.module.econtract.dal.mysql.businesstype;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.businesstype.vo.BusinessTypePageReqVO;
import com.yaoan.module.econtract.dal.dataobject.businesstype.BusinessTypeDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 业务类型 Mapper
 *
 * @author lls
 */
@Mapper
public interface BusinessTypeMapper extends BaseMapperX<BusinessTypeDO> {

    default PageResult<BusinessTypeDO> selectPage(BusinessTypePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BusinessTypeDO>()
                .eqIfPresent(BusinessTypeDO::getCode, reqVO.getCode())
                .likeIfPresent(BusinessTypeDO::getName, reqVO.getName())
                .likeIfPresent(BusinessTypeDO::getTableName, reqVO.getTableName())
                .orderByDesc(BusinessTypeDO::getId));
    }

}