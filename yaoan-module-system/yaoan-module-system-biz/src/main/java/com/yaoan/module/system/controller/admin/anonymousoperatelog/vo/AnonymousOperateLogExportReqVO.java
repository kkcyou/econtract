package com.yaoan.module.system.controller.admin.anonymousoperatelog.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import com.yaoan.framework.common.pojo.PageParam;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 匿名用户操作日志记录 Excel 导出 Request VO，参数和 AnonymousOperateLogPageReqVO 是一致的")
@Data
public class AnonymousOperateLogExportReqVO {

    @Schema(description = "链路追踪编号", example = "13102")
    private String traceId;

    @Schema(description = "用户会话编号", example = "25203")
    private Long sessionId;

    @Schema(description = "用户类型")
    private String jwtData;

    @Schema(description = "模块标题")
    private String module;

    @Schema(description = "操作名", example = "张三")
    private String name;

    @Schema(description = "操作分类", example = "1")
    private Long type;

    @Schema(description = "操作内容")
    private String content;

    @Schema(description = "拓展字段")
    private String exts;

    @Schema(description = "请求方法名")
    private String requestMethod;

    @Schema(description = "请求地址", example = "https://www.iocoder.cn")
    private String requestUrl;

    @Schema(description = "用户 IP")
    private String userIp;

    @Schema(description = "浏览器 UA")
    private String userAgent;

    @Schema(description = "Java 方法名")
    private String javaMethod;

    @Schema(description = "Java 方法的参数")
    private String javaMethodArgs;

    @Schema(description = "操作时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] startTime;

    @Schema(description = "执行时长")
    private Integer duration;

    @Schema(description = "结果码")
    private Integer resultCode;

    @Schema(description = "结果提示")
    private String resultMsg;

    @Schema(description = "结果数据")
    private String resultData;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "签发方标识")
    private String iss;

    @Schema(description = "接收方标识")
    private String aud;

    @Schema(description = "业务id", example = "25629")
    private String bizId;

}
