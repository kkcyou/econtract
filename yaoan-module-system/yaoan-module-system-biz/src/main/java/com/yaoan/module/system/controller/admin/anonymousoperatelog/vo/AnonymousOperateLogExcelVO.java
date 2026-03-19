package com.yaoan.module.system.controller.admin.anonymousoperatelog.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 匿名用户操作日志记录 Excel VO
 *
 * @author doujiale
 */
@Data
public class AnonymousOperateLogExcelVO {

    @ExcelProperty("日志主键")
    private Long id;

    @ExcelProperty("链路追踪编号")
    private String traceId;

    @ExcelProperty("用户会话编号")
    private Long sessionId;

    @ExcelProperty("用户类型")
    private String jwtData;

    @ExcelProperty("模块标题")
    private String module;

    @ExcelProperty("操作名")
    private String name;

    @ExcelProperty("操作分类")
    private Long type;

    @ExcelProperty("操作内容")
    private String content;

    @ExcelProperty("拓展字段")
    private String exts;

    @ExcelProperty("请求方法名")
    private String requestMethod;

    @ExcelProperty("请求地址")
    private String requestUrl;

    @ExcelProperty("用户 IP")
    private String userIp;

    @ExcelProperty("浏览器 UA")
    private String userAgent;

    @ExcelProperty("Java 方法名")
    private String javaMethod;

    @ExcelProperty("Java 方法的参数")
    private String javaMethodArgs;

    @ExcelProperty("操作时间")
    private LocalDateTime startTime;

    @ExcelProperty("执行时长")
    private Integer duration;

    @ExcelProperty("结果码")
    private Integer resultCode;

    @ExcelProperty("结果提示")
    private String resultMsg;

    @ExcelProperty("结果数据")
    private String resultData;

    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @ExcelProperty("签发方标识")
    private String iss;

    @ExcelProperty("接收方标识")
    private String aud;

    @ExcelProperty("业务id")
    private String bizId;

}
