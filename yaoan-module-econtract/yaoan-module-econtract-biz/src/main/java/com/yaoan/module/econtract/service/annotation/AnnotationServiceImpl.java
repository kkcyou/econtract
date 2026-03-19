package com.yaoan.module.econtract.service.annotation;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.datapermission.core.annotation.DataPermission;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.BpmTaskRespDTO;
import com.yaoan.module.bpm.enums.model.FlowableModelEnums;
import com.yaoan.module.econtract.controller.admin.annotation.vo.AnnotationListRespVO;
import com.yaoan.module.econtract.controller.admin.annotation.vo.AnnotationSaveUpdateBatchReqVO;
import com.yaoan.module.econtract.controller.admin.annotation.vo.AnnotationSaveUpdateReqVO;
import com.yaoan.module.econtract.controller.admin.annotation.vo.BigAnnotationListRespVO;
import com.yaoan.module.econtract.controller.admin.model.vo.IdReqVO;
import com.yaoan.module.econtract.convert.annotation.AnnotationConverter;
import com.yaoan.module.econtract.dal.dataobject.annotation.AnnotationDO;
import com.yaoan.module.econtract.dal.dataobject.bpm.contract.BpmContract;
import com.yaoan.module.econtract.dal.mysql.annotation.AnnotationMapper;
import com.yaoan.module.econtract.dal.mysql.annotation.AnnotationUserRelMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.contract.BpmContractMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.contractborrow.ContractBorrowBpmMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.model.ModelBpmMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.register.BpmContractRegisterMapper;
import com.yaoan.module.econtract.dal.mysql.bpm.template.TemplateBpmMapper;
import com.yaoan.module.econtract.dal.mysql.change.BpmContractChangeMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.PaymentScheduleMapper;
import com.yaoan.module.econtract.dal.mysql.contracttemplate.ContractTemplateMapper;
import com.yaoan.module.econtract.dal.mysql.model.SimpleModelMapper;
import com.yaoan.module.econtract.dal.mysql.payment.paymentapplication.PaymentApplicationMapper;
import com.yaoan.module.econtract.dal.mysql.term.TermMapper;
import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import com.yaoan.module.econtract.enums.common.IfEnums;
import com.yaoan.module.system.api.config.SystemConfigApi;
import com.yaoan.module.system.api.dept.PostApi;
import com.yaoan.module.system.api.dept.dto.PostDTO;
import com.yaoan.module.system.api.permission.RoleApi;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import com.yaoan.module.system.enums.config.AnnotationScanPermissionEnums;
import com.yaoan.module.system.enums.config.SystemConfigValueEnums;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.module.system.enums.config.AnnotationScanPermissionEnums.APPROVER_SCAN;
import static com.yaoan.module.system.enums.config.AnnotationScanPermissionEnums.CARBON_COPY_SCAN;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/25 14:39
 */
@Slf4j
@Service
public class AnnotationServiceImpl implements AnnotationService {
    @Resource
    private AnnotationMapper annotationMapper;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private RoleApi roleApi;
    @Resource
    private BpmTaskApi taskApi;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;
    @Resource
    private BpmContractRegisterMapper bpmContractRegisterMapper;
    @Resource
    private SimpleModelMapper simpleModelMapper;
    @Resource
    private ModelBpmMapper modelBpmMapper;
    @Resource
    private PaymentApplicationMapper paymentApplicationMapper;
    @Resource
    private TermMapper termMapper;
    @Resource
    private ContractTemplateMapper contractTemplateMapper;
    @Resource
    private TemplateBpmMapper templateBpmMapper;
    @Resource
    private BpmContractChangeMapper bpmContractChangeMapper;
    @Resource
    private BpmContractMapper bpmContractMapper;
    @Resource
    private ContractBorrowBpmMapper contractBorrowBpmMapper;
    @Resource
    private PaymentScheduleMapper paymentScheduleMapper;
    @Resource
    private AnnotationUserRelMapper annotationUserRelMapper;
    @Resource
    private SystemConfigApi systemConfigApi;
    @Resource
    private PostApi postApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String save(AnnotationSaveUpdateReqVO vo) {
        //获取当前节点
        BpmTaskRespDTO taskRespDTO = taskApi.getTaskRespDTO(vo.getTaskId());
        AnnotationDO entity = AnnotationConverter.INSTANCE.annoSaveUpdateReq2Entity(vo);
        if (ObjectUtil.isNotNull(taskRespDTO)) {
            //找到当前审批节点
            FlowableModelEnums modelEnums = FlowableModelEnums.getInstanceByName(taskRespDTO.getName());
            if (ObjectUtil.isNotNull(modelEnums)) {
                entity.setBusinessId(vo.getBusinessId());
                entity.setBusinessType(vo.getBusinessType());
                entity.setApproveIndex(modelEnums.getIndex());
                annotationMapper.insert(entity);
            }
        }

        return entity.getId();
    }

