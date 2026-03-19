package com.yaoan.module.econtract.service.contractreviewitems;

import cn.hutool.core.collection.CollectionUtil;
import com.yaoan.framework.common.exception.util.ServiceExceptionUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.ContractReviewItemsBaseVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.relchecklistrule.*;
import com.yaoan.module.econtract.convert.contractreviewitems.ContractReviewItemsConvert;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ContractReviewItemsDO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.RelChecklistRuleDO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ReviewChecklistDO;
import com.yaoan.module.econtract.dal.mysql.contractreviewitems.ContractReviewItemsMapper;
import com.yaoan.module.econtract.dal.mysql.contractreviewitems.RelChecklistRuleMapper;
import com.yaoan.module.econtract.dal.mysql.contractreviewitems.ReviewChecklistMapper;
import com.yaoan.module.econtract.enums.contractreviewitems.BasisTypeEnums;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 审查清单-审查规则关联 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class RelChecklistRuleServiceImpl implements RelChecklistRuleService {

    @Resource
    private RelChecklistRuleMapper relChecklistRuleMapper;

    @Resource
    private ContractReviewItemsMapper contractReviewItemsMapper;

    @Resource
    private ContractReviewItemsService contractReviewItemsService;

    @Resource
    private ReviewChecklistMapper ReviewChecklistMapper;

    @Override
    public String createRelChecklistRule(RelChecklistRuleSaveReqVO createReqVO) {
        // 插入
        RelChecklistRuleDO relChecklistRule = BeanUtils.toBean(createReqVO, RelChecklistRuleDO.class);
        relChecklistRuleMapper.insert(relChecklistRule);
        // 返回
        return relChecklistRule.getId();
    }

    @Override
    public void updateRelChecklistRule(RelChecklistRuleSaveReqVO updateReqVO) {
        // 校验存在
        validateRelChecklistRuleExists(updateReqVO.getId());
        // 更新
        RelChecklistRuleDO updateObj = BeanUtils.toBean(updateReqVO, RelChecklistRuleDO.class);
        relChecklistRuleMapper.updateById(updateObj);
    }

    @Override
    public void deleteRelChecklistRule(String id) {
        // 校验存在
        validateRelChecklistRuleExists(id);
        // 删除
        relChecklistRuleMapper.deleteById(id);
    }

    private void validateRelChecklistRuleExists(String id) {
        if (relChecklistRuleMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.invalidParamException("数据不存在！");
        }
    }

    @Override
    public RelChecklistRuleDO getRelChecklistRule(String id) {
        return relChecklistRuleMapper.selectById(id);
    }

    @Override
    public PageResult<RelChecklistRuleDO> getRelChecklistRulePage(RelChecklistRulePageReqVO pageReqVO) {
        return relChecklistRuleMapper.selectPage(pageReqVO);
    }

    @Override
    public List<RelCheckListRuleCommonRespVO> getRelChecklistRuleByIds(List<String> checkListIds) {

        List<RelCheckListRuleCommonRespVO> returnCommon = new ArrayList<>();
        //查询ids下的所有数据 N:N
        List<RelChecklistRuleDO> relChecklistRuleDOS = relChecklistRuleMapper.selectList(RelChecklistRuleDO::getReviewListId, checkListIds);

        if (relChecklistRuleDOS != null && !relChecklistRuleDOS.isEmpty()) {

            //将数据进行分组
            Map<String, List<RelChecklistRuleDO>> groupMap = relChecklistRuleDOS.stream()
                    .collect(Collectors.groupingBy(RelChecklistRuleDO::getReviewListId));

            //查询清单ids对应的清档名称
            List<String> reviewListIds = CollectionUtils.convertList(relChecklistRuleDOS, RelChecklistRuleDO::getReviewListId);

            List<ReviewChecklistDO> reviewChecklistDOS = ReviewChecklistMapper.selectBatchIds(reviewListIds);

            //将id和名称组合成map
            Map<String, ReviewChecklistDO> reviewChecklistMap = CollectionUtils.convertMap(reviewChecklistDOS, ReviewChecklistDO::getId);

            groupMap.forEach((reviewListId, relChecklistRules) -> {

                //获取清单名称
                String reviewListName = "";
                if (reviewChecklistMap.get(reviewListId) != null) {
                    ReviewChecklistDO reviewChecklistDO = reviewChecklistMap.get(reviewListId);
                    reviewListName = reviewChecklistDO.getReviewListName();
                }

                List<ContractRuleCommonRespVO> contractRuleCommonRespVOS = new ArrayList<>();

                for (RelChecklistRuleDO relChecklistRuleDO : relChecklistRules) {
                    String reviewId = relChecklistRuleDO.getReviewId();

                    ContractReviewItemsBaseVO contractReviewItems = contractReviewItemsService.getContractReviewItems(reviewId);
                    ContractRuleCommonRespVO contractRuleCommonRespVO = ContractReviewItemsConvert.INSTANCE.convertRespVO(contractReviewItems);
                    if (CollectionUtil.isNotEmpty(contractRuleCommonRespVO.getLegalBasis())) {
                        contractRuleCommonRespVO.getLegalBasis().forEach(item -> item.setReviewId(null).setId(null).setType(BasisTypeEnums.getInstance(Integer.valueOf(item.getType())).getInfo()));
                    }

                    contractRuleCommonRespVOS.add(contractRuleCommonRespVO);
                }
                returnCommon.add(new RelCheckListRuleCommonRespVO().setReviewItems(contractRuleCommonRespVOS).setReviewListname(reviewListName));
            });
        }
        return returnCommon;
    }

    @Override
    public List<ReviewRulesRespVO> getReviewRulesByCheckListIds(List<String> checkListIds) {
        List<ReviewRulesRespVO> result = new ArrayList<>();
        List<RelChecklistRuleDO> relChecklistRuleDOS = relChecklistRuleMapper.selectList(RelChecklistRuleDO::getReviewListId, checkListIds);
        if (CollectionUtil.isNotEmpty(relChecklistRuleDOS)) {
            List<String> reviewIds = relChecklistRuleDOS.stream().map(RelChecklistRuleDO::getReviewId).collect(Collectors.toList());
            List<ContractReviewItemsDO> contractReviewItemsDOS = contractReviewItemsMapper.selectList(ContractReviewItemsDO::getId, reviewIds);
            if (CollectionUtil.isNotEmpty(contractReviewItemsDOS)) {
                contractReviewItemsDOS.forEach(item -> {
                    ReviewRulesRespVO reviewRulesRespVO = new ReviewRulesRespVO();
                    reviewRulesRespVO.setReviewContent(item.getReviewContent()).setReviewId(item.getId());
                    result.add(reviewRulesRespVO);
                });
            }
        }
        // 如果result中存在相同的审查id，则合并
        result.forEach(item -> {
            if (result.stream().filter(i -> i.getReviewId().equals(item.getReviewId())).count() > 1) {
                result.removeIf(i -> i.getReviewId().equals(item.getReviewId()));
            }
        });
        return result;
    }


}