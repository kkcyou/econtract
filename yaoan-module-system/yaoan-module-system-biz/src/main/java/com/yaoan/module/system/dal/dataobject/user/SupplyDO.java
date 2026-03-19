package com.yaoan.module.system.dal.dataobject.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

/**
 * 管理后台的用户 DO
 *
 * @author 芋道源码
 */

@TableName("system_supply")
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupplyDO extends BaseDO {
    private static final long serialVersionUID = -1255412233774911689L;

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 供应商名称中文
     */
    private String supplyCn;
    /**
     * 统一社会信用代码
     */
    private String orgCode;
    /**
     * 开户银行账号
     */
    private String bankAccount;
    /**
     * 开户银行 1
     */
    private String bankName;
    /**
     * 业务类型
     */
    private String businessType;
    /**
     * 企业类型
     */
    private String companyType;
    /**
     * 经济性质（字典）
     */
    private String ecokindcode;
    /**
     * 企业认定行业划分（下拉字典）
     */
    private String hyhf;
    /**
     * 地址
     */
    private String addr;
    /**
     * 传真号码
     */
    private String fax;
    /**
     * 企业电话
     */
    private String tel;
    /**
     * 供应商类型
     */
    private String type;
    /**
     * 法人 1
     */
    private String legalPerson;
    /**
     * 法人电话
     */
    private String legalTel;
    /**
     * 法人手机号码
     */
    private String legalMobile;
    /**
     * 法人身份证号
     */
    private String legalIdCard;
    /**
     * 联系人邮箱
     */
    private String personEmail;
    /**
     * 公司邮箱
     */
    private String supplierEmail;
    /**
     * 法人邮箱
     */
    private String legalEmail;
    /**
     * 邮政编码
     */
    private String supplierZip;
    /**
     * 法人地址
     */
    private String legalAddr;

    /**
     * 联系人地址 1
     */
    private String personAddr;
    /**
     * 联系人电话 1
     */
    private String personMobile;
    private String personTel;
    /**
     * 联系人姓名 1
     */
    private String personName;
    /**
     * 工商注册地址（地区字典+地址）
     */
    private String regAddr;
    /**
     * 企业规模（大、中、小、微、其他）(提示：填写''营业收入''和''从业人员''信息后，系统会自动做出企业规模认定。)0 大型企业\n4 微型企业\n1 中型企业\n2
     * 小型企业\n3 其他
     */
    private String unitScopeCode;

    /**
     * 供应商区划编码
     */
    private String reginCode;


}
