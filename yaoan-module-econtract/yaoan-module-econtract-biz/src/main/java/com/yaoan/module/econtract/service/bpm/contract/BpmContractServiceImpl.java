package com.yaoan.module.econtract.service.bpm.contract;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.yaoan.framework.common.exception.ErrorCode;
import com.yaoan.framework.common.pojo.ExternallnterfaceResult;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.yaoan.module.bpm.api.task.dto.BpmTaskAllInfoRespVO;
import com.yaoan.module.bpm.api.task.dto.ContractProcessInstanceRelationInfoRespDTO;
import com.yaoan.module.bpm.api.task.dto.TaskForWarningReqDTO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.api.contract.ContractProcessApi;
import com.yaoan.module.econtract.api.contract.dto.ContractProcessDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.dto.EncryptDTO;
import com.yaoan.module.econtract.controller.admin.bpm.common.CommonBpmAutoPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.contract.vo.*;
import com.yaoan.module.econtract.convert.bpm.contract.BpmContractConverter;
import com.yaoan.module.econtract.convert.config.SystemConfigDTOConverter;
import com.yaoan.module.econtract.convert.contract.ContractConverter;
import com.yaoan.module.econtract.dal.dataobject.bpm.contract.BpmContract;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.TradingContractExtDO;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.mysql.bpm.contract.BpmContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.TradingContractExtMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.enums.*;
import com.yaoan.module.econtract.enums.common.IfEnums;
import com.yaoan.module.econtract.enums.common.flow.FlowableStatusEnums;
import com.yaoan.module.econtract.enums.contract.ContractSourceTypeEnums;
import com.yaoan.module.econtract.enums.gcy.gpmall.HLJContractStatusEnums;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import com.yaoan.module.econtract.enums.warning.WarningBusinessEnum;
import com.yaoan.module.econtract.framework.core.event.version.FileVersionEvent;
import com.yaoan.module.econtract.framework.core.event.version.FileVersionEventPublisher;
import com.yaoan.module.econtract.framework.core.event.warning.EcmsWarningEvent;
import com.yaoan.module.econtract.framework.core.event.warning.EcmsWarningEventPublisher;
import com.yaoan.module.econtract.service.contract.version.ContractVersionService;
import com.yaoan.module.econtract.util.EcontractUtil;
import com.yaoan.module.econtract.util.Html2WordUtil;
import com.yaoan.module.econtract.util.flowable.FlowableUtil;
import com.yaoan.module.econtract.util.gcy.Sm4Utils;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.enums.FileUploadPathEnum;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.enums.config.SystemConfigKeyEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.yaoan.module.econtract.enums.StatusConstants.*;

/**
 * <p>
 * 合同审批流程申请表 服务实现类
 * </p>
 *
 * @author doujiale
 * @since 2023-10-10
 */
@Slf4j
@Service
public class BpmContractServiceImpl implements BpmContractService {
    @Value("${feign.client.contract.client_id}")
    private String clientId;
    @Value("${feign.client.contract.client_secret}")
    private String clientSecret;
    public static final String TRADING_PROCESS_KEY = "gpmall_contract_process";
    public static String PROCESS_KEY = "contract_approve";
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private BpmContractMapper bpmContractMapper;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;
    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private ContractVersionService contractVersionService;
    @Resource
    private FileVersionEventPublisher fileVersionEventPublisher;
    @Resource
    private FileApi fileApi;
    @Resource
    private TradingContractExtMapper tradingContractExtMapper;
    @Resource
    private EcmsWarningEventPublisher warningEventPublisher;
    @Resource
    private ContractProcessApi contractProcessApi;

