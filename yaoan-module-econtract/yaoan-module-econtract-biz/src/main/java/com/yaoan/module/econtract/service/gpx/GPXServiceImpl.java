package com.yaoan.module.econtract.service.gpx;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.econtract.controller.admin.catalog.vo.ModelIdVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractPlaybackV3RespVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading.SupplierCombinationInfoVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading.TradingSupplierVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.*;
import com.yaoan.module.econtract.controller.admin.gpx.vo.autofill.PlaybackReqVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.draft.*;
import com.yaoan.module.econtract.convert.contract.trading.TradingSupplierConverter;
import com.yaoan.module.econtract.convert.gcy.gpmall.GPMallOrderConverter;
import com.yaoan.module.econtract.convert.gpx.GPXConverter;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.TradingContractExtDO;
import com.yaoan.module.econtract.dal.dataobject.contract.trading.TradingSupplierDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.*;
import com.yaoan.module.econtract.dal.dataobject.model.SimpleModel;
import com.yaoan.module.econtract.dal.dataobject.statistics.StatisticsDO;
import com.yaoan.module.econtract.dal.mysql.contract.TradingContractExtMapper;
import com.yaoan.module.econtract.dal.mysql.contract.trading.TradingSupplierMapper;
import com.yaoan.module.econtract.dal.mysql.gpx.*;
import com.yaoan.module.econtract.dal.mysql.model.SimpleModelMapper;
import com.yaoan.module.econtract.dal.mysql.statistics.StatisticsMapper;
import com.yaoan.module.econtract.enums.common.IfEnums;
import com.yaoan.module.econtract.enums.gcy.gpmall.HLJContractStatusEnums;
import com.yaoan.module.econtract.enums.gpx.BiddingMethodEnums;
import com.yaoan.module.econtract.enums.gpx.GpxPlanIdTag;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import com.yaoan.module.econtract.enums.order.ProjectCategoryEnums;
import com.yaoan.module.econtract.service.catalog.PurCatalogService;
import com.yaoan.module.econtract.service.gpx.zcd.ZcdService;
import com.yaoan.module.econtract.service.hljsupply.HLJSupplyService;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.user.OrganizationApi;
import com.yaoan.module.system.api.user.SupplyApi;
import com.yaoan.module.system.api.user.dto.OrganizationDTO;
import com.yaoan.module.system.api.user.dto.SupplyDTO;
import com.yaoan.module.system.enums.user.UserTypeEnums;
import com.yaoan.module.system.service.user.SupplyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.EXIST_DRAFT;
import static com.yaoan.module.system.enums.config.SystemConfigKeyEnums.IF_NEED_MODEL_CATEGORY;
import static com.yaoan.module.system.enums.config.SystemConfigKeyEnums.IS_CHECK_EVALUATE;

/**
 * @description:
 * @author: Pele
 * @date: 2024/11/20 15:10
 */
@Slf4j
@Service
public class GPXServiceImpl implements GPXService {

    @Resource
    private GPXProjectMapper gpxProjectMapper;
    @Resource
    private PackageInfoMapper packageInfoMapper;
    @Resource
    private SupplierInfoMapper supplierInfoMapper;
    @Resource
    private SupplierCombinationInfoMapper supplierCombinationInfoMapper;
    @Resource
    private PlanInfoMapper planInfoMapper;
    @Resource
    private PlanDetailInfoMapper planDetailMapper;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private StatisticsMapper statisticsMapper;
    @Resource
    private PackageDetailInfoMapper packageDetailInfoMapper;
    @Resource
    private PackageDetailParamMapper packageDetailParamMapper;
    @Resource
    private BidConfirmQuotationDetailMapper bidConfirmQuotationDetailMapper;
    @Resource
    private BatchPlanInfoMapper batchPlanInfoMapper;
    @Resource
    private TradingContractExtMapper tradingContractExtMapper;
    @Resource
    private ZcdService zcdService;
    @Resource
    private PurCatalogService purCatalogService;
    @Resource
    private HLJSupplyService hljSupplyService;
    @Resource
    private OrganizationApi organizationApi;
    @Resource
    private TradingSupplierMapper tradingSupplierMapper;
    @Resource
    private SupplyApi supplyApi;
    @Resource
    private SimpleModelMapper simpleModelMapper;

