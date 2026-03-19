package com.yaoan.module.econtract.controller.admin.contract;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.yaoan.framework.common.pojo.CommonResult;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.idempotent.core.annotation.Idempotent;
import com.yaoan.framework.operatelog.core.annotations.OperateLog;
import com.yaoan.module.econtract.api.contract.ContractApi;
import com.yaoan.module.econtract.controller.admin.contract.vo.*;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.controller.admin.model.vo.RecentUseModelVO;
import com.yaoan.module.econtract.controller.admin.order.vo.GPMallPageRespVO;
import com.yaoan.module.econtract.controller.admin.relative.vo.RelativeByUserRespVO;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.service.contract.ContractService;
import com.yaoan.module.econtract.util.WkHtmlToPdfManager;
import com.yaoan.module.econtract.util.YhAgentUtil;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.enums.FileUploadPathEnum;
import com.yh.scofd.agent.ConvertException;
import com.yh.scofd.agent.wrapper.Const;
import com.yh.scofd.agent.wrapper.PackException;
import com.yh.scofd.agent.wrapper.model.MarkPosition;
import com.yh.scofd.agent.wrapper.model.TextInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.common.pojo.CommonResult.success;
import static com.yaoan.module.econtract.enums.StatusConstants.READY_TO_UPLOAD_PATH;

/**
 * 合同管理模块操作接口
 */
@Slf4j
@RestController
@RequestMapping("econtract/contract")
@Validated
@Tag(name = "合同管理", description = "合同模块操作接口")
public class EcmsContractController {

    @Resource
    private ContractService contractService;
    @Resource
    private ContractApi contractApi;

    /**
     * 合同管理 - 页面展示
     * 登录用户作为  -发起人-
     *
     * @param contractPageReqVO
     * @return
     */
    @PostMapping(value = "/page")
    @Operation(summary = "合同管理-数据展示", description = "获取合同列表")
    public CommonResult<PageResult<ContractPageRespVO>> pageDraftingList(@Parameter(name = "contractPageReqVO", description = "获取合同草拟列表")
                                                                         @RequestBody ContractPageReqVO contractPageReqVO) {

        PageResult<ContractPageRespVO> result = new PageResult<>();
        switch (contractPageReqVO.getIdentifier()) {
            case 0:
                //合同草拟-待发送
                // status:0,1
                result = contractService.getSentPage(contractPageReqVO);
                break;
            case 1:
                //合同草拟-已发送（待相对方确认）
                result = contractService.getSendPage(contractPageReqVO);
                break;
            case 2:
                //合同草拟-待发送方确认  （待发起方发起签署，发起方确认之后，就是）
                result = contractService.getConfirmPage(contractPageReqVO);
                break;
            case 3:
                //合同确认
                result = contractService.getAffirmPage(contractPageReqVO);
                break;
            case 4:
                //合同签署-待签署
                result = contractService.getSignUnfinishedPage(contractPageReqVO);
                break;
            case 5:
                //合同签署-签署完成
                result = contractService.getSignFinishPage(contractPageReqVO);
                break;
            case 6:
                //合同签署-逾期未签署
                result = contractService.getSignOverduePage(contractPageReqVO);
                break;
            case 7:
                //合同签署-整合
                result = contractService.getSignPage(contractPageReqVO);
                break;
            case 8:
                //归档管理-电子签署
                result = contractService.getFilingPage(contractPageReqVO);
                break;
            case 9:
                //草稿箱
                result = contractService.getDraftsPage(contractPageReqVO);
                break;
            case 10:
                //待审核
                result = contractService.getCheckPage(contractPageReqVO);
                break;
            case 11:
                //待送审
                result = contractService.getToCheckPage(contractPageReqVO);
                break;
            case 12:
                //合同登记-补录
                result = contractService.getRegisterPage(contractPageReqVO);
                break;
            case 13:
                // 审批中（内部审批中的合同）
                result = contractService.getAuditingPage(contractPageReqVO);
                break;
            case 14:
                // 相对方已确认
                result = contractService.getConfirmedPage(contractPageReqVO);
                break;
            default:
                throw exception(ErrorCodeConstants.CONTRACT_PAGE_PARAM_ERROR);
        }
        return success(result);
    }