    public static void main(String[] args) {
        String htmlContent = "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><style></style><meta charset=\"utf-8\" /><title>合同模板</title></head><body><p style=\"text-align:center;\"><b><font size=\"5\">政府采购服务类采购合同（试行）</font></b><br /></p><p style=\"text-align:center;\"><font face=\"宋体\" size=\"3\"><br /></font></p><div>\n" +
                "          <div>\n" +
                "          <table id=\"DIYTable\" style=\"width: 98%; margin-left: 1%; margin-right: 1%;\">\n" +
                "          <tbody><tr><td style=\"line-height: 32px; vertical-align: top; width: 50%;\"><font size=\"3\" face=\"宋体\">采购单位（甲方）：<span style=\"border-top: medium none currentcolor; border-right: medium none currentcolor; border-bottom: none; border-left: medium none currentcolor; border-image: none 100% / 1 / 0 stretch; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"64850b3abd62532c0c2ef753eb5786c4&amp;-&amp;20\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"甲方名称（盖章）/发包人\" code=\"buyerOrgName\" placeholder=\"\" readonly=\"true\">黑龙江省医疗保障局</span><br />供应商（乙方）：<span style=\"border-top: medium none currentcolor; border-right: medium none currentcolor; border-bottom: none; border-left: medium none currentcolor; border-image: none 100% / 1 / 0 stretch; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"0ac7c5d2249627050c18025b01037454&amp;-&amp;19\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"乙方名称(盖章)/承包人\" code=\"supplierName\" placeholder=\"\" readonly=\"true\">上海金仕达卫宁软件科技有限公司</span><br />签订地点：<span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"42bab21886d0ae6ac58717e1146cf0a6&amp;-&amp;1\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"合同订立地点\" code=\"contractSignAddress\" placeholder=\"\" readonly=\"true\">哈尔滨</span></font></td><td style=\"line-height: 32px; vertical-align: top; width: 50%;\"><font face=\"宋体\" size=\"3\">采购计划号：<span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"cdf984eff9d9fbfc0f2b8fcbffb6a7a1&amp;-&amp;21\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"采购计划号（采购计划明细ID）\" code=\"buyPlanBillGuid\" placeholder=\"\" readonly=\"true\">黑政采计划[2024]12113</span><br />招标编号：<span style=\"border-top: medium none currentcolor; border-right: medium none currentcolor; border-bottom: none; border-left: medium none currentcolor; border-image: none 100% / 1 / 0 stretch; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"287f24587ba98c3f9b504f261d817c97&amp;-&amp;22\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"招标编号\" code=\"bidCode\" placeholder=\"\" readonly=\"true\">[230001]SC[LHCS]20240004</span><br />签订时间：<input class=\"tagYaoAn\" id=\"883f51295cd60434d1e6a0473dd3299f&amp;-&amp;1\" tag=\"date\" parameter=\"yes\" data-date=\"yes\" readonly=\"readonly\" style=\"border-top: none; border-right: none; border-left: none; cursor: pointer; border-bottom: 1px solid; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" type=\"txt\" name=\"合同签订日期\" code=\"contractSignTime\" placeholder=\"\" /></font></td></tr></tbody></table></div></div><p><font size=\"3\" face=\"宋体\"><i><br /></i></font></p><p><font size=\"3\" face=\"宋体\">根据《中华人民共和国民法典》、《中华人民共和国政府采购法》等法律、法规规定，按照<span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"24c33a9d8e34a016aa8511e1bbe94ea5&amp;-&amp;1\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"项目名称\" code=\"projectHomeName\" placeholder=\"\" readonly=\"true\">招标基金监管第三方机构  </span>项目（招标编号：<span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"287f24587ba98c3f9b504f261d817c97&amp;-&amp;3\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"招标编号\" code=\"bidCode\" placeholder=\"\" readonly=\"true\">[230001]SC[LHCS]20240004</span> ）的采购文件及中标（成交）供应商投标（响应）文件等，确定乙方为甲方<span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"24c33a9d8e34a016aa8511e1bbe94ea5&amp;-&amp;2\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"项目名称\" code=\"projectHomeName\" placeholder=\"\" readonly=\"true\">招标基金监管第三方机构  </span>项目供应商，经双方协商一致，签订本合同。</font></p><p style=\"font-weight: bold; margin-top: 5px; margin-bottom: 5px;\">一、合同文件</p><p><font size=\"3\" face=\"宋体\">本次政府采购活动的相关文件为本合同不可分割的组成部分，与本合同具有同等法律效力，这些文件包括但不限于：</font></p><p><font size=\"3\" face=\"宋体\">1、采购文件、澄清和答疑文件等；</font></p><p><font size=\"3\" face=\"宋体\">2、乙方投标（响应）文件等；</font></p><p><font size=\"3\" face=\"宋体\">3、乙方书面承诺等；</font></p><p><font size=\"3\" face=\"宋体\">4、中标（成交）通知书。</font></p><p style=\"font-weight: bold; margin-top: 5px; margin-bottom: 5px;\">二、合同标的</p><div id=\"b7a825ba1a1a05554dcbf29c0fdf01d1&amp;-&amp;1\" class=\"tagYaoAn\" parameter=\"yes\" code=\"orderGoods\" style=\"width: 100%; background: rgb(255, 255, 255);\" tag=\"tableParams\"><table style=\"width: 100%; border-collapse: collapse; font-size: 14px; border: 1px solid rgb(51, 51, 51);\"><thead style=\"background: rgb(229, 231, 235);\"><tr style=\"height: 45px;\"><th tcode=\"name\" style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">标的名称</th><th tcode=\"require\" style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">采购需求</th><th tcode=\"number\" style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">数量</th><th tcode=\"price\" style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">单价（元）</th><th tcode=\"amount\" style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">金额（元）</th><th tcode=\"notes\" style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">备注</th></tr></thead><tbody><tr><td style=\"border: 1px solid rgb(51, 51, 51); text-align: center; white-space: pre-wrap; background: transparent;\">2024年度医疗保障基金监管第三方机构 服务采购2</td><td style=\"border: 1px solid rgb(51, 51, 51); text-align: center; white-space: pre-wrap; background: transparent;\">按招标文件要求执行</td><td style=\"border: 1px solid rgb(51, 51, 51); text-align: center; white-space: pre-wrap; background: transparent;\">1</td><td style=\"border: 1px solid rgb(51, 51, 51); text-align: center; white-space: pre-wrap; background: transparent;\">930000</td><td style=\"border: 1px solid rgb(51, 51, 51); text-align: center; white-space: pre-wrap; background: transparent;\">930000.0000</td><td style=\"border: 1px solid rgb(51, 51, 51); text-align: center; white-space: pre-wrap; background: transparent;\"></td></tr><tr style=\"height: 45px;\"><td style=\"text-align: center; border: 1px solid rgb(51, 51, 51); background: transparent;\">合计</td><td colspan=\"6\" style=\"border: 1px solid rgb(51, 51, 51); background: transparent;\">￥930000元 （大写: 玖拾叁万元整）</td></tr></tbody></table></div><p style=\"font-weight: bold; margin-top: 5px; margin-bottom: 5px;\">四、合同期限（任选其一）</p><p><span class=\"tagYaoAn\" tag=\"radio\" parameter=\"yes\" style=\"color: rgb(61, 61, 61); width: fit-content; background: rgb(255, 255, 255);\" name=\"合同期限（任选其一）\" code=\"CS-HTQX-9011\" placeholder=\"任选其一\" id=\"12d4f2fa9f8aa3e59aab28231a7908e5&amp;-&amp;1\" value=\"0\"><font size=\"3\" face=\"宋体\"><span style=\"margin-right:5px;\"><input type=\"checkbox\" style=\"margin-right:5px;\" name=\"12d4f2fa9f8aa3e59aab28231a7908e5\" value=\"0\" label=\"选项一\" checked=\"true\" />本合同期限起止时间<span style=\"border-width: initial; border-style: none; border-color: initial; background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"8ca5c66f685eef9384b2bd4297232a66&amp;-&amp;2\" tag=\"rangePicker\" parameter=\"yes\" type=\"txt\" name=\"本合同期限起止时间\" code=\"contractTerm\" placeholder=\"\" readonly=\"readonly\">2024年09月09日至2025年09月08日</span>，共<span style=\"border-width: initial; border-style: none; border-color: initial; background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"ceb57ad2896ca58c3cdac89a42c7b488&amp;-&amp;3\" tag=\"number\" parameter=\"yes\" type=\"number\" name=\"合同期限起止天数\" code=\"contractDays\" placeholder=\"\" readonly=\"true\">365</span>天。<br /></span><span style=\"margin-right:5px;\"><input type=\"checkbox\" style=\"margin-right:5px;\" name=\"12d4f2fa9f8aa3e59aab28231a7908e5\" value=\"1\" label=\"选项二\" /><span style=\"color: rgb(51, 51, 51);\">本项目服务期限采用</span><span style=\"color: rgb(51, 51, 51);\">1+1+1方式，采购结果</span><input type=\"number\" style=\"border-top: none; border-right: none; border-left: none; border-bottom: 1px solid rgb(51, 51, 51); color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"b1bc16d0a8cb6c9fca20a9e45236ab89&amp;-&amp;5\" tag=\"number\" parameter=\"yes\" name=\"采购结果有效期年限\" code=\"purchaseResultValidity\" placeholder=\"\" readonly=\"true\" /><span style=\"color: rgb(51, 51, 51);\">年有效。合同一年一签，是否续签，由甲方视财政预算安排及对乙方提供服务的绩效考核等情况确定。<br /></span>本合同期限起止时间<input type=\"txt\" style=\"border-top: none; border-right: none; border-left: none; border-bottom: 1px solid rgb(51, 51, 51); color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"8ca5c66f685eef9384b2bd4297232a66&amp;-&amp;7\" tag=\"rangePicker\" parameter=\"yes\" name=\"本合同期限起止时间\" code=\"contractTerm\" placeholder=\"\" readonly=\"readonly\" /><span style=\"color: rgb(51, 51, 51);\">。</span></span></font></span><br /></p><p style=\"font-weight: bold; margin-top: 5px; margin-bottom: 5px;\">三、服务项目及要求</p><p><font size=\"3\" face=\"宋体\"><span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"8175e91b976038169d778412e9c0168e&amp;-&amp;17\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"服务项目及要求补充\" code=\"remark9\" placeholder=\"\" readonly=\"true\">1.采购内容：根据国家医保局基金监管有关工作要求，结合我省基金监管工作实际，拟采取政府购买服务的 方式，引入第三方力量，对省内部分定点医疗机构、定点零售药店、经办机构开展监督检查，协助省医疗保 障局开展日常监督检查工作。聚焦重症医学、麻醉、肺部肿瘤、骨科、血透、心内、检查、检验、康复理疗 等重点领域；聚焦医保基金结算费用支出排名靠前的重点药品和医用耗材；聚焦虚假就医、医保药品倒卖等 行为；聚焦异地就医、门诊共济改革措施实施后的监管，会同公安、卫健等部门开展医保基金违法违规问题 专项整治行动，依法严厉打击欺诈骗保违法犯罪行为，形成强大声势和有力震慑。 2.需满足要求：为黑龙 江省医疗保障局开展的基金监管检查工作提供相对固定的人员，并由相关负责人带队，主要开展疑点数据筛 查、材料汇总归档以及文字综合等工作，协助黑龙江省医疗保障局开展医保基金违法违规问题专项整治和举 报线索核查等工作，根据检查情况完成相关检查工作总结和检查报告、检查归档等，派驻至少两名人员到省 医保局驻场工作，向黑龙江省医保局报告。 服务家数：从全省各市（地） 以及农垦、森工、铁路、石油四 行业定点医药机构中抽选至少62家定点医疗机构、90家药店以及部分经办机构开展现场检查，此外，根据甲 方需要协助甲方开展对定点医药机构监督检查、大数据筛查等工作。</span></font></p><p style=\"font-weight: bold; margin-top: 5px; margin-bottom: 5px;\">五、合同金额及结算方式</p><p><font size=\"3\" face=\"宋体\">1、资金性质：</font><font face=\"宋体\" size=\"3\"><span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"e5061fc233893a3cbe5ce0ab5c162b5b&amp;-&amp;10\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"资金性质\" code=\"capitalNature\" placeholder=\"\" readonly=\"true\">财政性资金</span>。（财政性资金：按财政国库集中支付规定程序办理；自筹资金：<span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"54b5cb664ba3aa96ecb79e6d2aaa941d&amp;-&amp;2\" tag=\"number\" parameter=\"yes\" type=\"number\" name=\"通用-自筹资金\" code=\"selfRaisedFunds\" placeholder=\"\" readonly=\"true\">0</span>。）  </font></p><p><font size=\"3\" face=\"宋体\">2、合同金额：本合同有效期内服务价款金额：<span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"7387008ecc57e4c6976df7c7376c4658&amp;-&amp;3\" tag=\"number\" parameter=\"yes\" type=\"number\" name=\"合同金额(元)/合同总价\" code=\"totalMoney\" placeholder=\"\" readonly=\"true\">930000</span>元（大写 ：<span style=\"border-top: medium none currentcolor; border-right: medium none currentcolor; border-bottom: none; border-left: medium none currentcolor; border-image: none 100% / 1 / 0 stretch; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"68ac62d0be62eef54e83b5d0e3c21202&amp;-&amp;13\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"人民币大写\" code=\"shiftMoney\" placeholder=\"\" readonly=\"true\">玖拾叁万元整</span>）；</font></p><p><font size=\"3\" face=\"宋体\">3、付款方式：<select class=\"tagYaoAn\" id=\"fcadde66dc3f542f71ef0c3552634d62&amp;-&amp;3\" tag=\"drop-down\" parameter=\"yes\" name=\"付款方式-付款计划\" code=\"paymentMethod-plan\" placeholder=\"请选择\" style=\"text-align-last: left; color: rgb(61, 61, 61); width: auto; background: transparent; border: none; padding-left: 0px; padding-right: 0px; display: inline-block;\" disabled=\"\" value=\"1\"><option value=\"\" style=\"display: none;\" disabled=\"\">请选择</option><option value=\"0\">全额付款</option><option value=\"1\" selected=\"selected\">分期付款</option><option value=\"2\">成本补偿</option><option value=\"3\">绩效激励</option></select></font></p><p><font size=\"3\" face=\"宋体\">4、付款期数：<select class=\"tagYaoAn\" id=\"bb873dd059532d3be5e42da9506db3f8&amp;-&amp;1\" tag=\"drop-down\" parameter=\"yes\" style=\"text-align-last: left; color: rgb(61, 61, 61); width: auto; background: transparent; border: none; padding-left: 0px; padding-right: 0px; display: inline-block;\" name=\"付款期数\" code=\"paymentSorts\" placeholder=\"请选择付款期数\" disabled=\"\" value=\"1\"><option value=\"\" style=\"display: none;\" disabled=\"\">请选择付款期数</option><option value=\"0\">一期</option><option value=\"1\" selected=\"selected\">二期</option><option value=\"2\">三期</option><option value=\"3\">四期</option><option value=\"4\">五期</option><option value=\"5\">六期</option><option value=\"6\">七期</option><option value=\"7\">八期</option><option value=\"8\">九期</option><option value=\"9\">十期</option><option value=\"10\">十一期</option><option value=\"11\">十二期</option></select><br /></font></p><p><font size=\"3\" face=\"宋体\">5、结算方式：</font></p><div id=\"c230690a9a9089c471cfea73f262feae&amp;-&amp;1\" class=\"tagYaoAn\" parameter=\"yes\" code=\"payMentInfo\" style=\"width: 100%; background: rgb(255, 255, 255);\" tag=\"tableParams\"><table style=\"width: 100%; border-collapse: collapse; font-size: 14px; border: 1px solid rgb(51, 51, 51);\"><thead style=\"background: rgb(229, 231, 235);\"><tr style=\"height: 45px;\"><th style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">序号</th><th style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">项目阶段</th><th style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">具体内容及交付结果</th><th style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">付款金额(元)</th><th style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">付款期限(天)</th><th style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">计划支付时间</th></tr></thead><tbody><tr><td style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">1</td><td style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">一期</td><td style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">中标并签订合同后，中标方先提供合同金额10%银行保单作为履约保证金，采购方支付合同额50%</td><td style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">465000</td><td style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">90</td><td style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">2024-12-09</td></tr><tr><td style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">2</td><td style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">二期</td><td style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">项目结束同时甲方相关人员在验收单打分达到60分以上视为验收合格，之后付清全部服务费用，在扣除全部产生的违约费用后，无息返还剩余履约保证金</td><td style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">465000</td><td style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">90</td><td style=\"border: 1px solid rgb(51, 51, 51); text-align: center; height: 45px; background: transparent;\">2025-09-08</td></tr></tbody></table></div><p><font size=\"3\" face=\"宋体\">6、<!--[endif]-->甲方每次付款前，乙方应向甲方开具符合甲方要求的<select class=\"tagYaoAn\" id=\"85f6ec0e9754d89b70d135520c0b4df7&amp;-&amp;5\" tag=\"drop-down\" parameter=\"yes\" name=\"发票类型\" code=\"invoiceType\" placeholder=\"请选择\" style=\"text-align-last: left; color: rgb(61, 61, 61); width: auto; background: transparent; border: none; padding-left: 0px; padding-right: 0px; display: inline-block;\" disabled=\"\" value=\"普通发票\"><option value=\"\" style=\"display: none;\" disabled=\"\">请选择</option><option value=\"增值税专用发票\">增值税专用发票</option><option value=\"普通发票\" selected=\"selected\">普通发票</option></select>，乙方未按合同约定开具发票导致甲方逾期付款的，甲方不承担违约责任。</font></p><p style=\"font-weight: bold; margin-top: 5px; margin-bottom: 5px;\">八、保密条款</p><p><font size=\"3\" face=\"宋体\">甲方按照本合同约定提供给乙方的任何资料和信息，以及乙方在服务过程中知悉的甲方的商业和技术秘密信息 ，属甲方的保密信息和甲方拥有所有权的财产，乙方应对该资料和信息严格保密，除为履行本合同约定服务需要向行政机关作出的披露外，未经甲方书面同意，不得用于本合同约定服务以外的任何其他用途，亦不得以任何方式向任何第三方泄露或公开，并保证在本合同约定服务履行完毕后，将所有资料和信息归还甲方。本保密条款不因双方合同终止而无效，自本合同签订之日起，至相关信息已经被公开或事实上一方违反本条款不会给对方造成任何形式的损害时止，本保密条款对双方仍具有约束力。乙方如有失密或泄密行为，则视为乙方违约，甲方有权解除本合同；无论甲方是否解 除合同，乙方均应当向甲方支付</font><font face=\"宋体\" size=\"3\"><span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"b18d4d39eadff643aa90f07d921fd7e5&amp;-&amp;1\" tag=\"number\" parameter=\"yes\" type=\"number\" name=\"乙方支付违约金金额（保密条款）\" code=\"deditAmount\" placeholder=\"\" readonly=\"true\">93000元</span></font><font size=\"3\" face=\"宋体\">违约金，并赔偿给甲方造成的损失。</font></p><p style=\"font-weight: bold; margin-top: 5px; margin-bottom: 5px;\">六、双方权利义务和质量保证</p><p><font size=\"3\" face=\"宋体\">（一）甲方权利义务</font></p><p><font size=\"3\" face=\"宋体\">1、甲方有权按照采购文件及投标（响应）文件要求获取乙方所提供的专业化服务；</font></p><p><font size=\"3\" face=\"宋体\">2、甲方保证服务期间，对乙方工作给予支持，提供采购需求必须的基础工作条件；</font></p><p><font size=\"3\" face=\"宋体\">3、甲方应按合同约定向乙方按期支付服务费。</font></p><p><font size=\"3\" face=\"宋体\">4、</font><font size=\"3\" face=\"宋体\"><span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"3aeb8c6a272f4246eef84a6973dd961c&amp;-&amp;8\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"双方权利义务和质量保证补充说明\" code=\"remark1\" placeholder=\"\" readonly=\"true\">无</span>。</font></p><p><font size=\"3\" face=\"宋体\">（二）乙方权利义务</font></p><p><font size=\"3\" face=\"宋体\">1、乙方有权要求甲方提供为完成本次服务所需的相关材料和相关信息；</font></p><p><font size=\"3\" face=\"宋体\">2、有权按照本合同约定收取服务费；</font></p><p><font size=\"3\" face=\"宋体\">3、乙方应恪守职业道德，充分利用其专业知识和业务资源保证完成本合同及附件所列明的工作内容；</font></p><p><font size=\"3\" face=\"宋体\">4、乙方必须在双方议定的时间、地点完成本次服务工作；</font></p><p><font size=\"3\" face=\"宋体\">5、乙方为甲方提供服务期间，严格做好安全防护措施，并为提供服务的员工按法律规定办理工伤、意外保险，并承担相关费用。服务期间发生安全事故的，责任由乙方承担，由此造成甲方、乙方人员或者第三方损失的，乙方承担全部赔偿；</font></p><p><font size=\"3\" face=\"宋体\">6、乙方保证所提供的服务或其任何一部分均不会侵犯任何第三方的专利权、商标权或著作权。一旦出现侵权、索赔或诉讼，乙方应承担全部责任。乙方保证提供的服务不存在危及人身及财产安全的隐患，不存在违反国家法律、法规 及行业规范要求的有关安全条款，否则应承担全部法律责任 ；</font></p><p><font size=\"3\" face=\"宋体\">7、乙方不得擅自转让其应履行的合同义务。</font></p><p><font size=\"3\" face=\"宋体\">8、</font><font size=\"3\" face=\"宋体\"><span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"3aeb8c6a272f4246eef84a6973dd961c&amp;-&amp;9\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"双方权利义务和质量保证补充说明\" code=\"remark1\" placeholder=\"\" readonly=\"true\">无</span>。</font></p><p style=\"font-weight: bold; margin-top: 5px; margin-bottom: 5px;\">九、合同履约、 验收</p><p><font size=\"3\" face=\"宋体\">政府采购合同的履约适用于民法典的规定，合同签订双方应当严格按照民法典的相关规定履行各自权利和义务。</font></p><p><font size=\"3\" face=\"宋体\">1、合同签订后，乙方提供服务应当符合采购文件及投标（响应）文件及本合同约定，如提供服务不符合采购文件及 投标（响应）文件及本合同约定要求的，甲方有权提出异议并拒绝接受服务；</font></p><p><font size=\"3\" face=\"宋体\">2、合同履约过程中，甲方对乙方提供服务有异议的，可以以口头或书面形式向乙方提出，乙方应在接到甲方通知之日起</font><font face=\"宋体\" size=\"3\"><span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"b0bedc0cbb12375ac9f86fdab5d20ea0&amp;-&amp;2\" tag=\"number\" parameter=\"yes\" type=\"number\" name=\"验收异议解决工作日个数\" code=\"acceptanceProblemDealDays\" placeholder=\"\" readonly=\"true\">30</span></font><font size=\"3\" face=\"宋体\">日内予以解决，否则视为乙方违约，参照本合同第十二条承担违约责任 ；</font></p><p><font size=\"3\" face=\"宋体\">3、</font><font size=\"3\" face=\"宋体\"><span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"1531978ac5c9d2494fd8b809a743d8a9&amp;-&amp;28\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"验收补充说明\" code=\"remark3\" placeholder=\"\" readonly=\"true\">无</span>。</font></p><p style=\"font-weight: bold; margin-top: 5px; margin-bottom: 5px;\">七、知识产权归属</p><p><font size=\"3\" face=\"宋体\">本合同所约定的工作内容中，本项目正式成果的知识产权归</font><font size=\"3\" face=\"宋体\"><span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"3fb43215f099b5dd6e7c0489abdfaeea&amp;-&amp;6\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"知识产权归属方\" code=\"knowledgePropertyOwner\" placeholder=\"\" readonly=\"true\">甲</span>方所有。</font></p><p><font size=\"3\" face=\"宋体\"><span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"0b55af440a21c844054ef13b84abb994&amp;-&amp;7\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"知识产权归属补充说明\" code=\"remark2\" placeholder=\"\" readonly=\"true\">无</span></font></p><p style=\"font-weight: bold; margin-top: 5px; margin-bottom: 5px;\">十、履约保证金</p><p><font size=\"3\" face=\"宋体\">履约担保：是否收取履约保证金：<span class=\"tagYaoAn\" tag=\"radio\" parameter=\"yes\" style=\"color: rgb(61, 61, 61); width: fit-content; background: rgb(255, 255, 255);\" name=\"履约担保 ：是否收取履约保证金\" code=\"isPerformanceMo\" placeholder=\"请选择\" id=\"9ec6dc1bdea15b4c69ecbf8e03159e50&amp;-&amp;12\" value=\"1\"><span style=\"margin-right:5px;\"><input type=\"checkbox\" style=\"margin-right:5px;\" name=\"9ec6dc1bdea15b4c69ecbf8e03159e50\" value=\"1\" label=\"是\" checked=\"true\" />是 </span><span style=\"margin-right:5px;\"><input type=\"checkbox\" style=\"margin-right:5px;\" name=\"9ec6dc1bdea15b4c69ecbf8e03159e50\" value=\"0\" label=\"否\" />否<br />收取履约保证金形式：<span style=\"border-width: initial; border-style: none; border-color: initial; background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"3b72bb2d87f752743eb8bb290ad8c855&amp;-&amp;14\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"收取履约保证金形式\" code=\"CS-LYBZJ-XS-022\" readonly=\"true\" value=\"保函（保险）\">保函（保险）</span><br />收取履约保证金金额：<span style=\"border-width: initial; border-style: none; border-color: initial; background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"1e9066c722af894aadf13da71ea17308&amp;-&amp;15\" tag=\"number\" parameter=\"yes\" type=\"number\" name=\"保证金金额\" code=\"performanceMoney\" placeholder=\"\" readonly=\"true\">93000</span><br />履约担保期限：<span style=\"border-width: initial; border-style: none; border-color: initial; background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"385f8b9ba8b9c850dddbb6a675365989&amp;-&amp;16\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"履约担保期限\" code=\"performAssureTime\" placeholder=\"\" readonly=\"true\">1</span><br />履约保证金退还：<span style=\"border-width: initial; border-style: none; border-color: initial; background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"8f9a05b04bdccd6c9da9b63c389b61ed&amp;-&amp;17\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"履约保证金退还\" code=\"CS-BZJTH-0924\" placeholder=\"\" readonly=\"true\">项目结束后甲方相关人员在验收单打分达到60分以上，视为验收合格，之后付清全部服务费用，在扣除全部产生的违约金费用后，无息返还剩余履约保证金。</span></span></span></font><br /></p><p style=\"font-weight: bold; margin-top: 5px; margin-bottom: 5px;\">十二、违约责任</p><p><font size=\"3\" face=\"宋体\">1、“不可抗力”是指不能预见、不能避免并不能克服的客观情况，包括但不限于：天灾、水灾、地震或其他灾难战争或暴乱，以及其他在受影响的一方合理控制范围以外且经该方合理努力后也不能防止或避免的类似事件；</font></p><p><font size=\"3\" face=\"宋体\">2、由于不可抗力的原因，而不能履行合同或延迟履行合同的一方可视不可抗力的实际影响免除部分或全部违约责任。但受不可抗力影响的一方应立即通知对方,并在不可抗力发生后</font><font face=\"宋体\" size=\"3\"><span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"5cbc9d0f5045dd06c1ee9ccdd1044ebc&amp;-&amp;3\" tag=\"number\" parameter=\"yes\" type=\"number\" name=\"延迟履约方不可抗力发生后应出示证明文件天数\" code=\"accidentStartDays\" placeholder=\"\" readonly=\"true\">30</span>日内出示相关的主管部门签发的证明文件,以便对方审查、确认 ；</font></p><p><font face=\"宋体\" size=\"3\">3、不可抗力事件终止或消除后，受不可抗力影响的一方，应立即通知对方, 不可抗力事件终止或消除后</font><font face=\"宋体\" size=\"3\"><span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"1f3167b487c965e6935b0bfd55bb7ad0&amp;-&amp;4\" tag=\"number\" parameter=\"yes\" type=\"number\" name=\"延迟履约方不可抗力结束后应出示证明文件天数\" code=\"accidentEndDays\" placeholder=\"\" readonly=\"true\">30</span>日内出示相关的主管部门签发的证明文件确认不可抗力事件的终止或消除；</font></p><p><font size=\"3\" face=\"宋体\">4、由于不可抗力的原因，致使合同无法按期履行或不能履行的，所造成的损失由双方各自承担。受不可抗力影响的 一方应当采取合理的措施防止损失的扩大，否则应就扩大的损失负赔偿责任。</font></p><p style=\"font-weight: bold; margin-top: 5px; margin-bottom: 5px;\">十三、合同争议解决</p><p><font size=\"3\" face=\"宋体\">因履行本合同引起的或与本合同有关的争议，甲乙双方应首先通过友好协商解决，如果协商不能解决的，按下列方式解决：</font></p><p><span class=\"tagYaoAn\" id=\"c112fadfa6100d2cebc4abb03c72c6d4&amp;-&amp;1\" tag=\"radio\" parameter=\"yes\" style=\"color: rgb(61, 61, 61); width: fit-content; background: rgb(255, 255, 255);\" name=\"争议解决方法\" code=\"dispute\" placeholder=\"请选择争议解决方法\" value=\"1\"><font size=\"3\" face=\"宋体\"><span style=\"margin-right:5px;\"><input type=\"checkbox\" style=\"margin-right:5px;\" name=\"c112fadfa6100d2cebc4abb03c72c6d4\" value=\"0\" label=\"提交___仲裁委员会仲裁\" />提交<input type=\"text\" style=\"border-top: none; border-right: none; border-left: none; border-bottom: 1px solid rgb(51, 51, 51); color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"a8fbb85d69a113dc5ae9b958c307e210&amp;-&amp;2\" tag=\"txt\" parameter=\"yes\" name=\"仲裁委员会类型\" code=\"arbitrationCommissionType\" placeholder=\"\" readonly=\"true\" />仲裁委员会仲裁<br /></span><span style=\"margin-right:5px;\"><input type=\"checkbox\" style=\"margin-right:5px;\" name=\"c112fadfa6100d2cebc4abb03c72c6d4\" value=\"1\" label=\"向___人民法院起诉\" checked=\"true\" />向<span style=\"border-width: initial; border-style: none; border-color: initial; background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"682c044b0c4e3900db6c9178e799369b&amp;-&amp;3\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"人民法院类型\" code=\"courtType\" placeholder=\"\" readonly=\"true\">甲方所在地</span>人民法院起诉</span></font></span><br /></p><p style=\"font-weight: bold; margin-top: 5px; margin-bottom: 5px;\">十五、合同文件组成</p><p><font face=\"宋体\" size=\"3\">1、政府采购采购文件;</font></p><p><font face=\"宋体\" size=\"3\">2、乙方提供的投标(响应)文件;</font></p><p><font face=\"宋体\" size=\"3\">3、甲方提供工程清单;</font></p><p><font face=\"宋体\" size=\"3\">4、投标(响应)承诺书:</font></p><p><font face=\"宋体\" size=\"3\">5、评标记录;</font></p><p><font face=\"宋体\" size=\"3\">6、中标(成交)通知书。</font></p><p><font face=\"宋体\" size=\"3\">本甲乙双方电子签章后生效，自签订之日起7个工作日内，将合同(电子版)通过政府采购管理平台上传至本级政府采购监督管理部门备案(纸质版合同根据甲乙双方需要自行签订留存)。</font></p><p><font face=\"宋体\" size=\"3\"><br /></font></p><div>\n" +
                "          <table id=\"borderDIYTable\" style=\"width: 98%; margin-right: 1%; margin-left: 1%; border-top: 1px solid rgb(215, 215, 215); border-right: 1px solid rgb(215, 215, 215);\">\n" +
                "          <tbody><tr><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">甲方（章）</font></td><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">乙方（章）</font></td></tr><tr><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\"><br /><br /><br /><br />签订时间：</font><font size=\"3\" face=\"宋体\"><input class=\"tagYaoAn\" id=\"883f51295cd60434d1e6a0473dd3299f&amp;-&amp;3\" tag=\"date\" parameter=\"yes\" data-date=\"yes\" readonly=\"readonly\" style=\"border-top: none; border-right: none; border-left: none; cursor: pointer; border-bottom: 1px solid; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" type=\"txt\" name=\"合同签订日期\" code=\"contractSignTime\" placeholder=\"\" /></font></td><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\"><br /><br /><br /><br />签订时间：<input class=\"tagYaoAn\" id=\"883f51295cd60434d1e6a0473dd3299f&amp;-&amp;2\" tag=\"date\" parameter=\"yes\" data-date=\"yes\" readonly=\"readonly\" style=\"border-top: none; border-right: none; border-left: none; cursor: pointer; border-bottom: 1px solid; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" type=\"txt\" name=\"合同签订日期\" code=\"contractSignTime\" placeholder=\"\" /></font></td></tr><tr><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">签订地点：<span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"0d918c00e833024d4f67d5db49f55659&amp;-&amp;5\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"甲方签章（签订）地点\" code=\"orgSinAddress\" placeholder=\"\" readonly=\"true\">哈尔滨</span></font></td><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">签订地点：<span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"e2d293048cae56d670b4faea14cd9894&amp;-&amp;6\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"乙方签章（签订）地点\" code=\"supplierSinAddress\" placeholder=\"\" readonly=\"true\">哈尔滨</span></font></td></tr><tr><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">单位地址：</font><font face=\"宋体\" size=\"3\"><span style=\"border-top: medium none currentcolor; border-right: medium none currentcolor; border-bottom: none; border-left: medium none currentcolor; border-image: none 100% / 1 / 0 stretch; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"ea8739f7593747385d1bee0413bf70ae&amp;-&amp;72\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"单位地址（甲方）\" code=\"registeredAddress\" placeholder=\"\" readonly=\"true\">哈尔滨市香坊区中山路68号</span></font></td><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">单位地址：<span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"8dbd7ae6287fa3fdd1f72b427c2e6b34&amp;-&amp;7\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"单位地址（乙方）\" code=\"deliveryAddress\" placeholder=\"\" readonly=\"true\">上海市浦东新区环林东路799弄65号1层</span></font></td></tr><tr><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">法定(或授权)代表人：</font><font face=\"宋体\" size=\"3\"><span style=\"border-top: medium none currentcolor; border-right: medium none currentcolor; border-bottom: none; border-left: medium none currentcolor; border-image: none 100% / 1 / 0 stretch; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"b21361f8c9cbc30ef8d9c347623f3b74&amp;-&amp;69\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"甲方联系人/法定代表人\" code=\"buyerProxy\" placeholder=\"\" readonly=\"true\">葛洪</span></font></td><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">法定(或授权)代表人：<span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"5941b7b1a6f8c7c03e10be8230fbf3fa&amp;-&amp;9\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"乙方联系人/法定/授权\" code=\"supplierProxy\" placeholder=\"\" readonly=\"true\">赵蒙海</span></font></td></tr><tr><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">委托代理人：</font><font face=\"宋体\" size=\"3\"><span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"2389a05b300068226c39738c06908043&amp;-&amp;70\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"甲方代表\" code=\"buyerLink\" placeholder=\"\" readonly=\"true\">王金梁</span></font></td><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">委托代理人：<span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"5392562601fd99ba84f9aa2ee4ae24a0&amp;-&amp;10\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"乙方代表/法定代表人\" code=\"supplierLink\" placeholder=\"\" readonly=\"true\">张元亭</span></font></td></tr><tr><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">电话：<span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"0411bef6a5a6cf7bf6a1615610beade6&amp;-&amp;5\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"联系电话（甲方）\" code=\"buyerLinkMobile\" placeholder=\"\" readonly=\"true\">0451-88830066</span></font></td><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">电话：<span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"fa8b8c3031c4f529a2846850d04cc33e&amp;-&amp;3\" tag=\"number\" parameter=\"yes\" type=\"number\" name=\"联系电话（乙方）\" code=\"supplierLinkMobile\" placeholder=\"\" readonly=\"true\">17612446280</span></font></td></tr><tr><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">电子邮箱：</font><font face=\"宋体\" size=\"3\"><span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"6a37924a92e734404f2c3f50ddf95129&amp;-&amp;66\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"甲方电子邮箱\" code=\"buyerEmail\" placeholder=\"\" readonly=\"true\">27446179@qq.com</span></font></td><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">电子邮箱：</font><font face=\"宋体\" size=\"3\"><span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"399e9f1c39399e9869ba4133488467e4&amp;-&amp;61\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"乙方电子邮箱\" code=\"supplierEmail\" placeholder=\"\" readonly=\"true\">zhangyuanting@tech-winning.com</span></font></td></tr><tr><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">开户银行：</font><font face=\"宋体\" size=\"3\"><span style=\"border-top: medium none currentcolor; border-right: medium none currentcolor; border-bottom: none; border-left: medium none currentcolor; border-image: none 100% / 1 / 0 stretch; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"17f6a7c71369d97083bf5e0662f56c9a&amp;-&amp;51\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"甲方开户银行\" code=\"orgBankName\" placeholder=\"\" readonly=\"true\">中国银行哈尔滨开发区支行</span></font></td><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">开户银行：</font><font face=\"宋体\" size=\"3\"><span style=\"border-top: medium none currentcolor; border-right: medium none currentcolor; border-bottom: none; border-left: medium none currentcolor; border-image: none 100% / 1 / 0 stretch; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"9ff24f80fcc6eaa1f646c79d745731df&amp;-&amp;62\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"开户银行（乙方）\" code=\"bankName\" placeholder=\"\" readonly=\"true\">上海浦东发展银行第一营业部支行</span></font></td></tr><tr><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">账号：<span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"6685d9c5363e27ac4be89e3ec80d2c29&amp;-&amp;1\" tag=\"number\" parameter=\"yes\" type=\"number\" name=\"甲方账号\" code=\"orgBankAccount\" placeholder=\"\" readonly=\"true\">172740543994</span></font></td><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">账号：<span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"31ffdb1be9814045e19de1c83991d61f&amp;-&amp;2\" tag=\"number\" parameter=\"yes\" type=\"number\" name=\"银行账号（乙方）\" code=\"bankAccount\" placeholder=\"\" readonly=\"true\">97990078801300004539</span></font></td></tr><tr><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">账号名称：</font><font face=\"宋体\" size=\"3\"><span style=\"border-top: medium none currentcolor; border-right: medium none currentcolor; border-bottom: none; border-left: medium none currentcolor; border-image: none 100% / 1 / 0 stretch; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"161235560884f2a0c5b7f7508d984579&amp;-&amp;52\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"甲方账号名称\" code=\"orgBankAccountName\" placeholder=\"\" readonly=\"true\">黑龙江省医疗保障局</span></font></td><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font size=\"3\" face=\"宋体\">账号名称：<span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"72a939d5c868aeda597b019f5dda8ad1&amp;-&amp;5\" tag=\"txt\" parameter=\"yes\" type=\"text\" name=\"乙方账号名称\" code=\"supplierBankAccountName\" placeholder=\"\" readonly=\"true\">上海金仕达卫宁软件科技有限公司</span></font></td></tr><tr><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">邮政编码：<span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"2df9630598fcfea067e3ed2174f215ca&amp;-&amp;4\" tag=\"number\" parameter=\"yes\" type=\"number\" name=\"甲方邮政编码\" code=\"orgPostalCode\" placeholder=\"\" readonly=\"true\">150036</span></font></td><td style=\"line-height: 32px; vertical-align: top; width: 50%; border-bottom: 1px solid rgb(215, 215, 215); border-left: 1px solid rgb(215, 215, 215); padding: 3px 5px !important;\"><font face=\"宋体\" size=\"3\">邮政编码：<span style=\"border-width: initial; border-style: none; border-color: initial; color: rgb(61, 61, 61); background: rgb(255, 255, 255);\" class=\"tagYaoAn\" id=\"f21794049ec41772c74090956b395ad3&amp;-&amp;2\" tag=\"number\" parameter=\"yes\" type=\"number\" name=\"乙方邮政编码\" code=\"supplierPostalCode\" placeholder=\"\" readonly=\"true\">200000</span></font></td></tr></tbody></table></div><p><br /></p><p><font face=\"宋体\" size=\"3\"><br /></font></p><p><font face=\"宋体\" size=\"3\"><br /></font></p></body></html>`\n" +
                " ";
        String filePath = "C:\\Users\\lls\\Desktop\\test\\1.doc"; // 指定文件路径

        // 使用Jsoup解析HTML
        Document doc = Jsoup.parse(htmlContent);

        // 查找所有的<select>标签
        Elements selects = doc.select("select");

        // 遍历所有的<select>标签并替换为<span>标签
        for (Element select : selects) {
            Element selectedOption = select.select("option[selected]").first();
            // 创建一个新的span元素
            Element span = new Element("font");
            if (selectedOption == null) {
                // 设置span的值为选中的option的值
                span.text("");
            } else {
                // 设置span的值为选中的option的值
                span.text(selectedOption.text());
            }
            // 使用span替换select
            select.replaceWith(span);

        }
        // 使用Jsoup选择器找到所有的checkbox元素
        Elements checkboxes = doc.select("input[type=checkbox]");

        // 遍历所有checkbox元素，并在每个checkbox前添加一个span元素
        for (Element checkbox : checkboxes) {
            Element span = new Element(org.jsoup.parser.Tag.valueOf("font"), "C:\\Users\\lls\\Desktop\\test");

            if (checkbox.hasAttr("checked")) {
                span.attr("style", "background-color: #0075ff; font-size: 13px;display: inline-block;width:13px;height:12px;color:#ffffff;text-align:center");
                // 创建一个新的span元素
                span.text("√"); // 你可以根据需要设置span的内容
                // 在checkbox前添加span元素
                checkbox.before(span);
            } else {
                span.attr("style", "border:1px solid #8c8a8c;background-color: #ffffff; font-size: 13px;display: inline-block;width:13px;height:3px;color:#ffffff;text-align:center");
                // 创建一个新的span元素
                span.text("√"); // 你可以根据需要设置span的内容
                // 在checkbox前添加span元素
                checkbox.before(span);
            }
            checkbox.before(span);
        }
        String htmlcontent = doc.html();
        File wordFile = Html2WordUtil.htmlBytes2WordFile(htmlcontent.getBytes(), "C:\\Users\\lls\\Desktop\\test\\22");

        // 将HTML字节码转换为Word文件
//            File wordFile = Html2WordUtil.htmlBytes2WordFile(contractDO.getContractContent(), localFolderPath);
//             //将存于path的pdf文件上到minio的路径FileUploadPathEnum.MODEL.getPath(vo.getCode(),vo.getName())
        //  byte[] bytes = Html2WordUtil.readHtmlFile(localFolderPath);
        //Document doc = Jsoup.parse(buildHtml);

        // 将Document对象的内容写入文件


//        // 输出替换后的HTML内容
//        System.out.println(doc.body().html());
//        File file = new File(filePath);
//        try (FileWriter writer = new FileWriter(filePath)) {
//            writer.write(doc.html());
//            System.out.println("字符串已成功写入文件。");
//        } catch (IOException e) {
//            System.err.println("写入文件时发生错误: " + e.getMessage());
//        }


    }

