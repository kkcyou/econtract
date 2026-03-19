package com.yaoan.module.econtract.controller.admin.common.vo.flowable;

import com.yaoan.framework.common.pojo.PageParam;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2024/2/29 16:42
 */
@Data
public class FlowableParam {

    /**
     * {@link com.yaoan.module.econtract.enums.common.flow.FlowableStatusEnums}
     */
    private String flowableStatus;

    /**
     * {@link com.yaoan.module.econtract.enums.common.flow.FlowableStatusEnums}
     */
    private String flowableStatusName;

    /**
     * 历史任务操作结果
     */
    private Integer hisTaskResult;

    /**
     * 已办任务操作结果
     */
    private Integer doneTaskResult;

    /**
     * 被分派到任务的人
     */
    private Long assigneeId;

}
