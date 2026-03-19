package com.yaoan.module.econtract.service.contract;

import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.module.econtract.controller.admin.contract.vo.*;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.RecentUseModelVO;
import com.yaoan.module.econtract.controller.admin.order.vo.GPMallPageRespVO;
import com.yaoan.module.econtract.controller.admin.outward.contract.VO.*;
import com.yaoan.module.econtract.controller.admin.relative.vo.RelativeByUserRespVO;
import com.yaoan.module.econtract.controller.admin.sign.vo.VerificationRespVO;
import com.yaoan.module.econtract.enums.ContractStatusEnums;
import com.yaoan.module.infra.enums.FileUploadPathEnum;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;

public interface ContractService {

    /**
     * 付款管理列表展示-金额数据
     */
    PaymentAmount getPayment(PaymentSchedulePageReqVO paymentSchedulePageReqVO);

    /**
     * 付款管理列表展示-分页数据
     */
    PageResult<PaymentSchedulePageRespVO> getPaymentPage(PaymentSchedulePageReqVO paymentSchedulePageReqVO);

    /**
     * 根据合同id 支付计划
     * @param contractId
     * @return
     */
    List<PaymentScheduleRespVO> getPaymentSchedule(String contractId);

    /**
     * 项目采购数据列表展示
     */
    PageResult<ProjectPurchasingRespVO> getProjectPurchasingPage(PageParam pageParam);

    /**
     * 模板id生成副本文件（项目采购-合同草拟）
     * @param templateId
     * @return
     * @throws Exception
     */
    Long getTemplateCopy(String templateId) throws Exception ;

    /**
     * 项目采购数据查看详情
     * @param id
     * @return
     */
    ProjectPurchasingRespVO getProjectPurchasingById(String id);

    /**
     * 合同管理 - 合同草拟
     * 登录用户作为  -发起人-
     * 合同状态为  -新增|被退回-
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractPageRespVO> getSentPage(ContractPageReqVO contractPageReqVO);

    /**
     * 合同管理 - 合同草拟
     * 登录用户作为  -发起人-
     * 合同状态为   -已发送-
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractPageRespVO> getSendPage(ContractPageReqVO contractPageReqVO);

    /**
     * 合同管理 - 合同草拟
     * 登录用户作为  -发起人-
     * 合同状态为   -待确认-   （待发起签署）
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractPageRespVO> getConfirmPage(ContractPageReqVO contractPageReqVO);


    /**
     * 合同确认-查询合同列表
     * 签署方=登录用户
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractPageRespVO> getAffirmPage(ContractPageReqVO contractPageReqVO);

    /**
     * 合同管理 - 合同签署
     * 登录用户作为  -签署方-
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractPageRespVO> getSignPage(ContractPageReqVO contractPageReqVO);
    /**
     * 合同管理 - 合同签署
     * 待签署
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractPageRespVO> getWaitSignPage(ContractPageReqVO contractPageReqVO);
    /**
     * 合同管理 - 合同确认
     * 待确认
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractPageRespVO> getWaitConfirmPage(ContractPageReqVO contractPageReqVO);
    PageResult<ContractPageRespVO> getDoneConfirmPage(ContractPageReqVO contractPageReqVO);
    /**
     * 合同管理 - 合同签署
     * 已签署
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractPageRespVO> getDoneSignPage(ContractPageReqVO contractPageReqVO);
    /**
     * 合同管理 - 用印申请合同
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractPageRespVO> getSignetPage(ContractPageReqVO contractPageReqVO);
    /**
     * 合同管理 - 获取政采或非政采合同数量
     * @param type 请求查询类型： signet：用印、 sign: 签署
     * @return
     */
    ContractGovOrNotNumRespVO getGovOrNotNum(String type);

    /**
     * 归档管理-电子签署
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractPageRespVO> getFilingPage(ContractPageReqVO contractPageReqVO);

    /**
     * 草稿箱
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractPageRespVO> getDraftsPage(ContractPageReqVO contractPageReqVO);
    PageResult<ContractPageRespVO> getMyContractPage(ContractPageReqVO contractPageReqVO);

    /**
     * 待审核
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractPageRespVO> getCheckPage(ContractPageReqVO contractPageReqVO);

    /**
     * 待送审
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractPageRespVO> getToCheckPage(ContractPageReqVO contractPageReqVO);

    /**
     * 合同登记-补录
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractPageRespVO> getRegisterPage(ContractPageReqVO contractPageReqVO);

    /**
     * 上传合同
     * 创建人 当前登录用户
     * @param contractDocumentPageReqVO
     * @return
     */
    PageResult<ContractRespVO> getDocumentPage(ContractDocumentPageReqVO contractDocumentPageReqVO);


