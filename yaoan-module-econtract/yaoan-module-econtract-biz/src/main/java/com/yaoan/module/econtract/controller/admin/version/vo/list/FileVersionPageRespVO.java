package com.yaoan.module.econtract.controller.admin.version.vo.list;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @description:
 * @author: Pele
 * @date: 2024/8/29 20:36
 */
@Data
public class FileVersionPageRespVO {
    /**
     * 主键
     */
    private Long id;

    /**
     * 业务id
     */
    private String businessId;

    /**
     * 业务名称
     */
    private String businessName;

    /**
     * 业务类型
     */
    private Integer businessType;

    /**
     * 拷贝文件id
     */
    private Long copyFileId;

    /**
     * 拷贝文件名称
     */
    private String copyFileName;

    /**
     * 拷贝文件URL
     */
    private String copyFileUrl;

    /**
     * 来源的拷贝文件id
     */
    private Long sourceCopyFileId;

    /**
     * 来源的拷贝文件名称
     */
    private String sourceCopyFileName;

    /**
     * 备注
     */
    private String remark;

    private String creator;

    private String creatorName;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
