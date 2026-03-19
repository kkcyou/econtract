package com.yaoan.module.system.dal.mysql.dept;

import cn.hutool.core.collection.CollectionUtil;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.mapper.BaseMapperX;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.system.controller.admin.dept.vo.post.PostExportReqVO;
import com.yaoan.module.system.controller.admin.dept.vo.post.PostPageReqVO;
import com.yaoan.module.system.dal.dataobject.dept.PostDO;
import com.yaoan.module.system.dal.dataobject.dept.UserPostDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface PostMapper extends BaseMapperX<PostDO> {

    default List<PostDO> selectList(Collection<Long> ids, Collection<Integer> statuses) {
        return selectList(new LambdaQueryWrapperX<PostDO>()
                .inIfPresent(PostDO::getId, ids)
                .inIfPresent(PostDO::getStatus, statuses));
    }

    default PageResult<PostDO> selectPage(PostPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PostDO>()
                .likeIfPresent(PostDO::getCode, reqVO.getCode())
                .likeIfPresent(PostDO::getName, reqVO.getName())
                .eqIfPresent(PostDO::getStatus, reqVO.getStatus())
                .orderByDesc(PostDO::getId));
    }

    default List<PostDO> selectList(PostExportReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<PostDO>()
                .likeIfPresent(PostDO::getCode, reqVO.getCode())
                .likeIfPresent(PostDO::getName, reqVO.getName())
                .eqIfPresent(PostDO::getStatus, reqVO.getStatus()));
    }

    default PostDO selectByName(String name,Long companyId) {
        return selectOne(PostDO::getName, name,PostDO::getCompanyId,companyId);
    }

    default PostDO selectByCode(String code,Long companyId) {
        return selectOne(PostDO::getCode, code, PostDO::getCompanyId,companyId);
    }

    /**
     * 找到用户id相关的岗位
     */
    default List<PostDO> getPostByUserIds(List<Long> userIds) {
        MPJLambdaWrapper<PostDO> mpjLambdaWrapper = new MPJLambdaWrapper<PostDO>()
                .selectAll(PostDO.class).orderByDesc(PostDO::getCreateTime);
        if (CollectionUtil.isNotEmpty(userIds)) {
            mpjLambdaWrapper.leftJoin(UserPostDO.class, UserPostDO::getPostId, PostDO::getId)
                    .in(UserPostDO::getUserId, userIds);
        }
        return selectList(mpjLambdaWrapper);
    }
}
