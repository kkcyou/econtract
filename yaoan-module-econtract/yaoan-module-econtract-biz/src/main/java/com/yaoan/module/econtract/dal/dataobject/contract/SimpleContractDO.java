package com.yaoan.module.econtract.dal.dataobject.contract;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.module.econtract.enums.AmountTypeEnums;
import com.yaoan.module.econtract.enums.ContractUploadTypeEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description:
 * @author: Pele
 * @date: 2023/11/9 9:42
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_contract")
public class SimpleContractDO extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = -7877314774771489528L;
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
     * 履约完成时间
     * */
    private Date performanceCompleteDate;
    /**
     * 签署截止日期
     */
    private Date expirationDate;

    /**
     * 合同分类
     */
    private Integer contractCategory;

    /**
     * 合同类型
     */
    private String contractType;

    /**
     * 合同描述 备注
     */
    private String contractDescription;

    /**
     * 签署文件名称
     */
    private String fileName;

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

    //------------------------------------------合同补录---------------------------------------
    /**
     * 上传合同
     * 默认
     * 0 - 模板起草
     * 1 - 上传文件起草
     * 2 - 合同补录 上传文件
     * 3 - 依据已成交的采购项目或订单起草
     * {@link ContractUploadTypeEnums}
     */
    private Integer upload;

    /**
     * 签署类型
     * 0 - 电子签署
     * 1 - 纸质签署
     */
    private Integer signType;

    /**
     * 合同存放处
     */
    private String deposit;

    /**
     * 签署日期
     */
    private Date signDate;

    /**
     * 合同有效期-开始时间
     */
    private Date validity0;

    /**
     * 合同有效期-结束时间
     */
    private Date validity1;

    /**
     * 结算类型
     * {@link AmountTypeEnums}
     * PAY(0, "付款"),
     * RECEIPT(1, "收款"),
     * NO_SETTLE(2, "不结算"),
     * DIRECTION(3, "收支双向"),
     */
    private Integer amountType;

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

    //------------新增第五期需求-内蒙------------

    /**
     * 签约地点
     */
    private String location;

    /**
     * 合同有效期
     */
    private Long validity;

    /**
     * 涉密条款 1是 0否
     */
    private Integer secrecyClause;

    /**
     * 推送数据id
     */
    private String businessesId;

    /**
     * 推送数据类型
     */
    private Integer businessesType;

    /**
     * 当前支付计划的sort排序标识
     */
    private Integer currentScheduleSort;
    /**
     * 提交时间
     */
    private LocalDateTime submitTime;
    /**
     * 审批时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approveTime;

    /**
     * 变动原因/申请原因
     */
    private String changeReason;

    /**
     * 主合同id（变动合同独有）
     */
    private String mainContractId;

    /**
     * 变动类型（1=变更，2=补充，3=解除）
     * {@link com.yaoan.module.econtract.enums.change.ContractChangeTypeEnums}
     */
    private Integer changeType;
    /**
     * 甲方名称
     */
    private String partAName;
    /**
     * 乙方名称
     */
    private String partBName;

    /**
     * 签署顺序
     */
    private String signOrder;

    /**
     * 合同的履约风险天数
     * 按合同逾期计划中最早的一期的时间算
     */
    private Date riskDate;
}
