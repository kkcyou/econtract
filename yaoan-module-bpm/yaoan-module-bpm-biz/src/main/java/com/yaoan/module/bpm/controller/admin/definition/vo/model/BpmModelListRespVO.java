package com.yaoan.module.bpm.controller.admin.definition.vo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@Data
@ToString(callSuper = true)
public class BpmModelListRespVO   {

    @Schema(description = "起草审批流程")
    private List<BpmModel> draftList;

    @Schema(description = "登记流程")
    private List<BpmModel> registerList;

    @Schema(description = "变更流程")
    private List<BpmModel> changeList;

    @Schema(description = "付款流程")
    private List<BpmModel> paymentList;

    @Schema(description = "收款流程")
    private List<BpmModel> collectionList;

    @Schema(description = "借阅流程")
    private List<BpmModel> borrowList;


}
