package com.yaoan.module.econtract.controller.admin.contractarchives.vo;

import cn.hutool.core.date.DateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.common.pojo.PageParam;
import com.yaoan.module.econtract.enums.AmountTypeEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static com.yaoan.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;


@Schema(description = "管理后台 - 合同档案分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ContractArchivesPageReqVO extends PageParam {

    @Schema(description = "合同名称")
    private String contractName;

    @Schema(description = "合同编号")
    private String contractCode;

    @Schema(description = "最小合同金额")
    private String minAmount;

    @Schema(description = "最大合同金额")
    private String maxAmount;

    @Schema(description = "甲方")
    private String partyAName;

    @Schema(description = "乙方")
    private String partyBName;

    @Schema(description = "开始签订时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date beginContractSignTime;

    @Schema(description = "结束签订时间")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date endContractSignTime;

    @Schema(description = "开始合同生效日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date beginEffectiveDate;

    @Schema(description = "结束合同生效日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date endEffectiveDate;

    @Schema(description = "开始合同终止日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date beginTerminationDate;

    @Schema(description = "结束合同终止日期")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
    private Date endTerminationDate;

    @Schema(description = "档号")
    private String code;

    @Schema(description = "档案名称", example = "芋艿")
    private String name;

    @Schema(description = "状态 待审批0 已归档1 ", example = "1")
    private Integer status;

    @Schema(description = "合同id", example = "23270")
    private String contractId;

    @Schema(description = "全宗号")
    private String fondsNo;

    @Schema(description = "一级类别号")
    private String firstLevelNo;

    @Schema(description = "二级类别号")
    private String secondLevelNo;

    @Schema(description = "档案存储年限  10年 10 30年 30 60年60 永久 Y")
    private List<String> archiveStorageYear;

    @Schema(description = "年度")
    private String year;

    @Schema(description = "案卷号")
    private String volumeNo;

    @Schema(description = "档案载体 电子0 纸质1")
    private List<Integer> medium;

    @Schema(description = "是否开放 开放0 控制1", example = "1")
    private List<Integer> openStatus;

    @Schema(description = "责任人")
    private String accountableUser;

    @Schema(description = "纸质档案地址")
    private String archiveAddress;

    @Schema(description = "档案份数", example = "21105")
    private String archiveCount;

    @Schema(description = "合同正文业务id", example = "13642")
    private String attachmentId;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "开始归档时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date beginArchiveTime;

    @Schema(description = "结束归档时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date endArchiveTime;

    @Schema(description = "流程实例的编号", example = "11350")
    private String processInstanceId;

    @Schema(description = "部门标识", example = "30019")
    private Long deptId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "归档人姓名")
    private String archiveUserName;

    @Schema(description = "项目名称")
    private String projectName;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "订单名称")
    private String orderName;

    @Schema(description = "订单编号")
    private String orderCode;
    /**
     * 结算类型
     * {@link AmountTypeEnums}
     * PAY(0, "付款"),
     * RECEIPT(1, "收款"),
     * NO_SETTLE(2, "不结算"),
     * DIRECTION(3, "收支双向"),
     */
    private List<Integer> amountType;

    /**
     * 合同类型
     */
    private List<String> contractType;


    @Schema(description = "菜单标识：0：档案检索，1：借阅申请")
    private Integer flag;


    private String creator;

    @Schema(description = "项目编码")
    private String proCode;
    @Schema(description = "项目名称")
    private String proName;

}