    @PostMapping(value = "/pageV2")
    @Operation(summary = "合同管理-数据展示(新)", description = "获取合同列表")
    public CommonResult<PageResult<ContractPageRespVO>> pageList(@Parameter(name = "contractPageReqVO", description = "获取合同草拟列表")
                                                                         @RequestBody ContractPageReqVO contractPageReqVO) {

        PageResult<ContractPageRespVO> result = new PageResult<>();
        switch (contractPageReqVO.getIdentifier()) {
            case 0:
                //草稿
                result = contractService.getDraftsPage(contractPageReqVO);
                break;
            case 1:
                //我的合同
                result = contractService.getMyContractPage(contractPageReqVO);
                break;
            default:
                throw exception(ErrorCodeConstants.CONTRACT_PAGE_PARAM_ERROR);
        }
        return success(result);
    }


    /**
     * 合同签署管理-列表展示
     */
    @PostMapping(value = "/page/sign/page")
    @Operation(summary = "合同签署管理-数据展示", description = "获取签署合同信息列表,contractPageReqVO.identifier: 0未签署，1已签署")
    public CommonResult<PageResult<ContractPageRespVO>> pageSignList(@Parameter(name = "contractPageReqVO", description = "获取签署合同列表")
                                                                                @RequestBody ContractPageReqVO contractPageReqVO) {
        PageResult<ContractPageRespVO> result = new PageResult<>();
        switch (contractPageReqVO.getIdentifier()){
            case 0:
                result = contractService.getWaitSignPage(contractPageReqVO);
                break;
            case 1:
                result = contractService.getDoneSignPage(contractPageReqVO);
                break;
            default:
                throw exception(ErrorCodeConstants.CONTRACT_PAGE_PARAM_ERROR);
        }
        return success(result);
    }
    /**
     * 合同确认管理-列表展示
     */
    @PostMapping(value = "/page/confirm/page")
    @Operation(summary = "合同确认管理-数据展示", description = "获取确认合同信息列表,contractPageReqVO.identifier: 0待确认，1已确认")
    public CommonResult<PageResult<ContractPageRespVO>> pageConfirmList(@Parameter(name = "contractPageReqVO", description = "获取签署合同列表")
                                                                                @RequestBody ContractPageReqVO contractPageReqVO) {
        PageResult<ContractPageRespVO> result = new PageResult<>();
        switch (contractPageReqVO.getIdentifier()){
            case 0:
                result = contractService.getWaitConfirmPage(contractPageReqVO);
                break;
            case 1:
                result = contractService.getDoneConfirmPage(contractPageReqVO);
                break;
            default:
                throw exception(ErrorCodeConstants.CONTRACT_PAGE_PARAM_ERROR);
        }
        return success(result);
    }

    /**
     * 用印申请合同-列表展示
     */
    @PostMapping(value = "/page/signet/page")
    @Operation(summary = "用印申请合同-数据展示", description = "获取用印申请合同信息列表")
    public CommonResult<PageResult<ContractPageRespVO>> pageSignetList(@Parameter(name = "contractPageReqVO", description = "获取用印申请合同列表")
                                                                     @RequestBody ContractPageReqVO contractPageReqVO) {
        PageResult<ContractPageRespVO> result = contractService.getSignetPage(contractPageReqVO);
        return success(result);
    }
    /**
     * 用印申请合同-政采与非政采类型数量
     */
    @PostMapping(value = "/govOrNotNum")
    @Operation(summary = "用印申请合同-政采与非政采类型数量", description = "政采与非政采类型数量")
    @Parameter(name = "type", description = "发起用印申请页：signetAdd")
    public CommonResult<ContractGovOrNotNumRespVO> govOrNotNum(String type) {
        ContractGovOrNotNumRespVO result = contractService.getGovOrNotNum(type);
        return success(result);
    }


    /**
     * 查询备案列表
     */
    @PostMapping(value = "/page/filingsList")
    @Operation(summary = "查询备案列表", description = "查询备案列表")
    public CommonResult<PageResult<ContractPageRespVO>> getFilingsList(@RequestBody ContractPageReqVO contractPageReqVO) {
        PageResult<ContractPageRespVO> result = contractService.getFilingsList(contractPageReqVO);
        return success(result);
    }

    /**
     * 档案借阅合同列表
     */
    @PostMapping(value = "/page/loan")
    @Operation(summary = "档案借阅合同列表", description = "获取档案借阅合同列表")
    public CommonResult<PageResult<LoanPageRespVO>> pageLoan(@RequestBody LoanPageReqVO loanPageReqVO) {
        PageResult<LoanPageRespVO> result = contractService.getLoanPage(loanPageReqVO);
        return success(result);
    }

