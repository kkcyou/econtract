package com.yaoan.module.econtract.dal.mysql.review;

import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.dal.dataobject.review.ReviewConfigLogDO;
import com.yaoan.module.econtract.dal.dataobject.review.ReviewContractDO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ReviewConfigLogMapper extends BaseMapperX<ReviewConfigLogDO> {
}
