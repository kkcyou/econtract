package com.yaoan.module.econtract.controller.admin.signet.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.module.econtract.dal.dataobject.businessfile.BusinessFileDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import net.sf.cglib.core.Local;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Schema(description = "管理后台 - 发票 Response VO")
@Data
@ExcelIgnoreUnannotated
public class SignetManageRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "15915")
    private String id;
    @Schema(description = "印章ID")
    private String sealId;
    @Schema(description = "印章名称")
    private String sealName;

    @Schema(description = "印章管理员id")
    private Long sealAdminId;
    @Schema(description = "印章管理员名称")
    private String sealAdminIdName;
    @Schema(description = "申请人")
    private String creator;
    @Schema(description = "申请人名字")
    private String creatorName;
    @Schema(description = "申请日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
    @Schema(description = "申请事由")
    private String reason;

    /**
     * 合同id
     */
    @Schema(description = "合同id")
    private String contractId;
    @Schema(description = "合同编号")
    private String contractCode;
    @Schema(description = "合同名称")
    private String contractName;
    @Schema(description = "合同类型")
    private String contractType;
    @Schema(description = "合同总金额")
    private BigDecimal amount;

    @Schema(description = "币种 默认人民币 rmb", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private String currencyType;

    @Schema(description = "流程实例的编号", example = "5550")
    private String processInstanceId;

    /**
     * 附件集合
     */
    @Schema(description = "附件集合", example = "30")
    private List<BusinessFileDO> attachmentList;

    /**
     * 印章图片id
     */
    @Schema(description = "印章图片", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long sealPictureId;

    /**
     * 印章图片地址
     */
    @Schema(description = "印章图片地址", requiredMode = Schema.RequiredMode.REQUIRED)
    private String sealPictureUrl;
    /**
     * 部门id
     */
    private Long deptId;

    /**
     * 部门名称
     */
    private String deptName;

    /**
     * 用印类型
     * 线上签署、线下签署
     * */
    private String signType;
    /**
     * 签订人
     * */
    private String signerName;

    /**
     * 申请时间
     * */
    private Date applyTime;

    /**
     * 用印时间
     * */
    private Date signTime;


    private String sealCode;

}