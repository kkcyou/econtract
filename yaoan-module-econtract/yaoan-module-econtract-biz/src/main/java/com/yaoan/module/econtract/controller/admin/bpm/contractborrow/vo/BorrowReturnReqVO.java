package com.yaoan.module.econtract.controller.admin.bpm.contractborrow.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.bpm.api.bpm.activity.dto.BpmProcessRespDTO;
import com.yaoan.module.econtract.controller.admin.contractarchives.vo.AttachmentVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class BorrowReturnReqVO {

    /**
     * id
     */
    private String id;

    /**
     * 实际归还时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date actualReturnTime;
    /**
     * 备注
     */
    private String reason;



}
