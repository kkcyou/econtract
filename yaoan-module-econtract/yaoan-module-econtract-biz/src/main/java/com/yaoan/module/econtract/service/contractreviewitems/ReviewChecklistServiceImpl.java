package com.yaoan.module.econtract.service.contractreviewitems;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.exception.util.ServiceExceptionUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewchecklist.ReviewChecklistPageReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewchecklist.ReviewChecklistRespVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewchecklist.ReviewChecklistSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.RelChecklistRuleDO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ReviewChecklistDO;
import com.yaoan.module.econtract.dal.mysql.contractreviewitems.RelChecklistRuleMapper;
import com.yaoan.module.econtract.dal.mysql.contractreviewitems.ReviewChecklistMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 审查清单 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ReviewChecklistServiceImpl implements ReviewChecklistService {

    @Resource
    private ReviewChecklistMapper reviewChecklistMapper;

    @Resource
    private RelChecklistRuleMapper relChecklistRuleMapper;

    @Override
    public String createReviewChecklist(ReviewChecklistSaveReqVO createReqVO) {
        // 插入
        ReviewChecklistDO reviewChecklist = BeanUtils.toBean(createReqVO, ReviewChecklistDO.class);
        reviewChecklistMapper.insert(reviewChecklist);

        //设置审查清单同规则关联
        List<RelChecklistRuleDO> relChecklistRuleList = new ArrayList<>();
        for (String ruleId : createReqVO.getChecklistRuleList()) {
            RelChecklistRuleDO relChecklistRuleDO = new RelChecklistRuleDO();
            relChecklistRuleDO.setReviewListId(reviewChecklist.getId());
            relChecklistRuleDO.setReviewId(ruleId);
            relChecklistRuleList.add(relChecklistRuleDO);
        }
        relChecklistRuleMapper.insertBatch(relChecklistRuleList);

        // 返回
        return reviewChecklist.getId();
    }

    @Override
    public void updateReviewChecklist(ReviewChecklistSaveReqVO updateReqVO) {
        // 校验存在
        validateReviewChecklistExists(updateReqVO.getId());
        // 更新
        ReviewChecklistDO updateObj = BeanUtils.toBean(updateReqVO, ReviewChecklistDO.class);
        reviewChecklistMapper.updateById(updateObj);
        //更新审查清单同规则关联
        relChecklistRuleMapper.delete(RelChecklistRuleDO::getReviewListId, updateReqVO.getId());
        List<RelChecklistRuleDO> relChecklistRuleList = new ArrayList<>();
        for (String ruleId : updateReqVO.getChecklistRuleList()) {
            RelChecklistRuleDO relChecklistRuleDO = new RelChecklistRuleDO();
            relChecklistRuleDO.setReviewListId(updateObj.getId());
            relChecklistRuleDO.setReviewId(ruleId);
            relChecklistRuleList.add(relChecklistRuleDO);
        }
        relChecklistRuleMapper.insertBatch(relChecklistRuleList);
    }

    @Override
    public void deleteReviewChecklist(String id) {
        // 校验存在
        validateReviewChecklistExists(id);
        // 删除
        reviewChecklistMapper.deleteById(id);
        // 删除审查清单同规则关联
        relChecklistRuleMapper.delete(RelChecklistRuleDO::getReviewListId, id);
    }

    private void validateReviewChecklistExists(String id) {
        if (reviewChecklistMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.invalidParamException("数据不存在！");
        }
    }

    @Override
    public ReviewChecklistRespVO getReviewChecklist(String id) {
        ReviewChecklistDO reviewChecklistDO = reviewChecklistMapper.selectById(id);

        ReviewChecklistRespVO respVO = BeanUtils.toBean(reviewChecklistDO, ReviewChecklistRespVO.class);
        // 查询审查规则关联表
        List<RelChecklistRuleDO> relChecklistRuleDOList = relChecklistRuleMapper.selectList(RelChecklistRuleDO::getReviewListId, id);
        List<String> itemIds = relChecklistRuleDOList.stream().map(RelChecklistRuleDO::getReviewId).collect(Collectors.toList());
        respVO.setItemIds(itemIds);
        return respVO;
    }

    @Override
    public PageResult<ReviewChecklistDO> getReviewChecklistPage(ReviewChecklistPageReqVO pageReqVO) {
        // 处理时间
        if (ObjectUtil.isNotEmpty(pageReqVO.getCreateTime())) {
            Date[] createTime = pageReqVO.getCreateTime();
            // 将结束时间设置成23:59:59
            createTime[1] = new Date(createTime[1].getTime() + 24 * 60 * 60 * 1000 - 1);
            pageReqVO.setCreateTime(createTime);
        }
        return reviewChecklistMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ReviewChecklistRespVO> getReviewChecklistList() {
        List<ReviewChecklistDO> list = reviewChecklistMapper.selectList(ReviewChecklistDO::getStatus, Boolean.TRUE);
        return BeanUtils.toBean(list, ReviewChecklistRespVO.class);
    }

    @Override
    public void updateReviewChecklistStatus(ReviewChecklistSaveReqVO updateReqVO) {

        validateReviewChecklistExists(updateReqVO.getId());

        ReviewChecklistDO reviewChecklistDO = new ReviewChecklistDO().setId(updateReqVO.getId()).setStatus(updateReqVO.getStatus());
        reviewChecklistMapper.updateById(reviewChecklistDO);
    }

}