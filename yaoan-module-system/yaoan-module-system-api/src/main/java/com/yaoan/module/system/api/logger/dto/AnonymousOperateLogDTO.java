package com.yaoan.module.system.api.logger.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 匿名用户操作日志记录创建 Request VO")
@Data
@ToString(callSuper = true)
public class AnonymousOperateLogDTO {

    @Schema(description = "链路追踪编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13102")
    //@NotNull(message = "链路追踪编号不能为空")
    private String traceId;

    @Schema(description = "用户会话编号", example = "25203")
    private Long sessionId;

    @Schema(description = "用户类型", requiredMode = Schema.RequiredMode.REQUIRED)
    //@NotNull(message = "用户类型不能为空")
    private String jwtData;

    @Schema(description = "模块标题", requiredMode = Schema.RequiredMode.REQUIRED)
    //@NotNull(message = "模块标题不能为空")
    private String module;

    @Schema(description = "操作名", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    //@NotNull(message = "操作名不能为空")
    private String name;

    @Schema(description = "操作分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    //@NotNull(message = "操作分类不能为空")
    private Long type;

    @Schema(description = "操作内容", requiredMode = Schema.RequiredMode.REQUIRED)
    //@NotNull(message = "操作内容不能为空")
    private String content;

    @Schema(description = "拓展字段", requiredMode = Schema.RequiredMode.REQUIRED)
    //@NotNull(message = "拓展字段不能为空")
    private String exts;

    @Schema(description = "请求方法名")
    private String requestMethod;

    @Schema(description = "请求地址", example = "https://www.iocoder.cn")
    private String requestUrl;

    @Schema(description = "用户 IP")
    private String userIp;

    @Schema(description = "浏览器 UA")
    private String userAgent;

    @Schema(description = "Java 方法名", requiredMode = Schema.RequiredMode.REQUIRED)
    //@NotNull(message = "Java 方法名不能为空")
    private String javaMethod;

    @Schema(description = "Java 方法的参数")
    private String javaMethodArgs;

    @Schema(description = "操作时间", requiredMode = Schema.RequiredMode.REQUIRED)
    //@NotNull(message = "操作时间不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime startTime;

    @Schema(description = "执行时长", requiredMode = Schema.RequiredMode.REQUIRED)
    //@NotNull(message = "执行时长不能为空")
    private Integer duration;

    @Schema(description = "结果码", requiredMode = Schema.RequiredMode.REQUIRED)
    //@NotNull(message = "结果码不能为空")
    private Integer resultCode;

    @Schema(description = "结果提示")
    private String resultMsg;

    @Schema(description = "结果数据")
    private String resultData;

    @Schema(description = "签发方标识")
    private String iss;

    @Schema(description = "接收方标识")
    private String aud;

    @Schema(description = "业务id", example = "25629")
    private String bizId;
}
