package com.yaoan.module.econtract.service.reviewitembasis;


import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.reviewitembasis.vo.ReviewItemBasisCreateReqVO;
import com.yaoan.module.econtract.controller.admin.reviewitembasis.vo.ReviewItemBasisExportReqVO;
import com.yaoan.module.econtract.controller.admin.reviewitembasis.vo.ReviewItemBasisPageReqVO;
import com.yaoan.module.econtract.controller.admin.reviewitembasis.vo.ReviewItemBasisUpdateReqVO;
import com.yaoan.module.econtract.convert.reviewitembasis.ReviewItemBasisConvert;
import com.yaoan.module.econtract.dal.dataobject.reviewitembasis.ReviewItemBasisDO;
import com.yaoan.module.econtract.dal.mysql.reviewitembasis.ReviewItemBasisMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;


/**
 * 合同审查规则依据 Service 实现类
 *
 * @author admin
 */
@Service
@Validated
public class ReviewItemBasisServiceImpl implements ReviewItemBasisService {

    @Resource
    private ReviewItemBasisMapper reviewItemBasisMapper;

    @Override
    public String createReviewItemBasis(ReviewItemBasisCreateReqVO createReqVO) {
        // 插入
        ReviewItemBasisDO reviewItemBasis = ReviewItemBasisConvert.INSTANCE.convert(createReqVO);
        reviewItemBasisMapper.insert(reviewItemBasis);
        // 返回
        return reviewItemBasis.getId();
    }

    @Override
    public void updateReviewItemBasis(ReviewItemBasisUpdateReqVO updateReqVO) {
        // 校验存在
        validateReviewItemBasisExists(updateReqVO.getId());
        // 更新
        ReviewItemBasisDO updateObj = ReviewItemBasisConvert.INSTANCE.convert(updateReqVO);
        reviewItemBasisMapper.updateById(updateObj);
    }

    @Override
    public void deleteReviewItemBasis(String id) {
        // 校验存在
        validateReviewItemBasisExists(id);
        // 删除
        reviewItemBasisMapper.deleteById(id);
    }

    private void validateReviewItemBasisExists(String id) {
        if (reviewItemBasisMapper.selectById(id) == null) {
            throw exception(ErrorCodeConstants.SYSTEM_ERROR,"合同审查规则依据不存在");
        }
    }

    @Override
    public ReviewItemBasisDO getReviewItemBasis(String id) {
        return reviewItemBasisMapper.selectById(id);
    }

    @Override
    public List<ReviewItemBasisDO> getReviewItemBasisList(Collection<String> ids) {
        return reviewItemBasisMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ReviewItemBasisDO> getReviewItemBasisPage(ReviewItemBasisPageReqVO pageReqVO) {
        return reviewItemBasisMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ReviewItemBasisDO> getReviewItemBasisList(ReviewItemBasisExportReqVO exportReqVO) {
        return reviewItemBasisMapper.selectList(exportReqVO);
    }

}
