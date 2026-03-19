package com.yaoan.module.econtract.service.reviewresult;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.module.econtract.enums.contractreviewitems.RiskLevelEnums;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.yaoan.module.econtract.controller.admin.reviewresult.vo.*;
import com.yaoan.module.econtract.dal.dataobject.reviewresult.ReviewResultDO;
import com.yaoan.framework.common.pojo.PageResult;

import com.yaoan.module.econtract.convert.reviewresult.ReviewResultConvert;
import com.yaoan.module.econtract.dal.mysql.reviewresult.ReviewResultMapper;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.*;

/**
 * 智能审查结果 Service 实现类
 *
 * @author admin
 */
@Service
@Validated
public class ReviewResultServiceImpl implements ReviewResultService {

    @Resource
    private ReviewResultMapper reviewResultMapper;

    @Override
    public String createReviewResult(ReviewResultCreateReqVO createReqVO) {
        // 插入
        ReviewResultDO reviewResult = ReviewResultConvert.INSTANCE.convert(createReqVO);
        reviewResultMapper.insert(reviewResult);
        // 返回
        return reviewResult.getId();
    }

    @Override
    public void updateReviewResult(ReviewResultUpdateReqVO updateReqVO) {
        // 校验存在
        validateReviewResultExists(updateReqVO.getId());
        // 更新
        ReviewResultDO updateObj = ReviewResultConvert.INSTANCE.convert(updateReqVO);
        reviewResultMapper.updateById(updateObj);
    }

    @Override
    public void deleteReviewResult(String id) {
        // 校验存在
        validateReviewResultExists(id);
        // 删除
        reviewResultMapper.deleteById(id);
    }

    private void validateReviewResultExists(String id) {
        if (reviewResultMapper.selectById(id) == null) {
            throw exception(DATA_ERROR);
        }
    }

    @Override
    public ReviewResultDO getReviewResult(String id) {
        return reviewResultMapper.selectById(id);
    }

    @Override
    public List<ReviewResultDO> getReviewResultList(Collection<String> ids) {
        return reviewResultMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<ReviewResultRespVO> getReviewResultPage(ReviewResultPageReqVO pageReqVO) {
        PageResult<ReviewResultDO> doPageResult = reviewResultMapper.selectPage(pageReqVO);
        PageResult<ReviewResultRespVO> result = ReviewResultConvert.INSTANCE.convertPage(doPageResult);
        return enhancePage(result);
    }

    private PageResult<ReviewResultRespVO> enhancePage(PageResult<ReviewResultRespVO> resultPage) {
        if(CollectionUtil.isEmpty(resultPage.getList())) {return resultPage;}
        for (ReviewResultRespVO respVO : resultPage.getList()) {
            RiskLevelEnums levelEnums = RiskLevelEnums.getInstance(respVO.getRiskLevel());
            if(ObjectUtil.isNotNull(levelEnums)) {
                respVO.setRiskLevelName(levelEnums.getInfo());
            }
        }
        return resultPage;
    }

    @Override
    public List<ReviewResultDO> getReviewResultList(ReviewResultExportReqVO exportReqVO) {
        return reviewResultMapper.selectList(exportReqVO);
    }

}
