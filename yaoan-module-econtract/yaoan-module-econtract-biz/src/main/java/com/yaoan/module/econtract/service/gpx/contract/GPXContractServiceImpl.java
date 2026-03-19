package com.yaoan.module.econtract.service.gpx.contract;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.module.econtract.api.contract.ContractProcessApi;
import com.yaoan.module.econtract.api.contract.dto.ContractArchiveStateRespDTO;
import com.yaoan.module.econtract.api.contract.dto.ContractFileDTO;
import com.yaoan.module.econtract.api.contract.dto.PackageUpdateDTO;
import com.yaoan.module.econtract.api.contract.dto.mongolia.EncryptResponseDto;
import com.yaoan.module.econtract.api.gcy.buyplan.MedicalApi;
import com.yaoan.module.econtract.api.gcy.buyplan.OrgApi;
import com.yaoan.module.econtract.api.gcy.buyplan.SuperVisionApi;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.ContractArchiveStateDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.DeleteContractRequestDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.TokenRequestDTO;
import com.yaoan.module.econtract.controller.admin.aop.service.OutOpenApiService;
import com.yaoan.module.econtract.controller.admin.contract.vo.SignatoryRelReqVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.CommentCreateRespVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractCreateReqVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractProjectVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractRespVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading.PurchaseVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading.TradingSupplierVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.GPXContractQuotationRelRespVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.PlanDetailInfoRespVO;
import com.yaoan.module.econtract.controller.admin.model.vo.ModelSingleRespVo;
import com.yaoan.module.econtract.controller.admin.model.vo.TermsDetailsVo;
import com.yaoan.module.econtract.controller.admin.supervise.vo.PaymentPlanVO;
import com.yaoan.module.econtract.convert.contract.ContractConverter;
import com.yaoan.module.econtract.convert.contract.PaymentScheduleConverter;
import com.yaoan.module.econtract.convert.contract.ext.gcy.ContractParamFieldConverter;
import com.yaoan.module.econtract.convert.contract.ext.gxp.TradingContractExtConverter;
import com.yaoan.module.econtract.convert.contract.trading.PurchaseConverter;
import com.yaoan.module.econtract.convert.contract.trading.TradingSupplierConverter;
import com.yaoan.module.econtract.convert.gcy.gpmall.CommentConverter;
import com.yaoan.module.econtract.convert.gcy.gpmall.ContractFileConverter;
import com.yaoan.module.econtract.convert.gcy.gpmall.ContractFileMapper;
import com.yaoan.module.econtract.convert.gcy.gpmall.ContractPaymentPlayConverter;
import com.yaoan.module.econtract.convert.gpx.GPXConverter;
import com.yaoan.module.econtract.convert.term.ContractTermConverter;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractObjectDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.OrderContractParamFieldDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.TradingContractExtDO;
import com.yaoan.module.econtract.dal.dataobject.contract.trading.PurchaseDO;
import com.yaoan.module.econtract.dal.dataobject.contract.trading.TradingSupplierDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.CommentDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.ContractFileDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.ContractGoodsDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.*;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.dataobject.statistics.StatisticsDO;
import com.yaoan.module.econtract.dal.dataobject.term.ContractTermDO;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import com.yaoan.module.econtract.dal.dataobject.term.ContractTermDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractObjectMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contract.TradingContractExtMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractParamFieldMapper;
import com.yaoan.module.econtract.dal.mysql.contract.trading.ContractCancellationMapper;
import com.yaoan.module.econtract.dal.mysql.contract.trading.PurchaseMapper;
import com.yaoan.module.econtract.dal.mysql.contract.trading.TradingSupplierMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.gcy.gpmall.CommentMapper;
import com.yaoan.module.econtract.dal.mysql.gcy.gpmall.ContractGoodsMapper;
import com.yaoan.module.econtract.dal.mysql.gpx.*;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import com.yaoan.module.econtract.dal.mysql.statistics.StatisticsMapper;
import com.yaoan.module.econtract.dal.mysql.term.ContractTermMapper;
import com.yaoan.module.econtract.dal.mysql.term.TermMapper;
import com.yaoan.module.econtract.dal.mysql.term.ContractTermMapper;
import com.yaoan.module.econtract.enums.AmountTypeEnums;
import com.yaoan.module.econtract.enums.BusinessTokenConfigEnums;
import com.yaoan.module.econtract.enums.ContractUploadTypeEnums;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.gcy.gpmall.ContractArchiveStateEnums;
import com.yaoan.module.econtract.enums.gcy.gpmall.HLJContractStatusEnums;
import com.yaoan.module.econtract.enums.gpx.BiddingMethodEnums;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import com.yaoan.module.econtract.enums.order.ProjectCategoryEnums;
import com.yaoan.module.econtract.enums.supervise.CountryTypeEnum;
import com.yaoan.module.econtract.enums.supervise.ForeignInvestmentTypeEnum;
import com.yaoan.module.econtract.enums.supervise.SupplierFeaturesEnum;
import com.yaoan.module.econtract.enums.supervise.SupplierSizeEnum;
import com.yaoan.module.econtract.service.contract.ContractService;
import com.yaoan.module.econtract.service.gcy.buyplan.EcmsGcyBuyPlanServiceImpl;
import com.yaoan.module.econtract.service.gpmall.GPMallContractService;
import com.yaoan.module.econtract.service.gpx.GPXService;
import com.yaoan.module.econtract.service.hljsupply.HLJSupplyService;
import com.yaoan.module.econtract.service.model.ModelService;
import com.yaoan.module.econtract.util.WkHtmlToPdfManager;
import com.yaoan.module.econtract.util.gcy.Sm4Utils;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.enums.FileUploadPathEnum;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.config.dto.SystemConfigRespDTO;
import com.yaoan.module.system.api.region.RegionApi;
import com.yaoan.module.system.api.region.dto.RegionDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.OrganizationApi;
import com.yaoan.module.system.api.user.SupplyApi;
import com.yaoan.module.system.api.user.dto.OrganizationDTO;
import com.yaoan.module.system.api.user.dto.SupplyDTO;
import com.yaoan.module.system.enums.config.SystemConfigKeyEnums;
import com.yaoan.module.system.service.user.SupplyService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.OutServiceExceptionUtil.outException;
import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.EMPTY_DATA_ERROR_V2;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY;
import static com.yaoan.module.econtract.enums.StatusConstants.*;
import static com.yaoan.module.infra.enums.ErrorCodeConstants.DIY_ERROR;

@Service
@Slf4j
public class GPXContractServiceImpl implements GPXContractService {
    public static final String TRADING_PROCESS_KEY = "gpmall_contract_process";

    @Resource
    private ContractFileMapper contractFileMapper;

    @Resource
    private ContractGoodsMapper contractGoodsMapper;
    @Resource
    private PackageInfoMapper packageInfoMapper;
    @Resource
    private TradingContractExtMapper tradingContractExtMapper;
    @Resource
    private OrganizationApi orgApi;
    @Resource
    private GPXProjectMapper gpxProjectMapper;
    @Resource
    private GPXService gpxService;
    @Resource
    private PlanInfoMapper planInfoMapper;
    @Resource
    private BatchPlanInfoMapper batchPlanInfoMapper;
    @Resource
    private PackageDetailInfoMapper packageDetailInfoMapper;
    @Resource
    private PurchaseMapper purchaseMapper;
    @Resource
    private TradingSupplierMapper tradingSupplierMapper;
    @Resource
    private ContractParamFieldMapper contractParamFieldMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private ContractService contractService;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private GPXContractQuotationRelMapper gpxContractQuotationRelMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private SupplierInfoMapper supplierInfoMapper;
    @Resource
    private BidConfirmQuotationDetailMapper bidConfirmQuotationDetailMapper;
    @Resource
    private HLJSupplyService hljSupplyService;
    @Resource
    private WkHtmlToPdfManager wkHtmlToPdfManager;
    @Resource
    private ModelService modelService;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private RegionApi regionApi;
    @Resource
    private PlanDetailInfoMapper planDetailInfoMapper;
    @Resource
    private SuperVisionApi superVisionApi;
    @Resource
    private OutOpenApiService outOpenApiService;
    @Resource
    private EcmsGcyBuyPlanServiceImpl gcyBuyPlanService;
    @Resource
    private OrgApi gpxorgApi;
    @Autowired
    private MedicalApi medicalApi;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private StatisticsMapper statisticsMapper;
    @Resource
    private FileApi fileApi;
    @Resource
    private ContractCancellationMapper contractCancellationMapper;
    @Resource
    private SignatoryRelMapper signatoryRelMapper;
    @Resource
    private ContractProcessApi contractProcessApi;
    @Value("${feign.client.contract.client_id}")
    private String clientId;
    @Value("${feign.client.contract.client_secret}")
    private String clientSecret;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private RelativeMapper relativeMapper;
    @Resource
    private OrganizationApi organizationApi;
    @Resource
    private SupplyApi supplyApi;
    @Resource
    private ContractTermMapper contractTermMapper;
    @Resource
    private TermMapper termMapper;
    @Resource
    private ContractObjectMapper contractObjectMapper;
    @Override
    public String create(GPXContractCreateReqVO contractCreateReqVO) throws Exception {
        //校验验收比例
        if (StringUtils.isNotEmpty(contractCreateReqVO.getSpotCheckProportion()) && !StringUtils.isNumeric(contractCreateReqVO.getSpotCheckProportion())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "抽查检测比例必须为数字");
        }
        //校验合同金额
        List<TradingContractExtDO> existContractDOList = tradingContractExtMapper.getContractsPackageId(contractCreateReqVO);
        BigDecimal totalPackageMoney = new BigDecimal(0);

