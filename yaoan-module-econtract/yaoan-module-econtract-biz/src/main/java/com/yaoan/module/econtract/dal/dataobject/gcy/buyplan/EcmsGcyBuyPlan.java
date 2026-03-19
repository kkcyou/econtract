package com.yaoan.module.econtract.dal.dataobject.gcy.buyplan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * <p>
 * 计划信息表
 * </p>
 *
 * @author doujiale
 * @since 2024-03-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_gcy_buy_plan")
public class EcmsGcyBuyPlan extends BaseDO {

    private static final long serialVersionUID = -5333285772369908952L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 采购计划的唯一识别码(全局唯一)
     */
    private String buyPlanGuid;

    /**
     * 预算项目唯一标识
     */
    private String budgetProjectGuid;

    /**
     * 指定交易平台的代码，采购计划中不指定交易平台则此值为NULL。(参见选项字典【Platform】定义)
     */
    private String platform;

    /**
     * 采购计划执行控制模式(参见选项字典【BuyPlanExecMode】定义)
     */
    private String buyPlanExecMode;

    /**
     * 计划所属年度
     */
    private Integer year;

    /**
     * 采购计划备案时间(精确到秒，格式：YYYY-MM-DD HH:mm:ss)
     */
    private Date archiveTime;

    /**
     * 采购计划创建/操作方式(参见选项字典【BuyPlanCreateType】定义)。
     */
    private String buyPlanCreateType;

    /**
     * 采购需求唯一识别码(有采购需求时为非空)
     */
    private String requirementGuid;

    /**
     * 计划备案/核准文号
     */
    private String buyPlanCode;

    /**
     * 计划名称
     */
    private String buyPlanName;

    /**
     * 财政区划代码(参见财政区划定义)
     */
    private String regionCode;

    /**
     * 财政区划名称(参见财政区划定义)
     */
    private String regionName;

    /**
     * 采购单位唯一识别码(参见采购单位定义)
     */
    private String orgGuid;

    /**
     * 采购单位预算编码(参见采购单位定义)
     */
    private String orgCode;

    /**
     * 采购单位名称(参见采购单位定义)
     */
    private String orgName;

    /**
     * 单位组织机构代码
     */
    private String license;

    /**
     * 代理机构的唯一识别码(参见代理机构定义)(项目采购为必填，电子卖场采购为NULL)
     */
    private String agentGuid;

    /**
     * 代理机构统一社会信用代码(参见代理机构定义)(项目采购为必填，电子卖场采购为NULL)
     */
    private String agentLicense;

    /**
     * 当前采购计划拟委托的代理机构名称(参见代理机构定义)(项目采购为必填，电子卖场采购为NULL)
     */
    private String agentName;

    /**
     * 代理机构类型(参见选项字典【AgentType】定义)
     */
    private String agentType;

    /**
     * 采购计划的采购预算总金额，金额单位为元。
     */
    private Double money;

    /**
     * 采购计划所属的采购项目总金额，金额单位为元。
     */
    private Double budgetProjectMoney;

    /**
     * 采购分类(参见选项字典【PurCatalogType】定义)
     */
    private String purCatalogType;

    /**
     * 采购组织形式(参见选项字典【Kind】定义)。
     */
    private String kind;

    /**
     * 采购实施形式(参见选项字典【Implement】定义)
     */
    private String implement;

    /**
     * 是否涉及进口产品采购(1:是,0:否)。
     */
    private Integer isImports;

    /**
     * 是否PPP项目(1:是,0:否)
     */
    private Integer isPPP;

    /**
     * 是否涉密项目(1:是,0:否)
     */
    private Integer isSecret;

    /**
     * 是否单位自行组织采购活动(1:是,0:否)
     */
    private Integer selfPurchase;

    /**
     * 是否允许与其他单位联合采购
     */
    private Integer unionPurchase;

    /**
     * 预计采购完成日期
     */
    private Date estimatedDate;

    /**
     * 是否高校和科研机构设备采购(1:是,0:否)
     */
    private Integer isScience;

    /**
     * 是否属于政务信息系统(1:是,0:否)
     */
    private Integer isGovInfomation;

    /**
     * 是否涉及政府购买服务(1:是,0:否)
     */
    private Integer govService;

    /**
     * 是否属于政府采购需求标准(2023年版)规范产品 (1:是,0:否)
     */
    private String infoInnovationProduct;

    /**
     * 是否一签多年(1:是,0:否)
     */
    private Integer yearMore;

    /**
     * 一签多年的最大期限（年）(参见选项字典【YearMoreYears】定义)
     */
    private String yearMoreYears;

    /**
     * 是否自由贸易试验区采购单位
     */
    private Integer isFtzOrg;

    /**
     * 项目联系人
     */
    private String enHandler;

    /**
     * 联系人电话
     */
    private String enHandlerPhone;

    /**
     * 项目负责人
     */
    private String enResponsor;

    /**
     * 负责人电话
     */
    private String enResponsorPhone;

    /**
     * 监管机构ID
     */
    private String regulatorsGuid;

    /**
     * 监管机构名称
     */
    private String regulatorsName;

    /**
     * 监管机构联系人
     */
    private String regulatorsLinkMan;

    /**
     * 监管机构联系电话
     */
    private String regulatorsTelphone;

    /**
     * 计划变更时间
     */
    private Date changeTime;

    /**
     * 计划变更原因
     */
    private String changeReason;

    /**
     * 采购需求概述
     */
    private String purchaseDemandSummary;

    /**
     * 符合条件但不面向中小企业采购的原因
     */
    private String noReserveReason;

    /**
     * 计划审批/备案意见
     */
    private String buyPlanComment;

    /**
     * 采购计划接收状态(参见选项字典【BuyPlanExecStatus】定义)。
     */
    private String buyPlanExecStatus;

    /**
     * 接收/退回此计划的交易平台代码(参见选项字典【Platform】定义)
     */
    private String execPlatform;

    /**
     * 执行平台采购计划接收/退回时间。(精确到秒，格式：YYYY-MM-DD HH:mm:ss)
     */
    private Date execTime;

    /**
     * 采购意向公示唯一标识
     */
    private String intentionNoticeGuid;

    /**
     * 采购意向发布时间
     */
    private Date intentionNoticePublishDate;

}
