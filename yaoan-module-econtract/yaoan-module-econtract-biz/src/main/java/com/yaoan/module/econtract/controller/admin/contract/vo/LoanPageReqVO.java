package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
public class LoanPageReqVO extends PageParam {
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
     * 归档开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date archiveStartTime;

    /**
     * 归档结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date archiveEndTime;

    /**
     * 是否借阅 0-否 1-是
     */
    private Integer borrow;
}
