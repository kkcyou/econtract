package com.yaoan.module.econtract.service.warningcfg;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.yaoan.framework.common.enums.UserTypeEnum;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.datapermission.core.util.DataPermissionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.tenant.core.util.TenantUtils;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.ContractProcessInstanceRelationInfoRespDTO;
import com.yaoan.module.bpm.api.task.dto.TaskForWarningReqDTO;
import com.yaoan.module.econtract.api.contract.ContractApi;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.WarningCfgCreateReqVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.WarningCfgExportReqVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.WarningCfgPageReqVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.WarningCfgUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.*;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.create.WarningItem4CfgCreateReqVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.create.WarningItemRule4CfgCreateReqVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.create.WarningNotice4CfgCreateReqVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.get.WarningCfgBase4CfgRespVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.get.WarningItem4CfgRespVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.get.WarningItemRule4CfgRespVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.get.WarningNotice4CfgRespVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.page.WarningItemPageRespVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.page.WarningPageRespVO;
import com.yaoan.module.econtract.controller.admin.warningcfg.vo.update.WarningCfgTurnReqVO;
import com.yaoan.module.econtract.convert.warningcfg.WarningCfgConvert;
import com.yaoan.module.econtract.convert.warningitem.WarningItemConvert;
import com.yaoan.module.econtract.convert.warningitemrule.WarningItemRuleConvert;
import com.yaoan.module.econtract.convert.warningnoticecfg.WarningNoticeCfgConvert;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.PaymentScheduleDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import com.yaoan.module.econtract.dal.dataobject.contractarchives.ContractArchivesDO;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.dataobject.relativeContact.RelativeContact;
import com.yaoan.module.econtract.dal.dataobject.warningcfg.WarningCfgDO;
import com.yaoan.module.econtract.dal.dataobject.warningitem.WarningItemDO;
import com.yaoan.module.econtract.dal.dataobject.warningitemrule.WarningItemRuleDO;
import com.yaoan.module.econtract.dal.dataobject.warningmodel.WarningModelDO;
import com.yaoan.module.econtract.dal.dataobject.warningmonitor.WarningMonitorDO;
import com.yaoan.module.econtract.dal.dataobject.warningnoticecfg.WarningNoticeCfgDO;
import com.yaoan.module.econtract.dal.dataobject.warningparam.WarningParamDO;
import com.yaoan.module.econtract.dal.dataobject.warningrulemonitor.WarningRuleMonitorRelDO;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contractarchives.ContractArchivesMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.relativeContact.RelativeContactMapper;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import com.yaoan.module.econtract.dal.mysql.warningcfg.WarningCfgMapper;
import com.yaoan.module.econtract.dal.mysql.warningitem.WarningItemMapper;
import com.yaoan.module.econtract.dal.mysql.warningitemrule.WarningItemRuleMapper;
import com.yaoan.module.econtract.dal.mysql.warningmodel.WarningModelMapper;
import com.yaoan.module.econtract.dal.mysql.warningmonitor.WarningMonitorMapper;
import com.yaoan.module.econtract.dal.mysql.warningnoticecfg.WarningNoticeCfgMapper;
import com.yaoan.module.econtract.dal.mysql.warningparam.WarningParamMapper;
import com.yaoan.module.econtract.dal.mysql.warningrulemonitorrel.WarningRuleMonitorRelMapper;
import com.yaoan.module.econtract.enums.WarningRulesNotifyTemplateEnums;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.econtract.enums.warning.*;
import com.yaoan.module.econtract.service.warningcfg.query.DynamicQueryService;
import com.yaoan.module.econtract.service.warningcfg.query.QueryCondition;
import com.yaoan.module.econtract.service.workday.WorkDayService;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.dal.dataobject.notify.NotifyMessageDO;
import com.yaoan.module.system.dal.dataobject.notify.NotifyTemplateDO;
import com.yaoan.module.system.dal.mysql.notify.NotifyMessageMapper;
import com.yaoan.module.system.service.notify.NotifySendService;
import com.yaoan.module.system.service.notify.NotifyTemplateService;
import liquibase.repackaged.org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DATA_ERROR;
import static com.yaoan.framework.security.core.util.SecurityFrameworkUtils.getLoginUser;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR;

/**
 * 预警检查配置表(new预警) Service 实现类
 *
 * @author admin
 */
@Service
@Validated
public class WarningCfgServiceImpl implements WarningCfgService {

    @Resource
    private WarningCfgMapper warningCfgMapper;
    @Resource
    private WarningItemMapper warningItemMapper;
    @Resource
    private WarningItemRuleMapper warningItemRuleMapper;
    @Resource
    private WarningNoticeCfgMapper warningNoticeCfgMapper;
    @Resource
    private WarningRuleMonitorRelMapper warningRuleMonitorRelMapper;
    @Resource
    private WarningMonitorMapper warningMonitorMapper;
    @Resource
    private WarningRuleMonitorRelMapper warningRuleMonitorMapper;
    @Resource
    private WarningParamMapper warningParamMapper;
    @Resource
    private WarningModelMapper warningModelMapper;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private ContractArchivesMapper contractArchivesMapper;
    @Resource
    private RelativeContactMapper relativeContactMapper;
    @Resource
    private DynamicQueryService dynamicQueryService;

    @Resource
    private ContractApi contractApi;
    @Resource
    private SignatoryRelMapper signatoryRelMapper;
    @Resource
    private RelativeMapper relativeMapper;
    @Resource
    private WorkDayService workDayService;
    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private NotifySendService notifySendService;

    @Resource
    private NotifyMessageMapper notifyMessageMapper;
    @Resource
    private NotifyTemplateService notifyTemplateService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String createWarningCfg(WarningCfgCreateReqVO createReqVO) {
        String configId = createReqVO.getCfgBase4CfgCreateReqVO().getId();
        WarningCfgDO warningCfg = new WarningCfgDO();
        List<WarningItemDO> totalItemDOList = new ArrayList<>();
        List<WarningItemRuleDO> totalRuleDOList = new ArrayList<>();
        List<WarningNoticeCfgDO> noticeCfgDOList = new ArrayList<>();
        enhanceReq(createReqVO);
        // 检查点基本信息
        warningCfg = WarningCfgConvert.INSTANCE.convertReq2Do(createReqVO.getCfgBase4CfgCreateReqVO());

        // 预警事项
        totalItemDOList = WarningItemConvert.INSTANCE.convertListR2D(createReqVO.getItemReqVOList());
        List<WarningRuleMonitorRelDO> relDOList = new ArrayList<>();
        // 预警规则
        for (WarningItem4CfgCreateReqVO itemReqVO : createReqVO.getItemReqVOList()) {
            itemReqVO.setConfigId(configId);
            if (CollUtil.isNotEmpty(itemReqVO.getRules())) {
                for (WarningItemRule4CfgCreateReqVO reqVORule : itemReqVO.getRules()) {
                    //规则
                    WarningItemRuleDO itemRuleDO = WarningItemRuleConvert.INSTANCE.req2DO4Cfg(reqVORule);
                    itemRuleDO.setConfigId(configId);
                    totalRuleDOList.add(itemRuleDO);
                    if (CollUtil.isNotEmpty(reqVORule.getWarningNoticeList())) {
                        for (WarningNotice4CfgCreateReqVO noticeReqVO : reqVORule.getWarningNoticeList()) {
                            WarningNoticeCfgDO noticeCfgDO = WarningNoticeCfgConvert.INSTANCE.req2Do(noticeReqVO);
                            noticeCfgDO.setConfigId(configId);
                            noticeCfgDOList.add(noticeCfgDO);
                        }
                    }
                }
            }
        }

        //批量保存（需注意更新操作，避免主键异常）
        //判断是否需要清理数据
        Long count = warningCfgMapper.selectCount(WarningCfgDO::getId, configId);
        if (0L < count) {
            //编辑
            warningCfgMapper.updateById(warningCfg);
            //清空关联的旧数据
            List<String> configIds = new ArrayList<>();
            configIds.add(createReqVO.getCfgBase4CfgCreateReqVO().getId());
            warningItemMapper.deleteBatch(configId);
            warningItemRuleMapper.deleteBatch(configId);
            warningNoticeCfgMapper.deleteBatch(configId);
            warningRuleMonitorRelMapper.deleteBatch(configId);
        } else {
            warningCfgMapper.insert(warningCfg);
        }

        warningItemMapper.insertBatch(totalItemDOList);
        warningItemRuleMapper.insertBatch(totalRuleDOList);
        warningNoticeCfgMapper.insertBatch(noticeCfgDOList);
        warningRuleMonitorRelMapper.insertBatch(relDOList);
        // 返回
        return warningCfg.getId();
    }

