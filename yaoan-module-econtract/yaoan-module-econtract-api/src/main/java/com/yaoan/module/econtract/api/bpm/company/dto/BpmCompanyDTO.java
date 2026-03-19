package com.yaoan.module.econtract.api.bpm.company.dto;

import com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-29 11:05
 */
@Data
public class BpmCompanyDTO {
  private String id;

  /** 公司id */
  private Long companyId;

  /** 负责人的用户编号 */
  private Long leaderUserId;

  /** 提交人名称 */
  private String submitterName;

  /** 公司名称 */
  private String companyName;

  /** 公司信用代码 */
  private String companyCreditNo;

  /** 用户身份证 */
  private String userIdCard;

  /** 审批类型 */
  private String approveType;

  /** 审批结果 {@link BpmProcessInstanceResultEnum} */
  private Integer result;

  /** 流程实例的编号 */
  private String processInstanceId;

  /** 原因 */
  private String reason;

  private LocalDateTime createTime;
}
