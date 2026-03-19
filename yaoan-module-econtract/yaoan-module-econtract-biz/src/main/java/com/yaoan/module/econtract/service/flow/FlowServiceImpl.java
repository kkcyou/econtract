package com.yaoan.module.econtract.service.flow;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import com.alibaba.fastjson.JSON;
import com.yaoan.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.framework.web.core.util.WebFrameworkUtils;
import com.yaoan.module.bpm.api.task.BpmProcessInstanceApi;
import com.yaoan.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.yaoan.module.bpm.enums.model.FlowableModelEnums;
import com.yaoan.module.bpm.enums.model.HLJFlowableModelEnums;
import com.yaoan.module.econtract.api.purchasing.IProjectPurchasingApi;
import com.yaoan.module.econtract.api.purchasing.dto.ReqIdsDTO;
import com.yaoan.module.econtract.controller.admin.contract.vo.ProjectPurchasingRespVO;
import com.yaoan.module.econtract.dal.dataobject.contract.ContractDO;
import com.yaoan.module.econtract.dal.dataobject.contract.SignatoryRelDO;
import com.yaoan.module.econtract.dal.dataobject.contract.ext.gcy.ContractOrderExtDO;
import com.yaoan.module.econtract.dal.dataobject.relative.Relative;
import com.yaoan.module.econtract.dal.mysql.contract.ContractMapper;
import com.yaoan.module.econtract.dal.mysql.contract.ext.gcy.ContractOrderExtMapper;
import com.yaoan.module.econtract.dal.mysql.relative.RelativeMapper;
import com.yaoan.module.econtract.dal.mysql.signatoryrel.SignatoryRelMapper;
import com.yaoan.module.econtract.enums.ActivityConfigurationEnum;
import com.yaoan.module.econtract.enums.ErrorCodeConstants;
import com.yaoan.module.econtract.enums.common.IfNumEnums;
import com.yaoan.module.econtract.enums.gcy.gpmall.ContractDrafterEnums;
import com.yaoan.module.econtract.enums.neimeng.PlatformEnums;
import com.yaoan.module.system.api.dept.CompanyApi;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.bpm.enums.model.FlowableModelEnums.*;
import static com.yaoan.module.econtract.enums.ActivityConfigurationEnum.ECMS_CONTRACT_RELATIVES;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.DATA_ERROR;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.SYSTEM_ERROR;

/**
 * <p>
 * flow 服务实现类
 * </p>
 *
 * @author doujl
 * @since 2023-07-04
 */
@Service
public class FlowServiceImpl implements FlowService {

    @Resource
    private SignatoryRelMapper signatoryRelMapper;
    @Resource
    private RelativeMapper relativeMapper;
    @Resource
    private ExecutorFactory executorFactory;
    @Resource
    private CompanyApi companyApi;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;
    /**
     * 虚拟系统对接api
     */
    @Resource
    private IProjectPurchasingApi projectPurchasingApi;

    @Override
    public String createContractProcessInstance(String contractId) {

        List<Long> userIds = this.buildTaskUserIds(contractId);
        //创建流程
        return confirmSignExecute(userIds, contractId);
    }

    private String confirmSignExecute(List<Long> userIds, String contractId) {
        if (CollectionUtil.isEmpty(userIds)) {
            throw exception(SYSTEM_ERROR, "确认签章的相对方不存在。");
        }

        BpmProcessInstanceCreateReqDTO bpmProcessInstanceCreateReqDTO = new BpmProcessInstanceCreateReqDTO();
        ActivityConfigurationEnum activityConfigurationEnum = null;
        Map<String, List<Long>> startUserSelectAssignees = new HashMap<String, List<Long>>();
        List<Long> ids1 = new ArrayList<Long>();
        List<Long> ids2 = new ArrayList<Long>();
        List<Long> ids3 = new ArrayList<Long>();
        List<Long> ids4 = new ArrayList<Long>();
        List<Long> ids5 = new ArrayList<Long>();
        List<Long> ids6 = new ArrayList<Long>();
        activityConfigurationEnum = ECMS_CONTRACT_RELATIVES;
        //因为之前就是放的就是先1后0，所以在这矫正顺序
        //userIds顺序为 采购人-供应商-采购人-供应商
        //此处发送相对方第一节点应为供应商，故get(1)
//        ids1.add(userIds.get(0));
        ids2.add(userIds.get(0));
//        ids3.add(userIds.get(0));
//        ids4.add(userIds.get(0));
        startUserSelectAssignees.put(BOTH_CONFIRM_02.getKey(), ids2);
//        startUserSelectAssignees.put(BOTH_SIGN_01.getKey(), ids3);
        //依次签
//        startUserSelectAssignees.put(BOTH_CONFIRM_02_SORT.getKey(), ids2);
//        startUserSelectAssignees.put(BOTH_SIGN_01_SORT.getKey(), ids3);

        Map map = new HashMap();
        ContractDO contractDO = contractMapper.selectById(contractId);
//        map.put("isSequential", IfNumEnums.YES.getCode().equals(contractDO.getIsSequential()));
        if(ObjectUtil.isNull(contractDO)){
            throw exception(DATA_ERROR);
        }
        bpmProcessInstanceCreateReqDTO.setProcessDefinitionKey(activityConfigurationEnum.getDefinitionKey()).setBusinessKey(contractId).setStartUserSelectAssignees(startUserSelectAssignees).setVariables(map);
        enhanceFlowOrder(contractDO,bpmProcessInstanceCreateReqDTO);
        return bpmProcessInstanceApi.createProcessInstance(SecurityFrameworkUtils.getLoginUserId(), bpmProcessInstanceCreateReqDTO);
    }

