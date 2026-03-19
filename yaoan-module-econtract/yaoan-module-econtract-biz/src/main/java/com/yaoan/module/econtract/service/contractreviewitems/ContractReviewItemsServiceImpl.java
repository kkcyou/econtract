package com.yaoan.module.econtract.service.contractreviewitems;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.ClauseGroupRespVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.ContractReviewItemsBaseVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.ContractReviewItemsExportReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.ContractReviewItemsListReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.ContractReviewItemsPageReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.ContractReviewItemsRespVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.ContractReviewItemsUpdateStatusReqVO;
import com.yaoan.module.econtract.controller.admin.contractreviewitems.vo.ReviewContractTypeVO;
import com.yaoan.module.econtract.controller.admin.reviewitembasis.vo.ReviewItemBasisBaseVO;
import com.yaoan.module.econtract.convert.contractreviewitems.ContractReviewItemsConvert;
import com.yaoan.module.econtract.convert.reviewitembasis.ReviewItemBasisConvert;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ContractReviewItemsDO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.RelChecklistRuleDO;
import com.yaoan.module.econtract.dal.dataobject.contractreviewitems.ReviewContractTypeRelDO;
import com.yaoan.module.econtract.dal.dataobject.reviewitembasis.ReviewItemBasisDO;
import com.yaoan.module.econtract.dal.mysql.contractreviewitems.ContractReviewItemsMapper;
import com.yaoan.module.econtract.dal.mysql.contractreviewitems.RelChecklistRuleMapper;
import com.yaoan.module.econtract.dal.mysql.contractreviewitems.ReviewContractTypeRelMapper;
import com.yaoan.module.econtract.dal.mysql.reviewitembasis.ReviewItemBasisMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.contractreviewitems.BasisTypeEnums;
import com.yaoan.module.econtract.enums.contractreviewitems.ReviewContractTypeEnums;
import com.yaoan.module.econtract.enums.contractreviewitems.RiskLevelEnums;
import com.yaoan.module.econtract.enums.contractreviewitems.RiskUnfavorableEnums;
import com.yaoan.module.econtract.enums.contractreviewitems.TermClassificationEnums;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;


/**
 * 合同审查规则 Service 实现类
 *
 * @author admin
 */
@Service
@Validated
public class ContractReviewItemsServiceImpl implements ContractReviewItemsService {