        if (CollectionUtil.isNotEmpty(existContractDOList)) {
            totalPackageMoney = existContractDOList.stream()
                    .map(TradingContractExtDO::getTotalMoney)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        totalPackageMoney = totalPackageMoney.add(contractCreateReqVO.getTotalMoney());



        List<GPXContractQuotationRelDO> contractQuotationRelDOList = new ArrayList<GPXContractQuotationRelDO>();
        String packageId = contractCreateReqVO.getPackageId();
        String planId = contractCreateReqVO.getPlanId();
        String projectId = contractCreateReqVO.getProjectId();
        TradingContractExtDO contractDO = TradingContractExtConverter.INSTANCE.toEntity(contractCreateReqVO);
        //添加区划
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        contractDO.setContractDrafter(loginUser.getType());
        String regionCode = "";
        if (ObjectUtil.isNotEmpty(loginUser.getRegionCode())) {
            regionCode = loginUser.getRegionCode();
        } else {
            regionCode = orgApi.getOrgRegionCodeByOrgId(loginUser.getOrgId());
        }
        if (StringUtils.isNotEmpty(regionCode)) {
            contractDO.setRegionCode(regionCode);
        }
        if (StringUtils.isEmpty(contractDO.getId())) {
            GPXProjectDO projectDO = gpxProjectMapper.selectOne(GPXProjectDO::getProjectGuid, projectId);
            if (ObjectUtil.isNull(projectDO)) {
                throw exception(EMPTY_DATA_ERROR_V2, "采购项目");
            }
            gpxService.checkPlan(contractCreateReqVO.getBuyPlanId(), null);
//            tradingContractExtMapper.insert(contractDO);
            contractDO.setId(IdUtil.fastSimpleUUID());
            //合同类型=项目类型
            contractDO.setContractType(ProjectCategoryEnums.getInstance(projectDO.getProjectType()).getValue());

            contractDO.setProjectCategoryCode(ProjectCategoryEnums.getInstance(projectDO.getProjectType()).getCode());
            contractDO.setBuyPlanId(planId);
            contractDO.setProjectGuid(projectId);
            contractDO.setBidGuid(packageId);
            contractDO.setPlatform(PlatformEnums.GPMS_GPX.getCode());

            contractDO.setBuyPlanId(planId);
            contractDO.setBuyPlanPackageId(packageId);
            if (ObjectUtil.isEmpty(contractCreateReqVO.getSourceCode())) {
                throw exception(EMPTY_DATA_ERROR_V2, "计划来源编码");
            } else {
                contractDO.setSourceCode(contractCreateReqVO.getSourceCode());
            }
            //编辑
            //校验名称,编码是否重复
            //保存文件富文本
            String content = contractCreateReqVO.getContent();
            if (StringUtils.isNotEmpty(content)) {
                contractDO.setContractContent(content.getBytes());
            }
            //添加新附件
            if (CollectionUtil.isNotEmpty(contractCreateReqVO.getContractFileDTOList())) {
                List<ContractFileDO> dos = ContractFileConverter.INSTANCE.toDOS(contractCreateReqVO.getContractFileDTOList());
                for (ContractFileDO aDo : dos) {
                    aDo.setContractId(contractDO.getId());
                }
                contractFileMapper.insertBatch(dos);
            }
            //添加甲方信息
            BiddingMethodEnums instance = BiddingMethodEnums.getInstance(projectDO.getBiddingMethodCode());
            if (BiddingMethodEnums.COMMON == instance) {
                PlanInfoDO planInfoDO = planInfoMapper.selectOne(PlanInfoDO::getPlanId, planId);
                if (ObjectUtil.isNotNull(planInfoDO)) {
                    //单位id保存合同表，流程执行人使用
                    contractDO.setBuyerOrgId(planInfoDO.getPurchaseUnitId());
                    contractDO.setBuyerOrgName(planInfoDO.getPurchaseUnitName());
                    contractDO.setRegionCode(planInfoDO.getZoneCode());
                    contractDO.setRegionName(planInfoDO.getZoneName());
                }
            }
            BatchPlanInfoDO batchPlanInfoDO = new BatchPlanInfoDO();
            if (BiddingMethodEnums.UNION == instance || BiddingMethodEnums.BATCH == instance) {
                batchPlanInfoDO = batchPlanInfoMapper.selectOne(BatchPlanInfoDO::getPlanId, planId);
                if (ObjectUtil.isNotNull(batchPlanInfoDO)) {
                    //单位id保存合同表，流程执行人使用
                    contractDO.setBuyerOrgId(batchPlanInfoDO.getPurchaserId());
                    contractDO.setBuyerOrgName(batchPlanInfoDO.getPurchaserName());
                    contractDO.setRegionCode(batchPlanInfoDO.getZoneCode());
                    contractDO.setRegionName(batchPlanInfoDO.getZoneName());
                }
            }
            //绑定采购内容集合
            List<PackageDetailInfoDO> packageDetailInfoDOList = packageDetailInfoMapper.selectList(PackageDetailInfoDO::getPackageGuid, packageId);
            if (CollectionUtil.isEmpty(packageDetailInfoDOList)) {
                throw exception(EMPTY_DATA_ERROR_V2, "采购包明细");
            }
            List<PurchaseVO> purchaseVOList = PurchaseConverter.INSTANCE.convertList(packageDetailInfoDOList);
            List<PurchaseDO> purchaseDOList = PurchaseConverter.INSTANCE.convertListV1(purchaseVOList);
            purchaseDOList.forEach(purchaseDO -> purchaseDO.setContractId(contractDO.getId()));
            purchaseMapper.insertBatch(purchaseDOList);
            //添加新供应商信息
            List<TradingSupplierDO> tradingSupplierDOList = TradingSupplierConverter.INSTANCE.convertLsit(contractCreateReqVO.getTradingSupplierVOList());
            tradingSupplierDOList.forEach(tradingSupplierDO -> tradingSupplierDO.setContractId(contractDO.getId()));
            tradingSupplierMapper.insertBatch(tradingSupplierDOList);
            //供应商id保存合同表，流程执行人使用
            contractDO.setSupplierId(tradingSupplierDOList.get(0).getSupplierId());
            contractDO.setSupplierName(tradingSupplierDOList.get(0).getSupplierName());
            tradingContractExtMapper.insert(contractDO);
            ContractDO contract = ContractConverter.INSTANCE.tradingExt2DO(contractDO);
            //转成产品的合同类型数据
            ProjectCategoryEnums projectCategoryEnums = ProjectCategoryEnums.getInstance(projectDO.getProjectType());
            if(ObjectUtil.isNotNull(projectCategoryEnums)){
                List<ContractType> contractTypes=  contractTypeMapper.selectList(ContractType::getPlatId,String.valueOf(projectCategoryEnums.getValue()));
                if(CollectionUtil.isNotEmpty(contractTypes)){
                    contract.setContractType(contractTypes.get(0).getId());
                }
            }
            //相对方存入合同
            List<SignatoryRelReqVO> signatoryList = new ArrayList<SignatoryRelReqVO>();
            SignatoryRelReqVO supplier = new SignatoryRelReqVO().setType(2).setInitiator(false);
            Relative relative = relativeMapper.selectById(contractDO.getSupplierId());
            if (ObjectUtil.isNotNull(relative)) {
                supplier.setSignatoryName(relative.getCompanyName()).setSignatoryId(relative.getId()).setUserId(relative.getContactId());
            }else{
                supplier.setSignatoryName(contractDO.getSupplierName()).setSignatoryId(contractDO.getSupplierId());
            }
            SignatoryRelReqVO org = new SignatoryRelReqVO().setSignatoryId(loginUser.getOrgId()).setType(1).setInitiator(true).setUserId(loginUser.getId());
            OrganizationDTO organizationDTO = organizationApi.getOrganization(loginUser.getOrgId());
            if (ObjectUtil.isNotNull(organizationDTO)) {
                org.setSignatoryName(organizationDTO.getName()).setSignatoryId(organizationDTO.getId());
            }
            //产品默认采购人是发起方
            signatoryList.add(org);
            signatoryList.add(supplier);
            handlePart(contract, signatoryList);
            // 电子交易一定是 依据已成交的采购项目或订单起草
            //政府采购合同默认金额类型：0 付款
            contract.setAmountType(AmountTypeEnums.PAY.getCode());
            contractMapper.insert(contract.setUpload(ContractUploadTypeEnums.ORDER_DRAFT.getCode()));
            //绑定相对方关系(单位端的相对方一定是供应商)
            signatoryRelMapper.insert(new SignatoryRelDO().setContractId(contractDO.getId()).setSignatoryId(contractDO.getSupplierId()).setType(2));
            //合同内容
            if (CollectionUtil.isNotEmpty(contractCreateReqVO.getQuotationRelReqVOList())) {
                contractQuotationRelDOList = GPXConverter.INSTANCE.listConQuoR2D(contractCreateReqVO.getQuotationRelReqVOList());
            }

            //联合采购-绑定采购内容
            if (BiddingMethodEnums.UNION.getCode().equals(projectDO.getBiddingMethodCode())) {
                //校验采购内容的计划
                if (CollectionUtil.isNotEmpty(contractQuotationRelDOList)) {
                    BigDecimal planAmount = new BigDecimal(0);
                    if (ObjectUtil.isNotNull(batchPlanInfoDO)) {
                        planAmount = batchPlanInfoDO.getPlanBudget();
                    }
                    BigDecimal sumPrice = new BigDecimal(0);
                    for (GPXContractQuotationRelDO quotationRelDO : contractQuotationRelDOList) {
                        if (!contractCreateReqVO.getPlanId().equals(quotationRelDO.getPlanId())) {
                            throw exception(DIY_ERROR, "关联的的计划明细不属于当前计划");
                        }
                        sumPrice = sumPrice.add(new BigDecimal(quotationRelDO.getTotalPrice()));
                    }
                    //校验采购明细总和是否超过计划金额
                    if (0 > planAmount.compareTo(sumPrice)) {
                        throw exception(DIY_ERROR, "采购内容总金额不可超过计划金额。");
                    }
                }
            }
            //如果达到包中标金额，则隐藏该订单数据
            PackageInfoDO packageInfoDO = packageInfoMapper.selectById(contractCreateReqVO.getPackageId());
            //如果是带年底合同来的合同，Hidden = 1 不进行重复发送   |   != 1  才执行发送逻辑
            if (ObjectUtil.isNotEmpty(contractDO) && packageInfoDO.getHidden() != 1) {
                if (ObjectUtil.isNotEmpty(packageInfoDO)){
                    if (0 == totalPackageMoney.compareTo(new BigDecimal(packageInfoDO.getWinBidAmount().toString()))) {
                        Map<String, Object> bodyParam = new HashMap<>();
                        bodyParam.put("client_id", clientId);
                        bodyParam.put("client_secret", clientSecret);
                        String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
                        JSONObject jsonObject = JSONObject.parseObject(token);
                        try {
                            PackageUpdateDTO packageUpdateDTO = new PackageUpdateDTO().setPackageGuid(contractDO.getBuyPlanPackageId()).setHidden(1);
                            String result = contractProcessApi.updateStatusPackage(jsonObject.getString("access_token"), packageUpdateDTO);
                            JSONObject resultJson = JSONObject.parseObject(result);
                            if (!"0".equals(resultJson.getString("code"))) {
                                throw new RuntimeException(result);
                            }
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                            log.error("推送电子合同更新包隐藏状态失败", e);
                        }
                    }
                }
            }
            try {
                // 添加合同标的信息表
                if (ObjectUtil.isNotEmpty(contractDO.getBidGuid())) {
                    List<ContractObjectDO> contractObjectDOS = new ArrayList<>();
                    List<BidConfirmQuotationDetailDO> bidConfirmQuotationDetailDOS = bidConfirmQuotationDetailMapper.selectList(BidConfirmQuotationDetailDO::getPackageId, contractDO.getBidGuid());
                    if (CollectionUtil.isNotEmpty(bidConfirmQuotationDetailDOS)) {
                        bidConfirmQuotationDetailDOS.forEach(detail -> {
                            ContractObjectDO contractObjectDO = new ContractObjectDO();
                            contractObjectDO.setContractId(contractDO.getId())
                                    .setContractId(contractDO.getId())
                                    .setOrderId(contractDO.getBidGuid())
                                    .setObjectName(detail.getCategoryName())
                                    .setBrand(detail.getBrandName())
                                    .setRegularType(detail.getModelSpecification())
                                    .setObjectUnitPrice(detail.getUnitPrice())
                                    .setObjectQuantity(detail.getCount().doubleValue())
                                    .setUnit(detail.getUnit())
                                    .setObjectAmount(detail.getTotalPrice() != null ?new BigDecimal(detail.getTotalPrice()) : null);
                            contractObjectDOS.add(contractObjectDO);
                        });
                    }
                    contractObjectMapper.insertBatch(contractObjectDOS);
                }
            } catch (Exception e) {
                System.out.println("合同标的信息表添加失败");
            }
        } else {
            //编辑
            //删除旧供应商信息
            tradingSupplierMapper.delete(new QueryWrapper<TradingSupplierDO>().eq("contract_id", contractDO.getId()));
            //添加新供应商信息
            List<TradingSupplierDO> tradingSupplierDOList = TradingSupplierConverter.INSTANCE.convertLsit(contractCreateReqVO.getTradingSupplierVOList());
            tradingSupplierDOList.forEach(tradingSupplierDO -> {
                tradingSupplierDO.setContractId(contractDO.getId());
            });
            tradingSupplierMapper.insertBatch(tradingSupplierDOList);
            TradingSupplierDO tradingSupplierDO = tradingSupplierDOList.get(0);
            //供应商id保存合同表，流程执行人使用
            contractDO.setSupplierId(tradingSupplierDO.getSupplierId());
            contractDO.setSupplierName(tradingSupplierDO.getSupplierName());
            //保存文件阵容
            String content = contractCreateReqVO.getContent();
            if (StringUtils.isNotEmpty(content)) {
                contractDO.setContractContent(content.getBytes());
            }

//            contractDO.setProjectCategoryCode(ProjectCategoryEnums.getInstance(projectDO.getProjectType()).getCode());
            tradingContractExtMapper.updateById(contractDO);
            ContractDO contract = ContractConverter.INSTANCE.tradingExt2DO(contractDO);
            //转成产品的合同类型数据
            GPXProjectDO projectDO1 = gpxProjectMapper.selectOne(GPXProjectDO::getProjectGuid, projectId);
            if (ObjectUtil.isNull(projectDO1)) {
                throw exception(EMPTY_DATA_ERROR_V2, "采购项目");
            }
            ProjectCategoryEnums projectCategoryEnums = ProjectCategoryEnums.getInstance(projectDO1.getProjectType());
            if(ObjectUtil.isNotNull(projectCategoryEnums)){
                List<ContractType> contractTypes=  contractTypeMapper.selectList(ContractType::getPlatId,String.valueOf(projectCategoryEnums.getValue()));
                if(CollectionUtil.isNotEmpty(contractTypes)){
                    contract.setContractType(contractTypes.get(0).getId());
                }
            }
            //相对方存入合同
            List<SignatoryRelReqVO> signatoryList = new ArrayList<SignatoryRelReqVO>();
            SignatoryRelReqVO supplier = new SignatoryRelReqVO().setType(2).setInitiator(false);
            Relative relative = relativeMapper.selectById(contractDO.getSupplierId());
            if (ObjectUtil.isNotNull(relative)) {
                supplier.setSignatoryName(relative.getCompanyName()).setSignatoryId(relative.getId()).setUserId(relative.getContactId());
            }
            SignatoryRelReqVO org = new SignatoryRelReqVO().setSignatoryId(loginUser.getOrgId()).setType(1).setInitiator(true).setUserId(loginUser.getId());
            OrganizationDTO organizationDTO = organizationApi.getOrganization(loginUser.getOrgId());
            if (ObjectUtil.isNotNull(organizationDTO)) {
                org.setSignatoryName(organizationDTO.getName()).setSignatoryId(organizationDTO.getId());
            }
            //产品默认采购人是发起方
            signatoryList.add(org);
            signatoryList.add(supplier);
            handlePart(contract, signatoryList);

            // 合同更新时根据富文本生成更新pdf
            if (ObjectUtil.isNotEmpty(contract.getContractContent())) {
                //富文本生成pdf，存查看的文件id地址
                String folderId = "TEMPLATE-" + IdUtil.fastSimpleUUID();
                String s = FileNameUtil.mainName(contract.getFileName());
                String localFolderPath = READY_TO_UPLOAD_PATH + "/" + folderId;
                FileUtil.mkdir(localFolderPath);
                wkHtmlToPdfManager.fileAuthHtmlToPdfFromstring(StringUtils.toEncodedString(contractDO.getContractContent(), StandardCharsets.UTF_8), localFolderPath + "/" + s + ".pdf");
                Path path = Paths.get(localFolderPath + "/" + s + ".pdf");
                Long pdfFileId = fileApi.uploadFile(s + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + s + ".pdf", Files.readAllBytes(path));
                FileUtil.del(localFolderPath);
                contract.setPdfFileId(pdfFileId);
            }

            contractMapper.updateById(contract);
            TradingContractExtDO oldContract = tradingContractExtMapper.selectById(contractCreateReqVO.getId());
            if (ObjectUtil.isNotNull(oldContract)) {
                GPXProjectDO projectDO = gpxProjectMapper.selectOne(GPXProjectDO::getProjectGuid, oldContract.getProjectGuid());
                if (ObjectUtil.isNull(projectDO)) {
                    throw exception(EMPTY_DATA_ERROR_V2, "采购项目");
                }
                BatchPlanInfoDO batchPlanInfoDO = batchPlanInfoMapper.selectOne(BatchPlanInfoDO::getPlanId, oldContract.getBuyPlanId());
                //联合采购-绑定采购内容
                if (BiddingMethodEnums.UNION.getCode().equals(projectDO.getBiddingMethodCode())) {
                    contractQuotationRelDOList = GPXConverter.INSTANCE.listConQuoR2D(contractCreateReqVO.getQuotationRelReqVOList());
                    //校验采购内容的计划
                    if (CollectionUtil.isNotEmpty(contractQuotationRelDOList)) {
                        BigDecimal planAmount = new BigDecimal(0);
                        if (ObjectUtil.isNotNull(batchPlanInfoDO)) {
                            planAmount = batchPlanInfoDO.getPlanBudget();
                        }
                        BigDecimal sumPrice = new BigDecimal(0);
                        for (GPXContractQuotationRelDO quotationRelDO : contractQuotationRelDOList) {
                            if (!oldContract.getBuyPlanId().equals(quotationRelDO.getPlanId())) {
                                throw exception(DIY_ERROR, "关联的的计划明细不属于当前计划");
                            }
                            sumPrice = sumPrice.add(new BigDecimal(quotationRelDO.getTotalPrice()));
                        }
                        //校验采购明细总和是否超过计划金额
                        if (0 > planAmount.compareTo(sumPrice)) {
                            throw exception(DIY_ERROR, "采购内容总金额不可超过计划金额。");
                        }
                    }
                }
            }
            //删除合同绑定的参数信息（货物类合同履约验收要求信息）
            contractParamFieldMapper.delete(new LambdaQueryWrapperX<OrderContractParamFieldDO>().eqIfPresent(OrderContractParamFieldDO::getContractId, contractDO.getId()));
        }
        //先删除原有附件
        contractFileMapper.delete(new QueryWrapper<ContractFileDO>().eq("contract_id", contractDO.getId()));
        //添加新附件
        if (CollectionUtil.isNotEmpty(contractCreateReqVO.getContractFileDTOList())) {
            List<ContractFileDO> dos = ContractFileConverter.INSTANCE.toDOS(contractCreateReqVO.getContractFileDTOList());
            for (ContractFileDO aDo : dos) {
                aDo.setContractId(contractDO.getId());
            }
            contractFileMapper.insertBatch(dos);
        }
//            //删除旧采购内容
//            purchaseMapper.delete(new QueryWrapper<PurchaseDO>().eq("contract_id", contractDO.getId()));
//            //添加采购内容集合
//            List<PurchaseDO> purchaseDOList = PurchaseConverter.INSTANCE.convertListV1(contractCreateReqVO.getPurchaseVOList());
//            purchaseDOList.forEach(purchaseDO -> purchaseDO.setContractId(contractDO.getId()));
//            purchaseMapper.insertBatch(purchaseDOList);
        //删除旧阶段支付信息
        paymentScheduleMapper.delete(new QueryWrapper<PaymentScheduleDO>().eq("contract_id", contractDO.getId()));
        // 插入阶段支付信息
        if (ObjectUtils.isNotEmpty(contractCreateReqVO.getPayMentInfo())) {
            List<PaymentScheduleDO> dos = PaymentScheduleConverter.INSTANCE.stage2PaymentScheduleDOS2(contractCreateReqVO.getPayMentInfo());
            dos.forEach(item -> {
                item.setContractId(contractDO.getId());
            });
            paymentScheduleMapper.insertBatch(dos);
        }
        //保存合同支付计划信息
        if (ObjectUtil.isNotEmpty(contractCreateReqVO.getPaymentPlanList())) {
            List<PaymentScheduleDO> dos = ContractPaymentPlayConverter.INSTANCE.toDOS(contractCreateReqVO.getPaymentPlanList());
            dos.forEach(item -> item.setContractId(contractDO.getId()));
            paymentScheduleMapper.insertBatch(dos);
        }
        if (CollectionUtil.isNotEmpty(contractCreateReqVO.getTerms())) {
            //删除旧合同条款
            contractTermMapper.delete(new LambdaQueryWrapperX<ContractTermDO>().eq(ContractTermDO::getContractId, contractDO.getId()));
            //保存合同条款信息
            List<ContractTermDO> contractTermDOList = ContractTermConverter.INSTANCE.convertList(contractCreateReqVO.getTerms());
            for (ContractTermDO contractTermDO : contractTermDOList) {
                contractTermDO.setContractId(contractDO.getId());
            }
            contractTermMapper.insertBatch(contractTermDOList);
        }

        if (CollectionUtil.isNotEmpty(contractCreateReqVO.getTerms())) {
            TenantUtils.executeIgnore(() -> {
                //删除旧合同条款
                contractTermMapper.delete(new LambdaQueryWrapperX<ContractTermDO>().eq(ContractTermDO::getContractId, contractDO.getId()));
                //保存合同条款信息
                List<ContractTermDO> contractTermDOList = ContractTermConverter.INSTANCE.convertList(contractCreateReqVO.getTerms());
                for (ContractTermDO contractTermDO : contractTermDOList) {
                    contractTermDO.setContractId(contractDO.getId());
                }
                contractTermMapper.insertBatch(contractTermDOList);
            });
        }

        if (ObjectUtil.isNotEmpty(contractCreateReqVO.getSend())) {
            if (contractCreateReqVO.getSend() == 1) {
                contractService.tradingSend(contractCreateReqVO.getId());
            }
        }
        //如果是更新操作，需要清空redis文件
        System.out.println("更新操作，需要清空redis文件：合同id：" + contractCreateReqVO.getId());
        if (StringUtils.isNotBlank(contractCreateReqVO.getId())) {
            redisUtils.del(REDIS_CONTRACT_ID + contractCreateReqVO.getId());
        }
        System.out.println("redis是否删除成功：" + redisUtils.hasKey(REDIS_CONTRACT_ID + contractCreateReqVO.getId()));
        //再确认一遍
        if (redisUtils.hasKey(REDIS_CONTRACT_ID + contractCreateReqVO.getId())) {
            redisUtils.del(REDIS_CONTRACT_ID + contractCreateReqVO.getId());
        }


        //关联联合采购的合同明细（先清空，再绑定）
        if (CollectionUtil.isNotEmpty(contractQuotationRelDOList)) {
            contractQuotationRelDOList.stream().forEach(i -> {
                        i.setContractId(contractDO.getId());
                    }
            );
            gpxContractQuotationRelMapper.delete(new LambdaQueryWrapperX<GPXContractQuotationRelDO>().eq(GPXContractQuotationRelDO::getContractId, contractDO.getId()));
            gpxContractQuotationRelMapper.insertBatch(contractQuotationRelDOList);
            try {
                // 编辑合同标的信息表
                contractObjectMapper.delete(ContractObjectDO::getContractId, contractDO.getId());
                List<ContractObjectDO> contractObjectDOS = new ArrayList<>();
                List<GPXContractQuotationRelDO> contractQuotationRelDO = gpxContractQuotationRelMapper.selectList(GPXContractQuotationRelDO::getContractId, contractDO.getId());
                if (CollectionUtil.isNotEmpty(contractQuotationRelDO)) {
                    contractQuotationRelDO.forEach(detail -> {
                        ContractObjectDO contractObjectDO = new ContractObjectDO();
                        contractObjectDO.setContractId(contractDO.getId())
                                .setContractId(contractDO.getId())
                                .setOrderId(contractDO.getBidGuid())
                                .setObjectName(detail.getDetailName())
                                .setBrand(detail.getBrandName())
                                .setObjectUnitPrice(detail.getUnitPrice())
                                .setObjectQuantity(detail.getCount().doubleValue())
                                .setUnit(detail.getUnit())
                                .setObjectAmount(detail.getTotalPrice() != null ?new BigDecimal(detail.getTotalPrice()) : null);
                        contractObjectDOS.add(contractObjectDO);
                    });
                }
                contractObjectMapper.insertBatch(contractObjectDOS);
            } catch (Exception e) {
                System.out.println("合同标的信息表添加失败");
            }
        }

        PackageInfoDO packageInfoDO = packageInfoMapper.selectById(contractCreateReqVO.getPackageId());
        if (ObjectUtil.isNotNull(packageInfoDO)) {
            //合同总金额不可超过包的中标金额
            if (totalPackageMoney.doubleValue() > packageInfoDO.getWinBidAmount()) {
                throw exception(DIY_ERROR, "已签订的合同金额（" + totalPackageMoney.doubleValue() + "元） 不能超过包中标金额（" + packageInfoDO.getWinBidAmount() + "元），请根据提示重新操作");
            }
            //如果达到包中标金额，则隐藏该订单数据
            if (0 == totalPackageMoney.compareTo(new BigDecimal(packageInfoDO.getWinBidAmount().toString()))) {
                if (ObjectUtil.isNotEmpty(packageInfoDO) && packageInfoDO.getHidden() != 1){
                    packageInfoDO.setHidden(1);
                    packageInfoMapper.updateById(packageInfoDO);
                    Map<String, Object> bodyParam = new HashMap<>();
                    bodyParam.put("client_id", clientId);
                    bodyParam.put("client_secret", clientSecret);
                    String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
                    JSONObject jsonObject = JSONObject.parseObject(token);
                    try {
                        PackageUpdateDTO packageUpdateDTO = new PackageUpdateDTO().setPackageGuid(packageInfoDO.getPackageGuid()).setHidden(1);
                        String result = contractProcessApi.updateStatusPackage(jsonObject.getString("access_token"), packageUpdateDTO);
                        JSONObject resultJson = JSONObject.parseObject(result);
                        if (!"0".equals(resultJson.getString("code"))) {
                            throw new RuntimeException(result);
                        }
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        log.error("推送电子合同更新包隐藏状态失败", e);
                    }
                }
            }
        }
        //货物类合同新增合同履约验收要求信息
        setContractParamFieldInfo(contractCreateReqVO, contractDO.getId());
        return contractDO.getId();
    }


