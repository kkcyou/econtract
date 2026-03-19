package com.yaoan.module.econtract.dal.dataobject.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.framework.tenant.core.db.TenantBaseDO;
import com.yaoan.module.econtract.enums.ModelTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Pele
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_model")
public class SimpleModel extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = -8895116780509694280L;
    /**
     * 模板id
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 模板编码
     */
    private String code;

    /**
     * 模板的类型
     * 关联{@link ModelTypeEnum }
     */
    private String type;

    /**
     * 模板名称
     */
    private String name;

    /**
     * 模板分类ID
     */
    private Integer categoryId;

    /**
     * 合同类型ID
     */
    private String contractType;

    /**
     * 时效模式 (0=部分时间有效，1=长期有效)
     *
     */
    private Integer timeEffectModel;

    /**
     * 模板生效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime effectStartTime;

    /**
     * 模板生效结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime effectEndTime;

    /**
     * 模板描述
     */
    private String description;

    /**
     * 远端文件对应ID（文件上传生成的模板的文件ID）
     */
    private Long remoteFileId;

    /**
     * 富文本转换文件ID（条款生成的模板的文件ID）
     */
    private Long rtfPdfFileId;

    /**
     * 模板启用状态
     */
    private Integer status;

    /**
     * 审批状态
     */
    private Integer approveStatus;

    /**
     * 审批时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime approveTime;

    /**
     * 提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime submitTime;

    /**
     * 审批通过时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime approveResultTime;

    /**
     * 范本id
     */
    private String templateId;

    /**
     * 版本
     */
    private Double version;

    /**
     * 收藏 0未收藏 1已收藏
     */
    private Integer collect;

    /**
     * 生效时间内是否启用 0未启用 1启用
     */
    private Integer effectStatus;

    /**
     * 是否失效 0失效 1有效
     */
    private Integer effective;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 区划编号
     */
    private String regionCode;
    /**
     * 区划名称
     */
    private String regionName;


    /**
     * 是否有编辑权限
     * 0=无，1=有
     */
    private Integer editPermission;


    /**
     * 单位ID
     */
    private String orgId;

    /**
     * 公司ID 
     */
    private Integer companyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 是否政府采购 0否 1是
     */
    private String isGov;

    /**
     * 业务平台
     * */
    private String platform;

}
