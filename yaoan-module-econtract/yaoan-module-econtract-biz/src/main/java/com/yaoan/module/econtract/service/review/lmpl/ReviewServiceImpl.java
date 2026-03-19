package com.yaoan.module.econtract.service.review.lmpl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.review.vo.*;
import com.yaoan.module.econtract.convert.review.ReviewConverter;
import com.yaoan.module.econtract.dal.dataobject.review.ReviewContractDO;
import com.yaoan.module.econtract.dal.dataobject.review.PointsDataDO;
import com.yaoan.module.econtract.dal.dataobject.review.ReviewListDO;
import com.yaoan.module.econtract.dal.dataobject.review.ReviewPointsDO;
import com.yaoan.module.econtract.dal.mysql.review.ReviewContractMapper;
import com.yaoan.module.econtract.dal.mysql.review.PointsDataMapper;
import com.yaoan.module.econtract.dal.mysql.review.ReviewMapper;
import com.yaoan.module.econtract.dal.mysql.review.ReviewPointsMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.service.review.ReviewService;
import com.yaoan.module.econtract.util.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;


@Service
@Slf4j
public class ReviewServiceImpl implements ReviewService {

    @Resource
    private ReviewMapper reviewMapper;
    @Resource
    private ReviewContractMapper reviewContractMapper;
    @Resource
    private ReviewPointsMapper reviewPointsMapper;
    @Resource
    private PointsDataMapper pointsDataMapper;