    /**
     * 付款管理-列表展示-金额数据
     */
    @PostMapping(value = "/page/payment")
    @Operation(summary = "付款管理-数据展示", description = "获取付款列表")
    public CommonResult<PaymentAmount> pagePayment(@RequestBody PaymentSchedulePageReqVO paymentSchedulePageReqVO) {
        PaymentAmount result = contractService.getPayment(paymentSchedulePageReqVO);
        return success(result);
    }

    /**
     * 付款管理-列表展示
     */
    @PostMapping(value = "/page/payment/page")
    @Operation(summary = "付款管理-数据展示", description = "获取付款列表")
    public CommonResult<PageResult<PaymentSchedulePageRespVO>> pagePaymentList(@RequestBody PaymentSchedulePageReqVO paymentSchedulePageReqVO) {
        PageResult<PaymentSchedulePageRespVO> result = contractService.getPaymentPage(paymentSchedulePageReqVO);
        return success(result);
    }


    /**
     * 根据合同id 支付计划
     */
    @GetMapping(value = "/payment/schedule/{contractId}")
    @Operation(summary = "付款管理-数据展示", description = "获取付款列表")
    public CommonResult<List<PaymentScheduleRespVO>> getPaymentSchedule(@PathVariable String contractId) {
        List<PaymentScheduleRespVO> result = contractService.getPaymentSchedule(contractId);
        return success(result);
    }

    /**
     * 项目采购数据列表展示
     *
     * @param
     * @return
     */
    @PostMapping(value = "/page/purchasing")
    @Operation(summary = "项目采购数据", description = "项目采购数据")
    public CommonResult<PageResult<ProjectPurchasingRespVO>> pageProjectPurchasingList(PageParam pageParam) {
        PageResult<ProjectPurchasingRespVO> result = contractService.getProjectPurchasingPage(pageParam);
        return success(result);
    }

    /**
     * 生成模板副本 （项目采购-草拟合同）
     */
    @GetMapping(value = "/purchasing/model/copy/{templateId}")
    @Operation(summary = "项目采购-草拟合同", description = "生成模板副本")
    public CommonResult<Long> copy(@PathVariable String templateId) throws Exception {
        Long fileId = contractService.getTemplateCopy(templateId);
        return success(fileId);
    }

    /**
     * 生成模板副本V2 （只生成空白文件）
     */
    @GetMapping(value = "/purchasing/model/copyV2/{id}")
    @Operation(summary = "生成模板副本V2", description = "生成模板副本V2")
    public CommonResult<Long> copyV2(@PathVariable String templateId) throws Exception {
        Long fileId = contractService.getTemplateCopyV2(templateId);
        return success(fileId);
    }

    /**
     * 项目采购数据详情查看
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/purchasing/detail/{id}")
    @Operation(summary = "项目采购数据详情", description = "项目采购数据详情")
    public CommonResult<ProjectPurchasingRespVO> detail(@PathVariable String id) {
        ProjectPurchasingRespVO projectPurchasingRespVO = contractService.getProjectPurchasingById(id);
        return success(projectPurchasingRespVO);
    }

    /**
     * 上传合同列表查看
     * 创建人 当前登录用户
     *
     * @param contractDocumentPageReqVO
     * @return
     */
    @PostMapping(value = "/page/document")
    @Operation(summary = "归档管理-数据展示", description = "获取合同列表")
    public CommonResult<PageResult<ContractRespVO>> pageDocumentList(@Valid @RequestBody ContractDocumentPageReqVO contractDocumentPageReqVO) {
        PageResult<ContractRespVO> result = contractService.getDocumentPage(contractDocumentPageReqVO);
        return success(result);
    }

    /**
     * 合同创建(修改) 是否提交审批
     *
     * @param contractCreateReqVO
     * @return
     */
    @PostMapping(value = "/create")
    @Operation(summary = "创建合同")
    @Idempotent(timeout = 1, timeUnit = TimeUnit.SECONDS, message = "正在保存，请勿重复提交")
    public CommonResult<String> create(@Valid @RequestBody ContractCreateReqVO contractCreateReqVO) throws Exception {
        String ContractId = contractService.createContract(contractCreateReqVO);
        return success(ContractId);
    }

