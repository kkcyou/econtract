package com.yaoan.module.system.controller.admin.user.saas.vo;

import com.yaoan.framework.common.validation.Mobile;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

/**
 * @description:
 * @author: Pele
 * @date: 2025-7-23 20:11
 */
@Data
public class SaasUserSaveReqVO {
  @Schema(description = "用户账号", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
  @NotBlank(message = "用户账号为必填")
  @Pattern(regexp = "^[a-zA-Z0-9]{4,30}$", message = "用户账号由 数字、字母 组成")
  @Size(min = 4, max = 30, message = "用户账号长度为 4-30 个字符")
  private String username;

  @Schema(description = "用户密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "yudao")
  @NotBlank(message = "用户密码为必填")
  @Pattern(
          regexp = "(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9#@!~%^&*.$-_+]{6,35}$|^(?=.*[a-zA-Z])(?=.*[#@!~%^&*.$-_+])[a-zA-Z0-9#@!~%^&*.$-_+]{6,35}$|^(?=.*[0-9])(?=.*[#@!~%^&*.$-_+])[a-zA-Z0-9#@!~%^&*.$-_+]{6,35}",
          message = "密码必须为6-35位，且包含字母、数字、符号(#@!~%^&*.$-_+)中至少两种组合"
  )
  @Size(min = 6, max = 35, message = "密码必须为6至35位字母，数字，且包含字母、数字、符号(#@!~%^&*.$-_+)中至少两种组合")
  private String password;

  @Schema(description = "手机号码", example = "15601691300")
  @NotBlank(message = "手机号码为必填")
  @Mobile
  private String mobile;

  /**
   * 身份证
   */
//  @NotBlank(message = "身份证为必填")
  @Size(min = 18, max = 18, message = "身份证必须是18位")
  private String idCard;

}
