package com.yaoan.module.econtract.service.gpmall;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.json.JsonUtils;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.econtract.api.contract.ContractProcessApi;
import com.yaoan.module.econtract.api.contract.GPFAOpenApi;
import com.yaoan.module.econtract.api.contract.OpenApi;
import com.yaoan.module.econtract.api.contract.dto.*;
import com.yaoan.module.econtract.api.contract.dto.gcy.CancellationFileDTO;
import com.yaoan.module.econtract.api.contract.dto.mongolia.EncryptResponseDto;
import com.yaoan.module.econtract.api.gcy.buyplan.SuperVisionApi;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.ContractArchiveStateDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.EncryptDTO;
import com.yaoan.module.econtract.api.gcy.order.DraftOrderInfo;
import com.yaoan.module.econtract.api.gcy.order.DraftOrderInfoVO;
import com.yaoan.module.econtract.api.gcy.order.GoodsVO;
import com.yaoan.module.econtract.api.gcy.order.PurCatalogInfoVo;
import com.yaoan.module.econtract.controller.admin.aop.service.OutOpenApiService;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.BpmContractCreateReqVO;
import com.yaoan.module.econtract.controller.admin.catalog.vo.ModelIdVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.*;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.CancellationFileVO;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.save.OrderContractCreateReqV2Vo;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.upload.UploadContractPaymentPlanVo;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.GPXContractRespVO;
import com.yaoan.module.econtract.controller.admin.gpx.contractVO.trading.TradingSupplierVO;
import com.yaoan.module.econtract.controller.admin.gpx.vo.draft.PlanInfo;
import com.yaoan.module.econtract.controller.admin.model.vo.TermsDetailsVo;
import com.yaoan.module.econtract.controller.admin.order.vo.GPMallPageRespVO;
import com.yaoan.module.econtract.controller.admin.order.vo.GoodsRespVO;
import com.yaoan.module.econtract.convert.contract.ContractConverter;
import com.yaoan.module.econtract.convert.contract.PaymentScheduleConverter;
import com.yaoan.module.econtract.convert.contract.ext.gcy.ContractOrderExtConverter;
import com.yaoan.module.econtract.convert.contract.ext.gcy.ContractParamFieldConverter;
import com.yaoan.module.econtract.convert.contract.ext.gcy.UploadContractOrderExtConverter;
import com.yaoan.module.econtract.convert.contract.ext.gxp.TradingContractExtConverter;
import com.yaoan.module.econtract.convert.gcy.gpmall.AssociatedPlanConverter;
import com.yaoan.module.econtract.convert.gcy.gpmall.ContractFileMapper;
import com.yaoan.module.econtract.convert.gcy.gpmall.GPMallOrderConverter;
import com.yaoan.module.econtract.convert.gpx.GPXConverter;
import com.yaoan.module.econtract.convert.term.ContractTermConverter;
import com.yaoan.module.econtract.convert.model.ModelConverter;
import com.yaoan.module.econtract.convert.term.ContractTermConverter;
import com.yaoan.module.econtract.dal.dataobject.category.backups.ContractInfoBackupsDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractObjectDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.OrderContractParamFieldDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.UploadContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.contract.trading.TradingSupplierDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.*;
import com.yaoan.module.econtract.dal.dataobject.gcy.rel.ContractOrderRelDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.*;
import com.yaoan.module.econtract.dal.dataobject.model.SimpleModel;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.dataobject.term.ContractTermDO;
import com.yaoan.module.econtract.dal.dataobject.term.ContractTermDO;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import com.yaoan.module.econtract.dal.mysql.category.backups.ContractInfoBackupsMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractObjectMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractOrderExtMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractParamFieldMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.UploadContractOrderExtMapper;
import com.yaoan.module.econtract.dal.mysql.contract.trading.ContractCancellationMapper;
import com.yaoan.module.econtract.dal.mysql.contract.trading.TradingSupplierMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.gcy.rel.ContractOrderRelMapper;
import com.yaoan.module.econtract.dal.mysql.gpx.BatchPlanInfoMapper;
import com.yaoan.module.econtract.dal.mysql.gpx.GPXProjectMapper;
import com.yaoan.module.econtract.dal.mysql.gpx.PackageInfoMapper;
import com.yaoan.module.econtract.dal.mysql.gpx.PlanInfoMapper;
import com.yaoan.module.econtract.dal.mysql.model.SimpleModelMapper;
import com.yaoan.module.econtract.dal.mysql.order.*;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import com.yaoan.module.econtract.dal.mysql.term.ContractTermMapper;
import com.yaoan.module.econtract.dal.mysql.term.TermMapper;
import com.yaoan.module.econtract.dal.mysql.term.ContractTermMapper;
import com.yaoan.module.econtract.enums.*;
import com.yaoan.module.econtract.enums.common.IfEnums;
import com.yaoan.module.econtract.enums.gcy.gpmall.*;
import com.yaoan.module.econtract.enums.neimeng.AttachmentTypeEnums;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import com.yaoan.module.econtract.enums.order.ProjectCategoryEnums;
import com.yaoan.module.econtract.enums.supervise.PurCatalogTypeEnums;
import com.yaoan.module.econtract.enums.supervise.SupplierFeaturesEnum;
import com.yaoan.module.econtract.enums.supervise.SupplierSizeEnum;
import com.yaoan.module.econtract.service.bpm.contract.BpmContractService;
import com.yaoan.module.econtract.service.catalog.PurCatalogService;
import com.yaoan.module.econtract.service.external.ExternalInterfaceService;
import com.yaoan.module.econtract.service.gcy.buyplan.EcmsGcyBuyPlanServiceImpl;
import com.yaoan.module.econtract.service.gcy.gpmall.vo.ContractPaymentPlanVo;
import com.yaoan.module.econtract.service.hljsupply.HLJSupplyService;
import com.yaoan.module.econtract.service.relative.RelativeService;
import com.yaoan.module.econtract.util.AmountUtil;
import com.yaoan.module.econtract.util.ContractCodeUtil;
import com.yaoan.module.econtract.util.gcy.Sm4Utils;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.enums.FileUploadPathEnum;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.config.dto.SystemConfigRespDTO;
import com.yaoan.module.system.api.region.RegionApi;
import com.yaoan.module.system.api.region.dto.RegionDTO;
import com.yaoan.module.system.api.user.OrganizationApi;
import com.yaoan.module.system.api.user.SupplyApi;
import com.yaoan.module.system.api.user.dto.OrganizationDTO;
import com.yaoan.module.system.api.user.dto.SupplyDTO;
import com.yaoan.module.system.enums.config.SystemConfigKeyEnums;
import com.yaoan.module.system.enums.user.UserTypeEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.OutServiceExceptionUtil.outException;
import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.module.econtract.enums.ContractUploadTypeEnums.ORDER_DRAFT;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.*;
import static com.yaoan.module.econtract.enums.StatusConstants.REDIS_CONTRACT_ID;
import static com.yaoan.module.econtract.enums.StatusConstants.SUFFIX_PDF;
import static com.yaoan.module.econtract.enums.gcy.gpmall.GCYOrderStatusEnums.WAITE_TO_DRAFT;
import static com.yaoan.module.system.enums.config.SystemConfigKeyEnums.IF_NEED_MODEL_CATEGORY;

/**
 * @description:
 * @author: Pele
 * @date: 2024/6/6 19:30
 */
@Slf4j
@Service
public class GPMallContractServiceImpl implements GPMallContractService {
    @Resource
    private ContractInfoBackupsMapper contractInfoBackupsMapper;
    @Resource
    private ContractOrderExtMapper contractOrderExtMapper;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private GPMallOrderOldMapper gpMallOrderMapper;
    @Resource
    private OrganizationApi organizationApi;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private ContractParamFieldMapper contractParamFieldMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private SignatoryRelMapper signatoryRelMapper;
    @Resource
    private RegionApi regionApi;
    @Resource
    private HLJSupplyService hljSupplyService;
    @Resource
    private GPMallGoodsMapper gpMallGoodsMapper;
    @Resource
    private GPMallProjectMapper gpMallProjectMapper;
    @Resource
    private AssociatedPlanMapper associatedPlanMapper;
    @Resource
    private GoodsPurCatalogMapper goodsPurCatalogMapper;
    @Resource
    private PurCatalogService purCatalogService;
    @Resource
    private RelativeService relativeService;
    @Resource
    private FileApi fileApi;
    @Resource
    private ContractOrderRelMapper contractOrderRelMapper;
    @Resource
    private PackageInfoMapper packageInfoMapper;
    @Resource
    private PlanInfoMapper planInfoMapper;
    @Resource
    private TradingSupplierMapper tradingSupplierMapper;
    @Resource
    private UploadContractOrderExtMapper uploadContractOrderExtMapper;
    @Autowired
    private ContractProcessApi contractProcessApi;
    @Value("${feign.client.contract.client_id}")
    private String clientId;
    @Value("${feign.client.contract.client_secret}")
    private String clientSecret;
    @Resource
    private ContractFileMapper contractFileMapper;
    @Resource
    private EcmsGcyBuyPlanServiceImpl gcyBuyPlanService;
    @Resource
    private GPFAOpenApi gpfaOpenApi;
    @Resource
    private OpenApi openApi;
    @Resource
    private ExternalInterfaceService externalInterfaceService;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private SuperVisionApi superVisionApi;
    @Resource
    private OutOpenApiService outOpenApiService;
    @Resource
    private ContractCancellationMapper contractCancellationMapper;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;
    @Resource
    private BpmContractService bpmContractService;
    @Resource
    private GPXProjectMapper gpxProjectMapper;
    @Resource
    private BatchPlanInfoMapper batchPlanInfoMapper;
    @Resource
    private SupplyApi supplyApi;
    @Resource
    private ContractTermMapper contractTermMapper;
    @Resource
    private TermMapper termMapper;
    @Resource
    private ContractObjectMapper contractObjectMapper;


    @Override
    public GPXContractRespVO queryById4Sign(String id) {
        AtomicReference<ContractOrderExtDO> atomicOrderContractDO = new AtomicReference<>();
        TenantUtils.executeIgnore(() -> {
            atomicOrderContractDO.set(contractOrderExtMapper.selectById(id));
        });
        ContractOrderExtDO contractOrderExtDO = atomicOrderContractDO.get();//contractOrderExtMapper.selectById(id);
        if (ObjectUtil.isNull(contractOrderExtDO)) {
            throw exception(DIY_ERROR, "请确认合同编号是否正确。");
        }
        List<TradingSupplierVO> tradingSupplierVOList = new ArrayList<TradingSupplierVO>();
        GPXContractRespVO respVO = TradingContractExtConverter.INSTANCE.toRespVO4Sign(contractOrderExtDO);
        SupplyDTO supplyDTO = supplyApi.getSupply(contractOrderExtDO.getSupplierId());
        if (ObjectUtil.isNull(supplyDTO)) {
            supplyDTO = hljSupplyService.getSupply(contractOrderExtDO.getSupplierId());
        }
        // 供应商信息补全
        if (ObjectUtil.isNotNull(supplyDTO)) {
            TradingSupplierVO tradingSupplierVO = new TradingSupplierVO();

            tradingSupplierVO.setSupplierId(contractOrderExtDO.getSupplierId());

            // 供应商公章
            tradingSupplierVO.setSupplierName(supplyDTO.getSupplyCn());
            //法人
            tradingSupplierVO.setLegalRepresentative(supplyDTO.getLegalPerson());
            // 供应商规模
            tradingSupplierVO.setSupplierSizeStr(ObjectUtil.isNull(SupplierSizeEnum.getInstance(contractOrderExtDO.getSupplierSize())) ? "" : SupplierSizeEnum.getInstance(contractOrderExtDO.getSupplierSize()).getInfo());
            //特殊性质
            tradingSupplierVO.setSupplierFeaturesStr(ObjectUtil.isNull(SupplierFeaturesEnum.getInstance(contractOrderExtDO.getSupplierFeatures())) ? "" : SupplierFeaturesEnum.getInstance(contractOrderExtDO.getSupplierFeatures()).getInfo());
            // 开户名称
            tradingSupplierVO.setPayPlanbAccount(supplyDTO.getSupplyCn());
            //开户银行
            tradingSupplierVO.setBankName(StringUtils.isBlank(respVO.getBankName()) ? supplyDTO.getBankName() : respVO.getBankName());
            //银行账号
            tradingSupplierVO.setBankAccount(StringUtils.isBlank(respVO.getBankAccount()) ? supplyDTO.getBankAccount() : respVO.getBankAccount());
            //收款金额
            tradingSupplierVO.setSupplierPayAmount(contractOrderExtDO.getTotalMoney());
            // 纳税人识别号
            tradingSupplierVO.setSupplierTaxpayerNum(StringUtils.isBlank(respVO.getSupplierTaxpayerNum()) ? supplyDTO.getOrgCode() : respVO.getSupplierTaxpayerNum());
            //供应商区域
            tradingSupplierVO.setSupplierLocation(supplyDTO.getAddr());
            //供应商注册地址
            tradingSupplierVO.setRegisteredAddress(supplyDTO.getRegAddr());
            //联系方式
            tradingSupplierVO.setSupplierLinkMobile(StringUtils.isBlank(contractOrderExtDO.getSupplierLinkMobile()) ? supplyDTO.getPersonMobile() : contractOrderExtDO.getSupplierLinkMobile());
            // 供应商传真
            tradingSupplierVO.setSupplierFax(StringUtils.isBlank(contractOrderExtDO.getSupplierFax()) ? supplyDTO.getFax() : contractOrderExtDO.getSupplierFax());
            tradingSupplierVOList.add(tradingSupplierVO);
        }

        return respVO.setTradingSupplierVOList(tradingSupplierVOList);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ContractDataDTO queryByOrderId(String orderId) {
        ContractDataDTO contractDataDTO = null;
        ContractInfoBackupsDO contractInfoBackupsDO = contractInfoBackupsMapper.selectOne(ContractInfoBackupsDO::getOrderId, orderId);
        if (ObjectUtils.isNotEmpty(contractInfoBackupsDO)) {
            String contractInfo = contractInfoBackupsDO.getContractInfo();
            contractDataDTO = JsonUtils.parseObject(contractInfo, ContractDataDTO.class);
        }
        return contractDataDTO;
    }

    /**
     * 电子卖场-保存合同
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveContractAll(OrderContractCreateReqV2Vo contractCreateReqVO) throws JsonProcessingException {
        //数据校验
        checkIntegerType(contractCreateReqVO);

//        contractCreateReqVO.setContractType(1);

        String id = null;
        //修改
        if (StringUtils.isNotEmpty(contractCreateReqVO.getId())) {
            ContractOrderExtDO contractOrderExtDO = contractOrderExtMapper.selectOne(new LambdaQueryWrapperX<ContractOrderExtDO>().eq(ContractOrderExtDO::getId, contractCreateReqVO.getId()).select(ContractOrderExtDO::getContractDrafter, ContractOrderExtDO::getOrderId, ContractOrderExtDO::getPlatform));
            if (ObjectUtils.isEmpty(contractOrderExtDO)) {
                throw exception(ErrorCodeConstants.GOMall_Query_Error, "合同不存在");
            }
            //超市合同是否能够修改
//            if (ObjectUtil.isNotEmpty(contractOrderExtDO.getContractDrafter()) && !contractOrderExtDO.getContractDrafter().equals(SecurityFrameworkUtils.getLoginUser().getType())) {
//                throw exception(ErrorCodeConstants.GOMall_Query_Error, ContractDrafterEnums.getInstance(contractOrderExtDO.getContractDrafter()).getInfo() + ",不允许修改合同");
//            }
        }
        if (CollectionUtil.isNotEmpty(contractCreateReqVO.getOrderIdList())) {
            List<DraftOrderInfoDO> draftOrderInfoDOS = gpMallOrderMapper.selectList(new LambdaQueryWrapperX<DraftOrderInfoDO>().inIfPresent(DraftOrderInfoDO::getOrderGuid, contractCreateReqVO.getOrderIdList()));
            if (CollectionUtil.isEmpty(draftOrderInfoDOS)) {
                String str = "";
                for (String s : contractCreateReqVO.getOrderIdList()) {
                    str = str + s;
                }
                throw exception(ErrorCodeConstants.GOMall_Query_Error, "查询订单" + str + "中有不存在订单");
            }
            String contractFrom = draftOrderInfoDOS.get(0).getContractFrom();
            if (!contractFrom.equals(contractCreateReqVO.getContractFrom())) {
                contractCreateReqVO.setContractFrom(contractFrom);
            }
        }
        ContractOrderExtDO extDO = ContractOrderExtConverter.INSTANCE.toDO(contractCreateReqVO);
        id = saveContractV4(contractCreateReqVO, extDO);
        //清除文件id缓存
        redisUtils.del(REDIS_CONTRACT_ID + id);
        log.info("redis是否删除成功：" + redisUtils.hasKey(REDIS_CONTRACT_ID + id));

        //再确认一遍
        if (redisUtils.hasKey(REDIS_CONTRACT_ID + id)) {
            redisUtils.del(REDIS_CONTRACT_ID + id);
        }
        return id;
    }

    @Override
    public String pushContractToEcontract(String id) {
        ContractOrderExtDO contractOrderExtDO = contractOrderExtMapper.selectById(id);
        if (ObjectUtil.isEmpty(contractOrderExtDO)) {
            return "No Contract";
        }
        //同步合同到电子合同
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("client_id", clientId);
        bodyParam.put("client_secret", clientSecret);
        String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
        JSONObject jsonObject = JSONObject.parseObject(token);
        if (jsonObject.get("error") != null) {
            throw exception(ErrorCodeConstants.DIY_ERROR, jsonObject.getString("error_description"));
        }
        ContractOrderExtDTO contractOrderExtDTO = ContractOrderExtConverter.INSTANCE.toContractOrderExtDTO(contractOrderExtDO);
        contractOrderExtDTO.setFromProduct(true);
        //支付计划
        List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.selectList(PaymentScheduleDO::getContractId, contractOrderExtDTO.getId());
        //支付计划
        if (CollectionUtil.isNotEmpty(paymentScheduleDOS)) {
            if (StringUtils.isNotBlank(paymentScheduleDOS.get(0).getStageName())) {
                //阶段计划
                contractOrderExtDTO.setPayMentInfo(PaymentScheduleConverter.INSTANCE.gpxDO2StageDTOList(paymentScheduleDOS));
            } else {
                //其他的支付计划
                contractOrderExtDTO.setPaymentPlanList(PaymentScheduleConverter.INSTANCE.PaymentListToDTO(paymentScheduleDOS));
            }
        }
        //订单合并情况
        List<ContractOrderRelDO> contractOrderRelDOS = contractOrderRelMapper.selectList(ContractOrderRelDO::getContractId, id);
        if (CollectionUtil.isNotEmpty(contractOrderRelDOS) && contractOrderRelDOS.size() > 1) {
            contractOrderExtDTO.setOrderIds(contractOrderRelDOS.stream().map(ContractOrderRelDO::getOrderId).collect(Collectors.toList()));
        }
        contractOrderExtDTO.setCreator(contractOrderExtDO.getCreator());
        try {
            EncryptDTO encryptDTO = Sm4Utils.convertParam(contractOrderExtDTO);
            String result = contractProcessApi.pushContractToEcontract(jsonObject.getString("access_token"), encryptDTO);
            System.out.println(result);
            JSONObject resultJson = JSONObject.parseObject(result);
            if (!"0".equals(resultJson.getString("code"))) {
                throw exception(ErrorCodeConstants.DIY_ERROR, "合同发送失败");
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 检查是否为数字
     *
     * @param vo
     */
    private void checkIntegerType(OrderContractCreateReqV2Vo vo) {

        if (StringUtils.isNotEmpty(vo.getSpotCheckProportion()) && !StringUtils.isNumeric(vo.getSpotCheckProportion())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "抽查检测比例必须为数字");
        }
        if (StringUtils.isNotEmpty(vo.getSupplierLinkMobile()) && vo.getSupplierLinkMobile().length() < 7) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "供应商负责人电话最短7位");
        }
        if (StringUtils.isNotEmpty(vo.getBankAccount()) && vo.getBankAccount().length() < 7) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "供应商开户行账号最短7位");
        }
        if (StringUtils.isNotEmpty(vo.getBuyerLinkMobile()) && vo.getBuyerLinkMobile().length() < 7) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "采购人负责人电话最短7位");
        }
        if (StringUtils.isNotEmpty(vo.getPayPlanBankAccount()) && vo.getPayPlanBankAccount().length() < 7) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "支付银行账号最短7位");
        }
        if (StringUtils.isNotEmpty(vo.getBankNo()) && vo.getBankNo().length() < 7) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "供应商银行行号最短7位");
        }
        if (StringUtils.isNotEmpty(vo.getWorkingDayNum()) && !StringUtils.isNumeric(vo.getWorkingDayNum())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "工作日须填写数字");
