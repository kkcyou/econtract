package com.yaoan.module.system.dal.dataobject.anonymousoperatelog;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;

/**
 * 匿名用户操作日志记录 DO
 *
 * @author doujiale
 */
@TableName("system_anonymous_operate_log")
@KeySequence("system_anonymous_operate_log_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnonymousOperateLogDO extends BaseDO {

    /**
     * 日志主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 链路追踪编号
     */
    private String traceId;
    /**
     * 用户会话编号
     */
    private Long sessionId;
    /**
     * 用户类型
     */
    private String jwtData;
    /**
     * 模块标题
     */
    private String module;
    /**
     * 操作名
     */
    private String name;
    /**
     * 操作分类
     */
    private Long type;
    /**
     * 操作内容
     */
    private String content;
    /**
     * 拓展字段
     */
    private String exts;
    /**
     * 请求方法名
     */
    private String requestMethod;
    /**
     * 请求地址
     */
    private String requestUrl;
    /**
     * 用户 IP
     */
    private String userIp;
    /**
     * 浏览器 UA
     */
    private String userAgent;
    /**
     * Java 方法名
     */
    private String javaMethod;
    /**
     * Java 方法的参数
     */
    private String javaMethodArgs;
    /**
     * 操作时间
     */
    private LocalDateTime startTime;
    /**
     * 执行时长
     */
    private Integer duration;
    /**
     * 结果码
     */
    private Integer resultCode;
    /**
     * 结果提示
     */
    private String resultMsg;
    /**
     * 结果数据
     */
    private String resultData;
    /**
     * 签发方标识
     */
    private String iss;
    /**
     * 接收方标识
     */
    private String aud;
    /**
     * 业务id
     */
    private String bizId;

}
