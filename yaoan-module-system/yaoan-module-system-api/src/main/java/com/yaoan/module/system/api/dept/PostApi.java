package com.yaoan.module.system.api.dept;

import com.yaoan.module.system.api.dept.dto.PostDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 岗位 API 接口
 *
 * @author 芋道源码
 */
public interface PostApi {

    /**
     * 校验岗位们是否有效。如下情况，视为无效：
     * 1. 岗位编号不存在
     * 2. 岗位被禁用
     *
     * @param ids 岗位编号数组
     */
    void validPostList(Collection<Long> ids);

    /**
     * 根据岗位ids获取岗位信息
     */
    List<PostDTO> getPostList(Collection<Long> ids);

    /**
     * 根据用户id获得岗位信息
     * <userId,岗位信息></>
     */
    List<PostDTO> getPostByUserIds(List<Long> userIds);

    Map<Long, List<PostDTO>>  getUserPostRelMap(List<Long> userIds);
}