    /**
     * 新增模板html转word
     */
    private Long html2word(ContractDO contractDO) {
        if (0 == contractDO.getContractContent().length) {
            return 0L;
        }
        String fileName = contractDO.getName() + "." + SUFFIX_DOCX;
        Long newFileId = 0L;
        String uuid = String.valueOf(UUID.randomUUID());
        String path = MODEL_RTF_PDF_TEMP_PATH + uuid + ".pdf";
        //富文本生成pdf，存查看的文件id地址
        String folderId = "TEMP-" + IdUtil.fastSimpleUUID();
        String localFolderPath = READY_TO_UPLOAD_PATH + "/" + folderId;
        FileUtil.touch(path);
        // 将HTML字节码转换为Word文件
        File wordFile = Html2WordUtil.htmlBytes2WordFile(contractDO.getContractContent(), localFolderPath);
        //将存于path的pdf文件上到minio的路径FileUploadPathEnum.MODEL.getPath(vo.getCode(),vo.getName())
        try {
            newFileId = fileApi.uploadFile(fileName, FileUploadPathEnum.CONTRACT_DRAFT.getPath(uuid, fileName), Html2WordUtil.readHtmlFile(localFolderPath));
        } catch (IOException e) {
            e.printStackTrace();
            log.error("合同发起内部审批流程-html转换docx失败");
        }
        FileUtil.del(path);
        FileUtil.del(localFolderPath);
        return newFileId;
    }

