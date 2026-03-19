package com.yaoan.module.econtract.controller.admin.contractarchives.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 合同档案补充 Request VO")
@Data
public class ContractArchivesSupplementReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "26904")
    @NotNull(message = "id不能为空,需传入修改数据的id")
    private String id;

    @Schema(description = "档号")
    private String code;

    @Schema(description = "档案名称", example = "芋艿")
    private String name;

    @Schema(description = "状态 待审批0 已归档1 ", example = "1")
    private Integer status;

    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.REQUIRED, example = "23270")
    @NotEmpty(message = "合同id不能为空")
    private String contractId;

    @Schema(description = "全宗号")
    private String fondsNo;

    @Schema(description = "一级类别号")
    private String firstLevelNo;

    @Schema(description = "二级类别号")
    private String secondLevelNo;

    @Schema(description = "档案存储年限  10年 10 30年 30 60年60 永久 forever")
    private String archiveStorageYear;

    @Schema(description = "案卷号")
    private String volumeNo;

    @Schema(description = "档案载体 电子0 纸质1 都有2", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "档案载体 电子0 纸质1不能为空")
    private List<Integer> medium;

    @Schema(description = "是否开放 开放0 控制1", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "是否开放 开放0 控制1不能为空")
    private Integer openStatus;

    @Schema(description = "责任人")
    private String accountableUser;

    @Schema(description = "纸质档案地址")
    private String archiveAddress;

    @Schema(description = "档案份数", example = "21105")
    private String archiveCount;

    @Schema(description = "合同正文业务id", example = "13642")
    private List<Long> contractContentId;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "归档时间，传时间戳")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime archiveTime;

    @Schema(description = "流程实例的编号", example = "11350")
    private String processInstanceId;

    @Schema(description = "部门标识", example = "30019")
    private Long deptId;

    @Schema(description = "附件名称", example = "附件1")
    private List<String> attachmentNames;

    @Schema(description = "归档人")
    private String archiveUser;

    @Schema(description = "补充备注")
    private String supplementRemark;
    @Schema(description = "合同正文业务id", example = "13642")
    private String attachmentId;

    @Schema(description = "合同正文文件集合", example = "13642")
    private List<AttachmentVO> contractFiles;

    @Schema(description = "附件集合", example = "30")
    private List<AttachmentVO> attachmentIds;

    /**
     * 归档年度
     */
    @Schema(description = "归档年度")
    private String year;

    /**
     * 项目编码
     */
    private String proCode;
    private String proName;
}