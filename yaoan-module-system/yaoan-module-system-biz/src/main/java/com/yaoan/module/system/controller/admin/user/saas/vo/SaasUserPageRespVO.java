package com.yaoan.module.system.controller.admin.user.saas.vo;

import com.yaoan.framework.common.enums.CommonStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description: saas用户页面查询返回数据VO
 * @author: zhc
 * @date: 2025-7-28 20:11
 */
@Data
public class SaasUserPageRespVO {
  @Schema(description = "序号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private Integer sort;
  @Schema(description = "用户 编号", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private Long id;
  @Schema(description = "用户账号（名称）", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "yudao")
  private String username;
  @Schema(description = "用户昵称", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "芋道")
  private String nickname;
  @Schema(description = "部门", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "嘻嘻嘻部门")
  private String deptName;
  @Schema(description = "身份证号", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "534646765656787654")
  private String idCard;
  @Schema(description = "手机号码", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "15601691300")
  private String mobile;
  @Schema(description = "账户状态，0：开启，1：关闭", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
  /**
   * 帐号状态
   *
   * 枚举 {@link CommonStatusEnum}
   */
  private Integer status;
  @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "2024-09-09")
  private String createTime;
}