    /**
     * html转doc
     */
    private Long html2Doc(ContractDO contractDO) {
        if (0 == contractDO.getContractContent().length) {
            return 0L;
        }
        log.info("===== html2Doc开始转换 =====");
        String fileName = contractDO.getName() + "." + SUFFIX_DOCX;
        Long newFileId = 0L;
        String uuid = String.valueOf(UUID.randomUUID());
        String path = MODEL_RTF_PDF_TEMP_PATH + uuid + "." + SUFFIX_DOCX;
        //富文本生成pdf，存查看的文件id地址
        String folderId = "TEMP-" + IdUtil.fastSimpleUUID();
        String localFolderPath = READY_TO_UPLOAD_PATH + "/" + folderId;
        FileUtil.touch(path);

        try {
            //new String(htmlBytes,"utf-8");
            String htmlContent = buildHtml(new String(contractDO.getContractContent(), "utf-8"));
//            System.out.println("htmlcontent");
//            System.out.println(htmlContent);
            Document doc = Jsoup.parse(htmlContent);
            // byte[] bytes = buildHtml.getBytes("utf-8");
            File wordFile = Html2WordUtil.htmlString2WordFile(htmlContent, localFolderPath);

            // 将HTML字节码转换为Word文件
//            File wordFile = Html2WordUtil.htmlBytes2WordFile(contractDO.getContractContent(), localFolderPath);
//             //将存于path的pdf文件上到minio的路径FileUploadPathEnum.MODEL.getPath(vo.getCode(),vo.getName())
            byte[] bytes = Html2WordUtil.readHtmlFile(localFolderPath);

            newFileId = fileApi.uploadFile(fileName, FileUploadPathEnum.CONTRACT_DRAFT.getPath(uuid, fileName), bytes);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("合同发起内部审批流程-html转换doc失败");
        }
        FileUtil.del(path);
        FileUtil.del(localFolderPath);
        return newFileId;
    }

