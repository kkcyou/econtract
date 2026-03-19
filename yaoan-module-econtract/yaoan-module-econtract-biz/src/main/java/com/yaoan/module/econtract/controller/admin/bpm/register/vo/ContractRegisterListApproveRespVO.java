package com.yaoan.module.econtract.controller.admin.bpm.register.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.yaoan.module.econtract.controller.admin.common.vo.flowable.FlowableParam;
import com.yaoan.module.econtract.controller.admin.contract.vo.SignatoryRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: Pele
 * @date: 2024/1/26 14:07
 */
@Data
public class ContractRegisterListApproveRespVO extends FlowableParam {

    private static final long serialVersionUID = 7845096702742493564L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同编码
     */
    private String code;

    /**
     * 任务名称
     */
    private String name;


    /**
     * 合同类型
     */
    private String contractType;
    /**
     * 合同类型name
     */
    private String contractTypeName;


    /**
     * 主文件地址id
     */
    private Long fileAddId;

    /**
     * 签署状态
     * 0-待发送 - 新增
     * 1-被退回
     * 2-已发送
     * 3-待确认
     * 4-待签署
     * 5-已拒签
     * 6-签署完成
     * 7-逾期未签署
     * 8-合同终止签署中
     * 9-合同终止
     * 10-合同变更
     * 11-待送审
     * 12-审核中
     * 13审核未通过
     */
    private Integer status;

    /**
     * 对应的流程编号
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;

    /**
     * 流程任务id
     */
    private String taskId;

    //------------------------------------------合同补录---------------------------------------
    /**
     * 上传合同
     * 默认
     * 0 - 模板起草
     * 1 - 上传文件起草
     * 2 - 合同补录 上传文件
     * 3 - 依据已成交的采购项目或订单起草
     */
    private Integer upload;

    /**
     * 签署类型
     * 0 - 电子签署
     * 1 - 纸质签署
     */
    private Integer signType;


    /**
     * 签署日期
     */
    private Date signDate;


    /**
     * 合同金额
     */
    private Double amount;

    /**
     * 归档
     * 0 - 未归档
     * 1 - 已归档
     */
    private Integer document;

    /**
     * 是否是模板上传 保存模板id
     */
    private String templateId;

    /**
     * 合同文件pdf 地址
     */
    private Long pdfFileId;

    /**
     * 当前支付计划的sort排序标识
     */
    private Integer currentScheduleSort;
    /**
     * 提交时间
     */
    private LocalDateTime submitTime;

    /**
     * 签署方集合
     */
    @Schema(description = "签署方集合", requiredMode = Schema.RequiredMode.REQUIRED)
    private List<SignatoryRespVO> signatoryRespVOList;
}