    @Resource
    private ContractReviewItemsMapper contractReviewItemsMapper;
    @Resource
    private ReviewItemBasisMapper reviewItemBasisMapper;
    @Resource
    private ReviewContractTypeRelMapper reviewContractTypeRelMapper;
    @Resource
    private RelChecklistRuleMapper relChecklistRuleMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createContractReviewItems(ContractReviewItemsBaseVO createReqVO) {
        // 1. 插入主表
        ContractReviewItemsDO contractReviewItems = ContractReviewItemsConvert.INSTANCE.convert(createReqVO);
        contractReviewItemsMapper.insert(contractReviewItems);
        String reviewId = contractReviewItems.getId();

        // 2. 处理法律依据
        processLegalBasis(createReqVO.getLegalBasis(), reviewId);

        // 3. 处理合同类型
        processContractTypes(createReqVO.getContractTypes(), reviewId);

        return reviewId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateContractReviewItems(ContractReviewItemsBaseVO updateReqVO) {
        // 1. 校验存在
        validateContractReviewItemsExists(updateReqVO.getId());

        // 2. 更新主表
        ContractReviewItemsDO updateObj = ContractReviewItemsConvert.INSTANCE.convert(updateReqVO);

        // 3. 更新关联表 - 先删除后插入
        // 更新法律依据
        reviewItemBasisMapper.delete(new LambdaQueryWrapperX<ReviewItemBasisDO>().eq(ReviewItemBasisDO::getReviewId, updateObj.getId()));
        processLegalBasis(updateReqVO.getLegalBasis(), updateReqVO.getId());

        // 更新合同类型
        reviewContractTypeRelMapper.delete(new LambdaQueryWrapperX<ReviewContractTypeRelDO>().eq(ReviewContractTypeRelDO::getReviewId, updateObj.getId()));
        processContractTypes(updateReqVO.getContractTypes(), updateReqVO.getId());

        // 4. 更新主表
        contractReviewItemsMapper.updateById(updateObj);
    }

    private void processLegalBasis(List<ReviewItemBasisBaseVO> legalBasis, String reviewId) {
        if (CollectionUtils.isEmpty(legalBasis)) {
            return;
        }

        List<ReviewItemBasisDO> basisList = ReviewItemBasisConvert.INSTANCE.convertList03(legalBasis);
        basisList.forEach(item -> item.setReviewId(reviewId));
        reviewItemBasisMapper.insertBatch(basisList);
    }

    private void processContractTypes(List<ReviewContractTypeVO> contractTypes, String reviewId) {
        if (CollectionUtils.isEmpty(contractTypes)) {
            return;
        }
        List<ReviewContractTypeRelDO> typeList = contractTypes.stream().map(type -> {
            ReviewContractTypeRelDO relDO = new ReviewContractTypeRelDO();
            relDO.setReviewId(reviewId);
            relDO.setContractTypeId(type.getContractTypeId());
            relDO.setContractTypeName(ReviewContractTypeEnums.getInstance(type.getContractTypeId()).getInfo());
            return relDO;
        }).collect(Collectors.toList());
        reviewContractTypeRelMapper.insertBatch(typeList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteContractReviewItems(String id) {
        // 校验存在
        validateContractReviewItemsExists(id);
        // 校验是否有审查清单在使用
        checkReviewIfUse(id);
        // 删除
        contractReviewItemsMapper.deleteById(id);
        // 删除关联表数据
        reviewItemBasisMapper.delete(new LambdaQueryWrapperX<ReviewItemBasisDO>().eq(ReviewItemBasisDO::getReviewId, id));
        // 更新合同类型
        reviewContractTypeRelMapper.delete(new LambdaQueryWrapperX<ReviewContractTypeRelDO>().eq(ReviewContractTypeRelDO::getReviewId, id));

    }

    private void checkReviewIfUse(String id) {
        if (relChecklistRuleMapper.selectCount(new LambdaQueryWrapperX<RelChecklistRuleDO>().eq(RelChecklistRuleDO::getReviewId, id)) > 0) {
            throw exception(ErrorCodeConstants.DIY_ERROR, "审查规则正在使用中，无法删除");
        }
    }

    private void validateContractReviewItemsExists(String id) {
        if (contractReviewItemsMapper.selectById(id) == null) {
            throw exception(ErrorCodeConstants.DIY_ERROR, "合同审查规则不存在");
        }
    }

    @Override
    public ContractReviewItemsBaseVO getContractReviewItems(String id) {
        ContractReviewItemsDO contractReviewItemsDO = contractReviewItemsMapper.selectById(id);
        if (ObjectUtil.isNull(contractReviewItemsDO)) {
            throw exception(ErrorCodeConstants.DIY_ERROR, "合同审查规则不存在");
        }

        ContractReviewItemsBaseVO contractReviewItemsBaseVO = ContractReviewItemsConvert.INSTANCE.convertV1(contractReviewItemsDO);
        // 获取法律依据列表
        List<ReviewItemBasisDO> basisList = reviewItemBasisMapper.selectList(new LambdaQueryWrapper<ReviewItemBasisDO>().eq(ReviewItemBasisDO::getReviewId, id));
        if (CollectionUtil.isNotEmpty(basisList)) {
            contractReviewItemsBaseVO.setLegalBasis(ReviewItemBasisConvert.INSTANCE.convertList04(basisList));
        }
        // 获取合同类型列表
        List<ReviewContractTypeRelDO> typeList = reviewContractTypeRelMapper.selectList(new LambdaQueryWrapper<ReviewContractTypeRelDO>().eq(ReviewContractTypeRelDO::getReviewId, id));
        if (CollectionUtil.isNotEmpty(typeList)) {
            contractReviewItemsBaseVO.setContractTypes(ContractReviewItemsConvert.INSTANCE.convertListV3(typeList));
        }
        return contractReviewItemsBaseVO;

    }

    @Override
    public List<ClauseGroupRespVO> getContractReviewItemsList(ContractReviewItemsListReqVO reqVO) {
        // 1. 构建查询条件
        LambdaQueryWrapperX<ContractReviewItemsDO> queryWrapper = new LambdaQueryWrapperX<ContractReviewItemsDO>()
                .likeIfPresent(ContractReviewItemsDO::getReviewContent, reqVO.getReviewContent())
                .eqIfPresent(ContractReviewItemsDO::getRiskLevel, reqVO.getRiskLevelFlag())
                .eqIfPresent(ContractReviewItemsDO::getTermId, reqVO.getTermId())
                .eq(ContractReviewItemsDO::getReviewStatus, 1)
                .orderByDesc(ContractReviewItemsDO::getUpdateTime);

        // 2. 处理合同类型筛选
        if (CollectionUtil.isNotEmpty(reqVO.getContractTypesIds())) {
            List<ReviewContractTypeRelDO> reviewContractTypeRelDOS = reviewContractTypeRelMapper.selectList(ReviewContractTypeRelDO::getContractTypeId, reqVO.getContractTypesIds());
            List<String> reviewIds = reviewContractTypeRelDOS.stream().map(ReviewContractTypeRelDO::getReviewId).distinct().collect(Collectors.toList());
            if (CollectionUtils.isEmpty(reviewIds)) {
                return Collections.emptyList();
            }
            queryWrapper.in(ContractReviewItemsDO::getId, reviewIds);
        }

        //3. 增加清单id筛选项
        if(StringUtils.isNotEmpty(reqVO.getReviewListId())){
            List<String> ruleS = relChecklistRuleMapper.selectList(RelChecklistRuleDO::getReviewListId, reqVO.getReviewListId()).stream()
                    .map(RelChecklistRuleDO::getReviewId).distinct().collect(Collectors.toList());

            queryWrapper.in(ContractReviewItemsDO::getId, ruleS);
        }

        // 3. 查询所有符合条件的记录
        List<ContractReviewItemsDO> allItems = contractReviewItemsMapper.selectList(queryWrapper);
        if (CollectionUtil.isEmpty(allItems)) {
            return Collections.emptyList();
        }
        // 查询依据表
        List<ReviewItemBasisDO> basisList = reviewItemBasisMapper.selectList(
                new LambdaQueryWrapper<ReviewItemBasisDO>()
                        .in(ReviewItemBasisDO::getReviewId, allItems.stream().map(ContractReviewItemsDO::getId).collect(Collectors.toList())));
        Map<String, List<ReviewItemBasisDO>> basisMap = basisList.stream().collect(Collectors.groupingBy(ReviewItemBasisDO::getReviewId));
        // 4. 按条款分组并统计
        Map<Integer, List<ContractReviewItemsDO>> groupedByClause = allItems.stream().collect(Collectors.groupingBy(ContractReviewItemsDO::getTermId));
        // 5. 转换为响应VO
        return convertToClauseGroupRespVOs(groupedByClause, basisMap);
    }

    private List<ClauseGroupRespVO> convertToClauseGroupRespVOs(Map<Integer, List<ContractReviewItemsDO>> groupedByClause, Map<String, List<ReviewItemBasisDO>> basisMap) {

        return groupedByClause.entrySet().stream().map(
                entry -> buildClauseGroupRespVO(entry.getKey(), entry.getValue(), basisMap))
                .sorted(Comparator.comparing(ClauseGroupRespVO::getTermId)).collect(Collectors.toList());
    }


    private ClauseGroupRespVO buildClauseGroupRespVO(Integer termId, List<ContractReviewItemsDO> items, Map<String, List<ReviewItemBasisDO>> basisMap) {

        ClauseGroupRespVO vo = new ClauseGroupRespVO();
        vo.setTermId(termId);
        vo.setTermName(TermClassificationEnums.getInstance(termId).getInfo());

        List<ContractReviewItemsBaseVO> itemVOs = convertAndEnrichItems(items, basisMap);
        vo.setRules(itemVOs);
        vo.setRuleIds(itemVOs.stream().map(ContractReviewItemsBaseVO::getId).distinct().collect(Collectors.toList()));

        return vo;
    }

    private List<ContractReviewItemsBaseVO> convertAndEnrichItems(List<ContractReviewItemsDO> items, Map<String, List<ReviewItemBasisDO>> basisMap) {
        List<ContractReviewItemsBaseVO> itemVOs = ContractReviewItemsConvert.INSTANCE.convertListV4(items);
        itemVOs.forEach(item -> {
            // 设置风险等级名称
            item.setRiskLevelName(RiskLevelEnums.getInstance(item.getRiskLevel()).getInfo());
            // 设置风险不利方名称
            item.setRiskPartyName(RiskUnfavorableEnums.getInstance(item.getRiskParty()).getInfo());
            // 设置法律依据
            List<ReviewItemBasisDO> bases = basisMap.get(item.getId());
            if (CollectionUtil.isEmpty(bases)) {
                return;
            }
            List<ReviewItemBasisBaseVO> reviewItemBasisBaseVOS = ReviewItemBasisConvert.INSTANCE.convertList04(bases);
            reviewItemBasisBaseVOS.forEach(vo -> {
                vo.setTypeName(BasisTypeEnums.getInstance(Integer.valueOf(vo.getType())).getInfo());
            });
            item.setLegalBasis(reviewItemBasisBaseVOS);
        });

        return itemVOs;
    }

    @Override
    public PageResult<ContractReviewItemsRespVO> getContractReviewItemsPage(ContractReviewItemsPageReqVO pageReqVO) {
        // 条款id是规则id
        List<String> termId = pageReqVO.getTermId();
        if (CollectionUtil.isNotEmpty(termId)) {
            // 获取所有枚举的 code 值（转为字符串）
            List<String> enumCodes = Arrays.stream(TermClassificationEnums.values())
                    .map(enumValue -> enumValue.getCode().toString())
                    .collect(Collectors.toList());
            List<String> ruleIds = termId.stream()
                    .filter(term -> enumCodes.stream().noneMatch(term::equals))
                    .collect(Collectors.toList());
            pageReqVO.setRuleIds(ruleIds);
            pageReqVO.setTermId(null);
        }
        //如果存在清单id,且规则id为空，则查询清单下符合当前条款的合同审查规则
        if (StringUtils.isNotEmpty(pageReqVO.getReviewListId()) && ObjectUtil.isEmpty(pageReqVO.getRuleIds())) {
            List<String> ruleS = relChecklistRuleMapper.selectList(RelChecklistRuleDO::getReviewListId, pageReqVO.getReviewListId())
                    .stream().map(RelChecklistRuleDO::getReviewId).distinct().collect(Collectors.toList());
            pageReqVO.setRuleIds(ruleS);
        }
        // 处理合同类型筛选
        if (CollectionUtil.isNotEmpty(pageReqVO.getContractTypesIds())) {
            List<ReviewContractTypeRelDO> reviewContractTypeRelDOS = reviewContractTypeRelMapper.selectList(ReviewContractTypeRelDO::getContractTypeId, pageReqVO.getContractTypesIds());
            List<String> reviewIds = reviewContractTypeRelDOS.stream().map(ReviewContractTypeRelDO::getReviewId).distinct().collect(Collectors.toList());
            List<String> ruleIds = pageReqVO.getRuleIds();
            if (CollectionUtil.isEmpty(ruleIds)) {
                pageReqVO.setRuleIds(reviewIds);
            } else {
                // 取reviewIds和ruleIds的交集
                ruleIds = ruleIds.stream().filter(reviewIds::contains).collect(Collectors.toList());
                pageReqVO.setRuleIds(ruleIds);
            }
        }
        if (CollectionUtil.isNotEmpty(pageReqVO.getTermId()) || CollectionUtil.isNotEmpty(pageReqVO.getContractTypesIds())) {
            if (CollectionUtil.isEmpty(pageReqVO.getRuleIds())) {
                return PageResult.empty();
            }
        }
        PageResult<ContractReviewItemsDO> contractReviewItemsDOPageResult = contractReviewItemsMapper.selectPage(pageReqVO);
        if (CollectionUtils.isEmpty(contractReviewItemsDOPageResult.getList())) {
            return PageResult.empty();
        }
        PageResult<ContractReviewItemsRespVO> result = ContractReviewItemsConvert.INSTANCE.convertPage(contractReviewItemsDOPageResult);
        // 填充合同类型名称
        List<String> ids = result.getList().stream().map(ContractReviewItemsRespVO::getId).collect(Collectors.toList());
        List<ReviewContractTypeRelDO> reviewContractTypeRelDOS = reviewContractTypeRelMapper.selectList(ReviewContractTypeRelDO::getReviewId, ids);
        Map<String, List<ReviewContractTypeRelDO>> reviewContractTypeRelMap = reviewContractTypeRelDOS.stream().collect(Collectors.groupingBy(ReviewContractTypeRelDO::getReviewId));
        result.getList().forEach(item -> {
            item.setTermName(TermClassificationEnums.getInstance(item.getTermId()).getInfo());
            // 设置风险等级名称
            item.setRiskLevelName(RiskLevelEnums.getInstance(item.getRiskLevel()).getInfo());
            // 设置风险不利方名称
            item.setRiskPartyName(RiskUnfavorableEnums.getInstance(item.getRiskParty()).getInfo());
            List<ReviewContractTypeRelDO> reviewContractTypeRelDOS1 = reviewContractTypeRelMap.get(item.getId());
            if (CollectionUtil.isNotEmpty(reviewContractTypeRelDOS1)) {
                reviewContractTypeRelDOS1.forEach(reviewContractTypeRelDO -> {});
                // 将名称用,拼接添加到contractTypeName
                String collect = reviewContractTypeRelDOS1.stream().map(ReviewContractTypeRelDO::getContractTypeName).collect(Collectors.joining(","));
                item.setContractTypeName(collect);

            }
        });
        return result;
    }

    @Override
    public List<ContractReviewItemsDO> getContractReviewItemsExcelList(ContractReviewItemsExportReqVO exportReqVO) {
        return contractReviewItemsMapper.selectList(exportReqVO);
    }

    @Override
    public void updateReviewStatus(ContractReviewItemsUpdateStatusReqVO updateReqVO) {
        // 校验是否有审查清单在使用
        checkReviewIfUse(updateReqVO.getId());
        contractReviewItemsMapper.updateById(new ContractReviewItemsDO().setId(updateReqVO.getId()).setReviewStatus(updateReqVO.getStatus()));
    }

    @Override
    public PageResult<ContractReviewItemsRespVO> getContractReviewItemsPageV2(ContractReviewItemsPageReqVO pageVO) {
        // 条款id是规则id
        List<String> termId = pageVO.getTermId();
        if (CollectionUtil.isNotEmpty(termId)) {
            // 获取所有枚举的 code 值（转为字符串）
            List<String> enumCodes = Arrays.stream(TermClassificationEnums.values())
                    .map(enumValue -> enumValue.getCode().toString())
                    .collect(Collectors.toList());
            List<String> ruleIds = termId.stream()
                    .filter(term -> enumCodes.stream().noneMatch(term::equals))
                    .collect(Collectors.toList());
            pageVO.setRuleIds(ruleIds);
            pageVO.setTermId(null);
        }
        //如果存在清单id,且规则id为空，则查询清单下符合当前条款的合同审查规则
        if (StringUtils.isNotEmpty(pageVO.getReviewListId()) && ObjectUtil.isEmpty(pageVO.getRuleIds())) {
            List<String> ruleS = relChecklistRuleMapper.selectList(RelChecklistRuleDO::getReviewListId, pageVO.getReviewListId())
                    .stream().map(RelChecklistRuleDO::getReviewId).distinct().collect(Collectors.toList());
            pageVO.setRuleIds(ruleS);
        }

        PageResult<ContractReviewItemsDO> contractReviewItemsDOPageResult = contractReviewItemsMapper.selectPage(pageVO);
        if (CollectionUtils.isEmpty(contractReviewItemsDOPageResult.getList())) {
            return PageResult.empty();
        }
        PageResult<ContractReviewItemsRespVO> result = ContractReviewItemsConvert.INSTANCE.convertPage(contractReviewItemsDOPageResult);
        // 填充合同类型名称
        List<String> ids = result.getList().stream().map(ContractReviewItemsRespVO::getId).collect(Collectors.toList());
        List<ReviewContractTypeRelDO> reviewContractTypeRelDOS = reviewContractTypeRelMapper.selectList(ReviewContractTypeRelDO::getReviewId, ids);
        Map<String, List<ReviewContractTypeRelDO>> reviewContractTypeRelMap = reviewContractTypeRelDOS.stream().collect(Collectors.groupingBy(ReviewContractTypeRelDO::getReviewId));
        // 获取法律依据列表
        List<String> reviewIds = result.getList().stream().map(ContractReviewItemsRespVO::getId).collect(Collectors.toList());
        List<ReviewItemBasisDO> basisList = reviewItemBasisMapper.selectList(ReviewItemBasisDO::getReviewId, reviewIds);
        Map<String, List<ReviewItemBasisDO>> basisMap = basisList.stream().collect(Collectors.groupingBy(ReviewItemBasisDO::getReviewId));
        result.getList().forEach(item -> {
            item.setTermName(TermClassificationEnums.getInstance(item.getTermId()).getInfo());
            // 设置风险等级名称
            item.setRiskLevelName(RiskLevelEnums.getInstance(item.getRiskLevel()).getInfo());
            // 设置风险不利方名称
            item.setRiskPartyName(RiskUnfavorableEnums.getInstance(item.getRiskParty()).getInfo());
            List<ReviewContractTypeRelDO> reviewContractTypeRelDOS1 = reviewContractTypeRelMap.get(item.getId());
            if (CollectionUtil.isNotEmpty(reviewContractTypeRelDOS1)) {
                reviewContractTypeRelDOS1.forEach(reviewContractTypeRelDO -> {});
                // 将名称用,拼接添加到contractTypeName
                String collect = reviewContractTypeRelDOS1.stream().map(ReviewContractTypeRelDO::getContractTypeName).collect(Collectors.joining(","));
                item.setContractTypeName(collect);

            }
            List<ReviewItemBasisDO> basisList1 = basisMap.get(item.getId());
            if (CollectionUtil.isNotEmpty(basisList1)) {
                item.setLegalBasis(ReviewItemBasisConvert.INSTANCE.convertList04(basisList1));
            }
        });
        return result;
    }

}