    private void enhanceReq(WarningCfgCreateReqVO createReqVO) {
        //补全模块来源信息
        List<String> modelIds = createReqVO.getCfgBase4CfgCreateReqVO().getModelIds();
        if (CollUtil.isEmpty(modelIds)) {
            throw exception(DIY_ERROR, "模块来源不可为空。");
        }
        createReqVO.getCfgBase4CfgCreateReqVO().setModelId(modelIds.get(modelIds.size() - 1));
        WarningModelDO warningModelDO = warningModelMapper.selectById(createReqVO.getCfgBase4CfgCreateReqVO().getModelId());
        if (ObjectUtil.isNotNull(warningModelDO)) {
            createReqVO.getCfgBase4CfgCreateReqVO().setModelCode(warningModelDO.getCode());
            createReqVO.getCfgBase4CfgCreateReqVO().setModelName(warningModelDO.getName());
        }

        Map<String, List<WarningItemRule4CfgCreateReqVO>> ruleMap = new HashMap<>();
        if (CollUtil.isNotEmpty(createReqVO.getRuleReqVOList())) {
            ruleMap = com.yaoan.framework.common.util.collection.CollectionUtils.convertMultiMap(createReqVO.getRuleReqVOList(), WarningItemRule4CfgCreateReqVO::getWarningItemId);
        }
        for (WarningItem4CfgCreateReqVO item4CfgCreateReqVO : createReqVO.getItemReqVOList()) {
            List<WarningItemRule4CfgCreateReqVO> ruleList = ruleMap.get(item4CfgCreateReqVO.getId());
            if (CollUtil.isNotEmpty(ruleList)) {
                item4CfgCreateReqVO.setRules(ruleList);
            }
        }

    }


    @Override
    public WarningCfgRespVO getWarningCfg(String configId) {
        WarningCfgRespVO respVO = new WarningCfgRespVO();
        List<WarningItem4CfgRespVO> itemRespVOList = new ArrayList<>();
        List<WarningItemRule4CfgRespVO> respVOList = new ArrayList<>();
        List<WarningItemRule4CfgRespVO> finalRules = new ArrayList<>();

        List<WarningNoticeCfgDO> noticeCfgDOList = new ArrayList<>();
        List<WarningNotice4CfgRespVO> notice4CfgRespVOList = new ArrayList<>();

        Map<String, WarningItem4CfgRespVO> itemDOMap = new HashMap<>();
        Map<String, List<WarningItemRule4CfgRespVO>> ruleResp4RuleIdMap = new HashMap<>();
        Map<String, List<WarningNotice4CfgRespVO>> noticeCfgDO4RuleIdMap = new HashMap<>();

        WarningCfgDO cfgDO = warningCfgMapper.selectById(configId);
        if (Objects.isNull(cfgDO)) {
            throw exception(DATA_ERROR);
        }
        WarningCfgBase4CfgRespVO cfgBase4CfgRespVO = WarningCfgConvert.INSTANCE.do2Resp(cfgDO);
        enhanceWarningCfgResp(cfgBase4CfgRespVO);
        List<WarningItemDO> itemDOList = warningItemMapper.selectList(WarningItemDO::getConfigId, configId);
        if (CollUtil.isNotEmpty(itemDOList)) {
            itemRespVOList = WarningItemConvert.INSTANCE.listDo2Resp(itemDOList);
        }
        List<String> itemIds = itemDOList.stream().map(WarningItemDO::getId).collect(Collectors.toList());
        List<WarningItemRuleDO> ruleDOList = warningItemRuleMapper.selectList(WarningItemRuleDO::getWarningItemId, itemIds);
        if (CollUtil.isNotEmpty(ruleDOList)) {
            respVOList = WarningItemRuleConvert.INSTANCE.listDo2Resp(ruleDOList);
            ruleResp4RuleIdMap = com.yaoan.framework.common.util.collection.CollectionUtils.convertMultiMap(respVOList, WarningItemRule4CfgRespVO::getWarningItemId);

            List<String> ruleIds = ruleDOList.stream().map(WarningItemRuleDO::getId).collect(Collectors.toList());
            noticeCfgDOList = warningNoticeCfgMapper.selectList(WarningNoticeCfgDO::getRuleId, ruleIds);
            if (CollUtil.isNotEmpty(noticeCfgDOList)) {
                notice4CfgRespVOList = WarningNoticeCfgConvert.INSTANCE.listDo2Resp(noticeCfgDOList);
                noticeCfgDO4RuleIdMap = com.yaoan.framework.common.util.collection.CollectionUtils.convertMultiMap(notice4CfgRespVOList, WarningNotice4CfgRespVO::getRuleId);
            }
        }


        for (WarningItem4CfgRespVO itemRespVO : itemRespVOList) {
            List<WarningItemRule4CfgRespVO> rule4CfgRespVOS = ruleResp4RuleIdMap.get(itemRespVO.getId());
            if (CollUtil.isNotEmpty(rule4CfgRespVOS)) {
                enhanceRuleResp(rule4CfgRespVOS, noticeCfgDO4RuleIdMap, finalRules);
            }
        }

        return respVO.setCfgBase4CfgRespVO(cfgBase4CfgRespVO).setItemRespVOList(itemRespVOList).setRuleRespVOList(finalRules);
    }

    private void enhanceWarningCfgResp(WarningCfgBase4CfgRespVO cfgBase4CfgRespVO) {
        WarningModelDO modelDO = warningModelMapper.selectById(cfgBase4CfgRespVO.getModelId());
        if (ObjectUtil.isNotNull(modelDO)) {
            List<String> modelIds = new ArrayList<>();
            if (StringUtils.isNotEmpty(modelDO.getParentId())) {
                modelIds.add(modelDO.getParentId());
            }
            modelIds.add(modelDO.getId());
            cfgBase4CfgRespVO.setModelIds(modelIds);
        }
    }

    private List<WarningItemRule4CfgRespVO> enhanceRuleResp(List<WarningItemRule4CfgRespVO> rule4CfgRespVOS, Map<String, List<WarningNotice4CfgRespVO>> noticeCfgDO4RuleIdMap, List<WarningItemRule4CfgRespVO> finalRules) {
        if (CollUtil.isEmpty(rule4CfgRespVOS)) {
            return Collections.emptyList();
        }
        for (WarningItemRule4CfgRespVO rule4CfgRespVO : rule4CfgRespVOS) {
            List<WarningNotice4CfgRespVO> notice4CfgRespVOS = noticeCfgDO4RuleIdMap.get(rule4CfgRespVO.getId());
            if (CollUtil.isNotEmpty(notice4CfgRespVOS)) {
                rule4CfgRespVO.setWarningNoticeList(notice4CfgRespVOS);
            }
            finalRules.add(rule4CfgRespVO);
        }
        return rule4CfgRespVOS;
    }


    @Override
    public void updateWarningCfg(WarningCfgUpdateReqVO updateReqVO) {
        // 校验存在
        validateWarningCfgExists(updateReqVO.getId());
        // 更新
        WarningCfgDO updateObj = WarningCfgConvert.INSTANCE.convert(updateReqVO);
        warningCfgMapper.updateById(updateObj);
    }

    @Override
    public void deleteWarningCfg(String id) {
        // 校验存在
        validateWarningCfgExists(id);
        // 删除
        warningCfgMapper.deleteById(id);
    }

    private void validateWarningCfgExists(String id) {
        if (warningCfgMapper.selectById(id) == null) {
//            throw exception(WARNING_CFG_NOT_EXISTS);
        }
    }

    @Override
    public List<WarningCfgDO> getWarningCfgList(Collection<String> ids) {
        return warningCfgMapper.selectBatchIds(ids);
    }

