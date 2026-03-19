package com.yaoan.module.econtract.controller.admin.contractarchives.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - 合同档案 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ContractArchivesRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "26904")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "档号")
    @ExcelProperty("档号")
    private String code;

    @Schema(description = "档案名称", example = "芋艿")
    @ExcelProperty("档案名称")
    private String name;

    @Schema(description = "状态 待审批0 已归档1 ", example = "1")
    @ExcelProperty("状态 待审批0 已归档1 ")
    private Integer status;

    @Schema(description = "合同id", requiredMode = Schema.RequiredMode.REQUIRED, example = "23270")
    @ExcelProperty("合同id")
    private String contractId;
    private String contractName;

    @Schema(description = "全宗号")
    @ExcelProperty("全宗号")
    private String fondsNo;

    @Schema(description = "一级类别号")
    @ExcelProperty("一级类别号")
    private String firstLevelNo;

    @Schema(description = "二级类别号")
    @ExcelProperty("二级类别号")
    private String secondLevelNo;

    @Schema(description = "档案存储年限  10年 10 30年 30 60年60 永久 Y")
    private String archiveStorageYear;
    @Schema(description = "档案存储年限  10年 10 30年 30 60年60 永久 Y")
    @ExcelProperty("档案存储年限  10年 10 30年 30 60年60 永久 Y")
    private String archiveStorageYearName;

    @Schema(description = "归档年度")
    @ExcelProperty("归档年度")
    private String year;

    @Schema(description = "案卷号")
    @ExcelProperty("案卷号")
    private String volumeNo;

    @Schema(description = "档案载体 电子0 纸质1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String medium;

    @Schema(description = "档案载体 电子0 纸质1", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("档案载体 电子0 纸质1")
    private String mediumName;

    @Schema(description = "是否开放 开放0 控制1", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String openStatus;

    @Schema(description = "是否开放 开放0 控制1", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("是否开放 开放0 控制1")
    private String openStatusName;

    @Schema(description = "责任人")
    @ExcelProperty("责任人")
    private String accountableUser;

    @Schema(description = "纸质档案地址")
    @ExcelProperty("纸质档案地址")
    private String archiveAddress;

    @Schema(description = "档案份数", example = "21105")
    @ExcelProperty("档案份数")
    private String archiveCount;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "归档时间")
    @ExcelProperty("归档时间")
    private LocalDateTime archiveTime;

    @Schema(description = "流程实例的编号", example = "11350")
    @ExcelProperty("流程实例的编号")
    private String processInstanceId;

    @Schema(description = "部门标识", example = "30019")
    @ExcelProperty("部门标识")
    private Long deptId;
    private String deptName;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "归档人")
    private String creatorName;
    /**
     * 合同正文业务id
     */
    private String attachmentId;

    @Schema(description = "合同正文业务集合", example = "13642")
    private List<AttachmentVO> contractContentIds;

    @Schema(description = "附件集合", example = "30")
    private List<AttachmentVO> attachmentIds;

    @Schema(description = "借阅记录列表")
    private List<AttachmentBorrowVO> borrowRecords;

    @Schema(description = "是否本部门 0：否，1：是")
    private Integer isDept;

    @Schema(description = "项目编码")
    private String proCode;
    @Schema(description = "项目名称")
    private String proName;

    @Schema(description = "记录退回类型 0归档退回 1补充退回")
    private Integer rejectType;
}