//            vo.getWorkingDayNum();//工作日个数
        }
        if (StringUtils.isNotEmpty(vo.getIsSmallCompany()) && !StringUtils.isNumeric(vo.getIsSmallCompany())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "是否中小企业融资须填写数字");
//            vo.getIsSmallCompany();//是否中小企业融资
        }
        if (StringUtils.isNotEmpty(vo.getIsSignChoose()) && !StringUtils.isNumeric(vo.getIsSignChoose())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "合同允许采购人选择签章须填写数字");
        }
//        vo.getIsSignChoose();//合同允许采购人选择签章
        if (StringUtils.isNotEmpty(vo.getIsSign()) && !StringUtils.isNumeric(vo.getIsSign())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "须合同是否可以签章填写数字");
        }
//        vo.getIsSign();//合同是可以签章
        if (StringUtils.isNotEmpty(vo.getIsPublicity()) && !StringUtils.isNumeric(vo.getIsPublicity())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "合同是否公示须填写数字");
        }
//        vo.getIsPublicity();//合同是否公示
        if (StringUtils.isNotEmpty(vo.getIsExport()) && !StringUtils.isNumeric(vo.getIsExport())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "合同是否备案须填写数字");
        }
//        vo.getIsExport();//合同是否备案
        if (StringUtils.isNotEmpty(vo.getContainInstall()) && !StringUtils.isNumeric(vo.getContainInstall())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "是否包安装须填写数字");
        }
//        vo.getContainInstall();//是否包安装
        if (StringUtils.isNotEmpty(vo.getContractNum()) && !StringUtils.isNumeric(vo.getContractNum())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "合同份数须填写数字");
        }
//        vo.getContractNum();//合同份数
        if (StringUtils.isNotEmpty(vo.getPersonNum()) && !StringUtils.isNumeric(vo.getPersonNum())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "人员数量须填写数字");
        }
//        vo.getPersonNum();//人员数量
        if (StringUtils.isNotEmpty(vo.getGoodsTime()) && !StringUtils.isNumeric(vo.getGoodsTime())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "日历天数须填写数字");
        }
//        vo.getGoodsTime();//日历天数（服务时间，交货时间）
        if (StringUtils.isNotEmpty(vo.getAccidentDays()) && !StringUtils.isNumeric(vo.getAccidentDays())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "事故发生后天数须填写数字");
        }
//        vo.getAccidentDays();//事故发生后天数
        if (StringUtils.isNotEmpty(vo.getContinuationDays()) && !StringUtils.isNumeric(vo.getContinuationDays())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "不可抗力事件延续天数须填写数字");
        }
//        vo.getContinuationDays();//不可抗力事件延续天数
        if (StringUtils.isNotEmpty(vo.getSupplierWorkingDayNum2()) && !StringUtils.isNumeric(vo.getSupplierWorkingDayNum2())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "乙方逾期超过约定的工作日个数须填写数字");
        }
//        vo.getSupplierWorkingDayNum2();//乙方逾期超过约定的工作日个数
        if (StringUtils.isNotEmpty(vo.getSupplierWorkingDayNum1()) && !StringUtils.isNumeric(vo.getSupplierWorkingDayNum1())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "乙方进行合格验收工作日个数须填写数字");
        }
//        vo.getSupplierWorkingDayNum1();//乙方进行合格验收工作日个数
        if (StringUtils.isNotEmpty(vo.getContractCategory()) && !StringUtils.isNumeric(vo.getContractCategory())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "合同分类须填写数字");
        }
//        vo.getContractCategory();//合同分类
        if (StringUtils.isNotEmpty(vo.getAcceptanceProblemDealDays()) && !StringUtils.isNumeric(vo.getAcceptanceProblemDealDays())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "验收异议解决工作日个数须填写数字");
        }
//        vo.getAcceptanceProblemDealDays();//验收异议解决工作日个数
        if (StringUtils.isNotEmpty(vo.getCanCancelDays()) && !StringUtils.isNumeric(vo.getCanCancelDays())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "可解除合同天数须填写数字");
        }
//        vo.getCanCancelDays();//可解除合同天数
        if (StringUtils.isNotEmpty(vo.getWorkDays()) && !StringUtils.isNumeric(vo.getWorkDays())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "工程竣工起止天数须填写数字");
        }
//        vo.getWorkDays();//工程竣工起止天数
        if (StringUtils.isNotEmpty(vo.getContractDays()) && !StringUtils.isNumeric(vo.getContractDays())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "合同期限起止天数须填写数字");
        }
//        vo.getContractDays();//合同期限起止天数
        if (StringUtils.isNotEmpty(vo.getPurchaseResultValidity()) && !StringUtils.isNumeric(vo.getPurchaseResultValidity())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "采购结果有效期年限须填写数字");
        }
//        vo.getPurchaseResultValidity();//采购结果有效期年限
        if (StringUtils.isNotEmpty(vo.getSupplierNoServiceOverDays()) && !StringUtils.isNumeric(vo.getSupplierNoServiceOverDays())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "乙方未按合同提供服务超期天数须填写数字");
        }
//        vo.getSupplierNoServiceOverDays();
        if (StringUtils.isNotEmpty(vo.getAccidentStartDays()) && !StringUtils.isNumeric(vo.getAccidentStartDays())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "延迟履约方不可抗力发生后应出示证明文件天数须填写数字");
        }
//        vo.getAccidentStartDays();//延迟履约方不可抗力发生后应出示证明文件天数
        if (StringUtils.isNotEmpty(vo.getAccidentEndDays()) && !StringUtils.isNumeric(vo.getAccidentEndDays())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "延迟履约方不可抗力结束后应出示证明文件天数须填写数字");
        }
//        vo.getAccidentEndDays();//延迟履约方不可抗力结束后应出示证明文件天数
        if (StringUtils.isNotEmpty(vo.getOrgMaterialsDays()) && !StringUtils.isNumeric(vo.getOrgMaterialsDays())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "甲方开工前提供材料天数须填写数字");
        }
//        vo.getOrgMaterialsDays();//甲方开工前提供材料天数
        if (StringUtils.isNotEmpty(vo.getOrgMaterialsNum()) && !StringUtils.isNumeric(vo.getOrgMaterialsNum())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "甲方开工前提供材料份数须填写数字");
        }
//        vo.getOrgMaterialsNum();//甲方开工前提供材料份数
        if (StringUtils.isNotEmpty(vo.getContractCopyNum()) && !StringUtils.isNumeric(vo.getContractCopyNum())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "交给乙方的监理合同副本份数须填写数字");
        }
//        vo.getContractCopyNum();//交给乙方的监理合同副本份数
        if (StringUtils.isNotEmpty(vo.getSupMaterialsDays()) && !StringUtils.isNumeric(vo.getSupMaterialsDays())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "乙方应向甲方提交施工相关材料天数须填写数字");
        }
//        vo.getSupMaterialsDays();//乙方应向甲方提交施工相关材料天数
        if (StringUtils.isNotEmpty(vo.getOrgDealDaysByWarrantyInform()) && !StringUtils.isNumeric(vo.getOrgDealDaysByWarrantyInform())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "甲方接到通知后处理天数须填写数字");
        }
//        vo.getOrgDealDaysByWarrantyInform();//甲方接到通知后处理天数
        if (StringUtils.isNotEmpty(vo.getSupDealDaysByGetBooks()) && !StringUtils.isNumeric(vo.getSupDealDaysByGetBooks())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "乙方收到书面异议后处理天数须填写数字");
        }
//        vo.getSupDealDaysByGetBooks();//乙方收到书面异议后处理天数
        if (StringUtils.isNotEmpty(vo.getSupSubmitDrawingEndNum()) && !StringUtils.isNumeric(vo.getSupSubmitDrawingEndNum())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "乙方提供的竣工图纸份数须填写数字");
        }
//        vo.getSupSubmitDrawingEndNum();//乙方提供的竣工图纸份数
        if (StringUtils.isNotEmpty(vo.getProjectWarrantyYears()) && !StringUtils.isNumeric(vo.getProjectWarrantyYears())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "工程质量保修期年限须填写数字");
        }
//        vo.getProjectWarrantyYears();//工程质量保修期年限
        if (StringUtils.isNotEmpty(vo.getSupDealDaysByWarrantyInform()) && !StringUtils.isNumeric(vo.getSupDealDaysByWarrantyInform())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "乙方收到保修通知后进行保修天数须填写数字");
        }
//        vo.getSupDealDaysByWarrantyInform();//乙方收到保修通知后进行保修天数
        if (StringUtils.isNotEmpty(vo.getSupDealDaysByMaintainInform()) && !StringUtils.isNumeric(vo.getSupDealDaysByMaintainInform())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "乙方收到维修通知后进行维修天数须填写数字");
        }
//        vo.getSupDealDaysByMaintainInform();//乙方收到维修通知后进行维修天数
        if (StringUtils.isNotEmpty(vo.getClauseSecret()) && !StringUtils.isNumeric(vo.getClauseSecret())) {
            throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "合同文本是否包含涉密条款须填写数字");
        }
