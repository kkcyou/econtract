package com.yaoan.module.system.service.dept;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.enums.CommonStatusEnum;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.system.api.dept.dto.PostDTO;
import com.yaoan.module.system.controller.admin.dept.vo.post.PostCreateReqVO;
import com.yaoan.module.system.controller.admin.dept.vo.post.PostExportReqVO;
import com.yaoan.module.system.controller.admin.dept.vo.post.PostPageReqVO;
import com.yaoan.module.system.controller.admin.dept.vo.post.PostUpdateReqVO;
import com.yaoan.module.system.convert.dept.PostConvert;
import com.yaoan.module.system.dal.dataobject.dept.PostDO;
import com.yaoan.module.system.dal.dataobject.dept.UserPostDO;
import com.yaoan.module.system.dal.mysql.dept.PostMapper;
import com.yaoan.module.system.dal.mysql.dept.UserPostMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.common.util.collection.CollectionUtils.convertMap;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUser;
import static com.yaoan.module.system.enums.ErrorCodeConstants.*;

/**
 * 岗位 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class PostServiceImpl implements PostService {

    @Resource
    private PostMapper postMapper;
    @Resource
    private UserPostMapper userPostMapper;

    @Override
    public Long createPost(PostCreateReqVO reqVO) {
        // 校验正确性
        validatePostForCreateOrUpdate(null, reqVO.getName(), reqVO.getCode(),getLoginUser().getCompanyId());
        //添加所属公司
        Long companyId = reqVO.getCompanyId() != null && reqVO.getCompanyId() > 0 ? reqVO.getCompanyId() : getLoginUser().getCompanyId();
        reqVO.setCompanyId(companyId);
        // 插入岗位
        PostDO post = PostConvert.INSTANCE.convert(reqVO);
        postMapper.insert(post);
        return post.getId();
    }

    @Override
    public void updatePost(PostUpdateReqVO reqVO) {
        // 校验正确性
        validatePostForCreateOrUpdate(reqVO.getId(), reqVO.getName(), reqVO.getCode(),getLoginUser().getCompanyId());

        // 更新岗位
        PostDO updateObj = PostConvert.INSTANCE.convert(reqVO);
        postMapper.updateById(updateObj);
    }

    @Override
    public void deletePost(Long id) {
        // 校验是否存在
        validatePostExists(id);
        // 删除部门
        postMapper.deleteById(id);
    }

    private void validatePostForCreateOrUpdate(Long id, String name, String code,Long companyId) {
        // 校验自己存在
        validatePostExists(id);
        // 校验岗位名的唯一性
        validatePostNameUnique(id, name, companyId);
        // 校验岗位编码的唯一性
        validatePostCodeUnique(id, code,companyId);
    }

    private void validatePostNameUnique(Long id, String name,Long companyId) {
        PostDO post = postMapper.selectByName(name,companyId);
        if (post == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的岗位
        if (id == null) {
            throw exception(POST_NAME_DUPLICATE);
        }
        if (!post.getId().equals(id)) {
            throw exception(POST_NAME_DUPLICATE);
        }
    }

    private void validatePostCodeUnique(Long id, String code,Long companyId) {
        PostDO post = postMapper.selectByCode(code,companyId);
        if (post == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的岗位
        if (id == null) {
            throw exception(POST_CODE_DUPLICATE);
        }
        if (!post.getId().equals(id)) {
            throw exception(POST_CODE_DUPLICATE);
        }
    }

    private void validatePostExists(Long id) {
        if (id == null) {
            return;
        }
        if (postMapper.selectById(id) == null) {
            throw exception(POST_NOT_FOUND);
        }
    }

    @Override
    public List<PostDO> getPostList(Collection<Long> ids, Collection<Integer> statuses) {
        return postMapper.selectList(ids, statuses);
    }

    @Override
    public PageResult<PostDO> getPostPage(PostPageReqVO reqVO) {
        return postMapper.selectPage(reqVO);
    }

    @Override
    public List<PostDO> getPostList(PostExportReqVO reqVO) {
        return postMapper.selectList(reqVO);
    }

    @Override
    public PostDO getPost(Long id) {
        return postMapper.selectById(id);
    }

    @Override
    public void validatePostList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得岗位信息
        List<PostDO> posts = postMapper.selectBatchIds(ids);
        Map<Long, PostDO> postMap = convertMap(posts, PostDO::getId);
        // 校验
        ids.forEach(id -> {
            PostDO post = postMap.get(id);
            if (post == null) {
                throw exception(POST_NOT_FOUND);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(post.getStatus())) {
                throw exception(POST_NOT_ENABLE, post.getName());
            }
        });
    }

    @Override
    public List<PostDO> getPostByUserIds(List<Long> userIds) {
        return postMapper.getPostByUserIds(userIds);
    }


}
