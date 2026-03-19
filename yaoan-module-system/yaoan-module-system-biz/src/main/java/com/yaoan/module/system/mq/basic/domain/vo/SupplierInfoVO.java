package com.yaoan.module.system.mq.basic.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 供应商信息返回实体
 **/
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class SupplierInfoVO implements Serializable {
    private static final long serialVersionUID = -2116898601101130682L;

    /**
     * 供应商id
     **/
    private String id;
    /**
     * 组织机构代码
     **/
    private String orgCode;
    /**
     * 区划编码
     */
    private String zoneCode;
    /**
     * 省
     **/
    private String province;
    /**
     * 市
     **/
    private String city;
    /**
     * 县
     **/
    private String county;
    /**
     * 供应商名称中文
     **/
    private String supplyCn;
    /**
     * 供应商名称英文
     **/
    private String supplyEn;
    /**
     * 供应商简称(中文)
     **/
    private String supplyCnnick;
    /**
     * 供应商简称(英文或拼音)
     **/
    private String supplyEnnick;
    /**
     * 是否原厂商
     **/
    private String supplyAddr;
    /**
     * 邮政编码
     **/
    private String supplyZip;
    /**
     * 企业邮箱
     **/
    private String supplyEmail;
    /**
     * 企业电话
     **/
    private String supplyTel;
    /**
     * 传真号码
     **/
    private String supplyFax;
    /**
     * 企业网址
     **/
    private String supplyWebsite;
    /**
     * 经济性质
     **/
    private String ECOKINDCODE;
    /**
     * 成立时间（日期选择）
     **/
    private String estdate;
    /**
     * 徽标LOGO（图片上传）
     **/
    private String unitlogo;
    /**
     * 注册资本（万元）
     **/
    private BigDecimal regMoney;
    /**
     * 国民经济行业名称（字典 树结构）
     **/
    private String industrycode;
    /**
     * 企业认定行业划分（下拉字典）
     **/
    private String hyhf;
    /**
     * 国别/地区，采用GB/T 2659-2000中的3位数字码
     **/
    private BigDecimal tradeMoney;
    /**
     * 开户银行
     **/
    private String bankName;
    /**
     * 开户银行账号
     **/
    private String bankAccount;
    /**
     * 是否监狱企业：
     **/
    private String isPrisonEnter;
    /**
     * 是否残疾人福利性单位
     **/
    private String isWefale;
    /**
     * 是否台资企业
     **/
    private String isTaiwan;
    /**
     * 企业类型
     **/
    private String companyType;
    /**
     * 从业人员（人）
     **/
    private String workpersonsum;
    /**
     * 企业规模（大、中、小、微、其他）(提示：填写'营业收入'和'从业人员'信息后，系统会自动做出企业规模认定。)
     **/
    private String unitscopecode;
    /**
     * 企业规模名称，前端显示使用
     **/
    private String unitscopename;
    /**
     * 工商注册地址（地区字典+地址）
     **/
    private String regAddr;
    /**
     * 主营范围
     **/
    private String maintradescope;
    /**
     * 兼营范围
     **/
    private String parttradescope;
    /**
     * 近三年营业情况
     **/
    private String year3trade;
    /**
     * 近三年内有无重大违法记录
     **/
    private String year3outlaw;
    /**
     * 法人代表姓名
     **/
    private String legalPerson;
    /**
     * 法人手机号码
     **/
    private String legalMobile;
    /**
     * 法人电话号码
     **/
    private String legalTel;
    /**
     * 法人身份证号
     **/
    private String legalIdcardno;
    /**
     * 法人邮政编码
     **/
    private String legalZip;
    /**
     * 法人传真号码
     **/
    private String legalFax;
    /**
     * 法人电子邮箱
     **/
    private String legalEmail;
    /**
     * 法人地址
     **/
    private String legalAddr;
    /**
     * 联系人姓名
     **/
    private String personName;
    /**
     * 联系人手机号码
     **/
    private String personMobile;
    /**
     * 联系人传真号码
     **/
    private String personFax;
    /**
     * 联系人邮政编码
     **/
    private String personZip;
    /**
     * 联系人电子邮箱
     **/
    private String personEmail;
    /**
     * 联系人地址
     **/
    private String personAddr;
    /**
     * 注册时间
     **/
    private String regdate;
    /**
     * 供应商类型
     **/
    private String supplyType;
    /**
     * 联系人电话号码
     **/
    private String personTel;
    /**
     * 明细备注
     **/
    private String detailInfo;
    /**
     * 有效Y 暂停S 黑名单B
     **/
    private String status;

    /**
     * 品目名称
     */
    private String goodsclassname;

    /**
     * 第三方系统毕加索的orgid
     */
    private String bijiasuoOrgId;

    /**
     * 第三方系统ID，山东此字段存的是东软的ct_out_id，映射到卖场的t_s_supplier表的interfacecode字段
     */
    private String outSystemId;


}
