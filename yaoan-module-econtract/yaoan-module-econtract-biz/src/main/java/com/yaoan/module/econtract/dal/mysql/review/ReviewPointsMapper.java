package com.yaoan.module.econtract.dal.mysql.review;

import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.query.MPJQueryWrapper;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.controller.admin.review.vo.ReviewReqVO;
import com.yaoan.module.econtract.dal.dataobject.review.ReviewListDO;
import com.yaoan.module.econtract.dal.dataobject.review.ReviewPointsDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewPointsMapper extends BaseMapperX<ReviewPointsDO> {
}