//        vo.getClauseSecret();//合同文本是否包含涉密条款

    }

    public String saveContractV4(OrderContractCreateReqV2Vo gpMallContractCreateReqVO, ContractOrderExtDO extDO) throws JsonProcessingException {

        ContractDO contractDO = ContractConverter.INSTANCE.req2D(gpMallContractCreateReqVO).setUpload(ORDER_DRAFT.getCode());
        DraftOrderInfoDO order = new DraftOrderInfoDO();
        //添加订单
        if (ObjectUtil.isNotEmpty(gpMallContractCreateReqVO.getOrderIdList())) {
            extDO.setOrderId(gpMallContractCreateReqVO.getOrderIdList().get(0));
            order = gpMallOrderMapper.selectOne(DraftOrderInfoDO::getOrderGuid, extDO.getOrderId());
        }
        //添加区划
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        if (ObjectUtils.isEmpty(extDO.getContractDrafter())) {
            extDO.setContractDrafter(loginUser.getType());
        }
        String regionCode = "";
        if (ObjectUtil.isNotEmpty(loginUser.getRegionCode())) {
            regionCode = loginUser.getRegionCode();
        } else {
            regionCode = organizationApi.getOrgRegionCodeByOrgId(loginUser.getOrgId());
        }
        if (StringUtils.isNotEmpty(regionCode)) {
            extDO.setRegionCode(regionCode);
        }
        if (BeanUtil.isEmpty(extDO)) {
            return null;
        }
        //设置相对方id和采购人id
        //设置合同状态
        if (gpMallContractCreateReqVO.getSend() == 0) {
            //保存按钮进入，状态为草稿
            extDO.setStatus(ContractAuditStatusEnums.CONTRACT_AUDITSTATUS_TOBESENT.getCode());
        }

        //优化富文本内容
        if (ObjectUtil.isNotEmpty(extDO.getContractContent())) {
            String contractContent = regionTemplateCurrencyV3(extDO.getContractContent());
            extDO.setContractContent(contractContent.getBytes(StandardCharsets.UTF_8));
        }
        //如果是修改
        if (ObjectUtils.isNotEmpty(extDO.getId())) {
            //同步扩展表
            ContractOrderExtDO orderContractDO = contractOrderExtMapper.selectOne(new LambdaQueryWrapperX<ContractOrderExtDO>().eqIfPresent(ContractOrderExtDO::getId, extDO.getId()).notIn(ContractOrderExtDO::getStatus, ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode(), ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode(), ContractStatusEnums.CONTRACT_AUDITSTATUS_DELETE.getCode()).select(ContractOrderExtDO::getId, ContractOrderExtDO::getModify, ContractOrderExtDO::getProjectCategoryCode));
            if (ObjectUtils.isNotEmpty(orderContractDO)) {
                if (1 == orderContractDO.getModify()) {
                    //合同已锁定   不可修改合同
                    throw exception(ErrorCodeConstants.GOMall_Query_Error, "合同已被锁定，暂不可修改合同");
                }
                extDO.setProjectCategoryCode(StringUtils.isNotEmpty(extDO.getProjectCategoryCode()) ? extDO.getProjectCategoryCode() : orderContractDO.getProjectCategoryCode());
                contractOrderExtMapper.updateById(extDO);
                //更新主合同
                //转成产品的合同类型数据(非交易)
                if (ObjectUtil.isNotNull(order)) {
                    ProjectCategoryEnums projectCategoryEnums = ProjectCategoryEnums.getInstance(order.getProjectCategoryCode());
                    if (ObjectUtil.isNotNull(projectCategoryEnums)) {
                        List<ContractType> contractTypes = contractTypeMapper.selectList(ContractType::getPlatId, String.valueOf(projectCategoryEnums.getValue()));
                        if (CollectionUtil.isNotEmpty(contractTypes)) {
                            contractDO.setContractType(contractTypes.get(0).getId());
                        }
                    }
                }


                contractMapper.updateById(contractDO);
                //删除旧付款计划
                paymentScheduleMapper.delete(new LambdaQueryWrapperX<PaymentScheduleDO>().eqIfPresent(PaymentScheduleDO::getContractId, extDO.getId()));
                //删除合同绑定的参数信息（货物类合同履约验收要求信息）
                contractParamFieldMapper.delete(new LambdaQueryWrapperX<OrderContractParamFieldDO>().eqIfPresent(OrderContractParamFieldDO::getContractId, extDO.getId()));

                List<ContractOrderRelDO> contractOrderRelDOS = contractOrderRelMapper.selectList(ContractOrderRelDO::getContractId, extDO.getId());
                if (CollectionUtil.isNotEmpty(contractOrderRelDOS)) {
                    List<String> orderList = contractOrderRelDOS.stream().map(ContractOrderRelDO::getOrderId).collect(Collectors.toList());
                    //京东只能供应商起草，则生成草稿后订单不可再起草合同
                    List<DraftOrderInfoDO> orderInfoDOList = gpMallOrderMapper.selectList(DraftOrderInfoDO::getOrderGuid, orderList);
                    if (CollectionUtil.isNotEmpty(orderInfoDOList)) {
                        Map<String, Object> bodyParam = new HashMap<>();
                        bodyParam.put("client_id", clientId);
                        bodyParam.put("client_secret", clientSecret);
                        String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
                        JSONObject jsonObject = JSONObject.parseObject(token);
                        for (DraftOrderInfoDO orderInfoDO : orderInfoDOList) {
                            if (ObjectUtil.isNotEmpty(orderInfoDO) && ObjectUtil.isNotEmpty(orderInfoDO.getStatus())
                                    && !GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode().equals(orderInfoDO.getStatus())) {
                                orderInfoDO.setStatus(GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode());
                                try {
                                    DraftOrderInfo draftOrderInfo = new DraftOrderInfo();
                                    draftOrderInfo.setOrderGuid(orderInfoDO.getOrderGuid()).setStatus(GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode());
                                    String result = contractProcessApi.updateOrderStatus(jsonObject.getString("access_token"), draftOrderInfo);
                                    JSONObject resultJson = JSONObject.parseObject(result);
                                    if (!"0".equals(resultJson.getString("code"))) {
                                        throw exception(ErrorCodeConstants.DIY_ERROR, result);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    log.error("推送电子合同更新订单状态失败", e);
                                }
                            }
                        }
                        gpMallOrderMapper.updateBatch(orderInfoDOList);
                    }
                }
                //京东只能供应商起草，则生成草稿后订单不可再起草合同
                List<DraftOrderInfoDO> orderInfoDOList = gpMallOrderMapper.selectList(DraftOrderInfoDO::getOrderGuid, gpMallContractCreateReqVO.getOrderIdList());
                if (CollectionUtil.isNotEmpty(orderInfoDOList)) {
                    Map<String, Object> bodyParam = new HashMap<>();
                    bodyParam.put("client_id", clientId);
                    bodyParam.put("client_secret", clientSecret);
                    String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
                    JSONObject jsonObject = JSONObject.parseObject(token);
                    for (DraftOrderInfoDO orderInfoDO : orderInfoDOList) {
                        if (ObjectUtil.isNotEmpty(orderInfoDO) && ObjectUtil.isNotEmpty(orderInfoDO.getStatus())
                                && !GCYOrderStatusEnums.DRAFTED.getCode().equals(orderInfoDO.getStatus())) {
                            orderInfoDO.setStatus(GCYOrderStatusEnums.DRAFTED.getCode());
                            try {
                                DraftOrderInfo draftOrderInfo = new DraftOrderInfo();
                                draftOrderInfo.setOrderGuid(orderInfoDO.getOrderGuid()).setStatus(GCYOrderStatusEnums.DRAFTED.getCode());
                                String result = contractProcessApi.updateOrderStatus(jsonObject.getString("access_token"), draftOrderInfo);
                                JSONObject resultJson = JSONObject.parseObject(result);
                                if (!"0".equals(resultJson.getString("code"))) {
                                    throw exception(ErrorCodeConstants.DIY_ERROR, result);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                log.error("推送电子合同更新订单状态失败", e);
                            }
                        }
                    }
                    gpMallOrderMapper.updateBatch(orderInfoDOList);
                }
            }
        } else {
            //如果是新增,获得订单相关的正常状态的合同
            List<ContractOrderExtDO> orderContractDOS = contractOrderExtMapper.selectList1(gpMallContractCreateReqVO);
            orderContractDOS.forEach(item -> {
                if (item.getContractDrafter().equals(loginUser.getType())) {
                    throw exception(ErrorCodeConstants.GOMall_Query_Error, "此订单已起草合同，请到合同管理查看");
                }
                if (!HLJContractStatusEnums.CONTRACT_AUDITSTATUS_TOBESENT.getCode().equals(item.getStatus())) {
                    throw exception(ErrorCodeConstants.GOMall_Query_Error, "该订单已存在在途合同，请到合同管理查看");
                }
            });
            if (UserTypeEnums.PURCHASER_ORGANIZATION.getCode().equals(loginUser == null ? null : loginUser.getType())) {
                //发起方为采购单位
                extDO.setSendId(extDO.getBuyerOrgId());
                extDO.setRelativeId(extDO.getSupplierId());
            }
            if (UserTypeEnums.SUPPLIER.getCode().equals(loginUser == null ? null : loginUser.getType())) {
                //发起方为供应商
                extDO.setSendId(extDO.getSupplierId());
                extDO.setRelativeId(extDO.getBuyerOrgId());
            }

            //校验订单id不为空
            if (CollectionUtil.isEmpty(gpMallContractCreateReqVO.getOrderIdList())) {
                throw exception(ErrorCodeConstants.ORDER_ID_NULL);
            }
            //新增合同前先校验订单是否还存在
            List<DraftOrderInfoDO> draftOrderInfoDOS = gpMallOrderMapper.selectList(new LambdaQueryWrapperX<DraftOrderInfoDO>().inIfPresent(DraftOrderInfoDO::getOrderGuid, gpMallContractCreateReqVO.getOrderIdList()).eqIfPresent(DraftOrderInfoDO::getOrderStatus, OrderStatusEnums.FINISH.getCode()).select(DraftOrderInfoDO::getProjectCategoryCode));
            if (draftOrderInfoDOS.size() == 0) {
                throw exception(ErrorCodeConstants.DATA_MISSING, "订单不存在");
            }
            String projectCategoryCode = draftOrderInfoDOS.get(0) == null ? null : draftOrderInfoDOS.get(0).getProjectCategoryCode();
            extDO.setProjectCategoryCode(StringUtils.isNotEmpty(projectCategoryCode) ? projectCategoryCode : ProjectCategoryEnums.GOODS.getCode());
            //保存按钮进入，设置合同状态为待发送
            //设置合同起草方
            extDO.setContractDrafter(loginUser == null ? null : loginUser.getType());
            List<SignatoryRelReqVO> signatoryList = new ArrayList<SignatoryRelReqVO>();
            SignatoryRelReqVO supplier = new SignatoryRelReqVO().setType(2).setInitiator(false);

            Relative relative = relativeService.selectById(contractDO.getSupplierId());
            if (ObjectUtil.isNotNull(relative)) {
                supplier.setSignatoryName(relative.getCompanyName()).setSignatoryId(relative.getId()).setUserId(relative.getContactId());
            } else {
                //不维护本地相对方和供应商的情况，直接取订单的供应商信息
                supplier.setSignatoryId(order.getSupplierGuid()).setSignatoryName(order.getSupplierName());
            }
            SignatoryRelReqVO org = new SignatoryRelReqVO().setSignatoryId(loginUser.getOrgId()).setType(1).setInitiator(true).setUserId(loginUser.getId());
            OrganizationDTO organizationDTO = organizationApi.getOrganization(loginUser.getOrgId());
            if (ObjectUtil.isNotNull(organizationDTO)) {
                org.setSignatoryName(organizationDTO.getName()).setSignatoryId(organizationDTO.getId());
            }
            //产品默认采购人是发起方
            signatoryList.add(org);
            signatoryList.add(supplier);
            handlePart(contractDO, signatoryList);
            //信息入合同表
            //转成产品的合同类型数据(非交易)
            if (ObjectUtil.isNotNull(order)) {
                ProjectCategoryEnums projectCategoryEnums = ProjectCategoryEnums.getInstance(order.getProjectCategoryCode());
                if (ObjectUtil.isNotNull(projectCategoryEnums)) {
                    List<ContractType> contractTypes = contractTypeMapper.selectList(ContractType::getPlatId, String.valueOf(projectCategoryEnums.getValue()));
                    if (CollectionUtil.isNotEmpty(contractTypes)) {
                        contractDO.setContractType(contractTypes.get(0).getId());
                    }
                }
            }
            contractMapper.insert(contractDO);
            //绑定相对方关系
            signatoryRelMapper.insert(new SignatoryRelDO().setContractId(contractDO.getId()).setSignatoryId(extDO.getRelativeId()).setType(2));
            //信息入拓展表
            contractOrderExtMapper.insert(extDO.setId(contractDO.getId()));
            log.info("保存合同时间：" + System.currentTimeMillis());
            //修改订单的状态为 已草拟
            //京东只能供应商起草，则生成草稿后订单不可再起草合同
            List<DraftOrderInfoDO> orderInfoDOList = gpMallOrderMapper.selectList(DraftOrderInfoDO::getOrderGuid, gpMallContractCreateReqVO.getOrderIdList());
            Map<String, Object> bodyParam = new HashMap<>();
            bodyParam.put("client_id", clientId);
            bodyParam.put("client_secret", clientSecret);
            String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
            JSONObject jsonObject = JSONObject.parseObject(token);
            if (CollectionUtil.isNotEmpty(orderInfoDOList)) {
                for (DraftOrderInfoDO orderInfoDO : orderInfoDOList) {
                    if (ObjectUtil.isNotEmpty(orderInfoDO) && ObjectUtil.isNotEmpty(orderInfoDO.getStatus())
                            && !GCYOrderStatusEnums.DRAFTED.getCode().equals(orderInfoDO.getStatus())) {
                        orderInfoDO.setStatus(GCYOrderStatusEnums.DRAFTED.getCode());
                        try {
                            DraftOrderInfo draftOrderInfo = new DraftOrderInfo();
                            draftOrderInfo.setOrderGuid(orderInfoDO.getOrderGuid()).setStatus(GCYOrderStatusEnums.DRAFTED.getCode());
                            String result = contractProcessApi.updateOrderStatus(jsonObject.getString("access_token"), draftOrderInfo);
                            JSONObject resultJson = JSONObject.parseObject(result);
                            if (!"0".equals(resultJson.getString("code"))) {
                                throw exception(ErrorCodeConstants.DIY_ERROR, result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("推送电子合同更新订单状态失败", e);
                        }
                    }
                }
                gpMallOrderMapper.updateBatch(orderInfoDOList);
            }
        }

        //添加支付计划表
        if (ObjectUtil.isNotEmpty(gpMallContractCreateReqVO.getPaymentPlanList())) {
            List<PaymentScheduleDO> planDOList = PaymentScheduleConverter.INSTANCE.toPaymentScheduleDOS(gpMallContractCreateReqVO.getPaymentPlanList());
            planDOList.forEach(item -> {
                item.setContractId(extDO.getId());
//                //发起方为采购单位  payee: 供应商id
                item.setPayee(extDO.getSupplierId());
            });
            paymentScheduleMapper.insertBatch(planDOList);
        }
        //添加支付计划表
        if (CollectionUtil.isNotEmpty(gpMallContractCreateReqVO.getPayMentInfo())) {
            List<PaymentScheduleDO> planDOList = PaymentScheduleConverter.INSTANCE.stage2PaymentScheduleDOS2(gpMallContractCreateReqVO.getPayMentInfo());
            planDOList.forEach(item -> {
                item.setContractId(extDO.getId());
//                //发起方为采购单位  payee: 供应商id
                item.setPayee(extDO.getSupplierId());
            });
            paymentScheduleMapper.insertBatch(planDOList);
        }
        //保存合同条款信息
        if (CollectionUtil.isNotEmpty(gpMallContractCreateReqVO.getTerms())) {
            TenantUtils.executeIgnore(() -> {
                //删除旧合同条款
                contractTermMapper.delete(new LambdaQueryWrapperX<ContractTermDO>().eq(ContractTermDO::getContractId, extDO.getId()));
                //保存合同条款信息
                List<ContractTermDO> contractTermDOList = ContractTermConverter.INSTANCE.convertList(gpMallContractCreateReqVO.getTerms());
                for (ContractTermDO contractTermDO : contractTermDOList) {
                    contractTermDO.setContractId(extDO.getId());
                }
                contractTermMapper.insertBatch(contractTermDOList);
            });

        }
        //货物类合同新增合同履约验收要求信息
        setContractParamFieldInfo(gpMallContractCreateReqVO, extDO.getId());
        log.info("合同数据推送成功");

        //更新合同和订单关系
        List<ContractOrderRelDO> relList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(gpMallContractCreateReqVO.getOrderIdList())) {
            for (String orderId : gpMallContractCreateReqVO.getOrderIdList()) {
                ContractOrderRelDO relDO = new ContractOrderRelDO().setOrderId(orderId).setContractId(extDO.getId());
                relList.add(relDO);
            }

            List<String> contractIds = new ArrayList<>();
            contractIds.add(extDO.getId());
            contractOrderRelMapper.delete(new LambdaQueryWrapperX<ContractOrderRelDO>().eq(ContractOrderRelDO::getContractId, extDO.getId()));
            contractOrderRelMapper.insertBatch(relList);
        }
        try {
            // 添加合同标的信息表
            if (ObjectUtil.isNotEmpty(extDO.getOrderId())) {
                List<ContractObjectDO> contractObjectDOS = new ArrayList<>();
                List<GoodsDO> goodsDOSs = gpMallGoodsMapper.selectList(GoodsDO::getOrderId, extDO.getOrderId());
                if (CollectionUtil.isNotEmpty(goodsDOSs)) {
                    goodsDOSs.forEach(detail -> {
                        ContractObjectDO contractObjectDO = new ContractObjectDO();
                        contractObjectDO.setContractId(contractDO.getId())
                                .setContractId(extDO.getId())
                                .setOrderId(extDO.getOrderId())
                                .setObjectName(detail.getGoodsName())
                                .setBrand(detail.getGoodsBrandName())
                                .setRegularType(detail.getGoodsSpecification())
                                .setObjectUnitPrice(detail.getGoodsOnePrice())
                                .setObjectQuantity(Double.valueOf(detail.getQty()))
                                .setUnit(detail.getUnit())
                                .setObjectAmount(detail.getTotalMoney());
                        contractObjectDOS.add(contractObjectDO);
                    });
                }
                contractObjectMapper.insertBatch(contractObjectDOS);
            }
        } catch (Exception e) {
            System.out.println("合同标的信息表添加失败");
        }
        return extDO.getId();
    }

    public void setContractParamFieldInfo(OrderContractCreateReqV2Vo gpMallContractCreateReqVO, String contractId) {
        Boolean flag = gpMallContractCreateReqVO.isAddOrderContractParamFieldDO();
        if (flag) {
            //货物类合同新增合同履约验收要求信息不是全部为空，可以新增
            OrderContractParamFieldDO aDo = ContractParamFieldConverter.INSTANCE.toDO(gpMallContractCreateReqVO);
            if (ObjectUtil.isNotEmpty(aDo)) {
                aDo.setContractId(contractId);
                contractParamFieldMapper.insert(aDo);
            }
        }

    }

    /**
     * 修改富文本格式
     *
     * @param content
     * @return rtftext
     */
    public String regionTemplateCurrencyV3(byte[] content) {

        String templateValue = StringUtils.toEncodedString(content, StandardCharsets.UTF_8);
        Document doc = Jsoup.parse(templateValue);
        doc.getElementsByClass("tagYaoAn").forEach(element -> {
            String tagStr = element.attr("tag");
            if ("drop-down".equals(tagStr.trim())) {
                element.attr("style", "border: none; text-align-last: center;font-size: 16px;color: #3d3d3d;width:auto;padding-left: 2%;padding-right: 2%; -webkit-appearance: none; -moz-appearance: none; appearance: none;");
            }
            doc.getElementsByTag("input").forEach(inputElement -> {
                inputElement.removeAttr("placeholder");
                String style = inputElement.attr("style");
                style = style.replace("border-bottom: 1px solid;", "border-bottom: none;");
                inputElement.attr("style", style);
            });
        });
        doc.getElementsByTag("p").forEach(pElement -> {
            Element uElement = pElement.select("u").first();

            // 获取第一个<u>标签
            if (uElement != null) {
                Elements allElements = uElement.getAllElements();
                String text = allElements.text();
                uElement.replaceWith(new TextNode(text));
            }
        });
        return doc.outerHtml();
    }

    /**
     * 根据订单id获取模板类型id
     *
     * @param tradingType
     * @param platform
     * @return
     */
    public Integer queryModelTypeId(String tradingType, String platform) {
        Integer temptelatype = null;
        if (ObjectUtils.isNotEmpty(tradingType)) {
            Integer orderSource = Integer.valueOf(tradingType);
            if (orderSource == 0) {
                if (ObjectUtils.isEmpty(platform)) {
                    temptelatype = 0;
                } else {
                    switch (PlatformEnum.getInstance(Integer.valueOf(platform))) {
                        case JBG_PLATFORM:
                            temptelatype = 12;
                            break;
                        case SS_UNITED_PLATFORM:
                            temptelatype = 2110;
                            break;
                        case FPG_PLATFORM:
                            temptelatype = 210;
                            break;
                        case HIGH_QUALITY:
                            temptelatype = 4025;
                            break;
                        case HIGH_QUALITY_ZS:
                            temptelatype = 4026;
                            break;
                        case HIGH_QUALITY_DW:
                            temptelatype = 4027;
                            break;
                        case HIGH_QUALITY_ZH:
                            temptelatype = 4028;
                            break;
                        case HIGH_QUALITY_HZ:
                            temptelatype = 4029;
                            break;
                        case HIGH_QUALITY_JM:
                            temptelatype = 4030;
                            break;
                        case GREEN_BUILDING_PURCHASING:
                            temptelatype = 3112;
                            break;
                        case XZLZG:
                            temptelatype = 2012;
                            break;
                        default:
                            temptelatype = 0;
                            break;
                    }
                }
            } else if (orderSource == 1) {
                if (ObjectUtils.isEmpty(platform)) {
                    temptelatype = 1;
                } else {
                    switch (PlatformEnum.getInstance(Integer.valueOf(platform))) {
                        case LAMPS_PLATFORM:
                            temptelatype = 1117;
                            break;
                        default:
                            temptelatype = 1;
                            break;
                    }
                }
            } else if (orderSource == 3) {
                if (ObjectUtils.isEmpty(platform)) {
                    temptelatype = 3;
                } else {
                    switch (PlatformEnum.getInstance(Integer.valueOf(platform))) {
                        case UNION_PLATFORM:
                            temptelatype = 1033;
                            break;
                        default:
                            temptelatype = 3;
                            break;
                    }
                }
            } else if (orderSource == 4) {
                temptelatype = 4;
            } else if (orderSource == 5) {
                temptelatype = 5;
            } else if (orderSource == 6 || orderSource == 2) {
                temptelatype = 2;

            } else if (orderSource == 7) {
                if (ObjectUtils.isEmpty(platform)) {
                    temptelatype = 1;
                } else {
                    switch (PlatformEnum.getInstance(Integer.valueOf(platform))) {
                        case JBG_PLATFORM:
                            temptelatype = 1217;
                            break;
                        default:
                            temptelatype = 1;
                            break;
                    }
                }
            } else if (orderSource == 8) {
                temptelatype = 8;

            } else if (orderSource == 9) {
                temptelatype = 9;

            } else if (orderSource == 12) {
                if (ObjectUtils.isEmpty(platform)) {
                    temptelatype = 236;
                } else {
                    switch (PlatformEnum.getInstance(Integer.valueOf(platform))) {
                        case UNION_PLATFORM:
                            temptelatype = 1032;
                            break;
                        default:
                            temptelatype = 236;
                            break;
                    }
                }
            } else if (orderSource == 13) {
                if (ObjectUtils.isEmpty(platform)) {
                    temptelatype = 621;
                } else {
                    switch (PlatformEnum.getInstance(Integer.valueOf(platform))) {
                        case JBG_PLATFORM:
                            temptelatype = 1216;
                            break;
                        default:
                            temptelatype = 621;
                            break;
                    }
                }
            } else if (orderSource == 14) {
                temptelatype = 1410;

            } else if (orderSource == 15) {
                if (ObjectUtils.isEmpty(platform)) {
                    temptelatype = 1010;
                } else {
                    switch (PlatformEnum.getInstance(Integer.valueOf(platform))) {
                        case UNION_CART_PLATFORM:
                            temptelatype = 1053;
                            break;
                        case UNION_FURNITURE_PLATFORM:
                            temptelatype = 1051;
                            break;
                        case UNION_LAMPS_PLATFORM:
                            temptelatype = 1055;
                            break;
                        case UNION_COUNTRY_REVITALIZE_PLATFORM:
                            temptelatype = 1057;
                            break;
                        default:
                            temptelatype = 1010;
                            break;
                    }
                }
            } else if (orderSource == 16) {
                if (ObjectUtils.isEmpty(platform)) {
                    temptelatype = 1017;
                } else {
                    switch (PlatformEnum.getInstance(Integer.valueOf(platform))) {
                        case UNION_CART_PLATFORM:
                            temptelatype = 1052;
                            break;
                        case UNION_FURNITURE_PLATFORM:
                            temptelatype = 1050;
                            break;
                        case UNION_LAMPS_PLATFORM:
                            temptelatype = 1054;
                            break;
                        case UNION_COUNTRY_REVITALIZE_PLATFORM:
                            temptelatype = 1056;
                            break;
                        default:
                            temptelatype = 1017;
                            break;
                    }
                }
            } else if (orderSource == 17) {
                temptelatype = 405;

            } else if (orderSource == 21) {
                temptelatype = 504;
            }
        }
        return temptelatype;
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
                    contractDO.setPartAId(partAReqVO.getSignatoryId());

                }
                if (partBReqVO != null) {
                    contractDO.setPartBName(partBReqVO.getSignatoryName());
                    contractDO.setPartBId(partBReqVO.getSignatoryId());
                }
                // 更新传入的 signatoryList
                signatoryList.clear();
                signatoryList.addAll(voList);
            }
        }
    }

    @Override
    public ContractDataVo queryById(String id) throws Exception {
        //2.获取可变参数信息

        ContractOrderExtDO orderContractDO = contractOrderExtMapper.selectOne(new LambdaQueryWrapperX<ContractOrderExtDO>().eqIfPresent(ContractOrderExtDO::getId, id).notIn(ContractOrderExtDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_DELETE.getCode()));
        if (BeanUtil.isEmpty(orderContractDO)) {
            throw exception(ErrorCodeConstants.CONTRACT_NAME_NOT_EXISTS);
        }
//        ContractDataVo contractDataVo = OrderContractConverter.INSTANCE.toContractDataVo(orderContractDO);
        ContractDataVo contractDataVo = BeanUtils.toBean(orderContractDO, ContractDataVo.class);
        this.buildReturnInfo(contractDataVo, id);

        ContractDataDTO contractDataDTO = null;
        ContractInfoBackupsDO contractInfoBackupsDO = contractInfoBackupsMapper.selectOne(ContractInfoBackupsDO::getOrderId, orderContractDO.getOrderId());
        if (ObjectUtils.isNotEmpty(contractInfoBackupsDO)) {
            String contractInfo = contractInfoBackupsDO.getContractInfo();
            contractDataDTO = JsonUtils.parseObject(contractInfo, ContractDataDTO.class);
        }
        if (ObjectUtils.isNotEmpty(contractDataDTO)) {
            ContractDTO contractDTO = contractDataDTO == null ? null : contractDataDTO.getContractDTO();
            //获取文件路径
            contractDTO.setPdfFileId(orderContractDO.getPdfFileId());
            String url = orderContractDO.getPdfFileId() == null || orderContractDO.getPdfFileId() == 0 ? null : fileApi.getURL(orderContractDO.getPdfFileId());
            contractDTO.setPdfFilePath(ObjectUtil.isEmpty(url) ? null : url);
            contractDataVo.setContractDTO(contractDTO);
            contractDataVo.setResultMap(contractDataDTO == null ? null : contractDataDTO.getResultMap());
            contractDataVo.setModelId(contractDataDTO == null ? null : contractDataDTO.getModelId());
        }
        //获取支付计划信息
        List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.selectList(PaymentScheduleDO::getContractId, orderContractDO.getId());
        if (CollectionUtil.isNotEmpty(paymentScheduleDOS)) {
            paymentScheduleDOS = paymentScheduleDOS.stream().sorted(Comparator.comparingInt(PaymentScheduleDO::getSort)).collect(Collectors.toList());
//            List<PaymentScheduleVO> vos2 = PaymentScheduleConverter.INSTANCE.toVos2(paymentScheduleDOS);
            List<PaymentScheduleVO> vos2 = BeanUtils.toBean(paymentScheduleDOS, PaymentScheduleVO.class);
            vos2.forEach(item -> {
                if (orderContractDO.getSupplierId().equals(item.getPayee())) {
                    if (StringUtils.isNotBlank(item.getPayee())) {
                        item.setPayee(orderContractDO.getSupplierName());
                    }
                    if (orderContractDO.getBuyerOrgId().equals(item.getPayee())) {
                        item.setPayee(orderContractDO.getBuyerOrgName());
                    }
                }

            });
            contractDataVo.setPaymentScheduleVOList(vos2);
        }
        //获取阶段支付信息
//        List<StagePaymentDO> stagePaymentDOS = stagePaymentMapper.selectList(StagePaymentDO::getContractId, orderContractDO.getId());
//        List<StagePaymentVO> stagePaymentVOS = StagePaymentConverter.INSTANCE.toVOS(stagePaymentDOS);
//        contractDataVo.setPayMentInfo(stagePaymentVOS);
        //获取备案和数据推送失败原因
//        List<PushBackupDataDO> pushBackupDataDO1 = pushBackupDataMapper.selectList(new LambdaQueryWrapperX<PushBackupDataDO>().eqIfPresent(PushBackupDataDO::getContractId, orderContractDO.getId()));
//        Map<String, PushBackupDataDO> backupDataDOMap = CollectionUtils.convertMap(pushBackupDataDO1, PushBackupDataDO::getErrorType);
//        if (ObjectUtil.isNotEmpty(backupDataDOMap)) {
//            //推送失败原因
//            contractDataVo.setPushFailInfo(backupDataDOMap.get(ErrorLogTypeEnums.PUSH_CONTRACT_DATA_ERROR.getCode()) == null ? null : backupDataDOMap.get(ErrorLogTypeEnums.PUSH_CONTRACT_DATA_ERROR.getCode()).getFailInfo());
//            //备案失败原因
//            contractDataVo.setBakFailInfo(backupDataDOMap.get(ErrorLogTypeEnums.BAK_ERROR.getCode()) == null ? null : backupDataDOMap.get(ErrorLogTypeEnums.BAK_ERROR.getCode()).getFailInfo());
//        }
        List<String> orderIds = new ArrayList<String>();
        List<ContractOrderRelDO> relDOList = new ArrayList<>();
        relDOList = contractOrderRelMapper.selectList(ContractOrderRelDO::getContractId, id);
        if (CollectionUtil.isNotEmpty(relDOList)) {
            orderIds = relDOList.stream().map(ContractOrderRelDO::getOrderId).collect(Collectors.toList());
        }
        List<GoodsDO> goodsDOS = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(relDOList)) {
            goodsDOS = gpMallGoodsMapper.selectList(new LambdaQueryWrapperX<GoodsDO>().inIfPresent(GoodsDO::getOrderId, orderIds));
        }
        List<GoodsVO> goodsVOS = GPMallOrderConverter.INSTANCE.toGoodsVOS(goodsDOS);
        contractDataVo.setGoodsVOS(goodsVOS);
        //获取支付计划
        LambdaQueryWrapperX<PaymentScheduleDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.eq(PaymentScheduleDO::getContractId, id);
        List<PaymentScheduleDO> contractPaymentPlanDOList = paymentScheduleMapper.selectList(wrapperX);