    @Override
    public PageResult<WarningPageRespVO> getWarningCfgPage(WarningCfgPageReqVO pageReqVO) {
        if (StringUtils.isNotEmpty(pageReqVO.getModelId())) {
            List<WarningModelDO> warningModelDOS = warningModelMapper.selectList(WarningModelDO::getParentId, pageReqVO.getModelId());
            if (CollUtil.isNotEmpty(warningModelDOS)) {
                List<String> modelIds = warningModelDOS.stream().map(WarningModelDO::getId).collect(Collectors.toList());
                modelIds.add(pageReqVO.getModelId());
                pageReqVO.setModelIds(modelIds);
            } else {
                pageReqVO.setModelIds(Collections.singletonList(pageReqVO.getModelId()));
            }
        }
        PageResult<WarningCfgDO> doPage = warningCfgMapper.selectPage(pageReqVO);
        PageResult<WarningPageRespVO> result = WarningCfgConvert.INSTANCE.pageDo2Resp(doPage);

        if (CollUtil.isEmpty(doPage.getList())) {
            return result.setTotal(0L).setList(new ArrayList<WarningPageRespVO>());
        }
        List<String> configIds = doPage.getList().stream().map(WarningCfgDO::getId).collect(Collectors.toList());
        List<WarningItemDO> itemDOList = warningItemMapper.selectList(WarningItemDO::getConfigId, configIds);
        List<WarningItemPageRespVO> itemPageRespVOList = new ArrayList<>();
        Map<String, List<WarningItemPageRespVO>> Item4CfgRespVOMap = new HashMap<>();
        if (CollUtil.isNotEmpty(itemDOList)) {
            itemPageRespVOList = WarningItemConvert.INSTANCE.listDo2Resp2(itemDOList);
            Item4CfgRespVOMap = com.yaoan.framework.common.util.collection.CollectionUtils.convertMultiMap(itemPageRespVOList, WarningItemPageRespVO::getConfigId);
        }

        return enhancePage(result, Item4CfgRespVOMap);
    }

    private PageResult<WarningPageRespVO> enhancePage(PageResult<WarningPageRespVO> result, Map<String, List<WarningItemPageRespVO>> item4CfgRespVOMap) {
        for (WarningPageRespVO cfgRespVO : result.getList()) {
            List<WarningItemPageRespVO> itemPageRespVOList = item4CfgRespVOMap.get(cfgRespVO.getId());
            if (CollUtil.isNotEmpty(itemPageRespVOList)) {
                cfgRespVO.setItemPageRespVOList(itemPageRespVOList);
            }
        }
        return result;
    }

    @Override
    public List<WarningCfgDO> getWarningCfgList(WarningCfgExportReqVO exportReqVO) {
        return warningCfgMapper.selectList(exportReqVO);
    }

    // 预警消息检查
    @Override
    public void warningCheck(String flag, TaskForWarningReqDTO taskParams, String businessId, String... modelCodes) {
        // 查询全部模块来源
        List<WarningModelDO> warningModelDOS = new ArrayList<>();
        if (modelCodes.length != 0 && modelCodes[0] != null) {
            warningModelDOS = warningModelMapper.selectList(WarningModelDO::getCode, modelCodes[0]);
        } else {
            warningModelDOS = warningModelMapper.selectList();
        }
        Map<String, String> modelMap = warningModelDOS.stream().collect(Collectors.toMap(WarningModelDO::getId, WarningModelDO::getCode));
        // 查询启用的检查
        List<WarningCfgDO> warningCfgDOS = warningCfgMapper.selectList(WarningCfgDO::getStatus, IfNumEnums.YES.getCode());
        if (modelCodes.length != 0 && modelCodes[0] != null) {
            warningCfgDOS = warningCfgDOS.stream().filter(item -> Arrays.asList(modelCodes).contains(item.getModelCode())).collect(Collectors.toList());
        }
        List<String> configIds = warningCfgDOS.stream().map(WarningCfgDO::getId).collect(Collectors.toList());
        Map<String, WarningCfgDO> cfgMap = warningCfgDOS.stream().collect(Collectors.toMap(WarningCfgDO::getId, item -> item));
        // 查询全部的事项
        List<WarningItemDO> warningItemDOS = warningItemMapper.selectList(WarningItemDO::getConfigId, configIds);
        List<String> itemIds = warningItemDOS.stream().map(WarningItemDO::getId).collect(Collectors.toList());
        Map<String, WarningItemDO> itemMap = warningItemDOS.stream().collect(Collectors.toMap(WarningItemDO::getId, item -> item));
        // 查询全部立即执行的规则
        if (!CollectionUtils.isEmpty(itemIds)) {
            LambdaQueryWrapperX<WarningItemRuleDO> queryWrapperX = new LambdaQueryWrapperX<>();
            queryWrapperX.in(WarningItemRuleDO::getWarningItemId, itemIds);
            if ("2".equals(flag)) {
                queryWrapperX.ne(WarningItemRuleDO::getCompareType, WarningCompareTypeEnum.RUN_NOW.getCode());
            } else {
                queryWrapperX.eq(WarningItemRuleDO::getCompareType, WarningCompareTypeEnum.RUN_NOW.getCode());

            }

            List<WarningItemRuleDO> warningItemRuleDOS = warningItemRuleMapper.selectList(queryWrapperX);
            List<String> ruleIds = warningItemRuleDOS.stream().map(WarningItemRuleDO::getId).collect(Collectors.toList());
            List<String> monitorIds = warningItemRuleDOS.stream().map(WarningItemRuleDO::getMonitorItemId).collect(Collectors.toList());

            // 查询规则相关全部的通知消息配置
            List<WarningNoticeCfgDO> warningNoticeCfgDOS = warningNoticeCfgMapper.selectList(WarningNoticeCfgDO::getRuleId, ruleIds);
            Map<String, List<WarningNoticeCfgDO>> noticeCfgDOMap = warningNoticeCfgDOS.stream().collect(Collectors.groupingBy(WarningNoticeCfgDO::getRuleId));

            // 查询规则对应的监控项
            List<WarningMonitorDO> warningMonitorDOS = warningMonitorMapper.selectList(WarningMonitorDO::getId, monitorIds);
            Map<String, WarningMonitorDO> warningMonitorDOMap = warningMonitorDOS.stream().collect(Collectors.toMap(WarningMonitorDO::getId, item -> item));
            //监控项对应的子项
            List<WarningMonitorDO> monitorChildList = warningMonitorMapper.selectList(WarningMonitorDO::getParentId, monitorIds);
            Map<String, List<WarningMonitorDO>> monitorChildMap = monitorChildList.stream().collect(Collectors.groupingBy(WarningMonitorDO::getParentId));

            // 遍历规则配置
            for (WarningItemRuleDO warningItemRuleDO : warningItemRuleDOS) {
                // 获取当前规则对应的监控项
                List<WarningMonitorDO> monitorDOList = new ArrayList<>();
                monitorDOList.add(warningMonitorDOMap.get(warningItemRuleDO.getMonitorItemId()));
                if (CollectionUtils.isNotEmpty(monitorChildMap.get(warningItemRuleDO.getMonitorItemId()))) {
                    monitorDOList.addAll(monitorChildMap.get(warningItemRuleDO.getMonitorItemId()));
                }
                // 确认监控监控类型，监控模块是否一致
                validateWarningMonitors(monitorDOList);

                Map nMap = new HashMap();
                // 获取符合预警提醒的数据
                List dataList = checkData(warningItemRuleDO, taskParams, businessId, monitorDOList, nMap);
                if (CollectionUtils.isEmpty(dataList)) {
                    continue;
                }

                // 数据抽取参数信息
                Map<Object, Map> paramsMap = new HashMap();
                setDataParams(paramsMap, dataList, monitorDOList.get(0).getModelId(), modelMap.get(monitorDOList.get(0).getModelId()), monitorDOList.get(0).getFlowKey());

                // 查询规则的消息配置
                List<WarningNoticeCfgDO> warningNoticeCfgDO = noticeCfgDOMap.get(warningItemRuleDO.getId());

                // 每条数据发起通知
                for (Object o : dataList) {
                    // 每种消息通知配置
                    if (CollectionUtil.isEmpty(warningNoticeCfgDO)) {
                        continue;
                    }
                    for (WarningNoticeCfgDO noticeCfgDO : warningNoticeCfgDO) {
                        DataPermissionUtils.executeIgnore(() -> {
                            TenantUtils.executeIgnore(() -> {
                                List<Long> userIds = new ArrayList<>();
                                // 判断用户类型
                                if (WarningNoticeUserTypeEnum.BY_CONFIG.getCode().equals(noticeCfgDO.getUserType())) {
                                    // 如果是配置
                                    if (noticeCfgDO.getUserIds() != null) {
                                        String[] split = noticeCfgDO.getUserIds().split(",");
                                        for (String s : split) {
                                            userIds.add(Long.parseLong(s));
                                        }
                                    }
                                } else if (WarningNoticeUserTypeEnum.BY_DATA.getCode().equals(noticeCfgDO.getUserType())) {
                                    // 如果是根据业务数据
                                    userIds.add(Long.valueOf(getPrivateFieldValue(o, "creator") + ""));
                                } else {
                                    // 如果是根据工作流，判断是流程节点人还是流程创建人
                                    if (WarningNoticeFlowUserTypeEnums.ASSIGNEE.getCode().equals(noticeCfgDO.getFlowUserType())) {
                                        String assignee = (String) getPrivateFieldValue(o, "assignee");
                                        if (StringUtils.isNotEmpty(assignee)) {
                                            userIds.add(Long.valueOf(assignee));
                                        }
                                    } else {
                                        String processInstanceCreator = (String) getPrivateFieldValue(o, "processInstanceCreator");
                                        if (StringUtils.isNotEmpty(processInstanceCreator)) {
                                            userIds.add(Long.valueOf(processInstanceCreator));
                                        }
                                    }
                                }
                                userIds = CollUtil.distinct(userIds);

                                Map params = new HashMap();
                                if (WarningMonitorTypeEnum.BUSINESS.getCode().equals(monitorDOList.get(0).getType())) {
                                    Object id = getPrivateFieldValue(o, "id");
                                    params = paramsMap.get(id);
                                    if (ObjectUtil.isNotEmpty(params)) {
                                        if (ObjectUtil.isNotEmpty(nMap.get(id))) {
                                            params.put("n", nMap.get(id));
                                        } else {
                                            params.put("n", warningItemRuleDO.getCompareItemStart());
                                        }
                                    }
                                } else {
                                    Object id = getPrivateFieldValue(o, "taskId");
                                    params = paramsMap.get(id);
                                    if (ObjectUtil.isNotEmpty(params)) {
                                        if (ObjectUtil.isNotEmpty(nMap.get(id))) {
                                            params.put("n", nMap.get(id));
                                        } else {
                                            params.put("n", warningItemRuleDO.getCompareItemStart());
                                        }
                                    }
                                }
                                if (CollectionUtils.isNotEmpty(params)) {
                                    params.put("m", warningItemRuleDO.getCompareItemEnd());
                                    if (WarningNoticeWayEnum.WEB_MESSAGE.getCode().equals(noticeCfgDO.getNoticeWay())) {
                                        // 组装预警相关数据信息
                                        Map warningMap = new HashMap();
                                        warningMap.put("businessId", getPrivateFieldValue(o, "id"));
                                        warningMap.put("taskId", getPrivateFieldValue(o, "taskId"));
                                        warningMap.put("ruleId", warningItemRuleDO.getId());
                                        WarningItemDO warningItemDO = itemMap.get(warningItemRuleDO.getWarningItemId());
                                        WarningCfgDO warningCfgDO = cfgMap.get(warningItemDO.getConfigId());

                                        warningMap.put("modelName", warningCfgDO.getModelName());
                                        warningMap.put("warnName", warningCfgDO.getName());
                                        warningMap.put("itemName", warningItemDO.getItemName());
                                        // 发送站内消息
                                        List<AdminUserRespDTO> userList = adminUserApi.getUserList(userIds);
                                        Map<Long, Long> tenantIdMap = userList.stream().collect(Collectors.toMap(AdminUserRespDTO::getId, AdminUserRespDTO::getTenantId));
                                        sendMessage(userIds, noticeCfgDO.getContentTemplate(), params, warningMap, tenantIdMap);
                                    } else if (WarningNoticeWayEnum.MESSAGE.getCode().equals(noticeCfgDO.getNoticeWay())) {
                                        // 发送短信
                                    } else {
                                        // 发送邮件
                                    }
                                }
                            });
                        });
                    }
                }
            }
        }
    }

