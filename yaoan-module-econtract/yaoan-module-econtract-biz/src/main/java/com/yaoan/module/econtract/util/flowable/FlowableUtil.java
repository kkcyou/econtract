package com.yaoan.module.econtract.util.flowable;

import cn.hutool.core.util.ObjectUtil;
import com.yaoan.module.bpm.enums.model.FlowableModelEnums;
import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import com.yaoan.module.econtract.controller.admin.bpm.model.vo.ModelBpmSubmitCreateReqVO;
import com.yaoan.module.econtract.controller.admin.bpm.template.vo.TemplateBpmSubmitCreateReqVO;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.BatchSubmitReqVO;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.SubmitReqVO;
import com.yaoan.module.econtract.controller.admin.term.vo.TermAddVO;
import com.yaoan.module.econtract.enums.common.flow.FlowableStatusEnums;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.yaoan.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.yaoan.module.econtract.enums.ErrorCodeConstants.FLOWABLE_SUBMIT_REQUEST_ERROR;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/22 10:55
 */
@Slf4j
public class FlowableUtil {

    /**
     * 适配各种工作流 提交入参的转换
     */
    public static <T> List<T> enhanceBusinessSubmitReq(Class<T> clazz, List<SubmitReqVO> submitVOList) {
        List<T> result = new ArrayList<>();
        for (SubmitReqVO submitReqVO : submitVOList) {
            T rs = null;
            try {
                rs = clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
                log.error("工作流提交请求参数异常");
                throw exception(FLOWABLE_SUBMIT_REQUEST_ERROR);
            }
            //条款审批 请求参数的转换
            if (rs instanceof TermAddVO) {
                ((TermAddVO) rs).setId(submitReqVO.getBusinessId());
                result.add(rs);
            }
            //模板审批 请求参数的转换
            if (rs instanceof ModelBpmSubmitCreateReqVO) {
                // 进行 ModelBpmSubmitCreateReqVO 类型的处理
                ((ModelBpmSubmitCreateReqVO) rs).setModelId(submitReqVO.getBusinessId());
                result.add(rs);
            }
            //范本审批 请求参数的转换
            if (rs instanceof TemplateBpmSubmitCreateReqVO) {
                // 进行 ModelBpmSubmitCreateReqVO 类型的处理
                ((TemplateBpmSubmitCreateReqVO) rs).setId(submitReqVO.getBusinessId());
                result.add(rs);
            }
        }
        return result;
    }

    public static String getFlowableStatus(Long loginUserId, Long assigneeUserId) {
        if (loginUserId.equals(assigneeUserId)) {
            return FlowableStatusEnums.TO_DO.getCode();
        } else {
            return FlowableStatusEnums.DONE.getCode();
        }
    }

    public static List<String> enhanceBackTaskIdList(List<SubmitReqVO> backVOList) {
        return backVOList.stream().map(SubmitReqVO::getTaskId)
                .collect(Collectors.toList());
    }

    /**
     * 得到草稿的申请
     */
    public static List<SubmitReqVO> getSubmitReqVO(BatchSubmitReqVO vo) {
        return vo.getSubmitReqList().stream()
                .filter(item -> String.valueOf(BpmProcessInstanceResultEnum.DRAFT.getResult()).equals(item.getResult())
                        || String.valueOf(BpmProcessInstanceResultEnum.CANCEL.getResult()).equals(item.getResult()))
                .collect(Collectors.toList());
    }

    /**
     * 得到退回的申请
     */
    public static List<SubmitReqVO> getBackReqVO(BatchSubmitReqVO vo) {
        return vo.getSubmitReqList().stream()
                .filter(item -> String.valueOf(BpmProcessInstanceResultEnum.BACK.getResult()).equals(item.getResult()))
                .collect(Collectors.toList());
    }



    private <T> List<T> enhanceBusinessSubmitReq1(Class<T> clazz, List<SubmitReqVO> submitVOList) {
        List<T> result = new ArrayList<>();
        for (SubmitReqVO item : submitVOList) {
            if (clazz.equals(ModelBpmSubmitCreateReqVO.class)) {
                T r = (T) new ModelBpmSubmitCreateReqVO();
                // 进行其他类型转换或处理
            }
            if (clazz.equals(TermAddVO.class)) {
                List<ModelBpmSubmitCreateReqVO> list = new ArrayList<>();
                for (SubmitReqVO submitReqVO : submitVOList) {
                    T r = (T) new TermAddVO();
                    result.add(r);
                }
            }
            // 其他类型的处理逻辑


        }
        return result;
    }


    /**
     * Map< 序号,公司ID >
     */
    public static Map<Integer, Long> getIdsFromTextValue(String textValue) {
        Map<Integer, Long> result = new HashMap<Integer, Long>();
        // 将字符串根据分隔符 "_" 拆分成多个部分
        String[] parts = textValue.split("_");

        // 遍历拆分后的每个部分，将其转换为 Long 类型并添加到列表中
        Integer sort = 0;
        for (String part : parts) {
            sort = sort + 1;
            try {
                long value = Long.parseLong(part);
                result.put(sort, value);
            } catch (NumberFormatException e) {
                // 如果无法转换为 Long 类型，则忽略该部分
                log.error("无法转换: " + part);
            }
        }
        return result;
    }

}
