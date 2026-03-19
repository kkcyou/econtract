package com.yaoan.module.econtract.dal.dataobject.contractarchives;

import cn.hutool.core.date.DateTime;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 合同档案 DO
 *
 * @author lls
 */
@TableName("ecms_contract_archives")
@KeySequence("ecms_contract_archives_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractArchivesDO extends TenantBaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 档号
     */
    private String code;
    /**
     * 档案名称
     */
    private String name;
    /**
     * 状态 待审批0 已归档1 草稿2
     */
    private Integer status;
    /**
     * 合同id
     */
    private String contractId;
    /**
     * 全宗号
     */
    private String fondsNo;
    /**
     * 一级类别号
     */
    private String firstLevelNo;
    /**
     * 二级类别号
     */
    private String secondLevelNo;
    /**
     * 档案存储年限  10年 10 30年 30 60年60 永久 forever
     */
    private String archiveStorageYear;
    /**
     * 案卷号
     */
    private String volumeNo;
    /**
     * 归档年度
     */
    private String year;
    /**
     * 档案载体 电子0 纸质1 多个使用逗号间隔
     */
    private String medium;
    /**
     * 是否开放 开放0 控制1
     */
    private Integer openStatus;
    /**
     * 责任人
     */
    private String accountableUser;
    /**
     * 纸质档案地址
     */
    private String archiveAddress;
    /**
     * 档案份数
     */
    private String archiveCount;
    /**
     * 合同正文业务id
     */
    @Builder.Default
    private String attachmentId = UUID.randomUUID().toString();
    /**
     * 备注
     */
    private String remark;
    /**
     * 归档时间
     */
    private LocalDateTime archiveTime;

    /**
     * 部门标识
     */
    private Long deptId;

    /**
     * 创建人姓名
     */
    private String creatorName;

    /**
     * 纸质档案是否借出 未借出0 已借出1，借出后新增借阅申请不可借阅纸质档案
     */
    private Integer isBorrow;

    /**
     * 项目编码
     */
    private String proCode;
    private String proName;

    /**
     * 记录退回类型 0归档退回 1补充退回
     */
    private Integer rejectType;
}