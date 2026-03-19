package com.yaoan.module.econtract.service.gcy.gpmall;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.econtract.api.contract.ContractProcessApi;
import com.yaoan.module.econtract.api.contract.GPFAOpenApi;
import com.yaoan.module.econtract.api.contract.IdReqDTO;
import com.yaoan.module.econtract.api.contract.OpenApi;
import com.yaoan.module.econtract.api.contract.dto.PackageUpdateDTO;
import com.yaoan.module.econtract.api.contract.dto.gcy.CancellationFileDTO;
import com.yaoan.module.econtract.api.contract.dto.mongolia.EncryptResponseDto;
import com.yaoan.module.econtract.api.gcy.buyplan.MedicalApi;
import com.yaoan.module.econtract.api.gcy.buyplan.OrgApi;
import com.yaoan.module.econtract.api.gcy.buyplan.SuperVisionApi;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.ContractArchiveStateDTO;
import com.yaoan.module.econtract.controller.admin.aop.service.OutOpenApiService;
import com.yaoan.module.econtract.controller.admin.gcy.gpmall.vo.CancellationFileVO;
import com.yaoan.module.econtract.convert.gcy.gpmall.ContractFileMapper;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.TradingContractExtDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.DraftOrderInfoDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.CancellationFileDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.PackageInfoDO;
import com.yaoan.module.econtract.dal.dataobject.statistics.StatisticsDO;
import com.yaoan.module.econtract.dal.mysql.category.backups.ContractInfoBackupsMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contract.TradingContractExtMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractOrderExtMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractParamFieldMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.UploadContractOrderExtMapper;
import com.yaoan.module.econtract.dal.mysql.contract.trading.ContractCancellationMapper;
import com.yaoan.module.econtract.dal.mysql.contract.trading.PurchaseMapper;
import com.yaoan.module.econtract.dal.mysql.contract.trading.TradingSupplierMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.gcy.gpmall.CommentMapper;
import com.yaoan.module.econtract.dal.mysql.gcy.gpmall.ContractGoodsMapper;
import com.yaoan.module.econtract.dal.mysql.gcy.rel.ContractOrderRelMapper;
import com.yaoan.module.econtract.dal.mysql.gpx.*;
import com.yaoan.module.econtract.dal.mysql.order.*;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import com.yaoan.module.econtract.dal.mysql.statistics.StatisticsMapper;
import com.yaoan.module.econtract.enums.BusinessTokenConfigEnums;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.gcy.gpmall.HLJContractStatusEnums;
import com.yaoan.module.econtract.enums.gpx.BiddingMethodEnums;
import com.yaoan.module.econtract.service.bpm.contract.BpmContractService;
import com.yaoan.module.econtract.service.catalog.PurCatalogService;
import com.yaoan.module.econtract.service.contract.ContractService;
import com.yaoan.module.econtract.service.external.ExternalInterfaceService;
import com.yaoan.module.econtract.service.gcy.buyplan.EcmsGcyBuyPlanServiceImpl;
import com.yaoan.module.econtract.service.gpx.GPXService;
import com.yaoan.module.econtract.service.hljsupply.HLJSupplyService;
import com.yaoan.module.econtract.service.model.ModelService;
import com.yaoan.module.econtract.util.gcy.Sm4Utils;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.enums.FileUploadPathEnum;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.config.dto.SystemConfigRespDTO;
import com.yaoan.module.system.api.region.RegionApi;
import com.yaoan.module.system.api.region.dto.RegionDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.OrganizationApi;
import com.yaoan.module.system.api.user.dto.OrganizationDTO;
import com.yaoan.module.system.enums.config.SystemConfigKeyEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

import static com.yaoan.framework.common.exception.util.OutServiceExceptionUtil.outException;
import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.EMPTY_DATA_ERROR_V2;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.MODEL_CATEGORY_CHECK_EMPTY;
import static com.yaoan.module.econtract.enums.gcy.gpmall.GCYOrderStatusEnums.WAITE_TO_DRAFT;

/**
 * @description:
 * @author: Pele
 * @date: 2023/12/4 12:02
 */
@Slf4j
@Service
public class GPMallProjectServiceImpl implements GPMallProjectService{

