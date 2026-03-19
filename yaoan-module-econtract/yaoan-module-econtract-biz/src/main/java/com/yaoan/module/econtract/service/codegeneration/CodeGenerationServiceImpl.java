package com.yaoan.module.econtract.service.codegeneration;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.module.econtract.controller.admin.codegeneration.vo.*;
import com.yaoan.module.econtract.convert.codegeneration.CodeGenerationConvert;
import com.yaoan.module.econtract.dal.dataobject.codegeneration.CodeGenerationDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contractInvoiceManage.ContractInvoiceManageDO;
import com.yaoan.module.econtract.dal.dataobject.contracttemplate.ContractTemplate;
import com.yaoan.module.econtract.dal.dataobject.contracttype.ContractType;
import com.yaoan.module.econtract.dal.dataobject.model.SimpleModel;
import com.yaoan.module.econtract.dal.dataobject.term.Term;
import com.yaoan.module.econtract.dal.mysql.codegeneration.CodeGenerationMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contractinvoicemanage.ContractInvoiceManageMapper;
import com.yaoan.module.econtract.dal.mysql.contracttemplate.ContractTemplateMapper;
import com.yaoan.module.econtract.dal.mysql.contracttype.ContractTypeMapper;
import com.yaoan.module.econtract.dal.mysql.model.SimpleModelMapper;
import com.yaoan.module.econtract.dal.mysql.term.TermMapper;
import com.yaoan.module.econtract.enums.codeGeneration.CodeGenRuleEnums;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;
import static com.yaoan.module.econtract.util.EcontractUtil.getDefaultCodeAutoByTimestamp;
import static com.yaoan.module.system.enums.config.SystemConfigKeyEnums.*;

/**
 * @description:
 * @author: Pele
 * @date: 2024/9/5 10:36
 */
@Service
public class CodeGenerationServiceImpl implements CodeGenerationService {
    private static final String YEAR = CodeGenRuleEnums.YEAR.getCode();
    private static final String MONTH = CodeGenRuleEnums.MONTH.getCode();
    private static final String DAY = CodeGenRuleEnums.DAY.getCode();
    private static final String DIY = CodeGenRuleEnums.DIY.getCode();
    private static final String SORT = CodeGenRuleEnums.SORT.getCode();
    private static final String CONTRACT = "contract";
    private static final String MODEL = "model";
    private static final String TERM = "term";
    private static final String TEMPLATE = "template";
    private static final String INVOICE = "invoice";
    private static final String DIVISION = ",";
    @Resource
    private CodeGenerationMapper codeGenerationMapper;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ContractTypeMapper contractTypeMapper;
    @Resource
    private SimpleModelMapper simpleModelMapper;
    @Resource
    private ContractTemplateMapper contractTemplateMapper;
    @Resource
    private TermMapper termMapper;
    @Resource
    private ContractInvoiceManageMapper contractInvoiceManageMapper;
    @Resource
    private SystemConfigApi systemConfigApi;

    @Override
    public PageResult<CodeGenerationRespVO> list(CodeGenerationReqVO vo) {
        PageResult<CodeGenerationDO> pageResult = codeGenerationMapper.selectPage(vo, new LambdaQueryWrapperX<CodeGenerationDO>()
                .eqIfPresent(CodeGenerationDO::getBusinessType, vo.getBusinessType())
                .eqIfPresent(CodeGenerationDO::getChildrenType, vo.getChildrenType())
                .likeIfPresent(CodeGenerationDO::getBusinessName, vo.getBusinessName())
                .eqIfPresent(CodeGenerationDO::getStatus, vo.getStatus())
                .orderByDesc(CodeGenerationDO::getCreateTime));
        PageResult<CodeGenerationRespVO> respVOPageResult = CodeGenerationConvert.INSTANCE.pageD2R(pageResult);
        if (CollectionUtil.isNotEmpty(respVOPageResult.getList())) {
            for (CodeGenerationRespVO respVO : respVOPageResult.getList()) {
                AdminUserRespDTO userRespDTO = adminUserApi.getUser(Long.valueOf(respVO.getCreator()));
                if (ObjectUtil.isNotNull(userRespDTO)) {
                    respVO.setCreatorName(userRespDTO.getNickname());
                }
                String rule = getTranslateRule(respVO.getGenerateRule());
                respVO.setGenerateRuleStr(rule);
            }
        }

        return respVOPageResult;
    }

    @Override
    public String save(CodeGenerationSaveReqVO vo) {
        CodeGenerationDO codeGenerationDO = CodeGenerationConvert.INSTANCE.r2D(vo);
        codeGenerationMapper.insert(codeGenerationDO);
        return String.valueOf(codeGenerationDO.getId());
    }

