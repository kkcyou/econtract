package com.yaoan.module.econtract.service.contract.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.SM2;
import cn.hutool.http.HttpUtil;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaoan.framework.common.util.io.FileUtils;
import com.yaoan.framework.common.util.json.JsonUtils;
import com.yaoan.module.econtract.api.contract.SppGPTApi;
import com.yaoan.module.econtract.api.contract.dto.SppGPTDetailDTO;
import com.yaoan.module.econtract.api.contract.dto.SppGPTGetTokenDTO;
import com.yaoan.module.econtract.api.contract.dto.SppGPTResponseDTO;
import com.yaoan.module.econtract.api.gcy.buyplan.OrgApi;
import com.yaoan.module.econtract.controller.admin.contract.vo.*;
import com.yaoan.module.econtract.controller.admin.contract.vo.extraction.*;
import com.yaoan.module.econtract.controller.admin.relative.vo.RelativeByUserRespVO;
import com.yaoan.module.econtract.controller.admin.supervise.vo.ContractPayeeInfoVO;
import com.yaoan.module.econtract.controller.admin.supervise.vo.PaymentPlanVO;
import com.yaoan.module.econtract.controller.admin.supervise.vo.SuperviseGoodsVO;
import com.yaoan.module.econtract.controller.admin.supervise.vo.SuperviseProjectVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDetailsDO;
import com.yaoan.module.econtract.dal.dataobject.gcy.buyplan.EcmsGcyBuyPlanBill;
import com.yaoan.module.econtract.dal.mysql.contract.ContractDetailsMapper;
import com.yaoan.module.econtract.dal.mysql.gcy.buyplan.EcmsGcyBuyPlanBillMapper;
import com.yaoan.module.econtract.dal.mysql.gcy.buyplan.EcmsGcyBuyPlanMapper;
import com.yaoan.module.econtract.enums.supervise.SupplierSizeEnum;
import com.yaoan.module.econtract.framework.sppgpt.SppGPTProperties;
import com.yaoan.module.econtract.service.contract.ContractExtractionService;
import com.yaoan.module.econtract.service.contract.com.cxf.client.LawyeeService;
import com.yaoan.module.econtract.util.MinioUtils;
import com.yaoan.module.infra.api.file.FileApi;
import com.yaoan.module.infra.api.file.dto.FileDTO;
import com.yaoan.module.infra.enums.FileUploadPathEnum;
import com.yaoan.module.system.api.region.RegionApi;
import com.yaoan.module.system.api.user.OrganizationApi;
import com.yaoan.module.system.api.user.SupplyApi;
import com.yaoan.module.system.api.user.dto.OrganizationDTO;
import com.yaoan.module.system.api.user.dto.SupplyDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.module.econtract.util.EcontractUtil.inputStreamToBytes;

@Service
@Slf4j
public class ContractExtractionServiceImpl implements ContractExtractionService {
    @Resource
    private FileApi fileApi;
    @Resource
    private SppGPTProperties sppGPTProperties;
    @Resource
    private SppGPTApi sppGPTApi;
    @Resource
    private ContractDetailsMapper contractDetailsMapper;
    @Resource
    private EcmsGcyBuyPlanBillMapper buyPlanBillMapper;
    @Resource
    private SupplyApi supplyApi;
    @Resource
    private RegionApi regionApi;
    @Resource
    private OrganizationApi orgApi;
    @Resource
    private MinioUtils minioUtils;

