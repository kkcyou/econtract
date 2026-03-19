package com.yaoan.module.econtract.controller.admin.contract.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class LoanPageRespVO {
    /**
     * 合同id
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 合同编码
     */
    private String code;

    /**
     * 归档人
     */
    private String archiveUser;

    /**
     * 归档时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime archiveTime;

    /**
     * 借阅
     */
    private Integer borrow;

    /**
     * 合同借阅文件地址
     */
    private Long borrowFileId;
}