    private void handlePart(ContractDO contractDO, List<SignatoryRelReqVO> signatoryList) {
        if (CollectionUtil.isNotEmpty(signatoryList)) {
            // 保存签署顺序
            List<Long> userIdList = signatoryList.stream().map(SignatoryRelReqVO::getUserId).filter(Objects::nonNull).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(userIdList)) {
                // 将 userIdList 拼接成字符串用 "_" 隔开，插入库
                String signOrder = userIdList.stream().map(String::valueOf).collect(Collectors.joining("_"));
                // 将拼接好的字符串插入到 contractDO 对象中
                contractDO.setSignOrder(signOrder);

                SignatoryRelReqVO partAReqVO = signatoryList.stream().filter(item -> item.getType() == 1).findFirst().orElse(null);
                SignatoryRelReqVO partBReqVO = signatoryList.stream().filter(item -> item.getType() == 2).findFirst().orElse(null);
                List<SignatoryRelReqVO> voList = signatoryList.stream().filter(item -> !item.getInitiator()).collect(Collectors.toList());

                if (partAReqVO != null) {
                    contractDO.setPartAName(partAReqVO.getSignatoryName());
                }
                if (partBReqVO != null) {
                    contractDO.setPartBName(partBReqVO.getSignatoryName());
                }
                // 更新传入的 signatoryList
                signatoryList.clear();
                signatoryList.addAll(voList);
            }
        }
    }

    private void setContractParamFieldInfo(GPXContractCreateReqVO gpxContractCreateReqVO, String contractId) {
        Boolean flag = gpxContractCreateReqVO.isAddOrderContractParamFieldDO();
        if (flag) {
            //货物类合同新增合同履约验收要求信息不是全部为空，可以新增
            OrderContractParamFieldDO aDo = ContractParamFieldConverter.INSTANCE.toDO(gpxContractCreateReqVO);
            if (ObjectUtil.isNotEmpty(aDo)) {
                aDo.setContractId(contractId);
                contractParamFieldMapper.insert(aDo);
            }
        }
    }

    @Override
    public void deleteById(String id) {
        TradingContractExtDO contractDO = tradingContractExtMapper.selectById(id);
        if (ObjectUtil.isEmpty(contractDO)) {
            throw exception(ErrorCodeConstants.EMPTY_DATA_ERROR_V2, "合同");
        }
        //先删除原有附件
        contractFileMapper.delete(new QueryWrapper<ContractFileDO>().eq("contract_id", contractDO.getId()));
        //删除采购标信息
        contractGoodsMapper.delete(new QueryWrapper<ContractGoodsDO>().eq("contract_id", contractDO.getId()));
        //恢复包信息
        PackageInfoDO packageInfoDO = packageInfoMapper.selectOne(new LambdaUpdateWrapper<PackageInfoDO>()
                .eq(PackageInfoDO::getPackageGuid,contractDO.getBuyPlanPackageId()));
        if (ObjectUtil.isNotEmpty(packageInfoDO) && packageInfoDO.getHidden() != 0){
            packageInfoMapper.update(null, new LambdaUpdateWrapper<PackageInfoDO>()
                    .eq(PackageInfoDO::getPackageGuid, contractDO.getBuyPlanPackageId())
                    .set(PackageInfoDO::getHidden, 0));
            Map<String, Object> bodyParam = new HashMap<>();
            bodyParam.put("client_id", clientId);
            bodyParam.put("client_secret", clientSecret);
            String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
            JSONObject jsonObject = JSONObject.parseObject(token);
            try {
                PackageUpdateDTO packageUpdateDTO = new PackageUpdateDTO().setPackageGuid(packageInfoDO.getPackageGuid()).setHidden(0);
                String result = contractProcessApi.updateStatusPackage(jsonObject.getString("access_token"), packageUpdateDTO);
                JSONObject resultJson = JSONObject.parseObject(result);
                if (!"0".equals(resultJson.getString("code"))) {
                    throw new RuntimeException(result);
                }
            } catch (RuntimeException e) {
                e.printStackTrace();
                log.error("推送电子合同更新包隐藏状态失败", e);
            }
        }
        tradingContractExtMapper.deleteById(id);
        contractMapper.deleteById(id);
        // 删除合同标的信息表数据
        contractObjectMapper.delete(ContractObjectDO::getContractId, id);
    }

    private void buildContractProjectInfo(GPXContractRespVO respVO, TradingContractExtDO orderContractDO) {
        if (orderContractDO != null && StringUtils.isNotBlank(orderContractDO.getBuyPlanPackageId()) && StringUtils.isNotBlank(orderContractDO.getBuyPlanId())) {
            PackageInfoDO packageInfoDO = packageInfoMapper.selectOne(PackageInfoDO::getPackageGuid, orderContractDO.getBuyPlanPackageId());
            if (ObjectUtil.isEmpty(packageInfoDO)){
                throw exception(EMPTY_DATA_ERROR_V2, "采购包明细");
            }
            GPXContractProjectVO projectVO = GPXConverter.INSTANCE.convertProject(packageInfoDO);
            GPXProjectDO projectDO = gpxProjectMapper.selectOne(GPXProjectDO::getProjectGuid, packageInfoDO.getProjectGuid());
            List<SupplierInfoDO> supplierInfos = supplierInfoMapper.selectList(SupplierInfoDO::getPackageId, packageInfoDO.getPackageGuid());
            if (CollectionUtil.isNotEmpty(supplierInfos)) {
                SupplyDTO supply = supplyApi.getSupply(supplierInfos.get(0).getSupplierId());
                if(ObjectUtil.isNull(supply)){
                    supply = hljSupplyService.getSupply(supplierInfos.get(0).getSupplierId());
                }
                projectVO.setSupplierId(supplierInfos.get(0).getSupplierId()).setSupplierName(supplierInfos.get(0).getSupplierName());
                if (ObjectUtil.isNotEmpty(supply)) {
                    projectVO.setPersonName(supply.getPersonName()).setPersonMobile(supply.getPersonMobile());
                }
            }
            if (projectDO != null && StringUtils.isNotBlank(projectDO.getBiddingMethodCode())) {
                BiddingMethodEnums instance = BiddingMethodEnums.getInstance(projectDO.getBiddingMethodCode());
                if (BiddingMethodEnums.COMMON == instance) {
                    PlanInfoDO planInfoDO = planInfoMapper.selectOne(PlanInfoDO::getPlanId, orderContractDO.getBuyPlanId());
                    if(ObjectUtil.isNotNull(planInfoDO)){
                        projectVO.setAgencyId(planInfoDO.getAgencyId()).setAgencyName(planInfoDO.getAgencyName()).setPurchaserId(planInfoDO.getPurchaseUnitId()).setPurchaserOrgName(planInfoDO.getPurchaseUnitName())
                                .setPurchaserLinkName(planInfoDO.getPurchaseLinkName()).setPurchaserLinkTel(planInfoDO.getPurchaseLinkTel());
                    }
                }
                if (BiddingMethodEnums.UNION == instance || BiddingMethodEnums.BATCH == instance) {
                    BatchPlanInfoDO batchPlanInfoDO = batchPlanInfoMapper.selectOne(BatchPlanInfoDO::getPlanId, orderContractDO.getBuyPlanId());
                    projectVO.setAgencyId(projectDO.getAgencyId()).setAgencyName(projectDO.getAgencyName()).setPurchaserId(batchPlanInfoDO.getPurchaserId()).setPurchaserOrgName(batchPlanInfoDO.getPurchaserName());
                }
            }
            respVO.setProjectInfo(projectVO);
        }

    }
    @Resource
    private GPMallContractService gpMallContractService;
    @Override
    public GPXContractRespVO queryById(String id) throws Exception {
        AtomicReference<TradingContractExtDO> atomicOrderContractDO = new AtomicReference<>();
        TenantUtils.executeIgnore(()->{
            atomicOrderContractDO.set(tradingContractExtMapper.selectById(id));
        });
        TradingContractExtDO orderContractDO = atomicOrderContractDO.get();
        if (ObjectUtil.isEmpty(orderContractDO)) {
            throw exception(ErrorCodeConstants.EMPTY_DATA_ERROR);
        }

        // 如果是非交易的签署详情，则走这里
        if(!PlatformEnums.GPMS_GPX.getCode().equals(orderContractDO.getPlatform())){
            return gpMallContractService.queryById4Sign(id);
        }
        GPXContractRespVO respVO = TradingContractExtConverter.INSTANCE.toRespVO(orderContractDO);
        List<PackageDetailInfoDO> packageDetailInfoList = packageDetailInfoMapper.selectList(PackageDetailInfoDO::getPackageGuid, orderContractDO.getBidGuid());
        if (CollectionUtil.isNotEmpty(packageDetailInfoList)) {
            List<String> detailIds = CollectionUtils.convertList(packageDetailInfoList, PackageDetailInfoDO::getDetailId);
            if (CollectionUtil.isNotEmpty(detailIds)) {
                List<BidConfirmQuotationDetailDO> bidConfirmQuotationDetails = bidConfirmQuotationDetailMapper.selectList(BidConfirmQuotationDetailDO::getPackageDetailId, detailIds);
                respVO.setBidConfirmDetailList(bidConfirmQuotationDetails);
            }
        }

        /**
         * 包信息 项目信息 计划信息
         */
        this.buildContractProjectInfo(respVO, orderContractDO);
        //根据合同id查询采购内容
        List<PurchaseDO> purchaseDOList = purchaseMapper.selectList(PurchaseDO::getContractId, id);
        List<PurchaseVO> purchaseVOList = PurchaseConverter.INSTANCE.convertListV2(purchaseDOList);
        if (CollectionUtil.isNotEmpty(purchaseDOList)) {
            respVO.setPurchaseVOList(purchaseVOList);
        }
        //添加供应商信息
        List<TradingSupplierDO> contractSupplierDOS = tradingSupplierMapper.selectList(TradingSupplierDO::getContractId, id);
        List<TradingSupplierVO> tradingSupplierVOList = TradingSupplierConverter.INSTANCE.convertLsitV1(contractSupplierDOS);
        List<String> regionCodes = tradingSupplierVOList.stream().map(TradingSupplierVO::getSupplierLocation).collect(Collectors.toList());
        List<RegionDTO> regionDTOList = regionApi.getRegionByCodes(regionCodes);
        Map<String,RegionDTO> regionDTOMap = new HashMap<String,RegionDTO>();
        if(CollectionUtil.isNotEmpty(regionDTOList)) {
            regionDTOMap = CollectionUtils.convertMap(regionDTOList,RegionDTO::getRegionCode);
        }
        if (CollectionUtil.isNotEmpty(contractSupplierDOS)) {
            respVO.setTradingSupplierVOList(tradingSupplierVOList);
        }
        if (ObjectUtil.isNotEmpty(tradingSupplierVOList)) {
            Map<String, RegionDTO> finalRegionDTOMap = regionDTOMap;
            tradingSupplierVOList.forEach(item -> {
                item.setCountryTypeStr(StringUtils.isEmpty(item.getCountryType()) ? null : ObjectUtil.isEmpty(CountryTypeEnum.getInstance(item.getCountryType())) ? null : CountryTypeEnum.getInstance(item.getCountryType()).getInfo());
                item.setSupplierSizeStr(ObjectUtil.isEmpty(item.getSupplierSize()) ? null : ObjectUtil.isEmpty(SupplierSizeEnum.getInstance(item.getSupplierSize())) ? null : SupplierSizeEnum.getInstance(item.getSupplierSize()).getInfo());
                item.setSupplierFeaturesStr(ObjectUtil.isEmpty(item.getSupplierFeatures()) ? null : ObjectUtil.isEmpty(SupplierFeaturesEnum.getInstance(item.getSupplierFeatures())) ? null : SupplierFeaturesEnum.getInstance(item.getSupplierFeatures()).getInfo());
                item.setForeignInvestmentTypeStr(ObjectUtil.isEmpty(item.getForeignInvestmentType()) ? null : ObjectUtil.isEmpty(ForeignInvestmentTypeEnum.getInstance(Integer.valueOf(item.getForeignInvestmentType()))) ? null : ForeignInvestmentTypeEnum.getInstance(Integer.valueOf(item.getForeignInvestmentType())).getInfo());
                RegionDTO regionDTO = finalRegionDTOMap.get(item.getSupplierLocation());
                if(ObjectUtil.isNotNull(regionDTO)){
                    item.setSupplierLocationName(regionDTO.getRegionName());
                }
            });
        }
        //合同附件
        List<ContractFileDO> contractFileDOList = contractFileMapper.selectList(ContractFileDO::getContractId, id);
        List<ContractFileDTO> dtoList = ContractFileConverter.INSTANCE.toDTOS(contractFileDOList);
        respVO.setContractFileDTOList(dtoList);
        //合同富文本转换
        if (ObjectUtil.isNotEmpty(orderContractDO.getContractContent())) {
            String encodedString = StringUtils.toEncodedString(orderContractDO.getContractContent(), StandardCharsets.UTF_8);
            respVO.setContent(encodedString);
        }
        //添加模板名称
        ModelSingleRespVo model = modelService.getModel(orderContractDO.getTemplateId());
        if (ObjectUtil.isNotEmpty(model)) {
            respVO.setTemplateName(model.getName());
        }
        //获取阶段支付信息

        List<PaymentScheduleDO> scheduleDOList = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>().eqIfPresent(PaymentScheduleDO::getContractId, orderContractDO.getId()));
        if (CollectionUtil.isNotEmpty(scheduleDOList)) {
            respVO.setPayMentInfo(PaymentScheduleConverter.INSTANCE.toUploadStageVO(scheduleDOList));
            //获取支付计划信息
            List<PaymentPlanVO> paymentPlanList = PaymentScheduleConverter.INSTANCE.toPaymentVOs(scheduleDOList);
            respVO.setPaymentPlanList(paymentPlanList);
        }

        //添加批注
        String subQuery = "SELECT code, MAX(create_time) AS max_create_time FROM ecms_gcy_comment where contract_id = '" + respVO.getId() + "'GROUP BY code";
        LambdaQueryWrapper<CommentDO> wrapper = new LambdaQueryWrapper<CommentDO>()
                .inSql(CommentDO::getId,
                        "SELECT m.id FROM ecms_gcy_comment m " +
                                "INNER JOIN (" + subQuery + ") sub " +
                                "ON m.code = sub.code AND m.create_time = sub.max_create_time"
                );
        List<CommentDO> commentDOList = commentMapper.selectList(wrapper);
        List<CommentCreateRespVO> commentCreateRespVOList = CommentConverter.INSTANCE.toRespVOList(commentDOList);
        if (CollectionUtil.isNotEmpty(commentDOList)) {
            commentCreateRespVOList.forEach(commentCreateRespVO -> {
                if (ObjectUtil.isNotEmpty(commentCreateRespVO.getCreator())) {
                    commentCreateRespVO.setCreatorName(adminUserApi.getUserNickById(Long.valueOf(commentCreateRespVO.getCreator())));
                }
            });
            respVO.setCommentCreateRespVOList(commentCreateRespVOList);
        }
        //区划信息
        if (StringUtils.isNotEmpty(respVO.getSupplierLocation())) {
            RegionDTO regionByCode = regionApi.getRegionByCode(respVO.getSupplierLocation());
            respVO.setSupplierLocationStr(ObjectUtil.isEmpty(regionByCode) ? null : regionByCode.getRegionName());
        }
        //添加条款信息
        TenantUtils.executeIgnore(() -> {
            List<ContractTermDO> contractTermDOS = contractTermMapper.selectList(ContractTermDO::getContractId, orderContractDO.getId());
            List<TermsDetailsVo> termsDetailsVos = ContractTermConverter.INSTANCE.convertListVO(contractTermDOS);
            respVO.setTerms(termsDetailsVos);
        });
        GPXProjectDO projectDO = gpxProjectMapper.selectById(orderContractDO.getProjectGuid());
        if (ObjectUtil.isNotNull(projectDO)) {

            Map<String, PlanDetailInfoDO> planDetailInfoDOMap = new HashMap<String, PlanDetailInfoDO>();
            //计划明细
            List<PlanDetailInfoRespVO> planDetailInfoRespVOList = new ArrayList<>();
            List<PlanDetailInfoDO> planDetailInfoDOList = planDetailInfoMapper.selectList(PlanDetailInfoDO::getPlanId, orderContractDO.getBuyPlanId());
            if (CollectionUtil.isNotEmpty(planDetailInfoDOList)) {
                planDetailInfoRespVOList = GPXConverter.INSTANCE.listPlanDetailD2R(planDetailInfoDOList);
                respVO.setPlanDetailInfoRespVOList(planDetailInfoRespVOList);
                planDetailInfoDOMap = CollectionUtils.convertMap(planDetailInfoDOList, PlanDetailInfoDO::getId);
            }
            //联合采购
            if (BiddingMethodEnums.UNION.getCode().equals(projectDO.getBiddingMethodCode())) {
                //采购内容
                List<GPXContractQuotationRelDO> quotationRelDOList = gpxContractQuotationRelMapper.selectList(GPXContractQuotationRelDO::getContractId, orderContractDO.getId());
                if (CollectionUtil.isNotEmpty(quotationRelDOList)) {
                    List<GPXContractQuotationRelRespVO> quotationRelRespVOList = GPXConverter.INSTANCE.listConQuaD2R(quotationRelDOList);
                    Map<String, PlanDetailInfoDO> finalPlanDetailInfoDOMap = planDetailInfoDOMap;
                    quotationRelRespVOList.stream().forEach(i -> {
                        PlanDetailInfoDO planDetailInfoDO = finalPlanDetailInfoDOMap.get(i.getPlanDetailId());
                        if (ObjectUtil.isNotNull(planDetailInfoDO)) {
                            i.setBudgetMoney(planDetailInfoDO.getBudgetMoney());
                        }
                    });
                    respVO.setQuotationRelRespVOList(quotationRelRespVOList);
                }
            } else if (BiddingMethodEnums.COMMON.getCode().equals(projectDO.getBiddingMethodCode())) {
                //一般项目多供应商
                Long supCount = supplierInfoMapper.selectCount(SupplierInfoDO::getPackageId, orderContractDO.getBuyPlanPackageId());
                if (1L < supCount) {
                    respVO.setSupplierType(2);
                    //采购内容
                    List<GPXContractQuotationRelDO> quotationRelDOList = gpxContractQuotationRelMapper.selectList(GPXContractQuotationRelDO::getContractId, orderContractDO.getId());
                    if (CollectionUtil.isNotEmpty(quotationRelDOList)) {
                        List<GPXContractQuotationRelRespVO> quotationRelRespVOList = GPXConverter.INSTANCE.listConQuaD2R(quotationRelDOList);
                        Map<String, PlanDetailInfoDO> finalPlanDetailInfoDOMap = planDetailInfoDOMap;
                        quotationRelRespVOList.forEach(i -> {
                            PlanDetailInfoDO planDetailInfoDO = finalPlanDetailInfoDOMap.get(i.getPlanDetailId());
                            if (ObjectUtil.isNotNull(planDetailInfoDO)) {
                                i.setBudgetMoney(planDetailInfoDO.getBudgetMoney());
                            }
                        });
                        respVO.setQuotationRelRespVOList(quotationRelRespVOList);
                    }
                }
            }
        }

        return respVO;
    }

    /**
     * 电子交易合同作废
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean invalidateContractById(String id, MultipartFile file) throws Exception {
//        if (ObjectUtil.isEmpty(file)) {
//            throw exception(MODEL_CATEGORY_CHECK_EMPTY, "附件不能为空");
//        }
        TradingContractExtDO tradingContractDO = tradingContractExtMapper.selectOne(new LambdaQueryWrapperX<TradingContractExtDO>()
                .select(TradingContractExtDO::getStatus, TradingContractExtDO::getId, TradingContractExtDO::getPlatform, TradingContractExtDO::getSourceCode, TradingContractExtDO::getBuyPlanPackageId, TradingContractExtDO::getBuyerOrgId).eq(TradingContractExtDO::getId, id));

        if (ObjectUtil.isEmpty(tradingContractDO)) {
            throw exception(EMPTY_DATA_ERROR_V2, "合同信息");
        }
//        if (HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode().equals(tradingContractDO.getStatus())) {
//            throw exception(MODEL_CATEGORY_CHECK_EMPTY, "合同已备案，不能作废");
//        }
        if (HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode().equals(tradingContractDO.getStatus()) || HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode().equals(tradingContractDO.getStatus()) ||
                HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode().equals(tradingContractDO.getStatus())) {

            String cValue = getCvalueByKey(SystemConfigKeyEnums.IF_NEW_JIANGUAN.getKey());
            //是否需要走监管新版本的接口(融通平台备案接口)  n：否  y：是
            String orgGuid = "";
            String regionCode = "";
            if ("y".equals(cValue)) {
                //获取采购单位ID和采购单位区划
                if (ObjectUtil.isEmpty(tradingContractDO)) {
                    throw outException(ErrorCodeConstants.DATA_MISSING, "合同不存在");
                }
                orgGuid = tradingContractDO.getBuyerOrgId();
                OrganizationDTO organization = orgApi.getOrganization(orgGuid);
                if (ObjectUtil.isNotNull(organization)) {
                    RegionDTO region = organization.getRegionGuid() == null ? null : regionApi.getRegionById(organization.getRegionGuid());
                    regionCode = region == null ? null : region.getRegionCode();
                }
                String accessToken = gcyBuyPlanService.getInitToken(BusinessTokenConfigEnums.SUPER_CONTROL.getInfo());
                EncryptResponseDto response = outOpenApiService.getContractArchiveStateV4(accessToken, orgGuid, regionCode, Sm4Utils.convertParam(new ContractArchiveStateDTO().setContractGuid(id).setPlatform(tradingContractDO.getPlatform())));
                if (response.getStatus().equals("0")) {
                    if (ObjectUtil.isNotEmpty(response.getData())) {
                        ContractArchiveStateRespDTO result = JSONObject.parseObject(Sm4Utils.decryptEcb(response.getData()), ContractArchiveStateRespDTO.class);
                        log.info("请求监管查询合同状态接口请求参数{}，响应结果{}", response.getData(), result);
                        if (ObjectUtil.isNotEmpty(result)) {
                            if (Objects.equals(result.getContractArchiveState(), ContractArchiveStateEnums.BAKED.getCode()) || Objects.equals(result.getContractArchiveState(), ContractArchiveStateEnums.BACING.getCode())) {
                                throw exception(MODEL_CATEGORY_CHECK_EMPTY, "合同已备案，不能作废");
                            } else if (Objects.equals(result.getContractArchiveState(), ContractArchiveStateEnums.BAK_FAIL.getCode()) || Objects.equals(result.getContractArchiveState(), ContractArchiveStateEnums.WAIT_BAK.getCode())) {
                                //电子交易推送--监管
                                //删除监管备案合同
                                ContractArchiveStateDTO contractArchiveStateDTO = new ContractArchiveStateDTO().setContractGuid(id).setPlatform(tradingContractDO.getPlatform());
                                EncryptResponseDto response1 = outOpenApiService.deleteContractV3(accessToken, orgGuid, regionCode, Sm4Utils.convertParam(contractArchiveStateDTO));
                                log.info("删除监管备案合同{},返回响应结果{}", id, response1);
                                if (!"0".equals(response1.getStatus())) {
                                    throw exception(ErrorCodeConstants.GOMall_Query_Error, "监管的合同删除失败");
                                }
                            }
                        }
                    }
                }
            } else {
                if (tradingContractDO.getSourceCode().equals("GPMS")) {
                    String accessToken = gcyBuyPlanService.getInitToken(BusinessTokenConfigEnums.SUPER_CONTROL.getInfo());
                    EncryptResponseDto response = superVisionApi.getContractArchiveStateV2(accessToken, Sm4Utils.convertParam(new ContractArchiveStateDTO().setContractGuid(id).setPlatform(tradingContractDO.getPlatform())));
                    if (response.getStatus().equals("0")) {
                        if (ObjectUtil.isNotEmpty(response.getData())) {
                            ContractArchiveStateRespDTO result = JSONObject.parseObject(Sm4Utils.decryptEcb(response.getData()), ContractArchiveStateRespDTO.class);
                            log.info("请求监管查询合同状态接口请求参数{}，响应结果{}", response.getData(), result);
                            if (ObjectUtil.isNotEmpty(result)) {
                                if (Objects.equals(result.getContractArchiveState(), ContractArchiveStateEnums.BAKED.getCode()) || Objects.equals(result.getContractArchiveState(), ContractArchiveStateEnums.BACING.getCode())) {
                                    throw exception(MODEL_CATEGORY_CHECK_EMPTY, "合同已备案，不能作废");
                                } else if (Objects.equals(result.getContractArchiveState(), ContractArchiveStateEnums.BAK_FAIL.getCode()) || Objects.equals(result.getContractArchiveState(), ContractArchiveStateEnums.WAIT_BAK.getCode())) {
                                    //电子交易推送--监管
                                    //删除监管备案合同
                                    ContractArchiveStateDTO contractArchiveStateDTO = new ContractArchiveStateDTO().setContractGuid(id).setPlatform(tradingContractDO.getPlatform());
                                    EncryptResponseDto response1 = superVisionApi.deleteContract(accessToken, Sm4Utils.convertParam(contractArchiveStateDTO));
                                    log.info("删除监管备案合同{},返回响应结果{}", id, response1);
                                    if (!"0".equals(response1.getStatus())) {
                                        throw exception(ErrorCodeConstants.GOMall_Query_Error, "监管的合同删除失败");
                                    }
                                }
                            }
                        }
                    }
                } else if (tradingContractDO.getSourceCode().equals("GPMS_PSP")) {
                    //采购单位服务平台
                    String accessToken = gcyBuyPlanService.getInitToken(BusinessTokenConfigEnums.SUPER_CONTROL.getInfo());
                    EncryptResponseDto response = gpxorgApi.getContractArchiveState(accessToken, Sm4Utils.convertParam(new ContractArchiveStateDTO().setContractGuid(id).setPlatform(tradingContractDO.getPlatform())));
                    if (response.getStatus().equals("0")) {
                        if (ObjectUtil.isNotEmpty(response.getData())) {
                            ContractArchiveStateRespDTO result = JSONObject.parseObject(Sm4Utils.decryptEcb(response.getData()), ContractArchiveStateRespDTO.class);
                            log.info("请求采购单位服务平台查询合同状态接口请求参数{}，响应结果{}", response.getData(), result);
                            if (ObjectUtil.isNotEmpty(result)) {
                                if (Objects.equals(result.getContractArchiveState(), ContractArchiveStateEnums.BAKED.getCode()) || Objects.equals(result.getContractArchiveState(), ContractArchiveStateEnums.BACING.getCode())) {
                                    throw exception(MODEL_CATEGORY_CHECK_EMPTY, "合同已备案，不能作废");
                                } else {
                                    //电子交易推送--监管
                                    //删除监管备案合同
                                    ContractArchiveStateDTO contractArchiveStateDTO = new ContractArchiveStateDTO().setContractGuid(id).setPlatform(tradingContractDO.getPlatform());
                                    log.info("删除采购单位服务平台备案合同" + id);
                                    EncryptResponseDto response1 = gpxorgApi.deleteContract(accessToken, Sm4Utils.convertParam(contractArchiveStateDTO));
                                    if (!"0".equals(response1.getStatus())) {
                                        throw exception(ErrorCodeConstants.GOMall_Query_Error, "监管的合同删除失败");
                                    }
                                }
                            }
                        }
                    }
                } else if (tradingContractDO.getSourceCode().equals("PICS")) {
                    //医疗
                    //获取医疗token
                    TokenRequestDTO tokenRequestDTO = new TokenRequestDTO();
                    tokenRequestDTO.setUsername("241777");
                    tokenRequestDTO.setNoise(System.currentTimeMillis() + "");
                    String sign = calculateSign(tokenRequestDTO.getNoise(), "teamlead");
                    tokenRequestDTO.setSign(sign);
                    String jsonString = medicalApi.getToken(tokenRequestDTO);
                    JSONObject jsonObject = JSONObject.parseObject(jsonString);
                    // 从data对象中获取access_token
                    JSONObject dataObject = jsonObject.getJSONObject("data");
                    String accessToken1 = dataObject.getString("access_token");
                    // 设置请求头，添加"Bearer "前缀
                    String authorizationHeader = "Bearer " + accessToken1;
                    ContractArchiveStateDTO contractReqDTO = new ContractArchiveStateDTO().setContractGuid(tradingContractDO.getId()).setPlatform(tradingContractDO.getPlatform());
//                    MedicalResponseDTO response = medicalApi.getContractArchiveStateV2(authorizationHeader, contractReqDTO);
//                    if (response.getCode().equals("0")) {
//                        if (ObjectUtil.isNotEmpty(response.getData())) {
//                    ContractArchiveStateRespDTO result = JSONObject.parseObject(Sm4Utils.decryptEcb(response.getCode()), ContractArchiveStateRespDTO.class);
                    //删除监管备案合同
                    DeleteContractRequestDTO deleteContractRequestDTO = new DeleteContractRequestDTO().setContractGuid(id).setPlatform(tradingContractDO.getPlatform());
                    log.info("删除监管备案合同" + id);
                    EncryptResponseDto response1 = medicalApi.deleteContract(authorizationHeader, deleteContractRequestDTO);
                    if (!"0".equals(response1.getStatus())) {
                        throw exception(ErrorCodeConstants.GOMall_Query_Error, "监管的合同删除失败");
                    }
//                    if (ObjectUtil.isNotEmpty(result)) {
//                                if (Objects.equals(result.getContractArchiveState(), ContractArchiveStateEnums.BAKED.getCode())) {
//                                    throw exception(MODEL_CATEGORY_CHECK_EMPTY, "合同已备案，不能作废");
//                                } else {
//                                }
//                            }
                }
            }
        }


        if (HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode().equals(tradingContractDO.getStatus())
                || HLJContractStatusEnums.CONTRACT_AUDITSTATUS_DELETE.getCode().equals(tradingContractDO.getStatus()) || HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode().equals(tradingContractDO.getStatus())) {
            System.out.println(tradingContractDO.getId() + "合同状态" + tradingContractDO.getStatus() + "，不需要修改状态");
            return true;
        }
        //上传附件
        if (ObjectUtil.isNotEmpty(file)) {
            String filename = UUID.randomUUID() + file.getOriginalFilename();
            try {
                Long fileId = fileApi.uploadFile(filename, FileUploadPathEnum.CONTRACT_DRAFT.getPath() + filename, file.getBytes());
                contractCancellationMapper.insert(new CancellationFileDO().setContractId(id).setFileId(fileId));
            } catch (IOException e) {
                throw exception(MODEL_CATEGORY_CHECK_EMPTY, "上传附件失败");
            }
        }
        //如果是一般项目多供应商包作废就会隐藏
        PackageInfoDO packageInfoDO = packageInfoMapper.selectById(tradingContractDO.getBuyPlanPackageId());
        if (ObjectUtil.isNotNull(packageInfoDO)) {
            if (BiddingMethodEnums.COMMON.getCode().equals(packageInfoDO.getBiddingMethodCode()) && 2 == packageInfoDO.getSupplierType()) {
                List<TradingContractExtDO> contractDOList = tradingContractExtMapper.selectList(new LambdaQueryWrapperX<TradingContractExtDO>()
                        .select(TradingContractExtDO::getId, TradingContractExtDO::getStatus)
                        .eq(TradingContractExtDO::getBuyPlanPackageId, tradingContractDO.getBuyPlanPackageId())
                        .ne(TradingContractExtDO::getStatus, -3)
                );
                if (CollectionUtil.isNotEmpty(contractDOList)) {
                    if (1 == contractDOList.size() && Objects.equals(id, contractDOList.get(0).getId())) {
                        if (ObjectUtil.isNotEmpty(packageInfoDO) && packageInfoDO.getHidden() != 0){
                            packageInfoDO.setHidden(0);//如果分包的最后一个没作废的合同都作废了，就展示起草
                            packageInfoMapper.updateById(packageInfoDO);
                            Map<String, Object> bodyParam = new HashMap<>();
                            bodyParam.put("client_id", clientId);
                            bodyParam.put("client_secret", clientSecret);
                            String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
                            JSONObject jsonObject = JSONObject.parseObject(token);
                            try {
                                PackageUpdateDTO packageUpdateDTO = new PackageUpdateDTO().setPackageGuid(packageInfoDO.getPackageGuid()).setHidden(0);
                                String result = contractProcessApi.updateStatusPackage(jsonObject.getString("access_token"), packageUpdateDTO);
                                JSONObject resultJson = JSONObject.parseObject(result);
                                if (!"0".equals(resultJson.getString("code"))) {
                                    throw new RuntimeException(result);
                                }
                            } catch (RuntimeException e) {
                                e.printStackTrace();
                                log.error("推送电子合同更新包隐藏状态失败", e);
                            }
                        }
                    }
                }
            } else {
                if (ObjectUtil.isNotEmpty(packageInfoDO) && packageInfoDO.getHidden() != 0){
                    packageInfoDO.setHidden(0);//如果分包的最后一个没作废的合同都作废了，就展示起草
                    packageInfoMapper.updateById(packageInfoDO);
                    Map<String, Object> bodyParam = new HashMap<>();
                    bodyParam.put("client_id", clientId);
                    bodyParam.put("client_secret", clientSecret);
                    String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
                    JSONObject jsonObject = JSONObject.parseObject(token);
                    try {
                        PackageUpdateDTO packageUpdateDTO = new PackageUpdateDTO().setPackageGuid(packageInfoDO.getPackageGuid()).setHidden(0);
                        String result = contractProcessApi.updateStatusPackage(jsonObject.getString("access_token"), packageUpdateDTO);
                        JSONObject resultJson = JSONObject.parseObject(result);
                        if (!"0".equals(resultJson.getString("code"))) {
                            throw new RuntimeException(result);
                        }
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                        log.error("推送电子合同更新包隐藏状态失败", e);
                    }
                }
            }
        }
        //合同的状态同步作废
        contractMapper.updateById(new ContractDO().setId(id).setStatus(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode()));
        tradingContractExtMapper.update(null, new LambdaUpdateWrapper<TradingContractExtDO>()
                .eq(TradingContractExtDO::getId, id)
//                .set(TradingContractDO::getPdfFileId, "0")
                .set(TradingContractExtDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode()));
        //数据统计表修改合同状态
        LambdaUpdateWrapper<StatisticsDO> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(StatisticsDO::getContractStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode());
        lambdaUpdateWrapper.eq(StatisticsDO::getContractId, id);
        statisticsMapper.update(null, lambdaUpdateWrapper);
//        //同步黑龙江
//        Map<String, Object> bodyParam = new HashMap<>();
//        bodyParam.put("client_id", clientId);
//        bodyParam.put("client_secret", clientSecret);
//        String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
//        JSONObject jsonObject = JSONObject.parseObject(token);
//        if (jsonObject.get("error") != null) {
//            try {
//                throw new Exception(jsonObject.getString("error_description"));
//            } catch (Exception e) {
//                throw new RuntimeException(jsonObject.getString("error_description"));
//            }
//        }
//        String result = contractProcessApi.invalidateContractById(jsonObject.getString("access_token"),new IdReqDTO().setId(id));
//        log.info("返回结果：" + result);
//        JSONObject resultJson = JSONObject.parseObject(result);
//        if (!"0".equals(resultJson.getString("code"))) {
//            throw new RuntimeException("作废同步状态失败");
//        }
        contractObjectMapper.delete(ContractObjectDO::getContractId, id);
        return true;
    }

    private String getCvalueByKey(String key) {
        ArrayList<String> keyList = new ArrayList<>();
        keyList.add(key);
        List<SystemConfigRespDTO> configsByCKeys = systemConfigApi.getConfigsByCKeys(keyList);
        String cValue = configsByCKeys.size() == 0 ? null : configsByCKeys.get(0).getCValue();
        return cValue;
    }

    private String calculateSign(String noise, String key) {
        try {
            String toHash = noise + "##" + key;
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(toHash.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }



    @Override
    public String printContract(String id) throws Exception {
        System.out.println("开始打印合同" + id);
        //找到富文本
        TradingContractExtDO contractDO = tradingContractExtMapper.selectById(id);
        if (ObjectUtil.isNull(contractDO)) {
            throw exception(EMPTY_DATA_ERROR_V2, "合同");
        }

        if (ObjectUtil.isNotNull(contractDO.getPdfFileId()) && contractDO.getPdfFileId() > 0) {
            //下载pdf文件
            return fileApi.getFileUrlV2Api(contractDO.getPdfFileId());
        }

        String redisKey = REDIS_CONTRACT_ID + contractDO.getId();
        System.out.println("Redis是否有值" + redisUtils.hasKey(redisKey));
        Object fileIdObj = redisUtils.get(redisKey);
        if (ObjectUtil.isNotNull(fileIdObj)) {
            String fileIdStr = (String) fileIdObj;
            Long fileId = Long.valueOf(fileIdStr);
            return fileApi.getFileUrlV2Api(fileId);
        }

        //富文本转pdf文件
        if (ObjectUtil.isNull(contractDO.getContractContent())) {
            throw exception(EMPTY_DATA_ERROR_V2, "合同富文本");
        }
        Long pdfId = rtfContentProcessor(contractDO, StringUtils.toEncodedString(contractDO.getContractContent(), StandardCharsets.UTF_8));
        redisUtils.set(redisKey, pdfId.toString(), 60 * 60 * 24 * 5);
        return fileApi.getFileUrlV2Api(pdfId);
    }


    /**
     * 富文本转pdf
     */
    private Long rtfContentProcessor(TradingContractExtDO vo, String content) throws Exception {

        String modelName = vo.getName();
        Long rtf_fileId = 0L;
        if (StringUtils.isNotBlank(content)) {
            String uuid = String.valueOf(UUID.randomUUID());
            String path = MODEL_RTF_PDF_TEMP_PATH + uuid + ".pdf";
            //富文本生成pdf，存查看的文件id地址
            String folderId = "TEMPLATE-" + IdUtil.fastSimpleUUID();
            String localFolderPath = READY_TO_UPLOAD_PATH + "/" + folderId;
            FileUtil.touch(path);
            contractService.convertRtf2Pdf(content, path);
            //将存于path的pdf文件上到minio的路径FileUploadPathEnum.TEMPLATE.getPath(vo.getCode(),vo.getName())
            rtf_fileId = fileApi.uploadFile(modelName + ".pdf", FileUploadPathEnum.CONTRACT_DRAFT_BEFORE.getPath(uuid, vo.getName() + ".pdf"), IoUtil.readBytes(FileUtil.getInputStream(path)));
            FileUtil.del(path);
            FileUtil.del(localFolderPath);
        }
        return rtf_fileId;
    }


    /**
     * 先判断合同表里面的pdfFileId有没有值，
     * 有值的话直接用，
     * 没有的话去redis查，redis有直接用，没有的话生成pdf，存redis
     */
    @Override
    public void downloadTContract(HttpServletResponse response, String id) throws Exception {
        //找到富文本
        TradingContractExtDO contractDO = tradingContractExtMapper.selectById(id);
        if (ObjectUtil.isNull(contractDO)) {
            throw exception(EMPTY_DATA_ERROR_V2, "合同");
        }

        if (ObjectUtil.isNotNull(contractDO.getPdfFileId()) && contractDO.getPdfFileId() > 0) {
            //下载pdf文件
            fileApi.getFileContentByFileId(response, contractDO.getPdfFileId());
            return;
        }

        String redisKey = REDIS_CONTRACT_ID + contractDO.getId();
        Object fileIdObj = redisUtils.get(redisKey);
        if (ObjectUtil.isNotNull(fileIdObj)) {
            String fileIdStr = (String) fileIdObj;
            fileApi.getFileContentByFileId(response, Long.valueOf(fileIdStr));
            return;
        }

        //富文本转pdf文件
        if (ObjectUtil.isNull(contractDO.getContractContent())) {
            throw exception(EMPTY_DATA_ERROR_V2, "合同富文本");
        }
        Long pdfId = rtfContentProcessor(contractDO, StringUtils.toEncodedString(contractDO.getContractContent(), StandardCharsets.UTF_8));
        redisUtils.set(redisKey, pdfId.toString(), 60 * 60 * 24 * 5);
        //下载pdf文件
        fileApi.getFileContentByFileId(response, pdfId);
    }
}
