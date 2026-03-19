package com.yaoan.module.econtract.controller.admin.gpx.contractVO;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 批注出参
 */
@Data
public class CommentCreateRespVO {
    /**
     * 主键id
     */
    private String id;
    /**
     * 合同id
     */
    private String contractId;

    /**
     * 备注名
     */
    private String name;

    /**
     * 备注编码
     */
    private String code;

    /**
     * 备注内容
     */
    private String content;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建者，目前使用 SysUser 的 id 编号
     */
    private String creator;

    /**
     * 创建者名称
     */
    private String creatorName;
}