    /**
     * 合同管理 - 合同签署
     * 待签署
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractPageRespVO> getSignUnfinishedPage(ContractPageReqVO contractPageReqVO);

    /**
     * 合同管理 - 合同签署
     * 签署完成
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractPageRespVO> getSignFinishPage(ContractPageReqVO contractPageReqVO);


    /**
     * 合同管理 - 合同签署
     * 逾期未签署
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractPageRespVO> getSignOverduePage(ContractPageReqVO contractPageReqVO);

    /**
     * 合同审批中
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractPageRespVO> getAuditingPage(ContractPageReqVO contractPageReqVO);

   /**
     * 相对方已确认
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractPageRespVO> getConfirmedPage(ContractPageReqVO contractPageReqVO);

    /**
     * 合同创建(修改) 是否提交审批
     * @param contractCreateReqVO
     * @return
     */
    String createContract(ContractCreateReqVO contractCreateReqVO) throws Exception;

    String receiveContract(ContractReceiveVO contractReceiveVO) throws Exception ;

    String createOrUpdateContract(FileUploadContractCreateReqVO contractCreateReqVO) throws Exception ;
    /**
     * 合同创建基础版
     */
    String createContractBase(ContractCreateBaseReqVO contractCreateBaseReqVO) throws Exception;

    /**
     * 合同创建(修改) 验签 系统对接api
     * @param contractDraft
     * @return
     */
    String checkContractOW(ContractDraft contractDraft) throws UnsupportedEncodingException;

    /**
     * 合同创建(修改) 系统对接api
     * @param apiCreateReqVO
     * @return
     */
    String createContractOW(ApiCreateReqVO apiCreateReqVO) ;

    /**
     * 文件格式转换，富文本、doc(x) -> pdf 并上传minIO
     * @param contractToPdfVO
     * @return
     */
    Long toPdf(ContractToPdfVO contractToPdfVO) throws Exception;
    /**
     * 指定存储目录
     * @param contractToPdfVO
     * @param fileUploadPathEnum
     * @return
     * @throws Exception
     */
    Long toPdf(ContractToPdfVO contractToPdfVO, FileUploadPathEnum fileUploadPathEnum) throws Exception;


    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    HashMap<String, Long> toPdfById(String id) throws Exception;


    /**
     * 合同名称、编码，校验
     * @param name,code
     * @return
     */
    Boolean exist(String name,String code);

    /**
     * 待发送页面 - 新增  -  发送
     * 修改合同签署状态 - 发起工作流
     * @param id
     * @return
     */
    void updateToSend(String id);

    /**
     * 待确认页面 - 发起签署
     * 修改合同签署状态 - 发起工作流
     * @param contractBaseVO
     * @return
     */
    void updateStatus4(ContractBaseVO contractBaseVO);

    /**
     * 合同管理 查看合同
     * @param id
     * @return
     */
    ContractRespVO getById(String id) throws Exception;

    /**
     * 查看合同-系统对接api
     * @param id
     * @return
     */
    ApiRespVO getByIdOW(String id) throws Exception;

    /**
     * 查看合同信息-系统对接api
     * @param id
     * @return
     */
    ApiInfoRespVO getInfoByIdOW(String id) throws Exception;

    /**
     * 履约 查看合同
     * @param prefRespVO
     * @return
     */
    ContractRespVO fulfillmentgetById(PrefRespVO prefRespVO) throws Exception;

    /**
     * 履约-合同查看-无富文本
     * @param prefRespVO
     * @return
     */
    ContractCheckRespVO checkById(PrefRespVO prefRespVO) throws Exception;

    /**
     * 根据合同id 查询签署方信息
     *
     * @param id
     * @return
     */
    List<RelativeByUserRespVO> signatoryById(String id);

    /**
     * 合同编辑
     *
     * @param contractUpdateReqVO
     * @return
     */
    void update(ContractUpdateReqVO contractUpdateReqVO) throws Exception;

    /**
     * 合同删除
     *
     * @param idListVO
     * @return
     */
    void deleteByIdList(ContractIdListVO idListVO) throws Exception;

    /**
     * 文件下载
     */
    void  downFile(HttpServletResponse response, String host, String filePath);

    /**
     * 查询合同状态对应数量
     * @return
     */
    List<StatusCountVO> statusCount();

    /**
     * 合同签约类型统计
     * @return
     */
    List<TypeStatisticsVO> typeStatistics();

    String documentCreate(DocumentRelReqVo documentRelReqVo);

