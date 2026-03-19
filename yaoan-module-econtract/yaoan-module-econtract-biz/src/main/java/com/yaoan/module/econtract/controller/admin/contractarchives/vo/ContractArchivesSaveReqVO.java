package com.yaoan.module.econtract.controller.admin.contractarchives.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 合同档案新增/修改 Request VO")
@Data
public class ContractArchivesSaveReqVO {
    //新增数据无需传入id
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "26904")
    private String id;

    @Schema(description = "档号")
    @Size(max = 25, message = "档号最多25字")
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

    /**
     * 归档年度
     */
    @Schema(description = "归档年度")
    private String year;

    @Schema(description = "档案载体 电子0 纸质1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "档案载体 电子0 纸质1不能为空")
    private List<Integer> medium;

    @Schema(description = "是否开放 开放0 控制1", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer openStatus;

    @Schema(description = "责任人")
    private String accountableUser;

    @Schema(description = "纸质档案地址")
    private String archiveAddress;

    @Schema(description = "档案份数", example = "21105")
    private String archiveCount;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "归档时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime archiveTime;

    @Schema(description = "部门标识", example = "30019")
    private Long deptId;

    @Schema(description = "合同正文文件集合", example = "13642")
    private List<AttachmentVO> contractFiles;

    @Schema(description = "附件集合", example = "30")
    private List<AttachmentVO> attachmentIds;
    @Schema(description = "项目编码", example = "30019")
    private String proCode;
    @Schema(description = "项目名称",example = "项目1")
    private String proName;

}