    private void enhanceFlowOrder(ContractDO contractDO, BpmProcessInstanceCreateReqDTO bpmProcessInstanceCreateReqDTO) {
        List<Integer> result = new ArrayList<>();
        String ruleOrder = contractDO.getFlowSortRule(); // 假设这是从数据库中获取的拼接的字符串
        if (StringUtils.isNotEmpty(ruleOrder)) {
            List<Integer> ruleValues = Arrays.stream(ruleOrder.split("_"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            result.addAll(ruleValues);
            bpmProcessInstanceCreateReqDTO.getVariables().put("if_sort_01", result.get(0));
            bpmProcessInstanceCreateReqDTO.getVariables().put("if_sort_02", result.get(1));
        }
    }

    @Override
    public List<Long> buildTaskUserIds(String contractId) {

        ContractDO contractDO = contractMapper.selectById(contractId);
        if (StringUtils.isNotEmpty(contractDO.getBusinessesId())) {
            List<Long> result = new ArrayList<>();
            ReqIdsDTO reqIdsDTO = new ReqIdsDTO();
            List<String> ids = new ArrayList<>();
            ids.add(contractDO.getBusinessesId());
            String str = projectPurchasingApi.queryPurchasingByIds(reqIdsDTO.setIds(ids));

            cn.hutool.json.JSONObject jsonObj = new cn.hutool.json.JSONObject(str);
            JSONArray jsonArray = jsonObj.getJSONArray("data");
            // 将JSONArray转换为List
            List<ProjectPurchasingRespVO> list = JSON.parseArray(jsonArray.toString(), ProjectPurchasingRespVO.class);
            ProjectPurchasingRespVO projectPurchasingRespVO = list.get(0);
            Long purchaser = Long.valueOf(projectPurchasingRespVO.getPurchaser());
            Long supplier = Long.valueOf(projectPurchasingRespVO.getSupplier());
//            result.add(supplier);
            result.add(purchaser);
//            result.add(purchaser);
//            result.add(supplier);
            return result;
        }

        List<Long> result = new ArrayList<>();

        //确认阶段 发起方最后一步 发起签署，放在相对方后 assign0 放在第二节点
        Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
        result.add(loginUserId);

        List<SignatoryRelDO> signatoryRels = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractId);
        List<String> signIds = signatoryRels.stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toList());
        List<Relative> relatives = relativeMapper.selectBatchIds(signIds);

        List<Long> userIds = relatives.stream().map(Relative::getContactId).collect(Collectors.toList());
        result.addAll(userIds);

        // 从拼接的字符串中提取出对应的用户ID
        String signOrder = contractDO.getSignOrder(); // 假设这是从数据库中获取的拼接的字符串
        if (StringUtils.isNotEmpty(signOrder)) {
            List<Long> signUserIds = Arrays.stream(signOrder.split("_"))
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            result.addAll(signUserIds);
        }

//        List<UserCompanyInfoRespDTO> userCompanyInfoList = companyApi.getUserCompanyInfoList(result);
//
//        List<Long> signUserIds = userCompanyInfoList.stream().map(UserCompanyInfoRespDTO::getUserId).collect(Collectors.toList());


        return result;
    }

    @Override
    public String createContractSignProcessInstance(String contractId) {

        List<SignatoryRelDO> signatoryRels = signatoryRelMapper.selectList(SignatoryRelDO::getContractId, contractId);
        List<String> signIds = signatoryRels.stream().map(SignatoryRelDO::getSignatoryId).collect(Collectors.toList());
        List<Relative> relatives = relativeMapper.selectBatchIds(signIds);
        List<Long> userIds = relatives.stream().map(Relative::getContactId).collect(Collectors.toList());

        FlowExecutor executor = executorFactory.getExecutorByUserIds(userIds);
        return executor.execute(userIds, contractId);
    }

    @Resource
    private ContractOrderExtMapper contractOrderExtMapper;


}