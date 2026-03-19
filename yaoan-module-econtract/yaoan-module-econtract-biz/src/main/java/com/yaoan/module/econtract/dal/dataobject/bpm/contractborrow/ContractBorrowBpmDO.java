package com.yaoan.module.econtract.dal.dataobject.bpm.contractborrow;

/**
 * @description:
 * @author: Pele
 * @date: 2023/10/8 21:19
 */

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Pele
 * 借阅审批流程DO
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_bpm_contract_borrow")
public class ContractBorrowBpmDO extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = 2972671240236379970L;

    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 流程发起人
     */
    private Long userId;

    /**
     * 提交人名称
     */
    private String submitterName;

    /**
     * 借阅id
     */
    private String borrowId;

    /**
     * 借阅标题名称
     */
    private String borrowName;

    /**
     * 借阅时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date submitTime;

    /**
     * 附件文件id
     */
    private Long fileId;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 借阅事由
     * */
    private String approveIntroduction;

    /**
     * 审批状态
     * {@link com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum}
     */
    private Integer result;

    /**
     * 审批时间
     */
    private LocalDateTime ApproveTime;

    /**
     * 审批类型
     */
    private String approveType;

    /**
     * 流程实例的编号
     */
    private String processInstanceId;

    /**
     * 原因
     */
    private String reason;

    /**
     * 租户编号
     */
    private Long tenantId;

    /**
     * 档案id
     */
    private String archiveId;

    /**
     * 借阅类型 1=纸质文件 0=电子文件
     */
    private String borrowType;

    /**
     * 电子文件权限 : 1=带水印查看 2=无水印查看 3=带水印下载 4=无水印下载
     */
    private String borrowPermission;

    /**
     * 预计归还时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date returnTime;

    /**
     * 实际归还时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualReturnTime;
    /**
     * 纸质是否归还 未归还0 已归还1
     */
    private Integer isReturn;
}