    @Override
    public String generateCodeByVO(CodeQueryReqVO vo) {
        CodeGenerationDO generationDO = new CodeGenerationDO();
        String result = "";
        Long ruleId = 0L;
        if (CONTRACT.equals(vo.getType())) {
            ContractType contractType = contractTypeMapper.selectById(vo.getId());
            if (ObjectUtil.isNull(contractType)) {
                throw exception(SYSTEM_ERROR, "合同类型为空");
            }
//            ruleId = contractType.getCodeRuleId();
        }
        if (MODEL.equals(vo.getType())) {
            String value = systemConfigApi.getConfigByKey(CODE_RULE_MODEL.getKey());
            if (StringUtils.isBlank(value)) {
                throw exception(SYSTEM_ERROR, "模板通用编号生成规则为空");
            }
            ruleId = Long.valueOf(value);
        }
        if (TERM.equals(vo.getType())) {
            String value = systemConfigApi.getConfigByKey(CODE_RULE_TERM.getKey());
            if (StringUtils.isBlank(value)) {
                throw exception(SYSTEM_ERROR, "条款通用编号生成规则为空");
            }
            ruleId = Long.valueOf(value);
        }
        if (TEMPLATE.equals(vo.getType())) {
            String value = systemConfigApi.getConfigByKey(CODE_RULE_TEMPLATE.getKey());
            if (StringUtils.isBlank(value)) {
                throw exception(SYSTEM_ERROR, "范本通用编号生成规则为空");
            }
            ruleId = Long.valueOf(value);
        }
        if (INVOICE.equals(vo.getType())) {
            String value = systemConfigApi.getConfigByKey(CODE_RULE_INVOICE.getKey());
            if (StringUtils.isBlank(value)) {
                throw exception(SYSTEM_ERROR, "收款通用编号生成规则为空");
            }
            ruleId = Long.valueOf(value);
        }
        generationDO = codeGenerationMapper.selectById(ruleId);
        if (ObjectUtil.isNull(generationDO)) {
            // 托底编号
            return getDefaultCodeAutoByTimestamp();
        }
        String generateRule = generationDO.getGenerateRule();
        String[] parts = generateRule.split(DIVISION);
        int t = 1;
        for (String part : parts) {

            String temp = getInfoFromRule(part, generateRule, vo.getType(), generationDO.getLength());
            result = result + temp;
            if (t < parts.length && !parts[t].equals(DAY) && !parts[t].equals(MONTH) && !parts[t].equals(YEAR)) {
                result = result + "-";
            }
            t++;
        }
        //加前缀
        if (StringUtils.isNotBlank(generationDO.getChildrenTypeTag())) {
            result = generationDO.getChildrenTypeTag() + "-" + result;
        }
        if (StringUtils.isNotBlank(generationDO.getPrefix())) {
            result = generationDO.getPrefix() + "-" + result;
        }
        return result;
    }


