package com.yaoan.module.system.controller.admin.user.saas.vo;

import com.yaoan.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-23 20:11
 */
@Data
public class SaasUserTransferAdminUserReqVO {
  @Schema(description = "转交用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
  private Long userId;
  @Schema(description = "转交用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
  private List<String> contractIds;
}
