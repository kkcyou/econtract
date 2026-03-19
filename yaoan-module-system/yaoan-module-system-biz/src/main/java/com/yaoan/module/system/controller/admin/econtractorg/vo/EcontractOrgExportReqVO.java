package com.yaoan.module.system.controller.admin.econtractorg.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yaoan.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 电子合同单位信息 Excel 导出 Request VO，参数和 EcontractOrgPageReqVO 是一致的")
@Data
public class EcontractOrgExportReqVO {

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

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "邮政编码")
    private String postCode;

    @Schema(description = "法人名称")
    private String legal;

    @Schema(description = "法人电话")
    private String legalPhone;

    @Schema(description = "区划guid", example = "24506")
    private String regionGuid;

}