    @Override
    public PageResult<ReviewPageRespVO> getReviewList(ReviewPageReqVO vo) {
        //查询审查清单列表
        PageResult<ReviewListDO> reviewListDOPageResult = reviewMapper.getReviewList(vo);
        PageResult<ReviewPageRespVO> result = ReviewConverter.INSTANCE.reviewListDOResp(reviewListDOPageResult);
        List<ReviewPageRespVO> list = result.getList();
        //根据审查清单id查询中间表
        List<ReviewContractDO> reviewContractDOS = reviewContractMapper.selectList();
        // 使用Map来存储每个审查清单id对应的合同类型列表
        Map<String, List<ReviewContractVO>> reviewIdToTypeListMap = reviewContractDOS.stream()
                .collect(Collectors.groupingBy(
                        ReviewContractDO::getReviewId,
                        Collectors.mapping(r -> {
                            ReviewContractVO reviewContractVO = new ReviewContractVO();
                            reviewContractVO.setTypeId(r.getTypeId());
                            reviewContractVO.setTypeName(r.getTypeName());
                            return reviewContractVO;
                        }, Collectors.toList())
                ));

        // 更新审查清单列表，将合同类型列表填充到对应的审查清单中
        list.forEach(reviewPageRespVO -> {
            String reviewId = reviewPageRespVO.getId();
            reviewPageRespVO.setTypeList(reviewIdToTypeListMap.getOrDefault(reviewId, Collections.emptyList()));
        });
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(ReviewCreateReqVO reviewCreateReqVO) {
        if (ObjectUtil.isEmpty(reviewCreateReqVO)) {
            throw exception(ErrorCodeConstants.EMPTY_REQUIRE_PARAM_ERROR);
        }
        //将数据新增到审查清单表
        ReviewListDO reviewListDO = new ReviewListDO();
        BeanUtils.copyProperties(reviewCreateReqVO, reviewListDO);
        reviewMapper.insert(reviewListDO);
        //将数据添加到审查清单表和审查点中间表
        List<PointsDataDO> pointsDataDOS = pointsDataMapper.selectList();
        List<ReviewPointsDO> reviewPointsDOList = getReviewPointsDOList(reviewCreateReqVO.getReviewPointsDOList(), reviewListDO.getId(), pointsDataDOS);
        reviewPointsMapper.insertBatch(reviewPointsDOList);
    }

    @Override
    public ReviewRespVO selectById(ReviewReqVO reviewReqVO) {
        //查询审查清单
        ReviewListDO reviewListDO = reviewMapper.selectById(reviewReqVO.getId());
        //根据id查询审查清单和合同类型中间表
        QueryWrapper<ReviewContractDO> wrapperType = new QueryWrapper<>();
        wrapperType.eq("review_id", reviewReqVO.getId());
        List<ReviewContractDO> reviewContractDOS = reviewContractMapper.selectList(wrapperType);
        List<ReviewContractVO> reviewContractVOS = ReviewConverter.INSTANCE.ReviewVoContractTypeToEntity(reviewContractDOS);
        //根据审查清单id查询审查清单和审查点中间表
        QueryWrapper<ReviewPointsDO> wrapper = new QueryWrapper<>();
        wrapper.eq("review_id", reviewReqVO.getId());
        List<ReviewPointsDO> reviewPointsDOList = reviewPointsMapper.selectList(wrapper);
        List<ReviewPointsVO> reviewPointsVOS = ReviewConverter.INSTANCE.ReviewPointsVoToEntity(reviewPointsDOList);
        ReviewRespVO reviewRespVO = ReviewConverter.INSTANCE.reviewToEntity(reviewListDO);
        reviewRespVO.setTypes(reviewContractVOS);
        reviewRespVO.setReviewPointsList(reviewPointsVOS);
        return reviewRespVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ReviewUpdateReqVO reviewUpdateReqVO) {
        //修改审查清单
        ReviewListDO reviewListDO = new ReviewListDO();
        BeanUtils.copyProperties(reviewUpdateReqVO, reviewListDO);
        reviewMapper.updateById(reviewListDO);
        //修改审查清单表和审查点中间表,先删除在添加
        LambdaUpdateWrapper<ReviewPointsDO> updateWrapperP = new LambdaUpdateWrapper<>();
        updateWrapperP.in(ReviewPointsDO::getReviewId, reviewUpdateReqVO.getId());
        reviewPointsMapper.delete(updateWrapperP);
        List<PointsDataDO> pointsDataDOS = pointsDataMapper.selectList();
        List<ReviewPointsDO> reviewPointsDOList = getReviewPointsDOList(reviewUpdateReqVO.getReviewPointsDOList(), reviewListDO.getId(), pointsDataDOS);
        reviewPointsMapper.insertBatch(reviewPointsDOList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByIdList(ReviewIdListVO idListVO) {
        //首先查询是否和合同类型关联
        List<ReviewContractDO> reviewContractDOS = reviewContractMapper.selectBatchIds(idListVO.getIds());
        //判断集合是否为空
        if (!reviewContractDOS.isEmpty()) {
            throw exception(ErrorCodeConstants.DATA_IS_CALL);
        }
        //根据审查清单id删除审查清单
        reviewMapper.deleteBatchIds(idListVO.getIds());
        //根据审查清单id删除审查清单表和审查点中间表
        LambdaUpdateWrapper<ReviewPointsDO> updateWrapperP = new LambdaUpdateWrapper<>();
        updateWrapperP.set(ReviewPointsDO::getDeleted, ParamUtil.ONE)
                .in(ReviewPointsDO::getReviewId, idListVO.getIds());
        reviewPointsMapper.update(null, updateWrapperP);
    }

    @NotNull
    private List<ReviewPointsDO> getReviewPointsDOList(List<ReviewPointsVO> list, String id, List<PointsDataDO> pointsDataDOS) {
        List<ReviewPointsDO> reviewPointsDOList = new ArrayList<>();
        for (ReviewPointsVO reviewPointsDO : list) {
            ReviewPointsDO reviewPoints = new ReviewPointsDO();
            reviewPoints.setReviewId(id);
            reviewPoints.setPointsId(reviewPointsDO.getPointsId());
            reviewPoints.setPrompt(reviewPointsDO.getPrompt());
            reviewPoints.setGrade(reviewPointsDO.getGrade());
            //如果是系统推荐，使用默认风险提示语
            if (Objects.equals(reviewPointsDO.getPrompt(), ParamUtil.ZERO)) {
                //根据id查询风险提示语
                for (PointsDataDO pointsDataDO : pointsDataDOS) {
                    if (pointsDataDO.getId().equals(reviewPointsDO.getPointsId())) {
                        reviewPoints.setRiskPrompt(pointsDataDO.getRiskPrompt());
                    }
                }
            } else {
                reviewPoints.setRiskPrompt(reviewPointsDO.getRiskPrompt());
            }
            reviewPointsDOList.add(reviewPoints);
        }
        return reviewPointsDOList;
    }
}
