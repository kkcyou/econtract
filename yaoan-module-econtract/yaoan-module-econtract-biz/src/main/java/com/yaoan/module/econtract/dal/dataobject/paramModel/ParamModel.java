package com.yaoan.module.econtract.dal.dataobject.paramModel;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author doujl
 * @since 2023-07-24
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_param_model")
public class ParamModel extends TenantBaseDO implements Serializable {
    private static final long serialVersionUID = -830858729134336089L;
    /**
     * 模板id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 参数id
     */
    private String paramId;

    /**
     * 参数名称
     */
    private String paramName;

    /**
     * 参数编号
     */
    private String paramCode;

    /**
     * 参数序号
     */
    private Integer paramNum;

    /**
     * 模板id
     */
    private String modelId;

    /**
     * 参数在模板上的位置（坐标）
     */
    private String location;

    /**
     * 标识id（文件上传和范本生成 模板的时候前端会传）
     */
    private String tagId;
}
