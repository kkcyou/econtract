package com.yaoan.module.econtract.controller.admin.bpm.contract.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2024/3/29 11:28
 */
@Data
public class ContractVersionListRespVO {

    /**
     * 主键
     */
    private String id;

    /**
     * 合同id
     */
    private String contractId;

    /**
     * 文件id
     */
    @TableField("file_id")
    private String fileId;

    /**
     * 文件名称
     */
    @TableField("file_name")
    private String fileName;

    /**
     * 版本号
     */
    @TableField("version_number")
    private Integer versionNumber;

    /**
     * 生成时间
     */
    private LocalDateTime createTime;
}
