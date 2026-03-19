package com.yaoan.module.system.controller.admin.oauth2.vo.client;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.yaoan.framework.common.pojo.PageParam;

import java.util.Date;

@Schema(description = "管理后台 - OAuth2 客户端分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OAuth2ClientPageReqVO extends PageParam {
    /**
     * 应用名，模糊匹配
     */
    @Schema(description = "应用名，模糊匹配", example = "土豆")
    private String name;
    /**
     * 状态
     */
    @Schema(description = "状态，参见 CommonStatusEnum 枚举", example = "1")
    private Integer status;
    /**
     * 开始创建时间
     */
    @Schema(description = "开始创建时间", example = "2024-03-07 00:00:00")
    private Date startCreateTime;
    /**
     * 创建结束时间
     */
    @Schema(description = "创建结束时间", example = "2024-03-07 00:00:00")
    private Date endCreateTime;




}