    /**
     * 合同创建(修改)
     *
     * @param contractCreateReqVO
     * @return
     */
    @PutMapping(value = "/createOrUpdateContract")
    @Operation(summary = "合同创建(修改)合同")
    @Idempotent(timeout = 10, timeUnit = TimeUnit.SECONDS, message = "正在保存，请勿重复提交")
    @OperateLog(enable = false)
    public CommonResult<String> createOrUpdateContract(@Valid @RequestBody FileUploadContractCreateReqVO contractCreateReqVO) throws Exception {
        String ContractId = contractService.createOrUpdateContract(contractCreateReqVO);
        return success(ContractId);
    }

    /**
     * 查看合同詳情
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/contract/query/{id}")
    @Operation(summary = "查看合同詳情")
    public CommonResult<ContractDataVo> queryById(@PathVariable String id) throws Exception {
        ContractDataVo orderContractRespVO = contractService.queryById(id);
        return success(orderContractRespVO);
    }

//    /**
//     * 获取合同
//     */
//    @GetMapping(value = "/get/upload/contract/{id}")
//    @Operation(summary = "获取合同")
//    public CommonResult<UploadContractCreateReqVO> getUploadContractById(@PathVariable("id") String id) throws Exception {
//        return success(contractService.getUploadContractById(id));
//    }

    /**
     * 获取订单和商品信息
     */
    @GetMapping(value = "/order/goods/{guid}")
    @Operation(summary = "获取订单和商品信息")
    public CommonResult<GPMallPageRespVO> getOrderAndGoodsByOrderId(@PathVariable("guid") String guid) {
        return success(contractService.getOrderAndGoodsByOrderId(guid));
    }

    /**
     * 合同创建基础版
     */
    @PostMapping(value = "/create/base")
    @Operation(summary = "创建合同基础版")
    public CommonResult<String> createBase(@Valid @RequestBody ContractCreateBaseReqVO contractCreateBaseReqVO) throws Exception {
        String ContractId = contractService.createContractBase(contractCreateBaseReqVO);
        return success(ContractId);
    }

    /**
     * 文件格式转换，富文本、doc(x) -> pdf 并上传minIO
     *
     * @param contractToPdfVO
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/to/pdf")
    @Operation(summary = "文件格式转换，富文本、doc(x) -> pdf 并上传minIO")
    public CommonResult<Long> toPdf(@Valid @RequestBody ContractToPdfVO contractToPdfVO) throws Exception {
        Long pdfId = contractService.toPdf(contractToPdfVO);
        return success(pdfId);
    }

    /**
     * 合同名称、编码，校验
     *
     * @param name,code
     * @return
     */
    @PostMapping(value = "/exist")
    @Operation(summary = "合同名称、编码，校验")
    public CommonResult<Boolean> exist(@RequestParam String name, @RequestParam String code) throws Exception {
        Boolean aBoolean = contractService.exist(name, code);
        return success(aBoolean);
    }

    /**
     * 合同查看
     *
     * @param id
     * @return
     */
    @Deprecated
    @GetMapping(value = "/getById/{id}")
    @Operation(summary = "根据Id查看合同")
    public CommonResult<ContractRespVO> getById(@PathVariable String id) throws Exception {
        ContractRespVO result = contractService.getById(id);
        return success(result);
    }

    /**
     * 履约 合同查看  所有合同查看接口 新
     * 台账合同详情
     *
     * @param prefRespVO
     * @return
     */
    @PostMapping(value = "/fulfillmentgetById/")
    @Operation(summary = "根据Id查看合同 台账合同详情")
    public CommonResult<ContractRespVO> fulfillmentgetById(@Valid @RequestBody PrefRespVO prefRespVO) throws Exception {
        ContractRespVO result = contractService.fulfillmentgetById(prefRespVO);
        return success(result);
    }

    /**
     * 履约-合同查看-无富文本
     *
     * @param prefRespVO
     * @return
     */
    @PostMapping(value = "/fulfillmentgetById/check")
    @Operation(summary = "根据Id查看合同-无富文本")
    public CommonResult<ContractCheckRespVO> checkById(@Valid @RequestBody PrefRespVO prefRespVO) throws Exception {
        ContractCheckRespVO result = contractService.checkById(prefRespVO);
        return success(result);
    }

