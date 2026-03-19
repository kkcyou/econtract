package com.yaoan.module.econtract.dal.mysql.review;

import cn.hutool.core.util.ObjectUtil;
import com.github.yulichang.query.MPJQueryWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.module.econtract.controller.admin.review.vo.ReviewPageReqVO;
import com.yaoan.module.econtract.dal.dataobject.review.ReviewListDO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface ReviewMapper extends BaseMapperX<ReviewListDO> {

    default PageResult<ReviewListDO> getReviewList(ReviewPageReqVO vo){
        MPJQueryWrapper<ReviewListDO> mpjQueryWrapper = new MPJQueryWrapper<ReviewListDO>()
                .select("id","name ","creator","updater","update_time","create_time")
                .orderByDesc("create_time");
        if((ObjectUtil.isNotEmpty(vo.getName()))){
            mpjQueryWrapper.like("name", vo.getName());
        }

        return selectPage(vo, mpjQueryWrapper);
    }

}
