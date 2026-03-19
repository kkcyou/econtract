package com.yaoan.module.econtract.service.review.lmpl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.yaoan.module.econtract.controller.admin.review.vo.ReviewPointsTypeReqVO;
import com.yaoan.module.econtract.controller.admin.review.vo.ReviewPointsTypeRespVO;
import com.yaoan.module.econtract.convert.review.ReviewConverter;
import com.yaoan.module.econtract.dal.dataobject.review.PointsTypeDO;
import com.yaoan.module.econtract.dal.mysql.review.PointsTypeMapper;
import com.yaoan.module.econtract.service.review.PointsTypeService;
import com.yaoan.module.econtract.util.TreeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;


@Service
@Slf4j
public class PointsTypeServiceImpl implements PointsTypeService {
    @Resource
    private PointsTypeMapper pointsTypeMapper;

    @Override
    public List<ReviewPointsTypeRespVO> getPointsList(ReviewPointsTypeReqVO reviewPointsTypeReqVO) {
        //查询全部数据
        List<PointsTypeDO> pageResult = pointsTypeMapper.getPointsOneList(reviewPointsTypeReqVO);
        List<ReviewPointsTypeRespVO> result = ReviewConverter.INSTANCE.reviewPointsTypeDOResp(pageResult);
        // 获取层级关系
        List<ReviewPointsTypeRespVO> child = TreeUtil.getChild("0", result);
        // 搜索树形结构数据
        return TreeUtil.childSearchName(child, reviewPointsTypeReqVO.getName());

    }

}
