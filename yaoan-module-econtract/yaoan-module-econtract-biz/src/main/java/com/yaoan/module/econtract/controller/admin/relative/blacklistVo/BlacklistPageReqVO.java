package com.yaoan.module.econtract.controller.admin.relative.blacklistVo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Schema(description = "参数")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BlacklistPageReqVO extends PageParam {
   
    @Schema(description = "相对方名称", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @Schema(description = "相对方编码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String code;
    @Schema(description = "相对方等级", requiredMode = Schema.RequiredMode.REQUIRED)
    private String levelNo;
    @Schema(description = "相对方性质", requiredMode = Schema.RequiredMode.REQUIRED)
    private String entityType;
    @Schema(description = "相对方类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private String relativeType;
    @Schema(description = "相对方来源", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sourceType;
    @Schema(description = "黑名单状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private String status;
    @Schema(description = "开始创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date startDate;
    private Integer applyStatus;
    private Integer applyType;
    private String applyMsg;
    private Integer auditType;
    private String auditMsg;
    @Schema(description = "创建结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date endDate;

    @Schema(description = "搜索字符串，用于模糊匹配相对方名称，统一信用代码，联系人，手机号码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String searchText;

    @Schema(description = "创建人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String creator;

    @Schema(description = "账号状态", requiredMode = Schema.RequiredMode.REQUIRED)
    private String accountStatus;

}
