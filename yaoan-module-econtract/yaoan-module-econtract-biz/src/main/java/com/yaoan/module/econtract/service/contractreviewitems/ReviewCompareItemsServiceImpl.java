package com.yaoan.module.econtract.service.contractreviewitems;

import com.yaoan.framework.common.exception.util.ServiceExceptionUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewcompareitems.ReviewCompareItemsPageReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.reviewcompareitems.ReviewCompareItemsSaveReqVO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ReviewCompareItemsDO;
import com.yaoan.module.econtract.dal.mysql.contractreviewitems.ReviewCompareItemsMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;


/**
 * 审查比对检测项 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ReviewCompareItemsServiceImpl implements ReviewCompareItemsService {

    @Resource
    private ReviewCompareItemsMapper reviewCompareItemsMapper;

    @Override
    public String createReviewCompareItems(ReviewCompareItemsSaveReqVO createReqVO) {
        // 插入
        ReviewCompareItemsDO reviewCompareItems = BeanUtils.toBean(createReqVO, ReviewCompareItemsDO.class);
        reviewCompareItemsMapper.insert(reviewCompareItems);
        // 返回
        return reviewCompareItems.getId();
    }

    @Override
    public void updateReviewCompareItems(ReviewCompareItemsSaveReqVO updateReqVO) {
        // 校验存在
        validateReviewCompareItemsExists(updateReqVO.getId());
        // 更新
        ReviewCompareItemsDO updateObj = BeanUtils.toBean(updateReqVO, ReviewCompareItemsDO.class);
        reviewCompareItemsMapper.updateById(updateObj);
    }

    @Override
    public void deleteReviewCompareItems(String id) {
        // 校验存在
        validateReviewCompareItemsExists(id);
        // 删除
        reviewCompareItemsMapper.deleteById(id);
    }

    private void validateReviewCompareItemsExists(String id) {
        if (reviewCompareItemsMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.invalidParamException("数据不存在！");
        }
    }

    @Override
    public ReviewCompareItemsDO getReviewCompareItems(String id) {
        return reviewCompareItemsMapper.selectById(id);
    }

    @Override
    public PageResult<ReviewCompareItemsDO> getReviewCompareItemsPage(ReviewCompareItemsPageReqVO pageReqVO) {
        return reviewCompareItemsMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ReviewCompareItemsDO> getAllReviewCompareItems() {
        return reviewCompareItemsMapper.selectList();
    }

}