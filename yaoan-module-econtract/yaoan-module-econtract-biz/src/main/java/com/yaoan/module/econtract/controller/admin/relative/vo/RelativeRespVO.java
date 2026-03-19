package com.yaoan.module.econtract.controller.admin.relative.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author doujiale
 */
@Data
@Schema(description = "返回相对方详细信息")
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RelativeRespVO extends BaseRelativeVO {

    @Schema(description = "相对方id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "相对方id不能为空")
    private String id;
   

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;


    @Schema(description = "创建人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String creator;
    @Schema(description = "创建人姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String creatorName;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime updateTime;

    @Schema(description = "更新人", requiredMode = Schema.RequiredMode.REQUIRED)
    private String updater;
    @Schema(description = "更新人姓名", requiredMode = Schema.RequiredMode.REQUIRED)
    private String updaterName;

    @Schema(description = "相对方性质  企业2  个人：1  单位3", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String entityTypeName;
    
    private String sourceTypeName;
    private String relativeTypeName;
    private String statusName;
    private String cardTypeName;
    private String legalCardTypeName;
    private String headCardTypeName;
}
