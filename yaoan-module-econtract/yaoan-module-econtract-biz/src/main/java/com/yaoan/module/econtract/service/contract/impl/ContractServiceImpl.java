package com.yaoan.module.econtract.service.contract.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.file.FileNameUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.extra.ssh.Sftp;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.yulichang.query.MPJQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kinggrid.pdf.executes.PdfElectronicSealDetails;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.common.util.json.JsonUtils;
import com.yaoan.framework.common.util.object.BeanUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.bpm.api.bpm.activity.BpmActivityApi;
import com.yaoan.module.bpm.api.bpm.activity.dto.BpmProcessRespDTO;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.yaoan.module.bpm.api.task.dto.BpmTaskAllInfoRespVO;
import com.yaoan.module.bpm.api.task.dto.ContractProcessInstanceRelationInfoRespDTO;
import com.yaoan.module.bpm.enums.model.FlowableModelEnums;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.contract.ContractApi;
import com.yaoan.module.econtract.api.contract.ContractProcessApi;
import com.yaoan.module.econtract.api.contract.LegalismApi;
import com.yaoan.module.econtract.api.contract.SppGPTApi;
import com.yaoan.module.econtract.api.contract.dto.ContractDTO;
import com.yaoan.module.econtract.api.contract.dto.ContractDataDTO;
import com.yaoan.module.econtract.api.contract.dto.ContractFileDTO;
import com.yaoan.module.econtract.api.gcy.order.DraftOrderInfo;
import com.yaoan.module.econtract.api.gcy.order.GoodsVO;
import com.yaoan.module.econtract.api.purchasing.IProjectPurchasingApi;
import com.yaoan.module.econtract.api.purchasing.dto.ReqIdsDTO;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.BpmContractCreateReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord.BorrowRecordRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.*;
import com.yaoan.module.econtract.controller.admin.contract.vo.ledger.BpmContractChangeRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ledger.ContractBorrowRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ledger.ContractFileRespVO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ledger.PayRecordRespVO;
import com.yaoan.module.econtract.controller.admin.contractPerformanceAcceptance.v2.vo.out.AcceptanceRecordRespVO;
import com.yaoan.module.econtract.controller.admin.contracttype.vo.ContractTypeSignetReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.RecentUseModelVO;
import com.yaoan.module.econtract.controller.admin.order.vo.GPMallPageRespVO;
import com.yaoan.module.econtract.controller.admin.order.vo.GoodsRespVO;
import com.yaoan.module.econtract.controller.admin.outward.contract.VO.*;
import com.yaoan.module.econtract.controller.admin.performance.contractPerformance.vo.ContractPerfReqVO;
import com.yaoan.module.econtract.controller.admin.performance.performanceTask.vo.PerformanceTaskInfoRespVO;
import com.yaoan.module.econtract.controller.admin.relative.vo.RelativeByUserRespVO;
import com.yaoan.module.econtract.controller.admin.sign.vo.SignInfoVO;
import com.yaoan.module.econtract.controller.admin.sign.vo.VerificationRespVO;
import com.yaoan.module.econtract.controller.admin.signet.vo.SignetManageRespVO;
import com.yaoan.module.econtract.convert.acceptance.AcceptanceConverter;
import com.yaoan.module.econtract.convert.agreement.PrefAgreementRelConverter;
import com.yaoan.module.econtract.convert.bpm.borrow.ContractBorrowBpmConverter;
import com.yaoan.module.econtract.convert.change.ChangeConverter;
import com.yaoan.module.econtract.convert.contract.*;
import com.yaoan.module.econtract.convert.gcy.gpmall.ContractFileConverter;
import com.yaoan.module.econtract.convert.gcy.gpmall.ContractFileMapper;
import com.yaoan.module.econtract.convert.gcy.gpmall.GPMallOrderConverter;
import com.yaoan.module.econtract.convert.model.ModelConverter;
import com.yaoan.module.econtract.convert.relative.RelativeConverter;
import com.yaoan.module.econtract.convert.term.ContractTermConverter;
import com.yaoan.module.econtract.dal.dataobject.acceptance.AcceptanceDO;
import com.yaoan.module.econtract.dal.dataobject.agreement.PrefAgreementRelDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.change.BpmContractChangeDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.contract.BpmContract;
import com.yaoan.module.econtract.dal.dataobject.bpm.contractborrow.ContractBorrowBpmDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.register.BpmContractRegisterDO;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import com.yaoan.module.econtract.dal.dataobject.category.ContractCategory;
import com.yaoan.module.econtract.dal.dataobject.category.backups.ContractInfoBackupsDO;
import com.yaoan.module.econtract.dal.dataobject.contract.*;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.TradingContractExtDO;
import com.yaoan.module.econtract.dal.dataobject.contract.outward.ContractDataDO;
import com.yaoan.module.econtract.dal.dataobject.contractInvoiceManage.ContractInvoiceManageDO;
import com.yaoan.module.econtract.dal.dataobject.contractarchives.ContractArchivesDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.AssociatedPlanDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.ContractFileDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.DraftOrderInfoDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.gpmall.GoodsDO;
import com.yaoan.module.econtract.dal.dataobject.gpx.PackageInfoDO;
import com.yaoan.module.econtract.dal.dataobject.model.Model;
import com.yaoan.module.econtract.dal.dataobject.model.MyCollectModel;
import com.yaoan.module.econtract.dal.dataobject.model.SimpleModel;
import com.yaoan.module.econtract.dal.dataobject.offlinefile.OfflineFileDO;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplicationDO;
import com.yaoan.module.econtract.dal.dataobject.performance.contractPerformance.ContractPerformanceDO;
import com.yaoan.module.econtract.dal.dataobject.purchasing.ReceiveBusinessesDO;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.dataobject.signet.BpmContractSignetDO;
import com.yaoan.module.econtract.dal.dataobject.term.ContractTermDO;
import com.yaoan.module.econtract.dal.dataobject.terminate.TerminateContractDO;
import com.yaoan.module.econtract.dal.dataobject.watermark.WatermarkDO;
import com.yaoan.module.econtract.dal.mysql.acceptance.AcceptanceMapper;
import com.yaoan.module.econtract.dal.mysql.agreement.PrefAgreementRelMapper;
import com.yaoan.module.econtract.dal.mysql.borrow.BorrowContractMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.contract.BpmContractMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.contractborrow.ContractBorrowBpmMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.register.BpmContractRegisterMapper;
import com.yaoan.module.econtract.dal.mysql.businessfile.BusinessFileMapper;
import com.yaoan.module.econtract.dal.mysql.category.ContractCategoryMapper;
import com.yaoan.module.econtract.dal.mysql.category.backups.ContractInfoBackupsMapper;
import com.yaoan.module.econtract.dal.mysql.change.BpmContractChangeMapper;
import com.yaoan.module.econtract.dal.mysql.contract.*;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractOrderExtMapper;
import com.yaoan.module.econtract.dal.mysql.contract.outward.ContractDataMapper;
import com.yaoan.module.econtract.dal.mysql.contract.trading.TradingSupplierMapper;
import com.yaoan.module.econtract.dal.mysql.contractarchives.ContractArchivesMapper;
import com.yaoan.module.econtract.dal.mysql.contractinvoicemanage.ContractInvoiceManageMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.formconfig.FormDOMapper;
import com.yaoan.module.econtract.dal.mysql.formconfig.FormItemMapper;
import com.yaoan.module.econtract.dal.mysql.gpx.PackageInfoMapper;
import com.yaoan.module.econtract.dal.mysql.model.ModelMapper;
import com.yaoan.module.econtract.dal.mysql.model.MyCollectModelMapper;
import com.yaoan.module.econtract.dal.mysql.model.SimpleModelMapper;
import com.yaoan.module.econtract.dal.mysql.offlinefile.OfflineFileMapper;
import com.yaoan.module.econtract.dal.mysql.order.AssociatedPlanMapper;
import com.yaoan.module.econtract.dal.mysql.order.GPMallGoodsMapper;
import com.yaoan.module.econtract.dal.mysql.order.GPMallOrderOldMapper;
import com.yaoan.module.econtract.dal.mysql.param.ParamMapper;
import com.yaoan.module.econtract.dal.mysql.payment.paymentapplication.PaymentApplScheRelMapper;
import com.yaoan.module.econtract.dal.mysql.payment.paymentapplication.PaymentApplicationMapper;
import com.yaoan.module.econtract.dal.mysql.performance.contractPerformance.ContractPerforMapper;
import com.yaoan.module.econtract.dal.mysql.receive.business.ReceiveBusinessesMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import com.yaoan.module.econtract.dal.mysql.signet.BpmContractSignetMapper;
import com.yaoan.module.econtract.dal.mysql.term.ContractTermMapper;
import com.yaoan.module.econtract.dal.mysql.term.TermMapper;
import com.yaoan.module.econtract.dal.mysql.terminate.TerminateContractMapper;
import com.yaoan.module.econtract.dal.mysql.watermark.WatermarkMapper;
import com.yaoan.module.econtract.enums.*;
import com.yaoan.module.econtract.enums.change.ContractChangeTypeEnums;
import com.yaoan.module.econtract.enums.common.CommonFlowableReqVOResultStatusEnums;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.econtract.enums.company.CompanyEnums;
import com.yaoan.module.econtract.enums.gcy.gpmall.HLJContractStatusEnums;
import com.yaoan.module.econtract.enums.gpx.BiddingMethodEnums;
import com.yaoan.module.econtract.enums.ledger.LedgerTabEnums;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import com.yaoan.module.econtract.enums.order.GCYOrderStatusEnums;
import com.yaoan.module.econtract.enums.order.ProjectCategoryEnums;
import com.yaoan.module.econtract.enums.payment.MoneyTypeEnums;
import com.yaoan.module.econtract.enums.payment.PaymentScheduleStatusEnums;
import com.yaoan.module.econtract.enums.payment.PaymentTypeEnums;
import com.yaoan.module.econtract.enums.payment.SettlementMethodEnums;
import com.yaoan.module.econtract.framework.core.event.version.FileVersionEvent;
import com.yaoan.module.econtract.framework.core.event.version.FileVersionEventPublisher;
import com.yaoan.module.econtract.service.bpm.contract.BpmContractService;
import com.yaoan.module.econtract.service.bpm.contractborrow.ContractBorrowBpmService;
import com.yaoan.module.econtract.service.change.ContractChangeService;
import com.yaoan.module.econtract.service.contract.ContractService;
import com.yaoan.module.econtract.service.contract.com.cxf.client.LawyeeService;
import com.yaoan.module.econtract.service.contracttype.ContractTypeService;
import com.yaoan.module.econtract.service.cx.ChangXieService;
import com.yaoan.module.econtract.service.flow.FlowService;
import com.yaoan.module.econtract.service.paramModel.ParamModelMapper;
import com.yaoan.module.econtract.service.performance.contractPerformance.ContractPerfService;
import com.yaoan.module.econtract.service.performance.perfTask.PerfTaskService;
import com.yaoan.module.econtract.service.relative.RelativeService;
import com.yaoan.module.econtract.service.signet.SignetService;
import com.yaoan.module.econtract.service.version.FileVersionService;
import com.yaoan.module.econtract.util.*;
import com.yaoan.module.econtract.util.flowable.FlowableUtil;
import com.yaoan.module.econtract.util.json.JsonSorter;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import com.yaoan.module.infra.api.file.dto.FileUploadRespDTO;
import com.yaoan.module.infra.enums.FileUploadPathEnum;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.config.dto.SystemConfigRespDTO;
import com.yaoan.module.system.api.dept.CompanyApi;
import com.yaoan.module.system.api.dept.DeptApi;
import com.yaoan.module.system.api.dept.dto.DeptRespDTO;
import com.yaoan.module.system.api.dept.dto.UserCompanyDeptRespDTO;
import com.yaoan.module.system.api.dept.dto.UserCompanyInfoRespDTO;
import com.yaoan.module.system.api.permission.RoleApi;
import com.yaoan.module.system.api.permission.dto.RoleRespDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.EcontractOrgApi;
import com.yaoan.module.system.api.user.OrganizationApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.api.user.dto.EcontractOrgDTO;
import com.yaoan.module.system.api.user.dto.OrganizationDTO;
import com.yaoan.module.system.dal.dataobject.systemuserrel.SystemuserRelDO;
import com.yaoan.module.system.dal.mysql.systemuserrel.SystemuserRelMapper;
import com.yaoan.module.system.enums.config.SystemConfigKeyEnums;
import com.yh.scofd.agent.HTTPAgent;
import com.yh.scofd.agent.wrapper.Const;
import com.yh.scofd.agent.wrapper.model.MarkPosition;
import com.yh.scofd.agent.wrapper.model.TextInfo;
import electronicseal.SealDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.util.Matrix;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.module.econtract.enums.ContractUploadTypeEnums.REGISTER;
import static com.yaoan.module.econtract.enums.ContractUploadTypeEnums.THIRD_PARTY;
import static com.yaoan.module.econtract.enums.EntityTypeEnums.COMPANY;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.*;
import static com.yaoan.module.econtract.enums.StatusConstants.READY_TO_UPLOAD_PATH;
import static com.yaoan.module.econtract.enums.StatusConstants.SUFFIX_PDF;

//@DataPermission(enable = false)
@Service
@Slf4j
public class ContractServiceImpl implements ContractService {


    //主合同签署-双方
    public static final String PROCESS_KEY_BOTH_OLD = "ecms_contract_confirm_sign_both";
    public static final String PROCESS_KEY_BOTH = ActivityConfigurationEnum.ECMS_CONTRACT_BOTH.getDefinitionKey();
    //主合同签署-三方
    public static final String PROCESS_KEY_TRIPARTITE_NOT = "ecms_contract_confirm_sign_tripartite";
    public static final String PROCESS_KEY_TRIPARTITE = ActivityConfigurationEnum.ECMS_CONTRACT_TRIPARTITE.getDefinitionKey();
    //合同确认签署-多方
    public static final String PROCESS_KEY_MANY = ActivityConfigurationEnum.ECMS_CONTRACT_RELATIVES.getDefinitionKey();

    //终止合同签署-双方
    public static final String PROCESS_KEY_TERMINATE_BOTH = "ecms_contract_terminate_both";
    //终止合同签署-三方
    public static final String PROCESS_KEY_TERMINATE_TRIPARTITE = "ecms_contract_sign_tripartite";
    //确认阶段标识
    public static final String CONFIRM = "%确认%";
    //签署阶段标识
    public static final String SIGN = "%签署%";
    private final static String APPROVE_COMPLETE = "审批完成";
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private FileApi fileApi;
    @Resource
    private MinioUtils minioUtils;
    @Resource
    private AttachmentRelMapper attachmentRelMapper;
    @Resource
    private SignatoryRelMapper signatoryRelMapper;
    @Resource
    private MyCollectModelMapper myCollectModelMapper;
    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private RelativeMapper relativeMapper;
    @Resource
    private RelativeService relativeService;
    @Resource
    private CompanyApi companyApi;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private ContractCategoryMapper contractCategoryMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private FlowService flowService;
    @Resource
    private PrefAgreementRelMapper prefAgreementRelMapper;
    @Resource
    private TerminateContractMapper terminateContractMapper;
    @Resource
    private PerfTaskService perfTaskService;
    @Resource
    private DocumentRelMapper documentRelMapper;
    @Resource
    private ContractPerforMapper contractPerforMapper;
    @Resource
    private ModelMapper modelMapper;
    @Resource
    private ContractPerfService contractPerfService;
    @Resource
    private BpmContractService bpmContractService;
    @Resource
    private ContractParameterMapper contractParameterMapper;
    @Resource
    private ContractSealMapper contractSealMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private ContractProjectPurchasingMapper contractProjectPurchasingMapper;
    @Resource
    private BpmContractMapper bpmContractMapper;
    @Resource
    private BpmContractRegisterMapper bpmContractRegisterMapper;
    @Resource
    private PaymentApplicationMapper paymentApplicationMapper;
    @Resource
    private PaymentApplScheRelMapper paymentApplScheRelMapper;
    @Resource
    private ContractArchivesMapper contractArchivesMapper;
    @Resource
    private BusinessFileMapper businessFileMapper;
    @Resource
    private ContractFileMapper contractFileMapper;

    @Resource
    private ContractTypeService contractTypeService;
    /**
     * 虚拟系统对接api
     */
    @Resource
    private IProjectPurchasingApi projectPurchasingApi;
    @Resource
    private ReceiveBusinessesMapper receiveBusinessesMapper;
    @Resource
    private ContractPurchaseMapper contractPurchaseMapper;
    @Resource
    private ContractSignatoryMapper contractSignatoryMapper;
    @Resource
    private SimpleModelMapper simpleModelMapper;
    @Resource
    private SimpleContractMapper simpleContractMapper;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;
    @Resource
    private BorrowContractMapper borrowContractMapper;
    @Resource
    private BpmContractChangeMapper bpmContractChangeMapper;
    @Resource
    private BpmActivityApi bpmActivityApi;
    @Resource
    private GPMallOrderOldMapper gpMallOrderMapper;
    @Resource
    private ContractDataMapper contractDataMapper;
    /**
     * 佩雷 模板参数 mapper 用法 service
     */
    @Resource
    private ParamModelMapper paramModelMapper;
    @Resource
    private FormItemMapper formItemMapper;
    @Resource
    private FormDOMapper formDOMapper;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private GPMallGoodsMapper gpMallGoodsMapper;
    @Resource
    private ParamMapper paramMapper;
    @Resource
    private LegalismApi legalismApi;
    @Resource
    private SppGPTApi sppGPTApi;
    @Resource
    private ContractDetailsMapper contractDetailsMapper;
    @Resource
    private ContractInfoBackupsMapper contractInfoBackupsMapper;
    @Resource
    private FileVersionService fileVersionService;
    @Resource
    private FileVersionEventPublisher fileVersionEventPublisher;
    @Resource
    private SystemuserRelMapper systemuserRelMapper;
    @Resource
    private ContractOrderExtMapper contractOrderExtMapper;
    @Resource
    private WatermarkMapper watermarkMapper;
    @Resource
    private OrganizationApi organizationApi;
    @Resource
    private ContractBorrowBpmMapper borrowBpmMapper;
    @Resource
    private TradingSupplierMapper tradingSupplierMapper;
    @Resource
    private TradingContractExtMapper tradingContractExtMapper;
    @Resource
    private AssociatedPlanMapper associatedPlanMapper;
    @Resource
    private DeptApi deptApi;
    @Resource
    private ContractApi contractApi;
    @Resource
    private OfflineFileMapper offlineFileMapper;
    @Resource
    private WkHtmlToPdfManager wkHtmlToPdfManager;
    @Resource
    private PackageInfoMapper packageInfoMapper;
    @Value("${feign.client.contract.client_id}")
    private String clientId;
    @Value("${feign.client.contract.client_secret}")
    private String clientSecret;
    @Resource
    private ContractProcessApi contractProcessApi;
    @Resource
    private ContractService contractService;

    @Resource
    private RoleApi roleApi;

    @Resource
    private ContractTermMapper contractTermMapper;
    @Resource
    private TermMapper termMapper;
    @Autowired
    private SignetService signetService;
    @Autowired
    private RedisUtils redisUtils;


    @Override
    public PageResult<SimpleContractListRespVO> listContracts(ContractPageReqVO reqVO) {
        List<Integer> govList = new ArrayList<>();
        govList.add(ContractUploadTypeEnums.ORDER_DRAFT.getCode());
        govList.add(ContractUploadTypeEnums.THIRD_PARTY.getCode());
        List<Integer> statusList = new ArrayList<>();
        statusList.add(ContractStatusEnums.PERFORMANCE_COMPLETE.getCode());
        statusList.add(ContractStatusEnums.PERFORMING.getCode());
        statusList.add(ContractStatusEnums.SIGN_COMPLETED.getCode());

        // 政采
        List<ContractDO> govContractList =  contractMapper.selectList(
                new LambdaQueryWrapperX<ContractDO>()
                        .select(ContractDO::getId,ContractDO::getUpload,ContractDO::getIsFilings)
                        .in(ContractDO::getUpload,govList)
                        .eq(ContractDO::getIsFilings,2)
                        .in(ContractDO::getStatus,statusList)
                        .last("limit 300")
        );
        List<String> govContractIds = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(govContractList)){
            govContractIds = govContractList.stream().map(ContractDO::getId).collect(Collectors.toList());
        }

        // 非政采
        List<ContractDO> ungovContractList =  contractMapper.selectList(
                new LambdaQueryWrapperX<ContractDO>()
                        .select(ContractDO::getId,ContractDO::getUpload,ContractDO::getIsFilings)
                        .notIn(ContractDO::getUpload,govList)
                        .in(ContractDO::getStatus,statusList)
                        .orderByDesc(ContractDO::getCreateTime)
                        .last("limit 300")
        );
        List<String> ungovContractIds = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(ungovContractList)){
            ungovContractIds = govContractList.stream().map(ContractDO::getId).collect(Collectors.toList());
        }

        List<String> sumContractIds = new ArrayList<>();
        sumContractIds.addAll(ungovContractIds);
        sumContractIds.addAll(govContractIds);
        reqVO.setSumContractIds(sumContractIds);
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectPage(reqVO);
        PageResult<SimpleContractListRespVO> result = ContractConverter.INSTANCE.convertPageDO2SimpleResp(contractDOPageResult);

        if(CollectionUtil.isNotEmpty(result.getList())){
            List<Long> creatorIds = result.getList().stream().map(SimpleContractListRespVO::getCreator).map(Long::parseLong).collect(Collectors.toList());
            List<AdminUserRespDTO> userRespDTOList = adminUserApi.getUserList(creatorIds);
            Map<Long,AdminUserRespDTO> userRespDTOMap = new HashMap<>();
            if(CollectionUtil.isNotEmpty(userRespDTOList)){
                userRespDTOMap = CollectionUtils.convertMap(userRespDTOList,AdminUserRespDTO::getId);
            }
            for (SimpleContractListRespVO respVO : result.getList()) {
                AdminUserRespDTO adminUserRespDTO = userRespDTOMap.get(Long.valueOf(respVO.getCreator()));
                if(ObjectUtil.isNotNull(adminUserRespDTO)){
                    respVO.setCreatorName(adminUserRespDTO.getNickname());
                }
            }
        }
        return result;
    }



    private static String urlEncode(String originalString) {
        try {
            return URLEncoder.encode(originalString, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    //    public static void main(String[] args) throws IOException {
//        String path = "D:\\DLL\\1717660848856_945.pdf";
//
//        String result = "";
//        // 创建 Web 服务客户端代理工厂
//        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
//        factory.setServiceClass(LawyeeService.class);
//
//        // 设置 Web 服务地址
//        factory.setAddress("http://1.119.162.45:8370/cxf/services/lawyeeservice");
//
//        // 创建 Web 服务客户端代理
//        LawyeeService client = (LawyeeService) factory.create();

    private static String urlEncodeWithCustomSpaceEncoding(String originalString) {
        String encodedString = urlEncode(originalString);
        assert encodedString != null;
        return encodedString.replace("+", "%20");
    }

    private static byte[] readFileToByteArray(String fileUrl) throws IOException {
//        URL url = new URL(fileUrl);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setRequestMethod("GET");
//        try (InputStream inputStream = connection.getInputStream()) {
        File file = new File(fileUrl);
        try (InputStream inputStream = new FileInputStream(file);) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            return output.toByteArray();
        } finally {
//            connection.disconnect();
        }
    }
    //展示结果集添加 合同分类名称-合同类型名称-签署方名称

    public static String getSign(String appId, String secret) {
        SM2 sm2 = new SM2(null, secret);

        // 请求参数
        Map params = new TreeMap();
        params.put("appId", appId);
        params.put("timestamp", new Date().getTime());

        // 将TreeMap转换为Url参数形式
        String query = HttpUtil.toParams(params);
        System.out.println("排序参数：" + query);

        // 生成sign
        String sign = Base64.getEncoder().encodeToString(sm2.encrypt(StrUtil.bytes(query, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey));
        System.out.println("生成签名：" + sign);
        return sign;
    }

    public static File downloadFileToTemp(FileDTO fileDTO) throws Exception {
        URL url = new URL(fileDTO.getUrl());
        try (InputStream inputStream = url.openStream()) {
            // 创建一个临时文件夹
            Path tempDir = Files.createTempDirectory("tempFiles");
            // 目标路径
            Path tempFilePath = tempDir.resolve(fileDTO.getName());
            Files.copy(inputStream, tempFilePath, StandardCopyOption.REPLACE_EXISTING);
            return tempFilePath.toFile();
        }
    }

    public static MultipartFile createMultipartFile(File file) throws Exception {
        try (FileInputStream input = new FileInputStream(file)) {
            return new MockMultipartFile("file", file.getName(), "application/pdf", input);
        }
    }

    /// /            byte[] fileBytes = readFileToByteArray(path);
//        byte[] fileBytes = new byte[0];
//        FileInputStream fis = null;
//        try {
//            File file = new File(path);
//            fileBytes = new byte[(int) file.length()];
//            fis = new FileInputStream(file);
//            fis.read(fileBytes);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (fis != null) {
//                try {
//                    fis.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        String token = "<Users>\n" +
//                "\t<item>\n" +
//                "\t\t<OrgID>0</OrgID>\n" +
//                "\t\t<OrgName>最高人民法院</OrgName>\n" +
//                "\t\t<DptName>业务部门</DptName>\n" +
//                "\t\t<UserName>910009</UserName>\n" +
//                "\t\t<UserID>1</UserID>\n" +
//                "\t\t<PostID></PostID>\n" +
//                "\t\t<DptID>1</DptID>\n" +
//                "\t\t<PostName>专员</PostName>\n" +
//                "\t</item>\n" +
//                "</Users>";
//        // 调用 Web 服务的方法
//        result = client.getXMLByText(fileBytes, token, "bs","HT24052145174322020123" ,"{\"fileType\":\"pdf\"}");
//
//        // 处理结果
//        System.out.println("token------------------: " + token);
//        System.out.println("Result from WebService------------------: " + result);
//    }
    public static void main(String[] args) throws IOException {
        //纠错接口
//        checkFileCallBackHY();
        //获取token
//        getToken();
        //获取纠错信息
//        notValidLogin();
    }

    private static void getToken() {
        try {
            // 设置参数
            String appId = "htsc";
            String orgId = "0";
            String userId = "910009";
            String tokenId = Base64.getEncoder().encodeToString((orgId + "@" + userId).getBytes());

            // 构建请求 URL
            String urlString = String.format("http://1.119.162.45:8380/correction/getToken/notValidLogin?appId=%s&tokenId=%s", appId, tokenId);
            URL url = new URL(urlString);

            // 打开连接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // 检查响应码
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 读取响应
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // 解析 JSON 响应
                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();

                System.out.println("code: " + jsonResponse.get("code").getAsInt());
                System.out.println("msg: " + jsonResponse.get("msg").getAsString());
                System.out.println("entity: " + jsonResponse.get("entity").getAsString());
            } else {
                System.out.println("GET request failed: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void notValidLogin() {
        try {
            // 创建 URL 对象
            URL url = new URL("http://1.119.162.45:8380/correction/checkResult/getErrorInfos/notValidLogin");

            // 创建 HttpURLConnection 对象
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // 构建请求体数据
            String requestBody = "{\"token\": \"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhdWQiOlsicmVzb3VyY2UtY2xpZW50IiwicmVzb3VyY2UtY2xp" + "ZW50LXRlc3QiXSwicGFzc3dvcmQiOnRydWUsInNjb3BlIjpbImFwaV91c2VyaW5mbyJdLCJpZCI6IjkxMDAwOSIsImV4cCI6MTcxODMwODQ3NiwianRpI" + "joiNzQ0YTY0YjQtZWJmMy00ZTNkLTk3MWYtYzhiODUzMGFhMjUwIiwiY2xpZW50X2lkIjoiY2xpZW50SWQiLCJ1c2VybmFtZSI6IjkxMDAwOSJ9.UTRjaB94C" + "-FzE0gaYZApICfwqDiOBbwG3eBYQhcYl5E\", \"fileId\": \"HLJGCYC2312990020241619559\"}";

            // 发送请求
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // 处理响应
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"))) {
                    StringBuilder response = new StringBuilder();
                    String responseLine = null;
                    while ((responseLine = br.readLine()) != null) {
                        response.append(responseLine.trim());
                    }
                    System.out.println("Response body: " + response.toString());
                }
            } else {
                System.err.println("Error occurred: " + conn.getResponseMessage());
            }

            // 关闭连接
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void checkFileCallBackHY() throws IOException {
//        String path = "https://hljcg.hlj.gov.cn/uploader-gpms/upload/commoninfo/2024/6/7/1717758455110_6283.pdf";
        String path = "C:\\Users\\Dell\\Desktop\\2024艾滋病防治项目人类免疫缺陷病毒1型核酸测定试剂盒（PCR-荧光法）买卖合同.pdf";
        String result = "";
//        byte[] fileBytes = readFileToByteArray(path);
        byte[] fileBytes = new byte[0];
        FileInputStream fis = null;
        try {
            File file = new File(path);
            fileBytes = new byte[(int) file.length()];
            fis = new FileInputStream(file);
            fis.read(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            // 创建 Web 服务客户端代理工厂
            JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
            factory.setServiceClass(LawyeeService.class);

            // 设置 Web 服务地址
            factory.setAddress("http://1.119.162.45:8370/cxf/services/lawyeeservice");

            // 创建 Web 服务客户端代理
            LawyeeService client = (LawyeeService) factory.create();

            String token = "<Users>\n" + "\t<item>\n" + "\t\t<OrgID>0</OrgID>\n" + "\t\t<OrgName>最高人民法院</OrgName>\n" + "\t\t<DptName>业务部门</DptName>\n" + "\t\t<UserName>910009</UserName>\n" + "\t\t<UserID>1</UserID>\n" + "\t\t<PostID></PostID>\n" + "\t\t<DptID>1</DptID>\n" + "\t\t<PostName>专员</PostName>\n" + "\t</item>\n" + "</Users>";
            // 调用 Web 服务的方法
            result = client.checkFileCallBackHY("CXZBGS[DY]20230033", fileBytes, "webservicecallback1", "http://1.119.162.45:8370/cxf/services/lawyeeservice?wsdl", "LawyeeServiceService", "{\"fileType\":\"pdf\"}", token);

            // 处理结果
            System.out.println("token------------------: " + token);
            System.out.println("Result from WebService------------------: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 递归提取节点及其属性和值
    private static void extractFields(Node node, Map<String, String> fields) {
        // 提取节点的属性
        if (node.hasAttributes()) {
            NamedNodeMap attributes = node.getAttributes();
            for (int i = 0; i < attributes.getLength(); i++) {
                Node attr = attributes.item(i);
                fields.put(attr.getNodeName(), attr.getNodeValue());
            }
        }

        // 如果节点有子节点，继续递归
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node child = childNodes.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                extractFields(child, fields);
            }
        }
    }

    @Override
    public void convertRtf2Pdf(String content, String targetPath) throws Exception {

        try {
            List<String> keys = new ArrayList<>();
            keys.add(SystemConfigKeyEnums.IF_FILE_CONVERT_SELF.getKey());
            String flag = systemConfigApi.getConfigsByCKeys(keys).get(0).getCValue();
            log.debug("【系统文件配置】是否使用本身转换配置！flag:{}", flag);
            if ("true".equals(flag)) {
                // 调用rtf2Pdf方法进行转换
                PdfConvertHtmlUtil.rtf2Pdf1(content, targetPath);
            } else {
                System.out.println("【友虹服务对接】触发了一次友虹组件任务！");
                File outputFile = new File(targetPath);
                System.out.println("【友虹服务对接】targetPath:" + targetPath);
                final HTTPAgent agent = new HTTPAgent(SpringUtil.getProperty("components.agent.test.url"));
                FileOutputStream out = new FileOutputStream(outputFile);
                agent.htmlStrToPDF(content, out);
                agent.close();
                System.out.println("【友虹服务对接】友虹组件任务处理完成！");
            }
        } catch (Exception e) {
            System.out.println("【友虹服务对接】处理异常！" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 付款管理-金额统计
     */
    @Override
    public PaymentAmount getPayment(PaymentSchedulePageReqVO paymentSchedulePageReqVO) {
        //获取当前时间
        Date date = new Date();
        paymentSchedulePageReqVO.setCurrentDate(date);
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectPaymentSchedule(paymentSchedulePageReqVO);
        PageResult<PaymentSchedulePageRespVO> resultPage = ContractConverter.INSTANCE.convertPaymentPage(contractDOPageResult);
        //封装名称-付款进度
        PaymentAmount paymentAmount = packageNamePayment(resultPage);
        return paymentAmount;
    }

    @Override
    public PageResult<PaymentSchedulePageRespVO> getPaymentPage(PaymentSchedulePageReqVO paymentSchedulePageReqVO) {
        //获取当前时间
        Date date = new Date();
        paymentSchedulePageReqVO.setCurrentDate(date);
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectPaymentSchedule(paymentSchedulePageReqVO);
        PageResult<PaymentSchedulePageRespVO> resultPage = ContractConverter.INSTANCE.convertPaymentPage(contractDOPageResult);
        packageNamePayment(resultPage);
        return resultPage;
    }

    @Override
    public List<PaymentScheduleRespVO> getPaymentSchedule(String contractId) {
        List<PaymentScheduleRespVO> result = new ArrayList<>();
        if (StringUtils.isNotBlank(contractId)) {
            List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.selectList(PaymentScheduleDO::getContractId, contractId);
            // 定义一个比较器，根据 sort 字段进行排序
            Comparator<PaymentScheduleDO> sortBySortField = Comparator.comparingInt(PaymentScheduleDO::getSort);
            // 对 paymentScheduleDOS 列表进行排序
            Collections.sort(paymentScheduleDOS, sortBySortField);
            result = PaymentScheduleConverter.INSTANCE.toList(paymentScheduleDOS);
            List paymentPlanIds = result.stream().map(item -> item.getId()).collect(Collectors.toList());
            MPJQueryWrapper<PaymentApplicationDO> mpjQueryWrapper = new MPJQueryWrapper<PaymentApplicationDO>().selectAll(PaymentApplicationDO.class).select("rel.schedule_id as contractId", "f.name as contractName").leftJoin("infra_file f on t.file_id = f.id").leftJoin("ecms_payment_appl_sche_rel rel on t.id=rel.application_id");
            mpjQueryWrapper.in("rel.schedule_id", paymentPlanIds);
            mpjQueryWrapper.in("t.result", 2);
            List<PaymentApplicationDO> paymentApplictionList = paymentApplicationMapper.selectList(mpjQueryWrapper);
            if (paymentApplictionList.size() > 0) {
                Map<String, PaymentApplicationDO> paymentApplicationDOMap = CollectionUtils.convertMap(paymentApplictionList, PaymentApplicationDO::getContractId);

                for (PaymentScheduleRespVO paymentScheduleRespVO : result) {
                    if (paymentApplicationDOMap.containsKey(paymentScheduleRespVO.getId())) {
                        paymentScheduleRespVO.setFileId(paymentApplicationDOMap.get(paymentScheduleRespVO.getId()).getFileId());
                        paymentScheduleRespVO.setFileName(paymentApplicationDOMap.get(paymentScheduleRespVO.getId()).getContractName());
                        paymentScheduleRespVO.setPaidAmount(paymentApplicationDOMap.get(paymentScheduleRespVO.getId()).getPayedAmount());
                    }
                }
            }
        }
        return result;
    }

    private PaymentAmount packageNamePayment(PageResult<PaymentSchedulePageRespVO> result) {
        PaymentAmount paymentAmount = new PaymentAmount();
        //合同总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        //合同已支付金额
        BigDecimal paidAmount = BigDecimal.ZERO;
        if (CollectionUtil.isNotEmpty(result.getList())) {
            List<String> typeIds = result.getList().stream().map(PaymentSchedulePageRespVO::getContractType).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            List<ContractType> contractTypes = contractTypeMapper.selectList(ContractType::getId, typeIds);
            Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypes, ContractType::getId);

            List<String> contractIds = result.getList().stream().map(PaymentSchedulePageRespVO::getId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            List<SignatoryRelDO> signatoryRelations = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractIds);

            Map<String, List<SignatoryRelDO>> contractRelationMap = CollectionUtils.convertMultiMap(signatoryRelations, SignatoryRelDO::getContractId);

            List<String> relationDataIds = signatoryRelations.stream().map(SignatoryRelDO::getSignatoryId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            List<Relative> relatives = relativeMapper.selectBatchIds(relationDataIds);
            Map<String, Relative> relativeMap = CollectionUtils.convertMap(relatives, Relative::getId);

            List<Long> creatorIds = result.getList().stream().map(item -> Long.valueOf(item.getCreator())).collect(Collectors.toList());

            List<AdminUserRespDTO> userInfo = adminUserApi.getUserList(creatorIds);
            Map<Long, AdminUserRespDTO> longAdminUserRespDTOMap = CollectionUtils.convertMap(userInfo, AdminUserRespDTO::getId);
            List<UserCompanyInfoRespDTO> userCompanyInfoList = companyApi.getUserCompanyInfoList(creatorIds);
            Map<Long, UserCompanyInfoRespDTO> companyInfoRespDTOMap = userCompanyInfoList.stream().collect(Collectors.toMap(UserCompanyInfoRespDTO::getUserId, Function.identity(), (v1, v2) -> v2));

            //合同付款进度
            List<PaymentScheduleDO> paymentSchedules = paymentScheduleMapper.selectPayList(contractIds);
            Map<String, List<PaymentScheduleDO>> paymentScheduleMap = CollectionUtils.convertMultiMap(paymentSchedules, PaymentScheduleDO::getContractId);


            for (PaymentSchedulePageRespVO item : result.getList()) {
                // 更新ContractPageRespVO对象的contractCategoryName
                ContractType contractType = contractTypeMap.get(item.getContractType());
                if (contractType != null) {
                    item.setContractTypeName(contractType.getName());
                }
                AdminUserRespDTO adminUserRespDTO = longAdminUserRespDTOMap.get(Long.valueOf(item.getCreator()));
                if (adminUserRespDTO != null) {
                    item.setCreatorName(adminUserRespDTO.getNickname());
                }
                UserCompanyInfoRespDTO companyInfo = companyInfoRespDTOMap.get(Long.valueOf(item.getCreator()));
                if (companyInfo != null) {
                    item.setMySignatoryName(companyInfo.getName());
                }
                List<SignatoryRelDO> signatoryRelDOS = contractRelationMap.get(item.getId());
                if (CollectionUtil.isNotEmpty(signatoryRelDOS)) {
                    List<String> signatoryList = new ArrayList<>();
                    //添加发起方公司名称
                    UserCompanyDeptRespDTO userCompanyDeptRespDTO = adminUserApi.selectUserCompanyDept(Long.valueOf(item.getCreator()));
                    String name = new String();
                    if (ObjectUtil.isNotEmpty(userCompanyDeptRespDTO)) {
                        if (ObjectUtil.isNotEmpty(userCompanyDeptRespDTO.getDeptId())) {
                            if (ObjectUtil.isNotEmpty(userCompanyDeptRespDTO.getCompanyInfo())) {
                                name = userCompanyDeptRespDTO.getCompanyInfo().getName();
                            }
                        } else {
                            name = userCompanyDeptRespDTO.getNickname();
                        }
                    }
                    signatoryList.add(name);
                    signatoryRelDOS.forEach(rel -> {
                        Relative relative = relativeMap.get(rel.getSignatoryId());
                        if (relative != null) {
                            signatoryList.add(relative.getCompanyName());
                        }
                    });
                    item.setSignatoryList(signatoryList);
                }
                //合同付款计划
                List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMap.get(item.getId());
                if (CollectionUtil.isNotEmpty(paymentScheduleDOS)) {
                    BigDecimal i = new BigDecimal(0);
                    for (PaymentScheduleDO paymentScheduleDO : paymentScheduleDOS) {
                        i = i.add(paymentScheduleDO.getPaidAmount());
                        //总已支付金额
                        paidAmount = paidAmount.add(paymentScheduleDO.getPaidAmount());
                    }
                    item.setPaymentRatio(i);
                }
                //合同总金额
                totalAmount = totalAmount.add(item.getAmount());
            }
        }
        //添加金额数据
        paymentAmount.setTotalAmount(totalAmount);
        paymentAmount.setPaidAmount(paidAmount);
        paymentAmount.setUnpaidAmount(totalAmount.subtract(paidAmount));
        //计算已支付比例
        if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
            paymentAmount.setPaidRatio(paidAmount.divide(totalAmount, 2, RoundingMode.HALF_UP).doubleValue());
        } else {
            paymentAmount.setPaidRatio(0.0);
        }
        //计算未支付比例
        if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
            paymentAmount.setUnpaidRatio(paymentAmount.getUnpaidAmount().divide(totalAmount, 2, RoundingMode.HALF_UP).doubleValue());
        } else {
            paymentAmount.setUnpaidRatio(0.0);
        }
        return paymentAmount;
    }

    @Override
    public PageResult<ProjectPurchasingRespVO> getProjectPurchasingPage(PageParam pageParam) {
        PageResult<ReceiveBusinessesDO> receiveBusinessesDOPageResult = receiveBusinessesMapper.selectPage(pageParam, null);
        List<String> collect = receiveBusinessesDOPageResult.getList().stream().map(ReceiveBusinessesDO::getProjectPurchasingId).collect(Collectors.toList());
        //调用其他系统api 获取项目采购信数据
        ReqIdsDTO reqIdsDTO = new ReqIdsDTO();
        if (CollectionUtil.isEmpty(collect)) {
            return new PageResult<>();
        }
        String str = projectPurchasingApi.queryPurchasingByIds(reqIdsDTO.setIds(collect));
        cn.hutool.json.JSONObject jsonObj = new cn.hutool.json.JSONObject(str);
        JSONArray jsonArray = jsonObj.getJSONArray("data");

        // 将JSONArray转换为List
        List<ProjectPurchasingRespVO> list = JSONObject.parseArray(jsonArray.toJSONString(0), ProjectPurchasingRespVO.class);
        // 添加对应条款的模板id
        list.forEach(item -> {
            String projectType = item.getProjectType();
            switch (projectType) {
                case "1":
                    // 货物类
                    item.setTemplateId("d62f5aa5773d250ec3d343aea1618433");
                    Model model = modelMapper.selectById(item.getTemplateId());
                    item.setTemplateName(model.getName());
                    item.setTemplateFileId(model.getRemoteFileId());
                    break;
                case "2":
                    // 服务类
                    item.setTemplateId("6223732d762e245f579e9d0d6ea5ed55");
                    Model model1 = modelMapper.selectById(item.getTemplateId());
                    item.setTemplateName(model1.getName());
                    item.setTemplateFileId(model1.getRemoteFileId());
                    break;
                case "3":
                    // 工程类
                    item.setTemplateId("6ac300d8cb99c4744b0e8765ed07c88b");
                    Model model2 = modelMapper.selectById(item.getTemplateId());
                    item.setTemplateName(model2.getName());
                    item.setTemplateFileId(model2.getRemoteFileId());
                    break;
                default:
                    break;
            }
        });

        PageResult<ProjectPurchasingRespVO> result = new PageResult<>();
        result.setList(list);

        result.setTotal(receiveBusinessesDOPageResult.getTotal());

        return result;
    }

    /**
     * 模板id生成副本文件（项目采购-合同草拟）
     *
     * @param templateId
     * @return
     * @throws Exception
     */
    @Override
    public Long getTemplateCopy(String templateId) throws Exception {
        Model model = modelMapper.selectById(templateId);
        // 因为范本新增时无remoteFileId字段，该校验暂时不启用
//        if (ObjectUtil.isEmpty(model.getRemoteFileId())) {
//            throw exception(ErrorCodeConstants.MODEL_NOT_EXISTS);
//        }
        if (ObjectUtil.isNotEmpty(model.getRemoteFileId())) {
            FileDTO fileDTO = fileApi.selectById(model.getRemoteFileId());
            byte[] fileContentById = fileApi.getFileContentById(model.getRemoteFileId());
            String suffix = EcontractUtil.getFileSuffix(fileDTO.getName());
            Long fileId = fileApi.uploadFile(fileDTO.getName(), FileUploadPathEnum.CONTRACT_DRAFT.getPath() + EcontractUtil.getTimeFolderPath() + IdUtil.fastSimpleUUID() + suffix, fileContentById);
            return fileId;
        } else {
            if (ObjectUtil.isNotNull(model.getModelContent())) {
                //如果没有doc文件，则将富文本html上传返回
                FileUploadRespDTO dto = fileApi.uploadFileVP(model.getName(), FileUploadPathEnum.FILE_COPY_PATH, model.getModelContent());
                return Optional.ofNullable(dto)  // 包装dto为Optional
                        .map(FileUploadRespDTO::getFileId)  // 如果dto不为null，获取fileId
                        .orElse(null);
            }


        }

        return null;
    }

    @Override
    public ProjectPurchasingRespVO getProjectPurchasingById(String id) {
        ReqIdsDTO reqIdsDTO = new ReqIdsDTO();
        List<String> ids = new ArrayList<>();
        ids.add(id);
        String str = projectPurchasingApi.queryPurchasingByIds(reqIdsDTO.setIds(ids));
        if (StringUtils.isBlank(str)) {
            return null;
        }
        cn.hutool.json.JSONObject jsonObj = new cn.hutool.json.JSONObject(str);
        JSONArray jsonArray = jsonObj.getJSONArray("data");
        // 将JSONArray转换为List
        List<ProjectPurchasingRespVO> list = JSON.parseArray(jsonArray.toString(), ProjectPurchasingRespVO.class);
        ProjectPurchasingRespVO projectPurchasingRespVO = list.get(0);
        return projectPurchasingRespVO;
    }

    @Override
    public PageResult<ApiPageRespVO> getApiPage(ApiPageReqVO apiPageReqVO) {
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectApiPage(apiPageReqVO);
        PageResult<ApiPageRespVO> apiPageRespVOPageResult = ContractConverter.INSTANCE.convertPageV2(contractDOPageResult);
        return apiPageRespVOPageResult;
    }

    /**
     * 草稿箱
     *
     * @param contractPageReqVO
     * @return
     */
    @Override
    public PageResult<ContractPageRespVO> getDraftsPage(ContractPageReqVO contractPageReqVO) {
        if (contractPageReqVO.getStatus() == null) {
            contractPageReqVO.setStatusList(CollectionUtil.newArrayList(ContractStatusEnums.TO_BE_CHECK.getCode(), ContractStatusEnums.CHECK_REJECTED.getCode(), ContractStatusEnums.APPROVE_BACK.getCode()));
        }
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectSentPage(contractPageReqVO);
        PageResult<ContractPageRespVO> contractPageRespVOPageResult = getContractPageRespVOPageV2Result(contractDOPageResult);
        if (CollectionUtil.isNotEmpty(contractPageRespVOPageResult.getList())) {
            enhanceBack4DraftsPage(contractPageRespVOPageResult);
        }
        return contractPageRespVOPageResult;
    }

    @Override
    public PageResult<ContractPageRespVO> getMyContractPage(ContractPageReqVO contractPageReqVO) {
        if (contractPageReqVO.getStatus() == null) {
            contractPageReqVO.setStatusList(CollectionUtil.newArrayList(
                    ContractStatusEnums.CHECKING.getCode(),
                    ContractStatusEnums.TO_BE_SENT.getCode(),
                    ContractStatusEnums.SENT.getCode(),
                    ContractStatusEnums.TO_BE_CONFIRMED.getCode(),
                    ContractStatusEnums.TO_BE_SIGNED.getCode(),
                    ContractStatusEnums.SIGN_COMPLETED.getCode(),
                    ContractStatusEnums.CONTRACT_AUDITSTATUS_NOT_SIGNED.getCode(),
                    ContractStatusEnums.CONTRACT_AUDITSTATUS_SEAL_APPROVAL.getCode(),
                    ContractStatusEnums.PERFORMANCE_CLOSURE.getCode(),
                    ContractStatusEnums.PERFORMING.getCode(),
                    ContractStatusEnums.PERFORMANCE_COMPLETE.getCode(),
                    ContractStatusEnums.PERFORMANCE_RISK.getCode(),
                    ContractStatusEnums.PERFORMANCE_RISK_DISPUTE.getCode(),
                    ContractStatusEnums.PERFORMANCE_RISK_PAUSE.getCode(),
                    ContractStatusEnums.PERFORMANCE_RISK_EXTENSION.getCode(),
                    ContractStatusEnums.PERFORMANCE_RISK_OVERDUE.getCode()
            ));
        }
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectSentPage(contractPageReqVO);
        PageResult<ContractPageRespVO> result = getContractPageRespVOPageV2Result(contractDOPageResult);
        // 签署流程中的合同，进行相关状态处理
        // 把当前人所在相对方的联系人的签署任务也查出来
        List<Long> userIds = new ArrayList<>(Arrays.asList(getLoginUserId()));
        // 从空间获取相对方信息
        String key4Space = SecurityFrameworkUtils.getLoginUserKey4Space();
        if (StringUtils.isNotBlank(key4Space)) {
            String relativeId = redisUtils.get(key4Space);
            if (StringUtils.isNotBlank(relativeId)) {
                Relative relative = relativeMapper.selectById(relativeId);
                if (ObjectUtil.isNotEmpty(relative)) {
//            Long oneDefaultContactId = relativeService.getOneDefaultContactId(relative.getId());
                    Long oneDefaultContactId = relative.getVirtualId();
                    if (!getLoginUserId().equals(oneDefaultContactId)) {
                        userIds.add(oneDefaultContactId);
                    }
                }
            }
        }
        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllTODORelationProcessInstanceInfosByProcessDefinitionKeys(userIds, new ArrayList<>(Arrays.asList(PROCESS_KEY_MANY)), null);
        Map<String, ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoMap = CollectionUtils.convertMap(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        result.getList().forEach(item -> {
            String statusName = getStatusName(item.getPlatform(), item.getStatus(), (Boolean) contractTypeService.isNeedSignet(new ContractTypeSignetReqVO().setId(item.getContractType())), item.getUpload(), item.getIsSign(), item.getIsFilings());
            if (StringUtils.isNotEmpty(statusName)) {
                item.setStatusName(statusName);
            }
            ContractProcessInstanceRelationInfoRespDTO relationInfoRespDTO = processInstanceRelationInfoMap.get(item.getProcessInstanceId());
            if (ObjectUtils.isNotEmpty(relationInfoRespDTO)) {
                item.setTaskId(relationInfoRespDTO.getTaskId());
                item.setHandleResult(relationInfoRespDTO.getProcessResult());
            }
        });
        result.getList().forEach(item -> {
//            if (ContractStatusEnums.CHECKING.getCode().equals(item.getStatus())) {
                item.setProcessInstanceId(null);
//            }
        });
        // 审批中的合同 进行相关状态处理
        enhanceCheckingDataPage(result);
        return result;
    }

    private void enhanceBack4DraftsPage(PageResult<ContractPageRespVO> contractPageRespVOPageResult) {
        List<String> instanceRegisterList = new ArrayList<String>();
        List<ContractPageRespVO> list = contractPageRespVOPageResult.getList();
        List<String> dolIdList = list.stream().map(ContractPageRespVO::getId).collect(Collectors.toList());
        List<BpmContract> bpmDOList = bpmContractMapper.selectList(new LambdaQueryWrapperX<BpmContract>().inIfPresent(BpmContract::getContractId, dolIdList));
        Map<String, BpmContract> bpmDOMap = CollectionUtils.convertMap(bpmDOList, BpmContract::getContractId);
        instanceRegisterList = bpmDOList.stream().map(BpmContract::getProcessInstanceId).collect(Collectors.toList());
        List<DraftOrderInfoDO> orderInfoDOList = new ArrayList<>();
        Map<String, DraftOrderInfoDO> orderInfoDOMap = new HashMap<>();
        Map<String, List<DraftOrderInfoDO>> orderInfoDOListMap = new HashMap<>();
        orderInfoDOList = gpMallOrderMapper.getOrdersByContractIds1(dolIdList);
        if (CollectionUtil.isNotEmpty(orderInfoDOList)) {
            orderInfoDOListMap = orderInfoDOList.stream()
                    .collect(Collectors.groupingBy(DraftOrderInfoDO::getOrglinkFax));
        }

        //电子交易的数据
        Map<String, PackageInfoDO> packageInfoDOMap = new HashMap<>();
        List<PackageInfoDO> packageInfoDOList = packageInfoMapper.selectList4Contract(dolIdList);
        if (CollectionUtil.isNotEmpty(packageInfoDOList)) {
            packageInfoDOMap = CollectionUtils.convertMap(packageInfoDOList, PackageInfoDO::getManagerName);
        }

        //回显流程状态taskId
        String loginUserId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());

        Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();

        List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
        List<BpmTaskAllInfoRespVO> toDOTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
        Map<String, BpmTaskAllInfoRespVO> toDOTaskEndTimeMap = new HashMap<String, BpmTaskAllInfoRespVO>();
        List<String> instanceList = new ArrayList<>();
        List<BpmContract> bpmContractList = bpmContractMapper.selectList(BpmContract::getContractId, dolIdList);
        Map<String, BpmContract> bpmContractMap = new HashMap<String, BpmContract>();
        if (CollectionUtil.isNotEmpty(bpmContractList)) {
            bpmContractMap = CollectionUtils.convertMap(bpmContractList, BpmContract::getContractId);
            instanceList = bpmContractList.stream().map(BpmContract::getProcessInstanceId).collect(Collectors.toList());
            instanceList.addAll(instanceRegisterList);
        }


        if (CollectionUtil.isNotEmpty(instanceList)) {
            taskAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(Long.valueOf(loginUserId), instanceList);
            toDOTaskAllInfoRespVOList = EcontractUtil.distinctTaskNullEndTime(taskAllInfoRespVOList);
            taskAllInfoRespVOList = EcontractUtil.distinctTask(taskAllInfoRespVOList);
            taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            toDOTaskEndTimeMap = CollectionUtils.convertMap(toDOTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

        }

        Map<String, BpmTaskAllInfoRespVO> finalTaskMap = taskMap;
        Map<String, BpmTaskAllInfoRespVO> finalToDOTaskEndTimeMap = toDOTaskEndTimeMap;
        Map<String, BpmContract> finalBpmContractMap = bpmContractMap;
        for (ContractPageRespVO item : list) {
            BpmContract registerBpmDO = bpmDOMap.get(item.getId());
            if (ObjectUtil.isNotNull(registerBpmDO)) {
                item.setProcessInstanceId(registerBpmDO.getProcessInstanceId());
                BpmProcessInstanceResultEnum resultEnum = BpmProcessInstanceResultEnum.getInstance(registerBpmDO.getResult());
                //如果是被退回
                if (ObjectUtil.isNotNull(resultEnum)) {
                    //如果是被退回的申请，回显任务id
                    if (BpmProcessInstanceResultEnum.BACK == resultEnum) {
                        BpmTaskAllInfoRespVO rejectedTask = finalToDOTaskEndTimeMap.get(registerBpmDO.getProcessInstanceId());
                        if (ObjectUtil.isNotNull(rejectedTask)) {
                            item.setTaskId(rejectedTask.getTaskId());
                        }
                    }
                    //待办任务的被分配人
                    if (BpmProcessInstanceResultEnum.APPROVE != resultEnum) {
                        BpmTaskAllInfoRespVO bpmTaskAllInfoRespVO = finalToDOTaskEndTimeMap.get(registerBpmDO.getProcessInstanceId());
                        if (ObjectUtil.isNotNull(bpmTaskAllInfoRespVO)) {
                            item.setAssigneeId(bpmTaskAllInfoRespVO.getAssigneeUserId());
                        }
                    }
                }
            }
            List<DraftOrderInfoDO> orderInfoDOList1 = orderInfoDOListMap.get(item.getId());
            if (CollectionUtil.isNotEmpty(orderInfoDOList1)) {
                DraftOrderInfoDO orderInfoDO = orderInfoDOList1.get(0);
                item.setSupplierName(orderInfoDO.getSupplierName());
                item.setOrderCode(orderInfoDO.getOrderCode());
            }
            //交易数据
            PackageInfoDO packageInfoDO = packageInfoDOMap.get(item.getId());
            if (ObjectUtil.isNotNull(packageInfoDO)) {
                item.setBiddingMethodCode(packageInfoDO.getBiddingMethodCode());
            }
        }
        contractPageRespVOPageResult.setList(list);
    }

    /**
     * 审核中
     *
     * @param contractPageReqVO
     * @return
     */
    @Override
    public PageResult<ContractPageRespVO> getCheckPage(ContractPageReqVO contractPageReqVO) {
        if (contractPageReqVO.getStatus() == null) {
            List<Integer> statusList = new ArrayList<>();
            //政采和非政采的 “我提交的合同” 列表：都是 展示审批中和审批通过的合同 0 12 2
            contractPageReqVO.setStatusList(CollectionUtil.newArrayList(
                    ContractStatusEnums.CHECKING.getCode(),
                    ContractStatusEnums.TO_BE_SENT.getCode(),
                    ContractStatusEnums.SENT.getCode(),
                    ContractStatusEnums.TO_BE_SIGNED.getCode(),
                    ContractStatusEnums.SIGN_COMPLETED.getCode(),
                    ContractStatusEnums.CONTRACT_AUDITSTATUS_NOT_SIGNED.getCode(),
                    ContractStatusEnums.CONTRACT_AUDITSTATUS_SEAL_APPROVAL.getCode(),
                    ContractStatusEnums.PERFORMANCE_CLOSURE.getCode(),
                    ContractStatusEnums.PERFORMING.getCode(),
                    ContractStatusEnums.PERFORMANCE_COMPLETE.getCode(),
                    ContractStatusEnums.PERFORMANCE_RISK.getCode(),
                    ContractStatusEnums.PERFORMANCE_RISK_DISPUTE.getCode(),
                    ContractStatusEnums.PERFORMANCE_RISK_PAUSE.getCode(),
                    ContractStatusEnums.PERFORMANCE_RISK_EXTENSION.getCode(),
                    ContractStatusEnums.PERFORMANCE_RISK_OVERDUE.getCode()
            ));
        }
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectSentPage(contractPageReqVO);
        PageResult<ContractPageRespVO> result = getContractPageRespVOPageResult(contractDOPageResult);
        result.getList().forEach(item -> item.setProcessInstanceId(null));
        enhancePage(result);
        result.getList().forEach(item -> {
            String statusName = getStatusName(item.getPlatform(), item.getStatus(), (Boolean) contractTypeService.isNeedSignet(new ContractTypeSignetReqVO().setId(item.getContractType())), item.getUpload(), item.getIsSign(), item.getIsFilings());
            if (StringUtils.isNotEmpty(statusName)) {
                item.setStatusName(statusName);
            }
        });

        return result;
    }

    private PageResult<ContractPageRespVO> enhancePage(PageResult<ContractPageRespVO> result) {
        if (CollectionUtil.isNotEmpty(result.getList())) {
            Map<String, BpmContract> bpmDOMap = new HashMap<String, BpmContract>();
            List<String> doList = result.getList().stream().map(ContractPageRespVO::getId).collect(Collectors.toList());

            List<BpmContract> bpmDOList = bpmContractMapper.selectList(new LambdaQueryWrapperX<BpmContract>().inIfPresent(BpmContract::getContractId, doList));
            if (CollectionUtil.isNotEmpty(bpmDOList)) {
                bpmDOMap = CollectionUtils.convertMap(bpmDOList, BpmContract::getContractId);
            }
            for (ContractPageRespVO item : result.getList()) {
                BpmContract bpmDO = bpmDOMap.get(item.getId());
                if (ObjectUtil.isNotNull(bpmDO)) {
                    item.setProcessInstanceId(bpmDO.getProcessInstanceId());
                    //所有终审后的合同，都显示“审批完成”
                    List<Integer> statusList = new ArrayList<>();
                    statusList.add(ContractStatusEnums.TO_BE_SENT.getCode());
                    statusList.add(ContractStatusEnums.SENT.getCode());
                    if (statusList.contains(item.getStatus())) {
                        item.setStatusName(APPROVE_COMPLETE);
                    }
                }
            }
        }
        return result;
    }

    private PageResult<ContractPageRespVO> enhanceCheckingDataPage(PageResult<ContractPageRespVO> result) {
        if (CollectionUtil.isNotEmpty(result.getList())) {
            Map<String, BpmContract> bpmDOMap = new HashMap<String, BpmContract>();
            List<String> doList = result.getList().stream().map(ContractPageRespVO::getId).collect(Collectors.toList());

            List<BpmContract> bpmDOList = bpmContractMapper.selectList(new LambdaQueryWrapperX<BpmContract>().inIfPresent(BpmContract::getContractId, doList));
            if (CollectionUtil.isNotEmpty(bpmDOList)) {
                bpmDOMap = CollectionUtils.convertMap(bpmDOList, BpmContract::getContractId);
            }
            for (ContractPageRespVO item : result.getList()) {
                BpmContract bpmDO = bpmDOMap.get(item.getId());
                if (ObjectUtil.isNotNull(bpmDO)/* && ContractStatusEnums.CHECKING.getCode().equals(item.getStatus())*/) {
                    item.setProcessInstanceId(bpmDO.getProcessInstanceId());
                    //所有终审后的合同，都显示“审批完成”
                    List<Integer> statusList = new ArrayList<>();
                    statusList.add(ContractStatusEnums.TO_BE_SENT.getCode());
                    statusList.add(ContractStatusEnums.SENT.getCode());
                    if (statusList.contains(item.getStatus())) {
                        item.setStatusName(APPROVE_COMPLETE);
                    }
                }
            }
        }
        return result;
    }

    /**
     * 代送审
     *
     * @param contractPageReqVO
     * @return
     */
    @Override
    public PageResult<ContractPageRespVO> getToCheckPage(ContractPageReqVO contractPageReqVO) {
        if (contractPageReqVO.getStatus() == null) {
            contractPageReqVO.setStatusList(CollectionUtil.newArrayList(ContractStatusEnums.TO_BE_CHECK.getCode()));
        }
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectSentPage(contractPageReqVO);
        PageResult<ContractPageRespVO> result = getContractPageRespVOPageResult(contractDOPageResult);
        return result;
    }

    @Override
    public PageResult<ContractPageRespVO> getRegisterPage(ContractPageReqVO contractPageReqVO) {
        if (ContractStatusEnums.TO_BE_CHECK.getCode().equals(contractPageReqVO.getStatus())) {
            List<Integer> statusList = new ArrayList<>(Arrays.asList(0, 4, 5));
            contractPageReqVO.setStatusList(statusList);
        } else if (ContractStatusEnums.CHECKING.getCode().equals(contractPageReqVO.getStatus())) {
            contractPageReqVO.setStatus(1);
        }
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectRegisterPage(contractPageReqVO);
        PageResult<ContractPageRespVO> result = getContractPageRespVOPageResult(contractDOPageResult);
        //封装审批状态result
        Map<String, BpmContractRegisterDO> stringBpmContractRegisterDOrMap = new HashMap<>();
        List<String> contractIds = result.getList().stream().map(ContractPageRespVO::getId).collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(contractIds)) {
            bpmContractRegisterMapper.selectList(BpmContractRegisterDO::getContractId, contractIds).forEach(item -> {
                stringBpmContractRegisterDOrMap.put(item.getContractId(), item);
            });
        } else {
            // 处理空列表的情况，可以添加相应的逻辑
        }
        result.getList().forEach(item -> {
            String id = item.getId();
            BpmContractRegisterDO bpmContractRegisterDO = stringBpmContractRegisterDOrMap.get(id);
            if (bpmContractRegisterDO != null) {
                item.setResult(bpmContractRegisterDO.getResult());
                item.setResultName(BpmProcessInstanceResultEnum.getInstance(bpmContractRegisterDO.getResult()).getDesc());
            }
        });

        if (CollectionUtil.isNotEmpty(result.getList())) {
            enhanceBack(result.getList());
        }

        //判断审批中列表能否撤回
        if (ContractStatusEnums.CHECKING.getCode().equals(contractPageReqVO.getStatus())) {
            List<String> idS = result.getList().stream().map(ContractPageRespVO::getId).collect(Collectors.toList());
            List<BpmContractRegisterDO> bpmContractRegisterDOS = new ArrayList<>();
            Map<String, BpmContractRegisterDO> stringBpmContractRegisterDOMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(idS)) {
                bpmContractRegisterDOS = bpmContractRegisterMapper.selectList(BpmContractRegisterDO::getContractId, idS);
                stringBpmContractRegisterDOMap = CollectionUtils.convertMap(bpmContractRegisterDOS, BpmContractRegisterDO::getContractId);
            }
            //待处理的任务
            List<BpmTaskAllInfoRespVO> toDoTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            Map<String, BpmTaskAllInfoRespVO> untreatedTaskDTOMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

            if (CollectionUtil.isNotEmpty(bpmContractRegisterDOS)) {
                List<String> instanceList = bpmContractRegisterDOS.stream().map(BpmContractRegisterDO::getProcessInstanceId).collect(Collectors.toList());

                if (CollectionUtil.isNotEmpty(instanceList)) {
                    taskAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(SecurityFrameworkUtils.getLoginUserId(), instanceList);

                    toDoTaskAllInfoRespVOList = EcontractUtil.distinctTaskNullEndTime(taskAllInfoRespVOList);
                    untreatedTaskDTOMap = CollectionUtils.convertMap(toDoTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

                }
            }
            if (CollectionUtil.isNotEmpty(untreatedTaskDTOMap) && CollectionUtil.isNotEmpty(stringBpmContractRegisterDOMap)) {
                for (ContractPageRespVO respVO : result.getList()) {
                    //判断是否可以撤回。
                    BpmContractRegisterDO bpmContractRegisterDO = stringBpmContractRegisterDOMap.get(respVO.getId());
                    if (ObjectUtil.isNotEmpty(bpmContractRegisterDO)) {
                        BpmTaskAllInfoRespVO taskDTO = untreatedTaskDTOMap.get(bpmContractRegisterDO.getProcessInstanceId());
                        if (ObjectUtil.isNotNull(taskDTO)) {
                            respVO.setAssigneeId(taskDTO.getAssigneeUserId());
                        }
                    }
                }
            }
        }
        return result;
    }

    private void enhanceBack(List<ContractPageRespVO> list) {
        List<String> instanceRegisterList = new ArrayList<String>();
        List<String> dolIdList = list.stream().map(ContractPageRespVO::getId).collect(Collectors.toList());
        List<BpmContractRegisterDO> bpmDOList = bpmContractRegisterMapper.selectList(new LambdaQueryWrapperX<BpmContractRegisterDO>().inIfPresent(BpmContractRegisterDO::getContractId, dolIdList));
        Map<String, BpmContractRegisterDO> bpmDOMap = CollectionUtils.convertMap(bpmDOList, BpmContractRegisterDO::getContractId);
        instanceRegisterList = bpmDOList.stream().map(BpmContractRegisterDO::getProcessInstanceId).collect(Collectors.toList());

        //回显流程状态taskId
        String loginUserId = String.valueOf(SecurityFrameworkUtils.getLoginUserId());

        Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();

        List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
        List<BpmTaskAllInfoRespVO> toDOTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
        Map<String, BpmTaskAllInfoRespVO> toDOTaskEndTimeMap = new HashMap<String, BpmTaskAllInfoRespVO>();
        List<String> instanceList = new ArrayList<>();
        List<BpmContractRegisterDO> bpmContractList = bpmContractRegisterMapper.selectList(BpmContractRegisterDO::getContractId, dolIdList);
        Map<String, BpmContractRegisterDO> bpmContractMap = new HashMap<String, BpmContractRegisterDO>();
        if (CollectionUtil.isNotEmpty(bpmContractList)) {
            bpmContractMap = CollectionUtils.convertMap(bpmContractList, BpmContractRegisterDO::getContractId);
            instanceList = bpmContractList.stream().map(BpmContractRegisterDO::getProcessInstanceId).collect(Collectors.toList());
            instanceList.addAll(instanceRegisterList);
        }


        if (CollectionUtil.isNotEmpty(instanceList)) {
            taskAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(Long.valueOf(loginUserId), instanceList);
            toDOTaskAllInfoRespVOList = EcontractUtil.distinctTaskNullEndTime(taskAllInfoRespVOList);
            taskAllInfoRespVOList = EcontractUtil.distinctTask(taskAllInfoRespVOList);
            taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            toDOTaskEndTimeMap = CollectionUtils.convertMap(toDOTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);

        }

        Map<String, BpmTaskAllInfoRespVO> finalTaskMap = taskMap;
        Map<String, BpmTaskAllInfoRespVO> finalToDOTaskEndTimeMap = toDOTaskEndTimeMap;
        Map<String, BpmContractRegisterDO> finalBpmContractMap = bpmContractMap;
        for (ContractPageRespVO item : list) {
            BpmContractRegisterDO registerBpmDO = bpmDOMap.get(item.getId());
            if (ObjectUtil.isNotNull(registerBpmDO)) {
                item.setProcessInstanceId(registerBpmDO.getProcessInstanceId());
                BpmProcessInstanceResultEnum resultEnum = BpmProcessInstanceResultEnum.getInstance(registerBpmDO.getResult());
                //如果是被退回
                if (ObjectUtil.isNotNull(resultEnum)) {
                    //如果是被退回的申请，回显任务id
                    if (BpmProcessInstanceResultEnum.BACK == resultEnum) {
                        BpmTaskAllInfoRespVO rejectedTask = finalToDOTaskEndTimeMap.get(registerBpmDO.getProcessInstanceId());
                        if (ObjectUtil.isNotNull(rejectedTask)) {
                            item.setTaskId(rejectedTask.getTaskId());
                        }
                    }
                    //待办任务的被分配人
                    if (BpmProcessInstanceResultEnum.APPROVE != resultEnum) {
                        BpmTaskAllInfoRespVO bpmTaskAllInfoRespVO = finalToDOTaskEndTimeMap.get(registerBpmDO.getProcessInstanceId());
                        if (ObjectUtil.isNotNull(bpmTaskAllInfoRespVO)) {
                            item.setAssigneeId(bpmTaskAllInfoRespVO.getAssigneeUserId());
                        }
                    }
                }

            }

        }

    }

    /**
     * 合同管理 - 合同草拟
     * 登录用户作为  -发起人-
     * 合同状态为  -新增|被退回-
     *
     * @param contractPageReqVO
     * @return
     */
    @Override
    public PageResult<ContractPageRespVO> getSentPage(ContractPageReqVO contractPageReqVO) {
        if (contractPageReqVO.getStatus() == null) {
            contractPageReqVO.setStatusList(CollectionUtil.newArrayList(ContractStatusEnums.TO_BE_SENT.getCode(), ContractStatusEnums.BE_SENT_BACK.getCode()));
        }
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectSentPage(contractPageReqVO);
        PageResult<ContractPageRespVO> contractPageRespVOPageResult = ContractConverter.INSTANCE.convertPage(contractDOPageResult);
        if (CollectionUtil.isNotEmpty(contractDOPageResult.getList())) {


            //获取任务id
            Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            Map<String, BpmTaskAllInfoRespVO> taskEndTimeMap = new HashMap<String, BpmTaskAllInfoRespVO>();

            List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> taskEndTimeAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();


            List<ContractDO> contractDOList = contractDOPageResult.getList();

            List<ContractPageRespVO> respVOList = contractPageRespVOPageResult.getList();
            List<String> idList = contractDOList.stream().map(ContractDO::getId).collect(Collectors.toList());
            Map<String, ContractDO> modelMap = CollectionUtils.convertMap(contractDOList, ContractDO::getId);
            List<BpmContract> modelBpmDOList = bpmContractMapper.selectList(new LambdaQueryWrapperX<BpmContract>().inIfPresent(BpmContract::getContractId, idList));

            Map<String, BpmContract> modelBpmDOMap = CollectionUtils.convertMap(modelBpmDOList, BpmContract::getContractId);
            List<String> instanceList = modelBpmDOList.stream().map(BpmContract::getProcessInstanceId).collect(Collectors.toList());

            if (CollectionUtil.isNotEmpty(instanceList)) {
                taskAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(Long.valueOf(getLoginUserId()), instanceList);
                taskAllInfoRespVOList = EcontractUtil.distinctTask(taskAllInfoRespVOList);
                taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            }
            //有结束时间的流程任务
            Map<String, BpmContract> modelEndTimeBpmDOMap = CollectionUtils.convertMap(modelBpmDOList, BpmContract::getContractId);
            if (CollectionUtil.isNotEmpty(instanceList)) {
                taskEndTimeAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(Long.valueOf(getLoginUserId()), instanceList);
                taskEndTimeAllInfoRespVOList = EcontractUtil.distinctTaskHaveEndTime(taskEndTimeAllInfoRespVOList);
                taskEndTimeMap = CollectionUtils.convertMap(taskEndTimeAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            }
            if (CollectionUtil.isNotEmpty(respVOList)) {
                for (ContractPageRespVO respVO : respVOList) {
                    respVO.setProcessInstanceId(null);
                    BpmContract bpmDO = modelBpmDOMap.get(respVO.getId());
                    if (ObjectUtil.isNotNull(bpmDO)) {
                        BpmTaskAllInfoRespVO taskAllInfoRespVO = taskMap.get(bpmDO.getProcessInstanceId());
                        if (ObjectUtil.isNotNull(taskAllInfoRespVO)) {
                            respVO.setTaskId(taskAllInfoRespVO.getTaskId());
                        }
                        respVO.setProcessInstanceId(bpmDO.getProcessInstanceId());
                    }
                }

            }
            //结果集添加 合同分类名称-合同类型名称-签署方名称
            if (CollectionUtil.isNotEmpty(contractPageRespVOPageResult.getList())) {
                packageName(contractPageRespVOPageResult);
            }
        }
        return contractPageRespVOPageResult;
    }

    /**
     * 展示结果集添加 合同分类名称-合同类型名称-签署方名称
     *
     * @param contractDOPageResult
     * @return
     */
    @NotNull
    private PageResult<ContractPageRespVO> getContractPageRespVOPageResult(PageResult<ContractDO> contractDOPageResult) {
        //封装展示集合结果
        PageResult<ContractPageRespVO> result = ContractConverter.INSTANCE.convertPage(contractDOPageResult);
        packageName(result);
        return result;
    }

    @NotNull
    private PageResult<ContractPageRespVO> getContractPageRespVOPageV2Result(PageResult<ContractDO> contractDOPageResult) {
        //封装展示集合结果
        PageResult<ContractPageRespVO> result = ContractConverter.INSTANCE.convertPage(contractDOPageResult);
        packageName4PageV2(result);
        return result;
    }

    private void packageNameV1(PageResult<ContractPageRespVO> result) {
        for (ContractPageRespVO contractPageRespVO : result.getList()) {
            contractPageRespVO.setContractCategoryName(contractCategoryMapper.selectById(contractPageRespVO.getContractCategory()).getName());
            contractPageRespVO.setContractTypeName(contractTypeMapper.selectById(contractPageRespVO.getContractType()).getName());
            List<SignatoryRelDO> signatoryRelDOList = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractPageRespVO.getId());
            //添加创建方名称
            contractPageRespVO.setCreatorName(adminUserApi.getUser(WebFrameworkUtils.getLoginUserId()).getNickname());
            //获取相对方id集合
            List<String> collect = signatoryRelDOList.stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toList());
            //创建签署方名称集合
            List<String> signatoryList = new ArrayList<>();
            //添加发起方公司名称
            ArrayList<Long> longs = new ArrayList<>();
            longs.add(WebFrameworkUtils.getLoginUserId());
            List<UserCompanyInfoRespDTO> userCompanyInfoList = companyApi.getUserCompanyInfoList(longs);
            if (CollectionUtil.isNotEmpty(userCompanyInfoList) && StringUtils.isNotBlank(userCompanyInfoList.get(0).getName())) {
                contractPageRespVO.setInitiator(userCompanyInfoList.get(0).getName());
            } else {
                //发起方是个体，通过id 获取nickname
                List<AdminUserRespDTO> userListByDeptIds = adminUserApi.getUserListByDeptIds(userCompanyInfoList.stream().map(UserCompanyInfoRespDTO::getUserId).collect(Collectors.toList()));
                if (CollectionUtil.isNotEmpty(userListByDeptIds)) {
                    signatoryList.add(userListByDeptIds.get(0).getNickname());
                }
            }
            for (String id : collect) {
                Relative relative = relativeMapper.selectById(id);
                if (ObjectUtil.isNotEmpty(relative)) {
                    if (StringUtils.isNotEmpty(relative.getCompanyName())) {
                        signatoryList.add(relative.getCompanyName());
                    } else {
                        signatoryList.add(relative.getName());
                    }
                }
            }
            contractPageRespVO.setSignatoryList(signatoryList);
        }
    }

    private void packageNameV3(PageResult<ContractPageRespVO> result) {
        //登录用户的信息传递给线程池中的任务
        Long userId = WebFrameworkUtils.getLoginUserId();

        List<Integer> categoryList = result.getList().stream().map(ContractPageRespVO::getContractCategory).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(categoryList)) {
            List<ContractCategory> contractCategoryList = contractCategoryMapper.selectList(ContractCategory::getId, categoryList);
            // 将ContractCategory对象映射到一个Map，以便后续查找
            Map<Integer, ContractCategory> categoryMap = contractCategoryList.stream().collect(Collectors.toMap(ContractCategory::getId, c -> c));

            // 更新ContractPageRespVO对象的contractCategoryName
            for (ContractPageRespVO contractPageRespVO : result.getList()) {
                Integer categoryId = contractPageRespVO.getContractCategory();
                ContractCategory category = categoryMap.get(categoryId);

                if (category != null) {
                    contractPageRespVO.setContractCategoryName(category.getName());
                }
            }
        }


        List<String> typeList = result.getList().stream().map(ContractPageRespVO::getContractType).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(typeList)) {
            List<ContractType> contractTypeList = contractTypeMapper.selectList(ContractType::getId, typeList);
            Map<String, ContractType> typeMap = contractTypeList.stream().collect(Collectors.toMap(ContractType::getId, c -> c));

            for (ContractPageRespVO contractPageRespVO : result.getList()) {
                String contractType = contractPageRespVO.getContractType();
                ContractType type = typeMap.get(contractType);

                if (type != null) {
                    contractPageRespVO.setContractTypeName(type.getName());
                }
            }
        }

        for (ContractPageRespVO contractPageRespVO : result.getList()) {
            List<SignatoryRelDO> signatoryRelDOList = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractPageRespVO.getId());
            //添加创建方名称
            contractPageRespVO.setCreatorName(adminUserApi.getUser(userId).getNickname());
            //获取相对方id集合
            List<String> collect = signatoryRelDOList.stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toList());
            //创建签署方名称集合
            List<String> signatoryList = new ArrayList<>();
            //添加发起方公司名称
            ArrayList<Long> longs = new ArrayList<>();
            longs.add(userId);
            List<UserCompanyInfoRespDTO> userCompanyInfoList = companyApi.getUserCompanyInfoList(longs);
            if (CollectionUtil.isNotEmpty(userCompanyInfoList) && StringUtils.isNotBlank(userCompanyInfoList.get(0).getName())) {
                contractPageRespVO.setInitiator(userCompanyInfoList.get(0).getName());
            } else {
                //发起方是个体，通过id 获取nickname
                List<AdminUserRespDTO> userListByDeptIds = adminUserApi.getUserListByDeptIds(userCompanyInfoList.stream().map(UserCompanyInfoRespDTO::getUserId).collect(Collectors.toList()));
                signatoryList.add(userListByDeptIds.get(0).getNickname());
            }
            for (String id : collect) {
                Relative relative = relativeMapper.selectById(id);
                if (ObjectUtil.isNotEmpty(relative)) {
                    if (StringUtils.isNotEmpty(relative.getCompanyName())) {
                        signatoryList.add(relative.getCompanyName());
                    } else {
                        signatoryList.add(relative.getName());
                    }
                }
            }
            contractPageRespVO.setSignatoryList(signatoryList);
        }
    }

    private void packageName(PageResult<ContractPageRespVO> result) {

        if (CollectionUtil.isNotEmpty(result.getList())) {
            List<String> contIds = result.getList().stream().map(ContractPageRespVO::getId).collect(Collectors.toList());
            List<TradingContractExtDO> tradingContractExtDOS = tradingContractExtMapper.selectList(TradingContractExtDO::getId, contIds);
            Map<String, String> contractAndPackMap = tradingContractExtDOS.stream().filter(Objects::nonNull)
                    .filter(extDO -> extDO.getId() != null && extDO.getBuyPlanPackageId() != null).collect(Collectors.toMap(TradingContractExtDO::getId, TradingContractExtDO::getBuyPlanPackageId));

            List<String> packageIds = tradingContractExtDOS.stream().map(TradingContractExtDO::getBuyPlanPackageId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(packageIds)) {
                List<PackageInfoDO> packageInfoDOS = packageInfoMapper.selectList(PackageInfoDO::getPackageGuid, packageIds);
                if (CollectionUtil.isNotEmpty(packageInfoDOS)) {
                    Map<String, PackageInfoDO> packageMap = packageInfoDOS.stream().collect(Collectors.toMap(PackageInfoDO::getPackageGuid, item -> item));
                    for (ContractPageRespVO contractPageRespVO : result.getList()) {
                        String id = contractPageRespVO.getId(); //合同id
                        String packageId = contractAndPackMap.get(id); // 包id
                        PackageInfoDO packageInfoDO = packageMap.get(packageId);
                        if (ObjectUtil.isNotEmpty(packageInfoDO)) {
                            contractPageRespVO.setPackageNumber(packageInfoDO.getPackageNumber());
                            contractPageRespVO.setPackageName(packageInfoDO.getPackageName());
                        }
                    }
                }
            }


            List<Integer> categoryIds = result.getList().stream().map(ContractPageRespVO::getContractCategory).filter(Objects::nonNull).collect(Collectors.toList());
            List<ContractCategory> contractCategories = new ArrayList<>();
//            if (CollectionUtil.isNotEmpty(categoryIds)) {
//                contractCategories = contractCategoryMapper.selectList(ContractCategory::getId, categoryIds);
//            }
            Map<Integer, ContractCategory> contractCategoryMap = CollectionUtils.convertMap(contractCategories, ContractCategory::getId);

            List<String> typeIds = result.getList().stream().map(ContractPageRespVO::getContractType).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            AtomicReference<Map<String, ContractType>> contractTypeMap = new AtomicReference<>(new HashMap<>());
            if (CollectionUtil.isNotEmpty(typeIds)) {
                //（相对方逻辑）免租户
                DataPermissionUtils.executeIgnore(() -> {
                    TenantUtils.executeIgnore(() -> {
                        List<ContractType> contractTypes = contractTypeMapper.selectList(ContractType::getId, typeIds);
                        contractTypeMap.set(CollectionUtils.convertMap(contractTypes, ContractType::getId));
                    });
                });
            }
            List<String> contractIds = result.getList().stream().map(ContractPageRespVO::getId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            //增强审批节点名称
            Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList;
            List<BpmContract> bpmDOList = bpmContractMapper.selectList(new LambdaQueryWrapperX<BpmContract>().inIfPresent(BpmContract::getContractId, contractIds)
                    .select(BpmContract::getContractId, BpmContract::getProcessInstanceId, BpmContract::getResult));

            Map<String, BpmContract> modelBpmDOMap = CollectionUtils.convertMap(bpmDOList, BpmContract::getContractId);
            List<String> instanceList = bpmDOList.stream().map(BpmContract::getProcessInstanceId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(instanceList)) {
                taskAllInfoRespVOList = bpmTaskApi.getAllTaskIdByProcessInstanceIds(instanceList);
                taskAllInfoRespVOList = EcontractUtil.distinctTask(taskAllInfoRespVOList);
                taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            }
            // 增强remark9
            List<ContractOrderExtDO> extDOList = new ArrayList<>();
            Map<String, ContractOrderExtDO> extMap = new HashMap<>();
            extDOList = contractOrderExtMapper.selectList(new LambdaQueryWrapperX<ContractOrderExtDO>().select(ContractOrderExtDO::getId, ContractOrderExtDO::getRemark9));
            if (CollectionUtil.isNotEmpty(extDOList)) {
                extMap = CollectionUtils.convertMap(extDOList, ContractOrderExtDO::getId);
            }
            List<SignatoryRelDO> signatoryRelations = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractIds);

            Map<String, List<SignatoryRelDO>> contractRelationMap = CollectionUtils.convertMultiMap(signatoryRelations, SignatoryRelDO::getContractId);

            List<String> relationDataIds = signatoryRelations.stream().map(SignatoryRelDO::getSignatoryId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            Map<String, Relative> relativeMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(relationDataIds)) {
                List<Relative> relatives = relativeMapper.selectBatchIds(relationDataIds);
                relativeMap = CollectionUtils.convertMap(relatives, Relative::getId);
            }

            List<Long> creatorIds = result.getList().stream().map(item -> Long.valueOf(item.getCreator())).collect(Collectors.toList());

            List<AdminUserRespDTO> userInfo = adminUserApi.getUserList(creatorIds);
            Map<Long, AdminUserRespDTO> longAdminUserRespDTOMap = CollectionUtils.convertMap(userInfo, AdminUserRespDTO::getId);
            List<UserCompanyInfoRespDTO> userCompanyInfoList = companyApi.getUserCompanyInfoList(creatorIds);
            Map<Long, UserCompanyInfoRespDTO> companyInfoRespDTOMap = userCompanyInfoList.stream().collect(Collectors.toMap(UserCompanyInfoRespDTO::getUserId, Function.identity(), (v1, v2) -> v2));

            Map<String, Relative> finalRelativeMap = relativeMap;
            Map<String, ContractOrderExtDO> finalExtMap = extMap;
            Map<String, BpmTaskAllInfoRespVO> finalTaskMap = taskMap;
            //根据合同id获取订单id，在获取计划
            List<ContractOrderExtDO> contractOrderExtDOS = contractOrderExtMapper.selectList(new LambdaQueryWrapperX<ContractOrderExtDO>().in(ContractOrderExtDO::getId, contractIds));
            List<String> orderIds = contractOrderExtDOS.stream().map(ContractOrderExtDO::getOrderId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            Map<String, ContractOrderExtDO> contractOrderExtMap = CollectionUtils.convertMap(contractOrderExtDOS, ContractOrderExtDO::getId);
            Map<String, AssociatedPlanDO> associatedPlanMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(orderIds)) {
                List<AssociatedPlanDO> associatedPlanDOS = associatedPlanMapper.selectList(new LambdaQueryWrapperX<AssociatedPlanDO>().in(AssociatedPlanDO::getOrderId, orderIds));
                associatedPlanMap = CollectionUtils.convertMap(associatedPlanDOS, AssociatedPlanDO::getOrderId);
            }
            LoginUser user = SecurityFrameworkUtils.getLoginUser();
            List<String> relativeContractIds = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(user)) {
                List<Relative> relatives = relativeMapper.selectList4Relative(user.getId());
                if (CollectionUtil.isNotEmpty(relatives)) {
                    List<String> relativeIds = relatives.stream().map(Relative::getId).collect(Collectors.toList());
                    List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getSignatoryId, relativeIds);
                    //当前用户作为相对方的合同id集合
                    relativeContractIds = signatoryRelDOS.stream().map(SignatoryRelDO::getContractId).collect(Collectors.toList());
                }
            }

            Map<String, AssociatedPlanDO> finalAssociatedPlanMap = associatedPlanMap;
            for (ContractPageRespVO item : result.getList()) {

                ContractOrderExtDO contractOrderExtDO = contractOrderExtMap.get(item.getId());
                if (ObjectUtil.isNotEmpty(contractOrderExtDO)) {
                    AssociatedPlanDO associatedPlanDO = finalAssociatedPlanMap.get(contractOrderExtDO.getOrderId());
                    item.setIsNeedBak(ObjectUtil.isEmpty(associatedPlanDO) || !"1".equals(associatedPlanDO.getBuyPlanSource()) ? 0 : 1);
                }

                BpmContract bpmContract = modelBpmDOMap.get(item.getId());
                if (ObjectUtil.isNotNull(bpmContract) && ObjectUtil.isNotNull(finalTaskMap.get(bpmContract.getProcessInstanceId()))) {
                    item.setNodeName(finalTaskMap.get(bpmContract.getProcessInstanceId()).getName());
                }

                ContractOrderExtDO orderExtDO = finalExtMap.get(item.getId());
                if (ObjectUtil.isNotNull(orderExtDO)) {
                    item.setRemark9(orderExtDO.getRemark9());
                }

                if (item.getPlatform() != null) {
                    item.setPlatformName(PlatformEnums.getInstance(item.getPlatform()).getInfo());
                }
                // 更新ContractPageRespVO对象的contractCategoryName
                ContractCategory category = contractCategoryMap.get(item.getContractCategory());
                if (category != null) {
                    item.setContractCategoryName(category.getName());
                }
                ContractType contractType = contractTypeMap.get().get(item.getContractType());
                if (contractType != null) {
                    item.setContractTypeName(contractType.getName());
                    item.setContractCategoryName(contractType.getName());
                }
                AdminUserRespDTO adminUserRespDTO = longAdminUserRespDTOMap.get(Long.valueOf(item.getCreator()));
                if (adminUserRespDTO != null) {
                    item.setCreatorName(adminUserRespDTO.getNickname());
                }
                UserCompanyInfoRespDTO companyInfo = companyInfoRespDTOMap.get(Long.valueOf(item.getCreator()));
                if (companyInfo != null) {
                    item.setInitiator(companyInfo.getName());
                } else {
                    item.setInitiator(item.getPartAName());
                }
                List<SignatoryRelDO> signatoryRelDOS = contractRelationMap.get(item.getId());
                List<String> signatoryList = new ArrayList<>();
                if (CollectionUtil.isNotEmpty(signatoryRelDOS)) {
                    signatoryRelDOS.forEach(rel -> {
                        Relative relative = finalRelativeMap.get(rel.getSignatoryId());
                        if (relative != null) {
                            signatoryList.add(relative.getCompanyName());
                        }
                    });
                }
                if (signatoryList.size() == 0 && StringUtils.isNotEmpty(item.getPartBName())) {
                    signatoryList.add(item.getPartBName());
                }
                item.setSignatoryList(signatoryList);
                // 添加当前用户是否是相对方标识
                item.setIsRelative((CollectionUtil.isNotEmpty(relativeContractIds) && relativeContractIds.contains(item.getId())) ? IfNumEnums.YES.getCode() : IfNumEnums.NO.getCode());
            }
        }
    }

    private void packageName4PageV2(PageResult<ContractPageRespVO> result) {

        if (CollectionUtil.isNotEmpty(result.getList())) {
            List<String> contIds = result.getList().stream().map(ContractPageRespVO::getId).collect(Collectors.toList());
            List<TradingContractExtDO> tradingContractExtDOS = tradingContractExtMapper.selectList(TradingContractExtDO::getId, contIds);
            Map<String, String> contractAndPackMap = tradingContractExtDOS.stream().filter(Objects::nonNull)
                    .filter(extDO -> extDO.getId() != null && extDO.getBuyPlanPackageId() != null).collect(Collectors.toMap(TradingContractExtDO::getId, TradingContractExtDO::getBuyPlanPackageId));

            List<String> packageIds = tradingContractExtDOS.stream().map(TradingContractExtDO::getBuyPlanPackageId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(packageIds)) {
                List<PackageInfoDO> packageInfoDOS = packageInfoMapper.selectList(PackageInfoDO::getPackageGuid, packageIds);
                if (CollectionUtil.isNotEmpty(packageInfoDOS)) {
                    Map<String, PackageInfoDO> packageMap = packageInfoDOS.stream().collect(Collectors.toMap(PackageInfoDO::getPackageGuid, item -> item));
                    for (ContractPageRespVO contractPageRespVO : result.getList()) {
                        String id = contractPageRespVO.getId(); //合同id
                        String packageId = contractAndPackMap.get(id); // 包id
                        PackageInfoDO packageInfoDO = packageMap.get(packageId);
                        if (ObjectUtil.isNotEmpty(packageInfoDO)) {
                            contractPageRespVO.setPackageNumber(packageInfoDO.getPackageNumber());
                            contractPageRespVO.setPackageName(packageInfoDO.getPackageName());
                        }
                    }
                }
            }


            List<Integer> categoryIds = result.getList().stream().map(ContractPageRespVO::getContractCategory).filter(Objects::nonNull).collect(Collectors.toList());
            List<ContractCategory> contractCategories = new ArrayList<>();
            Map<Integer, ContractCategory> contractCategoryMap = CollectionUtils.convertMap(contractCategories, ContractCategory::getId);

            List<String> typeIds = result.getList().stream().map(ContractPageRespVO::getContractType).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            AtomicReference<Map<String, ContractType>> contractTypeMap = new AtomicReference<>(new HashMap<>());
            if (CollectionUtil.isNotEmpty(typeIds)) {
                //（相对方逻辑）免租户
                DataPermissionUtils.executeIgnore(() -> {
                    TenantUtils.executeIgnore(() -> {
                        List<ContractType> contractTypes = contractTypeMapper.selectList();
                        contractTypeMap.set(CollectionUtils.convertMap(contractTypes, ContractType::getId));
                    });
                });
            }
            List<String> contractIds = result.getList().stream().map(ContractPageRespVO::getId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            //增强审批节点名称
            Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList;
            List<BpmContract> bpmDOList = bpmContractMapper.selectList(new LambdaQueryWrapperX<BpmContract>().inIfPresent(BpmContract::getContractId, contractIds)
                    .select(BpmContract::getContractId, BpmContract::getProcessInstanceId, BpmContract::getResult));

            Map<String, BpmContract> modelBpmDOMap = CollectionUtils.convertMap(bpmDOList, BpmContract::getContractId);
            List<String> instanceList = bpmDOList.stream().map(BpmContract::getProcessInstanceId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(instanceList)) {
                taskAllInfoRespVOList = bpmTaskApi.getAllTaskIdByProcessInstanceIds(instanceList);
                taskAllInfoRespVOList = EcontractUtil.distinctTask(taskAllInfoRespVOList);
                taskMap = CollectionUtils.convertMap(taskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            }
            // 增强remark9
            List<ContractOrderExtDO> extDOList = new ArrayList<>();
            Map<String, ContractOrderExtDO> extMap = new HashMap<>();
            extDOList = contractOrderExtMapper.selectList(new LambdaQueryWrapperX<ContractOrderExtDO>().select(ContractOrderExtDO::getId, ContractOrderExtDO::getRemark9));
            if (CollectionUtil.isNotEmpty(extDOList)) {
                extMap = CollectionUtils.convertMap(extDOList, ContractOrderExtDO::getId);
            }
            List<SignatoryRelDO> signatoryRelations = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractIds);

            Map<String, List<SignatoryRelDO>> contractRelationMap = CollectionUtils.convertMultiMap(signatoryRelations, SignatoryRelDO::getContractId);

            List<String> relationDataIds = signatoryRelations.stream().map(SignatoryRelDO::getSignatoryId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            Map<String, Relative> relativeMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(relationDataIds)) {
                List<Relative> relatives = relativeMapper.selectBatchIds(relationDataIds);
                relativeMap = CollectionUtils.convertMap(relatives, Relative::getId);
            }

            List<Long> creatorIds = result.getList().stream().map(item -> Long.valueOf(item.getCreator())).collect(Collectors.toList());

            List<AdminUserRespDTO> userInfo = adminUserApi.getUserList(creatorIds);
            Map<Long, AdminUserRespDTO> longAdminUserRespDTOMap = CollectionUtils.convertMap(userInfo, AdminUserRespDTO::getId);
            List<UserCompanyInfoRespDTO> userCompanyInfoList = companyApi.getUserCompanyInfoList(creatorIds);
            Map<Long, UserCompanyInfoRespDTO> companyInfoRespDTOMap = userCompanyInfoList.stream().collect(Collectors.toMap(UserCompanyInfoRespDTO::getUserId, Function.identity(), (v1, v2) -> v2));

            Map<String, Relative> finalRelativeMap = relativeMap;
            Map<String, ContractOrderExtDO> finalExtMap = extMap;
            Map<String, BpmTaskAllInfoRespVO> finalTaskMap = taskMap;
            //根据合同id获取订单id，在获取计划
            List<ContractOrderExtDO> contractOrderExtDOS = contractOrderExtMapper.selectList(new LambdaQueryWrapperX<ContractOrderExtDO>().in(ContractOrderExtDO::getId, contractIds));
            List<String> orderIds = contractOrderExtDOS.stream().map(ContractOrderExtDO::getOrderId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
            Map<String, ContractOrderExtDO> contractOrderExtMap = CollectionUtils.convertMap(contractOrderExtDOS, ContractOrderExtDO::getId);
            Map<String, AssociatedPlanDO> associatedPlanMap = new HashMap<>();
            if (CollectionUtil.isNotEmpty(orderIds)) {
                List<AssociatedPlanDO> associatedPlanDOS = associatedPlanMapper.selectList(new LambdaQueryWrapperX<AssociatedPlanDO>().in(AssociatedPlanDO::getOrderId, orderIds));
                associatedPlanMap = CollectionUtils.convertMap(associatedPlanDOS, AssociatedPlanDO::getOrderId);
            }
            LoginUser user = SecurityFrameworkUtils.getLoginUser();
            List<String> relativeContractIds = new ArrayList<>();
            if (ObjectUtil.isNotEmpty(user)) {
                List<Relative> relatives = relativeMapper.selectList4Relative(user.getId());
                if (CollectionUtil.isNotEmpty(relatives)) {
                    List<String> relativeIds = relatives.stream().map(Relative::getId).collect(Collectors.toList());
                    List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getSignatoryId, relativeIds);
                    //当前用户作为相对方的合同id集合
                    relativeContractIds = signatoryRelDOS.stream().map(SignatoryRelDO::getContractId).collect(Collectors.toList());
                }
            }

            Map<String, AssociatedPlanDO> finalAssociatedPlanMap = associatedPlanMap;
            for (ContractPageRespVO item : result.getList()) {

                ContractOrderExtDO contractOrderExtDO = contractOrderExtMap.get(item.getId());
                if (ObjectUtil.isNotEmpty(contractOrderExtDO)) {
                    AssociatedPlanDO associatedPlanDO = finalAssociatedPlanMap.get(contractOrderExtDO.getOrderId());
                    item.setIsNeedBak(ObjectUtil.isEmpty(associatedPlanDO) || !"1".equals(associatedPlanDO.getBuyPlanSource()) ? 0 : 1);
                }

                BpmContract bpmContract = modelBpmDOMap.get(item.getId());
                if (ObjectUtil.isNotNull(bpmContract) && ObjectUtil.isNotNull(finalTaskMap.get(bpmContract.getProcessInstanceId()))) {
                    item.setNodeName(finalTaskMap.get(bpmContract.getProcessInstanceId()).getName());
                }

                ContractOrderExtDO orderExtDO = finalExtMap.get(item.getId());
                if (ObjectUtil.isNotNull(orderExtDO)) {
                    item.setRemark9(orderExtDO.getRemark9());
                }

                if (item.getPlatform() != null) {
                    item.setPlatformName(PlatformEnums.getInstance(item.getPlatform()).getInfo());
                }
                // 更新ContractPageRespVO对象的contractCategoryName
                ContractCategory category = contractCategoryMap.get(item.getContractCategory());
                if (category != null) {
                    item.setContractCategoryName(category.getName());
                }
                String contractType = getContractTypeAll(item.getContractType(), new StringBuilder(), contractTypeMap.get());
//                ContractType contractType = contractTypeMap.get().get(item.getContractType());
                if (contractType != null) {
                    item.setContractTypeName(contractType);
                    item.setContractCategoryName(contractType);
                }
                AdminUserRespDTO adminUserRespDTO = longAdminUserRespDTOMap.get(Long.valueOf(item.getCreator()));
                if (adminUserRespDTO != null) {
                    item.setCreatorName(adminUserRespDTO.getNickname());
                }
                UserCompanyInfoRespDTO companyInfo = companyInfoRespDTOMap.get(Long.valueOf(item.getCreator()));
                if (companyInfo != null) {
                    item.setInitiator(companyInfo.getName());
                } else {
                    item.setInitiator(item.getPartAName());
                }
                List<SignatoryRelDO> signatoryRelDOS = contractRelationMap.get(item.getId());
                List<String> signatoryList = new ArrayList<>();
                if (CollectionUtil.isNotEmpty(signatoryRelDOS)) {
                    signatoryRelDOS.forEach(rel -> {
                        Relative relative = finalRelativeMap.get(rel.getSignatoryId());
                        if (relative != null) {
                            signatoryList.add(relative.getCompanyName());
                        }
                    });
                }
                if (signatoryList.size() == 0 && StringUtils.isNotEmpty(item.getPartBName())) {
                    signatoryList.add(item.getPartBName());
                }
                item.setSignatoryList(signatoryList);
                // 添加当前用户是否是相对方标识
                item.setIsRelative((CollectionUtil.isNotEmpty(relativeContractIds) && relativeContractIds.contains(item.getId())) ? IfNumEnums.YES.getCode() : IfNumEnums.NO.getCode());
            }
        }
    }

    private String getContractTypeAll(String contractType, StringBuilder contractTypeAll, Map<String, ContractType> contractTypeMap) {
        ContractType type = contractTypeMap.get(contractType);
        if (type != null && StringUtils.isNotEmpty(type.getParentId())) {
            contractTypeAll.insert(0,"/" + type.getName());
            getContractTypeAll(type.getParentId(), contractTypeAll, contractTypeMap);
        }
        if (contractTypeAll.length() == 0) {
            return "";
        } else {
            return contractTypeAll.substring(1, contractTypeAll.length());
        }
    }

    /**
     * 合同管理 - 合同草拟
     * 登录用户作为  -发起人-
     * 合同状态为   -已发送-
     *
     * @param contractPageReqVO
     * @return
     */
    @Override
    public PageResult<ContractPageRespVO> getSendPage(ContractPageReqVO contractPageReqVO) {
        if (contractPageReqVO.getStatus() == null) {
//            contractPageReqVO.setStatusList(CollectionUtil.newArrayList(ContractStatusEnums.SENT.getCode(), ContractStatusEnums.TO_BE_CONFIRMED.getCode()));
            contractPageReqVO.setStatusList(CollectionUtil.newArrayList(ContractStatusEnums.SENT.getCode()));
        }
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectSentPage(contractPageReqVO);
        PageResult<ContractPageRespVO> result = getContractPageRespVOPageResult(contractDOPageResult);
        return result;
    }

    /**
     * 合同管理 - 合同草拟
     * 登录用户作为  -发起人-
     * 合同状态为   -待确认- （待发起签署）
     *
     * @param contractPageReqVO
     * @return
     */
    @Override
    public PageResult<ContractPageRespVO> getConfirmPage(ContractPageReqVO contractPageReqVO) {
        if (contractPageReqVO.getStatus() == null) {
            contractPageReqVO.setStatusList(CollectionUtil.newArrayList(ContractStatusEnums.TO_BE_CONFIRMED.getCode()));
        }
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectSentPage(contractPageReqVO);
        PageResult<ContractPageRespVO> contractPageRespVOPageResult = getContractPageRespVOPageResult(contractDOPageResult);
        if (CollectionUtil.isEmpty(contractPageRespVOPageResult.getList())) {
            return PageResult.empty(0L);
        }
//老流程定义
        Map<String, String> taskMap = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(WebFrameworkUtils.getLoginUserId(), new ArrayList<>(Arrays.asList(PROCESS_KEY_TRIPARTITE_NOT, PROCESS_KEY_BOTH_OLD, PROCESS_KEY_BOTH, PROCESS_KEY_TRIPARTITE, PROCESS_KEY_MANY)), CONFIRM).stream().collect(Collectors.toMap(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId, ContractProcessInstanceRelationInfoRespDTO::getTaskId));
//        Map<String, String> taskMap = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(WebFrameworkUtils.getLoginUserId(), new ArrayList<>(Arrays.asList(ActivityConfigurationEnum.ECMS_CONTRACT_BOTH.getDefinitionKey(), ActivityConfigurationEnum.ECMS_CONTRACT_TRIPARTITE.getDefinitionKey())), CONFIRM).stream().collect(Collectors.toMap(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId, ContractProcessInstanceRelationInfoRespDTO::getTaskId));

        for (ContractPageRespVO contractPageRespVO : contractPageRespVOPageResult.getList()) {
            String taskId = taskMap.get(contractPageRespVO.getProcessInstanceId());
            if (StringUtils.isNotBlank(taskId)) {
                contractPageRespVO.setTaskId(taskId);
            }
        }
        return contractPageRespVOPageResult;
    }

    /**
     * 合同确认-查询合同列表
     * 签署方=登录用户
     *
     * @param contractPageReqVO
     * @return
     */
    @DataPermission(enable = false)
    @Override
    public PageResult<ContractPageRespVO> getAffirmPage(ContractPageReqVO contractPageReqVO) {
        contractPageReqVO.setStatusList(CollectionUtil.newArrayList(ContractStatusEnums.SENT.getCode(), ContractStatusEnums.TO_BE_CONFIRMED.getCode()));//, ContractStatusEnums.TO_BE_SIGNED.getCode()
        //添加当前用户作为相对方的合同id集合，作为查询条件
//        Relative relative = relativeMapper.selectOne(Relative::getContactId, WebFrameworkUtils.getLoginUserId());
        // 相对方多用户逻辑
        LoginUser user = SecurityFrameworkUtils.getLoginUser();
        /*if (StringUtils.isBlank(user.getMobile())) {
            throw exception(DIY_ERROR, "当前用户手机号未填写，请补充信息");
        }*/
        String key4Space = SecurityFrameworkUtils.getLoginUserKey4Space();
        if (StringUtils.isBlank(key4Space)) {
            return PageResult.empty();
        }
        String relativeId = redisUtils.get(key4Space);
        if (StringUtils.isBlank(relativeId)) {
            return PageResult.empty();
        }
        Relative relative = relativeMapper.selectById(relativeId);
        if (ObjectUtil.isEmpty(relative)) {
            return PageResult.empty(0L);
        }

        AtomicReference<Map<Long, AdminUserRespDTO>> userMap = new AtomicReference<>();

        //（相对方逻辑）免租户
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                contractPageReqVO.setContractIdList(signatoryRelMapper.selectList(SignatoryRelDO::getSignatoryId, relative.getId()).stream().map(SignatoryRelDO::getContractId).collect(Collectors.toList()));
            });
        });
        //工作流执行人操作状态查询
        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(WebFrameworkUtils.getLoginUserId(), new ArrayList<>(Arrays.asList(PROCESS_KEY_BOTH_OLD, PROCESS_KEY_TRIPARTITE_NOT, PROCESS_KEY_BOTH, PROCESS_KEY_TRIPARTITE, PROCESS_KEY_MANY)), CONFIRM);
//        Long oneDefaultContactId = relativeService.getOneDefaultContactId(relative.getId());
        Long oneDefaultContactId = relative.getVirtualId();
        if (ObjectUtil.isNotEmpty(relative) && !getLoginUserId().equals(oneDefaultContactId)) {
            List<ContractProcessInstanceRelationInfoRespDTO> relativeProcessInstanceInfos = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(oneDefaultContactId, new ArrayList<>(Arrays.asList(PROCESS_KEY_BOTH_OLD, PROCESS_KEY_TRIPARTITE_NOT, PROCESS_KEY_BOTH, PROCESS_KEY_TRIPARTITE, PROCESS_KEY_MANY)), CONFIRM);
            allRelationProcessInstanceInfos.addAll(relativeProcessInstanceInfos);
        }

        List<String> processInstanceIds = CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        if (CollectionUtil.isEmpty(processInstanceIds) || CollectionUtil.isEmpty(contractPageReqVO.getContractIdList())) {
            return PageResult.empty(0L);
        }
        contractPageReqVO.setProcessInstanceIds(processInstanceIds);
        Map<String, ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoMap = CollectionUtils.convertMap(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        AtomicReference<PageResult<ContractDO>> contractDOPageResult = new AtomicReference<>(new PageResult<>());
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                PageResult<ContractDO> t = selectAffirmPage(contractPageReqVO, null);
                contractDOPageResult.set(t);
            });
        });
        PageResult<ContractPageRespVO> result = getContractPageRespVOPageResult(contractDOPageResult.get());
        result.getList().forEach(resp -> {
            ContractProcessInstanceRelationInfoRespDTO relationInfoRespDTO = processInstanceRelationInfoMap.get(resp.getProcessInstanceId());
            if (ObjectUtils.isNotEmpty(relationInfoRespDTO)) {
                resp.setTaskId(relationInfoRespDTO.getTaskId());
                resp.setHandleResult(relationInfoRespDTO.getProcessResult());
            }
        });
        return result;
    }

    /**
     * 合同管理-合同签署
     * 签署方-登录用户
     *
     * @param contractPageReqVO
     * @return
     */
    @DataPermission(enable = false)
    @Override
    public PageResult<ContractPageRespVO> getSignPage(ContractPageReqVO contractPageReqVO) {
        if (contractPageReqVO.getStatus() == null) {
            contractPageReqVO.setStatusList(CollectionUtil.newArrayList(ContractStatusEnums.TO_BE_SIGNED.getCode(), ContractStatusEnums.SIGN_REJECTED.getCode(), ContractStatusEnums.SIGN_COMPLETED.getCode(), ContractStatusEnums.SIGN_OVERDUE.getCode(), ContractStatusEnums.TERMINATED.getCode(), ContractStatusEnums.CONTRACT_AUDITSTATUS_NOT_SIGNED.getCode(), ContractStatusEnums.CONTRACT_AUDITSTATUS_SEAL_APPROVAL.getCode()));
        }

        //工作流执行人操作状态查询
        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(WebFrameworkUtils.getLoginUserId(), new ArrayList<>(Arrays.asList(PROCESS_KEY_BOTH_OLD, PROCESS_KEY_TRIPARTITE_NOT, PROCESS_KEY_BOTH, PROCESS_KEY_TRIPARTITE, PROCESS_KEY_MANY)), SIGN);

        List<String> processInstanceIds = CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
//        if (CollectionUtil.isEmpty(processInstanceIds)) {
//            return PageResult.empty(0L);
//        }
        //（相对方逻辑）免租户
        AtomicReference<PageResult<ContractPageRespVO>> atomic = new AtomicReference<>();
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {


                contractPageReqVO.setProcessInstanceIds(processInstanceIds);
                Map<String, ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoMap = CollectionUtils.convertMap(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
                //获取用户部门id
                LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
                AdminUserRespDTO user = adminUserApi.getUser(loginUser.getId());
                PageResult<ContractDO> contractDOPageResult = selectAffirmPage(contractPageReqVO, user);

                PageResult<ContractPageRespVO> result = getContractPageRespVOPageResult(contractDOPageResult);
                result.getList().forEach(resp -> {
                    ContractProcessInstanceRelationInfoRespDTO relationInfoRespDTO = processInstanceRelationInfoMap.get(resp.getProcessInstanceId());
                    if (ObjectUtils.isNotEmpty(relationInfoRespDTO)) {
                        resp.setTaskId(relationInfoRespDTO.getTaskId());
                        resp.setHandleResult(relationInfoRespDTO.getProcessResult());
                    }
                });
                atomic.set(result);
            });
        });
        return atomic.get();

    }

    /**
     * 合同管理-合同签署
     * 签署方-登录用户
     * 已签署
     *
     * @param contractPageReqVO
     * @return
     */
    @DataPermission(enable = false)
    @Override
    public PageResult<ContractPageRespVO> getWaitSignPage(ContractPageReqVO contractPageReqVO) {

        contractPageReqVO.setIsSign(IfNumEnums.NO.getCode());
        if (IfNumEnums.YES.getCode().equals(contractPageReqVO.getIsGov())) {
            contractPageReqVO.setUploadList(CollectionUtil.newArrayList(THIRD_PARTY.getCode(), ContractUploadTypeEnums.ORDER_DRAFT.getCode()));
        }
        // 查出全部类型
        if (CollectionUtil.isNotEmpty(contractPageReqVO.getContractTypes())) {
            List<String> types = new ArrayList();
            selectAllChildTypes(contractPageReqVO.getContractTypes(), types);
            contractPageReqVO.setContractTypes(types);
        }
        //工作流执行人操作状态查询
        // 把当前人所在相对方的联系人的签署任务也查出来
        List<Long> userIds = new ArrayList<>(Arrays.asList(getLoginUserId()));

        // 从空间获取相对方信息
        String key4Space = SecurityFrameworkUtils.getLoginUserKey4Space();
        if (StringUtils.isNotBlank(key4Space)) {
            String relativeId = redisUtils.get(key4Space);
            Relative relative = new Relative();
            if (StringUtils.isNotBlank(relativeId)) {
                relative = relativeMapper.selectById(relativeId);
            } else {
                relative = relativeMapper.get4AffirmPage(getLoginUserId());
            }
            if (ObjectUtil.isNotEmpty(relative)) {
//            Long oneDefaultContactId = relativeService.getOneDefaultContactId(relative.getId());
                Long oneDefaultContactId = relative.getVirtualId();
                if (!getLoginUserId().equals(oneDefaultContactId)) {
                    userIds.add(oneDefaultContactId);
                }
            }
        }

        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllTODORelationProcessInstanceInfosByProcessDefinitionKeys(userIds, new ArrayList<>(Arrays.asList(PROCESS_KEY_BOTH_OLD, PROCESS_KEY_TRIPARTITE_NOT, PROCESS_KEY_BOTH, PROCESS_KEY_TRIPARTITE, PROCESS_KEY_MANY)), SIGN);

        List<String> processInstanceIds = CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        //（相对方逻辑）免租户
        AtomicReference<PageResult<ContractPageRespVO>> atomic = new AtomicReference<>();
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                contractPageReqVO.setProcessInstanceIds(processInstanceIds);
                Map<String, ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoMap = CollectionUtils.convertMap(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
                //获取用户部门id
                LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
                AdminUserRespDTO user = adminUserApi.getUser(loginUser.getId());
                PageResult<ContractDO> contractDOPageResult = selectWaitSignPage(contractPageReqVO, user);

                PageResult<ContractPageRespVO> result = getContractPageRespVOPageResult(contractDOPageResult);
                result.getList().forEach(resp -> {
                    ContractProcessInstanceRelationInfoRespDTO relationInfoRespDTO = processInstanceRelationInfoMap.get(resp.getProcessInstanceId());
                    if (ObjectUtils.isNotEmpty(relationInfoRespDTO)) {
                        resp.setTaskId(relationInfoRespDTO.getTaskId());
                        resp.setHandleResult(relationInfoRespDTO.getProcessResult());
                    }
                });
                atomic.set(result);
            });
        });
        return atomic.get();

    }

    /**
     * 合同管理-合同确认
     * 签署方-登录用户
     * 待确认
     *
     * @param contractPageReqVO
     * @return
     */
    @DataPermission(enable = false)
    @Override
    public PageResult<ContractPageRespVO> getWaitConfirmPage(ContractPageReqVO contractPageReqVO) {

        if (IfNumEnums.YES.getCode().equals(contractPageReqVO.getIsGov())) {
            contractPageReqVO.setUploadList(CollectionUtil.newArrayList(ContractUploadTypeEnums.THIRD_PARTY.getCode(), ContractUploadTypeEnums.ORDER_DRAFT.getCode()));
        }
        // 查出全部类型
        if (CollectionUtil.isNotEmpty(contractPageReqVO.getContractTypes())) {
            List<String> types = new ArrayList();
            selectAllChildTypes(contractPageReqVO.getContractTypes(), types);
            contractPageReqVO.setContractTypes(types);
        }
        // 把当前人所在相对方的联系人的签署任务也查出来
        List<Long> userIds = new ArrayList<>(Arrays.asList(getLoginUserId()));
        // 从空间获取相对方信息
        String key4Space = SecurityFrameworkUtils.getLoginUserKey4Space();
        if (StringUtils.isBlank(key4Space)) {
            return PageResult.empty();
        }
        String relativeId = redisUtils.get(key4Space);
        Relative relative = new Relative();
        if (StringUtils.isBlank(relativeId)) {
            // 如果不能从缓存中获得当前相对方。则尝试查询数据库
            relative = relativeMapper.get4AffirmPage(getLoginUserId());
//            return PageResult.empty();
        } else {
            relative = relativeMapper.selectById(relativeId);
        }
        if (ObjectUtil.isNotEmpty(relative)) {
//            Long oneDefaultContactId = relativeService.getOneDefaultContactId(relative.getId());
            Long oneDefaultContactId = relative.getVirtualId();
            if (!getLoginUserId().equals(oneDefaultContactId)) {
                userIds.add(oneDefaultContactId);
            }
        }
        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllTODORelationProcessInstanceInfosByProcessDefinitionKeys(userIds, new ArrayList<>(Arrays.asList(PROCESS_KEY_BOTH_OLD, PROCESS_KEY_TRIPARTITE_NOT, PROCESS_KEY_BOTH, PROCESS_KEY_TRIPARTITE, PROCESS_KEY_MANY)), CONFIRM);

        List<String> processInstanceIds = CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        if (CollectionUtil.isEmpty(processInstanceIds)) {
            return PageResult.empty(0L);
        }
        //（相对方逻辑）免租户
        AtomicReference<PageResult<ContractPageRespVO>> atomic = new AtomicReference<>();
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                contractPageReqVO.setProcessInstanceIds(processInstanceIds);
                Map<String, ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoMap = CollectionUtils.convertMap(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
                //获取用户部门id
                LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
                AdminUserRespDTO user = adminUserApi.getUser(loginUser.getId());
                PageResult<ContractDO> contractDOPageResult = selectWaitConfirmPage(contractPageReqVO, user);

                PageResult<ContractPageRespVO> result = getContractPageRespVOPageResult(contractDOPageResult);
                result.getList().forEach(resp -> {
                    ContractProcessInstanceRelationInfoRespDTO relationInfoRespDTO = processInstanceRelationInfoMap.get(resp.getProcessInstanceId());
                    if (ObjectUtils.isNotEmpty(relationInfoRespDTO)) {
                        resp.setTaskId(relationInfoRespDTO.getTaskId());
                        resp.setHandleResult(relationInfoRespDTO.getProcessResult());
                    }
                });
                atomic.set(result);
            });
        });
        return atomic.get();

    }


    @DataPermission(enable = false)
    @Override
    public PageResult<ContractPageRespVO> getDoneConfirmPage(ContractPageReqVO contractPageReqVO) {

        if (IfNumEnums.YES.getCode().equals(contractPageReqVO.getIsGov())) {
            contractPageReqVO.setUploadList(CollectionUtil.newArrayList(ContractUploadTypeEnums.THIRD_PARTY.getCode(), ContractUploadTypeEnums.ORDER_DRAFT.getCode()));
        }
        // 查出全部类型
        if (CollectionUtil.isNotEmpty(contractPageReqVO.getContractTypes())) {
            List<String> types = new ArrayList();
            selectAllChildTypes(contractPageReqVO.getContractTypes(), types);
            contractPageReqVO.setContractTypes(types);
        }
        // 把当前人所在相对方的联系人的签署任务也查出来
        List<Long> userIds = new ArrayList<>(Arrays.asList(getLoginUserId()));
        // 从空间获取相对方信息
        String key4Space = SecurityFrameworkUtils.getLoginUserKey4Space();
        if (StringUtils.isBlank(key4Space)) {
            return PageResult.empty();
        }
        String relativeId = redisUtils.get(key4Space);
        Relative relative = new Relative();
        if (StringUtils.isBlank(relativeId)) {
            // 如果不能从缓存中获得当前相对方。则尝试查询数据库
            relative = relativeMapper.get4AffirmPage(getLoginUserId());
//            return PageResult.empty();
        } else {
            relative = relativeMapper.selectById(relativeId);
        }
        if (ObjectUtil.isNotEmpty(relative)) {
//            Long oneDefaultContactId = relativeService.getOneDefaultContactId(relative.getId());
            Long oneDefaultContactId = relative.getVirtualId();
            if (!getLoginUserId().equals(oneDefaultContactId)) {
                userIds.add(oneDefaultContactId);
            }
        }
        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(userIds, new ArrayList<>(Arrays.asList(PROCESS_KEY_BOTH_OLD, PROCESS_KEY_TRIPARTITE_NOT, PROCESS_KEY_BOTH, PROCESS_KEY_TRIPARTITE, PROCESS_KEY_MANY)), CONFIRM);

        List<String> processInstanceIds = CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        //（相对方逻辑）免租户
        AtomicReference<PageResult<ContractPageRespVO>> atomic = new AtomicReference<>();
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                contractPageReqVO.setProcessInstanceIds(processInstanceIds);
                Map<String, ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoMap = CollectionUtils.convertMap(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
                //获取用户部门id
                LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
                AdminUserRespDTO user = adminUserApi.getUser(loginUser.getId());
                PageResult<ContractDO> contractDOPageResult = selectDoneConfirmPage(contractPageReqVO, user);

                // 用于判断相对方是否为操作合同确认退回的人
                List<Relative> relatives = relativeMapper.selectList4Relative(user.getId());
                List<String> relativeIds = relatives.stream().map(Relative::getId).collect(Collectors.toList());
                List<String> contractIds = new ArrayList<>();
                if (CollectionUtil.isNotEmpty(relativeIds)) {
                    List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getSignatoryId, relativeIds);
                    contractIds = signatoryRelDOS.stream().filter(e -> IfNumEnums.RJ.getCode().equals(e.getIsConfirm())).map(SignatoryRelDO::getContractId).collect(Collectors.toList());
                }
                // 处理返回结果
                PageResult<ContractPageRespVO> result = getContractPageRespVOPageResult(contractDOPageResult);
                for (ContractPageRespVO resp : result.getList()) {
                    ContractProcessInstanceRelationInfoRespDTO relationInfoRespDTO = processInstanceRelationInfoMap.get(resp.getProcessInstanceId());
                    if (ObjectUtils.isNotEmpty(relationInfoRespDTO)) {
                        resp.setTaskId(relationInfoRespDTO.getTaskId());
                        resp.setHandleResult(relationInfoRespDTO.getProcessResult());
                    }
                    // 判断相对方是否为操作合同确认退回的人
                    if (CollectionUtil.isNotEmpty(contractIds) && contractIds.contains(resp.getId())) {
                        resp.setIsRJRelative(IfNumEnums.YES.getCode());
                    } else {
                        resp.setIsRJRelative(IfNumEnums.NO.getCode());
                    }
                }
                atomic.set(result);
            });
        });
        return atomic.get();

    }

    /**
     * 合同管理-合同签署
     * 签署方-登录用户
     * 已签署
     *
     * @param contractPageReqVO
     * @return
     */
    @DataPermission(enable = false)
    @Override
    public PageResult<ContractPageRespVO> getDoneSignPage(ContractPageReqVO contractPageReqVO) {
        if (IfNumEnums.YES.getCode().equals(contractPageReqVO.getIsGov())) {
            contractPageReqVO.setUploadList(CollectionUtil.newArrayList(THIRD_PARTY.getCode(), ContractUploadTypeEnums.ORDER_DRAFT.getCode()));
        }
        // 查出全部类型
        if (CollectionUtil.isNotEmpty(contractPageReqVO.getContractTypes())) {
            List<String> types = new ArrayList();
            selectAllChildTypes(contractPageReqVO.getContractTypes(), types);
            contractPageReqVO.setContractTypes(types);
        }

        // 把当前人所在相对方的联系人的签署任务也查出来
        List<Long> userIds = new ArrayList<>(Arrays.asList(getLoginUserId()));
        // 从空间获取相对方信息
        String key4Space = SecurityFrameworkUtils.getLoginUserKey4Space();
        if (StringUtils.isNotBlank(key4Space)) {
            String relativeId = redisUtils.get(key4Space);
            Relative relative = new Relative();
            if (StringUtils.isNotBlank(relativeId)) {
                relative = relativeMapper.selectById(relativeId);
            } else {
                relative = relativeMapper.get4AffirmPage(getLoginUserId());
            }
            if (ObjectUtil.isNotEmpty(relative)) {
//            Long oneDefaultContactId = relativeService.getOneDefaultContactId(relative.getId());
                Long oneDefaultContactId = relative.getVirtualId();
                if (!getLoginUserId().equals(oneDefaultContactId)) {
                    userIds.add(oneDefaultContactId);
                }
            }
        }

        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(userIds, new ArrayList<>(Arrays.asList(PROCESS_KEY_BOTH_OLD, PROCESS_KEY_TRIPARTITE_NOT, PROCESS_KEY_BOTH, PROCESS_KEY_TRIPARTITE, PROCESS_KEY_MANY)), SIGN);

        List<String> processInstanceIds = CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        //（相对方逻辑）免租户
        AtomicReference<PageResult<ContractPageRespVO>> atomic = new AtomicReference<>();
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                contractPageReqVO.setProcessInstanceIds(processInstanceIds);
                Map<String, ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoMap = CollectionUtils.convertMap(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
                //获取用户部门id
                LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
                AdminUserRespDTO user = adminUserApi.getUser(loginUser.getId());
                PageResult<ContractDO> contractDOPageResult = selectSignedPage(contractPageReqVO, user);

                // 用于判断相对方是否为操作合同确认退回的人
                List<Relative> relatives = relativeMapper.selectList4Relative(user.getId());
                List<String> relativeIds = relatives.stream().map(Relative::getId).collect(Collectors.toList());
                List<String> contractIds = new ArrayList<>();
                if (CollectionUtil.isNotEmpty(relativeIds)) {
                    List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getSignatoryId, relativeIds);
                    contractIds = signatoryRelDOS.stream().filter(e -> IfNumEnums.RJ.getCode().equals(e.getIsSign())).map(SignatoryRelDO::getContractId).collect(Collectors.toList());
                }

                PageResult<ContractPageRespVO> result = getContractPageRespVOPageResult(contractDOPageResult);
                for (ContractPageRespVO resp : result.getList()) {
                    ContractProcessInstanceRelationInfoRespDTO relationInfoRespDTO = processInstanceRelationInfoMap.get(resp.getProcessInstanceId());
                    if (ObjectUtils.isNotEmpty(relationInfoRespDTO)) {
                        resp.setTaskId(relationInfoRespDTO.getTaskId());
                        resp.setHandleResult(relationInfoRespDTO.getProcessResult());
                    }
                    // 判断相对方是否为操作合同确认退回的人
                    if (CollectionUtil.isNotEmpty(contractIds) && contractIds.contains(resp.getId())) {
                        resp.setIsRJRelative(IfNumEnums.YES.getCode());
                    } else {
                        resp.setIsRJRelative(IfNumEnums.NO.getCode());
                    }
                }

                atomic.set(result);
            });
        });
        return atomic.get();
    }

    /**
     * 合同管理-用印申请合同
     * 签署方-登录用户
     *
     * @param contractPageReqVO
     * @return
     */
    @DataPermission(enable = false)
    @Override
    public PageResult<ContractPageRespVO> getSignetPage(ContractPageReqVO contractPageReqVO) {

        //输入参数处理
        contractPageReqVO.setStatus(ContractStatusEnums.CONTRACT_AUDITSTATUS_NOT_SIGNED.getCode());
        // contractPageReqVO.setIsSign(IfNumEnums.NO.getCode());
        if (IfNumEnums.YES.getCode().equals(contractPageReqVO.getIsGov())) {
            contractPageReqVO.setUploadList(CollectionUtil.newArrayList(THIRD_PARTY.getCode(), ContractUploadTypeEnums.ORDER_DRAFT.getCode()));
        } else if (IfNumEnums.NO.getCode().equals(contractPageReqVO.getIsGov())) {
            contractPageReqVO.setUploadList(CollectionUtil.newArrayList(ContractUploadTypeEnums.MODEL_DRAFT.getCode(), ContractUploadTypeEnums.UPLOAD_FILE.getCode(), REGISTER.getCode(), ContractUploadTypeEnums.COMPANY_LEVEL.getCode(), ContractUploadTypeEnums.UPLOAD_CONTRACT_FILE.getCode()));
        }
        // 合同类型值替换
        if (ObjectUtil.isNotNull(contractPageReqVO.getContractType())) {
            ProjectCategoryEnums projectCategoryEnums = ProjectCategoryEnums.getInstance(contractPageReqVO.getContractType());
            if (ObjectUtil.isNotNull(projectCategoryEnums)) {
                List<ContractType> contractTypes = contractTypeMapper.selectList(ContractType::getPlatId, String.valueOf(projectCategoryEnums.getValue()));
                if (CollectionUtil.isNotEmpty(contractTypes)) {
                    contractPageReqVO.setContractType(contractTypes.get(0).getId());
                }
            }
        }
        if (CollectionUtil.isNotEmpty(contractPageReqVO.getContractTypes())) {
            List<String> types = contractPageReqVO.getContractTypes();
            for (int i = 0; i < types.size(); i++) {
                ProjectCategoryEnums projectCategoryEnums = ProjectCategoryEnums.getInstance(types.get(i));
                if (ObjectUtil.isNotNull(projectCategoryEnums)) {
                    List<ContractType> contractTypes = contractTypeMapper.selectList(ContractType::getPlatId, String.valueOf(projectCategoryEnums.getValue()));
                    if (CollectionUtil.isNotEmpty(contractTypes)) {
                        types.set(i, contractTypes.get(0).getId());
                    }
                }
            }
            contractPageReqVO.setContractTypes(types);
        }
        //工作流执行人操作状态查询
        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(WebFrameworkUtils.getLoginUserId(), new ArrayList<>(Arrays.asList(PROCESS_KEY_BOTH_OLD, PROCESS_KEY_TRIPARTITE_NOT, PROCESS_KEY_BOTH, PROCESS_KEY_TRIPARTITE, PROCESS_KEY_MANY)), SIGN);

//        List<String> processInstanceIds = CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        //（相对方逻辑）免租户
        AtomicReference<PageResult<ContractPageRespVO>> atomic = new AtomicReference<>();
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
//                contractPageReqVO.setProcessInstanceIds(processInstanceIds);
                Map<String, ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoMap = CollectionUtils.convertMap(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
                //获取用户部门id
                LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
                AdminUserRespDTO user = adminUserApi.getUser(loginUser.getId());
                //转成产品的合同类型数据(非交易)
                PageResult<ContractDO> contractDOPageResult = selectAffirmPage(contractPageReqVO, user);

                PageResult<ContractPageRespVO> result = getContractPageRespVOPageResult(contractDOPageResult);
                result.getList().forEach(resp -> {
                    ContractProcessInstanceRelationInfoRespDTO relationInfoRespDTO = processInstanceRelationInfoMap.get(resp.getProcessInstanceId());
                    if (ObjectUtils.isNotEmpty(relationInfoRespDTO)) {
                        resp.setTaskId(relationInfoRespDTO.getTaskId());
                        resp.setHandleResult(relationInfoRespDTO.getProcessResult());
                    }
                });
                atomic.set(result);
            });
        });
        return atomic.get();
    }

    @Override
    public ContractGovOrNotNumRespVO getGovOrNotNum(String type) {

        ContractPageReqVO contractPageReqVO = new ContractPageReqVO();
        switch (type) {
            case "signetAdd": //用印申请发起页
                contractPageReqVO.setStatus(ContractStatusEnums.CONTRACT_AUDITSTATUS_NOT_SIGNED.getCode());
                //contractPageReqVO.setIsSign(IfNumEnums.YES.getCode());
                break;
            default:
        }
        //工作流执行人操作状态查询
        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(WebFrameworkUtils.getLoginUserId(), new ArrayList<>(Arrays.asList(PROCESS_KEY_BOTH_OLD, PROCESS_KEY_TRIPARTITE_NOT, PROCESS_KEY_BOTH, PROCESS_KEY_TRIPARTITE, PROCESS_KEY_MANY)), SIGN);

        List<String> processInstanceIds = CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        //（相对方逻辑）免租户
        AtomicReference<ContractGovOrNotNumRespVO> atomic = new AtomicReference<>();
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                //获取用户部门id
                LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
                AdminUserRespDTO user = adminUserApi.getUser(loginUser.getId());
                //转成产品的合同类型数据(非交易)
                MPJLambdaWrapper<ContractDO> mpjQueryWrapper = new MPJLambdaWrapper<ContractDO>().select(ContractDO::getId, ContractDO::getStatus, ContractDO::getUpload, ContractDO::getIsSign, ContractDO::getProcessInstanceId, ContractDO::getDeptId);
                //查询字段
                if (ObjectUtil.isNotEmpty(contractPageReqVO.getStatus())) {
                    mpjQueryWrapper.eq(ContractDO::getStatus, contractPageReqVO.getStatus());
                }
                // 签署状态
                if (ObjectUtil.isNotEmpty(contractPageReqVO.getIsSign())) {
                    mpjQueryWrapper.eq(ContractDO::getIsSign, contractPageReqVO.getIsSign());
                }
                //默认查询合同状态字段
                if (ObjectUtil.isNotEmpty(contractPageReqVO.getStatusList())) {
                    mpjQueryWrapper.in(ContractDO::getStatus, contractPageReqVO.getStatusList());
                }
                if (ObjectUtil.isNotEmpty(user)) {
                    List<Relative> relatives = relativeMapper.selectList4Relative(loginUser.getId());
                    if (CollectionUtil.isNotEmpty(relatives)) {
                        List<String> relativeIds = relatives.stream().map(Relative::getId).collect(Collectors.toList());
                        List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getSignatoryId, relativeIds);
                        List<String> contractIds = signatoryRelDOS.stream().map(SignatoryRelDO::getContractId).collect(Collectors.toList());
                        if (CollectionUtil.isNotEmpty(contractIds)) {
                            mpjQueryWrapper.and(w -> w.eq(ContractDO::getDeptId, user.getDeptId()).or().in(ContractDO::getId, contractIds));
                        } else {
                            mpjQueryWrapper.and(w -> w.eq(ContractDO::getDeptId, user.getDeptId()));
                        }
                    } else {
                        mpjQueryWrapper.and(w -> w.eq(ContractDO::getDeptId, user.getDeptId()));
                    }
                }
//                if (CollectionUtil.isNotEmpty(processInstanceIds)) {
//                    mpjQueryWrapper.in(ContractDO::getProcessInstanceId, processInstanceIds);
//                }
                List<ContractDO> contractDOS = contractMapper.selectList(mpjQueryWrapper);
                long govNum = contractDOS.stream().filter(contractDO -> THIRD_PARTY.getCode().equals(contractDO.getUpload()) || ContractUploadTypeEnums.ORDER_DRAFT.getCode().equals(contractDO.getUpload())).count();
                long notGovNum = contractDOS.size() - govNum;
                atomic.set(new ContractGovOrNotNumRespVO().setGovContractNum(govNum).setNotGovContractNum(notGovNum));
            });
        });
        return atomic.get();
    }

    /**
     * 归档管理-电子签署
     *
     * @param contractPageReqVO
     * @return
     */
    @Override
    public PageResult<ContractPageRespVO> getFilingPage(ContractPageReqVO contractPageReqVO) {
        if (ObjectUtil.isEmpty(contractPageReqVO.getDocument())) {
            contractPageReqVO.setDocumentList(new ArrayList<>(Arrays.asList(0, 1, 2)));
        }
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectFilingPage(contractPageReqVO);
        PageResult<ContractPageRespVO> result = getContractPageRespVOPageResult(contractDOPageResult);
        result.getList().forEach(resp -> {
            List<ContractArchivesDO> contractArchivesDO = contractArchivesMapper.selectList(ContractArchivesDO::getContractId, resp.getId());
            if (CollectionUtil.isNotEmpty(contractArchivesDO)) {
                resp.setArchivesId(contractArchivesDO.get(0).getId());
            }
        });
        return result;
    }

    /**
     * 上传合同 列表
     * 创建人 当前登录用户
     *
     * @param contractDocumentPageReqVO
     * @return
     */
    @Override
    public PageResult<ContractRespVO> getDocumentPage(ContractDocumentPageReqVO contractDocumentPageReqVO) {
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectDocumentPage(contractDocumentPageReqVO);
        PageResult<ContractRespVO> result = ContractConverter.INSTANCE.convertPage2(contractDOPageResult);
        //添加签署方信息
        for (ContractRespVO contractRespVO : result.getList()) {
            ArrayList<SignatoryRespVO> signatoryRespVOList = new ArrayList<>();
            //添加我方
            SignatoryRespVO signatoryRespVO = new SignatoryRespVO();
            //TODO 签署方信息  我方
            signatoryRespVOList.add(signatoryRespVO);
            //TODO 添加相对方

        }
        return result;
    }

    /**
     * 合同管理-合同签署
     * 待签署
     *
     * @param contractPageReqVO
     * @return
     */
    @DataPermission(enable = false)
    @Override
    public PageResult<ContractPageRespVO> getSignUnfinishedPage(ContractPageReqVO contractPageReqVO) {
        contractPageReqVO.setStatusList(CollectionUtil.newArrayList(ContractStatusEnums.TO_BE_SIGNED.getCode()));

        //工作流执行人操作状态查询
        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(WebFrameworkUtils.getLoginUserId(), new ArrayList<>(Arrays.asList(PROCESS_KEY_BOTH_OLD, PROCESS_KEY_TRIPARTITE_NOT, PROCESS_KEY_BOTH, PROCESS_KEY_TRIPARTITE, PROCESS_KEY_MANY)), SIGN);
        List<String> processInstanceIds = CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        if (CollectionUtil.isEmpty(processInstanceIds)) {
            return PageResult.empty(0L);
        }
        contractPageReqVO.setProcessInstanceIds(processInstanceIds);

        Map<String, ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoMap = CollectionUtils.convertMap(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        PageResult<ContractDO> contractDOPageResult = selectAffirmPage(contractPageReqVO, null);
        PageResult<ContractPageRespVO> result = getContractPageRespVOPageResult(contractDOPageResult);
        result.getList().forEach(resp -> {
            ContractProcessInstanceRelationInfoRespDTO relationInfoRespDTO = processInstanceRelationInfoMap.get(resp.getProcessInstanceId());
            if (ObjectUtils.isNotEmpty(relationInfoRespDTO)) {
                resp.setTaskId(relationInfoRespDTO.getTaskId());
                resp.setHandleResult(relationInfoRespDTO.getProcessResult());
            }
        });
        return result;
    }

    /**
     * 合同管理-合同签署
     * 签署完成
     *
     * @param contractPageReqVO
     * @return
     */
    @DataPermission(enable = false)
    @Override
    public PageResult<ContractPageRespVO> getSignFinishPage(ContractPageReqVO contractPageReqVO) {
        contractPageReqVO.setStatusList(CollectionUtil.newArrayList(ContractStatusEnums.SIGN_COMPLETED.getCode()));

        //工作流执行人操作状态查询
        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(WebFrameworkUtils.getLoginUserId(), new ArrayList<>(Arrays.asList(PROCESS_KEY_BOTH_OLD, PROCESS_KEY_TRIPARTITE_NOT, PROCESS_KEY_BOTH, PROCESS_KEY_TRIPARTITE, PROCESS_KEY_MANY)), SIGN);
        List<String> processInstanceIds = CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        if (CollectionUtil.isEmpty(processInstanceIds)) {
            return PageResult.empty(0L);
        }
        contractPageReqVO.setProcessInstanceIds(processInstanceIds);

        Map<String, ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoMap = CollectionUtils.convertMap(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        PageResult<ContractDO> contractDOPageResult = selectAffirmPage(contractPageReqVO, null);
        PageResult<ContractPageRespVO> result = getContractPageRespVOPageResult(contractDOPageResult);
        result.getList().forEach(resp -> {
            ContractProcessInstanceRelationInfoRespDTO relationInfoRespDTO = processInstanceRelationInfoMap.get(resp.getProcessInstanceId());
            if (ObjectUtils.isNotEmpty(relationInfoRespDTO)) {
                resp.setTaskId(relationInfoRespDTO.getTaskId());
                resp.setHandleResult(relationInfoRespDTO.getProcessResult());
            }
        });
        return result;
    }

    /**
     * 合同管理-合同签署
     * 逾期未签署
     *
     * @param contractPageReqVO
     * @return
     */
    @DataPermission(enable = false)
    @Override
    public PageResult<ContractPageRespVO> getSignOverduePage(ContractPageReqVO contractPageReqVO) {
        contractPageReqVO.setStatusList(CollectionUtil.newArrayList(ContractStatusEnums.SIGN_OVERDUE.getCode()));

        //工作流执行人操作状态查询
        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(WebFrameworkUtils.getLoginUserId(), new ArrayList<>(Arrays.asList(PROCESS_KEY_BOTH_OLD, PROCESS_KEY_TRIPARTITE_NOT, PROCESS_KEY_BOTH, PROCESS_KEY_TRIPARTITE, PROCESS_KEY_MANY)), SIGN);
        List<String> processInstanceIds = CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        if (CollectionUtil.isEmpty(processInstanceIds)) {
            return PageResult.empty(0L);
        }
        contractPageReqVO.setProcessInstanceIds(processInstanceIds);

        Map<String, ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoMap = CollectionUtils.convertMap(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        PageResult<ContractDO> contractDOPageResult = selectAffirmPage(contractPageReqVO, null);
        PageResult<ContractPageRespVO> result = getContractPageRespVOPageResult(contractDOPageResult);
        result.getList().forEach(resp -> {
            ContractProcessInstanceRelationInfoRespDTO relationInfoRespDTO = processInstanceRelationInfoMap.get(resp.getProcessInstanceId());
            if (ObjectUtils.isNotEmpty(relationInfoRespDTO)) {
                resp.setTaskId(relationInfoRespDTO.getTaskId());
                resp.setHandleResult(relationInfoRespDTO.getProcessResult());
            }
        });
        return result;
    }

    @Override
    public PageResult<ContractPageRespVO> getAuditingPage(ContractPageReqVO contractPageReqVO) {
        contractPageReqVO.setStatusList(CollectionUtil.newArrayList(ContractStatusEnums.CHECKING.getCode()));
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectSentPage(contractPageReqVO);
        PageResult<ContractPageRespVO> result = getContractPageRespVOPageResult(contractDOPageResult);
        result.getList().forEach(item -> item.setProcessInstanceId(null));
        enhancePage(result);
        result.getList().forEach(item -> {
            String statusName = getStatusName(item.getPlatform(), item.getStatus(), (Boolean) contractTypeService.isNeedSignet(new ContractTypeSignetReqVO().setId(item.getContractType())), item.getUpload(), item.getIsSign(), item.getIsFilings());
            if (StringUtils.isNotEmpty(statusName)) {
                item.setStatusName(statusName);
            }
        });

        return result;
    }

    @Override
    public PageResult<ContractPageRespVO> getConfirmedPage(ContractPageReqVO contractPageReqVO) {
        contractPageReqVO.setStatusList(
                CollectionUtil.newArrayList(
                        ContractStatusEnums.TO_BE_SIGNED.getCode(),
                        ContractStatusEnums.SIGN_REJECTED.getCode(),
                        ContractStatusEnums.TO_BE_CONFIRMED.getCode(),
                        ContractStatusEnums.CONTRACT_AUDITSTATUS_NOT_SIGNED.getCode(),
                        ContractStatusEnums.SIGN_COMPLETED.getCode(),
                        ContractStatusEnums.CONTRACT_AUDITSTATUS_SEAL_APPROVAL.getCode(),
                        ContractStatusEnums.PERFORMANCE_CLOSURE.getCode(),
                        ContractStatusEnums.PERFORMING.getCode(),
                        ContractStatusEnums.PERFORMANCE_COMPLETE.getCode(),
                        ContractStatusEnums.PERFORMANCE_RISK.getCode(),
                        ContractStatusEnums.PERFORMANCE_RISK_DISPUTE.getCode(),
                        ContractStatusEnums.PERFORMANCE_RISK_PAUSE.getCode(),
                        ContractStatusEnums.PERFORMANCE_RISK_EXTENSION.getCode(),
                        ContractStatusEnums.PERFORMANCE_RISK_OVERDUE.getCode()
                ));

        //工作流执行人操作状态查询
        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(WebFrameworkUtils.getLoginUserId(), new ArrayList<>(Arrays.asList(PROCESS_KEY_BOTH_OLD, PROCESS_KEY_TRIPARTITE_NOT, PROCESS_KEY_BOTH, PROCESS_KEY_TRIPARTITE, PROCESS_KEY_MANY)), SIGN);
        List<String> processInstanceIds = CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        List<ContractProcessInstanceRelationInfoRespDTO> allConfirmRelationProcessInstanceInfos = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(WebFrameworkUtils.getLoginUserId(), new ArrayList<>(Arrays.asList(PROCESS_KEY_BOTH_OLD, PROCESS_KEY_TRIPARTITE_NOT, PROCESS_KEY_BOTH, PROCESS_KEY_TRIPARTITE, PROCESS_KEY_MANY)), CONFIRM);
        List<String> confrimProcessInstanceIds = CollectionUtils.convertList(allConfirmRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        allRelationProcessInstanceInfos.addAll(allConfirmRelationProcessInstanceInfos);
        processInstanceIds.addAll(confrimProcessInstanceIds);
        contractPageReqVO.setProcessInstanceIds(processInstanceIds);

        Map<String, ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoMap = CollectionUtils.convertMap(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        PageResult<ContractDO> contractDOPageResult = selectAffirmPage(contractPageReqVO, null);
        PageResult<ContractPageRespVO> result = getContractPageRespVOPageResult(contractDOPageResult);
        result.getList().forEach(resp -> {
            ContractProcessInstanceRelationInfoRespDTO relationInfoRespDTO = processInstanceRelationInfoMap.get(resp.getProcessInstanceId());
            if (ObjectUtils.isNotEmpty(relationInfoRespDTO)) {
                resp.setTaskId(relationInfoRespDTO.getTaskId());
                resp.setHandleResult(relationInfoRespDTO.getProcessResult());
            }
        });
        return result;
    }

    /**
     * 合同创建基础版
     */
    @Override
    public String createContractBase(ContractCreateBaseReqVO contractCreateBaseReqVO) throws Exception {
        ContractDO contractDO = ContractConverter.INSTANCE.toEntity(contractCreateBaseReqVO);
        if (ObjectUtils.isNotEmpty(contractDO.getId())) {
            ContractDO contractUpdateDO = contractMapper.selectById(contractDO.getId());
            if (ObjectUtil.isNotEmpty(contractDO.getPdfFileId())) {
                if (ObjectUtil.isNotEmpty(contractUpdateDO.getPdfFileId())) {
                    fileApi.deleteFile(contractUpdateDO.getPdfFileId());
                }
                //富文本生成pdf，存查看的文件id地址
                String folderId = "TEMPLATE-" + IdUtil.fastSimpleUUID();
                String s = FileNameUtil.mainName(contractDO.getFileName());
                String localFolderPath = READY_TO_UPLOAD_PATH + "/" + folderId;
                FileUtil.mkdir(localFolderPath);
                wkHtmlToPdfManager.fileAuthHtmlToPdfFromstring(StringUtils.toEncodedString(contractDO.getContractContent(), StandardCharsets.UTF_8), localFolderPath + "/" + s + ".pdf");
                Path path = Paths.get(localFolderPath + "/" + s + ".pdf");
                Long pdfFileId = fileApi.uploadFile(s + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + s + ".pdf", Files.readAllBytes(path));
                FileUtil.del(localFolderPath);
                contractDO.setPdfFileId(pdfFileId);
            }

            if (CollectionUtil.isNotEmpty(contractCreateBaseReqVO.getSignatoryList())) {
                handlePart(contractDO, contractCreateBaseReqVO.getSignatoryList());
            }

            contractMapper.updateById(contractDO);


            //删除久附件信息、文件
            List<String> collect = attachmentRelMapper.selectList(AttachmentRelDO::getContractId, contractUpdateDO.getId()).stream().map(AttachmentRelDO::getId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(collect)) {
                attachmentRelMapper.deleteBatchIds(collect);
            }
            List<Long> addIdList = attachmentRelMapper.selectList(AttachmentRelDO::getContractId, contractUpdateDO.getId()).stream().map(AttachmentRelDO::getAttachmentAddId).collect(Collectors.toList());
            for (Long aLong : addIdList) {
                fileApi.deleteFile(aLong);
            }
            if (CollectionUtil.isNotEmpty(contractCreateBaseReqVO.getAttachmentList())) {
                //循环取出附件信息
                for (AttachmentRelCreateReqVO attachmentList : contractCreateBaseReqVO.getAttachmentList()) {
                    //保存附件信息
                    AttachmentRelDO ecmsAttachmentRelDO = AttachmentRelConverter.INSTANCE.convert(attachmentList);
                    //添加主合同绑定id
                    ecmsAttachmentRelDO.setContractId(contractDO.getId());
                    attachmentRelMapper.insert(ecmsAttachmentRelDO);
                }
            }

            //删除旧签署方关系
            signatoryRelMapper.deleteBatchIds(signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractDO.getId()).stream().map(SignatoryRelDO::getId).collect(Collectors.toList()));

            //添加签署方关系
            for (SignatoryRelReqVO signatoryRelReqVO : contractCreateBaseReqVO.getSignatoryList()) {
                SignatoryRelDO signatoryRelDO = SignatoryRelConverter.INSTANCE.convert(signatoryRelReqVO);
                if (ObjectUtil.isNotEmpty(signatoryRelReqVO.getSignatoryId())) {
                    signatoryRelDO.setContractId(contractDO.getId());
                    signatoryRelMapper.insert(signatoryRelDO);
                }
            }

            List<PaymentScheduleDO> oldPayList = paymentScheduleMapper.selectList(PaymentScheduleDO::getContractId, contractDO.getId());
            if (oldPayList.size() > 0) {
                //删除旧付款计划
                paymentScheduleMapper.deleteBatchIds(oldPayList.stream().map(PaymentScheduleDO::getId).collect(Collectors.toList()));

            }

            //保存付款计划信息
            if (CollectionUtil.isNotEmpty(contractCreateBaseReqVO.getPaymentScheduleVOList())) {
                List<PaymentScheduleDO> paymentScheduleDOList = new ArrayList<>();
                for (PaymentScheduleVO paymentScheduleVO : contractCreateBaseReqVO.getPaymentScheduleVOList()) {
                    PaymentScheduleDO paymentScheduleDO = PaymentScheduleConverter.INSTANCE.toEntity(paymentScheduleVO);
                    paymentScheduleDO.setContractId(contractDO.getId());
                    paymentScheduleDOList.add(paymentScheduleDO);
                }
                paymentScheduleMapper.insertBatch(paymentScheduleDOList);
            }

        } else {
            //校验编码是否重复
//            if (codeExist(contractDO.getId(), contractDO.getCode())) {
//                throw exception(ErrorCodeConstants.CODE_EXISTS);
//            }
            //新增
            contractDO.setStatus(ContractStatusEnums.TO_BE_CHECK.getCode());

            handlePart(contractDO, contractCreateBaseReqVO.getSignatoryList());

            contractMapper.insert(contractDO);

            //保存合同参数信息
            if (CollectionUtil.isNotEmpty(contractCreateBaseReqVO.getContractParameterVOList())) {
                List<ContractParameterDO> contractParameterDOList = new ArrayList<>();
                for (ContractParameterVO contractParameterVO : contractCreateBaseReqVO.getContractParameterVOList()) {
                    ContractParameterDO contractParameterDO = ContractParameterConverter.INSTANCE.toEntity(contractParameterVO);
                    contractParameterDO.setContractId(contractDO.getId());
                    contractParameterDOList.add(contractParameterDO);
                }
                contractParameterMapper.insertBatch(contractParameterDOList);
            }

            //保存付款计划信息
            if (CollectionUtil.isNotEmpty(contractCreateBaseReqVO.getPaymentScheduleVOList())) {
                List<PaymentScheduleDO> paymentScheduleDOList = new ArrayList<>();
                for (PaymentScheduleVO paymentScheduleVO : contractCreateBaseReqVO.getPaymentScheduleVOList()) {
                    PaymentScheduleDO paymentScheduleDO = PaymentScheduleConverter.INSTANCE.toEntity(paymentScheduleVO);
                    paymentScheduleDO.setContractId(contractDO.getId());
                    paymentScheduleDOList.add(paymentScheduleDO);
                }
                paymentScheduleMapper.insertBatch(paymentScheduleDOList);
            }

            if (CollectionUtil.isNotEmpty(contractCreateBaseReqVO.getAttachmentList())) {
                //循环取出附件信息
                for (AttachmentRelCreateReqVO attachmentList : contractCreateBaseReqVO.getAttachmentList()) {
                    //保存附件信息
                    AttachmentRelDO ecmsAttachmentRelDO = AttachmentRelConverter.INSTANCE.convert(attachmentList);
                    //添加主合同绑定id
                    ecmsAttachmentRelDO.setContractId(contractDO.getId());
                    attachmentRelMapper.insert(ecmsAttachmentRelDO);
                }
            }
            //添加签署方关系
            for (SignatoryRelReqVO signatoryRelReqVO : contractCreateBaseReqVO.getSignatoryList()) {
                SignatoryRelDO signatoryRelDO = SignatoryRelConverter.INSTANCE.convert(signatoryRelReqVO);
                if (ObjectUtil.isNotEmpty(signatoryRelReqVO.getSignatoryId())) {
                    signatoryRelDO.setContractId(contractDO.getId());
                    signatoryRelMapper.insert(signatoryRelDO);
                }
            }
        }
        return contractDO.getId();
    }

    /**
     * 添加合同（模板生成合同专用）
     *
     * @param contractCreateReqVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createContract(ContractCreateReqVO contractCreateReqVO) throws Exception {

        // 保存合同起码存在合同名称，添加判断防止注解校验失效
        if (ObjectUtil.isEmpty(contractCreateReqVO.getName())) {
            throw exception(DIY_ERROR, "合同名称不能为空");
        }
        // 当提交时做字段必填校验
        if (IfNumEnums.YES.getCode().equals(contractCreateReqVO.getIsSubmit())) {
            checkMustInput(contractCreateReqVO);
        }
        ContractDO contractDO = ContractConverter.INSTANCE.toEntity(contractCreateReqVO);
        if (ObjectUtils.isNotEmpty(contractDO.getId())) {
            ContractDO contractUpdateDO = contractMapper.selectById(contractDO.getId());
            // 当合同不是登记类型时，进行如下文件处理逻辑
            if (!ContractUploadTypeEnums.REGISTER.getCode().equals(contractDO.getUpload())) {
                if (ObjectUtil.isNotEmpty(contractDO.getPdfFileId())) {
                    if (ObjectUtil.isNotEmpty(contractUpdateDO.getPdfFileId())) {
                        try {
                            fileApi.deleteFile(contractUpdateDO.getPdfFileId());
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }
                    if (ObjectUtil.isNotEmpty(contractDO.getContractContent())) {
                        //富文本生成pdf，存查看的文件id地址
                        String folderId = "TEMPLATE-" + IdUtil.fastSimpleUUID();
                        String s = FileNameUtil.mainName(contractDO.getFileName());
                        String localFolderPath = READY_TO_UPLOAD_PATH + "/" + folderId;
                        FileUtil.mkdir(localFolderPath);
                        wkHtmlToPdfManager.fileAuthHtmlToPdfFromstring(StringUtils.toEncodedString(contractDO.getContractContent(), StandardCharsets.UTF_8), localFolderPath + "/" + s + ".pdf");
                        Path path = Paths.get(localFolderPath + "/" + s + ".pdf");
                        Long pdfFileId = fileApi.uploadFile(s + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + s + ".pdf", Files.readAllBytes(path));
                        FileUtil.del(localFolderPath);
                        contractDO.setPdfFileId(pdfFileId);
                    }
                }
            }

            if (CollectionUtil.isNotEmpty(contractCreateReqVO.getSignatoryList())) {
                handlePart(contractDO, contractCreateReqVO.getSignatoryList());
            }

            contractMapper.updateById(contractDO);


            //删除久附件信息、文件
            List<String> collect = attachmentRelMapper.selectList(AttachmentRelDO::getContractId, contractUpdateDO.getId()).stream().map(AttachmentRelDO::getId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(collect)) {
                attachmentRelMapper.deleteBatchIds(collect);
            }
            List<Long> addIdList = attachmentRelMapper.selectList(AttachmentRelDO::getContractId, contractUpdateDO.getId()).stream().map(AttachmentRelDO::getAttachmentAddId).collect(Collectors.toList());
            for (Long aLong : addIdList) {
                fileApi.deleteFile(aLong);
            }
            if (CollectionUtil.isNotEmpty(contractCreateReqVO.getAttachmentList())) {
                //循环取出附件信息
                for (AttachmentRelCreateReqVO attachmentList : contractCreateReqVO.getAttachmentList()) {
                    //保存附件信息
                    AttachmentRelDO ecmsAttachmentRelDO = AttachmentRelConverter.INSTANCE.convert(attachmentList);
                    //添加主合同绑定id
                    ecmsAttachmentRelDO.setContractId(contractDO.getId());
                    attachmentRelMapper.insert(ecmsAttachmentRelDO);
                }
            }

            //删除旧签署方关系
            List<String> signatoryRelIdList = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractDO.getId()).stream().map(SignatoryRelDO::getId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(signatoryRelIdList)) {
                signatoryRelMapper.deleteBatchIds(signatoryRelIdList);
            }

            //添加签署方关系
            for (SignatoryRelReqVO signatoryRelReqVO : contractCreateReqVO.getSignatoryList()) {
                SignatoryRelDO signatoryRelDO = SignatoryRelConverter.INSTANCE.convert(signatoryRelReqVO);
                if (ObjectUtil.isNotEmpty(signatoryRelReqVO.getSignatoryId())) {
                    signatoryRelDO.setContractId(contractDO.getId());
                    signatoryRelMapper.insert(signatoryRelDO);
                }
            }
            List<PaymentScheduleDO> oldPayList = paymentScheduleMapper.selectList(PaymentScheduleDO::getContractId, contractDO.getId());
            if (oldPayList.size() > 0) {
                //删除旧付款计划
                paymentScheduleMapper.deleteBatchIds(oldPayList.stream().map(PaymentScheduleDO::getId).collect(Collectors.toList()));

            }

            //保存付款计划信息
            if (CollectionUtil.isNotEmpty(contractCreateReqVO.getPaymentScheduleVOList())) {
                List<PaymentScheduleDO> paymentScheduleDOList = new ArrayList<>();
                for (PaymentScheduleVO paymentScheduleVO : contractCreateReqVO.getPaymentScheduleVOList()) {
                    PaymentScheduleDO paymentScheduleDO = PaymentScheduleConverter.INSTANCE.toEntity(paymentScheduleVO);
                    paymentScheduleDO.setContractId(contractDO.getId());
                    if (ObjectUtil.isEmpty(paymentScheduleVO.getAmountType())) {
                        paymentScheduleDO.setAmountType(contractCreateReqVO.getAmountType());
                    }
                    paymentScheduleDOList.add(paymentScheduleDO);
                }
                paymentScheduleMapper.insertBatch(paymentScheduleDOList);
            }
            //保存合同条款信息
            if (CollectionUtil.isNotEmpty(contractCreateReqVO.getTerms())) {
                //添加条款信息
                TenantUtils.executeIgnore(() -> {
                    //删除旧合同条款
                    contractTermMapper.delete(new LambdaQueryWrapperX<ContractTermDO>().eq(ContractTermDO::getContractId, contractCreateReqVO.getId()));
                    //保存合同条款信息
                    List<ContractTermDO> contractTermDOList = ContractTermConverter.INSTANCE.convertList(contractCreateReqVO.getTerms());
                    for (ContractTermDO contractTermDO : contractTermDOList) {
                        contractTermDO.setContractId(contractCreateReqVO.getId());
                    }
                    contractTermMapper.insertBatch(contractTermDOList);
                });
            }

        } else {
            //校验编码是否重复
//            if (codeExist(contractDO.getId(), contractDO.getCode())) {
//                throw exception(ErrorCodeConstants.CODE_EXISTS);
//            }
            //新增
            contractDO.setStatus(ContractStatusEnums.TO_BE_CHECK.getCode());

            handlePart(contractDO, contractCreateReqVO.getSignatoryList());

            if (contractCreateReqVO.getUpload() != null && ContractUploadTypeEnums.UPLOAD_FILE.getCode().equals(contractCreateReqVO.getUpload())) {
                if (contractCreateReqVO.getFileName() != null) {
                    contractDO.setFileName(contractCreateReqVO.getFileName());
                }
            }

            contractMapper.insert(contractDO);

            //保存合同参数信息
            if (CollectionUtil.isNotEmpty(contractCreateReqVO.getContractParameterVOList())) {
                List<ContractParameterDO> contractParameterDOList = new ArrayList<>();
                for (ContractParameterVO contractParameterVO : contractCreateReqVO.getContractParameterVOList()) {
                    ContractParameterDO contractParameterDO = ContractParameterConverter.INSTANCE.toEntity(contractParameterVO);
                    contractParameterDO.setContractId(contractDO.getId());
                    contractParameterDOList.add(contractParameterDO);
                }
                contractParameterMapper.insertBatch(contractParameterDOList);
            }

            //保存合同章信息
            if (CollectionUtil.isNotEmpty(contractCreateReqVO.getContractSealVOList())) {
                List<ContractSealDO> contractSealDOList = new ArrayList<>();
                for (ContractSealVO contractSealVO : contractCreateReqVO.getContractSealVOList()) {
                    ContractSealDO contractSealDO = ContractSealConverter.INSTANCE.toEntity(contractSealVO);
                    contractSealDO.setContractId(contractDO.getId());
                    contractSealDOList.add(contractSealDO);
                }
                contractSealMapper.insertBatch(contractSealDOList);
            }

            //保存付款计划信息
            if (CollectionUtil.isNotEmpty(contractCreateReqVO.getPaymentScheduleVOList())) {
                List<PaymentScheduleDO> paymentScheduleDOList = new ArrayList<>();
                for (PaymentScheduleVO paymentScheduleVO : contractCreateReqVO.getPaymentScheduleVOList()) {
                    PaymentScheduleDO paymentScheduleDO = PaymentScheduleConverter.INSTANCE.toEntity(paymentScheduleVO);
                    paymentScheduleDO.setContractId(contractDO.getId());
                    if (ObjectUtil.isEmpty(paymentScheduleVO.getAmountType())) {
                        paymentScheduleDO.setAmountType(contractCreateReqVO.getAmountType());
                    }
                    paymentScheduleDOList.add(paymentScheduleDO);
                }
                paymentScheduleMapper.insertBatch(paymentScheduleDOList);
            }

            //保存合同采购内容信息
            if (CollectionUtil.isNotEmpty(contractCreateReqVO.getContractPurchaseReqVOList())) {
                List<ContractPurchaseDO> contractPurchaseDOList = new ArrayList<>();
                for (ContractPurchaseReqVO contractPurchaseReqVO : contractCreateReqVO.getContractPurchaseReqVOList()) {
                    ContractPurchaseDO contractPurchaseDO = ContractPurchaseConverter.INSTANCE.convert(contractPurchaseReqVO);
                    contractPurchaseDO.setContractId(contractDO.getId());
                    contractPurchaseDOList.add(contractPurchaseDO);
                }
                contractPurchaseMapper.insertBatch(contractPurchaseDOList);
            }

            //保存合同签定方信息
            if (CollectionUtil.isNotEmpty(contractCreateReqVO.getContractSignatoryReqVOList())) {
                List<ContractSignatoryDO> contractSignatoryDOList = new ArrayList<>();
                for (ContractSignatoryReqVO contractSignatoryReqVO : contractCreateReqVO.getContractSignatoryReqVOList()) {
                    ContractSignatoryDO contractSignatoryDO = ContractSignatoryConverter.INSTANCE.convert(contractSignatoryReqVO);
                    contractSignatoryDO.setContractId(contractDO.getId());
                }
                contractSignatoryMapper.insertBatch(contractSignatoryDOList);
            }

            if (CollectionUtil.isNotEmpty(contractCreateReqVO.getAttachmentList())) {
                //循环取出附件信息
                for (AttachmentRelCreateReqVO attachmentList : contractCreateReqVO.getAttachmentList()) {
                    //保存附件信息
                    AttachmentRelDO ecmsAttachmentRelDO = AttachmentRelConverter.INSTANCE.convert(attachmentList);
                    //添加主合同绑定id
                    ecmsAttachmentRelDO.setContractId(contractDO.getId());
                    attachmentRelMapper.insert(ecmsAttachmentRelDO);
                }
            }
            //保存合同条款信息
            if (CollectionUtil.isNotEmpty(contractCreateReqVO.getTerms())) {
                TenantUtils.executeIgnore(() -> {
                    //保存合同条款信息
                    List<ContractTermDO> contractTermDOList = ContractTermConverter.INSTANCE.convertList(contractCreateReqVO.getTerms());
                    for (ContractTermDO contractTermDO : contractTermDOList) {
                        contractTermDO.setContractId(contractCreateReqVO.getId());
                    }
                    contractTermMapper.insertBatch(contractTermDOList);
                });
            }
            //添加签署方关系
            for (SignatoryRelReqVO signatoryRelReqVO : contractCreateReqVO.getSignatoryList()) {
                SignatoryRelDO signatoryRelDO = SignatoryRelConverter.INSTANCE.convert(signatoryRelReqVO);
                if (ObjectUtil.isNotEmpty(signatoryRelReqVO.getSignatoryId())) {
                    signatoryRelDO.setContractId(contractDO.getId());
                    signatoryRelMapper.insert(signatoryRelDO);
                }
            }
            //合同补录方式
            if (REGISTER.getCode().equals(contractDO.getUpload())) {
                //创建履约
                createPerf(contractDO);
            }
            //是否发起审批
            if (ObjectUtil.isNotEmpty(contractCreateReqVO.getInitiateApproval()) && IfNumEnums.YES.getCode().equals(contractCreateReqVO.getInitiateApproval())) {
                bpmContractService.createProcess(getLoginUserId(), new BpmContractCreateReqVO().setId(contractDO.getId()));
            }
        }
//        String folderId = "TEMPLATE-" + IdUtil.fastSimpleUUID();
//        String s = contractDO.getName();
//        String localFolderPath = READY_TO_UPLOAD_PATH + "/" + folderId;
//        FileUtil.mkdir(localFolderPath);
//        Long pdfFileId = new Long(0);
//        if(contractDO.getFileAddId() != null && contractDO.getFileAddId() != 0){
//            ByteArrayInputStream byteArrayInputStream = IoUtil.toStream(fileApi.getFileContentById(contractDO.getFileAddId()));
//            FileUtil.writeFromStream(byteArrayInputStream, localFolderPath + "/" + "TEMPLATE" + "/" + fileApi.getName(contractDO.getFileAddId()));
//            Path path = Paths.get(localFolderPath + "/" + FileNameUtil.mainName(fileApi.getName(contractDO.getFileAddId())) + ".pdf");
//
//            AsposeUtil.docx2Pdf(localFolderPath + "/" + "TEMPLATE" + "/" + fileApi.getName(contractDO.getFileAddId()),
//                    localFolderPath + "/" + FileNameUtil.mainName(fileApi.getName(contractDO.getFileAddId())) + ".pdf"); //红色水印Evaluation Only.
//            //  Path path = Paths.get(localFolderPath + "/" + FileNameUtil.mainName(fileApi.getName(contractToPdfVO.getFileAddId())) + ".pdf");
//            if (StringUtils.isNotBlank(contractDO.getName())) {
//                pdfFileId = fileApi.uploadFile(contractDO.getName() + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));
//
//            } else {
//                pdfFileId = fileApi.uploadFile(FileNameUtil.mainName(fileApi.getName(contractDO.getFileAddId())) + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));
//            }
//            contractDO.setPdfFileId(pdfFileId);
//            contractMapper.updateById(contractDO);
//        }
//
        // 文件留痕，起草的时候，只备份模板
        String remark = "";
        if (StringUtils.isNotBlank(contractCreateReqVO.getId())) {
            remark = "编辑合同";
        } else {
            remark = "起草合同";
        }
        fileVersionEventPublisher.sendEvent(new FileVersionEvent(this).setBusinessId(contractDO.getId()).setBusinessType(FileVersionEnums.CONTRACT.getCode()).setRemark(remark));


        return contractDO.getId();
    }

    @Override
    public String checkContractOW(ContractDraft contractDraft) throws UnsupportedEncodingException {
        String data64 = contractDraft.getData();
        if (StringUtils.isEmpty(data64)) {
            throw exception(ErrorCodeConstants.CODE_EXISTS);
        }

        //Base64解码
        byte[] dataByte = Base64.getDecoder().decode(data64);
        String s = new String(dataByte, StandardCharsets.UTF_8);
        String decodedData = URLDecoder.decode(s, String.valueOf(StandardCharsets.UTF_8));

        JSONObject jsonObject64 = JSON.parseObject(decodedData);
        String sign0 = jsonObject64.getString("sign");

        if (StringUtils.isEmpty(sign0)) {
            throw exception(ErrorCodeConstants.NO_ENCRYPT_INFO);
        }

        String sign1 = JsonSorter.encryptData(decodedData);

//        if (!sign0.equals(sign1)) {
//            throw exception(ErrorCodeConstants.SIGN_VERIFY_ERROR);
//        }

        String templateId = jsonObject64.getString("templateId");
        if (ObjectUtil.isNotEmpty(templateId)) {
            Model model = modelMapper.selectById(templateId);
            if (ObjectUtil.isNotEmpty(model)) {
                String encodedString = StringUtils.toEncodedString(model.getModelContent(), StandardCharsets.UTF_8);
                String s1 = this.urlEncodeWithCustomSpaceEncoding(encodedString);
                byte[] encode = Base64.getEncoder().encode(s1.getBytes());
                return StringUtils.toEncodedString(encode, StandardCharsets.UTF_8);
            } else {
                throw exception(ErrorCodeConstants.EMPTY_DATA_ERROR);
            }
        }
        return "验签成功";
    }

    @Override
    public String createContractOW(ApiCreateReqVO apiCreateReqVO) {
        ContractDO contractDO = ContractConverter.INSTANCE.toEntity(apiCreateReqVO);
        if (ObjectUtils.isNotEmpty(contractDO.getId())) {
            ContractDO contractUpdateDO = contractMapper.selectById(contractDO.getId());
            contractMapper.updateById(contractDO);

            Object contractData = apiCreateReqVO.getContractData();
            ContractDataDO contractDataDO = contractDataMapper.selectOne(ContractDataDO::getContractId, contractDO.getId());
            if (ObjectUtils.isNotEmpty(contractDataDO)) {
                contractDataMapper.updateById(contractDataDO.setData(contractData.toString()));
            } else {
                //保存合同数据信息
                if (StringUtils.isNotEmpty(contractData.toString())) {
                    contractDataMapper.insert(new ContractDataDO().setContractId(contractDO.getId()).setData(contractData.toString()));
                }
            }

            //删除久附件信息、文件
            List<String> collect = attachmentRelMapper.selectList(AttachmentRelDO::getContractId, contractUpdateDO.getId()).stream().map(AttachmentRelDO::getId).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(collect)) {
                attachmentRelMapper.deleteBatchIds(collect);
            }

            if (CollectionUtil.isNotEmpty(apiCreateReqVO.getAttachmentList())) {
                //循环取出附件信息
                for (AttachmentRelCreateReqVO attachmentList : apiCreateReqVO.getAttachmentList()) {
                    //保存附件信息
                    AttachmentRelDO ecmsAttachmentRelDO = AttachmentRelConverter.INSTANCE.convert(attachmentList);
                    //添加主合同绑定id
                    ecmsAttachmentRelDO.setContractId(contractDO.getId());
                    ecmsAttachmentRelDO.setAttachmentType("第三方系统上传附件-合同名称 ：" + contractDO.getName());
                    attachmentRelMapper.insert(ecmsAttachmentRelDO);
                }
            }
            //删除旧参数信息，保存新参数
            QueryWrapper<ContractParameterDO> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("contract_id", contractDO.getId());
            contractParameterMapper.delete(queryWrapper);
            //保存合同参数
            if (CollectionUtil.isNotEmpty(apiCreateReqVO.getContractParameters())) {
                apiCreateReqVO.getContractParameters().forEach(item -> {
                    item.setContractId(contractDO.getId());
                    contractParameterMapper.insert(item);
                });
            }

        } else {
            //新增
            contractDO.setStatus(ContractStatusEnums.TO_BE_CHECK.getCode());
            contractDO.setUpload(THIRD_PARTY.getCode());

            contractMapper.insert(contractDO);

            //保存合同数据信息
            Object contractData = apiCreateReqVO.getContractData();
            if (ObjectUtil.isNotEmpty(contractData)) {
                contractDataMapper.insert(new ContractDataDO().setContractId(contractDO.getId()).setData(contractData.toString()));
            }

            if (CollectionUtil.isNotEmpty(apiCreateReqVO.getAttachmentList())) {
                //循环取出附件信息
                for (AttachmentRelCreateReqVO attachmentList : apiCreateReqVO.getAttachmentList()) {
                    //保存附件信息
                    AttachmentRelDO ecmsAttachmentRelDO = AttachmentRelConverter.INSTANCE.convert(attachmentList);
                    //添加主合同绑定id
                    ecmsAttachmentRelDO.setContractId(contractDO.getId());
                    ecmsAttachmentRelDO.setAttachmentType("第三方系统上传附件-合同名称 ：" + contractDO.getName());
                    attachmentRelMapper.insert(ecmsAttachmentRelDO);
                }
            }
            //保存合同参数
            if (CollectionUtil.isNotEmpty(apiCreateReqVO.getContractParameters())) {
                apiCreateReqVO.getContractParameters().forEach(item -> {
                    item.setContractId(contractDO.getId());
                    contractParameterMapper.insert(item);
                });
            }
        }
        return contractDO.getId();
    }

    private void handlePart(ContractDO contractDO, List<SignatoryRelReqVO> signatoryList) {
        //如果起草方入参为空，则在此托底，避免发送时报错
        enhanceDrafter(signatoryList);
        if (CollectionUtil.isNotEmpty(signatoryList)) {
            // 保存签署顺序
            List<Long> userIdList = new ArrayList<>();
            for (SignatoryRelReqVO signatoryRelReqVO : signatoryList) {
                if (signatoryRelReqVO.getSignatoryId() != null) {
                    userIdList.add(relativeService.getOneDefaultContactId(signatoryRelReqVO.getSignatoryId()));
                } else {
                    userIdList.add(signatoryRelReqVO.getUserId());
                }
            }
            if (CollectionUtil.isNotEmpty(userIdList)) {
                // 将 userIdList 拼接成字符串用 "_" 隔开，插入库
                String signOrder = userIdList.stream().map(String::valueOf).collect(Collectors.joining("_"));
                // 将拼接好的字符串插入到 contractDO 对象中
                contractDO.setSignOrder(signOrder);

                // 甲方
                SignatoryRelReqVO partAReqVO = signatoryList.stream().filter(item -> Integer.valueOf(1).equals(item.getType())).findFirst().orElse(null);
//                // 乙方
//                SignatoryRelReqVO partBReqVO = signatoryList.stream().filter(item -> item.getType() == 2).findFirst().orElse(null);
//                // 丙方
//                SignatoryRelReqVO partCReqVO = signatoryList.stream().filter(item -> item.getType() == 3).findFirst().orElse(null);
//                // 丁方
//                SignatoryRelReqVO partDReqVO = signatoryList.stream().filter(item -> item.getType() == 4).findFirst().orElse(null);
                List<SignatoryRelReqVO> voList = signatoryList.stream().filter(item -> ObjectUtil.isNotEmpty(item.getSignatoryId())).collect(Collectors.toList());

                if (partAReqVO != null) {
                    contractDO.setPartAName(partAReqVO.getSignatoryName());
                }

                if (voList.size() > 1) {
                    StringBuilder partBName = new StringBuilder(voList.get(0).getSignatoryName());
                    for (int i = 1; i < voList.size(); i++) {
                        partBName.append(",").append(voList.get(i).getSignatoryName());
                    }
                    contractDO.setPartBName(partBName.toString());
                } else {
                    contractDO.setPartBName(CollectionUtil.isEmpty(voList) ? "" : voList.get(0).getSignatoryName());
                }
                // 更新传入的 signatoryList
                signatoryList.clear();
                signatoryList.addAll(voList);
            }
        }
    }

    private void enhanceDrafter(List<SignatoryRelReqVO> signatoryList) {
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        for (SignatoryRelReqVO relReqVO : signatoryList) {
            if (Integer.valueOf(1).equals(relReqVO.getType()) && ObjectUtil.isNull(relReqVO.getUserId())) {
                relReqVO.setUserId(loginUserId);
            }
        }
    }

    @Resource
    private ChangXieService changXieService;

    @Override
    public Long toPdf(ContractToPdfVO contractToPdfVO) throws Exception {
        //富文本生成pdf，存查看的文件id地址
        String folderId = "TEMPLATE-" + IdUtil.fastSimpleUUID();
        String s = contractToPdfVO.getName();
        String localFolderPath = READY_TO_UPLOAD_PATH + "/" + folderId;
        FileUtil.mkdir(localFolderPath);
        Long pdfFileId = new Long(0);
        //富文本生成pdf
        if (ObjectUtil.isNotEmpty(contractToPdfVO.getContent())) {
            try {
                wkHtmlToPdfManager.fileAuthHtmlToPdfFromstring(contractToPdfVO.getContent(), localFolderPath + "/" + s + ".pdf");
            } catch (Exception e) {
                System.out.println("【WK html转换pdf】处理异常！" + e.getMessage());
                e.printStackTrace();
                throw e;
            }

            Path path = Paths.get(localFolderPath + "/" + s + ".pdf");
            pdfFileId = fileApi.uploadFile(s + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));

//            ByteArrayInputStream byteArrayInputStream = IoUtil.toStream(contractToPdfVO.getContent().getBytes());
//            FileUtil.writeFromStream(byteArrayInputStream, localFolderPath + "/" + "TEMPLATE" + "/" + "temp.rtf");
//            YhAgentUtil.officeToPDF(localFolderPath + "/" + "TEMPLATE" + "/" + "temp.rtf", localFolderPath + "/" + s + ".pdf");
//            Path path = Paths.get(localFolderPath + "/" + s + ".pdf");
//            pdfFileId = fileApi.uploadFile(s + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));
        } else if (ObjectUtil.isNotEmpty(contractToPdfVO.getFileAddId()) && contractToPdfVO.getFileAddId() != 0) {
//            ByteArrayInputStream byteArrayInputStream = IoUtil.toStream(fileApi.getFileContentById(contractToPdfVO.getFileAddId()));
//            FileUtil.writeFromStream(byteArrayInputStream, localFolderPath + "/" + "TEMPLATE" + "/" + fileApi.getName(contractToPdfVO.getFileAddId()));
//            YhAgentUtil.officeToPDF(localFolderPath + "/" + "TEMPLATE" + "/" + fileApi.getName(contractToPdfVO.getFileAddId()), localFolderPath + "/" + FileNameUtil.mainName(fileApi.getName(contractToPdfVO.getFileAddId())) + ".pdf");
//            Path path = Paths.get(localFolderPath + "/" + FileNameUtil.mainName(fileApi.getName(contractToPdfVO.getFileAddId())) + ".pdf");
//            if (StringUtils.isNotBlank(contractToPdfVO.getName())) {
//                pdfFileId = fileApi.uploadFile(contractToPdfVO.getName() + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));
//
//            } else {
//                pdfFileId = fileApi.uploadFile(FileNameUtil.mainName(fileApi.getName(contractToPdfVO.getFileAddId())) + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));
//
//            }
            //将word清稿,然后转pdf
            Long cleanDraftFileId = changXieService.cleandraftV2(contractToPdfVO.getFileAddId());
            //畅写转pdf
            pdfFileId = changXieService.converterDocx2PdfV2(cleanDraftFileId, FileUploadPathEnum.CONTRACT_DRAFT);
            return pdfFileId;
        }
        FileUtil.del(localFolderPath);
        return pdfFileId;
    }

    @Override
    public Long toPdf(ContractToPdfVO contractToPdfVO, FileUploadPathEnum fileUploadPathEnum) throws Exception {
        //富文本生成pdf，存查看的文件id地址
        String folderId = "TEMPLATE-" + IdUtil.fastSimpleUUID();
        String s = contractToPdfVO.getName();
        String localFolderPath = READY_TO_UPLOAD_PATH + "/" + folderId;
        FileUtil.mkdir(localFolderPath);
        Long pdfFileId = new Long(0);
        //富文本生成pdf
        if (ObjectUtil.isNotEmpty(contractToPdfVO.getContent())) {
            try {
                wkHtmlToPdfManager.fileAuthHtmlToPdfFromstring(contractToPdfVO.getContent(), localFolderPath + "/" + s + ".pdf");
            } catch (Exception e) {
                System.out.println("【WK html转换pdf】处理异常！" + e.getMessage());
                e.printStackTrace();
                throw e;
            }

            Path path = Paths.get(localFolderPath + "/" + s + ".pdf");
            pdfFileId = fileApi.uploadFile(s + ".pdf", fileUploadPathEnum.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));

//            ByteArrayInputStream byteArrayInputStream = IoUtil.toStream(contractToPdfVO.getContent().getBytes());
//            FileUtil.writeFromStream(byteArrayInputStream, localFolderPath + "/" + "TEMPLATE" + "/" + "temp.rtf");
//            YhAgentUtil.officeToPDF(localFolderPath + "/" + "TEMPLATE" + "/" + "temp.rtf", localFolderPath + "/" + s + ".pdf");
//            Path path = Paths.get(localFolderPath + "/" + s + ".pdf");
//            pdfFileId = fileApi.uploadFile(s + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));
        } else if (ObjectUtil.isNotEmpty(contractToPdfVO.getFileAddId()) && contractToPdfVO.getFileAddId() != 0) {
//            ByteArrayInputStream byteArrayInputStream = IoUtil.toStream(fileApi.getFileContentById(contractToPdfVO.getFileAddId()));
//            FileUtil.writeFromStream(byteArrayInputStream, localFolderPath + "/" + "TEMPLATE" + "/" + fileApi.getName(contractToPdfVO.getFileAddId()));
//            YhAgentUtil.officeToPDF(localFolderPath + "/" + "TEMPLATE" + "/" + fileApi.getName(contractToPdfVO.getFileAddId()), localFolderPath + "/" + FileNameUtil.mainName(fileApi.getName(contractToPdfVO.getFileAddId())) + ".pdf");
//            Path path = Paths.get(localFolderPath + "/" + FileNameUtil.mainName(fileApi.getName(contractToPdfVO.getFileAddId())) + ".pdf");
//            if (StringUtils.isNotBlank(contractToPdfVO.getName())) {
//                pdfFileId = fileApi.uploadFile(contractToPdfVO.getName() + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));
//
//            } else {
//                pdfFileId = fileApi.uploadFile(FileNameUtil.mainName(fileApi.getName(contractToPdfVO.getFileAddId())) + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));
//
//            }
            //将word清稿,然后转pdf
            Long cleanDraftFileId = changXieService.cleandraftV2(contractToPdfVO.getFileAddId());
            //畅写转pdf
            pdfFileId = changXieService.converterDocx2PdfV2(cleanDraftFileId, FileUploadPathEnum.CONTRACT_DRAFT);
            return pdfFileId;
        }
        FileUtil.del(localFolderPath);
        return pdfFileId;
    }

    @Override
    public HashMap<String, Long> toPdfById(String id) throws Exception {
        ContractDO contractDO = contractMapper.selectById(id);
        if (ObjectUtil.isEmpty(contractDO)) {
            throw exception(ErrorCodeConstants.CONTRACT_NAME_NOT_EXISTS);
        }
        ContractToPdfVO contractToPdfVO = new ContractToPdfVO();
        contractToPdfVO.setContent(StringUtils.toEncodedString(contractDO.getContractContent(), StandardCharsets.UTF_8));
        Long pdfid = this.toPdf(contractToPdfVO);
        HashMap<String, Long> result = new HashMap<>();
        result.put(id, pdfid);
        return result;
    }

    @Override
    public Boolean exist(String name, String code) {
        boolean b = !contractMapper.exist(name, code);
        return b;
    }

    /**
     * 上传合同后，判断是否创建履约
     *
     * @param contractDO
     */
    private void createPerf(ContractDO contractDO) {
        //调用创建履约接口
        if (ObjectUtil.isNotEmpty(contractDO.getValidity1()) && new Date().before(contractDO.getValidity1())) {
            ContractPerfReqVO contractPerfReqVO = new ContractPerfReqVO();
            contractPerfReqVO.setContractId(contractDO.getId());
            contractPerfReqVO.setContractCode(contractDO.getCode());
            contractPerfReqVO.setContractName(contractDO.getFileName());
            contractPerfReqVO.setSignFinishTime(contractDO.getSignDate());
            contractPerfReqVO.setContractTypeId(contractDO.getContractType());
            contractPerfReqVO.setContractStatus(contractDO.getStatus());
            contractPerfReqVO.setValidity0(contractDO.getValidity0());
            contractPerfReqVO.setValidity1(contractDO.getValidity1());
            contractPerfService.createContractPerf(contractPerfReqVO);
        }
    }

    /**
     * 发起工作流
     * 创建合同确认流程
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateToSend(String id) {
        ContractDO contractDO = contractMapper.selectById(id);
        Date now = new Date();
        if (contractDO.getExpirationDate() != null) {
            Date expirationDate = contractDO.getExpirationDate();
            expirationDate.setHours(23);
            expirationDate.setMinutes(59);
            expirationDate.setSeconds(59);
            int comparisonResult = now.compareTo(expirationDate);
            if (comparisonResult > 0) {
                throw exception(ErrorCodeConstants.CONTRACT_EXPIRE);
            }
        }
        // 修改合同状态为已发送
        contractDO.setStatus(ContractStatusEnums.SENT.getCode());
        contractDO.setIsSign(IfNumEnums.NO.getCode());
        contractDO.setSendTime(LocalDateTime.now());
        String contractProcessInstance = flowService.createContractProcessInstance(id);
        contractDO.setProcessInstanceId(contractProcessInstance);
        contractMapper.updateById(contractDO);

        // 修改相对方合同关联表，每个相对方的签署状态为未签署，确认状态为未确认
        List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, id);
        signatoryRelDOS.forEach(item -> {
            item.setIsSign(IfNumEnums.NO.getCode());
            item.setIsConfirm(IfNumEnums.NO.getCode());
        });
        signatoryRelMapper.updateBatch(signatoryRelDOS);
        // 文件留痕
        fileVersionEventPublisher.sendEvent(new FileVersionEvent(this).setBusinessId(id).setBusinessType(FileVersionEnums.CONTRACT.getCode()).setRemark("创建合同确认流程"));
    }

    /**
     * 修改签署状态为-待签署4
     *
     * @param contractBaseVO
     */
    @Override
    public void updateStatus4(ContractBaseVO contractBaseVO) {
        ContractDO contractDO = ContractConverter.INSTANCE.convert(contractBaseVO);
        contractDO.setStatus(ContractStatusEnums.TO_BE_SIGNED.getCode());
        contractMapper.updateById(contractDO);
    }

    /**
     * 根据id 查看 合同
     *
     * @param id
     * @return
     */
    @Override
    public ContractRespVO getById(String id) throws Exception {
        ContractDO contractDO = contractMapper.selectById(id);
        if (contractDO == null) {
            throw exception(ErrorCodeConstants.CONTRACT_NAME_NOT_EXISTS);
        }
        ContractRespVO contractRespVO = ContractConverter.INSTANCE.convert(contractDO);
        List<AttachmentRelRespVO> attachmentRelRespVOList = AttachmentRelConverter.INSTANCE.convert(attachmentRelMapper.selectList(AttachmentRelDO::getContractId, id));
        contractRespVO.setAttachmentList(attachmentRelRespVOList);
        //合同分类、类型添加名称
        contractRespVO.setContractCategoryName(contractCategoryMapper.selectById(contractRespVO.getContractCategory()).getName());
        contractRespVO.setContractTypeName(contractTypeMapper.selectById(contractRespVO.getContractType()).getName());
        //添加签署方信息
        ArrayList<SignatoryRespVO> signatoryRespVOList = new ArrayList<>();
        if (contractDO.getUpload() != 2) {
            //通过草拟创建合同
            List<RelativeByUserRespVO> relativeByUserRespVOS = signatoryById(contractDO.getId());
            contractRespVO.setSignatoryList(relativeByUserRespVOS);
        } else {
            //TODO 通过上传合同创建
            //添加我方

        }
        //添加主合同id，url
        contractRespVO.setFileUrl(fileApi.getURL(contractRespVO.getFileAddId()));
        return contractRespVO;
    }

    @Override
    public ApiRespVO getByIdOW(String id) throws Exception {
        ContractDO contractDO = contractMapper.selectById(id);
        if (contractDO == null) {
            throw exception(ErrorCodeConstants.CONTRACT_NAME_NOT_EXISTS);
        }
        ApiRespVO apiRespVO = ContractConverter.INSTANCE.convertV4(contractDO);
        List<AttachmentRelRespVO> attachmentRelRespVOList = AttachmentRelConverter.INSTANCE.convert(attachmentRelMapper.selectList(AttachmentRelDO::getContractId, id));
        apiRespVO.setAttachmentList(attachmentRelRespVOList);
        //添加文件url
        Long pdfFileId = contractDO.getPdfFileId();
        if (ObjectUtil.isNotEmpty(pdfFileId)) {
            String url = fileApi.getURL(pdfFileId);
            if (StringUtils.isNotEmpty(url)) {
                apiRespVO.setContractFileUrl(url);
            }
        }
        return apiRespVO;
    }

    @Override
    public ApiInfoRespVO getInfoByIdOW(String id) throws Exception {
        ContractDO contractDO = contractMapper.selectById(id);
        if (contractDO == null) {
            throw exception(ErrorCodeConstants.CONTRACT_NAME_NOT_EXISTS);
        }
        ApiInfoRespVO apiInfoRespVO = ContractConverter.INSTANCE.convertV5(contractDO);
        List<AttachmentRelRespVO> attachmentRelRespVOList = AttachmentRelConverter.INSTANCE.convert(attachmentRelMapper.selectList(AttachmentRelDO::getContractId, id));
        apiInfoRespVO.setAttachmentList(attachmentRelRespVOList);
        //封装合同参数
        List<ContractParameterDO> contractParameterDOS = contractParameterMapper.selectList(ContractParameterDO::getContractId, contractDO.getId());
        if (ObjectUtil.isNotEmpty(contractParameterDOS)) {
            List<ContractParameterRespDO> contractParameterRespDOS = ContractParameterConverter.INSTANCE.convertList(contractParameterDOS);
            apiInfoRespVO.setContractParameterList(contractParameterRespDOS);
        }
        //添加文件url
        Long pdfFileId = contractDO.getPdfFileId();
        if (ObjectUtil.isNotEmpty(pdfFileId)) {
            String url = fileApi.getURL(pdfFileId);
            if (StringUtils.isNotEmpty(url)) {
                apiInfoRespVO.setContractFileUrl(url);
            }
        }
        return apiInfoRespVO;
    }

    /**
     * 根据合同id 履约id 查询合同信息
     *
     * @param prefRespVO
     * @return
     * @throws Exception
     */
    @DataPermission(enable = false)
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ContractRespVO fulfillmentgetById(PrefRespVO prefRespVO) throws Exception {
//        if (StringUtils.isNotBlank(prefRespVO.getPrefId())) {
//            prefRespVO.setContractId(contractPerforMapper.selectOne(ContractPerformanceDO::getId, prefRespVO.getPrefId()).getContractId());
//        }
        //（相对方逻辑）免租户
        AtomicReference<ContractRespVO> atomic = new AtomicReference<>();
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {


                ContractDO contractDO = contractMapper.selectById(prefRespVO.getContractId());
                if (contractDO == null) {
                    throw exception(ErrorCodeConstants.CONTRACT_NAME_NOT_EXISTS);
                }


                ContractRespVO contractRespVO = ContractConverter.INSTANCE.convert(contractDO);
                //校验是否已申请解除
                checkTerminateApply(contractDO, contractRespVO);
                if (ObjectUtil.isNotEmpty(contractDO.getAmountType())) {
                    contractRespVO.setAmountTypeName(AmountTypeEnums.getInstance(contractDO.getAmountType()) != null ? AmountTypeEnums.getInstance(contractDO.getAmountType()).getInfo() : null);
                }

                if (contractRespVO.getPlatform() != null) {
                    contractRespVO.setPlatformName(PlatformEnums.getInstance(contractRespVO.getPlatform()).getInfo());
                }
                List<AttachmentRelRespVO> attachmentRelRespVOList = AttachmentRelConverter.INSTANCE.convert(attachmentRelMapper.selectList(AttachmentRelDO::getContractId, prefRespVO.getContractId()));
                contractRespVO.setAttachmentList(attachmentRelRespVOList);

                // 如果ecms_attachment_rel文件表里查不到数据，从ecms_neimeng_contract_file查询并返回
                if (CollectionUtil.isEmpty(attachmentRelRespVOList)) {
                    List<ContractFileDO> contractFileDOList = contractFileMapper.selectList(ContractFileDO::getContractId, prefRespVO.getContractId());
                    List<ContractFileDTO> dtoList = ContractFileConverter.INSTANCE.toDTOS(contractFileDOList);
                    contractRespVO.setAttachmentList(BeanUtils.toBean(dtoList, AttachmentRelRespVO.class));
                }
                //添加签署方信息
                List<RelativeByUserRespVO> relativeByUserRespVOS = signatoryById(contractDO.getId());
                //如果只有一个，需要供应商信息补全
                /*if (1 == relativeByUserRespVOS.size()) {
                    relativeByUserRespVOS.add(new RelativeByUserRespVO().setName(contractDO.getPartBName()).setId(contractDO.getPartBId()));
                }*/
                contractRespVO.setSignatoryList(relativeByUserRespVOS);
                //合同分类、类型添加名称
                Integer contractCategory = contractRespVO.getContractCategory();
                if (ObjectUtil.isNotEmpty(contractCategory)) {
                    ContractCategory category = contractCategoryMapper.selectById(contractRespVO.getContractCategory());
                    contractRespVO.setContractCategoryName(category == null ? null : category.getName());
                }
                String contractType = contractRespVO.getContractType();
                if (ObjectUtil.isNotEmpty(contractType)) {
                    ContractType type = contractTypeMapper.selectById(contractRespVO.getContractType());
                    contractRespVO.setContractTypeName(type == null ? null : type.getName());
                }

                //添加归档信息
                if (Integer.valueOf(1).equals(contractDO.getDocument())) {
                    //查询归档信息
                    ContractArchivesDO contractArchivesDO = contractArchivesMapper.selectOne(ContractArchivesDO::getContractId, contractDO.getId());
                    if (ObjectUtil.isNotEmpty(contractArchivesDO)) {
                        contractRespVO.setDocumentName(adminUserApi.getUser(Long.valueOf(contractArchivesDO.getCreator())).getNickname());
                        contractRespVO.setDocumentDate(contractDO.getUpdateTime());
                        List<BusinessFileDO> businessFileDOS = businessFileMapper.selectList(BusinessFileDO::getBusinessId, contractArchivesDO.getId());
                        if (ObjectUtil.isNotEmpty(businessFileDOS)) {
                            contractRespVO.setDocumentAttachmentList(AttachmentRelConverter.INSTANCE.convertV2(businessFileDOS));
                        }
                        if (CollectionUtil.isEmpty(prefRespVO.getTabList()) || !prefRespVO.getTabList().contains(LedgerTabEnums.BORROW.getCode()))
                            //封装借阅记录信息
                            getBorrowList(contractArchivesDO, contractRespVO);
                    }
                }
                //添加条款信息
                List<ContractTermDO> contractTermDOList = contractTermMapper.selectList(new LambdaQueryWrapperX<ContractTermDO>().eq(ContractTermDO::getContractId, contractDO.getId()));
                if (CollectionUtil.isNotEmpty(contractTermDOList)) {
                    contractRespVO.setTerms(ContractTermConverter.INSTANCE.convertListVO(contractTermDOList));
                }
                atomic.set(contractRespVO);
            });
        });
        ContractRespVO contractRespVO = atomic.get();
//        if (true) {
//            return contractRespVO;
//        }
        //借阅记录中判断是否过期
        if (ObjectUtil.isNotEmpty(prefRespVO.getRemainTime())) {
            if ("已过期".equals(prefRespVO.getRemainTime()) || "未到借阅时间".equals(prefRespVO.getRemainTime())) {
                return contractRespVO;
            }
        }
        ContractDO contractDO = ContractConverter.INSTANCE.convert2Resp(contractRespVO);
        if (contractDO.getStatus() != null && ContractStatusEnums.getInstance(contractDO.getStatus()) != null) {
            contractRespVO.setStatusName(ContractStatusEnums.getInstance(contractDO.getStatus()).getDesc());
        }

        if (contractDO.getUpload() != 2) {
        } else {
            //TODO 通过上传合同创建

        }
        //添加补充协议数据
        if (StringUtils.isNotBlank(prefRespVO.getPrefId())) {
            List<PrefAgreementRelDO> prefAgreementRelDOS = prefAgreementRelMapper.selectList(PrefAgreementRelDO::getPrefId, prefRespVO.getPrefId());
            if (ObjectUtils.isNotEmpty(prefAgreementRelDOS)) {
                contractRespVO.setAgreements(PrefAgreementRelConverter.INSTANCE.convertList(prefAgreementRelDOS));
            }
        }
        //添加终止合同信息
        TerminateContractDO terminateContractDO = terminateContractMapper.selectOne(TerminateContractDO::getContractId, contractDO.getId());
        if (ObjectUtils.isNotEmpty(terminateContractDO)) {
            contractRespVO.setTerminationFileName(terminateContractDO.getFileName());
            contractRespVO.setTerminationFileAddId(terminateContractDO.getFileAddId());
        }
        //添加履约任务信息集合
//        List<PerformanceTaskInfoRespVO> performanceTaskInfoRespVOList = perfTaskService.queryPerfTaskListById(prefRespVO.getPrefId());
//        if (CollectionUtil.isNotEmpty(performanceTaskInfoRespVOList)) {
//            contractRespVO.setPerformanceTaskInfoRespVOList(performanceTaskInfoRespVOList);
//        }
        //添加主合同id，url
        if(contractRespVO.getFileAddId() != null && contractRespVO.getFileAddId() != 0){
            FileDTO fileDTO = fileApi.selectById(contractRespVO.getFileAddId());
            if (fileDTO != null){
                contractRespVO.setFileUrl(minioUtils.generatePresignedUrl(fileDTO.getBucketName(), fileDTO.getPath()));
            }
        }
       if(contractRespVO.getPdfFileId() != null && contractRespVO.getPdfFileId() != 0){
            FileDTO pdfFileDTO = fileApi.selectById(contractRespVO.getPdfFileId());
            if (pdfFileDTO != null){
                contractRespVO.setPdfFileUrl(minioUtils.generatePresignedUrl(pdfFileDTO.getBucketName(), pdfFileDTO.getPath()));
            }
        }

        //添加合同参数信息
        List<ContractParameterDO> contractParameterDOList = contractParameterMapper.selectList(ContractParameterDO::getContractId, contractRespVO.getId());
        if (CollectionUtil.isNotEmpty(contractParameterDOList)) {
            contractRespVO.setContractParameterDOList(contractParameterDOList);
        }
        //添加合同章信息
        List<ContractSealDO> contractSealDOList = contractSealMapper.selectList(ContractSealDO::getContractId, contractRespVO.getId());
        if (CollectionUtil.isNotEmpty(contractSealDOList)) {
            contractRespVO.setContractSealDOList(contractSealDOList);
        }
        //添加合同付款计划信息
        List<PaymentScheduleDO> paymentScheduleDOList = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>()
                .eq(PaymentScheduleDO::getContractId, contractRespVO.getId()).orderByAsc(PaymentScheduleDO::getSort));
        if (CollectionUtil.isNotEmpty(paymentScheduleDOList)) {
            List<PaymentScheduleRespVO> respVOList = PaymentScheduleConverter.INSTANCE.toRespVOList(paymentScheduleDOList);
            for (PaymentScheduleRespVO respVO : respVOList) {
                respVO.setMoneyTypeName(MoneyTypeEnums.getInstance(respVO.getMoneyType()).getInfo());
                respVO.setAmountTypeName(AmountTypeEnums.getInstance(respVO.getAmountType()).getInfo());
                if (ObjectUtil.isNotEmpty(respVO.getId())) {
                    Relative relative = relativeMapper.selectById(respVO.getId());
                    if (ObjectUtil.isNotEmpty(relative)) {
                        if (ObjectUtil.isNotEmpty(relative.getCompanyName())) {
                            respVO.setPayee(relative.getCompanyName());
                        }
                    }
                    respVO.setStatusName(PaymentScheduleStatusEnums.getInstance(respVO.getStatus()).getInfo());
                }
            }
            contractRespVO.setPaymentScheduleDOList(respVOList);
        }
        //添加合同采购内容信息
        List<ContractPurchaseDO> contractPurchaseDOList = contractPurchaseMapper.selectList(ContractPurchaseDO::getContractId, contractRespVO.getId());
        if (CollectionUtil.isNotEmpty(contractPurchaseDOList)) {
            contractRespVO.setContractPurchaseDOList(contractPurchaseDOList);
        }
        //添加签定方信息 --- 内蒙需求
        List<ContractSignatoryDO> contractSignatoryDOList = contractSignatoryMapper.selectList(ContractSignatoryDO::getContractId, contractRespVO.getId());
        if (CollectionUtil.isNotEmpty(contractSignatoryDOList)) {
            contractRespVO.setContractSignatoryDOList(contractSignatoryDOList);
        }

        //封装签署文件信息
        contractRespVO.setContractInfo(new ContractInfoVO().setFileName(contractRespVO.getFileName()).setFileAddId(contractRespVO.getFileAddId()).setPdfFileId(contractRespVO.getPdfFileId()).setTemplateId(contractRespVO.getTemplateId()));
        //文件名称托底
        if (StringUtils.isBlank(contractRespVO.getFileName())) {
            contractRespVO.setFileName(contractRespVO.getName() + ".pdf");
        }
        SimpleModel model = simpleModelMapper.selectById(contractRespVO.getTemplateId());
        if (ObjectUtil.isNotNull(model)) {
            contractRespVO.getContractInfo().setType(model.getType());
        }
        contractRespVO.setBuyPlanCode("无");


        //封装合同变动信息
//        List<BpmContractChangeDO> bpmContractChangeDOS = bpmContractChangeMapper.selectList(BpmContractChangeDO::getMainContractId, contractRespVO.getId());
//        if (CollectionUtil.isNotEmpty(bpmContractChangeDOS)) {
//            contractRespVO.setContractChangeList(bpmContractChangeDOS);
//        }

        //签署流程信息
        String processInstanceId = contractDO.getProcessInstanceId();
        if (StringUtils.isNotEmpty(processInstanceId)) {
            List<BpmProcessRespDTO> activityRecord = bpmActivityApi.getActivityRecord(processInstanceId);
            if (CollectionUtil.isNotEmpty(activityRecord)) {
                contractRespVO.setBpmProcessRespDTOList(activityRecord);
            }
        }
        //返回office类型合同的文件id
        if (Integer.valueOf(1).equals(contractDO.getEditType())) {
            contractRespVO.setFileAddId(contractDO.getFileAddId());
        }

        //获取当前用户信息
//        AdminUserRespDTO userDO = adminUserApi.getUser(SecurityFrameworkUtils.getLoginUserId());
        AdminUserRespDTO userDO = adminUserApi.getUser(Long.valueOf(contractDO.getCreator()));
        DeptRespDTO dept = new DeptRespDTO();
        if (ObjectUtil.isNotEmpty(userDO)) {
            //获取部门信息
            dept = deptApi.getDept(userDO.getDeptId());
            if (ObjectUtil.isNotEmpty(dept)) {
                contractRespVO.setDeptId(dept.getId());
                contractRespVO.setDeptName(dept.getName());
            }
            contractRespVO.setCreator(String.valueOf(userDO.getId()));
            contractRespVO.setCreatorName(userDO.getNickname());
        }
        if (CollectionUtil.isEmpty(prefRespVO.getTabList())) {
            prefRespVO.setTabList(Arrays.asList(
                    LedgerTabEnums.TEXT.getCode(),
                    LedgerTabEnums.SIGN.getCode(),
                    LedgerTabEnums.ACCEPTANCE.getCode(),
                    LedgerTabEnums.BORROW.getCode(),
                    LedgerTabEnums.CHANGE.getCode(),
                    LedgerTabEnums.PAY.getCode(),
                    LedgerTabEnums.COLLECT.getCode()
            ));
        }
        enhanceQueryResult4Ledger(prefRespVO, contractDO, contractRespVO, dept);
        //草拟流程id
        getProcessInsId(contractRespVO);
        if (ContractUploadTypeEnums.ORDER_DRAFT.getCode().equals(contractDO.getUpload()) || THIRD_PARTY.getCode().equals(contractDO.getUpload())) {
            ContractOrderExtDO contractOrderExtDO = contractOrderExtMapper.selectOne(new LambdaQueryWrapperX<ContractOrderExtDO>().select(ContractOrderExtDO::getPerformStartDate, ContractOrderExtDO::getPerformEndDate).eq(ContractOrderExtDO::getId, contractRespVO.getId()));
            if (ObjectUtil.isNotEmpty(contractOrderExtDO)) {
                contractRespVO.setValidity0(contractOrderExtDO.getPerformStartDate());
                contractRespVO.setValidity1(contractOrderExtDO.getPerformEndDate());
            }
        } else if (REGISTER.getCode().equals(contractDO.getUpload())) {
            //合同登记的合同登记contract_register_application_approve审批的流程实例id直接是存在主合同表里的
            contractRespVO.setProcessInstanceId(contractDO.getProcessInstanceId());
        }
        LoginUser user = SecurityFrameworkUtils.getLoginUser();
        List<String> relativeContractIds = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(user)) {
            List<Relative> relatives = relativeMapper.selectList4Relative(user.getId());
            if (CollectionUtil.isNotEmpty(relatives)) {
                List<String> relativeIds = relatives.stream().map(Relative::getId).collect(Collectors.toList());
                List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getSignatoryId, relativeIds);
                //当前用户作为相对方的合同id集合
                relativeContractIds = signatoryRelDOS.stream().map(SignatoryRelDO::getContractId).collect(Collectors.toList());
            }
        }
        contractRespVO.setIsRelative((CollectionUtil.isNotEmpty(relativeContractIds) && relativeContractIds.contains(contractRespVO.getId())) ? IfNumEnums.YES.getCode() : IfNumEnums.NO.getCode());
        if(StringUtils.isNotBlank(contractRespVO.getRelativeMainContractId())){
            ContractDO contractDO1 = contractMapper.selectById(contractRespVO.getRelativeMainContractId());
            contractRespVO.setRelativeMainContractName(ObjectUtil.isNotNull(contractDO1)?contractDO1.getName():"");
        }
        return contractRespVO;
    }

    private void checkTerminateApply(ContractDO contractDO, ContractRespVO contractRespVO) {
        Long count = bpmContractChangeMapper.selectCount(
                new LambdaQueryWrapperX<BpmContractChangeDO>().eq(BpmContractChangeDO::getMainContractId, contractDO.getId())
                        .eq(BpmContractChangeDO::getChangeType, ContractChangeTypeEnums.TERMINATE.getCode())
                        .eq(BpmContractChangeDO::getResult, BpmProcessInstanceResultEnum.PROCESS.getResult())
        );
        if (0L < count) {
            contractRespVO.setAppliedTerminate(1);
        }
    }

    private void getProcessInsId(ContractRespVO contractRespVO) {
        List<BpmContract> bpmContractList = bpmContractMapper.selectList(new LambdaQueryWrapperX<BpmContract>().eq(BpmContract::getContractId, contractRespVO.getId()).orderByDesc(BpmContract::getCreateTime));
        if (CollectionUtil.isNotEmpty(bpmContractList)) {
            String processInsId = bpmContractList.get(0).getProcessInstanceId();
            contractRespVO.setProcessInstanceId(processInsId);
        }
    }

    private void enhanceQueryResult4Ledger(PrefRespVO prefRespVO, ContractDO contractDO, ContractRespVO contractRespVO, DeptRespDTO dept) {
        if (CollectionUtil.isEmpty(prefRespVO.getTabList())) {
            return;
        }
        List<String> tabList = prefRespVO.getTabList();
        for (String tab : tabList) {
            LedgerTabEnums tabEnums = LedgerTabEnums.getInstance(tab);
            if (ObjectUtil.isNull(tabEnums)) {
                continue;
            }
            switch (tabEnums) {
                // 正文&附件
                case TEXT:
                    enhanceText(prefRespVO, contractDO, contractRespVO);
                    break;
                // 合同签订
                case SIGN:
                    enhanceSign(prefRespVO, contractDO, contractRespVO);
                    break;
                // 验收记录
                case ACCEPTANCE:
                    enhanceAcceptance(prefRespVO, contractDO, contractRespVO);
                    break;
                //借阅记录
                case BORROW:
                    enhanceBorrow(prefRespVO, contractDO, contractRespVO, dept);
                    break;
                // 合同变动
                case CHANGE:
                    enhanceChange(prefRespVO, contractDO, contractRespVO);
                    break;
                // 付款记录或
                case PAY:
                    enhancePayApplication(prefRespVO, contractDO, contractRespVO);
                    break;
//                // 收款记录
                case COLLECT:
                    enhanceCollect(prefRespVO, contractDO, contractRespVO);
                    break;
                default:
                    break;
            }
        }
    }

    @Resource
    private ContractInvoiceManageMapper contractInvoiceManageMapper;

    private void enhanceCollect(PrefRespVO prefRespVO, ContractDO contractDO, ContractRespVO contractRespVO) {
        List<ContractInvoiceManageDO> invoiceDOList = contractInvoiceManageMapper.selectList(new LambdaQueryWrapperX<ContractInvoiceManageDO>().eq(ContractInvoiceManageDO::getContractId, contractDO.getId()).orderByDesc(ContractInvoiceManageDO::getCreateTime));
        if (CollectionUtil.isEmpty(invoiceDOList)) {
            return;
        }
        List<String> planids = invoiceDOList.stream().map(ContractInvoiceManageDO::getPlanId).collect(Collectors.toList());
        List<Long> userIds = invoiceDOList.stream().map(ContractInvoiceManageDO::getCreator).map(Long::valueOf).collect(Collectors.toList());
        List<AdminUserRespDTO> userRespDTOList = adminUserApi.getUserList(userIds);
        Map<Long, AdminUserRespDTO> userRespDTOMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(userRespDTOList)) {
            userRespDTOMap = CollectionUtils.convertMap(userRespDTOList, AdminUserRespDTO::getId);
        }
        //相对方
        List<Relative> relativeList = relativeMapper.selectListByContractId(contractDO.getId());
        Map<String, Relative> relativeMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(relativeList)) {
            relativeMap = CollectionUtils.convertMap(relativeList, Relative::getId);
        }
        List<BpmTaskAllInfoRespVO> allInfoRespVOList;
        Map<String, BpmTaskAllInfoRespVO> allTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
        if (CollectionUtil.isNotEmpty(invoiceDOList)) {
            List<String> instanceList = invoiceDOList.stream().map(ContractInvoiceManageDO::getProcessInstanceId).collect(Collectors.toList());
            allInfoRespVOList = bpmTaskApi.getAllTaskIdByProcessInstanceIds(instanceList);
            allInfoRespVOList = EcontractUtil.distinctTask(allInfoRespVOList);
            allTaskMap = CollectionUtils.convertMap(allInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
        }
        List<PayRecordRespVO> collectApplicationRespVOList = new ArrayList<>();
        for (ContractInvoiceManageDO invoiceDO : invoiceDOList) {
            PayRecordRespVO payRecordRespVO = new PayRecordRespVO();
            payRecordRespVO.setId(invoiceDO.getId());
            payRecordRespVO.setContractId(contractDO.getId());
            //相对方
            payRecordRespVO.setRelativeName(contractDO.getPartBName());
            payRecordRespVO.setApplyAmount(invoiceDO.getInvoiceAmont());
            //支付比例
            if (!BigDecimal.ZERO.equals(payRecordRespVO.getApplyAmount()) && 0L != contractDO.getAmount()) {
                BigDecimal applyAmount = payRecordRespVO.getApplyAmount();
                BigDecimal contractAmount = BigDecimal.valueOf(contractDO.getAmount());
                BigDecimal ratio = applyAmount.divide(
                        contractAmount,        // 除数
                        4,                     // 保留4位小数
                        RoundingMode.HALF_UP   // 四舍五入模式
                );

                payRecordRespVO.setPaymentRatio(ratio.multiply(new BigDecimal("100")));
            }

            //申请人
            AdminUserRespDTO adminUserRespDTO = userRespDTOMap.get(Long.valueOf(invoiceDO.getCreator()));
            if (ObjectUtil.isNotNull(adminUserRespDTO)) {
                payRecordRespVO.setSubmitterName(adminUserRespDTO.getNickname());
            }
            payRecordRespVO.setApplyTime(Date.from(invoiceDO.getCreateTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Asia/Shanghai")).toInstant()));
            payRecordRespVO.setBuyerNumber(invoiceDO.getBuyerNumber());
            payRecordRespVO.setBankName(invoiceDO.getBankName());
            payRecordRespVO.setBankAccount(invoiceDO.getBankAccount());
            //票款方式
            payRecordRespVO.setAmountType(invoiceDO.getAmountType());
            InvoiceAmountTypeEnums invoiceAmountTypeEnums = InvoiceAmountTypeEnums.getInstance(invoiceDO.getAmountType());
            if (ObjectUtil.isNotEmpty(invoiceAmountTypeEnums)) {
                payRecordRespVO.setAmountTypeName(invoiceAmountTypeEnums.getInfo());
            }
            BpmTaskAllInfoRespVO taskAllInfoRespVO = allTaskMap.get(invoiceDO.getProcessInstanceId());
            if (ObjectUtil.isNotNull(taskAllInfoRespVO)) {
                payRecordRespVO.setNodeName(taskAllInfoRespVO.getName());
            }
            payRecordRespVO.setProcessInstanceId(invoiceDO.getProcessInstanceId());
            payRecordRespVO.setRelativeName(contractDO.getPartBName());
            payRecordRespVO.setBuyPlanIds(Arrays.asList(invoiceDO.getPlanId()));
            collectApplicationRespVOList.add(payRecordRespVO);
        }
        contractRespVO.setCollectApplicationRespVOList(collectApplicationRespVOList);
    }

    private void enhancePayApplication(PrefRespVO prefRespVO, ContractDO contractDO, ContractRespVO contractRespVO) {
        List<PaymentApplicationDO> applicationDOList = paymentApplicationMapper.selectList4lContractId(contractDO.getId());
        if (CollectionUtil.isEmpty(applicationDOList)) {
            return;
        }

        List<BpmTaskAllInfoRespVO> allInfoRespVOList;
        Map<String, BpmTaskAllInfoRespVO> allTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
        if (CollectionUtil.isNotEmpty(applicationDOList)) {
            List<String> instanceList = applicationDOList.stream().map(PaymentApplicationDO::getProcessInstanceId).collect(Collectors.toList());
            allInfoRespVOList = bpmTaskApi.getAllTaskIdByProcessInstanceIds(instanceList);
            allInfoRespVOList = EcontractUtil.distinctTask(allInfoRespVOList);
            allTaskMap = CollectionUtils.convertMap(allInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
        }


        List<Long> userIds = applicationDOList.stream().map(PaymentApplicationDO::getApplicantId).filter(Objects::nonNull).map(Long::valueOf).collect(Collectors.toList());
        List<AdminUserRespDTO> userRespDTOList = adminUserApi.getUserList(userIds);
        Map<Long, AdminUserRespDTO> userRespDTOMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(userRespDTOList)) {
            userRespDTOMap = CollectionUtils.convertMap(userRespDTOList, AdminUserRespDTO::getId);
        }
        List<String> instanceList = applicationDOList.stream().map(PaymentApplicationDO::getProcessInstanceId).collect(Collectors.toList());
        Map<String, BpmTaskAllInfoRespVO> finalTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();

        if (CollectionUtil.isNotEmpty(instanceList)) {
            finalTaskMap = CollectionUtils.convertMap(EcontractUtil.distinctTask(bpmTaskApi.getAllTaskIdByProcessInstanceIds(instanceList)), BpmTaskAllInfoRespVO::getProcessInstanceId);
        }

        List<PayRecordRespVO> payRecordRespVOList = new ArrayList<>();
        for (PaymentApplicationDO applicationDO : applicationDOList) {
            PayRecordRespVO respVO = new PayRecordRespVO();
            respVO.setId(applicationDO.getId());
            respVO.setContractId(contractDO.getId());
            respVO.setAmountType(applicationDO.getPaymentType());
            respVO.setApplyTime(Date.from(applicationDO.getApplyTime().atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("Asia/Shanghai")).toInstant()));
            respVO.setBuyPlanIds(Arrays.asList(respVO.getId()));
            respVO.setStatus(applicationDO.getStatus());


            //申请人
            AdminUserRespDTO adminUserRespDTO = userRespDTOMap.get(Long.valueOf(applicationDO.getApplicantId()));
            if (ObjectUtil.isNotNull(adminUserRespDTO)) {
                respVO.setSubmitterName(adminUserRespDTO.getNickname());
            }
//            //款项名称
//            MoneyTypeEnums moneyTypeEnums = MoneyTypeEnums.getInstance(applicationDO.getCollectionType());
//            if (ObjectUtil.isNotNull(moneyTypeEnums)) {
//                respVO.setMoneyTypeName(moneyTypeEnums.getInfo());
//            }
            //结算方式
            SettlementMethodEnums settlementMethodEnums = SettlementMethodEnums.getInstance(applicationDO.getSettlementMethod());
            if (ObjectUtil.isNotNull(settlementMethodEnums)) {
                respVO.setSettlementMethodStr(settlementMethodEnums.getInfo());
            }
            //票款方式
            PaymentTypeEnums paymentTypeEnums = PaymentTypeEnums.getInstance(applicationDO.getPaymentType());
            if (ObjectUtil.isNotNull(paymentTypeEnums)) {
                respVO.setAmountTypeName(paymentTypeEnums.getInfo());
            }
            BpmTaskAllInfoRespVO taskAllInfoRespVO = allTaskMap.get(applicationDO.getProcessInstanceId());
            if (ObjectUtil.isNotNull(taskAllInfoRespVO)) {
                respVO.setNodeName(taskAllInfoRespVO.getName());
            }
            respVO.setRelativeName(contractDO.getPartBName());

            respVO.setBankAccount(applicationDO.getPayeeAccount());
            respVO.setBankName(applicationDO.getPayeeBankName());
            respVO.setProcessInstanceId(applicationDO.getProcessInstanceId());
            respVO.setApplyAmount(applicationDO.getCurrentPayAmount());
            //比例
            if (!BigDecimal.ZERO.equals(respVO.getApplyAmount()) && 0L != contractDO.getAmount()) {
                BigDecimal applyAmount = respVO.getApplyAmount();
                BigDecimal contractAmount = new BigDecimal(contractDO.getAmount());
                BigDecimal ratio = applyAmount.divide(
                        contractAmount,        // 除数
                        4,                     // 保留4位小数
                        RoundingMode.HALF_UP   // 四舍五入模式
                );

                respVO.setPaymentRatio(ratio.multiply(new BigDecimal("100")));
                payRecordRespVOList.add(respVO);
            }
            BpmTaskAllInfoRespVO nowTaskAllInfoRespVO = finalTaskMap.get(respVO.getProcessInstanceId());
            if (ObjectUtil.isNotNull(nowTaskAllInfoRespVO)) {
                respVO.setStatusName(nowTaskAllInfoRespVO.getName());
            }
        }
        contractRespVO.setPayApplicationRespVOList(payRecordRespVOList);
    }


    private void enhanceChange(PrefRespVO prefRespVO, ContractDO contractDO, ContractRespVO contractRespVO) {
        List<BpmContractChangeDO> bpmContractChangeDOList = bpmContractChangeMapper.selectList(new LambdaQueryWrapperX<BpmContractChangeDO>().eq(BpmContractChangeDO::getMainContractId, contractRespVO.getId()).orderByDesc(BpmContractChangeDO::getCreateTime));
        if (CollectionUtil.isEmpty(bpmContractChangeDOList)) {
            return;
        }
        List<BpmContractChangeRespVO> contractChangeList = ChangeConverter.INSTANCE.listDo2Resp(bpmContractChangeDOList);
        for (BpmContractChangeRespVO changeRespVO : contractChangeList) {

            changeRespVO.setCreatorName(contractRespVO.getCreatorName());
            ContractChangeTypeEnums changeTypeEnums = ContractChangeTypeEnums.getInstance(changeRespVO.getChangeType());
            if (ObjectUtil.isNotNull(changeTypeEnums)) {
                changeRespVO.setChangeTypeName(changeTypeEnums.getInfo());
            }
            enhanceChangeResult(changeRespVO, changeRespVO.getResult());
        }
        contractRespVO.setContractChangeList(contractChangeList);
    }

    private void enhanceChangeResult(BpmContractChangeRespVO changeRespVO, Integer result) {
        switch (result) {
            case 0:
                changeRespVO.setResultName("待送审");
                return;
            case 1:
                changeRespVO.setResultName("审批中");
                break;
            case 2:
                changeRespVO.setResultName("审批通过");
                break;
            case 5:
                changeRespVO.setResultName("被退回");
            default:
                break;
        }

    }

    private void enhanceBorrow(PrefRespVO prefRespVO, ContractDO contractDO, ContractRespVO contractRespVO, DeptRespDTO dept) {
        List<ContractArchivesDO> contractArchivesDOList = contractArchivesMapper.selectList(new LambdaQueryWrapperX<ContractArchivesDO>().eq(ContractArchivesDO::getContractId, contractDO.getId()));
        ContractArchivesDO contractArchivesDO = null;
        if (!CollectionUtil.isEmpty(contractArchivesDOList)) {
            contractArchivesDO = contractArchivesDOList.get(0);
            contractRespVO.setArchiveId(contractArchivesDO.getId());
        }
        List<ContractBorrowBpmDO> borrowBpmDOList = borrowBpmMapper.select4Ledger(contractDO.getId());
        if (CollectionUtil.isEmpty(borrowBpmDOList)) {
            return;
        }
        List<ContractBorrowRespVO> contractBorrowRespVOList = new ArrayList<>();
        for (ContractBorrowBpmDO borrowBpmDO : borrowBpmDOList) {
            ContractBorrowRespVO borrowRespVO = new ContractBorrowRespVO()
                    .setBorrowBpmId(borrowBpmDO.getId())
                    .setContractId(contractRespVO.getId())
                    .setContractCode(contractDO.getCode())
                    .setContractName(contractRespVO.getName())
                    .setContractTypeName(contractRespVO.getContractTypeName())
                    .setBorrowerName(borrowBpmDO.getSubmitterName())
                    .setReturnTime(borrowBpmDO.getReturnTime())
                    .setSubmitTime(borrowBpmDO.getSubmitTime())
                    .setCreateTime(borrowBpmDO.getCreateTime())
                    .setBorrowerDeptName(dept.getName())
                    .setMedium(contractArchivesDO.getMedium())
                    .setArchiveId(borrowBpmDO.getArchiveId());

            contractBorrowRespVOList.add(borrowRespVO);
        }
        contractRespVO.setBorrowRecordRespVOList(contractBorrowRespVOList);
    }

    @Resource
    private AcceptanceMapper acceptanceMapper;

    private void enhanceAcceptance(PrefRespVO prefRespVO, ContractDO contractDO, ContractRespVO contractRespVO) {

        List<AcceptanceDO> acceptanceDOList = acceptanceMapper.selectList(new LambdaQueryWrapperX<AcceptanceDO>().eq(AcceptanceDO::getContractId, contractDO.getId()).orderByDesc(AcceptanceDO::getCreateTime));
        if (CollectionUtil.isEmpty(acceptanceDOList)) {
            return;
        }
        List<AcceptanceRecordRespVO> acceptanceRespVOList = AcceptanceConverter.INSTANCE.do2RespList(acceptanceDOList);
        List<String> planIds = acceptanceDOList.stream().map(AcceptanceDO::getPlanId).collect(Collectors.toList());
        List<PaymentScheduleDO> scheduleDOList = paymentScheduleMapper.selectList(PaymentScheduleDO::getId, planIds);
        Map<String, PaymentScheduleDO> scheduleDOMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(scheduleDOList)) {
            scheduleDOMap = CollectionUtils.convertMap(scheduleDOList, PaymentScheduleDO::getId);
        }
        for (AcceptanceRecordRespVO respVO : acceptanceRespVOList) {
            PaymentScheduleDO scheduleDO = scheduleDOMap.get(respVO.getPlanId());
            if (ObjectUtil.isNotEmpty(scheduleDO)) {
                respVO.setSort(scheduleDO.getSort());
                respVO.setAmount(scheduleDO.getAmount());
                //结算类型
                AmountTypeEnums amountTypeEnums = AmountTypeEnums.getInstance(scheduleDO.getAmountType());
                if (ObjectUtil.isNotNull(amountTypeEnums)) {
                    respVO.setAmountTypeName(amountTypeEnums.getInfo());
                }
                //款项名称
                MoneyTypeEnums moneyTypeEnums = MoneyTypeEnums.getInstance(scheduleDO.getMoneyType());
                if (ObjectUtil.isNotNull(moneyTypeEnums)) {
                    respVO.setMoneyTypeName(moneyTypeEnums.getInfo());
                }
                respVO.setStatus(scheduleDO.getStatus());
                PaymentScheduleStatusEnums scheduleStatusEnums = PaymentScheduleStatusEnums.getInstance(scheduleDO.getStatus());
                respVO.setStatusName(ObjectUtil.isNotNull(scheduleStatusEnums) ? scheduleStatusEnums.getInfo() : "");
            }

            if (ObjectUtil.isNotNull(contractDO)) {
                //相对方
                respVO.setRelativeName(contractDO.getPartBName());
            }
            //在这里显示的一定是需要验收的
            respVO.setNeedAcceptance(1);
        }
        List<AcceptanceRecordRespVO> sortedList = acceptanceRespVOList.stream()
                .sorted(Comparator.comparingInt(AcceptanceRecordRespVO::getSort))
                .collect(Collectors.toList());
        contractRespVO.setAcceptanceRespVOList(sortedList);
    }


    @Resource
    private BpmContractSignetMapper bpmContractSignetMapper;

    private void enhanceSign(PrefRespVO prefRespVO, ContractDO contractDO, ContractRespVO contractRespVO) {
        List<BpmContractSignetDO> bpmContractSignetDOs = bpmContractSignetMapper.selectList(new LambdaQueryWrapperX<BpmContractSignetDO>().eq(BpmContractSignetDO::getBusinessId, contractDO.getId()).orderByDesc(BpmContractSignetDO::getUpdateTime));
        if (CollectionUtil.isEmpty(bpmContractSignetDOs)) {
            return;
        }
        SignetManageRespVO signetManageRespVO = signetService.getSignetManage(bpmContractSignetDOs.get(0).getId());
        signetManageRespVO.setSignTime(contractDO.getSignDate());
        contractRespVO.setSignetManageRespVO(signetManageRespVO);
    }

    private void enhanceText(PrefRespVO prefRespVO, ContractDO contractDO, ContractRespVO contractRespVO) {
        ContractFileRespVO fileRespVO = new ContractFileRespVO();
        List<Long> fileIds = new ArrayList<>();

        //线下审批
        if (IfNumEnums.YES.getCode().equals(contractDO.getIsOffline())) {
            List<OfflineFileDO> offlineFileDOS = offlineFileMapper.selectList(new LambdaQueryWrapperX<OfflineFileDO>().eq(OfflineFileDO::getBusinessId, contractDO.getId()).orderByDesc(OfflineFileDO::getUpdateTime));
            OfflineFileDO offlineFileDO = new OfflineFileDO();
            if (CollectionUtil.isNotEmpty(offlineFileDOS)) {
                offlineFileDO = offlineFileDOS.get(0);
            }
            fileRespVO.setTxtFileId(contractDO.getPdfFileId());
            fileIds.add(fileRespVO.getTxtFileId());
            if (ObjectUtil.isNotNull(offlineFileDO)) {
                // 审批材料：如果是线下审批的显示，上传的审批文件
                if (ObjectUtil.isNotNull(offlineFileDO)) {
                    fileRespVO.setOfflineApproveFileId(offlineFileDO.getId());
                }
            }
        } else {
            // 非线下审批的合同正文：合同起草/签署合同文件(盖章的文件)
            if (IfNumEnums.YES.getCode().equals(contractDO.getIsOffline()) && ObjectUtil.isNotNull(contractDO.getPdfFileId()) && 0L != contractDO.getPdfFileId()) {
                contractRespVO.setPdfFileId(contractDO.getPdfFileId());
                FileDTO fileDTO = fileApi.selectById(contractDO.getPdfFileId());
                if (ObjectUtil.isNotNull(fileDTO)) {
                    fileRespVO.setTxtFileId(fileDTO.getId());
                    fileRespVO.setTxtFileName(fileDTO.getName());
                    fileRespVO.setTxtFileUrl(fileDTO.getUrl());
                }
            }
        }
        //线下签署
        if (IfNumEnums.YES.getCode().equals(contractDO.getIsOfflineSign())) {
            // 合同文件：如果是线下签署的显示，线上起草的文件
            if (ObjectUtil.isNotNull(contractDO.getOldPdfFileId())) {
                fileRespVO.setOfflineDraftFileId(contractDO.getOldPdfFileId());
            }
        }
        // 合同附件
        List<AttachmentRelDO> attachmentRelDOS = attachmentRelMapper.selectByContractId(contractDO.getId());
        if (CollectionUtil.isNotEmpty(attachmentRelDOS)) {
            List<FileDTO> attachmentFileVOs = new ArrayList<>();
            for (AttachmentRelDO attachmentRelDO : attachmentRelDOS) {
                FileDTO fileDTO = new FileDTO();
                fileDTO.setId(attachmentRelDO.getAttachmentAddId());
                fileDTO.setName(attachmentRelDO.getAttachmentName());
                fileDTO.setUrl(attachmentRelDO.getUrl());
                attachmentFileVOs.add(fileDTO);
            }
            fileRespVO.setAttachmentFileVOs(attachmentFileVOs);
        }

        //批量查询文件信息
        if (ObjectUtil.isNotNull(fileRespVO.getTxtFileId())) {
            fileIds.add(fileRespVO.getTxtFileId());
        }
        if (ObjectUtil.isNotNull(fileRespVO.getOfflineDraftFileId())) {
            fileIds.add(fileRespVO.getOfflineDraftFileId());
        }
        if (ObjectUtil.isNotNull(fileRespVO.getOfflineApproveFileId())) {
            fileIds.add(fileRespVO.getOfflineApproveFileId());
        }
        List<FileDTO> fileDTOs = fileApi.selectBatchIds(fileIds);
        if (CollectionUtil.isNotEmpty(fileDTOs)) {
            enhanceFileResult(fileDTOs, contractRespVO, fileRespVO);
        }
    }

    private void enhanceFileResult(List<FileDTO> fileDTOs, ContractRespVO contractRespVO, ContractFileRespVO fileRespVO) {
        Map<Long, FileDTO> fileDTOMap = new HashMap<>();
        if (CollectionUtil.isEmpty(fileDTOs)) {
            return;
        }
        fileDTOMap = CollectionUtils.convertMap(fileDTOs, FileDTO::getId);
        //签章正文
        if (ObjectUtil.isNotNull(fileRespVO.getTxtFileId())) {
            FileDTO fileDTO = fileDTOMap.get(fileRespVO.getTxtFileId());
            if (ObjectUtil.isNotNull(fileDTO)) {
                fileRespVO.setTxtFileName(fileDTO.getName());
                fileRespVO.setTxtFileUrl(fileDTO.getUrl());
            }
        }

        //线上正文
        if (ObjectUtil.isNotNull(fileRespVO.getOfflineDraftFileId())) {
            FileDTO fileDTO = fileDTOMap.get(fileRespVO.getOfflineDraftFileId());
            if (ObjectUtil.isNotNull(fileDTO)) {
                fileRespVO.setOfflineDraftFileName(fileDTO.getName());
                fileRespVO.setOfflineDraftFileUrl(fileDTO.getUrl());
            }
        }

        //线下审批材料
        if (ObjectUtil.isNotNull(fileRespVO.getOfflineApproveFileId())) {
            FileDTO fileDTO = fileDTOMap.get(fileRespVO.getOfflineApproveFileId());
            if (ObjectUtil.isNotNull(fileDTO)) {
                fileRespVO.setOfflineApproveFileName(fileDTO.getName());
                fileRespVO.setOfflineApproveFileUrl(fileDTO.getUrl());
            }
        }
        contractRespVO.setFileRespVO(fileRespVO);
    }


    private void getBorrowList(ContractArchivesDO contractArchivesDO, ContractRespVO contractRespVO) {
        List<ContractBorrowBpmDO> doPageResult = borrowBpmMapper.selectList(ContractBorrowBpmDO::getArchiveId, contractArchivesDO.getId());
        List<BorrowRecordRespVO> result = ContractBorrowBpmConverter.INSTANCE.convertList2(doPageResult);
        //查询存在的档案信息
        List<String> archiveIdS = result.stream().map(BorrowRecordRespVO::getArchiveId).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(archiveIdS)) {
            List<ContractArchivesDO> archivesDOS = contractArchivesMapper.selectList(ContractArchivesDO::getId, archiveIdS);
            Map<String, ContractArchivesDO> archivesDOMap = archivesDOS.stream().collect(Collectors.toMap(ContractArchivesDO::getId, Function.identity()));
            if (CollectionUtil.isNotEmpty(archivesDOMap)) {
                for (BorrowRecordRespVO borrowRecordRespVO : result) {
                    ContractArchivesDO archivesDO = archivesDOMap.get(borrowRecordRespVO.getArchiveId());
                    // 填充档案信息
                    if (ObjectUtil.isNotNull(archivesDO)) {
                        borrowRecordRespVO.setName(archivesDO.getName() != null ? archivesDO.getName() : null);
//                        borrowRecordRespVO.setCode(archivesDO.getCode() != null ? archivesDO.getCode() : null);
//                        borrowRecordRespVO.setFondsNo(archivesDO.getFondsNo() != null ? archivesDO.getFondsNo() : null);
//                        borrowRecordRespVO.setFirstLevelNo(archivesDO.getFirstLevelNo() != null ? archivesDO.getFirstLevelNo() : null);
//                        borrowRecordRespVO.setSecondLevelNo(archivesDO.getSecondLevelNo() != null ? archivesDO.getSecondLevelNo() : null);
//                        borrowRecordRespVO.setVolumeNo(archivesDO.getVolumeNo() != null ? archivesDO.getVolumeNo() : null);
                        borrowRecordRespVO.setProCode(archivesDO.getProCode() != null ? archivesDO.getProCode() : null);
                        borrowRecordRespVO.setProName(archivesDO.getProName() != null ? archivesDO.getProName() : null);
                        borrowRecordRespVO.setContractId(archivesDO.getContractId() != null ? archivesDO.getContractId() : null);
                    }
                    // 获取当前时间
                    Date date = new Date();
                    Date returnTime = borrowRecordRespVO.getReturnTime();
                    Date submitTime = borrowRecordRespVO.getSubmitTime();
                    if (borrowRecordRespVO.getResult() == 2) {
                        if (submitTime != null && submitTime.after(date)) {
                            if (borrowRecordRespVO.getBorrowType().contains("1")) {
                                borrowRecordRespVO.setBorrowStatusName("借阅中");
                            } else {
                                borrowRecordRespVO.setBorrowStatusName("待取档");
                            }
                        } else if (returnTime != null && returnTime.after(date)) {
                            if (ObjectUtil.isNotEmpty(borrowRecordRespVO.getIsReturn())) {
                                if (IfNumEnums.NO.getCode().equals(borrowRecordRespVO.getIsReturn())) {
                                    // 计算剩余时间
                                    Instant returnInstant = returnTime.toInstant();
                                    Instant submitInstant = date.toInstant();
                                    Duration duration = Duration.between(submitInstant, returnInstant);
                                    long remainTimeInSeconds = duration.getSeconds();
                                    long days = remainTimeInSeconds / (24 * 3600);
                                    long hours = (remainTimeInSeconds % (24 * 3600)) / 3600;
                                    borrowRecordRespVO.setRemainTime(days + "天" + hours + "小时");
                                    borrowRecordRespVO.setBorrowStatusName("借阅中");
                                } else if (IfNumEnums.YES.getCode().equals(borrowRecordRespVO.getIsReturn())) {
                                    borrowRecordRespVO.setBorrowStatusName("已归还");
                                }
                            } else {
                                // 计算剩余时间
                                Instant returnInstant = returnTime.toInstant();
                                Instant submitInstant = date.toInstant();
                                Duration duration = Duration.between(submitInstant, returnInstant);
                                long remainTimeInSeconds = duration.getSeconds();
                                long days = remainTimeInSeconds / (24 * 3600);
                                long hours = (remainTimeInSeconds % (24 * 3600)) / 3600;
                                borrowRecordRespVO.setRemainTime(days + "天" + hours + "小时");
                                borrowRecordRespVO.setBorrowStatusName("借阅中");
                            }

                        } else {
                            if (ObjectUtil.isNotEmpty(borrowRecordRespVO.getIsReturn())) {
                                if (IfNumEnums.YES.getCode().equals(borrowRecordRespVO.getIsReturn())) {
                                    borrowRecordRespVO.setBorrowStatusName("已归还");
                                } else if (borrowRecordRespVO.getBorrowType().contains("1") && borrowRecordRespVO.getIsReturn() == 0) {
                                    borrowRecordRespVO.setBorrowStatusName("借阅中");
                                }
                            }
                            if ("0".equals(borrowRecordRespVO.getBorrowType())) {
                                borrowRecordRespVO.setBorrowStatusName("已归还");
                            }

                        }
                    } else {
                        borrowRecordRespVO.setBorrowStatusName(CommonFlowableReqVOResultStatusEnums.getInstance(borrowRecordRespVO.getResult()).getInfo());
                    }
                }
            }
            contractRespVO.setBorrowRecordList(result);
        }
    }

    @Override
    public ContractCheckRespVO checkById(PrefRespVO prefRespVO) throws Exception {
        if (StringUtils.isNotBlank(prefRespVO.getPrefId())) {
            prefRespVO.setContractId(contractPerforMapper.selectOne(ContractPerformanceDO::getId, prefRespVO.getPrefId()).getContractId());
        }
        ContractDO contractDO = contractMapper.selectById(prefRespVO.getContractId());
        if (contractDO == null) {
            throw exception(ErrorCodeConstants.CONTRACT_NAME_NOT_EXISTS);
        }
        ContractCheckRespVO contractCheckRespVO = ContractConverter.INSTANCE.convertV2(contractDO);
        List<AttachmentRelRespVO> attachmentRelRespVOList = AttachmentRelConverter.INSTANCE.convert(attachmentRelMapper.selectList(AttachmentRelDO::getContractId, prefRespVO.getContractId()));
        contractCheckRespVO.setAttachmentList(attachmentRelRespVOList);
        //合同分类、类型添加名称
        contractCheckRespVO.setContractCategoryName(contractCategoryMapper.selectById(contractCheckRespVO.getContractCategory()).getName());
        contractCheckRespVO.setContractTypeName(contractTypeMapper.selectById(contractCheckRespVO.getContractType()).getName());
        //添加签署方信息
        ArrayList<SignatoryRespVO> signatoryRespVOList = new ArrayList<>();
        if (contractDO.getUpload() != 2) {
            //通过草拟创建合同
            List<RelativeByUserRespVO> relativeByUserRespVOList = signatoryById(contractDO.getId());

            contractCheckRespVO.setSignatoryList(relativeByUserRespVOList);
        } else {
            //TODO 通过上传合同创建

        }
        //添加补充协议数据
        if (StringUtils.isNotBlank(prefRespVO.getPrefId())) {
            List<PrefAgreementRelDO> prefAgreementRelDOS = prefAgreementRelMapper.selectList(PrefAgreementRelDO::getPrefId, prefRespVO.getPrefId());
            if (ObjectUtils.isNotEmpty(prefAgreementRelDOS)) {
                contractCheckRespVO.setAgreements(PrefAgreementRelConverter.INSTANCE.convertList(prefAgreementRelDOS));
            }
        }
        //添加终止合同信息
        TerminateContractDO terminateContractDO = terminateContractMapper.selectOne(TerminateContractDO::getContractId, contractDO.getId());
        if (ObjectUtils.isNotEmpty(terminateContractDO)) {
            contractCheckRespVO.setTerminationFileName(terminateContractDO.getFileName());
            contractCheckRespVO.setTerminationFileAddId(terminateContractDO.getFileAddId());
        }
        //添加履约任务信息集合
        List<PerformanceTaskInfoRespVO> performanceTaskInfoRespVOList = perfTaskService.queryPerfTaskListById(prefRespVO.getPrefId());
        if (CollectionUtil.isNotEmpty(performanceTaskInfoRespVOList)) {
            contractCheckRespVO.setPerformanceTaskInfoRespVOList(performanceTaskInfoRespVOList);
        }
        //添加归档信息
        if (Integer.valueOf(1).equals(contractDO.getDocument())) {
            DocumentRelDO documentRelDO = documentRelMapper.selectOne(DocumentRelDO::getContractId, contractDO.getId());
            contractCheckRespVO.setDocumentInfo(new DocumentInfoVO().setDocumentName(adminUserApi.getUser(Long.valueOf(contractDO.getUpdater())).getNickname()).setDocumentDate(contractDO.getUpdateTime()).setDocumentFileName(documentRelDO.getName()).setDocumentAddId(documentRelDO.getAddId()));
        }
        //TODO 添加存量上传文件信息
//        contractCheckRespVO.setUploadInfo(new UploadInfoVO().setSignDate(contractDO.getSignDate()).setValidity0(contractDO.getValidity0()).setValidity1(contractDO.getValidity1()).setAmountType(contractDO.getAmountType()).setAmount(contractDO.getAmount()).setMySignatory(contractDO.getMySignatory()).setSignName(contractDO.getSignName()));
        //封装签署文件信息
        contractCheckRespVO.setContractInfo(new ContractInfoVO().setFileName(contractDO.getFileName()).setFileAddId(contractDO.getFileAddId()).setPdfFileId(contractDO.getPdfFileId()).setTemplateId(contractDO.getTemplateId()));
        contractCheckRespVO.setBuyPlanCode("无");
        return contractCheckRespVO;
    }

    //根据合同id 获取签署方名称
    private List<String> signatoryNameById(String id) {
        //获取签署方id集合
        List<String> ids = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, id).stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toList());
        List<Relative> relativeList = relativeMapper.selectBatchIds(ids);
        List<String> signatoryNameList = new ArrayList<>();
        //添加发起方公司名称
        ArrayList<Long> longs = new ArrayList<>();
        longs.add(Long.valueOf(contractMapper.selectById(id).getCreator()));
        List<UserCompanyInfoRespDTO> userCompanyInfoList = companyApi.getUserCompanyInfoList(longs);
        if (StringUtils.isNotBlank(userCompanyInfoList.get(0).getName())) {
            signatoryNameList.add(userCompanyInfoList.get(0).getName());
        } else {
            //发起方是个体，通过id 获取nickname
            List<AdminUserRespDTO> userListByDeptIds = adminUserApi.getUserListByDeptIds(userCompanyInfoList.stream().map(UserCompanyInfoRespDTO::getUserId).collect(Collectors.toList()));
            signatoryNameList.add(userListByDeptIds.get(0).getNickname());
        }
        for (Relative relative : relativeList) {
            if (!relative.getCompanyName().isEmpty()) {
                signatoryNameList.add(relative.getCompanyName());
            } else {
                signatoryNameList.add(relative.getName());
            }
        }
        return signatoryNameList;
    }
@Resource
private EcontractOrgApi econtractOrgApi;
    /**
     * 根据合同id 获取签署方名称
     *
     * @param id
     * @return
     */
    @Override
    public List<RelativeByUserRespVO> signatoryById(String id) {
        //创建最终结果集合
        List<RelativeByUserRespVO> listResult = new ArrayList<>();
        //获取发起方信息
        RelativeByUserRespVO relativeByUserRespVO = new RelativeByUserRespVO();
        ContractDO contractDO = contractMapper.selectById(id);
        List<RelativeByUserRespVO> relativeByUserRespVOListList = new ArrayList<>();
//        ContractOrderExtDO contractOrderExtDO = contractOrderExtMapper.selectById(id);
//
//        //采购单位信息
//        if(contractDO.getBuyerOrgId()!=null && contractDO.getSupplierId()!=null){
//            OrganizationDTO organization = organizationApi.getOrganization(contractDO.getBuyerOrgId());
//            Relative relative = relativeMapper.selectById(contractDO.getSupplierId());
//            if(ObjectUtil.isNotEmpty(organization)){
//                RelativeByUserRespVO respVO = new RelativeByUserRespVO();
//                respVO.setOrgLinkMan(organization.getLinkMan());
//                respVO.setOrgPhone(organization.getLinkPhone());
//                respVO.setOrgFax(organization.getLinkFax());
//                respVO.setOrgAddress(organization.getAddress());
//                respVO.setSupplierId(relative.getId()!=null?relative.getId():null);
//                respVO.setSupplierName(contractOrderExtDO.getSupplierProxy()!=null?contractOrderExtDO.getSupplierProxy():null);
//                respVO.setSupplierAccountName(relative.getBankAccountName()!=null?relative.getBankAccountName():null);
//                respVO.setBankName(relative.getBankName()!=null?relative.getBankName():null);
//                respVO.setSupplierTaxpayerNum(contractOrderExtDO.getSupplierCode()!=null?contractOrderExtDO.getSupplierCode():null);
//                respVO.setSupplierAddress(relative.getAddress()!=null?relative.getAddress():null);
//                respVO.setSupplierPhone(relative.getContactTel()!=null?relative.getContactTel():null);
//                respVO.setSupplierFax(relative.getFax()!=null?relative.getFax():null);
//                if(contractOrderExtDO.getForeignInvestmentType()!=null){
//                    respVO.setForeignInvestmentTypeName(ForeignInvestmentTypeEnum.getInstance(contractOrderExtDO.getForeignInvestmentType()).getInfo());
//                }
//                relativeByUserRespVOListList.add(respVO);
//            }
//            listResult.addAll(relativeByUserRespVOListList);
//            return listResult;
//        }
        String creator = contractDO.getCreator();
        UserCompanyDeptRespDTO userCompanyDeptRespDTO = adminUserApi.selectUserCompanyDept(Long.valueOf(creator));
        //部门id为空 表示此账号为个人账户
        relativeByUserRespVO.setFlag("send");
        if (ObjectUtil.isEmpty(userCompanyDeptRespDTO.getDeptId())) {
            relativeByUserRespVO.setEntityType(EntityTypeEnums.INDIVIDUAL.getCode());
            relativeByUserRespVO.setName(userCompanyDeptRespDTO.getNickname());
            relativeByUserRespVO.setTel(userCompanyDeptRespDTO.getMobile());
            relativeByUserRespVO.setIdCard(userCompanyDeptRespDTO.getIdCard());
            relativeByUserRespVO.setAccount(userCompanyDeptRespDTO.getUsername());
            relativeByUserRespVO.setContactId(userCompanyDeptRespDTO.getId());
            relativeByUserRespVO.setCompanyName(userCompanyDeptRespDTO.getCompanyInfo().getName());
        } else {
            //部门id不为空 表示此账号为企业或者单位账户
            if (ObjectUtil.isNotEmpty(userCompanyDeptRespDTO.getCompanyInfo())) {
                //此部门为顶级部门
                relativeByUserRespVO.setCompanyName(userCompanyDeptRespDTO.getCompanyInfo().getName());
                relativeByUserRespVO.setCreditCode(userCompanyDeptRespDTO.getCompanyInfo().getCreditCode());
                relativeByUserRespVO.setEntityType(userCompanyDeptRespDTO.getCompanyInfo().getMajor().toString());
                relativeByUserRespVO.setContactName(userCompanyDeptRespDTO.getNickname());
                relativeByUserRespVO.setContactTel(userCompanyDeptRespDTO.getMobile());
                relativeByUserRespVO.setContactAccount(userCompanyDeptRespDTO.getUsername());
                relativeByUserRespVO.setName(userCompanyDeptRespDTO.getNickname());
                relativeByUserRespVO.setAccount(userCompanyDeptRespDTO.getUsername());
                relativeByUserRespVO.setContactId(userCompanyDeptRespDTO.getId());
                OrganizationDTO organization = organizationApi.getOrganization(userCompanyDeptRespDTO.getOrgId());
                if (ObjectUtil.isNotEmpty(organization)) {
                    relativeByUserRespVO.setContactAccount(organization.getLinkMan() != null ? organization.getLinkMan() : null);
                    relativeByUserRespVO.setContactName(organization.getLinkMan() != null ? organization.getLinkMan() : null);
                    relativeByUserRespVO.setContactTel(organization.getLinkPhone() != null ? organization.getLinkPhone() : null);
                    EcontractOrgDTO econtractOrg = econtractOrgApi.getEcontractOrgById(userCompanyDeptRespDTO.getOrgId());
                    if (ObjectUtil.isNotEmpty(econtractOrg)){
                        relativeByUserRespVO.setBankAccountName(econtractOrg.getBankAccountName());
                        relativeByUserRespVO.setBankName(econtractOrg.getBankName());
                        relativeByUserRespVO.setBankAccount(econtractOrg.getBankAccount());
                    }
                }
            }
        }
        //设置主体类型
        relativeByUserRespVO.setEntityTypeName(setEntityType(relativeByUserRespVO.getEntityType()));
        relativeByUserRespVO.setType(1); //甲方
        listResult.add(relativeByUserRespVO);
        //获取签署方id集合
        List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, id);
        List<String> ids = signatoryRelDOS.stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toList());
        // 相应id对应的类型（乙方，丙方）
        Map<String, Integer> idTypeMap = signatoryRelDOS.stream().filter(item -> ObjectUtil.isNotEmpty(item.getType())).collect(Collectors.toMap(SignatoryRelDO::getSignatoryId, SignatoryRelDO::getType));
        //获取相对方对应信息
        List<Relative> relativeList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(ids)) {
            relativeList = relativeMapper.selectBatchIds(ids);
        }
        //如果本地没维护该相对方
        if (CollectionUtil.isEmpty(relativeList)) {
            /*relativeByUserRespVOListList.add(new RelativeByUserRespVO()
                    .setEntityTypeName(COMPANY.getCode())
                    .setFlag("relative")
                    .setCompanyName(contractDO.getPartBName())
            );
            listResult.addAll(relativeByUserRespVOListList);*/
            return listResult;
        }
        relativeByUserRespVOListList = RelativeConverter.INSTANCE.convert2List(relativeList);
        for (RelativeByUserRespVO respVO : relativeByUserRespVOListList) {
            respVO.setEntityTypeName(setEntityType(respVO.getEntityType()));
            respVO.setFlag("relative");
            respVO.setAccount(respVO.getCode());
            // respVO.setName(respVO.getContactName());
            respVO.setContactId(respVO.getContactId());
            respVO.setType(idTypeMap.get(respVO.getId()));
        }
        listResult.addAll(relativeByUserRespVOListList);
        String signOrder = contractDO.getSignOrder();
        if (StringUtils.isNotEmpty(signOrder)) {
            String[] s = signOrder.split("_");
            Map<Long, Integer> sortMap = new HashMap();
            for (int i = 0; i < s.length; i++) {
                sortMap.put(Long.valueOf(s[i]), i + 1);
            }
            for (RelativeByUserRespVO byUserRespVO : listResult) {
                Integer integer = sortMap.get(byUserRespVO.getVirtualId() == null ? byUserRespVO.getContactId() : byUserRespVO.getVirtualId());
                byUserRespVO.setSort(integer == null ? 0 : integer);
            }
            listResult.sort((o1, o2) -> o1.getSort() - o2.getSort());
        }
        return listResult;
    }

    private String setEntityType(String entityType) {
        String entityTypeName = null;
        //个人
        if (EntityTypeEnums.INDIVIDUAL.getCode().equals(entityType)) {
            entityTypeName = EntityTypeEnums.INDIVIDUAL.getInfo();
        }
        //单位
        if (EntityTypeEnums.ORGANIZATION.getCode().equals(entityType)) {
            entityTypeName = EntityTypeEnums.ORGANIZATION.getInfo();
        }
        //企业
        if (COMPANY.getCode().equals(entityType)) {
            entityTypeName = COMPANY.getInfo();
        }
        return entityTypeName;
    }

    /**
     * 修改合同
     *
     * @param contractUpdateReqVO
     */
    @Override
    public void update(ContractUpdateReqVO contractUpdateReqVO) throws Exception {
        //判断库里是否有相应的合同附件信息
        List<AttachmentRelDO> attachmentRelDOList = attachmentRelMapper.selectList(AttachmentRelDO::getContractId, contractUpdateReqVO.getId());
        if (!attachmentRelDOList.isEmpty()) {
            for (AttachmentRelDO attachmentRelDO : attachmentRelDOList) {
                fileApi.deleteFile(attachmentRelDO.getAttachmentAddId());
            }
            attachmentRelMapper.deleteBatchIds(attachmentRelDOList.stream().map(AttachmentRelDO::getId).collect(Collectors.toList()));
        }

        //上传主文件
        String source = DateUtil.today() + "/" + System.currentTimeMillis() + "-" + contractUpdateReqVO.getFileName();
//        byte[] decodedBytes = Base64.getDecoder().decode(contractUpdateReqVO.getFile());
        //上传主文件并保存地址id
//        contractUpdateReqVO.setFileAddId(fileApi.uploadFile(contractUpdateReqVO.getFileName(), source,decodedBytes));

        if (ObjectUtils.isNotEmpty(contractUpdateReqVO.getAttachmentList())) {
            //循环取出附件
            for (AttachmentRelCreateReqVO attachmentList : contractUpdateReqVO.getAttachmentList()) {
                //创建上传附件路径
//                String attachmentSource = DateUtil.today() + "-附件" + "/" + System.currentTimeMillis() + "-" + contractUpdateReqVO.getFileName();
//                byte[] attachmentBytes = Base64.getDecoder().decode(attachmentList.getAttachmentFile());
                //保存附件信息
                AttachmentRelDO ecmsAttachmentRelDO = new AttachmentRelDO();
                ecmsAttachmentRelDO.setAttachmentName(attachmentList.getAttachmentName());
                //上传附件并保存地址id
//                ecmsAttachmentRelDO.setAttachmentAddId(fileApi.uploadFile(attachmentList.getAttachmentName(),attachmentSource,attachmentBytes));
                ecmsAttachmentRelDO.setAttachmentType(attachmentList.getAttachmentType());
                attachmentRelMapper.insert(ecmsAttachmentRelDO);
            }
        }

        ContractDO contractDO = ContractConverter.INSTANCE.convert(contractUpdateReqVO);
        //修改合同信息
        contractMapper.updateById(contractDO);

        //删除旧签署方
        signatoryRelMapper.delete(new LambdaQueryWrapper<SignatoryRelDO>().eq(SignatoryRelDO::getContractId, contractUpdateReqVO.getId()));
        //添加签署方关系
        for (SignatoryRelReqVO signatoryRelReqVO : contractUpdateReqVO.getSignatoryList()) {
            SignatoryRelDO signatoryRelDO = SignatoryRelConverter.INSTANCE.convert(signatoryRelReqVO);
            signatoryRelDO.setContractId(contractDO.getId());
            signatoryRelMapper.insert(signatoryRelDO);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByIdList(ContractIdListVO idListVO) throws Exception {
        updateOrderStatus(idListVO.getIds());
        List<ContractDO> contractDOList = contractMapper.selectBatchIds(idListVO.getIds());
        contractMapper.deleteBatchIds(idListVO.getIds());
        //删除支付计划
        paymentScheduleMapper.delete(new LambdaQueryWrapperX<PaymentScheduleDO>().in(PaymentScheduleDO::getContractId, idListVO.getIds()));
        for (ContractDO contractDO : contractDOList) {
            //删除改前文件以及信息
            try {
                fileApi.deleteFile(contractDO.getFileAddId());
            } catch (Exception e) {
            }
            //判断库里是否有相应的合同附件信息
            List<AttachmentRelDO> attachmentRelDOList = attachmentRelMapper.selectList(AttachmentRelDO::getContractId, contractDO.getId());
            if (CollectionUtil.isNotEmpty(attachmentRelDOList)) {
                for (AttachmentRelDO attachmentRelDO : attachmentRelDOList) {
                    try {
                        fileApi.deleteFile(attachmentRelDO.getAttachmentAddId());
                    } catch (Exception e) {
                    }
                }
                attachmentRelMapper.deleteBatchIds(attachmentRelDOList.stream().map(AttachmentRelDO::getId).collect(Collectors.toList()));
            }
            //通过草拟方式创建
            if (ObjectUtils.isNotEmpty(contractDO.getDocument()) && 0 == contractDO.getDocument()) {
                //删除签署方信息
                signatoryRelMapper.delete(new LambdaQueryWrapper<SignatoryRelDO>().eq(SignatoryRelDO::getContractId, contractDO.getId()));
            } else {
                //通过上传方式创建
                documentRelMapper.delete(new LambdaQueryWrapper<DocumentRelDO>().eq(DocumentRelDO::getContractId, contractDO.getId()));
            }
        }
    }

    /**
     * 修改订单状态
     */
    private void updateOrderStatus(List<String> ids) {
        if (CollectionUtil.isNotEmpty(ids)) {
            List<ContractDO> contractDOS = contractMapper.selectList(new LambdaQueryWrapper<ContractDO>().in(ContractDO::getId, ids).select(ContractDO::getId, ContractDO::getStatus, ContractDO::getUpload, ContractDO::getOrderGuid));
            List<ContractDO> govContractDOS = new ArrayList<>();
            for (ContractDO contractDO : contractDOS) {
                if (ContractUploadTypeEnums.ORDER_DRAFT.getCode().equals(contractDO.getUpload()) || ContractUploadTypeEnums.THIRD_PARTY.getCode().equals(contractDO.getUpload())) {
                    govContractDOS.add(contractDO);
                }
            }
            if (govContractDOS.size() == 0) {
                return;
            }
            Set<String> orderIds = govContractDOS.stream().map(ContractDO::getOrderGuid).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
            List<DraftOrderInfoDO> draftOrderInfoDOS = orderIds.size() == 0 ? null : gpMallOrderMapper.selectList(new LambdaQueryWrapperX<DraftOrderInfoDO>().in(DraftOrderInfoDO::getOrderGuid, orderIds));
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
            if (CollectionUtil.isNotEmpty(draftOrderInfoDOS)) {
                for (DraftOrderInfoDO draftOrderInfoDO : draftOrderInfoDOS) {
                    //校验本地状态，如果已改，则不执行修改、同步电子合同
                    if (ObjectUtil.isNotEmpty(draftOrderInfoDO) && ObjectUtil.isNotEmpty(draftOrderInfoDO.getStatus())
                            && !GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode().equals(draftOrderInfoDO.getStatus())) {
                        draftOrderInfoDO.setStatus(GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode());
                        try {
                            DraftOrderInfo draftOrderInfo = new DraftOrderInfo();
                            draftOrderInfo.setOrderGuid(draftOrderInfoDO.getOrderGuid()).setStatus(GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode());
                            String result = contractProcessApi.updateOrderStatus(jsonObject.getString("access_token"), draftOrderInfo);
                            JSONObject resultJson = JSONObject.parseObject(result);
                            if (!"0".equals(resultJson.getString("code"))) {
                                throw new RuntimeException(result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("推送电子合同更新订单状态失败", e);
                        }
                    }
                }
                gpMallOrderMapper.updateBatch(draftOrderInfoDOS);
            }
        }
    }

    @Override
    public void downFile(HttpServletResponse response, String host, String filePath) {
        int port = 22;
        String username = "root";
        String password = "YaoAn@cn1";
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        File localfile = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            Sftp sftp = new Sftp(host, port, username, password);
            String path = "/file" + new Random().nextInt(10) + "/";
            localfile = new File(path);
            if (!localfile.exists()) {
                localfile.mkdirs();
            }
            sftp.download(filePath, localfile);
            // 将文件内容写入响应输出流
            File file = new File(path + fileName);
            inputStream = new FileInputStream(file);
            outputStream = response.getOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                FileUtil.del(localfile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 查询合同状态对应数量
     *
     * @return
     */
    @Override
    public List<StatusCountVO> statusCount() {
        List<StatusCountVO> resultList = new ArrayList<>();
        //待发送及其数量
        PageResult<ContractPageRespVO> contractDOPageResult1 = this.getSentPage(new ContractPageReqVO().setStatusList(CollectionUtil.newArrayList(ContractStatusEnums.TO_BE_SENT.getCode(), ContractStatusEnums.BE_SENT_BACK.getCode())));
        resultList.add(buildStatusCountVO("待发送", contractDOPageResult1.getTotal(), 0));
        //已发送
        PageResult<ContractPageRespVO> contractDOPageResult2 = this.getSendPage(new ContractPageReqVO());
        resultList.add(buildStatusCountVO("已发送", contractDOPageResult2.getTotal(), 1));
        //待确认
        PageResult<ContractPageRespVO> contractDOPageResult3 = this.getConfirmPage(new ContractPageReqVO());
        resultList.add(buildStatusCountVO("待确认", contractDOPageResult3.getTotal(), 2));
        //待签署
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        AdminUserRespDTO user = adminUserApi.getUser(loginUser.getId());
        PageResult<ContractDO> contractDOPageResult4 = selectWaitSignPage(new ContractPageReqVO().setStatusList(CollectionUtil.newArrayList(ContractStatusEnums.TO_BE_SIGNED.getCode())), user);
        // PageResult<ContractPageRespVO> contractDOPageResult4 = this.getSignUnfinishedPage(new ContractPageReqVO().setStatusList(CollectionUtil.newArrayList(ContractStatusEnums.TO_BE_SIGNED.getCode())));
        resultList.add(buildStatusCountVO("待签署", contractDOPageResult4.getTotal(), 4));
        //签署完成
        PageResult<ContractPageRespVO> contractDOPageResult6 = this.getSignFinishPage(new ContractPageReqVO().setStatusList(CollectionUtil.newArrayList(ContractStatusEnums.SIGN_COMPLETED.getCode())));
        resultList.add(buildStatusCountVO("签署完成", contractDOPageResult6.getTotal(), 5));
        //逾期未签署
        PageResult<ContractPageRespVO> contractDOPageResult7 = this.getSignOverduePage(new ContractPageReqVO().setStatusList(CollectionUtil.newArrayList(ContractStatusEnums.SIGN_OVERDUE.getCode())));
        resultList.add(buildStatusCountVO("逾期未签署", contractDOPageResult7.getTotal(), 6));
        //待送审
        PageResult<ContractPageRespVO> contractDOPageResult8 = this.getToCheckPage(new ContractPageReqVO());
        resultList.add(buildStatusCountVO("待送审", contractDOPageResult8.getTotal(), 11));
        return resultList;
    }

    // 构建StatusCountVO对象
    private StatusCountVO buildStatusCountVO(String name, long count, int identifier) {
        StatusCountVO statusCountVO = new StatusCountVO();
        statusCountVO.setName(name);
        statusCountVO.setCount(count);
        statusCountVO.setIdentifier(identifier);
        return statusCountVO;
    }

    /**
     * 合同签约类型统计
     *
     * @return
     */
    @Override
    public List<TypeStatisticsVO> typeStatistics() {
        List<TypeStatisticsVO> resultList = new ArrayList<>();
        //获取当前用户作为签署方的合同id集合
        ArrayList<String> contractIdList = getContractIdList();
        //添加创建方为当前用户的合同id集合
        QueryWrapper<ContractDO> queryWrapper = new QueryWrapper<ContractDO>().select("id").eq("creator", WebFrameworkUtils.getLoginUserId());
        contractIdList.addAll(contractMapper.selectList(queryWrapper).stream().map(ContractDO::getId).collect(Collectors.toList()));
        if (CollectionUtil.isEmpty(contractIdList)) {
            return resultList;
        }
        //查询当前用户所有签署完成的合同

        List<ContractDO> contractDOList = contractMapper.selectTypeStatistics(contractIdList);
        List<String> typeList = contractDOList.stream().map(contractDO -> contractDO.getContractType()).collect(Collectors.toList());
        Map<String, Long> map = new HashMap<>();
        for (String type : typeList) {
            if (map.containsKey(type)) {
                map.put(type, map.get(type).longValue() + 1);
            } else {
                map.put(type, new Long(1));
            }
        }
        for (Map.Entry<String, Long> entry : map.entrySet()) {
            //查询类型对应的名字
            TypeStatisticsVO typeStatisticsVO = new TypeStatisticsVO();
            typeStatisticsVO.setName(contractTypeMapper.selectById(entry.getKey()).getName());
            typeStatisticsVO.setCount(entry.getValue());
            resultList.add(typeStatisticsVO);
        }
        return resultList;
    }

    /**
     * 提交归档
     *
     * @param documentRelReqVo
     * @return
     */
    @Override
    public String documentCreate(DocumentRelReqVo documentRelReqVo) {
        if (ObjectUtil.isNotEmpty(documentRelReqVo.getContractId())) {
            ContractDO contractDO = contractMapper.selectById(documentRelReqVo.getContractId());
            if (ObjectUtil.isNotNull(contractDO)) {
                if (IfNumEnums.YES.getCode().equals(contractDO.getIsLocked())) {
                    throw exception(ErrorCodeConstants.CONTRACT_IS_LOCKED, documentRelReqVo.getContractId());
                }
            }
        }
        Long aLong = documentRelMapper.selectCount(DocumentRelDO::getContractId, documentRelReqVo.getContractId());
        if (aLong != 0) {
            throw exception(ErrorCodeConstants.CONTRACT_DOCUMENT_EXISTS);
        }
        ContractDO contractDO = new ContractDO();
        contractDO.setId(documentRelReqVo.getContractId());
        contractDO.setDocument(1);
        //设置归档人-归档时间
        Long loginUserId = WebFrameworkUtils.getLoginUserId();
        AdminUserRespDTO user = adminUserApi.getUser(loginUserId);
        contractDO.setArchiveUser(user.getNickname());
        contractDO.setArchiveTime(LocalDateTime.now());
        contractMapper.updateById(contractDO);
        DocumentRelDO documentRelDO = DocumentRelConverter.INSTANCE.convert(documentRelReqVo);
        documentRelMapper.insert(documentRelDO);
        return contractDO.getId();
    }

    /**
     * 获取当前用户作为签署方的合同id集合
     *
     * @return
     */
    @NotNull
    private ArrayList<String> getContractIdList() {
        //查询当前登录用户的签署方id
        if (ObjectUtils.isEmpty(relativeMapper.selectOne(Relative::getContactId, WebFrameworkUtils.getLoginUserId()))) {
            return new ArrayList<>();
        }
        String id = relativeMapper.selectOne(Relative::getContactId, WebFrameworkUtils.getLoginUserId()).getId();
        //查询对应的签署方合同
        List<SignatoryRelDO> signatoryRelDOList = signatoryRelMapper.selectList(SignatoryRelDO::getSignatoryId, id);
        ArrayList<String> contractIdList = new ArrayList<>();
        signatoryRelDOList.forEach(signatoryRelDO -> {
            contractIdList.add(signatoryRelDO.getContractId());
        });
        return contractIdList;
    }

    private Boolean nameExist(String id, String name) {
        return contractMapper.nameExist(id, name);
    }

//    public static void main(String[] args) throws IOException {
//        File srcFile = new File("E:/转换套件产品接口文档.pdf");
//        FileOutputStream out = new FileOutputStream("E:/转换套件产品接口文档后后后.pdf");
//        final HTTPAgent agent = new HTTPAgent("http://152.136.27.193:9000/v1/");
//        String json = "{\n" +
//                "  \"srcFile\": \"input.pdf\",\n" +
//                "  \"out\": \"output.pdf\",\n" +
//                "  \"textinfo\": {\n" +
//                "    \"text\": \"  仅 供 借 阅  \",\n" +
//                "    \"font\": \" 宋 体 \",\n" +
//                "    \"fontSize\": 14,\n" +
//                "    \"color\": \"#808080\",\n" +
//                "    \"angle\": 45,\n" +
//                "    \"xAlign\": \"Center\",\n" +
//                "    \"yAlign\": \"Middle\",\n" +
//                "    \"tiled\": true\n" +
//                "  },\n" +
//                "  \"markPosition\": {\n" +
//                "    \"x\": 10,\n" +
//                "    \"y\": 10,\n" +
//                "    \"width\": 120,\n" +
//                "    \"height\": 120,\n" +
//                "    \"index\": \"all\"\n" +
//                "  },\n" +
//                "  \"printable\": true,\n" +
//                "  \"visible\": true\n" +
//                "}";
//        try {
//            //旋转角度是 45 的倍数。颜色后 2 位可调整文字透明度 00-FF。
//            JSONObject jsonObject = JSONObject.parseObject(json);
//            JSONObject textinfoJson = jsonObject.getJSONObject("textinfo");
//            String text = textinfoJson.getString("text");
//            String font = textinfoJson.getString("font");
//            int fontSize = Integer.valueOf(textinfoJson.getString("fontSize"));
//            String color = textinfoJson.getString("color");
//            int angle = Integer.valueOf(textinfoJson.getString("angle"));
//            String xAlign = textinfoJson.getString("xAlign");
//            String yAlign = textinfoJson.getString("yAlign");
//            boolean tiled = textinfoJson.getBoolean("tiled");
//            TextInfo textinfo = new TextInfo(text, font, fontSize, color, angle, Const.XAlign.valueOf(xAlign), Const.YAlign.valueOf(yAlign));
//            textinfo.setTiled(tiled);
//            JSONObject markPositionJson = jsonObject.getJSONObject("markPosition");
//            int x = Integer.valueOf(markPositionJson.getString("x"));
//            int y = Integer.valueOf(markPositionJson.getString("y"));
//            int width = Integer.valueOf(markPositionJson.getString("width"));
//            int height = Integer.valueOf(markPositionJson.getString("height"));
//            String index = markPositionJson.getString("index");
//            MarkPosition mk = new MarkPosition(x, y, width, height, MarkPosition.INDEX_ALL);
//            boolean printable = jsonObject.getBoolean("printable");
//            boolean visible = jsonObject.getBoolean("visible");
//
//            YhAgentUtil.convertAndAddTextWatermarkToPDF(srcFile, out, textinfo, mk, printable, visible);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            agent.close();
//        }
//    }

    private Boolean codeExist(String id, String code) {
        return contractMapper.codeExist(id, code);
    }

    /**
     * 获取下一个执行人信息
     *
     * @param contractId
     */
    @DataPermission(enable = false)
    @Override
    public RelativeByUserRespVO next(String contractId) {
        //获取执行人排序集合
        List<Long> result = new ArrayList<>();

//        Long loginUserId = Long.valueOf(contractMapper.selectById(contractId).getCreator());
        //（相对方逻辑）免租户
        AtomicReference<List<Relative>> atomic = new AtomicReference<>();
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                Long loginUserId = Long.valueOf(contractMapper.selectById(contractId).getCreator());

                result.add(loginUserId);
                List<SignatoryRelDO> signatoryRels = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractId);
                List<String> signIds = signatoryRels.stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toList());
                List<Relative> relatives = relativeMapper.selectBatchIds(signIds);


                List<Long> userIds = relatives.stream().map(Relative::getContactId).collect(Collectors.toList());
                result.addAll(userIds);
                List<UserCompanyInfoRespDTO> userCompanyInfoList = companyApi.getUserCompanyInfoList(result);
                List<Long> signUserIds = userCompanyInfoList.stream().map(UserCompanyInfoRespDTO::getUserId).collect(Collectors.toList());
                result.addAll(signUserIds);
                //获取下一个执行人
//        int i = 0;
//        for (Long aLong : result) {
//            if (i == 1) {
//                UserCompanyDeptRespDTO userCompanyDeptRespDTO = adminUserApi.selectUserCompanyDept(aLong);
//                RelativeByUserRespVO relativeByUserRespVO = new RelativeByUserRespVO();
//                //身份证号
//                relativeByUserRespVO.setIdCard(userCompanyDeptRespDTO.getIdCard());
//                //单位/企业-名称
//                relativeByUserRespVO.setCompanyName(userCompanyDeptRespDTO.getCompanyInfo().getName());
//                //统一社会信用代码
//                relativeByUserRespVO.setCreditCode(userCompanyDeptRespDTO.getCompanyInfo().getCreditCode());
//                //个人手机号
//                relativeByUserRespVO.setTel(userCompanyDeptRespDTO.getMobile());
//                //单位/企业-手机号
//                relativeByUserRespVO.setContactTel(userCompanyDeptRespDTO.getMobile());
//                //姓名
//                relativeByUserRespVO.setName(userCompanyDeptRespDTO.getNickname());
//                return relativeByUserRespVO;
//            }
//            if (WebFrameworkUtils.getLoginUserId().equals(aLong)) {
//                i++;
//            }
//        }

                atomic.set(relatives);
            });
        });
        List<Relative> relatives = atomic.get();
        if (relatives.size() > 0) {
            Relative relative = relatives.get(0);
            //下一执行人信息，即相对方信息，相对方表中存在上述信息，无需去公司表查询
            RelativeByUserRespVO relativeByUserRespVO = new RelativeByUserRespVO();
            //身份证号
            relativeByUserRespVO.setIdCard(relative.getIdCard());
            //单位/企业-名称
            relativeByUserRespVO.setCompanyName(relative.getName());
            if (relative.getEntityType() != null) {
                EntityTypeEnums entityTypeEnums = EntityTypeEnums.getInstance(relative.getEntityType());
                if (entityTypeEnums != null) {
                    relativeByUserRespVO.setEntityTypeName(EntityTypeEnums.getInstance(relative.getEntityType()).getInfo());
                }
                relativeByUserRespVO.setEntityType(relative.getEntityType());
            }

            //统一社会信用代码
            relativeByUserRespVO.setCreditCode(relative.getCreditCode());
            //个人手机号
            relativeByUserRespVO.setTel(relative.getContactTel());
            //单位/企业-手机号
            relativeByUserRespVO.setContactTel(relative.getContactTel());
            //姓名
            relativeByUserRespVO.setName(relative.getContactName());
            return relativeByUserRespVO;
        }

        return new RelativeByUserRespVO();
    }

    @DataPermission(enable = false)
    @Override
    public List<RelativeByUserRespVO> nextAll(String contractId) {
        //获取执行人排序集合
        List<Long> result = new ArrayList<>();

        //（相对方逻辑）免租户
        AtomicReference<List<Relative>> atomic = new AtomicReference<>();
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                Long loginUserId = Long.valueOf(contractMapper.selectById(contractId).getCreator());

                result.add(loginUserId);
                List<SignatoryRelDO> signatoryRels = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractId);
                List<String> signIds = signatoryRels.stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toList());
                List<Relative> relatives = relativeMapper.selectBatchIds(signIds);


                List<Long> userIds = relatives.stream().map(Relative::getContactId).collect(Collectors.toList());
                result.addAll(userIds);
                List<UserCompanyInfoRespDTO> userCompanyInfoList = companyApi.getUserCompanyInfoList(result);
                List<Long> signUserIds = userCompanyInfoList.stream().map(UserCompanyInfoRespDTO::getUserId).collect(Collectors.toList());
                result.addAll(signUserIds);
                atomic.set(relatives);
            });
        });
        List<Relative> relatives = atomic.get();
        if (relatives.size() > 0) {
            List<RelativeByUserRespVO> retList = new ArrayList<>();
            for (Relative relative : relatives) {
                //下一执行人信息，即相对方信息，相对方表中存在上述信息，无需去公司表查询
                RelativeByUserRespVO relativeByUserRespVO = new RelativeByUserRespVO();
                //身份证号
                relativeByUserRespVO.setIdCard(relative.getIdCard());
                //单位/企业-名称
                relativeByUserRespVO.setCompanyName(relative.getName());
                if (relative.getEntityType() != null) {
                    EntityTypeEnums entityTypeEnums = EntityTypeEnums.getInstance(relative.getEntityType());
                    if (entityTypeEnums != null) {
                        relativeByUserRespVO.setEntityTypeName(EntityTypeEnums.getInstance(relative.getEntityType()).getInfo());
                    }
                    relativeByUserRespVO.setEntityType(relative.getEntityType());
                }

                //统一社会信用代码
                relativeByUserRespVO.setCreditCode(relative.getCreditCode());
                //个人手机号
                relativeByUserRespVO.setTel(relative.getContactTel());
                //单位/企业-手机号
                relativeByUserRespVO.setContactTel(relative.getContactTel());
                //姓名
                relativeByUserRespVO.setName(relative.getContactName());
                retList.add(relativeByUserRespVO);
            }

            return retList;
        }

        return new ArrayList<>();
    }

    @DataPermission(enable = true)
    @Override
    public List<Object> recentUse(ReqVO reqVO) {
        List<Object> list = new ArrayList<>();
        //2.最近使用模板
        List<Model> models1 = modelMapper.searchTextInfo(reqVO.getSearchText());
        if (CollectionUtil.isEmpty(models1)) {
            return Collections.emptyList();
        }
        List<RecentUseModelVO> recentUseModelInfo = ModelConverter.INSTANCE.ModelToVo(models1);
        List<String> contractTypeIds = recentUseModelInfo.stream().map(RecentUseModelVO::getContractType).collect(Collectors.toList());
        recentUseModelInfo = setModelParam(contractTypeIds, recentUseModelInfo);
        if (StringUtils.isNotBlank(reqVO.getSearchText())) {
            list.addAll(recentUseModelInfo);
        } else {
            //2.设置文件信息
            //2.1获取最近使用的6条合同的文件id
            List<Map<String, Object>> fileinfo = new ArrayList<>();
            List<ContractDO> contractDOS2 = contractMapper.selectFileContract();
            List<Long> fileIds = contractDOS2.stream().map(ContractDO::getFileAddId).collect(Collectors.toList());
            Map<Long, ContractDO> contractDOMap = CollectionUtils.convertMap(contractDOS2, ContractDO::getFileAddId);
            if (CollectionUtil.isNotEmpty(fileIds)) {
                List<FileDTO> fileDTOS = fileApi.selectBatchIds(fileIds);
                for (FileDTO fileDTO : fileDTOS) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("fileName", fileDTO.getName());
                    map.put("useTime", contractDOMap.get(fileDTO.getId()) == null ? null : contractDOMap.get(fileDTO.getId()).getCreateTime());
                    map.put("contractSource", "文件");
                    map.put("fileId", fileDTO.getId());
                    fileinfo.add(map);
                }
            }
            if (recentUseModelInfo.size() <= 3 && fileinfo.size() <= 3) {
                list.addAll(recentUseModelInfo);
                list.addAll(fileinfo);
            } else if (recentUseModelInfo.size() > 3 && fileinfo.size() > 3) {
                list.addAll(recentUseModelInfo.stream().limit(3).collect(Collectors.toList()));
                list.addAll(fileinfo.stream().limit(3).collect(Collectors.toList()));
            } else if (recentUseModelInfo.size() >= 3 && fileinfo.size() < 3) {
                list.addAll(fileinfo);
                if ((recentUseModelInfo.size() + fileinfo.size()) > 6) {
                    for (int i = 0; i < 6 - fileinfo.size(); i++) {
                        list.add(recentUseModelInfo.get(i));
                    }
                } else {
                    for (int i = 0; i < recentUseModelInfo.size(); i++) {
                        list.add(recentUseModelInfo.get(i));
                    }
                }
            } else if (fileinfo.size() >= 3 && recentUseModelInfo.size() < 3) {
                list.addAll(recentUseModelInfo);
                if ((recentUseModelInfo.size() + fileinfo.size()) > 6) {
                    for (int i = 0; i < 6 - recentUseModelInfo.size(); i++) {
                        list.add(fileinfo.get(i));
                    }
                } else {
                    for (int i = 0; i < fileinfo.size(); i++) {
                        list.add(fileinfo.get(i));
                    }
                }
            }
        }
        return list;
    }

    @DataPermission(enable = true)
    @Override
    public RecentUseRespVO SearchTextInfo(ReqVO reqVO) {
        RecentUseRespVO recentUseRespVO = new RecentUseRespVO();
        //1.我的收藏
        List<MyCollectModel> myCollectModels = myCollectModelMapper.selectList(new LambdaQueryWrapperX<MyCollectModel>().likeIfPresent(MyCollectModel::getName, reqVO.getSearchText()));
        List<RecentUseModelVO> recentUseModelInfo = ModelConverter.INSTANCE.ModelToVo2(myCollectModels);
        List<String> contractTypeIds = recentUseModelInfo.stream().map(RecentUseModelVO::getContractType).collect(Collectors.toList());
        recentUseModelInfo = setModelParam(contractTypeIds, recentUseModelInfo);
        recentUseRespVO.setMyCollectModelInfo(recentUseModelInfo);
        //2.最近使用模板
        List<Model> models1 = modelMapper.searchTextInfo(reqVO.getSearchText());
        List<RecentUseModelVO> recentUseModelInfo2 = ModelConverter.INSTANCE.ModelToVo(models1);
        List<String> contractTypeIds2 = recentUseModelInfo2.stream().map(RecentUseModelVO::getContractType).collect(Collectors.toList());
        recentUseModelInfo2 = setModelParam(contractTypeIds2, recentUseModelInfo2);
        recentUseRespVO.setRecentUseModelInfo(recentUseModelInfo2);
        return recentUseRespVO;
    }

    @DataPermission(enable = true)
    @Override
    public List<RecentUseModelVO> myCollectModel(ReqVO reqVO) {
        List<SimpleModel> simpleModelList = simpleModelMapper.selectList(new LambdaQueryWrapperX<SimpleModel>().likeIfPresent(SimpleModel::getName, reqVO.getSearchText()).eq(SimpleModel::getCollect, 1).orderByDesc(SimpleModel::getUpdateTime).last("LIMIT 6"));
        List<RecentUseModelVO> result = ModelConverter.INSTANCE.convertToRecentVO(simpleModelList);
        List<String> contractTypeIds = result.stream().map(RecentUseModelVO::getContractType).collect(Collectors.toList());
        //设置模板参数信息
        List<RecentUseModelVO> recentUseModelVOS = setModelParam(contractTypeIds, result);
        if (recentUseModelVOS.size() > 0) {
            return recentUseModelVOS;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 设置合同类型和模板
     */
    private List<RecentUseModelVO> setModelParam(List<String> contractTypeIds, List<RecentUseModelVO> recentUseModelInfo) {
        if (CollectionUtil.isNotEmpty(recentUseModelInfo)) {
            List<ContractType> contractTypes = contractTypeMapper.selectBatchIds(contractTypeIds);
            Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypes, ContractType::getId);
            //1.3model转换成RecentUseModelVO
            List<String> collect = recentUseModelInfo.stream().map(RecentUseModelVO::getId).collect(Collectors.toList());
            List<Map<String, Object>> maps = contractMapper.selectMaps(new QueryWrapper<ContractDO>().select("COUNT(template_id) AS count,template_id AS templateId").in("template_id", collect).isNotNull("template_id").groupBy("template_id"));
            Map<Object, Map<String, Object>> modelMap = CollectionUtils.convertMap(maps, (map) -> map.get("templateId"));
            for (RecentUseModelVO recentUseModelVO : recentUseModelInfo) {
                //1.4设置使用次数
                if (ObjectUtil.isEmpty(modelMap) || ObjectUtil.isEmpty(modelMap.get(recentUseModelVO.getId()))) {
                    recentUseModelVO.setUseNum(0 + "次");
                } else {
                    recentUseModelVO.setUseNum(modelMap.get(recentUseModelVO.getId()).get("count") + "次");
                }
//               getUseCount(recentUseModelVO.getId(),recentUseModelVO);
                //1.5设置合同来源
                recentUseModelVO.setContractSource("模板");
                //1.6 设置合同类型
                recentUseModelVO.setContractTypeName(contractTypeMap.get(recentUseModelVO.getContractType()) == null ? null : contractTypeMap.get(recentUseModelVO.getContractType()).getName());
                //1.7 设置模板时效
                if (Integer.valueOf(1).equals(recentUseModelVO.getTimeEffectModel())) {
                    recentUseModelVO.setTimeEffectModelName(ModelTimeEffectEnums.getInstance(recentUseModelVO.getTimeEffectModel()).getInfo());
                } else {
                    recentUseModelVO.setTimeEffectModelName(recentUseModelVO.getEffectStartTime() + "----" + recentUseModelVO.getEffectEndTime());
                }
            }
        }
        return recentUseModelInfo;
    }

    /**
     * 查询备案列表
     *
     * @param contractPageReqVO
     * @return
     */
    @Override
    public PageResult<ContractPageRespVO> getFilingsList(ContractPageReqVO contractPageReqVO) {
        if (ObjectUtil.isEmpty(contractPageReqVO.getIsFilings())) {
            contractPageReqVO.setDocumentList(new ArrayList<>(Arrays.asList(0, 1, 2)));
        }
        // 查出全部类型
        if (CollectionUtil.isNotEmpty(contractPageReqVO.getContractTypes())) {
            List<String> types = new ArrayList();
            selectAllChildTypes(contractPageReqVO.getContractTypes(), types);
            contractPageReqVO.setContractTypes(types);
        }

        PageResult<ContractDO> contractDOPageResult = contractMapper.selectFilingsPage(contractPageReqVO);

        PageResult<ContractPageRespVO> result = getContractPageRespVOPageResult(contractDOPageResult);
        return result;
    }

    @Override
    public PageResult<LoanPageRespVO> getLoanPage(LoanPageReqVO loanPageReqVO) {
        PageResult<ContractDO> contractDOPageResult = contractMapper.selectLoanPage(loanPageReqVO);
        PageResult<LoanPageRespVO> loanPageRespVOPageResult = ContractConverter.INSTANCE.convertPageV1(contractDOPageResult);
        return loanPageRespVOPageResult;
    }


    @Override
    public Long addTextWatermarkToPDFCommon(PdfWatermarkReqVO reqVO) throws Exception {
        //转pdf
        String contractId = reqVO.getContractId();
        String watermarkId = reqVO.getWatermarkId();

        AtomicReference<ContractDO> contractDO = new AtomicReference<>(new ContractDO());
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                contractDO.set(contractMapper.selectById(contractId));

            });
        });
        if (ObjectUtil.isNotEmpty(contractDO)) {
            final HTTPAgent agent = new HTTPAgent("http://152.136.27.193:9000/v1/");
            String folderId = "TEMPLATE-" + IdUtil.fastSimpleUUID();
            String s = contractDO.get().getName();
            String localFolderPath = "/data/file/ready_to_up" + "/" + folderId;
//            String localFolderPath = "E:" + "/" + folderId;
            String outPath = localFolderPath + "/" + IdUtil.fastSimpleUUID();
            FileUtil.mkdir(localFolderPath);
            FileUtil.mkdir(outPath);
            ByteArrayInputStream byteArrayInputStream = IoUtil.toStream(fileApi.getFileContentById(contractDO.get().getPdfFileId()));
            FileUtil.writeFromStream(byteArrayInputStream, localFolderPath + "/" + s + ".pdf");
            File srcFile = new File(localFolderPath + "/" + s + ".pdf");
            FileOutputStream out = new FileOutputStream(outPath + "/" + s + ".pdf");
            if (ObjectUtil.isNotEmpty(watermarkId)) {
                WatermarkDO watermarkDO = watermarkMapper.selectById(watermarkId);
                if (ObjectUtil.isNotEmpty(watermarkDO)) {
                    if (Integer.valueOf(0).equals(watermarkDO.getType())) {
                        textWithWatermark(watermarkDO, srcFile, out, agent);
                    }
                    if (Integer.valueOf(1).equals(watermarkDO.getType())) {
                        imageWithWatermark(srcFile, watermarkDO, out, agent);
                    }
                }
            } else {
                try {
                    //旋转角度是 45 的倍数。颜色后 2 位可调整文字透明度 00-FF。
                    TextInfo textinfo = new TextInfo("  仅 供 借 阅  ", " 宋 体 ", 14, "#808080", 45, Const.XAlign.Center, Const.YAlign.Middle);
                    textinfo.setTiled(true);
                    MarkPosition mk = new MarkPosition(10, 10, 120, 120, MarkPosition.INDEX_ALL);
                    boolean printable = true;
                    boolean visible = true;
                    YhAgentUtil.convertAndAddTextWatermarkToPDF(srcFile, out, textinfo, mk, printable, visible);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    agent.close();
                }
            }
            Path path = Paths.get(outPath + "/" + s + ".pdf");
            Long borrowFileId = fileApi.uploadFile(s + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));
            contractDO.get().setBorrowFileId(borrowFileId);
            contractMapper.updateById(contractDO.get());
            FileUtil.del(localFolderPath);
            return borrowFileId;
        } else {
            throw exception(DIY_ERROR, contractId + "该合同不存在");
        }
    }

    @Override
    public OfflineFileRespVO getOfflineFile(String contractId) {
        List<OfflineFileDO> offlineFileDOList = offlineFileMapper.selectList(
                new LambdaQueryWrapperX<OfflineFileDO>().eq(OfflineFileDO::getBusinessId, contractId).orderByDesc(OfflineFileDO::getCreateTime));
        if (CollectionUtil.isEmpty(offlineFileDOList)) {
            return null;
        }
        OfflineFileDO offlineFileDO = offlineFileDOList.get(0);
        if (ObjectUtil.isEmpty(offlineFileDO)) {
            log.error("该合同没有线下文件，请先上传线下文件后再进行操作。");
            throw exception(DIY_ERROR, "请先上传线下文件后再进行操作。");
        }
        FileDTO fileDTO = fileApi.selectById(offlineFileDO.getFileId());
        if (ObjectUtil.isNotNull(fileDTO)) {
            return new OfflineFileRespVO().setFileId(fileDTO.getId()).setFileName(fileDTO.getName()).setFilePath(fileDTO.getPath()).setUrl(fileDTO.getUrl());
        }
        return null;
    }

    @Override
    public ContractPageRespVO getContractInfoById(String contractId) {
        ContractDO contractDOResult = contractMapper.selectById(contractId);
        ContractPageRespVO result = ContractConverter.INSTANCE.convertV3(contractDOResult);
        PageResult<ContractDO> contractDOPageResult = new PageResult<>();
        contractDOPageResult.setList(Arrays.asList(contractDOResult));
        PageResult<ContractPageRespVO> contractPageRespVOPageResult = getContractPageRespVOPageResult(contractDOPageResult);
        List<ContractPageRespVO> list = contractPageRespVOPageResult.getList();
        if (CollectionUtil.isNotEmpty(list)) {
            result = list.get(0);
        }
        return result;
    }

    @Override
    public PageResult<TransferContractAllRespVO> queryTransferContractAll(TransferContractAllReqVO reqVO) {
        //1.从redis中获取企业的ID
        String acheKey = SecurityFrameworkUtils.getLoginUserKey4Space();
        //获取到相对方ID
        String relativeId = redisUtils.get(acheKey);
        //获取所有供应商为登录用户的合同
        PageResult<ContractDO> contractDOPageResult = contractMapper.queryTransferContractAll(reqVO, relativeId);
        PageResult<TransferContractAllRespVO> transferContractAllRespVO = ContractConverter.INSTANCE.toTransferContractAllRespVO(contractDOPageResult);
        if (CollectionUtil.isEmpty(transferContractAllRespVO.getList())) {
            return null;
        }
        Map<String, ContractDO> contractDOMap = CollectionUtils.convertMap(contractDOPageResult.getList(), ContractDO::getId);
        List<Long> creatorIds = contractDOPageResult.getList().stream().map(item -> Long.valueOf(item.getCreator())).distinct().collect(Collectors.toList());
        List<UserCompanyInfoRespDTO> userCompanyInfoList = companyApi.getUserCompanyInfo(creatorIds);
        Map<Long, UserCompanyInfoRespDTO> companyInfoRespDTOMap = userCompanyInfoList.stream().collect(Collectors.toMap(UserCompanyInfoRespDTO::getUserId, Function.identity(), (v1, v2) -> v2));
        List<String> contractIds = contractDOPageResult.getList().stream().map(ContractDO::getId).filter(StringUtils::isNotBlank).collect(Collectors.toList());
        List<SignatoryRelDO> signatoryRelations = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractIds);

        Map<String, List<SignatoryRelDO>> contractRelationMap = CollectionUtils.convertMultiMap(signatoryRelations, SignatoryRelDO::getContractId);
        List<String> relationDataIds = signatoryRelations.stream().map(SignatoryRelDO::getSignatoryId).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        Map<String, Relative> relativeMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(relationDataIds)) {
            List<Relative> relatives = relativeMapper.selectBatchIds(relationDataIds);
            relativeMap = CollectionUtils.convertMap(relatives, Relative::getId);
        }

        Map<String, Relative> finalRelativeMap = relativeMap;
        for (int i = 0; i < transferContractAllRespVO.getList().size(); i++) {
            TransferContractAllRespVO item = transferContractAllRespVO.getList().get(i);
            item.setStatus(ContractStatusEnums.getInstance(Integer.valueOf(item.getStatus())).getDesc());
            item.setSort(i + 1);
            ContractDO contractDO = contractDOMap.get(item.getId());
            UserCompanyInfoRespDTO companyInfo = companyInfoRespDTOMap.get(Long.valueOf(contractDO.getCreator()));
            if (companyInfo != null) {
                item.setInitiator(companyInfo.getName());
            } else {
                item.setInitiator(contractDO.getPartAName());
            }
            List<SignatoryRelDO> signatoryRelDOS = contractRelationMap.get(item.getId());
            List<String> signatoryList = new ArrayList<>();
            if (CollectionUtil.isNotEmpty(signatoryRelDOS)) {
                signatoryRelDOS.forEach(rel -> {
                    Relative relative = finalRelativeMap.get(rel.getSignatoryId());
                    if (relative != null) {
                        signatoryList.add(relative.getCompanyName());
                    }
                });
            }
            if (signatoryList.size() == 0 && StringUtils.isNotEmpty(contractDO.getPartBName())) {
                signatoryList.add(contractDO.getPartBName());
            }
            item.setSignatoryList(signatoryList);
        }
        return transferContractAllRespVO;
    }


    /**
     * 合同添加水印
     *
     * @param contractId
     * @throws Exception
     */
    @Override
    public Long addTextWatermarkToPDF(String contractId, String watermarkId) throws Exception {
        final HTTPAgent agent = new HTTPAgent("http://152.136.27.193:9000/v1/");
        ContractDO contractDO = contractMapper.selectById(contractId);
        if (ObjectUtil.isNotEmpty(contractDO)) {
            String folderId = "TEMPLATE-" + IdUtil.fastSimpleUUID();
            String s = contractDO.getName();
            String localFolderPath = "/data/file/ready_to_up" + "/" + folderId;
//            String localFolderPath = "E:" + "/" + folderId;
            String outPath = localFolderPath + "/" + IdUtil.fastSimpleUUID();
            FileUtil.mkdir(localFolderPath);
            FileUtil.mkdir(outPath);
            ByteArrayInputStream byteArrayInputStream = IoUtil.toStream(fileApi.getFileContentById(contractDO.getPdfFileId()));
            FileUtil.writeFromStream(byteArrayInputStream, localFolderPath + "/" + s + ".pdf");
            File srcFile = new File(localFolderPath + "/" + s + ".pdf");
            FileOutputStream out = new FileOutputStream(outPath + "/" + s + ".pdf");
            if (ObjectUtil.isNotEmpty(watermarkId)) {
                WatermarkDO watermarkDO = watermarkMapper.selectById(watermarkId);
                if (ObjectUtil.isNotEmpty(watermarkDO)) {
                    if (Integer.valueOf(0).equals(watermarkDO.getType())) {
                        textWithWatermark(watermarkDO, srcFile, out, agent);
                    }
                    if (Integer.valueOf(1).equals(watermarkDO.getType())) {
                        imageWithWatermark(srcFile, watermarkDO, out, agent);
                    }
                }
            } else {
                try {
                    //旋转角度是 45 的倍数。颜色后 2 位可调整文字透明度 00-FF。
                    TextInfo textinfo = new TextInfo("  仅 供 借 阅  ", " 宋 体 ", 14, "#808080", 45, Const.XAlign.Center, Const.YAlign.Middle);
                    textinfo.setTiled(true);
                    MarkPosition mk = new MarkPosition(10, 10, 120, 120, MarkPosition.INDEX_ALL);
                    boolean printable = true;
                    boolean visible = true;
                    // todo 水印后续改成使用changxie
//                    YhAgentUtil.convertAndAddTextWatermarkToPDF(srcFile, out, textinfo, mk, printable, visible);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    agent.close();
                }
            }
            Path path = Paths.get(outPath + "/" + s + ".pdf");
            Long borrowFileId = fileApi.uploadFile(s + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));
            contractDO.setBorrowFileId(borrowFileId);
            contractMapper.updateById(contractDO);
            FileUtil.del(localFolderPath);
            return borrowFileId;
        } else {
            throw exception(DIY_ERROR, contractId + "该合同不存在");
        }
    }

    private void imageWithWatermark(File srcFile, WatermarkDO watermarkDO, FileOutputStream out, HTTPAgent agent) throws IOException {
        try {
            // 加载 PDF 文件
            PDDocument document = PDDocument.load(srcFile);
            // 从 URL 下载图片
            URL imageUrl = new URL(watermarkDO.getFileUrl());
            InputStream imageStream = imageUrl.openStream();
            byte[] imageBytes = readBytesFromInputStream(imageStream);
            imageStream.close();
            // 将图片加载到 PDDocument
            PDImageXObject image = PDImageXObject.createFromByteArray(document, imageBytes, watermarkDO.getId());
            // 遍历所有页面添加图片水印
            for (PDPage page : document.getPages()) {
                try (PDPageContentStream contentStream = new PDPageContentStream(
                        document, page, PDPageContentStream.AppendMode.APPEND, true, true)) {
                    // 获取页面尺寸
                    float pageWidth = page.getMediaBox().getWidth();
                    float pageHeight = page.getMediaBox().getHeight();
                    // 设置图片位置和大小
                    float picWidth = Float.parseFloat(watermarkDO.getPicWidth());
                    float picHeight = Float.parseFloat(watermarkDO.getPicHeight());
//                    float x = (pageWidth - picWidth );
//                    float y = (pageHeight - picHeight);
                    image.setWidth((int) picWidth);
                    image.setHeight((int) picHeight);
                    // 水平和垂直方向的步长
                    float xStep = picWidth + 30; // 水印宽度 + 间距
                    float yStep = picHeight + 30; // 水印高度 + 间距

                    // 遍历行和列进行平铺绘制
                    for (float y = 0; y < pageHeight; y += yStep) {
                        for (float x = 0; x < pageWidth; x += xStep) {
                            // 保存当前绘制状态
                            contentStream.saveGraphicsState();

                            // 平移并旋转水印
                            Matrix transform = Matrix.getRotateInstance(
                                    Math.toRadians(45), // 旋转角度（45 度）
                                    x + picWidth / 2, // 旋转中心 X 坐标
                                    y + picHeight / 2  // 旋转中心 Y 坐标
                            );
                            contentStream.transform(transform);

                            // 绘制图片
                            contentStream.drawImage(image, x, y, picWidth, picHeight);

                            // 恢复绘制状态
                            contentStream.restoreGraphicsState();
                        }
                    }
                    // 绘制图片
//                    contentStream.drawImage(image, 0, 0, pageWidth, pageHeight);
//                    contentStream.drawImage(image, x, y, image.getWidth(), image.getHeight());
                }
            }
            // 保存水印文件到输出路径
            document.save(out);
            document.close();
        } catch (Exception e) {
            System.err.println("图片水印添加失败: " + e.getMessage());
            e.printStackTrace();
        } finally {
            agent.close();
        }
    }

    public byte[] readBytesFromInputStream(InputStream inputStream) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] temp = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(temp)) != -1) {
            buffer.write(temp, 0, bytesRead);
        }
        return buffer.toByteArray();
    }

    private void textWithWatermark(WatermarkDO watermarkDO, File srcFile, FileOutputStream out, HTTPAgent agent) throws IOException {
        try {
            // 动态生成水印文字信息
            String colorHex = "#808080";
            if (watermarkDO.getWatermarkAlpha() != null) {
                colorHex += watermarkDO.getWatermarkAlpha();
            } else {
                colorHex += "FF"; // 默认不透明
            }
            TextInfo textinfo = new TextInfo(
                    watermarkDO.getContent(),
                    "宋体", // 默认字体
                    watermarkDO.getWatermarkSize(),
                    colorHex,
                    watermarkDO.getWatermarkAngle(),
                    Const.XAlign.Center,
                    Const.YAlign.Middle
            );
            // 设置平铺逻辑
            textinfo.setTiled("full".equals(watermarkDO.getPosition()));
            // 设置水印位置
            MarkPosition mk = new MarkPosition(10, 10, 120, 120, MarkPosition.INDEX_ALL);
            // 添加水印
            boolean printable = true;
            boolean visible = true;
            YhAgentUtil.convertAndAddTextWatermarkToPDF(srcFile, out, textinfo, mk, printable, visible);
        } catch (Exception e) {
            System.err.println("设置水印异常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            agent.close();
        }
    }

    /**
     * 通过参数添加水印
     */
    public Long paramsAddTextWatermarkToPDF(Long fileId) throws Exception {
        final HTTPAgent agent = new HTTPAgent("http://152.136.27.193:9000/v1/");
        String folderId = "TEMPLATE-" + IdUtil.fastSimpleUUID();
        String s = fileApi.getName(fileId);
        String localFolderPath = "/data/file/ready_to_up" + "/" + folderId;
//            String localFolderPath = "E:" + "/" + folderId;
        String outPath = localFolderPath + "/" + IdUtil.fastSimpleUUID();
        FileUtil.mkdir(localFolderPath);
        FileUtil.mkdir(outPath);
        ByteArrayInputStream byteArrayInputStream = IoUtil.toStream(fileApi.getFileContentById(fileId));
        FileUtil.writeFromStream(byteArrayInputStream, localFolderPath + "/" + s + ".pdf");
        File srcFile = new File(localFolderPath + "/" + s + ".pdf");
        FileOutputStream out = new FileOutputStream(outPath + "/" + s + ".pdf");
        ArrayList<String> strings = new ArrayList<>();
        strings.add(SystemConfigKeyEnums.BORROW_PDF_WATERMARK.getKey());
        List<SystemConfigRespDTO> configsByCKeys = systemConfigApi.getConfigsByCKeys(strings);
        String json = "";
        if (CollectionUtil.isNotEmpty(configsByCKeys)) {
            json = configsByCKeys.get(0).getCValue();
        }
        try {
            //旋转角度是 45 的倍数。颜色后 2 位可调整文字透明度 00-FF。
            JSONObject jsonObject = JSONObject.parseObject(json);
            JSONObject textinfoJson = jsonObject.getJSONObject("textinfo");
            String text = textinfoJson.getString("text");
            String font = textinfoJson.getString("font");
            int fontSize = Integer.valueOf(textinfoJson.getString("fontSize"));
            String color = textinfoJson.getString("color");
            int angle = Integer.valueOf(textinfoJson.getString("angle"));
            String xAlign = textinfoJson.getString("xAlign");
            String yAlign = textinfoJson.getString("yAlign");
            boolean tiled = textinfoJson.getBoolean("tiled");
            TextInfo textinfo = new TextInfo(text, font, fontSize, color, angle, Const.XAlign.valueOf(xAlign), Const.YAlign.valueOf(yAlign));
            textinfo.setTiled(tiled);
            JSONObject markPositionJson = jsonObject.getJSONObject("markPosition");
            int x = Integer.valueOf(markPositionJson.getString("x"));
            int y = Integer.valueOf(markPositionJson.getString("y"));
            int width = Integer.valueOf(markPositionJson.getString("width"));
            int height = Integer.valueOf(markPositionJson.getString("height"));
            String index = markPositionJson.getString("index");
            MarkPosition mk = new MarkPosition(x, y, width, height, MarkPosition.INDEX_ALL);
            boolean printable = jsonObject.getBoolean("printable");
            boolean visible = jsonObject.getBoolean("visible");

            YhAgentUtil.convertAndAddTextWatermarkToPDF(srcFile, out, textinfo, mk, printable, visible);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            agent.close();
        }
        Path path = Paths.get(outPath + "/" + s + ".pdf");
        Long uploadFile = fileApi.uploadFile(s + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));
        FileUtil.del(localFolderPath);
        return uploadFile;
    }

    @Override
    public Boolean updateStatus(List<ApiStatusReqVO> list) {
        List<ContractDO> contractDOList = ContractConverter.INSTANCE.converList(list);
        contractMapper.updateBatch(contractDOList);
        return true;
    }

    @Override
    public VerificationRespVO verification(InputStream inputStream) {

        List<PdfElectronicSealDetails.Signinfo> signInfos = SealDetails.getSignDetail(inputStream, null, "", "");

        List<SignInfoVO> list = signInfos.stream().map(signInfo -> SignInfoVO.builder().build().setSignTime(signInfo.getSignTime()).setSignName(signInfo.getSignName()).setHash(signInfo.getHash()).setSignData(signInfo.getSignData()).setHashType(signInfo.getHashType()).setIndex(signInfo.getIndex()).setAppName(signInfo.getAppName()).setSignSn(signInfo.getSignSn()).setTsTime(signInfo.getTsTime()).setUserName(signInfo.getUserName()).setCompName(signInfo.getCompName()).setKeySn(signInfo.getKeySn()).setNewHash(signInfo.getNewHash()).setDigitalSigVerify(signInfo.getDigitalSigVerify()).setSealImgData(signInfo.getSealImgData()).setExtParam(signInfo.getExtParam()).setSealImgData(signInfo.getSealImgData()).setTamper(signInfo.isTamper())).collect(Collectors.toList());

        return VerificationRespVO.builder().build().setSignNum(signInfos.size()).setSignInfo(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String submit(Long loginUserId, IdReqVO reqVO) {
        //判断走审批流
//        if (systemConfigApi.ifApprove(ActivityConfigurationEnum.ECMS_CONTRACT_BOTH)) {
        // 走审批流
        return submitOne(loginUserId, reqVO);
//        } else {
//            // 不走审批流，直接通过
//            return fastPassOne(reqVO);
//        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String govsubmit(Long loginUserId, IdReqVO reqVO) {
        contractApi.productApproveSendEcms("", reqVO.getId());
        return reqVO.getId();

    }

    private ContractDetailsDO getContractDetailsDO(Contract contract) {
        return new ContractDetailsDO().setId(contract.getTaskId()).setStatus(contract.getStatus()).setContractBaseInfo(JsonUtils.toJsonString(contract.getContractBaseInfo())).setPurchaseDetail(JsonUtils.toJsonString(contract.getPurchaseDetail())).setSuppliers(JsonUtils.toJsonString(contract.getSuppliers())).setSubSuppliers(JsonUtils.toJsonString(contract.getSubSuppliers())).setPaymentPlan(JsonUtils.toJsonString(contract.getPaymentPlan())).setAcceptRequirement(JsonUtils.toJsonString(contract.getAcceptRequirement())).setProjectManager(JsonUtils.toJsonString(contract.getProjectManager()));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String receiveContract(ContractReceiveVO contractReceiveVO) throws Exception {
        ContractDO contractDO1 = contractMapper.selectById(contractReceiveVO.getId());

        if (contractReceiveVO.getDeleted()) {
            if (ObjectUtil.isEmpty(contractDO1)) {
                return "推送成功，本端未查询到该合同，无需删除或撤销： " + contractReceiveVO.getId();
            }
            //contractMapper.deleteById(contractReceiveVO.getId());
            // 如果是撤销，判断合同当前是否处于审批中，是，则不可撤销
            if (contractReceiveVO.getIsCancle()) {
                // 如果合同状态不是待送审状态（即合同在审批中 ），抛异常提示
                if (!Objects.equals(contractDO1.getStatus(), ContractStatusEnums.TO_BE_CHECK.getCode())) {
                    throw exception(ErrorCodeConstants.CONTRACT_IS_CHECKING);
                }
            }

            //合同状态设置为已删除
            ContractDO contractDO = new ContractDO()
                    .setId(contractReceiveVO.getId())
                    .setStatus(ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode());
            contractMapper.updateById(contractDO);
            bpmContractMapper.delete(new LambdaQueryWrapperX<BpmContract>().eq(BpmContract::getContractId, contractReceiveVO.getId()));
            return "推送成功，合同已删除 " + contractReceiveVO.getId();
        }

        ContractDO contractDO = BeanUtils.toBean(contractReceiveVO, ContractDO.class);
        //根据采购单位去配置表查找当前系统对应的用户id
        LambdaQueryWrapperX<SystemuserRelDO> lambdaQueryWrapperX = new LambdaQueryWrapperX();
        lambdaQueryWrapperX.eq(SystemuserRelDO::getBuyerOrgId, contractReceiveVO.getBuyerOrgId());
        List<SystemuserRelDO> systemuserRelDOList = systemuserRelMapper.selectList(lambdaQueryWrapperX);
        if (systemuserRelDOList.size() == 0) {
            return "操作失败,找不到对应采购单位用户！";
        }
        SystemuserRelDO systemuserRelDO = systemuserRelDOList.get(0);
        contractDO.setCreator(systemuserRelDO.getCurrentUserId() == null ? null : systemuserRelDO.getCurrentUserId().toString());
        contractDO.setAmount(contractReceiveVO.getTotalMoney().doubleValue());
        contractDO.setUpdater(systemuserRelDO.getCurrentUserId() == null ? null : systemuserRelDO.getCurrentUserId().toString());
        contractDO.setTenantId(systemuserRelDO.getCurrentTenantId());
        contractDO.setDeptId(systemuserRelDO.getDeptId());
        contractDO.setStatus(ContractStatusEnums.TO_BE_CHECK.getCode());
        contractDO.setUpload(THIRD_PARTY.getCode());
        contractDO.setCreateTime(null);
        contractDO.setUpdateTime(null);
        contractDO.setFileAddId(0L);
        // contractDO.setPdfFileId(0L);

        contractDO.setLocation(contractReceiveVO.getContractSignAddress());
        contractDO.setPartAName(contractReceiveVO.getBuyerOrgName());
        contractDO.setPartAId(contractReceiveVO.getBuyerOrgId());
        contractDO.setPartBId(contractReceiveVO.getSupplierId());
        contractDO.setPartBName(contractReceiveVO.getSupplierName());
        contractDO.setLocation(contractReceiveVO.getContractSignAddress());

        //将合同类型转换成单位端的合同类型
        if (contractReceiveVO.getProjectCategoryCode() != null) {
            ProjectCategoryEnums projectCategoryEnums = ProjectCategoryEnums.getInstance(contractReceiveVO.getProjectCategoryCode());
            if (ObjectUtil.isNotNull(projectCategoryEnums)) {
                List<ContractType> contractTypes = contractTypeMapper.selectList(ContractType::getPlatId, String.valueOf(projectCategoryEnums.getValue()));
                if (CollectionUtil.isNotEmpty(contractTypes)) {
                    contractDO.setContractType(contractTypes.get(0).getId());
                }
            }
        }

        ContractOrderExtDO contractOrderExtDO = BeanUtils.toBean(contractReceiveVO, ContractOrderExtDO.class);
        contractOrderExtDO.setCreateTime(null);
        contractOrderExtDO.setUpdateTime(null);
        ContractDO oldContract = contractMapper.selectById(contractDO.getId());
        if (oldContract != null) {
            contractMapper.updateById(contractDO);
            contractOrderExtMapper.updateById(contractOrderExtDO);
        } else {
            contractMapper.insert(contractDO);
            //Relative relative = relativeMapper.selectById(contractDO.getSupplierId());
            //供应商起草同步过来的合同的相对方类型一定是供应商-乙方2
            signatoryRelMapper.insert(new SignatoryRelDO().setContractId(contractDO.getId()).setSignatoryId(contractDO.getSupplierId()).setType(2));
            contractOrderExtMapper.insert(contractOrderExtDO);
        }
        //履约计划 - 支付计划
        if (CollectionUtil.isNotEmpty(contractReceiveVO.getPaymentScheduleDTOList())) {
            List<String> ids = new ArrayList();
            contractReceiveVO.getPaymentScheduleDTOList().forEach(paymentScheduleDTO -> {
                ids.add(paymentScheduleDTO.getId());
                paymentScheduleDTO.setDeptId(systemuserRelDO.getDeptId());
            });
            paymentScheduleMapper.delete(new LambdaQueryWrapperX<PaymentScheduleDO>().eq(PaymentScheduleDO::getContractId, contractDO.getId()).notIn(PaymentScheduleDO::getId, ids));
            paymentScheduleMapper.saveOrUpdateBatch(contractReceiveVO.getPaymentScheduleDTOList());
        }

        if (CollectionUtil.isNotEmpty(contractReceiveVO.getTradingSupplierDTOS())) {
            tradingSupplierMapper.saveOrUpdateBatch(contractReceiveVO.getTradingSupplierDTOS());
        }
        return contractDO.getId();

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String createOrUpdateContract(FileUploadContractCreateReqVO contractCreateReqVO) throws Exception {
        // return null;
        //添加合同表
        ContractDO contractDO = null;
        //todo
//        if (ObjectUtil.isEmpty(contractCreateReqVO.getExpirationDate())) {
//            contractCreateReqVO.setExpirationDate(DateUtil.parse("2099-12-31"));
//        }
        if (StringUtils.isEmpty(contractCreateReqVO.getContractType())) {
            contractCreateReqVO.setContractType("827987cb24b9db1618df9eb2879cfed8");
        }
        switch (contractCreateReqVO.getUpload()) {
            // MODEL_DRAFT (0, "模板起草"),
            // UPLOAD_FILE (1, "文件起草"),
            // ORDER_DRAFT(3, "依据已成交的采购项目或订单起草"),
            //UPLOAD_CONTRACT_FILE(6, "上传合同文件"),
            case 0:
                contractDO = createContractByModel(contractCreateReqVO);
                break;
            case 1:
                contractDO = createContractByFile(contractCreateReqVO);
                break;
            case 3:
                contractDO = createContractByOrder(contractCreateReqVO);
                break;
            case 6:
                contractDO = uploadContractCreateOrUpdate(contractCreateReqVO);
                break;
            default:
                break;
        }
        //保存合同参数信息
        if (ObjectUtil.isEmpty(contractDO)) {
            throw exception(ErrorCodeConstants.EMPTY_DATA_ERROR);
        }
        if (contractCreateReqVO.getUpload() != 6) {
            gpMallGoodsMapper.delete(new LambdaQueryWrapperX<GoodsDO>().eq(GoodsDO::getOrderId, contractDO.getOrderGuid()));
            if (CollectionUtil.isNotEmpty(contractCreateReqVO.getGoodsList())) {
                List<GoodsDO> goodsDOS = GPMallOrderConverter.INSTANCE.goodsVOList2DOList(contractCreateReqVO.getGoodsList());
                String contractId = contractDO.getId();
                goodsDOS.forEach(goodsDO -> goodsDO.setContractId(contractId));
                gpMallGoodsMapper.insertBatch(goodsDOS);
            }
        }
        contractParameterMapper.delete(new LambdaQueryWrapperX<ContractParameterDO>().eq(ContractParameterDO::getContractId, contractDO.getId()));
        if (CollectionUtil.isNotEmpty(contractCreateReqVO.getContractParameterVOList())) {
            List<ContractParameterDO> contractParameterDOList = new ArrayList<>();
            for (ContractParameterVO contractParameterVO : contractCreateReqVO.getContractParameterVOList()) {
                ContractParameterDO contractParameterDO = ContractParameterConverter.INSTANCE.toEntity(contractParameterVO);
                contractParameterDO.setContractId(contractDO.getId());
                contractParameterDOList.add(contractParameterDO);
            }
            contractParameterMapper.insertBatch(contractParameterDOList);
        }

        //保存合同章信息
        if (CollectionUtil.isNotEmpty(contractCreateReqVO.getContractSealVOList())) {
            List<ContractSealDO> contractSealDOList = new ArrayList<>();
            for (ContractSealVO contractSealVO : contractCreateReqVO.getContractSealVOList()) {
                ContractSealDO contractSealDO = ContractSealConverter.INSTANCE.toEntity(contractSealVO);
                contractSealDO.setContractId(contractDO.getId());
                contractSealDOList.add(contractSealDO);
            }
            contractSealMapper.insertBatch(contractSealDOList);
        }
        if (contractCreateReqVO.getUpload() != 6) {
            //删除所有关联
            paymentScheduleMapper.delete(new LambdaQueryWrapperX<PaymentScheduleDO>().eq(PaymentScheduleDO::getContractId, contractDO.getId()));
            //保存付款计划信息
            if (CollectionUtil.isNotEmpty(contractCreateReqVO.getPaymentScheduleVOList())) {
                List<PaymentScheduleDO> paymentScheduleDOList = new ArrayList<>();
                for (PaymentScheduleVO paymentScheduleVO : contractCreateReqVO.getPaymentScheduleVOList()) {
                    PaymentScheduleDO paymentScheduleDO = PaymentScheduleConverter.INSTANCE.toEntity(paymentScheduleVO);
                    paymentScheduleDO.setContractId(contractDO.getId());
                    paymentScheduleDOList.add(paymentScheduleDO);
                }
                paymentScheduleMapper.insertBatch(paymentScheduleDOList);
            }
        }
        contractPurchaseMapper.delete(new LambdaQueryWrapperX<ContractPurchaseDO>().eq(ContractPurchaseDO::getContractId, contractDO.getId()));
        //保存合同采购内容信息
        if (CollectionUtil.isNotEmpty(contractCreateReqVO.getContractPurchaseReqVOList())) {
            List<ContractPurchaseDO> contractPurchaseDOList = new ArrayList<>();
            for (ContractPurchaseReqVO contractPurchaseReqVO : contractCreateReqVO.getContractPurchaseReqVOList()) {
                ContractPurchaseDO contractPurchaseDO = ContractPurchaseConverter.INSTANCE.convert(contractPurchaseReqVO);
                contractPurchaseDO.setContractId(contractDO.getId());
                contractPurchaseDOList.add(contractPurchaseDO);
            }
            contractPurchaseMapper.insertBatch(contractPurchaseDOList);
        }

        //保存合同签定方信息
        contractSignatoryMapper.delete(new LambdaQueryWrapperX<ContractSignatoryDO>().eq(ContractSignatoryDO::getContractId, contractDO.getId()));
        if (CollectionUtil.isNotEmpty(contractCreateReqVO.getContractSignatoryReqVOList())) {
            List<ContractSignatoryDO> contractSignatoryDOList = new ArrayList<>();
            for (ContractSignatoryReqVO contractSignatoryReqVO : contractCreateReqVO.getContractSignatoryReqVOList()) {
                ContractSignatoryDO contractSignatoryDO = ContractSignatoryConverter.INSTANCE.convert(contractSignatoryReqVO);
                contractSignatoryDO.setContractId(contractDO.getId());
            }
            contractSignatoryMapper.insertBatch(contractSignatoryDOList);
        }

        attachmentRelMapper.delete(new LambdaQueryWrapperX<AttachmentRelDO>().eq(AttachmentRelDO::getContractId, contractDO.getId()));
        if (CollectionUtil.isNotEmpty(contractCreateReqVO.getAttachmentList())) {
            //循环取出附件信息
            for (AttachmentRelCreateReqVO attachmentList : contractCreateReqVO.getAttachmentList()) {
                //保存附件信息
                AttachmentRelDO ecmsAttachmentRelDO = AttachmentRelConverter.INSTANCE.convert(attachmentList);
                //添加主合同绑定id
                ecmsAttachmentRelDO.setContractId(contractDO.getId());
                attachmentRelMapper.insert(ecmsAttachmentRelDO);
            }
        }
        //添加签署方关系
        signatoryRelMapper.delete(new LambdaQueryWrapperX<SignatoryRelDO>().eq(SignatoryRelDO::getContractId, contractDO.getId()));

        if (CollectionUtil.isNotEmpty(contractCreateReqVO.getSignatoryList())) {
            for (SignatoryRelReqVO signatoryRelReqVO : contractCreateReqVO.getSignatoryList()) {
                SignatoryRelDO signatoryRelDO = SignatoryRelConverter.INSTANCE.convert(signatoryRelReqVO);
                if (ObjectUtil.isNotEmpty(signatoryRelReqVO.getSignatoryId())) {
                    signatoryRelDO.setContractId(contractDO.getId());
                    signatoryRelMapper.insert(signatoryRelDO);
                }
            }
        }
        //是否发起审批
        if (ObjectUtil.isNotEmpty(contractCreateReqVO.getInitiateApproval()) && IfNumEnums.YES.getCode().equals(contractCreateReqVO.getInitiateApproval())) {
            bpmContractService.createProcess(getLoginUserId(), new BpmContractCreateReqVO().setId(contractDO.getId()));
        }

        return contractDO.getId();
    }

    @Override
    public ContractDataVo queryById(String id) throws Exception {
        ContractDO contractDO = contractMapper.selectById(id);
        ContractDataVo contractDataVo = ContractConverter.INSTANCE.toContractDataVo(contractDO);
        ContractDataDTO contractDataDTO = null;
        ContractInfoBackupsDO contractInfoBackupsDO = contractDO.getOrderGuid() == null ? null : contractInfoBackupsMapper.selectOne(new LambdaQueryWrapperX<ContractInfoBackupsDO>().eq(ContractInfoBackupsDO::getOrderId, contractDO.getOrderGuid()).last(" limit 1"));
        if (ObjectUtils.isNotEmpty(contractInfoBackupsDO)) {
            String contractInfo = contractInfoBackupsDO.getContractInfo();
            contractDataDTO = JsonUtils.parseObject(contractInfo, ContractDataDTO.class);
        }
        if (ObjectUtils.isNotEmpty(contractDataDTO)) {
            ContractDTO contractDTO = contractDataDTO == null ? null : contractDataDTO.getContractDTO();
            contractDataVo.setContractDTO(contractDTO);
            contractDataVo.setResultMap(contractDataDTO == null ? null : contractDataDTO.getResultMap());
//            contractDataVo.setModelId(contractDataDTO == null ? null : contractDataDTO.getModelId());
        }
        //设置附件信息
        List<AttachmentRelDO> attachmentRelDOS = attachmentRelMapper.selectList(new LambdaQueryWrapperX<AttachmentRelDO>().eq(AttachmentRelDO::getContractId, contractDO.getId()));
        List<AttachmentRelCreateReqVO> attachmentRelCreateReqVO = AttachmentRelConverter.INSTANCE.toAttachmentRelCreateReqVO(attachmentRelDOS);
        contractDataVo.setAttachmentList(attachmentRelCreateReqVO);

        //签署方id集合
        List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(new LambdaQueryWrapperX<SignatoryRelDO>().eq(SignatoryRelDO::getContractId, contractDO.getId()));
        List<SignatoryRelReqVO> signatoryRelReqVOS = SignatoryRelConverter.INSTANCE.toSignatoryRelReqVOS(signatoryRelDOS);
        contractDataVo.setSignatoryList(signatoryRelReqVOS);
        //合同参数信息集合
        List<ContractParameterDO> contractParameterDOS = contractParameterMapper.selectList(new LambdaQueryWrapperX<ContractParameterDO>().eq(ContractParameterDO::getContractId, contractDO.getId()));
        List<ContractParameterVO> contractParameterVOS = ContractParameterConverter.INSTANCE.toContractParameterVOS(contractParameterDOS);
        contractDataVo.setContractParameterVOList(contractParameterVOS);
        //合同章信息
        List<ContractSealDO> contractSealDOS = contractSealMapper.selectList(new LambdaQueryWrapperX<ContractSealDO>().eq(ContractSealDO::getContractId, contractDO.getId()));
        List<ContractSealVO> contractSealVOS = ContractSealConverter.INSTANCE.toContractSealVOS(contractSealDOS);
        contractDataVo.setContractSealVOList(contractSealVOS);
        //付款信息集合
        List<PaymentScheduleDO> paymentScheduleDOS = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>().eq(PaymentScheduleDO::getContractId, contractDO.getId()));
        List<PaymentScheduleVO> paymentScheduleVO = PaymentScheduleConverter.INSTANCE.toPaymentScheduleVO(paymentScheduleDOS);
        contractDataVo.setPaymentScheduleVOList(paymentScheduleVO);
        //合同采购内容集合
        List<ContractPurchaseDO> contractPurchaseDOS = contractPurchaseMapper.selectList(new LambdaQueryWrapperX<ContractPurchaseDO>().eq(ContractPurchaseDO::getContractId, contractDO.getId()));
        List<ContractPurchaseReqVO> contractPurchaseReqVOS = ContractPurchaseConverter.INSTANCE.toContractPurchaseReqVO(contractPurchaseDOS);
        contractDataVo.setContractPurchaseReqVOList(contractPurchaseReqVOS);
        //合同签订方信息集合
        List<ContractSignatoryDO> contractSignatoryDOS = contractSignatoryMapper.selectList(new LambdaQueryWrapperX<ContractSignatoryDO>().eq(ContractSignatoryDO::getContractId, contractDO.getId()));
        List<ContractSignatoryReqVO> contractSignatoryReqVOS = ContractSignatoryConverter.INSTANCE.toContractSignatoryReqVOS(contractSignatoryDOS);
        contractDataVo.setContractSignatoryReqVOList(contractSignatoryReqVOS);
        //采购标的信息
        List<GoodsDO> goodsDOS = gpMallGoodsMapper.selectList(new LambdaQueryWrapperX<GoodsDO>().eq(GoodsDO::getContractId, contractDO.getId()));
        List<GoodsVO> goodsVOS = GPMallOrderConverter.INSTANCE.toGoodsVOS(goodsDOS);
        contractDataVo.setGoodsVOS(goodsVOS);
        return contractDataVo;
    }

    /**
     * 上传合同
     *
     * @param vo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ContractDO uploadContractCreateOrUpdate(FileUploadContractCreateReqVO vo) throws Exception {
        if (StringUtils.isNotEmpty(vo.getOrderGuid())) {
            //根据订单ID获取合同信息
            LambdaQueryWrapper<ContractDO> queryWrapper = new LambdaQueryWrapperX<ContractDO>().select(ContractDO::getId, ContractDO::getStatus, ContractDO::getName).eq(ContractDO::getOrderGuid, vo.getOrderGuid()).notIn(ContractDO::getStatus, ContractStatusEnums.CONTRACT_AUDITSTATUS_DELETE.getCode(), ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode(), ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode());
            if (StringUtils.isNotEmpty(vo.getId())) {
                queryWrapper.ne(ContractDO::getId, vo.getId());
            }
            List<ContractDO> contractInfoList = contractMapper.selectList(queryWrapper);
            if (CollectionUtil.isNotEmpty(contractInfoList)) {
                throw exception(ErrorCodeConstants.MODEL_CHECK_CONTRACT_TYPE_EMPTY, "订单已存在签署完成的合同:" + contractInfoList.get(0).getName());
            }
        }
        ContractDO contractDO = ContractConverter.INSTANCE.fileuploadVOtoUploadDO(vo);
        //文件名称托底
        if (StringUtils.isBlank(vo.getFileName())) {
            contractDO.setFileName(vo.getName() + ".pdf");
        }

        //设置支付计划信息
        List<PaymentScheduleDO> planDOList = new ArrayList<>();
//        if (CollectionUtil.isNotEmpty(vo.getStagePaymentList())) {
//            List<PaymentScheduleDO> planDOs = PaymentScheduleConverter.INSTANCE.toPaymentScheduleDOS2(vo.getStagePaymentList());
//            planDOList.addAll(planDOs);
//        }
//        if (CollectionUtil.isNotEmpty(vo.getPaymentPlanList())) {
//            List<PaymentScheduleDO> planDOs = PaymentScheduleConverter.INSTANCE.toPaymentScheduleDOS(vo.getPaymentPlanList());
//            planDOList.addAll(planDOs);
//        }
        if (CollectionUtil.isNotEmpty(vo.getPaymentScheduleVOList())) {
            List<PaymentScheduleDO> planDOs = PaymentScheduleConverter.INSTANCE.toPaymentScheduleDOS3(vo.getPaymentScheduleVOList());
            planDOList.addAll(planDOs);
        }
//        if (CollectionUtil.isNotEmpty(planDOList)) {
//            //支付时间递增
//            Date playData = null;
//            for (PaymentScheduleDO play : planDOList) {
//                if (ObjectUtil.isNotNull(playData) && playData.after(play.getPaymentTime())) {
//                    throw exception(ErrorCodeConstants.MODEL_CHECK_CONTRACT_TYPE_EMPTY, "支付计划时间必须递增");
//                }
//                playData = play.getPaymentTime();
//                if (ObjectUtils.isEmpty(playData)) {
//                    throw exception(ErrorCodeConstants.MODEL_CHECK_CONTRACT_TYPE_EMPTY, "支付计划时间不能为空");
//                }
//            }
//            //判断总金额与合同总金额
//            BigDecimal sum = planDOList.stream().map(PaymentScheduleDO::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
//            if (sum.compareTo(vo.getTotalMoney()) != 0) {
//                throw exception(ErrorCodeConstants.MODEL_CHECK_CONTRACT_TYPE_EMPTY, "付款计划总金额与合同金额不一致");
//            }
//            //判断总比例
//            double sumProportion = planDOList.stream().map(PaymentScheduleDO::getPaymentRatio).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue();
//            if (sumProportion != 100) {
//                throw exception(ErrorCodeConstants.MODEL_CHECK_CONTRACT_TYPE_EMPTY, "付款计划总比例错误");
//            }
//        }

        String purchaserOrgGuid = vo.getBuyerOrgId();
        String supplierGuid = vo.getSupplierId();
        //设置采购人和供应商ID与名称
        if (ObjectUtils.isNotEmpty(contractDO.getOrderGuid())) {
            //交易关联包需要校验
            DraftOrderInfoDO draftOrderInfoDO = gpMallOrderMapper.selectOne(new LambdaQueryWrapperX<DraftOrderInfoDO>().eq(DraftOrderInfoDO::getOrderGuid, contractDO.getOrderGuid()).orderByDesc(DraftOrderInfoDO::getCreateTime).last(" limit 1"));
            //修改草拟合同状态为已草拟
//京东只能供应商起草，则生成草稿后订单不可再起草合同
            if (ObjectUtils.isNotEmpty(draftOrderInfoDO)) {
                if (ObjectUtil.isNotEmpty(draftOrderInfoDO) && ObjectUtil.isNotEmpty(draftOrderInfoDO.getStatus())
                        && !GCYOrderStatusEnums.DRAFTED.getCode().equals(draftOrderInfoDO.getStatus())) {
                    draftOrderInfoDO.setStatus(GCYOrderStatusEnums.DRAFTED.getCode());
                    Map<String, Object> bodyParam = new HashMap<>();
                    bodyParam.put("client_id", clientId);
                    bodyParam.put("client_secret", clientSecret);
                    String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
                    JSONObject jsonObject = JSONObject.parseObject(token);
                    try {
                        DraftOrderInfo draftOrderInfo = new DraftOrderInfo();
                        draftOrderInfo.setOrderGuid(draftOrderInfoDO.getOrderGuid()).setStatus(GCYOrderStatusEnums.DRAFTED.getCode());
                        String result = contractProcessApi.updateOrderStatus(jsonObject.getString("access_token"), draftOrderInfo);
                        JSONObject resultJson = JSONObject.parseObject(result);
                        if (!"0".equals(resultJson.getString("code"))) {
                            throw new RuntimeException(result);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("推送电子合同更新订单状态失败", e);
                    }
                    gpMallOrderMapper.updateById(draftOrderInfoDO);
                }
                purchaserOrgGuid = draftOrderInfoDO.getPurchaserOrgGuid();
                supplierGuid = draftOrderInfoDO.getSupplierGuid();
            }
            //查询签署方信息拼接
            List<Relative> relatives = relativeMapper.selectBatchIds(Arrays.asList(purchaserOrgGuid, supplierGuid));
            List<SignatoryRelReqVO> list = new ArrayList<>();
            //采购单位默认首签
            String finalPurchaserOrgGuid = purchaserOrgGuid;
            relatives.forEach(relative -> {
                SignatoryRelReqVO signatoryRelReqVO = new SignatoryRelReqVO();
                signatoryRelReqVO.setSignatoryId(relative.getId());
                signatoryRelReqVO.setSignatoryName(relative.getCompanyName());
                signatoryRelReqVO.setUserId(relative.getContactId());
                if (finalPurchaserOrgGuid.equals(relative.getId())) {
                    signatoryRelReqVO.setType(1);
                    signatoryRelReqVO.setInitiator(true);
                } else {
                    signatoryRelReqVO.setType(2);
                    signatoryRelReqVO.setInitiator(false);
                }
                list.add(signatoryRelReqVO);
            });
            vo.setSignatoryList(list);
            handlePart(contractDO, list);
            if (ObjectUtil.isEmpty(vo.getInitiateApproval()) || vo.getInitiateApproval() != 1) {
                //保存按钮进入，状态为待发送
                contractDO.setStatus(ContractStatusEnums.TO_BE_CHECK.getCode());
            } else {
                //发送按钮进入，状态为已发送
                contractDO.setStatus(ContractStatusEnums.CHECKING.getCode());
            }
            if (ObjectUtil.isNotEmpty(draftOrderInfoDO)) {
                //电子卖场修改状态gpms-gpx-5.3   协议定点
                contractDO.setPartAId(draftOrderInfoDO.getPurchaserOrgGuid());
                contractDO.setPartAName(draftOrderInfoDO.getPurchaserOrg());
                contractDO.setPartBId(draftOrderInfoDO.getSupplierGuid());
                contractDO.setPartBName(draftOrderInfoDO.getSupplierName());
//                contractDO.setOrderCode(draftOrderInfoDO.getOrderCode());
            }
        } else {
            //供应商id保存合同表，流程执行人使用
            contractDO.setPartBId(vo.getSupplierId());
            contractDO.setPartBName(vo.getSupplierName());
            contractDO.setPartAId(vo.getBuyerOrgId());
            contractDO.setPartAName(vo.getBuyerOrgName());
            handlePart(contractDO, vo.getSignatoryList());
        }
        //设置合同起草方
        if (ObjectUtils.isNotEmpty(contractDO.getId())) {
            ContractDO orderContractDO = contractMapper.selectById(contractDO.getId());
            if (ObjectUtil.isNotEmpty(orderContractDO)) {
                //删除旧文件 保存新文件....
                //编辑
                contractMapper.updateById(contractDO);
//                //删除关联的付款计划信息
                paymentScheduleMapper.delete(new LambdaQueryWrapperX<PaymentScheduleDO>().eq(PaymentScheduleDO::getContractId, contractDO.getId()));
            }
        } else {
            //信息入合同表
            contractMapper.insert(contractDO);
        }
        //新增支付计划信息
        if (CollectionUtil.isNotEmpty(planDOList)) {
            planDOList.forEach(item -> {
                if (contractDO.getPartBId() != null) {
                    item.setPayee(contractDO.getPartBId());
                }
                item.setContractId(contractDO.getId());
            });
            paymentScheduleMapper.insertBatch(planDOList);
        }
        return contractDO;
    }

    public void uploadFile(ContractDO contractDO, MultipartFile file, String path) {
        String filename = file.getOriginalFilename();
        if (!StringUtils.equals(SUFFIX_PDF, FileNameUtil.extName(filename).toLowerCase())) {
            throw exception(ErrorCodeConstants.FILE_NAME_NOT_RIGHT);
        }
        //上传文件
        try {
            Long fileId = fileApi.uploadFile(UUID.randomUUID() + "_" + filename, FileUploadPathEnum.CONTRACT_DRAFT.getPath() + path + UUID.randomUUID() + contractDO.getCode() + "_" + filename, file.getBytes());
            contractDO.setPdfFileId(fileId);
        } catch (Exception e) {
            throw exception(ErrorCodeConstants.FILE_UPLOAD_ERROR);
        }
    }

    private ContractDO createContractByModel(ContractCreateReqVO contractCreateReqVO) throws Exception {
//先存合同
        ContractDO contractDO = ContractConverter.INSTANCE.toEntity(contractCreateReqVO);
        //校验编码是否重复
//        if (codeExist(contractDO.getId(), contractDO.getCode())) {
//            throw exception(ErrorCodeConstants.CODE_EXISTS);
//        }

        if (ObjectUtil.isNotEmpty(contractCreateReqVO.getInitiateApproval()) && contractCreateReqVO.getInitiateApproval() != 1) {
            //保存按钮进入，状态为待发送
            contractDO.setStatus(ContractStatusEnums.TO_BE_SENT.getCode());
        } else {
            //将富文本保存为pdf存入minio返回文件id
            if (ObjectUtils.isNotEmpty(contractCreateReqVO.getContractContent())) {
                Long pdfId = this.toPdf(new ContractToPdfVO().setContent(contractCreateReqVO.getContractContent()).setName(contractCreateReqVO.getName()));
                contractDO.setPdfFileId(pdfId);
            }
            //发送按钮进入，状态为已发送
            contractDO.setStatus(ContractStatusEnums.SENT.getCode());
        }

        //handlePart签署顺序
        if (ObjectUtil.isNotEmpty(contractCreateReqVO.getSignatoryList())) {
            handlePart(contractDO, contractCreateReqVO.getSignatoryList());
        } else {
            List<SystemConfigRespDTO> signSort = systemConfigApi.getConfigsByCKeys(Arrays.asList("sign_sort"));
            if (ObjectUtil.isNotEmpty(signSort)) {
                contractDO.setSignOrder(signSort.get(0).getCValue());
            } else {
                contractDO.setSignOrder("164_171");
            }
        }
        //删除旧文件
        //添加新文件
        if (ObjectUtils.isNotEmpty(contractDO.getId())) {
            ContractDO orderContractDO = contractMapper.selectById(contractDO.getId());
            if (ObjectUtil.isNotEmpty(orderContractDO)) {
                try {
                    if (ObjectUtils.isNotEmpty(orderContractDO.getPdfFileId())) {
                        fileApi.deleteFile(orderContractDO.getPdfFileId());
                    }
                } catch (Exception e) {
                }
            }
            //编辑
            contractMapper.updateById(contractDO);
        } else {
            //新增
            //信息入合同表
            contractMapper.insert(contractDO);
        }


        return contractDO;
    }

    private ContractDO createContractByFile(ContractCreateReqVO contractCreateReqVO) {
        //保存基础信息
        ContractDO contractDO = ContractConverter.INSTANCE.toEntity(contractCreateReqVO);
        contractDO.setStatus(ContractStatusEnums.TO_BE_CHECK.getCode());
        //校验编码是否重复
//        if (codeExist(contractDO.getId(), contractDO.getCode())) {
//            throw exception(ErrorCodeConstants.CODE_EXISTS);
//        }
        //合同是否上传成功 以及 附件是否上传成功
        FileDTO fileDTO = fileApi.selectById(contractCreateReqVO.getPdfFileId());
        if (ObjectUtil.isEmpty(fileDTO)) {
            throw exception(ErrorCodeConstants.SYSTEM_ERROR, "合同上传失败");
        }
//        List<Long> collect = contractCreateReqVO.getAttachmentList().stream().map(AttachmentRelCreateReqVO::getAttachmentAddId).collect(Collectors.toList());
//        List<FileDTO> fileDTOS = fileApi.selectBatchIds(collect);
//        if (fileDTOS.size() != collect.size()) {
//            throw exception(ErrorCodeConstants.SYSTEM_ERROR, "合同附件上传失败");
//        }
        //handlePart签署顺序
        if (ObjectUtil.isNotEmpty(contractCreateReqVO.getSignatoryList())) {
            handlePart(contractDO, contractCreateReqVO.getSignatoryList());
        }
        if (StringUtils.isEmpty(contractDO.getId())) {
            contractMapper.insert(contractDO);
        } else {
            contractMapper.update(contractDO, new LambdaQueryWrapperX<ContractDO>().eq(ContractDO::getId, contractDO.getId()));
        }
        return contractDO;
    }

    private ContractDO createContractByOrder(ContractCreateReqVO contractCreateReqVO) throws Exception {
        //先存合同
        ContractDO orderContractDO = null;
        String orderGuid = contractCreateReqVO.getOrderGuid();
        if (StringUtils.isNotEmpty(contractCreateReqVO.getId())) {
            orderContractDO = contractMapper.selectById(contractCreateReqVO.getId());
            orderGuid = orderContractDO == null ? null : orderContractDO.getOrderGuid();
        }
        ContractDO contractDO = ContractConverter.INSTANCE.toEntity(contractCreateReqVO);
        //存文件
        if (StringUtils.isNotEmpty(contractCreateReqVO.getContractContent())) {
            Long pdfId = this.toPdf(new ContractToPdfVO().setContent(contractCreateReqVO.getContractContent()).setName(contractCreateReqVO.getName()));
            contractDO.setPdfFileId(pdfId);
        }
        DraftOrderInfoDO orderInfoDO = gpMallOrderMapper.selectOne(new LambdaQueryWrapperX<DraftOrderInfoDO>().eq(DraftOrderInfoDO::getOrderGuid, orderGuid).last(" limit 1"));
        if (ObjectUtil.isEmpty(orderInfoDO)) {
            throw exception(ErrorCodeConstants.EMPTY_DATA_ERROR);
        }
        //查询签署方信息拼接
        String purchaserOrgGuid = orderInfoDO.getPurchaserOrgGuid();
        String supplierGuid = orderInfoDO.getSupplierGuid();
        List<Relative> relatives = relativeMapper.selectBatchIds(Arrays.asList(purchaserOrgGuid, supplierGuid));
        List<SignatoryRelReqVO> list = new ArrayList<>();
        //采购单位默认首签
        relatives.forEach(relative -> {
            SignatoryRelReqVO signatoryRelReqVO = new SignatoryRelReqVO();
            signatoryRelReqVO.setSignatoryId(relative.getId());
            signatoryRelReqVO.setSignatoryName(relative.getCompanyName());
            signatoryRelReqVO.setUserId(relative.getContactId());
            if (purchaserOrgGuid.equals(relative.getId())) {
                signatoryRelReqVO.setType(1);
                signatoryRelReqVO.setInitiator(true);
            } else {
                signatoryRelReqVO.setType(2);
                signatoryRelReqVO.setInitiator(false);
            }
            list.add(signatoryRelReqVO);
        });

        contractCreateReqVO.setSignatoryList(list);
        //校验编码是否重复
//        if (codeExist(contractDO.getId(), contractDO.getCode())) {
//            throw exception(ErrorCodeConstants.CODE_EXISTS);
//        }
        //设置发起方-采购人 和签署方-供应商
        contractDO.setPartAId(purchaserOrgGuid);
        contractDO.setPartAName(orderInfoDO.getPurchaserOrg());
        contractDO.setPartBId(supplierGuid);
        contractDO.setPartBName(orderInfoDO.getSupplierName());


        if (ObjectUtil.isEmpty(contractCreateReqVO.getInitiateApproval()) || contractCreateReqVO.getInitiateApproval() != 1) {
            //保存按钮进入，状态为待发送
            contractDO.setStatus(ContractStatusEnums.TO_BE_CHECK.getCode());
        } else {
            //将富文本保存为pdf存入minio返回文件id
            if (ObjectUtils.isNotEmpty(contractCreateReqVO.getContractContent())) {
                Long pdfId = this.toPdf(new ContractToPdfVO().setContent(contractCreateReqVO.getContractContent()).setName(contractCreateReqVO.getName()));
                contractDO.setPdfFileId(pdfId);
            }
            //发送按钮进入，状态为已发送
            contractDO.setStatus(ContractStatusEnums.CHECKING.getCode());
        }

        //handlePart签署顺序
//        List<SystemConfigRespDTO> signSort = systemConfigApi.getConfigsByCKeys(Arrays.asList("sign_sort"));
//        if (ObjectUtil.isNotEmpty(signSort)) {
//            contractDO.setSignOrder(signSort.get(0).getCValue());
//        }
        handlePart(contractDO, contractCreateReqVO.getSignatoryList());
        //删除旧文件
        //添加新文件
        if (ObjectUtils.isNotEmpty(contractDO.getId())) {
            if (ObjectUtil.isNotEmpty(orderContractDO)) {
                try {
                    if (ObjectUtils.isNotEmpty(orderContractDO.getPdfFileId())) {
                        fileApi.deleteFile(orderContractDO.getPdfFileId());
                    }
                } catch (Exception e) {
                }
            }
            //编辑
            contractMapper.updateById(contractDO);
        } else {
            //新增
            //信息入合同表
            contractMapper.insert(contractDO);
        }
        //修改草拟合同状态为已草拟
        if (ObjectUtils.isNotEmpty(orderInfoDO)) {
            if (ObjectUtil.isNotEmpty(orderInfoDO) && ObjectUtil.isNotEmpty(orderInfoDO.getStatus())
                    && !GCYOrderStatusEnums.DRAFTED.getCode().equals(orderInfoDO.getStatus())) {
                orderInfoDO.setStatus(GCYOrderStatusEnums.DRAFTED.getCode());
                gpMallOrderMapper.updateById(orderInfoDO);
                Map<String, Object> bodyParam = new HashMap<>();
                bodyParam.put("client_id", clientId);
                bodyParam.put("client_secret", clientSecret);
                String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
                JSONObject jsonObject = JSONObject.parseObject(token);
                try {
                    DraftOrderInfo draftOrderInfo = new DraftOrderInfo();
                    draftOrderInfo.setOrderGuid(orderInfoDO.getOrderGuid()).setStatus(GCYOrderStatusEnums.DRAFTED.getCode());
                    String result = contractProcessApi.updateOrderStatus(jsonObject.getString("access_token"), draftOrderInfo);
                    JSONObject resultJson = JSONObject.parseObject(result);
                    if (!"0".equals(resultJson.getString("code"))) {
                        throw new RuntimeException(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("推送电子合同更新订单状态失败", e);
                }
            }
        }
        return contractDO;

    }

    public String submitOne(Long loginUserId, IdReqVO reqVO) {

        String contractId = reqVO.getId();
        ContractDO contractDO = contractMapper.selectById(contractId);

        if (ObjectUtil.isNull(contractDO)) {
            throw exception(EMPTY_DATA_ERROR);
        }
        //1.插入请求单
        //校验是否发起过审批
        ContractStatusEnums statusEnums = ContractStatusEnums.getInstance(contractDO.getStatus());
        if (ContractStatusEnums.TO_BE_SENT != statusEnums) {
            throw exception(ErrorCodeConstants.CONTRACT_BPM_EXISTS);
        }

        contractDO.setStatus(ContractStatusEnums.SENT.getCode());
        contractDO.setSendTime(LocalDateTime.now());

        // 2 发起申请 BPM
        // 2.1 流程变量
        Map<String, Object> processInstanceVariables = new HashMap<String, Object>(16);
        processInstanceVariables.put("contractId", contractId);
        //签署节点的保存
        processInstanceVariables.put(FlowableModelEnums.COMPANY_IDS_TAG.getKey(), contractDO.getSignOrder());
        Map<Integer, Long> companyIds = FlowableUtil.getIdsFromTextValue(contractDO.getSignOrder());
        Integer companyCount = companyIds.size();
        // 2.2 流程实例id
        BpmProcessInstanceCreateReqDTO bpmProcessInstanceCreateReqDTO = new BpmProcessInstanceCreateReqDTO().setVariables(processInstanceVariables).setBusinessKey(contractId);

        //如果是双方合同（4个节点）
        if (4 == companyCount) {
            bpmProcessInstanceCreateReqDTO.setProcessDefinitionKey(ActivityConfigurationEnum.ECMS_CONTRACT_BOTH.getDefinitionKey());
        }
        //如果是三方合同（6个节点）
        if (6 == companyCount) {
            bpmProcessInstanceCreateReqDTO.setProcessDefinitionKey(ActivityConfigurationEnum.ECMS_CONTRACT_TRIPARTITE.getDefinitionKey());
        }
        //将相应的流程 发起
        String processInstanceId = processInstanceApi.createProcessInstance(loginUserId, bpmProcessInstanceCreateReqDTO);
        contractDO.setProcessInstanceId(processInstanceId);
        contractMapper.updateById(contractDO);
        return contractDO.getProcessInstanceId();
    }

    private String fastPassOne(IdReqVO reqVO) {
        BpmContract bpmDO = new BpmContract().setId(IdUtil.simpleUUID())
                //绑定合同
                .setContractId(reqVO.getId())
                //直接自动通过
                .setResult(BpmProcessInstanceResultEnum.APPROVE.getResult());
        bpmContractMapper.insert(bpmDO);
        return "success";
    }

    @Override
    public UploadContractCreateReqVO getUploadContractById(String id) throws Exception {
//        if (ObjectUtil.isNotEmpty(id)) {
//            ContractDO orderContractDO = contractMapper.selectById(id);
//            if (ObjectUtil.isNotEmpty(orderContractDO)) {
//                UploadContractCreateReqVO uploadContractCreateReqVO = ContractConverter.INSTANCE.doConvert2UploadVo(orderContractDO);
//                GPMallPageRespVO orderAndGoodsByOrderId = getOrderAndGoodsByOrderId(uploadContractCreateReqVO.getOrderGuid());
//                uploadContractCreateReqVO.setGpmallPageRespVO(orderAndGoodsByOrderId);
//                //付款计划
//                List<PaymentScheduleDO> contractPaymentPlanDOList = paymentScheduleMapper.selectList(new LambdaQueryWrapperX<PaymentScheduleDO>().eq(PaymentScheduleDO::getContractId, id).orderByAsc(PaymentScheduleDO::getSort));
//                if (CollectionUtil.isNotEmpty(contractPaymentPlanDOList)) {
//                    uploadContractCreateReqVO.setPaymentScheduleVOList(PaymentScheduleConverter.INSTANCE.toPaymentScheduleVO(contractPaymentPlanDOList));
//                }
//                return uploadContractCreateReqVO;
//            }
//        }
        return null;
    }

    /**
     * 通过订单id获取订单信息和商品信息
     *
     * @param guid
     * @return
     */
    @Override
    public GPMallPageRespVO getOrderAndGoodsByOrderId(String guid) {
        if (ObjectUtil.isNotEmpty(guid)) {
            //订单信息
            DraftOrderInfoDO draftOrderInfoDO = gpMallOrderMapper.selectOne(new LambdaQueryWrapperX<DraftOrderInfoDO>().eq(DraftOrderInfoDO::getOrderGuid, guid).last(" limit 1"));
            GPMallPageRespVO mallPageRespVO = GPMallOrderConverter.INSTANCE.convertOrderDO2Resp(draftOrderInfoDO);
            if (ObjectUtils.isNotEmpty(mallPageRespVO)) {
                List<GoodsDO> goodsDOList = gpMallGoodsMapper.selectList(new LambdaQueryWrapperX<GoodsDO>().eq(GoodsDO::getOrderId, guid));
                List<GoodsRespVO> goodsRespVO = GPMallOrderConverter.INSTANCE.conertGoodsListDO2Resp(goodsDOList);
                mallPageRespVO.setGoodsRespVOList(goodsRespVO);
            }
            return mallPageRespVO;
        }
        return null;
    }

    /**
     * 修改签署状态为-签署完成 6
     *
     * @param uploadSignedContractVO
     */
    @Override
    public String uploadSignedContract(UploadSignedContractVO uploadSignedContractVO) {
        ContractDO contractDO = contractMapper.selectById(uploadSignedContractVO.getId());
        //contractDO.setId(uploadSignedContractVO.getId());
        // 如果合同原文件id为空或者是0，则将现在的文件id赋值给原文件id
        if (ObjectUtil.isEmpty(contractDO.getOldPdfFileId()) || Long.valueOf("0").equals(contractDO.getOldPdfFileId())) {
            contractDO.setOldPdfFileId(contractDO.getPdfFileId());
        }
        contractDO.setIsOfflineSign(IfNumEnums.YES.getCode());
        // 设置文件id为新上传的文件id
        contractDO.setPdfFileId(uploadSignedContractVO.getPdfFileId());
        //设置为签署完成
        contractDO.setStatus(ContractStatusEnums.SIGN_COMPLETED.getCode());
        contractDO.setIsSign(IfNumEnums.YES.getCode());
        //修改归档状态为0-待归档
        contractDO.setDocument(0);
        Long count = contractPerforMapper.selectCount(new LambdaQueryWrapperX<ContractPerformanceDO>().eqIfPresent(ContractPerformanceDO::getContractId, contractDO.getId()).eqIfPresent(ContractPerformanceDO::getCreator, String.valueOf(WebFrameworkUtils.getLoginUserId())));
        if (count == 0) {
            //将签署完成的合同信息放入合同履约表
            inserContractPerfor(contractDO, String.valueOf(WebFrameworkUtils.getLoginUserId()));
            inserContractPerfor(contractDO, contractDO.getCreator());
        }
        contractMapper.updateById(contractDO);
        return contractDO.getId();
    }

    @Override
    public DocInfoRespVO getDocInfo(String contractId) throws Exception {
        DocInfoRespVO respVO = new DocInfoRespVO();
        ContractDO contractDO = contractMapper.selectById(contractId);
        if (ObjectUtil.isNotNull(contractDO)) {
            String url = fileApi.getURL(contractDO.getFileAddId());
            if (StringUtils.isNotBlank(url)) {
                respVO.setUrl(url);
                respVO.setContractName(contractDO.getName());
            }
        }
        return respVO;
    }

    @Override
    public DocInfoRespVO getPdfUrl(String contractId) throws Exception {
        DocInfoRespVO result = new DocInfoRespVO();
        ContractDO contractDO = contractMapper.selectById(contractId);
        if (ObjectUtil.isNull(contractDO)) {
            throw exception(SYSTEM_ERROR, "该合同不存在.");
        }
        if (Long.valueOf(0).equals(contractDO.getFileAddId())) {
            return result;
        }
        result.setContractName(contractDO.getName());
        Long docFileId = contractDO.getFileAddId();
        Long pdfFileId = toPdf(new ContractToPdfVO().setFileAddId(docFileId).setName(contractDO.getName()));
        result.setFileId(pdfFileId);
        String url = fileApi.getURL(pdfFileId);
        if (StringUtils.isNotBlank(url)) {
            result.setUrl(url);
        }
        return result;
    }

    @Override
    public Long getTemplateCopyV2(String templateId) {
        return fileApi.createFileInfo(new FileDTO());
    }

    private void inserContractPerfor(ContractDO contractDO, String creator) {
        //将签署完成的合同信息放入合同履约表
        ContractPerformanceDO contractPerformance = new ContractPerformanceDO();
        if (StringUtils.isNotEmpty(creator)) {
            Long userId = Long.parseLong(creator);
            AdminUserRespDTO user = adminUserApi.getUser(userId);
            if (ObjectUtil.isNotNull(user)) {
                contractPerformance.setDeptId(user.getDeptId());
            }
        }

        contractPerformance.setContractCode(contractDO.getCode());
        contractPerformance.setContractId(contractDO.getId());
        contractPerformance.setContractName(contractDO.getName());
        contractPerformance.setSignFinishTime(new Date());
        contractPerformance.setContractTypeId(contractDO.getContractType());
        //合同履约状态为待建立履约
        contractPerformance.setContractStatus(ContractPerfEnums.WAIT_CREATE_PERFORMANCE.getCode());
        contractPerformance.setCreator(creator);
        //新增合同履约信息

        contractPerforMapper.insert(contractPerformance);
        //新增履约任务

    }

    @Override
    public void tradingSend(String id) throws Exception {
        bpmContractService.createProcessV1(getLoginUserId(), id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateOrgSinTime(String contractId) {
        //采购人已签署
        contractMapper.updateById(new ContractDO().setId(contractId).setIsSign(1));
        //添加采购人签章时间
        contractOrderExtMapper.updateById(new ContractOrderExtDO().setId(contractId).setOrgSinTime(new Date()));
    }

    @Override
    public GpxPrjAndGpMallNumVO getGpTaskNum() {

        // region 【查询gpmall订单数量】
        //合同起草方：采购人（1）/供应商（2）。默认为供应商
        Long fwgcMallNum = 0L;
        Long kjxyMallNum = 0L;
        Long xyddMallNum = 0L;
        Long dzmcMallNum = 0L;
        List<String> orderIdList = new ArrayList<>();
        List<ContractOrderExtDO> contractDOList = contractOrderExtMapper.selectList(new LambdaQueryWrapperX<ContractOrderExtDO>()
                .select(ContractOrderExtDO::getOrderId, ContractOrderExtDO::getPlatform)
                .notIn(ContractOrderExtDO::getStatus, CollectionUtil.newArrayList(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCELLATION.getCode(),
                        HLJContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode(), HLJContractStatusEnums.CONTRACT_AUDITSTATUS_DELETE.getCode())));
        if (ObjectUtil.isNotEmpty(contractDOList)) {
            orderIdList = contractDOList.stream().map(ContractOrderExtDO::getOrderId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        }

        LoginUser user = SecurityFrameworkUtils.getLoginUser();
        LambdaQueryWrapperX<DraftOrderInfoDO> qLambdaWrapper = new LambdaQueryWrapperX<DraftOrderInfoDO>();
        qLambdaWrapper.in(DraftOrderInfoDO::getStatus, Arrays.asList(com.yaoan.module.econtract.enums.gcy.gpmall.GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode(), com.yaoan.module.econtract.enums.gcy.gpmall.GCYOrderStatusEnums.DRAFTED.getCode()))
                .gt(DraftOrderInfoDO::getOrderStatus, 0)
                .le(DraftOrderInfoDO::getOrderStatus, 98);
        if (CollectionUtil.isNotEmpty(orderIdList)) {
            qLambdaWrapper.notIn(DraftOrderInfoDO::getOrderGuid, orderIdList);
        }
        //只看本单位的
        if (StringUtils.isNotBlank(user.getOrgId())) {
            qLambdaWrapper.eq(DraftOrderInfoDO::getPurchaserOrgGuid, user.getOrgId());
        }
        List<DraftOrderInfoDO> draftOrderInfoDOS = gpMallOrderMapper.selectList(qLambdaWrapper.select(DraftOrderInfoDO::getContractFrom, DraftOrderInfoDO::getStatus, DraftOrderInfoDO::getId, DraftOrderInfoDO::getContractDrafter));

        //查出服务工程超市订单数量
        fwgcMallNum = draftOrderInfoDOS.stream().filter(item -> PlatformEnums.ZHUBAJIE.getCode().equals(item.getContractFrom()) && com.yaoan.module.econtract.enums.gcy.gpmall.GCYOrderStatusEnums.WAITE_TO_DRAFT.getCode().equals(item.getStatus())).count();

        // 电子卖场订单数量
        dzmcMallNum = draftOrderInfoDOS.stream().filter(item -> PlatformEnums.JDMALL.getCode().equals(item.getContractFrom())).count();
        // 协议定点订单数量
        xyddMallNum = draftOrderInfoDOS.stream().filter(item -> PlatformEnums.GPMALL.getCode().equals(item.getContractFrom())).count();

        Integer finalContractDrafter = SecurityFrameworkUtils.getLoginUser().getType();
        if (ObjectUtil.isNotEmpty(finalContractDrafter)) {
            // 框架协议订单数量
            kjxyMallNum = draftOrderInfoDOS.stream().filter(item -> PlatformEnums.GP_GPFA.getCode().equals(item.getContractFrom())).count();
        }
        // endregion

        // region 【查询交易执行gpxproject订单数量】
        // 去除合同状态为草稿、已发送、已确认、待盖章、完成、待代理机构审核、未备案、备案中、已备案
        Long gpxPrjNum = 0L;
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
        List<TradingContractExtDO> tradingContractDOS = tradingContractExtMapper.selectList4GPX(excludedStatuses);
        List<String> idList = tradingContractDOS.stream().map(TradingContractExtDO::getBuyPlanPackageId)
                .filter(Objects::nonNull).collect(Collectors.toList());
        //联合采购：只能采购人起草
        LoginUser loginUser = SecurityFrameworkUtils.getLoginUser();
        //供应商用户
        if (ObjectUtil.isNotEmpty(loginUser) && "2".equals(String.valueOf(loginUser.getType()))) {
            LambdaQueryWrapperX<PackageInfoDO> mpjLambdaWrapper = new LambdaQueryWrapperX<PackageInfoDO>();
            mpjLambdaWrapper
//                .notIn(CollectionUtil.isNotEmpty(idList), PackageInfoDO::getPackageGuid, idList)
                    .eq(PackageInfoDO::getIsLost, 0)
                    .ne(PackageInfoDO::getHidden, 1);
            // 供应商不可见联合采购数据
            mpjLambdaWrapper.ne(PackageInfoDO::getBiddingMethodCode, BiddingMethodEnums.UNION.getCode());
            gpxPrjNum = packageInfoMapper.selectCount(mpjLambdaWrapper);
        } else {
            String orgId = user.getOrgId();
            LambdaQueryWrapperX<PackageInfoDO> mpjLambdaWrapper = new LambdaQueryWrapperX<PackageInfoDO>();
            mpjLambdaWrapper
                    .notIn(CollectionUtil.isNotEmpty(idList), PackageInfoDO::getPackageGuid, idList)
                    .eq(PackageInfoDO::getIsLost, 0)
                    .ne(PackageInfoDO::getHidden, 1)
                    .orderByDesc(PackageInfoDO::getUpdateTime)
            ;
            if (StringUtils.isNotBlank(orgId)) {
                mpjLambdaWrapper.like(PackageInfoDO::getPurchaserOrgIds, orgId);
            }
            gpxPrjNum = packageInfoMapper.selectCount(mpjLambdaWrapper);
        }
        // endregion

        return new GpxPrjAndGpMallNumVO().setZhubajieNum(fwgcMallNum).setAgreementNum(kjxyMallNum).setJdMallNum(dzmcMallNum).setGpMallNum(xyddMallNum).setGpxPrjNum(gpxPrjNum);
    }

    private PageResult<ContractDO> selectAffirmPage(ContractPageReqVO contractPageReqVO, AdminUserRespDTO user) {
        // 相对方id
        String signId = contractPageReqVO.getRelativeId() != null ? contractPageReqVO.getRelativeId() : CompanyEnums.SUBMITTER.getCode();

        MPJQueryWrapper<ContractDO> mpjQueryWrapper = new MPJQueryWrapper<ContractDO>();
        if (!CompanyEnums.SUBMITTER.getCode().equals(signId)) {
            mpjQueryWrapper
                    .select(ContractDO.class, info ->
                            !"contract_content".equals(info.getColumn()));
        }
        addSelected(mpjQueryWrapper, contractPageReqVO);
        if (ObjectUtil.isNotEmpty(user)) {
            List<Relative> relatives = relativeMapper.selectList4Relative(user.getId());
            if (CollectionUtil.isNotEmpty(relatives)) {
                List<String> relativeIds = relatives.stream().map(Relative::getId).collect(Collectors.toList());
                List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getSignatoryId, relativeIds);
                List<String> contractIds = signatoryRelDOS.stream().map(SignatoryRelDO::getContractId).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(contractIds)) {
                    mpjQueryWrapper.and(w -> w.eq("t.dept_id", user.getDeptId()).or().lambda().in(ContractDO::getId, contractIds));
                } else {
                    mpjQueryWrapper.and(w -> w.eq("t.dept_id", user.getDeptId()));
                }
            } else {
                mpjQueryWrapper.and(w -> w.eq("t.dept_id", user.getDeptId()));
            }
        }
        return contractMapper.selectPage(contractPageReqVO, mpjQueryWrapper);
    }

    // 获取待签署合同列表接口
    private PageResult<ContractDO> selectWaitSignPage(ContractPageReqVO contractPageReqVO, AdminUserRespDTO user) {

        // 相对方id
        String signId = contractPageReqVO.getRelativeId() != null ? contractPageReqVO.getRelativeId() : CompanyEnums.SUBMITTER.getCode();

        MPJQueryWrapper<ContractDO> mpjQueryWrapper = new MPJQueryWrapper<ContractDO>();
        if (!CompanyEnums.SUBMITTER.getCode().equals(signId)) {
            mpjQueryWrapper
                    .select(ContractDO.class, info ->
                            !"contract_content".equals(info.getColumn()));
        }
        addSelected(mpjQueryWrapper, contractPageReqVO);

        // 待签署的都得有任务id
        if (CollectionUtil.isEmpty(contractPageReqVO.getProcessInstanceIds())) {
            // 把当前人所在相对方的联系人的签署任务也查出来
            List<Long> userIds = new ArrayList<>(Arrays.asList(getLoginUserId()));
            // 从空间获取相对方信息
            String key4Space = SecurityFrameworkUtils.getLoginUserKey4Space();
            if (StringUtils.isBlank(key4Space)) {
                return PageResult.empty();
            }
            String relativeId = redisUtils.get(key4Space);
            if (StringUtils.isBlank(relativeId)) {
                return PageResult.empty();
            }
            Relative relative = relativeMapper.selectById(relativeId);
            if (ObjectUtil.isNotEmpty(relative)) {
//                Long oneDefaultContactId = relativeService.getOneDefaultContactId(relative.getId());
                Long oneDefaultContactId = relative.getVirtualId();
                if (!getLoginUserId().equals(oneDefaultContactId)) {
                    userIds.add(oneDefaultContactId);
                }
            }
            List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllTODORelationProcessInstanceInfosByProcessDefinitionKeys(userIds, new ArrayList<>(Arrays.asList(PROCESS_KEY_BOTH_OLD, PROCESS_KEY_TRIPARTITE_NOT, PROCESS_KEY_BOTH, PROCESS_KEY_TRIPARTITE, PROCESS_KEY_MANY)), SIGN);
            List<String> processInstanceIds = CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
            contractPageReqVO.setProcessInstanceIds(processInstanceIds);
        }
        if (CollectionUtil.isNotEmpty(contractPageReqVO.getProcessInstanceIds())) {
            mpjQueryWrapper.lambda().and(el -> {
                el.in(ContractDO::getProcessInstanceId, contractPageReqVO.getProcessInstanceIds()).or().in(ContractDO::getUpload, Arrays.asList(ContractUploadTypeEnums.ORDER_DRAFT.getCode(), THIRD_PARTY.getCode()));
            });
        }

        if (ObjectUtil.isNotEmpty(user)) {
            List<Relative> relatives = relativeMapper.selectList4Relative(user.getId());
            // 逻辑调整，不仅查自己的单位id，也查当前用户的角色数据权限对应的deptId
            List<RoleRespDTO> roles = roleApi.getRoles(user.getId());
            List<Long> deptIds = new ArrayList<>();
            for (RoleRespDTO role : roles) {
                if (CollectionUtil.isNotEmpty(role.getDataScopeDeptIds())) {
                    deptIds.addAll(role.getDataScopeDeptIds());
                }
            }
            deptIds.add(user.getDeptId());
            CollectionUtil.distinct(deptIds);
            if (CollectionUtil.isNotEmpty(relatives) && CollectionUtil.isNotEmpty(contractPageReqVO.getProcessInstanceIds())) {
                List<String> relativeIds = relatives.stream().map(Relative::getId).collect(Collectors.toList());
                List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getSignatoryId, relativeIds);
                List<String> contractIds = signatoryRelDOS.stream().filter(e -> IfNumEnums.NO.getCode().equals(e.getIsSign())).map(SignatoryRelDO::getContractId).collect(Collectors.toList());

                //当前用户作为相对方的合同id集合
                mpjQueryWrapper.lambda().and(el -> {
                    // 查询未签署的合同，且按dept和相对方集合查询当前用户相关的数据
                    el.in(ContractDO::getStatus, Arrays.asList(ContractStatusEnums.TO_BE_SIGNED.getCode(), ContractStatusEnums.SIGN_REJECTED.getCode())).and(mx -> {
                                if (CollectionUtil.isNotEmpty(contractIds)) {
                                    // 如果相对方未签署列表不为空，那么除了查本单位未签署的合同，还要查已签署但相对方未签署的合同
                                    mx.and(w -> w.in(DeptBaseDO::getDeptId, deptIds).eq(ContractDO::getIsSign, IfNumEnums.NO.getCode())).or(i -> i.in(ContractDO::getId, contractIds)/*.and(sta -> sta.eq(ContractDO::getIsSign, IfNumEnums.YES.getCode()).or().in(ContractDO::getStatus, Arrays.asList(ContractStatusEnums.SIGN_REJECTED.getCode(), ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode())))*/);
                                } else {
                                    mx.eq(ContractDO::getIsSign, IfNumEnums.NO.getCode()).and(w -> w.in(DeptBaseDO::getDeptId, deptIds));
                                }

                            }
                    );
                });
            } else {
                //当前用户不存在作为相对方的数据，只是主体，只查未签署的合同，并按部门筛选
                mpjQueryWrapper.lambda().in(ContractDO::getStatus, Arrays.asList(ContractStatusEnums.TO_BE_SIGNED.getCode(), ContractStatusEnums.SIGN_REJECTED.getCode())).eq(ContractDO::getIsSign, IfNumEnums.NO.getCode()).in(DeptBaseDO::getDeptId, deptIds);
            }
        }
        return contractMapper.selectPage(contractPageReqVO, mpjQueryWrapper);
    }


    // 获取待确认合同列表接口
    private PageResult<ContractDO> selectWaitConfirmPage(ContractPageReqVO contractPageReqVO, AdminUserRespDTO user) {

        // 相对方id
        String signId = contractPageReqVO.getRelativeId() != null ? contractPageReqVO.getRelativeId() : CompanyEnums.SUBMITTER.getCode();

        MPJQueryWrapper<ContractDO> mpjQueryWrapper = new MPJQueryWrapper<ContractDO>();
        if (!CompanyEnums.SUBMITTER.getCode().equals(signId)) {
            mpjQueryWrapper
                    .select(ContractDO.class, info ->
                            !"contract_content".equals(info.getColumn()));
        }
        addSelected(mpjQueryWrapper, contractPageReqVO);

        if (CollectionUtil.isNotEmpty(contractPageReqVO.getProcessInstanceIds())) {
            mpjQueryWrapper.lambda().in(ContractDO::getProcessInstanceId, contractPageReqVO.getProcessInstanceIds());
        }
        if (ObjectUtil.isNotEmpty(user)) {
            List<Relative> relatives = relativeMapper.selectList4Relative(user.getId());
            // 逻辑调整，不仅查自己的单位id，也查当前用户的角色数据权限对应的deptId
            List<RoleRespDTO> roles = roleApi.getRoles(user.getId());
            List<Long> deptIds = new ArrayList<>();
            for (RoleRespDTO role : roles) {
                if (CollectionUtil.isNotEmpty(role.getDataScopeDeptIds())) {
                    deptIds.addAll(role.getDataScopeDeptIds());
                }
            }
            deptIds.add(user.getDeptId());
            CollectionUtil.distinct(deptIds);
            if (CollectionUtil.isNotEmpty(relatives) && CollectionUtil.isNotEmpty(contractPageReqVO.getProcessInstanceIds())) {
                List<String> relativeIds = relatives.stream().map(Relative::getId).collect(Collectors.toList());
                List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getSignatoryId, relativeIds);
                List<String> contractIds = signatoryRelDOS.stream().filter(e -> IfNumEnums.NO.getCode().equals(e.getIsConfirm())).map(SignatoryRelDO::getContractId).collect(Collectors.toList());

                //当前用户作为相对方的合同id集合
                mpjQueryWrapper.lambda().eq(ContractDO::getStatus, ContractStatusEnums.SENT.getCode()).and(el -> {
                    // 查询未签署的合同，且按dept和相对方集合查询当前用户相关的数据
                    el.in(DeptBaseDO::getDeptId, deptIds);
                    if (CollectionUtil.isNotEmpty(contractIds)) {
                        el.or().in(ContractDO::getId, contractIds);
                    }
                });
            } else {
                //当前用户不存在作为相对方的数据，只是主体，只查未签署的合同，并按部门筛选
                mpjQueryWrapper.lambda().eq(ContractDO::getIsSign, IfNumEnums.NO.getCode()).in(DeptBaseDO::getDeptId, deptIds);
            }
        }
        return contractMapper.selectPage(contractPageReqVO, mpjQueryWrapper);
    }

    // 获取已确认合同列表接口
    private PageResult<ContractDO> selectDoneConfirmPage(ContractPageReqVO contractPageReqVO, AdminUserRespDTO user) {

        // 相对方id
        String signId = contractPageReqVO.getRelativeId() != null ? contractPageReqVO.getRelativeId() : CompanyEnums.SUBMITTER.getCode();

        MPJQueryWrapper<ContractDO> mpjQueryWrapper = new MPJQueryWrapper<ContractDO>();
        if (!CompanyEnums.SUBMITTER.getCode().equals(signId)) {
            mpjQueryWrapper
                    .select(ContractDO.class, info ->
                            !"contract_content".equals(info.getColumn()));
        }
        addSelected(mpjQueryWrapper, contractPageReqVO);

        if (ObjectUtil.isNotEmpty(user)) {
            List<Relative> relatives = relativeMapper.selectList4Relative(user.getId());
            // 逻辑调整，不仅查自己的单位id，也查当前用户的角色数据权限对应的deptId
            List<RoleRespDTO> roles = roleApi.getRoles(user.getId());
            List<Long> deptIds = new ArrayList<>();
            for (RoleRespDTO role : roles) {
                if (CollectionUtil.isNotEmpty(role.getDataScopeDeptIds())) {
                    deptIds.addAll(role.getDataScopeDeptIds());
                }
            }
            deptIds.add(user.getDeptId());
            CollectionUtil.distinct(deptIds);
            if (CollectionUtil.isNotEmpty(relatives)) {
                List<String> relativeIds = relatives.stream().map(Relative::getId).collect(Collectors.toList());
                List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getSignatoryId, relativeIds);
                List<String> contractIds = signatoryRelDOS.stream().filter(e -> !IfNumEnums.NO.getCode().equals(e.getIsConfirm())).map(SignatoryRelDO::getContractId).collect(Collectors.toList());

                //当前用户作为相对方的合同id集合
                mpjQueryWrapper.lambda().and(el -> {
                    // 查询未签署的合同，且按dept和相对方集合查询当前用户相关的数据
                    el.and(mx -> {
                                if (CollectionUtil.isNotEmpty(contractIds)) {
                                    mx.and(i -> i.eq(ContractDO::getStatus, ContractStatusEnums.TO_BE_CONFIRMED.getCode()).in(DeptBaseDO::getDeptId, deptIds)).or().in(ContractDO::getId, contractIds);
                                } else {
                                    mx.eq(ContractDO::getStatus, ContractStatusEnums.TO_BE_CONFIRMED.getCode()).and(w -> w.in(DeptBaseDO::getDeptId, deptIds));
                                }

                            }
                    );
                });
            } else {
                //当前用户不存在作为相对方的数据，只是主体，只查未签署的合同，并按部门筛选
                mpjQueryWrapper.lambda().eq(ContractDO::getIsSign, IfNumEnums.NO.getCode()).in(DeptBaseDO::getDeptId, deptIds);
            }
        }
        return contractMapper.selectPage(contractPageReqVO, mpjQueryWrapper);
    }

    // 获取已签署合同列表接口
    private PageResult<ContractDO> selectSignedPage(ContractPageReqVO contractPageReqVO, AdminUserRespDTO user) {
        // 相对方id
        String signId = contractPageReqVO.getRelativeId() != null ? contractPageReqVO.getRelativeId() : CompanyEnums.SUBMITTER.getCode();

        MPJQueryWrapper<ContractDO> mpjQueryWrapper = new MPJQueryWrapper<ContractDO>();
        if (!CompanyEnums.SUBMITTER.getCode().equals(signId)) {
            mpjQueryWrapper
                    .select(ContractDO.class, info ->
                            !"contract_content".equals(info.getColumn()));
        }

        // 当前用户作为签署方-要查询的合同id集合
        addSelected(mpjQueryWrapper, contractPageReqVO);

        if (ObjectUtil.isNotEmpty(user)) {
            List<Relative> relatives = relativeMapper.selectList4Relative(user.getId());
            // 逻辑调整，不仅查自己的单位id，也查当前用户的角色数据权限对应的deptId
            List<RoleRespDTO> roles = roleApi.getRoles(user.getId());
            List<Long> deptIds = new ArrayList<>();
            for (RoleRespDTO role : roles) {
                if (CollectionUtil.isNotEmpty(role.getDataScopeDeptIds())) {
                    deptIds.addAll(role.getDataScopeDeptIds());
                }
            }
            deptIds.add(user.getDeptId());
            CollectionUtil.distinct(deptIds);

            if (CollectionUtil.isNotEmpty(relatives)) {
                List<String> relativeIds = relatives.stream().map(Relative::getId).collect(Collectors.toList());
                List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getSignatoryId, relativeIds);
                List<String> contractIds = signatoryRelDOS.stream().filter(e -> !IfNumEnums.NO.getCode().equals(e.getIsSign())).map(SignatoryRelDO::getContractId).collect(Collectors.toList());

                // 当前用户作为签署方的合同不为空
                if (CollectionUtil.isNotEmpty(contractIds)) {
                    mpjQueryWrapper.lambda().and(wp -> wp.and(sta -> {
//                    // 先查询合同状态为已签署的合同，并按dept和相对方合同id筛选
//                    sta.eq(ContractDO::getStatus, ContractStatusEnums.SIGN_COMPLETED.getCode()).and(wz -> {
//                        wz.in(DeptBaseDO::getDeptId, deptIds).or().in(ContractDO::getId, contractIds);
//                    });
                        // 作为相对方且签署状态为已签署的合同
                        sta.in(ContractDO::getId, contractIds);
                    })).or(el -> {
                        // 再筛选当前用户作为发起用户，签署状态为待签署或取消，且签署状态为已签署的数据。 再按部门筛选
                        el.in(ContractDO::getStatus, Arrays.asList(ContractStatusEnums.TO_BE_SIGNED.getCode(), ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode())).eq(ContractDO::getIsSign, IfNumEnums.YES.getCode()).eq(DeptBaseDO::getDeptId, user.getDeptId());
                        el.notIn(ContractDO::getId, contractIds);
                    });
                } else {
                    mpjQueryWrapper.lambda().and(wp -> wp.and(sta -> {
                        sta.in(ContractDO::getStatus, Arrays.asList(ContractStatusEnums.TO_BE_SIGNED.getCode(), ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode())).
                                eq(ContractDO::getIsSign, IfNumEnums.YES.getCode());
                    }).or().in(ContractDO::getStatus, Arrays.asList(ContractStatusEnums.SIGN_COMPLETED.getCode(), ContractStatusEnums.SIGN_REJECTED.getCode())));
                    mpjQueryWrapper.lambda().in(DeptBaseDO::getDeptId, deptIds);
                }
            } else {
                //相对方不存在时，当前用户只是合同发起方，筛选合同状态为已签署，或者签署状态为待签署/已取消，且签署状态为已签署的数据。 再按部门筛选
                mpjQueryWrapper.lambda().and(wp -> wp.and(sta -> {
                    sta.in(ContractDO::getStatus, Arrays.asList(ContractStatusEnums.TO_BE_SIGNED.getCode(), ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode())).
                            eq(ContractDO::getIsSign, IfNumEnums.YES.getCode());
                }).or().in(ContractDO::getStatus, Arrays.asList(ContractStatusEnums.SIGN_COMPLETED.getCode(), ContractStatusEnums.SIGN_REJECTED.getCode())));
                mpjQueryWrapper.lambda().in(DeptBaseDO::getDeptId, deptIds);
            }
        }
        return contractMapper.selectPage(contractPageReqVO, mpjQueryWrapper);
    }

    @Override
    public void update0(String contractId) {
        ContractDO contractDO = contractMapper.selectById(contractId);
        if (ObjectUtil.isNull(contractDO)) {
            throw exception(DIY_ERROR, "该合同不存在。");
        }
        contractDO.setId(contractId).setStatus(0).setIsOffline(IfNumEnums.YES.getCode());

        //转pdf
        try {
            if (Long.valueOf(0).equals(contractDO.getPdfFileId())) {
                //使用黑龙江模式，使用合同富文本转pdf
//                            List<FileVersionDO> fileVersionDOList = fileVersionMapper.selectList(new LambdaQueryWrapperX<FileVersionDO>().eq(FileVersionDO::getBusinessId,contractDO.getId()).orderByAsc(FileVersionDO::getId));
                Long fileAddId = contractDO.getFileAddId();

                Long pdfFileId = toPdf(new ContractToPdfVO().setFileAddId(fileAddId).setName(contractDO.getName()).setContent(StringUtils.toEncodedString(contractDO.getContractContent(), StandardCharsets.UTF_8)).setName(contractDO.getName()));
                contractDO.setPdfFileId(pdfFileId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("电子合同转PDF失败!");
            throw exception(SYSTEM_ERROR, "电子合同转PDF失败!失败原因：" + e.getMessage());
        }
        contractMapper.updateById(contractDO);
        contractOrderExtMapper.updateById(new ContractOrderExtDO().setId(contractId).setStatus(0));
    }

    @Override
    public void update3(IdReqVO vo) {
        ContractDO contractDO = contractMapper.selectById(vo.getContractId());
        ContractDO updateContractDO = new ContractDO().setId(vo.getContractId()).setStatus(ContractStatusEnums.TO_BE_SENT.getCode()).setIsOffline(IfNumEnums.YES.getCode());
        //转pdf
        try {
            if (Long.valueOf(0).equals(contractDO.getPdfFileId())) {
                //使用黑龙江模式，使用合同富文本转pdf
//                            List<FileVersionDO> fileVersionDOList = fileVersionMapper.selectList(new LambdaQueryWrapperX<FileVersionDO>().eq(FileVersionDO::getBusinessId,contractDO.getId()).orderByAsc(FileVersionDO::getId));
                Long fileAddId = contractDO.getFileAddId();

                Long pdfFileId = toPdf(new ContractToPdfVO().setFileAddId(fileAddId).setName(contractDO.getName()).setContent(StringUtils.toEncodedString(contractDO.getContractContent(), StandardCharsets.UTF_8)).setName(contractDO.getName()));
                updateContractDO.setPdfFileId(pdfFileId);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("电子合同转PDF失败!");
            throw exception(SYSTEM_ERROR, "电子合同转PDF失败!失败原因：" + e.getMessage());
        }

        offlineFileMapper.insert(new OfflineFileDO().setBusinessType(1).setBusinessId(vo.getContractId()).setFileId(vo.getFileId()));

        contractMapper.updateById(updateContractDO);
        //交易比较特殊，由于没有确认环节，此处直接发送
        if (PlatformEnums.GPMS_GPX.getCode().equals(contractDO.getPlatform())) {
            contractApi.productApproveSendEcms("", contractDO.getId());
        }

    }

    @Override
    public void updateStat(String contractId) {
        ContractDO contractDO = contractMapper.selectById(contractId);
        if (ObjectUtil.isNull(contractDO)) {
            throw exception(DIY_ERROR, "该合同不存在。");
        }
        // 修改签署状态为已处理
        ContractDO updateContractDO = new ContractDO().setId(contractId).setStatus(ContractStatusEnums.CONTRACT_AUDITSTATUS_CANCEL.getCode()).setIsSign(IfNumEnums.YES.getCode());
        contractMapper.updateById(updateContractDO);
    }

    @Override
    public void downloadPDF4Contract(HttpServletResponse response, String contractId) throws Exception {
        // 获取范本的富文本
        ContractDO contractDO = contractMapper.selectById(contractId);
        if (ObjectUtil.isNull(contractDO)) {
            throw exception(SYSTEM_ERROR, "合同不存在");
        }
        Long downloadFileId = 0L;
        if (ObjectUtil.isNotNull(contractDO.getPdfFileId()) && 0 != contractDO.getPdfFileId()) {
            downloadFileId = contractDO.getPdfFileId();
        } else {
            downloadFileId = toPdf(new ContractToPdfVO().setName(contractDO.getName())
                    .setContent(StringUtils.toEncodedString(Optional.ofNullable(contractDO.getContractContent())
                            .orElse(new byte[0]), StandardCharsets.UTF_8)).setFileAddId(contractDO.getFileAddId()));
        }

        // 下载
        fileApi.getFileContentByFileId(response, downloadFileId);
    }


    // 根据来源contractFrom，合同状态status,是否需要签章isNeedSignet，起草方upload:3采购单位4供应商，返回状态名称
    private String getStatusName(String contractFrom, Integer status, boolean isNeedSignet, Integer upload, Integer isSign, Integer isFillings) {
        // 如果是交易起草
        if (StringUtils.equals(contractFrom, PlatformEnums.GPMS_GPX.getCode())) {
//            if (CollectionUtil.newArrayList(ContractStatusEnums.TO_BE_CHECK.getCode(), ContractStatusEnums.CHECK_REJECTED.getCode(), ContractStatusEnums.APPROVE_BACK.getCode()).contains(status)) {
//                return "草稿";
//            }
            if (isNeedSignet && ContractStatusEnums.CONTRACT_AUDITSTATUS_NOT_SIGNED.getCode().equals(status)) {
                return "待发起用印";
            }
            if (isNeedSignet && ContractStatusEnums.CONTRACT_AUDITSTATUS_SEAL_APPROVAL.getCode().equals(status)) {
                return "用印审批中";
            }
//            if (IfNumEnums.NO.getCode().equals(isSign) && ContractStatusEnums.TO_BE_SIGNED.getCode().equals(status)) {
//                return "待签署";
//            }
//            if (IfNumEnums.YES.getCode().equals(isSign) && ContractStatusEnums.TO_BE_SIGNED.getCode().equals(status)) {
//                return "待供应商签署";
//            }
        } else {
            // 如果是服务工程超市-采购单位起草
            if (CollectionUtil.newArrayList(ContractUploadTypeEnums.ORDER_DRAFT.getCode(), ContractUploadTypeEnums.MODEL_DRAFT.getCode(), ContractUploadTypeEnums.UPLOAD_FILE.getCode(), ContractUploadTypeEnums.COMPANY_LEVEL.getCode(), ContractUploadTypeEnums.UPLOAD_CONTRACT_FILE.getCode()).contains(upload)) {
//                if (CollectionUtil.newArrayList(ContractStatusEnums.TO_BE_CHECK.getCode(), ContractStatusEnums.CHECK_REJECTED.getCode(), ContractStatusEnums.APPROVE_BACK.getCode()).contains(status)) {
//                    return "草稿";
//                }
//                if (ContractStatusEnums.TO_BE_SENT.getCode().equals(status) || ContractStatusEnums.BE_SENT_BACK.getCode().equals(status)) {
//                    return "待发送供应商确认";
//                }
//                if (ContractStatusEnums.SENT.getCode().equals(status)) {
//                    return "待供应商确认";
//                }
                if (isNeedSignet && ContractStatusEnums.CONTRACT_AUDITSTATUS_NOT_SIGNED.getCode().equals(status)) {
                    return "待发起用印";
                }
                if (isNeedSignet && ContractStatusEnums.CONTRACT_AUDITSTATUS_SEAL_APPROVAL.getCode().equals(status)) {
                    return "用印审批中";
                }
//                if (IfNumEnums.NO.getCode().equals(isSign) && ContractStatusEnums.TO_BE_SIGNED.getCode().equals(status)) {
//                    return "待签署";
//                }
//                if (IfNumEnums.YES.getCode().equals(isSign) && ContractStatusEnums.TO_BE_SIGNED.getCode().equals(status)) {
//                    return "待供应商签署";
//                }
            }
            // 如果是供应商起草
            if (THIRD_PARTY.getCode().equals(upload)) {
//                if (ContractStatusEnums.TO_BE_CHECK.getCode().equals(status)) {
//                    return "待送审";
//                }
//                if (ContractStatusEnums.TO_BE_SENT.getCode().equals(status) || ContractStatusEnums.BE_SENT_BACK.getCode().equals(status)) {
//                    return "待发供应商签署";
//                }
//                if (ContractStatusEnums.SENT.getCode().equals(status)) {
//                    return "待供应商签署";
//                }
                // 如果是用印待签署状态
                if (isNeedSignet && ContractStatusEnums.CONTRACT_AUDITSTATUS_NOT_SIGNED.getCode().equals(status)) {
                    return "待发起用印";
                }
                if (isNeedSignet && ContractStatusEnums.CONTRACT_AUDITSTATUS_SEAL_APPROVAL.getCode().equals(status)) {
                    return "用印审批中";
                }
//                if (ContractStatusEnums.TO_BE_SIGNED.getCode().equals(status)) {
//                    return "待签署";
//                }
            }
        }
        // 如果合同签署完成
//        if (CollectionUtil.newArrayList(ContractStatusEnums.SIGN_COMPLETED.getCode(), ContractStatusEnums.PERFORMANCE_CLOSURE.getCode(), ContractStatusEnums.PERFORMING.getCode(), ContractStatusEnums.PERFORMANCE_COMPLETE.getCode()
//                , ContractStatusEnums.PERFORMANCE_RISK.getCode(), ContractStatusEnums.PERFORMANCE_RISK_DISPUTE.getCode(), ContractStatusEnums.PERFORMANCE_RISK_PAUSE.getCode(), ContractStatusEnums.PERFORMANCE_RISK_EXTENSION.getCode(), ContractStatusEnums.PERFORMANCE_RISK_OVERDUE.getCode()).contains(status)) {
//            // 待备案
//            if (ContractFilingsStatusEnums.FILINGS_WAIT.getCode().equals(isFillings)) {
//                return ContractFilingsStatusEnums.FILINGS_WAIT.getDesc();
//            }
//            // 备案中
//            if (ContractFilingsStatusEnums.FILINGS_ING.getCode().equals(isFillings)) {
//                return ContractFilingsStatusEnums.FILINGS_ING.getDesc();
//            }
//            // 已备案
//            if (ContractFilingsStatusEnums.FILINGS_DONE.getCode().equals(isFillings)) {
//                return ContractFilingsStatusEnums.FILINGS_DONE.getDesc();
//            }
//        }
        return null;
    }

    private void selectAllChildTypes(List parentIds, List typeIds) {
        typeIds.addAll(parentIds);
        List<ContractType> list = contractTypeMapper.selectList(ContractType::getParentId, parentIds);
        if (CollectionUtil.isNotEmpty(list)) {
            List<String> ids = list.stream().map(getId -> getId.getId()).collect(Collectors.toList());
            selectAllChildTypes(ids, typeIds);
        }
    }

    private void checkMustInput(ContractCreateReqVO vo) {
        if (StringUtils.isEmpty(vo.getCode())) {
            throw exception(DIY_ERROR, "合同编号不能为空");
        }
        if (ObjectUtil.isEmpty(vo.getAmount())) {
            throw exception(DIY_ERROR, "合同金额不能为空");
        }
        if (ObjectUtil.isEmpty(vo.getAmount())) {
            throw exception(DIY_ERROR, "合同金额不能为空");
        }
    }

    private void addSelected(MPJQueryWrapper<ContractDO> mpjQueryWrapper, ContractPageReqVO contractPageReqVO) {
        mpjQueryWrapper.lambda().orderByDesc(ContractDO::getUpdateTime)
                //排除富文本字段
                .select(ContractDO.class, info ->
                        !"contract_content".equals(info.getColumn())).select("t.id");

        if (CollectionUtil.isNotEmpty(contractPageReqVO.getContractIdList())) {
            mpjQueryWrapper.lambda().in(ContractDO::getId, contractPageReqVO.getContractIdList());
        }
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getContractCategory())) {
            mpjQueryWrapper.lambda().like(ContractDO::getContractCategory, contractPageReqVO.getContractCategory());
        }
        if (StringUtils.isNotEmpty(contractPageReqVO.getCode())) {
            mpjQueryWrapper.lambda().like(ContractDO::getCode, contractPageReqVO.getCode());
        }
        if (StringUtils.isNotEmpty(contractPageReqVO.getName())) {
            mpjQueryWrapper.lambda().like(ContractDO::getName, contractPageReqVO.getName());
        }
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getExpirationDate0()) && ObjectUtil.isNotEmpty(contractPageReqVO.getExpirationDate1())) {
            mpjQueryWrapper.lambda().between(ContractDO::getExpirationDate, contractPageReqVO.getExpirationDate0().toDateStr(), contractPageReqVO.getExpirationDate1().toDateStr());
        }
        if (StringUtils.isNotEmpty(contractPageReqVO.getContractType())) {
            mpjQueryWrapper.lambda().eq(ContractDO::getContractType, contractPageReqVO.getContractType());
        }
        if (CollectionUtil.isNotEmpty(contractPageReqVO.getContractTypes())) {
            mpjQueryWrapper.lambda().in(ContractDO::getContractType, contractPageReqVO.getContractTypes());
        }
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getContractSourceType())) {
            mpjQueryWrapper.lambda().eq(ContractDO::getContractSourceType, contractPageReqVO.getContractSourceType());
        }
        if (CollectionUtil.isNotEmpty(contractPageReqVO.getContractSourceTypes())) {
            mpjQueryWrapper.lambda().in(ContractDO::getContractSourceType, contractPageReqVO.getContractSourceTypes());
        }
        // upload
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getUploadList())) {
            mpjQueryWrapper.lambda().in(ContractDO::getUpload, contractPageReqVO.getUploadList());
        }
        //合同管理-合同签署 搜索条件 -归档-
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getDocument())) {
            mpjQueryWrapper.lambda().eq(ContractDO::getDocument, contractPageReqVO.getDocument());
        }

        if (CollectionUtil.isNotEmpty(contractPageReqVO.getStatusList())) {
            mpjQueryWrapper.lambda().in(ContractDO::getStatus, contractPageReqVO.getStatusList());
        }
        if (ObjectUtil.isNotEmpty(contractPageReqVO.getStatus())) {
            mpjQueryWrapper.lambda().eq(ContractDO::getStatus, contractPageReqVO.getStatus());
        }
        // 如果相对方不为空
        if (StringUtils.isNotEmpty(contractPageReqVO.getRelativeName())) {

            MPJLambdaWrapper<Relative> mpjLambdaWrapper = new MPJLambdaWrapper<>();
            mpjLambdaWrapper.like(Relative::getName, contractPageReqVO.getRelativeName()).or().like(Relative::getContactName, contractPageReqVO.getRelativeName()).or().like(Relative::getCompanyName, contractPageReqVO.getRelativeName());
            List<Relative> relatives = relativeMapper.selectList(mpjLambdaWrapper);
            if (CollectionUtil.isNotEmpty(relatives)) {
                List<String> relativeIds = relatives.stream().map(Relative::getId).collect(Collectors.toList());
                List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getSignatoryId, relativeIds);
                List<String> contractIds = signatoryRelDOS.stream().map(SignatoryRelDO::getContractId).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(contractIds)) {
                    mpjQueryWrapper.lambda().in(ContractDO::getId, contractIds);
                }
            }
        }
    }
}
