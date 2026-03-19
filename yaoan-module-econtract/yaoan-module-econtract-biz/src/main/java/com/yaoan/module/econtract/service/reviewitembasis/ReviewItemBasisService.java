package com.yaoan.module.econtract.service.reviewitembasis;

import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.reviewitembasis.vo.ReviewItemBasisCreateReqVO;
import com.yaoan.module.econtract.controller.admin.reviewitembasis.vo.ReviewItemBasisExportReqVO;
import com.yaoan.module.econtract.controller.admin.reviewitembasis.vo.ReviewItemBasisPageReqVO;
import com.yaoan.module.econtract.controller.admin.reviewitembasis.vo.ReviewItemBasisUpdateReqVO;
import com.yaoan.module.econtract.dal.dataobject.reviewitembasis.ReviewItemBasisDO;

import java.util.*;
import javax.validation.*;

/**
 * 合同审查规则依据 Service 接口
 *
 * @author admin
 */
public interface ReviewItemBasisService {

    /**
     * 创建合同审查规则依据
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createReviewItemBasis(@Valid ReviewItemBasisCreateReqVO createReqVO);

    /**
     * 更新合同审查规则依据
     *
     * @param updateReqVO 更新信息
     */
    void updateReviewItemBasis(@Valid ReviewItemBasisUpdateReqVO updateReqVO);

    /**
     * 删除合同审查规则依据
     *
     * @param id 编号
     */
    void deleteReviewItemBasis(String id);

    /**
     * 获得合同审查规则依据
     *
     * @param id 编号
     * @return 合同审查规则依据
     */
    ReviewItemBasisDO getReviewItemBasis(String id);

    /**
     * 获得合同审查规则依据列表
     *
     * @param ids 编号
     * @return 合同审查规则依据列表
     */
    List<ReviewItemBasisDO> getReviewItemBasisList(Collection<String> ids);

    /**
     * 获得合同审查规则依据分页
     *
     * @param pageReqVO 分页查询
     * @return 合同审查规则依据分页
     */
    PageResult<ReviewItemBasisDO> getReviewItemBasisPage(ReviewItemBasisPageReqVO pageReqVO);

    /**
     * 获得合同审查规则依据列表, 用于 Excel 导出
     *
     * @param exportReqVO 查询条件
     * @return 合同审查规则依据列表
     */
    List<ReviewItemBasisDO> getReviewItemBasisList(ReviewItemBasisExportReqVO exportReqVO);

}
