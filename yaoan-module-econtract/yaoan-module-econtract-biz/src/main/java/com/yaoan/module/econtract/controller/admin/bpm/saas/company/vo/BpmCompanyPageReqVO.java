package com.yaoan.module.econtract.controller.admin.bpm.saas.company.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.framework.common.util.date.DateUtils;
import com.yaoan.module.bpm.api.task.dto.ContractProcessInstanceRelationInfoRespDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-29 15:27
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BpmCompanyPageReqVO extends PageParam {

    private static final long serialVersionUID = 2253908793486061597L;
    /**
     * 提交人名称
     */
    private String submitterName;

    /**
     * 用户身份证
     */
    private String userIdCard;

    /**
     * 0=待办
     * 1=已办
     * 2=全部
     */
    private Integer flag;
    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邀请方式
     */
    private Integer inviteMethod;
    /**
     * 实名情况
     * {@link com.yaoan.module.econtract.enums.saas.RealNameEnums}
     */
    private Integer realName;
    /**
     * 流程实例
     */
    List<String> instanceIdList;
    /**
     * 搜索任务状态的字段
     */
    private Integer taskResult;

    /**
     * 查询创建时间范围的起始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startCreateTime;

    /**
     * 查询创建时间范围的结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endCreateTime;

    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    private Map<String, ContractProcessInstanceRelationInfoRespDTO> instanceRelationInfoRespDTOMap;
}