    /**
     * 获得上一个节点的审批人信息(从哪个节点退回的)
     */
    private List<String> getLastApproverIds(AnnotationSaveUpdateReqVO vo) {
        List<String> result = new ArrayList<String>();
        String businessId = vo.getBusinessId();
        String definitionKey = vo.getBusinessType();
        ActivityConfigurationEnum configurationEnum = ActivityConfigurationEnum.getInstanceByDefKey(definitionKey);
        if (ObjectUtil.isNull(configurationEnum)) {
            return Collections.emptyList();
        }
        //获得上一个节点的审批人信息(比如：高级审批人获得初级审批人的信息)
        switch (configurationEnum) {
            case CONTRACT_DRAFT_APPROVE:
                BpmContract bpmDO = bpmContractMapper.selectOne(BpmContract::getContractId, businessId);
                if (ObjectUtil.isNotNull(bpmDO)) {
                    String processInstanceId = bpmDO.getProcessInstanceId();
                    String processDefinitionId = taskApi.getProcessDefinitionIdByProcessInstanceId(processInstanceId);
                    BpmTaskRespDTO bpmTaskRespDTO = taskApi.getTaskRespDTO(vo.getTaskId());
                    if (ObjectUtil.isNotNull(bpmTaskRespDTO)) {
                        Set<Long> userIds = taskApi.getLastApproverInfos(processDefinitionId, processInstanceId, bpmTaskRespDTO.getName(), Long.valueOf(bpmDO.getCreator()));
                        if (CollectionUtil.isNotEmpty(userIds)) {
                            Set<String> userIdsAsString = userIds.stream().map(String::valueOf).collect(Collectors.toSet());
                            result = new ArrayList<>(userIdsAsString);
                        }
                    }
                }
                return result;
            default:
                return result;

        }
    }

    @Override
    public String update(AnnotationSaveUpdateReqVO vo) {
        AnnotationDO entity = AnnotationConverter.INSTANCE.annoSaveUpdateReq2Entity(vo);
        annotationMapper.updateById(entity);
        return "success";
    }

    /**
     * 按照审批节点分组，组内数据按照时间升序
     */
    @Override
    @DataPermission(enable = false)
    public List<BigAnnotationListRespVO> getAnnotationByFileId(IdReqVO vo) {
        List<Integer> indexList = new ArrayList<Integer>();

        if (StringUtils.isNotBlank(vo.getTaskId())) {
            //获取当前节点
            BpmTaskRespDTO taskRespDTO = taskApi.getTaskRespDTO(vo.getTaskId());
            if (ObjectUtil.isNotNull(taskRespDTO)) {
                FlowableModelEnums modelEnums = FlowableModelEnums.getInstanceByName(taskRespDTO.getName());
                if (ObjectUtil.isNotNull(modelEnums)) {
                    Integer index = modelEnums.getIndex();
                    //加多了，也不会查错，因为不会存在超过的
                    Integer nextIndex = index + 1;
                    indexList.add(index);
                    indexList.add(nextIndex);
                }
            }
        }
        List<AnnotationDO> annotationList = new ArrayList<AnnotationDO>();
        List<BigAnnotationListRespVO> bigRespVOS = new ArrayList<BigAnnotationListRespVO>();
        List<AnnotationListRespVO> respVOS = new ArrayList<AnnotationListRespVO>();
        Long fileId = Long.valueOf(vo.getId());
        String configValue = "";
        AnnotationScanPermissionEnums enums = AnnotationScanPermissionEnums.getInstance(vo.getFlag());
        if (ObjectUtil.isNull(enums)) {
            return Collections.emptyList();
        }
        //审批人角色
        if (APPROVER_SCAN == enums) {
            configValue = systemConfigApi.getPermissionForApproveScanAnnotations();
            // 审批人的浏览批注的权限：自己和下一个节点审批人
            if (SystemConfigValueEnums.APPROVER_PERMISSION_SCAN_ANNOTATION_LIMITED.getValue().equals(configValue)) {
                annotationList = annotationMapper.getAnnotationsForApprover(fileId, indexList);
            }
        }
        //抄送人角色
        if (CARBON_COPY_SCAN == enums) {
            configValue = systemConfigApi.getPermissionForCopyScanAnnotations();
            // 全部不可见
            if (IfEnums.NO.getCode().equals(configValue)) {
                return Collections.emptyList();
            }
        }
        //全部可见
        if (SystemConfigValueEnums.APPROVER_PERMISSION_SCAN_ANNOTATION_ALL.getValue().equals(configValue) || IfEnums.YES.equals(configValue)) {
            annotationList = annotationMapper.getAnnotationByFileId(fileId);
        }

        if (CollectionUtil.isNotEmpty(annotationList)) {
            respVOS = AnnotationConverter.INSTANCE.listEntity2RespVO(annotationList);
            return enhanceList(respVOS);
        }

        return Collections.emptyList();
    }

