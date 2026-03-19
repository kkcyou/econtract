package com.yaoan.module.system.controller.admin.systemuserrel.vo;

import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 系统对接用户关系分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class SystemuserRelPageReqVO extends PageParam {

    @Schema(description = "采购单位id", example = "32426")
    private String buyerOrgId;

    @Schema(description = "采购人id", example = "28279")
    private String buyerUserId;

    @Schema(description = "对应本系统用户id", example = "15726")
    private Long currentUserId;

    @Schema(description = "对应本系统租户id", example = "18964")
    private Long currentTenantId;

    @Schema(description = "对应本系统部门id", example = "28504")
    private Long deptId;

    @Schema(description = "创建人名称", example = "张三")
    private String creatorName;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