//        contractDataVo.setPaymentPlanList(ContractPaymentPlayConverter.INSTANCE.toPlanVo(contractPaymentPlanDOList));
//        List<ContractPaymentPlanVo> contractPaymentPlanVoList = BeanUtils.toBean(contractPaymentPlanDOList, ContractPaymentPlanVo.class);
        List<ContractPaymentPlanVo> contractPaymentPlanVoList = PaymentScheduleConverter.INSTANCE.toPlanVoList(contractPaymentPlanDOList);
        List<StagePaymentVO> payMentInfo = PaymentScheduleConverter.INSTANCE.toPaymentVO(contractPaymentPlanDOList);
        contractDataVo.setPaymentPlanList(contractPaymentPlanVoList);
        contractDataVo.setPayMentInfo(payMentInfo);

        //添加条款信息
        TenantUtils.executeIgnore(() -> {
            List<ContractTermDO> contractTermDOS = contractTermMapper.selectList(ContractTermDO::getContractId, orderContractDO.getId());
            List<TermsDetailsVo> termsDetailsVos = ContractTermConverter.INSTANCE.convertListVO(contractTermDOS);
            contractDataVo.setTerms(termsDetailsVos);
        });
        //添加附件-补充协议
//        List<ContractFileDO> contractFileDOList = contractFileMapper.selectList(new LambdaQueryWrapper<ContractFileDO>()
//                .eq(ContractFileDO::getContractId, contractDataVo.getId())
//                .eq(ContractFileDO::getAttachmentType, AttachmentTypeEnums.OTHER.getCode()));
//        if (ObjectUtil.isNotEmpty(contractFileDOList)) {
//            for (ContractFileDO contractFileDO : contractFileDOList) {
//                contractFileDO.setFileName(fileApi.getName(contractFileDO.getFileId()));
//            }
//            contractDataVo.setContractFileDOList(contractFileDOList);
//        }
        List<OrderRespVO> orderRespVOS = new ArrayList<OrderRespVO>();
        //设置合同分类
        String projectCategoryCode = null;
        List<DraftOrderInfoDO> draftOrderInfoDOS = gpMallOrderMapper.getOrdersByContractId2(id);
        if (CollectionUtil.isNotEmpty(draftOrderInfoDOS)) {
            projectCategoryCode = draftOrderInfoDOS.get(0).getProjectCategoryCode();
            orderRespVOS = GPMallOrderConverter.INSTANCE.listDo2Resp9(draftOrderInfoDOS);
            contractDataVo.setOrderList(orderRespVOS);
        }
        if (ObjectUtil.isNull(projectCategoryCode)) {
            String purCatalogType = goodsDOS.size() == 0 ? null : goodsDOS.get(0).getPurCatalogType();
            projectCategoryCode = purCatalogType == null ? null : PurCatalogTypeEnums.getInstance(purCatalogType).getCode();
        }
        ContractDTO contractDTO = new ContractDTO();
        if (ObjectUtil.isNotNull(contractDataVo.getContractDTO())) {
            contractDTO = contractDataVo.getContractDTO().setProjectCategoryCode(projectCategoryCode);
        } else {
            contractDTO.setProjectCategoryCode(projectCategoryCode);
        }
        ProjectDO projectDO = gpMallProjectMapper.selectOne(new LambdaQueryWrapper<ProjectDO>().eq(ProjectDO::getOrderId, orderContractDO.getOrderId()).last(" limit 1"));
        contractDTO.setProjectName(ObjectUtil.isNotEmpty(projectDO) ? projectDO.getProjectName() : null);
        contractDataVo.setContractDTO(contractDTO);
        return contractDataVo;
    }

    @Override
    public ContractDataVo queryByIdV2(String id) {
        return null;
    }

    private void buildReturnInfo(ContractDataVo contractDataVo, String id) {
//        List<EcmsReturnDraft> returnDrafts = returnDraftMapper.selectList(new LambdaQueryWrapperX<EcmsReturnDraft>().eq(EcmsReturnDraft::getContractId, id).orderByDesc(EcmsReturnDraft::getCreateTime));
//        if (CollectionUtil.isEmpty(returnDrafts)) {
//            return;
//        }
//        contractDataVo.setReturnInfoList(RewriteConverter.INSTANCE.convertList(returnDrafts));
    }

    /**
     * 上传合同
     *
     * @param vo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String uploadContractCreateOrUpdate(UploadContractCreateReqVO vo) throws Exception {
        //政采合同默认结算类型为付款
        if (StringUtils.isNotBlank(vo.getPlatform())) {
            vo.setAmountType(AmountTypeEnums.PAY.getCode());
        }

        //是否签署完成的合同
        List<Integer> statusList = new ArrayList<>();
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_DELETE.getCode());
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode());
        statusList.add(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode());
        if (CollectionUtil.isNotEmpty(vo.getOrderIdList())) {
            List<UploadContractOrderExtDO> contractInfoList = uploadContractOrderExtMapper.selectList4Update(vo);

            if (StringUtils.isNotEmpty(vo.getId())) {
                contractInfoList = contractInfoList.stream().filter(contractUploadDO -> !vo.getId().equals(contractUploadDO.getId())).collect(Collectors.toList());
            }
            contractInfoList = contractInfoList.stream().filter(item -> !statusList.contains(item.getStatus())).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(contractInfoList)) {
                throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "订单已存在签署完成的合同:" + contractInfoList.get(0).getName());
            }
        }

        if (StringUtils.isNotEmpty(vo.getBidGuid()) && StringUtils.isNotEmpty(vo.getBuyPlanId())) {
            List<UploadContractOrderExtDO> contractUploadDOList = uploadContractOrderExtMapper.selectList(new LambdaQueryWrapperX<UploadContractOrderExtDO>().select(UploadContractOrderExtDO::getId, UploadContractOrderExtDO::getStatus, UploadContractOrderExtDO::getName).eq(UploadContractOrderExtDO::getBuyPlanPackageId, vo.getBidGuid()).eq(UploadContractOrderExtDO::getBuyPlanId, vo.getBuyPlanId()));
            List<UploadContractOrderExtDO> contractPlanList = contractUploadDOList.stream().filter(contractUploadDO -> StringUtils.isNotEmpty(vo.getId()) && vo.getId().equals(contractUploadDO.getId())).collect(Collectors.toList());
            contractPlanList = contractPlanList.stream().filter(item -> !statusList.contains(item.getStatus())).collect(Collectors.toList());
            if (ObjectUtil.isNotEmpty(contractPlanList)) {
                throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "订单已存在签署完成的合同:" + contractUploadDOList.get(0).getName());
            }
        }

        UploadContractOrderExtDO contractDO = UploadContractOrderExtConverter.INSTANCE.uploadVOtoUploadDO(vo);
        //添加区划
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        //添加起草方
        contractDO.setContractDrafter(loginUser.getType());
        String regionCode = "";
        if (ObjectUtil.isNotEmpty(loginUser.getRegionCode())) {
            regionCode = loginUser.getRegionCode();
        } else {
            regionCode = organizationApi.getOrgRegionCodeByOrgId(loginUser.getOrgId());
        }
        if (StringUtils.isNotEmpty(regionCode)) {
            contractDO.setRegionCode(regionCode);
        }
        if (ObjectUtil.isEmpty(contractDO.getClauseSecret())) {
            contractDO.setClauseSecret(0);
        }
        if (ObjectUtil.isEmpty(contractDO.getAllowMortgage())) {
            contractDO.setAllowMortgage("0");
        }
        if (ObjectUtil.isEmpty(contractDO.getIsRetentionMoney())) {
            contractDO.setIsRetentionMoney(0);
        }
        if (ObjectUtil.isEmpty(contractDO.getIsPerformanceMoney())) {
            contractDO.setIsPerformanceMoney(0);
        }
        if (CollectionUtil.isNotEmpty(vo.getPaymentPlanList())) {
            List<UploadContractPaymentPlanVo> dos = vo.getPaymentPlanList();
            List<PaymentScheduleDO> planDOs = PaymentScheduleConverter.INSTANCE.listUploadPayment2PlanDO(dos);
            //支付时间递增
            Date payDate = null;
            for (PaymentScheduleDO scheduleDO : planDOs) {
                if (ObjectUtil.isNotNull(payDate) && payDate.after(scheduleDO.getPaymentTime())) {
                    throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "支付计划时间必须递增");
                }
                if (ObjectUtil.isNotNull(scheduleDO.getPaymentTime())) {
                    payDate = scheduleDO.getPaymentTime();
                } else {
                    String stagePayDate = scheduleDO.getPaymentDate();
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                    payDate = format.parse(stagePayDate);
                }
                if (ObjectUtil.isNull(payDate) || ObjectUtil.isEmpty(payDate)) {
                    throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "支付计划时间不能为空");
                }
//            if (play.getPayDate().before(contractDO.getStartDate()) || play.getPayDate().after(contractDO.getEndDate())) {
//                throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "第" + play.getPeriods() + "期合同支付计划时间有误，请检查");
//            }
            }
            //判断总金额与合同总金额
            BigDecimal sum = dos.stream().map(UploadContractPaymentPlanVo::getMoney).map(BigDecimal::valueOf).reduce(BigDecimal.ZERO, BigDecimal::add);
            if (sum.compareTo(vo.getTotalMoney()) != 0) {
                throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "付款计划总金额与合同金额不一致");
            }
            //判断总比例
            double sumProportion = dos.stream().mapToDouble(UploadContractPaymentPlanVo::getPayProportion).sum();
            if (sumProportion != 100) {
                throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "付款计划总比例错误");
            }
        }
        // platform: gpms-gpx-5.3
        if (PlatformEnums.GPMS_GPX.getCode().equals(vo.getPlatform()) && StringUtils.isEmpty(vo.getId())) {
            PackageInfoDO packageInfoDO = packageInfoMapper.selectOne(PackageInfoDO::getPackageGuid, vo.getBidGuid());
            PlanInfoDO planInfoDO = planInfoMapper.selectOne(new LambdaQueryWrapperX<PlanInfoDO>().eq(PlanInfoDO::getProjectId, packageInfoDO.getProjectGuid()).last(" limit 1"));
            //合同金额必须等于包的中标金额
//            if(contractDO.getTotalMoney().compareTo(BigDecimal.valueOf(packageInfoDO.getWinBidAmount())) != 0){
//                throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "合同金额必须等于包的中标金额");
//            }
            contractDO.setTotalMoney(BigDecimal.valueOf(packageInfoDO.getWinBidAmount()));
            contractDO.setSourceCode(ObjectUtil.isNotEmpty(planInfoDO) ? planInfoDO.getSourceCode() : null);
            contractDO.setBuyPlanPackageId(contractDO.getBidGuid());
            contractDO.setProjectGuid(packageInfoDO.getProjectGuid());
            contractDO.setProjectCategoryCode(packageInfoDO.getProjectType());
        }
        contractDO.setRemark9("99");
        //设置相对方id和采购人id
        if (PlatformEnums.GPMS_GPX.getCode().equals(vo.getPlatform()) && StringUtils.isNotEmpty(vo.getId())) {
            UploadContractOrderExtDO uploadDO = uploadContractOrderExtMapper.selectOne(new LambdaQueryWrapperX<UploadContractOrderExtDO>().select(UploadContractOrderExtDO::getBidGuid).eq(UploadContractOrderExtDO::getId, vo.getId()));
            PackageInfoDO packageInfoDO = packageInfoMapper.selectOne(PackageInfoDO::getPackageGuid, uploadDO.getBidGuid());
//            if(contractDO.getTotalMoney().compareTo(BigDecimal.valueOf(packageInfoDO.getWinBidAmount())) != 0){
//                throw exception(ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY, "合同金额必须等于包的中标金额");
//            }
            PlanInfoDO planInfoDO = planInfoMapper.selectOne(new LambdaQueryWrapperX<PlanInfoDO>().eq(PlanInfoDO::getProjectId, packageInfoDO.getProjectGuid()).last(" limit 1"));
            contractDO.setTotalMoney(BigDecimal.valueOf(packageInfoDO.getWinBidAmount()));
            contractDO.setSourceCode(ObjectUtil.isNotEmpty(planInfoDO) ? planInfoDO.getSourceCode() : null);
            contractDO.setBuyPlanPackageId(contractDO.getBidGuid());
            contractDO.setProjectGuid(packageInfoDO.getProjectGuid());
        }
        if (CollectionUtil.isNotEmpty(vo.getOrderIdList())) {
            //交易关联包需要校验
            // List<DraftOrderInfoDO> draftOrderInfoDOs = gpMallOrderMapper.selectOne4Check(vo);
//            DraftOrderInfoDO draftOrderInfoDO = gpMallOrderMapper.selectOne(new LambdaQueryWrapperX<DraftOrderInfoDO>()
//                    .eq(DraftOrderInfoDO::getOrderGuid, contractDO.getOrderId()).orderByDesc(DraftOrderInfoDO::getCreateTime).last(" limit 1"));
            MPJLambdaWrapper<DraftOrderInfoDO> mpjLambdaWrapper = new MPJLambdaWrapper<DraftOrderInfoDO>()
                    // .leftJoin(ContractOrderRelDO.class, ContractOrderRelDO::getOrderId, DraftOrderInfoDO::getOrderGuid)
                    .select(DraftOrderInfoDO::getId, DraftOrderInfoDO::getOrderGuid, DraftOrderInfoDO::getStatus,
                            DraftOrderInfoDO::getPurchaserOrgGuid, DraftOrderInfoDO::getPurchaserOrg,
                            DraftOrderInfoDO::getSupplierGuid, DraftOrderInfoDO::getSupplierName,
                            DraftOrderInfoDO::getOrderCode, DraftOrderInfoDO::getProjectCategoryCode,
                            DraftOrderInfoDO::getOrderStatus, DraftOrderInfoDO::getCreateTime)
                    .distinct();
            mpjLambdaWrapper.in(DraftOrderInfoDO::getOrderGuid, vo.getOrderIdList()).orderByDesc(DraftOrderInfoDO::getCreateTime).last(" limit 1");
            List<DraftOrderInfoDO> draftOrderInfoDOs = gpMallOrderMapper.selectList(mpjLambdaWrapper);
            if (CollectionUtil.isNotEmpty(draftOrderInfoDOs)) {
                DraftOrderInfoDO draftOrderInfoDO = draftOrderInfoDOs.get(0);
                //电子卖场修改状态gpms-gpx-5.3   协议定点
                contractDO.setBuyerOrgId(draftOrderInfoDO.getPurchaserOrgGuid());
                contractDO.setBuyerOrgName(draftOrderInfoDO.getPurchaserOrg());
                contractDO.setSupplierId(draftOrderInfoDO.getSupplierGuid());
                contractDO.setSupplierName(draftOrderInfoDO.getSupplierName());
                //TODO 多个订单的编号，只能够先存入第一个订单的编号
                contractDO.setOrderCode(draftOrderInfoDO.getOrderCode());
                contractDO.setOrderId(draftOrderInfoDO.getOrderGuid());
                contractDO.setProjectCategoryCode(ObjectUtil.isNotEmpty(draftOrderInfoDO.getProjectCategoryCode()) ? draftOrderInfoDO.getProjectCategoryCode() : PurCatalogTypeEnums.GOODS.getKey());
            }
        } else {
            //供应商id保存合同表，流程执行人使用
            contractDO.setSupplierId(vo.getSupplierId());
            contractDO.setSupplierName(vo.getSupplierName());
            contractDO.setBuyerOrgId(vo.getPurchaser());
            contractDO.setBuyerOrgName(vo.getPurchaserName());
        }
        if (UserTypeEnums.PURCHASER_ORGANIZATION.getCode().equals(loginUser == null ? null : loginUser.getType())) {
            //发起方为采购单位
            contractDO.setSendId(contractDO.getBuyerOrgId());
            contractDO.setRelativeId(contractDO.getSupplierId());
        }
        if (UserTypeEnums.SUPPLIER.getCode().equals(loginUser == null ? null : loginUser.getType())) {
            //发起方为供应商
            contractDO.setSendId(contractDO.getSupplierId());
            contractDO.setRelativeId(contractDO.getBuyerOrgId());
        }
        //设置合同起草方
        contractDO.setContractDrafter(loginUser == null ? null : loginUser.getType());
        //校验名称,编码是否重复
//        DBExistUtil.isCodeExist(contractDO.getId(), contractDO.getCode(), orderContractUploadMapper);
        //付款计划校验
        //计划支付时间在合同生效期.合同终止时间之间
        SupplyDTO supply = supplyApi.getSupply(vo.getSupplierId());
        if (ObjectUtil.isNull(supply)) {
            supply = hljSupplyService.getSupply(vo.getSupplierId());
        }
        if (ObjectUtil.isNotEmpty(supply)) {
            contractDO.setBankName(StringUtils.isNotEmpty(vo.getBankName()) ? vo.getBankName() : supply.getBankName());
            contractDO.setBankAccount(StringUtils.isNotEmpty(vo.getBankAccount()) ? vo.getBankAccount() : supply.getBankAccount());
        }

        //设置合同状态
        if (vo.getSend() == 0) {
            //保存按钮进入，状态为待发送
            contractDO.setStatus(ContractStatusEnums.TO_BE_SENT.getCode());
        } else if (vo.getSend() == 1) {
            //提交按钮
            contractDO.setStatus(ContractStatusEnums.SENT.getCode());
        } else if (vo.getSend() == 2) {
            //确认按钮
            contractDO.setStatus(ContractStatusEnums.TO_BE_SIGNED.getCode());
        } else if (vo.getSend() == 11) {
            // 进入草稿箱
            contractDO.setStatus(ContractStatusEnums.TO_BE_CHECK.getCode());
        }
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.getMonthValue();
        int day = currentDate.getDayOfMonth();
        String path = year + "/" + month + "/" + day + "/";
        if (ObjectUtils.isNotEmpty(contractDO.getId())) {
            UploadContractOrderExtDO orderContractDO = uploadContractOrderExtMapper.selectById(contractDO.getId());
            if (ObjectUtil.isNotEmpty(orderContractDO)) {
                //删除就文件 保存新文件
                //判断合同是否是pdf文件
                MultipartFile file = vo.getFile();
                if (ObjectUtils.isNotEmpty(file)) {
                    String filename = file.getOriginalFilename();
                    if (!StringUtils.equals(SUFFIX_PDF, FileNameUtil.extName(filename).toLowerCase())) {
                        throw exception(ErrorCodeConstants.FILE_NAME_NOT_RIGHT);
                    }
//                        if(ObjectUtil.isNotEmpty(orderContractDO.getPdfFileId()) && orderContractDO.getPdfFileId() != 0){

//                        FileDTO fileDTO = fileApi.selectById(orderContractDO.getPdfFileId());
//                        if (fileDTO != null) {
//                            fileApi.deleteFile(orderContractDO.getPdfFileId());
//                        }
//                        }
                    //上传文件
                    try {
                        Long fileId = fileApi.uploadFile(filename, FileUploadPathEnum.CONTRACT_DRAFT.getPath() + path + UUID.randomUUID() + contractDO.getCode() + "_" + filename, file.getBytes());
                        contractDO.setPdfFileId(fileId);
                    } catch (Exception e) {
                        throw exception(ErrorCodeConstants.FILE_UPLOAD_ERROR);
                    }
                }
                //编辑

                uploadContractOrderExtMapper.updateById(contractDO);
                ContractDO contractDO1 = ContractConverter.INSTANCE.ext2DO(contractDO);

                //转成产品的合同类型数据(非交易)
                if (ObjectUtil.isNotNull(contractDO.getProjectCategoryCode())) {
                    ProjectCategoryEnums projectCategoryEnums = ProjectCategoryEnums.getInstance(contractDO.getProjectCategoryCode());
                    if (ObjectUtil.isNotNull(projectCategoryEnums)) {
                        List<ContractType> contractTypes = contractTypeMapper.selectList(ContractType::getPlatId, String.valueOf(projectCategoryEnums.getValue()));
                        if (CollectionUtil.isNotEmpty(contractTypes)) {
                            contractDO1.setContractType(contractTypes.get(0).getId());
                        }
                    }
                }
                contractMapper.updateById(contractDO1);
                if (vo.getSend() == 1) {
                    BpmContractCreateReqVO bpmContractCreateReqVO = new BpmContractCreateReqVO();
                    bpmContractCreateReqVO.setId(contractDO1.getId());
                    bpmContractCreateReqVO.setReason("");
                    bpmContractService.createProcess(getLoginUserId(), bpmContractCreateReqVO);
                }
                //绑定合同和订单关系
                List<ContractOrderRelDO> relList = new ArrayList<>();
                if (CollectionUtil.isNotEmpty(vo.getOrderIdList())) {
                    for (String orderId : vo.getOrderIdList()) {
                        ContractOrderRelDO relDO = new ContractOrderRelDO().setOrderId(orderId).setContractId(contractDO.getId());
                        relList.add(relDO);
                    }
                    contractOrderRelMapper.delete(ContractOrderRelDO::getContractId, contractDO.getId());
                    contractOrderRelMapper.insertBatch(relList);
                }
                //非交易同步订单状态
                //todo 后续需要拿到和后面类似的方法逻辑放到一起
                if (!PlatformEnums.GPMS_GPX.getCode().equals(vo.getPlatform())) {
                    List<DraftOrderInfoDO> orderInfoDOList = gpMallOrderMapper.selectListByContractIdAndOrderIds(contractDO.getId(), vo.getOrderIdList());
                    if (CollectionUtil.isNotEmpty(orderInfoDOList)) {
                        Map<String, Object> bodyParam = new HashMap<>();
                        bodyParam.put("client_id", clientId);
                        bodyParam.put("client_secret", clientSecret);
                        String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
                        JSONObject jsonObject = JSONObject.parseObject(token);
                        for (DraftOrderInfoDO orderInfoDO : orderInfoDOList) {
                            if (vo.getOrderIdList().contains(orderInfoDO.getOrderGuid())) {
                                if (ObjectUtil.isNotEmpty(orderInfoDO) && ObjectUtil.isNotEmpty(orderInfoDO.getStatus())
                                        && !GCYOrderStatusEnums.DRAFTED.getCode().equals(orderInfoDO.getStatus())) {
                                    orderInfoDO.setStatus(GCYOrderStatusEnums.DRAFTED.getCode());
                                    try {
                                        DraftOrderInfo draftOrderInfo = new DraftOrderInfo();
                                        draftOrderInfo.setOrderGuid(orderInfoDO.getOrderGuid()).setStatus(GCYOrderStatusEnums.DRAFTED.getCode());
                                        String result = contractProcessApi.updateOrderStatus(jsonObject.getString("access_token"), draftOrderInfo);
                                        JSONObject resultJson = JSONObject.parseObject(result);
                                        if (!"0".equals(resultJson.getString("code"))) {
                                            throw exception(ErrorCodeConstants.DIY_ERROR, result);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        log.error("推送电子合同更新订单状态失败", e);
                                    }
                                }
                            } else {
                                if (ObjectUtil.isNotEmpty(orderInfoDO) && ObjectUtil.isNotEmpty(orderInfoDO.getStatus())
                                        && !GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode().equals(orderInfoDO.getStatus())) {
                                    orderInfoDO.setStatus(GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode());
                                    try {
                                        DraftOrderInfo draftOrderInfo = new DraftOrderInfo();
                                        draftOrderInfo.setOrderGuid(orderInfoDO.getOrderGuid()).setStatus(GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode());
                                        String result = contractProcessApi.updateOrderStatus(jsonObject.getString("access_token"), draftOrderInfo);
                                        JSONObject resultJson = JSONObject.parseObject(result);
                                        if (!"0".equals(resultJson.getString("code"))) {
                                            throw exception(ErrorCodeConstants.DIY_ERROR, result);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        log.error("推送电子合同更新订单状态失败", e);
                                    }
                                }
                            }
                        }
                        gpMallOrderMapper.updateBatch(orderInfoDOList);
                    }
                }
                //先清空计划
                paymentScheduleMapper.delete(new LambdaQueryWrapperX<PaymentScheduleDO>().eq(PaymentScheduleDO::getContractId, contractDO.getId()));
                //付款计划表
                if (CollectionUtil.isNotEmpty(vo.getPaymentPlanList())) {
                    List<PaymentScheduleDO> planDO = PaymentScheduleConverter.INSTANCE.listUploadPayment2PlanDO(vo.getPaymentPlanList());
                    planDO.forEach(item -> item.setContractId(contractDO.getId()));
                    paymentScheduleMapper.insertBatch(planDO);
                }

            }
        } else {
            //新增  文件不能为空
            //判断合同是否是pdf文件
            MultipartFile file = vo.getFile();
            if (ObjectUtils.isNotEmpty(file)){
                String filename = file.getOriginalFilename();
                if (!StringUtils.equals(SUFFIX_PDF, FileNameUtil.extName(filename).toLowerCase())) {
                    throw exception(ErrorCodeConstants.FILE_NAME_NOT_RIGHT);
                }
                //上传文件
                try {
                    Long fileId = fileApi.uploadFile(filename, FileUploadPathEnum.CONTRACT_DRAFT.getPath() + path + UUID.randomUUID() + contractDO.getCode() + "_" + filename, file.getBytes());
                    contractDO.setPdfFileId(fileId);
                } catch (Exception e) {
                    throw exception(ErrorCodeConstants.FILE_UPLOAD_ERROR);
                }
            }
//            if (ObjectUtils.isEmpty(file)) {
//                throw exception(ErrorCodeConstants.FILE_NAME_NOT_NULL);
//            }
//            String filename = file.getOriginalFilename();
//            if (!StringUtils.equals(SUFFIX_PDF, FileNameUtil.extName(filename).toLowerCase())) {
//                throw exception(ErrorCodeConstants.FILE_NAME_NOT_RIGHT);
//            }
//            //上传文件
//            try {
//                Long fileId = fileApi.uploadFile(filename, FileUploadPathEnum.CONTRACT_DRAFT.getPath() + path + UUID.randomUUID() + contractDO.getCode() + "_" + filename, file.getBytes());
//                contractDO.setPdfFileId(fileId);
//            } catch (Exception e) {
//                throw exception(ErrorCodeConstants.FILE_UPLOAD_ERROR);
//            }
            //信息入合同表
            contractDO.setSupplierSize(StringUtils.isEmpty(vo.getSupplierSize())?"0":contractDO.getSupplierSize());
            contractDO.setSupplierFeatures(StringUtils.isEmpty(vo.getSupplierFeatures())?"0":contractDO.getSupplierFeatures());
            uploadContractOrderExtMapper.insert(contractDO);
            ContractDO contractDO1 = ContractConverter.INSTANCE.ext2DO(contractDO);
            //默认合同主表状态
            contractDO1.setStatus(ContractStatusEnums.TO_BE_CHECK.getCode());
            List<SignatoryRelReqVO> signatoryList = new ArrayList<SignatoryRelReqVO>();
            SignatoryRelReqVO supplier = new SignatoryRelReqVO().setType(2).setInitiator(false);
            Relative relative = relativeService.selectById(contractDO.getSupplierId());
            if (ObjectUtil.isNotNull(relative)) {
                supplier.setSignatoryName(relative.getCompanyName()).setSignatoryId(relative.getId()).setUserId(relative.getContactId());
            } else {
                supplier.setSignatoryName(contractDO.getSupplierName()).setSignatoryId(contractDO.getSupplierId()).setUserId(0L);
            }
            SignatoryRelReqVO org = new SignatoryRelReqVO().setSignatoryId(loginUser.getOrgId()).setType(1).setInitiator(true).setUserId(loginUser.getId());
            OrganizationDTO organizationDTO = organizationApi.getOrganization(loginUser.getOrgId());
            if (ObjectUtil.isNotNull(organizationDTO)) {
                org.setSignatoryName(organizationDTO.getName()).setSignatoryId(organizationDTO.getId());
            }
            //产品默认采购人是发起方
            signatoryList.add(org);
            signatoryList.add(supplier);
            handlePart(contractDO1, signatoryList);
            //信息入合同表
            //转成产品的合同类型数据(非交易)
            if (ObjectUtil.isNotNull(contractDO.getProjectCategoryCode())) {
                ProjectCategoryEnums projectCategoryEnums = ProjectCategoryEnums.getInstance(contractDO.getProjectCategoryCode());
                if (ObjectUtil.isNotNull(projectCategoryEnums)) {
                    List<ContractType> contractTypes = contractTypeMapper.selectList(ContractType::getPlatId, String.valueOf(projectCategoryEnums.getValue()));
                    if (CollectionUtil.isNotEmpty(contractTypes)) {
                        contractDO1.setContractType(contractTypes.get(0).getId());
                    }
                }
            }
            contractDO1.setUpload(ORDER_DRAFT.getCode());
            if (ObjectUtil.isNotEmpty(vo.getUploadFileAiId())) {
                contractDO1.setUploadFileAiId(vo.getUploadFileAiId());
            }
            contractMapper.insert(contractDO1);
            //绑定相对方关系
            SignatoryRelDO signatoryDO = new SignatoryRelDO().setContractId(contractDO.getId()).setSignatoryId(contractDO.getRelativeId()).setType(2);
            signatoryRelMapper.insert(signatoryDO);
            if (vo.getSend() == 1) {
                BpmContractCreateReqVO bpmContractCreateReqVO = new BpmContractCreateReqVO();
                bpmContractCreateReqVO.setId(contractDO1.getId());
                bpmContractCreateReqVO.setReason("");
                bpmContractService.createProcess(getLoginUserId(), bpmContractCreateReqVO);
            }


            //contractMapper.insert(contractDO1.setUpload(ORDER_DRAFT.getCode()));
            //生成草稿后订单不可再起草合同
            List<DraftOrderInfoDO> draftOrderInfoDOs = gpMallOrderMapper.getOrdersByContractId(contractDO.getId());
            if (CollectionUtil.isNotEmpty(draftOrderInfoDOs)) {
                Map<String, Object> bodyParam = new HashMap<>();
                bodyParam.put("client_id", clientId);
                bodyParam.put("client_secret", clientSecret);
                String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
                JSONObject jsonObject = JSONObject.parseObject(token);
                for (DraftOrderInfoDO draftOrderInfoDO : draftOrderInfoDOs) {
                    if (ObjectUtil.isNotEmpty(draftOrderInfoDO) && ObjectUtil.isNotEmpty(draftOrderInfoDO.getStatus())
                            && !GCYOrderStatusEnums.DRAFTED.getCode().equals(draftOrderInfoDO.getStatus())) {
                        draftOrderInfoDO.setStatus(GCYOrderStatusEnums.DRAFTED.getCode());
                        try {
                            DraftOrderInfo draftOrderInfo = new DraftOrderInfo();
                            draftOrderInfo.setOrderGuid(draftOrderInfoDO.getOrderGuid()).setStatus(GCYOrderStatusEnums.DRAFTED.getCode());
                            String result = contractProcessApi.updateOrderStatus(jsonObject.getString("access_token"), draftOrderInfo);
                            JSONObject resultJson = JSONObject.parseObject(result);
                            if (!"0".equals(resultJson.getString("code"))) {
                                throw exception(ErrorCodeConstants.DIY_ERROR, result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("推送电子合同更新订单状态失败", e);
                        }
                    }
                }
                gpMallOrderMapper.updateBatch(draftOrderInfoDOs);
            }
            //新增-绑定合同和订单关系
            List<ContractOrderRelDO> relList = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(vo.getOrderIdList())) {
                for (String orderId : vo.getOrderIdList()) {
                    ContractOrderRelDO relDO = new ContractOrderRelDO().setOrderId(orderId).setContractId(contractDO.getId());
                    relList.add(relDO);
                }
                contractOrderRelMapper.insertBatch(relList);
            }
            //起草的订单状态同步
            List<DraftOrderInfoDO> orderInfoDOList = gpMallOrderMapper.selectList(DraftOrderInfoDO::getOrderGuid, vo.getOrderIdList());
            if (CollectionUtil.isNotEmpty(orderInfoDOList)) {
                Map<String, Object> bodyParam = new HashMap<>();
                bodyParam.put("client_id", clientId);
                bodyParam.put("client_secret", clientSecret);
                String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
                JSONObject jsonObject = JSONObject.parseObject(token);
                for (DraftOrderInfoDO orderInfoDO : orderInfoDOList) {
                    if (ObjectUtil.isNotEmpty(orderInfoDO) && ObjectUtil.isNotEmpty(orderInfoDO.getStatus())
                            && !GCYOrderStatusEnums.DRAFTED.getCode().equals(orderInfoDO.getStatus())) {
                        orderInfoDO.setStatus(GCYOrderStatusEnums.DRAFTED.getCode());
                        try {
                            DraftOrderInfo draftOrderInfo = new DraftOrderInfo();
                            draftOrderInfo.setOrderGuid(orderInfoDO.getOrderGuid()).setStatus(GCYOrderStatusEnums.DRAFTED.getCode());
                            String result = contractProcessApi.updateOrderStatus(jsonObject.getString("access_token"), draftOrderInfo);
                            JSONObject resultJson = JSONObject.parseObject(result);
                            if (!"0".equals(resultJson.getString("code"))) {
                                throw exception(ErrorCodeConstants.DIY_ERROR, result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("推送电子合同更新订单状态失败", e);
                        }
                    }
                }
                gpMallOrderMapper.updateBatch(orderInfoDOList);
            }
        }

        //添加付款计划
        //先清空计划
        paymentScheduleMapper.delete(new LambdaQueryWrapperX<PaymentScheduleDO>().eq(PaymentScheduleDO::getContractId, contractDO.getId()));
        //付款计划表

        if (CollectionUtil.isNotEmpty(vo.getPaymentPlanList())) {
            List<PaymentScheduleDO> planDO = PaymentScheduleConverter.INSTANCE.listUploadPayment2PlanDO(vo.getPaymentPlanList());
            planDO.forEach(item -> item.setContractId(contractDO.getId()));
            paymentScheduleMapper.insertBatch(planDO);
        }
        //带期限的付款计划
        if (CollectionUtil.isNotEmpty(vo.getStagePaymentList())) {
            List<PaymentScheduleDO> stagePaymentDOS = PaymentScheduleConverter.INSTANCE.listUploadStage2PlanDO(vo.getStagePaymentList());
            stagePaymentDOS.forEach(item -> item.setContractId(contractDO.getId()));
            paymentScheduleMapper.insertBatch(stagePaymentDOS);
        }

        if (PlatformEnums.GPMS_GPX.getCode().equals(vo.getPlatform())) {
            tradingSupplierMapper.delete(new LambdaQueryWrapperX<TradingSupplierDO>().eq(TradingSupplierDO::getContractId, contractDO.getId()));
            TradingSupplierDO tradingSupplierDO = new TradingSupplierDO();
            if (ObjectUtil.isNotEmpty(supply)) {
//                BeanUtils.copyProperties(supply, tradingSupplierDO);
                tradingSupplierDO.setLegalRepresentative(supply.getLegalPerson());
                tradingSupplierDO.setBankAccount(StringUtils.isNotEmpty(vo.getBankAccount()) ? vo.getBankAccount() : supply.getBankAccount());
                tradingSupplierDO.setBankName(StringUtils.isNotEmpty(vo.getBankName()) ? vo.getBankName() : supply.getBankName());
                tradingSupplierDO.setSupplierPayAmount(vo.getTotalMoney());
            }
            tradingSupplierDO.setContractId(contractDO.getId());
            tradingSupplierDO.setSupplierId(contractDO.getSupplierId());
            tradingSupplierDO.setSupplierName(contractDO.getSupplierName());
            tradingSupplierMapper.insert(tradingSupplierDO);
        }


        //是否发起审批
        if (ObjectUtil.isNotEmpty(vo.getSend()) && vo.getSend() == 1) {
            if (PlatformEnums.ZHUBAJIE.getCode().equals(contractDO.getPlatform())) {
                DraftOrderInfoDO draftOrderInfoDO = gpMallOrderMapper.selectOne(DraftOrderInfoDO::getOrderGuid, contractDO.getOrderId());
                if (ObjectUtils.isNotEmpty(draftOrderInfoDO) && ObjectUtil.isNotEmpty(draftOrderInfoDO.getStatus())
                        && !GCYOrderStatusEnums.DRAFTED.getCode().equals(draftOrderInfoDO.getStatus())) {
                    draftOrderInfoDO.setStatus(GCYOrderStatusEnums.DRAFTED.getCode());

                    gpMallOrderMapper.updateById(draftOrderInfoDO);
                }
            }
            contractDO.setSendTime(new Date());

//            String process = flowService.createJingdongContractProcessInstance(contractDO.getId());
//            contractDO.setProcessInstanceId(process);
//            contractOrderExtMapper.updateById(contractDO);
        } else if (ObjectUtil.isNotEmpty(vo.getSend()) && vo.getSend() == 2) {
//            OrderContractExtDO approveDO = orderContractMapper.selectOne(new LambdaQueryWrapperX<OrderContractDO>()
//                    .select(OrderContractDO::getId, OrderContractDO::getBuyerOrgId, OrderContractDO::getSupplierId)
//                    .eq(OrderContractDO::getId, contractDO.getId()).last(" limit 1"));
//            Map<String, Object> processInstanceVariables = new HashMap<>();
//            processInstanceVariables.put("buyerOrgId", approveDO.getBuyerOrgId());
//            processInstanceVariables.put("supplierId", approveDO.getSupplierId());
//            String processInstanceId = processInstanceApi.createProcessInstance(getLoginUserId(),
//                    new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(TRADING_PROCESS_KEY)
//                            .setVariables(processInstanceVariables)
//                            .setBusinessKey(contractDO.getId()));
//            contractDO.setSendTime(new Date());
//            contractDO.setStatus(2);
//            contractDO.setProcessInstanceId(processInstanceId);
//            orderContractUploadMapper.updateById(contractDO);
        }

        //隐藏包信息
        if (PlatformEnums.GPMS_GPX.getCode().equals(contractDO.getPlatform())) {
            PackageInfoDO packageInfoDO = packageInfoMapper.selectOne(new LambdaUpdateWrapper<PackageInfoDO>()
                    .eq(PackageInfoDO::getPackageGuid, contractDO.getBuyPlanPackageId()));
            if (ObjectUtil.isNotEmpty(packageInfoDO) && packageInfoDO.getHidden() != 1) {
                packageInfoMapper.update(null, new LambdaUpdateWrapper<PackageInfoDO>()
                        .eq(PackageInfoDO::getPackageGuid, vo.getBidGuid())
                        .set(PackageInfoDO::getHidden, 1));
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
                        throw exception(ErrorCodeConstants.DIY_ERROR, result);
                    }
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    log.error("推送电子合同更新包隐藏状态失败", e);
                }
            }
        }
        return contractDO.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteContractById(String id) throws Exception {
        ContractOrderExtDO orderContractDO = contractOrderExtMapper.selectOne(new LambdaQueryWrapperX<ContractOrderExtDO>().eqIfPresent(ContractOrderExtDO::getId, id).select(ContractOrderExtDO::getId, ContractOrderExtDO::getTotalMoney, ContractOrderExtDO::getPlatform, ContractOrderExtDO::getStatus, ContractOrderExtDO::getCode, ContractOrderExtDO::getSupplierName, ContractOrderExtDO::getBuyerOrgName, ContractOrderExtDO::getModify, ContractOrderExtDO::getPdfFileId));
        if (ObjectUtil.isNotEmpty(orderContractDO) && orderContractDO.getModify() == 1) {
            throw exception(ErrorCodeConstants.GOMall_Query_Error, "此合同已锁定，不可删除");
        }
        if (StringUtils.isNotEmpty(id) && ObjectUtil.isNotEmpty(orderContractDO)) {
            orderContractDO.setStatus(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_DELETE.getCode());
            orderContractDO.setDeleted(true);
            //删除合同以及拓展信息
            contractOrderExtMapper.updateById(orderContractDO);
            contractMapper.deleteById(id);

            //刪除合同文件
            if (ObjectUtil.isNotEmpty(orderContractDO.getPdfFileId())) {
                try {
                    fileApi.deleteFile(orderContractDO.getPdfFileId());
                } catch (Exception e) {
                }
            }
            //修改草拟合同状态为草拟
            //根据订单id获取订单信息
            List<DraftOrderInfoDO> draftOrderInfoDOS = gpMallOrderMapper.getOrdersByContractId(id);
            if (CollectionUtil.isNotEmpty(draftOrderInfoDOS)) {
                Map<String, Object> bodyParam = new HashMap<>();
                bodyParam.put("client_id", clientId);
                bodyParam.put("client_secret", clientSecret);
                String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
                JSONObject jsonObject = JSONObject.parseObject(token);
                for (DraftOrderInfoDO orderInfoDO : draftOrderInfoDOS) {
                    if (ObjectUtil.isNotEmpty(orderInfoDO) && ObjectUtil.isNotEmpty(orderInfoDO.getStatus())
                            && !GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode().equals(orderInfoDO.getStatus())) {
                        orderInfoDO.setStatus(GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode());
                        try {
                            DraftOrderInfo draftOrderInfo = new DraftOrderInfo();
                            draftOrderInfo.setOrderGuid(orderInfoDO.getOrderGuid()).setStatus(GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode());
                            String result = contractProcessApi.updateOrderStatus(jsonObject.getString("access_token"), draftOrderInfo);
                            JSONObject resultJson = JSONObject.parseObject(result);
                            if (!"0".equals(resultJson.getString("code"))) {
                                throw exception(ErrorCodeConstants.DIY_ERROR, result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("推送电子合同更新订单状态失败", e);
                        }
                    }
                }
                gpMallOrderMapper.updateBatch(draftOrderInfoDOS);
            }
//            gPMallContractService.sendEcmsOperationLogEvent(orderContractDO, 1, null);
        }
        // 合同标的信息表删除
        contractObjectMapper.delete(ContractObjectDO::getContractId, id);
    }

    @Override
    public UploadContractCreateReqVO getUploadContractById(String id) throws Exception {
        if (StringUtils.isNotBlank(id)) {
            AtomicReference<UploadContractOrderExtDO> atomicOrderContractDO = new AtomicReference<>();
            TenantUtils.executeIgnore(() -> {
                atomicOrderContractDO.set(uploadContractOrderExtMapper.selectById(id));
            });
            UploadContractOrderExtDO orderContractDO = atomicOrderContractDO.get();
            if (ObjectUtil.isNotEmpty(orderContractDO)) {
                UploadContractCreateReqVO uploadContractCreateReqVO = UploadContractOrderExtConverter.INSTANCE.doConvert2UploadVo(orderContractDO);
                List<DraftOrderInfoDO> orderInfoDOList = gpMallOrderMapper.getOrdersByContractId(id);
                if (CollectionUtil.isNotEmpty(orderInfoDOList)) {
                    List<String> orderIds = orderInfoDOList.stream().map(DraftOrderInfoDO::getOrderGuid).collect(Collectors.toList());
                    List<GPMallPageRespVO> orderAndGoodsByOrderIds = getOrderAndGoodsByOrderIds(orderIds);
                    uploadContractCreateReqVO.setGpmallPageRespVO(orderAndGoodsByOrderIds);
                }
//                GPMallPageRespVO orderAndGoodsByOrderId = getOrderAndGoodsByOrderId(uploadContractCreateReqVO.getOrderId());
//                uploadContractCreateReqVO.setGpmallPageRespVO(orderAndGoodsByOrderId);
                if (StringUtils.isNotEmpty(orderContractDO.getBuyPlanId())) {
                    PlanInfoDO planInfoDO = planInfoMapper.selectOne(new LambdaQueryWrapperX<PlanInfoDO>()
                            .eq(PlanInfoDO::getPlanId, orderContractDO.getBuyPlanId()).orderByDesc(PlanInfoDO::getCreateTime).last(" limit 1"));
                    if (ObjectUtil.isNotEmpty(planInfoDO)) {
                        PlanInfo planInfo = GPXConverter.INSTANCE.toInfoVO(planInfoDO);
                        planInfo.setToSignAmount(BigDecimal.ZERO);
                        uploadContractCreateReqVO.setPlanInfo(planInfo);
                    }
                }

                //付款计划
                List<PaymentScheduleDO> contractPaymentPlanDOList = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>()
                        .eq(PaymentScheduleDO::getContractId, id));
                if (CollectionUtil.isNotEmpty(contractPaymentPlanDOList)) {
                    if (StringUtils.isNotBlank(contractPaymentPlanDOList.get(0).getStageName())) {
                        uploadContractCreateReqVO.setStagePaymentList(PaymentScheduleConverter.INSTANCE.toUploadStageVO(contractPaymentPlanDOList));
                    } else {
                        uploadContractCreateReqVO.setPaymentPlanList(PaymentScheduleConverter.INSTANCE.toPaymentUploadVO(contractPaymentPlanDOList));

                    }
                }

                //添加附件-补充协议
                List<ContractFileDO> contractFileDOList = contractFileMapper.selectList(new LambdaQueryWrapper<ContractFileDO>()
                        .eq(ContractFileDO::getContractId, id)
                        .eq(ContractFileDO::getAttachmentType, AttachmentTypeEnums.OTHER.getCode()));
                if (ObjectUtil.isNotEmpty(contractFileDOList)) {
                    for (ContractFileDO contractFileDO : contractFileDOList) {
                        contractFileDO.setFileName(fileApi.getName(contractFileDO.getFileId()));
                    }
                    uploadContractCreateReqVO.setContractFileDOList(contractFileDOList);
                }

                //合同关联计划
                AssociatedPlanDO associatedPlanDO = associatedPlanMapper.getOneByContractId(id);
                if (ObjectUtil.isNotNull(associatedPlanDO)) {
                    AssociatedPlanRespVO associatedPlanVO = AssociatedPlanConverter.INSTANCE.do2RespVO(associatedPlanDO);
                    uploadContractCreateReqVO.setAssociatedPlanVO(associatedPlanVO);
                }
                //制造商信息
                if (StringUtils.isNotEmpty(orderContractDO.getPropertyServiceType())) {
//                    制造商规模
                    SupplierSizeEnum supplierSizeEnum = SupplierSizeEnum.getInstance(Integer.valueOf(orderContractDO.getPropertyServiceType()));
                    uploadContractCreateReqVO.setPropertyServiceTypeName(ObjectUtil.isNotEmpty(supplierSizeEnum) ? supplierSizeEnum.getInfo() : "");
                }
                if (StringUtils.isNotEmpty(orderContractDO.getPropertyServiceAddress())) {
//                制造商所在区域：propertyServiceAddressName
                    RegionDTO regionByCode = regionApi.getRegionByCode(orderContractDO.getPropertyServiceAddress());
                    uploadContractCreateReqVO.setPropertyServiceAddressName(ObjectUtil.isNotEmpty(regionByCode) ? regionByCode.getRegionName() : "");
                }
                if (StringUtils.isNotEmpty(orderContractDO.getSupplierLocation())) {
//                供应商所在区域：supplierLocationName
                    RegionDTO regionByCode = regionApi.getRegionByCode(orderContractDO.getSupplierLocation());
                    uploadContractCreateReqVO.setSupplierLocationName(ObjectUtil.isNotEmpty(regionByCode) ? regionByCode.getRegionName() : "");
                }
                if (StringUtils.isNotEmpty(orderContractDO.getSupplierSize())) {
//                    供应商规模
                    SupplierSizeEnum supplierSizeEnum = SupplierSizeEnum.getInstance(Integer.valueOf(orderContractDO.getSupplierSize()));
                    uploadContractCreateReqVO.setSupplierSizeName(ObjectUtil.isNotEmpty(supplierSizeEnum) ? supplierSizeEnum.getInfo() : "");
                }
                if (StringUtils.isNotEmpty(orderContractDO.getSupplierFeatures())) {
                    SupplierFeaturesEnum instance = SupplierFeaturesEnum.getInstance(Integer.valueOf(orderContractDO.getSupplierFeatures()));
//                    供应商特殊性质：supplierFeaturesName
                    uploadContractCreateReqVO.setSupplierFeaturesName(ObjectUtil.isNotEmpty(instance) ? instance.getInfo() : "");
                }

                //合同类型
                List<ContractType> contractTypes = contractTypeMapper.selectByPlatId(uploadContractCreateReqVO.getContractType());
                if (CollectionUtil.isNotEmpty(contractTypes)) {
                    uploadContractCreateReqVO.setContractType(contractTypes.get(0).getName());
                }
                // 智能审查文件ID
                ContractDO contractDO = contractMapper.selectOne(new LambdaQueryWrapperX<ContractDO>().eq(ContractDO::getId, id).select(ContractDO::getUploadFileAiId));
                if (ObjectUtil.isNotEmpty(contractDO)) {
                    uploadContractCreateReqVO.setUploadFileAiId(contractDO.getUploadFileAiId());
                }
                return uploadContractCreateReqVO;
            }
        }
        return null;
    }

    /**
     * 通过订单id获取订单信息和商品信息
     *
     * @param orderIds
     * @return
     */
    @Override
    public List<GPMallPageRespVO> getOrderAndGoodsByOrderIds(List<String> orderIds) {

        //订单信息
        List<DraftOrderInfoDO> draftOrderInfoDOs = gpMallOrderMapper.selectList(new LambdaQueryWrapperX<DraftOrderInfoDO>().in(DraftOrderInfoDO::getOrderGuid, orderIds));
        List<GPMallPageRespVO> mallPageRespVOs = GPMallOrderConverter.INSTANCE.listOrderDO2Resp(draftOrderInfoDOs);
        List<String> supplierIds = mallPageRespVOs.stream().map(GPMallPageRespVO::getSupplierGuid).collect(Collectors.toList());
        List<SupplyDTO> supplyDTOList = new ArrayList<SupplyDTO>();
        Map<String, SupplyDTO> supplyDTOMap = new HashMap<>();
        supplyDTOList = supplyApi.getSupplyByIds(supplierIds);
        if (CollectionUtil.isEmpty(supplyDTOList)) {
            supplyDTOList = hljSupplyService.getSupplyList(supplierIds);
        }
        if (CollectionUtil.isNotEmpty(supplyDTOList)) {
            supplyDTOMap = CollectionUtils.convertMap(supplyDTOList, SupplyDTO::getId);
        }
        if (CollectionUtil.isNotEmpty(mallPageRespVOs)) {
            List<GoodsDO> goodsDOList = gpMallGoodsMapper.selectList(new LambdaQueryWrapperX<GoodsDO>()
                    .in(GoodsDO::getOrderId, orderIds));
            if (CollectionUtil.isNotEmpty(goodsDOList)) {
                Map<String, List<GoodsRespVO>> goodsDOMap = new HashMap<>();
                List<GoodsRespVO> goodsRespVOList = GPMallOrderConverter.INSTANCE.conertGoodsListDO2Resp(goodsDOList);
                goodsDOMap = CollectionUtils.convertMultiMap(goodsRespVOList, GoodsRespVO::getOrderId);
                for (GPMallPageRespVO respVO : mallPageRespVOs) {
                    List<GoodsRespVO> goodsRespVOS = goodsDOMap.get(respVO.getOrderGuid());
                    if (ObjectUtil.isNotNull(goodsRespVOS)) {
                        respVO.setGoodsRespVOList(goodsRespVOS);
                    }
                    //供应商数据托底
                    SupplyDTO supplyDTO = supplyDTOMap.get(respVO.getSupplierGuid());
                    if (ObjectUtil.isNotNull(supplyDTO)) {
                        respVO.setSupplierAddr(supplyDTO.getAddr());
                        respVO.setSupplierTel(supplyDTO.getTel());
                        respVO.setSupplierlinkFax(StringUtils.isBlank(respVO.getSupplierlinkFax()) ? supplyDTO.getFax() : respVO.getSupplierlinkFax());
                        respVO.setUnitScopeCode(supplyDTO.getUnitScopeCode());
                        respVO.setSupplierLinkman(StringUtils.isBlank(respVO.getSupplierLinkman()) ? supplyDTO.getPersonName() : respVO.getSupplierLinkman());
                    }

                }
            }
        }
        return mallPageRespVOs;

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ContractDataDTO queryByOrderIdV4(String orderId, String contractFrom) throws Exception {
        //设置相对方id和采购人id
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        List<ContractOrderExtDO> orderContractDOS = contractOrderExtMapper.selectList(new LambdaQueryWrapperX<ContractOrderExtDO>().eqIfPresent(ContractOrderExtDO::getOrderId, orderId)
                .notIn(ContractOrderExtDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_DELETE.getCode())
                .select(ContractOrderExtDO::getId, ContractOrderExtDO::getStatus, ContractOrderExtDO::getContractDrafter, ContractOrderExtDO::getOrderId));
        orderContractDOS.forEach(item -> {
            if (item.getContractDrafter().equals(loginUser.getType())) {
                throw exception(ErrorCodeConstants.GOMall_Query_Error, "此订单已起草合同，请到合同管理查看");
            }
            if (!HLJContractStatusEnums.CONTRACT_AUDITSTATUS_TOBESENT.getCode().equals(item.getStatus())) {
                throw exception(ErrorCodeConstants.GOMall_Query_Error, "该订单已存在在途合同，请到合同管理查看");
            }
        });

        ContractDataDTO contractDataVo = new ContractDataDTO();
        ContractDTO contractDTO = new ContractDTO();
        List<DraftOrderInfoDO> draftOrderInfoDOS = gpMallOrderMapper.selectList(new LambdaQueryWrapperX<DraftOrderInfoDO>().eqIfPresent(DraftOrderInfoDO::getOrderGuid, orderId).notIn(DraftOrderInfoDO::getOrderStatus,
                OrderStatusEnums.CONFIRM_CANCEL.getCode(), OrderStatusEnums.DELETE.getCode(), OrderStatusEnums.NULLIFY.getCode(), OrderStatusEnums.RETURNS.getCode()).orderByDesc(DraftOrderInfoDO::getUpdateTime));
        String token = "";
        if (CollectionUtil.isEmpty(draftOrderInfoDOS)) {
            if (PlatformEnums.GPMALL.getCode().equals(contractFrom) || PlatformEnums.GP_GPFA.getCode().equals(contractFrom)) {
                token = gcyBuyPlanService.getInitToken(BusinessTokenConfigEnums.SUPER_CONTROL.getInfo());
                //为空说明订单不存在，卖场，曜安数据不同步，需查询此订单信息进行订单更新
                RestResponseDTO<DraftOrderInfo> restResponseDTO = null;
                if (PlatformEnums.GP_GPFA.getCode().equals(contractFrom)) {
                    //框彩平台-调用框彩平台的订单信息查询接口
//                    restResponseDTO = gpfaOpenApi.getOrderInfo(token, orderId);
                } else if (PlatformEnums.GPMALL.getCode().equals(contractFrom)) {
                    //电子卖场-调用电子卖场的订单信息查询接口
                    restResponseDTO = openApi.getOrderInfo(token, orderId);
                }
                DraftOrderInfo draftOrderInfo = restResponseDTO.getData();
                Boolean success = restResponseDTO.getSuccess();
                if (ObjectUtils.isNotEmpty(draftOrderInfo)) {
                    List<DraftOrderInfo> draftOrderInfoList = new ArrayList<>();
                    DraftOrderInfoVO draftOrderInfoVO = new DraftOrderInfoVO();
                    draftOrderInfoList.add(draftOrderInfo);
                    draftOrderInfoVO.setPlatform(contractFrom);
                    draftOrderInfoVO.setDraftOrderInfoList(draftOrderInfoList);
                    //保存订单信息
                    EncryptDTO encryptDTO = Sm4Utils.convertParam(draftOrderInfoVO);
                    externalInterfaceService.saveOrderInfo(encryptDTO);
                    //重新获取订单信息
                    draftOrderInfoDOS = gpMallOrderMapper.selectList(new LambdaQueryWrapperX<DraftOrderInfoDO>().eq(DraftOrderInfoDO::getOrderGuid, orderId).notIn(DraftOrderInfoDO::getOrderStatus,
                            OrderStatusEnums.CONFIRM_CANCEL.getCode(), OrderStatusEnums.DELETE.getCode(), OrderStatusEnums.NULLIFY.getCode(), OrderStatusEnums.RETURNS.getCode()).orderByDesc(DraftOrderInfoDO::getUpdateTime).select(
                            DraftOrderInfoDO::getOrderCode, DraftOrderInfoDO::getId, DraftOrderInfoDO::getOrderGuid, DraftOrderInfoDO::getContractFrom));

                } else if (!success) {
                    throw exception(ErrorCodeConstants.GOMall_Query_Error, restResponseDTO.getMessage());
                }
            } else {
                throw exception(ErrorCodeConstants.GOMall_Query_Error, "查询订单" + orderId + "不存在");
            }
        }
        ContractInfoBackupsDO contractInfoBackupsDO = contractInfoBackupsMapper.selectOne(ContractInfoBackupsDO::getOrderId, orderId);
        if (ObjectUtils.isNotEmpty(contractInfoBackupsDO)) {
            String contractInfo = contractInfoBackupsDO.getContractInfo();
            contractDataVo = JsonUtils.parseObject(contractInfo, ContractDataDTO.class);
        } else if (ObjectUtils.isEmpty(contractInfoBackupsDO) && (PlatformEnums.GPMALL.getCode().equals(contractFrom) || PlatformEnums.GP_GPFA.getCode().equals(contractFrom))) {
            //框彩和协议定点为空时可能是1.0 推送过来的订单数据   需要重新发框彩和协议定点接口获取填充数据
            RestResponseDTO<ContractDataDTO> contractContentData = null;
            if (PlatformEnums.GP_GPFA.getCode().equals(contractFrom)) {
                //框彩平台-调用框彩平台的获取起草合同时需要的业务数据接口
                contractContentData = gpfaOpenApi.getContractContentData(token, orderId);
            } else if (PlatformEnums.GPMALL.getCode().equals(contractFrom)) {
                //电子卖场-调用电子卖场的获取起草合同时需要的业务数据接口
                contractContentData = openApi.getContractContentData(token, orderId);
            }
            //TODO: 框采测试环境目前数据错误，暂时略过，继续流程
//            if (ObjectUtil.isEmpty(contractContentData.getData()) && !contractContentData.getSuccess()) {
//                throw exception(ErrorCodeConstants.GOMall_Query_Error, contractContentData.getMessage());
//            }
            if (ObjectUtil.isNotEmpty(contractContentData.getData())) {
                contractDataVo = contractContentData.getData();
            }
            //将合同草拟数据进行备份
            if (ObjectUtil.isNotEmpty(contractDataVo)) {
                Long aLong = contractInfoBackupsMapper.selectCount(ContractInfoBackupsDO::getOrderId, orderId);
                if (aLong > 0) {
                    contractInfoBackupsMapper.updateById(new ContractInfoBackupsDO().setOrderId(orderId).setContractInfo(JsonUtils.toJsonString(contractDataVo)));
                } else {
                    contractInfoBackupsMapper.insert(new ContractInfoBackupsDO().setOrderId(orderId).setContractInfo(JsonUtils.toJsonString(contractDataVo)));
                }
            }
        }
        if (ObjectUtil.isNotEmpty(contractDataVo.getContractDTO())) {
            contractDTO = contractDataVo.getContractDTO();
        }
        //设置合同编号
        contractDTO.setContractCode(ContractCodeUtil.getContractCode(contractFrom));
        DraftOrderInfoDO draftOrderInfoDO = draftOrderInfoDOS.size() == 0 ? null : draftOrderInfoDOS.get(0);
        //设置成交百分百
        contractDTO.setTransactionRatio(draftOrderInfoDO == null ? null : draftOrderInfoDO.getTransactionRatio());
        //合同总金额
        if (ObjectUtils.isEmpty(contractDTO.getTotalMoney())) {
            //为空去订单金额
            if (ObjectUtils.isNotEmpty(draftOrderInfoDO)) {
                contractDTO.setTotalMoney(draftOrderInfoDO == null ? null : draftOrderInfoDO.getOrderTotalAmount());
                contractDTO.setShiftMoney(draftOrderInfoDO.getOrderTotalAmount() == null ? null : AmountUtil.trsferCapital(draftOrderInfoDO.getOrderTotalAmount().doubleValue()));
            }
        }
        //设置采购单位id,名称
        if (StringUtils.isEmpty(contractDTO.getBuyerOrgGuid())) {
            //为空取订单中的采购单位id
            contractDTO.setBuyerOrgGuid(draftOrderInfoDO == null ? null : draftOrderInfoDO.getPurchaserOrgGuid());
        }
        //设置供应商id,名称
        if (StringUtils.isEmpty(contractDTO.getSupplierGuid())) {
            //为空取订单中的供应商id
            contractDTO.setSupplierGuid(draftOrderInfoDO == null ? null : draftOrderInfoDO.getSupplierGuid());
        }
        //设置采购人信息
        OrganizationDTO organization = organizationApi.getOrganization(contractDTO.getBuyerOrgGuid());
        if (ObjectUtils.isNotEmpty(organization)) {
            if (StringUtils.isNotEmpty(organization.getName())) {
                contractDTO.setBuyerOrgName(organization.getName());
            }
            if (StringUtils.isNotEmpty(organization.getLinkMan())) {
                contractDTO.setBuyerProxy(organization.getLinkMan());
            }
            if (StringUtils.isNotEmpty(organization.getLinkPhone())) {
                contractDTO.setBuyerLinkMobile(organization.getLinkPhone());
            }
            if (StringUtils.isNotEmpty(organization.getLinkFax())) {
                contractDTO.setOrglinkFax(organization.getLinkFax());
            }
            if (StringUtils.isNotEmpty(organization.getAddress())) {
                contractDTO.setDeliveryAddress(organization.getAddress());
            }
            RegionDTO region = organization.getRegionGuid() == null ? null : regionApi.getRegionByGuid(organization.getRegionGuid());
            if (StringUtils.isNotEmpty(organization.getRegionCode())) {
                contractDTO.setRegionCode(organization.getRegionCode());
            }
            if (StringUtils.isEmpty(contractDTO.getRegionCode())) {
                contractDTO.setRegionCode(region == null ? null : region.getRegionCode());
            }
            if (ObjectUtil.isNotEmpty(region) && StringUtils.isNotEmpty(region.getRegionName())) {
                contractDTO.setRegionName(region.getRegionName());
            }
            if (ObjectUtil.isNull(contractDTO.getPayType())) {
                contractDTO.setPayType(organization.getPayType());
            }
        }
        //设置合同来源
        contractDTO.setContractFrom(draftOrderInfoDO == null ? null : draftOrderInfoDO.getContractFrom());
        //设置供应商信息
        SupplyDTO supply = supplyApi.getSupply(contractDTO.getSupplierGuid());
        if (ObjectUtil.isNull(supply)) {
            supply = hljSupplyService.getSupply(contractDTO.getSupplierGuid());
        }
        if (ObjectUtils.isNotEmpty(supply)) {
            if (StringUtils.isNotEmpty(supply.getLegalPerson())) {
                contractDTO.setSupplierProxy(supply.getLegalPerson());
            }
            if (StringUtils.isNotEmpty(supply.getLegalAddr())) {
                contractDTO.setRegisteredAddress(supply.getLegalAddr());
            }
            if (StringUtils.isNotEmpty(supply.getPersonMobile())) {
                contractDTO.setSupplierLinkMobile(supply.getPersonMobile());
            }
            if (StringUtils.isNotEmpty(supply.getBankName())) {
                contractDTO.setBankName(supply.getBankName());
            }
            if (StringUtils.isNotEmpty(supply.getBankAccount())) {
                contractDTO.setBankAccount(supply.getBankAccount());
            }
            if (StringUtils.isNotEmpty(supply.getReginCode())) {
                contractDTO.setSupplierLocation(supply.getReginCode() == null ? "" : supply.getReginCode());
            }
            if (StringUtils.isNotEmpty(supply.getSupplyCn())) {
                contractDTO.setSupplierName(supply.getSupplyCn());
            }
        }
        //设置区划信息
        if (StringUtils.isEmpty(contractDTO.getRegionCode())) {
            contractDTO.setRegionCode(draftOrderInfoDO == null ? null : draftOrderInfoDO.getRegionCode());
        }
        if (StringUtils.isEmpty(contractDTO.getRegionName())) {
            contractDTO.setRegionName(draftOrderInfoDO == null ? null : draftOrderInfoDO.getRegionFullName());
        }
        //设置采购标项Guid
        String purCatalogCode = null;
        BigDecimal totalMoney = new BigDecimal(0);
        List<GoodsDO> goodsDOS = gpMallGoodsMapper.selectList(new LambdaQueryWrapper<GoodsDO>().in(GoodsDO::getOrderId, orderId));
        if (CollectionUtil.isNotEmpty(goodsDOS)) {
            List<GoodsVO> goodsVOS = GPMallOrderConverter.INSTANCE.toGoodsVOS(goodsDOS);
            contractDTO.setBuyPlanBillGuid(goodsDOS.size() == 0 ? null : goodsDOS.get(0).getBuyPlanBillGuid());
            Set<String> goodsIds = goodsDOS.stream().map(GoodsDO::getId).collect(Collectors.toSet());
            List<GoodsPurCatalogDO> goodsPurCatalogDOS = goodsIds == null ? null : goodsPurCatalogMapper.selectList(GoodsPurCatalogDO::getGoodsId, goodsIds);
            Map<String, GoodsPurCatalogDO> goodsPurCatalogDOMap = CollectionUtils.convertMap(goodsPurCatalogDOS, GoodsPurCatalogDO::getGoodsId);
            goodsVOS.forEach(goodsVO -> {
                String goodsId = goodsVO.getId();
                GoodsPurCatalogDO goodsPurCatalogDO = goodsPurCatalogDOMap == null ? null : goodsPurCatalogDOMap.get(goodsId);
                PurCatalogInfoVo purCatalogInfoVo = new PurCatalogInfoVo();
                purCatalogInfoVo.setPurCatalogName(goodsPurCatalogDO == null ? null : goodsPurCatalogDO.getPurCatalogName());
                purCatalogInfoVo.setPurCatalogCode(goodsPurCatalogDO == null ? null : goodsPurCatalogDO.getPurCatalogCode());
                List<PurCatalogInfoVo> purCatalogCodeList = new ArrayList<>();
                purCatalogCodeList.add(purCatalogInfoVo);
                goodsVO.setPurCatalogCodeList(purCatalogCodeList);
            });
            contractDataVo.setGoodsVOS(goodsVOS);
            if (CollectionUtil.isNotEmpty(goodsPurCatalogDOS)) {
                purCatalogCode = goodsPurCatalogDOS.get(0).getPurCatalogCode();
            }

            if (ObjectUtil.isNull(contractDTO.getTotalMoney())) {
                for (GoodsDO goodsDO : goodsDOS) {
                    totalMoney = totalMoney.add(goodsDO.getTotalMoney());
                }
                contractDTO.setTotalMoney(totalMoney);
                contractDTO.setShiftMoney(AmountUtil.trsferCapital(totalMoney.doubleValue()));
            }
        }
        String value = systemConfigApi.getConfigByKey(IF_NEED_MODEL_CATEGORY.getKey());
        Boolean needModel = IfEnums.YES.equals(value);
        List<ModelIdVO> modelIdList = new ArrayList<>();
        if (needModel) {
            modelIdList = purCatalogService.getModelIdByOrderCode(draftOrderInfoDOS.size() == 0 ? null : draftOrderInfoDOS.get(0).getId(), purCatalogCode, null, null, null, null);
        } else {
            modelIdList = getModel4Order(draftOrderInfoDOS.get(0));
        }        //查询模板顺序配置开关  模板顺序0通用类模板在前  1平台通用类在前
        if (CollectionUtil.isNotEmpty(modelIdList)) {
            contractDataVo.setModelId(modelIdList.get(0).getModelId());
        }

        //设置是否涉密采购
        AssociatedPlanDO associatedPlanDO = associatedPlanMapper.selectOne(AssociatedPlanDO::getOrderId, orderId);
        contractDTO.setSecret(associatedPlanDO == null ? null : associatedPlanDO.getSecret());
        //采购计划备案书/核准书编号
        contractDTO.setBuyPlanCode(associatedPlanDO == null ? null : associatedPlanDO.getBuyPlanCode());
        //设置采购方式
        contractDTO.setPurchaseMethod(associatedPlanDO == null ? null : associatedPlanDO.getPurchaseMethod());
//        contractDTO.setKind(associatedPlanDO == null ? null :associatedPlanDO.getKind());
        //设置项目所属分类==采购分类
        contractDTO.setPurCatalogType(goodsDOS.size() == 0 ? null : goodsDOS.get(0).getPurCatalogType());
        contractDataVo.setUserType(loginUser.getType());
        contractDTO.setDatenow(new Date());
        //设置项目名称，项目ID，项目编号，
        ProjectDO projectDO = gpMallProjectMapper.selectOne(ProjectDO::getOrderId, orderId);
        if (ObjectUtil.isNotNull(projectDO)) {
            contractDTO.setProjectName(projectDO.getProjectName());
            if (PlatformEnums.ZHUBAJIE.getCode().equals(contractFrom)) {
                contractDTO.setProjectGuid(projectDO.getProjectCode());
                contractDTO.setProjectCode(projectDO.getProjectGuid());
            } else {
                contractDTO.setProjectGuid(projectDO.getProjectGuid());
                contractDTO.setProjectCode(projectDO.getProjectCode());
            }
            if (StringUtils.isEmpty(contractDTO.getKind())) {
                contractDTO.setKind(projectDO.getKind());
            }
        }
        if (ObjectUtil.isNotNull(draftOrderInfoDO) && ObjectUtil.isNotNull(draftOrderInfoDO.getProjectCategoryCode())) {
            ProjectCategoryEnums projectCategoryEnums = ProjectCategoryEnums.getInstance(draftOrderInfoDO.getProjectCategoryCode());
            switch (projectCategoryEnums) {
                case GOODS:
                    contractDTO.setContractName("政府采购货物买卖合同（试行）");
                    break;
                case ENGINEER:
                    contractDTO.setContractName("政府采购工程类采购合同(试行)");
                    break;
                case SERVICE:
                    contractDTO.setContractName("政府采购服务类采购合同（试行）");
                    break;
            }
        }
        if (ObjectUtil.isNull(contractDTO.getContractName())) {
            //TODO 暂时托底数据
            if (StringUtils.isBlank(contractDTO.getPurCatalogType())) {
                contractDTO.setPurCatalogType("1");
            }
            PurCatalogTypeEnums purCatalogTypeEnums = PurCatalogTypeEnums.getInstance(contractDTO.getPurCatalogType());

            switch (purCatalogTypeEnums) {
                case GOODS:
                    contractDTO.setContractName("政府采购货物买卖合同（试行）");
                    break;
                case ENGINEER:
                    contractDTO.setContractName("政府采购工程类采购合同(试行)");
                    break;
                case SERVICE:
                    contractDTO.setContractName("政府采购服务类采购合同（试行）");
                    break;
            }
        }
        //设置合同分类
        if (ObjectUtil.isNull(draftOrderInfoDO.getProjectCategoryCode())) {
            contractDTO.setProjectCategoryCode(contractDTO.getPurCatalogType() == null ? null : PurCatalogTypeEnums.getInstance(contractDTO.getPurCatalogType()).getCode());
        } else {
            contractDTO.setProjectCategoryCode(draftOrderInfoDO.getProjectCategoryCode());
        }
        //设置供应商规模
        if (StringUtils.isNotEmpty(contractDTO.getSupplierSize())) {
            SupplierSizeEnum instance = SupplierSizeEnum.getInstanceByCode_str(contractDTO.getSupplierSize());
            contractDTO.setSupplierSize(instance == null ? contractDTO.getSupplierSize() : instance.getCode().toString());
        }
        //设置供应商特殊性质
        if (StringUtils.isNotEmpty(contractDTO.getSupplierFeatures())) {
            SupplierFeaturesEnum instance = SupplierFeaturesEnum.getInstanceByCode_str(contractDTO.getSupplierFeatures());
            contractDTO.setSupplierFeatures(instance == null ? contractDTO.getSupplierFeatures() : instance.getCode().toString());
        }
        contractDataVo.setContractDTO(contractDTO);
        contractDataVo.setOrderCode(draftOrderInfoDO.getOrderCode());
//        ContractDataDTO contractDataDTO = OrderContractConverter.INSTANCE.toContractDataDTO(contractDataVo);
        return contractDataVo;
    }

    private List<ModelIdVO> getModel4Order(DraftOrderInfoDO draftOrderInfoDO) {
        // 1、匹配当前起草订单所属合同类型，与当前采购包或订单相同交易类型(电子交易/框架协议/协议定点!.)的模板
        //    并优先匹配增加品目的模板
        List<SimpleModel> simpleModels = simpleModelMapper.getModel4OrderCatalog(draftOrderInfoDO);
        if (CollectionUtil.isEmpty(simpleModels)) {
            // 2、如果没找到，则找没品目的模板(模板配的品目中包含当前起草订单/采购包的品目)
            simpleModels = simpleModelMapper.getModel4OrderNoCatalog(draftOrderInfoDO);
        }
        if (CollectionUtil.isEmpty(simpleModels)) {
            // 3、如果都没找到，则找通用类
            simpleModels = simpleModelMapper.getModel4OrderAllPlatform(draftOrderInfoDO);
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

    @Resource
    private SimpleModelMapper simpleModelMapper;

    /**
     * 卖场无计划合同作废
     *
     * @param vo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @DataPermission(enable = false)
    public String cancelContract(CancellationFileVO vo) throws Exception {
        if (ObjectUtil.isEmpty(vo.getContractId())) {
            throw exception(ErrorCodeConstants.GOMall_Query_Error, "合同id不能为空");
        }
//        if (ObjectUtil.isEmpty(vo.getReason())) {
//            throw exception(ErrorCodeConstants.GOMall_Query_Error, "作废理由不能为空");
//        }
        //是否是采购人
//        LoginUser loginUser = getLoginUser();
//        if (loginUser.getType() != UserTypeEnums.PURCHASER_ORGANIZATION.getCode()) {
//            throw exception(ErrorCodeConstants.GOMall_Query_Error, "只允许采购人作废");
//        }
        ContractOrderExtDO orderContractDO = contractOrderExtMapper.selectOne(new LambdaQueryWrapper<ContractOrderExtDO>()
                .eq(ContractOrderExtDO::getId, vo.getContractId())
                .select(ContractOrderExtDO::getId, ContractOrderExtDO::getPlatform, ContractOrderExtDO::getStatus, ContractOrderExtDO::getOrderId));
        if (ObjectUtil.isEmpty(orderContractDO)) {
            throw exception(ErrorCodeConstants.GOMall_Query_Error, "合同不存在");
        }
        // 删除合同标的信息表数据
        contractObjectMapper.delete(ContractObjectDO::getContractId, vo.getContractId());
        //是否是卖场的合同
//        if (!orderContractDO.getPlatform().equals(PlatformEnums.JDMALL.getCode())) {
//            throw exception(ErrorCodeConstants.GOMall_Query_Error, "不是卖场的合同");
//        }
        //是不是签署完的
//        if (!orderContractDO.getStatus().equals(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode())) {
//            throw exception(ErrorCodeConstants.GOMall_Query_Error, "合同未签署完成");
//        }
        //是不是无计划的
//        List<AssociatedPlanDO> associatedPlanDOS = associatedPlanMapper.selectList(new LambdaQueryWrapper<AssociatedPlanDO>()
//                .eq(AssociatedPlanDO::getOrderId, orderContractDO.getOrderId()));
//        if (CollectionUtil.isNotEmpty(associatedPlanDOS)) {
//            throw exception(ErrorCodeConstants.GOMall_Query_Error, "合同不是无计划的");
//        }
        if (HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode().equals(orderContractDO.getStatus())
                || HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode().equals(orderContractDO.getStatus())
                || HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode().equals(orderContractDO.getStatus())) {
            String cValue = getCvalueByKey(SystemConfigKeyEnums.IF_NEW_JIANGUAN.getKey());
            //查询监管备案状态
            EncryptResponseDto response = null;
            ContractArchiveStateDTO contractReqDTO = new ContractArchiveStateDTO().setContractGuid(orderContractDO.getId()).setPlatform(orderContractDO.getPlatform());
            EncryptDTO encryptDTO = Sm4Utils.convertParam(contractReqDTO);
            //是否需要走监管新版本的接口(融通平台备案接口)  n：否  y：是
            String orgGuid = "";
            String regionCode = "";
            String accessToken = gcyBuyPlanService.getInitToken(BusinessTokenConfigEnums.SUPER_CONTROL.getInfo());
            if ("y".equals(cValue)) {
                //获取采购单位ID和采购单位区划
                if (ObjectUtil.isEmpty(orderContractDO)) {
                    throw outException(ErrorCodeConstants.DATA_MISSING, "合同不存在");
                }
                orgGuid = orderContractDO.getBuyerOrgId();
                OrganizationDTO organization = organizationApi.getOrganization(orgGuid);
                if (ObjectUtil.isNotNull(organization)) {
                    RegionDTO region = organization.getRegionGuid() == null ? null : regionApi.getRegionById(organization.getRegionGuid());
                    regionCode = region == null ? null : region.getRegionCode();
                }
                String responseStr = superVisionApi.getContractArchiveStateV4(accessToken, orgGuid, regionCode, encryptDTO);
                response = JSONObject.parseObject(responseStr, EncryptResponseDto.class);
            } else {
                //服务工程超市和京东卖场
                if (contractReqDTO.getPlatform().equals(PlatformEnums.ZHUBAJIE.getCode()) || contractReqDTO.getPlatform().equals(PlatformEnums.JDMALL.getCode())) {
                    response = superVisionApi.getContractArchiveState(accessToken, encryptDTO);
                }
            }
            if (ObjectUtil.isNotEmpty(response)) {
                if ("0".equals(response.getStatus())) {
                    if (ObjectUtil.isNotEmpty(response.getData())) {
                        ContractArchiveStateRespDTO result = JSONObject.parseObject(Sm4Utils.decryptEcb(response.getData()), ContractArchiveStateRespDTO.class);
                        //如果合同备案状态不为空，并且状态为已备案，则更新备案中合同状态为已备案
                        if (ObjectUtil.isNotEmpty(result)) {
                            log.info("合同作废请求监管查询合同状态接口请求参数{}，响应结果{}", response.getData(), result);
                            if (Objects.equals(result.getContractArchiveState(), ContractArchiveStateEnums.BAKED.getCode())
                                    || Objects.equals(result.getContractArchiveState(), ContractArchiveStateEnums.BACING.getCode())) {
                                throw exception(MODEL_CATEGORY_CHECK_EMPTY, "合同已备案，不能作废");
                            } else if (Objects.equals(result.getContractArchiveState(), ContractArchiveStateEnums.BAK_FAIL.getCode()) || Objects.equals(result.getContractArchiveState(), ContractArchiveStateEnums.WAIT_BAK.getCode())) {
                                ContractArchiveStateDTO contractArchiveStateDTO = new ContractArchiveStateDTO().setContractGuid(orderContractDO.getId()).setPlatform(orderContractDO.getPlatform());
                                EncryptResponseDto response1 = outOpenApiService.deleteContractV3(accessToken, orgGuid, regionCode, Sm4Utils.convertParam(contractArchiveStateDTO));
                                log.info("合同作废删除监管备案合同{},返回响应结果{}", orderContractDO.getId(), response1);
                                if (!"0".equals(response1.getStatus())) {
                                    throw exception(ErrorCodeConstants.GOMall_Query_Error, "监管的合同删除失败");
                                }
                            }
                        }
                    }
                } else {
                    log.info("合同作废请求监管查询合同状态接口请求参数{}，响应结果{}", response.getData(), response);
                    throw exception(ErrorCodeConstants.GOMall_Query_Error, "监管的备案状态查询失败");
                }
                contractCancellationMapper.insert(new CancellationFileDO().setContractId(vo.getContractId()).setReason(vo.getReason()).setFileId(vo.getFileId()));
                //修改状态
                contractOrderExtMapper.update(null, new LambdaUpdateWrapper<ContractOrderExtDO>()
                        .eq(ContractOrderExtDO::getId, vo.getContractId())
                        .set(ContractOrderExtDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode()));
                ContractDO contractDO = new ContractDO().setId(vo.getContractId()).setStatus(ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode());
                contractMapper.updateById(contractDO);

                //恢复订单状态到待草拟
                DraftOrderInfoDO orderInfo = gpMallOrderMapper.selectOne(DraftOrderInfoDO::getOrderGuid, orderContractDO.getOrderId());


                if (ObjectUtil.isNotNull(orderInfo)) {
                    orderInfo.setStatus(WAITE_TO_DRAFT.getCode());
                    gpMallOrderMapper.updateById(orderInfo);
                } else {
                    List<DraftOrderInfoDO> orderInfoDOList = gpMallOrderMapper.selectOrdersByContractId(vo.getContractId());
                    if (CollectionUtil.isEmpty(orderInfoDOList) && StringUtils.isNotBlank(orderContractDO.getOrderId())) {
                        orderInfoDOList = gpMallOrderMapper.selectList(new LambdaQueryWrapperX<DraftOrderInfoDO>().eq(DraftOrderInfoDO::getOrderGuid, orderContractDO.getOrderId()));
                    }
                    if (CollectionUtil.isNotEmpty(orderInfoDOList)) {
                        orderInfoDOList.stream().forEach(order -> order.setStatus(WAITE_TO_DRAFT.getCode()));
                        gpMallOrderMapper.updateBatch(orderInfoDOList);
                    } else {
                        throw exception(EMPTY_DATA_ERROR_V2, ("订单"));
                    }
                }


                //同步到黑龙江电子合同
                Map<String, Object> bodyParam = new HashMap<>();
                bodyParam.put("client_id", clientId);
                bodyParam.put("client_secret", clientSecret);
                String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
                JSONObject jsonObject = JSONObject.parseObject(token);
                String result = contractProcessApi.cancelContract(jsonObject.getString("access_token"), new CancellationFileDTO().setContractId(contractDO.getId()).setReason("单位端_作废"));
                JSONObject resultJson = JSONObject.parseObject(result);
                if (!"0".equals(resultJson.getString("code"))) {
                    throw exception(ErrorCodeConstants.DIY_ERROR, result);
                }
                return "OK";
            } else {
                throw exception(ErrorCodeConstants.GOMall_Query_Error, "合同作废监管的备案状态查询失败-");
            }
        } else {
            throw exception(ErrorCodeConstants.GOMall_Query_Error, "合同未签署完成");
        }
    }

    private String getCvalueByKey(String key) {
        ArrayList<String> keyList = new ArrayList<>();
        keyList.add(key);
        List<SystemConfigRespDTO> configsByCKeys = systemConfigApi.getConfigsByCKeys(keyList);
        String cValue = configsByCKeys.size() == 0 ? null : configsByCKeys.get(0).getCValue();
        return cValue;
    }


}
