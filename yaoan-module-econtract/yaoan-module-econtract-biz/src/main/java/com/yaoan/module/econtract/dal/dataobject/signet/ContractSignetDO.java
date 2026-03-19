package com.yaoan.module.econtract.dal.dataobject.signet;
import com.baomidou.mybatisplus.annotation.TableField;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.EqualsAndHashCode;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 印章表
 */

@Data
@TableName("ecms_contract_signet")
@EqualsAndHashCode(callSuper = true)
public class ContractSignetDO extends DeptBaseDO {
    private static final long serialVersionUID = 1728357424056L;

    /**
     * 主键id
     */
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 印章名称
     */
    private String sealName;

    /**
     * 印章编号
     */
    private String sealCode;

    /**
     * 印章管理员id
     */
    private Long sealAdminId;

    /**
     * 印章管理员名称
     */
    private String sealAdminName;

    /**
     * 印章类型
     */
    private Integer sealType;

    /**
     * 印章图片id
     */
    private Long sealPictureId;
    /**
     * 印章图片地址
     */
    private String sealPictureUrl;

    /**
     * 印章状态（0:已注销，1：已启用，2：已停用）
     */
    private Integer sealStatus;

    /**
     * 是否长期有效（0：否，1：是）
     */
    private Integer isPermanent;

    /**
     * 印章有效期开始时间
     */
    private Date sealStartDate;

    /**
     * 印章有效期结束时间
     */
    private Date sealEndDate;

    /**
     * 印章规格id
     */
    private Integer specsId;

    /**
     * PIN码
     */
    private String pinCode;

    /**
     * 用印审批流程key
     */
    private String sealProcess;

    /**
     * 公司id
     */
    private Long companyId;

}
