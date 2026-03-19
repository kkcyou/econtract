package com.yaoan.module.econtract.dal.dataobject.term;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.yaoan.framework.tenant.core.db.DeptBaseDO;
import com.yaoan.module.econtract.enums.term.TermTypeEnums;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 合同
 * </p>
 *
 * @author doujiale
 * @since 2023-09-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("ecms_term")
@Document(indexName = "es_term")
public class Term extends DeptBaseDO implements Serializable {


    private static final long serialVersionUID = -5467268500909788707L;
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    @Field(type= FieldType.Keyword)
    private String id;

    /**
     * 条款编码
     */
    private String code;

    /**
     * 条款名称
     */
    @Field(type= FieldType.Text, analyzer = "ik_max_word")
    private String name;

    /**
     * 条款类型(head合同封页，com合同条款，end合同结尾)
     * {@link TermTypeEnums}
     */
    private String termType;

    /**
     * 条款分类-左边树形结构
     */
    private Integer categoryId;

    /**
     * 合同类型
     */
    private String contractType;

    /**
     * 状态 0未发布 1已发布
     */
    private String status;

    /**
     * 是否有参数（1有参数，0没有参数）
     */
    private String ifParam;

    /**
     * 条款内容
     */
    private byte[] termContent;

    /**
     * 条款内容str
     */
    @Field(type= FieldType.Text, analyzer = "ik_max_word")
    private String termContentTxt;

    /**
     * 缩略图base64
     */
    private String thumbnail;

    /**
     * 条款描述
     */
    private String remark;

    /**
     * 审批状态
     * {@link com.yaoan.module.bpm.enums.task.BpmProcessInstanceResultEnum}
     */
    private Integer result;

    /**
     * 流程实例id
     */
    private String processInstanceId;

    /**
     * 申请时间
     */
    private LocalDateTime ApplyTime;

    /**
     * 审批时间
     */
    private LocalDateTime ApproveTime;

    /**
     * 原因说明(审批流)
     */
    private String reason;
    /**
     * 条款说明
     */
    private String termComment;

    /**
     * 是否必选
     */
    private Boolean isRequired;

    /**
     * 畅写内容域的内容
     */
    private String cxContent;

    /**
     * 是否展示序号
     */
    private Boolean showSort;

    /**
     * 是否展示名称
     */
    private Boolean showName;

    /**
     * 可否编辑
     */
    private Boolean enableEdit;

    /**
     * 分类
     */
    private String termKind;

    /**
     * 分类
     */
    private String termKindName;

    /**
     * 所属行业
     */
    private String tradeType;
    private String tradeTypeName;
    /**
     * 条款依据
     */
    private String termAccord;
    /**
     * 条款库类别 0公共库，1单位库，2其他
     */
    private Integer termLibrary;
}