    /**
     * 根据合同id 查询签署方信息
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/signatoryById/{id}")
    @Operation(summary = "根据Id查看签署方信息")
    public CommonResult<List<RelativeByUserRespVO>> signatoryById(@PathVariable String id) {
        List<RelativeByUserRespVO> resultList = contractService.signatoryById(id);
        return success(resultList);
    }

    /**
     * 合同编辑
     *
     * @param contractUpdateReqVO
     * @return
     */
    @Deprecated
    @PostMapping(value = "/update")
    @Operation(summary = "编辑合同")
    public CommonResult<Boolean> update(@Valid @RequestBody ContractUpdateReqVO contractUpdateReqVO) throws Exception {
        contractService.update(contractUpdateReqVO);
        return success(true);
    }

    /**
     * 合同删除
     *
     * @param idListVO
     * @return
     */
    @PostMapping(value = "/delete")
    @Operation(summary = "删除合同")
    public CommonResult<Boolean> delete(@Valid @RequestBody ContractIdListVO idListVO) throws Exception {
        contractService.deleteByIdList(idListVO);
        return success(true);
    }

    /**
     * 文件下载
     */
    @PostMapping(value = "/down", produces = {"application/json;charset=UTF-8"})
    @Operation(summary = "文件下载")
    public void downFile(HttpServletResponse response, @RequestParam("host") String host, @RequestParam("filePath") String filePath) {
        contractService.downFile(response, host, filePath);
    }

    /**
     * 指标数据
     *
     * @return
     */
    @GetMapping(value = "/status")
    @Operation(summary = "工作台合同状态数量")
    public CommonResult<Map<String, List<StatusCountVO>>> getStatusCount() {
        List<StatusCountVO> result02 = contractService.statusCount();
        Map<String, List<StatusCountVO>> result = new HashMap<>();
        result.put("list", result02);
        return success(result);
    }

    /**
     * 合同签约类型统计
     *
     * @return
     */
    @GetMapping(value = "/type/statistics")
    @Operation(summary = "合同签约类型统计")
    public CommonResult<List<TypeStatisticsVO>> getTypeStatistics() {
        List<TypeStatisticsVO> result = contractService.typeStatistics();
        return success(result);
    }

    /**
     * 提交归档
     *
     * @param documentRelReqVo
     * @return
     */
    @PostMapping(value = "/document/create")
    @Operation(summary = "提交归档")
    public CommonResult<String> documentCreate(@Valid @RequestBody DocumentRelReqVo documentRelReqVo) {
        String ContractId = contractService.documentCreate(documentRelReqVo);
        return success(ContractId);
    }

    /**
     * 获取下一个执行人信息
     *
     * @param contractId
     */
    @GetMapping(value = "/next/{contractId}")
    @Operation(summary = "获取下一个执行人信息")
    public CommonResult<RelativeByUserRespVO> next(@PathVariable String contractId) {
        RelativeByUserRespVO result = contractService.next(contractId);
        return success(result);
    }

    @GetMapping(value = "/nextAll/{contractId}")
    @Operation(summary = "获取下一步所有执行人信息")
    public CommonResult<List<RelativeByUserRespVO>> nextAll(@PathVariable String contractId) {
        List<RelativeByUserRespVO> result = contractService.nextAll(contractId);
        return success(result);
    }

    /**
     * 获取最近使用的文本
     */
    @PostMapping(value = "/recentUse")
    @Operation(summary = "获取最近使用的文本")
    public CommonResult<List<Object>> recentUse(@Valid @RequestBody ReqVO reqVO) {
        List<Object> result = contractService.recentUse(reqVO);
        return success(result);
    }

    /**
     * 我收藏的模板
     */
    @PostMapping(value = "/myCollectModel")
    @Operation(summary = "我的收藏模板")
    public CommonResult<List<RecentUseModelVO>> myCollectModel(@Valid @RequestBody ReqVO reqVO) {
        List<RecentUseModelVO> result = contractService.myCollectModel(reqVO);
        return success(result);
    }

    /**
     * 草拟合同搜索框
     */
    @PostMapping(value = "/search")
    @Operation(summary = "草拟合同搜索框")
    public CommonResult<RecentUseRespVO> search(@Valid @RequestBody ReqVO reqVO) {
        RecentUseRespVO result = contractService.SearchTextInfo(reqVO);
        return success(result);
    }

