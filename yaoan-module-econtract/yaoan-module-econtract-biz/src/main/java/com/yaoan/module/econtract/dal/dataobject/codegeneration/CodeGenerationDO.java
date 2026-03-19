package com.yaoan.module.econtract.dal.dataobject.codegeneration;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.mybatis.core.dataobject.BaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import com.yaoan.module.econtract.enums.codeGeneration.CodeGenBusinessTypeEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @description: 编号生成
 * @author: Pele
 * @date: 2024/9/5 10:31
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_code_generation")
public class CodeGenerationDO extends TenantBaseDO {
    private static final long serialVersionUID = -4231097539509840158L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 编码类型/业务类型
     * {@link CodeGenBusinessTypeEnums}
     */
    private String businessType;
    /**
     * 业务名称
     */
    private String businessName;

    /**
     * 开启状态 默认开
     * 1=开
     * 0=关
     */
    private Integer status;
    /**
     * 业务名称
     */
    private String childrenType;
    /**
     * 子类类型标签
     */
    private String childrenTypeTag;
    /**
     * 编码格式
     */
    private String generateRule;

    /**
     * 需要长度
     */
    private Integer length;

    /**
     * 业务前缀
     */
    private String prefix;

    /**
     * 备注
     */
    private String remark;
}
