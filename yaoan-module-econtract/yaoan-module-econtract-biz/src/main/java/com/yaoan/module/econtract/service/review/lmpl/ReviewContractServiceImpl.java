package com.yaoan.module.econtract.service.review.lmpl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.review.vo.*;
import com.yaoan.module.econtract.convert.review.ReviewConverter;
import com.yaoan.module.econtract.dal.dataobject.review.ReviewConfigLogDO;
import com.yaoan.module.econtract.dal.dataobject.review.ReviewContractDO;
import com.yaoan.module.econtract.dal.dataobject.review.ReviewListDO;
import com.yaoan.module.econtract.dal.mysql.review.ReviewConfigLogMapper;
import com.yaoan.module.econtract.dal.mysql.review.ReviewContractMapper;
import com.yaoan.module.econtract.dal.mysql.review.ReviewMapper;
import com.yaoan.module.econtract.service.review.ReviewContractService;

import com.yaoan.module.econtract.util.ParamUtil;
import com.yaoan.module.system.api.permission.RoleApi;
import com.yaoan.module.system.api.permission.dto.RoleRespDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class ReviewContractServiceImpl implements ReviewContractService {

    @Resource
    private ReviewContractMapper reviewContractMapper;

    @Resource
    private ReviewMapper reviewMapper;

    @Resource
    private ReviewConfigLogMapper reviewConfigLogMapper;

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private RoleApi roleApi;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ReviewContractCreateReqVO reviewContractCreateReqVO) {
        //查询审查清单和合同类型中间表，如果没有数据说明是新增，如果有数据说明是修改
        QueryWrapper<ReviewContractDO> wrapper = new QueryWrapper<>();
        wrapper.eq("type_id", reviewContractCreateReqVO.getTypeId());
        ReviewContractDO contractDO = reviewContractMapper.selectOne(wrapper);
        if (!(contractDO.getReviewId() == null)) {
            //旧名称
            ReviewListDO reviewListDO0 = reviewMapper.selectById(contractDO.getReviewId());
            //新名称
            ReviewListDO reviewListDO1 = reviewMapper.selectById(reviewContractCreateReqVO.getReviewId());
            //获取用户
            AdminUserRespDTO user = adminUserApi.getUser(SecurityFrameworkUtils.getLoginUserId());
            //获取角色
            RoleRespDTO roles = roleApi.getRole(user.getId());
            System.out.println("角色："+roles);
            //新增配置记录
            ReviewConfigLogDO reviewConfigLogDO = new ReviewConfigLogDO();
            reviewConfigLogDO.setTypeId(reviewContractCreateReqVO.getTypeId());
            reviewConfigLogDO.setName(user.getNickname());
            reviewConfigLogDO.setRole( roles.getName());
            reviewConfigLogDO.setReviewName0(reviewListDO0.getName());
            reviewConfigLogDO.setReviewName1(reviewListDO1.getName());
            reviewConfigLogMapper.insert(reviewConfigLogDO);
            //修改
            UpdateWrapper<ReviewContractDO> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("type_id", reviewContractCreateReqVO.getTypeId());
            ReviewContractDO reviewContractDO = ReviewConverter.INSTANCE.reviewContractCreateToEntity(reviewContractCreateReqVO);

            reviewContractMapper.update(reviewContractDO, updateWrapper);
        } else {
            ReviewContractDO reviewContractDO = ReviewConverter.INSTANCE.reviewContractCreateToEntity(reviewContractCreateReqVO);
            reviewContractMapper.insert(reviewContractDO);
        }
    }

    @Override
    public ReviewContractRespVO selectByTypeId(ReviewContractReqVO reqVO) {
        //查询关联审查清单id
        QueryWrapper<ReviewContractDO> wrapper = new QueryWrapper<>();
        wrapper.eq("type_id", reqVO.getTypeId());
        ReviewContractDO reviewContractDO = reviewContractMapper.selectOne(wrapper);
        //查询审查清单名称
        ReviewListDO reviewListDO = reviewMapper.selectById(reviewContractDO.getReviewId());
        ReviewContractRespVO reviewContractRespVO = ReviewConverter.INSTANCE.reviewContractToEntity(reviewContractDO);
        reviewContractRespVO.setName(reviewListDO.getName());
        return reviewContractRespVO;
    }

    @Override
    public ReviewConfigLogRespVO getConfigLog(ReviewConfigLogReqVO reqVO) {
        //根据合同类型查询配置记录
        QueryWrapper<ReviewConfigLogDO> wrapper = new QueryWrapper<>();
        wrapper.eq("type_id", reqVO.getTypeId());
        ReviewConfigLogDO reviewConfigLogDO = reviewConfigLogMapper.selectOne(wrapper);
        return ReviewConverter.INSTANCE.reviewConfigLogToEntity(reviewConfigLogDO);
    }

}
