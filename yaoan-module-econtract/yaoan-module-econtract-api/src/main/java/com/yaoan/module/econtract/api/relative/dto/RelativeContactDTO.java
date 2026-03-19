package com.yaoan.module.econtract.api.relative.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2025/3/13 21:08
 */
@Data
public class RelativeContactDTO {

  private String id;
  private Long userId;

  @Schema(description = "相对方id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private String relativeId;

  @Schema(description = "联系人名称", requiredMode = Schema.RequiredMode.REQUIRED)
  private String name;

  @Schema(description = "联系人部门", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private String dept;

  @Schema(description = "联系人职务", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private String duty;

  @Schema(description = "联系人邮箱", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  private String email;

  @Schema(description = "联系人手机号", requiredMode = Schema.RequiredMode.REQUIRED)
  private String contactTel;

  @Schema(description = "联系人证件类型", requiredMode = Schema.RequiredMode.REQUIRED)
  private Integer cardType;

  @Schema(description = "联系人证件号", requiredMode = Schema.RequiredMode.REQUIRED)
  private String cardNo;
  @Schema(description = "部门id", requiredMode = Schema.RequiredMode.REQUIRED)
  private Long deptId;
  private Long companyId;
  private Long tenantId;
}