    @Override
    public void updateStatus(WarningCfgTurnReqVO reqVO) {
        int rs = warningCfgMapper.updateById(new WarningCfgDO().setId(reqVO.getId()).setStatus(reqVO.getStatus()));
        if (rs == 0) {
            throw exception(DIY_ERROR, "操作失败，请联系管理员。");
        }
    }

    // 校验监控项
    private void validateWarningMonitors(List<WarningMonitorDO> monitorDOList) {
        List<Integer> types = monitorDOList.stream().map(WarningMonitorDO::getType).distinct().collect(Collectors.toList());
        if (types.size() != 1) {
            throw exception(DIY_ERROR, "监控项类型不一致");
        }
        if (WarningMonitorTypeEnum.BUSINESS.getCode().equals(types.get(0))) {
            List<String> businessCodes = monitorDOList.stream().map(WarningMonitorDO::getBusinessCode).distinct().collect(Collectors.toList());
            if (businessCodes.size() != 1) {
                throw exception(DIY_ERROR, "监控项业务编码不一致");
            }
        }
        if (WarningMonitorTypeEnum.WORK_FLOW.getCode().equals(types.get(0))) {
            List<String> flowKeys = monitorDOList.stream().map(WarningMonitorDO::getFlowKey).distinct().collect(Collectors.toList());
            if (flowKeys.size() != 1) {
                throw exception(DIY_ERROR, "监控项流程编码不一致");
            }
        }
    }


