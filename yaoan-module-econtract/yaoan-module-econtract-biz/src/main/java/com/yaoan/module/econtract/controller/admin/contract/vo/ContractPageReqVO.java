package com.yaoan.module.econtract.controller.admin.contract.vo;

import cn.hutool.core.date.DateTime;
import com.yaoan.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Schema(description = "Contract PageRequest VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class ContractPageReqVO extends PageParam {

    private static final long serialVersionUID = 4062716961545479961L;

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
     *签署截止日期-开始时间
     */
    @Schema(description = "签署截止日期-开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DateTime expirationDate0;

    /**
     *签署截止日期-结束时间
     */
    @Schema(description = "签署截止日期-结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DateTime expirationDate1;

    /**
     *归档时间日期-开始时间
     */
    @Schema(description = "归档时间日期-开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DateTime documentDate0;

    /**
     *归档时间-结束时间
     */
    @Schema(description = "归档时间日期-结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DateTime documentDate1;

    /**
     * 签署日期-开始时间
     */
    @Schema(description = "签署日期-开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DateTime signDate0;

    /**
     * 签署日期-结束时间
     */
    @Schema(description = "签署日期-结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DateTime signDate1;

    /**
     * 发送时间-开始时间
     */
    @Schema(description = "发送时间-开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DateTime sendTime0;

    /**
     * 发送时间-结束时间
     */
    @Schema(description = "发送时间-结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DateTime sendTime1;

    /**
     * 确认时间-开始时间
     */
    @Schema(description = "确认时间-开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DateTime confirmTime0;

    /**
     * 确认时间-结束时间
     */
    @Schema(description = "确认时间-结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DateTime confirmTime1;

    /**
     * 合同生效日期-开始时间
     */
    @Schema(description = "合同生效日期-开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DateTime effectiveDate0;

    /**
     * 合同生效日期-结束时间
     */
    @Schema(description = "合同生效日期-结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DateTime effectiveDate1;

    /**
     * 合同终止日期-开始时间
     */
    @Schema(description = "合同终止日期-开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DateTime terminationDate0;

    /**
     * 合同终止日期-结束时间
     */
    @Schema(description = "合同终止日期-结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DateTime terminationDate1;

    /**
     * 补录时间-开始时间
     */
    @Schema(description = "补录时间-开始时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DateTime supplementDate0;

    /**
     * 补录时间-结束时间
     */
    @Schema(description = "补录时间-结束时间", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private DateTime supplementDate1;

    /**
     *合同分类
     */
    @Schema(description = "合同分类", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer contractCategory;

    /**
     *合同类型
     */
    @Schema(description = "合同类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractType;

    /**
     *合同类型集合
     */
    @Schema(description = "合同类型集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<String> contractTypes;

    /**
     *合同描述
     */
    @Schema(description = "合同描述", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String contractDescription;

    /**
     *签署文件名称
     */
    @Schema(description = "签署文件名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String fileName;

    /**
     *主文件地址Id
     */
    @Schema(description = "主文件地址Id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
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
     *签署状态list
     */
    @Schema(description = "签署状态list", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<Integer> statusList;

    /**
     * 需要查询的合同id集合
     * 当前登录=签署方
     */
    @Schema(description = "合同idList", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<String> contractIdList;

    /**
     *流程id集合
     */
    @Schema(description = "流程id集合", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<String> processInstanceIds;

    /**
     * 前端标识 identifier
     *   = 0   合同草拟-待发送
     *   = 1   合同草拟-已发送
     *   = 2   合同草拟-待确认
     *   = 3   合同确认
     *   = 4   合同签署-待签署
     *   = 5   合同签署-签署完成
     *   = 6   合同签署-逾期未签署
     *   = 7   合同签署-整合
     *   = 8   归档管理-电子签署
     *   = 9   草稿箱
     *   = 10  待审核
     */
    @Schema(description = "查询页面标识", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer identifier;

    /**
     * 归档：
     * 0 - 未归档
     * 1 - 已归档
     * 2 - 归档中
     */
    @Schema(description = "归档", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer document;

    /**
     * 归档list 全部归档数据
     */
    @Schema(description = "归档list", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private List<Integer> documentList;

    @Schema(description = "备案状态", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer isFilings;
    
    /**
     * 相对方公司名称、相对方联系人名称
     * 对应表里两个字段，-企业、单位 | 个人-
     */
    @Schema(description = "相对方公司名称、相对方联系人名称", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String relativeName;

    /**
     * 合同金额-范围开始
     */
    private Double amount0;

    /**
     * 合同金额-范围结束
     */
    private Double amount1;

    /**
     * 签署类型
     * 0 - 电子签署
     * 1 - 纸质签署
     */
    @Schema(description = "签署类型", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Integer signType;

    /**
     * 被分派到任务的人
     * */
    private Long assigneeId;
    /**
     * 相对方id
     */
    private String relativeId;

    /**
     * 甲方名称
     */
    @Schema(description = "甲方", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String partyAName;

    /**
     * 乙方名称
     */
    @Schema(description = "乙方", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String partyBName;

    /**
     * 合同来源 @
     */
    private Integer upload ;

    private List<Integer> uploadList ;


    /**
     * 采购人是否签署
     */
    private Integer isSign;
    /**
     * 是否政采
     */
    private Integer isGov;

    /**
     * 合同来源类型
     * 0=电子合同
     * 1=上传合同
     * {@link com.yaoan.module.econtract.enums.contract.ContractSourceTypeEnums}
     */
    private Integer contractSourceType;
    private List<Integer> contractSourceTypes;

    @Schema(description = "通用查询标识")
    private String queryKey;
    /**
     * 年份
     */
    private Integer year;

    private List<String> sumContractIds;
}