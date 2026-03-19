package com.yaoan.module.econtract.service.code.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.module.econtract.controller.admin.code.vo.CodeRuleCreateReqVO;
import com.yaoan.module.econtract.controller.admin.code.vo.CodeRuleInfoReqVO;
import com.yaoan.module.econtract.controller.admin.code.vo.ListCodeRuleReqVO;
import com.yaoan.module.econtract.controller.admin.code.vo.ListCodeRuleRespVO;
import com.yaoan.module.econtract.controller.admin.codegeneration.vo.CodeQueryReqVO;
import com.yaoan.module.econtract.convert.code.CodeRuleConvert;
import com.yaoan.module.econtract.dal.dataobject.code.CodeRuleDO;
import com.yaoan.module.econtract.dal.dataobject.code.CodeRuleInfoDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contractInvoiceManage.ContractInvoiceManageDO;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.ContractTemplate;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.model.SimpleModel;
import com.yaoan.module.econtract.dal.dataobject.paymentapplication.PaymentApplicationDO;
import com.yaoan.module.econtract.dal.mysql.code.CodeRuleInfoMapper;
import com.yaoan.module.econtract.dal.mysql.code.CodeRuleMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contractinvoicemanage.ContractInvoiceManageMapper;
import com.yaoan.module.econtract.dal.mysql.contracttemplate.ContractTemplateMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.model.SimpleModelMapper;
import com.yaoan.module.econtract.dal.mysql.payment.paymentapplication.PaymentApplicationMapper;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.service.code.CodeRuleService;
import com.yaoan.module.system.api.dept.dto.UserCompanyDeptRespDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;

@Service
public class CodeRuleServiceImpl implements CodeRuleService {
    private static final String CONTRACT = "contract";
    private static final String MODEL = "model";
    private static final String TEMPLATE = "template";
    private static final String INVOICE = "invoice";
    private static final String PAYMENT = "payment";

    @Resource
    private CodeRuleMapper codeRuleMapper;

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private CodeRuleInfoMapper codeRuleInfoMapper;

    @Resource
    private ContractTypeMapper contractTypeNewMapper;

    @Resource
    private AdminUserApi userApi;
    @Resource
    private ContractInvoiceManageMapper contractInvoiceManageMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private SimpleModelMapper simpleModelMapper;
    @Resource
    private ContractTemplateMapper contractTemplateMapper;
    @Resource
    private PaymentApplicationMapper paymentApplicationMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createOrUpdate(CodeRuleCreateReqVO codeRuleCreateReqVO) {
        // 名字重复校验
        if (isNameDuplicate(codeRuleCreateReqVO.getName(), codeRuleCreateReqVO.getId())) {
            throw exception(ErrorCodeConstants.CODE_NAME_EXISTS_ERROR);
        }

        if (ObjectUtil.isEmpty(codeRuleCreateReqVO.getId())) {
            // 新增
            CodeRuleDO codeRuleDO = CodeRuleConvert.INSTANCE.convertDO(codeRuleCreateReqVO);
            codeRuleMapper.insert(codeRuleDO);
            List<CodeRuleInfoDO> codeRuleInfoDOS = CodeRuleConvert.INSTANCE.convertDOList(codeRuleCreateReqVO.getCodeRuleInfoReqVOList());
            if (CollectionUtil.isNotEmpty(codeRuleInfoDOS)){
                codeRuleInfoDOS.forEach(i -> i.setCodeRuleId(codeRuleDO.getId()));
                codeRuleInfoMapper.insertBatch(codeRuleInfoDOS);
            }
            return codeRuleDO.getId();
        } else {
            // 编辑
            CodeRuleDO codeRuleDO = CodeRuleConvert.INSTANCE.convertDO(codeRuleCreateReqVO);
            codeRuleMapper.updateById(codeRuleDO);
            // 批量删除原有记录
            LambdaQueryWrapperX<CodeRuleInfoDO> wrapperX = new LambdaQueryWrapperX<>();
            wrapperX.eq(CodeRuleInfoDO::getCodeRuleId, codeRuleDO.getId());
            List<CodeRuleInfoDO> existingCodeRuleInfoDOS = codeRuleInfoMapper.selectList(wrapperX);
            if (CollectionUtil.isNotEmpty(existingCodeRuleInfoDOS)) {
                codeRuleInfoMapper.deleteBatchIds(existingCodeRuleInfoDOS.stream().map(CodeRuleInfoDO::getId).collect(Collectors.toList()));
            }
            List<CodeRuleInfoDO> codeRuleInfoDOS = CodeRuleConvert.INSTANCE.convertDOList(codeRuleCreateReqVO.getCodeRuleInfoReqVOList());
            codeRuleInfoDOS.forEach(i -> i.setCodeRuleId(codeRuleDO.getId()));
            codeRuleInfoMapper.insertBatch(codeRuleInfoDOS);
            return codeRuleDO.getId();
        }
    }