    private List<BigAnnotationListRespVO> enhanceList(List<AnnotationListRespVO> respVOS) {
        List<BigAnnotationListRespVO> result = new ArrayList<BigAnnotationListRespVO>();
        List<Long> userIds = respVOS.stream().map(AnnotationListRespVO::getCreator).collect(Collectors.toList());
        //岗位信息
        List<PostDTO> postDTOList = new ArrayList<PostDTO>();
        Map<Long, PostDTO> postDTOMap = new HashMap<Long, PostDTO>();
        postDTOList = postApi.getPostByUserIds(userIds);

        Map<Long, List<PostDTO>> userPostDTOMap = new HashMap<Long, List<PostDTO>>();
        userPostDTOMap = postApi.getUserPostRelMap(userIds);

        if (CollectionUtil.isNotEmpty(postDTOList)) {
            postDTOMap = CollectionUtils.convertMap(postDTOList, PostDTO::getId);
        }
        //用户信息
        if (CollectionUtil.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        Map<Long, AdminUserRespDTO> userRespDTOMap = adminUserApi.getUserRoleInfos(userIds);

        for (AnnotationListRespVO respVO : respVOS) {
            //批注者姓名
            AdminUserRespDTO userRespDTO = userRespDTOMap.get(respVO.getCreator());
            if (ObjectUtil.isNotNull(userRespDTO)) {
                respVO.setCreatorName(userRespDTO.getNickname());
                respVO.setRoleId(userRespDTO.getRoleId());
                respVO.setCreatorRoleName(userRespDTO.getRoleName());


                List<PostDTO> postList = userPostDTOMap.get(respVO.getCreator());
                if (CollectionUtil.isNotEmpty(postList)) {
                    PostDTO postDTO = postDTOList.get(0);
                    respVO.setCreatorPostName(postDTO.getName());
                }
            }
        }
        // 按 creatorRoleName 分组成 Map<String, List<AnnotationListRespVO>>
        Map<String, List<AnnotationListRespVO>> mapByRoleName = respVOS.stream()
                .collect(Collectors.groupingBy(AnnotationListRespVO::getCreatorRoleName));

        for (Map.Entry<String, List<AnnotationListRespVO>> listEntry : mapByRoleName.entrySet()) {
            List<AnnotationListRespVO> list = listEntry.getValue();
            BigAnnotationListRespVO bigAnnotationListRespVO = new BigAnnotationListRespVO();
            bigAnnotationListRespVO.setCreatorRoleName(list.get(0).getCreatorRoleName());
            bigAnnotationListRespVO.setRespVOS(list);
            result.add(bigAnnotationListRespVO);
        }

        return result;
    }

    @Override
    public String delete(IdReqVO vo) {
        annotationMapper.deleteBatchIds(vo.getIdList());
        return "success";
    }

    @Override
    public String checkAnnotationsByFileId(IdReqVO vo) {
        String result = IfEnums.YES.getCode();
        Long fileId = Long.valueOf(vo.getId());
        Long count = annotationMapper.checkAnnotationsByFileId(fileId);
        if (0 < count) {
            result = IfEnums.NO.getCode();
        }
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String saveBatch(AnnotationSaveUpdateBatchReqVO vo) {

        //先将该文件的批注都清空
//        try {
//            annotationMapper.delete(new LambdaQueryWrapperX<AnnotationDO>().eqIfPresent(AnnotationDO::getBusinessId, vo.getBusinessId()));
//        } catch (Exception e) {
//            log.error("清空相关批注时，发生异常");
//        }

        //获取当前节点
        BpmTaskRespDTO taskRespDTO = taskApi.getTaskRespDTO(vo.getTaskId());
        if (ObjectUtil.isNotNull(taskRespDTO)) {
            //找到当前审批节点
            FlowableModelEnums modelEnums = FlowableModelEnums.getInstanceByName(taskRespDTO.getName());
            if (ObjectUtil.isNotNull(modelEnums)) {
                List<AnnotationDO> doList = AnnotationConverter.INSTANCE.listAnnoSaveUpdateReq2Entity(vo.getReqVOS());
                for (AnnotationDO annotationDO : doList) {
                    annotationDO.setBusinessId(vo.getBusinessId());
                    annotationDO.setBusinessType(vo.getBusinessType());
                    annotationDO.setApproveIndex(modelEnums.getIndex());
                }
                annotationMapper.insertBatch(doList);
            }
        }

        return "success";
    }


}