    /**
     * 测试pdf文件添加水印
     */
    @PostMapping(value = "/test/text")
    @Operation(summary = "测试pdf文件添加水印")
    public CommonResult<String> test(@RequestParam("sourcePath") String sourcePath, @RequestParam("targetPath") String targetPath) throws IOException, PackException, ConvertException {
        File srcFile = new File(sourcePath);
        FileOutputStream out = new FileOutputStream(targetPath);
        TextInfo textinfo = new TextInfo("测试水印", "宋体", 18, "#00ff0033", 45, Const.XAlign.Center, Const.YAlign.Middle);
        MarkPosition mk = new MarkPosition(10, 10, 100, 100, new int[]{1, 3});
        boolean printable = true;
        boolean visible = true;
        YhAgentUtil.convertAndAddTextWatermarkToPDF(srcFile, out, textinfo, mk, printable, visible);
        return success("测试水印添加成功");
    }

    /**
     * 测试合同加水印
     */
    @PostMapping(value = "/test/contract")
    @Operation(summary = "测试合同加水印")
    public CommonResult<String> testContract(
            @RequestParam("contractId") String contractId,
            @RequestParam(value = "watermarkId", required = false) String watermarkId) throws Exception {
        contractService.addTextWatermarkToPDF(contractId,watermarkId);
        return success("测试水印添加成功");
    }

    /**
     * 下载加水印的pdf（公用免租户）
     */
    @PostMapping(value = "/getPdfWithWatermark")
    @Operation(summary = "下载加水印的pdf")
    public CommonResult<Long> getPdfWithWatermark(@RequestBody  PdfWatermarkReqVO reqVO) throws Exception {
        return success(contractService.addTextWatermarkToPDFCommon(reqVO));

    }


    @PostMapping(value = "/uploadSignedContract")
    @Operation(summary = "SppGPT获取合同解析详情")
    public CommonResult<String> uploadSignedContract(@Valid @RequestBody UploadSignedContractVO uploadSignedContractVO) throws Exception {
        String id = contractService.uploadSignedContract(uploadSignedContractVO);
        return success(id);
    }

    /**
     * 通过合同id，获得合同名称和doc文件的url
     */
    @GetMapping(value = "/getDocInfo")
    @Operation(summary = "通过合同id，获得合同名称和doc文件的url")
    public CommonResult<DocInfoRespVO> getDocInfo(@RequestParam("contractId") String contractId) throws Exception {
        return success(contractService.getDocInfo(contractId));
    }

    /**
     * 将合同文件转pdf，返回pdf的url
     */
    @GetMapping(value = "/getPdfUrl")
    @Operation(summary = "通过合同id，获得合同名称和doc文件的url")
    public CommonResult<DocInfoRespVO> getPdfUrl(@RequestParam("contractId") String contractId) throws Exception {
        return success(contractService.getPdfUrl(contractId));
    }

    @Operation(summary = "同步签署信息")
    @GetMapping(value = "/pushSignInfo")
    public CommonResult pushContractSignInfo(@RequestParam("contractId") String contractId) {
        contractApi.productSignSendEcms(contractId);
        return success(contractId);
    }

    @Resource
    private WkHtmlToPdfManager wkHtmlToPdfManager;

    @Resource
    private FileApi fileApi;

    @PostMapping(value = "/test/pdf")
    @Operation(summary = "测试转换pdf")
    public CommonResult<Long> testPdf(@RequestParam("file") MultipartFile file) throws Exception {
        // 读取文件内容并转换为 PDF
        String htmlContent = new String(file.getBytes(), StandardCharsets.UTF_8);
        String folderId = "TEMPLATE-" + IdUtil.fastSimpleUUID();
        String localFolderPath = READY_TO_UPLOAD_PATH + "/" + folderId;
        FileUtil.mkdir(localFolderPath);
        Long pdfFileId = new Long(0);

            try {
                wkHtmlToPdfManager.fileAuthHtmlToPdfFromstring(htmlContent, localFolderPath + ".pdf");
            } catch (Exception e) {
                System.out.println("【WK html转换pdf】处理异常！" + e.getMessage());
                e.printStackTrace();
            }
            Path path = Paths.get(localFolderPath + ".pdf");
            pdfFileId = fileApi.uploadFile(folderId + ".pdf", FileUploadPathEnum.CONTRACT_SIGNING.getPath() + IdUtil.fastSimpleUUID() + ".pdf", Files.readAllBytes(path));

        FileUtil.del(localFolderPath);
        return success(pdfFileId);
    }
    @Operation(summary = "添加采购人签署时间")
    @GetMapping(value = "/updateOrgSinTime")
    public CommonResult updateOrgSinTime(@RequestParam("contractId") String contractId) {
        contractService.updateOrgSinTime(contractId);
        return success(contractId);
    }



