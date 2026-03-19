package com.yaoan.module.econtract.service.bpm.saas.company;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.redis.core.RedisUtils;
import com.yaoan.framework.security.core.LoginUser;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.yaoan.module.bpm.api.task.dto.ContractProcessInstanceRelationInfoRespDTO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.bpm.saas.company.BpmCompanySubmitReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.saas.company.vo.BpmCompanyPageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.saas.company.vo.BpmCompanyPageRespVO;
import com.yaoan.module.econtract.convert.saas.company.CompanyBpmConvert;
import com.yaoan.module.econtract.dal.dataobject.bpm.saas.company.CompanyBpmDO;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.dataobject.relativeContact.RelativeContact;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.relativeContact.RelativeContactMapper;
import com.yaoan.module.econtract.dal.mysql.saas.company.CompanyBpmMapper;
import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import com.yaoan.module.econtract.enums.StatusConstants;
import com.yaoan.module.econtract.enums.saas.InviteMethodEnums;
import com.yaoan.module.econtract.enums.saas.RealNameEnums;
import com.yaoan.module.system.api.dept.CompanyApi;
import com.yaoan.module.system.api.dept.dto.CompanyRespDTO;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.dal.dataobject.user.AdminUserDO;
import com.yaoan.module.system.service.notify.NotifySendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DATA_ERROR;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DIY_ERROR;
import static com.yaoan.module.econtract.enums.WarningRulesNotifyTemplateEnums.*;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-28 16:29
 */
@Service
public class BpmCompanyServiceImpl implements BpmCompanyService {
    @Resource
    private CompanyBpmMapper companyBpmMapper;
    @Resource
    private CompanyApi companyApi;
    @Resource
    private BpmProcessInstanceApi processInstanceApi;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private RelativeMapper relativeMapper;
    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private RelativeContactMapper relativeContactMapper;
    @Resource
    private AdminUserApi adminUserApi;
@Resource
private NotifySendService notifySendService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String submit(BpmCompanySubmitReqVO reqVO) {
        CompanyRespDTO companyRespDTO = companyApi.getCompany4CreditCode(reqVO.getCreditCode());
        if (ObjectUtil.isEmpty(companyRespDTO)) {
            throw exception(DATA_ERROR);
        }

        // 注册后直接加入，前端会传用户id
        Long loginUserId = reqVO.getUserId();
        if(ObjectUtil.isNull(loginUserId)){
            // 登录后加入企业，则前端不传用户id
            loginUserId = SecurityFrameworkUtils.getLoginUserId();
        }
        AdminUserRespDTO adminUserRespDTO = adminUserApi.getUser(loginUserId);
        if(ObjectUtil.isNull(adminUserRespDTO)) {
            throw exception(DATA_ERROR);
        }

        //校验是否重复加入
        checkIfRepeatJoin(loginUserId, companyRespDTO);
        //校验是否有在途申请
        checkIfRepeatApply(loginUserId,companyRespDTO);
        // 保存业务数据
        Long companyId = companyRespDTO.getId();
        CompanyRespDTO dto = companyApi.getOneById(companyId);
        CompanyBpmDO companyBpmDO = CompanyBpmConvert.INSTANCE.dto2Do(dto);
        companyBpmDO.setCompanyId(companyId);
        companyBpmDO.setResult(BpmProcessInstanceResultEnum.PROCESS.getResult());
        companyBpmDO.setReason(reqVO.getReason());
        companyBpmDO.setSubmitterName(adminUserRespDTO.getNickname());
        companyBpmDO.setUserIdCard(adminUserRespDTO.getIdCard());
        companyBpmDO.setRealName(adminUserRespDTO.getRealName());
        companyBpmDO.setInviteMethod(adminUserRespDTO.getInviteMethod());
        companyBpmDO.setMobile(adminUserRespDTO.getMobile());
        companyBpmDO.setCreator(String.valueOf(loginUserId));
        companyBpmDO.setUpdater(String.valueOf(loginUserId));
        companyBpmMapper.insert(companyBpmDO);
        String bpmDOId = companyBpmDO.getId();
        // 发起工作流
        Map<String, Object> processInstanceVariables = new HashMap<String, Object>(16);
        processInstanceVariables.put("companyId", companyId);
        // 2.2 流程实例id
        String processInstanceId =
                processInstanceApi.createProcessInstance(
                        loginUserId,
                        new BpmProcessInstanceCreateReqDTO()
                                .setProcessDefinitionKey(ActivityConfigurationEnum.SAAS_COMPANY_JOIN.getDefinitionKey())
                                .setVariables(processInstanceVariables)
                                .setBusinessKey(companyBpmDO.getId()));

        companyBpmMapper.updateById(new CompanyBpmDO().setId(bpmDOId).setProcessInstanceId(processInstanceId));

        // 站内信
        Map<String, Object> templateParams = new HashMap<>();
        templateParams.put("companyName", companyBpmDO.getCompanyName());
        notifySendService.sendSingleNotifyToAdmin(Long.valueOf(companyBpmDO.getCreator()), SAAS_COMPANY_JOIN_APPLICATION_SUBMITTED_REMINDER.getCode(), templateParams);

        return bpmDOId;
    }

    private void checkIfRepeatApply(Long loginUserId, CompanyRespDTO companyRespDTO) {
       Long count = companyBpmMapper.selectCount(new LambdaQueryWrapperX<CompanyBpmDO>()
               .eq(CompanyBpmDO::getCompanyCreditNo, companyRespDTO.getCreditCode())
               .eq(CompanyBpmDO::getCreator, String.valueOf(loginUserId))
               .eq(CompanyBpmDO::getResult, BpmProcessInstanceResultEnum.PROCESS.getResult())
       );
        if(0L != count) {
            throw exception(DIY_ERROR,"已发起的申请还在审批中，请等候管理员操作。");
        }
    }