    // 检查筛选数据
    private List checkData(WarningItemRuleDO warningItemRuleDO, TaskForWarningReqDTO taskParams, String businessId, List<WarningMonitorDO> monitorDOList, Map nMap) {
        List dataList = new ArrayList();
        String compareField = "";
        String moniId = "";
        // 如果是监控业务数据类型的
        if (WarningMonitorTypeEnum.BUSINESS.getCode().equals(monitorDOList.get(0).getType())) {
            moniId = "id";
            String businessCode = monitorDOList.get(0).getBusinessCode();
            if (WarningCompareTypeEnum.RUN_NOW.getCode().equals(warningItemRuleDO.getCompareType())) {
                // 如果是业务类型且是立即执行类型监听，如果业务id为空，则不执行
                if (StringUtils.isEmpty(businessId)) {
                    return dataList;
                } else {
                    QueryCondition queryCondition = new QueryCondition();
                    queryCondition.setField("id");
                    queryCondition.setValue(businessId);
                    queryCondition.setOperator(WarningCompareTypeEnum.EQUAL.getCode());
                    return removeNoticeData(warningItemRuleDO, dynamicQueryService.dynamicQuery(WarningBusinessEnum.getInstance(businessCode).getFactoryName(), Arrays.asList(queryCondition)));

                }
            }
            // 判断监控规则单位
            List conditions = new ArrayList();
            for (WarningMonitorDO warningMonitorDO : monitorDOList) {
                Integer compareType = warningItemRuleDO.getCompareType();
                QueryCondition queryCondition = new QueryCondition();
                queryCondition.setField(warningMonitorDO.getBusinessField());
                if (WarningMonitorCompareTypeEnum.BY_VALUE.getCode().equals(warningMonitorDO.getCompareType())) {
                    // 如果是取值的
                    if (StringUtils.isNotEmpty(warningMonitorDO.getCompareStr())) {
                        queryCondition.setValue(warningMonitorDO.getCompareStr());
                        compareType = WarningCompareTypeEnum.EQUAL.getCode();
                    }
                } else if (WarningMonitorCompareTypeEnum.BY_SQL.getCode().equals(warningMonitorDO.getCompareType())) {
                    // 如果是根据sql查询的
                    if (StringUtils.isNotEmpty(warningMonitorDO.getCompareStr())) {
                        queryCondition.setValue(warningMonitorDO.getCompareStr());
                        queryCondition.setSqlFlag(true);
                        compareType = WarningCompareTypeEnum.IN.getCode();
                    }

                } else if (WarningMonitorCompareTypeEnum.BY_CAL_SQL.getCode().equals(warningMonitorDO.getCompareType())){
                    String compareStr = "";
                    if (ObjectUtil.isNotEmpty(warningItemRuleDO.getCompareItemEnd())){
                        compareStr = warningMonitorDO.getCompareStr()+ " BETWEEN "+warningItemRuleDO.getCompareItemStart()+" AND "+ warningItemRuleDO.getCompareItemEnd();
                    } else {
                        compareStr = warningMonitorDO.getCompareStr()+ "  "+WarningCompareTypeEnum.getInstance(compareType).getMathSymbol()+" " +warningItemRuleDO.getCompareItemStart();
                    }
                    queryCondition.setValue(compareStr);
                    queryCondition.setSqlFlag(true);
                    compareType = WarningCompareTypeEnum.IN.getCode();

                } else{
                    Object startItem = null;
                    Object endItem = null;
                    // 1.自然日
                    if (WarningUnitTypeEnum.DAY.getCode().equals(warningItemRuleDO.getCompareDataType())) {
                        // 如果是计算的
                        if (WarningMonitorCompareTypeEnum.BY_CALCULATION.getCode().equals(warningMonitorDO.getCompareType())) {
                            // 如果监控项是计差 如果比较字段为空，则认为是与当前字段比较，否则计算字段间的值
                            if (StringUtils.isNotEmpty(warningMonitorDO.getCompareStr())) {
                                queryCondition.setField(warningMonitorDO.getBusinessField()).setCalField(warningMonitorDO.getCompareStr()).setN(warningItemRuleDO.getCompareItemStart());
                            } else {
                                compareField = warningMonitorDO.getBusinessField();
//                                queryCondition.setField(warningMonitorDO.getBusinessField()).setCalField("CURDATE()").setN(warningItemRuleDO.getCompareItemStart());
                                Date startDate = new Date();
                                Date endDate = new Date();
                                // 临期, 往后算n天，监控字段小于n天后
                                if (WarningMonitorCalculateTypeEnum.SUB_NOW.getCode().equals(warningMonitorDO.getCalculateType())) {
                                    if (WarningCompareTypeEnum.LESS.getCode().equals(warningItemRuleDO.getCompareType()) || WarningCompareTypeEnum.LESS_EQUAL.getCode().equals(warningItemRuleDO.getCompareType())) {
                                        // 如果是临期，时间小于三天
                                        // 起始日期为现在， 终止日期为三天后
                                        startDate = new Date();
                                        endDate = DateUtils.addDays(new Date(), warningItemRuleDO.getCompareItemStart());
                                        compareType = WarningCompareTypeEnum.BETWEEN.getCode();
                                    } else if (WarningCompareTypeEnum.EQUAL.getCode().equals(warningItemRuleDO.getCompareType())) {
                                        Date item = DateUtils.addDays(new Date(), warningItemRuleDO.getCompareItemStart());
                                        SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                                        SimpleDateFormat endSdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
                                        try {
                                            startDate = DateUtils.parseDate(startSdf.format(item), "yyyy-MM-dd HH:mm:ss");
                                            endDate = DateUtils.parseDate(endSdf.format(item), "yyyy-MM-dd HH:mm:ss");
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        compareType = WarningCompareTypeEnum.BETWEEN.getCode();
                                    } else {
                                        compareType = WarningCompareTypeEnum.GREATER.getCode();
                                        startDate = DateUtils.addDays(new Date(), warningItemRuleDO.getCompareItemStart());
                                        if (ObjectUtil.isNotEmpty(warningItemRuleDO.getCompareItemEnd())) {
                                            endDate = DateUtils.addDays(new Date(), warningItemRuleDO.getCompareItemEnd());
                                            compareType = WarningCompareTypeEnum.BETWEEN.getCode();
                                        }
                                    }
                                }
                                // 超期，往前算n天，监控字段大于n天前
                                if (WarningMonitorCalculateTypeEnum.NOW_SUB.getCode().equals(warningMonitorDO.getCalculateType())) {
                                    // 1. 获取前一天的日期（当前日期减1天）
                                    LocalDate yesterday = LocalDate.now().minusDays(1);
                                    // 2. 设置前一天的时间为 23:59:59.999
                                    LocalDateTime yesterdayEnd = yesterday.atTime(23, 59, 59, 999_000_000); // 999毫秒（纳秒单位）
                                    // 3. 转换为 Date 对象（关联系统时区）
                                    Date now = Date.from(
                                            yesterdayEnd.atZone(ZoneId.systemDefault()).toInstant()
                                    );
                                    if (WarningCompareTypeEnum.LESS.getCode().equals(warningItemRuleDO.getCompareType()) || WarningCompareTypeEnum.LESS_EQUAL.getCode().equals(warningItemRuleDO.getCompareType())) {
                                        startDate = DateUtils.addDays(now, -warningItemRuleDO.getCompareItemStart());
                                        endDate = now;
                                        compareType = WarningCompareTypeEnum.BETWEEN.getCode();
                                    } else if (WarningCompareTypeEnum.EQUAL.getCode().equals(warningItemRuleDO.getCompareType())) {
                                        Date item = workDayService.calculateJumpRestDays(now, warningItemRuleDO.getCompareItemStart(), 0);
                                        SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                                        SimpleDateFormat endSdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
                                        try {
                                            startDate = DateUtils.parseDate(startSdf.format(item), "yyyy-MM-dd HH:mm:ss");
                                            endDate = DateUtils.parseDate(endSdf.format(item), "yyyy-MM-dd HH:mm:ss");
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        compareType = WarningCompareTypeEnum.BETWEEN.getCode();
                                    } else {
                                        startDate = DateUtils.addDays(now, -warningItemRuleDO.getCompareItemStart());
                                        compareType = WarningCompareTypeEnum.LESS.getCode();
                                        if (ObjectUtil.isNotEmpty(warningItemRuleDO.getCompareItemEnd())) {
                                            endDate = DateUtils.addDays(now, -warningItemRuleDO.getCompareItemEnd());
                                            compareType = WarningCompareTypeEnum.BETWEEN.getCode();
                                        }
                                    }
                                }
                                if (WarningCompareTypeEnum.BETWEEN.getCode().equals(compareType)) {
                                    SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                                    SimpleDateFormat endSdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
                                    startItem = startDate.before(endDate) ? startDate : endDate;
                                    endItem = startDate.before(endDate) ? endDate : startDate;
                                    try {
                                        startItem = DateUtils.parseDate(startSdf.format(startItem), "yyyy-MM-dd HH:mm:ss");
                                        endItem = DateUtils.parseDate(endSdf.format(endItem), "yyyy-MM-dd HH:mm:ss");

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    startItem = startDate;
                                }
                            }
                        }
                        if (WarningCompareTypeEnum.BETWEEN.getCode().equals(compareType)) {
                            queryCondition.setValue(Arrays.asList(startItem, endItem));
                        } else {
                            queryCondition.setValue(startItem);
                        }
                    }
                    // 2.工作日
                    if (WarningUnitTypeEnum.WORK_DAY.getCode().equals(warningItemRuleDO.getCompareDataType())) {
                        if (WarningMonitorCompareTypeEnum.BY_CALCULATION.getCode().equals(warningMonitorDO.getCompareType())) {
                            // 如果监控项是计差 如果比较字段为空，则认为是与当前字段比较，否则计算字段间的值
                            if (StringUtils.isNotEmpty(warningMonitorDO.getCompareStr())) {
                                queryCondition.setField(warningMonitorDO.getBusinessField()).setCalField(warningMonitorDO.getCompareStr()).setN(warningItemRuleDO.getCompareItemStart());
                            } else {
                                compareField = warningMonitorDO.getBusinessField();
                                // 如果监控项是计差
                                // 临期 向后计算日期
                                Date startDate = new Date();
                                Date endDate = new Date();
                                if (WarningMonitorCalculateTypeEnum.SUB_NOW.getCode().equals(warningMonitorDO.getCalculateType())) {
                                    if (WarningCompareTypeEnum.LESS.getCode().equals(warningItemRuleDO.getCompareType()) || WarningCompareTypeEnum.LESS_EQUAL.getCode().equals(warningItemRuleDO.getCompareType())) {
                                        startDate = workDayService.calculateJumpRestDays(new Date(), warningItemRuleDO.getCompareItemStart(), 1);
                                        endDate = new Date();
                                        compareType = WarningCompareTypeEnum.BETWEEN.getCode();
                                    } else if (WarningCompareTypeEnum.EQUAL.getCode().equals(warningItemRuleDO.getCompareType())) {
                                        Date item = workDayService.calculateJumpRestDays(new Date(), warningItemRuleDO.getCompareItemStart(), 1);
                                        SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                                        SimpleDateFormat endSdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
                                        try {
                                            startDate = DateUtils.parseDate(startSdf.format(item), "yyyy-MM-dd HH:mm:ss");
                                            endDate = DateUtils.parseDate(endSdf.format(item), "yyyy-MM-dd HH:mm:ss");
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        compareType = WarningCompareTypeEnum.BETWEEN.getCode();
                                    } else {
                                        startDate = workDayService.calculateJumpRestDays(new Date(), warningItemRuleDO.getCompareItemStart(), 1);
                                        compareType = WarningCompareTypeEnum.GREATER.getCode();
                                        if (ObjectUtil.isNotEmpty(warningItemRuleDO.getCompareItemEnd())) {
                                            endDate = workDayService.calculateJumpRestDays(new Date(), warningItemRuleDO.getCompareItemEnd(), 1);
                                            compareType = WarningCompareTypeEnum.BETWEEN.getCode();
                                        }
                                    }
                                }
                                // 超期 向前计算日期
                                if (WarningMonitorCalculateTypeEnum.NOW_SUB.getCode().equals(warningMonitorDO.getCalculateType())) {
                                    // 1. 获取前一天的日期（当前日期减1天）
                                    LocalDate yesterday = LocalDate.now().minusDays(1);
                                    // 2. 设置前一天的时间为 23:59:59.999
                                    LocalDateTime yesterdayEnd = yesterday.atTime(23, 59, 59, 999_000_000); // 999毫秒（纳秒单位）
                                    // 3. 转换为 Date 对象（关联系统时区）
                                    Date now = Date.from(
                                            yesterdayEnd.atZone(ZoneId.systemDefault()).toInstant()
                                    );

                                    if (WarningCompareTypeEnum.LESS.getCode().equals(warningItemRuleDO.getCompareType()) || WarningCompareTypeEnum.LESS_EQUAL.getCode().equals(warningItemRuleDO.getCompareType())) {
                                        startDate = now;
                                        endDate = workDayService.calculateJumpRestDays(now, warningItemRuleDO.getCompareItemStart(), 0);
                                        compareType = WarningCompareTypeEnum.BETWEEN.getCode();
                                    } else if (WarningCompareTypeEnum.EQUAL.getCode().equals(warningItemRuleDO.getCompareType())) {
                                        Date item = workDayService.calculateJumpRestDays(now, warningItemRuleDO.getCompareItemStart(), 0);
                                        SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                                        SimpleDateFormat endSdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
                                        try {
                                            startDate = DateUtils.parseDate(startSdf.format(item), "yyyy-MM-dd HH:mm:ss");
                                            endDate = DateUtils.parseDate(endSdf.format(item), "yyyy-MM-dd HH:mm:ss");
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        compareType = WarningCompareTypeEnum.BETWEEN.getCode();
                                    } else {
                                        startDate = workDayService.calculateJumpRestDays(now, warningItemRuleDO.getCompareItemStart(), 0);
                                        compareType = WarningCompareTypeEnum.LESS.getCode();
                                        if (ObjectUtil.isNotEmpty(warningItemRuleDO.getCompareItemEnd())) {
                                            endDate = workDayService.calculateJumpRestDays(now, warningItemRuleDO.getCompareItemEnd(), 0);
                                            compareType = WarningCompareTypeEnum.BETWEEN.getCode();
                                        }
                                    }
                                }
                                if (WarningCompareTypeEnum.BETWEEN.getCode().equals(compareType)) {
                                    SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                                    SimpleDateFormat endSdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
                                    startItem = startDate.before(endDate) ? startDate : endDate;
                                    endItem = startDate.before(endDate) ? endDate : startDate;
                                    try {
                                        startItem = DateUtils.parseDate(startSdf.format(startItem), "yyyy-MM-dd HH:mm:ss");
                                        endItem = DateUtils.parseDate(endSdf.format(endItem), "yyyy-MM-dd HH:mm:ss");

                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    startItem = startDate;
                                }
//                                queryCondition.setField(warningMonitorDO.getBusinessField()).setCalField("CURDATE()").setN(warningItemRuleDO.getCompareItemStart());
                            }
                        }

                        if (WarningCompareTypeEnum.BETWEEN.getCode().equals(compareType)) {
                            queryCondition.setValue(Arrays.asList(startItem, endItem));
                        } else {
                            queryCondition.setValue(startItem);
                        }
                    }

                    // 3.数量

                    // 4.金额

                    // 5.百分比
                }
                queryCondition.setOperator(compareType);
                conditions.add(queryCondition);
            }
            // 根据规则租户筛选对应数据
            QueryCondition queryCondition = new QueryCondition();
            queryCondition.setOperator(WarningCompareTypeEnum.EQUAL.getCode());
            queryCondition.setField("tenant_id").setValue(warningItemRuleDO.getTenantId());
            conditions.add(queryCondition);
            // 符合要求的业务数据
            dataList = removeNoticeData(warningItemRuleDO, dynamicQueryService.dynamicQuery(WarningBusinessEnum.getInstance(businessCode).getFactoryName(), conditions));
        }
        // 如果是监控工作量类型的
        if (WarningMonitorTypeEnum.WORK_FLOW.getCode().equals(monitorDOList.get(0).getType())) {
            // 如果是业务类型且是立即执行类型监听，如果流程key为空，则不执行
            if (WarningCompareTypeEnum.RUN_NOW.getCode().equals(warningItemRuleDO.getCompareType())) {
                // 如果是业务类型且是立即执行类型监听，如果业务id为空，则不执行
                if (ObjectUtil.isNull(taskParams) || StringUtils.isEmpty(taskParams.getProcessInstanceId())) {
                    return dataList;
                }
            }
            compareField = "createTime";
            moniId = "taskId";
            String flowKey = monitorDOList.get(0).getFlowKey();
            String flowStage = monitorDOList.get(0).getFlowStage();

            Date startDate = null;
            Date endDate = null;
            // 1.自然日
            if (WarningUnitTypeEnum.DAY.getCode().equals(warningItemRuleDO.getCompareDataType())) {

                for (WarningMonitorDO warningMonitorDO : monitorDOList) {
                    if (WarningMonitorCompareTypeEnum.BY_CALCULATION.getCode().equals(warningMonitorDO.getCompareType())) {
                        // 如果监控项是计差
                        if (WarningCompareTypeEnum.EQUAL.getCode().equals(warningItemRuleDO.getCompareType())) {
                            Date item = workDayService.calculateJumpRestDays(new Date(), warningItemRuleDO.getCompareItemStart(), 0);
                            SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                            SimpleDateFormat endSdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
                            try {
                                startDate = DateUtils.parseDate(startSdf.format(item), "yyyy-MM-dd HH:mm:ss");
                                endDate = DateUtils.parseDate(endSdf.format(item), "yyyy-MM-dd HH:mm:ss");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            startDate = DateUtils.addDays(new Date(), -warningItemRuleDO.getCompareItemStart());
                            if (ObjectUtil.isNotEmpty(warningItemRuleDO.getCompareItemEnd())) {
                                endDate = DateUtils.addDays(new Date(), -warningItemRuleDO.getCompareItemEnd());
                            }
                        }
                    }
                }
            }

            // 2.工作日
            if (WarningUnitTypeEnum.WORK_DAY.getCode().equals(warningItemRuleDO.getCompareDataType())) {
                for (WarningMonitorDO warningMonitorDO : monitorDOList) {
                    if (WarningMonitorCompareTypeEnum.BY_CALCULATION.getCode().equals(warningMonitorDO.getCompareType())) {
                        // 如果监控项是计差
                        if (WarningCompareTypeEnum.EQUAL.getCode().equals(warningItemRuleDO.getCompareType())) {
                            Date item = workDayService.calculateJumpRestDays(new Date(), warningItemRuleDO.getCompareItemStart(), 0);
                            SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                            SimpleDateFormat endSdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
                            try {
                                startDate = DateUtils.parseDate(startSdf.format(item), "yyyy-MM-dd HH:mm:ss");
                                endDate = DateUtils.parseDate(endSdf.format(item), "yyyy-MM-dd HH:mm:ss");
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        } else {
                            startDate = workDayService.calculateJumpRestDays(new Date(), warningItemRuleDO.getCompareItemStart(), 1);
                            if (ObjectUtil.isNotEmpty(warningItemRuleDO.getCompareItemEnd())) {
                                endDate = workDayService.calculateJumpRestDays(new Date(), warningItemRuleDO.getCompareItemEnd(), 1);
                            }
                        }
                    }

                }
            }
            if (ObjectUtil.isEmpty(taskParams)) {
                taskParams = new TaskForWarningReqDTO();
            }
            if (ObjectUtil.isNotEmpty(endDate)) {
                SimpleDateFormat startSdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
                SimpleDateFormat endSdf = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
                Date startItem = startDate.before(endDate) ? startDate : endDate;
                Date endItem = startDate.before(endDate) ? endDate : startDate;
                try {
                    startItem = DateUtils.parseDate(startSdf.format(startItem), "yyyy-MM-dd HH:mm:ss");
                    endItem = DateUtils.parseDate(endSdf.format(endItem), "yyyy-MM-dd HH:mm:ss");

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                taskParams.setStartDate(startItem).setEndDate(endItem);
            } else {
                taskParams.setStartDate(startDate);
            }
            taskParams.setTenantId(warningItemRuleDO.getTenantId());
            dataList = removeNoticeDataForFlow(warningItemRuleDO, bpmTaskApi.getToDoTaskInfoByDefinitionKeyAndStage(flowKey, taskParams, flowStage));
        }

        // 如果是复杂逻辑类型的
        if (WarningMonitorTypeEnum.COMPLEX_LOGIC.getCode().equals(monitorDOList.get(0).getType())) {
            switch (monitorDOList.get(0).getCompareStr()) {
                case "HTJQ":
                    // 合同拒签
                    noticeRelative(businessId, warningItemRuleDO, dataList);
                    break;
            }
        }
        for (Object data : dataList) {
            try {
                if (compareField.contains("_")) {
                    String[] s = compareField.split("_");
                    compareField = s[0];
                    for (int i = 1; i < s.length; i++) {
                        s[i] = s[i].substring(0, 1).toUpperCase() + s[i].substring(1);
                        compareField += s[i];
                    }
                }
                String s = String.valueOf(getPrivateFieldValue(data, compareField)).trim();
                if (StringUtils.isNotEmpty(s) && !"null".equals(s)){
                    Date date = DateUtils.parseDate(s, "yyyy-MM-dd HH:mm:ss");
                    // 1. 获取当前日期（只包含年月日）
                    LocalDate today = LocalDate.now();
                    // 2. 结合默认时区，转换为当天0点的时间（包含时区信息，避免时区问题）
                    ZonedDateTime zeroZonedTime = today.atStartOfDay(ZoneId.systemDefault());
                    // 3. 转换为时间戳（毫秒数）
                    long zeroTime = zeroZonedTime.toInstant().toEpochMilli();
                    long millisDiff = Math.abs(date.getTime() - zeroTime);
                    nMap.put(getPrivateFieldValue(data, moniId), millisDiff / (24 * 60 * 60 * 1000));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return dataList;
    }

    // 设置消息参数Map<消息配置id, 消息相关参数配置>，   dataList数据， modelId模块来源, flag流程/业务
    private void setDataParams(Map retMap, List dataList, String modelId, String modelCode, String flowKey) {
        // 如果是工作流数据
        if (StringUtils.isNotEmpty(flowKey)) {
            List<ContractProcessInstanceRelationInfoRespDTO> tasks = dataList;
            if (CollectionUtils.isEmpty(tasks)) {
                return;
            }
            List<String> processInstanceIds = tasks.stream().map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId).collect(Collectors.toList());
            QueryCondition queryCondition = new QueryCondition();
            queryCondition.setField("process_instance_id");
            queryCondition.setOperator(WarningCompareTypeEnum.IN.getCode());
            queryCondition.setValue(processInstanceIds);
            List businessList = dynamicQueryService.dynamicQuery(WarningBusinessEnum.getInstance(modelCode).getFactoryName(), Arrays.asList(queryCondition));
            Map processInstanceIdMap = new HashMap();
            for (Object business : businessList) {
                processInstanceIdMap.put(getPrivateFieldValue(business, "processInstanceId"), business);
            }

            List<WarningParamDO> paramDOList = warningParamMapper.selectList(WarningParamDO::getModelId, modelId);

            for (ContractProcessInstanceRelationInfoRespDTO task : tasks) {
                Map map = new HashMap();
                Object o = processInstanceIdMap.get(task.getProcessInstanceId());
                if (ObjectUtil.isNotEmpty(o)) {
                    paramDOList.forEach(item -> {
                        if (StringUtils.isNotEmpty(item.getFieldStr())) {
                            map.put(item.getFieldStr(), getPrivateFieldValue(o, item.getFieldStr()));
                        }
                    });
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    map.put("taskRecvTime", sdf.format(task.getCreateTime()));
                    retMap.put(task.getTaskId(), map);
                    String assignee = task.getAssignee();
                    AdminUserRespDTO user = adminUserApi.getUser(Long.valueOf(assignee));
                    if (ObjectUtil.isEmpty(user)) {
                        Relative relative = relativeMapper.selectOne(Relative::getVirtualId, Long.valueOf(assignee));
                        if (ObjectUtil.isNotEmpty(relative)) {
                            SignatoryRelDO signatoryRelDO = signatoryRelMapper.selectOne(SignatoryRelDO::getContractId, getPrivateFieldValue(o, "id"), SignatoryRelDO::getSignatoryId, relative.getId());
                            if (ObjectUtil.isNotEmpty(signatoryRelDO)) {
                                task.setAssignee(signatoryRelDO.getContactId().toString());
                            }
                        }
                    }

                }
            }
        } else {
            List<WarningParamDO> paramDOList = warningParamMapper.selectList(WarningParamDO::getModelId, modelId);
            for (Object data : dataList) {
                Map map = new HashMap();
                paramDOList.forEach(item -> {
                    if (StringUtils.isNotEmpty(item.getFieldStr())) {
                        map.put(item.getFieldStr(), getPrivateFieldValue(data, item.getFieldStr()));
                    }
                });
                retMap.put(getPrivateFieldValue(data, "id"), map);
            }
            addParams(dataList, retMap);
        }
    }

    private void addParams(List dataList, Map map) {
        List<String> ids = (List) dataList.stream().map(data -> getPrivateFieldValue(data, "id")).collect(Collectors.toList());
        // 看看contractId能否查出值
        Object contractId = getPrivateFieldValue(dataList.get(0), "contractId");
        if (ObjectUtil.isNotEmpty(contractId)) {
            // 如果合同id不为空，证明数据与合同关联
            List contractIds = (List) dataList.stream().map(data -> getPrivateFieldValue(data, "contractId")).collect(Collectors.toList());
            List<ContractDO> contractDOS = contractMapper.selectList(ContractDO::getId, contractIds);
            Map<String, ContractDO> contractDOMap = contractDOS.stream().collect(Collectors.toMap(ContractDO::getId, item -> item));
            for (Object data : dataList) {
                Map cMap = (Map) map.get(getPrivateFieldValue(data, "id"));
                ContractDO contract = contractDOMap.get(getPrivateFieldValue(data, "contractId"));
                if (ObjectUtil.isNotEmpty(contract)) {
                    cMap.put("contractName", contract.getName());
                    cMap.put("contractCode", contract.getCode());
                }
            }
        }

        Object planId = getPrivateFieldValue(dataList.get(0), "planId");
        if (ObjectUtil.isNotEmpty(planId)) {
            // 如果合同id不为空，证明数据与合同关联
            List planIds = (List) dataList.stream().map(data -> getPrivateFieldValue(data, "planId")).collect(Collectors.toList());
            List<PaymentScheduleDO> plans = paymentScheduleMapper.selectList(PaymentScheduleDO::getId, planId);
            Map<String, PaymentScheduleDO> planDOMap = plans.stream().collect(Collectors.toMap(PaymentScheduleDO::getId, item -> item));
            for (Object data : dataList) {
                Map cMap = (Map) map.get(getPrivateFieldValue(data, "id"));
                PaymentScheduleDO paymentScheduleDO = planDOMap.get(getPrivateFieldValue(data, "planId"));
                cMap.put("sort", paymentScheduleDO.getSort());
            }
        }

        Object archiveId = getPrivateFieldValue(dataList.get(0), "archiveId");
        if (ObjectUtil.isNotEmpty(archiveId)) {
            // 如果合同id不为空，证明数据与合同关联
            List archiveIds = (List) dataList.stream().map(data -> getPrivateFieldValue(data, "archiveId")).collect(Collectors.toList());
            List<ContractArchivesDO> archivesDOS = contractArchivesMapper.selectList(ContractArchivesDO::getId, archiveIds);
            Map<String, ContractArchivesDO> planDOMap = archivesDOS.stream().collect(Collectors.toMap(ContractArchivesDO::getId, item -> item));
            for (Object data : dataList) {
                Map cMap = (Map) map.get(getPrivateFieldValue(data, "id"));
                ContractArchivesDO archivesDO = planDOMap.get(getPrivateFieldValue(data, "archiveId"));
                cMap.put("archiveName", archivesDO.getName());
            }
        }
    }

    // 获取字段值
    public static Object getPrivateFieldValue(Object object, String fieldName) {
        if (StringUtils.isEmpty(fieldName)) {
            return null;
        }
        Field field = null; // 获取所有声明的字段（包括私有）
        try {
            Class<?> clazz = object.getClass();
            field = findField(clazz, fieldName);
            if (field == null) {
                return null;
            }
            field.setAccessible(true); // 必须设置为可访问
            Object fieldValue = field.get(object);
            // 处理不同日期类型
            if (fieldValue instanceof Date) {
                return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format((Date) fieldValue);
            } else if (fieldValue instanceof LocalDate) {
                return ((LocalDate) fieldValue).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
            } else if (fieldValue instanceof LocalDateTime) {
                return ((LocalDateTime) fieldValue).format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
            } else {
                return fieldValue;
            }
        } catch (IllegalAccessException | SecurityException e) {
            return null;
        }
    }

    private static Field findField(Class<?> clazz, String fieldName) {
        if (clazz == null) { // 到达 Object 类仍未找到
            return null;
        }
        try {
            // 先查找当前类的字段（包括私有）
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            // 当前类未找到，递归查找父类
            return findField(clazz.getSuperclass(), fieldName);
        }
    }

    // 发送消息
    private void sendMessage(List<Long> userIds, String messageTemplate, Map params, Map warningMap, Map<Long, Long> tenantMap) {
        for (Long userId : userIds) {
            notifySendService.sendSingleNotifyForWarning(userId, tenantMap.get(userId), UserTypeEnum.ADMIN.getValue(), messageTemplate, params, warningMap);
        }
    }


    private List removeNoticeDataForFlow(WarningItemRuleDO warningItemRuleDO, List dataList) {
        if (CollectionUtil.isEmpty(dataList)) {
            return new ArrayList();
        }
        if (WarningNoticeTimesTypeEnum.ONCE.getCode().equals(warningItemRuleDO.getNoticeTimesType())) {
            List taskIds = (List) dataList.stream().map(data -> getPrivateFieldValue(data, "taskId")).collect(Collectors.toList());
            // 查询出已经通知过的数据
            List<NotifyMessageDO> notifyMessageDOS = notifyMessageMapper.selectList(new LambdaQueryWrapperX<NotifyMessageDO>().in(NotifyMessageDO::getTaskId, taskIds).eq(NotifyMessageDO::getRuleId, warningItemRuleDO.getId()));
            List<String> existIds = notifyMessageDOS.stream().map(NotifyMessageDO::getTaskId).collect(Collectors.toList());
            return (List) dataList.stream().filter(data -> !existIds.contains(getPrivateFieldValue(data, "taskId"))).collect(Collectors.toList());
        }
        if (WarningNoticeTimesTypeEnum.DIY_DAY.getCode().equals(warningItemRuleDO.getNoticeTimesType())) {
            List taskIds = (List) dataList.stream().map(data -> getPrivateFieldValue(data, "taskId")).collect(Collectors.toList());
            // 查询出已经通知过的数据
            List<NotifyMessageDO> notifyMessageDOS = notifyMessageMapper.selectList(new LambdaQueryWrapperX<NotifyMessageDO>().inIfPresent(NotifyMessageDO::getTaskId, taskIds).eq(NotifyMessageDO::getRuleId, warningItemRuleDO.getId()).apply("ABS(DATEDIFF(create_time, CURDATE())) < {0}", warningItemRuleDO.getNoticeTimesDay()));
            List<String> existIds = notifyMessageDOS.stream().map(NotifyMessageDO::getTaskId).collect(Collectors.toList());
            return (List) dataList.stream().filter(data -> !existIds.contains(getPrivateFieldValue(data, "taskId"))).collect(Collectors.toList());
        }
        return dataList;
    }

    private List removeNoticeData(WarningItemRuleDO warningItemRuleDO, List dataList) {
        List ids = (List) dataList.stream().map(data -> getPrivateFieldValue(data, "id")).collect(Collectors.toList());
        if (WarningNoticeTimesTypeEnum.ONCE.getCode().equals(warningItemRuleDO.getNoticeTimesType())) {
            // 查询出已经通知过的数据
            List<NotifyMessageDO> notifyMessageDOS = notifyMessageMapper.selectList(new LambdaQueryWrapperX<NotifyMessageDO>().inIfPresent(NotifyMessageDO::getBusinessId, ids).eq(NotifyMessageDO::getRuleId, warningItemRuleDO.getId()));
            List<String> existIds = notifyMessageDOS.stream().map(NotifyMessageDO::getBusinessId).collect(Collectors.toList());
            return (List) dataList.stream().filter(data -> !existIds.contains(String.valueOf(getPrivateFieldValue(data, "id")))).collect(Collectors.toList());
        }
        if (WarningNoticeTimesTypeEnum.DIY_DAY.getCode().equals(warningItemRuleDO.getNoticeTimesType())) {
            // 查询出已经通知过的数据
            List<NotifyMessageDO> notifyMessageDOS = notifyMessageMapper.selectList(new LambdaQueryWrapperX<NotifyMessageDO>().inIfPresent(NotifyMessageDO::getBusinessId, ids).eq(NotifyMessageDO::getRuleId, warningItemRuleDO.getId()).apply("ABS(DATEDIFF(create_time, CURDATE())) < {0}", warningItemRuleDO.getNoticeTimesDay()));
            List<String> existIds = notifyMessageDOS.stream().map(NotifyMessageDO::getBusinessId).distinct().collect(Collectors.toList());
            return (List) dataList.stream().filter(data -> !existIds.contains(String.valueOf(getPrivateFieldValue(data, "id")))).collect(Collectors.toList());
        }
        return dataList;
    }

    private void noticeRelative(String contractId, WarningItemRuleDO warningItemRuleDO, List dataList) {
        DataPermissionUtils.executeIgnore(() -> {
            TenantUtils.executeIgnore(() -> {
                Map<String, Object> templateParams = new HashMap<>();
                ContractDO contractDO = contractMapper.selectOne(ContractDO::getId, contractId);
                if (ObjectUtil.isEmpty(contractDO)) {
                    return;
                }
                dataList.add(contractDO);
                List<RelativeContact> relativeContacts = relativeContactMapper.selectList(RelativeContact::getUserId, getLoginUser().getId());
                String relativeId = "";
                //如果相对方与用户关系列表查不到该用户所属相对方，就查相对方表
                if (CollectionUtil.isEmpty(relativeContacts)) {
                    List<Relative> relatives = relativeMapper.selectList(Relative::getContactId, getLoginUser().getId());
                    if (CollectionUtil.isNotEmpty(relatives)) {
                        templateParams.put("relativeName", relatives.get(0).getName());
                        relativeId = relatives.get(0).getId();
                    }
                } else {
                    templateParams.put("relativeName", relativeContacts.get(0).getName());
                    relativeId = relativeContacts.get(0).getRelativeId();
                }
                // 更新 relative_signatory_rel 表中 relative_signatory_rel.is_sign = 1
                if (ObjectUtil.isNotEmpty(contractDO)) {
                    templateParams.put("contractName", contractDO.getName());

                    List<SignatoryRelDO> signatoryRelDOS = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractDO.getId());
                    for (SignatoryRelDO signatoryRelDO : signatoryRelDOS) {
                        if (signatoryRelDO.getContactId() == null || signatoryRelDO.getContactId() == 0){
                            Relative relative = relativeMapper.selectById(signatoryRelDO.getSignatoryId());
                            List<Long> contactUsers = relativeMapper.selectList(Relative::getId, signatoryRelDO.getSignatoryId()).stream().map(Relative::getContactId).collect(Collectors.toList());
                            if (CollectionUtil.isNotEmpty(contactUsers)){
                                signatoryRelDO.setContactId(contactUsers.get(0));
                            }
                        }
                    }
                    List<Long> users = signatoryRelDOS.stream().filter(item -> ObjectUtil.isNotEmpty(item.getContactId())).map(SignatoryRelDO::getContactId).collect(Collectors.toList());
                    users.remove(getLoginUser().getId());
                    List<AdminUserRespDTO> userList = adminUserApi.getUserList(users);
                    Map<Long, Long> tenantIdMap = userList.stream().collect(Collectors.toMap(AdminUserRespDTO::getId, AdminUserRespDTO::getTenantId));
                    // 通知其他相对方
                    // 组装预警相关数据信息
                    Map warningMap = new HashMap();
                    warningMap.put("businessId", contractDO.getId());
                    warningMap.put("ruleId", warningItemRuleDO.getId());
                    WarningItemDO warningItemDO = warningItemMapper.selectById(warningItemRuleDO.getWarningItemId());
                    WarningCfgDO warningCfgDO = warningCfgMapper.selectById(warningItemDO.getConfigId());

                    warningMap.put("modelName", warningCfgDO.getModelName());
                    warningMap.put("warnName", warningCfgDO.getName());
                    warningMap.put("itemName", warningItemDO.getItemName());
                    NotifyTemplateDO template = notifyTemplateService.getNotifyTemplateByCodeFromCache(WarningRulesNotifyTemplateEnums.RELATIVE_REJECT_REMINDER.getCode());

                    users.stream().distinct().collect(Collectors.toList()).forEach(item -> {
                        notifySendService.sendSingleNotifyForWarning(item, tenantIdMap.get(item), UserTypeEnum.ADMIN.getValue(), template.getContent(), templateParams, warningMap);
                    });
                    // 通知合同发起方
                    notifySendService.sendSingleNotifyForWarning(Long.valueOf(contractDO.getCreator()), contractDO.getTenantId(), UserTypeEnum.ADMIN.getValue(), template.getContent(), templateParams, warningMap);
                }
            });
        });
    }
}
