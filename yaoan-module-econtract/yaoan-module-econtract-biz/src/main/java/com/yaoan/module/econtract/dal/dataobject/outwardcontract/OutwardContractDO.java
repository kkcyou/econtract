package com.yaoan.module.econtract.dal.dataobject.outwardcontract;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.*;

/**
 * 对外合同 DO
 *
 * @author Pele
 */
@TableName("ecms_outward_contract")
@KeySequence("ecms_outward_contract_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutwardContractDO extends TenantBaseDO {

    private static final long serialVersionUID = -3469702794421679251L;
    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 合同编码
     */
    private String code;
    /**
     * 合同名称
     */
    private String name;
    /**
     * 解除之前的状态
     */
    private Integer oldStatus;
    /**
     * 合同状态
     */
    private Integer status;
    /**
     * 合同内容
     */
    private byte[] contractContent;
    /**
     * 合同分类
     */
    private Integer contractCategory;
    /**
     * 合同类型
     */
    private String contractType;
    /**
     * 合同描述
     */
    private String contractDescription;
    /**
     * 签署文件名称
     */
    private String fileName;
    /**
     * 文件地址id
     */
    private Long fileAddId;
    /**
     * 文件pdf地址id
     */
    private Long pdfFileId;

    private String userId;

    /**
     * 部门标识
     */
    private Long deptId;
    /**
     * 公司id
     */
    private Long companyId;

}
