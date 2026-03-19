package com.yaoan.module.system.api.dept;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.system.api.dept.dto.PostDTO;
import com.yaoan.module.system.convert.dept.PostConvert;
import com.yaoan.module.system.dal.dataobject.dept.PostDO;
import com.yaoan.module.system.dal.dataobject.dept.UserPostDO;
import com.yaoan.module.system.dal.mysql.dept.UserPostMapper;
import com.yaoan.module.system.service.dept.PostService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 岗位 API 实现类
 *
 * @author 芋道源码
 */
@Service
public class PostApiImpl implements PostApi {

    @Resource
    private PostService postService;
    @Resource
    private UserPostMapper userPostMapper;

    @Override
    public void validPostList(Collection<Long> ids) {
        postService.validatePostList(ids);
    }

    @Override
    public List<PostDTO> getPostList(Collection<Long> ids) {
        List<PostDTO> dtos = PostConvert.INSTANCE.toDTOS(postService.getPostList(ids));
        return dtos;


    }

    @Override
    public List<PostDTO> getPostByUserIds(List<Long> userIds) {
        List<PostDO> list = postService.getPostByUserIds(userIds);
        return PostConvert.INSTANCE.listEntity2DTO(list);
    }

    /**
     * Map<用户，List<岗位>>
     */
    @Override
    public Map<Long, List<PostDTO>> getUserPostRelMap(List<Long> userIds) {
        Map<Long, List<PostDTO>> result = new HashMap<Long, List<PostDTO>>();
        List<PostDO> list = postService.getPostByUserIds(userIds);
        Map<Long, PostDO> postDOMap = new HashMap<Long, PostDO>();
        if (CollectionUtil.isNotEmpty(list)) {
            postDOMap = CollectionUtils.convertMap(list, PostDO::getId);
        }
        List<UserPostDO> userPostDOList = userPostMapper.selectList(new LambdaQueryWrapperX<UserPostDO>().inIfPresent(UserPostDO::getUserId));
        if (CollectionUtil.isEmpty(userPostDOList)) {
            return Collections.emptyMap();
        }
        for (Long userId : userIds) {
            List<PostDTO> postDTOList = new ArrayList<PostDTO>();
            List<UserPostDO> userPostRel = userPostDOList.stream()
                    .filter(userPost -> Objects.equals(userPost.getUserId(), userId))
                    .collect(Collectors.toList());
            for (UserPostDO userPostDO : userPostRel) {
                PostDTO postDTO = new PostDTO();
                postDTO.setId(userPostDO.getId());
                PostDO postDO = postDOMap.get(userPostDO.getPostId());
                if (ObjectUtil.isNotNull(postDO)) {
                    postDTO.setName(postDO.getName());
                    postDTO.setCode(postDO.getCode());
                }
                postDTOList.add(postDTO);
            }
            result.put(userId, postDTOList);
        }
        return result;
    }

}