    public String buildHtml(String htmlContent) {
        // 使用Jsoup解析HTML
        Document doc = Jsoup.parse(htmlContent);
        
        Elements tables = doc.select("[id=\"DIYTable\"]");
        if(tables != null && tables.size() > 0) {
            Element table = tables.first();
            Elements tds = table.select("td");
            //匹配三列的货物类模板
            if(tds != null && tds.size()  == 3) {
                //第一列没有什么内容
                if(tds.get(0).html() != null && tds.get(0).text().length()<5 ) {
                    String html = "";
                    for (Element td : tds) {
                        html+=td.html();
                        //div.appendChildren(td.children());
                    }
                    table.before("<div style=\"margin-left: 150px;\" id=\"\">"+html+"</div><br />");
                    table.remove();
                }
            }
            
        }
        
        
        // 查找所有的<select>标签
        Elements selects = doc.select("select");

        // 遍历所有的<select>标签并替换为<span>标签
        for (Element select : selects) {
            Element selectedOption = select.select("option[selected]").first();
            // 创建一个新的span元素
            Element span = new Element("span");
            if (selectedOption == null) {
                // 设置span的值为选中的option的值
                span.text("");
            } else {
                // 设置span的值为选中的option的值
                span.text(selectedOption.text());
            }
            // 使用span替换select
            select.replaceWith(span);

        }
        // 使用Jsoup选择器找到所有的checkbox元素
        Elements checkboxes = doc.select("input[type=checkbox]:not([style*='display:none']):not([hidden])");

        // 遍历所有checkbox元素，并在每个checkbox前添加一个span元素
        for (Element checkbox : checkboxes) {
            Element span = new Element(org.jsoup.parser.Tag.valueOf("span"), "");

            if (checkbox.hasAttr("checked")) {
                span.attr("style", "background-color: #0075ff; font-size: 14px;display: inline-block;width:13px;height:12px;color:#ffffff;text-align:center");
                // 创建一个新的span元素
                span.text("√"); // 你可以根据需要设置span的内容
                // 在checkbox前添加span元素
                checkbox.before(span);
            } else {
                span.attr("style", "border:1px solid #8c8a8c;color:#c7c7c7;background-color: #c7c7c7; font-size: 14px;display: inline-block;width:13px;height:12px;text-align:center");
                // 创建一个新的span元素
                span.text("√"); // 你可以根据需要设置span的内容
                // 在checkbox前添加span元素
                checkbox.before(span);
            }
            //checkbox.before(span);
            checkbox.remove();
        }

        return doc.html();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createProcess(Long loginUserId, BpmContractCreateReqVO bpmCreateReqVO) {
        ContractDO contractDO = contractMapper.selectById(bpmCreateReqVO.getId());

        if (contractDO == null) {
            throw exception(ErrorCodeConstants.NO_DATA_FIND_ERROR);
        }
        //upload为3,合同起草方式为上传文件起草，并且pdfFileId为0时，不能提交审批，没有上传合同文件，需要上传合同文件
        if(contractDO.getUpload().equals(ContractUploadTypeEnums.ORDER_DRAFT.getCode()) &&
                contractDO.getContractSourceType().equals(ContractSourceTypeEnums.UPLOAD.getCode()) && contractDO.getPdfFileId() == 0){
            throw exception(ErrorCodeConstants.SYSTEM_ERROR,"合同文件为空");
        }
        //判断合同是否已经超过签署时间
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
        contractDO.setStatus(ContractStatusEnums.CHECKING.getCode());
        contractDO.setSubmitTime(LocalDateTime.now());
        //发送时设置为未签署
        contractDO.setIsSign(0);
        //动态配置是否转word文件
        String v = systemConfigApi.getConfigByKey(SystemConfigKeyEnums.IF_CONVERT_DOC.getKey());
        if (IfEnums.YES.getCode().equals(v)) {
            //合同html转word
            enhanceContractWordFile(contractDO);
        }
        contractMapper.updateById(contractDO);

        //更新合同版本
        contractVersionService.save(new ContractVersionSaveReqVO().setContractId(bpmCreateReqVO.getId()));

        //删除原来的流程
        bpmContractMapper.delete(new LambdaQueryWrapperX<BpmContract>().eq(BpmContract::getContractId, contractDO.getId()));
        //1.插入请求单
        BpmContract bpmContract = new BpmContract().setContractId(contractDO.getId())
                .setContractCode(contractDO.getCode()).setContractName(contractDO.getName())
                .setContractType(contractDO.getContractType()).setReason(bpmCreateReqVO.getReason())
                .setUserId(loginUserId).setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());

        bpmContractMapper.insert(bpmContract);
        //2.发起 BPM 流程
        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("reason", bpmCreateReqVO.getReason());
        processInstanceVariables.put("amount", contractDO.getAmount());
        processInstanceVariables.put("ifAudit", 0);
        Double auditAmount = 10000000D;
        String auditType = "工程类";
        //判断是否需要审计岗位审批(工程类并且金额大于1000万)
        if (auditAmount <= contractDO.getAmount()) {
            ContractType contractType = contractTypeMapper.selectById(contractDO.getContractType());
            if (ObjectUtil.isNotNull(contractType)) {
                processInstanceVariables.put("contractType", contractType.getName());
                if (auditType.equals(contractType.getName())) {
                    processInstanceVariables.put("ifAudit", 1);
                }
            }
        }
        if (ObjectUtil.isNotEmpty(contractDO.getContractType())) {
            ContractType contractType = contractTypeMapper.selectById(contractDO.getContractType());
            if (ObjectUtil.isNotEmpty(contractType) && ObjectUtil.isNotEmpty(contractType.getDraftApprovalProcess())) {
                PROCESS_KEY = contractType.getDraftApprovalProcess();
            }
        }
        Map<String, List<Long>> startUserSelectAssignees = new HashMap<>();
        String processInstanceId = processInstanceApi.createProcessInstance(loginUserId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setStartUserSelectAssignees(startUserSelectAssignees)
                        .setVariables(processInstanceVariables).setBusinessKey(bpmContract.getId()));

        //3.将工作流的编号，更新到申请单中
        bpmContractMapper.updateById(new BpmContract().setId(bpmContract.getId()).setProcessInstanceId(processInstanceId));

        //合同审批相关消息提醒触发
        TaskForWarningReqDTO taskParams = new TaskForWarningReqDTO()
                .setProcessInstanceId(processInstanceId);
        warningEventPublisher.setEvent(new EcmsWarningEvent(this).setFlag("1").setTaskParams(taskParams).setModelCode(WarningBusinessEnum.CONTRACT_APPROVE.getCode()));
        // 文件留痕
        fileVersionEventPublisher.sendEvent(
                new FileVersionEvent(this)
                        .setBusinessId(contractDO.getId())
                        .setBusinessType(FileVersionEnums.CONTRACT.getCode())
                        .setRemark("提交合同")
        );

        return bpmContract.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String backProcess(BpmContractCreateReqVO bpmContractCreateReqVO) {
        // 根据ID查询合同和合同扩展信息
        ContractDO contractDO1 = contractMapper.selectById(bpmContractCreateReqVO.getId());
        TradingContractExtDO tradingContractExtDO = tradingContractExtMapper.selectById(bpmContractCreateReqVO.getId());
        // 如果合同存在
        if (ObjectUtil.isNotEmpty(contractDO1)) {
            // 创建一个新的ContractDO对象，设置ID、isSign和status
            ContractDO updateContractDO = new ContractDO()
                    .setId(bpmContractCreateReqVO.getId())
                    .setIsSign(0)
                    .setStatus(ContractStatusEnums.TO_BE_CHECK.getCode());

            // 如果合同的upload是3，并且合同扩展信息存在且remark9为"99"，则设置pdfFileId为0
            if (contractDO1.getUpload().equals(ContractUploadTypeEnums.ORDER_DRAFT.getCode())
                    && ObjectUtil.isNotEmpty(tradingContractExtDO)
                    && "99".equals(tradingContractExtDO.getRemark9())) {
                updateContractDO.setPdfFileId(0L);
                // 更新合同拓展表
                tradingContractExtMapper.updateById(new TradingContractExtDO()
                        .setId(bpmContractCreateReqVO.getId())
                        .setPdfFileId(0L));
            }
            //如果合同upload是4，将合同状态变为已删除
            if (contractDO1.getUpload().equals(ContractUploadTypeEnums.THIRD_PARTY.getCode())) {
                updateContractDO.setStatus(ContractStatusEnums.CONTRACT_AUDITSTATUS_DELETE.getCode());
            }
            // 更新合同信息
            contractMapper.updateById(updateContractDO);
        }

        //删除流程
        BpmContract bpmContract = bpmContractMapper.selectOne(new LambdaQueryWrapperX<BpmContract>()
                .eqIfPresent(BpmContract::getContractId, bpmContractCreateReqVO.getId()));
        if (ObjectUtil.isNotNull(bpmContract)) {
            bpmContractMapper.deleteById(bpmContract);
        }
        ContractDO contractDO = contractMapper.selectById(bpmContractCreateReqVO.getId());
        if (ObjectUtil.isNotEmpty(contractDO) &&
                contractDO.getUpload().equals(ContractUploadTypeEnums.ORDER_DRAFT.getCode()) ||
                contractDO.getUpload().equals(ContractUploadTypeEnums.THIRD_PARTY.getCode())){
            if (contractDO.getUpload().equals(ContractUploadTypeEnums.ORDER_DRAFT.getCode())
                    && contractDO.getStatus().equals(ContractStatusEnums.CHECKING.getCode()) ){
                // 单位端起草的合同在 审批中 退回时不需要调退回至草稿接口 ---- upload=3 status=12
            }else{
                //推送到电子合同
                Map<String, Object> bodyParam = new HashMap<>();
                bodyParam.put("client_id", clientId);
                bodyParam.put("client_secret", clientSecret);
                String token = contractProcessApi.oauthCenterToken(bodyParam, null, null, null, null, null);
                JSONObject jsonObject = JSONObject.parseObject(token);
                if(ObjectUtil.isEmpty(bpmContractCreateReqVO.getReason())){
                    throw exception(ErrorCodeConstants.REASON_NOT_NULL);
                }
                EncryptDTO encryptDTO = null;
                try {
                    encryptDTO = Sm4Utils.convertParam(new ContractProcessDTO().setContractId(bpmContractCreateReqVO.getId()).setReason(bpmContractCreateReqVO.getReason()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                String result = contractProcessApi.backContract(jsonObject.getString("access_token"), encryptDTO);
                log.info("电子合同返回结果" + result);
                JSONObject resultJson = JSONObject.parseObject(result);
                if (!"0".equals(resultJson.getString("code"))) {
                     throw exception(new ErrorCode(500,resultJson.getString("msg")));
                    //throw new RuntimeException(result);
                }
            }
        }
        return "true";
    }

    private void enhanceContractWordFile(ContractDO contractDO) {
        if (ObjectUtil.isNotNull(contractDO.getContractContent()) && ObjectUtil.isNotEmpty(contractDO.getContractContent())) {
            Long wordFileId = html2Doc(contractDO);
            contractDO.setFileAddId(wordFileId);
            contractDO.setEditType(1);
        }
    }

    @Override
    public PageResult<BpmContractRespVO> getApprovePage(BpmContractPageReqVO reqVO) {
        //查询当前人待办和已处理的合同审批流程数据
        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(SecurityFrameworkUtils.getLoginUserId(), Collections.singleton(PROCESS_KEY));

        if (CollectionUtil.isEmpty(allRelationProcessInstanceInfos)) {
            return new PageResult<>();
        }
        reqVO.setProcessInstanceIds(CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId));

        PageResult<BpmContract> performancePage = bpmContractMapper.selectPage(reqVO);

        if (CollectionUtil.isEmpty(performancePage.getList())) {
            return new PageResult<>();
        }

        PageResult<BpmContractRespVO> result = BpmContractConverter.INSTANCE.convertPage(performancePage);

        return enhance(result, allRelationProcessInstanceInfos);
    }

    private PageResult<BpmContractRespVO> enhance(PageResult<BpmContractRespVO> result, List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos) {

        List<ContractProcessInstanceRelationInfoRespDTO> todoList = allRelationProcessInstanceInfos.stream().filter(item -> item.getProcessResult().equals(BpmProcessInstanceResultEnum.PROCESS.getResult())).collect(Collectors.toList());

        Map<String, String> processTaskMap = CollectionUtils.convertMap(todoList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId, ContractProcessInstanceRelationInfoRespDTO::getTaskId);

        List<Long> userIds = result.getList().stream().map(BpmContractRespVO::getUserId).collect(Collectors.toList());
        List<AdminUserRespDTO> userInfoList = adminUserApi.getUserList(userIds);
        Map<Long, AdminUserRespDTO> userInfoMap = CollectionUtils.convertMap(userInfoList, AdminUserRespDTO::getId);
        result.getList().forEach(item -> {
            AdminUserRespDTO userInfo = userInfoMap.get(item.getUserId());
            if (userInfo != null) {
                item.setUserName(userInfo.getNickname());
            }
            BpmProcessInstanceResultEnum instance = BpmProcessInstanceResultEnum.getInstance(item.getResult());
            if (instance != null) {
                item.setResultStr(instance.getDesc());
            }
            String taskIdStr = processTaskMap.get(item.getProcessInstanceId());
            if (StringUtils.isNotBlank(taskIdStr)) {
                item.setTaskId(taskIdStr);
            }
        });

        return result;
    }

    @Override
    public BigBpmContractRespVO getBpmAllTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO) {
        // 查询所有任务(已过滤掉已取消的任务)
//        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(ActivityConfigurationEnum.CONTRACT_DRAFT_APPROVE.getDefinitionKey());
        //查询合同类型表，取起草流程key
        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>()
                .eq(ContractType::getTypeStatus, 1));
        List<String> definitionKeys = contractTypes.stream().map(ContractType::getDraftApprovalProcess).filter(Objects::nonNull).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());

        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        if (ObjectUtil.isNotEmpty(definitionKeys)) {
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(definitionKeys.get(0), definitionKeys.subList(1, definitionKeys.size()).toArray(new String[0]));

        } else {
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey(ActivityConfigurationEnum.CONTRACT_DRAFT_APPROVE.getDefinitionKey());
        }
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<ContractDO> doPageResult = contractMapper.selectApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);

    }

    @Override
    public BigBpmContractRespVO getBpmDoneTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO) {
        Integer taskResult = StatusConstants.TEMP_INTEGER;
        // 查询待办任务
        if (ObjectUtil.isNotNull(pageVO.getTaskResult())) {
            taskResult = pageVO.getTaskResult();
        }
        //查询合同类型表，取起草流程key
        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>()
                .eq(ContractType::getTypeStatus, 1));
        List<String> definitionKeys = contractTypes.stream().map(ContractType::getDraftApprovalProcess).filter(Objects::nonNull).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        //获得已处理任务数据(已过滤掉已取消的任务),可筛选审批状态
        if (ObjectUtil.isNotEmpty(definitionKeys)) {
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(definitionKeys.get(0), taskResult, definitionKeys.subList(1, definitionKeys.size()).toArray(new String[0]));

        } else {
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult(ActivityConfigurationEnum.CONTRACT_DRAFT_APPROVE.getDefinitionKey(), taskResult);
        }
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);

        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<ContractDO> doPageResult = contractMapper.selectApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);

    }

    @Override
    public BigBpmContractRespVO getBpmToDoTaskPage(Long loginUserId, CommonBpmAutoPageReqVO pageVO) {
        //查询合同类型表，取起草流程key
        List<ContractType> contractTypes = contractTypeMapper.selectList(new LambdaQueryWrapperX<ContractType>().eq(ContractType::getTypeStatus, 1));
        List<String> definitionKeys = contractTypes.stream().map(ContractType::getDraftApprovalProcess).filter(Objects::nonNull).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        // 查询待办任务
        if (ObjectUtil.isNotEmpty(definitionKeys)) {
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(definitionKeys.get(0), definitionKeys.subList(1, definitionKeys.size()).toArray(new String[0]));
        } else {
            processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey(ActivityConfigurationEnum.CONTRACT_DRAFT_APPROVE.getDefinitionKey());
        }
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 获得 ProcessInstance
        List<String> instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        pageVO.setInstanceIdList(instanceIdList);
        PageResult<ContractDO> doPageResult = contractMapper.selectApprovePage(pageVO);
        return enhanceBpmPage(doPageResult, instanceRelationInfoRespDTOMap);
    }

    private BigBpmContractRespVO enhanceBpmPage(PageResult<ContractDO> doPageResult, Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap) {
        List<ContractDO> doList = doPageResult.getList();

        List<BpmContractRespVO> respVOList = ContractConverter.INSTANCE.convertBpmDO2Resp(doList);
        if (CollectionUtil.isNotEmpty(doList)) {
            Map<String, ContractDO> contractDOMap = CollectionUtils.convertMap(doList, ContractDO::getId);
            List<ContractType> contractTypeList = contractTypeMapper.selectList();
            //合同类型map
            Map<String, ContractType> contractTypeMap = CollectionUtils.convertMap(contractTypeList, ContractType::getId);
            //发起人信息map
            List<AdminUserRespDTO> userRespDTOList = adminUserApi.getUserList();
            Map<Long, AdminUserRespDTO> userRespDTOMap = CollectionUtils.convertMap(userRespDTOList, AdminUserRespDTO::getId);


            //流程信息
            List<String> dolIdList = doList.stream().map(ContractDO::getId).collect(Collectors.toList());
            List<BpmContract> bpmDOList = bpmContractMapper.selectList(new LambdaQueryWrapperX<BpmContract>().inIfPresent(BpmContract::getContractId, dolIdList));
            Map<String, BpmContract> bpmDOMap = CollectionUtils.convertMap(bpmDOList, BpmContract::getContractId);

            List<String> instanceList = bpmDOList.stream().map(BpmContract::getProcessInstanceId).filter(Objects::nonNull).collect(Collectors.toList());

            Map<String, BpmTaskAllInfoRespVO> taskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            Map<String, BpmTaskAllInfoRespVO> taskEndTimeMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            Map<String, BpmTaskAllInfoRespVO> taskDoneEndTimeMap = new HashMap<String, BpmTaskAllInfoRespVO>();

            Long loginUserId = getLoginUserId();
            List<BpmTaskAllInfoRespVO> originalTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> taskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> taskEndTimeAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

            //待处理的任务
            Map<String, BpmTaskAllInfoRespVO> toDoTaskMap = new HashMap<String, BpmTaskAllInfoRespVO>();
            List<BpmTaskAllInfoRespVO> toDoTaskAllInfoRespVOList = new ArrayList<BpmTaskAllInfoRespVO>();

            if (CollectionUtil.isNotEmpty(instanceList)) {
                originalTaskAllInfoRespVOList = bpmTaskApi.getAllTaskInfoRespByProcessInstanceIds(Long.valueOf(loginUserId), instanceList);

                toDoTaskAllInfoRespVOList = EcontractUtil.distinctTaskNullEndTimeByUserId(originalTaskAllInfoRespVOList, getLoginUserId());
                toDoTaskMap = CollectionUtils.convertMap(toDoTaskAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            }

            //有结束时间的流程任务
            if (CollectionUtil.isNotEmpty(instanceList)) {
                taskEndTimeAllInfoRespVOList = EcontractUtil.distinctTaskHaveEndTime(originalTaskAllInfoRespVOList);
                taskEndTimeMap = CollectionUtils.convertMap(taskEndTimeAllInfoRespVOList, BpmTaskAllInfoRespVO::getProcessInstanceId);
            }


            for (BpmContractRespVO respVO : respVOList) {
                if(respVO.getPlatform() != null){
                    respVO.setPlatformName(PlatformEnums.getInstance(respVO.getPlatform()).getInfo());
                }
                ContractDO contractDO = contractDOMap.get(respVO.getId());
                if (ObjectUtil.isNotNull(contractDO)) {
                    respVO.setContractName(contractDO.getName());
                    respVO.setContractType(contractDO.getContractType());
                    respVO.setContractCode(contractDO.getCode());
                    respVO.setUpload(contractDO.getUpload());
                }
                ContractType contractType = contractTypeMap.get(respVO.getContractType());
                if (ObjectUtil.isNotNull(contractType)) {
                    respVO.setContractTypeName(contractType.getName());
                }
                BpmContract bpmDO = bpmDOMap.get(respVO.getId());
                if (ObjectUtil.isNotNull(bpmDO)) {
                    //最新审批时间
                    BpmTaskAllInfoRespVO endTimeTask = taskEndTimeMap.get(bpmDO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(endTimeTask)) {
                        respVO.setApproveTime(endTimeTask.getEndTime());
                    }
                    //流程实例
                    respVO.setProcessInstanceId(bpmDO.getProcessInstanceId());
                    //提交人
                    AdminUserRespDTO userRespDTO = userRespDTOMap.get(Long.valueOf(bpmDO.getCreator()));
                    if (ObjectUtil.isNotNull(userRespDTO)) {
                        respVO.setSubmitter(userRespDTO.getNickname());
                    }
                    //流程状态
                    respVO.setResult(bpmDO.getResult());
                    //流程任务
                    ContractProcessInstanceRelationInfoRespDTO processInstanceRelationInfoRespDTO = instanceRelationInfoRespDTOMap.get(bpmDO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(processInstanceRelationInfoRespDTO)) {
                        respVO.setTaskId(processInstanceRelationInfoRespDTO.getTaskId());
                    }
                    //历史任务信息（所有审批人）
                    BpmTaskAllInfoRespVO historyTask = taskEndTimeMap.get(bpmDO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(historyTask)) {
                        respVO.setHisTaskResult(historyTask.getResult());
                    }
                    //全部审批列表的审批状态(找不到当前登录人的待办任务，既是已办任务
                    BpmTaskAllInfoRespVO toDoTask = toDoTaskMap.get(bpmDO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(toDoTask)) {
                        respVO.setAssigneeId(toDoTask.getAssigneeUserId());
                        respVO.setFlowableStatus(FlowableUtil.getFlowableStatus(loginUserId, toDoTask.getAssigneeUserId()));

                    }
                    //审批状态(全部里)
                    else {
                        respVO.setFlowableStatus(FlowableStatusEnums.DONE.getCode());
                    }
                    //已审批任务的状态赋值（当前人对该审批的最近的一次操作结果）
                    ContractProcessInstanceRelationInfoRespDTO infoRespDTO = instanceRelationInfoRespDTOMap.get(bpmDO.getProcessInstanceId());
                    if (ObjectUtil.isNotNull(infoRespDTO)) {
                        respVO.setDoneTaskResult(infoRespDTO.getProcessResult());
                    }
                }
            }
            PageResult<BpmContractRespVO> pageResult = new PageResult<BpmContractRespVO>();
            pageResult.setList(respVOList).setTotal(doPageResult.getTotal());
            BigBpmContractRespVO respVO = new BigBpmContractRespVO().setPageResult(pageResult);
            //获取配置
            respVO.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.CONTRACT_DRAFT_APPROVE)));
            return respVO;

        }
        BigBpmContractRespVO result = new BigBpmContractRespVO()
                .setPageResult(new PageResult<BpmContractRespVO>()
                        .setList(Collections.emptyList())
                        .setTotal(doPageResult.getTotal())
                );
        result.setFlowableConfigRespVO(SystemConfigDTOConverter.INSTANCE.dto2resp(systemConfigApi.getFlowableByProDefKey(ActivityConfigurationEnum.CONTRACT_DRAFT_APPROVE)));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createProcessV1(Long loginUserId, String id) throws Exception {

        TradingContractExtDO orderContractDO = tradingContractExtMapper.selectById(id);

        if (orderContractDO == null) {
            throw exception(ErrorCodeConstants.NO_DATA_FIND_ERROR);
        }
        orderContractDO.setStatus(HLJContractStatusEnums.CONTRACT_AUDITSTATUS_SURE.getCode());
        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("buyerOrgId", orderContractDO.getBuyerOrgId());
        processInstanceVariables.put("supplierId", orderContractDO.getSupplierId());
        String processInstanceId = processInstanceApi.createProcessInstance(loginUserId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(TRADING_PROCESS_KEY)
                        .setVariables(processInstanceVariables)
                        .setBusinessKey(id));
        orderContractDO.setProcessInstanceId(processInstanceId);
        //3.将工作流的编号，更新到申请单中
        tradingContractExtMapper.updateById(orderContractDO);
        return processInstanceId;
    }


}
