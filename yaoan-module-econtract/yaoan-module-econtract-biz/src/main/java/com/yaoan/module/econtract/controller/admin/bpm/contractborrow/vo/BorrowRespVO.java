package com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.bpm.api.bpm.activity.dto.BpmProcessRespDTO;
import com.yaoan.module.econtract.controller.admin.contractarchives.vo.AttachmentVO;
import com.yaoan.module.econtract.controller.admin.contractarchives.vo.ContractArchivesRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class BorrowRespVO {
    /**
     * 合同集合
     * */
    private List<BorrowBpmReqVO> borrowBpmReqVOList;

    /**
     * 申请人名称
     */
    private String creatorName;

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
    @Schema(description = "借阅事由")
    private String approveIntroduction;

    /**
     * 借阅时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date submitTime;

    /**
     * 流程信息响应VO集合
     */
    private List<BpmProcessRespDTO> bpmProcessRespVOList;

    /**
     * 档案名称
     */
    private String archiveName;
    /**
     * 档案档号
     */
    private String archiveCode;
    /**
     * 借阅类型
     */
    private String borrowType;
    private String borrowTypeStr;
    /**
     * 电子文件权限
     */
    private String borrowPermission;
    private String borrowPermissionStr;
    /**
     * 预计归还时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date returnTime;
    /**
     * 附件文件
     */
    @Schema(description = "附件文件")
    private List<AttachmentVO> files;

    /**
     * 借阅标题名称
     */
    private String borrowName;

    // 档案信息
    private ContractArchivesRespVO archiveInfo;
}