    @Override
    @DataPermission(enable = false)
    @Transactional(rollbackFor = Exception.class)
    public String draft(List<ProjectInfo> list) {
        List<PackageInfoDO> packageInfoDOList = new ArrayList<>();
        List<PackageDetailInfoDO> packageDetailInfoDOList = new ArrayList<>();
        List<PlanInfoDO> planInfoDOList = new ArrayList<>();
        List<PackageDetailParamDO> packageDetailParamDOList = new ArrayList<>();
        List<SupplierInfoDO> supplierInfoDOList = new ArrayList<>();
        List<SupplierCombinationInfoDO> supplierCombinationInfoDOList = new ArrayList<>();
        List<BidConfirmQuotationDetailDO> bidConfirmQuotationDetailDOList = new ArrayList<>();
        List<BatchPlanInfoDO> batchPlanInfoDOList = new ArrayList<>();
        List<PlanDetailInfoDO> planDetailInfoDOList = new ArrayList<>();
        List<GPXProjectDO> projectDOList = new ArrayList<>();
        List<StatisticsDO> statisticsDOList = new ArrayList<>();

        List<String> projectIds = list.stream().map(ProjectInfo::getId).collect(Collectors.toList());
        List<GPXProjectDO> exitProjects = gpxProjectMapper.selectList(GPXProjectDO::getProjectGuid, projectIds);
        List<String> exitProjectIds = exitProjects.stream().map(GPXProjectDO::getProjectGuid).collect(Collectors.toList());

        for (ProjectInfo projectInfo : list) {
            if (!exitProjectIds.contains(projectInfo.getId())) {
                GPXProjectDO cp = GPXConverter.INSTANCE.cp(projectInfo);
                projectDOList.add(cp);
            }
            //采购单位ids
            List<String> joinOrgIds = new ArrayList<>();

            //一般项目采购的采购单位ids
            if (BiddingMethodEnums.COMMON.getCode().equals(projectInfo.getBiddingMethodCode())) {

                if (projectInfo.getPlanInfo() != null && StringUtils.isNotBlank(projectInfo.getPlanInfo().getPurchaseUnitId())) {
                    joinOrgIds.add(projectInfo.getPlanInfo().getPurchaseUnitId());
                }
            }
            // 2计划批次信息
            Map<String, List<String>> packIdOrgIdsMap = new HashMap<String, List<String>>();

            if (BiddingMethodEnums.UNION.getCode().equals(projectInfo.getBiddingMethodCode()) || BiddingMethodEnums.BATCH.getCode().equals(projectInfo.getBiddingMethodCode())) {

                if (CollectionUtil.isNotEmpty(projectInfo.getBatchPlanInfoList())) {
                    batchPlanInfoDOList = GPXConverter.INSTANCE.listBatchPlanR2D(projectInfo.getBatchPlanInfoList());
                    packIdOrgIdsMap = getUnionOrgIdsMap(projectInfo);
                    //批次计划的计划明细
                    for (BatchPlanInfo batchPlanInfo : projectInfo.getBatchPlanInfoList()) {
                        if (CollectionUtil.isNotEmpty(batchPlanInfo.getPlanDetailInfo())) {
                            List<PlanDetailInfoDO> planDetailInfoDOS = GPXConverter.INSTANCE.cPlanDetailR2DList(batchPlanInfo.getPlanDetailInfo());
                            planDetailInfoDOList.addAll(planDetailInfoDOS);
                        }
                    }
                }
            }

            // 3采购计划
            if (ObjectUtil.isNotNull(projectInfo.getPlanInfo())) {
                PlanInfoDO planInfoDO = GPXConverter.INSTANCE.cPlanR2D(projectInfo.getPlanInfo());
                planInfoDO.setProjectId(projectInfo.getId());
                PlanInfoDO planInfoDO2 = planInfoMapper.selectById(planInfoDO.getPlanId());
                // 如果存在相同计划，就加Tag，每个项目一查
                if (ObjectUtil.isNotNull(planInfoDO2)) {
                    String planId = IdUtil.fastSimpleUUID() + GpxPlanIdTag.GPX_PLAN_PLAN_ID_TAG + planInfoDO.getPlanId();
                    planInfoDO.setPlanId(planId);
                }

                planInfoDOList.add(planInfoDO);
                // 3.1计划明细
                if (CollectionUtil.isNotEmpty(projectInfo.getPlanInfo().getPlanDetailInfo())) {
                    List<PlanDetailInfoDO> planDetailInfoDO = GPXConverter.INSTANCE.cPlanDetailR2DList(projectInfo.getPlanInfo().getPlanDetailInfo());
                    if (CollectionUtil.isNotEmpty(planDetailInfoDO)) {
                        planDetailInfoDOList.addAll(planDetailInfoDO);
                    }
                }
            }

            // 1分包信息
            if (CollectionUtil.isNotEmpty(projectInfo.getPackageInfoList())) {
                for (PackageInfo packageInfo : projectInfo.getPackageInfoList()) {

                    PackageInfoDO packageInfoDO = GPXConverter.INSTANCE.c1(packageInfo);
                    packageInfoDO.setProjectGuid(projectInfo.getId()).setBiddingMethodCode(projectInfo.getBiddingMethodCode()).setBiddingMethodName(projectInfo.getBiddingMethodName()).setProjectType(projectInfo.getProjectType()).setProjectTypeName(projectInfo.getProjectTypeName()).setPurchaseMethodCode(projectInfo.getPurchaseMethodCode()).setPurchaseMethodName(projectInfo.getPurchaseMethodName()).setProjectName(projectInfo.getProjectName()).setProjectCode(projectInfo.getProjectCode());
                    String packageId = packageInfo.getId();
                    if (BiddingMethodEnums.UNION.getCode().equals(projectInfo.getBiddingMethodCode()) || BiddingMethodEnums.BATCH.getCode().equals(projectInfo.getBiddingMethodCode())) {
                        joinOrgIds = packIdOrgIdsMap.get(packageInfo.getId());

                    }
                    if (CollectionUtil.isNotEmpty(joinOrgIds)) {
                        packageInfoDO.setPurchaserOrgIds(String.join(",", joinOrgIds));
                    }

                    // 1.1.1分包明细
                    if (CollectionUtil.isNotEmpty(packageInfo.getPackageDetailInfoList())) {
                        for (PackageDetailInfo packageDetailInfo : packageInfo.getPackageDetailInfoList()) {
                            PackageDetailInfoDO packageDetailInfoDO = GPXConverter.INSTANCE.c2(packageDetailInfo);
                            packageDetailInfoDO.setPackageGuid(packageId);
                            packageDetailInfoDOList.add(packageDetailInfoDO);
                            // 1.1.1包明细参数
                            if (ObjectUtil.isNotNull(packageDetailInfo.getPackageDetailParam())) {
                                List<PackageDetailParamDO> packageDetailParamDO = GPXConverter.INSTANCE.cPackageDetailParamR2DList(packageDetailInfo.getPackageDetailParam());
                                if (CollectionUtil.isNotEmpty(packageDetailParamDO)) {
                                    packageDetailParamDOList.addAll(packageDetailParamDO);
                                }
                            }
                        }
                    }
                    // 1.2中标供应商
                    if (CollectionUtil.isNotEmpty(packageInfo.getSupplierList())) {
                        //判断是否是多供应商
                        if (1 < packageInfo.getSupplierList().size()) {
                            packageInfoDO.setSupplierType(2);
                        }
                        for (SupplierInfo supplierInfo : packageInfo.getSupplierList()) {
                            SupplierInfoDO supplierInfoDO = GPXConverter.INSTANCE.cSupR2D(supplierInfo);
                            supplierInfoDOList.add(supplierInfoDO);
                            // 1.2.1联合体供应商
                            if (CollectionUtil.isNotEmpty(supplierInfo.getSupplierCombinationInfoList())) {
                                List<SupplierCombinationInfoDO> supplierCombinationInfoDOS = GPXConverter.INSTANCE.listsupComR2D(supplierInfo.getSupplierCombinationInfoList());
                                supplierCombinationInfoDOList.addAll(supplierCombinationInfoDOS);
                            }
                            // 1.2.2供应商报价明细
                            if (CollectionUtil.isNotEmpty(supplierInfo.getBidConfirmQuotationDetailList())) {
                                List<BidConfirmQuotationDetailDO> bidConfirmQuotationDetailDOs = GPXConverter.INSTANCE.listBidConfirmQuaR2D(supplierInfo.getBidConfirmQuotationDetailList());
                                bidConfirmQuotationDetailDOs = bidConfirmQuotationDetailDOs.stream().peek(detail -> detail.setPackageId(packageInfo.getId())).collect(Collectors.toList());
                                bidConfirmQuotationDetailDOList.addAll(bidConfirmQuotationDetailDOs);
                            }
                        }
                    }
                    packageInfoDOList.add(packageInfoDO);
                }
            }
        }


        List<String> packageIds = packageInfoDOList.stream().map(PackageInfoDO::getPackageGuid).collect(Collectors.toList());
        List<String> packageDetailIds = packageDetailInfoDOList.stream().map(PackageDetailInfoDO::getDetailId).collect(Collectors.toList());
        List<String> supplierIdList = supplierInfoDOList.stream().map(SupplierInfoDO::getId).collect(Collectors.toList());
        List<String> planIds = new ArrayList<>();

        //批量插入
        if (CollectionUtil.isNotEmpty(projectDOList)) {
            gpxProjectMapper.saveOrUpdateBatch(projectDOList);
        }
        if (CollectionUtil.isNotEmpty(packageInfoDOList)) {
            packageInfoMapper.saveOrUpdateBatch(packageInfoDOList);//分包可能不是一批来的，不可误删分包
            try {
                List<PackageInfoDO> packageInfoList = new ArrayList<>();
                packageInfoDOList.forEach(packageInfoDO -> {
                    //判断是否流标
                    if (packageInfoDO.getIsLost() == 0) {
                        packageInfoList.add(packageInfoDO);
                    }
                });
                if (CollectionUtil.isNotEmpty(packageInfoList)) {
                    statisticsDOList = GPMallOrderConverter.INSTANCE.packageToStatisticsList(packageInfoList);
                }
                if (CollectionUtil.isNotEmpty(planInfoDOList)) {
                    Map<String, String> planMap = planInfoDOList.stream().collect(Collectors.toMap(PlanInfoDO::getProjectId, PlanInfoDO::getPlanCode));
                    Map<String, String> orgMap = planInfoDOList.stream().collect(Collectors.toMap(PlanInfoDO::getPurchaseUnitId, PlanInfoDO::getPurchaseUnitName));
                    statisticsDOList.forEach(statisticsDO -> {
                        //添加来源
                        statisticsDO.setPlatform(PlatformEnums.GPMS_GPX.getCode());
                        //计算超期时间
                        if (statisticsDO.getWinBidTime() != null) {
                            LocalDateTime overdueTime = statisticsDO.getWinBidTime().plusDays(30); // 增加30天
                            statisticsDO.setOverdueTime(overdueTime);
                        }
                        // 遍历 packageInfoDOList，并设置对应的 buyPlanCode
                        packageInfoDOList.stream().filter(item -> {
                            String projectGuid = item.getProjectGuid();
                            String purchaserOrgIds = item.getPurchaserOrgIds();
                            return (projectGuid != null && planMap.containsKey(projectGuid)) || (purchaserOrgIds != null && orgMap.containsKey(purchaserOrgIds));
                        }).findFirst().ifPresent(item -> {
                            // 设置 buyPlanCode
                            String buyPlanCode = planMap.get(item.getProjectGuid());
                            if (buyPlanCode != null) {
                                statisticsDO.setBuyPlanCode(buyPlanCode);
                            }
                            // 设置 orgName
                            String orgName = orgMap.get(item.getPurchaserOrgIds());
                            if (orgName != null) {
                                statisticsDO.setOrgName(orgName);
                            }
                        });
                    });
                }
                //添加到数据统计表包信息
                statisticsMapper.saveOrUpdateBatch(statisticsDOList);
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        if (CollectionUtil.isNotEmpty(packageDetailInfoDOList)) {
            packageDetailInfoMapper.active(packageDetailIds);
            packageDetailInfoMapper.saveOrUpdateBatch(packageDetailInfoDOList);
            packageDetailInfoMapper.delete(new LambdaQueryWrapperX<PackageDetailInfoDO>().notIn(PackageDetailInfoDO::getDetailId, packageDetailIds).in(PackageDetailInfoDO::getPackageGuid, packageIds));
        }
        if (CollectionUtil.isNotEmpty(packageDetailParamDOList)) {
            List<String> packageDetailParamIds = packageDetailParamDOList.stream().map(PackageDetailParamDO::getId).collect(Collectors.toList());
            packageDetailParamMapper.active(packageDetailParamIds);
            packageDetailParamMapper.saveOrUpdateBatch(packageDetailParamDOList);
            packageDetailParamMapper.delete(new LambdaQueryWrapperX<PackageDetailParamDO>().notIn(PackageDetailParamDO::getId, packageDetailParamIds).in(PackageDetailParamDO::getPackageId, packageIds));
        }
        if (CollectionUtil.isNotEmpty(supplierInfoDOList)) {
            supplierInfoMapper.active(supplierIdList);
            supplierInfoMapper.saveOrUpdateBatch(supplierInfoDOList);
            supplierInfoMapper.delete(new LambdaQueryWrapperX<SupplierInfoDO>().notIn(SupplierInfoDO::getId, supplierIdList).in(SupplierInfoDO::getPackageId, packageIds));
        }
        if (CollectionUtil.isNotEmpty(supplierCombinationInfoDOList)) {
            List<String> saveIds = supplierCombinationInfoDOList.stream().map(SupplierCombinationInfoDO::getId).collect(Collectors.toList());
            supplierCombinationInfoMapper.active(saveIds);
            supplierCombinationInfoMapper.saveOrUpdateBatch(supplierCombinationInfoDOList);
            supplierCombinationInfoMapper.delete(new LambdaQueryWrapperX<SupplierCombinationInfoDO>().notIn(SupplierCombinationInfoDO::getId, saveIds).in(SupplierCombinationInfoDO::getPackageId, packageIds));
        }
        if (CollectionUtil.isNotEmpty(bidConfirmQuotationDetailDOList)) {
            List<String> saveIds = bidConfirmQuotationDetailDOList.stream().map(BidConfirmQuotationDetailDO::getId).collect(Collectors.toList());
            bidConfirmQuotationDetailMapper.active(saveIds);
            bidConfirmQuotationDetailMapper.saveOrUpdateBatch(bidConfirmQuotationDetailDOList);
            bidConfirmQuotationDetailMapper.delete(new LambdaQueryWrapperX<BidConfirmQuotationDetailDO>().notIn(BidConfirmQuotationDetailDO::getId, saveIds).in(BidConfirmQuotationDetailDO::getPackageId, packageIds));
        }
        if (CollectionUtil.isNotEmpty(batchPlanInfoDOList)) {
            planIds = batchPlanInfoDOList.stream().map(BatchPlanInfoDO::getId).collect(Collectors.toList());
            batchPlanInfoMapper.activate(planIds);
            batchPlanInfoMapper.saveOrUpdateBatch(batchPlanInfoDOList);
            batchPlanInfoMapper.delete(new LambdaQueryWrapperX<BatchPlanInfoDO>().notIn(BatchPlanInfoDO::getId, planIds).in(BatchPlanInfoDO::getPackageId, packageIds));
        }
        if (CollectionUtil.isNotEmpty(planInfoDOList)) {
            planInfoMapper.saveOrUpdateBatch(planInfoDOList);
            //一般项目采购的采购计划会有改动的情况吗？  @AMXi：不会
            //所以不需要清数据
        }
        if (CollectionUtil.isNotEmpty(planDetailInfoDOList)) {
            List<String> saveIds = planDetailInfoDOList.stream().map(PlanDetailInfoDO::getId).collect(Collectors.toList());
            planDetailMapper.active(saveIds);
            planDetailMapper.saveOrUpdateBatch(planDetailInfoDOList);
            planDetailMapper.delete(new LambdaQueryWrapperX<PlanDetailInfoDO>().notIn(PlanDetailInfoDO::getId, saveIds).in(PlanDetailInfoDO::getPackageId, packageIds));
        }

        return "true";
    }

    /**
     * 联合采购时，获得按包id分组的采购单位id
     */
    private Map<String, List<String>> getUnionOrgIdsMap(ProjectInfo projectInfo) {
        if (CollectionUtil.isEmpty(projectInfo.getBatchPlanInfoList())) {
            return Collections.emptyMap();
        }

        Map<String, List<String>> orgIdByPackIdMap = new HashMap<>();

        for (BatchPlanInfo batchPlanInfo : projectInfo.getBatchPlanInfoList()) {
            String packageId = batchPlanInfo.getPackageId();
            String purchaserId = batchPlanInfo.getPurchaserId();

            // 检查 packageId 是否已经存在于 orgIdByPackIdMap 中
            if (orgIdByPackIdMap.containsKey(packageId)) {
                // 如果已存在，则将 purchaserId 添加到对应的列表中
                orgIdByPackIdMap.get(packageId).add(purchaserId);
            } else {
                // 如果不存在，则创建一个新的列表，并将 purchaserId 添加进去
                List<String> purchaserIds = new ArrayList<>();
                purchaserIds.add(purchaserId);
                orgIdByPackIdMap.put(packageId, purchaserIds);
            }
        }
        return orgIdByPackIdMap;
    }

    /**
     * 一般项目有中标金额还有剩余金额就可以起草
     * 联合采购不隐藏
     */
    @Override
    public PageResult<GPXListRespVO> list(GPXListReqVO reqVO) {
        // 分包
        // 去除合同状态为草稿、已发送、已确认、待盖章、完成、待代理机构审核、未备案、备案中、已备案
        List<Integer> excludedStatuses = Arrays.asList(
                HLJContractStatusEnums.CONTRACT_AUDITSTATUS_TOBESENT.getCode(),
                HLJContractStatusEnums.CONTRACT_AUDITSTATUS_TOBECONFIRMED.getCode(),
                HLJContractStatusEnums.CONTRACT_AUDITSTATUS_SURE.getCode(),
                HLJContractStatusEnums.CONTRACT_AUDITSTATUS_SURE2.getCode(),
                HLJContractStatusEnums.CONTRACT_AUDITSTATUS_DELIVERED.getCode(),
                HLJContractStatusEnums.BUYER_SIGNED.getCode(),
                HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECEIVED.getCode(),
                HLJContractStatusEnums.TO_BE_CONFIRMED_BY_AGENCY.getCode(),
                HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode(),
                HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode(),
                HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode()
        );
        //去除在途合同的包id(保留联合采购)
//        List<TradingContractDO> tradingContractDOS = tradingContractMapper.selectList(new LambdaQueryWrapper<TradingContractDO>()
//        .eq(TradingContractDO::getPlatform, PlatformEnums.GPMS_GPX.getCode())
//                        .ne(TradingContractDO::getPlatform,PlatformEnums.GPMS_GPX.getCode())//电子交易不去除
//                .in(TradingContractDO::getStatus, excludedStatuses));
        List<TradingContractExtDO> tradingContractDOS = tradingContractExtMapper.selectList4GPX(excludedStatuses);
        List<String> idList = tradingContractDOS.stream().map(TradingContractExtDO::getBuyPlanPackageId)
                .filter(Objects::nonNull).collect(Collectors.toList());
        PageResult<PackageInfoDO> packagePage = new PageResult<PackageInfoDO>();
        //联合采购：只能采购人起草
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        //供应商用户
        if (Integer.valueOf("2").equals(loginUser.getType())) {
            packagePage = packageInfoMapper.selectPage4Sup(reqVO, idList);
        } else {
            packagePage = packageInfoMapper.selectPage1(reqVO, idList);
        }
//        PageResult<PackageInfoDO> packagePage = packageInfoMapper.selectPage(reqVO);
        return enhanceCommonPageV2(packagePage, BiddingMethodEnums.getInstance(reqVO.getBiddingMethodCode()), reqVO.getToken());
    }

    private PageResult<GPXListRespVO> enhanceCommonPageV2(PageResult<PackageInfoDO> packagePage, BiddingMethodEnums instance, String token) {

        PageResult<GPXListRespVO> result = GPXConverter.INSTANCE.pageD2R(packagePage);
        if (CollectionUtil.isEmpty(packagePage.getList())) {
            return result;
        }
        List<String> projectIds = packagePage.getList().stream().map(PackageInfoDO::getProjectGuid).collect(Collectors.toList());
        List<String> packageIds = packagePage.getList().stream().map(PackageInfoDO::getPackageGuid).collect(Collectors.toList());
        List<GPXProjectDO> projectDOList = gpxProjectMapper.selectList(GPXProjectDO::getProjectGuid, projectIds);
        Map<String, GPXProjectDO> projectDOMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(projectDOList)) {
            projectDOMap = CollectionUtils.convertMap(projectDOList, GPXProjectDO::getProjectGuid);
        }
        List<String> projectCodes = projectDOList.stream().map(GPXProjectDO::getProjectCode).collect(Collectors.toList());
        Map<String, PlanInfoDO> planInfoMap = new HashMap<>();
        Map<String, List<BatchPlanInfoDO>> batchPlanInfoMap = new HashMap<>();
        List<String> planIds = new ArrayList<>();
        //信用评价
        List<String> toDoCodes = new ArrayList<>();
        String evaluateConfig = systemConfigApi.getConfigByKey(IS_CHECK_EVALUATE.getKey());
//        if (IfEnums.YES.getCode().equals(evaluateConfig)) {
//            toDoCodes = getToEvaluateCodes(projectCodes, token);
//        }
        List<SupplierInfoDO> supplierInfos = supplierInfoMapper.selectList(SupplierInfoDO::getPackageId, packageIds);

        Map<String, List<SupplierInfoDO>> supplierMap = CollectionUtils.convertMultiMap(supplierInfos, SupplierInfoDO::getPackageId);

        List<TradingContractExtDO> contractDOList = tradingContractExtMapper.selectBackedContracts(planIds);
        Map<String, TradingContractExtDO> planContractMap = CollectionUtils.convertMap(contractDOList, TradingContractExtDO::getBuyPlanId);


        for (GPXListRespVO item : result.getList()) {
            //一般项目采购
            if (BiddingMethodEnums.COMMON.getCode().equals(item.getBiddingMethodCode())) {
                List<PlanInfoDO> plans = planInfoMapper.selectList(PlanInfoDO::getProjectId, projectIds);
                if (CollectionUtil.isNotEmpty(plans)) {
                    planIds.addAll(plans.stream().map(PlanInfoDO::getPlanId).collect(Collectors.toList()));
                }
                planInfoMap = CollectionUtils.convertMap(plans, PlanInfoDO::getProjectId);
            }
            //批量集中采购
            if (BiddingMethodEnums.BATCH.getCode().equals(item.getBiddingMethodCode())) {
                //todo: 供应商草拟待定
                List<BatchPlanInfoDO> batchProjectPlanInfoList = batchPlanInfoMapper.batchProjectPlanInfoList(packageIds);
                if (CollectionUtil.isNotEmpty(batchProjectPlanInfoList)) {
                    planIds.addAll(batchProjectPlanInfoList.stream().map(BatchPlanInfoDO::getPlanId).collect(Collectors.toList()));
                }
                batchPlanInfoMap = CollectionUtils.convertMultiMap(batchProjectPlanInfoList, BatchPlanInfoDO::getPackageId);
            }
            //联合采购
            if (BiddingMethodEnums.UNION.getCode().equals(item.getBiddingMethodCode())) {
                //todo: 供应商草拟待定
                List<BatchPlanInfoDO> unionProjectPlanInfoList = batchPlanInfoMapper.unionProjectPlanInfoList(projectIds);
                if (CollectionUtil.isNotEmpty(unionProjectPlanInfoList)) {
                    planIds.addAll(unionProjectPlanInfoList.stream().map(BatchPlanInfoDO::getPlanId).collect(Collectors.toList()));
                }
                batchPlanInfoMap = CollectionUtils.convertMultiMap(unionProjectPlanInfoList, BatchPlanInfoDO::getProjectId);
            }
            List<PlanRespVO> planResps = new ArrayList<>();
            //1.项目信息 包信息
            //2.采购单位 代理机构计划信息
            item.setToSignAmount(item.getAmount());
            item.setSignedAmount(BigDecimal.ZERO);
            if (BiddingMethodEnums.COMMON.getCode().equals(item.getBiddingMethodCode())) {
                PlanInfoDO planInfoDO = planInfoMap.get(item.getProjectGuid());
                if (planInfoDO != null) {
                    item.setAgencyId(planInfoDO.getAgencyId()).setAgencyName(planInfoDO.getAgencyName()).setPurchaserId(planInfoDO.getPurchaseUnitId()).setPurchaserOrgName(planInfoDO.getPurchaseUnitName()).setPurchaserLinkName(planInfoDO.getPurchaseUnitName()).setPurchaserLinkTel(planInfoDO.getPurchaseLinkTel());
                    planResps.add(new PlanRespVO().setPlanId(planInfoDO.getPlanId()).setPlanName(planInfoDO.getPlanName()).setPlanCode(planInfoDO.getPlanCode()).setPlanAmount(planInfoDO.getPlanBudget()).setToSignAmount(planInfoDO.getPlanBudget()).setSourceCode(planInfoDO.getSourceCode()));
                    //5. 已签 可签
                    if (planContractMap.containsKey(planInfoDO.getPlanId())) {
                        TradingContractExtDO contractDO = planContractMap.get(planInfoDO.getPlanId());
                        if (contractDO != null) {
                            item.setSignedAmount(contractDO.getTotalMoney());
                            item.setToSignAmount(item.getAmount().subtract(contractDO.getTotalMoney()));
                        }
                    }
                }
            } else if (BiddingMethodEnums.BATCH.getCode().equals(item.getBiddingMethodCode())) {
                List<BatchPlanInfoDO> batchPlanInfoList = batchPlanInfoMap.get(item.getPackageGuid());
                if (CollectionUtil.isNotEmpty(batchPlanInfoList)) {
                    BatchPlanInfoDO planInfoDO = batchPlanInfoList.get(0);
                    item.setPurchaserId(planInfoDO.getPurchaserId()).setPurchaserOrgName(planInfoDO.getPurchaserName());
                    BigDecimal signedAmount = BigDecimal.ZERO;
                    for (BatchPlanInfoDO batchPlanInfoDO : batchPlanInfoList) {
                        PlanRespVO planRespVO = new PlanRespVO().setPlanId(batchPlanInfoDO.getPlanId()).setPlanName(batchPlanInfoDO.getPlanName()).setPlanCode(batchPlanInfoDO.getPlanCode()).setPlanAmount(batchPlanInfoDO.getPlanBudget()).setToSignAmount(batchPlanInfoDO.getPlanBudget()).setSourceCode(batchPlanInfoDO.getSourceCode());
                        planResps.add(planRespVO);
                        if (planContractMap.containsKey(planInfoDO.getPlanId())) {
                            TradingContractExtDO contractDO = planContractMap.get(planInfoDO.getPlanId());
                            if (contractDO != null) {
                                signedAmount = contractDO.getTotalMoney().add(signedAmount);
                            }
                        }
                    }
                    item.setSignedAmount(signedAmount);
                    item.setToSignAmount(item.getAmount().subtract(signedAmount));
                    //代理机构
                    GPXProjectDO projectDO = projectDOMap.get(item.getProjectGuid());
                    if (ObjectUtil.isNotNull(projectDO)) {
                        item.setAgencyId(projectDO.getAgencyId());
                        item.setAgencyName(projectDO.getAgencyName());
                    }
                }
            } else if (BiddingMethodEnums.UNION.getCode().equals(item.getBiddingMethodCode())) {
                List<BatchPlanInfoDO> batchPlanInfoList = batchPlanInfoMap.get(item.getProjectGuid());
                if (CollectionUtil.isNotEmpty(batchPlanInfoList)) {
                    //联合采购都是同一个采购人，所以采购单位取值都一样
                    BatchPlanInfoDO planInfoDO = batchPlanInfoList.get(0);
                    item.setPurchaserId(planInfoDO.getPurchaserId()).setPurchaserOrgName(planInfoDO.getPurchaserName());
                    BigDecimal signedAmount = BigDecimal.ZERO;
                    for (BatchPlanInfoDO batchPlanInfoDO : batchPlanInfoList) {
                        PlanRespVO planRespVO = new PlanRespVO().setPlanId(batchPlanInfoDO.getPlanId()).setPlanName(batchPlanInfoDO.getPlanName()).setPlanCode(batchPlanInfoDO.getPlanCode()).setPlanAmount(batchPlanInfoDO.getPlanBudget()).setToSignAmount(batchPlanInfoDO.getPlanBudget()).setSourceCode(batchPlanInfoDO.getSourceCode());
                        planResps.add(planRespVO);
                        if (planContractMap.containsKey(planInfoDO.getPlanId())) {
                            TradingContractExtDO contractDO = planContractMap.get(planInfoDO.getPlanId());
                            if (contractDO != null) {
                                signedAmount = contractDO.getTotalMoney().add(signedAmount);
                            }
                        }
                    }
                    item.setSignedAmount(signedAmount);
                    item.setToSignAmount(item.getAmount().subtract(signedAmount));
                    //代理机构
                    GPXProjectDO projectDO = projectDOMap.get(item.getProjectGuid());
                    if (ObjectUtil.isNotNull(projectDO)) {
                        item.setAgencyId(projectDO.getAgencyId());
                        item.setAgencyName(projectDO.getAgencyName());
                        item.setProjectType(projectDO.getProjectType());
                        item.setProjectTypeName(projectDO.getProjectTypeName());
                    }
                }
            }
            item.setPlans(planResps);
            //4.中标供应商信息
            //一般项目采购的供应商起草列表逻辑
            LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
            if (BiddingMethodEnums.COMMON.getCode().equals(item.getBiddingMethodCode()) && UserTypeEnums.SUPPLIER.getCode().equals(loginUser.getType())) {

                List<SupplierRespVO> supplierResps = new ArrayList<>();
                List<SupplierInfoDO> supplierInfoList = supplierMap.get(item.getPackageGuid());
                if (CollectionUtil.isNotEmpty(supplierInfoList)) {
                    for (SupplierInfoDO supplierInfoDO : supplierInfoList) {
                        if (supplierInfoDO.getSupplierId().equals(loginUser.getSupplyId())) {
                            supplierResps.add(new SupplierRespVO().setSupplierName(supplierInfoDO.getSupplierName()).setSupplierId(supplierInfoDO.getSupplierId()).setUpdateTime(supplierInfoDO.getUpdateTime()));
                        }
                    }
                    item.setSupplierList(supplierResps);
                }

            } else {
                List<SupplierRespVO> supplierResps = new ArrayList<>();
                List<SupplierInfoDO> supplierInfoList = supplierMap.get(item.getPackageGuid());
                if (CollectionUtil.isNotEmpty(supplierInfoList)) {
                    for (SupplierInfoDO supplierInfoDO : supplierInfoList) {
                        supplierResps.add(new SupplierRespVO().setSupplierName(supplierInfoDO.getSupplierName()).setSupplierId(supplierInfoDO.getSupplierId()).setUpdateTime(supplierInfoDO.getUpdateTime()));
                    }
                    supplierResps = supplierResps.stream().sorted(Comparator.comparing(SupplierRespVO::getUpdateTime).reversed()).collect(Collectors.toList());
                    item.setSupplierList(supplierResps);
                }
            }
            //已签订金额和可签订金额
            //关联包的合同总金额  CONTRACT_AUDITSTATUS_CANCEL  CONTRACT_AUDITSTATUS_CANCELLATION
            ArrayList<Integer> list = new ArrayList<>();
            list.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode());
            list.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode());
            list.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_DELETE.getCode());
            List<TradingContractExtDO> tradingContractDOS = tradingContractExtMapper.selectList(new LambdaQueryWrapperX<TradingContractExtDO>().select(TradingContractExtDO::getTotalMoney).notIn(TradingContractExtDO::getStatus, list).eq(TradingContractExtDO::getBuyPlanPackageId, item.getPackageGuid()));
            BigDecimal totalMoney = BigDecimal.ZERO;
            if (CollectionUtil.isNotEmpty(tradingContractDOS)) {
                totalMoney = tradingContractDOS.stream().filter(many -> ObjectUtil.isNotEmpty(many.getTotalMoney())).map(TradingContractExtDO::getTotalMoney).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            item.setSignedAmount(totalMoney);
            BigDecimal signAmount = item.getAmount().subtract(totalMoney);
            if (signAmount.compareTo(BigDecimal.ZERO) < 0) {
                signAmount = BigDecimal.ZERO;
            }
            item.setToSignAmount(signAmount);
            //诚信评价
            //默认已评
            item.setHaveValuated(true);
            if (CollectionUtil.isNotEmpty(toDoCodes)) {
                //如果未评匹配，则为未评
                if (toDoCodes.contains(item.getProjectCode())) {
                    item.setHaveValuated(false);
                }
            }
            List<TradingContractExtDO> contractS = checkPlanV2(item.getPackageGuid(), null);
            if (CollectionUtil.isNotEmpty(contractS)) {
                item.setIsDraft(true);
            } else {
                item.setIsDraft(false);
            }
        }
        return result;
    }

    public List<TradingContractExtDO> checkPlanV2(String packageId, String contractId) {
        List<Integer> statusList = new ArrayList<>();
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_TOBECONFIRMED.getCode());
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_TOBESENT.getCode());
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_SURE.getCode());
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_SURE2.getCode());
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_DELIVERED.getCode());
        statusList.add(HLJContractStatusEnums.BUYER_SIGNED.getCode());
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECEIVED.getCode());
        statusList.add(HLJContractStatusEnums.TO_BE_CONFIRMED_BY_AGENCY.getCode());
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode());
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode());
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode());
        List<TradingContractExtDO> tradingContracts = tradingContractExtMapper.selectList(new LambdaQueryWrapperX<TradingContractExtDO>().eq(TradingContractExtDO::getBuyPlanPackageId, packageId).select(TradingContractExtDO::getId, TradingContractExtDO::getStatus));
        List<TradingContractExtDO> targetContracts = tradingContracts.stream().filter(item -> (StringUtils.isNotEmpty(contractId) && contractId.equals(item.getId())) || statusList.contains(item.getStatus())).collect(Collectors.toList());

        return targetContracts;
    }

    /**
     * 一般项目采购的返回金额是中标供应商的中标金额
     */
    @Override
    public GPXContractPlaybackV3RespVO playbackInfoV3(PlaybackReqVO reqVO) throws Exception {
        //校验该计划是否已生成合同
//        this.checkPlan(reqVO.getPackageId(), reqVO.getContractId());
        GPXContractPlaybackV3RespVO result = new GPXContractPlaybackV3RespVO();
        List<BidConfirmQuotationDetailDO> bidConfirmQuotationDetails = new ArrayList<BidConfirmQuotationDetailDO>();
        Map<String, List<BidConfirmQuotationDetailDO>> quotationDetailDOBySupNameMap = new HashMap<String, List<BidConfirmQuotationDetailDO>>();
        Long supCount = supplierInfoMapper.selectCount(SupplierInfoDO::getPackageId, reqVO.getPackageId());
        if (1L == supCount) {
            result.setSupplierType(1);
        }
        if (1L < supCount) {
            result.setSupplierType(2);
        }

        //项目类型
        String projectId = reqVO.getProjectId();
        GPXProjectDO projectDO = gpxProjectMapper.selectOne(GPXProjectDO::getProjectGuid, projectId);
        result.setContractType(ProjectCategoryEnums.getInstance(projectDO.getProjectType()).getValue());
        result.setProjectType(projectDO.getProjectType());
        String buyerOrgName = "";
        String buyerOrgId = "";
        String supplyId = "";
        //用户只会选择一个供应商
        List<SupplierInfoDO> supplierInfos = supplierInfoMapper.selectList(new LambdaQueryWrapperX<SupplierInfoDO>().in(SupplierInfoDO::getSupplierId, reqVO.getSupplierIdList()).eq(SupplierInfoDO::getPackageId, reqVO.getPackageId()));
        SupplierInfoDO supplierInfoDO = new SupplierInfoDO();
        if (CollectionUtil.isNotEmpty(supplierInfos)) {
            supplierInfoDO = supplierInfos.get(0);
            List<PackageDetailInfoDO> packageDetailInfoList = packageDetailInfoMapper.selectList(PackageDetailInfoDO::getPackageGuid, reqVO.getPackageId());
            if (CollectionUtil.isNotEmpty(packageDetailInfoList)) {
                List<String> detailIds = CollectionUtils.convertList(packageDetailInfoList, PackageDetailInfoDO::getDetailId);
                if (CollectionUtil.isNotEmpty(detailIds)) {
                    bidConfirmQuotationDetails = bidConfirmQuotationDetailMapper.selectList(BidConfirmQuotationDetailDO::getPackageDetailId, detailIds);
                    result.setBidConfirmDetailList(bidConfirmQuotationDetails);
                    if (CollectionUtil.isNotEmpty(bidConfirmQuotationDetails)) {
                        quotationDetailDOBySupNameMap = CollectionUtils.convertMultiMap(bidConfirmQuotationDetails, BidConfirmQuotationDetailDO::getSupplierName);
                    }
                }// 包-》包明细-》明细id-》报价明细
            }
            supplyId = supplierInfoDO.getSupplierId();
            SupplyDTO supply = supplyApi.getSupply(supplierInfoDO.getSupplierId());
            if(ObjectUtil.isNull(supply)){
                supply = hljSupplyService.getSupply(supplierInfoDO.getSupplierId());
            }
            if (supply != null) {
                result.setSupplierName(supply.getSupplyCn());
                result.setRegisteredAddress(supply.getAddr());
                result.setSupplierLink(supply.getFax());
                result.setSupplierProxy(supply.getLegalPerson());
                result.setSupplierFax(supply.getFax());
                result.setSupplierCode(supply.getOrgCode());
                result.setSupplierOrgCode(supply.getReginCode());
                result.setSupplierLinkMobile(supply.getPersonMobile());
                SupplyBankReqVO supplyBankReqVO = new SupplyBankReqVO();
                supplyBankReqVO.setSupplyCode(reqVO.getPackageId());
                SupplyBankRespVO supplyBankRespVO = new SupplyBankRespVO();
                try {
                    supplyBankRespVO = zcdService.bankAccount(supplyBankReqVO);
                }catch (Exception e) {
                    log.error("zcdService获取金融信息异常。回填托底数据");
                    supplyBankRespVO.setBankName(supply.getBankName());
                    supplyBankRespVO.setBankAccount(supply.getBankAccount());
                }
                if (ObjectUtil.isNotEmpty(supplyBankRespVO)) {
                    if (StringUtils.isNotEmpty(supplyBankRespVO.getBankAccount())) {
                        result.setBankAccount(supplyBankRespVO.getBankAccount());
                    }
                    if (StringUtils.isNotEmpty(supplyBankRespVO.getBankName())) {
                        result.setBankName(supplyBankRespVO.getBankName());
                    }
                }
            }
        }

        PackageInfoDO packageInfoDO = packageInfoMapper.selectById(reqVO.getPackageId());
        if(ObjectUtil.isNull(packageInfoDO)){
            log.error("{}请检查采购包信息。", reqVO.getPackageId());
            throw exception(DIY_ERROR,"请检查采购包信息。");
        }

        //一般项目采购
        if (BiddingMethodEnums.COMMON.getCode().equals(packageInfoDO.getBiddingMethodCode())) {
            List<PlanInfoDO> plans = planInfoMapper.selectList(PlanInfoDO::getProjectId, packageInfoDO.getProjectGuid());
            if (CollectionUtil.isNotEmpty(plans)) {
                OrganizationDTO organization = organizationApi.getOrganization(plans.get(0).getPurchaseUnitId());
                if (organization != null) {
                    buyerOrgName = organization.getName();
                    buyerOrgId = organization.getId();
                    result.setOrgName(organization.getName());
                    result.setDeliveryAddress(organization.getAddress());
                    result.setBuyerLink(organization.getLinkFax());
                    result.setBuyerProxy(organization.getLegal());
                    result.setBuyerLinkMobile(organization.getLinkPhone());
                }
            }
            if (StringUtils.isNotEmpty(reqVO.getPlanId())) {
                PlanInfoDO planInfoDO = planInfoMapper.selectOne(new LambdaQueryWrapperX<PlanInfoDO>().eq(PlanInfoDO::getPlanId, reqVO.getPlanId()).last(" limit 1"));
                result.setPlanCode(planInfoDO.getPlanCode());
            }
            //中标供应商的报价明细的总和
            List<BidConfirmQuotationDetailDO> quotationDetailDOS = quotationDetailDOBySupNameMap.get(supplierInfoDO.getSupplierName());
            if (CollectionUtil.isNotEmpty(quotationDetailDOS)) {
                try {
                    BigDecimal reduce = quotationDetailDOS.stream().map(quotationDetailDO -> new BigDecimal(quotationDetailDO.getTotalPrice()))
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    result.setTotalMoney(reduce.doubleValue());
                } catch (Exception e) {
                    Double totalPrice4Quotations = quotationDetailDOS.stream()
                            .map(BidConfirmQuotationDetailDO::getTotalPrice) // 获取所有价格字符串
                            .mapToDouble(priceStr -> {
                                try {
                                    return Double.parseDouble(priceStr); // 转换为 Double
                                } catch (NumberFormatException ex) {
                                    return 0.0; // 处理解析错误
                                }
                            })
                            .sum();
                    result.setTotalMoney(Double.valueOf(totalPrice4Quotations));
                }
            }

            //如果是一般项目多供应商，这显示采购内容
            if (2 == result.getSupplierType() && ObjectUtil.isNotNull(supplierInfoDO)) {
                List<GPXContractQuotationRelRespVO> quotationRelRespVOList = new ArrayList<>();
                List<BidConfirmQuotationDetailDO> quotationDetailDOList = bidConfirmQuotationDetailMapper.selectList(BidConfirmQuotationDetailDO::getSupplierName, supplierInfoDO.getSupplierName());
                Map<String, BidConfirmQuotationDetailDO> quotationDetailDOByPackageDetailIdMap = new HashMap<>();
                if (CollectionUtil.isNotEmpty(quotationDetailDOList)) {
                    quotationDetailDOByPackageDetailIdMap = CollectionUtils.convertMap(quotationDetailDOList, BidConfirmQuotationDetailDO::getPackageDetailId);
                }
                List<PackageDetailInfoDO> packageDetailInfoDOList = packageDetailInfoMapper.selectList(PackageDetailInfoDO::getPackageGuid, reqVO.getPackageId());
                if (CollectionUtil.isNotEmpty(packageDetailInfoDOList)) {
                    for (PackageDetailInfoDO packageDetailInfoDO : packageDetailInfoDOList) {
                        BidConfirmQuotationDetailDO quotationDetailDO = quotationDetailDOByPackageDetailIdMap.get(packageDetailInfoDO.getDetailId());
                        if (ObjectUtil.isNotNull(quotationDetailDO)) {
                            GPXContractQuotationRelRespVO respVO = GPXConverter.INSTANCE.quoD2R(quotationDetailDO);
                            respVO.setCatalogueCode(packageDetailInfoDO.getCatalogueCode());
                            respVO.setCatalogueName(packageDetailInfoDO.getCatalogueName());
                            respVO.setDetailName(packageDetailInfoDO.getDeatilName());
                            quotationRelRespVOList.add(respVO);
                        }
                    }
                }
                result.setQuotationRelRespVOList(quotationRelRespVOList);
            }

        }
        //批量集中采购
        if (BiddingMethodEnums.BATCH.getCode().equals(packageInfoDO.getBiddingMethodCode()) || BiddingMethodEnums.UNION.getCode().equals(packageInfoDO.getBiddingMethodCode())) {
            if (SecurityFrameworkUtils.getLoginUser() != null && UserTypeEnums.PURCHASER_ORGANIZATION.getCode().equals(SecurityFrameworkUtils.getLoginUser().getType())) {
                OrganizationDTO organization = organizationApi.getOrganization(SecurityFrameworkUtils.getLoginUser().getOrgId());
                buyerOrgName = organization.getName();
                buyerOrgId = organization.getId();
                result.setOrgName(organization.getName());
                result.setDeliveryAddress(organization.getAddress());
                result.setBuyerLink(organization.getLinkFax());
                result.setBuyerProxy(organization.getName());
                result.setBuyerLinkMobile(organization.getLinkPhone());
            }
            result.setTotalMoney(packageInfoDO.getWinBidAmount());
        }
        result.setSupplierCombinationList(getSupplierInfoList(reqVO));

        result.setBidCode(packageInfoDO.getProjectCode());
        result.setProjectCode(packageInfoDO.getProjectCode());
        result.setProjectName(packageInfoDO.getProjectName());
        result.setAmount(packageInfoDO.getAmount());
//        result.setTotalMoney(packageInfoDO.getWinBidAmount());
        result.setPurchaseMethodCode(packageInfoDO.getPurchaseMethodCode());
        result.setPurchaseMethodName(packageInfoDO.getPurchaseMethodName());


        List<ModelIdVO> modelIdByOrderCode = new ArrayList<>();
        Boolean needCategory = IfEnums.YES.getCode().equals(systemConfigApi.getConfigByKey(IF_NEED_MODEL_CATEGORY.getKey()));

        if(needCategory){
            modelIdByOrderCode = purCatalogService.getModelIdByOrderCode(reqVO.getPackageId(), null, packageInfoDO.getProjectType(), null, buyerOrgName, buyerOrgId);
        }else {
            //不需要模板分类
             modelIdByOrderCode =  getModel4Package(packageInfoDO);

        }
        if (CollectionUtil.isNotEmpty(modelIdByOrderCode)) {
            List<String> idList = modelIdByOrderCode.stream().map(ModelIdVO::getModelId).collect(Collectors.toList());
            result.setTemplateIdList(idList);
        }

        //联合采购
        if (BiddingMethodEnums.UNION.getCode().equals(projectDO.getBiddingMethodCode())) {
            //采购内容
            List<GPXContractQuotationRelRespVO> quotationRelRespVOList = new ArrayList<>();
            List<BidConfirmQuotationDetailDO> quotationDetailDOList = bidConfirmQuotationDetailMapper.selectList4Union(reqVO.getPackageId());
            Map<String, BidConfirmQuotationDetailDO> quotationDetailDOByPackageDetailIdMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(quotationDetailDOList)) {
                quotationDetailDOByPackageDetailIdMap = CollectionUtils.convertMap(quotationDetailDOList, BidConfirmQuotationDetailDO::getPackageDetailId);
            }
            List<PackageDetailInfoDO> packageDetailInfoDOList = packageDetailInfoMapper.selectList(PackageDetailInfoDO::getPackageGuid, reqVO.getPackageId());
            if (CollectionUtil.isNotEmpty(packageDetailInfoDOList)) {
                for (PackageDetailInfoDO packageDetailInfoDO : packageDetailInfoDOList) {
                    BidConfirmQuotationDetailDO quotationDetailDO = quotationDetailDOByPackageDetailIdMap.get(packageDetailInfoDO.getDetailId());
                    if (ObjectUtil.isNotNull(quotationDetailDO)) {
                        GPXContractQuotationRelRespVO respVO = GPXConverter.INSTANCE.quoD2R(quotationDetailDO);
                        respVO.setCatalogueCode(packageDetailInfoDO.getCatalogueCode());
                        respVO.setCatalogueName(packageDetailInfoDO.getCatalogueName());
                        respVO.setDetailName(packageDetailInfoDO.getDeatilName());
                        quotationRelRespVOList.add(respVO);
                    }
                }
            }
            result.setQuotationRelRespVOList(quotationRelRespVOList);

            //计划明细
            List<PlanDetailInfoRespVO> planDetailInfoRespVOList = new ArrayList<>();
            List<PlanDetailInfoDO> planDetailInfoDOList = planDetailMapper.selectList(PlanDetailInfoDO::getPlanId, reqVO.getPlanId());
            if (CollectionUtil.isNotEmpty(planDetailInfoDOList)) {
                planDetailInfoRespVOList = GPXConverter.INSTANCE.listPlanDetailD2R(planDetailInfoDOList);
                result.setPlanDetailInfoRespVOList(planDetailInfoRespVOList);
            }
        }

        //从历史合同中查找采购人和供应商信息
        buildEmptyFileds(result,supplyId);
        
        return result;
    }

    private List<ModelIdVO> getModel4Package(PackageInfoDO packageInfoDO) {
        // 1、匹配当前起草订单所属合同类型，与当前采购包或订单相同交易类型(电子交易/框架协议/协议定点!.)的模板，如果没有，再匹配通用类模板。
        //    优先匹配增加品目的模板
        List<SimpleModel> simpleModels = simpleModelMapper.getModel4PackageCatalog(packageInfoDO);
        if (CollectionUtil.isEmpty(simpleModels)) {
            // 2、如果没找到，则找没品目的模板(模板配的品目中包含当前起草订单/采购包的品目)
            simpleModels = simpleModelMapper.getModel4PackageNoCatalog(packageInfoDO);
        }
        if (CollectionUtil.isEmpty(simpleModels)) {
            // 3、如果都没找到，则找通用类
            simpleModels = simpleModelMapper.getModel4PackageAllPlatform(packageInfoDO);
        }
        List<ModelIdVO> modelIdVOList = new ArrayList<>();
        for (SimpleModel simpleModel : simpleModels) {
            ModelIdVO modelIdVO = new ModelIdVO()
                    .setModelId(simpleModel.getId())
                    .setModelCode(simpleModel.getCode())
                    .setModelName(simpleModel.getName());
            modelIdVOList.add(modelIdVO);
        }
        return modelIdVOList;
    }

    private void buildEmptyFileds(GPXContractPlaybackV3RespVO gpxContractPlaybackV3RespVO,String supplyId){
        LambdaQueryWrapperX<TradingContractExtDO> lambdaQueryWrapperX = new LambdaQueryWrapperX();
        lambdaQueryWrapperX.select(TradingContractExtDO::getBuyerOrgName,TradingContractExtDO::getBuyerLegalPerson,
                TradingContractExtDO::getBuyerPhone,TradingContractExtDO::getOrgBankAccount,TradingContractExtDO::getOrgBankName,
                TradingContractExtDO::getBuyerBankAccountName,TradingContractExtDO::getOrgTaxpayerNum,TradingContractExtDO::getOrgAddress,TradingContractExtDO::getOrgFax,
                //供应商基础信息
                TradingContractExtDO::getSupplierProxy,TradingContractExtDO::getSupplierLinkMobile
        );
        lambdaQueryWrapperX.orderByDesc(TradingContractExtDO::getCreateTime);
        lambdaQueryWrapperX.last("limit 1");
        List<TradingContractExtDO> tradingContractExtDOList =  tradingContractExtMapper.selectList(lambdaQueryWrapperX);
        if (CollectionUtil.isNotEmpty(tradingContractExtDOList)) {
            TradingContractExtDO tradingContractExtDO = tradingContractExtDOList.get(0);
            if(tradingContractExtDO == null ){
                return;
            }
            // 
            if(StringUtils.isEmpty(gpxContractPlaybackV3RespVO.getBuyerProxy()) && StringUtils.isNotEmpty(tradingContractExtDO.getBuyerLegalPerson())){
                gpxContractPlaybackV3RespVO.setBuyerProxy(tradingContractExtDO.getBuyerLegalPerson());
            }
            if(StringUtils.isEmpty(gpxContractPlaybackV3RespVO.getBuyerLinkMobile()) && StringUtils.isNotEmpty(tradingContractExtDO.getBuyerPhone())){
                gpxContractPlaybackV3RespVO.setBuyerLinkMobile(tradingContractExtDO.getBuyerPhone());
            }
            if(StringUtils.isEmpty(gpxContractPlaybackV3RespVO.getOrgBankAccount()) && StringUtils.isNotEmpty(tradingContractExtDO.getOrgBankAccount())){
                gpxContractPlaybackV3RespVO.setOrgBankAccount(tradingContractExtDO.getOrgBankAccount());
            }
            if(StringUtils.isEmpty(gpxContractPlaybackV3RespVO.getOrgBankName()) && StringUtils.isNotEmpty(tradingContractExtDO.getOrgBankName())){
                gpxContractPlaybackV3RespVO.setOrgBankName(tradingContractExtDO.getOrgBankName());
            }
            if(StringUtils.isEmpty(gpxContractPlaybackV3RespVO.getBuyerBankAccountName()) && StringUtils.isNotEmpty(tradingContractExtDO.getBuyerBankAccountName())){
                gpxContractPlaybackV3RespVO.setBuyerBankAccountName(tradingContractExtDO.getBuyerBankAccountName());
            }
            if(StringUtils.isEmpty(gpxContractPlaybackV3RespVO.getOrgTaxpayerNum()) && StringUtils.isNotEmpty(tradingContractExtDO.getOrgTaxpayerNum())){
                gpxContractPlaybackV3RespVO.setOrgTaxpayerNum(tradingContractExtDO.getOrgTaxpayerNum());
            }
            if(StringUtils.isEmpty(gpxContractPlaybackV3RespVO.getOrgFax()) && StringUtils.isNotEmpty(tradingContractExtDO.getOrgFax())){
                gpxContractPlaybackV3RespVO.setOrgFax(tradingContractExtDO.getOrgFax());
            }
            if(StringUtils.isEmpty(gpxContractPlaybackV3RespVO.getOrgAddress()) && StringUtils.isNotEmpty(tradingContractExtDO.getOrgAddress())){
                gpxContractPlaybackV3RespVO.setOrgAddress(tradingContractExtDO.getOrgAddress());
            }
            if(StringUtils.isEmpty(gpxContractPlaybackV3RespVO.getSupplierProxy()) && StringUtils.isNotEmpty(tradingContractExtDO.getSupplierProxy())){
                gpxContractPlaybackV3RespVO.setSupplierProxy(tradingContractExtDO.getSupplierProxy());
            }
        }
        /**
         * 由于采购单位分租户，此处查询的供应商肯定是当前单位的供应商，只需要根据供应商id查询即可
         */
        LambdaQueryWrapperX<TradingSupplierDO> lambdaQueryWrapperX2 = new LambdaQueryWrapperX();
        lambdaQueryWrapperX2.eq(TradingSupplierDO::getSupplierId, supplyId).orderByDesc(TradingSupplierDO::getCreateTime).last(" limit 1 ");
        List<TradingSupplierDO> contractSupplierDOS = tradingSupplierMapper.selectList(lambdaQueryWrapperX2);
        if(CollectionUtil.isNotEmpty(contractSupplierDOS)) {
            TradingSupplierDO tradingSupplierDO = contractSupplierDOS.get(0);
            
            if(StringUtils.isEmpty(gpxContractPlaybackV3RespVO.getSupplierProxy()) && StringUtils.isNotEmpty(tradingSupplierDO.getLegalRepresentative())){
                gpxContractPlaybackV3RespVO.setSupplierProxy(tradingSupplierDO.getLegalRepresentative());
            }
            if(StringUtils.isEmpty(gpxContractPlaybackV3RespVO.getSupplierLinkMobile()) && StringUtils.isNotEmpty(tradingSupplierDO.getSupplierLinkMobile())){
                gpxContractPlaybackV3RespVO.setSupplierLinkMobile(tradingSupplierDO.getSupplierLinkMobile());
            }
            
            if(StringUtils.isEmpty(gpxContractPlaybackV3RespVO.getBankName()) && StringUtils.isNotEmpty(tradingSupplierDO.getBankName())){
                gpxContractPlaybackV3RespVO.setBankName(tradingSupplierDO.getBankName());
            }
            if(StringUtils.isEmpty(gpxContractPlaybackV3RespVO.getBankAccount()) && StringUtils.isNotEmpty(tradingSupplierDO.getBankAccount())){
                gpxContractPlaybackV3RespVO.setBankAccount(tradingSupplierDO.getBankAccount());
            }
            if(StringUtils.isEmpty(gpxContractPlaybackV3RespVO.getSupplierBankAccountName()) && StringUtils.isNotEmpty(tradingSupplierDO.getPayPlanbAccount())){
                gpxContractPlaybackV3RespVO.setSupplierBankAccountName(tradingSupplierDO.getPayPlanbAccount());
            }
            
            if(ObjectUtil.isEmpty(gpxContractPlaybackV3RespVO.getSupplierSize()) && ObjectUtil.isNotEmpty(tradingSupplierDO.getSupplierSize())){
                gpxContractPlaybackV3RespVO.setSupplierSize(tradingSupplierDO.getSupplierSize());
            }
            if(StringUtils.isEmpty(gpxContractPlaybackV3RespVO.getSupplierOrgCode()) && StringUtils.isNotEmpty(tradingSupplierDO.getSupplierLocation())){
                gpxContractPlaybackV3RespVO.setSupplierOrgCode(tradingSupplierDO.getSupplierLocation());
            }
            if(ObjectUtil.isEmpty(gpxContractPlaybackV3RespVO.getSupplierFeatures()) && ObjectUtil.isNotEmpty(tradingSupplierDO.getSupplierFeatures())){
                gpxContractPlaybackV3RespVO.setSupplierFeatures(tradingSupplierDO.getSupplierFeatures());
            }
            if(StringUtils.isEmpty(gpxContractPlaybackV3RespVO.getSupplierCode()) && StringUtils.isNotEmpty(tradingSupplierDO.getSupplierTaxpayerNum())){
                gpxContractPlaybackV3RespVO.setSupplierCode(tradingSupplierDO.getSupplierTaxpayerNum());
            }

            if(StringUtils.isEmpty(gpxContractPlaybackV3RespVO.getRegisteredAddress()) && StringUtils.isNotEmpty(tradingSupplierDO.getRegisteredAddress())){
                gpxContractPlaybackV3RespVO.setRegisteredAddress(tradingSupplierDO.getRegisteredAddress());
            }
            
        }
        
        
    }
    /**
     * 获取联合体采购人信息
     *
     * @param reqVO
     * @return
     */
    @Override
    public List<SupplierCombinationInfoVO> getSupplierInfoList(PlaybackReqVO reqVO) throws Exception {
        //添加供应商信息
        //联合体
        if (ObjectUtil.isNotEmpty(reqVO.getSupplierIdList())) {
            List<SupplierCombinationInfoDO> supplierList = supplierCombinationInfoMapper.selectList(
                    new LambdaQueryWrapperX<SupplierCombinationInfoDO>().eq(SupplierCombinationInfoDO::getMainSupplierId, reqVO.getSupplierIdList().get(0))
                            .neIfPresent(SupplierCombinationInfoDO::getSupplierId, reqVO.getSupplierIdList().get(0)));
            if (CollectionUtil.isNotEmpty(supplierList)) {
                List<SupplierCombinationInfoVO> supplierCombinationInfoVOList = GPXConverter.INSTANCE.supplierCombinationList2VO(supplierList);
                //添加供应商信息
                List<String> supplierIds = CollectionUtils.convertList(supplierCombinationInfoVOList, SupplierCombinationInfoVO::getSupplierId);
                List<SupplyDTO> supplierDTOs = hljSupplyService.getSupplyList(supplierIds);
                Map<String, SupplyDTO> supplyMap = CollectionUtils.convertMap(supplierDTOs, SupplyDTO::getId);
                supplierCombinationInfoVOList.forEach(itemVo -> {
                    SupplyDTO supply = supplyMap.get(itemVo.getSupplierId());
                    if (supply != null) {
                        BeanUtils.copyProperties(supply, itemVo);
                        SupplyBankRespVO supplyBankRespVO = null;
                        try {
                            supplyBankRespVO = zcdService.bankAccount(new SupplyBankReqVO().setSupplyCode(supply.getOrgCode()).setPackageCode(reqVO.getPackageId()));
                        } catch (Exception e) {
                            log.error("获取供应商银行账号异常", e);
                        }
                        if (ObjectUtil.isNotEmpty(supplyBankRespVO) && ObjectUtil.isNotEmpty(supplyBankRespVO.getBankAccount())) {
                            itemVo.setBankAccount(supplyBankRespVO.getBankAccount());
                        }
                        if (ObjectUtil.isNotEmpty(supplyBankRespVO) && ObjectUtil.isNotEmpty(supplyBankRespVO.getBankName())) {
                            itemVo.setBankName(supplyBankRespVO.getBankName());
                        }
                    }
                });
                return supplierCombinationInfoVOList;
            }
        }
        return Collections.emptyList();
    }




    @Override
    public void checkPlan(String packageId, String contractId) {
        List<Integer> statusList = new ArrayList<>();
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_TOBECONFIRMED.getCode());
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_TOBESENT.getCode());
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_SURE.getCode());
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_SURE2.getCode());
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_DELIVERED.getCode());
        statusList.add(HLJContractStatusEnums.BUYER_SIGNED.getCode());
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECEIVED.getCode());
        statusList.add(HLJContractStatusEnums.TO_BE_CONFIRMED_BY_AGENCY.getCode());
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode());
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode());
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode());
        List<TradingContractExtDO> tradingContracts = tradingContractExtMapper.selectList(new LambdaQueryWrapperX<TradingContractExtDO>()
                .eq(TradingContractExtDO::getBuyPlanPackageId, packageId).select(TradingContractExtDO::getId, TradingContractExtDO::getStatus));
        List<TradingContractExtDO> targetContracts = tradingContracts.stream().filter(item -> (StringUtils.isNotEmpty(contractId) && contractId.equals(item.getId())) || statusList.contains(item.getStatus())).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(targetContracts)) {
            throw exception(EXIST_DRAFT);
        }
    }
}