    private boolean isNameDuplicate(String name, String id) {
        // 检查名字是否重复
        CodeRuleDO codeRuleDO = codeRuleMapper.selectOne(new LambdaQueryWrapperX<CodeRuleDO>().eq(CodeRuleDO::getName, name));
        if (codeRuleDO != null) {
            // 如果存在同名记录，且不是当前记录本身，则认为名字重复
            return !codeRuleDO.getId().equals(id);
        }
        return false;
    }

    @Override
    public PageResult<ListCodeRuleRespVO> list(ListCodeRuleReqVO reqVO) {
        LambdaQueryWrapper<CodeRuleDO> objectLambdaQueryWrapper = new LambdaQueryWrapperX<>();
        if (ObjectUtil.isNotEmpty(reqVO.getName())){
            objectLambdaQueryWrapper.like(CodeRuleDO::getName, reqVO.getName());
        }
        if (ObjectUtil.isNotEmpty(reqVO.getStatus())){
            objectLambdaQueryWrapper.eq(CodeRuleDO::getStatus, reqVO.getStatus());
        }
        if (ObjectUtil.isNotEmpty( reqVO.getIsReserve())){
            objectLambdaQueryWrapper.eq(CodeRuleDO::getIsReserve, reqVO.getIsReserve());
        }
        objectLambdaQueryWrapper.orderByDesc(CodeRuleDO::getCreateTime);
        PageResult<CodeRuleDO> codeRuleDOPageResult = codeRuleMapper.selectPage(reqVO, objectLambdaQueryWrapper);
        PageResult<ListCodeRuleRespVO> listCodeRuleRespVOPageResult = CodeRuleConvert.INSTANCE.convertPage(codeRuleDOPageResult);
        List<String> idS = listCodeRuleRespVOPageResult.getList().stream().map(ListCodeRuleRespVO::getId).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(idS)){
            return PageResult.empty();
        }
        List<ContractType> contractTypeNewDOS = contractTypeNewMapper.selectList(new LambdaQueryWrapperX<ContractType>().in(ContractType::getCodeRuleId, idS));
        fillData(contractTypeNewDOS, listCodeRuleRespVOPageResult);
        return listCodeRuleRespVOPageResult;
    }

    private void fillData(List<ContractType> contractTypeNewDOS, PageResult<ListCodeRuleRespVO> listCodeRuleRespVOPageResult) {
        // 提取 Map
        Map<String, List<String>> codeRuleIdToNamesMap = contractTypeNewDOS.stream()
                .collect(Collectors.groupingBy(
                        ContractType::getCodeRuleId,
                        Collectors.mapping(ContractType::getName, Collectors.toList())
                ));
        // 获取用户信息
        List<Long> creatorIds = listCodeRuleRespVOPageResult.getList().stream().map(ListCodeRuleRespVO::getCreator).collect(Collectors.toList());
        AtomicReference<Map<Long, String>> creatorIdToNameMap = new AtomicReference<>(new HashMap<>());
        DataPermissionUtils.executeIgnore(()->{
            TenantUtils.executeIgnore(()->{
                List<AdminUserRespDTO> userInfo = adminUserApi.getUserList(creatorIds);
                creatorIdToNameMap.set(CollectionUtils.convertMap(userInfo, AdminUserRespDTO::getId, AdminUserRespDTO::getNickname));
            });
        });
        for (ListCodeRuleRespVO listCodeRuleRespVO : listCodeRuleRespVOPageResult.getList()) {
            String codeRuleId = listCodeRuleRespVO.getId();
            List<String> names = codeRuleIdToNamesMap.get(codeRuleId);
            if (names != null) {
                listCodeRuleRespVO.setContractTypeListName(names);
                listCodeRuleRespVO.setContractTypeName(String.join(",", names));
            }
            Long creatorId = listCodeRuleRespVO.getCreator();
            String creatorName = creatorIdToNameMap.get().get(creatorId);
            if (creatorName != null) {
                listCodeRuleRespVO.setCreatorName(creatorName);
            }

            //状态赋值中文名称
            listCodeRuleRespVO.setStatusName(listCodeRuleRespVO.getStatus() == 1 ? "启用" : "停用");

            LocalDateTime localDateTime = listCodeRuleRespVO.getCreateTime();
            Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
            String formattedDateTime = DateUtil.formatDateTime(date);
            listCodeRuleRespVO.setCreateTimeStr(formattedDateTime);
        }
    }

    @Override
    public ListCodeRuleRespVO getById(String id) {
        CodeRuleDO codeRuleDO = codeRuleMapper.selectById(id);
        if (codeRuleDO != null) {
            ListCodeRuleRespVO listCodeRuleRespVO = CodeRuleConvert.INSTANCE.convert(codeRuleDO);
            List<CodeRuleInfoDO> codeRuleInfoDOS = codeRuleInfoMapper.selectList(new LambdaQueryWrapperX<CodeRuleInfoDO>().eq(CodeRuleInfoDO::getCodeRuleId, id).orderByAsc(CodeRuleInfoDO::getSort));
            listCodeRuleRespVO.setCodeRuleInfoReqVOList(CodeRuleConvert.INSTANCE.convertList(codeRuleInfoDOS));
            //合同类型名称
            List<ContractType> contractTypeNewDOS = contractTypeNewMapper.selectList(new LambdaQueryWrapperX<ContractType>().eq(ContractType::getCodeRuleId, listCodeRuleRespVO.getId()));
            if (CollectionUtil.isNotEmpty(contractTypeNewDOS)){
                listCodeRuleRespVO.setContractTypeListName(contractTypeNewDOS.stream().map(ContractType::getName).collect(Collectors.toList()));
            }
            AdminUserRespDTO user = adminUserApi.getUser(listCodeRuleRespVO.getCreator());
            if (ObjectUtil.isNotNull(user)){
                listCodeRuleRespVO.setCreatorName(user.getNickname());
            }
            return listCodeRuleRespVO;
       }
        return null;
    }

    /**
     * 根据编号规则生成编号
     */
    @Override
    @Transactional
    public String generate( CodeQueryReqVO vo) {
        ListCodeRuleRespVO byId = this.getById(vo.getCodeRuleId());
        if (byId == null || byId.getCodeRuleInfoReqVOList() == null || byId.getCodeRuleInfoReqVOList().isEmpty()) {
            return null;
        }
        String typePrefix = null;
        if(ObjectUtil.isNotEmpty(vo.getContractType())){
            ContractType contractType = contractTypeNewMapper.selectById(vo.getContractType());
            typePrefix = contractType.getTypePrefix();
        }
        StringBuilder codeBuilder = new StringBuilder();
        List<CodeRuleInfoReqVO> codeRuleInfoList = byId.getCodeRuleInfoReqVOList();
        //根据用户id获取用户对应的部门和公司信息
        UserCompanyDeptRespDTO userCompanyDeptRespDTO = userApi.selectUserCompanyDept(SecurityFrameworkUtils.getLoginUserId());
        //公司名称
        String companyName = userCompanyDeptRespDTO.getCompanyInfo() != null ? userCompanyDeptRespDTO.getCompanyInfo().getName() : "COMPANY";
        //部门名称
        String departmentName = userCompanyDeptRespDTO.getDeptInfo() != null ? userCompanyDeptRespDTO.getDeptInfo().getName() : "DEPT";
        for (CodeRuleInfoReqVO rule : codeRuleInfoList) {
            String part = generatePart(rule, companyName, departmentName, vo, typePrefix);
            if (part != null && !part.isEmpty()) {
                if (codeBuilder.length() > 0) {
                    codeBuilder.append(rule.getConnector());
                }
                codeBuilder.append(part);
            }
        }

        return codeBuilder.toString();
    }

    @Override
    public String generate4ContractType(CodeQueryReqVO vo) {
        ContractType contractType = contractTypeNewMapper.selectById(vo.getContractType());
        if (contractType == null) {
            return "DEFAULT"+ UUID.randomUUID().toString().toUpperCase().substring(0,6);
        }

        return generate(vo.setCodeRuleId(contractType.getCodeRuleId()));
    }

    /**
     * 根据规则生成编号的一部分
     * @param rule
     * @return
     */
    private String generatePart(CodeRuleInfoReqVO rule, String companyName, String departmentName, CodeQueryReqVO vo, String typePrefix) {
        switch (rule.getPrefix()) {
            case "公司前缀":
                return getFirstLetterUpperCase(companyName);
            case "部门前缀":
                return getFirstLetterUpperCase(departmentName);
            case "合同类型前缀":
                return typePrefix;
            case "年份":
                return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy"));
            case "月份":
                return LocalDate.now().format(DateTimeFormatter.ofPattern("MM"));
            case "天":
                return LocalDate.now().format(DateTimeFormatter.ofPattern("dd"));
            case "四位流水号":
                return generateSerialNumber(rule, 4, vo.getType());
            case "六位流水号":
                return generateSerialNumber(rule, 6, vo.getType());
            case "固定文本":
                return rule.getFixedText();
            default:
                return null;
        }
    }

    private String generateSerialNumber(CodeRuleInfoReqVO rule, int length,String type) {
        LocalDate currentDate = LocalDate.now();
        if (rule.getLastResetDate() == null || !rule.getLastResetDate().isEqual(currentDate)) {
            rule.resetSerialNumber();
        }
        Long count = 0L;
        Long l = getCountInfo(count, type, LocalDateTime.now(), LocalDateTime.now().with(LocalTime.MIN));
        return getSort(l,length);
    }
    private Long getCountInfo(Long count, String type, LocalDateTime date, LocalDateTime startTime) {
        switch (type) {
            case CONTRACT:
                count = contractMapper.selectCount(new LambdaQueryWrapperX<ContractDO>().betweenIfPresent(ContractDO::getCreateTime, startTime, date));
                break;
            case MODEL:
                count = simpleModelMapper.selectAllCountWithTime(startTime, date);
//                count = simpleModelMapper.selectCount(new LambdaQueryWrapperX<SimpleModel>().betweenIfPresent(SimpleModel::getCreateTime, startTime, date));
                break;
            case TEMPLATE:
                count = contractTemplateMapper.selectCount(new LambdaQueryWrapperX<ContractTemplate>().betweenIfPresent(ContractTemplate::getCreateTime, startTime, date));
                break;
            case INVOICE:
                count = contractInvoiceManageMapper.selectCount(new LambdaQueryWrapperX<ContractInvoiceManageDO>().betweenIfPresent(ContractInvoiceManageDO::getCreateTime, startTime, date));
                break;
            case PAYMENT:
                count = paymentApplicationMapper.selectCount(new LambdaQueryWrapperX<PaymentApplicationDO>().betweenIfPresent(PaymentApplicationDO::getCreateTime, startTime, date));
                break;
            default:
                count = contractMapper.selectCount(new LambdaQueryWrapperX<ContractDO>().betweenIfPresent(ContractDO::getCreateTime, startTime, date));
        }
        return count;
    }
    String getSort(Long count, int length) {
        count = count + 1;
        if (length == 4) {
            DecimalFormat formatter = new DecimalFormat("0000");

            // 格式化整数
            String formatted = formatter.format(count);
            return formatted;
        }
        if (length == 6) {
            DecimalFormat formatter = new DecimalFormat("000000");

            // 格式化整数
            String formatted = formatter.format(count);
            return formatted;
        }
        return "";
    }
    private String getFirstLetterUpperCase(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        for (char c : text.toCharArray()) {
            if (Character.toString(c).matches("[\\u4E00-\\u9FA5]")) { // 判断是否为汉字
                try {
                    String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(c, format);
                    if (pinyinArray != null) {
                        result.append(pinyinArray[0].charAt(0)); // 获取拼音的首字母
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                result.append(Character.toUpperCase(c)); // 非汉字直接转换为大写
            }
        }
        return result.toString();
    }

    @Override
    public void delete(String id) {
        codeRuleMapper.deleteById(id);
        codeRuleInfoMapper.delete(new LambdaQueryWrapperX<CodeRuleInfoDO>().eq(CodeRuleInfoDO::getCodeRuleId, id));
    }

    @Override
    public void updateStatus(String id) {
        CodeRuleDO codeRuleDO = codeRuleMapper.selectById(id);
        Assert.notNull(codeRuleDO, "编号规则不存在");
        codeRuleDO.setStatus(codeRuleDO.getStatus() == 0 ? 1 : 0);
        codeRuleMapper.updateById(codeRuleDO);
    }

    @Override
    public PageResult<ListCodeRuleRespVO> export(ListCodeRuleReqVO reqVO) {
        if(ObjectUtil.isNotEmpty(reqVO.getIds())){
            PageResult<ListCodeRuleRespVO> result = new PageResult<>();
            List<CodeRuleDO> contractTypes = codeRuleMapper.selectList(CodeRuleDO::getId, reqVO.getIds());
            List<ListCodeRuleRespVO> listCodeRuleRespVOS = CodeRuleConvert.INSTANCE.convertListV2(contractTypes);
            List<String> idS = listCodeRuleRespVOS.stream().map(ListCodeRuleRespVO::getId).collect(Collectors.toList());
            if (CollectionUtil.isEmpty(idS)){
                return PageResult.empty();
            }
            List<ContractType> contractTypeNewDOS = contractTypeNewMapper.selectList(new LambdaQueryWrapperX<ContractType>().in(ContractType::getCodeRuleId, idS));
            result.setList(listCodeRuleRespVOS);
            fillData(contractTypeNewDOS, result);
            return result;
        }
        return this.list(reqVO);
    }
}