    /**
     * 政采起草-交易执行和服务工程超市数量
     */
    @Operation(summary = "政采起草-交易执行和服务工程超市数量", description = "政采起草-交易执行和服务工程超市数量,contractFrom:服务工程超市-合同来源")
    @GetMapping(value = "/getGpxAndGpMallNum")
    public CommonResult<GpxPrjAndGpMallNumVO> getGpxAndGpMallNum() {
        return CommonResult.success(contractService.getGpTaskNum());
    }

    /**
     * 更新合同状态0
     * 最新需求，在草稿箱上传线下审批文件并把状态改为待发送。故弃用此接口
     */
    @GetMapping(value = "/update0/{contractId}")
    @Operation(summary = "更新合同状态0", description = "更新合同状态0")
    public CommonResult<Boolean> update0(@PathVariable String contractId) throws Exception {
        //最新需求，在草稿箱上传线下审批文件并把状态改为待发送。故弃用此接口。改用update3
        // contractService.update0(contractId);
        return success(true);
    }

    /**
     * 更新合同状态3 
     * 最新需求，在草稿箱上传线下审批文件并把状态改为待发送
     */
    @PostMapping(value = "/update3")
    @Operation(summary = "更新合同状态3", description = "更新合同状态3")
    public CommonResult<Boolean> update3(@RequestBody IdReqVO vo) throws Exception {
        contractService.update3(vo);
        return success(true);
    }

    /**
     * 更新合同状态 通用
     */
    @PostMapping(value = "/updateStat/{contractId}")
    @Operation(summary = "合同签署页取消合同", description = "合同签署页取消合同")
    public CommonResult<Boolean> updateStat(@PathVariable String contractId) throws Exception {
        contractService.updateStat(contractId);
        return success(true);
    }
    /**
     * 下载合同PDF
     * 直接pdf下载
     * 或
     * 富文本转pdf后，下载
     * 或
     * 或doc转pdf后，下载
     */
    @GetMapping(value = "/downloadPDF4Contract/{contractId}")
    @Operation(summary = "下载合同PDF", description = "下载合同PDF")
    @OperateLog(logArgs = false)
    public void downloadPDF4Contract(HttpServletResponse response, @PathVariable String contractId ) throws Exception {
        contractService.downloadPDF4Contract(response,contractId);
    }


    /**
     * 根据合同id查询线下审批文件的文件信息
     */
    @GetMapping(value = "/getOfflineFile/{contractId}")
    @Operation(summary = "根据合同id查询线下审批文件的文件信息", description = "根据合同id查询线下审批文件的文件信息")
    @OperateLog(logArgs = false)
    public CommonResult<OfflineFileRespVO> getOfflineFile(@PathVariable String contractId ) throws Exception {
       return success( contractService.getOfflineFile(contractId));
    }

    /**
     * 根据合同id查询合同信息
     */
    @GetMapping(value = "/getContractInfoById/{contractId}")
    @Operation(summary = "合同信息", description = "合同信息")
    public CommonResult<ContractPageRespVO> getContractInfoById(@PathVariable String contractId) {
        ContractPageRespVO result = contractService.getContractInfoById(contractId);
        return success(result);
    }

    @PostMapping("/queryTransferContractAll")
    @Operation(summary = "获取批量转交合同列表信息")
    public CommonResult<PageResult<TransferContractAllRespVO>> queryTransferContractAll(@RequestBody TransferContractAllReqVO reqVO) {
        PageResult<TransferContractAllRespVO> respVOS=  contractService.queryTransferContractAll(reqVO);
        return success(respVOS);
    }

    @PostMapping("/listContracts")
    @Operation(summary = "简易版合同列表")
    public CommonResult<PageResult<SimpleContractListRespVO>> listContracts(@RequestBody ContractPageReqVO reqVO) {
        return success(contractService.listContracts(reqVO));
    }


}
