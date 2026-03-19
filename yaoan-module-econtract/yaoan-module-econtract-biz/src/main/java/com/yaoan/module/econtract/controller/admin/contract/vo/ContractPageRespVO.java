package com.yaoan.module.econtract.controller.admin.contract.vo;

import com.yaoan.module.econtract.enums.common.IfNumEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Schema(description = "Contract PageResp VO")
@Data
public class ContractPageRespVO {

    //    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 合同编码
     */
    @Schema(description = "合同编码", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String code;

    /**
     * 合同名称
     */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String name;

    /**
     * 签署截止日期
     */
    @Schema(description = "签署截止日期", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Date expirationDate;

    /**
     * 合同分类id
     */
    @Schema(description = "合同分类id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer contractCategory;

    /**
     * 合同分类名称
     */
    @Schema(description = "合同分类名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractCategoryName;

    /**
     * 合同类型id
     */
    @Schema(description = "合同类型id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractType;

    /**
     * 合同类型名称
     */
    @Schema(description = "合同类型名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractTypeName;

    /**
     * 合同描述
     */
    @Schema(description = "合同描述", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractDescription;

    /**
     * 签署文件名称
     */
    @Schema(description = "签署文件名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileName;

    /**
     * 主文件地址
     */
    @Schema(description = "主文件地址", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileAddId;

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
     */
    @Schema(description = "签署状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer status;

    /**
     * 签署状态名称
     */
    @Schema(description = "签署状态名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String statusName;

    /**
     * 节点名称
     * */
    private String nodeName;

    /**
     * 任务id
     */
    @Schema(description = "任务id")
    private String taskId;

    /**
     * 1-待处理
     * 2-通过
     * 5-拒绝
     */
    @Schema(description = "处理状态")
    private Integer handleResult;

    /**
     * 流程实例id
     */
    @Schema(description = "流程实例id")
    private String processInstanceId;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    /**
     * 创建人姓名
     */
    @Schema(description = "创建人姓名")
    private String creatorName;

    /**
     * 创建者，目前使用 SysUser 的 id 编号
     */
    private String creator;

    /**
     * 发起方
     */
    @Schema(description = "发起方")
    private String initiator;

    /**
     * 签署方集合
     */
    @Schema(description = "签署方集合")
    private List<String> signatoryList;

    /**
     * 归档
     * 0 - 未归档
     * 1 - 已归档
     */
    @Schema(description = "归档\n" +
            "     * 0 - 未归档\n" +
            "     * 1 - 已归档")
    private Integer document;
    private String archivesId;

    /**
     * 上传合同
     * 默认
     * 0 - 模板起草
     * 1 - 上传文件起草
     * 2 - 合同补录 上传文件
     * 3 - 依据已成交的采购项目或订单起草
     */
    private Integer upload;
    private String uploadName;
    /**
     * 合同金额
     */
    @Schema(description = "合同金额")
    private Double amount;

    /**
     * 签署日期
     */
    @Schema(description = "签署日期")
    private Date signDate;

    /**
     * 合同有效期-开始时间
     */
    @Schema(description = "合同有效期-开始时间")
    private Date validity0;

    /**
     * 合同有效期-结束时间
     */
    @Schema(description = "合同有效期-结束时间")
    private Date validity1;

    /**
     * 签署类型
     * 0 - 电子签署
     * 1 - 纸质签署
     */
    @Schema(description = "签署类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer signType;

    /**
     * 甲方名称
     */
    private String partAName;
    /**
     * 乙方名称
     */
    private String partBName;

    /**
     * 变动类型
     */
    private Integer changeType;
    /**
     * 变动类型名称
     */
    private String changeTypeName;
    /**
     * 变动次数
     */
    private Integer changeCount;

    /**
     * 是否可以撤回
     */
    private Boolean enableRepeal;

    /**
     * 申请结果
     */
    private Integer result;

    /**
     * 申请结果名称
     */
    private String resultName;

    /**
     * 被分派到任务的人
     */
    private Long assigneeId;

    /**
     * 编辑类型
     */
    private Integer editType;

    /**
     * 平台
     */
    private String platform;
    private String platformName;

    /**
     * 订单编号
     */
    private String orderCode;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 合同来源类型
     * 0=电子合同
     * 1=上传合同
     * {@link com.yaoan.module.econtract.enums.contract.ContractSourceTypeEnums}
     */
    private Integer contractSourceType;

    /**
     * 招标项目方式名称：common:一般项目采购、batch:批量集中采购、union:联合采购、other:其他
     */
    private String biddingMethodCode;

    /**
     * 采购人是否签署
     */
    private Integer isSign;

    /**
     * 是否备案 0未备案 1备案中 2已备案
     */
    private Integer isFilings;

    private String remark9;
    /**
     * 是否需要备案标识
     */
    private Integer isNeedBak;


    /**
     * 采购包名称
     */
    @Schema(description = "采购包名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String packageName;
    /**
     * 采购包号
     */
    @Schema(description = "采购包号", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer packageNumber;

    /**
     * 乙方指派联系人-供应商指派联系人 ，乙方联系人对应 value12 供应商（乙方）代表
     */
    @Schema(description = "乙方指派联系人", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierLink;
    /**
     * 供应商负责人电话
     */
    @Schema(description = "供应商负责人电话", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String supplierLinkMobile;

    /**
     * 合同文件pdf 地址
     */
    private Long pdfFileId;

    /**
     * 是否线下
     * {@link IfNumEnums}
     */
    private Integer isOffline;

    /**
     * 是否是相对方
     * */
    private Integer isRelative;

    /**
     * 是否是操作的相对方标识，用于相对方确认/签署页面判断当前相对方是否可以发起撤回
     */
    private Integer isRJRelative;
    /**
     * 相对方流程的规则设置（0=会签，1=依次签）
     * */
    private String flowSortRule;

    /**
     * 智能审查文件id,用于判断是不是智能起草的合同
     */
    private String uploadFileAiId;
}

