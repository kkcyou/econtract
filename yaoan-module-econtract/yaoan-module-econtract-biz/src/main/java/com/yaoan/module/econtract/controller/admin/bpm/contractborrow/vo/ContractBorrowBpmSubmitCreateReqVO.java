package com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.controller.admin.contractarchives.vo.AttachmentVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2023/10/9 14:00
 */
@Data
public class ContractBorrowBpmSubmitCreateReqVO implements Serializable {
    private static final long serialVersionUID = -2062465685437565242L;
    /**
     * id
     */
    @Schema(description = "id")
    private String id;

    /**
     * 合同集合
     * */
    @NotBlank(message = "合同id不能为空")
    @Schema(description = "合同id")
    private List<BorrowBpmReqVO> borrowBpmReqVOList;

    /**
     * 借阅时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date submitTime;

    /**
     * 借阅事由
     * */
    @Schema(description = "借阅事由")
    private String approveIntroduction;

    /**
     * 附件文件id
     */
    @Schema(description = "附件文件id")
    private Long fileId;

    /**
     * 附件文件
     */
    @Schema(description = "附件文件")
    private List<AttachmentVO> files;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 是否提交审批 0=否 1=是
     */
    @Schema(description = "是否提交审批 0=否 1=是")
    private Integer submit;

    /**
     * 借阅标题名称
     */
    private String borrowName;

    /**
     * 档案id
     */
    private String archiveId;

    /**
     * 借阅类型 1=纸质文件 0=电子文件
     */
    private List<Integer> borrowType;

    /**
     * 电子文件权限 : 1=带水印查看 2=无水印查看 3=带水印下载 4=无水印下载
     */
    private List<Integer> borrowPermission;

    /**
     * 归还时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date returnTime;
}
