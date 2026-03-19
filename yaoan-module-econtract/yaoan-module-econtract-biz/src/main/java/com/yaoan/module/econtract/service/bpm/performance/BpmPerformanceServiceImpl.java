package com.yaoan.module.econtract.service.bpm.performance;

import cn.hutool.core.collection.CollectionUtil;
import com.yaoan.framework.common.pojo.PageResult;
import com.yaoan.framework.common.util.collection.CollectionUtils;
import com.yaoan.framework.security.core.util.SecurityFrameworkUtils;
import com.yaoan.module.bpm.api.task.BpmTaskApi;
import com.yaoan.module.bpm.api.task.dto.ContractProcessInstanceRelationInfoRespDTO;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.bpm.performance.vo.PerformancePageReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.performance.vo.PerformanceRespVO;
import com.yaoan.module.econtract.convert.bpm.performance.suspend.PerformanceConverter;
import com.yaoan.module.econtract.dal.dataobject.bpm.performance.suspend.BpmPerformance;
import com.yaoan.module.econtract.dal.mysql.bpm.performance.suspend.BpmPerformanceMapper;
import com.yaoan.module.system.api.user.AdminUserApi;
import com.yaoan.module.system.api.user.dto.AdminUserRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.Security;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author doujl
 * @since 2023-07-24
 */
@Service
public class BpmPerformanceServiceImpl implements BpmPerformanceService {

    @Resource
    private BpmPerformanceMapper bpmPerformanceMapper;
    @Resource
    private BpmTaskApi bpmTaskApi;
    @Resource
    private AdminUserApi userApi;

    public static final String PROCESS_KEY = "performance_suspend";


    @Override
    public PageResult<PerformanceRespVO> getApprovePage(PerformancePageReqVO reqVO) {

        //查询当前人待办和已处理的履约中止流程数据
        List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos = bpmTaskApi.getAllRelationProcessInstanceInfosByProcessDefinitionKeys(SecurityFrameworkUtils.getLoginUserId(), Collections.singleton(PROCESS_KEY));

        if (CollectionUtil.isEmpty(allRelationProcessInstanceInfos)) {
            return new PageResult<>();
        }
        reqVO.setProcessInstanceIds(CollectionUtils.convertList(allRelationProcessInstanceInfos, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId));

        PageResult<BpmPerformance> performancePage = bpmPerformanceMapper.selectPage(reqVO);

        if (CollectionUtil.isEmpty(performancePage.getList())) {
            return new PageResult<>();
        }

        PageResult<PerformanceRespVO> result = PerformanceConverter.INSTANCE.convertPage(performancePage);

        return enhance(result, allRelationProcessInstanceInfos);
    }

    private PageResult<PerformanceRespVO> enhance(PageResult<PerformanceRespVO> result, List<ContractProcessInstanceRelationInfoRespDTO> allRelationProcessInstanceInfos) {

        List<ContractProcessInstanceRelationInfoRespDTO> todoList = allRelationProcessInstanceInfos.stream().filter(item -> item.getProcessResult().equals(BpmProcessInstanceResultEnum.PROCESS.getResult())).collect(Collectors.toList());

        Map<String, String> processTaskMap = CollectionUtils.convertMap(todoList, ContractProcessInstanceRelationInfoRespDTO::getProcessInstanceId, ContractProcessInstanceRelationInfoRespDTO::getTaskId);

        List<Long> userIds = result.getList().stream().map(PerformanceRespVO::getUserId).collect(Collectors.toList());
        List<AdminUserRespDTO> userInfoList = userApi.getUserList(userIds);
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
}
