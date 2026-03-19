package com.yaoan.module.econtract.dal.dataobject.contracttemplate;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 合同范本
 *
 * @author Pele
 * @TableName ecms_contract_template
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "ecms_contract_template")
public class ContractTemplate extends DeptBaseDO implements Serializable {

    private static final long serialVersionUID = 1793413671361816835L;

    /**
     * 范本主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;
    /**
     * 范本名称
     */
    private String name;
    /**
     * 范本编号
     */
    private String code;
    /**
     * 范本简介（500字）
     */
    private String templateIntro;

    /**
     * 范本上传方式 0.wps 1.富文本
     */
    private Integer uploadType;

    /**
     * 范本内容
     */
    private byte[] content;

    /**
     * 范本分类
     */
    private Integer templateCategoryId;

//    /**
//     * 范本类型（经确认，类型也是分类）
//     */
//    private String templateType;

    /**
     * 范本来源(官方范本 或 标准范本)
     */
    private String templateSource;

    /**
     * 发布机构
     */
    private String publishOrgan;

    /**
     * 发布时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime publishTime;

    /**
     * 个人发布时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime publishTimeReciever;

    /**
     * 发布情况 0-未发布 1-已发布
     */
    private Integer publishStatus;

    /**
     * 范本字数
     */
    private Integer wordCount;

    /**
     * 远端文件对应ID（上传后的文件）
     */
    private Long remoteFileId;

    /**
     * ofd文件对应ID（审批后的文件）
     */
    private Long ofdFileId;

    /**
     * 上传源文件ID
     */
    private Long sourceFileId;

    /**
     * 审批状态（0=未开启审批，1=审批中，2=审批未通过，3=审批通过）
     */
    private Integer approveStatus;

    /**
     * 版本
     */
    private Double version;

    /**
     * 区划编码
     */
    private String regionCode;

    /**
     * 区划名称
     */
    private String regionName;

    /**
     * 审批时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approveTime;

    /**
     * 提交时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime submitTime;

    /**
     * 合同类类型
     */
    private String contractType;

    /**
     * 模板时效 (0=部分时间有效，1=长期有效)
     */
    @Schema(description = "模板时效标识")
    private Integer timeEffectModel;



    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime effectStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime effectEndTime;

    /**
     * 文件名称
     */
    private String fileName;

}