    String getInfoFromRule(String rule, String generateRule, String type, Integer length) {
        LocalDateTime date = LocalDateTime.now();
        String formatted = "";
        if (StringUtils.isBlank(rule)) {
            return "";
        }
        String info = "";
        if (YEAR.equals(rule)) {
            return String.valueOf(date.getYear());
        }
        if (MONTH.equals(rule)) {
            DecimalFormat formatter = new DecimalFormat("00");

            // 格式化整数
            formatted = formatter.format(date.getMonthValue());
            return formatted;
        }
        if (DAY.equals(rule)) {
            DecimalFormat formatter = new DecimalFormat("00");
            formatted = formatter.format(date.getDayOfMonth());
            return formatted;
        }
        if (rule.contains(DIY)) {
            info = rule.replace("DIY_", "");
            return info;
        }
        if (rule.equals(SORT)) {
            Long count = 0L;
            if (generateRule.contains(DAY)) {
                LocalDateTime startTime = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), 0, 0, 0);
                count = getCountInfo(count, type, date, startTime);
                return getSort(count, length);
            }
            if (generateRule.contains(MONTH)) {
                LocalDateTime startTime = LocalDateTime.of(date.getYear(), date.getMonthValue(), 1, 0, 0, 0);
                count = getCountInfo(count, type, date, startTime);
                return getSort(count, length);
            }
            if (generateRule.contains(YEAR)) {
                LocalDateTime startTime = LocalDateTime.of(date.getYear(), 1, 1, 0, 0, 0);
                count = getCountInfo(count, type, date, startTime);
                return getSort(count, length);
            }
            return String.valueOf(count);
        }
        return "";
    }

    private Long getCountInfo(Long count, String type, LocalDateTime date, LocalDateTime startTime) {
        
        switch (type) {
            case CONTRACT:
                count = contractMapper.selectCount(new LambdaQueryWrapperX<ContractDO>().betweenIfPresent(ContractDO::getCreateTime, startTime, date));
                break;
            case MODEL:
                count = simpleModelMapper.selectCount(new LambdaQueryWrapperX<SimpleModel>().betweenIfPresent(SimpleModel::getCreateTime, startTime, date));
                break;
            case TEMPLATE:
                count = contractTemplateMapper.selectCount(new LambdaQueryWrapperX<ContractTemplate>().betweenIfPresent(ContractTemplate::getCreateTime, startTime, date));
                break;
            case INVOICE:
                count = contractInvoiceManageMapper.selectCount(new LambdaQueryWrapperX<ContractInvoiceManageDO>().betweenIfPresent(ContractInvoiceManageDO::getCreateTime, startTime, date));
                break;
            case TERM:
                count = termMapper.selectCount(new LambdaQueryWrapperX<Term>().betweenIfPresent(Term::getCreateTime, startTime, date));
                break;
            default:
                count = contractMapper.selectCount(new LambdaQueryWrapperX<ContractDO>().betweenIfPresent(ContractDO::getCreateTime, startTime, date));
        }
        return count;
//        count = contractMapper.selectCount(new LambdaQueryWrapperX<ContractDO>().betweenIfPresent(ContractDO::getCreateTime, startTime, date));
//        if (CONTRACT.equals(type)) {
//            count = contractMapper.selectCount(new LambdaQueryWrapperX<ContractDO>().betweenIfPresent(ContractDO::getCreateTime, startTime, date));
//        }
//        if (MODEL.equals(type)) {
//            count = simpleModelMapper.selectCount(new LambdaQueryWrapperX<SimpleModel>().betweenIfPresent(SimpleModel::getCreateTime, startTime, date));
//        }
//        if (TEMPLATE.equals(type)) {
//            count = contractTemplateMapper.selectCount(new LambdaQueryWrapperX<ContractTemplate>().betweenIfPresent(ContractTemplate::getCreateTime, startTime, date));
//        }
//        return count;
    }


    String getSort(Long count, Integer length) {
        count = count + 1;
        if (4 == length) {
            DecimalFormat formatter = new DecimalFormat("0000");

            // 格式化整数
            String formatted = formatter.format(count);
            return formatted;
        }
        if (6 == length) {
            DecimalFormat formatter = new DecimalFormat("000000");

            // 格式化整数
            String formatted = formatter.format(count);
            return formatted;
        }
        return "";
    }


    @Override
    public String update(CodeGenerationUpdateVO vo) {
        CodeGenerationDO codeGenerationDO = CodeGenerationConvert.INSTANCE.upR2D(vo);
        codeGenerationMapper.updateById(codeGenerationDO);
        return String.valueOf(codeGenerationDO.getId());
    }

    @Override
    public String deleteBatch(List<String> idList) {
        List<Long> longList = idList.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        codeGenerationMapper.deleteBatchIds(longList);
        return "success";
    }

    @Override
    public CodeGenDetailRespVO queryCodeRuleById(String id) {
        CodeGenerationDO generationDO = codeGenerationMapper.selectById(Long.valueOf(id));
        if (ObjectUtil.isNull(generationDO)) {
            throw exception(SYSTEM_ERROR, "该编号规则不存在.");
        }
        CodeGenDetailRespVO respVO = CodeGenerationConvert.INSTANCE.detailD2R(generationDO);

        AdminUserRespDTO user=  adminUserApi.getUser(Long.valueOf(respVO.getCreator()));
        if(ObjectUtil.isNotNull(user)){
            respVO.setCreatorName(user.getNickname());
        }
        return respVO;
    }

    private String getTranslateRule(String generateRule) {
        if (StringUtils.isBlank(generateRule)) {
            return " ";
        }
        String result = "";
        String[] parts = generateRule.split(DIVISION);
        for (String part : parts) {
            String temp = getChinese4CodeRule(part);
            result = result + temp;
        }
        return result;

    }

    private String getChinese4CodeRule(String part) {
        CodeGenRuleEnums enums = CodeGenRuleEnums.getInstance(part);
        if (ObjectUtil.isNotNull(enums)) {
            return enums.getInfo();
        }
        return "";

    }


}



