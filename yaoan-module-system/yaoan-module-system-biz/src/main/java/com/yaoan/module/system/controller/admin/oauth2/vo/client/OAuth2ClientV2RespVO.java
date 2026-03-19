package com.yaoan.module.system.controller.admin.oauth2.vo.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Schema(description = "管理后台 - OAuth2 客户端 Response VO")
@Data
@ToString(callSuper = true)
public class OAuth2ClientV2RespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private Date createTime;

    @Schema(description = "客户端编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "tudou")
    @NotNull(message = "客户端编号不能为空")
    private String clientId;

    @Schema(description = "客户端密钥", requiredMode = Schema.RequiredMode.REQUIRED, example = "fan")
    @NotNull(message = "客户端密钥不能为空")
    private String secret;

    @Schema(description = "应用名", requiredMode = Schema.RequiredMode.REQUIRED, example = "土豆")
    @NotNull(message = "应用名不能为空")
    private String name;

    @Schema(description = "应用图标", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn/xx.png")
    @NotNull(message = "应用图标不能为空")
    @URL(message = "应用图标的地址不正确")
    private String logo;

    @Schema(description = "应用描述", example = "我是一个应用")
    private String description;

    @Schema(description = "状态，参见 CommonStatusEnum 枚举", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态不能为空")
    private Integer status;


    @Schema(description = "可重定向的 URl 地址", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @NotNull(message = "可重定向的 URl 地址不能为空")
    private List<@NotEmpty(message = "重定向的 URI 不能为空")
    @URL(message = "重定向的 URl 格式不正确") String> redirectUris;
    /**
     * 过期时间
     */
    @Schema(description = "过期时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "https://www.iocoder.cn")
    @JsonFormat(timezone = "GMT+8" ,pattern = "yyyy-MM-dd")
    private Date expiresTime;


}
