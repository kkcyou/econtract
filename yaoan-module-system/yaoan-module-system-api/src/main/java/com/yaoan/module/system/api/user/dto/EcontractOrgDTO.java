package com.yaoan.module.system.api.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 电子合同单位信息 Base VO，提供给添加、修改、详细的子 VO 使用
 * 如果子 VO 存在差异的字段，请不要添加到这里，影响 Swagger 文档生成
 */
@Data
public class EcontractOrgDTO {

    @Schema(description = "单位id", requiredMode = Schema.RequiredMode.REQUIRED, example = "12231")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "单位名称", example = "芋艿")
    private String name;

    @Schema(description = "单位地址")
    private String address;

    @Schema(description = "纳税人识别号", example = "29620")
    private String taxpayerId;

    @Schema(description = "联系传真")
    private String linkFax;

    @Schema(description = "联系人")
    private String linkMan;

    @Schema(description = "联系电话")
    private String linkPhone;

    @Schema(description = "开户银行账号", example = "20316")
    private String bankAccount;

    @Schema(description = "开户名称", example = "王五")
    private String bankAccountName;

    @Schema(description = "开户银行名称", example = "王五")
    private String bankName;

    @Schema(description = "区划编码")
    private String regionCode;

    @Schema(description = "邮政编码")
    private String postCode;

    @Schema(description = "法人名称")
    private String legal;

    @Schema(description = "法人电话")
    private String legalPhone;

    @Schema(description = "区划guid", example = "24506")
    private String regionGuid;

}
