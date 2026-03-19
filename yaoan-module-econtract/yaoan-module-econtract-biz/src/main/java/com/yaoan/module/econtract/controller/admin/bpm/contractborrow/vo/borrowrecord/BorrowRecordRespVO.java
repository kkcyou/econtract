package com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo.borrowrecord;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class BorrowRecordRespVO {
    /**
     * 借阅id
     */
    private String id;

    /**
     * 档案id
     */
    @Schema(description = "档案id")
    private String archiveId;

//    @Schema(description = "档号")
//    private String code;

    @Schema(description = "档案名称", example = "芋艿")
    private String name;

//    @Schema(description = "全宗号")
//    private String fondsNo;
//
//    @Schema(description = "一级类别号")
//    private String firstLevelNo;
//
//    @Schema(description = "二级类别号")
//    private String secondLevelNo;
//
//    @Schema(description = "案卷号")
//    private String volumeNo;

    /**
     * 审批结果
     */
    @Schema(description = "审批结果")
    private Integer result;
    /**
     * 申请时间
     */
    @Schema(description = "申请时间")
    private LocalDateTime createTime;

    /**
     * 借阅时间
     */
    @Schema(description = "借阅时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date submitTime;

    /**
     * 预计归还时间
     */
    @Schema(description = "预计归还时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date returnTime;

    @Schema(description = "实际归还时间", example = "2023-08-01 01:00:00")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date actualReturnTime;

    /**
     * 剩余时间
     */
    @Schema(description = "剩余时间")
    private String remainTime;

    /**
     * 借阅状态
     */
    @Schema(description = "借阅状态")
    private Integer borrowStatus;

    @Schema(description = "借阅状态名称")
    private String borrowStatusName;

    /**
     * 借阅id
     * */
    private String borrowId;
    private String contractId;
    private String creator;
    /**
     * 借阅类型 1=纸质文件 0=电子文件
     */
    private String borrowType;

    /**
     * 纸质是否归还 未归还0 已归还1
     */
    private Integer isReturn;


    @Schema(description = "项目编码")
    private String proCode;
    @Schema(description = "项目名称")
    private String proName;

    @Schema(description = "档案载体 电子0 纸质1", requiredMode = Schema.RequiredMode.REQUIRED)
    private String medium;

    @Schema(description = "档案载体 电子0 纸质1", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("档案载体 电子0 纸质1")
    private String mediumName;
}