    @Resource
    private ContractOrderExtMapper contractOrderExtMapper;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private GPMallOrderOldMapper gpMallOrderMapper;
    @Resource
    private OrganizationApi organizationApi;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private RegionApi regionApi;
    @Resource
    private FileApi fileApi;
    @Resource
    private PackageInfoMapper packageInfoMapper;
    @Autowired
    private ContractProcessApi contractProcessApi;
    @Value("${feign.client.contract.client_id}")
    private String clientId;
    @Value("${feign.client.contract.client_secret}")
    private String clientSecret;
    @Resource
    private EcmsGcyBuyPlanServiceImpl gcyBuyPlanService;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private ContractCancellationMapper contractCancellationMapper;
    @Resource
    private TradingContractExtMapper tradingContractExtMapper;
    @Resource
    private OrganizationApi orgApi;
    @Resource
    private OrgApi gpxorgApi;
    @Resource
    private StatisticsMapper statisticsMapper;

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
        if (orderContractDO.getStatus().equals(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_NO_RECORD.getCode())
                || orderContractDO.getStatus().equals(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDING.getCode())
                || orderContractDO.getStatus().equals(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_RECORDED.getCode())) {
            String cValue = getCvalueByKey(SystemConfigKeyEnums.IF_NEW_JIANGUAN.getKey());
            //查询监管备案状态
            //是否需要走监管新版本的接口(融通平台备案接口)  n：否  y：是
            String orgGuid = "";
            String regionCode = "";

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

            } else {
                //服务工程超市和京东卖场

                }
            }
            if (ObjectUtil.isNotEmpty(1)) {

                contractCancellationMapper.insert(new CancellationFileDO().setContractId(vo.getContractId()).setReason(vo.getReason()).setFileId(vo.getFileId()));
                //修改状态
                contractOrderExtMapper.update(null, new LambdaUpdateWrapper<ContractOrderExtDO>()
                        .eq(ContractOrderExtDO::getId, vo.getContractId())
                        .set(ContractOrderExtDO::getStatus, HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode()));
                ContractDO contractDO = new ContractDO().setId(vo.getContractId()).setStatus(ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode());
                contractMapper.updateById(contractDO);

                //恢复订单状态到待草拟
                DraftOrderInfoDO orderInfo = gpMallOrderMapper.selectOne(DraftOrderInfoDO::getOrderGuid, orderContractDO.getOrderId());

                if (ObjectUtil.isNotNull(orderInfo)){
                    orderInfo.setStatus(WAITE_TO_DRAFT.getCode());
                    gpMallOrderMapper.updateById(orderInfo);
                }else {
                    List<DraftOrderInfoDO> orderInfoDOList = gpMallOrderMapper.selectOrdersByContractId(vo.getContractId());
                    if (CollectionUtil.isNotEmpty(orderInfoDOList)){
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
                String result = contractProcessApi.cancelContract(jsonObject.getString("access_token"),new CancellationFileDTO().setContractId(contractDO.getId()).setReason("单位端_作废"));
                JSONObject resultJson = JSONObject.parseObject(result);
                if (!"0".equals(resultJson.getString("code"))) {
                    throw new RuntimeException(result);
                }
                return "OK";
            } else {
                throw exception(ErrorCodeConstants.GOMall_Query_Error, "合同作废监管的备案状态查询失败-");
            }
        }

    private String getCvalueByKey(String key) {
        ArrayList<String> keyList = new ArrayList<>();
        keyList.add(key);
        List<SystemConfigRespDTO> configsByCKeys = systemConfigApi.getConfigsByCKeys(keyList);
        String cValue = configsByCKeys.size() == 0 ? null : configsByCKeys.get(0).getCValue();
        return cValue;
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

            } else {
                if (tradingContractDO.getSourceCode().equals("GPMS")) {

                } else if (tradingContractDO.getSourceCode().equals("GPMS_PSP")) {
                    //采购单位服务平台
                    String accessToken = gcyBuyPlanService.getInitToken(BusinessTokenConfigEnums.SUPER_CONTROL.getInfo());
                    EncryptResponseDto response = gpxorgApi.getContractArchiveState(accessToken, Sm4Utils.convertParam(new ContractArchiveStateDTO().setContractGuid(id).setPlatform(tradingContractDO.getPlatform())));
                    if (response.getStatus().equals("0")) {

                    }
                } else if (tradingContractDO.getSourceCode().equals("PICS")) {
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
        //同步黑龙江
        Map<String, Object> bodyParam = new HashMap<>();
        bodyParam.put("client_id", clientId);
        bodyParam.put("client_secret", clientSecret);
        String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
        JSONObject jsonObject = JSONObject.parseObject(token);
        if (jsonObject.get("error") != null) {
            try {
                throw new Exception(jsonObject.getString("error_description"));
            } catch (Exception e) {
                throw new RuntimeException(jsonObject.getString("error_description"));
            }
        }
        String result = contractProcessApi.invalidateContractById(jsonObject.getString("access_token"),new IdReqDTO().setId(id));
        log.info("返回结果：" + result);
        JSONObject resultJson = JSONObject.parseObject(result);
        if (!"0".equals(resultJson.getString("code"))) {
            throw new RuntimeException("作废同步状态失败");
        }
        return true;
    }

}