    private void checkIfRepeatJoin(Long loginUserId, CompanyRespDTO companyRespDTO) {
        Long count = relativeContactMapper.selectCount(
                new LambdaQueryWrapperX<RelativeContact>()
                        .eq(RelativeContact::getUserId, loginUserId)
                        .eq(RelativeContact::getCompanyId, companyRespDTO.getId()));
        if (0 != count) {
            throw exception(DIY_ERROR, "当前用户已加入该企业，不可重复加入。");
        }
    }

    @Override
    public PageResult<BpmCompanyPageRespVO> page(BpmCompanyPageReqVO reqVO) {
        PageResult<BpmCompanyPageRespVO> result = new PageResult<BpmCompanyPageRespVO>();
        result.setTotal(0L);
//        String acheKey = SecurityFrameworkUtils.getLoginUserKey4Space();
//        String relativeId = redisUtils.get(acheKey) + "";
//        Relative relative = relativeMapper.selectById(relativeId);
//        if (ObjectUtil.isEmpty(relative)) {
//            return result;
//        }
//        Long virtualId = relative.getVirtualId();
        // 流程实例
        enhanceFlow(reqVO);

        PageResult<CompanyBpmDO> bpmDOPageResult = companyBpmMapper.selectPage(reqVO);
        return enhancePage(bpmDOPageResult, reqVO);
    }

    private PageResult<BpmCompanyPageRespVO> enhancePage(PageResult<CompanyBpmDO> bpmDOPageResult, BpmCompanyPageReqVO reqVO) {
        if (CollectionUtil.isEmpty(bpmDOPageResult.getList()) || CollectionUtil.isEmpty(reqVO.getInstanceIdList())) {
            return new PageResult<BpmCompanyPageRespVO>().setTotal(0L);
        }
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = reqVO.getInstanceRelationInfoRespDTOMap();
        PageResult<BpmCompanyPageRespVO> result = CompanyBpmConvert.INSTANCE.pageDo2Resp(bpmDOPageResult);
        for (BpmCompanyPageRespVO respVO : result.getList()) {
            ContractProcessInstanceRelationInfoRespDTO relationInfoRespDTO = instanceRelationInfoRespDTOMap.get(respVO.getProcessInstanceId());
            if (ObjectUtil.isNotNull(relationInfoRespDTO)) {
                respVO.setTaskId(relationInfoRespDTO.getTaskId());
            }
            BpmProcessInstanceResultEnum resultEnum = BpmProcessInstanceResultEnum.getInstance(respVO.getResult());
            if (ObjectUtil.isNotNull(resultEnum)) {
                respVO.setResultName(resultEnum.getDesc());
                // 审核状态与列表一致
                if(BpmProcessInstanceResultEnum.REJECT == resultEnum) {
                    respVO.setResultName("已拒绝");
                }
            }

            RealNameEnums realNameEnums = RealNameEnums.getInstance(respVO.getRealName());
            respVO.setRealNameStr(realNameEnums == null ? "" : realNameEnums.getInfo());

            InviteMethodEnums inviteMethodEnums = InviteMethodEnums.getInstance(respVO.getInviteMethod());
            respVO.setInviteMethodStr(inviteMethodEnums == null ? "" : inviteMethodEnums.getInfo());
        }
        return result;
    }

    private void enhanceFlow( BpmCompanyPageReqVO reqVO) {
        List<ContractProcessInstanceRelationInfoRespDTO> processInstanceRelationInfoRespDTOList = new ArrayList<>();
        Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap = new HashMap<>();
        List<String> instanceIdList = new ArrayList<>();
        switch (reqVO.getFlag()) {
            // to do
            case 0:
                processInstanceRelationInfoRespDTOList = bpmTaskApi.getToDoTaskInfoByDefinitionKey4Saas(ActivityConfigurationEnum.SAAS_COMPANY_JOIN.getDefinitionKey());
                break;

            // done
            case 1:
                Integer taskResult = StatusConstants.TEMP_INTEGER;
                // 查询待办任务
                if (ObjectUtil.isNotNull(reqVO.getTaskResult())) {
                    taskResult = reqVO.getTaskResult();
                }
                //获得已处理任务数据(已过滤掉已取消的任务)
                processInstanceRelationInfoRespDTOList = bpmTaskApi.getDoneTaskInfoByDefinitionKeyAndResult4Saas(ActivityConfigurationEnum.SAAS_COMPANY_JOIN.getDefinitionKey(), taskResult);
                break;

            // all
            case 2:
                // 查询所有任务(已过滤掉已取消的任务)
                processInstanceRelationInfoRespDTOList = bpmTaskApi.getAllTaskInfoByDefinitionKey4Saas(ActivityConfigurationEnum.SAAS_COMPANY_JOIN.getDefinitionKey());
                break;

        }
        instanceRelationInfoRespDTOMap = CollectionUtils.convertMap(processInstanceRelationInfoRespDTOList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId);
        // 获得 ProcessInstance
        instanceIdList = processInstanceRelationInfoRespDTOList.stream()
                .map(ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId)
                .distinct()
                .collect(Collectors.toList());
        reqVO.setInstanceIdList(instanceIdList);
        reqVO.setInstanceRelationInfoRespDTOMap(instanceRelationInfoRespDTOMap);
    }


}