    @Override
    public List<JsonFormRespVO> toJsonForm(FormToJsonReqVO formToJsonReqVO) throws Exception {
        //1.法益webservice提取信息
        Long fileId = formToJsonReqVO.getFileId();
        String path = fileApi.getURL(fileId);

        String result = "";
        try {
            // 创建 Web 服务客户端代理工厂
            JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
            factory.setServiceClass(LawyeeService.class);

            // 设置 Web 服务地址
            factory.setAddress("http://218.247.254.201:8070/cxf/services/lawyeeservice");

            // 创建 Web 服务客户端代理
            LawyeeService client = (LawyeeService) factory.create();
            byte[] fileBytes = readFileToByteArray(path);
            String token = "<Users>\n" +
                    "\t<item>\n" +
                    "\t\t<OrgID>0</OrgID>\n" +
                    "\t\t<OrgName>最高人民法院</OrgName>\n" +
                    "\t\t<DptName>业务部门</DptName>\n" +
                    "\t\t<UserName>910009</UserName>\n" +
                    "\t\t<UserID>1</UserID>\n" +
                    "\t\t<PostID></PostID>\n" +
                    "\t\t<DptID>1</DptID>\n" +
                    "\t\t<PostName>专员</PostName>\n" +
                    "\t</item>\n" +
                    "</Users>";
            // 调用 Web 服务的方法
//            result = client.getXMLByText("1001", fileBytes, "bs", token);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //2.封装数据转换
//        String xmlData = result;
        // 将JSON字符串解析为JsonObject
//        JsonObject jsonObject = JsonParser.parseString(xmlData).getAsJsonObject();

        // 获取"xml"对应的值
//        xmlData = jsonObject.get("xml").getAsString();
        String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<合同基本信息 合同标识=\"e7ff8e1abe59d29c0920bd8b266foq36\" 合同编号=\"MF-2021-006\" 合同发文号=\"XX-2023-2608\" 合同名称=\"三明市花卉订购合同\" 合同封面名称原文=\"福建省三明市花卉订购合同\" 合同正文名称原文=\"三明市花卉订购合同\" 合同分类=\"订购回购合同\" 合同一级分类=\"买卖合同\" 合同二级分类=\"订购回购合同\" 合同三级分类=\"\" 是否官方范本=\"2\" 行业类别=\"143\" 文本性质=\"2\" 合同来源=\"1\" 适用方=\"各方\" 发布机构名称=\"三明市市场监督管理局\" 标准发布机构编码=\"\" 标准发布机构名称=\"三明市市场监督管理局\" 合同签署日期=\"20210209\" 合同生效日期=\"20210210\" 当事方合同签署地址=\"三明市\" 合同全文内容=\"三明市花卉订购合同&#xA;合同编号：MF-2021-006&#xA;买方：张某某&#xA;卖方：三明市花圃&#xA;根据《中华人民共和国合同法》及有关法律法规规定，买卖双方在平等、自愿、公平的基础上，经充分协商，就花卉订购事宜订立本合同。&#xA;一、花卉交售品种、数量、价格&#xA;1．卖方全年向买方交付各种花卉一千盆（枝、株），&#xA;分月分旬交售花卉的品种、数量、日期由双方另行约定。&#xA;2．花卉收购价格：买方以不低于伍拾元的保护价进行收购，或由双方于收购前按花卉等级和收购时市场收购价协商确定，但不得低于保护价。&#xA;3．花卉总价款为伍万元，由买方预付定金：壹万元；&#xA;二、质量要求&#xA;1．花卉品种的规格：&#xA;2．等级等质量要求：有国家标准的执行国家标准，未有国家标准的，按双方约定标准。&#xA;三、收购时间与方式&#xA;花卉交售时间由双方协商，买方须提前一天提出次日应交售的品种、数量，并以书面方式通知卖方。卖方交售与预约允许上下浮动10%。&#xA;四、包装要求与费用承担&#xA;买方提供包装标准及材料，负责包装费用，卖方按买方要求进行包装。&#xA;五、付款方式：&#xA;买方收货验收合格后，采用现金方式付款，最迟不得超过三天。&#xA;六、买方的权利和义务&#xA;1．及时验收卖方交售的花卉，收货后即支付花卉款。&#xA;2．确定花卉等级要按照国家规定的质量标准，不得任意压级压价。&#xA;3．有权拒收卖方交售的不符合国家行业质量标准的花卉，但必须向对方说明理由。&#xA;七、卖方的权利和义务&#xA;1．必须按照规定施用花肥花药，严禁对花卉使用违禁农药、化肥。&#xA;2．保证按合同约定的品种和数量种植花卉。&#xA;3．花卉种植如受气候条件的影响，允许在减产10%的幅度内不以违约论。&#xA;八、买方违约责任&#xA;1．没有按合同约定收购，造成花卉腐烂等损失，或故意压级压价，除赔偿卖方的损失外，应向卖方支付该批花卉总价值40%的违约金。&#xA;2．拖延支付花卉款，应参照银行关于拖延付款的规定，向卖方偿付违约金。&#xA;九、卖方违约责任&#xA;1．非因不可抗力，未完成当月合同总数量的90%，应向买方支付未完成部分花卉总价值40%的违约金。&#xA;2．交售使用违禁农药、化肥的花卉，应按该批货值40%向买方支付违约金。因此造成人身伤亡，卖方应承担一切责任。&#xA;十、不可抗力&#xA;如因不可抗力造成卖方不能按合同约定交售花卉，买方应据实减少卖方所承担的交售数量，卖方可不承担违约责任。&#xA;十一、争议解决方式&#xA;本合同在履行过程中发生争议的，由双方协商解决，如协商不成，可以依法向三明市沙县区人民法院起诉。&#xA;十二、合同期限&#xA;本合同有效期一年，自2021年2月10日至2022年2月10日止。&#xA;十三、本合同自双方签字盖章之日起生效。本合同未尽事宜，按照《合同法》等国家有关规定，经合同双方协商，作出补充协议，补充协议与本合同具有同等法律效力。&#xA;本合同一式二份，合同双方各执一份。&#xA;甲方：张某某（签章）&#xA;乙方：三明市花圃（签章）&#xA;签订日期：2021年2月9日\">\n" +
                "\t<合同当事人 当事人姓名或名称=\"张某某\" 自然人证照类型=\"身份证\" 自然人证照号码=\"110110199001011100\" 统一社会信用代码=\"\" 当事人类别=\"3\" 所属方类型=\"1\" 当事人身份=\"买方\" 法定代表人或负责人=\"\" 代理人类型=\"2\" 代理人姓名=\"张某胜\" 代理人身份或职务=\"\" 当事人简称=\"\" 联系地址=\"三明市沙县区\" 联系方式=\"138****5647\" 联系人=\"张某胜\" 当事方合同签署日期=\"20210209\" 当事方合同签署地址=\"三明市三元区\"/>\n" +
                "\t<合同当事人 当事人姓名或名称=\"三明市花圃\" 自然人证照类型=\"\" 自然人证照号码=\"\" 统一社会信用代码=\"220220190020203366\" 当事人类别=\"1\" 所属方类型=\"2\" 当事人身份=\"卖方\" 法定代表人或负责人=\"王某鹏\" 代理人类型=\"1\" 代理人姓名=\"王某飞\" 代理人身份或职务=\"销售经理\" 当事人简称=\"三明市花圃\" 联系地址=\"三明市三元区\" 联系方式=\"0598-***805\" 联系人=\"王某飞\" 当事方合同签署日期=\"20210208\" 当事方合同签署地址=\"三明市三元区\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"1\" 段落编项=\"\" 段落编项原文=\"\" 合同全文结构一级分段=\"3\" 段落框架结构=\"合同名称\" 段落内容=\"三明市花卉订购合同\" 段落字数=\"9\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"2\" 段落编项=\"\" 段落编项原文=\"\" 合同全文结构一级分段=\"4\" 段落框架结构=\"合同首部\" 段落内容=\"合同编号：MF-2021-006\" 段落字数=\"16\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"3\" 段落编项=\"\" 段落编项原文=\"\" 合同全文结构一级分段=\"4\" 段落框架结构=\"合同当事人\" 段落内容=\"买方：张某某\" 段落字数=\"6\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"4\" 段落编项=\"\" 段落编项原文=\"\" 合同全文结构一级分段=\"4\" 段落框架结构=\"合同当事人\" 段落内容=\"卖方：三明市花圃\" 段落字数=\"8\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"5\" 段落编项=\"\" 段落编项原文=\"\" 合同全文结构一级分段=\"6\" 段落框架结构=\"合同正文\" 段落内容=\"根据《中华人民共和国合同法》及有关法律法规规定，买卖双方在平等、自愿、公平的基础上，经充分协商，就花卉订购事宜订立本合同。\" 段落字数=\"61\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"6\" 段落编项=\"第1条\" 段落编项原文=\"一\" 合同全文结构一级分段=\"6\" 段落框架结构=\"条\" 段落内容=\"一、花卉交售品种、数量、价格\" 段落字数=\"14\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"7\" 段落编项=\"第1条第1款\" 段落编项原文=\"1\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"1．卖方全年向买方交付各种花卉一千盆（枝、株），\" 段落字数=\"24\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"8\" 段落编项=\"第1条第1款\" 段落编项原文=\"\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"分月分旬交售花卉的品种、数量、日期由双方另行约定。\" 段落字数=\"25\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"9\" 段落编项=\"第1条第2款\" 段落编项原文=\"2\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"2．花卉收购价格：买方以不低于伍拾元的保护价进行收购，或由双方于收购前按花卉等级和收购时市场收购价协商确定，但不得低于保护价。\" 段落字数=\"63\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"10\" 段落编项=\"第1条第3款\" 段落编项原文=\"3\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"3．花卉总价款为伍万元，由买方预付定金：壹万元；\" 段落字数=\"24\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"11\" 段落编项=\"第2条\" 段落编项原文=\"二\" 合同全文结构一级分段=\"6\" 段落框架结构=\"条\" 段落内容=\"二、质量要求\" 段落字数=\"6\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"12\" 段落编项=\"第2条第1款\" 段落编项原文=\"1\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"1．花卉品种的规格：\" 段落字数=\"10\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"13\" 段落编项=\"第2条第2款\" 段落编项原文=\"2\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"2．等级等质量要求：有国家标准的执行国家标准，未有国家标准的，按双方约定标准。\" 段落字数=\"39\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"14\" 段落编项=\"第3条\" 段落编项原文=\"三\" 合同全文结构一级分段=\"6\" 段落框架结构=\"条\" 段落内容=\"三、收购时间与方式\" 段落字数=\"9\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"15\" 段落编项=\"第3条第1款\" 段落编项原文=\"\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"花卉交售时间由双方协商，买方须提前一天提出次日应交售的品种、数量，并以书面方式通知卖方。卖方交售与预约允许上下浮动10%。\" 段落字数=\"61\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"16\" 段落编项=\"第4条\" 段落编项原文=\"四\" 合同全文结构一级分段=\"6\" 段落框架结构=\"条\" 段落内容=\"四、包装要求与费用承担\" 段落字数=\"11\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"17\" 段落编项=\"第4条第1款\" 段落编项原文=\"\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"买方提供包装标准及材料，负责包装费用，卖方按买方要求进行包装。\" 段落字数=\"31\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"18\" 段落编项=\"第5条\" 段落编项原文=\"五\" 合同全文结构一级分段=\"6\" 段落框架结构=\"条\" 段落内容=\"五、付款方式：\" 段落字数=\"7\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"19\" 段落编项=\"第5条第1款\" 段落编项原文=\"\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"买方收货验收合格后，采用现金方式付款，最迟不得超过三天。\" 段落字数=\"28\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"20\" 段落编项=\"第6条\" 段落编项原文=\"六\" 合同全文结构一级分段=\"6\" 段落框架结构=\"条\" 段落内容=\"六、买方的权利和义务\" 段落字数=\"10\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"21\" 段落编项=\"第6条第1款\" 段落编项原文=\"1\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"1．及时验收卖方交售的花卉，收货后即支付花卉款。\" 段落字数=\"24\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"22\" 段落编项=\"第6条第2款\" 段落编项原文=\"2\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"2．确定花卉等级要按照国家规定的质量标准，不得任意压级压价。\" 段落字数=\"30\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"23\" 段落编项=\"第6条第3款\" 段落编项原文=\"3\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"3．有权拒收卖方交售的不符合国家行业质量标准的花卉，但必须向对方说明理由。\" 段落字数=\"37\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"24\" 段落编项=\"第7条\" 段落编项原文=\"七\" 合同全文结构一级分段=\"6\" 段落框架结构=\"条\" 段落内容=\"七、卖方的权利和义务\" 段落字数=\"10\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"25\" 段落编项=\"第7条第1款\" 段落编项原文=\"1\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"1．必须按照规定施用花肥花药，严禁对花卉使用违禁农药、化肥。\" 段落字数=\"30\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"26\" 段落编项=\"第7条第2款\" 段落编项原文=\"2\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"2．保证按合同约定的品种和数量种植花卉。\" 段落字数=\"20\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"27\" 段落编项=\"第7条第3款\" 段落编项原文=\"3\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"3．花卉种植如受气候条件的影响，允许在减产10%的幅度内不以违约论。\" 段落字数=\"34\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"28\" 段落编项=\"第8条\" 段落编项原文=\"八\" 合同全文结构一级分段=\"6\" 段落框架结构=\"条\" 段落内容=\"八、买方违约责任\" 段落字数=\"8\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"29\" 段落编项=\"第8条第1款\" 段落编项原文=\"1\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"1．没有按合同约定收购，造成花卉腐烂等损失，或故意压级压价，除赔偿卖方的损失外，应向卖方支付该批花卉总价值40%的违约金。\" 段落字数=\"61\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"30\" 段落编项=\"第8条第2款\" 段落编项原文=\"2\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"2．拖延支付花卉款，应参照银行关于拖延付款的规定，向卖方偿付违约金。\" 段落字数=\"34\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"31\" 段落编项=\"第9条\" 段落编项原文=\"九\" 合同全文结构一级分段=\"6\" 段落框架结构=\"条\" 段落内容=\"九、卖方违约责任\" 段落字数=\"8\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"32\" 段落编项=\"第9条第1款\" 段落编项原文=\"1\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"1．非因不可抗力，未完成当月合同总数量的90%，应向买方支付未完成部分花卉总价值40%的违约金。\" 段落字数=\"48\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"33\" 段落编项=\"第9条第2款\" 段落编项原文=\"2\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"2．交售使用违禁农药、化肥的花卉，应按该批货值40%向买方支付违约金。因此造成人身伤亡，卖方应承担一切责任。\" 段落字数=\"54\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"34\" 段落编项=\"第10条\" 段落编项原文=\"十\" 合同全文结构一级分段=\"6\" 段落框架结构=\"条\" 段落内容=\"十、不可抗力\" 段落字数=\"6\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"35\" 段落编项=\"第10条第1款\" 段落编项原文=\"\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"如因不可抗力造成卖方不能按合同约定交售花卉，买方应据实减少卖方所承担的交售数量，卖方可不承担违约责任。\" 段落字数=\"51\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"36\" 段落编项=\"第11条\" 段落编项原文=\"十一\" 合同全文结构一级分段=\"6\" 段落框架结构=\"条\" 段落内容=\"十一、争议解决方式\" 段落字数=\"9\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"37\" 段落编项=\"第11条第1款\" 段落编项原文=\"\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"本合同在履行过程中发生争议的，由双方协商解决，如协商不成，可以依法向三明市沙县区人民法院起诉。\" 段落字数=\"47\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"38\" 段落编项=\"第12条\" 段落编项原文=\"十二\" 合同全文结构一级分段=\"6\" 段落框架结构=\"条\" 段落内容=\"十二、合同期限\" 段落字数=\"7\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"39\" 段落编项=\"第12条第1款\" 段落编项原文=\"\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"本合同有效期一年，自2021年2月10日至2022年2月10日止。\" 段落字数=\"33\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"40\" 段落编项=\"第13条\" 段落编项原文=\"十三\" 合同全文结构一级分段=\"6\" 段落框架结构=\"条\" 段落内容=\"十三、本合同自双方签字盖章之日起生效。本合同未尽事宜，按照《合同法》等国家有关规定，经合同双方协商，作出补充协议，补充协议与本合同具有同等法律效力。\" 段落字数=\"74\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"41\" 段落编项=\"第13条第1款\" 段落编项原文=\"\" 合同全文结构一级分段=\"6\" 段落框架结构=\"款\" 段落内容=\"本合同一式二份，合同双方各执一份。\" 段落字数=\"17\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"42\" 段落编项=\"\" 段落编项原文=\"\" 合同全文结构一级分段=\"8\" 段落框架结构=\"合同尾部\" 段落内容=\"甲方：张某某（签章）\" 段落字数=\"10\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"43\" 段落编项=\"\" 段落编项原文=\"\" 合同全文结构一级分段=\"8\" 段落框架结构=\"合同尾部\" 段落内容=\"乙方：三明市花圃（签章）\" 段落字数=\"12\"/>\n" +
                "\t<合同全文结构信息 段落编码=\"\" 段落顺序号=\"44\" 段落编项=\"\" 段落编项原文=\"\" 合同全文结构一级分段=\"8\" 段落框架结构=\"合同尾部\" 段落内容=\"签订日期：2021年2月9日\" 段落字数=\"14\"/>\n" +
                "\t<合同条文信息 合同条文标识=\"\" 合同条文顺序号=\"1\" 合同条文编项=\"第1条\" 段落内容=\"一、花卉交售品种、数量、价格&#xA;1．卖方全年向买方交付各种花卉一千盆（枝、株），&#xA;分月分旬交售花卉的品种、数量、日期由双方另行约定。&#xA;2．花卉收购价格：买方以不低于伍拾元的保护价进行收购，或由双方于收购前按花卉等级和收购时市场收购价协商确定，但不得低于保护价。&#xA;3．花卉总价款为伍万元，由买方预付定金：壹万元；\" 合同条文一级分类=\"标的条款\" 合同条文二级分类=\"标的基本信息条款\"/>\n" +
                "\t<合同条文信息 合同条文标识=\"\" 合同条文顺序号=\"2\" 合同条文编项=\"第2条\" 段落内容=\"二、质量要求&#xA;1．花卉品种的规格：&#xA;2．等级等质量要求：有国家标准的执行国家标准，未有国家标准的，按双方约定标准。\" 合同条文一级分类=\"标的条款\" 合同条文二级分类=\"标的质量条款\"/>\n" +
                "\t<合同条文信息 合同条文标识=\"\" 合同条文顺序号=\"3\" 合同条文编项=\"第3条\" 段落内容=\"三、收购时间与方式&#xA;花卉交售时间由双方协商，买方须提前一天提出次日应交售的品种、数量，并以书面方式通知卖方。卖方交售与预约允许上下浮动10%。\" 合同条文一级分类=\"履行\" 合同条文二级分类=\"履行方式\">\n" +
                "\t\t<合同要素信息 合同要素名称=\"履约时间通知方式\" 合同要素取值=\"书面\"/>\n" +
                "\t</合同条文信息>\n" +
                "\t<合同条文信息 合同条文标识=\"\" 合同条文顺序号=\"4\" 合同条文编项=\"第4条\" 段落内容=\"四、包装要求与费用承担&#xA;买方提供包装标准及材料，负责包装费用，卖方按买方要求进行包装。\" 合同条文一级分类=\"标的条款\" 合同条文二级分类=\"标的包装方式条款\">\n" +
                "\t\t<合同要素信息 合同要素名称=\"包装费用承担方\" 合同要素取值=\"买方\"/>\n" +
                "\t</合同条文信息>\n" +
                "\t<合同条文信息 合同条文标识=\"\" 合同条文顺序号=\"5\" 合同条文编项=\"第5条\" 段落内容=\"五、付款方式：&#xA;买方收货验收合格后，采用现金方式付款，最迟不得超过三天。\" 合同条文一级分类=\"价款及结算方式条款\" 合同条文二级分类=\"结算方式条款\">\n" +
                "\t\t<合同要素信息 合同要素名称=\"付款方式\" 合同要素取值=\"现金\"/>\n" +
                "\t</合同条文信息>\n" +
                "\t<合同条文信息 合同条文标识=\"\" 合同条文顺序号=\"6\" 合同条文编项=\"第6条\" 段落内容=\"六、买方的权利和义务&#xA;1．及时验收卖方交售的花卉，收货后即支付花卉款。&#xA;2．确定花卉等级要按照国家规定的质量标准，不得任意压级压价。&#xA;3．有权拒收卖方交售的不符合国家行业质量标准的花卉，但必须向对方说明理由。\" 合同条文一级分类=\"法律效力条款\" 合同条文二级分类=\"\"/>\n" +
                "\t<合同条文信息 合同条文标识=\"\" 合同条文顺序号=\"7\" 合同条文编项=\"第7条\" 段落内容=\"七、卖方的权利和义务&#xA;1．必须按照规定施用花肥花药，严禁对花卉使用违禁农药、化肥。&#xA;2．保证按合同约定的品种和数量种植花卉。&#xA;3．花卉种植如受气候条件的影响，允许在减产10%的幅度内不以违约论。\" 合同条文一级分类=\"法律效力条款\" 合同条文二级分类=\"\"/>\n" +
                "\t<合同条文信息 合同条文标识=\"\" 合同条文顺序号=\"8\" 合同条文编项=\"第8条\" 段落内容=\"八、买方违约责任&#xA;1．没有按合同约定收购，造成花卉腐烂等损失，或故意压级压价，除赔偿卖方的损失外，应向卖方支付该批花卉总价值40%的违约金。&#xA;2．拖延支付花卉款，应参照银行关于拖延付款的规定，向卖方偿付违约金。\" 合同条文一级分类=\"违约责任条款\" 合同条文二级分类=\"违约金条款\"/>\n" +
                "\t<合同条文信息 合同条文标识=\"\" 合同条文顺序号=\"9\" 合同条文编项=\"第9条\" 段落内容=\"九、卖方违约责任&#xA;1．非因不可抗力，未完成当月合同总数量的90%，应向买方支付未完成部分花卉总价值40%的违约金。&#xA;2．交售使用违禁农药、化肥的花卉，应按该批货值40%向买方支付违约金。因此造成人身伤亡，卖方应承担一切责任。\" 合同条文一级分类=\"违约责任条款\" 合同条文二级分类=\"违约金条款\"/>\n" +
                "\t<合同条文信息 合同条文标识=\"\" 合同条文顺序号=\"10\" 合同条文编项=\"第10条\" 段落内容=\"十、不可抗力&#xA;如因不可抗力造成卖方不能按合同约定交售花卉，买方应据实减少卖方所承担的交售数量，卖方可不承担违约责任。\" 合同条文一级分类=\"不可抗力条款\" 合同条文二级分类=\"\"/>\n" +
                "\t<合同条文信息 合同条文标识=\"\" 合同条文顺序号=\"11\" 合同条文编项=\"第11条\" 段落内容=\"十一、争议解决方式&#xA;本合同在履行过程中发生争议的，由双方协商解决，如协商不成，可以依法向三明市沙县区人民法院起诉。\" 合同条文一级分类=\"争议解决条款\" 合同条文二级分类=\"\">\n" +
                "\t\t<合同要素信息 合同要素名称=\"争议解决方式\" 合同要素取值=\"协商或起诉\"/>\n" +
                "\t</合同条文信息>\n" +
                "\t<合同条文信息 合同条文标识=\"\" 合同条文顺序号=\"12\" 合同条文编项=\"第12条\" 段落内容=\"十二、合同期限&#xA;本合同有效期一年，自2021年2月10日至2022年2月10日止。\" 合同条文一级分类=\"生效和有效期条款\" 合同条文二级分类=\"\"/>\n" +
                "\t<合同条文信息 合同条文标识=\"\" 合同条文顺序号=\"13\" 合同条文编项=\"第13条\" 段落内容=\"十三、本合同自双方签字盖章之日起生效。本合同未尽事宜，按照《合同法》等国家有关规定，经合同双方协商，作出补充协议，补充协议与本合同具有同等法律效力。&#xA;本合同一式二份，合同双方各执一份。\" 合同条文一级分类=\"合同签约份数条款\" 合同条文二级分类=\"\"/>\n" +
                "\t<引用法律法规信息 引用法律法规名称=\"中华人民共和国合同法\" 引用条款编项=\"第六十二条第一项\" 引用条=\"第六十二条\" 引用法条内容=\"当事人就有关合同内容约定不明确，依照本法第六十一条的规定仍不能确定的，适用下列规定：&#xA;（一）质量要求不明确的，按照国家标准、行业标准履行；没有国家标准、行业标准的，按照通常标准或者符合合同目的的特定标准履行。\" 引用款=\"\" 引用项=\"第一项\" 引用目=\"\"/>\n" +
                "\t<引用法律法规信息 引用法律法规名称=\"中华人民共和国合同法\" 引用条款编项=\"第一百二十八条\" 引用条=\"第一百二十八条\" 引用法条内容=\"当事人可以通过和解或者调解解决合同争议。当事人不愿和解、调解或者和解、调解不成的，可以根据仲裁协议向仲裁机构申请仲裁。涉外合同的当事人可以根据仲裁协议向中国仲裁机构或者其他仲裁机构申请仲裁。当事人没有订立仲裁协议或者仲裁协议无效的，可以向人民法院起诉。当事人应当履行发生法律效力的判决、仲裁裁决、调解书；拒不履行的，对方可以请求人民法院执行。\" 引用款=\"\" 引用项=\"\" 引用目=\"\"/>\n" +
                "\t<履约信息 履约主体姓名或名称=\"三明市花圃\" 履约内容原文=\"卖方全年向买方交付各种花卉一千盆（枝、株），&#xA;分月分旬交售花卉的品种、数量、日期由双方另行约定。\" 履约内容类型=\"2\" 履约金额原文=\"\" 履约金额=\"\" 履约金额币种=\"\" 履约时间原文=\"全年\" 履约时间日期=\"\" 履约时间期限=\"360\"/>\n" +
                "\t<履约信息 履约主体姓名或名称=\"张某某\" 履约内容原文=\"花卉总价款为伍万元，由买方预付定金：壹万元；\" 履约内容类型=\"1\" 履约金额原文=\"壹万元\" 履约金额=\"10000\" 履约金额币种=\"人民币\" 履约时间原文=\"\" 履约时间日期=\"\" 履约时间期限=\"\"/>\n" +
                "\t<履约信息 履约主体姓名或名称=\"张某某\" 履约内容原文=\"买方收货验收合格后，采用现金方式付款，最迟不得超过三天。\" 履约内容类型=\"1\" 履约金额原文=\"\" 履约金额=\"\" 履约金额币种=\"\" 履约时间原文=\"最迟不得超过三天\" 履约时间日期=\"\" 履约时间期限=\"3\"/>\n" +
                "\t<履约信息 履约主体姓名或名称=\"张某某\" 履约内容原文=\"花卉交售时间由双方协商，买方须提前一天提出次日应交售的品种、数量，并以书面方式通知卖方。\" 履约内容类型=\"5\" 履约金额原文=\"\" 履约金额=\"\" 履约金额币种=\"\" 履约时间原文=\"提前一天\" 履约时间日期=\"\" 履约时间期限=\"1\"/>\n" +
                "\t<履约信息 履约主体姓名或名称=\"三明市花圃\" 履约内容原文=\"卖方按买方要求进行包装。\" 履约内容类型=\"5\" 履约金额原文=\"\" 履约金额=\"\" 履约金额币种=\"\" 履约时间原文=\"\" 履约时间日期=\"\" 履约时间期限=\"\"/>\n" +
                "</合同基本信息>\n";
        //创建返回list
        List<JsonFormRespVO> resultList = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xmlData)));

            // 创建JSON对象
            ObjectMapper mapper = new ObjectMapper();

            //固定表单
            if (CollectionUtil.isEmpty(formToJsonReqVO.getFormIdList())) {
                //封装该表单所有表项value
                List<JsonRespVO> valueList1 = new ArrayList<>();
                //提取合同基本信息
                NodeList nodeList1 = document.getElementsByTagName("合同基本信息");
                for (int i = 0; i < nodeList1.getLength(); i++) {
                    Element element = (Element) nodeList1.item(i);
                    String name = element.getAttribute("合同名称");
                    String code = element.getAttribute("合同编号");
                    String contractSignTime = element.getAttribute("合同签署日期");
                    contractSignTime = DateUtil.format(DateUtil.parse(contractSignTime), "yyyy-MM-dd");
                    String contractSignAddress = element.getAttribute("当事方合同签署地址");

                    valueList1.add(new JsonRespVO().setName("合同名称").setCode("name").setValue(name));
                    valueList1.add(new JsonRespVO().setName("合同编号").setCode("code").setValue(code));
                    valueList1.add(new JsonRespVO().setName("签约日期").setCode("contractSignTime").setValue(contractSignTime));
                    valueList1.add(new JsonRespVO().setName("签订地址").setCode("contractSignAddress").setValue(contractSignAddress));
                    valueList1.add(new JsonRespVO().setName("履约开始日期").setCode("performStartDate").setValue("2024-03-28"));
                    valueList1.add(new JsonRespVO().setName("履约结束日期").setCode("performEndDate").setValue("2024-03-28"));
                    valueList1.add(new JsonRespVO().setName("履约地点").setCode("performAddress").setValue("内蒙"));
                    valueList1.add(new JsonRespVO().setName("合同金额").setCode("totalMoney").setValue("26000"));
                    valueList1.add(new JsonRespVO().setName("合同金额（大写）").setCode("shiftMoney").setValue("贰万陆仟元整"));
                    valueList1.add(new JsonRespVO().setName("是否存在预付款").setCode("isPrepayment").setValue("0"));
                    valueList1.add(new JsonRespVO().setName("是否收取履约保证金").setCode("isPerformanceMoney").setValue("1"));
                    valueList1.add(new JsonRespVO().setName("是否缴纳质量保证金").setCode("isRetentionMoney").setValue("0"));
                    valueList1.add(new JsonRespVO().setName("履约保证金金额（元）").setCode("performanceMoney").setValue("222"));
                    valueList1.add(new JsonRespVO().setName("履约保证金缴纳形式").setCode("performanceMoneyType").setValue("2"));
                    valueList1.add(new JsonRespVO().setName("质量保证金金额（元）").setCode("retentionMoney").setValue(""));
                    valueList1.add(new JsonRespVO().setName("质量保证金缴纳形式").setCode("retentionMoneyType").setValue(""));
                }
                resultList.add(new JsonFormRespVO().setName("合同基本信息").setCode("contractBaseInfo").setValueList(valueList1));

                List<JsonRespVO> valueList2 = new ArrayList<>();
                NodeList nodeList2 = document.getElementsByTagName("合同当事人");
                for (int j = 0; j < nodeList2.getLength(); j++) {
                    Element partyElement = (Element) nodeList2.item(j);
                    String partyName = partyElement.getAttribute("当事人姓名或名称");
                    String idType = partyElement.getAttribute("自然人证照类型");
                    String idNumber = partyElement.getAttribute("自然人证照号码");
                    String creditCode = partyElement.getAttribute("统一社会信用代码");
                    String partyType = partyElement.getAttribute("当事人类别");
                    String belongType = partyElement.getAttribute("所属方类型");
                    String partyIdentity = partyElement.getAttribute("当事人身份");
                    String legalRepresentative = partyElement.getAttribute("法定代表人或负责人");
                    String agentType = partyElement.getAttribute("代理人类型");
                    String agentName = partyElement.getAttribute("代理人姓名");
                    String agentIdentity = partyElement.getAttribute("代理人身份或职务");
                    String partyAbbreviation = partyElement.getAttribute("当事人简称");
                    String contactAddress = partyElement.getAttribute("联系地址");
                    String contactInformation = partyElement.getAttribute("联系方式");
                    String contactPerson = partyElement.getAttribute("联系人");
                    String partyContractSignDate = partyElement.getAttribute("当事方合同签署日期");
                    String partyContractSignAddress = partyElement.getAttribute("当事方合同签署地址");

                    valueList2.add(new JsonRespVO().setName("当事人姓名或名称").setCode("partyName").setValue(partyName));
                    valueList2.add(new JsonRespVO().setName("自然人证照类型").setCode("idType").setValue(idType));
                    valueList2.add(new JsonRespVO().setName("自然人证照号码").setCode("idNumber").setValue(idNumber));
                    valueList2.add(new JsonRespVO().setName("统一社会信用代码").setCode("creditCode").setValue(creditCode));
                    valueList2.add(new JsonRespVO().setName("当事人类别").setCode("partyType").setValue(partyType));
                    valueList2.add(new JsonRespVO().setName("所属方类型").setCode("belongType").setValue(belongType));
                    valueList2.add(new JsonRespVO().setName("当事人身份").setCode("partyIdentity").setValue(partyIdentity));
                    valueList2.add(new JsonRespVO().setName("法定代表人或负责人").setCode("legalRepresentative").setValue(legalRepresentative));
                    valueList2.add(new JsonRespVO().setName("代理人类型").setCode("agentType").setValue(agentType));
                    valueList2.add(new JsonRespVO().setName("代理人姓名").setCode("agentName").setValue(agentName));
                    valueList2.add(new JsonRespVO().setName("代理人身份或职务").setCode("agentIdentity").setValue(agentIdentity));
                    valueList2.add(new JsonRespVO().setName("当事人简称").setCode("partyAbbreviation").setValue(partyAbbreviation));
                    valueList2.add(new JsonRespVO().setName("联系地址").setCode("contactAddress").setValue(contactAddress));
                    valueList2.add(new JsonRespVO().setName("联系方式").setCode("contactInformation").setValue(contactInformation));
                    valueList2.add(new JsonRespVO().setName("联系人").setCode("contactPerson").setValue(contactPerson));
                    valueList2.add(new JsonRespVO().setName("当事方合同签署日期").setCode("partyContractSignDate").setValue(partyContractSignDate));
                    valueList2.add(new JsonRespVO().setName("当事方合同签署地址").setCode("partyContractSignAddress").setValue(partyContractSignAddress));
                }
                resultList.add(new JsonFormRespVO().setName("签订各方信息").setCode("contractParty").setValueList(valueList2));

                List<JsonRespVO> valueList3 = new ArrayList<>();
                NodeList nodeList3 = document.getElementsByTagName("履约信息");

                int performStartDateFlag = 0;
                int daysToAdd = 0;
                String performStartDate = "";
                Long totalMoney = 0L;
                for (int i = 0; i < nodeList3.getLength(); i++) {
                    Element element = (Element) nodeList3.item(i);
                    performStartDate = element.getAttribute("履约时间日期");
                    String days = element.getAttribute("履约时间期限");
                    String money = element.getAttribute("履约金额");

                    if (StringUtils.isNotEmpty(performStartDate)) {
                        performStartDateFlag++;
                    }
                    if (performStartDateFlag == 1) {
                        valueList3.add(new JsonRespVO().setName("履约开始日期").setCode("performStartDate").setValue(performStartDate));
                        performStartDateFlag++;
                    }
                    if (StringUtils.isNotEmpty(days)) {
                        daysToAdd = daysToAdd + Integer.parseInt(days);
                    }
                    if (StringUtils.isNotEmpty(money)) {
                        totalMoney = totalMoney + Long.parseLong(money);
                    }
                }
                if (daysToAdd > 0 && performStartDateFlag > 0) {
                    // 将performEndDate字符串转换为日期类型
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date endDate = sdf.parse(performStartDate);
                    // 使用Calendar类进行日期的加减操作
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(endDate);
                    calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);
                    // 获取加上天数后的日期
                    Date newDate = calendar.getTime();
                    // 将日期转换为字符串
                    String resultDate = sdf.format(newDate);
                    valueList3.add(new JsonRespVO().setName("履约结束日期").setCode("performEndDate").setValue(resultDate));
                }
                if (totalMoney > 0) {
                    valueList3.add(new JsonRespVO().setName("总金额").setCode("totalMoney").setValue(String.valueOf(totalMoney)));
                }
                resultList.add(new JsonFormRespVO().setName("履约信息").setCode("performInfo").setValueList(valueList3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }

    @Override
    public TokenRespVO generateToken() {
        String appId = sppGPTProperties.getAppId();
        String secret = sppGPTProperties.getSecret();
        String sign = getSign(appId, secret);
        //获取token
        SppGPTGetTokenDTO sppGPTGetTokenDTO = new SppGPTGetTokenDTO().setAppId(appId).setSign(sign);
        SppGPTResponseDTO stringSppGPTResponseDTO = sppGPTApi.generateToken(sppGPTGetTokenDTO);
        Object data = stringSppGPTResponseDTO.getData();
        JsonNode jsonNode = JsonUtils.parseTree(JsonUtils.toJsonString(data));
        TokenRespVO token = new TokenRespVO().setToken(jsonNode.get("token").asText());
        return token;
    }

    @Override
    public TaskIdRespVO upload(UploadReqVO vo) throws Exception {
        MultipartFile file = null;
        Map<String, Object> stringObjectMap = null;
        TaskIdRespVO result = new TaskIdRespVO();
        if (ObjectUtil.isNotEmpty(vo.getFileId())) {
            file = getFileAsMultipartFile(vo.getFileId(), result);

        } else {
            file = vo.getFile();
            String path = FileUtils.generatePath(IoUtil.readBytes(file.getInputStream()), file.getName());
            FileUploadPathEnum uploadPathEnum = FileUploadPathEnum.getInstance(vo.getCode());
            stringObjectMap = fileApi.uploadFileV3(file.getOriginalFilename(), uploadPathEnum, path, IoUtil.readBytes(file.getInputStream()));
            result.setFileId((Long) stringObjectMap.get("fileId"));
            result.setFileUrl((String) stringObjectMap.get("url"));
        }
        String token = "Bearer " + generateToken().getToken();
        SppGPTResponseDTO upload = sppGPTApi.uploadFile(token, "contract", file);
        Object data = upload.getData();
        JsonNode jsonNode = JsonUtils.parseTree(JsonUtils.toJsonString(data));
        result.setTaskId(jsonNode.get("taskId").asText());
        return result;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ContractExtractRespVO detail(TaskIdReqVO vo) {
        String token = "Bearer " + generateToken().getToken();
        String taskIds = vo.getTaskIds();
        SppGPTDetailDTO sppGPTDetailDTO = new SppGPTDetailDTO().setTaskIds(taskIds);
        SppGPTResponseDTO sppGPTResponseDTO = sppGPTApi.contractDetail(token, sppGPTDetailDTO);
        HashMap map = sppGPTResponseDTO.getData();
        String jsonString = JsonUtils.toJsonString(map);
        ContractDetailRespVO contractDetailRespVO = JsonUtils.parseObject(jsonString, ContractDetailRespVO.class);
        List<Contract> contracts = null;
        if (contractDetailRespVO != null) {
            contracts = contractDetailRespVO.getContracts();
        }
        String[] split = taskIds.split(",");
        List<String> ids = Arrays.asList(split);
        List<ContractDetailsDO> ContractDetailsSppDOS = contractDetailsMapper.selectBatchIds(ids);
        //如果为空，则全部新增
        if (ObjectUtil.isEmpty(ContractDetailsSppDOS)) {
            List<ContractDetailsDO> contractDetailsList = new ArrayList<>();
            if (contracts != null) {
                for (Contract contract : contracts) {
                    ContractDetailsDO ContractDetailsSppDO = getContractDetailsSppDO(contract);
                    contractDetailsList.add(ContractDetailsSppDO);
                }
            }
            if (!contractDetailsList.isEmpty()) {
                contractDetailsMapper.insertBatch(contractDetailsList);
            }
        } else {
            //如果不为空，需判断是哪条数据不为空，哪条数据为空，不为空的数据进行修改，为空的数据进行添加
            // 存在的 taskId 集合
            Set<String> taskIdList = ContractDetailsSppDOS.stream()
                    .map(ContractDetailsDO::getId)
                    .collect(Collectors.toSet());
            // 要更新的列表
            List<ContractDetailsDO> updateList = new ArrayList<>();
            // 要新增的列表
            List<ContractDetailsDO> insertList = new ArrayList<>();
            if (contracts != null) {
                for (Contract contract : contracts) {
                    ContractDetailsDO ContractDetailsSppDO = getContractDetailsSppDO(contract);
                    if (taskIdList.contains(contract.getTaskId())) {
                        // 更新
                        updateList.add(ContractDetailsSppDO);
                    } else {
                        // 新增
                        insertList.add(ContractDetailsSppDO);
                    }
                }
            }
            // 执行更新操作
            if (!updateList.isEmpty()) {
                for (ContractDetailsDO update : updateList) {
                    contractDetailsMapper.updateById(update);
                }
            }
            // 执行新增操作
            if (!insertList.isEmpty()) {
                contractDetailsMapper.insertBatch(insertList);
            }

        }

        ContractExtractRespVO contractExtractRespVO = mappingField(contracts);

        return contractExtractRespVO;
    }


    @Override
    public ContractDetailRespVO getDetail(String taskId) {
        ContractDetailsDO ContractDetailsSppDO = contractDetailsMapper.selectOne("task_id", taskId);
        if (ObjectUtil.isNotEmpty(ContractDetailsSppDO)) {
            ContractBaseInfo contractBaseInfo = JsonUtils.parseObject(ContractDetailsSppDO.getContractBaseInfo(), ContractBaseInfo.class);
            List<PurchaseDetail> purchaseDetail = JsonUtils.parseArray(ContractDetailsSppDO.getPurchaseDetail(), PurchaseDetail.class);
            List<Suppliers> suppliers = JsonUtils.parseArray(ContractDetailsSppDO.getSuppliers(), Suppliers.class);
            List<SubSuppliers> subSuppliers = JsonUtils.parseArray(ContractDetailsSppDO.getSubSuppliers(), SubSuppliers.class);
            List<PaymentPlan> paymentPlan = JsonUtils.parseArray(ContractDetailsSppDO.getPaymentPlan(), PaymentPlan.class);
            AcceptRequirement acceptRequirement = JsonUtils.parseObject(ContractDetailsSppDO.getAcceptRequirement(), AcceptRequirement.class);
            ProjectManager projectManager = JsonUtils.parseObject(ContractDetailsSppDO.getProjectManager(), ProjectManager.class);
            Contract contract = new Contract()
                    .setTaskId(ContractDetailsSppDO.getId())
                    .setStatus(ContractDetailsSppDO.getStatus())
                    .setContractBaseInfo(contractBaseInfo)
                    .setPurchaseDetail(purchaseDetail)
                    .setSuppliers(suppliers)
                    .setSubSuppliers(subSuppliers)
                    .setPaymentPlan(paymentPlan)
                    .setAcceptRequirement(acceptRequirement)
                    .setProjectManager(projectManager);
            return new ContractDetailRespVO().setContracts(Collections.singletonList(contract));
        }
        return null;
    }

    @Override
    public TaskIdRespVO uploadByFileId(UploadByFileIdReqVO vo) throws Exception {
        MultipartFile file = null;
        Map<String, Object> stringObjectMap = null;
        TaskIdRespVO result = new TaskIdRespVO();
        if (ObjectUtil.isNotEmpty(vo.getFileId())) {
            file = getFileAsMultipartFile(vo.getFileId(), result);
        }
        String token = "Bearer " + generateToken().getToken();
        SppGPTResponseDTO upload = sppGPTApi.uploadFile(token, "contract", file);
        Object data = upload.getData();
        JsonNode jsonNode = JsonUtils.parseTree(JsonUtils.toJsonString(data));
        result.setTaskId(jsonNode.get("taskId").asText());
        return result;
    }

    private static byte[] readFileToByteArray(String fileUrl) throws IOException {
        URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        try (InputStream inputStream = connection.getInputStream()) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
            return output.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

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

    public MultipartFile getFileAsMultipartFile(Long fileId, TaskIdRespVO result) throws Exception {
        // 根据文件ID获取文件地址
        FileDTO fileDTO = fileApi.selectById(fileId);
        RestTemplate restTemplate = new RestTemplate();
        String fileUrl = minioUtils.generatePresignedUrl(fileDTO.getBucketName(), fileDTO.getPath());
        URL url = new URL(fileUrl);
        InputStream inputStream = url.openStream();
        byte[] fileBytes = inputStreamToBytes(inputStream);

//        ResponseEntity<byte[]> response = restTemplate.getForEntity(fileDTO.getUrl(), byte[].class);
//        byte[] fileBytes = response.getBody();
        result.setFileUrl(fileDTO.getUrl());
        result.setFileId(fileId);
        return new MockMultipartFile(fileDTO.getName(),
                fileDTO.getName(), fileDTO.getType(), fileBytes);
    }

    private ContractDetailsDO getContractDetailsSppDO(Contract contract) {
        return new ContractDetailsDO()
                .setId(contract.getTaskId())
                .setStatus(contract.getStatus())
                .setContractBaseInfo(JsonUtils.toJsonString(contract.getContractBaseInfo()))
                .setPurchaseDetail(JsonUtils.toJsonString(contract.getPurchaseDetail()))
                .setSuppliers(JsonUtils.toJsonString(contract.getSuppliers()))
                .setSubSuppliers(JsonUtils.toJsonString(contract.getSubSuppliers()))
                .setPaymentPlan(JsonUtils.toJsonString(contract.getPaymentPlan()))
                .setAcceptRequirement(JsonUtils.toJsonString(contract.getAcceptRequirement()))
                .setProjectManager(JsonUtils.toJsonString(contract.getProjectManager()));
    }

    private ContractExtractRespVO mappingField(List<Contract> contracts) {
        ContractBaseInfoRespVo baseInfo = new ContractBaseInfoRespVo();
        ArrayList<RelativeByUserRespVO> signatoryList = new ArrayList<>();

        ContractPayeeInfoVO contractPayeeInfoVO = new ContractPayeeInfoVO();
        List<SuperviseGoodsVO> goods = new ArrayList<>();
        List<PaymentPlanVO> paymentPlan = new ArrayList<>();
        SuperviseProjectVO projectInfo = new SuperviseProjectVO();
        AcceptRequirement acceptRequirement = new AcceptRequirement();
        String supplyId = null;
        if (contracts != null) {
            for (Contract contract : contracts) {
                baseInfo.setTaskId(contract.getTaskId() != null ? contract.getTaskId() : null);
                baseInfo.setStatus(contract.getStatus() != null ? contract.getStatus() : null);
                if (ObjectUtil.isNotEmpty(contract.getContractBaseInfo())) {
                    //合同基本信息
                    baseInfo.setCode(contract.getContractBaseInfo().getContractNo() != null ? contract.getContractBaseInfo().getContractNo() : null);
                    baseInfo.setName(contract.getContractBaseInfo().getContractName() != null ? contract.getContractBaseInfo().getContractName() : null);
                    baseInfo.setContractSignTime(contract.getContractBaseInfo().getSigningDate() != null ? contract.getContractBaseInfo().getSigningDate() : null);
                    baseInfo.setContractSignAddress(contract.getContractBaseInfo().getSigningPlace() != null ? contract.getContractBaseInfo().getSigningPlace() : null);
                    baseInfo.setPerformStartDate(contract.getContractBaseInfo().getPerformanceStartDate() != null ? contract.getContractBaseInfo().getPerformanceStartDate() : null);
                    baseInfo.setPerformEndDate(contract.getContractBaseInfo().getPerformanceEndDate() != null ? contract.getContractBaseInfo().getPerformanceEndDate() : null);
                    baseInfo.setPerformAddress(contract.getContractBaseInfo().getPerformancePlace() != null ? contract.getContractBaseInfo().getPerformancePlace() : null);
                    baseInfo.setTotalMoney(contract.getContractBaseInfo().getContractAmount() != null ? contract.getContractBaseInfo().getContractAmount() : null);
                    baseInfo.setIsPerformanceMoney(contract.getContractBaseInfo().getIsPerformanceDeposit() != null ? contract.getContractBaseInfo().getIsPerformanceDeposit() : null);
                    baseInfo.setPerformanceMoney(contract.getContractBaseInfo().getPerformanceDepositAmount() != null ? contract.getContractBaseInfo().getPerformanceDepositAmount() : null);
                    RelativeByUserRespVO orgSignInfo = new RelativeByUserRespVO();
                    orgSignInfo.setCompanyName(contract.getContractBaseInfo().getPurchaserUnit() != null ? contract.getContractBaseInfo().getPurchaserUnit() : null);
                    orgSignInfo.setContactTel(contract.getContractBaseInfo().getPurchaserPhone() != null ? contract.getContractBaseInfo().getPurchaserPhone() : null);
//                    orgSignInfo.setPartyAAddress(contract.getContractBaseInfo().getPurchaserAddress() != null ? contract.getContractBaseInfo().getPurchaserAddress() : null);
                    orgSignInfo.setFax(contract.getContractBaseInfo().getPurchaserFax() != null ? contract.getContractBaseInfo().getPurchaserFax() : null);
                    orgSignInfo.setContactName(contract.getContractBaseInfo().getPurchaserLiaison() != null ? contract.getContractBaseInfo().getPurchaserLiaison() : null);
                    //如果甲方是当前单位添加到签署方 否则不添加
                    if (ObjectUtils.isNotEmpty(contract.getContractBaseInfo().getPurchaserUnit())) {
                        OrganizationDTO orgInfoByName = orgApi.getOrgInfoByName(contract.getContractBaseInfo().getPurchaserUnit());
                        if (ObjectUtils.isNotEmpty(orgInfoByName)) {
                            orgSignInfo.setIsRegister(true);
                        }else {
                            orgSignInfo.setIsRegister(false);
                        }
                    }
                    if (ObjectUtil.isNotEmpty(orgSignInfo)) {
                        signatoryList.add(orgSignInfo);
                    }
                    //供应商信息
                    RelativeByUserRespVO supSignInfo = new RelativeByUserRespVO();
                    supSignInfo.setContactName(contract.getContractBaseInfo().getSupplierLiaison() != null ? contract.getContractBaseInfo().getSupplierLiaison() : null);
                    //根据供应商名称获取供应商id
                    if (ObjectUtil.isNotEmpty(contract.getContractBaseInfo().getSupplierName())) {
                        SupplyDTO supply = supplyApi.getSupplyByName(contract.getContractBaseInfo().getSupplierName());
                        if (ObjectUtil.isNotEmpty(supply)) {
                            supplyId = supply.getId();
                            supSignInfo.setId(supply.getId());
                            supSignInfo.setCode(supply.getOrgCode());
                            contractPayeeInfoVO.setSupplierId(supply.getId());
                        }
                    }
                    //供应商区域
                    if (ObjectUtil.isNotEmpty(contract.getContractBaseInfo().getSupplierArea())) {
                        String region = contract.getContractBaseInfo().getSupplierArea();
                        // 使用正则表达式提取省/自治区、市、旗/区等多层次
                        String[] splitRegions = region.split("(?<=自治区|省|市|区|旗|县|盟|会)(?=\\S)");
                        // 存储获取到的区域 ID
                        String[] regionIds = new String[splitRegions.length];
                        // 循环遍历每个分割后的区域
                        for (int i = 0; i < splitRegions.length; i++) {
                            regionIds[i] = regionApi.getRegionIdByName(splitRegions[i]);
                        }
                        String idsResult = String.join(",", regionIds);
//                        supSignInfo.setSupplierLocation(idsResult);
                        contractPayeeInfoVO.setSupplierLocation(idsResult);
                    }

                    supSignInfo.setCompanyName(contract.getContractBaseInfo().getSupplierName() != null ? contract.getContractBaseInfo().getSupplierName() : null);
                    supSignInfo.setContactTel(contract.getContractBaseInfo().getSupplierPhone() != null ? contract.getContractBaseInfo().getSupplierPhone() : null);
//                    supSignInfo.setRegisteredAddress(contract.getContractBaseInfo().getSupplierAddress() != null ? contract.getContractBaseInfo().getSupplierAddress() : null);
                    supSignInfo.setFax(contract.getContractBaseInfo().getSupplierFax() != null ? contract.getContractBaseInfo().getSupplierFax() : null);
//                    supSignInfo.setSupplierLocationName(contract.getContractBaseInfo().getSupplierArea() != null ? contract.getContractBaseInfo().getSupplierArea() : null);
//                    if (ObjectUtil.isNotEmpty(contract.getContractBaseInfo().getSupplierScale())) {
//                        SupplierSizeEnum instanceByName = SupplierSizeEnum.getInstanceByName(contract.getContractBaseInfo().getSupplierScale());
//                        signatoryInfo.setSupplierSizeCode(String.valueOf(instanceByName.getCode()));
//                        contractPayeeInfoVO.setSupplierSizeCode(String.valueOf(instanceByName.getCode()));
//                    }
                    if (ObjectUtil.isNotEmpty(contract.getSuppliers())) {
                        Suppliers suppliers = contract.getSuppliers().get(0);
                        supSignInfo.setBankAccountName(suppliers.getSupplierAccountName() != null ? suppliers.getSupplierAccountName() : null);
                        supSignInfo.setBankName(suppliers.getSupplierOpeningBank() != null ? suppliers.getSupplierOpeningBank() : null);
                        supSignInfo.setBankAccount(suppliers.getSupplierAccountNo() != null ? suppliers.getSupplierAccountNo() : null);
                    }
                    //判断相对方是否存在 不存在则不添加
                    if (ObjectUtils.isNotEmpty(supSignInfo)) {
                        if(ObjectUtils.isNotEmpty(supSignInfo.getContactName())){
                            supSignInfo.setIsRegister(true);
                        }else {
                            supSignInfo.setIsRegister(false);
                        }
                        signatoryList.add(supSignInfo);
                    }
//                    supSignInfo.setSupplierSize(contract.getContractBaseInfo().getSupplierScale() != null ? contract.getContractBaseInfo().getSupplierScale() : null);
                    contractPayeeInfoVO.setSupplierSize(contract.getContractBaseInfo().getSupplierScale() != null ? Integer.valueOf(contract.getContractBaseInfo().getSupplierScale()) : null);
                    contractPayeeInfoVO.setSupplierName(contract.getContractBaseInfo().getSupplierName() != null ? contract.getContractBaseInfo().getSupplierName() : null);

//                    contractPayeeInfoVO.setSupplierLocationName(contract.getContractBaseInfo().getSupplierArea() != null ? contract.getContractBaseInfo().getSupplierArea() : null);
                    projectInfo.setProjectCode(contract.getContractBaseInfo().getProjectNo() != null ? contract.getContractBaseInfo().getProjectNo() : null);
                    projectInfo.setProjectName(contract.getContractBaseInfo().getProjectName() != null ? contract.getContractBaseInfo().getProjectName() : null);
                }

                acceptRequirement = contract.getAcceptRequirement() != null ? contract.getAcceptRequirement() : null;
                if (ObjectUtil.isNotEmpty(contract.getSuppliers())) {
                    Suppliers suppliers = contract.getSuppliers().get(0);
                    contractPayeeInfoVO.setPayeeAccountName(suppliers.getSupplierAccountName() != null ? suppliers.getSupplierAccountName() : null);
                    contractPayeeInfoVO.setBankName(suppliers.getSupplierOpeningBank() != null ? suppliers.getSupplierOpeningBank() : null);
                    contractPayeeInfoVO.setBankAccount(suppliers.getSupplierAccountNo() != null ? suppliers.getSupplierAccountNo() : null);
                }
                if (ObjectUtil.isNotEmpty(contract.getPurchaseDetail())) {
                    goods = contract.getPurchaseDetail().stream().map(detail -> {
                        //获取商品信息
                        EcmsGcyBuyPlanBill ecmsGcyBuyPlanBill = buyPlanBillMapper.selectOne(EcmsGcyBuyPlanBill::getGoodsName, detail.getProductName());
                        SuperviseGoodsVO goodsVO = new SuperviseGoodsVO();
                        if (ObjectUtil.isNotEmpty(ecmsGcyBuyPlanBill)) {
                            goodsVO.setPurCatalogCode(ecmsGcyBuyPlanBill.getPurCatalogCode());
                            goodsVO.setPurCatalogName(ecmsGcyBuyPlanBill.getPurCatalogName());
                            goodsVO.setPlanBuyMoney(ecmsGcyBuyPlanBill.getTotalPrice());
                            goodsVO.setPlanBuyNum(ecmsGcyBuyPlanBill.getNum());
                            goodsVO.setPlanBuyPrice(ecmsGcyBuyPlanBill.getPrice());
                            goodsVO.setPlanBuyUnit(ecmsGcyBuyPlanBill.getUnit());
                            goodsVO.setBillGuid(ecmsGcyBuyPlanBill.getBillGuid());
                        }
                        goodsVO.setGoodsName(detail.getProductName() != null ? detail.getProductName() : null);
                        goodsVO.setActualBuyMoney(detail.getTotalPrice() != null ? detail.getTotalPrice().doubleValue() : null);
                        goodsVO.setActualBuyNum(detail.getQuantity() != null ? detail.getQuantity() : null);
                        goodsVO.setActualBuyUnit(detail.getSaleUnit() != null ? detail.getSaleUnit() : null);
                        goodsVO.setActualBuyPrice(detail.getPrice() != null ? detail.getPrice().doubleValue() : null);
                        goodsVO.setSpec(detail.getServiceScope() != null ? detail.getServiceScope() : null);
                        return goodsVO;
                    }).collect(Collectors.toList());
                }
                if (ObjectUtil.isNotEmpty(contract.getPaymentPlan())) {
                    String finalSupplyId = supplyId;
                    paymentPlan = contract.getPaymentPlan().stream().map(plan -> {
                        PaymentPlanVO pay = new PaymentPlanVO();
                        if (ObjectUtil.isNotEmpty(plan.getPaymentPeriod())) {
                            pay.setPeriods(Integer.valueOf(plan.getPaymentPeriod()));
                        } else {
                            pay.setPeriods(null);
                        }
                        if (ObjectUtil.isNotEmpty(plan.getPlanPaymentDateDesc())) {
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                            try {
                                pay.setTime(formatter.parse(plan.getPlanPaymentDateDesc()));
                            } catch (ParseException e) {
                                pay.setTime(null);
                            }
                        } else {
                            pay.setTime(null);
                        }
                        pay.setPayeeId(finalSupplyId);
                        pay.setMoney(plan.getPaymentAmount() != null ? plan.getPaymentAmount().doubleValue() : null);
                        pay.setPayProportion(plan.getPaymentPercent() != null ? plan.getPaymentPercent().doubleValue() : null);
                        pay.setPayee(plan.getPayee() != null ? plan.getPayee() : null);
                        pay.setPayTerm(plan.getPaymentCondition() != null ? plan.getPaymentCondition() : null);
                        return pay;
                    }).collect(Collectors.toList());
                }
            }
        }
        ContractExtractRespVO contractExtractRespVO = new ContractExtractRespVO();
        contractExtractRespVO.setBaseInfo(baseInfo);
        contractExtractRespVO.setSignatoryInfoList(signatoryList);
        contractExtractRespVO.setGoodsList(goods);
        contractExtractRespVO.setPaymentPlanList(paymentPlan);
        contractExtractRespVO.setProjectInfo(projectInfo);
        contractExtractRespVO.setAcceptRequirement(acceptRequirement);
        contractExtractRespVO.setContractPayeeInfoVO(contractPayeeInfoVO);
        return contractExtractRespVO;
    }
}