    /**
     * 获取下一个执行人信息
     */
    RelativeByUserRespVO next(String contractId);
    List<RelativeByUserRespVO> nextAll(String contractId);

    List<Object> recentUse(ReqVO reqVO);
    RecentUseRespVO SearchTextInfo(ReqVO reqVO);
    List<RecentUseModelVO> myCollectModel(ReqVO reqVO);

    /**
     * 查询备案列表
     * @param contractPageReqVO
     * @return
     */
    PageResult<ContractPageRespVO> getFilingsList(ContractPageReqVO contractPageReqVO);

    
    /**
     * 档案借阅合同列表
     * @param loanPageReqVO
     * @return
     */
    PageResult<LoanPageRespVO> getLoanPage(LoanPageReqVO loanPageReqVO);

    /**
     * 添加水印
     * @param contractId
     * @throws Exception
     */
    Long addTextWatermarkToPDF(String contractId,String watermarkId) throws Exception;


    /**
     * 合同状态同步-系统对接api
     * @param list
     * @return
     */
    Boolean updateStatus(List<ApiStatusReqVO> list);

    /**
     * 合同列表-系统对接api
     */
    PageResult<ApiPageRespVO> getApiPage(ApiPageReqVO apiPageReqVO);

    /**
     * 验章信息
     * @param inputStream FILE
     * @return 验章信息
     */
    VerificationRespVO verification(InputStream inputStream);

//    /**
//     * 智能填写映射code
//     * @param toJsonReqVO
//     * @return
//     */
//    List<JsonRespVO> toJson(ToJsonReqVO toJsonReqVO) throws Exception;

//    /**
//     * 智能填写映射code 表单
//     * @param formToJsonReqVO
//     * @return
//     */
//    List<JsonFormRespVO> toJsonForm(FormToJsonReqVO formToJsonReqVO) throws Exception;

    String submit(Long loginUserId, IdReqVO reqVO);
    String govsubmit(Long loginUserId, IdReqVO reqVO);
//    GetTokenRespVO  getToken(String appId, String tokenId) throws UnsupportedEncodingException;
//    ErrorInfosRespVO getErrorInfos(ErrorInfosReqVO vo) throws JsonProcessingException;
//
//    CorrectionRespVO correction(CorrectionReqVO reqVO) throws Exception;
//
//    TokenRespVO generateToken();
//
//    TaskIdRespVO upload(MultipartFile file) throws Exception;
//
//    ContractDetailRespVO detail(String taskIds);
//
//    ContractDetailRespVO getDetail(String taskId);

    /**
     * 查看合同详情v2
     *
     * @param id
     * @return
     */
    ContractDataVo queryById(String id) throws Exception;
    UploadContractCreateReqVO getUploadContractById(String id) throws Exception;
    GPMallPageRespVO getOrderAndGoodsByOrderId(String id);

    void convertRtf2Pdf(String content, String targetPath) throws Exception;

    String uploadSignedContract(UploadSignedContractVO uploadSignedContractVO) throws Exception ;

    DocInfoRespVO getDocInfo(String contractId) throws Exception;

    /**
     * 通过合同id，将合同文件转pdf，返回pdf的url
     */
    DocInfoRespVO getPdfUrl(String contractId) throws Exception;

    /**
     * 生成模板副本V2 （只生成空白文件）
     */
    Long getTemplateCopyV2(String templateId);

//    Map<String, String> jsonV2(ToJsonReqVO toJsonReqVO) throws IOException;

//    Map<String, Object> saveFile(String caseID, byte[] documentData, String sign) throws IOException;



    /**
     * 黑龙江-电子交易
     * 待发送页面 - 新增  -  发送
     * 修改合同签署状态 - 发起工作流
     *
     * @param id
     * @return
     */
    void tradingSend(String id) throws Exception;

    void updateOrgSinTime(String contractId);

    /**
     * 政采合同起草获取待办数量
     */
    GpxPrjAndGpMallNumVO getGpTaskNum();

    void update0(String contractId);

    void update3(IdReqVO vo);
    void updateStat(String contractId);

    void downloadPDF4Contract(HttpServletResponse response, String contractId) throws Exception;

    Long addTextWatermarkToPDFCommon(PdfWatermarkReqVO reqVO) throws Exception;

    OfflineFileRespVO getOfflineFile(String contractId);

    ContractPageRespVO getContractInfoById(String contractId);
    PageResult<TransferContractAllRespVO> queryTransferContractAll( TransferContractAllReqVO reqVO);

    PageResult<SimpleContractListRespVO> listContracts(ContractPageReqVO reqVO);
}
